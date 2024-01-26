package es.tributasenasturias.documentopagoutils;

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

import es.tributasenasturias.documentos.util.Logger;
//import es.tributasenasturias.documentopagoutils.Logger;
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
			Node codigoNode = (Node) xpath.evaluate("/resultado/codigo", resultadoXML, XPathConstants.NODE);
			Node descrNode = (Node) xpath.evaluate("/resultado/descripcion", resultadoXML, XPathConstants.NODE);
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
		
	 public static String xmlToString(Node node) {
	        try {
	            Source source = new DOMSource(node);
	            StringWriter stringWriter = new StringWriter();
	            Result result = new StreamResult(stringWriter);
	            TransformerFactory factory = TransformerFactory.newInstance();	            
	            Transformer transformer = factory.newTransformer();
	            transformer.setOutputProperty("omit-xml-declaration", "yes");
	            transformer.transform(source, result);	            	         
	            Logger.error("ERROR:"+stringWriter.getBuffer().toString());
	            return stringWriter.getBuffer().toString();
	        } catch (Exception e) {
	            Logger.trace(e.getStackTrace());
	        }
	        return null;
	    }

	 
	private static String document2String(Document doc)throws Exception{
        TransformerFactory tFactory = TransformerFactory.newInstance();        
        Transformer transformer = tFactory.newTransformer();
        transformer.setOutputProperty("cdata-section-elements", "no");
        //transformer.setOutputProperty("encoding", "ISO-8859-1");
        
        DOMSource source = new DOMSource(doc);
        StringWriter sw=new StringWriter();
        StreamResult result = new StreamResult(sw);
        transformer.transform(source, result);
        
        String xmlString=sw.toString();        
        return xmlString;                              
    } 
	

	public final static String setRespuestaOK(String datos, String descripcion, String codigo,String pdf) {
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
			Node codigoNode = (Node) xpath.evaluate("/resultado/codigo", resultadoXML, XPathConstants.NODE);
			Node descrNode = (Node) xpath.evaluate("/resultado/descripcion", resultadoXML, XPathConstants.NODE);
			Node descrModeloPDF = (Node) xpath.evaluate("/resultado/modelopdf", resultadoXML, XPathConstants.NODE);
			
			codigoNode.setTextContent(codigo);
			descrNode.setTextContent(descripcion);
			descrModeloPDF.setTextContent(pdf);
			
			return xmlToString(resultadoXML);
			
			//return document2String(resultadoXML);
			//return resultadoXML.toString();
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


	
	
}
