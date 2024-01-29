package com.stpa.ws.server.util;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import com.stp.webservices.ClienteWebServices;
import com.stpa.ws.pref.m42.Preferencias;
import com.stpa.ws.server.constantes.Modelo046Constantes;
import com.stpa.ws.server.exception.StpawsException;

public class WebServicesUtil {
	
	

	public static String obtenerClave() throws StpawsException{
		String clave = "";
		String peticion = WebServicesUtil.generarPeticionClave();
		String xmlOut = WebServicesUtil.wsCall(peticion);
		try{
			clave = WebServicesUtil.wsResponseClave_AsList(xmlOut);
		}catch(Throwable e){
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen.num"), null);
		}
		return clave;
	}
	
	/**
	 * Obtiene el código de emisora que corresponde al modelo.
	 * @return
	 * @throws StpawsException
	 */
	public static String obtenerEmisora() throws StpawsException{
		String emisora = "";
		String peticion = WebServicesUtil.generarPeticionEmisora();
		String xmlOut = WebServicesUtil.wsCall(peticion);
		try{
			emisora = WebServicesUtil.wsResponseClave_AsList(xmlOut);
		}catch(Throwable e){
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen.num"), null);
		}
		return emisora;
	}
	
	private static String generarPeticionEmisora() throws StpawsException{
		XMLUtils xmlutils = new XMLUtils();
		xmlutils = iniGenerarPeticion(xmlutils,"INTERNET.emisor");
		//Cabecera
		fillPeti(xmlutils, "1","1" ,null , "1");
		fillPeti(xmlutils, "1","1" ,null , "2");
		fillPeti(xmlutils, "USU_WEB_SAC","1" ,null , "3");
		fillPeti(xmlutils, "33","1" ,null , "4");
		//Añadimos el parámetro de tipo de modelo.
		fillPeti(xmlutils, "046","1" ,null , "5");
		return finGenerarPeticion(xmlutils,6);
	}
	
	public static String generarPeticionClave() throws StpawsException{
		XMLUtils xmlutils = new XMLUtils();
		xmlutils = iniGenerarPeticion(xmlutils,"INTERNET.obtenerclaveconsejerias");
		return finGenerarPeticion(xmlutils,6);
	}
	
