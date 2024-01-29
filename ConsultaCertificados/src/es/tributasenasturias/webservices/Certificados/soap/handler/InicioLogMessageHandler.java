/**
 * 
 */
package es.tributasenasturias.webservices.Certificados.soap.handler;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.webservices.Certificados.utils.Log.GenericAppLogger;
import es.tributasenasturias.webservices.Certificados.utils.Log.TributasLogger;

/**
 * @author crubencvs
 *
 */
public class InicioLogMessageHandler implements SOAPHandler<SOAPMessageContext>{

	
	@Override
	public void close(MessageContext context) {
	}

	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		log(context);
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (salida)
		{
			return true; //Propagamos
		}
		return false;
	}
	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		
		log (context);
		return true;
	}
	
	private void log (SOAPMessageContext context)
	{
		GenericAppLogger log = new TributasLogger();
		//Sólo para mensajes de entrada
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (!salida.booleanValue())
		{
			log.info("*****Inicio de llamada a Consulta de certificados.");
		}
		else
		{
			log.info("*****Final de llamada a Consulta de certificados.");
		}
	
	}

}
