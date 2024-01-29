/**
 * 
 */
package es.tributasenasturias.services.ws.archivodigital.utils.Preferencias;

/**
 * @author crubencvs
 *
 */
public class PreferenciasException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3402340116336043150L;
	public PreferenciasException(String msg)
	{
		super(msg);
		
	}
	
	
	public PreferenciasException(String message, Throwable cause) {
		super(message, cause);
	}


	@Override
	public String toString() {
		return "PreferenciasException: " + super.getMessage();
	}

}
