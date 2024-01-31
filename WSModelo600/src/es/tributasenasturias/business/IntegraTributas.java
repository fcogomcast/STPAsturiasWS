package es.tributasenasturias.business;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import es.tributasenasturias.exception.PresentacionException;
import es.tributasenasturias.modelo600utils.ConversorParametrosLanzador;
import es.tributasenasturias.modelo600utils.Preferencias;
import es.tributasenasturias.modelo600utils.log.ILoggable;
import es.tributasenasturias.modelo600utils.log.LogHelper;
import es.tributasenasturias.validacion.XMLDOMUtils;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPL;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLService;

public class IntegraTributas implements ILoggable{

	private String data;
	private String resultado;
	private Document resultadoDoc;
	private Preferencias pref = new Preferencias();
	private ConversorParametrosLanzador cpl;
	private String datoEspecifico;
	private LogHelper log;
	protected IntegraTributas() throws Exception{
		pref.CargarPreferencias();
	};

	public boolean Ejecuta() throws PresentacionException{
		if (this.data.isEmpty()) {
			return false;
		}
		try {
			// llamar al servicio PXLanzador para ejecutar el procedimiento
			// almacenado de integracion
			// Se prepara la llamada al procedimiento almacenado
			cpl = new ConversorParametrosLanzador();			
			cpl.setProcedimientoAlmacenado(pref.getPAIntegra());
			// xml			
			String xmlDatos = new String();			
			xmlDatos = ("<![CDATA[".concat(this.data)).concat("]]>");			
			// xmlDatos = "600";
			cpl.setParametro(xmlDatos, ConversorParametrosLanzador.TIPOS.Clob);
			// conexion oracle
			cpl.setParametro("P", ConversorParametrosLanzador.TIPOS.String);
				
			LanzaPLService lanzaderaWS = new LanzaPLService();
			LanzaPL lanzaderaPort;
			
			lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
			
			// enlazador de protocolo para el servicio.
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;
			
							
			// Cambiamos el endpoint
			bpr.getRequestContext().put(
					javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
					pref.getEndpointLanzador());
			
			String respuesta = new String();													
			respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl.Codifica(), "", "", "", "");																																							
			log.debug(respuesta);
			cpl.setResultado(respuesta);
			String error = cpl.getNodoResultadoX("error");
			if (!error.equals("")){
				throw new PresentacionException(error);
			}
			this.resultado = cpl.getNodoResultadoX("CLOB_DATA");
			this.resultadoDoc=XMLDOMUtils.parseXml(this.resultado);
			this.datoEspecifico = cpl.getNodoResultadoX("STRING1_CANU");
			
			log.debug ("Texto de resultado:" + getDesResultado());
			log.debug("Código de resultado:" + getCodigoResultado());
		
			return true;
		}catch (Exception e) {
			throw new PresentacionException("Excepcion al integrar en Tributas::datos::"+cpl.Codifica() + e.getMessage(),e);
		}
	}

	public void setDatosXml(String s) {		
		this.data = new String(s);		
	}

	public String getResultado() {
		return this.resultado;
	}

	/*public String getDesResultado() {
		try {			
			javax.xml.parsers.DocumentBuilder db;
			javax.xml.parsers.DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			db = fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource();
			inStr.setCharacterStream(new java.io.StringReader(this.resultado.toString()));
			org.w3c.dom.Document m_resultadoXML;
			m_resultadoXML = db.parse(inStr);															
			//org.w3c.dom.NodeList nl = m_resultadoXML.getElementsByTagName("descripcion");	
			//org.w3c.dom.NodeList nl= XMLDOMUtils.getAllNodesXPath(m_resultadoXML,"/*[local-name()='remesa']/*[local-name()='resultado']/*[local-name()='descripcion']");
			org.w3c.dom.NodeList nl= XMLDOMUtils.getAllNodesXPath(resultadoDoc,"/*[local-name()='remesa']/*[local-name()='resultado']/*[local-name()='descripcion']");
			String desResultado = new String();
			
			if (nl.getLength()>0)
				desResultado = nl.item(0).getTextContent();
			else
				desResultado = null;
			
			return desResultado;
		
		} catch (java.io.IOException ex) {
			log.error("****Excepción de entrada/salida al recuperar el código de resultado: " + ex.getMessage());
			return null;
		} catch (SAXException saxe) {
			log.error("****Excepción  SAX al recuperar el código de resultado: " + saxe.getMessage());
			return null;
		} catch (ParserConfigurationException pce) {
			log.error("****Excepción de parser al recuperar el código de resultado: " + pce.getMessage());
			return null;
		} 						
	}
	*/
	public String getNumAutoliquidacion() {

		
		org.w3c.dom.NodeList nl= XMLDOMUtils.getAllNodesXPath(resultadoDoc,"/*[local-name()='remesa']/*[local-name()='declaracion']");
		if (nl.getLength()>0){
			org.w3c.dom.NamedNodeMap nnm=nl.item(0).getAttributes();
			org.w3c.dom.Node codigo = nnm.getNamedItem("codigo_declaracion");
			if (codigo!=null)
			{
				return codigo.getNodeValue();
			}
			else
			{
				return "";
			}
		}else{
			return "";
		}
	}

	public String getNifSp() {
		org.w3c.dom.NodeList nl = XMLDOMUtils.getAllNodesXPath(resultadoDoc,"/*[local-name()='remesa']/*[local-name()='declaracion']/*[local-name()='listaIntervinientes']/*[local-name()='interviniente']");
		for (int i = 0; i < nl.getLength(); i++) {
			org.w3c.dom.NamedNodeMap nnm = nl.item(i).getAttributes();
			org.w3c.dom.Node tipoInterviniente = nnm.getNamedItem("tipo");
			if (tipoInterviniente.getNodeValue().equalsIgnoreCase("Sujeto Pasivo")) {
				// Se trata de un sujeto pasivo y se devuelve el nif.
				org.w3c.dom.NodeList nlInterviniente = nl.item(i).getChildNodes();
				for (int j = 0; j < nlInterviniente.getLength(); j++) {
					if (nlInterviniente.item(j).getNodeType()== org.w3c.dom.Node.ELEMENT_NODE)
					{
						if (nlInterviniente.item(j).getLocalName().equalsIgnoreCase("idInterviniente")) {
							return nlInterviniente.item(j).getTextContent();
						}
					}
				}
			}
		}
			return null;
	}

	public String getNifPr() {
		org.w3c.dom.NodeList nl = XMLDOMUtils.getAllNodesXPath(resultadoDoc,"/*[local-name()='remesa']/*[local-name()='declaracion']/*[local-name()='listaIntervinientes']/*[local-name()='interviniente']");
		for (int i = 0; i < nl.getLength(); i++) {
			org.w3c.dom.NamedNodeMap nnm = nl.item(i).getAttributes();
			org.w3c.dom.Node tipoInterviniente = nnm.getNamedItem("tipo");
			if (tipoInterviniente.getNodeValue().equalsIgnoreCase("Presentador")) {
				// Se trata de un sujeto pasivo y se devuelve el nif.
				org.w3c.dom.NodeList nlInterviniente = nl.item(i).getChildNodes();
				for (int j = 0; j < nlInterviniente.getLength(); j++) {
					if (nlInterviniente.item(j).getNodeType()== org.w3c.dom.Node.ELEMENT_NODE)
					{
						if (nlInterviniente.item(j).getLocalName().equalsIgnoreCase("idInterviniente")) {
							return nlInterviniente.item(j).getTextContent();
						}
					}
				}
			}
		}
		return null;
	}

	public String getNumTarjeta() {
		//return cpl.getNodoResultadoX("numTarjeta");
		return XMLDOMUtils.getNodeText(XMLDOMUtils.getFirstNode(resultadoDoc, "numTarjeta"));
	}

	public String getFechaCaducidadTarjeta() {
		//return cpl.getNodoResultadoX("fechaCaducidadTarjeta");
		return XMLDOMUtils.getNodeText(XMLDOMUtils.getFirstNode(resultadoDoc, "fechaCaducidadTarjeta"));
	}

	public String getCCC() {
		//return cpl.getNodoResultadoX("ccc");
		return XMLDOMUtils.getNodeText(XMLDOMUtils.getFirstNode(resultadoDoc, "ccc"));
	}

	public String getNifOperante() {
		//return cpl.getNodoResultadoX("idOperante");
		return XMLDOMUtils.getNodeText(XMLDOMUtils.getFirstNode(resultadoDoc, "idOperante"));
	}

	public String getNRC() {
		//return cpl.getNodoResultadoX("nrc");
		return XMLDOMUtils.getNodeText(XMLDOMUtils.getFirstNode(resultadoDoc, "nrc"));
	}

	public String getFechaDevengo() {
		//return cpl.getNodoResultadoX("fechaDevengo");
		return XMLDOMUtils.getNodeText(XMLDOMUtils.getFirstNode(resultadoDoc, "fechaDevengo"));
	}

	public String getImporte() {
		//return cpl.getNodoResultadoX("totalIngresar");
		return XMLDOMUtils.getNodeText(XMLDOMUtils.getFirstNode(resultadoDoc, "totalIngresar"));
	}

	public String getCodNotario() {
		//return cpl.getNodoResultadoX("codNotario");
		return XMLDOMUtils.getNodeText(XMLDOMUtils.getFirstNode(resultadoDoc, "codNotario"));
	}

	public String getNumProtocolo() {
		//return cpl.getNodoResultadoX("numProtocolo");
		return XMLDOMUtils.getNodeText(XMLDOMUtils.getFirstNode(resultadoDoc, "numProtocolo"));
	}

	public String getProtocoloBis() {
		//return cpl.getNodoResultadoX("numProtocoloBis");
		return XMLDOMUtils.getNodeText(XMLDOMUtils.getFirstNode(resultadoDoc, "numProtocoloBis"));
	}

	public String getFechaDocumento() {
		//return cpl.getNodoResultadoX("fechaDocumento");
		return XMLDOMUtils.getNodeText(XMLDOMUtils.getFirstNode(resultadoDoc, "fechaDocumento"));
	}

	public String getDatosEspecifico() {
		return this.datoEspecifico;
	}
	
	public String getCodNotarioEscritura() {
		NodeList nodos = XMLDOMUtils.getAllNodesXPath(resultadoDoc, "//*[local-name()='escritura']/*[local-name()='codNotario']");
		if (nodos.getLength()>0)
		{
			return XMLDOMUtils.getNodeText(nodos.item(0));
		}
		return "";
	}
	
	public String getCodNotariaEscritura() {
		NodeList nodos = XMLDOMUtils.getAllNodesXPath(resultadoDoc, "//*[local-name()='escritura']/*[local-name()='codNotaria']");
		if (nodos.getLength()>0)
		{
			return XMLDOMUtils.getNodeText(nodos.item(0));
		}
		return "";
	}

	public String getNumProtocoloEscritura() {
		NodeList nodos = XMLDOMUtils.getAllNodesXPath(resultadoDoc, "//*[local-name()='escritura']/*[local-name()='numProtocolo']");
		if (nodos.getLength()>0)
		{
			return XMLDOMUtils.getNodeText(nodos.item(0));
		}
		return "";
	}

	public String getProtocoloBisEscritura() {
		NodeList nodos = XMLDOMUtils.getAllNodesXPath(resultadoDoc, "//*[local-name()='escritura']/*[local-name()='numProtocoloBis']");
		if (nodos.getLength()>0)
		{
			return XMLDOMUtils.getNodeText(nodos.item(0));
		}
		return "";
	}

	public String getFechaAutorizacionEscritura() {
		NodeList nodos = XMLDOMUtils.getAllNodesXPath(resultadoDoc, "//*[local-name()='escritura']/*[local-name()='fechaAutorizacion']");
		if (nodos.getLength()>0)
		{
			return XMLDOMUtils.getNodeText(nodos.item(0));
		}
		return "";
	}
	
