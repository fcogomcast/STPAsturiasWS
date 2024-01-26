/**
 * 
 */
package es.tributasenasturias.seguridad;

/**
 * @author crubencvs
 *
 */
public class XMLDOMDocumentException extends Exception {

	private static final long serialVersionUID = 1339131340168163L;

	/**
	 * 
	 */
	
	
	public XMLDOMDocumentException(String error, Throwable original) {
		super(error, original);
	}

	public XMLDOMDocumentException(String error) {
		super(error);
	}

}
