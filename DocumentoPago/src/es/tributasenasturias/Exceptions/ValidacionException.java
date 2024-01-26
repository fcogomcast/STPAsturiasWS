/**
 * 
 */
package es.tributasenasturias.Exceptions;

/** Implementa la excepción a devolver en los validadores.
 * @author crubencvs
 *
 */
public class ValidacionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 520464406390382410L;
	
	public ValidacionException(String error, Throwable original) {
		super(ValidacionException.class.getName()+"::"+ error, original);
	}

	public ValidacionException(String error) {
		super(ValidacionException.class.getName()+"::"+ error);
	}

}
