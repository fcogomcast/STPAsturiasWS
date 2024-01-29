package es.tributasenasturias.services.ws.custodiaimagenes.utils.log.LogHandler;


public class ClientLogHandler extends LogHandler {

	private final String LOG_FILE ="SOAP_CLIENT_CI.log";
	private final String LOG_DIR="proyectos//ArchivoDigital";
	
	
	public ClientLogHandler() {
			this.setLOG_DIR(LOG_DIR);
			this.setLOG_FILE(LOG_FILE);
	}

}
