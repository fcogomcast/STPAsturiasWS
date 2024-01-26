package es.tributasenasturias.services.ws.wsmultas.utils.Log;

/**
 * 
 * @author noelianbb
 *
 */
public class Logger extends GenericAppLogger
{

	private final String LOG_FILE = "Application.log";
	private final String LOG_DIR = "proyectos//WSConsultaDoinDocumentos";

	public Logger()
	{
		this.setLogFile(LOG_FILE);
		this.setLogDir(LOG_DIR);
		this.setNombre("WSConsultaDoinDocumentos"); //Nombre de proceso que aparecerá en el log.
	}
}
