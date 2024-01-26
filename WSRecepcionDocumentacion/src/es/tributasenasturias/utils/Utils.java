package es.tributasenasturias.utils;

import java.io.StringWriter;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import es.tributasenasturias.log.Logger;


public final class Utils {

	Utils () {}
	/**
	 * Inserta el código y descripción del resultado si se produce algún error.
	 * @param datos XML de salida.
	 * @param error Mensaje de error.
	 * @param codError Código de error.
	 * @return
	 */
	public final static String setErrores(String datos, String error, String codError) {
		Document resultadoXML;
		Logger log = new Logger(GestorIdLlamada.getIdLlamada());
		try {
			DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			fact.setNamespaceAware(true);
			DocumentBuilder db= fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 
			inStr.setCharacterStream(new java.io.StringReader(datos.toString()));
			try{
				resultadoXML = db.parse(inStr);
			}catch (java.io.IOException ex){
				log.error("ERROR génerico al parsear la respuesta Utils.setErrores: "+ex.getMessage());
				return datos;
			}catch (org.xml.sax.SAXException ex){
				log.error("ERROR al parsear la respuesta Utils.setErrores: "+ex.getMessage());
				return datos;
			}
			XPath xpath = XPathFactory.newInstance().newXPath();
			Node remesa = (Node) xpath.evaluate("/*[local-name()='remesa']", resultadoXML, XPathConstants.NODE);
			Node resultado = (Node) xpath.evaluate("/*[local-name()='remesa']/*[local-name()='resultado']", resultadoXML, XPathConstants.NODE);
			Node codigoNode;
			Node descrNode;
			String prefijo = remesa.getPrefix();
			String namespace = remesa.getNamespaceURI();
			if (resultado==null){
				//Crear nodo resultado
				if (prefijo==null){
					resultado = (Node)resultadoXML.createElementNS(namespace,"resultado");
					codigoNode = (Node)resultadoXML.createElementNS(namespace,"codigo");
					descrNode = (Node)resultadoXML.createElementNS(namespace,"descripcion");
				}else{
					resultado = (Node)resultadoXML.createElementNS(namespace,prefijo+":resultado");
					codigoNode = (Node)resultadoXML.createElementNS(namespace,prefijo+":codigo");
					descrNode = (Node)resultadoXML.createElementNS(namespace,prefijo+":descripcion");
					resultado.setPrefix(prefijo);
					codigoNode.setPrefix(prefijo);
					descrNode.setPrefix(prefijo);
				}
				resultado.appendChild(codigoNode);
				resultado.appendChild(descrNode);
				remesa.appendChild(resultado);
			}else{
				codigoNode = (Node) xpath.evaluate("/*[local-name()='remesa']/*[local-name()='resultado']/*[local-name()='codigo']", resultadoXML, XPathConstants.NODE);
				descrNode = (Node) xpath.evaluate("/*[local-name()='remesa']/*[local-name()='resultado']/*[local-name()='descripcion']", resultadoXML, XPathConstants.NODE);
			}	
			codigoNode.setTextContent(codError);
			descrNode.setTextContent(error);
			return document2String(resultadoXML);
		} catch (ParserConfigurationException pce) {
			log.error("ERROR al crear la instancia del parseador Utils.setErrores: "+pce.getMessage());
			return datos;
		}catch (XPathExpressionException xee) {
			log.error("ERROR al evaluar las expresiones xpath Utils.setErrores: "+xee.getMessage());
			return datos;
		} catch (Exception e) {
			log.error("ERROR generico Utils.setErrores: "+e.getMessage());
			return datos;
		}
	}
	
	/**
	 * Convierte un documento DOM a String, con codificación UTF-8
	 * @param doc Documento {@link Document}
	 * @return Cadena con la representación en texto del documento.
	 * @throws Exception
	 */
	private static String document2String(Document doc)throws Exception{
        TransformerFactory tFactory = TransformerFactory.newInstance();        
        Transformer transformer = tFactory.newTransformer();
        transformer.setOutputProperty("encoding", "UTF-8");
        DOMSource source = new DOMSource(doc);
        StringWriter sw=new StringWriter();
        StreamResult result = new StreamResult(sw);
        transformer.transform(source, result);
        
        String xmlString=sw.toString();
        return xmlString;
    } 
/**
 * Genera un mensaje SOAP de error con los datos que se indican.
 * @param msg Mensaje de error 
 * @param reason Razón del error.
 * @param codigo Código del detalle de error
 * @param mensaje Mensaje del detalle de error
 */
	@SuppressWarnings("unchecked")
	public static void generateSOAPErrMessage(SOAPMessage msg, String reason, String codigo, String mensaje) {
	       try {
	    	  SOAPEnvelope soapEnvelope= msg.getSOAPPart().getEnvelope();
	          SOAPBody soapBody = msg.getSOAPPart().getEnvelope().getBody();
	          SOAPFault soapFault = soapBody.addFault();
	          soapFault.setFaultString(reason);
	          Detail det= soapFault.addDetail();
	          Name name = soapEnvelope.createName("id");
	          det.addDetailEntry(name);
	          
	          name = soapEnvelope.createName("mensaje");
	          det.addDetailEntry(name);
	          DetailEntry entry;
	          Iterator<DetailEntry> it=det.getDetailEntries();
	          while (it.hasNext())
	          {
	        	  entry=it.next();
	        	  if (entry.getLocalName().equals("id"))
	        	  {	
	        		XMLDOMUtils.setNodeText (entry.getOwnerDocument(),entry, codigo);  
	        	  }
	        	  if (entry.getLocalName().equals("mensaje"))
	        	  {	
	        		XMLDOMUtils.setNodeText (entry.getOwnerDocument(),entry, mensaje);  
	        	  }
	        		  
	          }
	          throw new SOAPFaultException(soapFault); 
	       }
	       catch(SOAPException e) { }
	}
	/**
	 * Genera una excepción. 
	 * @param codigo Código de la excepción ("Client","Server",otro)
	 * @param mensaje Mensaje.
	 */
	public static void generarSOAPFault(String codigo, String mensaje)
	{
		SOAPFactory fac;
		try {
			fac = SOAPFactory.newInstance();
			SOAPFault sf = fac.createFault(mensaje, new QName("http://schemas.xmlsoap.org/soap/envelope/", codigo));
			throw new SOAPFaultException(sf);
		} catch (SOAPException e1) {
		}  

	}
}