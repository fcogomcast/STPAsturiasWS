/**
 * 
 */
package es.tributasenasturias.utils.log;







/** Implementa la funcionalidad de creaci�n de logs. Deber�a utilizarse esta, y no la creaci�n directa,
 *  porque se ocupa de la inicializaci�n correctamente.
 * @author crubencvs
 *
 */
public class LogFactory {

	private LogFactory(){}
	/**
	 * Construye un {@link NIVEL_LOG} en base a una cadena que corresponda con el identificador de ese valor.
	 * @param snivel Texto que representra un identificador de un enum
	 * @return Objeto {@link NIVEL_LOG}
	 */
	private static NIVEL_LOG string2Nivel(String snivel)
	{
		NIVEL_LOG nivel;
		try
		{
			nivel = NIVEL_LOG.valueOf(snivel);
		}
		catch (Exception ex) //Probablemente IllegalArgumentException
		{
			//Por defecto, INFO si no es un nivel v�lido.
			nivel = NIVEL_LOG.INFO;
		}
		return nivel;
	}
	/**
	 * Devuelve una instancia {@link Logger} que permite realizar log.
	 * Las l�neas de log se representar�n en el juego de caracteres por defecto de la plataforma.
	 * @param nivel Representaci�n en texto del nivel de log (ALL,DEBUG,INFO, TRACE,WARNING,ERROR,NONE)
	 * @param ficheroLog Nombre de fichero de log en el que escribir
	 * @return instancia de LogAplicacion
	 */
	public static Logger newLogger(String nivel,String ficheroLog)
	{
		TributasLogger slog=new TributasLogger(string2Nivel(nivel),ficheroLog);
		return new Logger(slog);
	}
	/**
	 * Devuelve una instancia {@link Logger} que permite realizar log, con una informaci�n extra
	 * que aparecer� en cada l�nea. Esta informaci�n extra es fija, es decir, aparecer� la misma en cada l�nea.
	 * Se puede utilizar para representar, por ejemplo, un identificador �nico de sesi�n.
	 * Las l�neas de log se representar�n en el juego de caracteres por defecto de la plataforma.
	 * @param nivel Representaci�n en texto del nivel de log (ALL,DEBUG,INFO, TRACE,WARNING,ERROR,NONE)
	 * @param ficheroLog Nombre de fichero de log en el que escribir
	 * @param infoExtra Informaci�n extra que se incluir� en cada l�nea de log. Puede ser un identificador de sesi�n
	 * @return instancia de LogAplicacion
	 */
	public static Logger newLogger(String nivel,String ficheroLog,String infoExtra)
	{
		TributasLogger slog=new TributasLogger(string2Nivel(nivel),ficheroLog);
		slog.setInfoExtra(infoExtra);
		return new Logger(slog);
	}
	/**
	 * Devuelve una instancia {@link Logger} que permite realizar log, con una informaci�n extra
	 * que aparecer� en cada l�nea. Esta informaci�n extra es fija, es decir, aparecer� la misma en cada l�nea.
	 * Se puede utilizar para representar, por ejemplo, un identificador �nico de sesi�n.
	 * Tambi�n se puede indicar el juego de caracteres en que se quiere representar cada l�nea de log.
	 * @param nivel Representaci�n en texto del nivel de log (ALL,DEBUG,INFO, TRACE,WARNING,ERROR,NONE)
	 * @param ficheroLog Nombre de fichero de log en el que escribir
	 * @param infoExtra Informaci�n extra que se incluir� en cada l�nea de log. Puede ser un identificador de sesi�n
	 * @param charsetName nombre del juego de caracteres a utilizar para imprimir l�nea, como "ISO-8859-1" o "UTF-8".
	 * Si no se indica, se utiliza el juego de caracteres por defecto.
	 * @return instancia de LogAplicacion
	 */
	public static Logger newLogger(String nivel,String ficheroLog,String infoExtra, String charsetName)
	{
		TributasLogger slog=new TributasLogger(string2Nivel(nivel),ficheroLog,charsetName);
		slog.setInfoExtra(infoExtra);
		return new Logger(slog);
	}
}
