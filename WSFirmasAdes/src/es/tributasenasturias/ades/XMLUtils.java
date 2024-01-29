package es.tributasenasturias.ades;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLUtils {

	/**
	 * Recupera el texto de un documento XML sin formatear 
	 * @param doc Documento xml
	 * @return
	 */
	public static String getXMLText(Document doc) throws TransformerConfigurationException, TransformerException 
	{
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "no"); 
		ByteArrayOutputStream baos= new ByteArrayOutputStream();
		StreamResult result = new StreamResult(baos);
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, result);
		String txt= new String(baos.toByteArray(),Charset.forName("UTF-8"));
		return txt;
	}
	
	/**
	 * Lee un fichero XML y lo convierte a objeto Document
	 * @param fichero Fichero XML a cargar 
	 * @return Objeto Document
	 * @throws IOException
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 */
	public static Document readXmlFile(String fichero) throws IOException, SAXException, ParserConfigurationException {
		DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		return factory.newDocumentBuilder().parse(new File(fichero));
	}
	/**
	 * Lee una cadena conteniendo un XML y lo convierte a objeto Document.
	 * @param xmlString Cadena conteniendo un XML
	 * @return Document
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static Document string2Xml(String xmlString) throws IOException, SAXException, ParserConfigurationException {
		DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		ByteArrayInputStream bis= new ByteArrayInputStream(xmlString.getBytes(Charset.forName("UTF-8")));
		Document doc=factory.newDocumentBuilder().parse(bis);
		//Lo he almacenado en un document para poder cerrar el stream. En realidad al ser un ByteArrayInputStream no importa, pero me parece que es mejor estilo.
		bis.close();
		return doc;
	}
	/**
	 * Incluye el texto indicado como primer nodo texto del elemento. Si existe, lo sustituye.
	 * @param e Elemento del XML DOM
	 * @param texto Texto del nodo
	 */
	public static void setElementText (Element e, String texto) {
		Node hijo=null;
		for (int i=0;i<e.getChildNodes().getLength();i++) {
			hijo= e.getChildNodes().item(i);
			if (hijo.getNodeType()==Node.TEXT_NODE) {
				break;
			}
		}
		if (hijo==null) {
			hijo= e.getOwnerDocument().createTextNode(texto);
			e.appendChild(hijo);
		} else {
			hijo.setNodeValue(texto);
		}
	}
	/**
	 * Incluye el texto indicado como primer nodo texto del nodo. Si existe, lo sustituye
	 * @param n Nodo del XML DOM
	 * @param texto Texto a incluir
	 */
	public static void setNodeText(Node n, String texto)
	{
		setElementText((Element)n, texto);
	}
	/**
	 * Asigna un valor a un atributo de un nodo. El atributo no tiene namespace
	 * Si el atributo no existe se crea. 
	 * @param n Nodo al que asignar el atributo.
	 * @param nombreAtributo Nombre del atributo
	 * @param valorAtributo Valor del atributo.
	 */
	public static void setAttributeValue(Node n, String nombreAtributo, String valorAtributo) {
		NamedNodeMap atributos= n.getAttributes();
		Node atributo= atributos.getNamedItem(nombreAtributo);
		if (atributo!=null) {
			atributo.setNodeValue(valorAtributo);
		} else {
			Attr atr1= n.getOwnerDocument().createAttribute(nombreAtributo);
			atr1.setNodeValue(valorAtributo);
			n.getAttributes().setNamedItem(atr1);
		}
	}
	/**
	 * Recupera el texto de un nodo
	 * @param nodo Nodo del que recuperar el texto
	 * @return Texto del nodo 
	 */
	public static String getNodeText(Node nodo)
	{
		String valor="";
		Node hijo=null;
		if (nodo!=null)
		{
			if (nodo.hasChildNodes())
			{
				for (hijo=nodo.getFirstChild();hijo!=null;hijo=hijo.getNextSibling())
				{
					if (hijo.getNodeType()==Node.TEXT_NODE||hijo.getNodeType()==Node.CDATA_SECTION_NODE)
					{
						valor = hijo.getNodeValue();// El texto del nodo está en el nodo texto hijo.
						break;
					}
				}
			}
		}
		return valor;
	}
	/**
	 * Recupera un nodo en el path indicado, o null si no existe. En caso de coincidencias
	 * múltiples, devuelve el  primero
	 * @param nodo Nodo desde el que evaluar el path
	 * @param path path a valorar
	 * @return Nodo si se ha encontrado, o null
	 */
	public static Node selectSingleNode (Node nodo, String path)
	{
		NodeList list;
		Node objetivo;
		try
		{
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression exp = xpath.compile(path);
			list = (NodeList)exp.evaluate (nodo, XPathConstants.NODESET);
			if (list.getLength()>0)
			{
				objetivo=list.item(0);//El primero
			}
			else
			{
				objetivo=null;
			}
		}
		catch (XPathExpressionException ex)
		{
			objetivo=null;
		}
		return objetivo;
	}
}
