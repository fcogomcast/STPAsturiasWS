package es.tributasenasturias.business;

import es.tributasenasturias.exception.PresentacionException;
import es.tributasenasturias.documentos.util.SHAUtils;
import es.tributasenasturias.modelo600utils.Preferencias;
import es.tributasenasturias.modelo600utils.log.ILoggable;
import es.tributasenasturias.modelo600utils.log.LogHelper;
import es.tributasenasturias.webservices.modelosPDF.clients.ModelosPDFPortBindingQSService;

public class ModelosPDF implements ILoggable{
	
	@SuppressWarnings("unused")
	private String codVerificacion;
	private String resultado; 
	private Preferencias pref = new Preferencias();
	private LogHelper log;
	protected ModelosPDF() throws Exception{
		pref.CargarPreferencias();
	}

	public String getCodigoVerificacion(String valor) throws Exception{
		String resultado = SHAUtils.hex_hmac_sha1("clave               ", valor);
		return resultado.substring(resultado.length() -16, resultado.length());
	}
	
	public boolean generarModelo600 (String xmlData) throws PresentacionException{

		try {
			// llamar al servicio PXEscritura para ejecutar el procedimiento almacenado de alta de expediente
		
			ModelosPDFPortBindingQSService modeloPDFWS = new ModelosPDFPortBindingQSService(); 
			//EscriturasAncertService escrituraWS = new EscriturasAncertService();
			es.tributasenasturias.webservices.modelosPDF.clients.ModelosPDF modelosPort;
			//EscriturasAncert escriturasPort;
			if (!pref.getEndpointModeloPDF().equals("")){
				log.debug ("=====Se utiliza el endpoint de la generacion del modelo pdf: " + pref.getEndpointModeloPDF());
				modelosPort = modeloPDFWS.getModelosPDFPortBindingQSPort();
				// enlazador de protocolo para el servicio.
				javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) modelosPort; 
				// Cambiamos el endpoint
				bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointModeloPDF()); 
			}else{
				log.debug ("=====Se utiliza el endpoint de generacion del modelo pdf por defecto: " + javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
				modelosPort = modeloPDFWS.getModelosPDFPortBindingQSPort();		
			}
	        
	        String respuesta = new String();
	        respuesta = modelosPort.createPDF600(xmlData);	        	
	        this.resultado = respuesta;
	        return true;
		
		} catch (Exception e) {
			throw new PresentacionException ("Excepción al generar modelo 600:" + e.getMessage(),e);
		}
	}
	
	public String getResultado () {
		return this.resultado;
	}
	
	@Override
	public void setLogger(LogHelper log){
		this.log = log;
	}

	@Override 
	public LogHelper getLogger(){
		return log;
	}
    

}
