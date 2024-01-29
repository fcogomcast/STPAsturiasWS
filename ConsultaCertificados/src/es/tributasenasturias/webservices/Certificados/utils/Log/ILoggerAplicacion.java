/**
 * 
 */
package es.tributasenasturias.webservices.Certificados.utils.Log;

/**
 * @author crubencvs
 *
 */
public interface ILoggerAplicacion {
	public void error (String msg);
	public void warning (String msg);
	public void debug (String msg);
	public void info (String msg);
}
