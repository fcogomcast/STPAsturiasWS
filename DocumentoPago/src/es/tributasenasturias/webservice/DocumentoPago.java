package es.tributasenasturias.webservice;

//import javax.jws.HandlerChain;
import java.io.ByteArrayOutputStream;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.WebServiceException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import es.tributasenasturias.docs.AltaDocumento;
import es.tributasenasturias.docs.CartaPagoExpedienteEjecutiva;
import es.tributasenasturias.docs.CertificadoPagoAutoliquidacion;
import es.tributasenasturias.docs.JustificantePagoAutoliquidacion;
import es.tributasenasturias.docs.JustificantePagoGestorDocumental;
import es.tributasenasturias.docs.JustificantePagoReferencia;
import es.tributasenasturias.docs.RetornoCartaPagoExpedienteEjecutiva;
import es.tributasenasturias.documentopagoutils.Base64;
import es.tributasenasturias.documentopagoutils.Logger;
import es.tributasenasturias.documentopagoutils.PdfComprimidoUtils;
import es.tributasenasturias.documentopagoutils.Preferencias;
import es.tributasenasturias.documentopagoutils.ProcesadorCartaPagoInterface;
import es.tributasenasturias.documentopagoutils.SelectorCartaPago;
import es.tributasenasturias.documentos.util.ConversorParametrosLanzador;
import es.tributasenasturias.documentos.util.XMLUtils;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivo;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivoService;




/*******************************************************************************
 * 
 * Impresión de diferentes documentos de pago, justificantes y 
 * cartas de pago
 * 
 * 
 ******************************************************************************/

@WebService(name = "DocumentoPago")
@HandlerChain (file = "HandlerChain.xml")
public class DocumentoPago {
	
