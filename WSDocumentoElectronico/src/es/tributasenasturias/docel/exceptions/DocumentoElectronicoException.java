package es.tributasenasturias.docel.exceptions;

public class DocumentoElectronicoException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7359523827278068674L;
	private static final String CAB="Error en proceso de documento electrónico:";
	public DocumentoElectronicoException(String message, Throwable cause) {
		super(CAB+message, cause);
	}

	public DocumentoElectronicoException(String message) {
		super(CAB+message);
	}

}
