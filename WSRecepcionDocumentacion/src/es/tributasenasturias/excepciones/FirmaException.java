package es.tributasenasturias.excepciones;

public class FirmaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3446460341979738530L;

	public FirmaException(String message, Throwable cause) {
		super(message, cause);
	}

	public FirmaException(String message) {
		super(message);
	}

}
