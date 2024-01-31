package es.tributasenasturias.servicios.asturcon.consultaDeuda.respuestasServicio;

import es.tributasenasturias.servicios.asturcon.consultaDeuda.RESPUESTAType;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.RESULTADOType;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.context.CallContext;
/**
 * Construye objetos para devolver respuestas del servicio.
 * @author crubencvs
 *
 */
public class RespuestasFactory {
	
	private RespuestasFactory(){};
	/**
	 * Devuelve una instancia  {@link ConstructorResultado} que permite construir un objeto {@link RESULTADOType}
	 * @param context Contexto de la sesión.
	 * @return instancia CertificadoValidator
	 */
	public static ConstructorResultado newConstructorResultado(CallContext context) throws RespuestasException
	{
		return new ConstructorResultado(context);
	}
	/**
	 * Devuelve una instancia {@link ConstructorRespuestas} que permite construir un objeto {@link RESPUESTAType}
	 * @param context Contexto de la sesión.
	 * @return instancia CertificadoValidator
	 */
	public static ConstructorRespuestas newConstructorRespuesta(CallContext context) throws RespuestasException
	{
		return new ConstructorRespuestas(context);
	}
}
