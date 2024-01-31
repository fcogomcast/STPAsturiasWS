package es.tributasenasturias.services.ws.WSConsultaPago048.utils.Log;

/**
 * 
 * @author noelianbb
 *
 */
public class Logger extends GenericAppLogger
{

	private final String LOG_FILE = "Application.log";
	private final String LOG_DIR = "WSConsultaPago048";

	public Logger()
	{
		this.setLogFile(LOG_FILE);
		this.setLogDir(LOG_DIR);
		this.setNombre("WSConsultaPago048"); //Nombre de proceso que aparecerá en el log.
	}
}
