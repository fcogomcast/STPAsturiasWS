package es.tributasenasturias.servicios.accesocertificado.log;

import es.tributasenasturias.servicios.accesocertificado.contextoLlamadas.CallContext;
import es.tributasenasturias.servicios.accesocertificado.contextoLlamadas.CallContextConstants;
import es.tributasenasturias.servicios.accesocertificado.exceptions.STPAException;

/**
 * Clase que permite instanciar la clase de log según condiciones propias de este proyecto.
 * Existe porque se recupera el logger a partir del contexto de llamada, si no, se podría llamar
 * directamente a las funciones de {@link es.tributasenasturias.utils.log.log.LogFactory}
 * @author crubencvs
 *
 */
public class LogFactory {

	/**
	 * Recupera un objeto {@link Logger} para poder realizar log, según el nivel de log que se especifica. 
	 * @param nivelLog Nivel de log
	 * @param ficheroLog nombre del fichero de log
	 * @return {@link Logger} con el que se podrá imprimir en log.
	 */
	public static es.tributasenasturias.utils.log.Logger newLogger(String nivelLog, String ficheroLog)
	{
		return es.tributasenasturias.utils.log.LogFactory.newLogger(nivelLog, ficheroLog);
	}
	/**
	 * Recupera un objeto {@link Logger} para poder realizar log, según el nivel de log que se especifica 
	 * y con la información extra que se indica y que se imprimirá en cada línea.
	 * @param nivelLog Nivel de log como una cadena de texto.
	 * @param ficheroLog Nombre del fichero de log
	 * @param infoExtra Información extra a imprimir en cada línea.
	 * @return {@link Logger} para imprimir en log.
	 */
	public static es.tributasenasturias.utils.log.Logger newLogger(String nivelLog, String ficheroLog,String infoExtra)
	{
		return es.tributasenasturias.utils.log.LogFactory.newLogger(nivelLog, ficheroLog,infoExtra);
	}
	/**
	 * Permite recuperar el logger a partir del contexto de llamada, si existe.
	 * @param context
	 * @return
	 */
	public static es.tributasenasturias.utils.log.Logger getLoggerFromContext(CallContext context) throws STPAException
	{
		if (context==null)
		{
			throw new STPAException (LogFactory.class.getName()+".No se ha indicado contexto de llamada en la creación de log");
		}else if (context.get(CallContextConstants.LOG)==null)
		{
			throw new STPAException (LogFactory.class.getName()+".No hay guardado un objeto log en el contexto de llamada.");
		}
		else
		{
			return (es.tributasenasturias.utils.log.Logger) context.get(CallContextConstants.LOG);
		}
	}
}
