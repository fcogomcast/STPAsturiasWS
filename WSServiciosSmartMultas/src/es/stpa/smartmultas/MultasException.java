package es.stpa.smartmultas;

public class MultasException extends Exception{

	private static final long serialVersionUID = -1023822346130979719L;

	public MultasException(String message, Throwable cause) {
		super(message, cause);
	}

	public MultasException(String message) {
		super(message);
	}

	public MultasException(Throwable cause) {
		super(cause);
	}

}
