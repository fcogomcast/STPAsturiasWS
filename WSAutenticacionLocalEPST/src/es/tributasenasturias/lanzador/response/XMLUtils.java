package es.tributasenasturias.lanzador.response;


import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.tributasenasturias.lanzador.LanzadorException;



public class XMLUtils {

	/**
	 * Parsea un xml que se incluye a trav�s de un string, y lo asigna al objeto documento 
	 * @param pXml. String que contiene XML.
	 * @return Documento Xml
	 * @throws XMLDOMDocumentException
	 */
	public static Document parseXml(String pXML) throws LanzadorException
	{
		javax.xml.parsers.DocumentBuilderFactory fact;
		javax.xml.parsers.DocumentBuilder db;
		Document doc=null;
		try
		{
			fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			fact.setNamespaceAware(true);
			db= fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource ();
			inStr.setCharacterStream(new java.io.StringReader(pXML));
			doc = db.parse(inStr);
			return doc;
		}
		catch (javax.xml.parsers.ParserConfigurationException ex)
		{
			throw new LanzadorException("Error al procesar el xml de respuesta del lanzador:"+ex.getMessage(),ex);
		}
		catch (org.xml.sax.SAXException ex)
		{
			throw new LanzadorException("Error al procesar el xml de respuesta del lanzador:"+ex.getMessage(),ex);
		}
		catch (java.io.IOException ex)
		{
			throw new LanzadorException("Error al procesar el xml de respuesta del lanzador:"+ex.getMessage(),ex);
		}
	}
	/**
	 * Recupera la lista de nodos que cumplen una condici�n XPATH.
	 * @param node {@link Node} en el que buscar.
	 * @param condicion Condici�n a tener en cuenta.
	 * @return Lista de nodos DOM que cumplan esa condicion coincida o null si no hay ninguno o hay error
	 * en la expresi�n.
	 */
	public static NodeList getAllNodesCondicion (Node node, String condicion)
	{
		NodeList list=null;
		try
		{
			//Compilamos una expresi�n XPath que devuelva todos los nodos con la condici�n
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression exp = xpath.compile(condicion);
			list = (NodeList)exp.evaluate(node, XPathConstants.NODESET);
		}
		catch (XPathExpressionException ex)
		{
			list=null;
		}
		return list;
	}
	/**
	 * Recupera un nodo que cumple una condici�n XPath
	 * @param nodo
	 * @param path
	 * @return el {@link Node} encontrado o null si no se encuentra ninguno.
	 */
	public static Node selectSingleNode (Node nodo, String condicion)
	{
		NodeList list;
		Node objetivo;
		list=getAllNodesCondicion(nodo,condicion);
		if (list!=null && list.getLength()>0)
		{
			objetivo=list.item(0);//El primero
		}
		else
		{
			objetivo=null;
		}
		return objetivo;
	}
	/**
	 * Recupera el n�mero de nodos que cumplen una  condici�n XPATH.
	 * @param node {@link Node} en el que buscar.
	 * @param condicion Condici�n a tener en cuenta.
	 * @return N�mero de nodos que cumplen la condici�n, como un entero.
	 */
	public static int selectNodesCount (Node node, String condicion)
	{
		Double list;
		try
		{
			//Compilamos una expresi�n XPath que devuelva todos los nodos con la condici�n
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression exp = xpath.compile(condicion);
			list = (Double)exp.evaluate(node, XPathConstants.NUMBER);
		}
		catch (XPathExpressionException ex)
		{
			list=0.0;
		}
		return list.intValue();
	}
}
