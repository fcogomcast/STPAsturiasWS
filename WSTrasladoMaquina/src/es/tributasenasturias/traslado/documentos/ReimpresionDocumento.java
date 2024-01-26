package es.tributasenasturias.traslado.documentos;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import es.tributasenasturias.servicios.documentos.WSDocumentos;
import es.tributasenasturias.servicios.documentos.WSDocumentos_Service;
import es.tributasenasturias.traslado.exceptions.DocumentoException;
import es.tributasenasturias.traslado.preferencias.Preferencias;
import es.tributasenasturias.traslado.soap.SoapClientHandler;


/**
 * Permite acceder a una reimpresión de documento.
 * @author crubencvs
 *
 */
public class ReimpresionDocumento{

	private Preferencias pref;
	private String idSesion;
	/**
	 * Constructor con preferencias.
	 * @param context
	 */
	public ReimpresionDocumento(Preferencias pref, String idSesion)
	{
		this.pref= pref;
		this.idSesion= idSesion;
	}
	/**
	 * Recupera una reimpresión de documento, pudiendo añadir además un código de verificación.
	 * @param elemento Identificador de elemento que permite localizar una reimpresión.
	 * @param tipoElemento Tipo de elemento que permite localizar una reimpresión.
	 * @param codigoVerificacion Código de verificación a imprimir en cada página de documento, o null si no se desea imprimir.
	 * @return Cadena en base64 conteniendo el documento reimpreso.
	 * @throws DocumentoException
	 */
	@SuppressWarnings("unchecked")
	public String getReimpresion (String elemento, String tipoElemento, String codigoVerificacion) throws DocumentoException
	{
		try
		{
			String doc=null;
			WSDocumentos_Service srv = new WSDocumentos_Service();
			WSDocumentos port = srv.getWSDocumentosPort();
			//Se modifica el endpoint y se enlaza con el log de cliente.
			BindingProvider bp = (BindingProvider) port;
			bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, pref.getEndpointImpresion());
			List<Handler> handlers = bp.getBinding().getHandlerChain();
			if (handlers==null)
			{
				handlers = new ArrayList<Handler>();
			}
			handlers.add(new SoapClientHandler(idSesion));
			bp.getBinding().setHandlerChain(handlers);
			doc=port.obtenerReimprimible(elemento, tipoElemento, codigoVerificacion);
			return doc;
		}
		catch (Exception e)
		{
			throw new DocumentoException ("Error al recuperar la reimpresión de documento:" + e.getMessage(),e);
		}
	}
}
