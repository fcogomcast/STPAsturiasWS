package es.tributasenasturias.ades.exceptions;

public class XadesException extends Exception {

	private static final String FIJO = "Error en firma Xades.";
	/**
	 * 
	 */
	private static final long serialVersionUID = -8401581926451641666L;

	public XadesException(String message, Throwable cause) {
		super(FIJO+message, cause);
	}

	public XadesException(String message) {
		super(FIJO+message);
	}

}
