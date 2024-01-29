package com.stpa.ws.server.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import com.stpa.ws.handler.ClientHandler;
import com.stpa.ws.pref.lt.Preferencias;

import es.tributasenasturias.webservice.DocumentoPago;
import es.tributasenasturias.webservice.DocumentoPagoService;


import stpa.services.LanzaPL;
import stpa.services.LanzaPLService;



public final class ClienteWebServices {

	public ClienteWebServices(){};
	/**
	 * Devuelve una instancia de la interfaz contra el servicio lanzador, para ejecutar llamadas a base de datos.
	 * La instancia tendrá un manejador para enviar a fichero sus intercambios de mensajes.
	 * @return {@link LanzaPL}
	 */
	@SuppressWarnings("unchecked")
	public static LanzaPL getLanzadorService ()
	{
		Preferencias pref = new Preferencias();
		LanzaPLService srv = new LanzaPLService();
		LanzaPL port = srv.getLanzaPLSoapPort();
		//Endpoint
		BindingProvider bpr = (BindingProvider) port;
		bpr.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, pref.getM_wslanzadorendpoint());
		//Manejador
		Binding binding = bpr.getBinding();
		List<Handler> handlers=binding.getHandlerChain();
		if (handlers==null)
		{
			handlers= new ArrayList<Handler>();
		}
		handlers.add(new ClientHandler(GestorIdLlamada.getIdLlamada()));
		binding.setHandlerChain(handlers);
		return port;
	}
	/**
	 * Recupera una instancia del servicio de documentos de pago, para poder utilizar el servicio de impresión de documentos.
	 * La instancia tendrá un manejador para enviar a fichero sus intercambios de mensajes.
	 * @return {@link DocumentoPago}
	 */
	@SuppressWarnings("unchecked")
	public static DocumentoPago getDocumentoService ()
	{
		Preferencias pref = new Preferencias();
		DocumentoPagoService srv = new DocumentoPagoService();
		DocumentoPago port = srv.getDocumentoPagoPort();
		//Endpoint
		BindingProvider bpr = (BindingProvider) port;
		bpr.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, pref.getM_wsdocumentopagoendpoint());
		//Manejador
		Binding binding = bpr.getBinding();
		List<Handler> handlers=binding.getHandlerChain();
		if (handlers==null)
		{
			handlers= new ArrayList<Handler>();
		}
		handlers.add(new ClientHandler(GestorIdLlamada.getIdLlamada()));
		binding.setHandlerChain(handlers);
		return port;
	}

	/**
	 * Recupera un documento de pago, mediante llamada a "obtenerDocumentoPago" del servicio de documentos de pago.
	 * @param idEper IdEper del valor para el que obtener el documento de pago
	 * @param tipoNotificacion Tipo de notificación.
	 * @param comprimido S=Devuelve el documento comprimido, N=No devuelve el documento comprimido
	 * @return El documento a devolver, sea comprimido o no, en formato Base 64
	 */
	public static String obtenerDocumentoPago(String idEper, String tipoNotificacion, String comprimido)
	{
		DocumentoPago port = getDocumentoService();
		return port.obtenerDocumentoPago(idEper, tipoNotificacion,comprimido);
	}
	/**
	 * Ejecuta una petición a base de datos mediante el lanzador.
	 * @param esquema Esquema contra el que ejecutar la petición.
	 * @param peticion Petición a ejecutar.
	 * @param ip Ip desde la que se ejecuta 
	 * @param nif NIF
	 * @param nombre Nombre
	 * @param certificado Certificado
	 * @return El resultado de la petición.
	 */
	public static String executePL (String esquema,String peticion, String ip, String nif, String nombre, String certificado)
	{
		LanzaPL port = getLanzadorService();
		return port.executePL(esquema, peticion, ip, nif, nombre, certificado);
	}
	
}
