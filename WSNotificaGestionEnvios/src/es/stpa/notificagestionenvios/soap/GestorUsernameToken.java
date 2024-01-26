package es.stpa.notificagestionenvios.soap;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Set;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


import es.stpa.notificagestionenvios.Base64;
import es.stpa.notificagestionenvios.Constantes;
import es.stpa.notificagestionenvios.preferencias.Preferencias;
import es.stpa.notificagestionenvios.preferencias.PreferenciasException;
import es.tributasenasturias.xml.XMLDOMUtils;

public class GestorUsernameToken implements SOAPHandler<SOAPMessageContext>{

	
	private void estableceUserNameToken(SOAPMessage soap,String usuario, String password) throws Exception{
		//Esto es una gochada, pero para que vayan probando, si nos da acceso, me vale.
		//Hay que cambiarlo por llamada al servicio de WSSecurity, cuando lo tengamos.
	    String created;
	    String id;
	    String peticion = "<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"> <wsse:UsernameToken wsu:Id=\"UsernameToken-#ID#\"> <wsse:Username>#API_KEY#</wsse:Username> <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">#PASSWORD#</wsse:Password> <wsse:Nonce EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\">#NONCE#</wsse:Nonce> <wsu:Created>#CREATED#</wsu:Created> </wsse:UsernameToken> </wsse:Security>";
	    SecureRandom srandom = SecureRandom.getInstance("SHA1PRNG");
	    srandom.setSeed(System.currentTimeMillis());
	    byte[] nonceBytes= new byte[16];
	    srandom.nextBytes(nonceBytes);
	    created= DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()).toString();
	    MessageDigest dig = MessageDigest.getInstance("SHA-1");
	    dig.update(nonceBytes);
	    dig.update(created.getBytes("UTF-8"));

	    dig.update(usuario.getBytes("UTF-8"));
	    byte[] digest= dig.digest();
	    password= new String(Base64.encode(digest));
	    byte[] idbytes= new byte[12];
	    srandom.nextBytes(idbytes);
	    StringBuilder sb= new StringBuilder(idbytes.length);
	    for (int i=0;i<idbytes.length;i++) {
  		sb.append(String.format("%02x", idbytes[i]));  
	    }
	    id=sb.toString();
	    peticion= peticion.replace("#API_KEY#", usuario).replace("#PASSWORD#", password).replace("#NONCE#", new String(Base64.encode(nonceBytes))).replace("#CREATED#", created).replace("#ID#", id);
	    SOAPHeader header= soap.getSOAPHeader();
	    if (header==null){
	    	soap.getSOAPPart().getEnvelope().addHeader();
	    }
	    Document doc = XMLDOMUtils.parseXml(peticion);
	    
	    Node wssecurity= header.getOwnerDocument().importNode(doc.getDocumentElement(), true);
	    header.appendChild(wssecurity);
	    
	}
	private void putUsernameToken(SOAPMessageContext context)
	{
		try
		{
			Preferencias pref= (Preferencias) context.get(Constantes.PREFERENCIAS);
			if (pref==null)
			{
				pref = new Preferencias();
			}
			
			Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			if (salida){
				String username= pref.getNotificaAPIKey();
				String password= username;
				/*Document doc= context.getMessage().getSOAPPart();
				WSSecSignature builder= new WSSecSignature();
				WSSecHeader header = new WSSecHeader();
				header.insertSecurityHeader(doc);
				WSSecUsernameToken usertoken = new WSSecUsernameToken();
				usertoken.setPasswordType(WSConstants.PASSWORD_DIGEST);
				usertoken.setUserInfo(username, password);
				usertoken.addCreated();
				usertoken.addNonce();
				usertoken.prepare(doc);
				
				builder.build(doc, null, header);
				*/
				
				/*UsernameToken userToken = new UsernameToken(true,doc);
				userToken.setName(username);
				userToken.setPassword(password);
				userToken.addNonce(doc);
				userToken.addCreated(true, doc);
				*/
				estableceUserNameToken(context.getMessage(),username, password);
			}
		}
		catch (PreferenciasException ex)
		{
			System.err.println ("Servicio de Gestión de notificaciones (sincronización): Error de preferencias en manejador SOAP ("+SoapClientHandler.class.getName()+":"+ex.getMessage());
			SOAPUtils.generateSOAPErrMessage(context.getMessage(), "Error en proceso de comunicación Gestión de Envíos de Notific@ <--> Servicio Remoto."	, "0002", "SOAP Handler", true);
		}/* catch (WSSecurityException e) {
			System.err.println ("Servicio de Gestión de notificaciones(sincronización): Error al insertar la seguridad de manejador SOAP ("+SoapClientHandler.class.getName()+":"+e.getMessage());
			SOAPUtils.generateSOAPErrMessage(context.getMessage(), "Error en proceso de comunicación Gestión de Envíos de Notific@ <--> Servicio Remoto."	, "0002", "SOAP Handler", true);
		}*/
		catch (Exception e) {
			System.err.println ("Servicio de Gestión de notificaciones(sincronización): Error al insertar la seguridad de manejador SOAP ("+SoapClientHandler.class.getName()+":"+e.getMessage());
			SOAPUtils.generateSOAPErrMessage(context.getMessage(), "Error en proceso de comunicación Gestión de Envíos de Notific@ <--> Servicio Remoto:"+e.getMessage()	, "0002", "SOAP Handler", true);
		}
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
		return false;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		putUsernameToken(context);
		return true;
	}
}
