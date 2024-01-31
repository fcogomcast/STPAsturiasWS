/**
 * 
 */
package es.tributasenasturias.webservices.context;

/**Implementa un factory para recuperar un contexto de llamada que se podr� utilizar en el servicio
 * para guardar informaci�n relacionada con esa llamada, como por ejemplo las preferencias a aplicar.
 * @author crubencvs
 *
 */
public final class CallContextManager {
	private CallContextManager(){};
	/**
	 * Recupera una instancia de CallContext para almacenar el contexto de la llamada.
	 * @return instancia {@link CallContext} 
	 */
	public static CallContext newCallContext()
	{
		return new CallContext();
	}
}
