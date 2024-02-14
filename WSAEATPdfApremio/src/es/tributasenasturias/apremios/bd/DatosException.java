package es.tributasenasturias.apremios.bd;

public class DatosException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4447386440986560619L;
	private static String msg= "Error en acceso a datos:";
	public DatosException(String message, Throwable cause) {
		super(msg+message, cause);
	}

	public DatosException(String message) {
		super(msg+message);
	}

	
}
