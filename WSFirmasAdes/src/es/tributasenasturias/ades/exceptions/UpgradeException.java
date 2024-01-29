package es.tributasenasturias.ades.exceptions;

public class UpgradeException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8542788561410013600L;

	public UpgradeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UpgradeException(String message) {
		super(message);
	}

}
