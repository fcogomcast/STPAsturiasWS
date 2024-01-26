package es.stpa.notificagestionenvios.soap;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;


import es.stpa.notificagestionenvios.Constantes;
import es.stpa.notificagestionenvios.preferencias.Preferencias;
import es.stpa.notificagestionenvios.preferencias.PreferenciasException;
import es.tributasenasturias.log.TributasLogger;





public class SoapClientHandler implements SOAPHandler<SOAPMessageContext> {

	/**
	 * Recupera el contenido del mensaje SOAP de entrada, sin los adjuntos.
	 * @param message
	 * @return
	 * @throws SOAPException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	private byte[] contenidoMensajeSinAdjuntos(SOAPMessage message) throws SOAPException, TransformerFactoryConfigurationError, TransformerException{
		Source src = message.getSOAPPart().getContent();
		Transformer tr = TransformerFactory.newInstance().newTransformer();
		tr.setOutputProperty(OutputKeys.INDENT,"no");
		ByteArrayOutputStream baos= new ByteArrayOutputStream();
		StreamResult dest = new StreamResult(baos);
		tr.transform(src, dest);
		return baos.toByteArray();
		
	}
	//Id de la sesión en que se utiliza el manejador. Identificará a la llamada del servicio web.
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
			String direccion=(salida)?"Envío":"Recepción";
			//CRUBENCVS 46641 28/11/2022. No incluyo los adjuntos
			//SOAPMessage msg = context.getMessage();
			//ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
	        //msg.writeTo(byteArray);
	        //String soapMessage = new String(byteArray.toByteArray());
			byte[] soap= contenidoMensajeSinAdjuntos(context.getMessage());
			String soapMessage = new String(soap);
			//FIN CRUBENCVS 46641
	        log.info(direccion+":"+soapMessage);
		}
		catch (javax.xml.soap.SOAPException ex)
		{
			if (log!=null)
			{
				log.info("Error en la grabación de log de SOAP cliente:" + ex.getMessage());
			}
			SOAPUtils.generateSOAPErrMessage(context.getMessage(), "Error en proceso de comunicación Gestión de Envíos de Notific@ <--> Servicio Remoto."	, "0002", "SOAP Handler", true);
		}
		catch (TransformerException ex)
		{
			if (log!=null)
			{
				log.info("Error en la grabación de log de SOAP cliente:" + ex.getMessage());
			}
			SOAPUtils.generateSOAPErrMessage(context.getMessage(), "Error en proceso de comunicación Gestión de Envíos de Notific@ <--> Servicio Remoto."	, "0002", "SOAP Handler", true);
		}
		catch (PreferenciasException ex)
		{
			//En este punto es seguro que no tenemos log. Grabamos en la consola de servidor.
			System.err.println ("Servicio Adviser de Notific@: Error de preferencias en manejador SOAP ("+SoapClientHandler.class.getName()+":"+ex.getMessage());
			SOAPUtils.generateSOAPErrMessage(context.getMessage(), "Error en proceso de comunicación Gestión de Envíos de Notific@ <--> Servicio Remoto."	, "0002", "SOAP Handler", true);
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
