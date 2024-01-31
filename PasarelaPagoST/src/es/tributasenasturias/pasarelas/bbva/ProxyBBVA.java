package es.tributasenasturias.pasarelas.bbva;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.SOAPFaultException;


import es.tributasenasturias.client.pasarela.bbva.ANULACIONIN;
import es.tributasenasturias.client.pasarela.bbva.ANULACIONOUT;
import es.tributasenasturias.client.pasarela.bbva.CONSULTACOBROSIN;
import es.tributasenasturias.client.pasarela.bbva.CONSULTACOBROSOUT;
import es.tributasenasturias.client.pasarela.bbva.ESBSOAPInterface;
import es.tributasenasturias.client.pasarela.bbva.ESBSOAPService;
import es.tributasenasturias.client.pasarela.bbva.PAGOIN;
import es.tributasenasturias.client.pasarela.bbva.PAGOOUT;
import es.tributasenasturias.client.pasarela.bbva.SignatureType;
import es.tributasenasturias.pasarelas.PreferenciasPasarela;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.utils.Logger;



/**
 * Realiza la comunicaci�n con BBVA.
 * @author crubencvs
 *
 */
public final class ProxyBBVA{


	private PreferenciasBBVA prefBBVA;
	private String idSesion;
	private String endpoint;
	private Logger logger;
	public ProxyBBVA (String endpoint, PreferenciasPasarela pref, String idSesion, Logger log)
	{
		this.endpoint=endpoint;
		this.prefBBVA=(PreferenciasBBVA)pref;
		this.idSesion=idSesion;
		this.logger=log;
	}

	/**
	 * Realizaci�n de la operaci�n de pago contra el Proxy que comunica con la entidad externa.
	 * La entidad remota espera que en el mensaje viaje tambi�n la firma, pero no se incluye en este punto,
	 * sino que se interceptar� el mensaje de salida y se firmar�.
	 * @param pago Datos del pago, seg�n lo que espera la entidad remota.
	 * @return Objeto con el resultado de pago.
	 */
	@SuppressWarnings("unchecked")
	public final PAGOOUT realizarPago(PAGOIN pago)
	{
		ESBSOAPInterface port;
		ESBSOAPService servPago=new ESBSOAPService();
		port = servPago.getESBSoapHttpBinding();
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
		if ("S".equals(prefBBVA.getValidarFirma()))
		{
			//CRUBENCVS 42479. Se prepara para poder autenticar mediante AutenticacionEPST 
			handlerList.add(new es.tributasenasturias.webservices.soap.GestionFirmaEntidadRemotaHandler(prefBBVA.getIdNodoFirmar(),prefBBVA.getNodoContenedorFirma(),prefBBVA.getNsNodoContenedorFirma(),prefBBVA.getValidaCertificado(),prefBBVA.getAliasServicio(), this.idSesion));
		}
		handlerList.add(new es.tributasenasturias.webservices.soap.LogMessageHandlerClient(idSesion));
		bi.setHandlerChain(handlerList);
		try
		{
			return port.pagoRecibos(new Holder<SignatureType>(), pago);
		}
		catch (SOAPFaultException e)
		{
			logger.error("Error en comunicaci�n con BBVA. Puede ser un error esperado. Mensaje:"+e.getMessage());
			//Puede ser normal, algunos errores esperados se devuelven as�.
			PAGOOUT out= new PAGOOUT();
			out.setRESULTADO(Constantes.ERROR_SOAP_FAULT_BBVA); //Error, valdr�a cualquier cosa no parametrizada.
			out.setRESULTADODESCRIPCION(e.getMessage());
			return out;
		}
	}
	
	/**
	 * Realizaci�n de la operaci�n de anulaci�n contra el Proxy que comunica con el entidad externa.
	 * La entidad remota espera que en el mensaje viaje tambi�n la firma, pero no se incluye en este punto,
	 * sino que se interceptar� el mensaje de salida y se firmar�.
	 * @param anulacion Datos de la anulaci�n, seg�n lo que espera la entidad remota.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final ANULACIONOUT realizarAnulacion(ANULACIONIN anulacion)
	{
		ESBSOAPInterface port;
		ESBSOAPService servPago=new ESBSOAPService();
		port = servPago.getESBSoapHttpBinding();
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
		if ("S".equals(prefBBVA.getValidarFirma()))
		{
			handlerList.add(new es.tributasenasturias.webservices.soap.GestionFirmaEntidadRemotaHandler(prefBBVA.getIdNodoFirmar(),prefBBVA.getNodoContenedorFirma(),prefBBVA.getNsNodoContenedorFirma(),prefBBVA.getValidaCertificado(),prefBBVA.getAliasServicio(), this.idSesion));
		}
		handlerList.add(new es.tributasenasturias.webservices.soap.LogMessageHandlerClient(idSesion));
		bi.setHandlerChain(handlerList);
		try
		{
			return port.anulacionPago(new Holder<SignatureType>(), anulacion);
		}
		catch (SOAPFaultException e)
		{
			logger.error("Error en comunicaci�n con BBVA. Puede ser un error esperado. Mensaje:"+e.getMessage());
			//Puede ser normal, algunos errores esperados se devuelven as�.
			ANULACIONOUT out= new ANULACIONOUT();
			out.setRESULTADO(Constantes.ERROR_SOAP_FAULT_BBVA); //Error, valdr�a cualquier cosa no parametrizada.
			out.setRESULTADODESCRIPCION(e.getMessage());
			return out;
		}
	}
	
	/**
	 * Realizaci�n de la operaci�n de pago contra el Proxy que comunica con la entidad remota.
	 * La entidad remota espera que en el mensaje viaje tambi�n la firma, pero no se incluye en este punto,
	 * sino que se interceptar� el mensaje de salida y se firmar�.
	 * @param consulta Datos de la consulta tal cual los espera la entidad remota.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final CONSULTACOBROSOUT realizarConsulta(CONSULTACOBROSIN consulta)
	{
		ESBSOAPInterface port;
		ESBSOAPService servPago=new ESBSOAPService();
		port = servPago.getESBSoapHttpBinding();
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
		if ("S".equals(prefBBVA.getValidarFirma()))
		{
			handlerList.add(new es.tributasenasturias.webservices.soap.GestionFirmaEntidadRemotaHandler(prefBBVA.getIdNodoFirmar(),prefBBVA.getNodoContenedorFirma(),prefBBVA.getNsNodoContenedorFirma(),prefBBVA.getValidaCertificado(),prefBBVA.getAliasServicio(), this.idSesion));
		}
		handlerList.add(new es.tributasenasturias.webservices.soap.LogMessageHandlerClient(idSesion));
		bi.setHandlerChain(handlerList);
		try
		{
			return port.consultaCobros(new Holder<SignatureType>(), consulta);
		}
		catch (SOAPFaultException e)
		{
			logger.error("Error en comunicaci�n con BBVA. Puede ser un error esperado. Mensaje:"+e.getMessage());
			//Puede ser normal, algunos errores esperados se devuelven as�.
			CONSULTACOBROSOUT out= new CONSULTACOBROSOUT();
			out.setRESULTADO(Constantes.ERROR_SOAP_FAULT_BBVA); //Error, valdr�a cualquier cosa no parametrizada.
			out.setRESULTADODESCRIPCION(e.getMessage());
			return out;
		}
	}

}
