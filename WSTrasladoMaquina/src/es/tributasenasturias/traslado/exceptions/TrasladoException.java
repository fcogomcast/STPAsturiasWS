package es.tributasenasturias.traslado.exceptions;

/**
 * Excepci�n propia de traslado de m�quinas, que se utilizar� para saber si la excepci�n es esperada
 * @author crubencvs
 *
 */
public class TrasladoException extends Exception{

	private static final long serialVersionUID = -3633669710102761808L;

	public TrasladoException(String message, Throwable cause) {
		super(message, cause);
	}

	public TrasladoException(String message) {
		super(message);
	}

}
