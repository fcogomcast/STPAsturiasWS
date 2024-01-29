package com.stpa.ws.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import com.stpa.ws.server.util.Logger;

public class ClientHandler extends SoapHandler {

	private Logger log=null;
	private com.stpa.ws.server.util.Logger.LOGTYPE logFile = com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG;
	
	public ClientHandler(String idLlamada) {
		super.setlogFile(logFile);
		log = new Logger(idLlamada);
	}
	
	public synchronized boolean handleMessage(SOAPMessageContext context) {
		log.debug("Escribo en el log: " + logFile, com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (!outboundProperty.booleanValue()){
			log.debug("OUTBOUND SOAP MESSAGE: ", logFile);
		}else{
			log.debug("INBOUND SOAP MESSAGE: ", logFile);
	    }
		try{
			SOAPMessage message = context.getMessage();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			message.writeTo(baos);
			log.debug(baos.toString(), logFile);	
		}catch(IOException ioe){
			log.error("Error escribiendo soapmessage: ",ioe, logFile);
		}catch(SOAPException se){
			log.error("Error escribiendo soapmessage: ",se, logFile);
		}catch(Throwable t){
			log.error("Error escribiendo soapmessage: ",t, logFile);
		}
	    
	    return true;
	}

}
