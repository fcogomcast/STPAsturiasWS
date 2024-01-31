package es.tributasenasturias.servicios.asturcon.consultaDeuda;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.jws.HandlerChain;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

import es.tributasenasturias.servicios.asturcon.consultaDeuda.Utils.Constantes;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.Utils.Utils;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.context.CallContext;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.context.CallContextConstants;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.context.CallContextManager;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.preferencias.Preferencias;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.preferencias.PreferenciasException;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.preferencias.PreferenciasFactory;
import es.tributasenasturias.utils.log.LogFactory;
import es.tributasenasturias.utils.log.Logger;
/**
 * SEI del servicio de consulta de Deuda que está disponible para Asturcon.
 * Permitirá realizar la operación de consulta de deuda para una serie de terceros en Tributas.
 * 07:03 PM(bt) Generated source version: 2.1
 * 
 */
@WebService(portName = "AsturconConsultaDeudaSOAP", serviceName = "AsturconConsultaDeuda", targetNamespace = "http://servicios.tributasenasturias.es/Asturcon/ConsultaDeuda/", wsdlLocation = "/wsdls/AsturconConsultaDeuda.wsdl", endpointInterface = "es.tributasenasturias.servicios.asturcon.consultaDeuda.AsturconConsultaDeuda")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
@HandlerChain(file="HandlerChain.xml")
public class AsturconConsultaDeuda_AsturconConsultaDeudaSOAPImpl implements
		AsturconConsultaDeuda {

	@Resource WebServiceContext wContext;
	public AsturconConsultaDeuda_AsturconConsultaDeudaSOAPImpl() {
		//La primera vez que se carga en memoria la clase, debería crear el fichero de preferencias.
		//Esto es al desplegar la aplicación, si no ha podido crearla ya más adelante lo intentará 
		//al usar las funciones. Esta variable que se usa en el constructor se descarta.
		try
		{
			PreferenciasFactory.newInstance();
		}
		catch (Exception ex)
		{}
	}

	/**
	 * Operación de solicitud de consulta de deuda. Permite que el sistema Asturcon XXI realice una 
	 * petición de consulta de deuda en el sistema Tributas. No se responde con los datos de la consulta
	 * realizada, sino solamente un acuse de recibo.
	 * @param mensaje Mensaje que contiene los datos de terceros a consultar, en formato
	 * 	es.tributasenasturias.servicios.asturcon.consultaDeuda.MENSAJEIN
	 * @return returns
	 *         es.tributasenasturias.servicios.asturcon.consultaDeuda.MENSAJEOUT
	 */
	public MENSAJEOUT solicitarConsulta(MENSAJEIN mensaje) {
		Logger log=null;
		try
		{
			//Creamos un contexto de llamada, para pasar parámetros como preferencias a las clases
			//que dependan de esta
			CallContext context = CallContextManager.newCallContext();
			Preferencias pref = (Preferencias) wContext.getMessageContext().get(Constantes.VAR_PREFERENCIAS);
			if (pref==null) //No debería, a menos que se haya eliminado el manejador que lo crea.
			{
				pref=PreferenciasFactory.newInstance();
			}
			String idSesion=(String)wContext.getMessageContext().get(Constantes.VAR_ID_SESION);
			if (idSesion==null || idSesion.equals(""))
			{
				//Recuperamos en este momento el identificador único.
				idSesion=Utils.getIdLlamada();
				wContext.getMessageContext().put(Constantes.VAR_ID_SESION, idSesion);
			}
			String xml=(String)wContext.getMessageContext().get(Constantes.VAR_XMLSOURCE);
			
			log = (Logger) wContext.getMessageContext().get(Constantes.VAR_LOG_APLICACION);
			if (log==null)
			{
				log=LogFactory.newLogger(pref.getModoLog(), pref.getFicheroLogApp(), idSesion);
			}
			log.debug(xml);
			context.setItem(CallContextConstants.LOG_APLICACION, log);
			context.setItem(CallContextConstants.ID_SESION, idSesion);
			context.setItem(CallContextConstants.PREFERENCIAS, pref);
			context.setItem(CallContextConstants.TEXTO_XML, xml);
			ConsultaDeudaWorker con = new ConsultaDeudaWorker(context);
			return con.solicitarConsulta(mensaje);
		}
		catch (PreferenciasException ex)
		{
			if (log!=null)
			{
				log.error("Error inesperado en la llamada al servicio Web:" + ex.getMessage(), ex);
			}
		}
		return null;
	}

}
