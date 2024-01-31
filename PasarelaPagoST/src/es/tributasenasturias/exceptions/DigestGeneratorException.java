/**
 * 
 */
package es.tributasenasturias.exceptions;

/**
 * @author crubencvs
 *
 */
public class DigestGeneratorException extends PasarelaPagoException {

	private static final long serialVersionUID = 1852623682771147938L;
	
	public DigestGeneratorException(String error, Throwable throwable){
		super (error, throwable);
	}
	public DigestGeneratorException (String error)
	{
		super (error);
	}
	
}
