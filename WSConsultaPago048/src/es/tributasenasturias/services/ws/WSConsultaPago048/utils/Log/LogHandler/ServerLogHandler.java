package es.tributasenasturias.services.ws.WSConsultaPago048.utils.Log.LogHandler;

public class ServerLogHandler extends LogHandler {

	private final String LOG_FILE ="SOAP_SERVER.log";
	private final String LOG_DIR="WSConsultaPago048";
	
	public ServerLogHandler() {
		this.setLOG_DIR(LOG_DIR);
		this.setLOG_FILE(LOG_FILE);
	}
}
