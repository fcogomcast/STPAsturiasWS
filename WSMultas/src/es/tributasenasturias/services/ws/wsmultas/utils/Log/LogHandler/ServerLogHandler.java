package es.tributasenasturias.services.ws.wsmultas.utils.Log.LogHandler;

public class ServerLogHandler extends LogHandler {

	private final String LOG_FILE ="SOAP_SERVER.log";
	private final String LOG_DIR="proyectos//WSConsultaDoinDocumentos";
	
	public ServerLogHandler() {
		this.setLOG_DIR(LOG_DIR);
		this.setLOG_FILE(LOG_FILE);
	}
}
