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
import es.stpa.itp_dgt.preferencias.PreferenciasException;
import es.stpa.itp_dgt.utils.Constantes;
import es.stpa.itp_dgt.utils.Utils;
import es.tributasenasturias.utils.log.LogFactory;
import es.tributasenasturias.utils.log.Logger;





/**
 * Clase que intercepta los mensajes de entrada y los escribe en los log.
 * @author crubencvs
 *
 */
public class SoapServerHandler implements SOAPHandler<SOAPMessageContext> {


	private void log(SOAPMessageContext context) 
	{
		Logger log=null;
		try
		{
			Preferencias pref= (Preferencias) context.get(Constantes.PREFERENCIAS);
			if (pref==null)
			{
				pref = new Preferencias();
			}
			String idSesion="Sin número de sesión recuperada.";
			idSesion=(String)context.get(Constantes.IDSESION);
			if (idSesion==null)
			{
				idSesion="Sin número de sesión recuperada.";
			}
			log=LogFactory.newLogger(pref.getModoLog(), pref.getFicheroLogSoapServer(), idSesion);
			Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			String direccion=(salida)?"Envío":"Recepción";
			SOAPMessage msg = context.getMessage();
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
	        msg.writeTo(byteArray);
	        String soapMessage = new String(byteArray.toByteArray());
	        log.info(direccion+"::"+soapMessage);
		}
		catch (javax.xml.soap.SOAPException ex)
		{
			if (log!=null)
			{
				log.info("Error en la grabación de log de SOAP servidor:" + ex.getMessage());
			}
			Utils.generateSOAPErrMessage(context.getMessage(), "Error en la grabación de log de SOAP:"+ ex.getMessage(), "", "");
		}
		catch (java.io.IOException ex)
		{
			if (log!=null)
			{
				log.info("Error en la grabación de log de SOAP servidor por error de Entrada/Salida:" + ex.getMessage());
			}
			Utils.generateSOAPErrMessage(context.getMessage(), "Error en la grabación de log de SOAP:"+ ex.getMessage(), "", "");
		}
		catch (PreferenciasException ex)
		{
			//En este punto es seguro que no tenemos log. Grabamos en la consola de servidor.
			System.err.println ("Servicio de Notificaciones BOE: Error de preferencias en manejador SOAP ("+SoapServerHandler.class.getName()+":"+ex.getMessage());
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
		
		log(context);
		
		
		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		
		
			log(context);
			return true;
		
	}

	

}
