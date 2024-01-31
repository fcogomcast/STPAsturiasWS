package es.tributasenasturias.exception;

public class ValidadorEsquemaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1351169766976183494L;

	public ValidadorEsquemaException() {
		super();
	}

	public ValidadorEsquemaException(String message, Throwable cause) {
		super("Validaci�n esquema:" + message, cause);
	}

	public ValidadorEsquemaException(String message) {
		super("Validaci�n esquema:" + message);
	}

	public ValidadorEsquemaException(Throwable cause) {
		super(cause);
	}

}
