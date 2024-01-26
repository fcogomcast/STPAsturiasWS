package es.tributasenasturias.seguridad.servicio;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * Indica si el iniciador de comunicaci�n con el servicio tiene permisos de ejecuci�n sobre el mismo.
 * Para saberlo, se utiliza el certificado que debe venir en la firma.
 * @author crubencvs
 *
 */
public class PermisoEjecucion{
	private stpa.services.LanzaPLService lanzaderaWS; // Servicio Web
	private stpa.services.LanzaPL lanzaderaPort; // Port (operaciones) a las que se llamas
	private ConversorParametrosLanzador conversor;
	private String endpointLanzador;
	private String procedimiento;
	private String esquema;
	private SOAPHandler<SOAPMessageContext> manejadorMensajes=null;
	//Utilizado para pasar los datos de conexi�n en el constructor, y no cambiar el interfaz
	//si se a�aden datos.
	public static class DatosConexion
	{
		private String endpointLanzador;
		private String procedimiento;
		private String esquema;
		private SOAPHandler<SOAPMessageContext> manejadorMensajes;
		public String getEndpointLanzador() {
			return endpointLanzador;
		}
		public void setEndpointLanzador(String endpointLanzador) {
			this.endpointLanzador = endpointLanzador;
		}
		public String getProcedimiento() {
			return procedimiento;
		}
		public void setProcedimiento(String procedimiento) {
			this.procedimiento = procedimiento;
		}
		public String getEsquema() {
			return esquema;
		}
		public void setEsquema(String esquema) {
			this.esquema = esquema;
		}
		public void setManejadorMensajes(
				SOAPHandler<SOAPMessageContext> manejadorMensajes) {
			this.manejadorMensajes = manejadorMensajes;
		}
		public SOAPHandler<SOAPMessageContext> getManejadorMensajes() {
			return manejadorMensajes;
		}
	}
	
