package es.tributasenasturias.ades.exceptions;

public class PadesException extends Exception {

	private static final String FIJO = "Error en firma Pades.";
	/**
	 * 
	 */
	private static final long serialVersionUID = 4674564013850491733L;

	public PadesException(String message, Throwable cause) {
		super(FIJO+message, cause);
	}

	public PadesException(String message) {
		super(FIJO+message);
	}

}
