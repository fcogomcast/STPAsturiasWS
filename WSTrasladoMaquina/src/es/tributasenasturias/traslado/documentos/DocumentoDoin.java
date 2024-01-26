package es.tributasenasturias.traslado.documentos;


import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.Handler;

import es.tributasenasturias.servicios.doin.WSConsultaDoinDocumentos;
import es.tributasenasturias.servicios.doin.WSConsultaDoinDocumentos_Service;
import es.tributasenasturias.traslado.exceptions.DocumentoException;
import es.tributasenasturias.traslado.preferencias.Preferencias;
import es.tributasenasturias.traslado.soap.SoapClientHandler;




/**
 * Permite la gesti�n de documentos de DOIN_DOCUMENTOS_INTERNET
 * @author crubencvs
 *
 */
public class DocumentoDoin{
	private String documento;
	private Preferencias pref;
	private String idSesion;
	
	/**
	 * @return the documento
	 */
	public String getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	/**
	 * Constructor
	 * @param p Preferencias de la llamada
	 * @param idSesion id Sesi�n de la petici�n
	 */
	protected DocumentoDoin(Preferencias p, String idSesion)
	{
		this.pref= p;
		this.idSesion=idSesion;
	}
	@SuppressWarnings("unchecked")
	public String altaDocumento (String nombreDoin, String tipo, String codigoVerificacion, String nifSujetoPasivo, String nifPresentador, String documento) throws DocumentoException
	{
		try{
			WSConsultaDoinDocumentos_Service srv = new WSConsultaDoinDocumentos_Service();
			WSConsultaDoinDocumentos port = srv.getWSConsultaDoinDocumentosSOAP();
			BindingProvider bp = (BindingProvider) port;
			bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, pref.getEndpointDocumentos());
			List<Handler> handlerList = bp.getBinding().getHandlerChain();
			if (handlerList==null) // puede que esto no sea posible
			{
				handlerList = new ArrayList<Handler>();
			}
			//A�adimos el manejador.
			handlerList.add(new SoapClientHandler(idSesion));
			bp.getBinding().setHandlerChain(handlerList);
			Holder<String>doc = new Holder<String>();
			Holder<String>error = new Holder<String>(); //Aqu� recibimos el resultado.
			Holder<String>resultado = new Holder<String>();
			port.altaDoinDocumento(nombreDoin, tipo, codigoVerificacion, nifSujetoPasivo, nifPresentador, documento, "PDF", true, doc, error, resultado);
			return error.value;
		} catch (Exception e)
		{
			throw new DocumentoException("Error en el alta de documento "+e.getMessage(),e);
		}
	}
	/**
	 * Recupera un documento de la tabla de doin
	 * @param nombre Nombre doin
	 * @param tipo Tipo de documento
	 * @param descomprimir Si se debe descomprimir
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getDocumentoDoin(String nombre, String tipo, boolean descomprimir) throws DocumentoException
	{
		try {
		WSConsultaDoinDocumentos_Service srv = new WSConsultaDoinDocumentos_Service();
		WSConsultaDoinDocumentos port = srv.getWSConsultaDoinDocumentosSOAP();
		BindingProvider bp = (BindingProvider) port;
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, pref.getEndpointDocumentos());
		List<Handler> handlerList = bp.getBinding().getHandlerChain();
		if (handlerList==null) // puede que esto no sea posible
		{
			handlerList = new ArrayList<Handler>();
		}
		//A�adimos el manejador.
		handlerList.add(new SoapClientHandler(idSesion));
		bp.getBinding().setHandlerChain(handlerList);
		Holder<String>doc = new Holder<String>();
		Holder<String>error = new Holder<String>(); //Aqu� recibimos el resultado.
		Holder<String>resultado = new Holder<String>();
		port.consultaDoinDocumento(nombre, tipo, descomprimir, doc, error, resultado);
		this.documento= doc.value;
		return error.value;
		} catch (Exception e) {
			throw new DocumentoException("Error en la recuperaci�n de documento "+e.getMessage(),e);
		}
	}
	
	

}
