package es.tributasenasturias.pasarelas.cajarural;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Binding;

import javax.xml.ws.handler.Handler;

import com.ruralserviciosinformaticos.empresa.se_tributosasturiasanulacion.ANULACIONIN;
import com.ruralserviciosinformaticos.empresa.se_tributosasturiasanulacion.ANULACIONOUT;
import com.ruralserviciosinformaticos.empresa.se_tributosasturiasanulacion.SETributosAsturiasAnulacionBinderQSService;
import com.ruralserviciosinformaticos.empresa.se_tributosasturiasanulacion.SETributosAsturiasAnulacionPortType;
import com.ruralserviciosinformaticos.empresa.se_tributosasturiasconsulta.CONSULTACOBROSIN;
import com.ruralserviciosinformaticos.empresa.se_tributosasturiasconsulta.CONSULTACOBROSOUT;
import com.ruralserviciosinformaticos.empresa.se_tributosasturiasconsulta.SETributosAsturiasConsultaBinderQSService;
import com.ruralserviciosinformaticos.empresa.se_tributosasturiasconsulta.SETributosAsturiasConsultaPortType;
import com.ruralserviciosinformaticos.empresa.se_tributosasturiaspago.PAGOIN;
import com.ruralserviciosinformaticos.empresa.se_tributosasturiaspago.PAGOOUT;
import com.ruralserviciosinformaticos.empresa.se_tributosasturiaspago.SETributosAsturiasPagoBinderQSService;
import com.ruralserviciosinformaticos.empresa.se_tributosasturiaspago.SETributosAsturiasPagoPortType;


import es.tributasenasturias.pasarelas.PreferenciasPasarela;
import es.tributasenasturias.utils.Logger;



/**
 * Realiza la comunicación con Caja Rural.
 * @author crubencvs
 *
 */
public final class ProxyCajaRural{


	private PreferenciasCajaRural prefCajaRural;
	private String idSesion;
	private String endpoint;
	public ProxyCajaRural (String endpoint, PreferenciasPasarela pref, String idSesion, Logger log)
	{
		this.endpoint=endpoint;
		this.prefCajaRural=(PreferenciasCajaRural)pref;
		this.idSesion=idSesion;
	}

	/**
	 * Realización de la operación de pago contra el Proxy que comunica con la entidad externa.
	 * La entidad remota espera que en el mensaje viaje también la firma, pero no se incluye en este punto,
	 * sino que se interceptará el mensaje de salida y se firmará.
	 * @param pago Datos del pago, según lo que espera la entidad remota.
	 * @return Objeto con el resultado de pago.
	 */
	@SuppressWarnings("unchecked")
	public final PAGOOUT realizarPago(PAGOIN pago)
	{
		SETributosAsturiasPagoPortType port;
		SETributosAsturiasPagoBinderQSService servPago=new SETributosAsturiasPagoBinderQSService();
		port = servPago.getSETributosAsturiasPagoBinderQSPort();
		javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) port; // enlazador de protocolo para el servicio.
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
		if ("S".equals(prefCajaRural.getValidarFirma()))
		{
			handlerList.add(new es.tributasenasturias.webservices.soap.GestionFirmaEntidadRemotaHandler(prefCajaRural.getIdNodoFirmar(),prefCajaRural.getNodoContenedorFirma(),prefCajaRural.getNsNodoContenedorFirma(),prefCajaRural.getValidaCertificado(),prefCajaRural.getAliasServicio(), this.idSesion));
		}
		handlerList.add(new es.tributasenasturias.webservices.soap.LogMessageHandlerClient(idSesion));
		handlerList.add(new es.tributasenasturias.webservices.soap.PreprocesoCajaRural());
		bi.setHandlerChain(handlerList);
		return port.pago(pago);

	}
	
	/**
	 * Realización de la operación de anulación contra el Proxy que comunica con el entidad externa.
	 * La entidad remota espera que en el mensaje viaje también la firma, pero no se incluye en este punto,
	 * sino que se interceptará el mensaje de salida y se firmará.
	 * @param anulacion Datos de la anulación, según lo que espera la entidad remota.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final ANULACIONOUT realizarAnulacion(ANULACIONIN anulacion)
	{
		SETributosAsturiasAnulacionPortType port;
		SETributosAsturiasAnulacionBinderQSService servPago=new SETributosAsturiasAnulacionBinderQSService();
		port = servPago.getSETributosAsturiasAnulacionBinderQSPort();
		javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) port; // enlazador de protocolo para el servicio.
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
		if ("S".equals(prefCajaRural.getValidarFirma()))
		{
			handlerList.add(new es.tributasenasturias.webservices.soap.GestionFirmaEntidadRemotaHandler(prefCajaRural.getIdNodoFirmar(),prefCajaRural.getNodoContenedorFirma(),prefCajaRural.getNsNodoContenedorFirma(),prefCajaRural.getValidaCertificado(),prefCajaRural.getAliasServicio(), this.idSesion));
		}
		handlerList.add(new es.tributasenasturias.webservices.soap.LogMessageHandlerClient(idSesion));
		handlerList.add(new es.tributasenasturias.webservices.soap.PreprocesoCajaRural());
		bi.setHandlerChain(handlerList);
		return port.anulacion(anulacion);
	}
	
	/**
	 * Realización de la operación de pago contra el Proxy que comunica con la entidad remota.
	 * La entidad remota espera que en el mensaje viaje también la firma, pero no se incluye en este punto,
	 * sino que se interceptará el mensaje de salida y se firmará.
	 * @param consulta Datos de la consulta tal cual los espera la entidad remota.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final CONSULTACOBROSOUT realizarConsulta(CONSULTACOBROSIN consulta)
	{
		SETributosAsturiasConsultaPortType port;
		SETributosAsturiasConsultaBinderQSService servPago=new SETributosAsturiasConsultaBinderQSService();
		port = servPago.getSETributosAsturiasConsultaBinderQSPort();
		javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) port; // enlazador de protocolo para el servicio.
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
		if ("S".equals(prefCajaRural.getValidarFirma()))
		{
			handlerList.add(new es.tributasenasturias.webservices.soap.GestionFirmaEntidadRemotaHandler(prefCajaRural.getIdNodoFirmar(),prefCajaRural.getNodoContenedorFirma(),prefCajaRural.getNsNodoContenedorFirma(),prefCajaRural.getValidaCertificado(),prefCajaRural.getAliasServicio(), this.idSesion));
		}
		handlerList.add(new es.tributasenasturias.webservices.soap.LogMessageHandlerClient(idSesion));
		handlerList.add(new es.tributasenasturias.webservices.soap.PreprocesoCajaRural());
		bi.setHandlerChain(handlerList);
		return port.consulta(consulta);
	}

}
