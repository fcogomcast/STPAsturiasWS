package es.tributasenasturias.docs;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import es.tributasenasturias.utilsProgramaAyuda.Logger;

public class DatosInterviniente {
	private String nifpr;
	private String nifsp;
	private String xmldata;
	public DatosInterviniente(String xmlData) {
		this.nifpr = null;
		this.nifsp = null;
		this.xmldata = xmlData;
		obtenerDatosInterviniente();
	}

	private void obtenerDatosInterviniente() {
		try {
			javax.xml.parsers.DocumentBuilder db;
			javax.xml.parsers.DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory
					.newInstance();
			db = fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource();
			String xmlData = new String(this.xmldata);
			xmlData = xmlData.replace("\r\n", "").replace("\n", "");			
			inStr.setCharacterStream(new java.io.StringReader(xmlData));
			org.w3c.dom.Document m_resultadoXML;
			m_resultadoXML = db.parse(inStr);
			org.w3c.dom.NodeList nl = m_resultadoXML
					.getElementsByTagName("interviniente");
			for (int i = 0; i < nl.getLength(); i++) {
				org.w3c.dom.NamedNodeMap nnm = nl.item(i).getAttributes();
				org.w3c.dom.Node tipoInterviniente = nnm.getNamedItem("tipo");
				if (tipoInterviniente.getNodeValue().equalsIgnoreCase(
						"Sujeto Pasivo")) {
					// Se trata de un sujeto pasivo y se devuelve el nif.
					org.w3c.dom.NodeList nlInterviniente = nl.item(i)
							.getChildNodes();
					for (int j = 0; j < nlInterviniente.getLength(); j++) {
						if (nlInterviniente.item(j).getNodeName()
								.equalsIgnoreCase("nif")) {							
							this.nifsp = nlInterviniente.item(j)
									.getTextContent();
						}
					}
				} else if (tipoInterviniente.getNodeValue().equalsIgnoreCase(
						"Presentador")) {
					// Se trata de un sujeto pasivo y se devuelve el nif.
					org.w3c.dom.NodeList nlInterviniente = nl.item(i)
							.getChildNodes();
					for (int j = 0; j < nlInterviniente.getLength(); j++) {
						if (nlInterviniente.item(j).getNodeName()
								.equalsIgnoreCase("nif")) { //
							this.nifpr = nlInterviniente.item(j)
									.getTextContent();
						}
					}
				}

			}
		} catch (java.io.IOException ex) {
			Logger.error(ex.getMessage());
		} catch (SAXException saxe) {
			Logger.error(saxe.getMessage());
		} catch (ParserConfigurationException pce) {
			Logger.error(pce.getMessage());
		}
	}

	public String getNifSP() {
		return this.nifsp;
	}
	
	public String getNifPr() {
		return this.nifpr;
	}
}
