package com.stpa.ws.server.formularios;

//import java.net.InetAddress;
//import java.net.MalformedURLException;
//import com.stp.webservices.ClienteWebServices;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.util.ClienteWebServices;
import com.stpa.ws.server.util.PropertiesUtils;
import com.stpa.ws.pref.lt.*;

public class GeneralWS {
	
	public static String TIPO_NUMBER = "2";
	public static String TIPO_VARCHAR2 = "1";
	public static String TIPO_CLOB = "1";
	
	public static String llamadaWebService(String peticion) throws Exception {
		
		Preferencias  pref = new Preferencias ();		
		try {
			pref.CargarPreferencias();								
		} catch (Exception e) {
			com.stpa.ws.server.util.Logger.errors("Error al cargar preferencias y plantilla al dar de alta el documento. "+e.getMessage(),null,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}
		
		/*ClienteWebServices clienteWS = new ClienteWebServices();*/
		String xmlOut;
		try {
			xmlOut = ClienteWebServices.executePL(pref.getM_wslanzadorentornoBDD(), peticion, "", "", "", "");		
		} catch (Exception wse) {
			throw new StpawsException(PropertiesUtils.getValorConfiguracion("com.stpa.ws.server.configuracion.messages","msg.err.conexion"), wse);
		}
		return xmlOut;
	}
}
