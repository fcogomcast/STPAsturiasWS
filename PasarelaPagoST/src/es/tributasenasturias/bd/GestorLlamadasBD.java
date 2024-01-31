package es.tributasenasturias.bd;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import es.tributasenasturias.bd.Datos.InfoLlamada;
import es.tributasenasturias.bd.Datos.ResultadoConsultaPagoTarjetaBD;
import es.tributasenasturias.dao.DatosProceso;
import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.dao.TipoLlamada;
import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.pasarelas.comunicacion.TraductorPasarelaEntidad;
import es.tributasenasturias.utils.EstadosPago;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.utils.Varios;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.CallContextManager;
import es.tributasenasturias.webservices.context.IContextReader;
import es.tributasenasturias.webservices.types.InicioOperacionPagoRequest;
import es.tributasenasturias.webservices.types.InicioPagoTarjetaRequest;

/**
 * Permite la gestión de llamadas a base de datos.
 * @author crubencvs
 *
 */
public final class GestorLlamadasBD implements IContextReader{



	private CallContext callContext;
	private Preferencias pref;
	private String idSesion;
	
	public GestorLlamadasBD(CallContext callContext)
	{
		this.callContext=callContext;
		pref = (Preferencias) callContext.get(CallContextConstants.PREFERENCIAS);
		idSesion = (String) callContext.get(CallContextConstants.ID_SESION);
	}
	
	//CRUBENCVS 31/03/2023
	//47535
	//Constructor al que no hace falta pasarle el contexto de invocación,
	//sino sólo los elementos constituyentes
	public GestorLlamadasBD(Preferencias pref, String idSesion, Logger log){
		CallContext cc = CallContextManager.newCallContext();
		cc.setItem(CallContextConstants.LOG_APLICACION, log);
		cc.setItem(CallContextConstants.PREFERENCIAS, pref);
		cc.setItem(CallContextConstants.ID_SESION, idSesion);
		this.callContext= cc;
		this.pref= pref;
		this.idSesion= idSesion;
	}

