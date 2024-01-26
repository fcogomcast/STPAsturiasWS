package es.tributasenasturias.servicios.RecepcionDocumentos;

import javax.jws.WebService;
import javax.jws.HandlerChain;
import javax.xml.ws.BindingType;

import es.tributasenasturias.utils.GestorIdLlamada;

/**
 * This class was generated by the JAX-WS RI. Oracle JAX-WS 2.1.3-06/19/2008
 * 07:03 PM(bt) Generated source version: 2.1
 * 
 */
@WebService(portName = "RecepcionDocumentacionSOAP", serviceName = "RecepcionDocumentacion", targetNamespace = "http://servicios.tributasenasturias.es/Terceros/RecepcionDocumentacion", wsdlLocation = "/wsdls/wsdlRecepcionDocumentos.wsdl", endpointInterface = "es.tributasenasturias.servicios.RecepcionDocumentos.RecepcionDocumentacion")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
@HandlerChain(file="handler.xml")
public class RecepcionDocumentacion_RecepcionDocumentacionSOAPImpl implements
		RecepcionDocumentacion {

	public RecepcionDocumentacion_RecepcionDocumentacionSOAPImpl() {
	}

	/**
	 * 
	 * @param parameters
	 * @return returns
	 *         es.tributasenasturias.servicios.RecepcionDocumentos.DocumentacionOutType
	 */
	public DocumentacionOutType recibirDocumentos(DocumentacionInType parameters) {
		DocumentacionOutType doc= new DocumentacionOutType();
		try
		{
			ReceptorLista rl = new ReceptorLista();
			doc.setResultado(rl.procesaLista(parameters.getListaDocumentos()));
		}
		catch (Exception e)
		{
			doc.setResultado(new GeneradorResultado().generaResultadoFatal());
		}
		finally
		{
			GestorIdLlamada.desasociarIdLlamada();
		}
		return doc;
	}

}