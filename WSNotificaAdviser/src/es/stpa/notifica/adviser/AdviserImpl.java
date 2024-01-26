package es.stpa.notifica.adviser;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;


import es.stpa.notifica.adviser.archivodigital.MediadorArchivoDigital;
import es.stpa.notifica.adviser.archivodigital.MediadorArchivoDigital.ResultadoCustodia;
import es.stpa.notifica.adviser.exceptions.AdviserException;
import es.stpa.notifica.adviser.preferencias.Preferencias;
import es.stpa.notifica.adviser.soap.SoapClientHandler;
import es.tributasenasturias.log.ILog;
import es.tributasenasturias.services.lanzador.client.LanzadorFactory;
import es.tributasenasturias.services.lanzador.client.ParamType;
import es.tributasenasturias.services.lanzador.client.ProcedimientoAlmacenado;
import es.tributasenasturias.services.lanzador.client.TLanzador;
import es.tributasenasturias.services.lanzador.client.response.RespuestaLanzador;

/**
 * Implementación de operaciones del Adviser
 * @author crubencvs
 *
 */
public class AdviserImpl {

	private Preferencias pref;
	private ILog log;
	private String idLlamada;
	
	public AdviserImpl(Preferencias pref, ILog log, String idLlamada){
		this.pref= pref;
		this.log= log;
		this.idLlamada=idLlamada;
	}
	/** Respuesta de las operaciones
	 * 
	 * @author crubencvs
	 *
	 */
	private static class OperacionRespuesta{
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
	 * Respuesta de la operación de envío sincronizable
	 * @author crubencvs
	 *
	 */
	private static class envioSincronizableResponse {
		private String codigo;
		private String descripcion;
		private String codigoNotifica;
		private String mensajeNotifica;
		public String getCodigo() {
			return codigo;
		}
		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}
		public String getDescripcion() {
			return descripcion;
		}
		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}
		public String getCodigoNotifica() {
			return codigoNotifica;
		}
		public void setCodigoNotifica(String codigoNotifica) {
			this.codigoNotifica = codigoNotifica;
		}
		public String getMensajeNotifica() {
			return mensajeNotifica;
		}
		public void setMensajeNotifica(String mensajeNotifica) {
			this.mensajeNotifica = mensajeNotifica;
		}
	}
	
	/**
	 * Sincronización del envío
	 * @param organismoEmisor
	 * @param identificador
	 * @param tipoEntrega
	 * @param modoNotificacion
	 * @param estado
	 * @param fechaEstado
	 * @param receptor
	 * @param acusePDF
	 * @param acuseXML
	 * @param opcionesSincronizarEnvio
	 * @param codigoRespuesta
	 * @param descripcionRespuesta
	 * @param opcionesResultadoSincronizarEnvio
	 * @throws AdviserException
	 */
	public void sincronizarEnvio(
			String organismoEmisor,			
			Holder<String> identificador, 
			BigInteger tipoEntrega,
			BigInteger modoNotificacion, 
			String estado,
			XMLGregorianCalendar fechaEstado, 
			Receptor receptor,
			Acuse acusePDF, 
			Acuse acuseXML, 
			Opciones opcionesSincronizarEnvio,
			Holder<String> codigoRespuesta,
			Holder<String> descripcionRespuesta,
			Holder<Opciones> opcionesResultadoSincronizarEnvio) throws AdviserException{
		
		OperacionRespuesta r= new OperacionRespuesta(); //Para que no sea nulo, no sea que no entre por ninguno.
		if (tipoEntrega==null || modoNotificacion==null){
			throw new AdviserException("Error, el tipo de entrega o el modo de notificación llegan vacíos");
		}
		//CRUBENCVS 45773 26/08/2022. Comprobamos si el envío es sincronizable. 
		//Si no lo es, pues ya no seguimos.
		envioSincronizableResponse sincronizable= envioSincronizable(identificador.value);
		if (!"000".equals(sincronizable.getCodigo())){
			log.info ("Se ha detectado que el envío no es sincronizable debido a:" + sincronizable.getDescripcion());
			r.setCodigo(sincronizable.getCodigoNotifica());
			r.setMensaje(sincronizable.getMensajeNotifica());
		} else {
			if (tipoEntrega.intValue()==1) {//Datado
				log.debug("Tipo Entrega=1. Tratamiento de datado de Notific@");
				r= datadoNotifica(organismoEmisor, 
								  identificador.value, 
								  fechaEstado.toGregorianCalendar().getTime(),
								  estado);
			} else if (tipoEntrega.intValue()==2){
				if (modoNotificacion.intValue()==5){
					//Se trata como datado notificado
					//Si termina correctamente, se procesa además la certificación
					log.debug("Tipo Entrega=2, Modo Notificación= 5. Tratamiento de datado de Notific@. Luego se intentará el tratamiento de certificación");
					r= datadoNotifica(organismoEmisor, 
							  identificador.value, 
							  fechaEstado.toGregorianCalendar().getTime(),
							  estado);
					if (!"000".equals(r.getCodigo())){
						log.debug("Tipo Entrega=2, Modo Notificación= 5. El código de error ha sido " + r.getCodigo() +" .Paramos");
						throw new AdviserException("Error en ejecución de datado para tipo de entrega= 2, modo de notificación=5:"+ r.getCodigo());
					} 
				}
				log.debug("Tipo Entrega=2. Tratamiento de certificación de Notific@. ");
				r= certificacionNotifica(identificador.value, 
						 acusePDF.getContenido(), 
						 acusePDF.getHash());
			} else if (tipoEntrega.intValue()==3){
				log.debug("Tipo Entrega= 3. Tratamiento de certificación de Notific@");
				r= certificacionNotifica(identificador.value, 
						 acusePDF.getContenido(), 
						 acusePDF.getHash());
			} else {
				throw new AdviserException("Error, el tipo de entrega tiene un valor no esperado (1,2 o 3)");
			}
		}	
		codigoRespuesta.value=r.getCodigo();
		descripcionRespuesta.value=r.getMensaje();
		log.debug("Respuesta: " + codigoRespuesta.value + ":" + descripcionRespuesta.value);
		
		
	}
	/** 
	 * Llama a base de datos para ejecutar la funcionalidad de datado por notificación
	 * @param organismo
	 * @param identificadorEnvio
	 * @param fecha
	 * @param estado
	 * @throws AdviserException
	 * @return {@link OperacionRespuesta} con la respuesta de la operación
	 */
	private OperacionRespuesta datadoNotifica(
			String organismo,
			String identificadorEnvio,
			Date fecha,
			String estado
			) throws AdviserException{
		final String ESUN="ESUN_ESTRUCTURA_UNIVERSAL";
		OperacionRespuesta datadoRespuesta= new OperacionRespuesta();
		try {
			String txtFecha="";
			SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
			txtFecha= sdf.format(fecha);
			log.info("Llamada a "+pref.getProcAlmacenadoDatadoNotificacion());
			TLanzador lanzador= LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(this.idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado(pref.getProcAlmacenadoDatadoNotificacion(), pref.getEsquemaBD());
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
			log.info("Terminada llamada a "+pref.getProcAlmacenadoDatadoNotificacion());
			RespuestaLanzador response= new RespuestaLanzador(soapResponse);
			if (!response.esErronea()){
				int filas= response.getNumFilasEstructura(ESUN);
				//En principio, esperamos una
				if (filas>0){
					//Si hay más de una, las restantes las ignoramos.
					datadoRespuesta.setCodigo(response.getValue(ESUN, 1, "C1"));
					datadoRespuesta.setMensaje(response.getValue(ESUN, 1, "C2"));
				} else {
					//CRUBENCVS 45773
					throw new AdviserException ("Error, no se han recibido los datos necesarios al ejecutar "+proc.getPeticion().getProcName());
				}

			} else {
				datadoRespuesta.setCodigo("000");
				datadoRespuesta.setMensaje("Error en ejecución de procedimiento almacenado");
			}
		} catch (Exception e){ 
			throw new AdviserException("Error en " + AdviserImpl.class.getName()+".datadoNotifica:"+e.getMessage(),e);
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
	private OperacionRespuesta certificacionNotifica(
			String identificadorEnvio,
			byte[] contenido,
			String hash
			) throws AdviserException{
		final String ESUN="ESUN_ESTRUCTURA_UNIVERSAL";
		OperacionRespuesta datadoRespuesta= new OperacionRespuesta();
		try {
			//Protegemos el documento en Archivo digital, para posteriormente enviar su id_adar 
			//a la base de datos
			MediadorArchivoDigital archivo= MediadorArchivoDigital.newInstance(this.pref, this.idLlamada);
			//FIXME: 
			ResultadoCustodia resCustodia= archivo.custodiar(
					         "USU_WEB_SAC",
							  identificadorEnvio, 
							  "REES", 
							  identificadorEnvio+".PDF", 
							  "N", 
							  contenido, 
							  hash, 
							  "", 
							  "N", 
							  "N", 
							  "N",
							  "N", 
							  "N");
			if (resCustodia.isError()){
				//Entiendo que lo mismo que si hay excepción. Decimos que OK a todo
				datadoRespuesta.setCodigo("000");
				datadoRespuesta.setMensaje("Error en custodia de archivo");
				log.error("Error en custodia de archivo");
			} else {
				log.info("Llamada a "+pref.getProcAlmacenadoCertificacionNotifica());
				TLanzador lanzador= LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(this.idLlamada));
				ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado(pref.getProcAlmacenadoCertificacionNotifica(), pref.getEsquemaBD());
				//Cabecera
				proc.param("1", ParamType.NUMERO);
				proc.param("1", ParamType.NUMERO);
				proc.param("USU_WEB_SAC", ParamType.CADENA);
				proc.param("33", ParamType.NUMERO);
				proc.param(identificadorEnvio, ParamType.CADENA);
				proc.param(String.valueOf(resCustodia.getIdDocumento()), ParamType.NUMERO);
				proc.param("NOTIFICA", ParamType.CADENA);
				proc.param("P", ParamType.CADENA);
							
				String soapResponse= lanzador.ejecutar(proc);
				log.info("Terminada llamada a "+pref.getProcAlmacenadoCertificacionNotifica());
				RespuestaLanzador response= new RespuestaLanzador(soapResponse);
				if (!response.esErronea()){
					int filas= response.getNumFilasEstructura(ESUN);
					//En principio, esperamos una
					if (filas>0){
						//Si hay más de una, las restantes las ignoramos.
						datadoRespuesta.setCodigo(response.getValue(ESUN, 1, "C1"));
						datadoRespuesta.setMensaje(response.getValue(ESUN, 1, "C2"));
					} else {
						//CRUBENCVS 45773
						throw new AdviserException ("Error, no se han recibido los datos necesarios al ejecutar "+proc.getPeticion().getProcName());
					}
	
				} else {
					datadoRespuesta.setCodigo("000");
					datadoRespuesta.setMensaje("Error en ejecución de procedimiento almacenado");
				}
			}
		} catch (Exception e){ //FIXME: ¿Todas las excepciones las dan por buenas?
			throw new AdviserException("Error en " + AdviserImpl.class.getName()+".certificacionNotifica:"+e.getMessage(),e);
		}
		return datadoRespuesta;
	}
	
	//CRUBENCVS 45773 26/08/2022. Recupero si el envío se puede sincronizar,
	//según su estado en base de datos
	private envioSincronizableResponse envioSincronizable(
			String identificadorEnvio
			) throws AdviserException{
		final String ESUN="ESUN_ESTRUCTURA_UNIVERSAL";
		envioSincronizableResponse respuesta= new envioSincronizableResponse();
		try {
			log.info("Llamada a "+pref.getProcAlmacenadoEnvioSusceptibleSincronizacion());
			TLanzador lanzador= LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(this.idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado(pref.getProcAlmacenadoEnvioSusceptibleSincronizacion(), pref.getEsquemaBD());
			//Cabecera
			proc.param("1", ParamType.NUMERO);
			proc.param("1", ParamType.NUMERO);
			proc.param("USU_WEB_SAC", ParamType.CADENA);
			proc.param("33", ParamType.NUMERO);
			proc.param(identificadorEnvio, ParamType.CADENA);
			proc.param("NOTIFICA", ParamType.CADENA);
			proc.param("P", ParamType.CADENA);
						
			String soapResponse= lanzador.ejecutar(proc);
			log.info("Terminada llamada a "+proc.getPeticion().getProcName());
			RespuestaLanzador response= new RespuestaLanzador(soapResponse);
			if (!response.esErronea()){
				int filas= response.getNumFilasEstructura(ESUN);
				//En principio, esperamos una
				if (filas>0){
					//Si hay más de una, las restantes las ignoramos.
					respuesta.setCodigo(response.getValue(ESUN, 1, "C1"));
					respuesta.setDescripcion(response.getValue(ESUN, 1, "C2"));
					respuesta.setCodigoNotifica(response.getValue(ESUN, 1, "C3"));
					respuesta.setMensajeNotifica(response.getValue(ESUN, 1, "C4"));
				} else {
					//CRUBENCVS 45773
					throw new AdviserException ("Error, no se han recibido los datos necesarios al ejecutar "+proc.getPeticion().getProcName());
				}

			} else {
				throw new AdviserException("Error en ejecución de procedimiento almacenado en "+ AdviserImpl.class.getName()+".envioSincronizable:"+ response.getTextoError());
			}
		} catch (Exception e){ 
			throw new AdviserException("Error en " + AdviserImpl.class.getName()+".envioSincronizable:"+e.getMessage(),e);
		}
		return respuesta;
	}
}
