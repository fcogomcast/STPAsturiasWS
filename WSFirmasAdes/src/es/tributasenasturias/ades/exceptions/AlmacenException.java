package es.tributasenasturias.ades.exceptions;

public class AlmacenException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4969187147991381451L;

	public AlmacenException(String message, Throwable cause) {
		super(message, cause);
	}

	public AlmacenException(String message) {
		super(message);
	}
	
	public AlmacenException(Throwable cause) {
		super(cause);
	}
}
