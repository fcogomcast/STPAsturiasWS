package es.tributasenasturias.servicios.accesocertificado.soap;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.servicios.accesocertificado.log.LogFactory;
import es.tributasenasturias.servicios.accesocertificado.preferencias.Preferencias;
import es.tributasenasturias.servicios.accesocertificado.utils.Util;
import es.tributasenasturias.utils.log.Logger;


public class SoapClientHandler implements SOAPHandler<SOAPMessageContext>{

	private String idSesion;
	private Preferencias prefs;
	
	public SoapClientHandler(String idSesion, Preferencias prefs) {
		super();
		this.idSesion = idSesion;
		this.prefs = prefs;
	}

	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}
	private void log(SOAPMessageContext context)
	{
		Logger log=null;
		try
		{
			if (!"S".equals(prefs.getDebugSOAP()))
			{
				return;
			}
			log=LogFactory.newLogger(prefs.getModoLog(), prefs.getFicheroLogClient(), idSesion);
			Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			String direccion=(salida)?"Envío":"Recepción";
			SOAPMessage msg = context.getMessage();
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
	        msg.writeTo(byteArray);
	        String soapMessage = new String(byteArray.toByteArray());
	        log.info(direccion+":"+soapMessage);
		}
		catch (javax.xml.soap.SOAPException ex)
		{
			if (log!=null)
			{
				log.info("Error en la grabación de log de SOAP cliente:" + ex.getMessage());
			}
			try
			{	Util.createSOAPFaultException("Error en el servicio de Acceso por Certificado, comunicación con terceros servicios.");
			}catch (Exception e){}
		}
		catch (java.io.IOException ex)
		{
			if (log!=null)
			{
				log.info("Error en la grabación de log de SOAP cliente:" + ex.getMessage());
			}
			try
			{
				Util.createSOAPFaultException("Error en el servicio de Acceso por Certificado, comunicación con terceros servicios.");
			} catch (Exception e){}
		}
	}
	@Override
	public void close(MessageContext context) {
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		log(context);
		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		log(context);
		return true;
	}

}
