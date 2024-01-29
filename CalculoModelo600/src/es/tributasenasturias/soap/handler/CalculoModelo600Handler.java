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

/**
 * @author davidsa
 * 
 */
public class CalculoModelo600Handler implements SOAPHandler<SOAPMessageContext> {
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
	public boolean handleMessage(SOAPMessageContext context) {	
	
		boolean valido = true;
		/*
		SOAPPart part = null;
		SOAPMessage msg = ((SOAPMessageContext) context).getMessage();
		part = msg.getSOAPPart();
		
		// Sólo para mensajes de entrada, se valida la firma
		
		final Preferencias pref = new Preferencias();
		// cargamos los datos del almacen de un fichero xml preferencias
		try {
			pref.CargarPreferencias();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			Logger.error("Error al cargar preferencias en handler.");
		}

		Boolean salida = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if (salida.booleanValue()) {
			if (pref.getFirmaDigital().equals("S")) {
				try {
					SOAPMessage msgSOAP;
					msgSOAP = creaMensajeSOAP(part.getEnvelope().getTextContent());
					//msgSOAP.setProperty(javax.xml.soap.SOAPMessage.CHARACTER_SET_ENCODING,"UTF-8");
					context.setMessage(msgSOAP);
					valido = true;
						
					if (!valido) {
						Logger.error("No se ha podido validar la firma. Revise el resto del log para encontrar más información.");
					}

				} catch (DOMException e) {
					// TODO Auto-generated catch block
					Logger.error("No se ha podido validar la firma. Revise el resto del log para encontrar más información."+ e.getMessage());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Logger.error("No se ha podido validar la firma. Revise el resto del log para encontrar más información."+ e.getMessage());
				}
			}
		}
*/
		return valido;
	}

	public SOAPMessage creaMensajeSOAP(String xmlContent) throws SOAPException {
		SOAPMessage msg = null;
		SOAPPart part = null;
		MessageFactory msgFactory = MessageFactory.newInstance();
		msg = msgFactory.createMessage();
		part = msg.getSOAPPart();
		ByteArrayInputStream bas = new ByteArrayInputStream(xmlContent
				.getBytes());
		StreamSource strs = new StreamSource(bas);
		part.setContent(strs);
		return msg;
	}
}
