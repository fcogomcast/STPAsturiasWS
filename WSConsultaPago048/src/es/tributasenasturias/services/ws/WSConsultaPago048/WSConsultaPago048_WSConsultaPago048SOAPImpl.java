package es.tributasenasturias.services.ws.WSConsultaPago048;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.Holder;

import es.tributasenasturias.services.ws.WSConsultaPago048.utils.Log.GenericAppLogger;
import es.tributasenasturias.services.ws.WSConsultaPago048.utils.Log.Logger;
import es.tributasenasturias.webservices.ResultadoConsulta;

/**
 * This class was generated by the JAX-WS RI. Oracle JAX-WS 2.1.3-06/19/2008
 * 07:03 PM(bt) Generated source version: 2.1
 * 
 */
@WebService(portName = "WSConsultaPago048SOAP", serviceName = "WSConsultaPago048", targetNamespace = "http://WSConsultaPago048.ws.services.tributasenasturias.es/WSConsultaPago048/", wsdlLocation = "/wsdls/WSConsultaPago048.wsdl", endpointInterface = "es.tributasenasturias.services.ws.WSConsultaPago048.WSConsultaPago048")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
@HandlerChain (file= "./utils/Log/LogHandler/HandlerChain.xml")
public class WSConsultaPago048_WSConsultaPago048SOAPImpl implements
		WSConsultaPago048 {

	public WSConsultaPago048_WSConsultaPago048SOAPImpl() {
	}
	
	/**
	 * 
	 * @param nrc
	 * @param error
	 * @param resultado
	 * @param numBastidor
	 * @param fechaPago
	 * @param numAutoliquidacion
	 */
	public void consultaPago048(String numAutoliquidacion, String numBastidor,
			Holder<String> resultado, Holder<String> error, Holder<String> nrc,
			Holder<String> fechaPago) {
		try
		{
			GenericAppLogger log=new Logger();
			log.trace("INICIO LLAMADA M�TODO WEB CONSULTAPAGO048");
			ResultadoConsulta resultadoConsulta = new ResultadoConsulta();
			ConsultaPago048Imp consulta = new ConsultaPago048Imp();
			
			resultadoConsulta = consulta.Ejecutar(numAutoliquidacion,numBastidor);
			resultado.value=resultadoConsulta.getRespuesta().getResultado();
			error.value =resultadoConsulta.getRespuesta().getError();
			nrc.value=resultadoConsulta.getRespuesta().getOperacion();
			fechaPago.value = resultadoConsulta.getRespuesta().getFechaPago();

			log.trace("FIN LLAMADA M�TODO WEB CONSULTAPAGO048");
			return;
		}
		catch (Exception ex)
		{
			GenericAppLogger log=new Logger();
			log.error("Error en la ejecuci�n del servicio web: " + ex.getMessage());
			resultado.value = "Error en la ejecuci�n del servicio web: " + ex.getMessage();
			System.out.println(ex.getStackTrace());
			return;
		}
	}
}