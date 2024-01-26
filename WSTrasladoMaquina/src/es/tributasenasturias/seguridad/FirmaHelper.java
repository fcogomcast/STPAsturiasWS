/**
 * 
 */
package es.tributasenasturias.seguridad;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.seguridad.firma.Exception_Exception;
import es.tributasenasturias.seguridad.firma.FirmaDigital;
import es.tributasenasturias.seguridad.firma.WsFirmaDigital;



/**Clase de utilidad para realizar firma digital de XML y validaci�n de firma.
 * Se utiliza la funci�n de firma que permite el proceso de cualquier XML, dados unos par�metros
 * que indique qu� se firma.
 * @author crubencvs
 *
 */
public class FirmaHelper {
	
	private WsFirmaDigital srv;
	private FirmaDigital port;
	
	@SuppressWarnings("unused")
	private FirmaHelper(){};
	/**
	 * Construye un objeto {@link FirmaHelper} que enviar� sus peticiones de firma a un endpoint concreto.
	 * @param endpoint Endpoint del servicio de firma Digital.
	 */
	public FirmaHelper (String endpoint)
	{
		srv = instanciarServicio();
		port = srv.getServicesPort();
		BindingProvider bpr=(BindingProvider) port;
		bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,endpoint);
	}
	/**
	 * Construye un objeto {@link FirmaHelper} que enviar� sus peticiones  de firma a un endpoint concreto,
	 * y cada uno de sus mensajes intercambiados se procesar� con el manejador de mensajes SOAP
	 * {@link SOAPHandler} especificado.
	 * @param endpoint Endpoint del servicio de firma Digital
	 * @param soapHandler {@link SOAPHandler} que procesar� los mensajes SOAP intercambiados.
	 */
	public FirmaHelper (String endpoint, SOAPHandler<SOAPMessageContext> soapHandler)
	{
		this (endpoint);
		changeHandler (soapHandler);
	}
	/**
	 * Modifica de forma din�mica la localizaci�n del WSDL
	 * @return Objeto servicio {@link WsFirmaDigital} que permite recuperar las operaciones de la firma digital.
	 */
	private WsFirmaDigital instanciarServicio()
	{
		return new WsFirmaDigital();
	}
	/**
	 * Firma un XML pasado, indicando qu� nodo se firmar� y d�nde se dejar� el mensaje.
	 * @param xmlData Cadena conteniendo el XML a firmar
	 * @param aliasCertificado Cadena conteniendo el alias de certificado con el que firmar el XML
	 * @param idNodoAFirmar Cadena del identificador del nodo a firmar.  Debe tener un atributo "ID" con este valor
	 * @param nodoPadre Cadena con el nombre del nodo padre sin cualificar, donde se dejar� el nodo de firma
	 * @param nsNodoPadre Cadena que contiene el namespace del nodo padre. Si no tuviera, se puede pasar vac�o.
	 * @return Cadena con el XML firmado.
	 * @throws SeguridadException Si se produce una excepci�n en la firma.
	 */
	public String firmaXML (String xmlData,
							String aliasCertificado, 
							String idNodoAFirmar, 
							String nodoPadre, 
							String nsNodoPadre) throws SeguridadException
	{
		String resultado;
		try {
			resultado = port.firmarXML(xmlData, aliasCertificado, idNodoAFirmar, nodoPadre, nsNodoPadre);
		} catch (Exception e) {
			throw new SeguridadException ("Error al firmar el xml:" + e.getMessage(),e);
		}
		return resultado;
	}
	/**
	 * Indica si un XML firmado es o no v�lido.
	 * @param xmlFirmado Cadena que contiene el XML firmado.
	 * @return boolean true si es v�lido, o boolean false si no.
	 * @throws SeguridadException Si se produce una excepci�n durante la validaci�n.
	 */
	public boolean esValido(String xmlFirmado) throws SeguridadException
	{
		try {
			return port.validar(xmlFirmado);
		} catch (Exception_Exception e) {
			throw new SeguridadException ("Error al validar el xml:" + e.getMessage(),e);
		}
	}
	/**
	 * Permite cambiar el manejador de mensajes SOAP que intercambiar� el objeto con el servicio web
	 * de firma digital.
	 * @param manejador Objeto {@link SOAPHandler} que procesar� los mensajes intercambiados con el 
	 * servicio web.
	 */
	@SuppressWarnings("unchecked")
	private void changeHandler(SOAPHandler<SOAPMessageContext> manejador)
	{
		javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) port; 
		if (manejador!=null)
		{
			Binding bi = bpr.getBinding();
			List <Handler> handlerList = bi.getHandlerChain();
			if (handlerList == null)
			{
			   handlerList = new ArrayList<Handler>();
			}
			handlerList.add(manejador);
			bi.setHandlerChain(handlerList);
		}	
	}
}
