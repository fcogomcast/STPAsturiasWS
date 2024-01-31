package es.tributasenasturias.tarjetas.core.db;

/**
 * Excepciones producidas durante el inicio de pago por tarjeta.
 * En principio se supone que la operación ha de terminar correctamente, cualquier
 * otra circunstancia se trata como una excepción.
 * @author crubencvs
 *
 */
public class InicioPagoTarjetaException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7655732173259630486L;
	private String codigoError;
	private String mensajeError;
	
	
	/**
	 * @param codigoError
	 * @param mensajeError
	 */
	public InicioPagoTarjetaException(String codigoError, String mensajeError) {
		this(codigoError, mensajeError, null);
	}
	
	
	public InicioPagoTarjetaException (String codigoError, String mensajeError, Throwable t){
		super(mensajeError, t);
		this.codigoError= codigoError;
		this.mensajeError= mensajeError;
	}

	public InicioPagoTarjetaException(Throwable t){
		super(t);
	}

	/**
	 * @return the codigoError
	 */
	public final String getCodigoError() {
		return codigoError;
	}


	/**
	 * @return the mensajeError
	 */
	public final String getMensajeError() {
		return mensajeError;
	}
	
	
}
