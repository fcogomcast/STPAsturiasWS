package es.tributasenasturias.business;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import es.tributasenasturias.exception.PresentacionException;
import es.tributasenasturias.modelo600utils.ConversorParametrosLanzador;
import es.tributasenasturias.modelo600utils.Preferencias;
import es.tributasenasturias.modelo600utils.log.ILoggable;
import es.tributasenasturias.modelo600utils.log.LogHelper;
import es.tributasenasturias.validacion.XMLDOMUtils;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPL;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLService;

public class AltaExpediente implements ILoggable{
	
	private String data;
	private String resultado; 
	private String codResultado;
	private String desResultado;
	private Preferencias pref = new Preferencias();
	private ConversorParametrosLanzador cpl;
	private LogHelper log;
	protected AltaExpediente() throws Exception{
			pref.CargarPreferencias();
	};
	
	public boolean Ejecuta () throws PresentacionException{
		if (this.data.isEmpty()) {
			return false;
		}
		try {
			// llamar al servicio PXLanzador para ejecutar el procedimiento almacenado de alta de expediente
			// Se prepara la llamada al procedimiento almacenado
			cpl = new ConversorParametrosLanzador();
	        cpl.setProcedimientoAlmacenado(pref.getPAAltaexpediente());
	        // xml
	        String xmlDatos = new String();
	        xmlDatos = ("<![CDATA[".concat(this.data)).concat("]]>");
	        //xmlDatos = "600";
	        cpl.setParametro(xmlDatos,ConversorParametrosLanzador.TIPOS.Clob);
	        // conexion oracle
	        cpl.setParametro("P",ConversorParametrosLanzador.TIPOS.String);            
	        	        	                    	    
	        LanzaPLService lanzaderaWS = new LanzaPLService();
	        LanzaPL lanzaderaPort;	

	        lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
	        // enlazador de protocolo para el servicio.
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort; 
			// Cambiamos el endpoint
			bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
			pref.getEndpointLanzador());
	        	        
	        String respuesta = new String();
	        try {	        	
	        	respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl.Codifica(), "", "", "", "");	        	
	        	cpl.setResultado(respuesta);
	        }catch (Exception ex) {
	        	throw new PresentacionException (ex.getMessage(),ex);
	        }
			String error = cpl.getNodoResultadoX("error");
			if (!error.equals("")){
				throw new PresentacionException(error);
			}
	        this.resultado = cpl.getNodoResultadoX("CLOB_DATA");
        	this.codResultado=getCodigoResultado();
	        if (!"00".equals(this.codResultado)){
	        	this.desResultado=getDesResultado();
	        	return false;
	        }else{
	        	return true;
	        }
		} catch (Exception e) {
			throw new PresentacionException ("Excepci�n en el alta de expediente:" + e.getMessage(),e);
		} 
	}
	
	
	private String getCodigoResultado() throws PresentacionException{
		try {			
			javax.xml.parsers.DocumentBuilder db;
			javax.xml.parsers.DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory
					.newInstance();
			db = fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource();
			inStr.setCharacterStream(new java.io.StringReader(this.resultado.toString()));
			org.w3c.dom.Document m_resultadoXML;
			m_resultadoXML = db.parse(inStr);															
			org.w3c.dom.NodeList nl= XMLDOMUtils.getAllNodesXPath(m_resultadoXML,"/*[local-name()='remesa']/*[local-name()='resultado']/*[local-name()='codigo']");
			String codigoResultado = new String();
			
			if (nl.getLength()>0)
				codigoResultado = nl.item(0).getTextContent();
			else
				codigoResultado = null;

			return codigoResultado;
		
		} catch (java.io.IOException ex) {
			throw new PresentacionException (this.getClass().getName()+".getCodigoResultado:Error de entrada/salida al recuperar el c�digo de resultado:" + ex.getMessage(),ex);
		} catch (SAXException saxe) {
			throw new PresentacionException (this.getClass().getName()+".getCodigoResultado:Error de SAX al recuperar c�digo de resultado:" + saxe.getMessage(),saxe);
		} catch (ParserConfigurationException pce) {
			throw new PresentacionException (this.getClass().getName()+".getCodigoResultado:Error de parser al recuperar el resultado:"+ pce.getMessage(),pce);
		} 						
	}
	private String getDesResultado() throws PresentacionException{
		try {			
			javax.xml.parsers.DocumentBuilder db;
			javax.xml.parsers.DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory
					.newInstance();
			db = fact.newDocumentBuilder();
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource();
			inStr.setCharacterStream(new java.io.StringReader(this.resultado.toString()));
			org.w3c.dom.Document m_resultadoXML;
			m_resultadoXML = db.parse(inStr);															
			org.w3c.dom.NodeList nl= XMLDOMUtils.getAllNodesXPath(m_resultadoXML,"/*[local-name()='remesa']/*[local-name()='resultado']/*[local-name()='descripcion']");
			String desResultado = new String();
			
			if (nl.getLength()>0)
				desResultado = nl.item(0).getTextContent();
			else
				desResultado = null;
			
			return desResultado;
		
		} catch (java.io.IOException ex) {
			throw new PresentacionException (this.getClass().getName()+".getDesResultado:Error de entrada/salida al recuperar el c�digo de resultado:" + ex.getMessage(),ex);
		} catch (SAXException saxe) {
			throw new PresentacionException (this.getClass().getName()+".getDesResultado:Error de SAX al recuperar c�digo de resultado:" + saxe.getMessage(),saxe);
		} catch (ParserConfigurationException pce) {
			throw new PresentacionException (this.getClass().getName()+".getDesResultado:Error de parser al recuperar el resultado:"+ pce.getMessage(),pce);
		} 						
	}
	
	public void setDatosXml (String s) {
			
		this.data = new String(s);
	}
	
	public String getResultado () {
		return this.resultado;
	}

	public String getTextResultado()
	{
		return this.desResultado;
	}
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