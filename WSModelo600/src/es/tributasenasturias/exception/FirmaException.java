/**
 * 
 */
package es.tributasenasturias.exception;

import es.tributasenasturias.exception.ConsultaCertificadosException;

/**
 * @author crubencvs
 *
 */
public class FirmaException extends ConsultaCertificadosException {

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
