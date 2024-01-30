package es.tributasenasturias.GenerarDocs600ANCERTutils;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.tributasenasturias.documentos.util.Logger;

public final class Utils {

	Utils () {}
	
	public final static String setErrores(String datos, String error, String codError) {
		Document resultadoXML;
		try {
			DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			DocumentBuilder db= fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 
			inStr.setCharacterStream(new java.io.StringReader(datos.toString()));
			try
			{
				resultadoXML = db.parse(inStr);
			}
			catch (java.io.IOException ex)
			{
				Logger.error("ERROR génerico al parsear la respuesta Utils.setErrores: "+ex.getMessage());
				return datos;
			}
			catch (org.xml.sax.SAXException ex)
			{
				Logger.error("ERROR al parsear la respuesta Utils.setErrores: "+ex.getMessage());
				return datos;
			}
			XPath xpath = XPathFactory.newInstance().newXPath();
			Node codigoNode = (Node) xpath.evaluate("/remesa/resultado/codigo", resultadoXML, XPathConstants.NODE);
			Node descrNode = (Node) xpath.evaluate("/remesa/resultado/descripcion", resultadoXML, XPathConstants.NODE);
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

	public final static String setDocumentoComparecencia(String datos, String doc) {
		Document resultadoXML;
		try {
			DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			DocumentBuilder db= fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 
			inStr.setCharacterStream(new java.io.StringReader(datos.toString()));

			resultadoXML = db.parse(inStr);
				
			XPath xpath = XPathFactory.newInstance().newXPath();
			Node codigoNode = (Node) xpath.evaluate("/remesa/declaracion/Presentacion/JustificanteComparecencia", resultadoXML, XPathConstants.NODE);
			if (codigoNode != null) {
				Logger.info(codigoNode.getNodeName()+"--"+doc);
			} else {
				Logger.info("retorna nulo"+datos+"--");
			}
			codigoNode.setNodeValue(doc);
			resultadoXML.getElementsByTagName("JustificanteComparecencia").item(0).setTextContent(doc);
			return document2String(resultadoXML);
		}
		catch (java.io.IOException ex)
		{
			Logger.error("ERROR génerico al parsear la respuesta Utils.setDocumentoComparecencia: "+ex.getMessage());
			return datos;
		}
		catch (org.xml.sax.SAXException ex)
		{
			Logger.error("ERROR al parsear la respuesta Utils.setDocumentoComparecencia: "+ex.getMessage());
			return datos;
		}			
		catch (ParserConfigurationException pce) {
			Logger.error("ERROR al crear la instancia del parseador Utils.setDocumentoComparecencia: "+pce.getMessage());
			return datos;
		}catch (XPathExpressionException xee) {
			Logger.error("ERROR al evaluar las expresiones xpath Utils.setDocumentoComparecencia: "+xee.getMessage());
			return datos;
		} catch (Exception e) {
			Logger.error("ERROR generico Utils.setDocumentoComparecencia: "+e.getMessage());
			return datos;
		}
	}
	
	
	public final static String setJustificantePresentacion(String datos, String doc) {
		Document resultadoXML;
		try {
			DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			DocumentBuilder db= fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 
			inStr.setCharacterStream(new java.io.StringReader(datos.toString()));

			resultadoXML = db.parse(inStr);
				
			XPath xpath = XPathFactory.newInstance().newXPath();
			Node codigoNode = (Node) xpath.evaluate("/remesa/declaracion/Presentacion/CertificadoPresentacionPago", resultadoXML, XPathConstants.NODE);
			if (codigoNode != null) {
				Logger.info(codigoNode.getNodeName()+"--"+doc);
			} else {
				Logger.info("retorna nulo"+datos+"--");
			}
			codigoNode.setNodeValue(doc);
			resultadoXML.getElementsByTagName("CertificadoPresentacionPago").item(0).setTextContent(doc);
			return document2String(resultadoXML);
		}
		catch (java.io.IOException ex)
		{
			Logger.error("ERROR génerico al parsear la respuesta Utils.setJustificantePresentacion: "+ex.getMessage());
			return datos;
		}
		catch (org.xml.sax.SAXException ex)
		{
			Logger.error("ERROR al parsear la respuesta Utils.setJustificantePresentacion: "+ex.getMessage());
			return datos;
		}			
		catch (ParserConfigurationException pce) {
			Logger.error("ERROR al crear la instancia del parseador Utils.setJustificantePresentacion: "+pce.getMessage());
			return datos;
		}catch (XPathExpressionException xee) {
			Logger.error("ERROR al evaluar las expresiones xpath Utils.setJustificantePresentacion: "+xee.getMessage());
			return datos;
		} catch (Exception e) {
			Logger.error("ERROR generico Utils.setJustificantePresentacion: "+e.getMessage());
			return datos;
		}
	}
	
	
	public final static String setJustificantePagoDocument(String datos, String doc) {
		Document resultadoXML;
		try {
			DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			DocumentBuilder db= fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 
			inStr.setCharacterStream(new java.io.StringReader(datos.toString()));
			try
			{
				resultadoXML = db.parse(inStr);
			}
			catch (java.io.IOException ex)
			{
				Logger.error("ERROR génerico al parsear la respuesta Utils.setDocument: "+ex.getMessage());
				return datos;
			}
			catch (org.xml.sax.SAXException ex)
			{
				Logger.error("ERROR al parsear la respuesta Utils.setDocument: "+ex.getMessage());
				return datos;
			}
			XPath xpath = XPathFactory.newInstance().newXPath();
			Node codigoNode = (Node) xpath.evaluate("/remesa/declaracion/Pago/JustificantePago", resultadoXML, XPathConstants.NODE);			
			codigoNode.setTextContent(doc);			
			return document2String(resultadoXML);
		} catch (ParserConfigurationException pce) {
			Logger.error("ERROR al crear la instancia del parseador Utils.setDocument: "+pce.getMessage());
			return datos;
		}catch (XPathExpressionException xee) {
			Logger.error("ERROR al evaluar las expresiones xpath Utils.setDocument: "+xee.getMessage());
			return datos;
		} catch (Exception e) {
			Logger.error("ERROR generico Utils.setDocument: "+e.getMessage());
			return datos;
		}
	}	
	
	
	public final static String DOM2String(Document doc)   
	{   
	    TransformerFactory transformerFactory =TransformerFactory.newInstance();   
	    Transformer transformer = null;   
	    try{   
	        transformer = transformerFactory.newTransformer();   
	    }catch (javax.xml.transform.TransformerConfigurationException error){   
	        Logger.error(error.getMessage());
	        return null;   
	    }   
	  
	    Source source = new DOMSource(doc);   
	  
	    StringWriter writer = new StringWriter();   
	    Result result = new StreamResult(writer);   
	    try{   
	        transformer.transform(source,result);   
	    }catch (javax.xml.transform.TransformerException error){   
	    	Logger.error(error.getMessage());   
	        return null;   
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
	
	private static String document2String(Document doc)throws Exception{
        TransformerFactory tFactory = TransformerFactory.newInstance();        
        Transformer transformer = tFactory.newTransformer();
        transformer.setOutputProperty("encoding", "ISO-8859-1");
        DOMSource source = new DOMSource(doc);
        StringWriter sw=new StringWriter();
        StreamResult result = new StreamResult(sw);
        transformer.transform(source, result);
        
        String xmlString=sw.toString();
        return xmlString;
        
    }
	public final static String getNumAutoliquidacion(String datos) {
		Document resultadoXML;
		try {
			DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			DocumentBuilder db= fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 
			inStr.setCharacterStream(new java.io.StringReader(datos.toString()));
			try
			{
				resultadoXML = db.parse(inStr);
			}
			catch (java.io.IOException ex)
			{
				Logger.error("ERROR de entrada/salida al parsear el documento de entrada para recuperar autoliquidación: "+ex.getMessage());
				return datos;
			}
			catch (org.xml.sax.SAXException ex)
			{
				Logger.error("ERROR al parsear el documento de entrada para recuperar autoliquidación: "+ex.getMessage());
				return datos;
			}
			XPath xpath = XPathFactory.newInstance().newXPath();
			Node numAuto = (Node) xpath.evaluate("/remesa/declaracion/@codigo", resultadoXML, XPathConstants.NODE);
			String numAutoliquidacion=null;
			if (numAuto!=null)
			{
			numAutoliquidacion = numAuto.getNodeValue();
			}
			return numAutoliquidacion;
		} catch (ParserConfigurationException pce) {
			Logger.error("ERROR al crear la instancia del parseador Utils.getNumAutoliquidacion: "+pce.getMessage());
			return null;
		}catch (XPathExpressionException xee) {
			Logger.error("ERROR al evaluar las expresiones xpath Utils.getNumAutoliquidacion: "+xee.getMessage());
			return null;
		} catch (Exception e) {
			Logger.error("ERROR generico Utils.getNumAutoliquidacion: "+e.getMessage());
			return null;
		}
	}
	public final static String insertarNodoResultado(String datos) {
		Document resultadoXML;
		try {
			DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			DocumentBuilder db= fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 
			inStr.setCharacterStream(new java.io.StringReader(datos.toString()));
			try
			{
				resultadoXML = db.parse(inStr);
			}
			catch (java.io.IOException ex)
			{
				Logger.error("ERROR de entrada/salida al parsear el documento de entrada para crear Nodos resultado: "+ex.getMessage());
				return datos;
			}
			catch (org.xml.sax.SAXException ex)
			{
				Logger.error("ERROR al parsear el documento de entrada para crear Nodos resultado: "+ex.getMessage());
				return datos;
			}
			XPath xpath = XPathFactory.newInstance().newXPath();
			Node remesa = (Node) xpath.evaluate("/remesa", resultadoXML, XPathConstants.NODE);
			if (remesa != null)
			{
				NodeList nodoResultado =remesa.getOwnerDocument().getElementsByTagName("resultado"); 
				if (nodoResultado.getLength()==0)
				{
					Node resultado = remesa.getOwnerDocument().createElement("resultado");
					Node codigo = remesa.getOwnerDocument().createElement("codigo");
					resultado.appendChild(codigo);
					Node descripcion = remesa.getOwnerDocument().createElement("descripcion");
					resultado.appendChild(descripcion);
					remesa.appendChild(resultado);
				}
			}
			return document2String(resultadoXML);
		} catch (ParserConfigurationException pce) {
			Logger.error("ERROR al crear la instancia del parseador Utils.getNumAutoliquidacion: "+pce.getMessage());
			return datos;
		}catch (XPathExpressionException xee) {
			Logger.error("ERROR al evaluar las expresiones xpath Utils.getNumAutoliquidacion: "+xee.getMessage());
			return datos;
		} catch (Exception e) {
			Logger.error("ERROR generico Utils.getNumAutoliquidacion: "+e.getMessage());
			return datos;
		}
	}
	public final static String borrarNodoFirma(String datos) {
		Document resultadoXML;
		try {
			DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			DocumentBuilder db= fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 
			inStr.setCharacterStream(new java.io.StringReader(datos.toString()));
			try
			{
				resultadoXML = db.parse(inStr);
			}
			catch (java.io.IOException ex)
			{
				Logger.error("ERROR de entrada/salida al parsear el documento de entrada para borrar nodo Firma: "+ex.getMessage());
				return datos;
			}
			catch (org.xml.sax.SAXException ex)
			{
				Logger.error("ERROR al parsear el documento de entrada para borrar nodo Firma: "+ex.getMessage());
				return datos;
			}
			XPath xpath = XPathFactory.newInstance().newXPath();
			Node remesa = (Node) xpath.evaluate("/remesa", resultadoXML, XPathConstants.NODE);
			/*if (remesa != null)
			{
				NodeList firma = remesa.getOwnerDocument().getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature");
				if (firma.getLength()>0)
				{
					remesa.removeChild(firma.item(0));
				}
			}*/
			
			Node signature = (Node) xpath.evaluate("/remesa/Signature",resultadoXML,XPathConstants.NODE);
			if (signature!=null)
			{
				remesa.removeChild(signature);
			}
			return document2String(resultadoXML);
		} catch (ParserConfigurationException pce) {
			Logger.error("ERROR al crear la instancia del parseador Utils.borrarNodoFirma: "+pce.getMessage());
			return datos;
		}catch (XPathExpressionException xee) {
			Logger.error("ERROR al evaluar las expresiones xpath Utils.borrarNodoFirma: "+xee.getMessage());
			return datos;
		} catch (Exception e) {
			Logger.error("ERROR generico Utils.borrarNodoFirma: "+e.getMessage());
			return datos;
		}
	}
	public final static String getNifSP(String datos) {
		Document resultadoXML;
		try {
			DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			DocumentBuilder db= fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 
			inStr.setCharacterStream(new java.io.StringReader(datos.toString()));
			try
			{
				resultadoXML = db.parse(inStr);
			}
			catch (java.io.IOException ex)
			{
				Logger.error("ERROR de entrada/salida al parsear el documento de entrada para recuperar nif SP: "+ex.getMessage());
				return datos;
			}
			catch (org.xml.sax.SAXException ex)
			{
				Logger.error("ERROR al parsear el documento de entrada para recuperar nif SP: "+ex.getMessage());
				return datos;
			}
			XPath xpath = XPathFactory.newInstance().newXPath();
			Node sp = (Node) xpath.evaluate("/remesa/declaracion/lista_intervinientes/interviniente[@tipo='Sujeto Pasivo']/nif", resultadoXML, XPathConstants.NODE);
			String nifSp=null;
			if (sp!=null)
			{
				nifSp = sp.getFirstChild().getNodeValue();
			}
			return nifSp;
		} catch (ParserConfigurationException pce) {
			Logger.error("ERROR al crear la instancia del parseador Utils.getNifSP: "+pce.getMessage());
			return datos;
		}catch (XPathExpressionException xee) {
			Logger.error("ERROR al evaluar las expresiones xpath Utils.getNifSP: "+xee.getMessage());
			return datos;
		} catch (Exception e) {
			Logger.error("ERROR generico Utils.getNifSP: "+e.getMessage());
			return datos;
		}
	}
	public final static String getNifPR(String datos) {
		Document resultadoXML;
		try {
			DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			DocumentBuilder db= fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 
			inStr.setCharacterStream(new java.io.StringReader(datos.toString()));
			try
			{
				resultadoXML = db.parse(inStr);
			}
			catch (java.io.IOException ex)
			{
				Logger.error("ERROR de entrada/salida al parsear el documento de entrada para recuperar nif PR: "+ex.getMessage());
				return datos;
			}
			catch (org.xml.sax.SAXException ex)
			{
				Logger.error("ERROR al parsear el documento de entrada para recuperar nif PR: "+ex.getMessage());
				return datos;
			}
			XPath xpath = XPathFactory.newInstance().newXPath();
			Node pr = (Node) xpath.evaluate("/remesa/declaracion/lista_intervinientes/interviniente[@tipo='Presentador']/nif", resultadoXML, XPathConstants.NODE);
			String nifPR=null;
			if (pr!=null)
			{
				nifPR = pr.getFirstChild().getNodeValue();
			}
			return nifPR;
		} catch (ParserConfigurationException pce) {
			Logger.error("ERROR al crear la instancia del parseador Utils.getNifPR: "+pce.getMessage());
			return datos;
		}catch (XPathExpressionException xee) {
			Logger.error("ERROR al evaluar las expresiones xpath Utils.getNifPr: "+xee.getMessage());
			return datos;
		} catch (Exception e) {
			Logger.error("ERROR generico Utils.getNifPR: "+e.getMessage());
			return datos;
		}
	}

}
