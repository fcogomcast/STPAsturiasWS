/**
 * 
 */
package es.tributasenasturias.webservices.Certificados.soap.handler;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.webservices.Certificados.Exceptions.PreferenciasException;
import es.tributasenasturias.webservices.Certificados.utils.FirmaHelper;
import es.tributasenasturias.webservices.Certificados.utils.SOAPFaultHelper;
import es.tributasenasturias.webservices.Certificados.utils.Log.GenericAppLogger;
import es.tributasenasturias.webservices.Certificados.utils.Log.TributasLogger;
import es.tributasenasturias.webservices.Certificados.utils.Preferencias.Preferencias;
import es.tributasenasturias.webservices.Certificados.validacion.FirmaValidator;

/**
 * @author crubencvs
 *
 */
public class FirmaMessageHandler implements SOAPHandler<SOAPMessageContext>{

	
	public void tratarError(SOAPMessageContext context)
	{
		try
		{
			SOAPBody body = context.getMessage().getSOAPBody();
			SOAPFault orgFault = null; //Fault que podría venir en el mensaje de salida.
			SOAPFault fault=null;
			if (body.hasFault())
			{
				orgFault=body.getFault();
				fault=orgFault;
			}
			else
			{
				fault = body.addFault();
			}
			QName faultName = new QName(SOAPConstants.URI_NS_SOAP_ENVELOPE, "Server");
			fault.setFaultCode(faultName);
			fault.setFaultActor("");
			fault.setFaultCode("");
			fault.setFaultString(("Error no controlado."));
		}
		catch (Exception ex)
		{ 
			GenericAppLogger log = new TributasLogger();
			log.error("Excepción al manejar el fallo del mensaje SOAP:" + ex.getMessage());
		}
	}
	@Override
	public void close(MessageContext context) {
	}

	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (salida)
		{
			return true; //Propagamos
		}
		return false;
	}
	@Override
	//Se modifica el contexto, por lo que ha de hacerse sincronizado?.
	public boolean handleMessage(SOAPMessageContext context) {
		SOAPMessage msg=null;
		msg= context.getMessage();
		GenericAppLogger log = new TributasLogger();
		Preferencias pref;
		try {
			pref =  Preferencias.getPreferencias();
		} catch (PreferenciasException p)
		{
			log.error("No se ha podido realizar la firma. Problema con las preferencias:"+ p.getMessage());
			throw SOAPFaultHelper.createSOAPFaultException("Error.");
		}
		//Sólo para mensajes de entrada, se valida la firma
		boolean valido=true;
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (!salida.booleanValue())
		{	
			if ("S".equalsIgnoreCase(pref.getValidaFirma()))
			{ 
				log.info(">>>Se valida la firma del mensaje de entrada.");
				valido=false;
				FirmaValidator firmav = new FirmaValidator();
				valido =firmav.isValid(msg);
				if ( !valido)
				{
					log.error("No se ha podido validar la firma. Revise el resto del log para poder encontrar más información.");
					if (!firmav.getResultado().toString().equals(""))
					{
						log.error(firmav.getResultado().toString());
					}
					throw SOAPFaultHelper.createSOAPFaultException("Error.");
					
				} 
			}
			else
			{
				log.info(">>>Validación de firma desactivada.");
			}
		}
		else
		{
			if ("S".equalsIgnoreCase(pref.getFirmarSalida()))
			{
				try
				{
					//Siempre y cuando no haya un "Fault" generado de un nivel inferior (Autenticación o Log de mensajes)
					if (!msg.getSOAPBody().hasFault())
					{
						log.info(">>>Se firma el mensaje de salida.");
						//En la salida se firma mensaje y se sustituye el que iba a salir por el firmado.
						FirmaHelper firm= new FirmaHelper();
						SOAPMessageContext contextFirmado=firm.getContextoFirmado(context);//Realmente aquí ya modifica "context". Debería guardarse una copia.
						if (contextFirmado!=null)
						{
							context = contextFirmado;
							valido=true;
						}
						else //Fallo.
						{
							log.error("No se ha podido realizar la firma. Revise el resto del log para encontrar más información.");
							throw SOAPFaultHelper.createSOAPFaultException("Error.");
						}
					}
					else
					{
						valido=true; // Si hay un "Fault" el proceso continúa a través de esta capa sin hacer nada.
					}
				}
				catch (SOAPException ex)
				{
					log.error("Error al recuperar el cuerpo SOAP en la salida  :" + ex.getMessage() );
					throw SOAPFaultHelper.createSOAPFaultException("Error.");
				}
			}
			else
			{
				log.info(">>>Firma de mensaje está desactivada.");
			}
			
		}
		return valido;
	}

}
