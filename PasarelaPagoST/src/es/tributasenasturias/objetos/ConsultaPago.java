/**
 * 
 */
package es.tributasenasturias.objetos;




import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import es.tributasenasturias.pasarelas.SincronizadorBDEntidadRemota;
import es.tributasenasturias.utils.EstadosPago;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.validacion.ValidacionFactory;
import es.tributasenasturias.validacion.ValidadorOperacion;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.IContextReader;
import es.tributasenasturias.webservices.parametros.ResultadoConsulta;
import es.tributasenasturias.bd.BDFactory;
import es.tributasenasturias.bd.ConsultaPagoBD;
import es.tributasenasturias.dao.DatosProceso;
import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.exceptions.PasarelaPagoException;

/**
 * Clase que implementa la funcionalidad de consulta de pagos.
 * Utilizada directamente desde la clase PasarelaPagoST.
 *
 */
public class ConsultaPago implements IContextReader{
	CallContext context;
	DatosProceso datosProceso;
	private Logger logger=null;

	
	/**
	 * Inicializa la variable de resultado.
	 * @param datosProceso Datos del proceso de consulta.
	 * @return
	 */
	private ResultadoConsulta inicializarResultado(DatosProceso datosProceso)
	{
		ResultadoConsulta result= new ResultadoConsulta();
		DatosEntradaServicio peticionServicio = datosProceso.getPeticionServicio();
		result.getPeticion().setReferencia(peticionServicio.getReferencia());
		result.getPeticion().setCliente(peticionServicio.getCliente());
		result.getPeticion().setAplicacion(peticionServicio.getAplicacion());
		result.getPeticion().setNumero_unico(peticionServicio.getNumeroUnico());
		result.getPeticion().setIdentificacion(peticionServicio.getIdentificacion());
		result.getPeticion().setNumero_autoliquidacion(peticionServicio.getJustificante());
		result.getPeticion().setEmisora(peticionServicio.getEmisora());
		result.getPeticion().setMac(peticionServicio.getMac());
		result.getPeticion().setLibre(peticionServicio.getLibre());
		return result;
	}
	
