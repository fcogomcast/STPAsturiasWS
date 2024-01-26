package es.tributasenasturias.traslado.soap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



import es.tributasenasturias.log.TributasLogger;
import es.tributasenasturias.seguridad.FirmaHelper;
import es.tributasenasturias.seguridad.SeguridadException;
import es.tributasenasturias.traslado.preferencias.Preferencias;
import es.tributasenasturias.traslado.preferencias.PreferenciasException;
import es.tributasenasturias.traslado.util.Constantes;
import es.tributasenasturias.traslado.util.Utils;




public class GestorFirma implements ISOAPHandler{
	
	/**
	 * Incluye en el cuerpo del mensaje SOAP el atributo "id" con el valor "Body"
	 * @param msg
	 * @throws SOAPException
	 */
	private void setBodyId(SOAPMessage msg) throws SOAPException
	{
		msg.getSOAPBody().addAttribute(new QName("id"), "Body");
	}
	
	/**
	 * Comprueba que existe una URI en la firma que apunta al mismo id que el cuerpo de mensaje 
	 * @param msg Mensaje
	 * @return true si existe, false si no
	 * @throws SOAPException Si no puede comprobar si existe
	 */
	private boolean checkSignatureURI(SOAPMessage msg) throws SOAPException{
		String idBody= getBodyId(msg);
		XPath xp= XPathFactory.newInstance().newXPath();
		try {
			NodeList referencias= (NodeList) xp.evaluate("/*[local-name()='Envelope']/*[local-name()='Header']/" +
					"*[local-name()='Signature']/*[local-name()='SignedInfo']/*[local-name()='Reference']", msg.getSOAPPart().getEnvelope(),XPathConstants.NODESET);
			if (referencias==null) {
				throw new SOAPException ("No se han encontrado referencias en la firma");
			}
			for (int x=0;x<referencias.getLength();x++) {
				Node referencia= referencias.item(x);
				NamedNodeMap atributos= referencia.getAttributes();
				if (atributos!=null){
					Node URI= atributos.getNamedItem("URI");
					if (URI!=null) {
						String uriNormalizada=URI.getNodeValue();
						if (uriNormalizada.startsWith("#")) {
							uriNormalizada=uriNormalizada.substring(1); //Ignoramos el '#' para comparar
						}
						if (idBody.equals(uriNormalizada)){
							return true; //Hay una referencia al elemento que buscamos.
						}
					}
				}
			}
			return false;
		} catch (XPathExpressionException xe){
			throw new SOAPException(xe);
		}
	}
	/**
	 * Devuelve el valor del atributo id (ID,Id) en el cuerpo de mensaje
	 * @param msg Mensaje
	 * @return Cadena con el valor del atributo
	 * @throws SOAPException Si no existe cuerpo o no existe atributo
	 */
	private String getBodyId(SOAPMessage msg) throws SOAPException {
		String id=null;
		SOAPBody body= msg.getSOAPBody();
		if (body==null) {
			throw new SOAPException("No existe cuerpo de mensaje. No se puede ejecutar el servicio"); 
		}
		if (body.getAttributeNode("id")!=null){
			id=body.getAttribute("id");
		} else if (body.getAttributeNode("ID")!=null){
			id= body.getAttribute("ID");
		}else if (body.getAttributeNode("Id")!=null){
			id=body.getAttribute("Id");
		}
		if (id==null) {
			throw new SOAPException("No existe atributo id en el mensaje");
		}
		return id;
	}
	/**
	 * Elimina el nodo signature, si existiese, del mensaje SOAP
	 * Se utiliza en la salida, para asegurarnos de que solo se genera un nodo Signature
	 * sin importar lo que la implementación de servicio pueda estar enviando.
	 * @param msg {@link SOAPMessage}
	 * @throws SOAPException
	 */
	private void eliminarSignature(SOAPMessage msg) throws SOAPException
	{
		SOAPHeader header=null;
		try{
			header= msg.getSOAPHeader();
			//Si tiene signature...
			NodeList hijos= header.getChildNodes();
			if (hijos.getLength()==0){
				return;
			}
			for (int i=0;i<hijos.getLength();i++) {
				if (hijos.item(i).getNodeType()==Node.ELEMENT_NODE && 
						"Signature".equals(hijos.item(i).getLocalName())) 
				{
					Node signature= hijos.item(i);
					header.removeChild(signature);
				}
			}
		}catch (SOAPException soap)
		{
			//No hay cabecera, luego no hay signature. Nos sirve.
			//Podría ser que no puede recuperarla, pero eso parece más bien
			//un problema de implementación de SOAP
		}
	}
	/**
	 * Gestiona la firma de mensaje de entrada al servicio, según XMLDSig.
	 * @param context Contexto del mensaje SOAP.
	 */
	@Override
	public void process(SOAPMessageContext context)
	{
		TributasLogger logAplicacion=null;
		ByteArrayInputStream bi=null;
		ByteArrayOutputStream bo=null;
		try
		{
			Preferencias pref = (Preferencias) context.get(Constantes.PREFERENCIAS);
			String idSesion = (String) context.get(Constantes.IDSESION);
			if (idSesion==null)
			{
				idSesion = "Sin sesión";
			}
			if (pref==null)
			{
				pref= new Preferencias();
			}
			if (logAplicacion==null)
			{
				logAplicacion= new TributasLogger(pref.getModoLog(), pref.getFicheroLogAplicacion(),idSesion);
			}
			//Comprobamos si firmamos o validamos.
			Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			if (salida.booleanValue())
			{
				//Firmamos.
				if ("S".equalsIgnoreCase(pref.getFirmarSalida()))
				{
					logAplicacion.debug("Manejador SOAP:[" + GestorFirma.class.getName()+"].Se firma.");
					setBodyId(context.getMessage());
					eliminarSignature(context.getMessage());
					//Recuperamos el texto de mensaje.
					bo=new ByteArrayOutputStream();
					context.getMessage().writeTo(bo);
					//Recuperamos qué mensaje firmamos, el de consulta o pago.
					FirmaHelper f = new FirmaHelper(pref.getEndpointFirma(), new SoapClientHandler(idSesion));
					String soap= new String(bo.toByteArray(),"UTF-8");
					String xmlFirmado=f.firmaXML(soap, pref.getAliasFirma(), pref.getNodoFirmar(), pref.getNodoPadreFirma(), pref.getNamespaceNodoPadre());
					bi = new ByteArrayInputStream(xmlFirmado.getBytes());
					context.getMessage().getSOAPPart().setContent(new StreamSource(bi));
					logAplicacion.debug("Manejador SOAP:[" + GestorFirma.class.getName()+"].Mensaje firmado:"+xmlFirmado);
				}
			}
			else
			{
				//Validamos.
				if ("S".equalsIgnoreCase(pref.getValidarFirma()))
				{
					bo=new ByteArrayOutputStream();
					context.getMessage().writeTo(bo);
					if (!checkSignatureURI(context.getMessage())) {
						logAplicacion.info  ("Manejador SOAP:[" + GestorFirma.class.getName()+"]. La URI de firma no coincide con el identificador de cuerpo de mensaje.");
						Utils.generateSOAPErrMessage(context.getMessage(), "Error en seguridad de mensaje.", "0002", "SOAP Handler",false);
					}
					logAplicacion.debug("Manejador SOAP:[" + GestorFirma.class.getName()+"].Mensaje para validar firma:" + new String(bo.toByteArray()));
					FirmaHelper fh = new FirmaHelper(pref.getEndpointFirma(), new SoapClientHandler(idSesion));
					if (fh.esValido(new String(bo.toByteArray())))
					{
						logAplicacion.info("Manejador SOAP:[" + GestorFirma.class.getName()+"].Firma Mensaje validada");
					}
					else
					{
						logAplicacion.error("Manejador SOAP:[" + GestorFirma.class.getName()+"].Firma Mensaje no válida.");
						Utils.generateSOAPErrMessage(context.getMessage(), "Error en seguridad de mensaje.", "0002", "SOAP Handler",false);
					}
					
				}
			}
		}catch (PreferenciasException e)
		{
			if (logAplicacion!=null) {logAplicacion.error ("Manejador SOAP:[" + GestorFirma.class.getName()+"].Error en preferencias:"+e.getMessage(),e);}
			Utils.generateSOAPErrMessage(context.getMessage(), "Error en seguridad de mensaje.", "0002", "SOAP Handler",true);
		} catch (SOAPException e) {
			if (logAplicacion!=null) {logAplicacion.error ("Manejador SOAP:[" + GestorFirma.class.getName()+"].Error:"+e.getMessage(),e);}
			Utils.generateSOAPErrMessage(context.getMessage(), "Error en seguridad SOAP.", "0002", "SOAP Handler", false);
		} catch (IOException e) {
			if (logAplicacion!=null) {logAplicacion.error ("Manejador SOAP:[" + GestorFirma.class.getName()+"].Error:"+e.getMessage(),e);}
			Utils.generateSOAPErrMessage(context.getMessage(), "Error en seguridad SOAP.", "0002", "SOAP Handler", false);
		} catch (SeguridadException e)
		{
			if (logAplicacion!=null) {logAplicacion.error ("Manejador SOAP:[" + GestorFirma.class.getName()+"].Error:"+e.getMessage(),e);}
			Utils.generateSOAPErrMessage(context.getMessage(), "Error en seguridad SOAP.", "0002", "SOAP Handler", false);
		}
		finally
		{
			if (bi!=null)
			{
				try {bi.close();} catch(Exception e){}
			}
			if (bo!=null)
			{
				try {bo.close();} catch (Exception e){}
			}
		}
	}

}
