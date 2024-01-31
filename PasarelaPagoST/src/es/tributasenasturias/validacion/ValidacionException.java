package es.tributasenasturias.validacion;

public class ValidacionException extends Exception{

	/**
	 * @param message
	 * @param cause
	 */
	public ValidacionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ValidacionException(String message) {
		super(message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5247804164565718139L;

}
