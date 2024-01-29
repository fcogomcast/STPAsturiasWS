package es.tributasenasturias.webservices.Certificados;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceContext;

import es.tributasenasturias.webservices.Certificados.impl.ConsultaCertificadoImpl;
import es.tributasenasturias.webservices.Certificados.impl.Flags;
import es.tributasenasturias.webservices.Certificados.impl.SalidaServicio;
import es.tributasenasturias.webservices.Certificados.utils.Constantes;
import es.tributasenasturias.webservices.Certificados.utils.Log.GenericAppLogger;
import es.tributasenasturias.webservices.Certificados.utils.Log.TributasLogger;

/**
 * This class was generated by the JAX-WS RI. Oracle JAX-WS 2.1.3-06/19/2008
 * 07:03 PM(bt) Generated source version: 2.1
 * 
 */
@WebService(portName = "ConsultaCertificadosSOAP", serviceName = "ConsultaCertificados", targetNamespace = "http://webservices.tributasenasturias.es/ConsultaCertificados/", wsdlLocation = "/wsdls/ConsultaCertificados.wsdl", endpointInterface = "es.tributasenasturias.webservices.Certificados.ConsultaCertificados")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
@HandlerChain (file="HandlerChain.xml")
public class ConsultaCertificados_ConsultaCertificadosSOAPImpl implements
		ConsultaCertificados {


	@Resource
	WebServiceContext ws;
	
	public ConsultaCertificados_ConsultaCertificadosSOAPImpl() {
	}

	/**
	 * 
	 * @param parameters
	 */
	public void consultaCertificados(Holder<SERVICIOWEB> parameters) {
		try
		{
			Flags flags;
			if (ws.getMessageContext().containsKey(Constantes.FLAGS))
			{
				flags=(Flags)ws.getMessageContext().get(Constantes.FLAGS);
			}
			else
			{
				flags = new Flags(); //En este caso, todos al valor por defecto (boolean=false)
			}
			SERVICIOWEB respuesta = null;
			ConsultaCertificadoImpl cons=new ConsultaCertificadoImpl();
			respuesta = cons.consultarCertificado(parameters.value, flags);
			parameters.value=respuesta;
		}
		catch (Exception ex)
		{
			GenericAppLogger log=new TributasLogger();
			log.error("Error en la ejecuci�n del servicio web: " + ex.getMessage());
			SERVICIOWEB respuesta= new SalidaServicio(parameters.value);
			respuesta.getRESPUESTA().setRESULTADO("Error no controlado.");
			parameters.value=respuesta;
			
		}
	}

}