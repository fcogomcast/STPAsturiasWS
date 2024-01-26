package es.stpa.notifica.adviser.exceptions;

public class AdviserException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8040377820796043987L;

	public AdviserException(String message, Throwable cause) {
		super(message, cause);
	}

	public AdviserException(String message) {
		super(message);
	}

	public AdviserException(Throwable cause) {
		super(cause);
	}

}
