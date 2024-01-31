package es.tributasenasturias.servicios.tercerosmodelo600;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceContext;

import es.tributasenasturias.operaciones.OperacionesModelo600;

/*************************************************************************
 * Clase WebService que ejecuta las operaciones sobre el modelo 600
 *************************************************************************/
@WebService(portName = "TercerosModelo600", serviceName = "TercerosModelo600", targetNamespace = "http://servicios.tributasenasturias.es/TercerosModelo600", wsdlLocation = "/wsdls/WSDLTercerosModelo600.wsdl", endpointInterface = "es.tributasenasturias.servicios.tercerosmodelo600.TercerosModelo600PortType")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
@HandlerChain(file = "HandlerChain.xml")
public class TercerosModelo600_TercerosModelo600Impl implements TercerosModelo600PortType{

	@Resource WebServiceContext context;
	private String MSG_INFO_0999 = " Se ha producido un error grave durante la operación.";

    public TercerosModelo600_TercerosModelo600Impl() {}

    /**
     * calcular Realiza el cálculo del modelo 600 en base a los datos de la autoliquidación de entrada
     * @param parameters
     * @throws CalculoFault
     */
    public void calcular(Holder<es.tributasenasturias.servicios.calculo600generico.Remesa> parameters) throws CalculoFault{
		String remesa="";
		String respuesta="";
		OperacionesModelo600 operaciones;
		try{
    		operaciones=new OperacionesModelo600();
    		remesa=context.getMessageContext().get("remesa").toString();
    		respuesta=operaciones.getCalculoModelo600(remesa);
    		parameters.value=operaciones.stringToRemesaCalculo(respuesta);
    		context.getMessageContext().put("nsFirma",es.tributasenasturias.operaciones.firma.FirmaHelper.getNsCalculo());
    	}catch(CalculoFault cf){
    		throw cf;
		}catch(Exception e){
    		CalculoModelo600FaultInfo info=new CalculoModelo600FaultInfo();
    		info.setId("0999");
    		info.setMensaje(MSG_INFO_0999);
    		CalculoFault fallo=new CalculoFault(e.getMessage(),info);
    		throw fallo;
    	}
    }

    /**
     * pagoPresentacion Realiza el cálculo del modelo 600 en base a los datos de la autoliquidación de entrada
     * @param parameters
     * @throws PagoFault
     */
    public void pagoPresentacion(Holder<es.tributasenasturias.servicios.pago600generico.Remesa> parameters) throws PagoFault{
		String remesa="";
		String respuesta="";
		OperacionesModelo600 operaciones;
		try{
    		operaciones=new OperacionesModelo600();
    		remesa=context.getMessageContext().get("remesa").toString();
    		respuesta=operaciones.getPagoPresentacionModelo600(remesa);
    		parameters.value=operaciones.stringToRemesaPago(respuesta);
    		context.getMessageContext().put("nsFirma",es.tributasenasturias.operaciones.firma.FirmaHelper.getNsPago());
    	}catch(PagoFault cf){
    		throw cf;
		}catch(Exception e){
    		PagoModelo600FaultInfo info=new PagoModelo600FaultInfo();
    		info.setId("0999");
    		info.setMensaje(MSG_INFO_0999);
    		PagoFault fallo=new PagoFault(e.getMessage(),info);
    		throw fallo;
    	}
    }
}