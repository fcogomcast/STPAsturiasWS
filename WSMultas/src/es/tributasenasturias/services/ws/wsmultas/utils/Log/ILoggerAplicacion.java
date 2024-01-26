/**
 * 
 */
package es.tributasenasturias.services.ws.wsmultas.utils.Log;

/**
 * @author nbarragan
 *
 */
public interface ILoggerAplicacion {
	public void error (String msg);
	public void warning (String msg);
	public void debug (String msg);
	public void info (String msg);
}
