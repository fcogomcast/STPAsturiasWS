/**
 * 
 */
package es.tributasenasturias.webservices.Certificados.utils.Log;

/**
 * @author crubencvs
 * 
 */



public class TributasLogger extends GenericAppLogger
{
	private static final String LOG_FILE = "Application.log";
	private static final String LOG_DIR = "proyectos/ConsultaCertificados";

	public TributasLogger()
	{
		this.setLogFile(LOG_FILE);
		this.setLogDir(LOG_DIR);
		this.setNombre("ConsultaCertificados"); //Nombre de proceso que aparecerá en el log.
	}
	
	
	
}
