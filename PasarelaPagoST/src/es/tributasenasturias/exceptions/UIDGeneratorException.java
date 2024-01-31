/**
 * 
 */
package es.tributasenasturias.exceptions;

/**
 * @author crubencvs
 *
 */
public class UIDGeneratorException extends PasarelaPagoException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9155468039348678033L;
	
	
	public UIDGeneratorException(String error, Throwable throwable){
		super (error, throwable);
	}
	public UIDGeneratorException (String error)
	{
		super (error);
	}
	
}
