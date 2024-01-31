/**
 * 
 */
package es.tributasenasturias.servicios.asturcon.pagosEnte.preferencias;

import es.tributasenasturias.servicios.asturcon.pagosEnte.context.CallContext;
import es.tributasenasturias.servicios.asturcon.pagosEnte.context.CallContextConstants;


/** Implementa una factoría de preferencias. Se podrá crear una nueva instancia o bien recuperar
 *  la que exista en un contexto de llamada.
 * @author crubencvs
 *
 */
public class PreferenciasFactory {
	private PreferenciasFactory(){};
	public static Preferencias newInstance() throws PreferenciasException
	{
		return new Preferencias();
	}
	/**
	 * Recupera las preferencias de contexto
	 * @param context Contexto de llamada del que recuperar las preferencias
	 * @return Objeto que contiene las preferencias {@link Preferencias}
	 * @throws PreferenciasException En caso de no poder recuperar las preferencias del contexto que se pasa.
	 */
	public static Preferencias getPreferenciasContexto(CallContext context) throws PreferenciasException
	{
		if (context==null)
		{
			throw new PreferenciasException ("Imposible recuperar preferencias del contexto de la llamada. El contexto es nulo");
		}
		Preferencias pref=(Preferencias)context.get(CallContextConstants.PREFERENCIAS);
		if (pref==null)
		{
			throw new PreferenciasException ("Imposible recuperar preferencias del contexto de la llamada."); 
		}
		return pref;
		
	}
}
