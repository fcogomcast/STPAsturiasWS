package es.tributasenasturias.servicios.codigobarras.soap;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.log.TributasLogger;
import es.tributasenasturias.servicios.codigobarras.Constantes;
import es.tributasenasturias.servicios.codigobarras.preferencias.Preferencias;
import es.tributasenasturias.servicios.codigobarras.preferencias.PreferenciasException;





public class SoapClientHandler implements SOAPHandler<SOAPMessageContext> {

	//Id de la sesi�n en que se utiliza el manejador. Identificar� a la llamada del servicio web.
	private String sesionId;
	public SoapClientHandler(String idsesion)
	{
		sesionId=idsesion;
	}
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
			if (!"DEBUG".equalsIgnoreCase(pref.getModoLog()) && 
					!"ALL".equalsIgnoreCase(pref.getModoLog())){
				return;
			}
			log=new TributasLogger(pref.getModoLog(), pref.getFicheroLogClient(), sesionId);
			Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			String direccion=(salida)?"Env�o":"Recepci�n";
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
				log.info("Error en la grabaci�n de log de SOAP cliente:" + ex.getMessage());
			}
			SOAPUtils.generateSOAPErrMessage(context.getMessage(), "Error en proceso de comunicaci�n Traslado M�quina <--> Servicio Remoto."	, "0002", "SOAP Handler", true);
		}
		catch (java.io.IOException ex)
		{
			if (log!=null)
			{
				log.info("Error en la grabaci�n de log de SOAP cliente:" + ex.getMessage());
			}
			SOAPUtils.generateSOAPErrMessage(context.getMessage(), "Error en proceso de comunicaci�n Servicio C�digos de  barras <--> Servicio Remoto."	, "0002", "SOAP Handler", true);
		}
		catch (PreferenciasException ex)
		{
			//En este punto es seguro que no tenemos log. Grabamos en la consola de servidor.
			System.err.println ("Servicio de c�digos de barras: Error de preferencias en manejador SOAP ("+SoapClientHandler.class.getName()+":"+ex.getMessage());
			SOAPUtils.generateSOAPErrMessage(context.getMessage(), "Error en proceso de comunicaci�n Servicio C�digos de barras <--> Servicio Remoto."	, "0002", "SOAP Handler", true);
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
