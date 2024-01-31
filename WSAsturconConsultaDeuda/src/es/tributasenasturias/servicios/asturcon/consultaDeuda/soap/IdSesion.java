package es.tributasenasturias.servicios.asturcon.consultaDeuda.soap;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.servicios.asturcon.consultaDeuda.Utils.Utils;


/**
 * Clase que intercepta el mensaje de entrada, y genera un Id único de sesión para él,
 *  para identificar sus entradas en los log. 
 * @author crubencvs
 *
 */
public class IdSesion implements SOAPHandler<SOAPMessageContext> {

	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (!salida.booleanValue())
		{
			//Lo generamos, para que exista.
			String idSesion=Utils.getIdLlamada();
			context.put("IdSesion", idSesion);
			context.setScope("IdSesion", MessageContext.Scope.APPLICATION);
		}
		return true;
	}

	

}
