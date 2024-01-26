package es.stpa.notificagestionenvios.exceptions;

public class BDException extends Exception{


	/**
	 * 
	 */
	private static final long serialVersionUID = 493607888398373605L;

	public BDException(String message, Throwable cause) {
		super(message, cause);
	}

	public BDException(String message) {
		super(message);
	}

	public BDException(Throwable cause) {
		super(cause);
	}

}
