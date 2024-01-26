package es.tributasenasturias.seguridad;


import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



import es.tributasenasturias.excepciones.FirmaException;
import es.tributasenasturias.log.Logger;
import es.tributasenasturias.services.firmaDigital.FirmaDigital;
import es.tributasenasturias.services.firmaDigital.WsFirmaDigital;
import es.tributasenasturias.servicios.RecepcionDocumentos.preferencias.Preferencias;
import es.tributasenasturias.utils.GestorIdLlamada;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.utils.XMLDOMUtils;


/**
 * Clase de ayuda para las operaciones con firma.
 * @author davidsa 
 */
public class FirmaHelper{

	protected FirmaHelper () {

	}
	
	/**
     * Prepara el mensaje de salida para poder ser firmado. Elimina el nodo "Signature" que pudiera
     * haber en la respuesta y añade un id al elemento "documentacionResponse/resultado" del mensaje SOAP.
     * @param mensaje {@link SOAPMessage} que sale.
     * @throws FirmaException
     */
    public void preparaMensaje(SOAPMessage mensaje) throws FirmaException
    {
    	try
    	{
	    	/*javax.xml.xpath.XPath xpath=javax.xml.xpath.XPathFactory.newInstance().newXPath();
			NodeList nodos = (NodeList) xpath.evaluate("//*[local-name()='Envelope']/*[local-name()='Header']",
					mensaje.getSOAPPart().getDocumentElement(), javax.xml.xpath.XPathConstants.NODESET);
			//Generar cabecera, si no existe.
			if (nodos.getLength()==0)
			{
				nodos = (NodeList) xpath.evaluate("//*[local-name()='Envelope']",
						mensaje.getSOAPPart().getDocumentElement(), javax.xml.xpath.XPathConstants.NODESET);
				if (nodos.getLength()>0)
				{
					SOAPFactory fact = SOAPFactory.newInstance();
					
					SOAPElement h = fact.createElement(new QName("http://schemas.xmlsoap.org/soap/envelope/","Header"));
					Node cabecera=mensaje.getSOAPPart().getEnvelope().getOwnerDocument().importNode(h, false);
					mensaje.getSOAPPart().getEnvelope().insertBefore(cabecera, mensaje.getSOAPPart().getEnvelope().getFirstChild());
					mensaje.getSOAPPart().getEnvelope().appendChild(cabecera);
					
				}
				nodos = (NodeList) xpath.evaluate("//*[local-name()='Envelope']/*[local-name()='Header']",
						mensaje.getSOAPPart().getDocumentElement(), javax.xml.xpath.XPathConstants.NODESET);
			}*/
    		javax.xml.xpath.XPath xpath=javax.xml.xpath.XPathFactory.newInstance().newXPath();
    		Node docuResponse=XMLDOMUtils.getFirstChildNode(mensaje.getSOAPBody(), "documentacionResponse");
    		if (docuResponse!=null)
    		{
				Node signature=(Node)xpath.evaluate("./*[local-name()='Signature']", docuResponse,javax.xml.xpath.XPathConstants.NODE);
				if (signature!=null)
				{
					docuResponse.removeChild(signature);
				}
				//Hemos quitado el nodo Signature que podría haber, creamos un id para respuesta
				NodeList nodos = (NodeList) xpath.evaluate("//*[local-name()='Envelope']/*[local-name()='Body']/*[local-name()='documentacionResponse']/*[local-name()='resultado']",
						mensaje.getSOAPPart().getDocumentElement(), javax.xml.xpath.XPathConstants.NODESET);
				if (nodos.getLength()>0)
				{
					Node body=nodos.item(0);
					NamedNodeMap atts = body.getAttributes();
					if (atts.getLength()>0)
					{
						//Si no hay nodo "ID".
						if (atts.getNamedItem("id")==null && atts.getNamedItem("ID")==null)
						{
							((Element)body).setAttribute("ID", Mensajes.RESPUESTA_ID);
						}
					}
					else
					{
						((Element)body).setAttribute("ID",Mensajes.RESPUESTA_ID);
					}
				}
    		}
    	}
    	catch (XPathExpressionException e)
    	{
    		throw new FirmaException("Error en modificación de mensaje para firma:"+e.getMessage(),e);
    	}
    	catch (SOAPException e)
    	{
    		throw new FirmaException("Error en modificación de mensaje para firma:"+e.getMessage(),e);
    	}
    }
	/**
	 * Genera un mensaje firmado a partir de un mensaje de entrada.
	 * 
	 * @param msg
	 * @return Cadena con mensaje firmado o null si no se ha podido generar.
	 */
	public String firmaMensaje(String msg,String ns) {
		String firmado = null;
		WsFirmaDigital srv = new WsFirmaDigital();					
		FirmaDigital srPort = srv.getServicesPort();
		Logger log= new Logger(GestorIdLlamada.getIdLlamada());
		try {
			Preferencias pref= Preferencias.getPreferencias();
			//Se modifica el endpoint
			String endpointFirma = pref.getEndPointFirma();			
			if (!endpointFirma.equals("")) {
				BindingProvider bpr = (BindingProvider) srPort;
				bpr.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,endpointFirma);
			}
			// Recuperamos la clave y certificado de firma.
			String certificado = pref.getAliasCertificadoFirma();
		    String msgFirmado = srPort.firmarXML(msg, certificado, Mensajes.RESPUESTA_ID, "documentacionResponse", ns);
		    if (srPort.validar(msgFirmado))
		    	log.info("Integridad de la firma de mensaje de salida correcta!!");
		    else
		    	log.error("Error al validar integridad de la firma del mensaje de salida.");
    			    			    
			firmado = msgFirmado;								
		} catch (Exception ex) {
			log.error("Error al firmar el mensaje de salida: "+ ex.getMessage());
			firmado = null;
		}	
		return firmado;
	}
	/**
	 * Valida un mensaje de entrada contra firma digital.
	 * @param msg
	 * @param ns
	 * @return
	 */
	public boolean esValido(String xml) {
		WsFirmaDigital srv = new WsFirmaDigital();					
		FirmaDigital srPort = srv.getServicesPort();
		Logger log= new Logger(GestorIdLlamada.getIdLlamada());
		try {
			Preferencias pref= Preferencias.getPreferencias();
			//Se modifica el endpoint
			String endpointFirma = pref.getEndPointFirma();			
			if (!endpointFirma.equals("")) {
				BindingProvider bpr = (BindingProvider) srPort;
				bpr.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,endpointFirma);
			}
		    if (srPort.validar(xml))
		    {
		    	return true;
		    }
		    else
		    {
		    	return false;
		    }
		} catch (Exception ex) {
			log.error("Error al firmar el mensaje de salida: "+ ex.getMessage());
			return false;
		}	
	}
	
	/**
     * Extrae el certificado.
     * @param mensaje
     * @return
     * @throws XMLDOMDocumentException
     */
