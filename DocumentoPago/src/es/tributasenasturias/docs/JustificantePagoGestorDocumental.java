package es.tributasenasturias.docs;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.Holder;

import es.tributasenasturias.documentopagoutils.Logger;
import es.tributasenasturias.documentopagoutils.Preferencias;
import es.tributasenasturias.documentos.util.Base64;
import es.tributasenasturias.documentos.util.ConversorParametrosLanzador;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.ArchivoDigital;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.ArchivoDigital_Service;
import es.tributasenasturias.servicios.documentos.WSDocumentos;
import es.tributasenasturias.servicios.documentos.WSDocumentos_Service;
import es.tributasenasturias.soap.handler.HandlerUtil;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivo;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivoService;

public class JustificantePagoGestorDocumental {

	public static class DatosDoinJustificante {
		private String nombreDoin;
		private String hashDoin;
		private String nifSujetoPasivo;
		private String nifPresentador;
		public String getNombreDoin() {
			return nombreDoin;
		}
		public void setNombreDoin(String nombreDoin) {
			this.nombreDoin = nombreDoin;
		}
		public String getHashDoin() {
			return hashDoin;
		}
		public void setHashDoin(String hashDoin) {
			this.hashDoin = hashDoin;
		}
		public String getNifSujetoPasivo() {
			return nifSujetoPasivo;
		}
		public void setNifSujetoPasivo(String nifSujetoPasivo) {
			this.nifSujetoPasivo = nifSujetoPasivo;
		}
		public String getNifPresentador() {
			return nifPresentador;
		}
		public void setNifPresentador(String nifPresentador) {
			this.nifPresentador = nifPresentador;
		}
		
	}
	/**
	 * Impresión de justificante de pago de un recibo o liquidación.
	 * 
	 * @param id_eper.
	 *            Identificador del valor
	 * @return Cadena con el documento PDF de justificante de pago, o vacío si
	 *         no se ha podido recuperar.
	 * @throws Exception
	 */
	public static String imprimirJustificante(String idEper) throws Exception {
		ConversorParametrosLanzador conversor;
		Preferencias pref = new Preferencias();
		String pdf;
		try {
			//41113. Si el fichero está en archivo digital, se trae.
			String idAdar=getIdentificadorJustificanteEnArchivoDigital(idEper);
			if (!"".equals(idAdar)){
				pdf=recuperarJustificanteArchivoDigital(idAdar, pref);
			} else {
				conversor = new ConversorParametrosLanzador();
				conversor.setProcedimientoAlmacenado(pref.getPaJustificantePago());
				conversor.setParametro("1",ConversorParametrosLanzador.TIPOS.Integer);
		        // peticion
		        conversor.setParametro("1",ConversorParametrosLanzador.TIPOS.Integer);
		        // usuario
		        conversor.setParametro("USU_WEB_SAC",ConversorParametrosLanzador.TIPOS.String);
		        // organismo
		        conversor.setParametro("33",ConversorParametrosLanzador.TIPOS.Integer);
				conversor.setParametro(idEper,
						ConversorParametrosLanzador.TIPOS.String);
				conversor.setParametro("CA",
						ConversorParametrosLanzador.TIPOS.String); // Idioma
				conversor.setParametro("P",
						ConversorParametrosLanzador.TIPOS.String); // Devolución a servicio web.
				conversor.setParametro("S",
						ConversorParametrosLanzador.TIPOS.String); // VieneImprJustificante
				conversor.setParametro("N",
						ConversorParametrosLanzador.TIPOS.String); // CargoDomi.
				conversor.setParametro("S",
						ConversorParametrosLanzador.TIPOS.String); // Imprimir
	
				WSDocumentos_Service srv = new WSDocumentos_Service();
				WSDocumentos port = srv.getWSDocumentosPort();
				javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) port;
				// Cambiamos el endpoint
				bpr.getRequestContext().put(
						javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
						pref.getEndpointDocumentos());
				// Vinculamos con el Handler
				HandlerUtil.setHandlerClient((javax.xml.ws.BindingProvider) port);
				pdf= port.impresionGD(conversor.Codifica(), "", false);
			}
			return pdf;
		} catch (ParserConfigurationException e) {
			throw new Exception(
					"Error en la impresión de justificante de recibo o liquidación:"
							+ e.getMessage(), e);
		}

	}
	
	public static DatosDoinJustificante getDatosDoinJustificante(String idEper) throws Exception
	{
		DatosDoinJustificante datos= new DatosDoinJustificante();
		ConversorParametrosLanzador conversor;
		Preferencias pref = new Preferencias();
		try {

							
			conversor = new ConversorParametrosLanzador();
	        conversor.setProcedimientoAlmacenado(pref.getPaDatosDoinJustificante());

	        
	        conversor.setParametro("1",ConversorParametrosLanzador.TIPOS.Integer);
	        // peticion
	        conversor.setParametro("1",ConversorParametrosLanzador.TIPOS.Integer);
	        // usuario
	        conversor.setParametro("USU_WEB_SAC",ConversorParametrosLanzador.TIPOS.String);
	        // organismo
	        conversor.setParametro("33",ConversorParametrosLanzador.TIPOS.Integer);	        
	        // codTactInforme
	        conversor.setParametro(idEper,ConversorParametrosLanzador.TIPOS.Integer);
	        conversor.setParametro("P",ConversorParametrosLanzador.TIPOS.String);

	        LanzaPLMasivoService lanzaderaWS = new LanzaPLMasivoService();					
			LanzaPLMasivo lanzaderaPort;			
			lanzaderaPort = lanzaderaWS.getLanzaPLMasivoSoapPort();
	        
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;
			bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointLanzador());
	        String respuesta = "";	
	        //Vinculamos con el Handler	        
	        HandlerUtil.setHandlerClient((javax.xml.ws.BindingProvider) lanzaderaPort);

	        
	        try {	        	
	        	respuesta = lanzaderaPort.executePL(pref.getEntorno(), conversor.Codifica(), "", "", "", "");
	        	conversor.setResultado(respuesta);
	        	//Referencia
	        	datos.setNombreDoin(conversor.getNodoResultado("STRING1_CANU"));
	        	String nif= conversor.getNodoResultado("STRING2_CANU");
	        	datos.setNifSujetoPasivo(nif);
	        	datos.setNifPresentador(nif);
	        	datos.setHashDoin(null); //No tiene.
	        	return datos;
	        }catch (Exception ex) {
	        		Logger.error("Error en lanzador al recuperar datos de doin del justificante de pago: "+ex.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
	        		throw ex;
	        }
		} catch (Exception e) {
			Logger.error("Excepcion generica al recuperar los datos del justificante de pago: "+e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			throw e;
		}
	}
	
	/**
	 * Recupera el contenido del justificante del archivo digital
	 * @param idAdar
	 * @param pref
	 * @return
	 * @throws Exception
	 */
	private static String recuperarJustificanteArchivoDigital(String idAdar, Preferencias pref) throws Exception{
		ArchivoDigital_Service srv = new ArchivoDigital_Service();
		ArchivoDigital port= srv.getArchivoDigitalSOAP();
		javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) port;
		bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, pref.getEndpointArchivoDigital());
		HandlerUtil.setHandlerClient((javax.xml.ws.BindingProvider) port);
		String obtenerSoloDatosArchivo="N";
		Holder<byte[]> contenidoArchivo= new Holder<byte[]>(); //Salida
		Holder<String> datosArchivo= new Holder<String>();
		Holder<String> error= new Holder<String>();
		port.obtieneArchivoPorId("USU_WEB_SAC", Integer.parseInt(idAdar), obtenerSoloDatosArchivo, contenidoArchivo, datosArchivo, error);
		if (error!=null && error.value!=null && error.value!=""){
			throw new Exception ("Error al recuperar el fichero del archivo digital:"+ error.value);
		}
		if (contenidoArchivo==null || "".equals(contenidoArchivo)){
			throw new Exception ("Error al recuperar el fichero del archivo digital, no hay contenido");
		}
		return new String(Base64.encode(contenidoArchivo.value));
	}
	/**
	 * Recupera el identificador del justificante de pago en el archivo digital, o cadena vacía si no hay.
	 * No devuelve errores.
	 * @param idEper
	 * @return
	 */
	public static String getIdentificadorJustificanteEnArchivoDigital(String idEper){
		String idAdar="";
		ConversorParametrosLanzador conversor;
		Preferencias pref = new Preferencias();
		try {

			
			conversor = new ConversorParametrosLanzador();
	        conversor.setProcedimientoAlmacenado(pref.getPaDatosJustificanteAD());

	        
	        conversor.setParametro("1",ConversorParametrosLanzador.TIPOS.Integer);
	        // peticion
	        conversor.setParametro("1",ConversorParametrosLanzador.TIPOS.Integer);
	        // usuario
	        conversor.setParametro("USU_WEB_SAC",ConversorParametrosLanzador.TIPOS.String);
	        // organismo
	        conversor.setParametro("33",ConversorParametrosLanzador.TIPOS.Integer);	        
	        // codTactInforme
	        conversor.setParametro(idEper,ConversorParametrosLanzador.TIPOS.Integer);
	        conversor.setParametro("P",ConversorParametrosLanzador.TIPOS.String);

	        LanzaPLMasivoService lanzaderaWS = new LanzaPLMasivoService();					
			LanzaPLMasivo lanzaderaPort;			
			lanzaderaPort = lanzaderaWS.getLanzaPLMasivoSoapPort();
	        
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;
			bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointLanzador());
	        String respuesta = "";	
	        //Vinculamos con el Handler	        
	        HandlerUtil.setHandlerClient((javax.xml.ws.BindingProvider) lanzaderaPort);

	        
	        respuesta = lanzaderaPort.executePL(pref.getEntorno(), conversor.Codifica(), "", "", "", "");
        	conversor.setResultado(respuesta);
        	String resultado=conversor.getNodoResultado("STRING1_CANU");
        	if ("AD".equals(resultado)){
        		idAdar= conversor.getNodoResultado("NUME1_CANU");
        	}
		} catch (Exception e) {
			Logger.error("Excepcion al recuperar los datos del justificante de pago en archivo digital: "+e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			idAdar="";
		}
		return idAdar;
	}

}
