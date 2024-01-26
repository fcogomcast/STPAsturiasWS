package es.stpa.notifica.adviser.soap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;





import es.stpa.notifica.adviser.Constantes;
import es.stpa.notifica.adviser.preferencias.Preferencias;
import es.stpa.notifica.adviser.preferencias.PreferenciasException;
import es.stpa.notifica.adviser.wssecurity.WSSecurityException;
import es.stpa.notifica.adviser.wssecurity.WSSecurityFactory;
import es.tributasenasturias.log.ILog;
import es.tributasenasturias.log.LoggerFactory;
import es.tributasenasturias.xml.XMLDOMDocumentException;
import es.tributasenasturias.xml.XMLDOMUtils;


public class GestionFirmaWS implements SOAPHandler<SOAPMessageContext>{
	
	/**
	 * Marca error de seguridad en el contexto de mensaje, para que la clase de implementación
	 * pueda recogerlo y responder con un mensaje controlado.
	 * @param context
	 */
	public static void marcaErrorSeguridad(SOAPMessageContext context){
		context.put(Constantes.ERROR_SEGURIDAD, Boolean.TRUE);
		context.setScope(Constantes.ERROR_SEGURIDAD, Scope.APPLICATION);
	}
	/**
	 * Gestiona la firma de mensaje de entrada al servicio, según WS-Security.
	 * @param context Contexto del mensaje SOAP.
	 */
	private void firmaMensaje(SOAPMessageContext context)
	{
		ILog logAplicacion=null;
		ByteArrayInputStream bi=null;
		ByteArrayOutputStream bo=null;
		try
		{
			Preferencias pref = (Preferencias) context.get(Constantes.PREFERENCIAS);
			String idSesion = (String) context.get(Constantes.IDSESION);
			boolean debug= false;
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
				logAplicacion= LoggerFactory.getLogger(pref.getModoLog(),pref.getFicheroLogAplicacion(), idSesion);
			}
			
			debug= ("DEBUG".equalsIgnoreCase(pref.getModoLog()) || "ALL".equalsIgnoreCase(pref.getModoLog()))?true:false;
			
			//Comprobamos si firmamos o validamos.
			Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			if (salida.booleanValue())
			{
				//Firmamos.
				if ("S".equalsIgnoreCase(pref.getFirmarSalida()))
				{
					if (debug)
					{
						logAplicacion.debug("Manejador SOAP:[" + GestionFirmaWS.class.getName()+"].Se firma.");
					}
					//Recuperamos el texto de mensaje.
					bo=new ByteArrayOutputStream();
					context.getMessage().writeTo(bo);
					//Recuperamos qué mensaje firmamos, el de consulta o pago.
					String xmlFirmado = XMLDOMUtils.getPrettyXMLText(context.getMessage().getSOAPPart());
					xmlFirmado=WSSecurityFactory.newConstructorResultado(pref,new SoapClientHandler(idSesion)).firmaMensaje(xmlFirmado);
					if (debug)
					{
						logAplicacion.debug("Manejador SOAP:[" + GestionFirmaWS.class.getName()+"].Mensaje firmado:"+xmlFirmado);
					}
					//bi = new ByteArrayInputStream(xmlFirmado.getBytes("ISO-8859-1"));
					bi = new ByteArrayInputStream(xmlFirmado.getBytes());
					context.getMessage().getSOAPPart().setContent(new StreamSource(bi));
				}
			}
			else
			{
				//Validamos.
				if ("S".equalsIgnoreCase(pref.getValidarFirma()))
				{
					if (debug)
					{
						logAplicacion.debug("Manejador SOAP:[" + GestionFirmaWS.class.getName()+"].Validamos la firma WSSecurity.");
					}
					bo=new ByteArrayOutputStream();
					context.getMessage().writeTo(bo);
					if (debug)
					{
						logAplicacion.debug("Manejador SOAP:[" + GestionFirmaWS.class.getName()+"].Mensaje para validar firma WSSecurity:" + new String(bo.toByteArray()));
					}
					boolean firmaCorrecta = WSSecurityFactory.newConstructorResultado(pref,new SoapClientHandler(idSesion)).validaFirmaSinCertificado(new String(bo.toByteArray()));
					if (firmaCorrecta)
					{
						logAplicacion.info("Manejador SOAP:[" + GestionFirmaWS.class.getName()+"].Firma WSSecurity validada");
					}
					else
					{
						logAplicacion.error("Manejador SOAP:[" + GestionFirmaWS.class.getName()+"].Firma de mensaje WSSecurity no válida.");
						marcaErrorSeguridad(context);
						//Este valor de ERROR_SEGURIDAD lo procesaremos en la clase de implemetación del servicio, para devolver
						//un mensaje controlado
					}
				}
			}
		}catch (PreferenciasException e)
		{
			if (logAplicacion!=null) {logAplicacion.error ("Manejador SOAP:[" + GestionFirmaWS.class.getName()+"].Error en preferencias:"+e.getMessage(),e);}
			marcaErrorSeguridad(context);
		}
		 catch (SOAPException e) {
			if (logAplicacion!=null) {logAplicacion.error ("Manejador SOAP:[" + GestionFirmaWS.class.getName()+"].Error:"+e.getMessage(),e);}
			marcaErrorSeguridad(context);
		} catch (IOException e) {
			if (logAplicacion!=null) {logAplicacion.error ("Manejador SOAP:[" + GestionFirmaWS.class.getName()+"].Error:"+e.getMessage(),e);}
			marcaErrorSeguridad(context);
		} 
		catch (WSSecurityException e)
		{
			if (logAplicacion!=null) {logAplicacion.error ("Manejador SOAP:[" + GestionFirmaWS.class.getName()+"].Error:"+e.getMessage(),e);}
			marcaErrorSeguridad(context);
		} catch (XMLDOMDocumentException e)
		{
			if (logAplicacion!=null) {logAplicacion.error ("Manejador SOAP:[" + GestionFirmaWS.class.getName()+"].Error:"+e.getMessage(),e);}
			marcaErrorSeguridad(context);
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
		firmaMensaje(context);
		return true;
	}
	

}
