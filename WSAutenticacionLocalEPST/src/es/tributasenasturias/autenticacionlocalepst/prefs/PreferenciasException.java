/**
 * 
 */
package es.tributasenasturias.autenticacionlocalepst.prefs;

/**
 * @author crubencvs
 *
 */
public class PreferenciasException extends Exception {

	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9145794724370952148L;
	
	public PreferenciasException(String error, Throwable original){
		super("PreferenciasException::"+error,original);
	}
	public PreferenciasException (String error)
	{
		super("PreferenciasException::"+error);
	}
	

}
