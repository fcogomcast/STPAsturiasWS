package es.tributasenasturias.pasarelas.caixa;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.SOAPFaultException;

import org.ejemplo.ANULACIONIN;
import org.ejemplo.ANULACIONOUT;
import org.ejemplo.CONSULTACOBROSIN;
import org.ejemplo.CONSULTACOBROSOUT;
import org.ejemplo.PAGOIN;
import org.ejemplo.PAGOOUT;
import org.w3._2000._09.xmldsig_.SignatureType;


import es.tributasenasturias.ejemplo.tributosasturias.TributosAsturias;
import es.tributasenasturias.ejemplo.tributosasturias.TributosAsturias_Service;
import es.tributasenasturias.pasarelas.PreferenciasPasarela;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.utils.Logger;



/**
 * Realiza la comunicación con LaCaixa.
 * @author crubencvs
 *
 */
public final class ProxyCaixa{


	private PreferenciasCaixa prefCaixa;
	private String idSesion;
	private String endpoint;
	private Logger logger;
	public ProxyCaixa (String endpoint, PreferenciasPasarela pref, String idSesion, Logger log)
	{
		this.endpoint=endpoint;
		this.prefCaixa=(PreferenciasCaixa)pref;
		this.idSesion=idSesion;
		this.logger=log;
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
		TributosAsturias port;
		TributosAsturias_Service servPago=new TributosAsturias_Service();
		port = servPago.getTributosAsturiasSOAP();
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
		if ("S".equals(prefCaixa.getValidarFirma()))
		{
			handlerList.add(new es.tributasenasturias.webservices.soap.GestionFirmaEntidadRemotaHandler(prefCaixa.getIdNodoFirmar(),prefCaixa.getNodoContenedorFirma(),prefCaixa.getNsNodoContenedorFirma(),prefCaixa.getValidaCertificado(),prefCaixa.getAliasServicio(), this.idSesion));
		}
		handlerList.add(new es.tributasenasturias.webservices.soap.LogMessageHandlerClient(idSesion));
		bi.setHandlerChain(handlerList);
		try
		{
			return port.pago(pago, new SignatureType(), new Holder<SignatureType>());
		}
		catch (SOAPFaultException e)
		{
			logger.error("Error en comunicación con LaCaixa. Mensaje:"+e.getMessage());
			PAGOOUT out= new PAGOOUT();
			out.setRESULTADO(Constantes.ERROR_SOAP_FAULT_CAIXA); //Error, valdría cualquier cosa no parametrizada.
			out.setRESULTADODESCRIPCION(e.getMessage());
			return out;
		}
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
		TributosAsturias port;
		TributosAsturias_Service servPago=new TributosAsturias_Service();
		port = servPago.getTributosAsturiasSOAP();
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
		if ("S".equals(prefCaixa.getValidarFirma()))
		{
			handlerList.add(new es.tributasenasturias.webservices.soap.GestionFirmaEntidadRemotaHandler(prefCaixa.getIdNodoFirmar(),prefCaixa.getNodoContenedorFirma(),prefCaixa.getNsNodoContenedorFirma(),prefCaixa.getValidaCertificado(),prefCaixa.getAliasServicio(), this.idSesion));
		}
		handlerList.add(new es.tributasenasturias.webservices.soap.LogMessageHandlerClient(idSesion));
		bi.setHandlerChain(handlerList);
		try
		{
			return port.anulacion(anulacion, new SignatureType(), new Holder<SignatureType>());
		}
		catch (SOAPFaultException e)
		{
			logger.error("Error en comunicación con LaCaixa. Mensaje:"+e.getMessage());
			ANULACIONOUT out= new ANULACIONOUT();
			out.setRESULTADO(Constantes.ERROR_SOAP_FAULT_CAIXA); //Error, valdría cualquier cosa no parametrizada.
			out.setRESULTADODESCRIPCION(e.getMessage());
			return out;
		}
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
		TributosAsturias port;
		TributosAsturias_Service servPago=new TributosAsturias_Service();
		port = servPago.getTributosAsturiasSOAP();
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
		if ("S".equals(prefCaixa.getValidarFirma()))
		{
			handlerList.add(new es.tributasenasturias.webservices.soap.GestionFirmaEntidadRemotaHandler(prefCaixa.getIdNodoFirmar(),prefCaixa.getNodoContenedorFirma(),prefCaixa.getNsNodoContenedorFirma(),prefCaixa.getValidaCertificado(),prefCaixa.getAliasServicio(), this.idSesion));
		}
		handlerList.add(new es.tributasenasturias.webservices.soap.LogMessageHandlerClient(idSesion));
		bi.setHandlerChain(handlerList);
		try
		{
			return port.consulta(consulta, new SignatureType(), new Holder<SignatureType>());
		}
		catch (SOAPFaultException e)
		{
			logger.error("Error en comunicación con LaCaixa. Mensaje:"+e.getMessage());
			CONSULTACOBROSOUT out= new CONSULTACOBROSOUT();
			out.setRESULTADO(Constantes.ERROR_SOAP_FAULT_CAIXA); //Error, valdría cualquier cosa no parametrizada.
			out.setRESULTADODESCRIPCION(e.getMessage());
			return out;
		}
	}

}