	public static String wsResponseClave_AsList(String respuestaWebService) throws StpawsException {

		String[] columnasARecuperar = new String[] { "STRING_CADE" };
		String[] estructurasARecuperar = new String[] { "CADE_CADENA" };
		Map<String, Object> respuestaAsMap = new HashMap<String, Object>();
		String clave = "";
		try {
			respuestaAsMap = XMLUtils.compilaXMLDoc(respuestaWebService, estructurasARecuperar, columnasARecuperar, false);
		} catch (RemoteException re) {
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), re);
		}
		Object[] objcol = (Object[]) respuestaAsMap.get("CADE_CADENA");
		if(objcol!=null){
			com.stpa.ws.server.util.Logger.debug("Rellenamos objeto de respuesta...",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			for (int i = 0; i < objcol.length; i++) {
				String[] objrow = (String[]) objcol[i];
				if (objrow!=null && !objrow[0].equals(""))
					clave = objrow[0];
			}
			com.stpa.ws.server.util.Logger.debug("Fin rellenado objeto de respuesta.",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}
		
		return clave;
	}
	
	/**
	 * @param peticion
	 * @return
	 * @throws StpawsException
	 */
	public static String wsCall(String peticion) throws StpawsException{
		com.stpa.ws.server.util.Logger.debug("Ini wsCall(String peticion)",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		Preferencias pref = new Preferencias();
		String xmlOut = "";
		ClienteWebServices clienteWS = new ClienteWebServices();
		try {
			//if (ClienteWebServices.WSLANZADOR == null) {
				String WSLANZADOR_WSDL_URL = pref.getM_wslanzadorwsdlurl();
				com.stpa.ws.server.util.Logger.debug("WSLANZADOR_WSDL_URL: "+WSLANZADOR_WSDL_URL,com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
				String WSLANZADOR_NAMESPACE = pref.getM_wslanzadorservicenamespace();
				com.stpa.ws.server.util.Logger.debug("WSLANZADOR_NAMESPACE: "+WSLANZADOR_NAMESPACE,com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
				String WSLANZADOR_SERVICE_NAME = pref.getM_wslanzadorservicename();
				com.stpa.ws.server.util.Logger.debug("WSLANZADOR_SERVICE_NAME: "+WSLANZADOR_SERVICE_NAME,com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
				clienteWS.inicializarWSLanzador(WSLANZADOR_WSDL_URL, WSLANZADOR_SERVICE_NAME, WSLANZADOR_NAMESPACE);
			//}
			InetAddress addr = InetAddress.getLocalHost();
			com.stpa.ws.server.util.Logger.debug("addr: "+addr,com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
			String hostaddress = addr.getHostAddress();
			com.stpa.ws.server.util.Logger.debug("hostaddress: "+hostaddress,com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
			String accesoWebservice = pref.getM_wslanzadorentornoBDD();
			com.stpa.ws.server.util.Logger.debug("accesoWebservice: "+accesoWebservice,com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
			com.stpa.ws.server.util.Logger.debug("peticion: "+peticion,com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
			xmlOut = clienteWS.ejecutarWSLanzadorExecutePL(accesoWebservice, peticion, hostaddress, "", "", "");
			com.stpa.ws.server.util.Logger.debug("Respuesta: "+xmlOut,com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
		} catch (MalformedURLException mfe) {
			com.stpa.ws.server.util.Logger.error(mfe.getMessage(),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"webservices.error.call.invalid.url"), mfe);
		} catch (Exception wse) {
			com.stpa.ws.server.util.Logger.error(wse.getMessage(),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"webservices.error.call.no.connect"), wse);
		}
		com.stpa.ws.server.util.Logger.debug("Fin wsCall(String peticion)",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return xmlOut;
	}
	
	/**
	 * @param xmlutils
	 * @param accion
	 * @return
	 * @throws StpawsException
	 */
	private static XMLUtils iniGenerarPeticion(XMLUtils xmlutils, String accion) throws  StpawsException{
		try {
			xmlutils.crearXMLDoc();
		} catch (RemoteException re) {
			com.stpa.ws.server.util.Logger.error(re.getMessage(),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"webservices.error.ini.generar.peticion"), re);
		}
		xmlutils.crearNode("peti", "", null, null);
		xmlutils.reParentar(1);
		xmlutils.crearNode("proc","",new String[]{"nombre"},new String[]{accion});
		xmlutils.reParentar(1);

		// Parametros de cabecera
		//mac 02/02/10
//		fillPeti(xmlutils,"1","2",null,"1");
//		fillPeti(xmlutils,"1","2",null,"2");
//		fillPeti(xmlutils,"USU_WEB_SAC","1",null,"3");
//		fillPeti(xmlutils,"33","2",null,"4");
		
		return xmlutils;
	}
	
	/**
	 * @param xmlutils
	 * @param posActual
	 * @return
	 * @throws StpawsException
	 */
	private static String finGenerarPeticion(XMLUtils xmlutils, int posActual) throws StpawsException{
		// Identificacion       
		fillPeti(xmlutils,"P","1",null,String.valueOf(posActual));
		
		xmlutils.reParentar(-1);
		xmlutils.reParentar(-1);
		String xmlIn = "";
		try {
			xmlIn = xmlutils.informarXMLDoc();
		} catch (RemoteException re) {
			com.stpa.ws.server.util.Logger.error(re.getMessage(),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"webservices.error.fin.generar.peticion"), re);
		}
		
		return xmlIn; 
	}
	
	/**
	 * @param xmlutils
	 * @param valor
	 * @param tipo
	 * @param formato
	 * @param orden
	 */
	private static void fillPeti(XMLUtils xmlutils, String valor, String tipo, String formato, String orden){
		xmlutils.crearNode("param", "", new String[] { "id" }, new String[] { orden });
		xmlutils.reParentar(1);
		xmlutils.crearNode("valor", valor);
		xmlutils.crearNode("tipo", tipo);
		if(formato!=null)
			xmlutils.crearNode("formato", formato);
		else
			xmlutils.crearNode("formato", "");
		xmlutils.reParentar(-1);
	}
}
