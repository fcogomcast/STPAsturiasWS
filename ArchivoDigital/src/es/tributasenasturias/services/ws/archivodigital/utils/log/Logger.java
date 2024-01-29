package es.tributasenasturias.services.ws.archivodigital.utils.log;

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
		this.setNombre("ArchivoDigital"); //Nombre de proceso que aparecerá en el log.
		this.setIdLlamada(idLlamada);
	}
}
