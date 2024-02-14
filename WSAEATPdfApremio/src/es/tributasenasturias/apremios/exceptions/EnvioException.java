package es.tributasenasturias.apremios.exceptions;

public class EnvioException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5124378260212992608L;

	public EnvioException(String message, Throwable cause) {
		super(message, cause);
	}

	public EnvioException(String message) {
		super(message);
	}

}
