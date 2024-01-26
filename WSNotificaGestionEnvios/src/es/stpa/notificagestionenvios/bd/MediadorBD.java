package es.stpa.notificagestionenvios.bd;

import java.text.SimpleDateFormat;
import java.util.Date;

import es.stpa.notificagestionenvios.exceptions.BDException;
import es.stpa.notificagestionenvios.preferencias.Preferencias;
import es.stpa.notificagestionenvios.soap.SoapClientHandler;
import es.tributasenasturias.log.ILog;
import es.tributasenasturias.services.lanzador.client.LanzadorFactory;
import es.tributasenasturias.services.lanzador.client.ParamType;
import es.tributasenasturias.services.lanzador.client.ProcedimientoAlmacenado;
import es.tributasenasturias.services.lanzador.client.TLanzador;
import es.tributasenasturias.services.lanzador.client.response.RespuestaLanzador;

public class MediadorBD {

	private Preferencias pref;
	private ILog log;
	private String idLlamada;
	public MediadorBD(Preferencias pref, ILog logger, String idLlamada){
		this.pref= pref;
		this.log= logger;
		this.idLlamada= idLlamada;
	}
		
	/** Respuesta de las operaciones de base de datos
	 * 
	 * @author crubencvs
	 *
	 */
	private static class OperacionBDRespuesta{
		private String codigo;
		private String mensaje;
		public String getCodigo() {
			return codigo;
		}
		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}
		public String getMensaje() {
			return mensaje;
		}
		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}
	}
	/** 
	 * Llama a base de datos para ejecutar la funcionalidad de datado por notificación
	 * @param organismo
	 * @param identificadorEnvio
	 * @param fecha
	 * @param estado
	 * @throws BDException
	 * @return {@link OperacionRespuesta} con la respuesta de la operación
	 */
	public OperacionBDRespuesta datadoNotifica(
			String organismo,
			String identificadorEnvio,
			Date fecha,
			String estado
			) throws BDException{
		final String ESUN="ESUN_ESTRUCTURA_UNIVERSAL";
		OperacionBDRespuesta datadoRespuesta= new OperacionBDRespuesta();
		try {
			String txtFecha="";
			SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
			txtFecha= sdf.format(fecha);
			log.info("Llamada a "+pref.getProcDatado());
			TLanzador lanzador= LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(this.idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado(pref.getProcDatado(), pref.getEsquemaBD());
			//Cabecera
			proc.param("1", ParamType.NUMERO);
			proc.param("1", ParamType.NUMERO);
			proc.param("USU_WEB_SAC", ParamType.CADENA);
			proc.param("33", ParamType.NUMERO);
			proc.param(organismo, ParamType.CADENA);
			proc.param(identificadorEnvio, ParamType.CADENA);
			proc.param(estado, ParamType.CADENA);
			proc.param(txtFecha, ParamType.FECHA,"DD/MM/YYYY HH24:MI:SS");
			proc.param("NOTIFICA", ParamType.CADENA);
			proc.param("P", ParamType.CADENA);
						
			String soapResponse= lanzador.ejecutar(proc);
			log.info("Teminada llamada a "+pref.getProcDatado());
			RespuestaLanzador response= new RespuestaLanzador(soapResponse);
			if (!response.esErronea()){
				int filas= response.getNumFilasEstructura(ESUN);
				//En principio, esperamos una
				if (filas>0){
					//Si hay más de una, las restantes las ignoramos.
					datadoRespuesta.setCodigo(response.getValue(ESUN, 1, "C1"));
					datadoRespuesta.setMensaje(response.getValue(ESUN, 1, "C2"));
				}

			} else {
				datadoRespuesta.setCodigo("000");
				datadoRespuesta.setMensaje("Error en ejecución de procedimiento almacenado");
			}
		} catch (Exception e){ 
			throw new BDException("Error en " + this.getClass().getName()+".datadoNotifica:"+e.getMessage(),e);
		}
		return datadoRespuesta;
	}
	
	/**
	 * Operativa de recepción de certificación por parte de Notific@. Custodia el archivo y registra en base de datos la recepción.
	 * @param identificadorEnvio
	 * @param contenido
	 * @param hash
	 * @return
	 */
	public OperacionBDRespuesta certificacionNotifica(
			String identificadorEnvio,
			String idAdar,
			byte[] contenido,
			String hash
			) throws BDException{
		final String ESUN="ESUN_ESTRUCTURA_UNIVERSAL";
		OperacionBDRespuesta datadoRespuesta= new OperacionBDRespuesta();
		try {
			log.info("Llamada a "+pref.getProcCertificacion());
			TLanzador lanzador= LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(this.idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado(pref.getProcCertificacion(), pref.getEsquemaBD());
			//Cabecera
			proc.param("1", ParamType.NUMERO);
			proc.param("1", ParamType.NUMERO);
			proc.param("USU_WEB_SAC", ParamType.CADENA);
			proc.param("33", ParamType.NUMERO);
			proc.param(identificadorEnvio, ParamType.CADENA);
			proc.param(idAdar, ParamType.NUMERO);
			proc.param("NOTIFICA", ParamType.CADENA);
			proc.param("P", ParamType.CADENA);
						
			String soapResponse= lanzador.ejecutar(proc);
			log.info("Terminada llamada a "+pref.getProcCertificacion());
			RespuestaLanzador response= new RespuestaLanzador(soapResponse);
			if (!response.esErronea()){
				int filas= response.getNumFilasEstructura(ESUN);
				//En principio, esperamos una
				if (filas>0){
					//Si hay más de una, las restantes las ignoramos.
					datadoRespuesta.setCodigo(response.getValue(ESUN, 1, "C1"));
					datadoRespuesta.setMensaje(response.getValue(ESUN, 1, "C2"));
				}

			} else {
				datadoRespuesta.setCodigo("000");
				datadoRespuesta.setMensaje("Error en ejecución de procedimiento almacenado");
			}
		} catch (Exception e){ 
			throw new BDException("Error en " +this.getClass().getName()+".certificacionNotifica:"+e.getMessage(),e);
		}
		return datadoRespuesta;
	}
}
