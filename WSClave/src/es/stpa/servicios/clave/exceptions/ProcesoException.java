package es.stpa.servicios.clave.exceptions;

public class ProcesoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7343948244906346443L;

	public ProcesoException(String message, Throwable cause) {
		super(ProcesoException.class.getName()+"::"+message, cause);
	}

	public ProcesoException(String message) {
		super(ProcesoException.class.getName()+"::"+message);
	}

	public ProcesoException(Throwable cause) {
		super(cause);
	}

}
