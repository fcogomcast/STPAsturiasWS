package es.tributasenasturias.exception;

public class PresentacionException extends BaseException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9213628138272308682L;
	public PresentacionException(String error, Throwable original) {
		super(error, original);
	}

	public PresentacionException(String error) {
		super(error);
	}

	
}
