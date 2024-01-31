/**
 * 
 */
package es.tributasenasturias.soap.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.tributasenasturias.modelo600utils.Preferencias;
import es.tributasenasturias.modelo600utils.Utils;
import es.tributasenasturias.operaciones.firma.FirmaFactory;
import es.tributasenasturias.operaciones.firma.FirmaHelper;

/**
 * @author davidsa
 */
public class FirmaHandler implements SOAPHandler<SOAPMessageContext> {

	private String MSG_ERROR_F002 = " Error grave en la mensajería. Contacte con el servicio técnico de EPST.";
	
	@Override
	public void close(MessageContext context) {}

	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return false;
	}
	/**
     * Prepara el mensaje de salida para poder ser firmado. Elimina el nodo "Signature" que pudiera
     * haber en el nodo "Header" y añade un id al elemento "Body" del mensaje SOAP.
     * @param mensaje {@link SOAPMessage} que sale.
     * @throws SOAPException
     */
    private void preparaMensaje(SOAPMessage mensaje) throws SOAPException
    {
    	Node remesa;
    	try
    	{
	    	javax.xml.xpath.XPath xpath=javax.xml.xpath.XPathFactory.newInstance().newXPath();
			NodeList nodos = (NodeList) xpath.evaluate("//*[local-name()='remesa']",
					mensaje.getSOAPPart().getDocumentElement(), javax.xml.xpath.XPathConstants.NODESET);
			if (nodos.getLength()>0)
			{
				remesa = nodos.item(0);
				Node signature=(Node)xpath.evaluate("./*[local-name()='Signature']", remesa,javax.xml.xpath.XPathConstants.NODE);
				if (signature!=null)
				{
					remesa.removeChild(signature);
				}
				//Hemos quitado el nodo Signature que podría haber, creamos un id para el cuerpo.
				nodos = (NodeList) xpath.evaluate("./*[local-name()='declaracion']",
						remesa, javax.xml.xpath.XPathConstants.NODESET);
				if (nodos.getLength()>0)
				{
					Node declaracion=nodos.item(0);
					NamedNodeMap atts = declaracion.getAttributes();
					if (atts.getLength()>0)
					{
						//Si no hay nodo "ID".
						if (atts.getNamedItem("id")==null && atts.getNamedItem("ID")==null)
						{
							((Element)declaracion).setAttribute("id", "declaracion");
						}
						else if (atts.getNamedItem("id")!=null)
						{
							((Element)declaracion).setAttribute("id", "declaracion");
						}
						else if (atts.getNamedItem("ID")!=null)
						{
							((Element)declaracion).setAttribute("id", "declaracion");
						}
					}
					else
					{
						((Element)declaracion).setAttribute("id", "declaracion");
					}
				}
		}
    	}
    	catch (XPathExpressionException e)
    	{
    		Utils.generateSOAPErrMessage (mensaje,"F002","F002",MSG_ERROR_F002);
    	}
    }
	@Override
	// Se modifica el contexto, por lo que ha de hacerse sincronizado.
	public synchronized boolean handleMessage(SOAPMessageContext context) throws SOAPFaultException {
		try {
			Boolean salida = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			Preferencias pref = new Preferencias();
			pref.CargarPreferencias();
			//-- FIRMAR SALIDA DEL SERVICIO --
			if (salida.booleanValue() && pref.getFirmaDigital().equals("S")) {
				preparaMensaje(context.getMessage());
				ByteArrayOutputStream ba=new ByteArrayOutputStream();
				context.getMessage().writeTo(ba);
				String xml=new String(ba.toByteArray(),Charset.forName("UTF-8"));
				FirmaFactory frFactory= new FirmaFactory();
				FirmaHelper firmaDigital = frFactory.newFirmaHelper();

				//log.debug("LO QUE MANDO A FIRMAR:"+cpl.getNodoResultado("CLOB_DATA"));
				String ns = context.get("nsFirma").toString();
				String respuesta = firmaDigital.firmaMensaje(xml,ns);
				ByteArrayInputStream bas = new ByteArrayInputStream(respuesta.getBytes());
				StreamSource strs = new StreamSource(bas);
				context.getMessage().getSOAPPart().setContent(strs);
				//log.debug("RESPUESTA:"+respuesta);							
			}
		}catch (Exception e) {
			Utils.generateSOAPErrMessage (context.getMessage(),"F002","F002",MSG_ERROR_F002);
		}
		return true;
	}
}