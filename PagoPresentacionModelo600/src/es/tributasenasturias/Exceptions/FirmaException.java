/**
 * 
 */
package es.tributasenasturias.Exceptions;

import es.tributasenasturias.Exceptions.BaseException;

/**
 * @author crubencvs
 *
 */
public class FirmaException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6533734080044276496L;

	public FirmaException(String error, Throwable original) {
		super(error, original);
	}

	public FirmaException(String error) {
		super(error);
	}

}
