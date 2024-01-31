package es.tributasenasturias.business;

import es.tributasenasturias.business.vo.PagoVO;
import es.tributasenasturias.modelo600utils.Preferencias;
import es.tributasenasturias.modelo600utils.log.ILoggable;
import es.tributasenasturias.modelo600utils.log.LogHelper;
import es.tributasenasturias.webservices.clients.pasarelaPago.PasarelaPagoST;
import es.tributasenasturias.webservices.clients.pasarelaPago.PasarelaPagoST_Service;
import es.tributasenasturias.webservices.clients.pasarelaPago.ResultadoPeticion;

/**
 * Implementa los métodos de pago. Acepta logs del llamador para indicar su salida. Si no se le pasa,
 * no se pueden realizar operaciones de log.
 * @author crubencvs
 *
 */
public class Pago implements ILoggable{
	
	private String resultado;
	private String error_resultado;
	private String cod_error;
	private Preferencias pref = new Preferencias();
	private PagoVO pagoVO = new PagoVO();
	private LogHelper log;
	
	protected Pago() {}
	
	public boolean pagarAutoliquidacion() {
		try {
			// llamar al servicio PXPasarelaPagoST
			pref.CargarPreferencias();
			PasarelaPagoST_Service pasarelaWS = new PasarelaPagoST_Service();
	        PasarelaPagoST pasarelaPort;
			if (!pref.getEndpointPago().equals(""))
			{
				log.debug("=====Se utiliza el endpoint de pasarela: " + pref.getEndpointPago());
				pasarelaPort = pasarelaWS.getPasarelaPagoSTPort();
				// enlazador de protocolo para el servicio.
				javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) pasarelaPort; 
				// Cambiamos el endpoint
				bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointPago()); 
			}
			else
			{
				log.debug("=====Se utiliza el endpoint de pasarela por defecto: " + javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
				pasarelaPort =pasarelaWS.getPasarelaPagoSTPort(); 				
			}
	        ResultadoPeticion respuesta = new ResultadoPeticion();
	        try {

	        	respuesta = pasarelaPort.peticion(this.getPagoVO().getOrigen(), 
	        			this.getPagoVO().getModalidad(), null, this.getPagoVO().getEmisora(), "600", 
	        			this.getPagoVO().getNifSp(), null, this.getPagoVO().getFechaDevengo(), this.getPagoVO().getDatoEspecifico(), 
	        			null, null, this.getPagoVO().getNumeroAutoliquidacion(), 
	        			null, this.getPagoVO().getImporte(), this.getPagoVO().getNumTarjeta(), this.getPagoVO().getFechaCaducidadTarjeta(), 
	        			this.getPagoVO().getCCC(), this.getPagoVO().getNifOperante(), null, null, null, null, null);

	        	log.debug("=====PAGO:Respuesta Peticion:"+respuesta.getRespuesta().getResultado());
	        	log.debug("=====PAGO:Código Retorno Peticion:"+respuesta.getRespuesta().getError());
	        	if (!respuesta.getRespuesta().getError().isEmpty()) {
	        		this.error_resultado = respuesta.getRespuesta().getError()+"--"+respuesta.getRespuesta().getResultado();
	        		this.cod_error = respuesta.getRespuesta().getError();	        		
	        	}
	        	
	        }catch (Exception ex) {
	        	this.error_resultado = "Error en el pago - generico: ".concat(ex.getMessage());
	        	this.cod_error = respuesta.getRespuesta().getError();
	        	log.error("Error en el pago - generico: ".concat(ex.getMessage()));
	        	log.trace(ex.getStackTrace());
	        }
	        
	        log.info("=====PAGO:resultado: ".concat(respuesta.getRespuesta().getResultado()));
	        this.resultado = respuesta.getRespuesta().getResultado();
	        
	        return true;

		} catch (Exception e) {
			this.error_resultado = "Excepcion generica en el pago:"+e.getMessage();
			this.cod_error = "9999";
			log.error("****Excepcion generica en el pago:"+e.getMessage());
			log.trace(e.getStackTrace());
			return false;
		}
	}
	
	public PagoVO getPagoVO() {
		return this.pagoVO;
	}
	
	public String getRespuestaPago () {
		return this.resultado;
	}
	
	public String getRespuestaErrorPago() {
		return this.error_resultado;
	}
	
	public String getCodError () {
		return this.cod_error;
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
