package es.tributasenasturias.seguridad.servicio;


import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.servicios.clientes.autenticacionEPST.AutenticacionEPST;
import es.tributasenasturias.servicios.clientes.autenticacionEPST.AutenticacionEPST_Service;





/**
 * 
 * @author crubencvs
 * Clase que encapsular� la llamada al servicio de autenticaci�n del Principado de Asturias.
 */
public class VerificadorCertificado{

	private String endpointAutenticacionEPST;
	AutenticacionEPST_Service serv;
	AutenticacionEPST port;
	//Manejador para los mensajes de comunicaci�n.
	//No es necesario que exista, pero si se quieren registrar o manipular los mensajes, es 
	//necesario indicarlo.
	private SOAPHandler<SOAPMessageContext> manejadorMensajes=null;
	/**
	 * Constructor por defecto, no se podr� instanciar.
	 */
	@SuppressWarnings("unused")
	private VerificadorCertificado()
	{
		
	}
	
	private AutenticacionEPST_Service instanciarAutenticacionEPST_Service()
	{
		//Se modifica de forma din�mica la localizaci�n del WSDL, que debe estar en el
		//jar donde se define AutenticacionEPST_Service, y debe estar situado en 
		//META-INF/wsdls/PXAutenticacionEPST.
		//Esto no es peor que lo que genera el cliente por defecto, que tambi�n lo mete,
		//pero al menos esto funciona bien.
		//Desactivado, s�lo tiene sentido si forma parte de una librer�a jar.
		//WebServiceClient ann = AutenticacionEPST_Service.class.getAnnotation(WebServiceClient.class);
		//URL urlWsdl = AutenticacionEPST_Service.class.getResource("/META-INF/wsdls/PXAutenticacionEPST.wsdl");
		//return new AutenticacionEPST_Service (urlWsdl,new QName(ann.targetNamespace(), ann.name()));
		return new AutenticacionEPST_Service();
	}
	/**
	 * Construye un comprobador de certificado cuyas llamadas se dirigir�n a un endpoint concreto.
	 * No se incluye informaci�n sobre manejadores de mensajes, con lo cual no se manejar�n.
	 * @param endpointAutenticacionEPST endpoint de autenticaci�n de EPST
	 */
	protected VerificadorCertificado(String endpointAutenticacionEPST) {		
		this.endpointAutenticacionEPST=endpointAutenticacionEPST;
		serv = instanciarAutenticacionEPST_Service();
		port = serv.getAutenticacionEPSTSOAP();
		javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) port; 
		bpr.getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY , endpointAutenticacionEPST);
	}
	/**
	 * Construye un comprobador de certificado cuyas llamadas se dirigir�n a un endpoint concreto,
	 * y cuyos mensajes los interceptar� un manejador que se indica.
	 */
	protected VerificadorCertificado(String endpointAutenticacionEPST, SOAPHandler<SOAPMessageContext> manejador) {		
		this(endpointAutenticacionEPST);
		this.manejadorMensajes=manejador;
	}
	
	/**
	 * Realiza la llamada a la funci�n "login" del servicio de autenticaci�n.
	 * @param certificado Certificado a validar
	 * @return identificador recuperado, si el resultado es Valido.
	 */
	@SuppressWarnings("unchecked")
	public InfoCertificado login (String certificado) throws SeguridadException
	{
		InfoCertificado info = SeguridadFactory.newInfoCertificado();
		//Para recuperar datos de la consulta de certificado.
        Holder<Boolean>esError = new Holder<Boolean>();
        Holder<String>idError = new Holder<String>();
        Holder<String>mensajeError = new Holder<String>();
        Holder<String>cif = new Holder<String>();
        Holder<String>nifNIE= new Holder<String>();
        Holder<String>razonSocial = new Holder<String>();
        Holder<String>apellido1 = new Holder<String>();
        Holder<String>apellido2 = new Holder<String>();
        Holder<String>nombre = new Holder<String>();
        Holder<String>tipo = new Holder<String>();
        //Inclu�mos el manejador de mensajes.
		if (manejadorMensajes!=null)
		{
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) port;
			Binding bi = bpr.getBinding();
			List<Handler> handlerList = bi.getHandlerChain();
			if (handlerList == null)
			{
			   handlerList = new ArrayList<Handler>();
			}
			handlerList.add(manejadorMensajes);
			bi.setHandlerChain(handlerList);
		}
		try
		{
			//Se realiza la operaci�n
			port.login(certificado, esError, idError, mensajeError, cif, nifNIE, razonSocial, apellido1, apellido2,nombre, tipo);
			if (!esError.value.booleanValue())
			{
				//Devolvemos CIF o NIF
				if (cif.value!=null && !"".equals(cif.value))
				{
					info.setCif(cif.value);
				}
				else if (nifNIE.value!=null && !"".equals(nifNIE.value))
				{
					info.setNifNie(nifNIE.value);
				}
				info.setApellido1(apellido1.value);
				info.setApellido2(apellido2.value);
				info.setRazonSocial(razonSocial.value);
				info.setNombre(nombre.value);
				info.setTipo(tipo.value);
				info.setValidez(InfoCertificado.Validez.VALIDO);
			}
			else
			{
				info.setValidez(InfoCertificado.Validez.INVALIDO);
			}
		}
		catch (Exception ex)
		{
			//Error producido, seguramente por comunicaciones o SOAP Fault.
			throw new SeguridadException ("Error producido en la comprobaci�n de validez del certificado:"+ ex.getMessage(),ex);
		}
		return info;
	}



	public SOAPHandler<SOAPMessageContext> getManejadorMensajes() {
		return manejadorMensajes;
	}

	public void setManejadorMensajes(
			SOAPHandler<SOAPMessageContext> manejadorMensajes) {
		this.manejadorMensajes = manejadorMensajes;
	}
	/**
	 * @return recupera el endpoint de autenticaci�n
	 */
	public String getEndpointAutenticacionEPST() {
		return endpointAutenticacionEPST;
	}
	/**
	 * @param endpointAutenticacionEPST el endpoint de autenticaci�n a utilizar
	 */
	public void setEndpointAutenticacionEPST(String endpointAutenticacionEPST) {
		this.endpointAutenticacionEPST = endpointAutenticacionEPST;
	}
}
