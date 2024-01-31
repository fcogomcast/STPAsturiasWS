package es.tributasenasturias.webservices.soap;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * Realiza el preproceso de los mensajes a Caja Rural. Por el momento,
 * sólo se utiliza para tratar el "mustUnderstand" de la Seguridad 
 * de Caja Rural.
 * @author crubencvs
 *
 */
public class PreprocesoCajaRural implements SOAPHandler<SOAPMessageContext>{

	/**
	 * Procesa el atributo "mustUnderstand" que se incluye en los mensajes de Caja Rural. 
	 */
	@Override
	public Set<QName> getHeaders() {
		Set<QName> headers = new HashSet<QName>();
    	QName nm = new QName("http://schemas.xmlsoap.org/soap/security/2000-12","Signature");
    	headers.add(nm);
    	return headers;
	}

	/* (non-Javadoc)
	 * @see javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public void close(MessageContext context) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see javax.xml.ws.handler.Handler#handleFault(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.xml.ws.handler.Handler#handleMessage(javax.xml.ws.handler.MessageContext)
	 */
	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		return true;
	}

}
