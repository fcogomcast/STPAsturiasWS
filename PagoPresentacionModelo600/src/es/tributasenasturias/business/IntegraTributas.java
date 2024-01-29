package es.tributasenasturias.business;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import es.tributasenasturias.Exceptions.PresentacionException;
import es.tributasenasturias.pagopresentacionmodelo600utils.ConversorParametrosLanzador;
import es.tributasenasturias.pagopresentacionmodelo600utils.Preferencias;
import es.tributasenasturias.webservice.pagopresentacion.log.ILoggable;
import es.tributasenasturias.webservice.pagopresentacion.log.LogHelper;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPL;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLService;

public class IntegraTributas implements ILoggable{

	private String data;
	private String resultado;
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
			respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl
					.Codifica(), "", "", "", "");																																							
			
			cpl.setResultado(respuesta);
			this.resultado = cpl.getNodoResultado("CLOB_DATA");																
			this.datoEspecifico = cpl.getNodoResultado("STRING1_CANU");
			
			log.debug ("Texto de resultado:" + getDesResultado());
			log.debug("Código de resultado:" + getCodigoResultado());
			// Ya no estamos comprobando si está duplicada. Si ya existía se da por bueno.
			//Chequeamos si está duplicada o no la autoliquidación.
			//if (cpl.getNodoResultado("STRING2_CANU").equals("S"))
			//	this.duplicado=true;
			//else 
			//	this.duplicado=false;
				 
		
			return true;
		} catch (Exception e) {
			throw new PresentacionException("Excepcion al integrar en Tributas::datos::"+cpl.Codifica() + e.getMessage(),e);
		}
	}

	public void setDatosXml(String s) {		
		this.data = new String(s);		
	}

	public String getResultado() {
		return this.resultado;
	}

	public String getDesResultado() {
		try {			
			javax.xml.parsers.DocumentBuilder db;
			javax.xml.parsers.DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory
					.newInstance();
			db = fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource();
			inStr.setCharacterStream(new java.io.StringReader(this.resultado.toString()));
			org.w3c.dom.Document m_resultadoXML;
			m_resultadoXML = db.parse(inStr);															
			org.w3c.dom.NodeList nl = m_resultadoXML.getElementsByTagName("descripcion");			
			String codigoResultado = new String();
			
			if (nl.getLength()>0)
				codigoResultado = nl.item(0).getTextContent();
			else
				codigoResultado = null;
			
			return codigoResultado;
		
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
	public String getNumAutoliquidacion() {

		try {
			javax.xml.parsers.DocumentBuilder db;
			javax.xml.parsers.DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory
					.newInstance();
			db = fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource();
			inStr.setCharacterStream(new java.io.StringReader(this.resultado
					.toString()));
			org.w3c.dom.Document m_resultadoXML;
			m_resultadoXML = db.parse(inStr);
			org.w3c.dom.NamedNodeMap nnm = m_resultadoXML.getElementsByTagName(
					"declaracion").item(0).getAttributes();
			org.w3c.dom.Node codigo = nnm.getNamedItem("codigo");
			return codigo.getNodeValue();
		} catch (java.io.IOException ex) {
			log.error("****Excepción de entrada/salida al recuperar el número de autoliquidacióin: " + ex.getMessage());
			return null;
		} catch (SAXException saxe) {
			log.error("****Excepción  del  SAXal recuperar el número de autoliquidacióin: "+ saxe.getMessage());
			return null;
		} catch (ParserConfigurationException pce) {
			log.error("****Excepción de parser al recuperar el número de autoliquidación: "+ pce.getMessage());
			return null;
		}
	}

	public String getNifSp() {
		try {
			javax.xml.parsers.DocumentBuilder db;
			javax.xml.parsers.DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory
					.newInstance();
			db = fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource();
			inStr.setCharacterStream(new java.io.StringReader(this.resultado
					.toString()));
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
							// Logger.info("Encuentra sujeto
							// pasivo"+nlInterviniente.item(j).getTextContent());
							return nlInterviniente.item(j).getTextContent();
						}
					}
				}
			}
			return null;
		} catch (java.io.IOException ex) {
			log.error("****Excepción de entrada/salida al recuperar el nif de sujeto pasivo: " + ex.getMessage());
			return null;
		} catch (SAXException saxe) {
			log.error("****Excepción en SAX al recuperar el nif de sujeto pasivo: " + saxe.getMessage());
			return null;
		} catch (ParserConfigurationException pce) {
			log.error("****Excepción al recuperar el nif de sujeto pasivo: " + pce.getMessage());
			return null;
		}
	}

	public String getNifPr() {
		try {
			javax.xml.parsers.DocumentBuilder db;
			javax.xml.parsers.DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory
					.newInstance();
			db = fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource();
			inStr.setCharacterStream(new java.io.StringReader(this.resultado
					.toString()));
			org.w3c.dom.Document m_resultadoXML;
			m_resultadoXML = db.parse(inStr);
			org.w3c.dom.NodeList nl = m_resultadoXML
					.getElementsByTagName("interviniente");
			for (int i = 0; i < nl.getLength(); i++) {
				org.w3c.dom.NamedNodeMap nnm = nl.item(i).getAttributes();
				org.w3c.dom.Node tipoInterviniente = nnm.getNamedItem("tipo");
				if (tipoInterviniente.getNodeValue().equalsIgnoreCase(
						"Presentador")) {
					// Se trata de un sujeto pasivo y se devuelve el nif.

					org.w3c.dom.NodeList nlInterviniente = nl.item(i)
							.getChildNodes();
					for (int j = 0; j < nlInterviniente.getLength(); j++) {
						if (nlInterviniente.item(j).getNodeName()
								.equalsIgnoreCase("nif")) {
							// Logger.info("Encuentra sujeto
							// pasivo"+nlInterviniente.item(j).getTextContent());
							return nlInterviniente.item(j).getTextContent();
						}
					}
				}
			}
			return null;
		} catch (java.io.IOException ex) {
			log.error("****Excepción de entrada/salida al recuperar el nif de presentador: " + ex.getMessage());
			return null;
		} catch (SAXException saxe) {
			log.error("****Excepción en SAX al recuperar el nif de presentador: " + saxe.getMessage());
			return null;
		} catch (ParserConfigurationException pce) {
			log.error("****Excepción de parser al recuperar el nif de presentador: " + pce.getMessage());
			return null;
		}
	}

	public String getNumTarjeta() {
		return cpl.getNodoResultado("NumTarjeta");
	}

	public String getFechaCaducidadTarjeta() {
		return cpl.getNodoResultado("FechaCaducidadTarjeta");
	}

	public String getCCC() {
		return cpl.getNodoResultado("ccc");
	}

	public String getNifOperante() {
		return cpl.getNodoResultado("NifOperante");
	}

	public String getNRC() {
		return cpl.getNodoResultado("nrc");
	}

	public String getFechaDevengo() {
		return cpl.getNodoResultado("FechaDevengo");
	}

	public String getImporte() {
		return cpl.getNodoResultado("TotalIngresar");
	}

	public String getCodNotario() {
		return cpl.getNodoResultado("codNotarioEscritura");
	}

	public String getCodNotaria() {
		return cpl.getNodoResultado("codNotariaEscritura");
	}

	public String getNumProtocolo() {
		return cpl.getNodoResultado("numProtocoloEscritura");
	}

	public String getProtocoloBis() {
		return cpl.getNodoResultado("protocoloBisEscritura");
	}

	public String getFirmaEscritura() {
		return cpl.getNodoResultado("firmaDocumentoEscritura");
	}

	public String getDocEscritura() {
		return cpl.getNodoResultado("documentoEscritura");
	}

	public String getFechaAutorizacion() {
		return cpl.getNodoResultado("fechaAutorizacionEscritura");
	}

	public String getDatosEspecifico() {
		return this.datoEspecifico;
	}
	
	public String getCodigoResultado() {
		try {			
			javax.xml.parsers.DocumentBuilder db;
			javax.xml.parsers.DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory
					.newInstance();
			db = fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource();
			inStr.setCharacterStream(new java.io.StringReader(this.resultado.toString()));
			org.w3c.dom.Document m_resultadoXML;
			m_resultadoXML = db.parse(inStr);															
			org.w3c.dom.NodeList nl = m_resultadoXML.getElementsByTagName("codigo");			
			String codigoResultado = new String();
			
			if (nl.getLength()>0)
				codigoResultado = nl.item(0).getTextContent();
			else
				codigoResultado = null;
			
			return codigoResultado;
		
		} catch (java.io.IOException ex) {
			log.error("****Excepción de entrada/salida al recuperar el código de resultado: " + ex.getMessage());
			return null;
		} catch (SAXException saxe) {
			log.error("****Excepción  en SAX al recuperar el código de resultado: " + saxe.getMessage());
			return null;
		} catch (ParserConfigurationException pce) {
			log.error("****Excepción de parser al recuperar el código de resultado: " + pce.getMessage());
			return null;
		} 						
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