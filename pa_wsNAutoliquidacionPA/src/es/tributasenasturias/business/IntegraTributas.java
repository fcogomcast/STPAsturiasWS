package es.tributasenasturias.business;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;

import es.tributasenasturias.utilsProgramaAyuda.ConversorParametrosLanzador;
import es.tributasenasturias.utilsProgramaAyuda.Logger;
import es.tributasenasturias.utilsProgramaAyuda.Preferencias;
import es.tributasenasturias.webservices.clients.lanzadera.LanzaPL;
import es.tributasenasturias.webservices.clients.lanzadera.LanzaPLService;

public class IntegraTributas {
	
	private String data;
	private String resultado; 
	private Preferencias pref = new Preferencias();
	private ConversorParametrosLanzador cpl;
	private String ip;
	public IntegraTributas() {
		try {
			pref.CargarPreferencias();
		} catch (Exception e) {
			Logger.error("Error al cargar preferencias al integrar en tributas. "+e.getMessage());
		}
	};
	
	public boolean Ejecuta () {
		if (this.data.isEmpty()) {
			return false;
		}
		try {
			// llamar al servicio PXLanzador para ejecutar el procedimiento almacenado de integracion
			// Se prepara la llamada al procedimiento almacenado
			cpl = new ConversorParametrosLanzador();
			
			if (pref.getDebug().equals("1")) 
				Logger.info(pref.getPAIntegra());
	        
			cpl.setProcedimientoAlmacenado(pref.getPAIntegra());
	        // Conexion
	        cpl.setParametro("1",ConversorParametrosLanzador.TIPOS.Integer);
	        // Peticion
	        cpl.setParametro("1",ConversorParametrosLanzador.TIPOS.Integer);
	        // Uusuario
	        cpl.setParametro(new String("USU_WEB_SAC"),ConversorParametrosLanzador.TIPOS.String);
	        // Organismo
	        cpl.setParametro("33",ConversorParametrosLanzador.TIPOS.Integer);
	        // Tipo Captura
	        cpl.setParametro(new String("WS"),ConversorParametrosLanzador.TIPOS.String);
	        // nifPR
	        cpl.setParametro(new String("11438140X"),ConversorParametrosLanzador.TIPOS.String);
	        // xml
	        String xmlDatos = new String();
	        xmlDatos = ("<![CDATA[".concat(this.data)).concat("]]>");
	        //xmlDatos = "600";
	        cpl.setParametro(xmlDatos,ConversorParametrosLanzador.TIPOS.Clob);
	        // conexion oracle
	        cpl.setParametro("P",ConversorParametrosLanzador.TIPOS.String);            
	        //Origen PA
	        cpl.setParametro("PA",ConversorParametrosLanzador.TIPOS.String);
	        //Es una presentación de cita previa
	        if (this.ip!=null)
	        {
	        	//Es de cita previa
	        	cpl.setParametro("S",ConversorParametrosLanzador.TIPOS.String);
	        	//Y su IP es esta
	        	cpl.setParametro(ip,ConversorParametrosLanzador.TIPOS.String);
	        }
	        
	        LanzaPLService lanzaderaWS = new LanzaPLService();
	        LanzaPL lanzaderaPort;
			if (!pref.getEndpointLanzador().equals(""))
			{				
				if (pref.getDebug().equals("1")) { 
					Logger.debug ("Se utiliza el endpoint de lanzadera: " + pref.getEndpointLanzador());
				}
				
				lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
				// enlazador de protocolo para el servicio.
				javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort; 
				// Cambiamos el endpoint
				bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointLanzador()); 
			}
			else
			{
				if (pref.getDebug().equals("1")) {
					Logger.debug ("Se utiliza el endpoint de lanzadera por defecto: " + javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
				}
				lanzaderaPort =lanzaderaWS.getLanzaPLSoapPort(); 				
			}
	        
	        String respuesta = new String();
	        try {
	        	respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl.Codifica(), "", "", "", "");
	        	cpl.setResultado(respuesta);
	        }catch (Exception ex) {
	        	if (pref.getDebug().equals("1")) {
	        		Logger.error("LANZADOR: ".concat(ex.getMessage()));
	        	}
	        }
	        if (pref.getDebug().equals("1")) {
	        	Logger.info("respuesta: ".concat(respuesta));
	        }
	        this.resultado = cpl.getResultado();
	        return true;
		} catch (Exception e) {
			Logger.error("Excepcion generica"+e.getMessage());
			return false;
		}
	}
	
	public void setDatosXml (String s) {
		this.data = new String(s);
	}
	
	public String getDatosXml () {
		return this.data;
	}
	
	public String getResultado () {
		return this.resultado;
	}
	
	public String getNumAutoliquidacion() {
		return cpl.getNodoResultado("STRING_CAOR");
	}
	
	public String getError() {
		return cpl.getNodoResultado("STRING1_CANU");
	}
	
	
	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getNifSp() {
		try
		{
			javax.xml.parsers.DocumentBuilder db;
			javax.xml.parsers.DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			db= fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 
			
			String xmlData = new String(this.resultado);					
			xmlData = xmlData.replace("\r\n", "").replace("\n", "");
		
			inStr.setCharacterStream(new java.io.StringReader(xmlData));
			org.w3c.dom.Document m_resultadoXML;
			m_resultadoXML = db.parse(inStr);
			org.w3c.dom.NodeList nl = m_resultadoXML.getElementsByTagName("interviniente");
			for (int i=0; i<nl.getLength(); i++) {
				org.w3c.dom.NamedNodeMap nnm = nl.item(i).getAttributes();
				org.w3c.dom.Node tipoInterviniente = nnm.getNamedItem("tipo");
				if (tipoInterviniente.getNodeValue().equalsIgnoreCase("Sujeto Pasivo")) {
					// Se trata de un sujeto pasivo y se devuelve el nif.
					
					org.w3c.dom.NodeList nlInterviniente = nl.item(i).getChildNodes();
					for (int j=0; j<nlInterviniente.getLength(); j++) {
						if (nlInterviniente.item(j).getNodeName().equalsIgnoreCase("nif")) {
							//Logger.info("Encuentra sujeto pasivo"+nlInterviniente.item(j).getTextContent());
							return nlInterviniente.item(j).getTextContent();
						}
					}
				}
			}
			return null;
		}
		catch (java.io.IOException ex)
		{
			Logger.error(ex.getMessage());
			return null;
		} catch (SAXException saxe) {
			Logger.error(saxe.getMessage());
			return null;
		} catch (ParserConfigurationException pce) {
			Logger.error(pce.getMessage());
			return null;
		}
	}
	
	public String getNumTarjeta() {
		return cpl.getNodoResultado("NumTarjeta");
	}
	
	public String getFechaCaducidadTarjeta() {
		return cpl.getNodoResultado("FechaCaducidadTarjeta");
	}
	
	public String getCCC () {
		return cpl.getNodoResultado("ccc");
	}
	
	public String getNifOperante() {
		return cpl.getNodoResultado("NifOperante");
	}
	
	public String getNRC () {
		return cpl.getNodoResultado("nrc");
	}
	
	public String getFechaDevengo () {
		return cpl.getNodoResultado("FechaDevengo");
	}
	
	public String getImporte () {
		return cpl.getNodoResultado("TotalIngresar");
	}
	
	public void setNumAutoliquidacion(String numAutoliquidacion) {
		try
		{			
			javax.xml.parsers.DocumentBuilder db;
			javax.xml.parsers.DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			db= fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 		
			String xmlData = new String(this.data);						
			xmlData = xmlData.replace("\r\n", "").replace("\n", "");
			
			inStr.setCharacterStream(new java.io.StringReader(xmlData));
			org.w3c.dom.Document m_resultadoXML;			
			org.w3c.dom.Element elemento;		
								
			m_resultadoXML = db.parse(inStr);
			org.w3c.dom.NodeList nl = m_resultadoXML.getElementsByTagName("declaracion");		
			elemento = (org.w3c.dom.Element) nl.item(0);
			elemento.setAttribute("codigo", numAutoliquidacion);				
						
			//Convertimos el documento XML  a string.
			try {
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				StreamResult result = new StreamResult(new java.io.StringWriter());			
				DOMSource source = new DOMSource(m_resultadoXML);			
				transformer.transform(source, result);
				String xmlString = result.getWriter().toString();
				this.data=xmlString;
			}
			catch (Exception e) {
				Logger.error("Error al convertir a String:" + e.getMessage());
			}
																									
		}
		catch (java.io.IOException ex) {
			Logger.error(ex.getMessage());			
		} catch (SAXException saxe) {
			Logger.error(saxe.getMessage());			
		} catch (ParserConfigurationException pce) {
			Logger.error(pce.getMessage());			
		}
	}
	

	
}
