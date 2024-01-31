package es.tributasenasturias.exceptions;

/**
 * Excepción cuando la fecha de anulación se ha excedido.
 * @author crubencvs
 *
 */
public class FechaAnulacionExcedida extends ValidacionesEntidadRemotaException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1543238059377895668L;

	/**
	 * @param message
	 * @param cause
	 */
	public FechaAnulacionExcedida(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public FechaAnulacionExcedida(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