//	public String getCodigoResultado() {
//		try {			
//			javax.xml.parsers.DocumentBuilder db;
//			javax.xml.parsers.DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
//			db = fact.newDocumentBuilder();
//			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource();
//			inStr.setCharacterStream(new java.io.StringReader(this.resultado.toString()));
//			org.w3c.dom.Document m_resultadoXML;
//			m_resultadoXML = db.parse(inStr);															
//			org.w3c.dom.NodeList nl= XMLDOMUtils.getAllNodesXPath(m_resultadoXML,"/*[local-name()='remesa']/*[local-name()='resultado']/*[local-name()='codigo']");
//			String codigoResultado = new String();
//			
//			if (nl.getLength()>0)
//				codigoResultado = nl.item(0).getTextContent();
//			else
//				codigoResultado = null;
//			
//			return codigoResultado;
//		
//		} catch (java.io.IOException ex) {
//			log.error("****Excepción de entrada/salida al recuperar el código de resultado: " + ex.getMessage());
//			return null;
//		} catch (SAXException saxe) {
//			log.error("****Excepción  en SAX al recuperar el código de resultado: " + saxe.getMessage());
//			return null;
//		} catch (ParserConfigurationException pce) {
//			log.error("****Excepción de parser al recuperar el código de resultado: " + pce.getMessage());
//			return null;
//		} 						
//	}
	public String getCodigoResultado() {
		org.w3c.dom.NodeList nl= XMLDOMUtils.getAllNodesXPath(resultadoDoc,"/*[local-name()='remesa']/*[local-name()='resultado']/*[local-name()='codigo']");
		String codigoResultado = new String();
		
		if (nl.getLength()>0)
			codigoResultado = nl.item(0).getTextContent();
		else
			codigoResultado = null;
		
		return codigoResultado;
		
	}
	public String getDesResultado() {
		org.w3c.dom.NodeList nl= XMLDOMUtils.getAllNodesXPath(resultadoDoc,"/*[local-name()='remesa']/*[local-name()='resultado']/*[local-name()='descripcion']");
		String desResultado = new String();
		
		if (nl.getLength()>0)
			desResultado = nl.item(0).getTextContent();
		else
			desResultado = null;
		
		return desResultado;
		
	}
	@Override
	public void setLogger(LogHelper log)
	{
		this.log = log;
	}
	@Override 
	public LogHelper getLogger()
	{
		return log;
	}
}