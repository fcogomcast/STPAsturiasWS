package es.tributasenasturias.business;

import es.tributasenasturias.utilsProgramaAyuda.Logger;
import es.tributasenasturias.utilsProgramaAyuda.Preferencias;
import es.tributasenasturias.webservices.clients.CifradoPA;
import es.tributasenasturias.webservices.clients.CifradoPA_Service;


public class Descifrar {
	
	private Preferencias pref = new Preferencias();
	private String m_respuesta = new String();
	// resultado 0 = OK, 1 = ERROR.
	private int i_resultado = 0;

	/**
	 * Descifrar Constructor de la clase, el propio constructor realiza la llamada al servicio de Cifrado_PA para descifrar.
	 *
	 * @param xmlData - Requiere un String que serán los datos a descifrar.
	 */
	public Descifrar(String xmlData) {
		try {
			pref.CargarPreferencias();
		} catch(Exception e) {
			Logger.error("Clase Descifrar - constructor Descifrar. "+e.getMessage());
			this.i_resultado = 1;
		}
		CifradoPA_Service cifradoWS = new CifradoPA_Service();
		CifradoPA cifradoPort;
		if (!pref.getEndpointCifrado().equals(""))
		{
			if (pref.getDebug().equals("1")) {
				Logger.debug ("Se utiliza el endpoint de cifrado: " + pref.getEndpointCifrado());
			}
			cifradoPort = cifradoWS.getCifradoPA();
			// enlazador de protocolo para el servicio.
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) cifradoPort; 
			// Cambiamos el endpoint
			bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointCifrado()); 
		}
		else
		{
			if (pref.getDebug().equals("1")) {
				Logger.debug ("Se utiliza el endpoint de cifrado por defecto: " + javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
			}
			cifradoPort =cifradoWS.getCifradoPA(); 				
		}
        
        
        try {
        	if (pref.getDebug().equals("1")) 
        		Logger.info("PASSWORD"+pref.getPasswordCifrado()+"XMLDATA:"+xmlData.substring(2));
        	this.m_respuesta = cifradoPort.descifrar(pref.getPasswordCifrado(), xmlData.substring(2));
        	
        }catch (Exception ex) {
        	if (pref.getDebug().equals("1")) {
        		Logger.error("CIFRADO: ".concat(ex.getMessage()));
        	}
        	this.i_resultado = 1;
        }
	}
	
	/**
	 * getResultado Método que devuelve el resultado del cifrado.
	 *
	 * @return String Devuelve el resultado del cifrado
	 */
	public String getResultado() {
		return this.m_respuesta;
	}
	
	public int getCodResultado() {
		return this.i_resultado;
	}
}
