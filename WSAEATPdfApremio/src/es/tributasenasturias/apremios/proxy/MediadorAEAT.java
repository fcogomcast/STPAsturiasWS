package es.tributasenasturias.apremios.proxy;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import https.www2_agenciatributaria_gob_es.static_files.common.internet.dep.aduanas.es.aeat.dit.adu.srel.ws.coe.coepdfv1.CoePdfV1;
import https.www2_agenciatributaria_gob_es.static_files.common.internet.dep.aduanas.es.aeat.dit.adu.srel.ws.coe.coepdfv1.CoePdfV1Service;
import https.www2_agenciatributaria_gob_es.static_files.common.internet.dep.aduanas.es.aeat.dit.adu.srel.ws.coe.coepdfv1ent.CoePdfV1Ent;
import https.www2_agenciatributaria_gob_es.static_files.common.internet.dep.aduanas.es.aeat.dit.adu.srel.ws.coe.coepdfv1sal.CoePdfV1Sal;
import es.tributasenasturias.apremios.ResultadoAEATType;
import es.tributasenasturias.apremios.preferencias.Preferencias;
import es.tributasenasturias.apremios.soap.SoapClientHandler;
import es.tributasenasturias.apremios.util.Base64;
import es.tributasenasturias.apremios.util.Constantes;

/**
 * Clase que actúa como mediador con el servicio de la AEAT
 * @author crubencvs
 *
 */
public class MediadorAEAT {

	private Preferencias pref;
	private String idLlamada;

	
	/**
	 * Establece el manejador para un port
	 * @param manejador Manejador de mensajes
	 * @param port Port
	 */
	@SuppressWarnings("unchecked")
	private void addHandler(SOAPHandler<SOAPMessageContext> manejador, CoePdfV1 port)
	{
		javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) port; 
		if (manejador!=null)
		{
			Binding bi = bpr.getBinding();
			List<Handler> handlerList = bi.getHandlerChain();
			if (handlerList == null)
			{
			   handlerList = new ArrayList<Handler>();
			}
			handlerList.add(manejador);
			bi.setHandlerChain(handlerList);
		}	
	}
	/**
	 * Establece el endpoint del servicio
	 * @param port
	 * @param endpoint
	 */
	private void establecerEndpoint (CoePdfV1 port, String endpoint) {
		javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) port;
		bpr.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
	}
	public MediadorAEAT(Preferencias pref, String idLlamada) {
		this.pref= pref;
		this.idLlamada= idLlamada;
	}
	/**
	 * Envía un pdf de apremio a la AEAT e informa del resultado
	 * @param claveLiquidacion Clave de liquidación cuyo documento de apremio se va a enviar
	 * @param nifDeudor Nif del deudor
	 * @param fichero Datos del pdf de apremio
	 * @return {@link ResultadoEnvioPdf} en donde se informa del resultado de la comunicación
	 */
	public ResultadoEnvioPdf enviarPdf (String claveLiquidacion, String nifDeudor, byte[] fichero) {
		CoePdfV1Service srv = new CoePdfV1Service();
		CoePdfV1 port = srv.getCoePdfV1();
		ResultadoEnvioPdf.Builder builder= new ResultadoEnvioPdf.Builder();
		establecerEndpoint(port, pref.getEndpointAEATEnvioPdf());
		addHandler(new SoapClientHandler(this.idLlamada),port);
		
		CoePdfV1Ent mensaje = new CoePdfV1Ent();
		mensaje.setAccion("A"); //Alta
		mensaje.setClaveLiquidacion(claveLiquidacion);
		mensaje.setModoEjecucion(this.pref.getModoEnvioAEAT());
		mensaje.setNifDeudor(nifDeudor);
		mensaje.setPdfBase64(String.valueOf(Base64.encode(fichero)));
		try {
			CoePdfV1Sal salida= port.coePdfV1(mensaje);
			ResultadoAEATType rAEAT= new ResultadoAEATType();
			rAEAT.setRespuesta(salida.getRespuesta());
			rAEAT.setTextoError(salida.getTextoError());
			rAEAT.setCSVDeclaracion(salida.getCsvDeclaracion());
			rAEAT.setTipoHash(salida.getTipoHash());
			rAEAT.setHashPdf(salida.getHashPdf());
			if ("S".equalsIgnoreCase(rAEAT.getRespuesta())) {
				builder.setEsError(false);
			} else {
				builder.setEsError(true).setCodError(Constantes.ERROR_AEAT);
			}
			builder.setResultadoAEAT(rAEAT);
			
		} catch (Exception e) {
			builder.setEsError(true).setCodError(Constantes.ERROR_AEAT);
		}
		return builder.build();
	}
}
