package es.tributasenasturias.servicios.asturcon.pagosEnte.respuestasServicio;

import es.tributasenasturias.servicios.asturcon.pagosEnte.RESPUESTAType;
import es.tributasenasturias.servicios.asturcon.pagosEnte.context.CallContext;
import es.tributasenasturias.servicios.asturcon.pagosEnte.context.IContextReader;



/**
 * Construye las respuesta de un mensaje de entrada
 * @author crubencvs
 *
 */
public class ConstructorRespuestas implements IContextReader{
	private CallContext context;
	
	private RESPUESTAType resp;
	private ConstructorResultado resultado;
	protected ConstructorRespuestas(CallContext context) throws RespuestasException
	{
		resp = new RESPUESTAType();
		resultado=RespuestasFactory.newConstructorResultado(context);
		this.context=context;
	}
	/**
	 * Construye una respuesta de finalización correcta para un mensaje de entrada.
	 */
	public RESPUESTAType getRespuestaOK()
	{
		resp.setID("RESPUESTA");
		resp.setRESULTADO(resultado.getResultadoOK());
		return resp;
	}
	/**
	 * Construye una respuesta de finalización incorrecta.
	 * @param razon. Razón del error.
	 */
	public RESPUESTAType getRespuestaError(String razon)
	{
		resp.setID("RESPUESTA");
		resp.setRESULTADO(resultado.getResultadoError(razon));
		return resp;
	}
	@Override
	public CallContext getCallContext() {
		return context;
	}

	@Override
	public void setCallContext(CallContext ctx) {
		context=ctx;
	}
}
