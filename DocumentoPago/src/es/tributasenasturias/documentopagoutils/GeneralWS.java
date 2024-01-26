package es.tributasenasturias.documentopagoutils;

import es.tributasenasturias.soap.handler.HandlerUtil;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivo;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivoService;

public class GeneralWS {
	
	public static String TIPO_NUMBER = "2";
	public static String TIPO_VARCHAR2 = "1";
	public static String TIPO_CLOB = "1";

public static String llamadaWebService(String peticion) throws Exception {
		String xmlRespuesta="";		
		Preferencias  pref = new Preferencias ();
		
		try {
			pref.CargarPreferencias();
									
		} catch (Exception e) {
			Logger.debug("Error al cargar preferencias y plantilla al dar de alta el documento. "+e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
				
		try {									
		

			LanzaPLMasivoService lanzaderaWS = new LanzaPLMasivoService();					
			LanzaPLMasivo lanzaderaPort;			
			lanzaderaPort = lanzaderaWS.getLanzaPLMasivoSoapPort();

			// enlazador de protocolo para el servicio.
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;
			
			// Cambiamos el endpoint
			bpr.getRequestContext().put(
					javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
					pref.getEndpointLanzador());

	        //Vinculamos con el Handler	        
	        //HandlerUtil.setHandlerClient((javax.xml.ws.BindingProvider) lanzaderaPort);	        				
	        xmlRespuesta = lanzaderaPort.executePL(pref.getEntorno(), peticion, "", "", "", "");
					
		} catch (Exception wse) {
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("es.tributasenasturias.configuracion.messages","msg.err.conexion"), wse);
		}
				
		return xmlRespuesta;				
	}	
	
	
}
