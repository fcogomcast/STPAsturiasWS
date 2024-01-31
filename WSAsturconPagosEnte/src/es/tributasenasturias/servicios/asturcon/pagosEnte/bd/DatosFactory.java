package es.tributasenasturias.servicios.asturcon.pagosEnte.bd;

import es.tributasenasturias.servicios.asturcon.pagosEnte.bd.ComunicacionPagosBD.DatosConexion;
import es.tributasenasturias.servicios.asturcon.pagosEnte.context.CallContext;
import es.tributasenasturias.servicios.asturcon.pagosEnte.context.CallContextConstants;
import es.tributasenasturias.servicios.asturcon.pagosEnte.preferencias.Preferencias;
import es.tributasenasturias.servicios.asturcon.pagosEnte.preferencias.PreferenciasException;
import es.tributasenasturias.servicios.asturcon.pagosEnte.preferencias.PreferenciasFactory;
import es.tributasenasturias.servicios.asturcon.pagosEnte.soap.SoapClientHandler;


/**
 * Construye objetos para devolver respuestas del servicio.
 * @author crubencvs
 *
 */
public class DatosFactory {
	
	private DatosFactory(){};
	private static String getIdSesionContexto(CallContext context)
	{
		String idSesion="";
		if (context!=null)
		{
			idSesion = (String)context.get(CallContextConstants.ID_SESION);
		}
		return idSesion;
	}
	/**
	 * Devuelve una instancia  {@link ComunicacionPagosBD} que permite registrar
	 * una la comunicación de pagos y anulaciones de traba en la base de datos.
	 * Como precondición se asume que en el contexto se ha cargado una copia de las preferencias en forma de {@link Preferencias}
	 * @param context Contexto de la sesión.
	 * @param xml Xml de pagos a Ente
	 * @return instancia ComunicacionPagosBD
	 */
	public static ComunicacionPagosBD newComunicacionPagosBD(CallContext context) throws DatosException
	{
		try
		{
			Preferencias pref=PreferenciasFactory.getPreferenciasContexto(context);
			DatosConexion datos= new DatosConexion();
			datos.setEndpointLanzador(pref.getEndPointLanzador());
			datos.setEsquema(pref.getEsquemaBaseDatos());
			datos.setProcedimiento(pref.getPAPagosEnte());
			datos.setManejadorMensajes(new SoapClientHandler(getIdSesionContexto(context)));
			return new ComunicacionPagosBD(datos);
		}
		catch (PreferenciasException ex)
		{
			throw new DatosException ("Error al construir el objeto para comunicar los datos de pagos y anulaciones de traba en B.D:"+ex.getMessage(),ex);
		}
	}
}