	private ConversorParametrosLanzador recuperarEstadoValor (String idEper, String tipo) {
		ConversorParametrosLanzador cpl=null;
		try {
			Preferencias pref = new Preferencias();					
			try {	
				pref.CargarPreferencias();
										
			} catch (Exception e) {
				Logger.debug("Error al cargar preferencias y plantilla al dar de alta el documento. "+e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			}			
							
			cpl = new ConversorParametrosLanzador();
			
	        cpl.setProcedimientoAlmacenado("INTERNET_RECIBOS.ComprobarEstadoValor");
	        cpl.setParametro("1",ConversorParametrosLanzador.TIPOS.Integer);
	        // peticion	    
	        cpl.setParametro("1",ConversorParametrosLanzador.TIPOS.Integer);
	        // usuario
	        cpl.setParametro("USU_WEB_SAC",ConversorParametrosLanzador.TIPOS.String);
	        // organismo
	        cpl.setParametro("33",ConversorParametrosLanzador.TIPOS.Integer);	        
	        // idEper
	        cpl.setParametro(idEper,ConversorParametrosLanzador.TIPOS.Integer);
	        cpl.setParametro(tipo,ConversorParametrosLanzador.TIPOS.String);
	        cpl.setParametro("P",ConversorParametrosLanzador.TIPOS.String);
	        
	        LanzaPLMasivoService lanzaderaWS = new LanzaPLMasivoService();					
			LanzaPLMasivo lanzaderaPort;			
			lanzaderaPort = lanzaderaWS.getLanzaPLMasivoSoapPort();

	        javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;
			
	        // Cambiamos el endpoint
			bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointLanzador());
			
	        //Vinculamos con el Handler	        
	        //HandlerUtil.setHandlerClient((javax.xml.ws.BindingProvider) lanzaderaPort);		
			
	        String respuesta = "";		        
	        try {	        	
	        	

	        	Logger.debug("ENTORNO: "+pref.getEntorno(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
	        	Logger.debug("ENDPOINT: "+pref.getEndpointLanzador(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
	        	Logger.debug("LLAMADA A RECUPERAR ESTADO: "+cpl.Codifica(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
	        	
	        	respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl.Codifica(), "", "", "", "");	        
	        	cpl.setResultado(respuesta);	    
	        	
	        	
	        	
	        }catch (Exception ex) {	       
	        	Logger.error("Error ejecutando llamada al Alta de documento: " +ex.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
	        }
		} catch (Exception e) {
			Logger.error("Error ejecutando llamada al Alta de documento:" +e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
		return cpl;
	}

	/**
	 * ObtenerDocumentoPago: Genera una carta de pago para una liquidación o recibo
	 * 
	 * @param idEper -
	 *            Requiere el idEper del valor
	 * @param tipoNotificacion si se va a notificar (NT) o no (otro valor)
	 * @param comprimido si el documento se va a devolver comprimido
	 * @param origen Origen de la petición, para discriminar el contexto en el que se ha pedido este documento. Por ejemplo, desde la impresión de recibos de campaña en el portal.
	 * 				 <p>En el momento de introducir este parámetro, para saber si se ha pedido un recibo de IVTM de la campaña actual y se va a devolver una liquidación existente en lugar del recibo</p>
	 * @see <p>Incidencia 30714 para el parámetro de origen</p>  
	 * @return Devuelve un xml con la carta de pago codificada en base 64
	 */
	
	@WebMethod()
	public String obtenerDocumentoPago(@WebParam(name = "idEper") String ideper, @WebParam(name = "tipoNotificacion") String tipoNoti, @WebParam(name = "comprimido") String comprimido, @WebParam(name="origen") String origen) {
		try {
	
			String resultado = " ";
			//CRUBENCVS 30714, parámetro de  origen.
			ProcesadorCartaPagoInterface cartaPago = SelectorCartaPago.seleccionarCartaPago(ideper, tipoNoti, comprimido, origen);
			resultado=cartaPago.procesar(ideper, tipoNoti, comprimido, origen);
			return resultado;
			
		} catch (Exception e) {
			Logger.error("Error en método 'obtenerDocumentoPago':"+e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			return "Error en método 'obtenerDocumentoPago"+e.getMessage();
		}
	}
	
//	@WebMethod()
//	public String obtenerJustificantePago(@WebParam(name = "idEper") String ideper, @WebParam(name = "comprimido") String comprimido) {		
//		try {	
//			ConversorParametrosLanzador cpl;
//			cpl=recuperarEstadoValor(ideper,"J");		
//			String resultado = " ";
//			if (cpl!=null && !cpl.getResultado().equals(null)) {
//			
//				try {												
//					Document docRespuesta = (Document) XMLUtils.compilaXMLObject(cpl.getResultado(), null);
//					Element[] rsCade = XMLUtils.selectNodes(docRespuesta, "//estruc[@nombre='CADE_CADENA']/fila");
//					
//					String dato =  XMLUtils.selectSingleNode(rsCade[0],"STRING_CADE").getTextContent();
//					
//					if (dato.equals("01"))
//					{
//						Logger.debug("El valor no se encuentra en el estado correcto.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
//						resultado =  "KO";
//					}
//					else
//					{
//						String pdfJustPago;
//						try {	
//							JustificantePago oJustificantePago = new JustificantePago(ideper);
//							pdfJustPago = PDFUtils.generarPdf(oJustificantePago);
//														
//							if (pdfJustPago==null || pdfJustPago.equalsIgnoreCase(""))							
//							{
//								Logger.debug("No se ha podido generar el justificante de pago.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
//								resultado =  "KO";
//							}
//							else
//							{
//								resultado = pdfJustPago;
//								/*
//								 * Alta documento
//								 */
//								Logger.debug("Se da de alta el documento.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
//								AltaDocumento docu = new AltaDocumento ();								
//								docu.setDocumento("R", oJustificantePago.getRefCobro(), null, oJustificantePago.getNifSp(),oJustificantePago.getNifSp(), pdfJustPago, "PDF");
//								Logger.debug("Despues del alta de documento.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
//								
//							}
//							
//						
//							Logger.debug("RESULTADO:" +resultado,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
//							Logger.debug("Se comprueba si es necesario comprimir el pdf",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
//							
//							if (comprimido.equalsIgnoreCase("S")) {
//								Logger.debug("Se comprime el pdf.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
//								byte[] docDecodificado = Base64.decode(pdfJustPago.toCharArray());
//								String documentzippeado = "";
//								ByteArrayOutputStream resulByteArray = new ByteArrayOutputStream();
//								resulByteArray.write(docDecodificado);
//								documentzippeado = PdfComprimidoUtils.comprimirPDF(resulByteArray);
//								resultado = documentzippeado; 
//							}
//													
//							Logger.debug("Se finaliza correctamente.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
//
//						} catch (Exception e) {
//							Logger.error("ERROR EN EL JUSTIFICANTE DE PAGO."+ e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
//							//Logger.error("ERROR STACK"+ e.getStackTrace(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
//							resultado =  "KO";
//						}
//
//					}
//				} catch (Exception e) {
//					Logger.error("Excepcion generica al recuperar el estado del valor: "+e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
//					Logger.trace(e.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
//				}
//		}			
//			return resultado;
//			
//		} catch (Exception e) {		
//			Logger.error("error ".concat(e.getMessage()),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);			
//			return "error ".concat(e.getMessage());
//		}
//	}
	
	@WebMethod()
	public String obtenerJustificantePago(@WebParam(name = "idEper") String ideper, @WebParam(name = "comprimido") String comprimido) {		
		try {	
			ConversorParametrosLanzador cpl;
			cpl=recuperarEstadoValor(ideper,"J");		
			String resultado = " ";
			if (cpl!=null && !cpl.getResultado().equals(null)) {
			
				try {												
					Document docRespuesta = (Document) XMLUtils.compilaXMLObject(cpl.getResultado(), null);
					Element[] rsCade = XMLUtils.selectNodes(docRespuesta, "//estruc[@nombre='CADE_CADENA']/fila");
					
					String dato =  XMLUtils.selectSingleNode(rsCade[0],"STRING_CADE").getTextContent();
					
					if (dato.equals("01"))
					{
						Logger.debug("El valor no se encuentra en el estado correcto.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
						resultado =  "KO";
					}
					else
					{
						String pdfJustPago;
						try {	
							pdfJustPago=JustificantePagoGestorDocumental.imprimirJustificante(ideper);
							
							if (pdfJustPago==null || pdfJustPago.equalsIgnoreCase(""))							
							{
								Logger.debug("No se ha podido generar el justificante de pago.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
								resultado =  "KO";
							}
							else
							{
								resultado = pdfJustPago;
								
								//Datos para DOIN
								Logger.debug("Se recuperan datos para el alta de Doin.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
								JustificantePagoGestorDocumental.DatosDoinJustificante doin= JustificantePagoGestorDocumental.getDatosDoinJustificante(ideper);
								/*
								 * Alta documento
								 */
								Logger.debug("Se da de alta el documento.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
								AltaDocumento docu = new AltaDocumento ();								
								docu.setDocumento("R", doin.getNombreDoin(), doin.getHashDoin(), doin.getNifSujetoPasivo(),doin.getNifPresentador(), pdfJustPago, "PDF");
								Logger.debug("Despues del alta de documento.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
								
							}
							
						
							Logger.debug("RESULTADO:" +resultado,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
							Logger.debug("Se comprueba si es necesario comprimir el pdf",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
							
							if (comprimido.equalsIgnoreCase("S")) {
								Logger.debug("Se comprime el pdf.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
								byte[] docDecodificado = Base64.decode(pdfJustPago.toCharArray());
								String documentzippeado = "";
								ByteArrayOutputStream resulByteArray = new ByteArrayOutputStream();
								resulByteArray.write(docDecodificado);
								documentzippeado = PdfComprimidoUtils.comprimirPDF(resulByteArray);
								resultado = documentzippeado; 
							}
													
							Logger.debug("Se finaliza correctamente.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);

						} catch (Exception e) {
							Logger.error("ERROR EN EL JUSTIFICANTE DE PAGO."+ e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
							//Logger.error("ERROR STACK"+ e.getStackTrace(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
							resultado =  "KO";
						}

					}
				} catch (Exception e) {
					Logger.error("Excepcion generica al recuperar el estado del valor: "+e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
					Logger.trace(e.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				}
		}			
			return resultado;
			
		} catch (Exception e) {		
			Logger.error("error ".concat(e.getMessage()),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);			
			return "error ".concat(e.getMessage());
		}
	}
	/**
	 * Generación de un documento justificante de pago, para una autoliquidación.
	 * Se da la opción de dar de alta el documento generado en base de datos.
	 * @param justificante Número de autoliquidación.
	 * @param altaDocumento true si se desea dar de alta en base de datos el documento generado.
	 * 					    false si no.
	 * @return
	 */
	@WebMethod(operationName="JustificantePagoAutoliquidacion")
	public String justificantePagoAutoliquidacion(@WebParam(name = "numeroJustificante") String justificante,
												  @WebParam (name= "altaDocumento") boolean altaDocumento) {
		try {
			JustificantePagoAutoliquidacion jus = new JustificantePagoAutoliquidacion();
			return jus.imprimirJustificante(justificante, altaDocumento);
		}
		catch (Exception e)
		{
			Logger.error("error ".concat(e.getMessage()),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);			
			throw new WebServiceException ("Error en la ejecución de justificante de autoliquidación:" + e.getMessage(),e);
		}
	}
	
	/**
	 * Generación de un documento justificante de pago, para una liquidación.
	 * Se da la opción de dar de alta el documento generado en base de datos.
	 * @param justificante Número de autoliquidación.
	 * @param altaDocumento true si se desea dar de alta en base de datos el documento generado.
	 * 					    false si no.
	 * @return
	 */
	@WebMethod(operationName="JustificantePagoReferencia")
	public String justificantePagoReferencia(@WebParam(name = "numeroReferencia") String referencia,
												  @WebParam (name= "altaDocumento") boolean altaDocumento) {
		try {	
			JustificantePagoReferencia jus = new JustificantePagoReferencia();
			return jus.imprimirJustificante(referencia, altaDocumento);
		}
		catch (Exception e)
		{
			Logger.error("error ".concat(e.getMessage()),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);			
			throw new WebServiceException ("Error en la ejecución de justificante de pago de referencia:" + e.getMessage(),e);
		}
	}
	
	/**
	 * ObtenerDocumentoPagoExpediente: Genera una carta de pago para un expediente
	 * 
	 * @param idEper -
	 *            Requiere el idEper del expediente
	 * @param tipo tipo de carta de pago
	 * @param importe importe de la carta de pago
	 * @param comprimido si el documento se va a devolver comprimido
	 * @return Devuelve un objeto {@link RetornoCartaPagoExpedienteEjecutiva} con el documento en pdf base64
	 */
	
	@WebMethod()
	public @WebResult (name="resultado") RetornoCartaPagoExpedienteEjecutiva obtenerDocumentoPagoExpediente(@WebParam(name = "idEper") String ideper, @WebParam(name = "tipo") String tipo, @WebParam (name= "importe") int importe, @WebParam(name = "comprimido") String comprimido) {
		try {
	
			RetornoCartaPagoExpedienteEjecutiva retorno= new RetornoCartaPagoExpedienteEjecutiva();
			retorno=CartaPagoExpedienteEjecutiva.procesar(ideper, tipo, importe, comprimido);
			return retorno;
			
		} catch (Exception e) {
			Logger.error("Error en método 'obtenerDocumentoPagoExpediente':"+e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			RetornoCartaPagoExpedienteEjecutiva retorno= new RetornoCartaPagoExpedienteEjecutiva();
			retorno.setEsError(true);
			retorno.setDocumento("");
			retorno.setCodigoResultado("9999");
			retorno.setDescripcionResultado("Error técnico en método obtenerDocumentoPagoExpediente " + e.getMessage());
			return retorno;
		}
	}
	
	// CRUBENCVS 46542 17/11/2022
	//Nueva operación para generar un certificado de pago de autoliquidación.
	/**
	 * Generación de un documento justificante de pago, para una autoliquidación.
	 * Se da la opción de dar de alta el documento generado en base de datos.
	 * @param justificante Número de autoliquidación.
	 * @param altaDocumento true si se desea dar de alta en base de datos el documento generado.
	 * 					    false si no.
	 * @return
	 */
	@WebMethod(operationName="CertificadoPagoAutoliquidacion")
	public String certificadoPagoAutoliquidacion(@WebParam(name = "numeroJustificante") String justificante,
												 @WebParam (name= "altaDocumento") boolean altaDocumento) {
		try {
			CertificadoPagoAutoliquidacion jus = new CertificadoPagoAutoliquidacion();
			return jus.imprimirCertificado(justificante, altaDocumento);
		}
		catch (Exception e)
		{
			Logger.error("error ".concat(e.getMessage()),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);			
			throw new WebServiceException ("Error en la ejecución de certificado de pago de autoliquidación:" + e.getMessage(),e);
		}
	}
	// FIN CRUBENCVS 46542
	// A partir de aquí, se ha movido al servicio WSDocumentos
	/*@WebMethod(operationName="obtenerReimprimible")
	public String obtenerReimprimible(@WebParam(name = "elemento") String elemento, @WebParam(name = "tipo") String tipo, @WebParam (name="codigoVerificacion") String codigoVerificacion) {
		try {
			Reimpresion impr = Reimpresion.newReimpresion();
			return impr.getPDFReimprimido(elemento, tipo, codigoVerificacion);
		}
		catch (Exception e)
		{
			Logger.error("error ".concat(e.getMessage()),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);			
			throw new WebServiceException ("Error en la ejecución de reimpresión:" + e.getMessage(),e);
		}
	}*/
	/*
	@WebMethod(operationName="impresionGD")
	public String impresionGD(@WebParam(name = "origenDatos") String origenDatos, @WebParam (name="codigoVerificacion") String codigoVerificacion) {
		try {
			ImpresionGD impr = ImpresionGD.newImpresionGD();
			return impr.getPDFImpresion(origenDatos, codigoVerificacion);
		}
		catch (Exception e)
		{
			Logger.error("error ".concat(e.getMessage()),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);			
			throw new WebServiceException ("Error en la ejecución de impresión por gestor documental:" + e.getMessage(),e);
		}
	}
	*/
}
