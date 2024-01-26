package es.tributasenasturias.soap.handler;

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

public class SoapHandler implements SOAPHandler<SOAPMessageContext>{
		
	private es.tributasenasturias.documentopagoutils.Logger.LOGTYPE logFile = es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.SERVERLOG;	

	public synchronized void setlogFile(es.tributasenasturias.documentopagoutils.Logger.LOGTYPE logFile){
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
		es.tributasenasturias.documentopagoutils.Logger.debug("Escribo en el log: " + logFile, es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outboundProperty.booleanValue()){
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