	/**
	 * Inicia la petición de pago.
	 * @param datosProceso Datos de proceso
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final ResultadoLlamadaBD iniciarPeticionBD(String idPasarela,DatosProceso datosProceso) throws PasarelaPagoException
	{
		Datos dat = new Datos(this.idSesion);
		try
		{
			TraductorPasarelaEntidad traductor= new TraductorPasarelaEntidad(pref.getFicheroPasarelas());
			String entidad=traductor.getEntidadFromPasarela(idPasarela);
			String pasarela;
			Map<String,String> resultadoPeticion=null;
			String fechaFormateada="";
			DatosEntradaServicio peticionServicio = datosProceso.getPeticionServicio();
			if (!"".equals(peticionServicio.getFechaCaducidadTarjeta()))
			{
				fechaFormateada = Varios.getDateFormatted(peticionServicio.getFechaCaducidadTarjeta(),"MMyy", "yyMM");
			}
			switch (datosProceso.getTipoLlamada())
			{
			case SERVICIO_WEB_AUTOLIQUIDACION:
				resultadoPeticion= dat.peticionPate(peticionServicio.getOrigen(),peticionServicio.getModalidad(), peticionServicio.getEmisora(), peticionServicio.getNifContribuyente(), peticionServicio.getNombreContribuyente(),
						peticionServicio.getFechaDevengo(),
						peticionServicio.getJustificante(),peticionServicio.getDatoEspecifico(), peticionServicio.getExpediente(), 
						peticionServicio.getImporte(), "", "",peticionServicio.getAplicacion(), 
						peticionServicio.getNumeroUnico(), peticionServicio.getNifOperante(),
						peticionServicio.getTarjeta(), fechaFormateada, peticionServicio.getCcc(),peticionServicio.getModelo(), entidad);
				break;
			case PORTAL_AUTOLIQUIDACION:
			case S1_AUTOLIQUIDACION:
				resultadoPeticion= dat.peticionPate(peticionServicio.getOrigen(),peticionServicio.getModalidad(), peticionServicio.getEmisora(), peticionServicio.getNifContribuyente(), peticionServicio.getNombreContribuyente(),
						peticionServicio.getFechaDevengo(),
						peticionServicio.getJustificante(),peticionServicio.getDatoEspecifico(), peticionServicio.getExpediente(), 
						peticionServicio.getImporte(), "", "","", 
						"", peticionServicio.getNifOperante(),
						peticionServicio.getTarjeta(), fechaFormateada, peticionServicio.getCcc(),peticionServicio.getModelo(),entidad);
				break;
			case PORTAL_LIQUIDACION:
			case S1_LIQUIDACION:
				resultadoPeticion= dat.peticionPate(peticionServicio.getOrigen(),peticionServicio.getModalidad(), peticionServicio.getEmisora(), peticionServicio.getNifContribuyente(), peticionServicio.getNombreContribuyente(),
						peticionServicio.getFechaDevengo(),
						"",peticionServicio.getDatoEspecifico(), peticionServicio.getExpediente(), 
						peticionServicio.getImporte(), peticionServicio.getIdentificacion(), peticionServicio.getReferencia(),"", 
						"", peticionServicio.getNifOperante(),
						peticionServicio.getTarjeta(), fechaFormateada, peticionServicio.getCcc(),peticionServicio.getModelo(),entidad);
				break;
			}
			
			ResultadoLlamadaBD resBD= new ResultadoLlamadaBD();
			String error = dat.getErrorLlamada();
			if (error!=null && !"".equals(error))
			{
				resBD.setError(true);
				resBD.setCodError(Mensajes.getErrorPeticionBD());
			}
			else
			{
				checkErrorLlamadaBD(dat,resBD,Mensajes.getErrorPeticionBD());
			}
			if (!resBD.isError())
			{
				resBD.getDatosPagoBD().setEstado(Varios.normalizeNull(resultadoPeticion.get(Datos.C_ESTADO_PETICION_PATE)));
				resBD.getDatosPagoBD().setJustificante(Varios.normalizeNull(resultadoPeticion.get(Datos.C_JUSTIFICANTE_PETICION_PATE)));
				pasarela= traductor.getPasarelaFromEntidad(Varios.normalizeNull(resultadoPeticion.get(Datos.C_PASARELA_PAGO_BD)));
				//resBD.getDatosPagoBD().setPasarelaPago(Varios.normalizeNull(resultadoPeticion.get(Datos.C_PASARELA_PAGO_BD)));
				resBD.getDatosPagoBD().setPasarelaPago(pasarela);
			}
			return resBD;
		}
		catch (RemoteException e)
		{
			throw new PasarelaPagoException ("Error en la inicialización de petición en base de datos:"+ e.getMessage(),e);
		}
		catch (ParseException e)
		{
			throw new PasarelaPagoException ("Error en la inicialización de petición en base de datos por problemas con la fecha de caducidad de la tarjeta:"+ e.getMessage(),e);
		}
	}
	/**
	 * Realiza una consulta a la base de datos.
	 * @param datosProceso Datos del proceso (datos de la petición a base de datos y quizá otros).
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final ResultadoLlamadaBD consultaBD(TipoLlamada tipoLlamadaServicio, String emisora, String justificante, String identificacion, String referencia) throws PasarelaPagoException
	{
		ResultadoLlamadaBD resBD= new ResultadoLlamadaBD();
		Datos dat= new Datos(this.idSesion);
		Map<String, String> resultadoConsulta=null;
		try
		{
			switch (tipoLlamadaServicio)
			{
			case SERVICIO_WEB_AUTOLIQUIDACION:
			case PORTAL_AUTOLIQUIDACION:
			case S1_AUTOLIQUIDACION:
				resultadoConsulta = dat.consultaPate(emisora, justificante);
				break;
			case PORTAL_LIQUIDACION:
			case S1_LIQUIDACION:
				resultadoConsulta = dat.consultaPate(emisora, identificacion,referencia);
				break;
			}
			String error = dat.getErrorLlamada();
			if (error!=null && !"".equals(error))
			{
				resBD.setError(true);
				resBD.setCodError(Mensajes.getErrorBD());
			}
			else
			{
				//Comprobamos el resultado de la consulta.
				checkErrorLlamadaBD(dat, resBD, Mensajes.getErrorBD());
			}
			if (!resBD.isError())
			{
				resBD.getDatosPagoBD().setFechaOperacion(Varios.normalizeNull(resultadoConsulta.get(Datos.C_FECHA_OPERACION)));
				resBD.getDatosPagoBD().setFechaPago(Varios.normalizeNull(resultadoConsulta.get(Datos.C_FECHA_PAGO)));
				resBD.getDatosPagoBD().setEstado(Varios.normalizeNull(resultadoConsulta.get(Datos.C_ESTADO_PETICION_PATE)));
				resBD.getDatosPagoBD().setAplicacion(Varios.normalizeNull(resultadoConsulta.get(Datos.C_APLICACION)));
				resBD.getDatosPagoBD().setNumeroUnico(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NUM_UNICO)));
				resBD.getDatosPagoBD().setNumeroOperacion(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NUM_OPERACION)));
				resBD.getDatosPagoBD().setNrc(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NRC)));
				
				TraductorPasarelaEntidad traductor= new TraductorPasarelaEntidad(pref.getFicheroPasarelas());
				String pasarela= traductor.getPasarelaFromEntidad(Varios.normalizeNull(resultadoConsulta.get(Datos.C_PASARELA_PAGO_BD)));
				//resBD.getDatosPagoBD().setPasarelaPago(Varios.normalizeNull(resultadoConsulta.get(Datos.C_PASARELA_PAGO_BD)));
				resBD.getDatosPagoBD().setPasarelaPago(pasarela);
				
				resBD.getDatosPagoBD().setNifContribuyente(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NIF_CONTRIBUYENTE)));
				resBD.getDatosPagoBD().setFechaDevengo(Varios.normalizeNull(resultadoConsulta.get(Datos.C_FECHA_DEVENGO)));
				resBD.getDatosPagoBD().setDatoEspecifico(Varios.normalizeNull(resultadoConsulta.get(Datos.C_DATO_ESPECIFICO)));
				resBD.getDatosPagoBD().setExpediente(Varios.normalizeNull(resultadoConsulta.get(Datos.C_EXPEDIENTE)));
				resBD.getDatosPagoBD().setNifOperante(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NIF_OPERANTE)));
				resBD.getDatosPagoBD().setImporte(Varios.normalizeNull(resultadoConsulta.get(Datos.C_IMPORTE)));
				// CRUBENCVS 04/03/2011.  Operaciones por plataformas de pago. UniversalPay
				resBD.getDatosPagoBD().setOperacionEpst(Varios.normalizeNull(resultadoConsulta.get(Datos.C_OPERACION_EPST)));
				resBD.getDatosPagoBD().setMedioPago(Varios.normalizeNull(resultadoConsulta.get(Datos.C_MEDIO_PAGO)));
				resBD.getDatosPagoBD().setHashDatos(Varios.normalizeNull(resultadoConsulta.get(Datos.C_HASH_DATOS)));
				resBD.getDatosPagoBD().setJustificante(justificante);
				resBD.getDatosPagoBD().setIdentificacion(identificacion);
				resBD.getDatosPagoBD().setReferencia(referencia);
				//
			}
			return resBD;
		}
		catch (Exception e)
		{
			throw new PasarelaPagoException ("Error en la consulta en pate:" + e.getMessage(),e);
		}
	}
	/**
	 * Realiza la actualización del registro de pago en BD.
	 * @param pasarela Identificador de la pasarela mediante la que se ha hecho la última operación.
	 * @param estado 
	 * @param justificante
	 * @param identificacion
	 * @param referencia
	 * @param nio
	 * @param fechaPago
	 * @param nrc
	 * @param resultado
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final ResultadoLlamadaBD actualizarBD(String pasarela,
												 String estado, 
												 String justificante, 
												 String identificacion, 
												 String referencia, 
												 String nio, 
												 String fechaPago, 
												 String nrc, 
												 String operante, 
												 String fechaDevengo, 
												 String resultado, 
												 String fechaAnulacion) throws PasarelaPagoException
	{
		ResultadoLlamadaBD resBD = new ResultadoLlamadaBD();
		Datos dat = new Datos(this.idSesion);
		try
		{
			String NIO="";
			String FEC_PAGO="";
			String NRC = "";
			if (estado.equals(EstadosPago.PAGADO.getValor()))
			{
				NIO=nio;
				FEC_PAGO=fechaPago;
				NRC = nrc;
			}
			String entidad=null;
			if (pasarela!=null)
			{
				entidad=new TraductorPasarelaEntidad(pref.getFicheroPasarelas()).getEntidadFromPasarela(pasarela);
			}
			dat.actualizarPate(entidad,justificante,identificacion,referencia, estado, resultado, NRC,"","","",NIO, FEC_PAGO, operante, fechaDevengo, fechaAnulacion,"");
			String errorLlamada = dat.getErrorLlamada();
			if (errorLlamada!=null && !"".equals(errorLlamada))
			{
				resBD.setError(true);
				resBD.setCodError(Mensajes.getErrorActualizacionBD());
				resBD.setTextoError(Mensajes.getExternalText(resBD.getCodError()));
			}
			else
			{
				//Comprobamos el resultado de la consulta.
				checkErrorLlamadaBD(dat, resBD, Mensajes.getErrorActualizacionBD());
			}
			return resBD;
		}
		catch (Exception e)
		{
			throw new PasarelaPagoException ("Error al actualizar valores en la base de datos:"+ e.getMessage(),e); 
		}
	}
	/**
	 * Obtiene el estado actual del registro de pago en la base de datos.
	 * @param origen 
	 * @param modalidad
	 * @param justificante
	 * @param identificacion
	 * @param referencia
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final ResultadoLlamadaBD obtenerEstadoBD(TipoLlamada tipoLlamadaServicio, String justificante, String identificacion, String referencia) throws PasarelaPagoException
	{
		ResultadoLlamadaBD resBD= new ResultadoLlamadaBD();
		Datos dat= new Datos(this.idSesion);
		Map<String, String> resultadoConsulta=null;
		try
		{
			switch (tipoLlamadaServicio)
			{
			case SERVICIO_WEB_AUTOLIQUIDACION:
			case PORTAL_AUTOLIQUIDACION:
			case S1_AUTOLIQUIDACION:
				resultadoConsulta = dat.obtenerResultadoPate(justificante);
				break;
			case PORTAL_LIQUIDACION:
			case S1_LIQUIDACION:
				resultadoConsulta = dat.obtenerResultadoPate(identificacion,referencia);
				break;
			}
			String error = dat.getErrorLlamada();
			if (error!=null && !"".equals(error))
			{
				resBD.setError(true);
				resBD.setCodError(Mensajes.getErrorBD());
			}
			else
			{
				//Comprobamos el resultado de la consulta.
				checkErrorLlamadaBD(dat, resBD, Mensajes.getErrorBD());
			}
			if (!resBD.isError())
			{
				resBD.getDatosPagoBD().setNrc(resultadoConsulta.get(Datos.C_NRC));
			}
			return resBD;
		}
		catch (Exception e)
		{
			throw new PasarelaPagoException ("Error en la consulta en pate:" + e.getMessage(),e);
		}
	}
	/**
	 * Comprueba si ha habido error en los accesos a base de datos, y devuelve el resultado de servicio
	 * si ha sido así. Acepta un error por defecto para mostrar si el error recibido en la llamada de base
	 * de datos es desconocido.
	 * @param datos Objeto {@link Datos} utilizado para realizar la última llamada a base de datos.
	 * @param result {@link ResultadoLlamadaBD} que se devolverá informado con el resultado del servicio.
	 * @param errorPorDefecto código de error por defecto para mostrar si de la base de datos no se ha
	 *        devuelto otro.
	 * @return true si ha habido error en la última llamada a base de datos, o false si no.
	 */
	private boolean checkErrorLlamadaBD (Datos datos, ResultadoLlamadaBD result,String errorPorDefecto)
	{
		boolean hayError=false;
		InfoLlamada infoUltimaLlamada = datos.getInfoUltimaLlamada();
		if (infoUltimaLlamada!=null && infoUltimaLlamada.isError())
		{
				String error = infoUltimaLlamada.getCodErrorServicio();
				String mensaje;
				if (!"".equals(error)) //Hay código informado
				{
					mensaje= infoUltimaLlamada.getDesErrorServicio();
				}
				else
				{
					error = errorPorDefecto;
					mensaje=Mensajes.getExternalText(error);
				}
				result.setError(true);
				result.setCodError(error);
				result.setTextoError(mensaje);
				hayError=true;
		}
		return hayError;
	}
	/**
	 * Inicia la anulación en base de datos. Esto implica la modificación del estado de pago en pate.
	 * @param datosProceso Datos del proceso, incluye los datos de la petición al servicio.
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final ResultadoLlamadaBD inicioAnulacion(DatosProceso datosProceso) throws PasarelaPagoException
	{
		Datos dat = new Datos(this.idSesion);
		try
		{
			Map<String,String> resultadoPeticion=null;
			DatosEntradaServicio peticionServicio = datosProceso.getPeticionServicio();
			switch (datosProceso.getTipoLlamada())
			{
			case SERVICIO_WEB_AUTOLIQUIDACION:
				resultadoPeticion= dat.inicioAnulacion(peticionServicio.getEmisora(), peticionServicio.getJustificante());
				break;
			case PORTAL_AUTOLIQUIDACION:
			case S1_AUTOLIQUIDACION:
				resultadoPeticion= dat.inicioAnulacion(peticionServicio.getEmisora(), peticionServicio.getJustificante());
				break;
			case PORTAL_LIQUIDACION:
			case S1_LIQUIDACION:
				resultadoPeticion= dat.inicioAnulacion(peticionServicio.getEmisora(),peticionServicio.getIdentificacion(),peticionServicio.getReferencia());
				break;
			}
			ResultadoLlamadaBD resBD= new ResultadoLlamadaBD();
			String error = dat.getErrorLlamada();
			if (error!=null && !"".equals(error))
			{
				resBD.setError(true);
				resBD.setCodError(Mensajes.getErrorPeticionBD());
			}
			else
			{
				checkErrorLlamadaBD(dat,resBD,Mensajes.getErrorPeticionBD());
			}
			if (!resBD.isError())
			{
				resBD.getDatosPagoBD().setEstado(Varios.normalizeNull(resultadoPeticion.get(Datos.C_ESTADO_PETICION_PATE)));
				resBD.getDatosPagoBD().setFechaPago(Varios.normalizeNull(resultadoPeticion.get(Datos.C_FECHA_PAGO)));
				
				TraductorPasarelaEntidad traductor= new TraductorPasarelaEntidad(pref.getFicheroPasarelas());
				String pasarela= traductor.getPasarelaFromEntidad(Varios.normalizeNull(resultadoPeticion.get(Datos.C_PASARELA_PAGO_BD)));
				//resBD.getDatosPagoBD().setPasarelaPago(Varios.normalizeNull(resultadoPeticion.get(Datos.C_PASARELA_PAGO_BD)));
				resBD.getDatosPagoBD().setPasarelaPago(pasarela);
				
				resBD.getDatosPagoBD().setNumeroOperacion(Varios.normalizeNull(resultadoPeticion.get(Datos.C_NUM_OPERACION)));
				resBD.getDatosPagoBD().setFechaDevengo(Varios.normalizeNull(resultadoPeticion.get(Datos.C_FECHA_DEVENGO)));
				resBD.getDatosPagoBD().setNifContribuyente(Varios.normalizeNull(resultadoPeticion.get(Datos.C_NIF_CONTRIBUYENTE)));
				resBD.getDatosPagoBD().setDatoEspecifico(Varios.normalizeNull(resultadoPeticion.get(Datos.C_DATO_ESPECIFICO)));
				resBD.getDatosPagoBD().setExpediente(Varios.normalizeNull(resultadoPeticion.get(Datos.C_EXPEDIENTE)));
				resBD.getDatosPagoBD().setNifOperante(Varios.normalizeNull(resultadoPeticion.get(Datos.C_NIF_OPERANTE)));
				resBD.getDatosPagoBD().setImporte(Varios.normalizeNull(resultadoPeticion.get(Datos.C_IMPORTE)));				
			}
			return resBD;
		}
		catch (RemoteException e)
		{
			throw new PasarelaPagoException ("Error en la inicialización de anulación en base de datos:"+ e.getMessage(),e);
		}
	}
	
	/**
	 * Realiza el cambio de pasarela en base de datos, para que las nuevas peticiones se efectúen por la indicada.
	 * @param idPasarela Identificador de pasarela por la que se van a dirigir las nuevas peticiones.
	 * @param datosProceso Datos del proceso.
	 * @return Resultado de llamada a base de datos.
	 * @throws PasarelaPagoException
	 */
	public final ResultadoLlamadaBD cambioPasarelaBD(String idPasarela,DatosProceso datosProceso) throws PasarelaPagoException
	{
		Datos dat = new Datos(this.idSesion);
		try
		{
			String entidad=new TraductorPasarelaEntidad(pref.getFicheroPasarelas()).getEntidadFromPasarela(idPasarela);
			DatosEntradaServicio peticionServicio = datosProceso.getPeticionServicio();
			
			dat.cambioPasarelaPago(peticionServicio.getOrigen(), peticionServicio.getModalidad(), peticionServicio.getEmisora(), datosProceso.getJustificante(), peticionServicio.getIdentificacion(), peticionServicio.getReferencia(), entidad);
			ResultadoLlamadaBD resBD= new ResultadoLlamadaBD();
			String error = dat.getErrorLlamada();
			if (error!=null && !"".equals(error))
			{
				resBD.setError(true);
				resBD.setCodError(Mensajes.getErrorPeticionBD());
			}
			else
			{
				checkErrorLlamadaBD(dat,resBD,Mensajes.getErrorPeticionBD());
			}
			return resBD;
		}
		catch (RemoteException e)
		{
			throw new PasarelaPagoException ("Error en el cambio de pasarela en base de datos:"+ e.getMessage(),e);
		}
	}
	/**
	 * Realiza una consulta a la base de datos para recuperar los datos en base a la aplicación y el número único.
	 * @param tipoLlamadaServicio Tipo de llamada (SW-3, PT-3, etc)
	 * @param aplicacion Aplicación (=servicio)
	 * @param numeroUnico Número único
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final ResultadoLlamadaBD consultaBD(TipoLlamada tipoLlamadaServicio, String aplicacion, String numeroUnico) throws PasarelaPagoException
	{
		ResultadoLlamadaBD resBD= new ResultadoLlamadaBD();
		Datos dat= new Datos(this.idSesion);
		Map<String, String> resultadoConsulta=null;
		try
		{
			switch (tipoLlamadaServicio)
			{
			case SERVICIO_WEB_AUTOLIQUIDACION:
				resultadoConsulta = dat.consultaJustificante(aplicacion, numeroUnico);
				break;
			case PORTAL_LIQUIDACION:
			case S1_LIQUIDACION:
			case PORTAL_AUTOLIQUIDACION:
			case S1_AUTOLIQUIDACION:
				throw new UnsupportedOperationException("Operación sólo disponible para Origen SW-3");
			}
			String error = dat.getErrorLlamada();
			if (error!=null && !"".equals(error))
			{
				resBD.setError(true);
				resBD.setCodError(Mensajes.getErrorBD());
			}
			else
			{
				//Comprobamos el resultado de la consulta.
				checkErrorLlamadaBD(dat, resBD, Mensajes.getErrorBD());
			}
			if (!resBD.isError())
			{
				
				resBD.getDatosPagoBD().setJustificante(Varios.normalizeNull(resultadoConsulta.get(Datos.C_JUSTIFICANTE_PETICION_PATE)));
				resBD.getDatosPagoBD().setFechaOperacion(Varios.normalizeNull(resultadoConsulta.get(Datos.C_FECHA_OPERACION)));
				resBD.getDatosPagoBD().setFechaPago(Varios.normalizeNull(resultadoConsulta.get(Datos.C_FECHA_PAGO)));
				resBD.getDatosPagoBD().setEstado(Varios.normalizeNull(resultadoConsulta.get(Datos.C_ESTADO_PETICION_PATE)));
				resBD.getDatosPagoBD().setAplicacion(Varios.normalizeNull(resultadoConsulta.get(Datos.C_APLICACION)));
				resBD.getDatosPagoBD().setNumeroUnico(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NUM_UNICO)));
				resBD.getDatosPagoBD().setNumeroOperacion(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NUM_OPERACION)));
				resBD.getDatosPagoBD().setNrc(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NRC)));
				
				TraductorPasarelaEntidad traductor= new TraductorPasarelaEntidad(pref.getFicheroPasarelas());
				String pasarela= traductor.getPasarelaFromEntidad(Varios.normalizeNull(resultadoConsulta.get(Datos.C_PASARELA_PAGO_BD)));
				resBD.getDatosPagoBD().setPasarelaPago(pasarela);
				
				resBD.getDatosPagoBD().setNifContribuyente(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NIF_CONTRIBUYENTE)));
				resBD.getDatosPagoBD().setFechaDevengo(Varios.normalizeNull(resultadoConsulta.get(Datos.C_FECHA_DEVENGO)));
				resBD.getDatosPagoBD().setDatoEspecifico(Varios.normalizeNull(resultadoConsulta.get(Datos.C_DATO_ESPECIFICO)));
				resBD.getDatosPagoBD().setExpediente(Varios.normalizeNull(resultadoConsulta.get(Datos.C_EXPEDIENTE)));
				resBD.getDatosPagoBD().setNifOperante(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NIF_OPERANTE)));
				resBD.getDatosPagoBD().setImporte(Varios.normalizeNull(resultadoConsulta.get(Datos.C_IMPORTE)));
			}
			return resBD;
		}
		catch (Exception e)
		{
			throw new PasarelaPagoException ("Error en la consulta en pate:" + e.getMessage(),e);
		}
	}
	
	/**
	 * Inicia la petición de pago para la plataforma de pago UniversalPay.
	 * @param peticion Datos de la peticiónde inicio de pago
	 * @param procAlmacenado Procedimiento almacenado a utilizar
	 * @param entidadBancaria Entidad bancaria asociada a la plataforma de pago
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final ResultadoLlamadaBD iniciarPagoTarjetaUniversalPay(InicioPagoTarjetaRequest peticion,
																   String entidadBancaria,
													   			   String procAlmacenado) throws PasarelaPagoException
	{
		Datos dat = new Datos(this.idSesion);
		try
		{
			Map<String,String> resultadoPeticion= dat.inicioPagoTarjetaUniversalPay(
								peticion.getOrigen(),
								peticion.getModalidad(), 
								peticion.getEmisora(), 
								peticion.getNifContribuyente(), 
								peticion.getFechaDevengo(), 
								peticion.getNumeroAutoliquidacion(), 
								peticion.getDatoEspecifico(), 
								peticion.getExpediente(), 
								peticion.getImporte(), 
								peticion.getIdentificacion(), 
							    peticion.getReferencia(), 
								peticion.getNifOperante(), 
								peticion.getModelo(), 
								entidadBancaria,
								procAlmacenado
								);
			
			ResultadoLlamadaBD resBD= new ResultadoLlamadaBD();
			String error = dat.getErrorLlamada();
			if (error!=null && !"".equals(error))
			{
				resBD.setError(true);
				resBD.setCodError(Mensajes.getErrorPeticionBD());
				resBD.setTextoError(Mensajes.getExternalText(resBD.getCodError()));
			}
			else
			{
				checkErrorLlamadaBD(dat,resBD,Mensajes.getErrorPeticionBD());
			}
			if (!resBD.isError())
			{
				resBD.getDatosPagoBD().setEstado(Varios.normalizeNull(resultadoPeticion.get(Datos.C_ESTADO_PETICION_PATE)));
				resBD.getDatosPagoBD().setOperacionEpst(Varios.normalizeNull(resultadoPeticion.get(Datos.C_OPERACION_EPST)));
				resBD.getDatosPagoBD().setMedioPago(Varios.normalizeNull(resultadoPeticion.get(Datos.C_MEDIO_PAGO)));
				resBD.getDatosPagoBD().setHashDatos(Varios.normalizeNull(resultadoPeticion.get(Datos.C_HASH_DATOS)));
			}
			return resBD;
		}
		catch (RemoteException e)
		{
			throw new PasarelaPagoException ("Error en la inicialización de pago con tarjeta en base de datos:"+ e.getMessage(),e);
		}

	}
	
	/**
	 * Realiza una consulta en bd de un pago por tarjeta realizado a través de una plataforma de pago
	 * @param origen
	 * @param modalidad
	 * @param emisora
	 * @param justificante
	 * @param identificacion
	 * @param referencia
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final ResultadoConsultaPagoBD consultaPagoTarjetaBD(
									String origen, 
									String modalidad, 
									String emisora, 
									String justificante, 
									String identificacion, 
									String referencia,
									String aplicacion,
									String numeroUnico
									) throws PasarelaPagoException
	{
		ResultadoConsultaPagoBD resBD= new ResultadoConsultaPagoBD();
		Datos dat= new Datos(this.idSesion);
		Map<String, String> resultadoConsulta=null;
		try
		{
			if (aplicacion!=null && !"".equals(Varios.null2empty(aplicacion)) && !"".equals(Varios.null2empty(numeroUnico))){
				resultadoConsulta= dat.consultaJustificante(aplicacion, numeroUnico);
			}
			else if (
					 "3".equals(modalidad) && !"".equals(Varios.null2empty(justificante)) 
				    ){
						resultadoConsulta = dat.consultaPate(emisora, justificante);
			}
			if ("2".equals(modalidad) && !"".equals(Varios.null2empty(identificacion)) && !"".equals(Varios.null2empty(referencia))){
				resultadoConsulta = dat.consultaPate(emisora, identificacion,referencia);
			}
			String error = dat.getErrorLlamada();
			if (error!=null && !"".equals(error))
			{
				resBD.setConsultaCorrecta(false);
			}
			else
			{
				//Puede ser un retorno de "NO HAY DATOS", en cuyo caso
				//la consulta es correcta, sólo que no hay datos
				if (dat.getInfoUltimaLlamada()!=null){
					InfoLlamada info= dat.getInfoUltimaLlamada();
					if (!info.isError()){
						resBD.setConsultaCorrecta(true);
						resBD.setHayDatos(true);
					} else if ("9995".equals(info.getCodErrorServicio())){ //No hay datos
						resBD.setConsultaCorrecta(true);
						resBD.setHayDatos(false);
					} else {
						resBD.setConsultaCorrecta(false);
					}
				} else {
					resBD.setConsultaCorrecta(false);
				}
			}
			if (resBD.isConsultaCorrecta() && resBD.isHayDatos())
			{
				resBD.getDatosPagoBD().setFechaOperacion(Varios.normalizeNull(resultadoConsulta.get(Datos.C_FECHA_OPERACION)));
				resBD.getDatosPagoBD().setFechaPago(Varios.normalizeNull(resultadoConsulta.get(Datos.C_FECHA_PAGO)));
				resBD.getDatosPagoBD().setEstado(Varios.normalizeNull(resultadoConsulta.get(Datos.C_ESTADO_PETICION_PATE)));
				resBD.getDatosPagoBD().setAplicacion(Varios.normalizeNull(resultadoConsulta.get(Datos.C_APLICACION)));
				resBD.getDatosPagoBD().setNumeroUnico(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NUM_UNICO)));
				resBD.getDatosPagoBD().setNumeroOperacion(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NUM_OPERACION)));
				resBD.getDatosPagoBD().setNrc(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NRC)));
				
				TraductorPasarelaEntidad traductor= new TraductorPasarelaEntidad(pref.getFicheroPasarelas());
				String pasarela= traductor.getPasarelaFromEntidad(Varios.normalizeNull(resultadoConsulta.get(Datos.C_PASARELA_PAGO_BD)));
				resBD.getDatosPagoBD().setPasarelaPago(pasarela);
				
				resBD.getDatosPagoBD().setNifContribuyente(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NIF_CONTRIBUYENTE)));
				resBD.getDatosPagoBD().setFechaDevengo(Varios.normalizeNull(resultadoConsulta.get(Datos.C_FECHA_DEVENGO)));
				resBD.getDatosPagoBD().setDatoEspecifico(Varios.normalizeNull(resultadoConsulta.get(Datos.C_DATO_ESPECIFICO)));
				resBD.getDatosPagoBD().setExpediente(Varios.normalizeNull(resultadoConsulta.get(Datos.C_EXPEDIENTE)));
				resBD.getDatosPagoBD().setNifOperante(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NIF_OPERANTE)));
				resBD.getDatosPagoBD().setImporte(Varios.normalizeNull(resultadoConsulta.get(Datos.C_IMPORTE)));
				resBD.getDatosPagoBD().setOperacionEpst(Varios.normalizeNull(resultadoConsulta.get(Datos.C_OPERACION_EPST)));
				resBD.getDatosPagoBD().setMedioPago(Varios.normalizeNull(resultadoConsulta.get(Datos.C_MEDIO_PAGO)));
				resBD.getDatosPagoBD().setHashDatos(Varios.normalizeNull(resultadoConsulta.get(Datos.C_HASH_DATOS)));
				resBD.getDatosPagoBD().setJustificante(justificante);
				resBD.getDatosPagoBD().setIdentificacion(identificacion);
				resBD.getDatosPagoBD().setReferencia(referencia);

			}
			return resBD;
		}
		catch (Exception e)
		{
			throw new PasarelaPagoException ("Error en la consulta en pate:" + e.getMessage(),e);
		}
	}
	/**
	 * Finalización de un pago en base de datos
	 * @param operacionEpst
	 * @param estado
	 * @param resultado
	 * @param numOperacion
	 * @param nrc
	 * @param fechaPago
	 * @param procAlmacenado
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final ResultadoLlamadaBD finalizarPagoTarjeta(
					String operacionEpst,
					String estado,
					String resultado,
					String numOperacion,
					String nrc,
					String fechaPago,
					//CRUBENCVS 11/11/2021
					String emisora,
					String justificante,
					String identificacion,
					String referencia,
					//FIN CRUBENCVS 11/11/2021
					String procAlmacenado
    ) throws PasarelaPagoException
	{
		ResultadoLlamadaBD resBD = new ResultadoLlamadaBD();
		resBD.setError(false);
		Datos dat = new Datos(this.idSesion);
		try
		{
			dat.finalizarPagoTarjeta(
					               operacionEpst, 
					               estado, 
					               resultado, 
					               numOperacion, 
					               nrc, 
					               fechaPago, 
					               emisora,
					               justificante,
					               identificacion,
					               referencia,
					               procAlmacenado
					               );	
			String errorLlamada = dat.getErrorLlamada();
			if (errorLlamada!=null && !"".equals(errorLlamada))
			{
				resBD.setError(true);
				resBD.setCodError(Mensajes.getErrorActualizacionBD());
				resBD.setTextoError(Mensajes.getExternalText(resBD.getCodError()));
			}
			else
			{
				//Comprobamos el resultado de la consulta.
				checkErrorLlamadaBD(dat, resBD, Mensajes.getErrorActualizacionBD());
			}
			return resBD;
		}
		catch (Exception e)
		{
			throw new PasarelaPagoException ("Error al finalizar el pago con tarjeta en base de datos:"+ e.getMessage(),e); 
		}
	}
	/**
	 * Inicia la anulación de pago en la base de datos
	 * @param emisora
	 * @param numeroAutoliquidacion
	 * @param identificacion
	 * @param referencia
	 * @param procAlmacenado
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final ResultadoLlamadaBD inicioAnulacionPagoTarjeta(
									String emisora,
									String numeroAutoliquidacion,
									String identificacion,
									String referencia,
									String procAlmacenado) throws PasarelaPagoException
	{
		ResultadoLlamadaBD resBD = new ResultadoLlamadaBD();
		resBD.setError(false);
		Datos dat = new Datos(this.idSesion);
		try
		{
			dat.inicioAnulacionPagoTarjeta(
					emisora,
					numeroAutoliquidacion,
					identificacion,
					referencia,
					procAlmacenado
					);
			String errorLlamada = dat.getErrorLlamada();
			if (errorLlamada!=null && !"".equals(errorLlamada))
			{
				resBD.setError(true);
				resBD.setCodError(Mensajes.getErrorActualizacionBD());
				resBD.setTextoError(Mensajes.getExternalText(resBD.getCodError()));
			}
			else
			{
				//Comprobamos el resultado de la consulta.
				checkErrorLlamadaBD(dat, resBD, Mensajes.getErrorActualizacionBD());
			}
			return resBD;
		}
		catch (Exception e)
		{
			throw new PasarelaPagoException ("Error al iniciar la anulación del pago con tarjeta en base de datos:"+ e.getMessage(),e); 
		}
	}
	/**
	 * Finaliza la anulación del pago con tarjeta
	 * @param emisora
	 * @param numeroAutoliquidacion
	 * @param identificacion
	 * @param referencia
	 * @param estado
	 * @param procAlmacenado
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final ResultadoLlamadaBD finalizaAnulacionPagoTarjeta(
			String emisora,
			String numeroAutoliquidacion,
			String identificacion,
			String referencia,
			String estado,
			String codResultado,
			String procAlmacenado) throws PasarelaPagoException
	{
		ResultadoLlamadaBD resBD = new ResultadoLlamadaBD();
		resBD.setError(false);
		Datos dat = new Datos(this.idSesion);
		try
		{
			dat.finAnulacionPagoTarjeta(
					emisora,
					numeroAutoliquidacion,
					identificacion,
					referencia,
					estado,
					codResultado,
					procAlmacenado
					);
			String errorLlamada = dat.getErrorLlamada();
			if (errorLlamada!=null && !"".equals(errorLlamada))
			{
				resBD.setError(true);
				resBD.setCodError(Mensajes.getErrorActualizacionBD());
				resBD.setTextoError(Mensajes.getExternalText(resBD.getCodError()));
			}
			else
			{
				//Comprobamos el resultado de la operación
				checkErrorLlamadaBD(dat, resBD, Mensajes.getErrorActualizacionBD());
			}
			return resBD;
		}
		catch (Exception e)
		{
			throw new PasarelaPagoException ("Error al finalizar la anulación del pago con tarjeta en base de datos:"+ e.getMessage(),e); 
		}
	}
	
	/**
	 * Valida la MAC que se pasa
	 * @param origen Origen de la mac recibida. En función de este origen se considera una clave de MAC u otra
	 * @param datosMac Datos con los que se intentará replicar la MAC, para verificar
	 * @param macRecibida MAC recibida
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final boolean validarMac(
			String origen,
			String datosMac,
			String macRecibida
			) throws PasarelaPagoException
	{
		try {
			Datos dat = new Datos(this.idSesion);
			return dat.validarMac(origen, datosMac, macRecibida);
		} catch (Exception e)
		{
			throw new PasarelaPagoException ("Error al verificar la MAC:"+ e.getMessage(),e); 
		}
	}
	/**
	 * Genera la MAC para el origen indicado
	 * @param origen
	 * @param datosMac
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final String generarMac(
			String origen,
			String datosMac
			) throws PasarelaPagoException
	{
		try {
			Datos dat = new Datos(this.idSesion);
			return dat.generarMac(origen, datosMac);
		} catch (Exception e)
		{
			throw new PasarelaPagoException ("Error al generar la MAC:"+ e.getMessage(),e); 
		}
	}
	/**
	 * Inicio de la operación de pago invocada  desde PA
	 * @param peticion
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final ResultadoLlamadaBD iniciarOperacionPago(InicioOperacionPagoRequest peticion) throws PasarelaPagoException
	{
		Datos dat = new Datos(this.idSesion);
		try
		{
			Map<String,String> resultadoPeticion= dat.inicioOperacionPago (
			peticion.getOrigen(),
			peticion.getModalidad(), 
			peticion.getEmisora(),
			peticion.getImporte(), 
			peticion.getNifContribuyente(), 
			peticion.getFechaDevengo(), 
			peticion.getDatoEspecifico(), 
			peticion.getAplicacion(),
			peticion.getNumeroUnico(),
			peticion.getNifOperante(), 
			peticion.getModelo()
		);
		
		ResultadoLlamadaBD resBD= new ResultadoLlamadaBD();
		String error = dat.getErrorLlamada();
		if (error!=null && !"".equals(error))
		{
			resBD.setError(true);
			resBD.setCodError(Mensajes.getErrorPeticionBD());
			resBD.setTextoError(Mensajes.getExternalText(resBD.getCodError()));
		}
		else
		{
			checkErrorLlamadaBD(dat,resBD,Mensajes.getErrorPeticionBD());
		}
		if (!resBD.isError())
		{
			resBD.getDatosPagoBD().setHashDatos(Varios.normalizeNull(resultadoPeticion.get(Datos.C_HASH_DATOS)));
		}
			return resBD;
		}
		catch (RemoteException e)
		{
			throw new PasarelaPagoException ("Error en la inicialización de pago con tarjeta en base de datos:"+ e.getMessage(),e);
		}
	
	}
	/**
	 * Consulta a realizar antes del inicio de pago con tarjeta, para conocer el estado
	 * del registro en PATE.
	 * Devolverá los datos del registro, además de una marca para indicar que se trata de pasarela nueva o no
	 * y otra para indicar que los datos del pago son inconsistentes con los encontrados en la base de datos,
	 * si se intenta iniciar un pago con datos diferentes.
	 * @param origen
	 * @param modalidad
	 * @param emisora
	 * @param importe
	 * @param justificante
	 * @param nifContribuyente
	 * @param fechaDevengo
	 * @param datoEspecifico
	 * @param expediente
	 * @param identificacion
	 * @param referencia
	 * @param nifOperante
	 * @param modelo
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final ResultadoConsultaPagoBD consultaInicioPagoTarjeta(
    String origen,
    String modalidad,
    String emisora,
    String importe,
    String justificante,
    String nifContribuyente,
    String fechaDevengo,
    String datoEspecifico,
    String expediente,
    String identificacion,
    String referencia,
    String nifOperante,
    String modelo
  ) throws PasarelaPagoException {
    ResultadoConsultaPagoBD resBD = new ResultadoConsultaPagoBD();
    Datos dat = new Datos(this.idSesion);
    Map < String, String > resultadoConsulta = null;
    try {
      resultadoConsulta = 
    	  dat.consultaInicioPagoTajeta(
    			  	origen, 
    			  	modalidad, 
    			  	emisora, 
    			  	importe, 
    			  	justificante, 
    			  	nifContribuyente, 
    			  	fechaDevengo, 
    			  	datoEspecifico, 
    			  	expediente, 
    			  	identificacion, 
    			  	referencia, 
    			  	nifOperante, 
    			  	modelo);
      String error = dat.getErrorLlamada();
      if (error != null && !"".equals(error)) {
        resBD.setConsultaCorrecta(false);
      } else {
        if (dat.getInfoUltimaLlamada() != null) {
          InfoLlamada info = dat.getInfoUltimaLlamada();
          if (!info.isError()) {
            resBD.setConsultaCorrecta(true);
            resBD.setHayDatos(true);
          } else if ("9995".equals(info.getCodErrorServicio())) { // No hay datos
            resBD.setConsultaCorrecta(true);
            resBD.setHayDatos(false);
          } else {
            resBD.setConsultaCorrecta(false);
          }
        } else {
          resBD.setConsultaCorrecta(false);
        }
      }
      if (resBD.isConsultaCorrecta() && resBD.isHayDatos()) {
        resBD.getDatosPagoBD().setFechaOperacion(Varios.normalizeNull(resultadoConsulta.get(Datos.C_FECHA_OPERACION)));
        resBD.getDatosPagoBD().setFechaPago(Varios.normalizeNull(resultadoConsulta.get(Datos.C_FECHA_PAGO)));
        resBD.getDatosPagoBD().setEstado(Varios.normalizeNull(resultadoConsulta.get(Datos.C_ESTADO_PETICION_PATE)));
        resBD.getDatosPagoBD().setAplicacion(Varios.normalizeNull(resultadoConsulta.get(Datos.C_APLICACION)));
        resBD.getDatosPagoBD().setNumeroUnico(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NUM_UNICO)));
        resBD.getDatosPagoBD().setNumeroOperacion(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NUM_OPERACION)));
        resBD.getDatosPagoBD().setNrc(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NRC)));

        if (!"".equals(Varios.normalizeNull(resultadoConsulta.get(Datos.C_OPERACION_EPST))) && 
        		"T".equals(Varios.normalizeNull(resultadoConsulta.get(Datos.C_MEDIO_PAGO)))){
        	//Es por pasarela nueva.
        	resBD.getDatosPagoBD().setPagoPasarelaNueva(true);
        }
        else {
        	resBD.getDatosPagoBD().setPagoPasarelaNueva(false);
        	//Sacamos también la pasarela por la que vamos a repetir el pago, en su caso
        	TraductorPasarelaEntidad traductor = new TraductorPasarelaEntidad(pref.getFicheroPasarelas());
        	//En la consulta previa al inicio de pago con tarjeta, cuando el registro se grabó
        	//desde el Principado, no tiene pasarela grabada, porque la pasarela se la ponemos nosotros
        	//al escoger plataforma de pago, y como tiene que ser genérico, para poder activar y 
        	//desactivar alguna en caso de no estar disponible, no podemos tener una por defecto.
        	try {
        		String pasarela = traductor.getPasarelaFromEntidad(Varios.normalizeNull(resultadoConsulta.get(Datos.C_PASARELA_PAGO_BD)));
        		resBD.getDatosPagoBD().setPasarelaPago(pasarela);
        	} catch (PasarelaPagoException pp){
        		resBD.getDatosPagoBD().setPasarelaPago("");
        	}
        }
        String datosConsistentes=Varios.normalizeNull(resultadoConsulta.get(Datos.C_DATOS_CONSISTENTES));
        resBD.setDatosConsistentes("1".equals(datosConsistentes)); //Para saber si los datos de pago han cambiado respecto a lo almacenado
        resBD.getDatosPagoBD().setNifContribuyente(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NIF_CONTRIBUYENTE)));
        resBD.getDatosPagoBD().setFechaDevengo(Varios.normalizeNull(resultadoConsulta.get(Datos.C_FECHA_DEVENGO)));
        resBD.getDatosPagoBD().setDatoEspecifico(Varios.normalizeNull(resultadoConsulta.get(Datos.C_DATO_ESPECIFICO)));
        resBD.getDatosPagoBD().setExpediente(Varios.normalizeNull(resultadoConsulta.get(Datos.C_EXPEDIENTE)));
        resBD.getDatosPagoBD().setNifOperante(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NIF_OPERANTE)));
        resBD.getDatosPagoBD().setImporte(Varios.normalizeNull(resultadoConsulta.get(Datos.C_IMPORTE)));
        resBD.getDatosPagoBD().setOperacionEpst(Varios.normalizeNull(resultadoConsulta.get(Datos.C_OPERACION_EPST)));
        resBD.getDatosPagoBD().setMedioPago(Varios.normalizeNull(resultadoConsulta.get(Datos.C_MEDIO_PAGO)));
        resBD.getDatosPagoBD().setHashDatos(Varios.normalizeNull(resultadoConsulta.get(Datos.C_HASH_DATOS)));
        resBD.getDatosPagoBD().setJustificante(justificante);
        resBD.getDatosPagoBD().setIdentificacion(identificacion);
        resBD.getDatosPagoBD().setReferencia(referencia);

      }
      return resBD;
    } catch (Exception e) {
      throw new PasarelaPagoException("Error en la consulta en pate:" + e.getMessage(), e);
    }
	}
	/**
	 * Convierte un registro de PATE perteneciente a las pasarelas antiguas a un
	 * registro con los datos necesarios para identificarlo como un pago de universalpay,
	 * para poder desviarlo por esa pasarela
	 * @param emisora
	 * @param justificante
	 * @param identificacion
	 * @param referencia
	 * @param procAlmacenado
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final ResultadoLlamadaBD convertirPagoAntiguoAUniversalPay(
			String emisora, 
			String justificante,
			String identificacion, 
			String referencia,
			String procAlmacenado) throws PasarelaPagoException
	{
		Datos dat = new Datos(this.idSesion);
		try
		{
			dat.convertirPagoAntiguoAUniversalPay(
					emisora,
					justificante,
					identificacion,
					referencia,
					procAlmacenado
					);
		
			ResultadoLlamadaBD resBD= new ResultadoLlamadaBD();
			String error = dat.getErrorLlamada();
			if (error!=null && !"".equals(error))
			{
				resBD.setError(true);
				resBD.setCodError(Mensajes.getErrorPeticionBD());
				resBD.setTextoError(Mensajes.getExternalText(resBD.getCodError()));
			}
			else
			{
				checkErrorLlamadaBD(dat,resBD,Mensajes.getErrorPeticionBD());
			}
			return resBD;
		}
		catch (RemoteException e)
		{
			throw new PasarelaPagoException ("Error en la inicialización de pago con tarjeta en base de datos:"+ e.getMessage(),e);
		}
	
	}
	/**
	 * Consulta previa al inicio de la operación de pago desde Principado de Asturias
	 * @param aplicacion
	 * @param numeroUnico
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final ResultadoConsultaPagoBD consultaInicioOperacionPago(
			String origen,
		    String modalidad,
		    String emisora,
		    String modelo,
		    String importe,
		    String nifContribuyente,
		    String fechaDevengo,
		    String datoEspecifico,
		    String nifOperante,
		    String aplicacion,
		    String numeroUnico
		  ) throws PasarelaPagoException {
		    ResultadoConsultaPagoBD resBD = new ResultadoConsultaPagoBD();
		    Datos dat = new Datos(this.idSesion);
		    Map < String, String > resultadoConsulta = null;
		    try {
		      resultadoConsulta = 
		    	  dat.consultaInicioOperacionPago(
		    			  	origen, 
		    			  	modalidad, 
		    			  	emisora, 
		    			  	modelo,
		    			  	importe, 
		    			  	nifContribuyente, 
		    			  	fechaDevengo, 
		    			  	datoEspecifico, 
		    			  	nifOperante,
		    			  	aplicacion,
		    			  	numeroUnico
		    			  	);
		      String error = dat.getErrorLlamada();
		      if (error != null && !"".equals(error)) {
		        resBD.setConsultaCorrecta(false);
		      } else {
		        if (dat.getInfoUltimaLlamada() != null) {
		          InfoLlamada info = dat.getInfoUltimaLlamada();
		          if (!info.isError()) {
		            resBD.setConsultaCorrecta(true);
		            resBD.setHayDatos(true);
		          } else if ("9995".equals(info.getCodErrorServicio())) { // No hay datos
		            resBD.setConsultaCorrecta(true);
		            resBD.setHayDatos(false);
		          } else {
		            resBD.setConsultaCorrecta(false);
		          }
		        } else {
		          resBD.setConsultaCorrecta(false);
		        }
		      }
		      if (resBD.isConsultaCorrecta() && resBD.isHayDatos()) {
		    	// Nos interesan pocos datos, sobre todo que exista y el estado y si es por la 
		    	// pasarela antigua o la nueva  
		        resBD.getDatosPagoBD().setFechaOperacion(Varios.normalizeNull(resultadoConsulta.get(Datos.C_FECHA_OPERACION)));
		        String fechaPago= Varios.normalizeNull(resultadoConsulta.get(Datos.C_FECHA_PAGO));
		        if (!"".equals(fechaPago)){
		        	try{
		        		SimpleDateFormat sd= new SimpleDateFormat("yyyyMMdd");
		        		Date fecha=sd.parse(fechaPago);
		        		sd= new SimpleDateFormat("yyyy-MM-dd");
		        		fechaPago= sd.format(fecha);
		        	}  catch(Exception e){
		        		fechaPago="";
		        	}
		        }
		        resBD.getDatosPagoBD().setFechaPago(fechaPago);
		        resBD.getDatosPagoBD().setEstado(Varios.normalizeNull(resultadoConsulta.get(Datos.C_ESTADO_PETICION_PATE)));
		        resBD.getDatosPagoBD().setAplicacion(Varios.normalizeNull(resultadoConsulta.get(Datos.C_APLICACION)));
		        resBD.getDatosPagoBD().setNumeroUnico(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NUM_UNICO)));
		        resBD.getDatosPagoBD().setNumeroOperacion(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NUM_OPERACION)));
		        resBD.getDatosPagoBD().setNrc(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NRC)));
		        
		        if (!"".equals(Varios.normalizeNull(resultadoConsulta.get(Datos.C_OPERACION_EPST))) && 
		        		"T".equals(Varios.normalizeNull(resultadoConsulta.get(Datos.C_MEDIO_PAGO)))){
		        	//Es por pasarela nueva.
		        	resBD.getDatosPagoBD().setPagoPasarelaNueva(true);
		        }
		        else {
		        	//Puede (y será normal) que no haya datos de pasarela asociados,
		        	//ya que eso corresponde al pago. 
		        	resBD.getDatosPagoBD().setPagoPasarelaNueva(false);
		        	//Puede que haya una petición anterior en estado "G", en cuyo caso no habrá pasarela asociada,
		        	//porque eso es cosa del pago, o bien que sea otro estado, en cuyo caso ya habrá datos de pasarela.
		        	if (!"".equals(Varios.normalizeNull(resultadoConsulta.get(Datos.C_PASARELA_PAGO_BD)))){
			        	//Sacamos también la pasarela por la que vamos a repetir el pago, en su caso
			        	TraductorPasarelaEntidad traductor = new TraductorPasarelaEntidad(pref.getFicheroPasarelas());
			            String pasarela = traductor.getPasarelaFromEntidad(Varios.normalizeNull(resultadoConsulta.get(Datos.C_PASARELA_PAGO_BD)));
			            resBD.getDatosPagoBD().setPasarelaPago(pasarela);
		        	}
		        }
		        resBD.getDatosPagoBD().setNifContribuyente(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NIF_CONTRIBUYENTE)));
		        resBD.getDatosPagoBD().setFechaDevengo(Varios.normalizeNull(resultadoConsulta.get(Datos.C_FECHA_DEVENGO)));
		        resBD.getDatosPagoBD().setDatoEspecifico(Varios.normalizeNull(resultadoConsulta.get(Datos.C_DATO_ESPECIFICO)));
		        resBD.getDatosPagoBD().setExpediente(Varios.normalizeNull(resultadoConsulta.get(Datos.C_EXPEDIENTE)));
		        resBD.getDatosPagoBD().setNifOperante(Varios.normalizeNull(resultadoConsulta.get(Datos.C_NIF_OPERANTE)));
		        resBD.getDatosPagoBD().setImporte(Varios.normalizeNull(resultadoConsulta.get(Datos.C_IMPORTE)));
		        resBD.getDatosPagoBD().setOperacionEpst(Varios.normalizeNull(resultadoConsulta.get(Datos.C_OPERACION_EPST)));
		        resBD.getDatosPagoBD().setMedioPago(Varios.normalizeNull(resultadoConsulta.get(Datos.C_MEDIO_PAGO)));
		        resBD.getDatosPagoBD().setHashDatos(Varios.normalizeNull(resultadoConsulta.get(Datos.C_HASH_DATOS)));
		        resBD.getDatosPagoBD().setJustificante(Varios.normalizeNull(resultadoConsulta.get(Datos.C_JUSTIFICANTE_PETICION_PATE)));
		        //También comprobaremos si han cambiado los datos.
		        String datosConsistentes=Varios.normalizeNull(resultadoConsulta.get(Datos.C_DATOS_CONSISTENTES));
		        resBD.setDatosConsistentes("1".equals(datosConsistentes)); //Para saber si los datos de pago han cambiado respecto a lo almacenado
		      }
		      return resBD;
		    } catch (Exception e) {
		      throw new PasarelaPagoException("Error en la consulta en pate:" + e.getMessage(), e);
		    }
			}
	// CRUBENCVS 43788. 18/11/2021. Aprovecho internamente la funcionalidad 
	//de actualizarPate para modificar el estado de "Solicitud Token" a  "Iniciado"
	//Es un poco artificial el tener que indicar la entidad, pero no
	//hay más remedio si quiero reutilizar
	public final ResultadoLlamadaBD cambiarEstadoAIniciado(String entidad, String justificante, String identificacion, String referencia) throws PasarelaPagoException
	{
		ResultadoLlamadaBD resBD = new ResultadoLlamadaBD();
		Datos dat = new Datos(this.idSesion);
		String ESTADO_INICIADO="I";
		try
		{
			dat.actualizarPate(entidad,justificante,identificacion,referencia, ESTADO_INICIADO, "", "","","","","", "", "", "", "","");
			String errorLlamada = dat.getErrorLlamada();
			if (errorLlamada!=null && !"".equals(errorLlamada))
			{
				resBD.setError(true);
				resBD.setCodError(Mensajes.getErrorActualizacionBD());
				resBD.setTextoError(Mensajes.getExternalText(resBD.getCodError()));
			}
			else
			{
				//Comprobamos el resultado de la consulta.
				checkErrorLlamadaBD(dat, resBD, Mensajes.getErrorActualizacionBD());
			}
			return resBD;
		}
		catch (Exception e)
		{
			throw new PasarelaPagoException ("Error al actualizar el registro a estado \"Iniciado\":"+ e.getMessage(),e); 
		}
	}
	/**
	 * Consulta de pago por tarjeta contra la nueva plataforma de pago
	 * @param origen
	 * @param emisora
	 * @param justificante
	 * @param identificacion
	 * @param referencia
	 * @param aplicacion
	 * @param numeroUnico
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final ResultadoConsultaPagoTarjetaBD consultaPagoTarjeta(
																    String origen,
																    String emisora,
																    String justificante,
																    String identificacion,
																    String referencia,
																    String aplicacion,
																    String numeroUnico
									) throws PasarelaPagoException
	{
		ResultadoConsultaPagoTarjetaBD resBD;
		Datos dat = new Datos(this.idSesion);
		try
		{
			resBD=dat.consultaPagoTarjeta(origen, 
										  emisora, 
										  justificante, 
										  identificacion, 
										  referencia, 
										  aplicacion, 
										  numeroUnico);
			
			return resBD;
		}
		catch (Exception e)
		{
			throw new PasarelaPagoException ("Error al consultar el pago en base de datos:"+ e.getMessage(),e); 
		}
	}
	/**
	 * Cambio de estado del registro a pagado
	 * @param entidad         Código de entidad en la que se ha pagado (0182, 3059,...)
	 * @param justificante    Número de autoliquidación
	 * @param identificacion  Identificación
	 * @param referencia      Referencia
	 * @param operacionEpst   Identificador de operación de plataforma de pago
	 * @param fechaPago       Fecha de pago, en formato YYYY-MM-DD
	 * @param nrc             NRC
	 * @param numeroOperacion Número de operación
	 * @param resultado       Resultado
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final ResultadoLlamadaBD cambiarEstadoAPagado(String entidad, 
														 String justificante, 
														 String identificacion, 
														 String referencia,
														 String operacionEpst,
														 String fechaPago,
														 String nrc,
														 String numeroOperacion,
														 String resultado) throws PasarelaPagoException
	{
		ResultadoLlamadaBD resBD = new ResultadoLlamadaBD();
		Datos dat = new Datos(this.idSesion);
		try
		{
			dat.actualizarPate(entidad,                        //pasarela
							   justificante,                   //justificante
							   identificacion,                 //identificacion
							   referencia,                     //referencia
							   EstadosPago.PAGADO.getValor(),  //estado
							   resultado,                      //resultado
							   nrc,                            //nrc
							   "",                             //tarjeta
							   "",                             //fechaCaducidad
							   "",                             //ccc
							   "",                             //numOperacion
							   fechaPago,                      //fechaOperacion 
							   "",                             //operante
							   "",                             //fechaDevengo
							   "",                             //fechaAnulacion
							   operacionEpst                   //operacionEpst
							   );
			String errorLlamada = dat.getErrorLlamada();
			if (errorLlamada!=null && !"".equals(errorLlamada))
			{
				resBD.setError(true);
				resBD.setCodError(Mensajes.getErrorActualizacionBD());
				resBD.setTextoError(Mensajes.getExternalText(resBD.getCodError()));
			}
			else
			{
				//Comprobamos el resultado de la consulta.
				checkErrorLlamadaBD(dat, resBD, Mensajes.getErrorActualizacionBD());
			}
			return resBD;
		}
		catch (Exception e)
		{
			throw new PasarelaPagoException ("Error al actualizar el registro a estado \"Iniciado\":"+ e.getMessage(),e); 
		}
	}
	
	@Override
	public CallContext getCallContext() {
		return callContext;
	}
	@Override
	public void setCallContext(CallContext ctx) {
		callContext= ctx;
	}
}
