package es.tributasenasturias.webservices.Certificados.bd;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.ws.handler.Handler;

import es.tributasenasturias.webservices.Certificados.Exceptions.DatosException;
import es.tributasenasturias.webservices.Certificados.impl.Flags;
import es.tributasenasturias.webservices.Certificados.sesion.GestorIdLlamada;
import es.tributasenasturias.webservices.Certificados.soap.handler.ClientLogHandler;
import es.tributasenasturias.webservices.Certificados.utils.Log.GenericAppLogger;
import es.tributasenasturias.webservices.Certificados.utils.Log.TributasLogger;
import es.tributasenasturias.webservices.Certificados.utils.Preferencias.Preferencias;
public class Datos 
{
	private final static String STRING_CADE = "STRING_CADE";
	private final static String FECHA1_CANU = "FECHA1_CANU";
	private final static String FECHA2_CANU = "FECHA2_CANU";
	@SuppressWarnings("unused")
	private final static String STRING1_CANU = "STRING1_CANU";
	private final static String STRING2_CANU = "STRING2_CANU";
	private final static String STRING3_CANU = "STRING3_CANU";
	private final static String STRING4_CANU = "STRING4_CANU";
	@SuppressWarnings("unused")
	private final static String STRING5_CANU = "STRING5_CANU";
	private final static String NUME1_CANU = "NUME1_CANU";
	private final static String ERRORNODE = "error";
	
	private stpa.services.LanzaPLService lanzaderaWS; // Servicio Web
	private stpa.services.LanzaPL lanzaderaPort; // Port (operaciones) a las que se llamas
	private ConversorParametrosLanzador conversor;
	private Preferencias preferencias;
	private String errorLlamada;
	private GenericAppLogger logger;
	@SuppressWarnings("unchecked")
	public Datos() throws DatosException
	{
		try
		{
			preferencias = Preferencias.getPreferencias();
			String endPointLanzador=preferencias.getEndPointLanzador();
			lanzaderaWS = new stpa.services.LanzaPLService();
			logger= new TributasLogger();
			javax.xml.ws.BindingProvider bpr;
			if (!endPointLanzador.equals(""))
			{
				logger.debug ("Se utiliza el endpoint de lanzadera: " + endPointLanzador);
				lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
				bpr = (javax.xml.ws.BindingProvider) lanzaderaPort; // enlazador de protocolo para el servicio.
				bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,endPointLanzador); // Cambiamos el endpoint
			}
			else
			{
				lanzaderaPort =lanzaderaWS.getLanzaPLSoapPort();
				bpr = (javax.xml.ws.BindingProvider) lanzaderaPort; // enlazador de protocolo para el servicio.
				logger.debug ("Se utiliza el endpoint de lanzadera por defecto: " + bpr.getRequestContext().get (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
			}
			List<Handler> handlers=bpr.getBinding().getHandlerChain();
			if (handlers==null)
			{
				handlers=new ArrayList<Handler>();
			}
			handlers.add(new ClientLogHandler());
			bpr.getBinding().setHandlerChain(handlers);
			conversor = new ConversorParametrosLanzador();
		}
		catch (Exception ex)
		{
			throw new DatosException ("Problema al construir el objeto de datos.",ex);
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
		conversor.setProcedimientoAlmacenado(preferencias.getPaPermisos());
		conversor.setParametro(preferencias.getAliasServicio(), ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(autorizado, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro("P", ConversorParametrosLanzador.TIPOS.String);
		
		try
		{
			String resultadoEjecutarPL = Ejecuta();
			logger.debug("Resultado: "+resultadoEjecutarPL);
			conversor.setResultado(resultadoEjecutarPL);
			this.setErrorLlamada(conversor.getNodoResultadoX(ERRORNODE));
			
			String autorizacion = conversor.getNodoResultadoX(STRING_CADE);
			HashMap<String, String> resultado = new HashMap<String, String>();
			resultado.put("AUTORIZACION", autorizacion);
			//Recogemos el tipo en el nodo STRING1_CANU
			String tipo = conversor.getNodoResultadoX(STRING1_CANU);
			resultado.put ("TIPO",tipo);
			return resultado;
		}
		catch (RemoteException ex)
		{
			throw new DatosException ("Problema al consultar el permiso de servicio en BD.",ex);
		}
	}
	/**
	 * Método que llama al procedimiento de comprobación de deuda.
	 * @param  nif - el NIF/CIF de la persona cuya deuda se va a comprobar.
	 * @param  nombre - el nombre de la persona cuya deuda se va a comprobar.
	 * @param  tipo - 
	 * @param  motivo - 
	 * @param  flags Flags que puedan interesar para la consulta de certificado.
	 * @return - un hash con los datos de resultado de la consulta a la base de datos.
	 * @throws RemoteException
	 */
	public HashMap<String,String> consultaCertificado(String nif, String nombre,
													  String tipo, String motivo,
													  Flags flags) throws DatosException
	{
		conversor.setProcedimientoAlmacenado(preferencias.getPaConsulta());
		conversor.setParametro("33", ConversorParametrosLanzador.TIPOS.Integer); //Provincia
		conversor.setParametro(nif, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(nombre, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(tipo, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro(motivo, ConversorParametrosLanzador.TIPOS.String);
		conversor.setParametro("P", ConversorParametrosLanzador.TIPOS.String);
		//Se incluye también el id de llamada, para seguir los errores
		conversor.setParametro(GestorIdLlamada.getIdLlamada(), ConversorParametrosLanzador.TIPOS.String);
		if (flags.isRedSARA())
		{
			conversor.setParametro("S", ConversorParametrosLanzador.TIPOS.String);
		}
		else
		{
			conversor.setParametro("N", ConversorParametrosLanzador.TIPOS.String);
		}
		
		try
		{
			String resultadoEjecutarPL = Ejecuta();
			logger.debug("Resultado: "+resultadoEjecutarPL);
			conversor.setResultado(resultadoEjecutarPL);
			this.setErrorLlamada(conversor.getNodoResultadoX(ERRORNODE));
			
			String res = conversor.getNodoResultadoX(STRING2_CANU);
			String nifConsultado = conversor.getNodoResultadoX(STRING3_CANU);
			String nombreConsultado = conversor.getNodoResultadoX(STRING4_CANU);
			String identificacion = conversor.getNodoResultadoX(NUME1_CANU);
			String fechaGeneracion = conversor.getNodoResultadoX(FECHA1_CANU);
			String fechaValidez = conversor.getNodoResultadoX(FECHA2_CANU);
			HashMap<String, String> resultado = new HashMap<String, String>();
			resultado.put("RESULTADO", res);
			resultado.put("IDENTIFICACION", identificacion);
			resultado.put("NIF", nifConsultado);
			resultado.put("NOMBRE",nombreConsultado);
			resultado.put("FECHA_GENERACION", fechaGeneracion);
			resultado.put("FECHA_VALIDEZ", fechaValidez);
			return resultado;
		}
		catch (RemoteException ex)
		{
			throw new DatosException ("Problema en la consulta de certificado en BD.",ex);
		}
	}

	private String Ejecuta() throws RemoteException
	{
		try {
			logger.debug("Ejecutando llamada al servicio lanzadera para acceso a base de datos: "+conversor.Codifica());
			return lanzaderaPort.executePL(
					preferencias.getEsquemaBaseDatos(),
					conversor.Codifica(),
					"", "", "", "");
		} catch (DatosException e) {
			throw new RemoteException ("Error al ejecutar el procedimiento almacenado. Causa: "+ e.getMessage() + "-"+e.getError(), e);
		}
		
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

