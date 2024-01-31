package es.tributasenasturias.servicios.asturcon.consultaDeuda.respuestasServicio;

import es.tributasenasturias.servicios.asturcon.consultaDeuda.EJECUCIONRESPUESTAType;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.EJECUCIONType;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.MENSAJEIN;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.RESPUESTAType;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.context.CallContext;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.context.IContextReader;

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
	
	private void setIDEntrada(RESPUESTAType respuesta,MENSAJEIN mensajeEntrada)
	{
		EJECUCIONType ejEntrada = mensajeEntrada.getEJECUCION();
		EJECUCIONRESPUESTAType ejec= new EJECUCIONRESPUESTAType();
		ejec.setFECHA(ejEntrada.getFECHA());
		ejec.setHORA(ejEntrada.getHORA());
		ejec.setID(ejEntrada.getID());
		ejec.setTIPOEJE(ejEntrada.getTIPOEJE());
		respuesta.setID(ejEntrada.getID());
		respuesta.setEJECUCION(ejec);
	}
	/**
	 * Construye una respuesta de finalización correcta para un mensaje de entrada.
	 * @param mensajeEntrada
	 */
	public RESPUESTAType getRespuestaOK(MENSAJEIN mensajeEntrada)
	{
		
		setIDEntrada(resp,mensajeEntrada);
		resp.setRESULTADO(resultado.getResultadoOK());
		return resp;
	}
	/**
	 * Construye una respuesta de finalización correcta pero consulta duplicada.
	 * @param mensajeEntrada
	 */
	public RESPUESTAType getRespuestaPetiDuplicada(MENSAJEIN mensajeEntrada)
	{
		
		setIDEntrada(resp,mensajeEntrada);
		resp.setRESULTADO(resultado.getResultadoDuplicada());
		return resp;
	}
	/**
	 * Construye una respuesta de finalización incorrecta.
	 * @param mensajeEntrada
	 */
	public RESPUESTAType getRespuestaError(MENSAJEIN mensajeEntrada, String razon)
	{
		setIDEntrada(resp,mensajeEntrada);
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
