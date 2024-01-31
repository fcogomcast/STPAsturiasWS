package es.tributasenasturias.servicios.asturcon.consultaDeuda.soap;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.NodeList;


import es.tributasenasturias.servicios.asturcon.consultaDeuda.Utils.Utils;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.xml.XMLDOMDocumentException;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.xml.XMLDOMUtils;



/**
 * Clase que intercepta los mensajes de entrada y los escribe en los log.
 * @author crubencvs
 *
 */
public class ExtraerCargaMensaje implements SOAPHandler<SOAPMessageContext> {

	private void extraer(SOAPMessageContext context) 
	{
		try
		{
			Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			if (!salida)
			{
				SOAPMessage msg = context.getMessage();
				String payload="";
				NodeList mensajes= msg.getSOAPBody().getElementsByTagName("MENSAJE");
				if (mensajes.getLength()>0)
				{
					payload=XMLDOMUtils.getXMLText(mensajes.item(0));
				}
		        context.put("XMLSource", payload);
		        context.setScope("XMLSource", MessageContext.Scope.APPLICATION);
			}
		}
		catch (javax.xml.soap.SOAPException ex)
		{
			Utils.generateSOAPErrMessage(context.getMessage(), "Error en proceso de mensaje SOAP."	, "0001", "SOAP Handler");
		}catch (XMLDOMDocumentException ex) {
			Utils.generateSOAPErrMessage(context.getMessage(),"Error en proceso de mensaje SOAP.","0001","SOAP Handler procesado de XML.");
		}
	}
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
		extraer(context);
		return true;
	}

	

}
