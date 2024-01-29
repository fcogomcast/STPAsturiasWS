package es.tributasenasturias.services.ws.imagenesmultas.utils.log;

/**
 * 
 * @author noelianbb
 *
 */
public class Logger extends GenericAppLogger
{
	public Logger(String idLlamada, String dirname, String file)
	{
		this.setLogFile(file);
		this.setLogDir(dirname);
		this.setNombre("WSImagenesMultas"); //Nombre de proceso que aparecerá en el log.
		this.setIdLlamada(idLlamada);
	}
}
