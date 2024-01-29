package es.tributasenasturias.webservice.firma;
import javax.xml.ws.BindingProvider;
import es.tributasenasturias.firmadigital.client.WsFirmaDigital;
import es.tributasenasturias.firmadigital.client.FirmaDigital;
import es.tributasenasturias.pagopresentacionmodelo600utils.Preferencias;
import es.tributasenasturias.webservice.pagopresentacion.log.ILoggable;
import es.tributasenasturias.webservice.pagopresentacion.log.LogHelper;

/**
 * @author davidsa Firma respuesta Web Service Ancert
 */
public class FirmaHelper implements ILoggable{
	private Preferencias pref = new Preferencias();
	private LogHelper log;
	/* Constructor */
	protected FirmaHelper () {
		pref.CompruebaFicheroPreferencias();		
	}
	
	/**
	 * Genera un mensaje firmado a partir de un mensaje de entrada.
	 * 
	 * @param msg
	 * @return Cadena con mensaje firmado o null si no se ha podido generar.
	 */
	public String firmaMensaje(String msg) {
		String firmado = null;
		WsFirmaDigital srv = new WsFirmaDigital();					
		FirmaDigital srPort = srv.getServicesPort();
		try {			
			// cargamos los datos del almacen de un fichero xml preferencias
			pref.CargarPreferencias();														
			//Se modifica el endpoint
			String endpointFirma = pref.getEndpointFirma();			
			if (!endpointFirma.equals("")) {
				BindingProvider bpr = (BindingProvider) srPort;
				bpr.getRequestContext().put(
						BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
						endpointFirma);
			}
			
			// Recuperamos la clave y certificado de firma.
			// CRUBENCVS 47084. 20/02/2023 Versión de firma para 
			// indicar  algoritmo
		    //String msgFirmado = srPort.firmarAncert(msg, certificado, clave);
			String msgFirmado = srPort.firmarXMLAlgoritmo(msg, pref.getAliasCertificado(), pref.getIdNodoFirma(), pref.getNodoPadre(), pref.getNamespaceNodoPadre(), pref.getUriAlgoritmoFirma(), pref.getUriAlgoritmoDigest());
			//FIN CRUBENCVS 47084
		    if (srPort.validar(msgFirmado))
		    	log.info("Integridad de la firma de mensaje de salida correcta!!");
		    else
		    	log.error("Error al validar integridad de la firma del mensaje de salida.");
    			    			    
			firmado = msgFirmado;								
		} catch (Exception ex) {
			log.error("Error al firmar el mensaje de salida: "
					+ ex.getMessage());
			firmado = null;
		}	
		return firmado;
	}	
	@Override
	public void setLogger(LogHelper log)
	{
		this.log = log;
	}
	@Override 
	public LogHelper getLogger()
	{
		return log;
	}
}
