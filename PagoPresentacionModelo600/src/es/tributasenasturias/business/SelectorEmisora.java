/**
 * 
 */
package es.tributasenasturias.business;

import java.rmi.RemoteException;

import es.tributasenasturias.pagopresentacionmodelo600utils.ConversorParametrosLanzador;
import es.tributasenasturias.pagopresentacionmodelo600utils.Preferencias;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPL;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLService;

/** Implementa funciones para seleccionar la emisora
 * @author crubencvs
 *
 */
public class SelectorEmisora {
	private LanzaPLService lanzaderaWS; // Servicio Web
	private LanzaPL lanzaderaPort; // Port (operaciones) a las que se llamas
	private ConversorParametrosLanzador conversor;
	private Preferencias preferencias = new Preferencias ();
	public SelectorEmisora() throws Exception
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
			throw new Exception ("Excepción al seleccionar la emisora correspondiente al modelo:" + ex.getMessage(),ex);
		}
	}
	/**
	 * Recupera la emisora que corresponde a un modelo.
	 * @param modelo {@link String} .modelo cuya emisora se quiere recuperar.
	 * @return {@link String} Emisora.
	 */
   public String getEmisoraDeModelo(String modelo) throws Exception
   {
	   String emisora;
	   conversor.setProcedimientoAlmacenado("INTERNET.emisor");
		conversor.setParametro("1", ConversorParametrosLanzador.TIPOS.Integer);//Conexión. 
		conversor.setParametro("1", ConversorParametrosLanzador.TIPOS.String); //Petición
		conversor.setParametro("USU_WEB_SAC", ConversorParametrosLanzador.TIPOS.String); //Usuario
		conversor.setParametro("33", ConversorParametrosLanzador.TIPOS.String); //Organismo
		conversor.setParametro(modelo, ConversorParametrosLanzador.TIPOS.String); //Modelo 600, que para eso es el que trata este servicio.
		conversor.setParametro("P", ConversorParametrosLanzador.TIPOS.String); //Conexión
		
		try
		{
			String resultadoEjecutarPL = Ejecuta();
			conversor.setResultado(resultadoEjecutarPL);
			String error= conversor.getNodoResultado("error");
			if (!error.equals(""))
			{
				throw new Exception ("Excepción en acceso a BD al recuperar la emisora del modelo:"+ error);
			}
			emisora = conversor.getNodoResultado("STRING_CADE");
			return emisora;
		}
		catch (RemoteException ex)
		{
			throw new Exception ("Excepción en acceso a BD al recuperar la emisora del modelo:" + ex.getMessage(),ex);
		}
   }
   private String Ejecuta() throws RemoteException
	{		
		return lanzaderaPort.executePL(
				preferencias.getEntorno(),
				conversor.Codifica(),				
				"", "", "", "");
		
		
	}
}
