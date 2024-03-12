package es.tributasenasturias.servicios.accesocertificado.soap;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.servicios.accesocertificado.contextoLlamadas.CallContextConstants;
import es.tributasenasturias.servicios.accesocertificado.log.LogFactory;
import es.tributasenasturias.servicios.accesocertificado.preferencias.Preferencias;
import es.tributasenasturias.servicios.accesocertificado.preferencias.PreferenciasException;
import es.tributasenasturias.servicios.accesocertificado.preferencias.PreferenciasFactory;
import es.tributasenasturias.servicios.accesocertificado.utils.Util;
import es.tributasenasturias.utils.log.Logger;

public class SoapServerHandler implements SOAPHandler<SOAPMessageContext>{

	private void log(SOAPMessageContext context) 
	{
		Logger log=null;
		try
		{
			Preferencias pref = (Preferencias) context.get(CallContextConstants.PREFERENCIAS);
			if (pref==null)
			{
				pref=PreferenciasFactory.newInstance();
			}
			if (!"S".equals(pref.getDebugSOAP()))
			{
				return;
			}
			String idSesion =null;
			Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			idSesion=(String)context.get(CallContextConstants.IDSESION);
			if (idSesion==null)
			{
				idSesion="Sesión no conocida.";
			}
			log=LogFactory.newLogger(pref.getModoLog(), pref.getFicheroLogServer(), idSesion);
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
				log.info("Error en el objeto " + SoapServerHandler.class.getName()+":" + ex.getMessage());
			}
			else
			{
				System.err.println ("WSAccesoPorCertificado::Error en el objeto " + SoapServerHandler.class.getName()+" :" + ex.getMessage());
			}
			try{
			Util.createSOAPFaultException("Error en el servicio de Acceso por Certificado.");
			}catch (SOAPException e){}
		}
		catch (java.io.IOException ex)
		{
			if (log!=null)
			{
				log.info("Error en el objeto " + SoapServerHandler.class.getName()+":" + ex.getMessage());
			}
			else
			{
				System.err.println ("WSAccesoPorCertificado::Error en el objeto " + SoapServerHandler.class.getName()+" :" + ex.getMessage());
			}
			try
			{
			Util.createSOAPFaultException("Error en el servicio de Acceso por Certificado.");
			} catch (SOAPException e){}
		} catch (PreferenciasException e) {
			System.err.println ("WSAccesoPorCertificado::Error en el objeto " + SoapServerHandler.class.getName()+" :" + e.getMessage());
			try
			{
				Util.createSOAPFaultException("Error en el servicio de Acceso por Certificado.");
			} catch (SOAPException s){}
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
