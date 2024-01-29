/**
 * Handler respuesta NumerosAutoliquidacion 
 */
package es.tributasenasturias.soap.handler;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.tributasenasturias.utilsProgramaAyuda.Logger;


/**
 * @author crubencvs
 *
 */

public class NumerosAutoliquidacionHandler implements SOAPHandler<SOAPMessageContext>{
	public static final String IP_KEY = "CLIENT_IP";
	private static final String NAME_IP = "ip";
	private static final String NAME_CITA_PREVIA = "citaPrevia";
	private static final String NS_CABECERA = "http://webservice.tributasenasturias.es/";
	@Override
	public void close(MessageContext context) {
	}

	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return false;
	}
	/**
	 * Recupera, si existe, el valor del nodo de cabecera "citaPrevia" y lo pone en el contexto de la aplicación 
	 * @param context SOAPMessageContext
	 * @throws SOAPException
	 */
	private void tratarCitaPrevia(SOAPMessageContext context) throws SOAPException
	{
		SOAPHeader header = context.getMessage().getSOAPHeader();
		NodeList nodos = header.getElementsByTagNameNS(NS_CABECERA, NAME_CITA_PREVIA);
		if (nodos.getLength()>0)
		{
			//Existe el nodo "citaPrevia". Buscamos el nodo "ip"
			SOAPHeaderElement citaPrevia = (SOAPHeaderElement)header.getChildElements(new QName(NS_CABECERA,NAME_CITA_PREVIA)).next();
			nodos = citaPrevia.getElementsByTagNameNS(NS_CABECERA, NAME_IP);
			if (nodos.getLength()>0)
			{
				//Pasamos el  valor al contexto de llamada
				Node ip = (Node)citaPrevia.getChildElements(new QName(NS_CABECERA,NAME_IP)).next();
				String text="";
				if (ip.hasChildNodes())
				{
					for (int k=0;k<ip.getChildNodes().getLength();k++)
					{
						Node n = ip.getChildNodes().item(k);
						if (n.getNodeType()==Node.TEXT_NODE)
						{
							text = n.getNodeValue();
							break;
						}
					}
				}
				if (!"".equals(text))
				{
					context.put(IP_KEY, text);
					context.setScope(IP_KEY, MessageContext.Scope.APPLICATION);
				}
			}
		}
	}
	@Override
	public boolean handleMessage(SOAPMessageContext context) {										
		boolean valido=true;									
			SOAPMessage msg = ((SOAPMessageContext) context).getMessage();											
			SOAPPart part =msg.getSOAPPart();
			SOAPHeader header = null;						
			try {									
				SOAPEnvelope env= part.getEnvelope();
				header=env.getHeader();
				if (header==null)
				{
					header=env.addHeader();
				}
				NodeList nodos = header.getElementsByTagNameNS(NS_CABECERA, "version");
				SOAPHeaderElement headerElement;
				if (nodos.getLength()==0)//No existe el nodo
				{
					headerElement = header.addHeaderElement(new QName(NS_CABECERA, "version"));
				}
				else
				{
					//Suponemos que hay uno sólo. Que hay uno lo sabemos.
					headerElement= (SOAPHeaderElement) header.getChildElements(new QName(NS_CABECERA, "version")).next();
				}
                   																					
				headerElement.addTextNode("1.0");											
			} catch (SOAPException e1) {Logger.error("Error SOAP en handler:"+e1.getMessage());}			
			catch (Exception e) {
				 Logger.error("Error en handler:"+e.getMessage());
			}
			
			
			//Sólo para mensajes de entrada, se valida la firma			
			Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			if (!salida.booleanValue())
			{
				try {
					tratarCitaPrevia(context);
				} catch (SOAPException e) {
					Logger.error("Error en tratarCitaPrevia:"+e.getMessage());
					try {
						SOAPFactory f = SOAPFactory.newInstance();
						SOAPFault fault = f.createFault("Error en el proceso de entrada al servicio",new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"));
						throw new SOAPFaultException(fault);
					} catch (SOAPException e1) {
						Logger.error("Error al tratar el error de tratarCitaPrevia:"+e1.getMessage());
					}
				}
			}
			if (salida.booleanValue())
			{															
				try {																			   					
				
					ByteArrayOutputStream baos = new ByteArrayOutputStream();               
					msg.writeTo(baos);													
					context.setMessage(msg);									
					valido=true;
									
					if ( !valido) {	
						valido=false;
						Logger.error("No se ha podido validar la firma. Revise el resto del log para encontrar más información.");				
					}												
				} catch (DOMException e) {
					valido=false;
					Logger.error("No se ha podido firmar. Revise el resto del log para encontrar más información."+e.getMessage());
				} catch (Exception e) {					
					valido=false;
					Logger.error("No se ha podido firmar. Revise el resto del log para encontrar más información."+e.getMessage());
				}					
			}					
		return valido;
	}

}
