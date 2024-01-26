package es.stpa.notificagestionenvios;

import es.stpa.notificagestionenvios.bd.MediadorBD;
import es.stpa.notificagestionenvios.docs.MediadorArchivoDigital;
import es.stpa.notificagestionenvios.docs.MediadorArchivoDigital.ResultadoCustodia;
import es.stpa.notificagestionenvios.exceptions.ActualizarRespuestaException;
import es.stpa.notificagestionenvios.exceptions.ArchivoDigitalException;
import es.stpa.notificagestionenvios.exceptions.BDException;
import es.stpa.notificagestionenvios.preferencias.Preferencias;
import es.tributasenasturias.log.ILog;

/**
 * Implementación de la operación que permite actualizar la respuesta de un envío
 * @author crubencvs
 *
 */
public class ActualizarRespuestaImpl {

	private Preferencias pref;
	private String idLlamada;
	private ILog log;
	
	public ActualizarRespuestaImpl(Preferencias pref, ILog logger, String idLlamada){
		this.pref= pref;
		this.idLlamada= idLlamada;
		this.log= logger;
	}
	/**
	 * Clase de resultado de la operación
	 * @author crubencvs
	 *
	 */
	public static class ResultadoActualizarRespuesta{
		private String codigoRespuesta;
		private String descripcionRespuesta;
		public String getCodigoRespuesta() {
			return codigoRespuesta;
		}
		public String getDescripcionRespuesta() {
			return descripcionRespuesta;
		}
		
	}
	
	
	
	public ResultadoActualizarRespuesta actualizarRespuesta(String identificadorEnvio) throws ActualizarRespuestaException{
		ResultadoActualizarRespuesta res= new ResultadoActualizarRespuesta();
		log.info("Se realiza la llamada a Notifica");
		MediadorNotifica mn= new MediadorNotifica(pref, log, idLlamada);
		MediadorNotifica.RespuestaConsulta resConsulta=mn.consultarEnvio(identificadorEnvio);
		if (!resConsulta.isError()){
			MediadorNotifica.RespuestaConsulta.Datado datado= resConsulta.getDatado();
			MediadorBD bd= new MediadorBD(pref, log, idLlamada);
			//Dado que, a diferencia del adviser, no podemos conocer el tipo de entrega,
			//si nos llega datado lo grabamos, y si nos llega certificación, también.
			if (datado!=null){
				log.debug("Hay respuesta de DATADO. Se comunica a base de datos");
				try {
					bd.datadoNotifica(datado.getOrganismoEmisor(), identificadorEnvio, datado.getFecha(), datado.getResultado());
				} catch (BDException bde){
					log.error("Error en comunicación del datado a base de datos:"+ bde.getMessage(), bde);
					throw new ActualizarRespuestaException("Error en comunicación del datado a base de datos:"+ bde.getMessage(), bde);
				}
				log.debug("Comunicado a base de datos el resultado de DATADO.");
			}
			MediadorNotifica.RespuestaConsulta.Certificacion certificacion= resConsulta.getCertificacion();
			if (certificacion!=null){
				log.debug("Hay respuesta de CERTIFICACION. Se almacena en Archivo Digital y se comunica a base de datos");
				try {
					log.debug("Almacenamos el fichero en archivo digital");
					MediadorArchivoDigital archivo= new MediadorArchivoDigital(idLlamada);
					ResultadoCustodia rCustodia= archivo.custodiar(
													  "USU_WEB_SAC", 
												      identificadorEnvio, 
												      "REES", 
												      identificadorEnvio+".PDF", 
												      "N", 
												      certificacion.getContenido(), 
												      certificacion.getHash(), 
												      "", 
												      "N", 
												      "N", 
												      "N", 
												      "N", 
												      "N");
					if (!rCustodia.isError()){
						bd.certificacionNotifica(identificadorEnvio, String.valueOf(rCustodia.getIdDocumento()), certificacion.getContenido(), certificacion.getHash());
					} else {
						log.error ("Error en custodia de archivo:" + rCustodia.getMensaje());
						throw new ArchivoDigitalException("Error en la custodia, mensaje:"+ rCustodia.getMensaje());
					}
				} catch (BDException bde){
					log.error("Error en comunicación de la certificación a base de datos:"+bde.getMessage(),bde);
					throw new ActualizarRespuestaException("Error en comunicación de la certificación a base de datos:"+ bde.getMessage(), bde);
				} catch (ArchivoDigitalException ad){
					log.error("Error al custodiar el fichero de certificación:"+ad.getMessage(),ad);
					throw new ActualizarRespuestaException("Error al custodiar el fichero de certificación:"+ ad.getMessage(), ad);
				}
			}
			res.codigoRespuesta= "0000";
			if (datado!=null || certificacion!=null){
				res.descripcionRespuesta= "Actualización completada";
			} else {
				res.descripcionRespuesta= "No hay nada que actualizar";
			}
		} else {
			res.codigoRespuesta= resConsulta.getCodRespuesta();
			res.descripcionRespuesta= resConsulta.getDescripcionRespuesta();
		}
		return res;
	}
}
