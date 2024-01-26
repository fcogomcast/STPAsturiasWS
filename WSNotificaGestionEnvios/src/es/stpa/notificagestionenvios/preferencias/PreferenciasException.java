/**
 * 
 */
package es.stpa.notificagestionenvios.preferencias;

/**
 * @author crubencvs
 *
 */
public class PreferenciasException extends Exception {

	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1389357315430535685L;
	public PreferenciasException(String error, Throwable original){
		super("PreferenciasException::"+error,original);
	}
	public PreferenciasException (String error)
	{
		super("PreferenciasException::"+error);
	}
	

}
