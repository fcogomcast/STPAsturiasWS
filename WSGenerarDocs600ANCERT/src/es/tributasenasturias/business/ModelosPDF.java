package es.tributasenasturias.business;

import es.tributasenasturias.GenerarDocs600ANCERTutils.Logger;
import es.tributasenasturias.GenerarDocs600ANCERTutils.Preferencias;
import es.tributasenasturias.documentos.util.SHAUtils;
import es.tributasenasturias.webservices.modelosPDF.clients.ModelosPDFPortBindingQSService;

public class ModelosPDF {
	
	private String codVerificacion;
	private String resultado; 
	private Preferencias pref = new Preferencias();
		
	public ModelosPDF() {
		try {
			pref.CargarPreferencias();
		} catch (Exception e) {
			Logger.error("Error al cargar preferencias al general el modelo 600. "+e.getMessage());
		}
	}
	
	
	
	public String getCodigoVerificacion(String valor) {
		try {
			String resultado = SHAUtils.hex_hmac_sha1("clave               ", valor);
			return resultado.substring(resultado.length() -16, resultado.length());
		} catch (Exception e) {
			System.out.println("Error al obtener el codigo de verificacion seguro: "+e.getMessage());
			return null;
		}
	
	}
	
	public boolean generarModelo600 (String xmlData) {

		try {
			// llamar al servicio PXEscritura para ejecutar el procedimiento almacenado de alta de expediente
		
			ModelosPDFPortBindingQSService modeloPDFWS = new ModelosPDFPortBindingQSService(); 
			//EscriturasAncertService escrituraWS = new EscriturasAncertService();
			es.tributasenasturias.webservices.modelosPDF.clients.ModelosPDF modelosPort;
			//EscriturasAncert escriturasPort;
			if (!pref.getEndpointModeloPDF().equals(""))
			{
				if (pref.getDebug().equals("1")) {
					Logger.debug ("Se utiliza el endpoint de la generacion del modelo pdf: " + pref.getEndpointModeloPDF());
				}
				modelosPort = modeloPDFWS.getModelosPDFPortBindingQSPort();
				// enlazador de protocolo para el servicio.
				javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) modelosPort; 
				// Cambiamos el endpoint
				bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointModeloPDF()); 
			}
			else
			{
				if (pref.getDebug().equals("1")) {
					Logger.debug ("Se utiliza el endpoint de generacion del modelo pdf por defecto: " + javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
				}
				modelosPort = modeloPDFWS.getModelosPDFPortBindingQSPort();		
			}
	        
	        String respuesta = new String();
	        try {
	        	respuesta = modelosPort.createPDF600(xmlData);	        	
	        }catch (Exception ex) {
	        	if (pref.getDebug().equals("1")) {
	        		Logger.info("MODELO PDF: ".concat(ex.getMessage()));
	        	}
	        }

	        this.resultado = respuesta;
	        return true;
		
		} catch (Exception e) {
			Logger.error("Excepcion generica: "+e.getMessage());
			return false;
		}
	}
	
	public String getResultado () {
		return this.resultado;
	}
	


}
