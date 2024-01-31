package es.tributasenasturias.exceptions;

public class ConversionExcepcion extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8625056830277696338L;

	/**
	 * @param message
	 * @param cause
	 */
	public ConversionExcepcion(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ConversionExcepcion(String message) {
		super(message);
	}

}
