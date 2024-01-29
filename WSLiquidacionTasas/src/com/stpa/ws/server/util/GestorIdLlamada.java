package com.stpa.ws.server.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Gestionará los id de llamada. Asociará un id de llamada al identificador de hilo actual.
 * Dado que cada petición en el servidor está atendida por un hilo, permitirá que cada llamada
 * guarde constancia del identificador que necesita para insertar en los log.
 * @author crubencvs
 *
 */
public final class GestorIdLlamada {
	
	private GestorIdLlamada(){};
	private static Map<String,String> id = new HashMap<String,String>();
	/**
	 * Añade un Id de llamada
	 * @param clave
	 * @param idLlamada
	 */
	private static void addIdLlamada(String clave, String idLlamada)
	{
		synchronized(id)
		{
			id.put(clave, idLlamada);
		}
	}
	/**
	 * Elimina un id de llamada
	 * @param clave
	 */
	private static void removeIdLlamada (String clave)
	{
		synchronized(id)
		{
			id.remove(clave);
		}
	}
	/**
	 * Asocia un id de llamada con el identificador de hilo actual.
	 * @param idLlamada Identificador de llamada
	 */
	public static void asociarIdLlamada (String idLlamada)
	{
		addIdLlamada(String.valueOf(Thread.currentThread().getId()),idLlamada);
	}
	/**
	 * Desasocia cualquier id de llamada con el identificador de hilo actual.
	 */
	public static void desasociarIdLlamada()
	{
		removeIdLlamada(String.valueOf(Thread.currentThread().getId()));
	}
	/**
	 * Recupera el identificador de llamada asociado al hilo actual.
	 * @return
	 */
	public static String getIdLlamada()
	{
		synchronized (id)
		{
			String k=String.valueOf(Thread.currentThread().getId());
			if (id.containsKey(k))
			{
				return id.get(k);
			}
			return null;
		}
	}
	
}
