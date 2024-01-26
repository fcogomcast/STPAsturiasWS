package es.tributasenasturias.log;

public class LoggerFactory {

	private LoggerFactory(){}
	/**
	 * Recupera la implementación de log que nos interesa.
	 * @param nivel
	 * @param ficheroLog
	 * @param idSesion
	 * @return
	 */
	public static ILog getLogger(String nivel,String ficheroLog, String idSesion){
		return new TributasLogger(nivel, ficheroLog, idSesion);
	}
}
