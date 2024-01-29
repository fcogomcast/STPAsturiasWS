package es.tributasenasturias.webservices.Certificados.soap.handler;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.w3c.dom.NodeList;

import es.tributasenasturias.webservices.Certificados.impl.Flags;
import es.tributasenasturias.webservices.Certificados.utils.Constantes;
import es.tributasenasturias.webservices.Certificados.utils.SOAPFaultHelper;
import es.tributasenasturias.webservices.Certificados.utils.XMLDOMUtils;
import es.tributasenasturias.webservices.Certificados.utils.Log.GenericAppLogger;
import es.tributasenasturias.webservices.Certificados.utils.Log.TributasLogger;
/**
 * Trata los flag que puedan venir en la cabecera SOAP:Header
 * @author crubencvs
 *
 */
public class TratarSOAPHeader implements SOAPHandler<SOAPMessageContext>{


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
			//Destruimos el flag de red SARA, si existiera
			if (context.containsKey(Constantes.FLAGS))
			{
				context.remove(Constantes.FLAGS);
			}
			return true; //Propagamos
		}
		return false;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context){
		//Sólo para entrada.
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		GenericAppLogger log = new TributasLogger();
		Flags flags = new Flags();
		if (!salida.booleanValue())
		{
			log.info(">>>Se gestiona la cabecera para comprobar los flags incluidos.");
			try
			{
				flags.setRedSARA(esRedSARA(context.getMessage().getSOAPHeader()));
				//Lo ponemos en contexto, para pasarlo a la implementación de servicio
				context.put(Constantes.FLAGS, flags);
				context.setScope(Constantes.FLAGS, MessageContext.Scope.APPLICATION);
			}
			catch (SOAPFaultException ex)
			{
				log.error(">>>Al comprobar los flags de cabecera se ha producido un error:" + ex.getMessage());
				throw ex;
			}
			catch (Exception ex)
			{
					log.error("Error al modificar el mensaje: "+ ex.getMessage());
					throw SOAPFaultHelper.createSOAPFaultException("Error al procesar los flags de cabecera del servicio web");
			}
		}
		else
		{
			//Destruimos el flag de Red SARA, si hubiera
			if (context.containsKey(Constantes.FLAGS))
			{
				context.remove(Constantes.FLAGS);
			}
		}
		return true;
	}
	/**
	 * Comprueba si el mensaje tiene un flag que indique que ha entrado por la Red SARA
	 * @param header
	 * @return
	 */
	private boolean esRedSARA (SOAPHeader header)
	{
		boolean res=false;
		//Recuperamos de cabecera el dato que nos dice si es o no de red SARA. Esto lo pondrá el proxy
		NodeList nodos = header.getElementsByTagNameNS("http://webservice.tributasenasturias.es/", "red_sara");
		if (nodos.getLength()>0)
		{
			String val =  XMLDOMUtils.getNodeText(nodos.item(0));
			if (val.equalsIgnoreCase("true"))
			{
				res=true;
			}
			else
			{
				res=false;
			}
		}
				
		return res;
	}
	
}
