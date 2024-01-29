/**
 * 
 */
package es.tributasenasturias.webservices.Certificados.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.firma.FirmaDigital;
import es.tributasenasturias.firma.WsFirmaDigital;
import es.tributasenasturias.webservices.Certificados.Exceptions.PreferenciasException;
import es.tributasenasturias.webservices.Certificados.soap.handler.ClientLogHandler;
import es.tributasenasturias.webservices.Certificados.utils.Log.GenericAppLogger;
import es.tributasenasturias.webservices.Certificados.utils.Log.TributasLogger;
import es.tributasenasturias.webservices.Certificados.utils.Preferencias.Preferencias;

/**
 * @author crubencvs
 * Utilizado para ejecutar procedimientos varios de la firma
 */
public class FirmaHelper {
	private static final String NAMESPACE_NODO_PADRE = "http://schemas.xmlsoap.org/soap/envelope/";
	private static final String NODO_PADRE = "Header";
	private static final String ID_FIRMAR = "Body";
	/**
	 * Crea y devuelve un mensaje SOAP con el contenido indicado en el string.
	 * @param xmlContent String con el mensaje SOAP en texto.
	 * @return
	 */
	private SOAPMessage creaMensajeSOAP(String xmlContent) throws SOAPException
	{
		SOAPMessage msg=null;
		SOAPPart part = null;
		MessageFactory msgFactory     = MessageFactory.newInstance();   
		msg = msgFactory.createMessage();
		part = msg.getSOAPPart();
		ByteArrayInputStream bas  = new ByteArrayInputStream(xmlContent.getBytes());
		StreamSource strs = new StreamSource (bas);
		part.setContent(strs);
		return msg;
		
		
	}
	/**
	 * Modifica los prefijos del mensaje SOAP para que sean los que espera 
	 * @param msg
	 * @throws SOAPException
	 */
	private SOAPMessage cambiaPrefijos(SOAPMessage msg) throws SOAPException
	{
		final String prefix="soapenv"; //Es el necesario para el servicio web de firma.
		//Primero, se modifican los prefijos. Han de ser "SOAP".
		msg.getSOAPPart().getEnvelope().setPrefix(prefix);
		//Header podrá tener, o no.
		SOAPHeader header = msg.getSOAPHeader();
		if (header==null)
		{
		     msg.getSOAPPart().getEnvelope().addHeader();
		}
		msg.getSOAPHeader().setPrefix(prefix);
		msg.getSOAPBody().setPrefix(prefix);
		return msg;
	}
	/**
	 * Prepara el mensaje para poder llamar al servicio de firma. Incluye cambiar prefijos y crear un atributo nuevo.
	 * @param msg
	 * @return
	 * @throws SOAPException
	 */
	private SOAPMessage preparaMensajeParaFirmar (SOAPMessage msg) throws SOAPException
	{
		msg=cambiaPrefijos (msg);
		//Ahora, se añade el atributo "Id" al cuerpo.
		Name attr = msg.getSOAPPart().getEnvelope().createName("Id");
		msg.getSOAPBody().addAttribute(attr, ID_FIRMAR);
		return  msg;
	}
	/**
	 * Genera un SOAPMessage firmado a partir de un SOAPMessage de entrada.
	 * @param msg
	 * @return SOAPMessage o null si no se ha podido generar.
	 */
	@SuppressWarnings("unchecked")
	public SOAPMessage firmaMensaje (SOAPMessage msg)
	{
		SOAPMessage firmado=null;
		GenericAppLogger log = new TributasLogger();
		WsFirmaDigital srv = new WsFirmaDigital();
		FirmaDigital srPort = srv.getServicesPort();
		try
		{
			//Se modifica el endpoint
			Preferencias pr = Preferencias.getPreferencias();
			String endpointFirma = pr.getEndPointFirma();
			if (!endpointFirma.equals(""))
			{
				BindingProvider bpr = (BindingProvider) srPort;
				bpr.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,endpointFirma);
				List<Handler> handlers = bpr.getBinding().getHandlerChain();
				if (handlers==null)
				{
					handlers=new ArrayList<Handler>();
				}
				handlers.add(new ClientLogHandler());
				bpr.getBinding().setHandlerChain(handlers);
			}
			//Se modifica el mensaje a firmar. En el "Body", ha de tener un atributo con valor "Body" (Id="Body").
			ByteArrayOutputStream msgTextBin= new ByteArrayOutputStream();
			try
			{
				//Prepara el mensaje para firmar.
				msg = preparaMensajeParaFirmar (msg);
				msg.writeTo(msgTextBin);
				String msgText = new String (msgTextBin.toByteArray());
				//Recuperamos la clave y certificado de firma.
				String certificado = pr.getCertificadoFirma();
				String msgFirmado = srPort.firmarXML(msgText, certificado, ID_FIRMAR, NODO_PADRE, NAMESPACE_NODO_PADRE);
				firmado = creaMensajeSOAP (msgFirmado);
			}
			catch (Exception ex)
			{
				log.error ("Error al firmar el mensaje de salida: " + ex.getMessage());
				log.trace(ex.getStackTrace());
				firmado=null;
			}
		}
		catch (PreferenciasException ex)
		{
			log.error("Error al firmar el mensaje de salida: "+ ex.getError() + "-" + ex.getMessage());
			log.trace(ex.getStackTrace());
			firmado=null;
			
		}
		return firmado;
	}
	/**
	 * Genera un SOAPMessageContext firmado a partir de un SOAPMessageContext de entrada.
	 * @param context
	 * @return SOAPMessageContext firmado o null si no se ha podido firmar.
	 */
	public SOAPMessageContext getContextoFirmado (SOAPMessageContext context)
	{
		SOAPMessageContext contextFirmado=null;
		SOAPMessage msg = context.getMessage();
		SOAPMessage msgFirmado = firmaMensaje (msg);
		GenericAppLogger log = new TributasLogger();
		contextFirmado= context;
		try
		{
		if (msgFirmado!=null)
		{
			contextFirmado.setMessage(msgFirmado);
		}
		else
		{
			log.error("No se ha podido implementar el mensaje firmado.");
			contextFirmado=null;
		}
		}
		catch (Exception ex)
		{
			log.error("Error al cambiar el contexto."+ ex.getMessage());
			contextFirmado=null;
		}
		return contextFirmado;
	}
}
