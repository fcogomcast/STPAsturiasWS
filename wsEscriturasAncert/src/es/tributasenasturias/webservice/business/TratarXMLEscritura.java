package es.tributasenasturias.webservice.business;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class TratarXMLEscritura {

	Document m_resultadoXML;
	public TratarXMLEscritura(String xmlData) throws ParserConfigurationException, IOException, SAXException {
			javax.xml.parsers.DocumentBuilder db;
			javax.xml.parsers.DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			db= fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 
			inStr.setCharacterStream(new java.io.StringReader(xmlData));
			this.m_resultadoXML = db.parse(inStr);
	}
	
	
	public String getNodoByName(String nodo) {
		return this.m_resultadoXML.getElementById("nodo").getNodeValue();
	}
}
