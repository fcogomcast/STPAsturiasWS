package es.tributasenasturias.utils;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.Handler;

import es.tributasenasturias.services.ws.wsconsultadoindocumentos.wsconsultadoindocumentos.WSConsultaDoinDocumentos;
import es.tributasenasturias.services.ws.wsconsultadoindocumentos.wsconsultadoindocumentos.WSConsultaDoinDocumentos_Service;
import es.tributasenasturias.webservice.DocumentoPago;
import es.tributasenasturias.webservice.DocumentoPagoService;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.IContextReader;

/**
 * Agrupa la funcionalidad de generación de justificantes de pago
 * @author crubencvs
 *
 */
public class JustificantePago implements IContextReader{
	
	Preferencias pref;
	CallContext context;
	String idSesion;
	DocumentoPago doc;
	WSConsultaDoinDocumentos consDoc;
	
	/**
	 * Constructor. Realiza la instanciación del cliente de servicio de generación de documento
	 *, desde 14/12/2012 también la instanciación del cliente de consulta de documentos..
	 * @param context
	 */
	public JustificantePago(CallContext context) {
		this.context= context;
		pref = (Preferencias) context.get(CallContextConstants.PREFERENCIAS);
		idSesion = (String)context.get(CallContextConstants.ID_SESION);
		DocumentoPagoService srv = new DocumentoPagoService();
		doc = srv.getDocumentoPagoPort();
		String endpointDocPago = pref.getEndpointJustificantePago();
		setEndpoint((javax.xml.ws.BindingProvider)doc, endpointDocPago);
		setLogHandler((javax.xml.ws.BindingProvider)doc,idSesion);
		
		WSConsultaDoinDocumentos_Service srvc = new WSConsultaDoinDocumentos_Service();
		consDoc = srvc.getWSConsultaDoinDocumentosSOAP();
		String endpointConsulta = pref.getEndpointConsultaDocumentos();
		setEndpoint ((javax.xml.ws.BindingProvider)consDoc,endpointConsulta);
		setLogHandler((javax.xml.ws.BindingProvider) consDoc,idSesion);
	}
	/**
	 * Recupera el documento almacenado en base de datos, si existe. Devuelve null en otro caso.
	 * @param justificante Número de justificante a buscar.
	 * @return Una cadena conteniendo el documento en base 64, o null si no se ha encontrado el documento.
	 */
	public String getDocumentoAlmacenado (String justificante, String tipoDocumento)
	{
		String pdf=null;
		Holder<String> documento= new Holder<String>();
		Holder<String> error= new Holder<String>();
		Holder<String> resultado= new Holder<String>();
		consDoc.consultaDoinDocumento(justificante, tipoDocumento, true, documento, error, resultado);
		if (documento.value!=null && !"".equals(documento.value))
		{
			pdf=documento.value;
		}
		return pdf;
		
	}
	/**
	 * Recupera el justificante de pago indicado.
	 * @param justificante
	 * @return
	 */
	public String getJustificante(String justificante)
	{
		String C_JUSTIFICANTE_PAGO="P";
		if (justificante!=null && !"".equals(justificante))
		{
			String docAlmacenado = getDocumentoAlmacenado (justificante,C_JUSTIFICANTE_PAGO);
			if (docAlmacenado!=null)
			{
				return docAlmacenado;
			}
			else
			{
				return doc.justificantePagoAutoliquidacion(justificante,true); //true= dar de alta documento.
			}
		}
		else
		{
			return "";
		}
	}
	/**
	 * Asocia un endpoint al bindingProvider que se indica. Si el endpoint es vacío o nulo, no se asocia.
	 * @param bpr BindingProvider al que asociar el endpoint.
	 * @param endpoint Dirección del endpoint a asociar.
	 */
	private void setEndpoint (BindingProvider bpr, String endpoint)
	{
		if (endpoint!=null && !"".equals(endpoint))
		{
			bpr.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
		}
	}
	/**
	 * Asocia un manejador de mensajes de salida al bindingProvider indicado.
	 * @param bpr BindingProvider al que asociar el log
	 * @param idSesion Identificador de sesión que se incluirá en las líneas del log.
	 */
	@SuppressWarnings("unchecked")
	private void setLogHandler(BindingProvider bpr, String idSesion)
	{
		javax.xml.ws.Binding bi = bpr.getBinding();
		List<Handler> handlerList = bi.getHandlerChain();
		if (handlerList==null)
		{
			handlerList = new ArrayList<Handler>();
		}
		handlerList.add(new es.tributasenasturias.webservices.soap.LogMessageHandlerClient(idSesion));
		bi.setHandlerChain(handlerList);
	}
	/* (non-Javadoc)
	 * @see es.tributasenasturias.webservices.context.IContextReader#getCallContext()
	 */
	@Override
	public CallContext getCallContext() {
		return context;
	}

	/* (non-Javadoc)
	 * @see es.tributasenasturias.webservices.context.IContextReader#setCallContext(es.tributasenasturias.webservices.context.CallContext)
	 */
	@Override
	public void setCallContext(CallContext ctx) {
		context=ctx;
	}
	
}
