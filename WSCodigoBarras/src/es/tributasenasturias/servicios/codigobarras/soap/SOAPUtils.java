package es.tributasenasturias.servicios.codigobarras.soap;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.UUID;

import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.soap.SOAPFaultException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class SOAPUtils {

	/**
	 * Recupera el nodo texto hijo de otro. S�lo recupera uno. Esta funci�n y setNodeText se incluyen es esta clase para que 
	 * no tenga dependencias externas.
	 * @param nodo Nodo de donde recuperar el valor
	 * @return
	 */
	private static Node getNodoTexto (Node nodo)
	{
		Node nodoValor=null;
		Node hijo=null;
		if (nodo!=null)
		{
			if (nodo.hasChildNodes())
			{
				for (hijo=nodo.getFirstChild();hijo!=null;hijo=hijo.getNextSibling())
				{
					if (hijo.getNodeType()==Node.TEXT_NODE||hijo.getNodeType()==Node.CDATA_SECTION_NODE)
					{
						nodoValor = hijo;// El texto del nodo est� en el nodo texto hijo.
						break;
					}
				}
			}
		}
		return nodoValor;
	}
	/**
	 * Da texto a un nodo
	 * @param doc Documento
	 * @param nodo Nodo al que dar valor
	 * @param texto   texto del nodo
	 */
	private static  void setNodeText (Document doc,Node nodo, String texto)
	{
		Node valorAnterior = getNodoTexto (nodo);
		if (valorAnterior != null) //Exist�a un nodo previo con valor
		{
			valorAnterior.setNodeValue(texto);
		}
		else
		{
			Node textN = doc.createTextNode(texto);
			nodo.appendChild(textN);
		}
	}
	/**
	 * Genera un SOAP Fault. El fault contendr� una secci�n de detalle.
	 * @param msg Mensaje SOAP sobre el que se generar� el error. Se utilizar� para a�adirle la secci�n SOAP Fault 
	 * @param reason Texto de raz�n de error (aparecer� en el faultString  )
	 * @param codigo C�digo de error que aparecer� en el detalle.
	 * @param mensaje Mensaje que aparecer� en el detalle.
	 * @param server true si el SOAPFault est� producido por un error en el proceso de servidor, false si est� producido por un error en los datos enviados por el cliente. 
	 */
	@SuppressWarnings("unchecked")
	public static void generateSOAPErrMessage(SOAPMessage msg, String reason, String codigo, String mensaje,boolean server) {
	       try {
	    	  SOAPEnvelope soapEnvelope= msg.getSOAPPart().getEnvelope();
	          SOAPBody soapBody = msg.getSOAPPart().getEnvelope().getBody();
	          SOAPFault soapFault = soapBody.addFault();
	          if (server)
	          {
	        	  soapFault.setFaultCode(new QName(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE,"Server"));
	          }
	          else
	          {
	        	  soapFault.setFaultCode(new QName(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE,"Client"));
	          }
	          soapFault.setFaultString(reason);
	          Detail det= soapFault.addDetail();
	          Name name = soapEnvelope.createName("id");
	          det.addDetailEntry(name);
	          
	          name = soapEnvelope.createName("mensaje");
	          det.addDetailEntry(name);
	          DetailEntry entry;
	          Iterator<DetailEntry> it=det.getDetailEntries();
	          while (it.hasNext())
	          {
	        	  entry=it.next();
	        	  if (entry.getLocalName().equals("id"))
	        	  {	
	        		setNodeText (entry.getOwnerDocument(),entry, codigo);  
	        	  }
	        	  if (entry.getLocalName().equals("mensaje"))
	        	  {	
	        		setNodeText (entry.getOwnerDocument(),entry, mensaje);  
	        	  }
	        		  
	          }
	          throw new SOAPFaultException(soapFault); 
	       }
	       catch(SOAPException e) { }
	}
	/**
	 * Codificaci�n en hexadecimal
	 * @param aInput
	 * @return
	 */
	private static String hexEncode( byte[] aInput){
		int MASK_240 = 0xf0;
		int SHIFT_DIVIDIR_16 = 4;
		int MASK_15 = 0x0f;
	    StringBuilder result = new StringBuilder();
	    char[] digits = {'0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f'};
	    for ( int idx = 0; idx < aInput.length; ++idx) {
	      byte b = aInput[idx];
	      result.append( digits[ (b&MASK_240) >> SHIFT_DIVIDIR_16 ] );
	      result.append( digits[ b&MASK_15] );
	    }
	    return result.toString();
	  }

	/**
	 * Recupera un checkcum basado en un identificador �nico. Se asegura que el identificador es �nico.
	 */
	public static String getIdLlamada()
	{
		UUID id=UUID.randomUUID();
		String identificador=null;
		byte[] mensaje = id.toString().getBytes();
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(mensaje);
			identificador=hexEncode(md.digest());

		}catch(NoSuchAlgorithmException nsae){
			identificador="";
		}
		return identificador;
	}
}
