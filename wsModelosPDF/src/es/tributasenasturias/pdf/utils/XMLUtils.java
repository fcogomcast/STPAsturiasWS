package es.tributasenasturias.pdf.utils;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUtils {

	/**
	 * Selecciona los elementos hijos del nodo que cumplan las condiciones de la expresion de XMpath
	 * @param doc
	 * @param nodename expresion XPath, o nombre del elemento a retornar
	 * @return
	 */
	public static Element[] selectNodes(Node doc, String nodename) {
		XPathFactory xpfact = XPathFactory.newInstance();
		XPath xp = xpfact.newXPath();
			try {
				if (doc == null)
					return new Element[0];
				NodeList o = (NodeList) xp.evaluate("//*[local-name()='"+nodename+"']", doc, XPathConstants.NODESET);
				Element[] result = new Element[o.getLength()];
				for (int i = 0; i < result.length; i++)
					result[i] = (Element) o.item(i);
				return result;
			} catch (XPathExpressionException e) {
				System.out.println(e.getMessage());
				return new Element[0];
			}
	}

	/**
	 * Selecciona el primer elementos hijo del nodo que cumplan las condiciones de la expresion de XMpath
	 * @param doc
	 * @param nodename expresion XPath, o nombre del elemento a retornar
	 * @return
	 */
	public static Element selectSingleNode(Node doc, String nodename,int pos) {
		if (doc != null) {
			Element[] result = selectNodes(doc, nodename);
			if (result.length > pos)
				return result[pos];
		}
		return null;
	}

	public static String getItemSimple(Node node,String name,String tipo,String def,boolean upper) {
		try{
			NodeList hijos = node.getChildNodes();
			for (int i=0; i<hijos.getLength(); i++) {
				if (hijos.item(i).getNodeType() == Node.ELEMENT_NODE && hijos.item(i).getLocalName().equalsIgnoreCase(name)){
					if (tipo!=null && hijos.item(i).getTextContent().toUpperCase().equalsIgnoreCase(tipo)) {
						return def;
					} else {
						if (upper)
							return hijos.item(i).getTextContent().toUpperCase();
						else
							return hijos.item(i).getTextContent();
					}
				}
			}
		}catch(Exception e){
			return "";
		}
		return "";
	}	

	public static String getItemIterador(Node node,String[] name,String[] tipo,String[] def,boolean upper,boolean acumular) {
		String valor="";
		StringBuilder sbuilder= new StringBuilder(512);
		try{
			NodeList hijos = node.getChildNodes();
			for (int i=0; i<hijos.getLength(); i++) {
				for (int j=0; j<name.length; j++) {
					if (hijos.item(i).getNodeType() == Node.ELEMENT_NODE && hijos.item(i).getLocalName().equalsIgnoreCase(name[j])){
						if (tipo!=null && hijos.item(i).getTextContent().toUpperCase().equalsIgnoreCase(tipo[j])) {
							return def[j];
						} else {
							if (upper)
								valor=hijos.item(i).getTextContent().toUpperCase();
							else
								valor=hijos.item(i).getTextContent();
							if (acumular){
								sbuilder.append(" ").append(valor);
							}
							else
								return valor;
						}
					}
				}
			}
		}catch(Exception e){
			return "";
		}
		if (acumular)
			return sbuilder.toString();
		return "";
	}	
}