package es.tributasenasturias.tarjetas.core.db;

public class DBConnException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6639168033245769265L;

	/**
	 * @param message
	 * @param cause
	 */
	public DBConnException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public DBConnException(String message) {
		super(message);
	}

}
