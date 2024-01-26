package es.stpa.notificagestionenvios;

import javax.xml.datatype.XMLGregorianCalendar;

import es.stpa.notificagestionenvios.MediadorNotifica.RespuestaSincronizacion;
import es.stpa.notificagestionenvios.docs.MediadorArchivoDigital;
import es.stpa.notificagestionenvios.exceptions.SincronizacionEnvioException;
import es.stpa.notificagestionenvios.preferencias.Preferencias;
import es.tributasenasturias.log.ILog;

public class SincronizaEnvioImpl {
	private Preferencias pref;
	private String idLlamada;
	private ILog log;
	
	public SincronizaEnvioImpl(Preferencias pref, ILog logger, String idLlamada){
		this.log=logger;
		this.pref= pref;
		this.idLlamada= idLlamada;
	}
	
	public static class RespuestaSincronizaEnvio{
		private String codigoRespuesta;
		private String descripcionRespuesta;

		public String getCodigoRespuesta(){
			return codigoRespuesta;
		}
		public void setCodigoRespuesta(String codigoRespuesta) {
			this.codigoRespuesta = codigoRespuesta;
		}
		public String getDescripcionRespuesta() {
			return descripcionRespuesta;
		}
		public void setDescripcionRespuesta(String descripcionRespuesta) {
			this.descripcionRespuesta = descripcionRespuesta;
		}
		
	}
	public RespuestaSincronizaEnvio sincronizarEnvio(
			String organismoEmisor, 
			String identificador,
			int tipoEntrega, 
			int modoNotificacion, 
			String estado,
			XMLGregorianCalendar fechaEstado, 
			String nifReceptor,
			String nombreReceptor, 
			int vinculoReceptor, 
			int idArchivoAcuse) throws SincronizacionEnvioException{
		RespuestaSincronizaEnvio respuesta= new RespuestaSincronizaEnvio();
		log.info("Se recupera el documento de acuse");
		byte[] pdf= recuperaDocumentoAcuse(idArchivoAcuse);
		log.info("Se realiza la llamada a Notifica");
		MediadorNotifica mn= new MediadorNotifica(pref, log, idLlamada);
		RespuestaSincronizacion r = mn.sincronizarEnvio(organismoEmisor, 
														identificador, 
														tipoEntrega, 
														modoNotificacion, 
														estado, 
														fechaEstado, 
														nifReceptor, 
														nombreReceptor, 
														vinculoReceptor, 
														pdf);
		respuesta.setCodigoRespuesta(r.getCodRespuesta());
		respuesta.setDescripcionRespuesta(r.getDescripcionRespuesta());
		return respuesta;
	}
	
	

	/**
	 * Recupera el documento de acuse
	 * @param idAdar Identificador del documento de acuse.
	 * @return Contenido del pdf a enviar
	 * @throws AltaRemesaException
	 */
	private byte[] recuperaDocumentoAcuse(int idAdar) throws SincronizacionEnvioException{
		try {
			MediadorArchivoDigital md = new MediadorArchivoDigital(this.idLlamada);
			return md.obtenerDocumentoPorId(String.valueOf(idAdar));
		}catch (Exception e){
			throw new SincronizacionEnvioException("Error al recuperar el documento de acuse:"+e.getMessage(),e);
		}
		
	}
}
