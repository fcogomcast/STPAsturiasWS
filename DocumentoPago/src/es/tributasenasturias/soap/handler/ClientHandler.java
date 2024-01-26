package es.tributasenasturias.soap.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class ClientHandler extends SoapHandler {

	private  es.tributasenasturias.documentopagoutils.Logger.LOGTYPE logFile = es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.CLIENTLOG;
	
	public ClientHandler() {
		super.setlogFile(logFile);
	}
	
	public synchronized boolean handleMessage(SOAPMessageContext context) {
		es.tributasenasturias.documentopagoutils.Logger.debug("Escribo en el log: " + logFile, es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (!outboundProperty.booleanValue()){
			 es.tributasenasturias.documentopagoutils.Logger.debug("OUTBOUND SOAP MESSAGE: ", logFile);
		}else{
			 es.tributasenasturias.documentopagoutils.Logger.debug("INBOUND SOAP MESSAGE: ", logFile);
	    }
		try{
			SOAPMessage message = context.getMessage();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			message.writeTo(baos);
			 es.tributasenasturias.documentopagoutils.Logger.debug(baos.toString(), logFile);	
		}catch(IOException ioe){
			 es.tributasenasturias.documentopagoutils.Logger.error("Error escribiendo soapmessage: ",ioe, logFile);
		}catch(SOAPException se){
			 es.tributasenasturias.documentopagoutils.Logger.error("Error escribiendo soapmessage: ",se, logFile);
		}catch(Throwable t){
			 es.tributasenasturias.documentopagoutils.Logger.error("Error escribiendo soapmessage: ",t, logFile);
		}
	    
	    return true;
	}

}
