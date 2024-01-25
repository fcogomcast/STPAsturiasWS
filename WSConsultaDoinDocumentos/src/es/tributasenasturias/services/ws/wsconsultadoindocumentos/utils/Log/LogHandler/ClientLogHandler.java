package es.tributasenasturias.services.ws.wsconsultadoindocumentos.utils.Log.LogHandler;


public class ClientLogHandler extends LogHandler {

	private final String LOG_FILE ="SOAP_CLIENT.log";
	private final String LOG_DIR="proyectos//WSConsultaDoinDocumentos";
	
	
	public ClientLogHandler() {
			this.setLOG_DIR(LOG_DIR);
			this.setLOG_FILE(LOG_FILE);
	}

}
