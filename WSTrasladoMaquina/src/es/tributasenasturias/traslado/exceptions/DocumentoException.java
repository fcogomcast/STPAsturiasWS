package es.tributasenasturias.traslado.exceptions;

/**
 * Se produce durante la impresión o reimpresión de documento.
 * @author crubencvs
 *
 */
public class DocumentoException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4109039692747875989L;

	public DocumentoException() {
		super();
	}

	public DocumentoException(String message, Throwable cause) {
		super("["+DocumentoException.class.getName()+"]:"+message, cause);
	}

	public DocumentoException(String message) {
		super("["+DocumentoException.class.getName()+"]:"+message);
	}

}
