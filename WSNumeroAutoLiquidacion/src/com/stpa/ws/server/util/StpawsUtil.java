package com.stpa.ws.server.util;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class StpawsUtil {

	static int hexcase = 1;
	static int chrsz = 8;
	
	/**
	 * Genera la mac con los datos de la peticion y 
	 * comprueba que sea igual a la pasada por parametro
	 * @param p_cliente Parametro de la peticion
	 * @param p_timestamp Parametro de la peticion
	 * @param p_modelo_autoliquidacion Parametro de la peticion
	 * @param p_libre Parametro de la peticion
	 * @param mac Parametro a validar 679E97E737C67B72191E474C24D4DAA617F5B416
	 * @return true si es correcta y false si no coincide
	 */
	public static boolean isMacValid(String p_cliente, String p_timestamp, 
			String p_modelo_autoliquidacion, String p_libre, String clave, String mac){
		boolean result = false;
		String s = SHAUtils.hex_sha1(p_cliente + p_timestamp + p_modelo_autoliquidacion + p_libre + clave);
		if(mac!=null && !"".equals(mac) && s!=null && !"".equals(s)){
			if(s.equals(mac)) result = true;
		}
		return result;
	}
	
	/**
	 * Genera la mac como resultado de la respuesta del ws.
	 * @param p_cliente Parametro de la peticion
	 * @param p_timestamp Parametro de la peticion
	 * @param p_modelo_autoliquidacion Parametro de la peticion
	 * @param p_libre Parametro de la peticion
	 * @param r_timestamp Parametro de la respuesta
	 * @param r_resultado Parametro de la respuesta
	 * @param r_numero_autoliquidacion Parametro de la respuesta
	 * @return La nueva mac generada para esta peticion y respuesta
	 */
	public static String genMac(String p_cliente, String p_timestamp, 
			String p_modelo_autoliquidacion, String p_libre, 
			String r_timestamp, String r_resultado, 
			String r_numero_autoliquidacion, String clave){
		return SHAUtils.hex_sha1(p_cliente + p_timestamp + p_modelo_autoliquidacion + 
				p_libre + r_timestamp + r_resultado + r_numero_autoliquidacion + clave);
	}
	
	public static String getTimeStamp(){
		Calendar ca = new GregorianCalendar();
		return ca.get(Calendar.DAY_OF_MONTH) + "/" + ca.get(Calendar.MONTH) + "/" + ca.get(Calendar.YEAR) + " " + ca.get(Calendar.HOUR) + ":" + ca.get(Calendar.MINUTE) + ":" + ca.get(Calendar.SECOND);
	}
	
	public static String calendarToString(Calendar ca){
		return ca.get(Calendar.DAY_OF_MONTH) + "/" + ca.get(Calendar.MONTH) + "/" + ca.get(Calendar.YEAR);
	}
}
