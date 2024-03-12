package es.tributasenasturias.utils.log;

/**
 * Mediante este objeto se permite la operaci�n de log, es decir, escribir a un fichero 
 * diferentes mensajes, con diferentes niveles de log (DEBUG, INFO, etc).
 * @author crubencvs
 *
 */
public class Logger implements ILog {
	private BaseLog log;
	/**
	 * Recupera la implementaci�n interna de log. No deber�a utilizarse.
	 * @return Instancia de {@link BaseLog}
	 */
	public BaseLog getLog() {
		return log;
	}
	/**
	 * Establece la implementaci�n interna de log. No deber�a utilizarse. Se construir� a trav�s
	 * de un {@link LogFactory}
	 * @param log Implementaci�n interna de log. Objeto {@link BaseLog}
	 */
	public void setLog(BaseLog log) {
		this.log = log;
	}
	/**
	 * Realiza la operaci�n de debug, si el nivel que se ha indicado al crear el {@link Logger} permite este nivel.
	 */
	@Override
	public void debug(String msg) {
		log.debug(msg);
	}
	/**
	 * Realiza log, con el nivel objetivo indicado por level. Este nivel objetivo se compara con el nivel
	 * indicado al crear el objeto, para comprobar si se puede imprimir.
	 */
	@Override
	public void doLog(String msg, NIVEL_LOG level) {
		log.doLog(msg,level);
	}
	/**
	 * Escribe un mensaje con nivel de error. Si se le pasa una excepci�n, indicar� informaci�n extra.
	 */
	@Override
	public void error(String msg, Throwable excepcion) {
		log.error(msg, excepcion);
	}
	/**
	 * Escribe un mensaje con nivel de error, si el nivel indicado al crear el {@link Logger} lo permite.
	 */
	@Override
	public void error(String msg) {
		log.error(msg);
	}
	/**
	 * Escribe un mensaje con nivel de informaci�n, si el nivel indicado al crear el {@link Logger} lo permite.
	 */
	@Override
	public void info(String msg) {
		log.info(msg);
	}
	/**
	 * Escribe una traza de la pila, si el nivel indicado al crear el {@link Logger} lo permite.
	 */
	@Override
	public void trace(StackTraceElement[] stackTraceElements) {
		log.trace(stackTraceElements);
	}
	/**
	 * Escribe un mensaje con nivel de traza, si el nivel indicado al crear el {@link Logger} lo permite.
	 */
	@Override
	public void trace(String msg) {
		log.trace(msg);
	}
	/**
	 * Escribe un mensaje con nivel de aviso, si el nivel indicado al crear el {@link Logger} lo permite.
	 */
	@Override
	public void warning(String msg) {
		log.warning(msg);
	}
	/**
	 * Constructor del objeto. Se ha de pasar una implementaci�n interna del log.
	 * @param log
	 */
	protected Logger(BaseLog log)
	{
		this.log = log;
	}
}
