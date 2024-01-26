package es.stpa.smartmultas.datos;

public class DatosException extends Exception{

	private static final long serialVersionUID = 2931040264373461343L;

	public DatosException(String message, Throwable cause) {
		super(message, cause);
	}

	public DatosException(String message) {
		super(message);
	}

}
