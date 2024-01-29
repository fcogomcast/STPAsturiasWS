package com.stpa.ws.server.constantes;

public class LiquidacionTasasConstantes {
	
	public static final String WS_NUMERO = "2";
	public static final String WS_CADENA = "1";
	public static final String WS_FECHA = "3";
	public static final String WS_CLOB = "4";
	public static final String WS_FORMATO_FECHA = "dd/mm/rrrr";
	
	
	/**
	 * A � Generaci�n de la liquidación de la tasa.
	 */
	public static final String GENERACION_LIQUIDACION_TASA = "A";
	
	/**
	 * C � Consulta, devolver� una liquidaci�n generada a partir del NUMERO_UNICO.
	 */
	public static final String CONSULTA_LIQUIDACION_TASA = "C";
	
	/**
	 * R � Rechazar la notificaci�n.
	 */
	public static final String RECHAZAR_NOTIFICACION = "R";
	
	public static final String MSJ_PROP = "com.stpa.ws.server.configuracion.messages";
	public static final String CONF_ACCESS = "com.stpa.ws.server.configuracion.access";
	public static final String STPAWS_GEN = "com.stpa.ws.server.configuracion.stpaws";
}
