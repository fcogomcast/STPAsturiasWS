package es.stpa.smartmultas.soap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import es.stpa.smartmultas.Constantes;
import es.stpa.smartmultas.preferencias.Preferencias;
import es.stpa.smartmultas.preferencias.PreferenciasException;
import es.tributasenasturias.log.TributasLogger;


public class GestionUsernameToken implements SOAPHandler<SOAPMessageContext>{
	
	/**
	 * Gestiona la firma de mensaje de entrada al servicio, según WS-Security.
	 * @param context Contexto del mensaje SOAP.
	 */
	protected void gestionUsernameToken(SOAPMessageContext context)
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
				pref=new Preferencias();
			}
			if (logAplicacion==null)
			{
				logAplicacion= new TributasLogger(pref.getModoLog(),pref.getDirectorioRaizLog(),pref.getFicheroLogAplicacion(), idSesion);
			}
			Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			if (!salida.booleanValue()){
				//Extraigo el username y password y lo valido contra las preferencias.
				//No compruebo el Nonce, porque la clave viene en texto plano
				SOAPMessage msg = context.getMessage();
				SOAPHeader header= msg.getSOAPHeader();
				XPath xpath= XPathFactory.newInstance().newXPath();
		        String username= (String) xpath.evaluate("*[local-name()='Security']/*[local-name()='UsernameToken']/*[local-name()='Username']/text()", header, XPathConstants.STRING);
		        String password= (String )xpath.evaluate("*[local-name()='Security']/*[local-name()='UsernameToken']/*[local-name()='Password']/text()", header, XPathConstants.STRING);
		        if (username==null || password==null){
		        	logAplicacion.info("Error, usuario o clave vacíos");
		        	SOAPUtils.generateSOAPErrMessage(context.getMessage(), "Error en seguridad de mensaje.", "0002", "SOAP Handler",true);
		        } else {
		        	if (!username.equals(pref.getUsuarioLogin()) || !password.equals(pref.getPasswordLogin())){
		        		logAplicacion.info("Error, usuario o clave inválidos");
		        		SOAPUtils.generateSOAPErrMessage(context.getMessage(), "Error en seguridad de mensaje.", "0002", "SOAP Handler",true);
		        	}
		        }
			}
		}catch (PreferenciasException e)
		{
			//Si no hay preferencias, no hay log.
			System.err.println ("Manejador SOAP:[" + GestionUsernameToken.class.getName()+"].Error en preferencias:"+e.getMessage());
			SOAPUtils.generateSOAPErrMessage(context.getMessage(), "Error en seguridad de mensaje.", "0002", "SOAP Handler",true);
		}
		 catch (SOAPException e) {
			if (logAplicacion!=null) {logAplicacion.error ("Manejador SOAP:[" + GestionUsernameToken.class.getName()+"].Error:"+e.getMessage(),e);}
			SOAPUtils.generateSOAPErrMessage(context.getMessage(), "Error en seguridad SOAP.", "0002", "SOAP Handler", false);
		} catch (XPathExpressionException xpe) {
			if (logAplicacion!=null) {logAplicacion.error ("Manejador SOAP:[" + GestionUsernameToken.class.getName()+"].Error:"+xpe.getMessage(),xpe);}
			SOAPUtils.generateSOAPErrMessage(context.getMessage(), "Error en seguridad SOAP.", "0002", "SOAP Handler", false);
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
		gestionUsernameToken(context);
		return true;
	}
	

}
