package es.tributasenasturias.utils.log;

/**
 * Interfaz que implementar�n las clases que escriban realmente a fichero las l�neas de log.
 * @author crubencvs
 *
 */
public interface ILogWriter {

	/**
	 * Indica el formateador que se utilizar� para construir los mensajes a escribir.
	 * @param formateador
	 */
	public abstract void setFormateador(IFormateadorMensaje formateador);

	/**
	 * Recupera el formateador que se usa para construir los mensajes a escribir.
	 */
	public abstract IFormateadorMensaje getFormateador();

	/**
	 * M�todo que realiza log. Se especifica que sea sincronizado, aunque en realidad no se 
	 * espera que una instancia se ejecute en varios hilos, pero como no se sabe qu� hace
	 * el servidor de aplicaciones, se deja as� por si acaso.
	 * @param message Mensaje a logear
	 * @param level Nivel de mensajes.
	 */
	public abstract void doLog(String message, NIVEL_LOG level);

	/**
	 * Utilidad para generar una entrada de error si el nivel de mensaje que se indica en preferencias lo permite.
	 * @param msg Mensaje a mostrar.
	 */
	public abstract void error(String msg);

	/** 
	 * Utilidad para generar una entrada de depuraci�n si el nivel de mensaje que se indica en preferencias lo permite.
	 * @param msg Mensaje a mostrar.
	 */
	public abstract void debug(String msg);

	/**
	 * Utilidad para generar una entrada de informaci�n si el nivel de mensaje que se indica en preferencias lo permite.
	 * @param msg Mensaje a mostrar.
	 */
	public abstract void info(String msg);

	/**
	 * Utilidad para generar una entrada de alerta si el nivel de mensaje que se indica en preferencias lo permite.
	 * @param msg Mensaje a mostrar.
	 */
	public abstract void warning(String msg);

	/**
	 * Utilidad para generar una entrada de traza si el nivel de mensaje que se indica en preferencias lo permite.
	 * @param message Mensaje a mostrar.
	 */
	public abstract void trace(String message);

	/**
	 * Utilidad para generar una entrada de traza con la pila de errores de excepci�n si el nivel de mensaje que se indica en preferencias lo permite.
	 * @param stackTraceElements Pila de excepci�n.
	 */
	public abstract void trace(StackTraceElement[] stackTraceElements);
	/**
	 * Modifica el nivel de log objetivo, es el nivel de log que indicar� si algo se imprime o no.
	 */
	public abstract void setNivelLog (NIVEL_LOG nivel);

}