package es.tributasenasturias.excepciones;

public class RecepcionDocumentosException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5148852336866120802L;

	public RecepcionDocumentosException(String message, Throwable cause) {
		super(message, cause);
	}

	public RecepcionDocumentosException(String message) {
		super(message);
	}

}
