package es.tributasenasturias.webservice.pagopresentacion.log;

public interface ILoggable {
	/**
	 * Establecerá el log mediante el que se mostrarán los mensajes.
	 * @param log Instancia de <a>LogHelper<a>.
	 */
	public void setLogger(LogHelper log);
	/**
	 * Devuelve la instancia de log mediante la que se muestran los mensajes en la instancia de clase que implemente esta interfaz.
	 * @return Instancia de LogHelper mediante la que se muestran los mensajes.
	 */
	public LogHelper getLogger();
}
