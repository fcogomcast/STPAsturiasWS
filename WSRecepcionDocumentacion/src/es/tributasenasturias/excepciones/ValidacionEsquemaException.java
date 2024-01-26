package es.tributasenasturias.excepciones;

import javax.xml.ws.WebServiceException;

import es.tributasenasturias.utils.Mensajes;

public class ValidacionEsquemaException extends WebServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3750482861848265607L;

	public ValidacionEsquemaException() {
		super();
	}

	public ValidacionEsquemaException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidacionEsquemaException(String message) {
		super(message);
	}

	public ValidacionEsquemaException(Throwable cause) {
		super(Mensajes.MSG_ERROR_F001+".Razón:"+cause.getMessage(),cause);
	}

}
