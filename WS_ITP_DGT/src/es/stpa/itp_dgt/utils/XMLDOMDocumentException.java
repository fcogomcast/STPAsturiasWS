/**
 * 
 */
package es.stpa.itp_dgt.utils;

/**
 * @author crubencvs
 *
 */
public class XMLDOMDocumentException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8537207252879512969L;

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
