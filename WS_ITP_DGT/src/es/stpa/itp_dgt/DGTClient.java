package es.stpa.itp_dgt;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import es.stpa.itp_dgt.preferencias.Preferencias;
import es.stpa.itp_dgt.preferencias.PreferenciasException;
import es.stpa.itp_dgt.soap.Seguridad;
import es.stpa.itp_dgt.soap.SoapClientHandler;
import es.trafico.servicios.vehiculos.accesosexternos.webservices.EnvioDTO;
import es.trafico.servicios.vehiculos.accesosexternos.webservices.ResultadoDTO;
import es.trafico.servicios.vehiculos.idex.EnviarDatosWS;
import es.trafico.servicios.vehiculos.idex.EnviarDatosWSService;
import es.tributasenasturias.utils.log.Logger;




public class DGTClient {
	private Preferencias pref;
	private EnviarDatosWSService service;
	private EnviarDatosWS port;
	private Logger log;
	private String idLlamada;
	
	public DGTClient(Preferencias pref, Logger log, String idLlamada) {
		super();
		this.pref = pref;
		this.log = log;
		this.idLlamada = idLlamada;
		this.service=new EnviarDatosWSService();
		this.port=service.getEnviarDatosWSPort();
	}

	/**
	 * Asigna los manejadores de salida para el port 
	 * @param port ServicioNotificaciones mediante el que se pueden utilizar las operaciones del servicio remoto
	 * @param contexto {@link ContextoLlamada} con los datos comunes de esta llamada al servicio
	 */
	@SuppressWarnings("unchecked")
	private void asignaManejadores(EnviarDatosWS port) throws PreferenciasException
	{
		
		
		BindingProvider bpr = (BindingProvider)port;
		bpr.getRequestContext().put(
				BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				pref.getEndpointDGT());
		List<Handler> handler=bpr.getBinding().getHandlerChain();
		if (handler==null)
		{
			handler= new ArrayList<Handler>();
		}
		handler.add(new Seguridad(pref, log, idLlamada));
		handler.add(new SoapClientHandler(pref, idLlamada));
		bpr.getBinding().setHandlerChain(handler);
	}
	
	
	public ResultadoDTO enviarFichero(String fichero) throws PreferenciasException
	{
		EnvioDTO envio=new EnvioDTO();
		envio.setFichero(fichero);
		envio.setTipo(pref.getFIS());
		asignaManejadores(port);
		return port.enviarFichero(envio);
	}
}
