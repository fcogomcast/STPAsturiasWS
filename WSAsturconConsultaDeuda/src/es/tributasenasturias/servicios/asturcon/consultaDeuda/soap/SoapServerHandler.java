package es.tributasenasturias.servicios.asturcon.consultaDeuda.soap;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.servicios.asturcon.consultaDeuda.Utils.Utils;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.preferencias.Preferencias;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.preferencias.PreferenciasException;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.preferencias.PreferenciasFactory;
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
			Preferencias pref= PreferenciasFactory.newInstance();
			String idSesion="Sin n�mero de sesi�n recuperada.";
			Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			idSesion=(String)context.get("IdSesion");
			if (idSesion==null)
			{
				idSesion="Sin n�mero de sesi�n recuperada.";
			}
			log=LogFactory.newLogger(pref.getModoLog(), pref.getFicheroLogSoapServer(), idSesion);
			String direccion=(salida)?"Env�o":"Recepci�n";
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
				log.info("Error en la grabaci�n de log de SOAP servidor:" + ex.getMessage());
			}
			Utils.generateSOAPErrMessage(context.getMessage(), "Error en proceso de mensaje SOAP."	, "0001", "SOAP Handler");
		}
		catch (java.io.IOException ex)
		{
			if (log!=null)
			{
				log.info("Error en la grabaci�n de log de SOAP servidor por error de Entrada/Salida:" + ex.getMessage());
			}
			Utils.generateSOAPErrMessage(context.getMessage(),"Error en proceso de mensaje SOAP.","0001","SOAP Handler I/O Exception");
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
