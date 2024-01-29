/**
 * 
 */
package es.tributasenasturias.webservices.Certificados.utils;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

import es.tributasenasturias.webservices.Certificados.utils.Log.GenericAppLogger;
import es.tributasenasturias.webservices.Certificados.utils.Log.TributasLogger;

/**
 * @author crubencvs
 *
 */
public class SOAPFaultHelper {
	 public static SOAPFaultException createSOAPFaultException(String pFaultString) 
	 {
		 SOAPFaultException sex=null;
		 try {
		 SOAPFault fault = SOAPFactory.newInstance().createFault();
		 fault.setFaultString(pFaultString);
		 fault.setFaultCode(new QName(SOAPConstants.URI_NS_SOAP_ENVELOPE, "Servidor"));
		 sex= new SOAPFaultException(fault);
		 }
		 catch (Exception ex)
		 {
			 GenericAppLogger log = new TributasLogger();
			 log.error("Error grave al construir el SOAP Fault para : FaultString--> "+ pFaultString  );
			 throw new RuntimeException ("Excepción grave al devolver el mensaje.");
			 
		 }
		 return sex;
	} 
}
