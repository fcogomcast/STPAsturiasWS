package com.stpa.ws.server.util;


/**
 * Genera id de sesi�n �nico.
 * 
 * @author crubencvs
 *
 */
public class GeneradorIdLlamada {
	private GeneradorIdLlamada(){};
	/**
	 * Genera un id de sesión único.
	 * @return
	 */
	public static String generaIdSesion()
	{
		java.util.UUID uid = java.util.UUID.randomUUID();
		return Integer.toHexString(uid.hashCode()); // UID para esta petici�n. Se añadirá al log.
	}
}
