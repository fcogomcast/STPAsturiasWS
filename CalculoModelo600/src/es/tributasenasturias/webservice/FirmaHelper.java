package es.tributasenasturias.webservice;
import java.io.ByteArrayInputStream;
import javax.xml.transform.stream.StreamSource;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.BindingProvider;
import es.tributasenasturias.utils.*;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.firmadigital.client.WsFirmaDigital;
import es.tributasenasturias.firmadigital.client.FirmaDigital;

/**
 * @author davidsa Firma respuesta Web Service Ancert
 */
public class FirmaHelper {
	private Preferencias pref = new Preferencias();
	/* Constructor */
	public FirmaHelper () {
		pref.CompruebaFicheroPreferencias();		
	}
	
	/**
	 * Crea y devuelve un mensaje SOAP con el contenido indicado en el string.
	 * 
	 * @param xmlContent
	 *            String con el mensaje SOAP en texto.
	 * @return
	 */
	public SOAPMessage creaMensajeSOAP(String xmlContent) throws SOAPException {
		SOAPMessage msg = null;
		SOAPPart part = null;
				
		MessageFactory msgFactory = MessageFactory.newInstance();		
		msg = msgFactory.createMessage();		
		part = msg.getSOAPPart();
		
		ByteArrayInputStream bas;
		try {		
			bas = new ByteArrayInputStream(xmlContent.getBytes("ISO-8859-1"));
			//bas = new ByteArrayInputStream(xmlContent.getBytes());			
			StreamSource strs = new StreamSource(bas);		
			part.setContent(strs);		
		}catch (Exception e) {			
			Logger.error("Error:"+e.getMessage());			
		}				
		return msg;
	}
	
	/**
	 * Genera un SOAPMessage firmado a partir de un SOAPMessage de entrada.
	 * 
	 * @param msg
	 * @return SOAPMessage o null si no se ha podido generar.
	 */
	public String firmaMensaje(String msg) {		
		String firmado = null;
		WsFirmaDigital srv = new WsFirmaDigital();					
		FirmaDigital srPort = srv.getServicesPort();
		try {			
			// cargamos los datos del almacen de un fichero xml preferencias
			pref.CargarPreferencias();								
			
			if (pref.getDebug().equals("1")) {
				Logger.info("pref.getEndpointFirma():"+pref.getEndpointFirma());
				Logger.info("Alias certificado:"+pref.getAliasCertificado());
				Logger.info("Id nodo Firmar:"+pref.getIdNodoFirma());
				Logger.info("Nodo Padre:"+pref.getNodoPadre());
				Logger.info("Namespace nodo padre:"+pref.getNamespaceNodoPadre());
				Logger.info("Algoritmo de firma:"+pref.getUriAlgoritmoFirma());
				Logger.info("Algoritmo de Digest:"+pref.getUriAlgoritmoDigest());
			}
			
			//Se modifica el endpoint
			String endpointFirma = pref.getEndpointFirma();			
			if (!endpointFirma.equals("")) {
				BindingProvider bpr = (BindingProvider) srPort;
				bpr.getRequestContext().put(
						BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
						endpointFirma);
			}

			try {											
				// CRUBENCVS 47084. 20/02/2023 Versión de firma para 
				// indicar  algoritmo
				//String msgFirmado = srPort.firmarAncert(msg, certificado, clave);
				String msgFirmado = srPort.firmarXMLAlgoritmo(msg, pref.getAliasCertificado(), pref.getIdNodoFirma(), pref.getNodoPadre(), pref.getNamespaceNodoPadre(), pref.getUriAlgoritmoFirma(), pref.getUriAlgoritmoDigest());
				//FIN CRUBENCVS 47084
			    if (pref.getDebug().equals("1")) {			   				    
				    Logger.info("MENSAJE QUE LLEGA AL SERVICIO DE FIRMA"+msg);				    
				    if (srPort.validar(msgFirmado))
				    	Logger.info("INTEGRIDAD DE LA FIRMA CORRECTA!!");
				    else
				    	Logger.error("Error al validar integridad de la firma ::"+ new String(msgFirmado));
			    }			    			    
			    	
				firmado = msgFirmado;								
			} catch (Exception ex) {
				Logger.error("Error al firmar el mensaje de salida: "
						+ ex.getMessage());
				firmado = null;
			}
		} catch (Exception ex) {
			Logger.error("Error al firmar el mensaje de salida: "
					+ ex.getMessage());
			firmado = null;
		}
	    Logger.info("RETURN MENSAJE FIRMADO:"+firmado);
		return firmado;
	}	

}
