package es.tributasenasturias.excepciones;

public class AutenticacionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8454502128441361696L;

	public AutenticacionException(String message, Throwable cause) {
		super(message, cause);
	}

	public AutenticacionException(String message) {
		super(message);
	}
	
}
