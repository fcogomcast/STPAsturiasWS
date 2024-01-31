package es.tributasenasturias.tarjetas.core.db;

import java.util.List;
import java.util.Map;

import es.tributasenasturias.tarjetas.core.PateRecord;
import es.tributasenasturias.tarjetas.core.Util;
import es.tributasenasturias.utils.ConversorParametrosLanzador;
/**
 * Acceso a datos de PATE, operaciones de más bajo nivel. Son consultas/modificaciones 
 * u otras operaciones sobre un registro.
 * @author crubencvs
 *
 */
public class PateDAO {

	private final DBConn conn;
	
	public PateDAO(DBConn conn){
		this.conn= conn;
	}
	/**
	 * Recupera un registro de pate que corresponda a la emisora + 
	 *                                                   justificante o
	 *                                                   identificación + referencia o
	 *                                                   aplicación + número único
	 *                                                   
	 * @param procAlmacenadoConsultaRegistro
	 * @param emisora
	 * @param justificante
	 * @param identificacion
	 * @param referencia
	 * @param aplicacion
	 * @param numeroUnico
	 * @return {@link PateRecord} con el registro recuperado o null si no hay datos
	 * @throws DAOException En caso de producirse un error
	 */
	public PateRecord getPate(String procAlmacenadoConsultaRegistro,
							  String emisora, 
							  String justificante,
							  String identificacion,
							  String referencia,
							  String aplicacion,
						      String numeroUnico) throws DAOException{
		try {
			ConversorParametrosLanzador conversor= new ConversorParametrosLanzador();
			conversor.setProcedimientoAlmacenado(procAlmacenadoConsultaRegistro);
			conversor.setParametro("1", ConversorParametrosLanzador.TIPOS.Integer);
			conversor.setParametro("1", ConversorParametrosLanzador.TIPOS.Integer);
			conversor.setParametro("USU_WEB_SAC", ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro("33", ConversorParametrosLanzador.TIPOS.Integer);
			conversor.setParametro(emisora, ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro(justificante, ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro(identificacion, ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro(referencia, ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro(aplicacion, ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro(numeroUnico, ConversorParametrosLanzador.TIPOS.String);
			conversor.setParametro("P", ConversorParametrosLanzador.TIPOS.String);
			
			Map<String, List<Map<String,String>>> result= conn.ejecutar(conversor.codifica());
			if (result.isEmpty()) {
				return null;
			}
			if (result.containsKey("ERROR")) {
				//Hay estructura de error, algo ha terminado mal.
				
				throw new DAOException(result.get("ERROR").get(0).get("ERROR"));
			}
			if (result.containsKey("ESUN_ESTRUCTURA_UNIVERSAL") && result.get("ESUN_ESTRUCTURA_UNIVERSAL").size()>0){
				Map<String,String> fila=result.get("ESUN_ESTRUCTURA_UNIVERSAL").get(0);
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
				String cToken				  = fila.get("C27");
				PateRecord pr = new PateRecord(cNumOper,
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
											   "", // Xml servicio sólo interesa en actualización
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
				pr.setToken(cToken);
				return pr;
			} else {
				return null;
			}
		} 
		catch (Exception e){
			throw new DAOException("Error al recuperar registro de PATE:"+e.getMessage(),e);
		}
		
	}
	

	
	
	
}


