package es.tributasenasturias.servicios.asturcon.pagosEnte;

import es.tributasenasturias.servicios.asturcon.pagosEnte.bd.ComunicacionPagosBD;
import es.tributasenasturias.servicios.asturcon.pagosEnte.bd.DatosException;
import es.tributasenasturias.servicios.asturcon.pagosEnte.bd.DatosFactory;
import es.tributasenasturias.servicios.asturcon.pagosEnte.bd.ComunicacionPagosBD.ResultadoComunicacion;
import es.tributasenasturias.servicios.asturcon.pagosEnte.context.CallContext;
import es.tributasenasturias.servicios.asturcon.pagosEnte.context.CallContextConstants;
import es.tributasenasturias.servicios.asturcon.pagosEnte.context.ContextException;
import es.tributasenasturias.servicios.asturcon.pagosEnte.context.IContextReader;
import es.tributasenasturias.servicios.asturcon.pagosEnte.context.LogContext;
import es.tributasenasturias.servicios.asturcon.pagosEnte.respuestasServicio.ConstructorRespuestas;
import es.tributasenasturias.servicios.asturcon.pagosEnte.respuestasServicio.RespuestasException;
import es.tributasenasturias.servicios.asturcon.pagosEnte.respuestasServicio.RespuestasFactory;
import es.tributasenasturias.utils.log.Logger;

public class PagoEnteWorker implements IContextReader{
	private CallContext context;
	private Logger log;
	@SuppressWarnings("unused")
	private PagoEnteWorker() {}
	public PagoEnteWorker(CallContext context)
	{
		this.context=context;
	}
	/**
	 * Recupera el texto del XML que se pasa como carga útil del mensaje.
	 * Como precondición, se supone que se ha guardado el texto en el contexto de la llamada.
	 * @param context Contexto donde estará almacenado el texto.
	 * @return Texto del XML
	 */
	public String getTextoXml(CallContext context)
	{
		if (context==null)
		{
			return "";
		}
		String xml=(String)context.get(CallContextConstants.TEXTO_XML);
		if (log==null)
		{
			return "";
		}
		return xml;
	}
	/**
	 * Construcción de la respuesta del servicio en caso de que se haya
	 * producido un error en la creación del objeto que construye respuestas o
	 * en la creación del objeto de log. Es retorcido, pero puede pasar.
	 * 
	 * @param mensaje
	 *            Mensaje de entrada.
	 * @return
	 */
	private RESPUESTAType construirRespuestaDefault(MENSAJEIn mensaje) {
		RESPUESTAType respuesta = new RESPUESTAType();
		respuesta.setID("RESPUESTA");
		RESULTADOType resultado = new RESULTADOType();
		// Hard Coded.
		resultado.setCODIGO("0100");
		resultado
				.setDESCRIPCION("Se ha producido un error durante la recepción de pagos y anulaciones de traba.");
		return respuesta;
	}
	/**
	 * Proceso principal de esta clase. Realiza la recepción de pagos y anulaciones de traba.
	 * @param mensaje Mensaje de entrada al servicio.
	 * @return el mensaje de salida del servicio.
	 */
	public MENSAJEOut enviarPagos(MENSAJEIn mensaje)
	{
		MENSAJEOut mensajeOut = new MENSAJEOut();
		ConstructorRespuestas conResp = null;
		try
		{
			log = LogContext.getLogFromContext (context);
			conResp = RespuestasFactory.newConstructorRespuesta(context);
			log.info("Inicio de consignación de documento de pagos y anulaciones de traba en base de datos.");
			String textoMensaje = getTextoXml(context);
			if (textoMensaje!=null && !"".equals(textoMensaje))
			{
				ComunicacionPagosBD comBd = DatosFactory.newComunicacionPagosBD(context);
				comBd.comunicarPagos(textoMensaje);
				//Se comprueba el resultado.
				RESPUESTAType respuesta= new RESPUESTAType();
				if (comBd.isComunicacionValida())
				{
					ResultadoComunicacion resCom= comBd.getResultadoComunicacion();
					if (resCom.equals(ResultadoComunicacion.VALIDO))
					{
						respuesta= conResp.getRespuestaOK();
						log.info ("Recepción de pagos y anulaciones correcta.");
					}
					else if (resCom.equals(ResultadoComunicacion.NO_VALIDO))
					{
						respuesta= conResp.getRespuestaError("Error en la recepción de pagos y anulaciones.");
						log.info ("Error en la recepción de pagos y anulaciones. Información adicional:"+ comBd.getInfoResultadoComunicacion());
					}
				}
				else
				{
					respuesta = conResp.getRespuestaError(null);
					log.info ("No se han recibido correctamente los pagos y anulaciones");
				}
				mensajeOut.setRESPUESTA(respuesta);
			}
		}
		catch (RespuestasException ex) {
			// Error en la construcción de mensajes.
			log.error("Error en la construcción de mensajes en la recepción de pagos y anulaciones de traba:" + ex.getMessage(),
					ex);
			// No podemos modificar la respuesta, porque es el propio
			// constructor de respuesta el que parece fallar.
			// Tenemos que construir la salida a mano.
			RESPUESTAType respuesta = construirRespuestaDefault(mensaje);
			mensajeOut.setRESPUESTA(respuesta);
		} catch (DatosException ex) {
			log.error("Error al recibir los pagos y anulaciones en base de datos en base de datos:"
					+ ex.getMessage(), ex);
			RESPUESTAType respuesta = conResp.getRespuestaError(null);
			mensajeOut.setRESPUESTA(respuesta);
		} catch (ContextException ex) {
			//Podría no haberse creado el log.
			if (log==null)
			{
				System.err.println("AsturconPagosEnte-"
						+ this.getClass().getName()
						+ "Error en la creación de log:" + ex.getMessage());
				ex.printStackTrace();
			}
			else
			{
				log.error(ex.getMessage(),ex);
			}
			RESPUESTAType respuesta = construirRespuestaDefault(mensaje);
			mensajeOut.setRESPUESTA(respuesta);
		} catch (Exception ex) {
			if (log != null) {
				log.error("Error inesperado en la ejecución del servicio:"
						+ ex.getMessage(), ex);
			} else {
				System.err.println(ex.getMessage());
			}
			construirRespuestaDefault(mensaje);
		}
		return mensajeOut;
	}
	@Override
	public CallContext getCallContext() {
		return context;
	}

	@Override
	public void setCallContext(CallContext ctx) {
		context = ctx;
	}
}
