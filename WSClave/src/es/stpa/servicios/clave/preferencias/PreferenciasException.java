/**
 * 
 */
package es.stpa.servicios.clave.preferencias;

/**
 * @author crubencvs
 *
 */
public class PreferenciasException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 382734265706914772L;
	public PreferenciasException(String error, Throwable original){
		super("PreferenciasException::"+error,original);
	}
	public PreferenciasException (String error)
	{
		super("PreferenciasException::"+error);
	}
	

}
