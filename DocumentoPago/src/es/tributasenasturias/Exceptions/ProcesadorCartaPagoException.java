/**
 * 
 */
package es.tributasenasturias.Exceptions;

/** Implementa la excepción a devolver en los procesadores de carta de pago.
 * @author crubencvs
 *
 */
public class ProcesadorCartaPagoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 520464406393382410L;
	
	public ProcesadorCartaPagoException(String error, Throwable original) {
		super(ProcesadorCartaPagoException.class.getName()+"::"+ error, original);
	}

	public ProcesadorCartaPagoException(String error) {
		super(ProcesadorCartaPagoException.class.getName()+"::"+ error);
	}

}
