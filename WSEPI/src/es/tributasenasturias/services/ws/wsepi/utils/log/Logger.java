package es.tributasenasturias.services.ws.wsepi.utils.log;

/**
 * 
 * @author noelianbb
 *
 */
public class Logger extends GenericAppLogger
{

	public Logger(String idLlamada, String dirname, String filename)
	{
		this.setLogFile(filename);
		this.setLogDir(dirname);
		this.setNombre("WSEPI"); //Nombre de proceso que aparecer� en el log.
		this.setIdLlamada(idLlamada);
	}
}
