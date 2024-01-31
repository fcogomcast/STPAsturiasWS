package es.tributasenasturias.webservice;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import es.tributasenasturias.GenerarDocs600ANCERTutils.Logger;
import es.tributasenasturias.GenerarDocs600ANCERTutils.Preferencias;
import es.tributasenasturias.GenerarDocs600ANCERTutils.Utils;
import es.tributasenasturias.business.ModelosPDF;
import es.tributasenasturias.docs.AltaDocumento;
import es.tributasenasturias.docs.Comparecencia;
import es.tributasenasturias.docs.JustificantePresentacion;
import es.tributasenasturias.docs.PDFUtils;

/*******************************************************************************
 * 
 * Clase WebService que obtiene los datos de la petición de cálculo del modelo
 * 600.
 * 
 * Recupera la información, realiza la llamada al procedimiento almacenado que
 * calcula el resultado de la autoliquidación y devuelve el resultado.
 * 
 ******************************************************************************/

@WebService(name = "GenerarDocs600ANCERT")
@HandlerChain(file = "HandlerChain.xml")
public class GenerarDocs600ANCERT {
	//private String XML_MSG_ERROR = "<ns2:remesa><ns2:resultado><ns2:codigo></ns2:codigo><ns2:descripcion></ns2:descripcion></ns2:resultado></ns2:remesa>";	
	private String XML_MSG_ERROR = "<remesa><resultado><codigo></codigo><descripcion></descripcion></resultado></remesa>";
	private String MSG_ERROR_01_01 = " Error en el XML de entrada (XML Vacío)";	
	private String MSG_ERROR_02_03 = " Error en el proceso de alta de expediente (Alta de documento)";
	
	
	private Preferencias pref = new Preferencias();
	private String v_numAutoliq = new String();

	public GenerarDocs600ANCERT() {
		// de este modo, al instalar el webservice, se creara el fichero de
		// preferencias si no existe
		pref.CompruebaFicheroPreferencias();
	}

	@WebMethod()
	public String getDocs600(@WebParam(name = "xmlData")
	String xmlData) {
		try {
			// cargamos los datos del almacen de un fichero xml preferencias
			pref.CargarPreferencias();
			String nodoRes=null;
			nodoRes=xmlData;
			//*** Se inserta el nodo de resultado.
			nodoRes = Utils.insertarNodoResultado(nodoRes);
			// Se comprueban los datos de entrada
			if (xmlData == null || xmlData.length() == 0) {
				// FIXME: Comprobar que se trata de un xml valido y validado con
				// el xsd.
				return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_01_01, "01");
			}

			if (pref.getDebug().equals("1")) {				
				Logger.info(" ************* XML DE ENTRADA AL SERVICIO ******************");
				Logger.info(xmlData);			
			}
										
			

			//*** Se recupera el número de autoliquidación
			String numAutoliquidacion;
			numAutoliquidacion = Utils.getNumAutoliquidacion(nodoRes);
			if (numAutoliquidacion ==null)
			{
				Logger.error("No se pude recuperar el número de autoliquidación de los datos de entrada");
				return Utils.setErrores(XML_MSG_ERROR, "Datos de entrada sin número de autoliquidación en el lugar esperado", "01");
			}
			//*** Se elimina el nodo de firma, este servicio es interno.
			nodoRes = Utils.borrarNodoFirma(nodoRes);
			// Sujeto pasivo
			
			String nifSP = Utils.getNifSP(nodoRes);
			if (nifSP == null)
			{
				Logger.error("No se pude recuperar el nif de sujeto pasivo de los datos de entrada");
				return Utils.setErrores(XML_MSG_ERROR, "Datos de entrada sin nif sujeto pasivo en el lugar esperado", "01");
			}
			// Presentador
			String nifPR = Utils.getNifPR(nodoRes);
			if (nifPR ==null)
			{
				Logger.error("No se pude recuperar el nif de presentador de los datos de entrada");
				return Utils.setErrores(XML_MSG_ERROR, "Datos de entrada sin nif de presentador en el lugar esperado", "01");
			}
			//=============================
			// ALTA DOCUMENTO COMPARECENCIA
			//=============================
			try {
				if (pref.getDebug().equals("1")) 						
					Logger.info(" **** ALTA DOCUMENTO COMPARECENCIA ****");
				
				Comparecencia oDocComparecencia = new Comparecencia(numAutoliquidacion);
				String pdfDocComparecencia = PDFUtils.generarPdf(oDocComparecencia);
				AltaDocumento ad = new AltaDocumento();
				// J : Se trada del documento de comparecencia.
				nodoRes = Utils.setDocumentoComparecencia(nodoRes, pdfDocComparecencia);
				ad.setDocumento("J", numAutoliquidacion, oDocComparecencia.getCodVerificacion(), nifSP, nifPR,pdfDocComparecencia, "PDF");
			} catch (Exception e) {
				Logger.error("ERROR EN EL DOCUMENTO DE COMPARECENCIA."+ e.getMessage());
				return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_02_03, "02");
			}

			//=========================================
			// ALTA DOCUMENTO JUSTIFICANTE PRESENTACION
			//=========================================
			try {
				if (pref.getDebug().equals("1")) 						
					Logger.info(" ************* ALTA JUSTIFICANTE PAGO/PRESENTACION  ******************");
				
				JustificantePresentacion oJustificantePre = new JustificantePresentacion(numAutoliquidacion);
				String pdfJustificantePre = PDFUtils.generarPdf(oJustificantePre);
				AltaDocumento ad = new AltaDocumento();
				
				// C : Se trata del justificante de pago y presentación.
				nodoRes = Utils.setJustificantePresentacion(nodoRes, pdfJustificantePre);
				ad.setDocumento("C", numAutoliquidacion, oJustificantePre.getCodVerificacion(), nifSP, nifPR,pdfJustificantePre, "PDF");
			} catch (Exception e) {
				Logger.error("ERROR EN EL DOCUMENTO DE JUSTIFICANTE DE PAGO Y PRESENTACION."+ e.getMessage());
				return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_02_03, "02");
			}

			//================
			// ALTA MODELO 600
			//================
			try {
				if (pref.getDebug().equals("1")) 						
					Logger.info(" ************* ALTA MODELO 600 PDF ******************");
				
				ModelosPDF modelo600 = new ModelosPDF();
				boolean resultModelo600 = modelo600
						.generarModelo600(nodoRes);
						
							
				if (!resultModelo600) {
					Logger.error("SE HA PRODUCIDO UN ERROR EN LA GENERACIÓN DEL DOCUMENTO DEL MODELO 600--: "+ modelo600.getResultado());					
					return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_02_03, "02");					
				} else { // Almacenamos el modelo en base de datos.

					AltaDocumento ad = new AltaDocumento();										
					ad.setDocumento("M", numAutoliquidacion, modelo600.getCodigoVerificacion("M"+numAutoliquidacion+nifSP),nifSP, nifPR, modelo600.getResultado(), "PDF");
				}
			} catch (Exception e) {
				Logger.error("SE HA PRODUCIDO UN ERROR EN LA GENERACIÓN DEL DOCUMENTO DEL MODELO 600: "+ e.getMessage());
				return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_02_03, "02");
			}

			//=============================
			// Firmamos salida del Servicio
			//=============================
			
			return nodoRes;
			
		} catch (Exception e) {
			if (pref.getDebug().equals("1")) {
				Logger.error("error".concat(e.getMessage()));
			}
			return "error".concat(e.getMessage());
		}
	}
}
