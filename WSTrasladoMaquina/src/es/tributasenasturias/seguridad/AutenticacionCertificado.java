package es.tributasenasturias.seguridad;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.seguridad.autenticacion.AutenticacionEPST;
import es.tributasenasturias.seguridad.autenticacion.AutenticacionEPST_Service;

/**
 * 
 * @author crubencvs
 * Clase que encapsulará la llamada al servicio de autenticación de certificado.
 */
public class AutenticacionCertificado{

	private AutenticacionEPST_Service service;
	private AutenticacionEPST port;
	private SOAPHandler<SOAPMessageContext> handler=null;
	
	/**
	 * Clase con los datos de certificado que devuelve la autenticación
	 * En caso de que el certificado no sea válido, sólo se devuelve el valor de "esValido",
	 * junto con el idError y mensajeError
	 * El resto de datos no se devolverán 
	 * @author crubencvs
	 *
	 */
	public static class ResultadoAutenticacion{
		private boolean esValido;
	    private String idError;
	    private String mensajeError;
	    private String cif;
	    private String nifnie;
	    private String razonSocial;
	    private String apellido1;
	    private String apellido2;
	    private String nombre;
	    private String tipo;
		public boolean esValido() {
			return esValido;
		}
		public String getIdError() {
			return idError;
		}
		public String getMensajeError() {
			return mensajeError;
		}
		public String getCif() {
			return cif;
		}
		public String getNifnie() {
			return nifnie;
		}
		public String getRazonSocial() {
			return razonSocial;
		}
		public String getApellido1() {
			return apellido1;
		}
		public String getApellido2() {
			return apellido2;
		}
		public String getNombre() {
			return nombre;
		}
		public String getTipo() {
			return tipo;
		}
	}
	/**
	 * Constructor, require el endpoint del servicio de autenticación de certificados.
	 * No es autónomo.
	 * @param endpoint Endpoint del servicio al que llamar para autenticar certificados
	 * @param handler Manejador SOAP para interceptar el mensaje
	 */
	public AutenticacionCertificado(String endpoint, SOAPHandler<SOAPMessageContext> handler) {		
		service = new AutenticacionEPST_Service();
		port= service.getAutenticacionEPSTSOAP();
		BindingProvider bpr=(BindingProvider) port;
		bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,endpoint);
		this.handler=handler;
	}
	/**
	 * Constructor, requiere el endpoint del servicio de autenticación de certificados
	 * No admite manejador SOAP, y por tanto no se puede interceptar el mensaje
	 * @param endpoint
	 */
	public AutenticacionCertificado(String endpoint){
		this(endpoint, null);
	}
	
	/**
	 * Realiza la comprobación de certificado
	 * @param certificado Certificado como cadena en base64 que se quiere comprobar
	 * @return ResultadoAutenticacion con el resultado de la autenticación de certificado
	 * @throws SeguridadException
	 */
	@SuppressWarnings("unchecked")
	public ResultadoAutenticacion comprobarCertificado(String certificado ) throws SeguridadException{
		ResultadoAutenticacion r = new ResultadoAutenticacion();
		
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
        
        if (this.handler!=null)
        {
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) port;
			Binding bi = bpr.getBinding();
			List<Handler> handlerList = bi.getHandlerChain();
			if (handlerList == null)
			{
			   handlerList = new ArrayList<Handler>();
			}
			handlerList.add(this.handler);
			bi.setHandlerChain(handlerList);
        }
        try{
        	port.login(certificado, esError, idError, mensajeError, cif, nifNIE, razonSocial, apellido1, apellido2, nombre, tipo);
        	if (!esError.value.booleanValue()){
        		if (cif.value!=null && !"".equals(cif.value)){
        			r.cif=cif.value;
        		} 
        		if (nifNIE.value!=null && !"".equals(nifNIE.value)){
        			r.nifnie="";
        		}
        		r.apellido1=apellido1.value;
        		r.apellido2=apellido2.value;
        		r.razonSocial=razonSocial.value;
        		r.nombre=nombre.value;
        		r.tipo=tipo.value;
        		r.esValido=true;
        	}
        	else {
        		r.esValido=false;
        		r.idError=idError.value;
        		r.mensajeError=mensajeError.value;
        	}
        }catch (Exception e){
        	throw new SeguridadException("Error producido en la comprobación del certificado: "+ e.getMessage(),e);
        }
		return r;
	}
	
}

