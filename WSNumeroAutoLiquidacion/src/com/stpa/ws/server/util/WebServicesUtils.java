package com.stpa.ws.server.util;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import com.stp.webservices.ClienteWebServices;
import com.stpa.ws.pref.na.Preferencias;
import com.stpa.ws.server.bean.NumAutoliquidacion;
import com.stpa.ws.server.bean.NumAutoliquidacionRespuesta;
import com.stpa.ws.server.constantes.NumAutoliquidacionConstantes;
import com.stpa.ws.server.exception.StpawsException;

public class WebServicesUtils {

	public static String generarPeticionNumeroSerie(NumAutoliquidacion na) throws StpawsException{
		XMLUtils xmlutils = new XMLUtils();
		xmlutils = iniGenerarPeticion(xmlutils,"INTERNET.NumeroSerie");
		// Modelo
		fillPeti(xmlutils,na.getPeticion().getP_modelo_autoliquidacion(),NumAutoliquidacionConstantes.WS_CADENA,null,"5");
		
		return finGenerarPeticion(xmlutils,6);
	}
	
	public static NumAutoliquidacion wsResponseNumeroSerie_AsList(String respuestaWebService, NumAutoliquidacion na) throws StpawsException {

		String[] columnasARecuperar = new String[] { "STRING_CADE" };
		String[] estructurasARecuperar = new String[] { "CADE_CADENA" };
		Map<String, Object> respuestaAsMap = new HashMap<String, Object>();
		try {
			respuestaAsMap = XMLUtils.compilaXMLDoc(respuestaWebService, estructurasARecuperar, columnasARecuperar, false);
		} catch (RemoteException re) {
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), re);
		}
		Object[] objcol = (Object[]) respuestaAsMap.get("CADE_CADENA");
		if(objcol!=null){
			com.stpa.ws.server.util.Logger.debug("Rellenamos objeto de respuesta...",com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
			NumAutoliquidacionRespuesta nar = na.getRespuesta();
			for (int i = 0; i < objcol.length; i++) {
				String[] objrow = (String[]) objcol[i];
				if (objrow!=null && !objrow[0].equals("")) {
					nar.setNumero_autoliquidacion(objrow[0]);
					nar.setResultado(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.ok"));
					nar.setTimestamp(StpawsUtil.getTimeStamp());
				}
			}
			com.stpa.ws.server.util.Logger.debug("Fin rellenado objeto de respuesta.",com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
		}
		
		return na;
	}
	
	public static String obtenerClave() throws StpawsException{
		String clave = "";
		String peticion = WebServicesUtils.generarPeticionClave();
		String xmlOut = WebServicesUtils.wsCall(peticion);
		try{
			clave = WebServicesUtils.wsResponseClave_AsList(xmlOut);
		}catch(Exception e){
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen.num"), e);
		}
		return clave;
	}
	
	public static String generarPeticionClave() throws StpawsException{
		XMLUtils xmlutils = new XMLUtils();
		xmlutils = iniGenerarPeticion(xmlutils,"INTERNET.obtenerclaveconsejerias");
		// Modelo
		//fillPeti(xmlutils,na.getPeticion().getP_modelo_autoliquidacion(),NumAutoliquidacionConstantes.WS_CADENA,null,"5");
		
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
			com.stpa.ws.server.util.Logger.debug("Rellenamos objeto de respuesta...",com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
			for (int i = 0; i < objcol.length; i++) {
				String[] objrow = (String[]) objcol[i];
				if (objrow!=null && !objrow[0].equals(""))
					clave = objrow[0];
			}
			com.stpa.ws.server.util.Logger.debug("Fin rellenado objeto de respuesta.",com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
		}
		
		return clave;
	}
	
	public static String wsCall(String peticion) throws StpawsException{
		//Preferencias pref = Preferencias.getInstance();
		Preferencias pref = new Preferencias();
		String xmlOut = "";
		ClienteWebServices clienteWS = new ClienteWebServices();
		try {
			//if (ClienteWebServices.WSLANZADOR == null) {
				String WSLANZADOR_WSDL_URL = pref.getM_wslanzadorwsdlurl();
				com.stpa.ws.server.util.Logger.debug("WSLANZADOR_WSDL_URL: " + WSLANZADOR_WSDL_URL,com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
				String WSLANZADOR_NAMESPACE = pref.getM_wslanzadorservicenamespace();
				com.stpa.ws.server.util.Logger.debug("WSLANZADOR_NAMESPACE: " + WSLANZADOR_NAMESPACE,com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
				String WSLANZADOR_SERVICE_NAME =pref.getM_wslanzadorservicename();
				com.stpa.ws.server.util.Logger.debug("WSLANZADOR_SERVICE_NAME: " + WSLANZADOR_SERVICE_NAME,com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
				clienteWS.inicializarWSLanzador(WSLANZADOR_WSDL_URL, WSLANZADOR_SERVICE_NAME, WSLANZADOR_NAMESPACE);
			//}
			InetAddress addr = InetAddress.getLocalHost();
			String hostaddress = addr.getHostAddress();
			String accesoWebservice = pref.getM_wslanzadorentornoBDD();
			com.stpa.ws.server.util.Logger.debug("accesoWebservice: " + accesoWebservice,com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
			com.stpa.ws.server.util.Logger.debug("Peticion:" + peticion,com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
			xmlOut = clienteWS.ejecutarWSLanzadorExecutePL(accesoWebservice, peticion, hostaddress, "", "", "");
			com.stpa.ws.server.util.Logger.debug("Respuesta:" + xmlOut,com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
		} catch (MalformedURLException mfe) {
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen.num"), mfe);
		} catch (Exception wse) {
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), wse);
		}
		return xmlOut;
	}
	
	private static XMLUtils iniGenerarPeticion(XMLUtils xmlutils, String accion) throws  StpawsException{
		try {
			xmlutils.crearXMLDoc();
		} catch (RemoteException re) {
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"),re);
		}
		xmlutils.crearNode("peti", "", null, null);
		xmlutils.reParentar(1);
		xmlutils.crearNode("proc","",new String[]{"nombre"},new String[]{accion});
		xmlutils.reParentar(1);

		// Parametros de cabecera
		//mac 2/2/10
		if(!accion.equals("INTERNET.obtenerclaveconsejerias")){
			fillPeti(xmlutils,"1","2",null,"1");
			fillPeti(xmlutils,"1","2",null,"2");
			fillPeti(xmlutils,"USU_WEB_SAC","1",null,"3");
			fillPeti(xmlutils,"33","2",null,"4");
		}
		
		return xmlutils;
	}
	
	private static String finGenerarPeticion(XMLUtils xmlutils, int posActual) throws StpawsException{
		// Identificacion       
		fillPeti(xmlutils,"P","1",null,String.valueOf(posActual));
		
		xmlutils.reParentar(-1);
		xmlutils.reParentar(-1);
		String xmlIn = "";
		try {
			xmlIn = xmlutils.informarXMLDoc();
		} catch (RemoteException re) {
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.gen"), re);
		}
		
		return xmlIn; 
	}
	
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
