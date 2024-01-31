package es.tributasenasturias.servicios.asturcon.consultaDeuda;


import es.tributasenasturias.servicios.asturcon.consultaDeuda.bd.DatosException;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.bd.DatosFactory;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.bd.PeticionConsultaBD;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.bd.PeticionConsultaBD.ResultadosPeticion;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.context.CallContext;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.context.CallContextConstants;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.context.ContextException;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.context.IContextReader;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.context.LogContext;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.respuestasServicio.ConstructorRespuestas;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.respuestasServicio.RespuestasException;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.respuestasServicio.RespuestasFactory;
import es.tributasenasturias.utils.log.Logger;

public class ConsultaDeudaWorker implements IContextReader {
	private CallContext context;
	private Logger log;

	public ConsultaDeudaWorker(CallContext context) {
		this.context = context;
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
	private RESPUESTAType construirRespuestaDefault(MENSAJEIN mensaje) {
		RESPUESTAType respuesta = new RESPUESTAType();
		EJECUCIONType ejecucion = mensaje.getEJECUCION();
		respuesta.setID(ejecucion.getID());
		EJECUCIONRESPUESTAType ejec = new EJECUCIONRESPUESTAType();
		ejec.setFECHA(ejecucion.getFECHA());
		ejec.setHORA(ejecucion.getHORA());
		ejec.setID(ejecucion.getID());
		ejec.setTIPOEJE(ejecucion.getTIPOEJE());
		RESULTADOType resultado = new RESULTADOType();
		// Hard Coded.
		resultado.setCODIGO("0100");
		resultado
				.setDESCRIPCION("Se ha producido un error durante la petición");
		return respuesta;
	}

	public MENSAJEOUT solicitarConsulta(MENSAJEIN mensaje) {
		MENSAJEOUT mensajeOut = new MENSAJEOUT();
		ConstructorRespuestas conResp = null;
		try {
			log = LogContext.getLogFromContext (context);
			conResp = RespuestasFactory.newConstructorRespuesta(context);
			log.info("Inicio de consignación de petición en base de datos.");
			String textoMensaje = getTextoXml(context);
			if (textoMensaje!=null && !textoMensaje.equals(""))
			{
				PeticionConsultaBD peticion = DatosFactory
						.newPeticionConsultaBD(context);
				peticion.solicitarConsulta(textoMensaje);
				// Comprobamos la respuesta.
				RESPUESTAType respuesta;
				if (peticion.isPeticionValida()) {
					if (peticion.getResultadoPeticion().equals(ResultadosPeticion.VALIDA)){
						respuesta = conResp.getRespuestaOK(mensaje);
						log.info("Consignación en base de datos correcta.");
					}
					else if (peticion.getResultadoPeticion().equals(ResultadosPeticion.DUPLICADA))
					{
						respuesta = conResp.getRespuestaPetiDuplicada(mensaje);
						log.info("Petición duplicada. Ya existe una petición con el mismo Id.");
					}
					else
					{
						respuesta = conResp.getRespuestaError(mensaje,"No se ha podido comprobar si la petición ya existía en nuestro sistema.");
						log.error("El resultado de la consignación de consulta en base de datos no es ninguno de los esperados");
						log.error("Información adicional:"+ peticion.getInfoResultadoPeticion());
					}
				} else {
					respuesta = conResp.getRespuestaError(mensaje, null);
					log.info("La petición no se ha podido consignar:"
							+ peticion.getInfoResultadoPeticion());
				}
				mensajeOut.setRESPUESTA(respuesta);
			}
			else
			{
				mensajeOut.setRESPUESTA(conResp.getRespuestaError(mensaje, "Imposible recuperar el texto del mensaje."));
			}
		} catch (RespuestasException ex) {
			// Error en la construcción de mensajes.
			log.error("Error en la solicitud de consulta:" + ex.getMessage(),
					ex);
			// No podemos modificar la respuesta, porque es el propio
			// constructor de respuesta el que parece fallar.
			// Tenemos que construir la salida a mano.
			RESPUESTAType respuesta = construirRespuestaDefault(mensaje);
			mensajeOut.setRESPUESTA(respuesta);
		} catch (DatosException ex) {
			log.error("Error al consignar la petición en base de datos:"
					+ ex.getMessage(), ex);
			RESPUESTAType respuesta = conResp.getRespuestaError(mensaje, null);
			mensajeOut.setRESPUESTA(respuesta);
		} catch (ContextException ex) {
			//Podría no haberse creado el log.
			if (log==null)
			{
				System.err.println("AsturconConsultaDeuda-"
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
