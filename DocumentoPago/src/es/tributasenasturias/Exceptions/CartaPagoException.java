/**
 * 
 */
package es.tributasenasturias.Exceptions;

/** Implementa la excepción a devolver en los procesadores de carta de pago.
 * @author crubencvs
 *
 */
public class CartaPagoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 520464405393382410L;
	
	public CartaPagoException(String error, Throwable original) {
		super(CartaPagoException.class.getName()+"::"+ error, original);
	}

	public CartaPagoException(String error) {
		super(CartaPagoException.class.getName()+"::"+ error);
	}

}
