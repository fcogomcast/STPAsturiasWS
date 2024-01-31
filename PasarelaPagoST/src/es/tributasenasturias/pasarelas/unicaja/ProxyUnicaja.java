package es.tributasenasturias.pasarelas.unicaja;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.handler.Handler;

import es.types.unicaja.consulta.CONSULTACOBROSIN;
import es.types.unicaja.consulta.CONSULTACOBROSOUT;
import es.types.unicaja.consulta.ConsultaCobrosCECAPRIN100;
import es.types.unicaja.pago.PAGC3POCECAPRIN100;
import es.types.unicaja.pago.PAGC3POIN;
import es.types.unicaja.pago.PAGC3POOUT;





/**
 * Realiza la comunicación con Unicaja.
 * @author crubencvs
 *
 */
public final class ProxyUnicaja {

	private String endpoint;
	private String idSesion;
	public ProxyUnicaja (String endpoint, String idSesion)
	{
		this.endpoint=endpoint; 
		this.idSesion= idSesion;
	}
	
	@SuppressWarnings("unchecked")
	public final PAGC3POOUT realizarPagoOAnulacion(PAGC3POIN pago, es.types.unicaja.pago.Header cabecera)
	{
		es.types.unicaja.pago.InfoCajaTaxes taxesWS;
		PAGC3POCECAPRIN100 servPago = new PAGC3POCECAPRIN100();
		taxesWS = servPago.getSOAPEventSource();
		javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) taxesWS; // enlazador de protocolo para el servicio.
		if (this.endpoint!=null && !"".equals(this.endpoint))
		{
			bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,this.endpoint);
		}
		Binding bi = bpr.getBinding();
		List <Handler> handlerList = bi.getHandlerChain();
		if (handlerList == null)
		{
		   handlerList = new ArrayList<Handler>();
		}
		handlerList.add(new es.tributasenasturias.webservices.soap.LogMessageHandlerClient(this.idSesion));
		bi.setHandlerChain(handlerList);
		return taxesWS.tributoLocalTramitacion(pago, cabecera);
	}
	
	@SuppressWarnings("unchecked")
	public final CONSULTACOBROSOUT realizarConsulta (CONSULTACOBROSIN consulta, es.types.unicaja.consulta.Header cabecera)
	{
		ConsultaCobrosCECAPRIN100 servConsulta= new ConsultaCobrosCECAPRIN100();
		es.types.unicaja.consulta.InfoCajaTaxes paymentWS; 
		paymentWS=servConsulta.getSOAPEventSource();
		javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) paymentWS; // enlazador de protocolo para el servicio.
		if (this.endpoint!=null && !"".equals(this.endpoint))
		{
			bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,this.endpoint);
		}
		Binding bi = bpr.getBinding();
		List <Handler> handlerList = bi.getHandlerChain();
		if (handlerList == null)
		{
		   handlerList = new ArrayList<Handler>();
		}
		handlerList.add(new es.tributasenasturias.webservices.soap.LogMessageHandlerClient(this.idSesion));
		bi.setHandlerChain(handlerList);
		return paymentWS.consultaCobros(consulta, cabecera);
	}
}
