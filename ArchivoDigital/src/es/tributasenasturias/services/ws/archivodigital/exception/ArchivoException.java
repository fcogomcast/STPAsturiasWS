package es.tributasenasturias.services.ws.archivodigital.exception;

public class ArchivoException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 347013815183905556L;

	public ArchivoException(String message, Throwable cause) {
		super(message, cause);
	}

	public ArchivoException(String message) {
		super(message);
	}

	public ArchivoException(Throwable cause) {
		super(cause);
	}

}
