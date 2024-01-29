package es.tributasenasturias.ades.exceptions;

public class AdESException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -820126599973898863L;

	public AdESException(String message, Throwable cause) {
		super(message, cause);
	}

	public AdESException(String message) {
		super(message);
	}

}
