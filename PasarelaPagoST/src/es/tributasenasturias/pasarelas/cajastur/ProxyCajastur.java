package es.tributasenasturias.pasarelas.cajastur;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.handler.Handler;

import com.tibco.schemas.infocajacommon.sharedresources.schemas.service.schema.Header;

import es.infocaja.schemas.consulta_cobros.CONSULTACOBROSIN;
import es.infocaja.schemas.consulta_cobros.CONSULTACOBROSOUT;
import es.infocaja.schemas.pag_c3po.PAGC3POIN;
import es.infocaja.schemas.pag_c3po.PAGC3POOUT;
import es.infocaja.tibco.soa.taxes.paymentqueryws.consultacobrosimpl.acceca__servicios.consultacobrosceca__1_0.ConsultaCobrosCECAPRIN100;
import es.infocaja.tibco.soa.taxesws.tributolocaltramitacionimpl.acceca__servicios.pag__c3po__ceca__1_0.SOAPEventSourceBindingQSService;


/**
 * Realiza la comunicación con Liberbank.
 * @author crubencvs
 *
 */
public final class ProxyCajastur {

	private String endpoint;
	private String idSesion;
	public ProxyCajastur (String endpoint, String idSesion)
	{
		this.endpoint=endpoint; 
		this.idSesion= idSesion;
	}
	
	@SuppressWarnings("unchecked")
	public final PAGC3POOUT realizarPagoOAnulacion(PAGC3POIN pago, Header cabecera)
	{
		es.infocaja.tibco.soa.taxesws.tributolocaltramitacionimpl.acceca__servicios.pag__c3po__ceca__1_0.InfoCajaTaxes taxesWS;
		SOAPEventSourceBindingQSService servPago=new SOAPEventSourceBindingQSService();
		taxesWS = servPago.getSOAPEventSourceBindingQSPort();
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
	public final CONSULTACOBROSOUT realizarConsulta (CONSULTACOBROSIN consulta, Header cabecera)
	{
		ConsultaCobrosCECAPRIN100 servConsulta= new ConsultaCobrosCECAPRIN100();
		es.infocaja.tibco.soa.taxes.paymentqueryws.consultacobrosimpl.acceca__servicios.consultacobrosceca__1_0.InfoCajaTaxes paymentWS; 
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