//    private String extraerCertificado(String mensaje)
//			throws XMLDOMDocumentException {
//		Document doc = XMLDOMUtils.parseXml(mensaje);
//		Node certificado = XMLDOMUtils
//				.selectSingleNode(
//						doc,
//						"/*[local-name()='Envelope']/*[local-name()='Header']/*[local-name()='Signature']/*[local-name()='KeyInfo']/*[local-name()='X509Data']/*[local-name()='X509Certificate']/text()");
//		
//		if (certificado==null)
//		{
//			return "";
//		}
//		return certificado.getNodeValue();
//		
//		}
	/**
	 * Comprueba si la identidad del certificado tiene permisos sobre el servicio.
	 * @param idSesion Id de la sesión
	 * @throws XMLDOMDocumentException En caso de no poder extraer el certificado del mensaje SOAP
	 * @throws SeguridadException En caso de no poder comprobar los permisos.
	 */
//	private boolean comprobarPermisos(String xml)
//			throws  SeguridadException
//	{
//		// Extraemos el certificado.
//		try
//		{
//			VerificadorCertificado vf=SeguridadFactory.newVerificadorCertificado("");
//			InfoCertificado i=vf.login(extraerCertificado(xml));
//			if (i.getValidez().equals(Validez.VALIDO))
//			{
//				
//			}
//			return true;
//		}
//		catch (XMLDOMDocumentException e)
//		{
//			throw new SeguridadException ("Error al comprobar los permisos del certificado recibido:"+ e.getMessage());
//		}
//	}
}