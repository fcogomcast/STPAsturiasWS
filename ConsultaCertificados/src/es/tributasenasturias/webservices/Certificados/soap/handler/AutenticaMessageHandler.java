package es.tributasenasturias.webservices.Certificados.soap.handler;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import es.tributasenasturias.webservices.Certificados.utils.SOAPFaultHelper;
import es.tributasenasturias.webservices.Certificados.utils.Log.GenericAppLogger;
import es.tributasenasturias.webservices.Certificados.utils.Log.TributasLogger;
import es.tributasenasturias.webservices.Certificados.validacion.Certificado.CertificadoValidator;

public class AutenticaMessageHandler implements SOAPHandler<SOAPMessageContext>{

	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	@Override
	public void close(MessageContext context) {
		// TODO Auto-generated method stub
		
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
	public boolean handleMessage(SOAPMessageContext context) {
		//Sólo para entrada.
		SOAPMessage msg = context.getMessage();
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		GenericAppLogger log = new TributasLogger();
		if (!salida.booleanValue())
		{
			log.info (">>>Se valida el certificado del mensaje de entrada.");
			try
			{
			CertificadoValidator cert = new CertificadoValidator();
			if (!cert.isValid(msg.getSOAPHeader()))
			{
				log.error("No se ha podido validar el certificado. Revise el resto del log para encontrar más información.");
				if (cert.getResultado().toString().equals(""))
				{
					log.error(cert.getResultado().toString());
				}
				throw SOAPFaultHelper.createSOAPFaultException("Error.");
				
			}
			else //Se guarda en el contexto los datos del certificado.
			{
				context.put("InfoCertificado", cert.getInfoCertificado());
				//Aunque es el valor por defecto, se hace que esta información esté disponible sólo 
				// para manejadores.
				context.setScope("InfoCertificado",MessageContext.Scope.HANDLER);
			}
				}
			catch (SOAPException ex)
			{
				log.error("Error al validar el certificado: "+ ex.getMessage());
				throw SOAPFaultHelper.createSOAPFaultException("Error.");
			}
			catch (Exception ex)
			{
				if (!(ex instanceof javax.xml.ws.soap.SOAPFaultException))
				{
					log.error("Error no controlado al validar el certificado: "+ ex.getMessage());
					throw SOAPFaultHelper.createSOAPFaultException("Error.");	
				}
				else
				{
					throw (SOAPFaultException) ex;
				}
			}
		}
		return true;
	}

}
