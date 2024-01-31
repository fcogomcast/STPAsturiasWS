package es.tributasenasturias.tarjetas.core.db;

import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import es.tributasenasturias.tarjetas.core.PateRecord;
import es.tributasenasturias.tarjetas.core.PeticionPagoTarjetaParameters;
import es.tributasenasturias.tarjetas.core.Util;
import es.tributasenasturias.tarjetas.core.sync.SincronizacionException;
import es.tributasenasturias.tarjetas.core.sync.SincronizadorEstadoPagoTarjeta;
import es.tributasenasturias.utils.ConversorParametrosLanzador;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.webservices.soap.LogMessageHandlerClient;
import es.tributasenasturias.webservices.types.InicioOperacionPagoRequest;

/**
 * Acceso a los PL de pasarela de pago en base de datos, y otras operaciones de alto nivel.
 * @author crubencvs
 *
 */

public class PLPasarelaRepository {

	private DBConn conn;
	private Preferencias preferencias;
	private Logger log;
	private String idSesion;
	
	public PLPasarelaRepository (Preferencias pref, Logger log, String idSesion){
		this.preferencias= pref;
		this.log= log;
		this.idSesion= idSesion;
		this.conn = new DBConn(pref.getEndPointLanzador(),pref.getEsquemaBaseDatos(), new LogMessageHandlerClient(idSesion));
	}
	/**
	 * Recibe el resultado de pago que nos envía Unicaja
	 * @param procAlmacenadoGrabarResultadoPagoUnicaja
	 * @param estado Estado del pago. En el momento de crear este servicio sólo recibimos resultado para "Pagado"
	 * @param resultado Código de resultado
	 * @param numeroPedido Número de pedido
	 * @param fechaPago Fecha de pago como AAAA-MM-DD HH24:MI:SS
	 * @param nrc Número de NRC
	 * @param numeroOperacion Número de operación bancaria
	 * @return true si se ha recibido correctamente, false si no.
	 * @throws PLPasarelaRepositoryException
	 */
	public boolean recibirResultadoPagoUnicaja(String procAlmacenadoGrabarResultadoPagoUnicaja,
											   String estado,
											   String resultado,
											   String numeroPedido,
											   String fechaPago,
											   String nrc,
											   String numeroOperacion,
											   String jsonRecibido
										   )throws PLPasarelaRepositoryException 
	{
		try {
			ConversorParametrosLanzador conversor= new ConversorParametrosLanzador();
			conversor.setProcedimientoAlmacenado(procAlmacenadoGrabarResultadoPagoUnicaja);
			conversor.setParametro("1", ConversorParametrosLanzador.TIPOS.Integer);
			conversor.setParametro("1", ConversorParametrosLanzador.TIPOS.Integer);
			conversor.setParametro("USU_WEB_SAC", ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro("33", ConversorParametrosLanzador.TIPOS.Integer);
			conversor.setParametro(estado, ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro(resultado, ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro(numeroPedido, ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro(fechaPago, ConversorParametrosLanzador.TIPOS.Date,"YYYY-MM-DD HH24:MI:SS");
			conversor.setParametro(nrc, ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro(numeroOperacion, ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro(jsonRecibido, ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro("P", ConversorParametrosLanzador.TIPOS.String);
			
			Map<String, List<Map<String,String>>> result= conn.ejecutar(conversor.codifica());
			if (result.isEmpty()) {
				return false;
			}
			if (result.containsKey("ERROR")) {
				//Hay estructura de error, algo ha terminado mal.
				
				throw new DAOException(result.get("ERROR").get(0).get("ERROR"));
			}
			Map<String,String> fila=result.get("CAOR_CADENA_ORDEN").get(0);
			String terminacion = fila.get("ORDEN_CAOR");
			
			return ("0".equals(terminacion)?true:false);
		} 
		catch (Exception e){
			throw new PLPasarelaRepositoryException("Error al recibir el resultado de pago de Unicaja:"+e.getMessage(),e);
		}
	}
	/**
	 * Recupera el registro del pago en la base, asegurándose de que está sincronizado
	 * con el estado del pago en la entidad remota.
	 * @param emisora
	 * @param justificante
	 * @param identificacion
	 * @param referencia
	 * @param aplicacion
	 * @param numeroUnico
	 * @return
	 * @throws SincronizacionException
	 * @throws PLPasarelaRepositoryException
	 */
	public PateRecord getRegistroSincronizadoBD (
										   String emisora,
										   String justificante,
										   String identificacion,
										   String referencia,
										   String aplicacion,
										   String numeroUnico) throws PLPasarelaRepositoryException, SincronizacionException{
		PateDAO pateDAO = new PateDAO(conn);
		PateRecord pateRecord;
		try {
			pateRecord= pateDAO.getPate(preferencias.getPAConsultaRegistroPate(),
										emisora, 
					                    justificante, 
					                    identificacion, 
					                   referencia, 
					                    aplicacion,  
					                    numeroUnico
					);
		} catch (DAOException dao){
			throw new PLPasarelaRepositoryException ("Error al recuperar el registro actual de pago en base de datos "+ dao.getMessage(), dao);
		}
		
		if (pateRecord!=null){
			//Sincronizamos datos entre base de datos y entidad de pago, 
			//si fuese necesario.
			SincronizadorEstadoPagoTarjeta sinc 
							= new SincronizadorEstadoPagoTarjeta(this.preferencias,
																 log,
																 this.idSesion);
			if (sinc.realizaSincronizacion(pateRecord)){
				try {
				pateRecord= pateDAO.getPate(preferencias.getPAConsultaRegistroPate(), 
											emisora, 
											justificante, 
											identificacion, 
											referencia, 
											aplicacion,  
											numeroUnico
				);
				} catch (DAOException dao){
					throw new PLPasarelaRepositoryException ("Error al recuperar el registro sincronizado de pago en base de datos "+ dao.getMessage(), dao);
				}
			}
		}
		return pateRecord;
	}
	
	
	/**
	 * Inicio de pago contra Unicaja. Intenta el inicio de pago y devuelve el registro
	 * de PATE con los datos del pago, si todo termina bien.
	 * @param peticion
	 * @throws PLPasarelaRepositoryException Si no puede terminar el proceso por cualquier motivo.
	 */
	public PateRecord iniciarPagoUnicaja(PeticionPagoTarjetaParameters peticion, String entidadPago) throws PLPasarelaRepositoryException{
		PateRecord pr;

		ConversorParametrosLanzador conversor;
		try {
			conversor= new ConversorParametrosLanzador();
		} catch (ParserConfigurationException pc){
			throw new PLPasarelaRepositoryException("Error técnico en el inicio de pago por tarjeta:"+ pc.getMessage(), pc);
		}
		
		conversor.setProcedimientoAlmacenado(preferencias.getPAIniciarPagoTarjeta());
		conversor.setParametro(peticion.getOrigen(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(peticion.getModalidad(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(peticion.getEmisora(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(peticion.getImporte(), ConversorParametrosLanzador.TIPOS.Integer);
		conversor.setParametro(peticion.getNumeroAutoliquidacion(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(peticion.getNifContribuyente(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(peticion.getFechaDevengo(), ConversorParametrosLanzador.TIPOS.Date,"DDMMYYYY");
		conversor.setParametro(peticion.getDatoEspecifico(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(peticion.getExpediente(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(peticion.getIdentificacion(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(peticion.getReferencia(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(peticion.getNifOperante(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(peticion.getModelo(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(entidadPago, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro("UNICAJA", ConversorParametrosLanzador.TIPOS.String);
		
		Map<String, List<Map<String,String>>> result;
		try {
			result= conn.ejecutar(conversor.codifica());
		} catch (DBConnException db){
			throw new PLPasarelaRepositoryException("Error en la llamada a base de datos para inicio de pago por tarjeta:" + db.getMessage(), db);
		}
		
		if (result.containsKey("ERROR")) {
			//Hay estructura de error, algo ha terminado mal.
			
			throw new PLPasarelaRepositoryException(result.get("ERROR").get(0).get("ERROR"));
		}
		
		if (!result.containsKey("CAOR_CADENA_ORDEN")){
			throw new PLPasarelaRepositoryException("No se ha recibido el estado de terminación del proceso desde la base de datos (ORDEN_CAOR)");
		}
		Map<String,String> fila=result.get("CAOR_CADENA_ORDEN").get(0);
		String terminacion = fila.get("ORDEN_CAOR");
		if (!"0".equals(terminacion)){
			throw new PLPasarelaRepositoryException("No se han recibido el estado de terminación esperado de la operación desde BD (Campo ORDEN_CAOR)");
		}
		if (result.containsKey("ESUN_ESTRUCTURA_UNIVERSAL")){
			
			fila=result.get("ESUN_ESTRUCTURA_UNIVERSAL").get(0);
			String cEstado                = fila.get("C1");
			String cAplicacion            = fila.get("C2");
			String cNumUnico              = fila.get("C3");
			String cNumOper               = fila.get("C4");
			String cNrc                   = fila.get("C5");
			String cFechaOperacion        = fila.get("C6");
			String cFechaPago             = fila.get("C7");
			String cPasarelaPago          = fila.get("C8");
			String cNif                   = fila.get("C9");
			String cFechaDevengo          = fila.get("C10");
			String cDatoEspecifico        = fila.get("C11");
			String cExpediente            = fila.get("C12");
			String cNifOperante           = fila.get("C13");
			String cImporte               = fila.get("C14");
			String cOperacionEpst         = fila.get("C15");
			String cMedioPago             = fila.get("C16");
			String cHashDatos             = fila.get("C17");
			String cResultado             = fila.get("C18");
			String cJustificante          = fila.get("C19");
			String cIdentificacion        = fila.get("C20");
			String cReferencia            = fila.get("C21");
			String cOrigen                = fila.get("C22");
			String cModalidad             = fila.get("C23");
			String cFechaAnulacion        = fila.get("C24");
			String cEmisora               = fila.get("C25");
			String cNombreContribuyente   = fila.get("C26");
			pr = new PateRecord(cNumOper,
										 cEmisora,
										 cImporte, 
										 cReferencia,
										 cIdentificacion,
										 cJustificante, 
										 cNif, 
										 Util.convertToDate(cFechaDevengo,"dd/MM/yyyy"), 
										 cDatoEspecifico,
										 cExpediente,
										 cModalidad,
										 Util.convertToDate(cFechaOperacion,"yyyyMMdd"),
										 cOrigen,cNrc,
										 cEstado,
										 Util.convertToDate(cFechaPago,"yyyyMMdd"),
										 "", //Xml Resultado. Sólo tiene interés para actualizaciones
										 cResultado,
										 cAplicacion,
										 cNumUnico,
										 cNifOperante,
										 Util.convertToDate(cFechaAnulacion,"dd/MM/yyyy"), 
										 cNombreContribuyente,
										 cPasarelaPago,
										 "", //Procesado no sube
										 cOperacionEpst,
										 cMedioPago,
										 cHashDatos);
		} else {
			throw new PLPasarelaRepositoryException ("No se han recibido datos del registro de pago desde la base de datos (ESUN_ESTRUCTURA_UNIVERSAL)");
		}
		return pr;
	}
	/**
	 * Actualización del registro de pago en base de datos
	 * Termina correctamente, o lanza una excepción
	 * @param pateRecord Nuevos datos del registro de pago. Los datos identificativos(emisora, justificante, identificación/referencia) no se actualizarán.
	 * @return
	 * @throws PLPasarelaRepositoryException
	 */
	public boolean actualizarRegistroPago(PateRecord pateRecord) throws PLPasarelaRepositoryException{
		try {
			ConversorParametrosLanzador conversor= new ConversorParametrosLanzador();
			conversor.setProcedimientoAlmacenado(preferencias.getPAActualizarPate());
			conversor.setParametro(pateRecord.getJustificante(), ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro(pateRecord.getIdentificacion(), ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro(pateRecord.getReferencia(), ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro(pateRecord.getEstado(), ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro(pateRecord.getResultado(), ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro(pateRecord.getNrc(), ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro("", ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro("", ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro("", ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro(pateRecord.getNumeroOperacion(), ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro(Util.convertFromDate(pateRecord.getFechaOperacion(),"yyyy-MM-dd HH:mm:ss"), ConversorParametrosLanzador.TIPOS.Date,"YYYY-MM-DD HH24:MI:SS");
			conversor.setParametro(pateRecord.getPasarelaPago(), ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro(pateRecord.getNifOperante(), ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro(Util.convertFromDate(pateRecord.getFechaDevengo(),"yyyy-MM-dd HH:mm:ss"), ConversorParametrosLanzador.TIPOS.Date, "YYYY-MM-DD HH24:MI:SS");
			conversor.setParametro(Util.convertFromDate(pateRecord.getFechaAnulacion(),"yyyy-MM-dd HH:mm:ss"), ConversorParametrosLanzador.TIPOS.Date, "YYYY-MM-DD HH24:MI:SS");
			conversor.setParametro(pateRecord.getOperacionEpst(), ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro(pateRecord.getToken(), ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro("<![CDATA["+pateRecord.getXmlServicio()+"]]>", ConversorParametrosLanzador.TIPOS.String);
		
			Map<String, List<Map<String,String>>> result= conn.ejecutar(conversor.codifica());
			if (result.isEmpty()) {
				throw new PLPasarelaRepositoryException("No se ha recibido resultado de la llamada a BD.");
			}
			if (result.containsKey("ERROR")) {
				//Hay estructura de error, algo ha terminado mal.
				
				throw new PLPasarelaRepositoryException(result.get("ERROR").get(0).get("ERROR"));
			}
			if (!result.containsKey("CAOR_CADENA_ORDEN")){
				throw new PLPasarelaRepositoryException("No se ha recibido el estado de terminación del proceso desde la base de datos (ORDEN_CAOR)");
			}
			Map<String,String> fila=result.get("CAOR_CADENA_ORDEN").get(0);
			String terminacion = fila.get("ORDEN_CAOR");
			if ("5004".equals(terminacion)){
				throw new PLPasarelaRepositoryException ("El trámite ya consta como pagado, no se actualiza.");
			} else if (!"0".equals(terminacion)){
				throw new PLPasarelaRepositoryException ("La actualización de pago no ha terminado correctamente, código:"+terminacion);
			}
			return true;
		} 
		catch (Exception e){
			if (!(e instanceof PLPasarelaRepositoryException)){
				throw new PLPasarelaRepositoryException("Error al actualizar registro de PATE:"+e.getMessage(),e);
			} else {
				throw (PLPasarelaRepositoryException) e;
			}
		}
	}
	
	public PateRecord inicioOperacionPago(InicioOperacionPagoRequest peticion) throws PLPasarelaRepositoryException{

		ConversorParametrosLanzador conversor;
		try {
			conversor= new ConversorParametrosLanzador();
		} catch (ParserConfigurationException pc){
			throw new PLPasarelaRepositoryException("Error técnico en el inicio de pago por tarjeta:"+ pc.getMessage(), pc);
		}
		
		conversor.setProcedimientoAlmacenado(preferencias.getPAInicioOperacionPago());
		conversor.setParametro(peticion.getOrigen(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(peticion.getModalidad(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(peticion.getEmisora(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(peticion.getImporte(), ConversorParametrosLanzador.TIPOS.Integer);
		conversor.setParametro(peticion.getNifContribuyente(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(peticion.getFechaDevengo(), ConversorParametrosLanzador.TIPOS.Date,"DDMMYYYY");
		conversor.setParametro(peticion.getDatoEspecifico(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(peticion.getAplicacion(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(peticion.getNumeroUnico(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(peticion.getNifOperante(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(peticion.getModelo(), ConversorParametrosLanzador.TIPOS.String);
		
		Map<String, List<Map<String,String>>> result;
		try {
			result= conn.ejecutar(conversor.codifica());
		} catch (DBConnException db){
			throw new PLPasarelaRepositoryException("Error en la llamada a base de datos para inicio de operación de pago:" + db.getMessage(), db);
		}
		
		if (result.containsKey("ERROR")) {
			//Hay estructura de error, algo ha terminado mal.
			
			throw new PLPasarelaRepositoryException(result.get("ERROR").get(0).get("ERROR"));
		}
		
		if (!result.containsKey("CAOR_CADENA_ORDEN")){
			throw new PLPasarelaRepositoryException("No se ha recibido el estado de terminación del proceso desde la base de datos (ORDEN_CAOR)");
		}
		Map<String,String> fila=result.get("CAOR_CADENA_ORDEN").get(0);
		String terminacion = fila.get("ORDEN_CAOR");
		if (!"0".equals(terminacion)){
			throw new PLPasarelaRepositoryException("No se han recibido el estado de terminación esperado de la operación desde BD (Campo ORDEN_CAOR)");
		}
		//Devuelvo el registro de base de datos actualizado.
		//FIXME: si se devolviesen los datos del registro desde la base de datos, podríamos evitar el segundo viaje.
		PateDAO pateDAO = new PateDAO(conn);
		try {
			return  pateDAO.getPate(preferencias.getPAConsultaRegistroPate(),
										peticion.getEmisora(), 
					                    "", 
					                    "", 
					                    "", 
					                    peticion.getAplicacion(),  
					                    peticion.getNumeroUnico()
					);
		} catch (DAOException dao){
			throw new PLPasarelaRepositoryException ("Error al recuperar el registro actual de pago en base de datos "+ dao.getMessage(), dao);
		}
	}
	
}
