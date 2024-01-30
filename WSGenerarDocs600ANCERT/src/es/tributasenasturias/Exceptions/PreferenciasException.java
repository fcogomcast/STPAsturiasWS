/**
 * 
 */
package es.tributasenasturias.Exceptions;

/**
 * @author crubencvs
 *
 */
public class PreferenciasException extends GenerarDocsANCERTException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3402340116336043150L;
	
	
	public PreferenciasException(String error, Throwable original){
		super(error,original);
	}
	public PreferenciasException (String error)
	{
		super(error);
	}
	

}
