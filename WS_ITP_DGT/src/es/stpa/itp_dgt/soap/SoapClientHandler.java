package es.stpa.itp_dgt.soap;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.stpa.itp_dgt.preferencias.Preferencias;
import es.stpa.itp_dgt.utils.Utils;
import es.tributasenasturias.utils.log.LogFactory;
import es.tributasenasturias.utils.log.Logger;


public class SoapClientHandler implements SOAPHandler<SOAPMessageContext> {

	private Preferencias pref;
	private String idLlamada;
	public SoapClientHandler(Preferencias pref, String idLlamada)
	{
		this.pref=pref;
		this.idLlamada=idLlamada;
	}
	
	private void log(SOAPMessageContext context)
	{
		Logger log=null;
		try
		{
			
			log=LogFactory.newLogger(pref.getModoLog(), pref.getFicheroLogSoapClient(), idLlamada);
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
				log.error("Error en la grabación de log de SOAP cliente:" + ex.getMessage());
			}
			Utils.generateSOAPErrMessage(context.getMessage(), "Error en comunicacion WSGestionAnuncios <--> Servicio Remoto."	, "0002", "SOAP Handler");
		}
		catch (java.io.IOException ex)
		{
			if (log!=null)
			{
				log.error("Error en la grabación de log de SOAP cliente:" + ex.getMessage());
			}
			Utils.generateSOAPErrMessage(context.getMessage(), "Error en comunicación WSGestionAnuncios <--> Servicio Remoto."	, "0002", "SOAP Handler");
		}
		catch (Exception ex)
		{
			//En este punto podemos no tener log. Grabamos en la consola de servidor.
			if (log==null)
			{
				System.err.println ("Servicio ITP DGT: Error inesperado en manejador SOAP ("+SoapServerHandler.class.getName()+":"+ex.getMessage());
				ex.printStackTrace();
			}
			else
			{
				log.error ("Error Inesperado en la grabación de SOAP cliente:"+ex.getMessage());
				Utils.generateSOAPErrMessage(context.getMessage(), "Error en comunicación WSGestionAnuncios <--> Servicio Remoto."	, "0002", "SOAP Handler");
			}
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
		log(context);
		
		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		log(context);
		return true;
	}
	
}
