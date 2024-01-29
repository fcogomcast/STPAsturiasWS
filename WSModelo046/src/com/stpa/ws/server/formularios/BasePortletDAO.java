package com.stpa.ws.server.formularios;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.stp.webservices.ClienteWebServices;
import com.stpa.ws.pref.m42.Preferencias;
import com.stpa.ws.server.constantes.Modelo046Constantes;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.util.PropertiesUtils;
import com.stpa.ws.server.util.UtilsComunes;
import com.stpa.ws.server.util.XMLUtils;

public class BasePortletDAO {
	public static Preferencias pref = new Preferencias();
	
	protected static boolean validateString(String p_str) {
		return (p_str != null && !p_str.equals(""));
	}
	
	protected static Object[] findWsLanzador(String XMLIn, String[] paramsEstructuras, String[] paramsRecuperables, int numFilas,
			boolean condParamError) throws StpawsException {
		return findWsLanzador(XMLIn, paramsEstructuras, paramsRecuperables, numFilas, condParamError, "", "", "");
	}

	protected static Object[] findWsLanzador(String XMLIn, String[] paramsEstructuras, String[] paramsRecuperables,
			int numFilas, boolean condParamError, String nif, String nombre, String xmlCertificado)
			throws StpawsException {
		try {
			ClienteWebServices clienteWS = new ClienteWebServices();
			//if (ClienteWebServices.WSLANZADOR == null) {
				String WSLANZADOR_WSDL_URL = pref.getM_wslanzadorwsdlurl();
				String WSLANZADOR_NAMESPACE = pref.getM_wslanzadorservicenamespace();
				String WSLANZADOR_SERVICE_NAME = pref.getM_wslanzadorservicename();
				com.stpa.ws.server.util.Logger.debug("WSLANZADOR_WSDL_URL: " + WSLANZADOR_WSDL_URL,com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
				com.stpa.ws.server.util.Logger.debug("WSLANZADOR_NAMESPACE: " + WSLANZADOR_NAMESPACE,com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
				com.stpa.ws.server.util.Logger.debug("WSLANZADOR_SERVICE_NAME: " + WSLANZADOR_SERVICE_NAME,com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
				clienteWS.inicializarWSLanzador(WSLANZADOR_WSDL_URL, WSLANZADOR_SERVICE_NAME, WSLANZADOR_NAMESPACE);
			//}
			
			String XMLOut = XMLIn;
			String hostaddress = "";
			try {
				InetAddress addr = InetAddress.getLocalHost();
				hostaddress = addr.getHostAddress();
			} catch (UnknownHostException e) {
				com.stpa.ws.server.util.Logger.error("BasePortletDAO.findWsLanzador",e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			}
			com.stpa.ws.server.util.Logger.debug("Peticion: " + XMLIn,com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
			XMLOut = clienteWS.ejecutarWSLanzadorExecutePL(UtilsComunes.getEntornoBDD(), XMLIn, hostaddress, nif, nombre,
					xmlCertificado);
			com.stpa.ws.server.util.Logger.debug("Respuesta: " + XMLOut,com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
			HashMap<String, Object> map = XMLUtils.compilaXMLDoc(XMLOut, paramsEstructuras, paramsRecuperables, false);
			if (map != null && map.containsKey("ERROR")) {
				Object[] lstErrores = (Object[]) map.get("ERROR");
				String listaErrores = "";
				for (int i = 0; i < lstErrores.length; i++)
					listaErrores += lstErrores[i].toString() + " \n";
				com.stpa.ws.server.util.Logger.error("BasePortletDAO.findWsLanzador 2",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				throw new StpawsException(listaErrores,null);
			} else if (map != null) {
				Object[] lstKeys=map.keySet().toArray();
				Arrays.sort(lstKeys);
				int it=0;
				ArrayList<Object> lstValores = new ArrayList<Object>();
				while (lstKeys!=null && it<lstKeys.length) {
					String key = (String)lstKeys[it];
					it++;
					Object[] lstFilas = (Object[]) map.get(key);
					lstValores.ensureCapacity(lstValores.size() + lstFilas.length);
					for (int i = 0; i < lstFilas.length && i != numFilas; i++) {
						String[] lstProps = (String[]) lstFilas[i];
						if (lstProps == null || lstProps.length == 0)
							continue;
						String[] lstStrings = new String[lstProps.length];
						if (condParamError && validateString(lstProps[0])) {
							com.stpa.ws.server.util.Logger.error("BasePortletDAO.findWsLanzador 3",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
							throw new StpawsException(lstProps[0].toString(),null);
						}
						for (int j = 0; j < lstProps.length; j++) {
							lstStrings[j] = new String(lstProps[j]);
						}
						lstValores.add(lstStrings);
					}
				}
				
				Object[] lstResults = null;
				if(lstValores != null){					
					lstResults = lstValores.toArray();	
				}				
				return lstResults;
			}
			return null;

		} catch (MalformedURLException mfe) {
			com.stpa.ws.server.util.Logger.error("URL inválida",mfe,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"webservices.error.invalidurl"),mfe);
		}  catch (RemoteException re) {
			com.stpa.ws.server.util.Logger.error("Error de conexión",re,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"webservices.error.noconnect"),re);
		} catch (Exception wse) {
			com.stpa.ws.server.util.Logger.error("Error de conexión", wse,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(Modelo046Constantes.MSG_PROP,"webservices.error.noconnect"),wse);
		}
	}
}
