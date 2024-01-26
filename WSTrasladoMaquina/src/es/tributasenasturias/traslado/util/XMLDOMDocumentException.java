/**
 * 
 */
package es.tributasenasturias.traslado.util;

/**
 * @author crubencvs
 *
 */
public class XMLDOMDocumentException extends Exception {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2664701235777772460L;

	public XMLDOMDocumentException(String error, Throwable original) {
		super(error, original);
	}

	public XMLDOMDocumentException(String error) {
		super(error);
	}

}