	/**
	 * Constructor
	 * @param datosProceso
	 */
	private ConsultaPago (DatosEntradaServicio peticion)
	{
		this.datosProceso = new DatosProceso(peticion); 
		
	}
	/**
	 * Constructor
	 * @param datosProceso Datos de la petición de pago.
	 * @param context  Contexto de la llamada.
	 * @throws PasarelaPagoException
	 */
	public ConsultaPago(DatosEntradaServicio peticion, CallContext context) throws PasarelaPagoException{
		
		this(peticion);
		this.context=context;
		Preferencias preferencias;
		logger = (Logger) context.get(CallContextConstants.LOG_APLICACION);
		preferencias=(Preferencias) context.get(CallContextConstants.PREFERENCIAS);
		if (logger==null || preferencias==null)
		{
			throw new PasarelaPagoException ("No se ha inicializado correctamente el contexto de llamada en la construcción de "+ConsultaPago.class.getName());
		}
	}
	/**
	 * Método que realiza la consulta del pago. <br/>
	 * Las operaciones que realiza son:<br/>
	 * 1.- Comprueba si en nuestro sistema está registrado el pago.<br/>
	 * 2.- Si está registrado correctamente, se indica que el tributo ya está pagado.<br/>
	 * 3.- Si no está registrado, o lo está pero figura como no pagado, se consulta su estado a través del servicio de Entidad de pago.<br/>
	 * 4.- Con el resultado que devuelve el servicio, se actualiza el estado del pago en el sistema y se devuelve el mismo.<br/>
	 * @return Resultado de la operación, en un objeto {@link Resultado}<br/>
	 */
	/**
	 * @return
	 */
	public ResultadoConsulta ejecutar()
	{
		ResultadoConsulta result = inicializarResultado(datosProceso);
		long iniTime=0;
		long endTime=0;
		iniTime=System.currentTimeMillis();
		logger.debug("INICIO DE LLAMADA CONSULTA");
		try
		{	
			
			//Validamos datos de entrada ante todo.
			logger.info("Validación de mensaje de entrada");
			ValidadorOperacion validador= ValidacionFactory.newValidadorOperacionConsulta(context);
			validador.validar(this.datosProceso);
			if (validador.isValido())
			{
				logger.info ("Validación finalizada. Mensaje válido.");
				logger.debug("Consulta del estado de pago en BD.");
				//Consulta del estado de pago en Pate
				ConsultaPagoBD cons = BDFactory.newConsultaPagoBD(context);
				//GestorLlamadasBD ges = BDFactory.newGestorLlamadasBD(context);
				//ResultadoLlamadaBD resConsultaPate = ges.consultaBD(datosProceso.getTipoLlamada(),peticion.getEmisora(),peticion.getJustificante(),peticion.getIdentificacion(),peticion.getReferencia());
				ConsultaPagoBD.Resultado resConsulta = cons.consultarPagoBD(datosProceso);
				if (!resConsulta.isError())
				{
					logger.debug ("Pasarela de pago recuperada de base de datos:"+ datosProceso.getPasarelaPagoPeticion());
					String estado = datosProceso.getEstado();
					logger.debug("Estado de pago:" + estado);
					if (estado.equals(EstadosPago.PAGADO.getValor()))
					{
						result.getRespuesta().setMessage(Mensajes.getTributoPagado());
						//Se actualiza el justificante, por si se ha consultado en base al número de aplicación y servicio.
						result.getPeticion().setNumero_autoliquidacion(datosProceso.getJustificante());
						result.getRespuesta().setOperacion(datosProceso.getNrc());
						result.getRespuesta().setFechaPago(formateaFechaSalida(datosProceso.getFechaPago()));
						logger.info("Tributo pagado.");
						
					}
					else if (estado.equals(EstadosPago.ANULADO.getValor()))
					{
						result.getRespuesta().setMessage(Mensajes.getTributoAnulado());
						logger.info ("Tributo anulado.");
					}
					else if (estado.equals(EstadosPago.INICIADO.getValor()) || estado.equals(EstadosPago.ANULACION_COMENZADA.getValor())||
							 estado.equals(EstadosPago.ERROR.getValor()))
					{
						logger.info ("Tributo con estado " + estado + " en base de datos, se consulta en entidad.");
						//Comprobación con Entidad externa.
						SincronizadorBDEntidadRemota sinc = new SincronizadorBDEntidadRemota(context);
						//sinc.sincronizar(idPasarelaBD,datosProceso);
						sinc.sincronizar(datosProceso.getPasarelaPagoPeticion(),datosProceso);
						if (!sinc.huboError())
						{
							EstadosPago estadoSinc = sinc.getEstado();
							switch (estadoSinc)
							{
							case PAGADO:
							case ANULADO:
								result.getRespuesta().setOperacion(sinc.getNrc());
								result.getRespuesta().setFechaPago(sinc.getFechaPago());
								if (estadoSinc.equals(EstadosPago.PAGADO))
								{
									result.getRespuesta().setMessage(Mensajes.getTributoPagado());
								}
								else
								{
									result.getRespuesta().setMessage(Mensajes.getTributoAnulado());
								}
								break;
							case NO_PAGADO:
								result.getRespuesta().setMessage(Mensajes.getTributoNoPagado());
								logger.info("Tributo no pagado.");
								break;
							} //Otro error debería ser excepción.
						}
						else
						{
							if (!"".equals(sinc.getTextoError()))
							{
								result.getRespuesta().setError(sinc.getCodError());
								result.getRespuesta().setResultado(sinc.getTextoError());
							}
							else
							{
								result.getRespuesta().setMessage(sinc.getCodError(), sinc.getTextoError());
							}
							logger.error("Error en sincronización de estado de pago con entidad remota:"+result.getRespuesta().getMensajeCompuesto());
						}
					}
					else
					{
						logger.error(Mensajes.getErrorBD() + ".-Error. Estado del objeto inconsistente en la base de datos.");
						result.getRespuesta().setMessage(Mensajes.getErrorBD());
					}
				}
				else
				{
					result.getRespuesta().setMessage(resConsulta.getCodigo(), resConsulta.getMessage());
					logger.error("Error en consulta pate:"+result.getRespuesta().getMensajeCompuesto());
				}
			}
			else
			{
				logger.debug ("Validación finalizada. Mensaje no válido.");
				//Depuración de mensajes de validación.
				for (String m:validador.getMensajesValidacion())
				{
					logger.debug(m);
				}
				result.getRespuesta().setMessage(validador.getCodigoError());
				logger.error(result.getRespuesta().getMensajeCompuesto());
			}
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			logger.trace(ex.getStackTrace());
			result.getRespuesta().setMessage(Mensajes.getFatalError());
			logger.error(result.getRespuesta().getMensajeCompuesto());
		}
		endTime=System.currentTimeMillis();
		logger.debug("FINAL CONSULTA EN " + Long.toString(endTime-iniTime) + " MS RETORNANDO COMO RESULTADO -> "+result.getRespuesta().getError());
		return result;
	}
	
	@Override
	public CallContext getCallContext() {
		return context;
	}
	@Override
	public void setCallContext(CallContext ctx) {
		context = ctx;
	}

	/**
	 * Formatea la fecha para salida por pantalla
	 * @param fechaEntrada
	 * @return
	 */
	private String formateaFechaSalida(String fechaEntrada)
	{
		String fecha;
		if (fechaEntrada!=null)
		{
			try
			{
				Date fmtFecha=new SimpleDateFormat("yyyyMMdd").parse(fechaEntrada);
				fecha =new SimpleDateFormat("yyyy-MM-dd").format(fmtFecha);
			}
			catch (ParseException e)
			{
				fecha=null;
			}
		}
		else
		{
			fecha=null;
		}
		return fecha;
	}
	
}
