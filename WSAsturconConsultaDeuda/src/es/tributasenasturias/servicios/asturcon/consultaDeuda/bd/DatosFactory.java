package es.tributasenasturias.servicios.asturcon.consultaDeuda.bd;

import es.tributasenasturias.servicios.asturcon.consultaDeuda.bd.PeticionConsultaBD.DatosConexion;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.context.CallContext;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.context.CallContextConstants;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.preferencias.Preferencias;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.preferencias.PreferenciasException;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.preferencias.PreferenciasFactory;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.soap.SoapClientHandler;
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
	 * Devuelve una instancia  {@link PeticionConsultaBD} que permite registrar
	 * una petici�n de consulta en base de datos.
	 * Como precondici�n se asume que en el contexto se ha cargado una copia de las preferencias en forma de {@link Preferencias}
	 * @param context Contexto de la sesi�n.
	 * @param xml Xml de petici�n de consulta
	 * @return instancia PeticionConsultaBD
	 */
	public static PeticionConsultaBD newPeticionConsultaBD(CallContext context) throws DatosException
	{
		try
		{
			Preferencias pref=PreferenciasFactory.getPreferenciasContexto(context);
			DatosConexion datos= new DatosConexion();
			datos.setEndpointLanzador(pref.getEndPointLanzador());
			datos.setEsquema(pref.getEsquemaBaseDatos());
			datos.setProcedimiento(pref.getPAConsultaDeuda());
			datos.setManejadorMensajes(new SoapClientHandler(getIdSesionContexto(context)));
			return new PeticionConsultaBD(datos);
		}
		catch (PreferenciasException ex)
		{
			throw new DatosException ("Error al construir el objeto para consignar la petici�n de consulta en B.D:"+ex.getMessage(),ex);
		}
	}
}
