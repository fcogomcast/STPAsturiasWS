package es.tributasenasturias.traslado.exceptions;

/**
 * Excepción propia de traslado de máquinas, que se utilizará para saber si la excepción es esperada
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
