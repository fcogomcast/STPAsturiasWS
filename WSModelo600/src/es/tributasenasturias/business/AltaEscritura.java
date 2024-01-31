package es.tributasenasturias.business;

import es.tributasenasturias.exception.PresentacionException;
import es.tributasenasturias.modelo600utils.Preferencias;
import es.tributasenasturias.modelo600utils.log.ILoggable;
import es.tributasenasturias.modelo600utils.log.LogHelper;
import es.tributasenasturias.webservices.escrituras.client.EscriturasAncert;
import es.tributasenasturias.webservices.escrituras.client.EscriturasAncertService;

public class AltaEscritura implements ILoggable{
	
	private String resultado; 
	private Preferencias pref = new Preferencias();
	
	//Log
	private LogHelper log;
	protected AltaEscritura() throws Exception{
		pref.CargarPreferencias();
	};
	public static enum EstadosAltaEscritura {CORRECTA, YA_EXISTE, ERROR_TECNICO, NO_NECESARIA_ALTA};
	
	public EstadosAltaEscritura Inserta (String codNotario,String codNotaria,String numProtocolo,String protocoloBis,String fechaDevengo,String firmaEscritura,String docEscritura) throws PresentacionException{

		try {
			// llamar al servicio PXEscritura para ejecutar el procedimiento almacenado de alta de expediente			
			EscriturasAncertService escrituraWS = new EscriturasAncertService();			
			EscriturasAncert escriturasPort;
												
			escriturasPort = escrituraWS.getEscriturasAncertPort();
			// enlazador de protocolo para el servicio.
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) escriturasPort;
			// Cambiamos el endpoint
			bpr.getRequestContext().put(
					javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
					pref.getEndpointAltaEscritura());
									
			String respuesta = new String();
        	respuesta = escriturasPort.setEscrituraOrigen(codNotario, codNotaria, numProtocolo, protocoloBis, fechaDevengo, firmaEscritura, docEscritura,"G");	        		        	
	        this.resultado = respuesta;
	        if ("OK".equals(respuesta))
	        {
	        	return EstadosAltaEscritura.CORRECTA;
	        }
	        if ("OKYN".equals(respuesta) || "OKYG".equals(respuesta))
	        {
	        	return EstadosAltaEscritura.YA_EXISTE;
	        }
	        return EstadosAltaEscritura.ERROR_TECNICO;
		} catch (Exception e) {
			throw new PresentacionException ("Excepción durante la inserción de la escritura:" + e.getMessage(),e);
		}
	}
	
	public String getResultado () {
		return this.resultado;
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
