package es.tributasenasturias.tarjetas.core.db;

public class ConsultaPagoTarjetaException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2476547082151810032L;

	/**
	 * @param message
	 * @param cause
	 */
	public ConsultaPagoTarjetaException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public ConsultaPagoTarjetaException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ConsultaPagoTarjetaException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
