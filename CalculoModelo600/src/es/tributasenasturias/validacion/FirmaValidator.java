/**
 * 
 */
package es.tributasenasturias.validacion;

import java.io.ByteArrayOutputStream;

import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;

import es.tributasenasturias.firmadigital.client.WsFirmaDigital;
import es.tributasenasturias.firmadigital.client.FirmaDigital;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;

/**
 * @author crubencvs
 *
 */
public class FirmaValidator implements IValidator<SOAPMessage> {

	private Preferencias pr = new Preferencias ();
	
	ResultadoValidacion resultado;
	public FirmaValidator()
	{
		resultado=new ResultadoValidacion();
	}
	/* (non-Javadoc)
	 * @see es.tributasenasturias.validacion.IValidator#getResultado()
	 */
	@Override
	public IResultadoValidacion getResultado() {
		return resultado;
	}

	/* (non-Javadoc)
	 * @see es.tributasenasturias.validacion.IValidator#isValid(java.lang.Object)
	 */
	/**
	 * Indica si la firma del mensaje es válida
	 * @param contextoSOAP Contexto del mensaje.
	 * @return
	 */
	@Override
	public boolean isValid(SOAPMessage msg) {		
		WsFirmaDigital srv = new WsFirmaDigital();					
		FirmaDigital srPort = srv.getServicesPort();
		boolean valido=false;
					
		try
		{		
			//Se modifica el endpoint
			try {
				pr.CargarPreferencias();
			} catch (Exception e) {
				Logger
						.error("Error al cargar preferencias al integrar en tributas. "
								+ e.getMessage());
			}
			
			String endpointFirma = pr.getEndpointFirma();
			if (!endpointFirma.equals(""))
			{
				BindingProvider bpr = (BindingProvider) srPort;
				bpr.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,endpointFirma);
			}
			ByteArrayOutputStream msgTextBin= new ByteArrayOutputStream();
			try
			{
				msg.writeTo(msgTextBin);
				//Validar
				String msgText = new String (msgTextBin.toByteArray());
				valido = srPort.validar(msgText);
			}
			catch (Exception ex)
			{
				resultado.addMessage("Error al validar la firma");
				Logger.error ("Error al validar la firma:" + ex.getMessage());
				
				valido=false;
				
			}
		}
		catch (Exception ex)
		{
			resultado.addMessage("Error al recuperar la dirección del servicio de firma");
			Logger.error ("Error al recuperar el endpoint del servicio de firma:" + ex.getMessage());
			Logger.trace(ex.getStackTrace());
			valido=false;
		}
		return valido;
	}
	
}
