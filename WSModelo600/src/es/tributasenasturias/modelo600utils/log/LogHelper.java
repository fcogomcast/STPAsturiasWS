package es.tributasenasturias.modelo600utils.log;

/**
 * Utilidad para facilitar los logs.
 * @author crubencvs
 */
public class LogHelper implements ILoggerAplicacion{

	private ILoggerAplicacion log;

	/**
	 * Constructor sin parámetros. No instancia el log, con lo que no se emitirán mensajes a través de la instancia.
	 */
	public LogHelper(){}

	/**
	 * Constructor al que se le pasa la ruta al log. Construirá una instancia de un logger para incluir los mensajes.
	 * @param pathToLog
	 */
	public LogHelper(String pathToLog) {
		this.log=new TributasLogger(pathToLog);
	}

	@Override
	public synchronized void debug(String msg) {
		if (log!=null){
			log.debug(msg);
		}
	}

	@Override
	public synchronized void doLog(String message, LEVEL level) {
		if (log!=null){
			log.doLog(message, level);
		}
	}

	@Override
	public synchronized void error(String msg) {
		if (log!=null){
			log.error(msg);
		}
	}

	@Override
	public synchronized void info(String msg) {
		if (log!=null){
			log.info(msg);
		}
	}

	@Override
	public synchronized void trace(StackTraceElement[] stackTraceElements) {
		if (log!=null){
			log.trace(stackTraceElements);
		}
	}

	@Override
	public synchronized void trace(String msg) {
		if (log!=null){
			log.trace(msg);
		}
	}

	@Override
	public synchronized void warning(String msg) {
		if (log!=null){
			log.warning(msg);
		}
	}
}
