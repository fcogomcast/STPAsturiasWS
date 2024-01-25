package es.tributasenasturias.services.ws.wsepi.utils.log.LogHandler;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
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
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import es.tributasenasturias.services.ws.wsepi.Constantes;
import es.tributasenasturias.services.ws.wsepi.utils.log.Preferencias.Preferencias;







/**
 * Carga datos en el contexto de servicio que serán utilizados
 * en el resto del servicio.
 * Estos objetos serán el log de aplicación, los mensajes de aplicación, las preferencias, y el id de sesión.
 * 
 * @author crubencvs
 *
 */
public class CargaContexto implements SOAPHandler<SOAPMessageContext> {

	/**
	 * Destruye las variables almacenadas en el contexto. Se hace así
	 * para asegurar que se libera toda la memoria.
	 * Estas variables se almacenan en el contexto del hilo de ejecución, con lo cual
	 * estarían vivas un tiempo indeterminado. De esta forma, las marcamos 
	 * para que el recolector de basura las elimine.
	 * Aunque no se hiciera no sería muy grave, porque nunca habría más de una copia
	 * por hilo, y la siguiente petición que entrara al hilo machacaría estos 
	 * datos.
	 * @param context
	 */
	private void destruirContextoSesion(SOAPMessageContext context)
	{
		//A la salida, nos cargamos los datos de contexto.
		context.remove(Constantes.ID_LLAMADA);
		context.remove(Constantes.PREFERENCIAS);
	}
	
	/**
	 * Codifica una serie de bytes en representación hexadecimal
	 * @param aInput Array de bytes
	 * @return  Representación hexadecimal del Array de bytes
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
	 * Recupera un checkcum basado en un identificador único. Se asegura que el identificador es único.
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
	/**
	 * Recupera el texto de un nodo.
	 * @param nodo Nodo del que tomar el texto
	 * @return Texto del nodo
	 */
	private static Node getTextoNodo (Node nodo)
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
						nodoValor = hijo;// El texto del nodo está en el nodo texto hijo.
						break;
					}
				}
			}
		}
		return nodoValor;
	}
	/**
	 * Crea un nodo texto en el nodo indicado
	 * @param doc Documento
	 * @param nodo Nodo en el que incluir otro nodo de tipo texto
	 * @param texto Texto a incluir
	 */
	private static  void setNodeText (Document doc,Node nodo, String texto)
	{
		Node valorAnterior = getTextoNodo (nodo);
		if (valorAnterior != null) //Existía un nodo previo con valor
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
	 * Genera un SOAP Fault. El fault contendrá una sección de detalle.
	 * @param msg Mensaje SOAP sobre el que se generará el error. Se utilizará para añadirle la sección SOAP Fault 
	 * @param reason Texto de razón de error (aparecerá en el faultString  )
	 * @param codigo Código de error que aparecerá en el detalle.
	 * @param mensaje Mensaje que aparecerá en el detalle.
	 * @param server true si el SOAPFault está producido por un error en el proceso de servidor, false si está producido por un error en los datos enviados por el cliente. 
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
	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		//Aunque se haya producido fallo, nos aseguramos de destruir el contexto
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (!salida.booleanValue())
		{
		destruirContextoSesion(context);
		}
		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		try
		{

			//A la entrada, cargamos lo necesario, o paramos y no continuamos si hay algún fallo.
			if (!salida.booleanValue())
			{
				//Lo generamos, para que exista.
				String idSesion=getIdLlamada();
				context.put(Constantes.ID_LLAMADA, idSesion);
				context.setScope(Constantes.ID_LLAMADA, MessageContext.Scope.APPLICATION);
				Preferencias pref = Preferencias.getPreferencias();
				context.put (Constantes.PREFERENCIAS,pref);
				context.setScope(Constantes.PREFERENCIAS, MessageContext.Scope.APPLICATION);
			}
			else
			{
				destruirContextoSesion(context);
			}
		}
		catch (Exception ex)
		{
			System.err.println ("WSEPI:: error en preferencias ::"+ex.getMessage());
			ex.printStackTrace();
			generateSOAPErrMessage(context.getMessage(),"Error en proceso de mensaje SOAP.","0001","Error al crear el entorno de la llamada", true);
		} 
		return true;
	}

}
