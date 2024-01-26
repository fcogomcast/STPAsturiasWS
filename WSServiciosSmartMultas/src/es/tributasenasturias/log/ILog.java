package es.tributasenasturias.log;

/**
 * Interfaz que implementar�n las clases que actu�n como logger. Esto se usa para tener una jerarqu�a sencilla
 * que permita realizar log, sin tener que usar otras librer�as. Podr�a ser m�s sencillo si tuvi�semos una
 * sola clase que simplemente escribiese a fichero.
 * @author crubencvs
 *
 */
public interface ILog {

	/**
	 * Los diferentes nivels de log que existen.
	 * Cuando se utiliza un nivel para filtrar mensajes enviados al log, un determinado nivel incluye a los siguientes en esta lista:
	 * <ul>
	 * <li>DEBUG</li>
	 * <li>INFO</li>
	 * <li>WARNING</li>
	 * <li>TRACE</li>
	 * <li>ERROR</li>
	 * </ul>
	 * Es decir, si el nivel objetivo para los mensajes es "INFO", se mostrar�n los mensajes que se identifiquen
	 * con los niveles "INFO", "WARNING", "TRACE" y "ERROR" pero no los identificados con el nivel "DEBUG".
	 * @author crubencvs
	 *
	 */
	static enum NIVEL_LOG {
		/**
		 * Indica que no se pasar� al log ninguno de los mensajes posibles.
		 */
		NONE,
		/**
		 * Utilizado para mensajes de depuraci�n.
		 */
		DEBUG,
		/**
		 * Utilizado para los mensajes de proceso normales, es en este nivel donde se mostrar�n 
		 * los mensajes de proceso correcto y esperado.
		 */
		INFO,
		/**
		 * Utilizado como nivel de aviso, no se ha producido error pero s� una circunstancia 
		 * que se considera que se sale del proceso normal.
		 */
		WARNING,
		/**
		 * Utilizado s�lo para trazas relacionadas con eventos dirigidos al 
		 * mantenimiento t�cnico, como volcados de pila. 
		 */
		TRACE,
		/**
		 * Utilizado para indicar que se han producido errores en el proceso.
		 */
		ERROR,
		/**
		 * Indica todos los niveles de log, agrupa a todos los anteriores excepto "NONE".
		 */
		ALL
	}
	/**
	 * Genera una entrada en el log, con el mensaje indicado y un nivel de log asociado.
	 * @param msg Mensaje a escribir.
	 * @param level {@link NIVEL_LOG} asociado.
	 */
	void doLog(String msg, NIVEL_LOG level);
	/**
	 * Genera una entrada de log con el mensaje indicado y el nivel de log de DEBUG
	 * @param msg Mensaje a escribir.
	 */
	void debug(String msg);
	/**
	 * Genera una entrada de log con el mensaje indicado y el nivel de log de ERROR
	 * @param msg Mensaje a escribir.
	 */
	void error(String msg);
	/**
	 * Genera una entrada de log con el mensaje indicado y el nivel de log de ERROR.
	 * Tambi�n recupera informaci�n de la excepci�n y la escribe.
	 * @param msg Mensaje a escribir.
	 * @param excepcion excepci�n de la que se recuperar� informaci�n extra para escribir.
	 */
	void error (String msg, Throwable excepcion);
	/**
	 * Genera una entrada de log con el mensaje indicado y el nivel de log de INFO
	 * @param msg Mensaje a escribir.
	 */
	void info(String msg);

	/**
	 * Genera una entrada de log con el volcado de pila que se pasa.
	 * @param stackTraceElements Elementos de la pila
	 */
	void trace(StackTraceElement[] stackTraceElements);
	/**
	 * Genera una entrada de log con el mensaje indicado y el nivel de log de TRACE
	 * @param msg Mensaje a escribir.
	 */
	void trace(String msg);
	/**
	 * Genera una entrada de log con el mensaje indicado y el nivel de log de WARNING
	 * @param msg Mensaje a escribir.
	 */
	void warning(String msg);
	

}
