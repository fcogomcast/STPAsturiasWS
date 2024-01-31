/**
 * 
 */
package es.tributasenasturias.soap.handler;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.validacion.XMLDOMUtils;

/**
 * @author davidsa
 */
public class ExtraccionXMLHandler implements SOAPHandler<SOAPMessageContext> {

	@Override
	public void close(MessageContext context) {}

	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return false;
	}

	@Override
	// Se modifica el contexto, por lo que ha de hacerse sincronizado.
	public synchronized boolean handleMessage(SOAPMessageContext context) {
		String remesa="";
		try {
			Boolean salida = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			if (!salida.booleanValue()) {
				//Buscamos el primer hijo que sea elemento
				remesa=XMLDOMUtils.Node2Text(XMLDOMUtils.getNthChildNodeWithoutNS(context.getMessage().getSOAPBody(),"remesa",1));
				context.put("remesa",remesa);
				context.setScope("remesa",MessageContext.Scope.APPLICATION);
			}
		} catch (Exception e) {
			context.put("remesa",remesa);
			context.setScope("remesa",MessageContext.Scope.APPLICATION);
		}
		return true;
	}
}