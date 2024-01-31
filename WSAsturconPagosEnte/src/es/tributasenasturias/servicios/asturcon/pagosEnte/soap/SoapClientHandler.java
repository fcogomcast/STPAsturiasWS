package es.tributasenasturias.servicios.asturcon.pagosEnte.soap;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.servicios.asturcon.pagosEnte.Utils.Utils;
import es.tributasenasturias.servicios.asturcon.pagosEnte.preferencias.Preferencias;
import es.tributasenasturias.servicios.asturcon.pagosEnte.preferencias.PreferenciasException;
import es.tributasenasturias.servicios.asturcon.pagosEnte.preferencias.PreferenciasFactory;
import es.tributasenasturias.utils.log.LogFactory;
import es.tributasenasturias.utils.log.Logger;


public class SoapClientHandler implements SOAPHandler<SOAPMessageContext> {

	//Id de la sesión en que se utiliza el manejador. Identificará a la llamada del servicio web.
	private String sesionId;
	public SoapClientHandler(String idsesion)
	{
		sesionId=idsesion;
	}
	private void log(SOAPMessageContext context)
	{
		Logger log=null;
		try
		{
			Preferencias pref= PreferenciasFactory.newInstance();
			log=LogFactory.newLogger(pref.getModoLog(), pref.getFicheroLogSoapClient(), sesionId);
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
			Utils.generateSOAPErrMessage(context.getMessage(), "Error en proceso de comunicación Consulta Deuda <--> Servicio Remoto."	, "0002", "SOAP Handler");
		}
		catch (java.io.IOException ex)
		{
			if (log!=null)
			{
				log.info("Error en la grabación de log de SOAP cliente:" + ex.getMessage());
			}
			Utils.generateSOAPErrMessage(context.getMessage(), "Error en proceso de comunicación Consulta Deuda <--> Servicio Remoto."	, "0002", "SOAP Handler");
		}
		catch (PreferenciasException ex)
		{
			//En este punto es seguro que no tenemos log. Grabamos en la consola de servidor.
			System.err.println ("Asturcon-Consulta de Deuda: Error de preferencias en manejador SOAP ("+SoapServerHandler.class.getName()+":"+ex.getMessage());
			ex.printStackTrace();
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
		log(context);
		return true;
	}
	
}
