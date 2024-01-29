package com.stpa.ws.handler;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import com.stpa.ws.server.formularios.Constantes;
import com.stpa.ws.server.util.GeneradorIdLlamada;


public class GeneraIdLlamada implements SOAPHandler<SOAPMessageContext>{

	/* (non-Javadoc)
	 * @see javax.xml.ws.handler.soap.SOAPHandler#getHeaders()
	 */
	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	/* (non-Javadoc)
	 * @see javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public void close(MessageContext context) {
		
	}

	/* (non-Javadoc)
	 * @see javax.xml.ws.handler.Handler#handleFault(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	/**
	 * Genera el Id de esta llamada.
	 */
	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		//En entrada, generamos el identificador �nico.
		Boolean salida = (Boolean) context.get(javax.xml.ws.handler.MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (!salida.booleanValue())
		{
			String idSesion = GeneradorIdLlamada.generaIdSesion();
			context.put(Constantes.ID_LLAMADA, idSesion);
			context.setScope(Constantes.ID_LLAMADA, MessageContext.Scope.APPLICATION);
		}
		return true;
	}

}
