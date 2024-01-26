package es.tributasenasturias.traslado.soap;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class SeguridadMensaje implements SOAPHandler<SOAPMessageContext>{

	
	//Procesa el mensaje
	private void process(SOAPMessageContext context, boolean isFault)
	{
		//Manejadores a ejecutar en "handlerMessage"
		ISOAPHandler[] hMensaje= {new GestorFirma(),
								  new ValidadorCertificado()
								};
		//Manejadores a ejecutar en handleFault
		ISOAPHandler[] hFault = {};
		//Tenemos que saber si se trata de entrada o salida
		//En entrada el orden de manejadores será el indicado, 
		//en salida el contrario
		boolean salida = ((Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).booleanValue();
		//Si algún proceso falla, lanzará una excepción SOAP con lo cual ahí terminará el interceptor
		//Entrada
		if (!salida)
		{
			if (!isFault)
			{
				for (int i=0;i<hMensaje.length;i++)
				{
					hMensaje[i].process(context);
				}
			}
			else {
				for (int i=0;i<hFault.length;i++)
				{
					hFault[i].process(context);
				}
			}
		}
		else
		{
			// Salida
			if (!isFault)
			{
				for (int i=hMensaje.length-1;i>=0;i--)
				{
					hMensaje[i].process(context);
				}
			}
			else
			{
				for (int i=hFault.length-1;i>=0;i--)
				{
					hFault[i].process(context);
				}
			}
		}
		
	}
	
	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	@Override
	public void close(MessageContext context) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		process(context,true);
		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		process(context,false);
		return true;
	}

}
