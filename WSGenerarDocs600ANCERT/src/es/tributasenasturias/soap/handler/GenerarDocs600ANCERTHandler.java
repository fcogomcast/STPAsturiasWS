/**
 * 
 */

package es.tributasenasturias.soap.handler;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPPart;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.GenerarDocs600ANCERTutils.Logger;
import es.tributasenasturias.GenerarDocs600ANCERTutils.Preferencias;


public class GenerarDocs600ANCERTHandler implements
		SOAPHandler<SOAPMessageContext> {
	@Override
	public void close(MessageContext context) {
	}

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
		boolean valido = true;

		return valido;
	}

	public SOAPMessage creaMensajeSOAP(String xmlContent) throws SOAPException {
		SOAPMessage msg = null;
		SOAPPart part = null;
		MessageFactory msgFactory = MessageFactory.newInstance();
		msg = msgFactory.createMessage();
		part = msg.getSOAPPart();
		ByteArrayInputStream bas = new ByteArrayInputStream(xmlContent.getBytes());
		StreamSource strs = new StreamSource(bas);
		part.setContent(strs);
		return msg;
	}
}
