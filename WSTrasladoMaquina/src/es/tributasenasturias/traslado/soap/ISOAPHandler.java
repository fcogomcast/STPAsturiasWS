package es.tributasenasturias.traslado.soap;

import javax.xml.ws.handler.soap.SOAPMessageContext;

public interface ISOAPHandler {

	public void process(SOAPMessageContext context);
}
