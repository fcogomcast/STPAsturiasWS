package es.tributasenasturias.bd;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.Binding;
import javax.xml.ws.handler.Handler;

import es.tributasenasturias.servicios.RecepcionDocumentos.preferencias.Preferencias;



/**
 * Clase de acceso a datos.
 */
public class Datos 
{
	private final static String STRING1_CANU = "STRING1_CANU";
	private final static String STRING2_CANU = "STRING2_CANU";
	private final static String STRING_CADE = "STRING_CADE";
	
	
	private final static String ERRORNODE = "error";
	
	private stpa.services.LanzaPLService lanzaderaWS; // Servicio Web
	private stpa.services.LanzaPL lanzaderaPort; // Port (operaciones) a las que se llama
	private ConversorParametrosLanzador conversor;
	private Preferencias preferencias;
	private String errorLlamada;
	//Constantes para utilizar fuera de la clase.
	public static final String C_ESTADO_ALTA = "EstadoAlta";
	public static final String C_DESC_ALTA= "DescripcionAlta";
	
	/**
	 * Constructor
	 * @param idSesion Identificador de la sesión en la que se instancia este objeto.
	 */
	@SuppressWarnings("unchecked")
	public Datos(String idSesion) 
	{
		try
		{
			preferencias = Preferencias.getPreferencias();
			String endPointLanzador=preferencias.getEndPointLanzador();
			lanzaderaWS = new stpa.services.LanzaPLService();
			if (!"".equals(endPointLanzador))
			{
				lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
				javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort; // enlazador de protocolo para el servicio.
				bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,endPointLanzador); // Cambiamos el endpoint
				//Asociamos el log con este port.
				Binding bi = bpr.getBinding();
				List <Handler> handlerList = bi.getHandlerChain();
				if (handlerList == null)
				{
				   handlerList = new ArrayList<Handler>();
				}
				handlerList.add(new es.tributasenasturias.soap.handler.MessageClientHandler(idSesion));
				bi.setHandlerChain(handlerList);
			}
			else
			{
				lanzaderaPort =lanzaderaWS.getLanzaPLSoapPort();
				javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort; // enlazador de protocolo para el servicio.
				//Asociamos el log con este port.
				Binding bi = bpr.getBinding();
				List <Handler> handlerList = bi.getHandlerChain();
				if (handlerList == null)
				{
				   handlerList = new ArrayList<Handler>();
				}
				handlerList.add(new es.tributasenasturias.soap.handler.MessageClientHandler(idSesion));
				bi.setHandlerChain(handlerList);
			}
			conversor = new ConversorParametrosLanzador();
		}
		catch (Exception ex)
		{
			//Se ignora. En la recuperación de la instancia se comprobará si se creó bien.
		}
	}
	
	
	public Map<String,String> insertarEscritura(String codNotario, String codNotaria,
												String numProtocolo, String numProtocoloBis,
												String fechaAutorizacion, String docEscritura,
												String firmaEscritura,String origen) throws RemoteException
	{
		conversor.setProcedimientoAlmacenado(preferencias.getPAInsertarEscritura());
		conversor.setParametro(codNotario, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(codNotaria, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(numProtocolo, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(numProtocoloBis, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(fechaAutorizacion, ConversorParametrosLanzador.TIPOS.Date,"DD/MM/YYYY");
		conversor.setParametro(docEscritura, ConversorParametrosLanzador.TIPOS.CLOB);
		conversor.setParametro(firmaEscritura, ConversorParametrosLanzador.TIPOS.CLOB);
		conversor.setParametro(origen, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro("P", ConversorParametrosLanzador.TIPOS.String);
		
		String resultadoEjecutarPL = Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
		Map<String, String> resultado = new HashMap<String, String>();
		resultado.put(C_ESTADO_ALTA, conversor.getNodoResultado(STRING1_CANU));
		resultado.put(C_DESC_ALTA, conversor.getNodoResultado(STRING2_CANU));
		return resultado;
	}
	/**
	 * Método que llama al procedimiento de comprobación de permisos para el servicio.
	 * @param autorizado - el NIF/CIF de la persona cuyos permisos sobre el servicio se van a comprobar.
	 * @return - un hash con un campo indexado por la cadena "AUTORIZACION", que indica si la persona está autorizada.
	 * @throws RemoteException
	 */
	public HashMap<String,String> permisoServicio(String autorizado) throws RemoteException{
		conversor.setProcedimientoAlmacenado(preferencias.getPAPermisoServicio()/*"INTERNET.permisoServicio"*/);
		conversor.setParametro(preferencias.getCodigoPermisoServicio(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(autorizado, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro("P", ConversorParametrosLanzador.TIPOS.String);

		String resultadoEjecutarPL = Ejecuta();
		conversor.setResultado(resultadoEjecutarPL);
		this.setErrorLlamada(conversor.getNodoResultadoX(ERRORNODE));
		
		String autorizacion = conversor.getNodoResultadoX(STRING_CADE);
		HashMap<String, String> resultado = new HashMap<String, String>();
		resultado.put("AUTORIZACION", autorizacion);
		return resultado;
		
	}
	/**
	 * Ejecuta un procedimiento almacenado en la base de datos mediante el servicio lanzador.
	 * @return String con los datos de XML de la respuesta del servicio.
	 * @throws RemoteException Si se produce un error de conexión.
	 */
	private String Ejecuta() throws RemoteException
	{
		return lanzaderaPort.executePL(
				preferencias.getEsquemaBaseDatos(),
				conversor.codifica(),
				"", "", "", "");
	}
	/**
	 * Método que devuelve el error en la ultima llamada a un procedimiento de esta clase.
	 * Los errores en llamada son devueltos en formato XML, este procedimiento devuelve el error
	 * extraído en formato de cadena.
	 * @return
	 */
	public String getErrorLlamada() {
		return errorLlamada;
	}

	private void  setErrorLlamada(String errorLlamada) {
		this.errorLlamada = errorLlamada;
	}
}
