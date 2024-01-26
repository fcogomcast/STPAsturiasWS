package es.tributasenasturias.seguridad;


public class SeguridadException extends Exception {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1385259774619731369L;

	/**
	 * 
	 */

	public SeguridadException(String error, Throwable original) {
		super(SeguridadException.class.getName()+"::" +  error, original);
	}

	public SeguridadException(String error) {
		super(SeguridadException.class.getName()+"::"+ error);
	}

}
