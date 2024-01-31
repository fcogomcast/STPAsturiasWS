package es.tributasenasturias.bd;

import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.CallContextManager;

/**
 * Clase para crear objetos de comunicación con base de datos.
 * @author crubencvs
 *
 */
public final class BDFactory {
	private BDFactory(){}
	/**
	 * Recupera un nuevo objeto de gestión de peticiones a base de datos.
	 * @return
	 */
	public static GestorLlamadasBD newGestorLlamadasBD(CallContext context)
	{
		return new GestorLlamadasBD(context);
	}
	
	/**
	 * Recupera un nuevo objeto de consulta de pagos a base de datos.
	 * @param context
	 * @return
	 */
	public static ConsultaPagoBD newConsultaPagoBD(CallContext context)
	{
		return new ConsultaPagoBD(context);
	}
	/**
	 * Recupera un nuevo objeto de gestión de peticiones a base de datos
	 * @param pref {@link Preferencias} de esta invocación
	 * @param idSesion Identificador de esta invocación
	 * @param log Logger de esta invocación
	 * @return
	 */
	public static GestorLlamadasBD newGestorLlamadasBD(Preferencias pref, String idSesion, Logger log){
		CallContext callContext= CallContextManager.newCallContext();
		callContext.setItem(CallContextConstants.ID_SESION, idSesion);
		callContext.setItem(CallContextConstants.PREFERENCIAS, pref);
		callContext.setItem(CallContextConstants.LOG_APLICACION, log);
		return new GestorLlamadasBD(callContext);
	}
}
