/**
 * 
 */
package es.tributasenasturias.webservices.Certificados.validacion;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import es.tributasenasturias.firma.FirmaDigital;
import es.tributasenasturias.firma.WsFirmaDigital;
import es.tributasenasturias.webservices.Certificados.Exceptions.PreferenciasException;
import es.tributasenasturias.webservices.Certificados.General.IResultado;
import es.tributasenasturias.webservices.Certificados.General.InfoResultado;
import es.tributasenasturias.webservices.Certificados.soap.handler.ClientLogHandler;
import es.tributasenasturias.webservices.Certificados.utils.Preferencias.Preferencias;

/**
 * @author crubencvs
 *
 */
public class FirmaValidator implements IValidator<SOAPMessage> {

	InfoResultado resultado;
	public FirmaValidator()
	{
		resultado=new InfoResultado();
	}
	/* (non-Javadoc)
	 * @see es.tributasenasturias.validacion.IValidator#getResultado()
	 */
	@Override
	public IResultado getResultado() {
		return resultado;
	}

	/* (non-Javadoc)
	 * @see es.tributasenasturias.validacion.IValidator#isValid(java.lang.Object)
	 */
	/**
	 * Indica si la firma del mensaje es v�lida
	 * @param contextoSOAP Contexto del mensaje.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean isValid(SOAPMessage msg) {
		WsFirmaDigital srv = new WsFirmaDigital();
		FirmaDigital srPort = srv.getServicesPort();
		boolean valido=false;
		try
		{
			//Se modifica el endpoint
			Preferencias pr = Preferencias.getPreferencias();
			String endpointFirma = pr.getEndPointFirma();
			if (!endpointFirma.equals(""))
			{
				BindingProvider bpr = (BindingProvider) srPort;
				bpr.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,endpointFirma);
				List<Handler> handlers= bpr.getBinding().getHandlerChain();
				if (handlers==null)
				{
					handlers=new ArrayList<Handler>();
				}
				handlers.add(new ClientLogHandler());
				bpr.getBinding().setHandlerChain(handlers);
			}
			ByteArrayOutputStream msgTextBin= new ByteArrayOutputStream();
			try
			{
				msg.writeTo(msgTextBin);
				//Validar
				String msgText = new String (msgTextBin.toByteArray());
				valido = srPort.validar(msgText);
				if (!valido)
				{
					resultado.addMessage("El servicio de validaci�n indica que esa firma no es v�lida.");
				}
			}
			catch (Exception ex)
			{
				resultado.addMessage("Error al validar la firma:" + ex.getMessage());
				resultado.addStackTrace(ex.getStackTrace());
				valido=false;
				
			}
		}
		catch (PreferenciasException ex)
		{
			resultado.addMessage("Error al recuperar la direcci�n del servicio de firma:" + ex.getMessage());
			resultado.addStackTrace(ex.getStackTrace());
			valido=false;
		}
		return valido;
	}
	
}
