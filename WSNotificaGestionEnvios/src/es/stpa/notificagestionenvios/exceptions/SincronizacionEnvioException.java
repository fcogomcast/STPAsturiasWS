package es.stpa.notificagestionenvios.exceptions;

public class SincronizacionEnvioException extends Exception{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4411504981589139805L;

	public SincronizacionEnvioException(String message, Throwable cause) {
		super(message, cause);
	}

	public SincronizacionEnvioException(String message) {
		super(message);
	}

	public SincronizacionEnvioException(Throwable cause) {
		super(cause);
	}

}
