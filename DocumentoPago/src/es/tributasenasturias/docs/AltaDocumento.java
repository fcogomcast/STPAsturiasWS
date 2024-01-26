package es.tributasenasturias.docs;

import java.io.ByteArrayOutputStream; //import java.io.ByteArrayInputStream;

//import es.tributasenasturias.documentos.util.Base64;
import es.tributasenasturias.documentopagoutils.Base64; //import es.tributasenasturias.documentos.util.XMLUtils; //import es.tributasenasturias.pagopresentacionmodelo600utils.ConversorParametrosLanzador;
import es.tributasenasturias.documentopagoutils.Logger;
import es.tributasenasturias.documentopagoutils.PdfComprimidoUtils;
import es.tributasenasturias.documentopagoutils.Preferencias;
import es.tributasenasturias.documentos.util.ConversorParametrosLanzador;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivo;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivoService;


public class AltaDocumento {

	private ConversorParametrosLanzador cpl;

	private Preferencias pref = new Preferencias();

	public AltaDocumento() {
		try {
			pref.CargarPreferencias();
		} catch (Exception e) {			
			es.tributasenasturias.documentopagoutils.Logger.error("Error al cargar preferencias al dar de alta el documento. "
							+ e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
	};

	public String setDocumento(String p_tipo, String p_nombre,
			String p_codVerif, String p_nifSp, String p_nifPr, String doc,
			String p_tipoDoc) {
		// Llamar al lanzador para que llame al procedimiento almacenado
		try {
			// llamar al servicio PXLanzador para ejecutar el procedimiento
			// almacenado de alta de expediente
			// Se prepara la llamada al procedimiento almacenado
			cpl = new ConversorParametrosLanzador();			
			cpl.setProcedimientoAlmacenado("Internet_documentosv2.AltaDocumento");
			// tipo
			cpl.setParametro(p_tipo, ConversorParametrosLanzador.TIPOS.String);
			// nombre
			cpl.setParametro(p_nombre,ConversorParametrosLanzador.TIPOS.String);
			// codigo verificacion
			cpl.setParametro(p_codVerif,ConversorParametrosLanzador.TIPOS.String);
			// sp
			cpl.setParametro(p_nifSp, ConversorParametrosLanzador.TIPOS.String);
			// presentador
			cpl.setParametro(p_nifPr, ConversorParametrosLanzador.TIPOS.String);
			// sesion
			cpl.setParametro("", ConversorParametrosLanzador.TIPOS.String);
			// origen
			cpl.setParametro("P", ConversorParametrosLanzador.TIPOS.String);
			// libre
			cpl.setParametro("", ConversorParametrosLanzador.TIPOS.String);
			// publicable = 'S'
			cpl.setParametro("S", ConversorParametrosLanzador.TIPOS.String);

			// Decodificamos documento.
			if (p_tipoDoc != "XML") {
				byte[] docDecodificado = Base64.decode(doc.toCharArray());
				String documentzippeado = new String();
				ByteArrayOutputStream resulByteArray = new ByteArrayOutputStream();
				resulByteArray.write(docDecodificado);
				documentzippeado = PdfComprimidoUtils
						.comprimirPDF(resulByteArray);
				
				cpl.setParametro(documentzippeado,
						ConversorParametrosLanzador.TIPOS.Clob);
				
			} else {
				cpl.setParametro(doc, ConversorParametrosLanzador.TIPOS.Clob);
			}

			// Tipo Document XML o PDF
			cpl.setParametro(p_tipoDoc,ConversorParametrosLanzador.TIPOS.String);
			// conoracle = 'P'
			cpl.setParametro("P", ConversorParametrosLanzador.TIPOS.String);

			LanzaPLMasivoService lanzaderaWS = new LanzaPLMasivoService();					
			LanzaPLMasivo lanzaderaPort;			
			lanzaderaPort = lanzaderaWS.getLanzaPLMasivoSoapPort();

			// enlazador de protocolo para el servicio.
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;
			// Cambiamos el endpoint
			bpr.getRequestContext().put(
					javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
					pref.getEndpointLanzador());

	        //Vinculamos con el Handler	        
	        //HandlerUtil.setHandlerClient((javax.xml.ws.BindingProvider) lanzaderaPort);
	        
			String respuesta = "";
			try {
				respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl
						.Codifica(), "", "", "", "");
				cpl.setResultado(respuesta);
			} catch (Exception ex) {

				if (pref.getDebug().equals("1")) {
					//amalia???
				//	es.tributasenasturias.documentopagoutils.Logger.debug("LANZADOR: ".concat(ex.getMessage()),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
					
					
				}
			}

			if (pref.getDebug().equals("1")) {
				es.tributasenasturias.documentopagoutils.Logger.debug("**** LLAMADA PROCEDIMIENTO ALTA DOCUMENTO ****",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				//es.tributasenasturias.documentopagoutils.Logger.debug("Error:  file '" + filename + "' doesn't exist!",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				es.tributasenasturias.documentopagoutils.Logger.debug("Entorno:" + pref.getEntorno(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				es.tributasenasturias.documentopagoutils.Logger.debug("Codifica:" + cpl.Codifica(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				es.tributasenasturias.documentopagoutils.Logger.debug("EndPointLanzador:" + pref.getEndpointLanzador(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				es.tributasenasturias.documentopagoutils.Logger.debug("Respuesta: ".concat(respuesta),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			}

			return "ok";
		} catch (Exception e) {
			Logger.error("Excepcion generica" + e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			return "ko";
		}
	}
}
