package es.tributasenasturias.validacion;

import java.rmi.RemoteException;
import java.util.HashMap;
import es.tributasenasturias.Exceptions.DatosException;
//import es.tributasenasturias.utils.Log.ILoggerAplicacion;
import es.tributasenasturias.pagopresentacionmodelo600utils.ConversorParametrosLanzador;
import es.tributasenasturias.pagopresentacionmodelo600utils.Preferencias;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPL;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLService;

public class Datos 
{
	private final String STRING_CADE = "STRING_CADE";
	private final String FECHA1_CANU = "FECHA1_CANU";
	private final String FECHA2_CANU = "FECHA2_CANU";
	@SuppressWarnings("unused")
	private final String STRING1_CANU = "STRING1_CANU";
	private final String STRING2_CANU = "STRING2_CANU";
	@SuppressWarnings("unused")
	private final String STRING3_CANU = "STRING3_CANU";
	@SuppressWarnings("unused")
	private final String STRING4_CANU = "STRING4_CANU";
	@SuppressWarnings("unused")
	private final String STRING5_CANU = "STRING5_CANU";
	private final String NUME1_CANU = "NUME1_CANU";
	private final String ERRORNODE = "error";
	
	private LanzaPLService lanzaderaWS; // Servicio Web
	private LanzaPL lanzaderaPort; // Port (operaciones) a las que se llamas
	private ConversorParametrosLanzador conversor;
	private Preferencias preferencias = new Preferencias ();
	private String errorLlamada;
	//private ILoggerAplicacion logger;
		
	public Datos() throws DatosException
	{										
		try
		{			
			preferencias.CargarPreferencias();
			String endPointLanzador=preferencias.getEndpointLanzador();			
			lanzaderaWS = new LanzaPLService();
			if (!endPointLanzador.equals(""))
			{								
				lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
				javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort; // enlazador de protocolo para el servicio.
				bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,endPointLanzador); // Cambiamos el endpoint
			}
			else
			{
				lanzaderaPort =lanzaderaWS.getLanzaPLSoapPort();
			}
			conversor = new ConversorParametrosLanzador();
		}
		catch (Exception ex)
		{
			throw new DatosException (ex.getMessage());
		}
	}

	/**
	 * Método que llama al procedimiento de comprobación de permisos para el servicio.
	 * @param autorizado - el NIF/CIF de la persona cuyos permisos sobre el servicio se van a comprobar.
	 * @return - un hash con un campo indexado por la cadena "AUTORIZACION", que indica si la persona está autorizada.
	 * @throws RemoteException
	 */
	public HashMap<String,String> permisoServicio(String autorizado) throws DatosException
	{
		conversor.setProcedimientoAlmacenado("INTERNET.permisoServicio");
		conversor.setParametro("ANCERT_CAL", ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(autorizado, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro("P", ConversorParametrosLanzador.TIPOS.String);
		
		try
		{
			String resultadoEjecutarPL = Ejecuta();
			
			conversor.setResultado(resultadoEjecutarPL);
			this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
			
			String autorizacion = conversor.getNodoResultado(STRING_CADE);
			HashMap<String, String> resultado = new HashMap<String, String>();
			resultado.put("AUTORIZACION", autorizacion);
			return resultado;
		}
		catch (RemoteException ex)
		{
			throw new DatosException (ex.getMessage());
		}
	}
	/**
	 * Método que llama al procedimiento de comprobación de deuda.
	 * @param  nif - el NIF/CIF de la persona cuya deuda se va a comprobar.
	 * @param  nombre - el nombre de la persona cuya deuda se va a comprobar.
	 * @param  tipo - 
	 * @param  motivo - 
	 * @return - un hash con los datos de resultado de la consulta a la base de datos.
	 * @throws RemoteException
	 */
	public HashMap<String,String> consultaCertificado(String nif, String nombre,
													  String tipo, String motivo) throws DatosException
	{
		conversor.setProcedimientoAlmacenado("INTERNET_CERTIFICADOS.SW_consultaCertificado");
		conversor.setParametro("33", ConversorParametrosLanzador.TIPOS.Integer); //Provincia
		conversor.setParametro(nif, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(nombre, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(tipo, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(motivo, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro("P", ConversorParametrosLanzador.TIPOS.String);
		
		try
		{
			String resultadoEjecutarPL = Ejecuta();
			conversor.setResultado(resultadoEjecutarPL);
			this.setErrorLlamada(conversor.getNodoResultado(ERRORNODE));
			
			String res = conversor.getNodoResultado(STRING2_CANU);
			String identificacion = conversor.getNodoResultado(NUME1_CANU);
			String fechaGeneracion = conversor.getNodoResultado(FECHA1_CANU);
			String fechaValidez = conversor.getNodoResultado(FECHA2_CANU);
			HashMap<String, String> resultado = new HashMap<String, String>();
			resultado.put("RESULTADO", res);
			resultado.put("IDENTIFICACION", identificacion);
			resultado.put("FECHA_GENERACION", fechaGeneracion);
			resultado.put("FECHA_VALIDEZ", fechaValidez);
			return resultado;
		}
		catch (RemoteException ex)
		{
			throw new DatosException (ex.getMessage());
		}
	}

	private String Ejecuta() throws RemoteException
	{		
		return lanzaderaPort.executePL(
				preferencias.getEntorno(),
				conversor.Codifica(),				
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

