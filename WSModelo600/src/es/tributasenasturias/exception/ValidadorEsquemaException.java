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
		super("Validación esquema:" + message, cause);
	}

	public ValidadorEsquemaException(String message) {
		super("Validación esquema:" + message);
	}

	public ValidadorEsquemaException(Throwable cause) {
		super(cause);
	}

}
