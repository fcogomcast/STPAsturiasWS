package es.stpa.servicios.clave.soap;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.stpa.servicios.clave.log.ILog;
import es.stpa.servicios.clave.log.TributasLogger;
import es.stpa.servicios.clave.preferencias.Preferencias;
import es.stpa.servicios.clave.preferencias.PreferenciasException;









public class SoapClientHandler implements SOAPHandler<SOAPMessageContext> {

	//Id de la sesión en que se utiliza el manejador. Identificará a la llamada del servicio web.
	private String sesionId;
	private Preferencias pref;
	public SoapClientHandler(Preferencias pref,String idsesion)
	{
		this.pref = pref;
		sesionId=idsesion;
	}
	private void log(SOAPMessageContext context)
	{
		ILog log=null;
		try
		{
			if (pref==null)
			{
				pref = new Preferencias();
			}

			log=new TributasLogger(pref.getModoLog(), pref.getFicheroLogClient(), sesionId);
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
			SOAPUtils.generateSOAPErrMessage(context.getMessage(), "Error en proceso de comunicación capa de Cl@ve <--> Servicio Remoto."	, "0002", "SOAP Handler", true);
		}
		catch (java.io.IOException ex)
		{
			if (log!=null)
			{
				log.info("Error en la grabación de log de SOAP cliente:" + ex.getMessage());
			}
			SOAPUtils.generateSOAPErrMessage(context.getMessage(), "Error en proceso de comunicación capa de Cl@ve <--> Servicio Remoto."	, "0002", "SOAP Handler", true);
		}
		catch (PreferenciasException ex)
		{
			//En este punto es seguro que no tenemos log. Grabamos en la consola de servidor.
			System.err.println ("Consulta de datos censales: Error de preferencias en manejador SOAP ("+SoapClientHandler.class.getName()+":"+ex.getMessage());
			SOAPUtils.generateSOAPErrMessage(context.getMessage(), "Error en proceso de comunicación capa de Cl@ve <--> Servicio Remoto."	, "0002", "SOAP Handler", true);
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
