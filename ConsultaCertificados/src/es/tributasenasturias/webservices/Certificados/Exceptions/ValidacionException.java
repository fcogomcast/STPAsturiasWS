/**
 * 
 */
package es.tributasenasturias.webservices.Certificados.Exceptions;

/**
 * @author crubencvs
 *
 */
public class ValidacionException extends ConsultaCertificadosException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 4147434194820543376L;

	public ValidacionException(String error, Throwable original) {
		super(error, original);
	}

	public ValidacionException(String error) {
		super(error);
	}

}
