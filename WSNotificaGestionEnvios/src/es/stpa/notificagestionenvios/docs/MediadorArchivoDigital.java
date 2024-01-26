package es.stpa.notificagestionenvios.docs;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.Handler;



import es.stpa.notificagestionenvios.exceptions.ArchivoDigitalException;
import es.stpa.notificagestionenvios.preferencias.Preferencias;
import es.stpa.notificagestionenvios.preferencias.PreferenciasException;
import es.stpa.notificagestionenvios.soap.SoapClientHandler;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.ArchivoDigital;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.ArchivoDigital_Service;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.CSVType;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.CertificadoType;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.FirmaType;


/**
 * Permite la invocación de operaciones del Archivo Digital
 * 
 * @author crubencvs
 * 
 */
public class MediadorArchivoDigital {
	
	private String idLlamada;
	public MediadorArchivoDigital(String idLlamada){
		this.idLlamada= idLlamada;
	}
	
	
	public static class ResultadoCustodia {
		private boolean error;
		private String mensaje;
		private int idDocumento;
		private String csv;
		public String getCsv() {
			return csv;
		}
		public void setCsv(String csv) {
			this.csv = csv;
		}
		public boolean isError() {
			return error;
		}
		public void setEsError(boolean esError) {
			this.error = esError;
		}
		public String getMensaje() {
			return mensaje;
		}
		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}
		public int getIdDocumento() {
			return idDocumento;
		}
		public void setIdDocumento(int idDocumento) {
			this.idDocumento = idDocumento;
		}
		
	}
	
	
	
	/**
	 * Recupera el contenido de un documento del archivo digital
	 * 
	 * @param idAdar
	 *            Identificador de custodia en el archivo digital
	 * @param pref
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public byte[] obtenerDocumentoPorId(String idAdar)
			throws ArchivoDigitalException {
		Preferencias pref;
		try {
			pref = new Preferencias();
		} catch (PreferenciasException p){
			throw new ArchivoDigitalException("Error en preferencias al obtener documento custodiado:"+p.getMessage());
		}
		ArchivoDigital_Service srv = new ArchivoDigital_Service();
		ArchivoDigital port = srv.getArchivoDigitalSOAP();
		javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) port;
		bpr.getRequestContext().put(
				javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				pref.getEndpointArchivoDigital());
		Binding bi = bpr.getBinding();
		
		List<Handler> handlerList = bi.getHandlerChain();
		if(handlerList == null){
			handlerList = new ArrayList<Handler>();
			bi.setHandlerChain(handlerList);
		}
		
		handlerList.add(new SoapClientHandler(idLlamada));
		
		String obtenerSoloDatosArchivo = "N";
		Holder<byte[]> contenidoArchivo = new Holder<byte[]>(); 
		Holder<String> datosArchivo = new Holder<String>();
		Holder<String> error = new Holder<String>();
		port.obtieneArchivoPorId("USU_WEB_SAC", Integer.parseInt(idAdar),
				obtenerSoloDatosArchivo, contenidoArchivo, datosArchivo, error);
		if (error != null && error.value != null && !"".equals(error.value) ) {
			throw new ArchivoDigitalException(
					"Error al recuperar el fichero del archivo digital:"
							+ error.value);
		}
		if (contenidoArchivo == null || contenidoArchivo.value==null) {
			throw new ArchivoDigitalException(
					"Error al recuperar el fichero del archivo digital, no hay contenido");
		}
		return contenidoArchivo.value;
	}
	
	/**
	 * Envía un documento a custodia.
	 * @param codUsuario
	 * @param identificador
	 * @param tipoElemento
	 * @param nombreFichero
	 * @param comprimido
	 * @param contenido
	 * @param hashArchivo
	 * @param metadatos
	 * @return Resultado de la custodia, como objeto interno
	 */
	@SuppressWarnings("unchecked")
	public ResultadoCustodia custodiar(
				String codUsuario, 
				String identificador, 
				String tipoElemento, 
				String nombreFichero,
				String comprimido, 
				byte[] contenido, 
				String hashArchivo, 
				String metadatos, 
				String firmaExterna,
				String firmaCSV, 
				String firmante, 
				String firmaCertificado, 
				String ltv
				) throws ArchivoDigitalException{
		
		Preferencias pref;
		try {
			pref = new Preferencias();
		} catch (PreferenciasException p){
			throw new ArchivoDigitalException("Error en preferencias al obtener documento custodiado:"+p.getMessage());
		}
		ArchivoDigital_Service srv = new ArchivoDigital_Service();
		ArchivoDigital port = srv.getArchivoDigitalSOAP();
		javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) port;
		bpr.getRequestContext().put(
				javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				pref.getEndpointArchivoDigital());
		Binding bi = bpr.getBinding();
		
		List<Handler> handlerList = bi.getHandlerChain();
		if(handlerList == null){
			handlerList = new ArrayList<Handler>();
			bi.setHandlerChain(handlerList);
		}
		
		handlerList.add(new SoapClientHandler(idLlamada));
		Holder<String> error= new Holder<String>();
		Holder<Integer> idArchivo= new Holder<Integer>();
		Holder<String> csv= new Holder<String>();
		Holder<String> hash= new Holder<String>();
		ResultadoCustodia resul= new ResultadoCustodia();
		FirmaType firmaType=null;
		if ("S".equalsIgnoreCase(firmaCSV) ||  "S".equalsIgnoreCase(firmaCertificado) || "S".equalsIgnoreCase(firmaExterna)) {
			firmaType= new FirmaType();
			if ("S".equalsIgnoreCase(firmaCSV)) {
				CSVType csvType= new CSVType();
				csvType.setFirmaCSV(firmaCSV);
				csvType.setFirmante(firmante);
				firmaType.setCSV(csvType);
			} 
			if ("S".equalsIgnoreCase(firmaCertificado) || "S".equalsIgnoreCase(firmaExterna)) {
				CertificadoType certificadoType= new CertificadoType();
				certificadoType.setFirmaCertificado(firmaCertificado);
				certificadoType.setLTV(ltv);
				firmaType.setFirmaExterna(firmaExterna);
				firmaType.setCertificado(certificadoType);
			}
		}
		
		port.custodia(codUsuario, 
					  identificador, 
					  tipoElemento, 
					  nombreFichero, 
					  comprimido, 
					  contenido, 
					  hashArchivo, 
					  metadatos, 
					  firmaType,
					  idArchivo, 
					  csv, 
					  hash,
					  error);
		//Condiciones de error.
		if (idArchivo.value==0) {
			resul.setEsError(true);
			resul.setMensaje(error.value);
		} else {
			resul.setEsError(false);
			resul.setIdDocumento(idArchivo.value.intValue());
			resul.setCsv(csv.value);
		}
		return resul;
	}
	

}
