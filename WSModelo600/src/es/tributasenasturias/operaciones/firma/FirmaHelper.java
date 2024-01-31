package es.tributasenasturias.operaciones.firma;

import javax.xml.ws.BindingProvider;
import es.tributasenasturias.firmadigital.client.WsFirmaDigital;
import es.tributasenasturias.firmadigital.client.FirmaDigital;
import es.tributasenasturias.modelo600utils.Preferencias;
import es.tributasenasturias.modelo600utils.log.ILoggable;
import es.tributasenasturias.modelo600utils.log.LogHelper;

/**
 * @author davidsa Firma respuesta Web Service Ancert
 */
public class FirmaHelper implements ILoggable{
	private final static String nsCalculo="http://servicios.tributasenasturias.es/Calculo600Generico";
	private final static String nsPago="http://servicios.tributasenasturias.es/Pago600Generico";
	private Preferencias pref = new Preferencias();
	private LogHelper log;

	protected FirmaHelper () {
		pref.CompruebaFicheroPreferencias();		
	}
	
	/**
	 * Genera un mensaje firmado a partir de un mensaje de entrada.
	 * 
	 * @param msg
	 * @return Cadena con mensaje firmado o null si no se ha podido generar.
	 */
	public String firmaMensaje(String msg,String ns) {
		String firmado = null;
		WsFirmaDigital srv = new WsFirmaDigital();					
		FirmaDigital srPort = srv.getServicesPort();
		try {			
			//Cargamos los datos del almacen de un fichero xml preferencias
			pref.CargarPreferencias();														
			//Se modifica el endpoint
			String endpointFirma = pref.getEndpointFirma();			
			if (!endpointFirma.equals("")) {
				BindingProvider bpr = (BindingProvider) srPort;
				bpr.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,endpointFirma);
			}

			// Recuperamos la clave y certificado de firma.
			String certificado = pref.getCertificadoFirma();
		    String msgFirmado = srPort.firmarXML(msg, certificado, "declaracion", "remesa", ns);
		    if (srPort.validar(msgFirmado))
		    	log.info("Integridad de la firma de mensaje de salida correcta!!");
		    else
		    	log.error("Error al validar integridad de la firma del mensaje de salida.");
    			    			    
			firmado = msgFirmado;								
		} catch (Exception ex) {
			log.error("Error al firmar el mensaje de salida: "+ ex.getMessage());
			firmado = null;
		}	
		return firmado;
	}

	@Override
	public void setLogger(LogHelper log){
		this.log = log;
	}

	@Override 
	public LogHelper getLogger(){
		return log;
	}

	public static String getNsCalculo() {
		return nsCalculo;
	}

	public static String getNsPago() {
		return nsPago;
	}

	
}