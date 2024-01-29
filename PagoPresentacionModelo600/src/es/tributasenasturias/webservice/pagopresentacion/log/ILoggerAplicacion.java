/**
 * 
 */
package es.tributasenasturias.webservice.pagopresentacion.log;

/**Operaciones de log.
 * @author crubencvs
 *
 */
public interface ILoggerAplicacion {
	public enum LEVEL {NONE, ERROR,INFO, TRACE,WARNING, DEBUG,ALL}
	public void doLog(String message, LEVEL level);
	public void error (String msg);
	public void warning (String msg);
	public void debug (String msg);
	public void info (String msg);
	public void trace (String msg);
	public void trace(StackTraceElement[] stackTraceElements);
}
