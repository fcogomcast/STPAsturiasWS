package es.stpa.smartmultas.soap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.stpa.smartmultas.Constantes;
import es.stpa.smartmultas.preferencias.Preferencias;
import es.stpa.smartmultas.preferencias.PreferenciasException;
import es.tributasenasturias.log.TributasLogger;




/**
 * Clase que intercepta los mensajes de entrada y los escribe en los log.
 * @author crubencvs
 *
 */
public class SoapServerHandler implements SOAPHandler<SOAPMessageContext> {

	
	/**
	 * Realiza el log de mensaje de entrada/salida
	 * @param context
	 */
	private void log(SOAPMessageContext context) 
	{
		TributasLogger log=null;
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
			log=new TributasLogger(pref.getModoLog(), pref.getDirectorioRaizLog(),pref.getFicheroLogServer(), idSesion);
			Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			String direccion=(salida)?"Envío":"Recepción";
			//Debido que puede haber una escritura adjunta, guardo sólo la parte de XML,
			//no el adjunto.
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
			SOAPUtils.generateSOAPErrMessage(context.getMessage(), "Error en proceso de mensaje SOAP."	, "0001", "SOAP Handler", true);
		}
		catch (PreferenciasException ex)
		{
			//En este punto es seguro que no tenemos log. Grabamos en la consola de servidor.
			System.err.println ("Servicios Smart Multas: Error de preferencias en manejador SOAP ("+SoapServerHandler.class.getName()+":"+ex.getMessage());
			SOAPUtils.generateSOAPErrMessage(context.getMessage(),"Error en proceso de mensaje SOAP.","0001","SOAP Handler I/O Exception", true);
		} catch (IOException io) {
			if (log!=null)
			{
				log.info("Error en la grabación de log de SOAP servidor al extraer el texto del mensaje SOAP:" + io.getMessage());
			}
			SOAPUtils.generateSOAPErrMessage(context.getMessage(),"Error en proceso de mensaje SOAP.","0001","SOAP Handler I/O Exception", true);
		}
	}

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		log (context);
		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		log (context);
		return true;
	}

	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	

}
