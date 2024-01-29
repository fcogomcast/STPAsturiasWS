package com.stpa.ws.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import com.stpa.ws.server.formularios.Constantes;
import com.stpa.ws.server.util.Logger;

public class SoapHandler implements SOAPHandler<SOAPMessageContext>{
		
	private com.stpa.ws.server.util.Logger.LOGTYPE logFile = com.stpa.ws.server.util.Logger.LOGTYPE.SERVERLOG;	

	public void setlogFile(com.stpa.ws.server.util.Logger.LOGTYPE logFile){
		this.logFile = logFile;
	}
	
	public void close(MessageContext context) {
	}

	public Set<QName> getHeaders() { 
	     return Collections.emptySet(); 
	} 

	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}
	
	public synchronized boolean handleMessage(SOAPMessageContext context) {
		Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		String sentido;
		String idLlamada=null;
		Logger log =null;
		if (outboundProperty.booleanValue()){
			sentido="OUTBOUND SOAP MESSAGE: ";
		}else{
			sentido="INBOUND SOAP MESSAGE: ";
	    }
		try{
			idLlamada= (String)context.get(Constantes.ID_LLAMADA);
			log = new Logger(idLlamada);
			log.debug("Escribo en el log: " + logFile, com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			SOAPMessage message = context.getMessage();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			message.writeTo(baos);
			log.info(sentido+"::"+baos.toString(), logFile);	
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