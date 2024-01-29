package es.tributasenasturias.ades.soap;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;


import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;




import es.tributasenasturias.ades.XMLUtils;
import es.tributasenasturias.ades.preferencias.Preferencias;
import es.tributasenasturias.ades.wssecurity.WSSecurityFactory;
import es.tributasenasturias.log.ILog;




public class Seguridad implements SOAPHandler<SOAPMessageContext> {

	private Preferencias pref;
	private ILog log;
	private String idLlamada;

	public Seguridad(Preferencias pref, ILog log, String idLlamada)
	{
		this.pref= pref;
		this.log=log;
		this.idLlamada=idLlamada;
	}
	/**
	 * Gestiona la seguridad del mensaje de salida
	 * Firma mensaje a la salida
	 * @param context {@link SOAPMessageContext} con el contexto del mensaje SOAP
	 */
	private void seguridadMensaje(SOAPMessageContext context) throws SOAPFaultException{
		try {
			if (pref==null || log == null)
			{
				throw new Exception ("Error. No se encuentran los objetos de preferencias o log en contexto de llamada SOAP.");
			}
			Boolean salida = (Boolean) context
					.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			if (salida){ 
				String mensajeSalida = XMLUtils.getXMLText(context.getMessage().getSOAPPart());
				mensajeSalida=WSSecurityFactory.newConstructorResultado(log, pref,new SoapClientHandler(idLlamada)).firmaMensaje(mensajeSalida);
				context.getMessage().getSOAPPart().setContent(new StreamSource(new StringReader(mensajeSalida)));
			}
		} 
		catch (Exception ex)
		{
			if (!(ex instanceof SOAPFaultException))
			{
				if (log!=null)
				{
					log.error("Error en las comprobaciones de seguridad del mensaje:"
							+ ex.getMessage(), ex);
				}
				else
				{
					System.err.println (new java.util.Date().toString()+"::Servicio de firma mediante Ades::error en las comprobaciones de seguridad del mensaje::"+ex.getMessage());
					ex.printStackTrace();
					
				}
				SOAPUtils.generateSOAPErrMessage(context.getMessage(), "Error en comunicación WSFirmasAdes <--> Servicio WSSecurity."	, "0002", "SOAP Handler", true);
			}
			else
			{
				throw (SOAPFaultException)ex;
			}
		}
	}

	@Override
	public Set<QName> getHeaders() {
		
		//Indicamos que entendemos la cabecera de seguridad de WS-Security.
		QName security= new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd","Security");
		HashSet<QName> headersEntendidos= new HashSet<QName>();
		headersEntendidos.add(security);
		return headersEntendidos;
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
		seguridadMensaje(context);
		return true;
	}
}
