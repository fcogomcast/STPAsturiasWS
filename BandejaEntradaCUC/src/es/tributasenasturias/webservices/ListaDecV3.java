package es.tributasenasturias.webservices;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import es.tributasenasturias.abstracts.ServiceProxy;
import es.tributasenasturias.impl.BandejaEntradaProxy;
import es.tributasenasturias.webservices.messages.ListaDecV3Ent;
import es.tributasenasturias.webservices.messages.ListaDecV3Sal;

@WebService(name = "ListaDecV3")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class ListaDecV3 {
	
	@WebMethod (operationName="ListaDecV3")
	@WebResult(name = "ListaDecV3Sal", partName = "ListaDecV3Sal")
	public final ListaDecV3Sal listaDecV3(
			@WebParam(name = "ListaDecV3Ent", partName = "ListaDecV3Ent")
			ListaDecV3Ent listaDecV3Ent)throws Exception
	{
		try
		{
			ServiceProxy prx = new BandejaEntradaProxy();
			return ((BandejaEntradaProxy) prx).callOperation(listaDecV3Ent);
		}
		catch (Exception ex)
		{
			//throw ex;
			ex.printStackTrace();
			return null;
		}
	}
}
