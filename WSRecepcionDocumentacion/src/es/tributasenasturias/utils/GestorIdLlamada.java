package es.tributasenasturias.utils;

import java.util.HashMap;
import java.util.Map;


/**
 * Gestionar� los id de llamada. Asociar� un id de llamada al identificador de hilo actual.
 * Dado que cada petici�n en el servidor est� atendida por un hilo, permitir� que cada llamada
 * guarde constancia del identificador que necesita para insertar en los log.
 * El objetivo es que toda la informaci�n de contexto se pueda mantener con algo parecido a esto,
 * recuperando la  informaci�n de contexto de llamada sin que se pasen directamente los par�metros a 
 * cada una de las llamadas. Si fuera necesario un cambio importante de la pasarela se podr�a introducir. 
 * @author crubencvs
 *
 */
public final class GestorIdLlamada {
	
	//WARN: Cuidado con la concurrencia. Esto bloquea cada vez que se accede a un elemento. 
	private GestorIdLlamada(){};
	//FIXME: Comprobar si esta implementaci�n es la m�s eficiente.
	private static Map<String,String> id = new HashMap<String,String>();
	/**
	 * A�ade un Id de llamada
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
		//Comentado, cuando se implemente se incluir�
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

