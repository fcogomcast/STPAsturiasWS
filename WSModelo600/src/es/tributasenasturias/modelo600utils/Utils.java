package es.tributasenasturias.modelo600utils;

import java.io.StringWriter;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
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
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.tributasenasturias.documentos.util.XMLUtils;
import es.tributasenasturias.exception.PresentacionException;
import es.tributasenasturias.validacion.XMLDOMUtils;

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
		try {
			DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			fact.setNamespaceAware(true);
			DocumentBuilder db= fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 
			inStr.setCharacterStream(new java.io.StringReader(datos.toString()));
			try{
				resultadoXML = db.parse(inStr);
			}catch (java.io.IOException ex){
				Logger.error("ERROR génerico al parsear la respuesta Utils.setErrores: "+ex.getMessage());
				return datos;
			}catch (org.xml.sax.SAXException ex){
				Logger.error("ERROR al parsear la respuesta Utils.setErrores: "+ex.getMessage());
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
			Logger.error("ERROR al crear la instancia del parseador Utils.setErrores: "+pce.getMessage());
			return datos;
		}catch (XPathExpressionException xee) {
			Logger.error("ERROR al evaluar las expresiones xpath Utils.setErrores: "+xee.getMessage());
			return datos;
		} catch (Exception e) {
			Logger.error("ERROR generico Utils.setErrores: "+e.getMessage());
			return datos;
		}
	}
	/**
	 * Inserta el documento de comparecencia en el documento Xml de salida
	 * @param datos XML de salida
	 * @param doc Documento de comparecencia
	 * @return Representación en texto del xml de salida con los datos del documento de comparecencia.
	 * @throws PresentacionException
	 */
	public final static String setDocumentoComparecencia(String datos, String doc) throws PresentacionException{
		Document resultadoXML;
		try {
			DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			DocumentBuilder db= fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 
			inStr.setCharacterStream(new java.io.StringReader(datos.toString()));

			resultadoXML = db.parse(inStr);
				
			NodeList nl = XMLDOMUtils.getAllNodes(resultadoXML, "justificanteComparecencia");
			if (nl.getLength()>0){
				Node codigoNode = nl.item(0);
				codigoNode.setTextContent(doc);			
				return document2String(resultadoXML);
			}else{
				throw new PresentacionException("No se ha podido generar el justificante de comparecencia porque no se ha encontrado el nodo 'justificanteComparecencia'");
			}
		}catch (java.io.IOException ex){
			throw new PresentacionException("ERROR génerico al parsear la respuesta Utils.setDocumentoComparecencia: "+ex.getMessage());
		}catch (org.xml.sax.SAXException ex){
			throw new PresentacionException("ERROR al parsear la respuesta Utils.setDocumentoComparecencia: "+ex.getMessage());
		}catch (ParserConfigurationException pce) {
			throw new PresentacionException("ERROR al crear la instancia del parseador Utils.setDocumentoComparecencia: "+pce.getMessage());
		}catch (XPathExpressionException xee) {
			throw new PresentacionException("ERROR al evaluar las expresiones xpath Utils.setDocumentoComparecencia: "+xee.getMessage());
		}catch (Exception e) {
			if (!(e instanceof PresentacionException))
				throw new PresentacionException("ERROR generico Utils.setDocumentoComparecencia: "+e.getMessage());
			else
				throw (PresentacionException)e;
		}
	}
	/**
	 * Inserta el justificante de presentación en el XML de salida
	 * @param datos XML de salida
	 * @param doc Documento de justificante de presentación.
	 * @return XML de salida con el justificante de presentación insertado.
	 * @throws PresentacionException
	 */
	public final static String setJustificantePresentacion(String datos, String doc) throws PresentacionException {
		Document resultadoXML;
		try {
			DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			DocumentBuilder db= fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 
			inStr.setCharacterStream(new java.io.StringReader(datos.toString()));

			resultadoXML = db.parse(inStr);
				
			NodeList nl = XMLDOMUtils.getAllNodes(resultadoXML, "certificadoPresentacionPago");
			if (nl.getLength()>0){
				Node codigoNode = nl.item(0);
				codigoNode.setTextContent(doc);			
				return document2String(resultadoXML);
			}else{
				throw new PresentacionException("No se ha podido generar el certificado de presentacion porque no se ha encontrado el nodo 'certificadoPresentacionPago'");
			}
		}catch (java.io.IOException ex){
			throw new PresentacionException("ERROR génerico al parsear la respuesta Utils.setJustificantePresentacion: "+ex.getMessage());
		}catch (org.xml.sax.SAXException ex){
			throw new PresentacionException("ERROR al parsear la respuesta Utils.setJustificantePresentacion: "+ex.getMessage());
		}catch (ParserConfigurationException pce) {
			throw new PresentacionException("ERROR al crear la instancia del parseador Utils.setJustificantePresentacion: "+pce.getMessage());
		}catch (XPathExpressionException xee) {
			throw new PresentacionException("ERROR al evaluar las expresiones xpath Utils.setJustificantePresentacion: "+xee.getMessage());
		}catch (Exception e) {
			if (!(e instanceof PresentacionException))
				throw new PresentacionException("EXCEPCIÓN generico Utils.setDocument: "+e.getMessage());
			else
				throw (PresentacionException)e;
		}
	}
	/**
	 * Asigna la representación de un justificante de pago al nodo de salida de la presentación.
	 * @param datos XML de salida
	 * @param doc Documento de justificante de pago
	 * @return Representación en texto del documento XML de salida con el justificante de pago
	 * @throws PresentacionException
	 */
	public final static String setJustificantePagoDocument(String datos, String doc) throws PresentacionException{
		Document resultadoXML;
		try {
			DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			fact.setNamespaceAware(true);
			DocumentBuilder db= fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 
			inStr.setCharacterStream(new java.io.StringReader(datos.toString()));
			try{
				resultadoXML = db.parse(inStr);
			}catch (java.io.IOException ex){
				throw new PresentacionException("ERROR de entrada/salida al parsear la respuesta Utils.setDocument: "+ex.getMessage());
			}catch (org.xml.sax.SAXException ex){
				throw new PresentacionException("ERROR al parsear la respuesta Utils.setDocument: "+ex.getMessage());
			}

			NodeList nl = XMLDOMUtils.getAllNodes(resultadoXML, "justificantePago");
			//XPath xpath = XPathFactory.newInstance().newXPath();
			//Node codigoNode = (Node) xpath.evaluate("/remesa/declaracion/pago/justificantePago", resultadoXML, XPathConstants.NODE);
			if (nl.getLength()>0)
			{
				Node codigoNode = nl.item(0);
				codigoNode.setTextContent(doc);		
				//Eliminamos el nillable=true porque en salida no tiene sentido.
				removeNillable(codigoNode);
				return document2String(resultadoXML);
			}
			else
			{
				throw new PresentacionException("No se ha podido generar la carta de pago porque no se ha encontrado el nodo 'justificantePago'");
			}
			
		} catch (ParserConfigurationException pce) {
			throw new PresentacionException("ERROR al crear la instancia del parseador Utils.setDocument: "+pce.getMessage());
		}catch (XPathExpressionException xee) {
			throw new PresentacionException("ERROR al evaluar las expresiones xpath Utils.setDocument: "+xee.getMessage());
		} catch (Exception e) {
			if (!(e instanceof PresentacionException))
				throw new PresentacionException("EXCEPCIÓN generico Utils.setDocument: "+e.getMessage());
			else
				throw (PresentacionException)e;
		}
	}
	/**
	 * convierte un Documento DOM a String
	 * @param doc Documento {@link Document}
	 * @return Representación en texto del parámetro de entrada.
	 * @throws PresentacionException
	 */
	public final static String DOM2String(Document doc) throws PresentacionException{   
	    TransformerFactory transformerFactory =TransformerFactory.newInstance();   
	    Transformer transformer = null;   
	    try{   
	        transformer = transformerFactory.newTransformer();   
	    }catch (javax.xml.transform.TransformerConfigurationException error){   
	        throw new PresentacionException("Error al configurar la transformación. "+Utils.class.getName()+".DOM2String");
	    }   
	  
	    Source source = new DOMSource(doc);   
	    StringWriter writer = new StringWriter();   
	    Result result = new StreamResult(writer);   
	    try{   
	        transformer.transform(source,result);   
	    }catch (javax.xml.transform.TransformerException error){
	        throw new PresentacionException("Error al recuperar el transformador. "+Utils.class.getName()+".DOM2String");
	    }   
	    String s = writer.toString();   
	    return s;
	}

	public final static String eliminaFormatoImporte(String datos) {
		return datos.replace(".", "").replace(",", "");
	}

	public final static String eliminaFormatoFecha(String datos) {
		return datos.replace("/", "");
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
	 * Aligera el texto del xml de entrada, eliminando los datos de escritura y la firma digital.
	 * @param xml
	 * @return
	 * @throws PresentacionException
	 */
	public final static String aligerarXML(Document xml) throws PresentacionException
	{
		Document x=xml;
		x=eliminaEscritura(x);
		x=eliminaFirma(x);
		return Utils.DOM2String(x);
	}
	/**
	 * Elimina los datos de la escritura que ocupan mucho espacio, para manejar un XML más ligero.
	 * @param docEntrada XML de entrada.
	 * @return
	 */
	public static Document eliminaEscritura(Document docEntrada)
	{
		Document doc;
		doc=docEntrada;
		Element[] escritura = XMLUtils.selectNodes(doc,"//*[local-name()='declaracion']/*[local-name()='escritura']");
		if (escritura.length != 0) {
			Element firmaEscritura = XMLUtils.selectSingleNode(escritura[0],"*[local-name()='firmaEscritura']");
			Element docEscritura   = XMLUtils.selectSingleNode(escritura[0],"*[local-name()='documentoEscritura']");
			if (firmaEscritura!=null){
				//escritura[0].removeChild(firmaEscritura);
				XMLDOMUtils.setNodeText(doc, firmaEscritura, "");
			}
			if (docEscritura!=null)
			{
				//escritura[0].removeChild(docEscritura);
				XMLDOMUtils.setNodeText(doc, docEscritura, "");
			}
		}
		
		return doc;
	}
	/**
	 * Elimina la firma del mensaje de entrada.
	 * @param docEntrada Documento de entrada.
	 * @return
	 */
	public static Document eliminaFirma (Document docEntrada)
	{
		Element[] remesas = XMLUtils.selectNodes(docEntrada,"//*[local-name()='remesa']");
		if (remesas.length!=0)
		{
			Element firma = XMLUtils.selectSingleNode(remesas[0], "*[local-name()='Signature']");
			if (firma!=null)
			{
				remesas[0].removeChild(firma);
			}
		}
		return docEntrada;
	}
	/**
	 * Asigna el  NRC al nodo de pago, si hubiera.
	 * @param datos XML de salida al que se añadirá el nodo de NRC
	 * @param nrc número de NRC
	 * @return Representación en texto del documento XML de salida.
	 * @throws PresentacionException
	 */
	public final static String setNRCPago(String datos, String nrc) throws PresentacionException{
		Document resultadoXML;
		try {
			DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			fact.setNamespaceAware(true);
			DocumentBuilder db= fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 
			inStr.setCharacterStream(new java.io.StringReader(datos.toString()));
			try{
				resultadoXML = db.parse(inStr);
			}catch (java.io.IOException ex){
				throw new PresentacionException("ERROR de entrada/salida al parsear la respuesta Utils.setNRCPago: "+ex.getMessage());
			}catch (org.xml.sax.SAXException ex){
				throw new PresentacionException("ERROR al parsear la respuesta Utils.setNRCPago: "+ex.getMessage());
			}

			NodeList nl = XMLDOMUtils.getAllNodes(resultadoXML, "pago");
			if (nl.getLength()>0)
			{
				Node justificante = XMLDOMUtils.getFirstChildNodeNS(nl.item(0), "justificantePago",nl.item(0).getNamespaceURI());
				Node nodeNrc=resultadoXML.createElementNS(nl.item(0).getNamespaceURI(),nl.item(0).getPrefix()+":nrc");
				XMLDOMUtils.setNodeText(resultadoXML, nodeNrc, nrc);
				nl.item(0).insertBefore(nodeNrc, justificante);
				return document2String(resultadoXML);
			}
			else
			{
				throw new PresentacionException("No se ha podido insertar el NRC porque no se ha encontrado el nodo 'justificantePago' asociado");
			}
			
		} catch (ParserConfigurationException pce) {
			throw new PresentacionException("ERROR al crear la instancia del parseador Utils.setNRCPago: "+pce.getMessage());
		}catch (XPathExpressionException xee) {
			throw new PresentacionException("ERROR al evaluar las expresiones xpath Utils.setNRCPago: "+xee.getMessage());
		} catch (Exception e) {
			if (!(e instanceof PresentacionException))
				throw new PresentacionException("EXCEPCIÓN generico Utils.setNRCPago: "+e.getMessage());
			else
				throw (PresentacionException)e;
		}
	}
	/**
	 * Elimina el atributo "nillable" de un nodo. Utilizado para devolver un justificante de pago.
	 * @param nodo
	 */
	private static void removeNillable (Node nodo)
	{
		if (nodo!=null)
		{
			Node nilAttr=null;
			NamedNodeMap attrs = nodo.getAttributes();
			if (attrs!=null)
			{
				nilAttr=attrs.getNamedItemNS("http://www.w3.org/2001/XMLSchema-instance","nil");
				if (nilAttr!=null)
				{
					attrs.removeNamedItemNS("http://www.w3.org/2001/XMLSchema-instance","nil");
				}
			}
		}
	}
	/**
	 * Rellena una cadena con un caracter por la izquierda
	 * @param in Cadena de entrada
	 * @param tam Tamaño hasta el que rellenar
	 * @param padChar Caracter con el que rellenar.
	 * @return
	 */
	public static String lpad(String in, int tam, char padChar) {                
	    if (in.length() <= tam) {
	        char[] temp = new char[tam];
	        for(int i =0;i<tam;i++){
	            temp[i]= padChar;
	        }
	        int posIniTemp = tam-in.length();
	        for(int i=0;i<in.length();i++){
	            temp[posIniTemp]=in.charAt(i);
	            posIniTemp++;
	        }            
	        return new String(temp);
	    }
	    return "";
	}
}