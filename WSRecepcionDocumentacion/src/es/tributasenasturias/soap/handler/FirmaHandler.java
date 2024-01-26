/**
 * 
 */
package es.tributasenasturias.soap.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;

import javax.xml.soap.SOAPException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import es.tributasenasturias.excepciones.FirmaException;
import es.tributasenasturias.excepciones.PreferenciasException;
import es.tributasenasturias.excepciones.RecepcionDocumentosException;
import es.tributasenasturias.log.Logger;
import es.tributasenasturias.seguridad.CertificadoValidator;
import es.tributasenasturias.seguridad.FirmaFactory;
import es.tributasenasturias.seguridad.FirmaHelper;
import es.tributasenasturias.servicios.RecepcionDocumentos.preferencias.Preferencias;
import es.tributasenasturias.utils.GeneradorIdSesion;
import es.tributasenasturias.utils.GestorIdLlamada;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.utils.Utils;


/**
 * @author davidsa
 */
public class FirmaHandler implements SOAPHandler<SOAPMessageContext> {

	
	@Override
	public void close(MessageContext context) {}

	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) throws SOAPFaultException {
			Boolean salida = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			Preferencias pref;
			Logger log = new Logger(GestorIdLlamada.getIdLlamada());
			try {
			pref = Preferencias.getPreferencias();
			//-- FIRMAR SALIDA DEL SERVICIO --
			if (salida.booleanValue()) {
				if ("S".equalsIgnoreCase(pref.getFirmarSalida()))
				{
					FirmaFactory frFactory= new FirmaFactory();
					FirmaHelper firmaDigital = frFactory.newFirmaHelper();
					firmaDigital.preparaMensaje(context.getMessage());
					ByteArrayOutputStream ba=new ByteArrayOutputStream();
					context.getMessage().writeTo(ba);
					String xml=new String(ba.toByteArray(),Charset.forName("UTF-8"));
	
					String ns = "http://servicios.tributasenasturias.es/Terceros/RecepcionDocumentacion";
					String respuesta = firmaDigital.firmaMensaje(xml,ns);
					if (respuesta!=null)
					{	
						ByteArrayInputStream bas = new ByteArrayInputStream(respuesta.getBytes());
						StreamSource strs = new StreamSource(bas);
						context.getMessage().getSOAPPart().setContent(strs);
					}
					else
					{
						log.error("Error: el mensaje de salida no ha podido ser firmado correctamente");
						Utils.generateSOAPErrMessage(context.getMessage(), "F002","F002",Mensajes.MSG_ERROR_F002);
					}
				}
			}
			else
			{
				//Generar Id.
				GestorIdLlamada.asociarIdLlamada(GeneradorIdSesion.generaIdSesion());
				//Validación.
				//Validamos la entrada firmada
	    		 ByteArrayOutputStream bo=new ByteArrayOutputStream();
				 context.getMessage().writeTo(bo);
				 String xmlRecibido = new String(bo.toByteArray());
				 FirmaFactory frFactory= new FirmaFactory();
				 FirmaHelper firmaDigital = frFactory.newFirmaHelper();
				 if ("S".equals(pref.getValidarFirma()) && !firmaDigital.esValido(xmlRecibido))
				 {
					 log.error("Error: El mensaje recibido no tiene una firma de xml válida.");
					 Utils.generateSOAPErrMessage(context.getMessage(), "F002","F002",Mensajes.MSG_ERROR_F002);
				 }
				 else
				 {
					 //Validamos el certificado.
					 if ("S".equals(pref.getValidarCertificado()))
					 {
						 CertificadoValidator cert = new CertificadoValidator();
						 if (!cert.isValid(xmlRecibido))
						 {
							 log.error("Error: el mensaje recibido no pasa la validación de certificado, o de permisos de ejecución.");
							 Utils.generateSOAPErrMessage(context.getMessage(), "F002","F002",Mensajes.MSG_ERROR_F002);
						 }
					 }
				 }
			}
			} catch (PreferenciasException e) {
				log.error("Error: problema con las preferencias:"+ e.getMessage());
				Utils.generateSOAPErrMessage (context.getMessage(),"F002","F002",Mensajes.MSG_ERROR_F002);
			} catch (RecepcionDocumentosException e) {
				log.error("Error:"+ e.getMessage());
				Utils.generateSOAPErrMessage (context.getMessage(),"F002","F002",Mensajes.MSG_ERROR_F002);
			} catch (FirmaException e) {
				log.error("Error: problema con la firma:"+ e.getMessage());
				Utils.generateSOAPErrMessage (context.getMessage(),"F002","F002",Mensajes.MSG_ERROR_F002);
			} catch (SOAPException e) {
				log.error("Error SOAP:"+ e.getMessage());
				Utils.generateSOAPErrMessage (context.getMessage(),"F002","F002",Mensajes.MSG_ERROR_F002);
			} catch (IOException e) {
				log.error("Error de E/S:"+ e.getMessage());
				Utils.generateSOAPErrMessage (context.getMessage(),"F002","F002",Mensajes.MSG_ERROR_F002);
			}
			finally
			{
				salida = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
				if (salida.booleanValue())
				{
					GestorIdLlamada.desasociarIdLlamada();
				}
			}
		return true;
	}
}