	private stpa.services.LanzaPLService instanciarServicio()
	{
		//Se modifica de forma din�mica la localizaci�n del WSDL, que debe estar en el
		//jar donde se define LanzaPLService, y debe estar situado en 
		//META-INF/wsdls/PXLanzador.
		//Esto no es peor que lo que genera el cliente por defecto, que tambi�n lo mete,
		//pero al menos esto funciona bien.
		//Desactivado, s�lo tiene sentido si se trata de una librer�a jar.
		//WebServiceClient ann = stpa.services.LanzaPLService.class.getAnnotation(WebServiceClient.class);
		//URL urlWsdl = stpa.services.LanzaPLService.class.getResource("/META-INF/wsdls/PXLanzador.wsdl");
		//return new stpa.services.LanzaPLService (urlWsdl,new QName(ann.targetNamespace(), ann.name()));
		return new stpa.services.LanzaPLService();
	}
	/**
	 * Modifica el endpoint del servicio.
	 * @param endpoint String conteniendo el endpoint del servicio.
	 * @throws SeguridadException Si no puede establecer el endpoint.
	 */
	private void changeEndpoint(String endpoint)
	{
		BindingProvider bpr=(BindingProvider) lanzaderaPort;
		bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,endpoint);
	}
	/**
	 * Modifica el manejador de mensajes SOAP que utilizar� esta clase en las comunicaciones con 
	 * servicios SOAP.
	 * @param manejador {@link SOAPHandler} para manejar los mensajes.
	 */
	@SuppressWarnings("unchecked")
	private void changeHandler(SOAPHandler<SOAPMessageContext> manejador)
	{
		javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort; // enlazador de protocolo para el servicio.
		if (manejador!=null)
		{
			Binding bi = bpr.getBinding();
			List <Handler> handlerList = bi.getHandlerChain();
			if (handlerList == null)
			{
			   handlerList = new ArrayList<Handler>();
			}
			handlerList.add(manejador);
			bi.setHandlerChain(handlerList);
		}	
	}
	
	/**
	 * Construye el objeto de comprobaci�n de permiso de ejecuci�n.
	 * @param datos Datos de la conexi�n {@link DatosConexion} con base de datos para comprobar los permisos. 
	 * @throws SeguridadException Si se produce un error al construir el objeto.
	 */
	protected PermisoEjecucion(DatosConexion datos) throws SeguridadException
	{
		try
		{
			if (datos==null)
			{
				throw new SeguridadException ("No se han pasado datos de conexi�n para comprobar el permiso de ejecuci�n.");
			}
			this.endpointLanzador=datos.getEndpointLanzador();
			procedimiento = datos.procedimiento;
			esquema=datos.esquema;
			this.manejadorMensajes=datos.getManejadorMensajes();
			conversor=new ConversorParametrosLanzador();
			lanzaderaWS=instanciarServicio();
			lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
			//Modificamos la localizaci�n del Wsdl, para que compruebe el nuevo endpoint.
			changeEndpoint (endpointLanzador);
			//Modificamos el manejador de mensajes utilizado.
			changeHandler(manejadorMensajes);
		}
		catch (ParserConfigurationException e) {
			throw new SeguridadException ("Error al construir el objeto de comprobaci�n en Base de Datos de los permisos de ejecuci�n del servicio:"+ e.getMessage(),e);
		}
	}
	private String Ejecuta() throws RemoteException
	{
		return lanzaderaPort.executePL(
				this.esquema,
				conversor.codifica(),
				"", "", "", "");
	}
	/**
	 * M�todo que llama al procedimiento de comprobaci�n de permisos para el servicio.
	 * Recupera datos sobre los permisos del servicio, como si tiene permisos y qu� usuario de tributas tiene.
	 * @param aliasServicio  El alias de servicio cuyos permisos se comprobar�n.
	 * @param cifNif - el NIF/CIF de la persona cuyos permisos sobre el servicio se van a comprobar.
	 * @return - un valor que indicar� el retorno del servicio, si ha sido correcto o no.
	 * @throws SystemException
	 */
	public DatosPermisosServicio permisoServicio(String aliasServicio,String cifNif) throws SeguridadException
	{
		conversor.setProcedimientoAlmacenado(this.procedimiento);
		conversor.setParametro(aliasServicio, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(cifNif, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro("P", ConversorParametrosLanzador.TIPOS.String);
		try
		{
			String errorLlamada="";
			String resultadoEjecutarPL = Ejecuta();
			
			conversor.setResultado(resultadoEjecutarPL);
			errorLlamada= conversor.getNodoResultado("error");
			if (!errorLlamada.equals(""))
			{
				throw new SeguridadException ("Error producido al consultar en Base de Datos el permiso de ejecuci�n del servicio "+aliasServicio+":"+ errorLlamada);
			}
			String autorizacion = conversor.getNodoResultado("STRING_CADE");
			String usuario = conversor.getNodoResultado("STRING1_CANU");
			String password = conversor.getNodoResultado("STRING2_CANU");
			DatosPermisosServicio resultado = SeguridadFactory.newDatosPermisosServicio();
			if (autorizacion.equals("01"))
			{
				resultado.setAutorizacion(DatosPermisosServicio.AutorizacionServicio.NO_AUTORIZADO);
			}
			else if (autorizacion.equals("02"))
			{
				resultado.setAutorizacion(DatosPermisosServicio.AutorizacionServicio.ERROR);
			}
			else
			{
				resultado.setAutorizacion(DatosPermisosServicio.AutorizacionServicio.AUTORIZADO);
				resultado.setUsuarioTributas(usuario);
				resultado.setPasswordTributas(password);
			}
			return resultado;
		}
		catch (RemoteException ex)
		{
			throw new SeguridadException (ex.getMessage(),ex);
		}
		catch (Exception ex)
		{
			throw new SeguridadException (ex.getMessage(),ex);
		}
	}
	/**
	 * @return el endpoint de lanzador
	 */
	public String getEndpointLanzador() {
		return endpointLanzador;
	}
	/**Establece el Endpoint de lanzador. 
	 * Se utilizar� para modificar en tiempo de ejecuci�n el endpoint del lanzador,
	 * @param endpointLanzador the endpointLanzador to set
	 */
	public void setEndpointLanzador (String endpointLanzador){
		this.endpointLanzador = endpointLanzador;
		changeEndpoint(endpointLanzador);
	}
	/**
	 * @return the procedimiento
	 */
	public String getProcedimiento() {
		return procedimiento;
	}
	/**
	 * @param procedimiento the procedimiento to set
	 */
	public void setProcedimiento(String procedimiento) {
		this.procedimiento = procedimiento;
	}
	/**
	 * @return the esquema
	 */
	public String getEsquema() {
		return esquema;
	}
	/**
	 * @param esquema the esquema to set
	 */
	public void setEsquema(String esquema) {
		this.esquema = esquema;
	}
	/**
	 * @return the manejadorMensajes
	 */
	public SOAPHandler<SOAPMessageContext> getManejadorMensajes() {
		return manejadorMensajes;
	}
	/**
	 * Establece el manejador de mensajes para los mensajes contra otros servicios
	 * utilizados por esta clase.
	 * @param manejadorMensajes the manejadorMensajes to set
	 */
	public void setManejadorMensajes(
			SOAPHandler<SOAPMessageContext> manejadorMensajes) {
		this.manejadorMensajes = manejadorMensajes;
		changeHandler(manejadorMensajes);
	}
}
