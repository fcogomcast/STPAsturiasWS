package es.tributasenasturias.apremios.proxy;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.apremios.preferencias.Preferencias;
import es.tributasenasturias.apremios.soap.SoapClientHandler;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.ArchivoDigital;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.ArchivoDigital_Service;

/**
 * Clase para invocar funcionalidades del archivo digital
 * @author crubencvs
 *
 */
public class MediadorArchivoDigital {

	private Preferencias pref;
	private String idLlamada;
	
	/**
	 * Establece el manejador para un port
	 * @param manejador Manejador de mensajes
	 * @param port Port
	 */
	@SuppressWarnings("unchecked")
	private void addHandler(SOAPHandler<SOAPMessageContext> manejador, ArchivoDigital port)
	{
		javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) port; 
		if (manejador!=null)
		{
			Binding bi = bpr.getBinding();
			List<Handler> handlerList = bi.getHandlerChain();
			if (handlerList == null)
			{
			   handlerList = new ArrayList<Handler>();
			}
			handlerList.add(manejador);
			bi.setHandlerChain(handlerList);
		}	
	}
	/**
	 * Establece el endpoint del servicio
	 * @param port
	 * @param endpoint
	 */
	private void establecerEndpoint (ArchivoDigital port, String endpoint) {
		javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) port;
		bpr.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
	}
	
	public MediadorArchivoDigital(Preferencias pref, String idLlamada)  {
		this.pref= pref;
		this.idLlamada= idLlamada;
	}
	/**
	 * Obtiene un documento del archivo digital por identificador
	 * @param idArchivo Identificador del archivo
	 * @return Contenido binario del fichero o array de bytes vacío si no se ha encontrado
	 */
	public byte[] obtenerArchivo(int idArchivo, String codUsuario) {
		byte[] resultado;
		ArchivoDigital_Service srv = new ArchivoDigital_Service();
		ArchivoDigital port= srv.getArchivoDigitalSOAP();
		establecerEndpoint(port, pref.getEndpointArchivoDigital());
		addHandler(new SoapClientHandler(idLlamada), port);
		Holder<byte[]> archivo= new Holder<byte[]>();
		Holder<String> datosArchivo= new Holder<String>();
		Holder<String> error= new Holder<String>();
		port.obtieneArchivoPorId(codUsuario, idArchivo, archivo, datosArchivo, error);
		if (archivo.value!=null && archivo.value.length>0) {
			resultado=archivo.value;
		} else {
			resultado= new byte[0];
		}
		return resultado;
	}
}
