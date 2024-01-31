package es.tributasenasturias.servicios.asturcon.consultaDeuda.context;

import es.tributasenasturias.utils.log.Logger;

/**
 * Permite operar con el log transmitido por contexto.
 * @author crubencvs
 *
 */
public class LogContext {

	/**
	 * Recupera el log transmitido mediante contexto {@link CallContext} para una llamada.
	 * @param context Contexto.
	 * @return {@link Logger} transmitido
	 * @throws ContextException Si el contexto es nulo, o no existe log transmitido por contexto.
	 */
	public static Logger getLogFromContext (CallContext context) throws ContextException
	{
		if (context==null)
		{
			throw new ContextException ("No se puede recuperar el log de contexto porque el context es nulo.");
		}
		Logger log =(Logger) context.get(CallContextConstants.LOG_APLICACION);
		if (log==null)
		{
			throw new ContextException ("No se puede recuperar el log de contexto porque no se encuentra en el contexto");
		}
		return log;
	}
}
