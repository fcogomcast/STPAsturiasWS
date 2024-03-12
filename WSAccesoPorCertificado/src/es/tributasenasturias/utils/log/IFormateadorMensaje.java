package es.tributasenasturias.utils.log;

/**
 * Operaciones que debe soportar una clase que pretenda actuar como formateador de un {@link ILogWriter}
 * @author crubencvs
 *
 */
public interface IFormateadorMensaje {
	/**
	 * Formatea un mensaje.
	 * @param message Mensaje a formatear.
	 * @param nivel Nivel de mensaje de log. Esta informaci�n puede no usarse.
	 * @return Cadena con el mensaje formateado.
	 */
public String formatea (String message,NIVEL_LOG nivel);
}
