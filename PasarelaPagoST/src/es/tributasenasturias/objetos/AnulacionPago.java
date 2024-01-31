/**
 * 
 */
package es.tributasenasturias.objetos;

import es.tributasenasturias.pasarelas.PasarelasFactory;
import es.tributasenasturias.pasarelas.comunicacion.ComunicadorPasarela;
import es.tributasenasturias.pasarelas.comunicacion.DatosComunicacion;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.utils.EstadosPago;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.validacion.ValidacionFactory;
import es.tributasenasturias.validacion.ValidadorOperacion;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.IContextReader;
import es.tributasenasturias.webservices.parametros.ResultadoAnulacion;
import es.tributasenasturias.bd.BDFactory;
import es.tributasenasturias.bd.GestorLlamadasBD;
import es.tributasenasturias.bd.ResultadoLlamadaBD;
import es.tributasenasturias.dao.DatosProceso;
import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.exceptions.PasarelaPagoException;

/**
 * Clase que implementa la funcionalidad de la anulación de pago.
 * Invocada directamente desde la clase PasarelaPago.
 *
 */
public class AnulacionPago implements IContextReader {

	CallContext context;
	private Preferencias preferencias;
	private Logger logger=null;
	DatosProceso datosProceso;
	
	/**
	 * Constructor de objeto, permite un contexto de llamada para recibir los datos de logger y preferencias.
	 * @param peticion Datos de la petición al método.
	 * @param context Contexto de la llamada.
	 * @throws PasarelaPagoException
	 */
	protected AnulacionPago(DatosEntradaServicio peticion, CallContext context) throws PasarelaPagoException{
		
		this(peticion);
		logger = new Logger();
		this.context=context;
		logger = (Logger) context.get(CallContextConstants.LOG_APLICACION);
		this.preferencias=(Preferencias) context.get(CallContextConstants.PREFERENCIAS);
		if (logger==null || preferencias==null)
		{
			throw new PasarelaPagoException ("No se ha inicializado correctamente el contexto de llamada.");
		}
	}

	private AnulacionPago (DatosEntradaServicio peticion)
	{
		this.datosProceso = new DatosProceso(peticion);
	}
	/**
	 * Inicializa la variable de resultado.
	 * @param datosProceso Datos de la petición.
	 * @return
	 */
	private ResultadoAnulacion inicializarResultado(DatosProceso datosProceso)
	{
		ResultadoAnulacion result= new ResultadoAnulacion();
		DatosEntradaServicio peticionServicio = datosProceso.getPeticionServicio();
		result.getPeticion().setReferencia(peticionServicio.getReferencia());
		result.getPeticion().setCliente(peticionServicio.getCliente());
		result.getPeticion().setAplicacion(peticionServicio.getAplicacion());
		result.getPeticion().setModelo(peticionServicio.getModelo());
		result.getPeticion().setIdentificacion(peticionServicio.getIdentificacion());
		result.getPeticion().setNumero_autoliquidacion(peticionServicio.getJustificante());
		result.getPeticion().setEmisora(peticionServicio.getEmisora());
		result.getPeticion().setImporte(peticionServicio.getImporte());
		result.getPeticion().setNif(peticionServicio.getNifContribuyente());
		result.getPeticion().setNombreContribuyente(peticionServicio.getNombreContribuyente());
		result.getPeticion().setFecha_devengo(peticionServicio.getFechaDevengo());
		result.getPeticion().setDato_especifico(peticionServicio.getDatoEspecifico());
		result.getPeticion().setExpediente(peticionServicio.getExpediente());
		result.getPeticion().setNif_operante(peticionServicio.getNifOperante());
		result.getPeticion().setTarjeta(peticionServicio.getTarjeta());
		result.getPeticion().setCcc(peticionServicio.getCcc());
		result.getPeticion().setFecha_caducidad(peticionServicio.getFechaCaducidadTarjeta());
		result.getPeticion().setNumero_peticion(peticionServicio.getNumeroPeticion());
		result.getPeticion().setNumero_unico(peticionServicio.getNumeroUnico());
		result.getPeticion().setMac(peticionServicio.getMac());
		result.getPeticion().setLibre(peticionServicio.getLibre());
		return result;
	}
	/**
	 * Método que implementa la funcionalidad de la anulación de pago.<br/>
	 * Realiza las siguientes operaciones:
	 * 1.- Comprueba el estado del pago en nuestro sistema.<br/>
	 * 2.- Si el estado de pago indica que aún no se ha pagado, se realiza una consulta de estado de pago contra el servicio de Entidad de pago, actualizando el estado.<br/>
	 * 3.- Si el estado de pago indica que se ha pagado, se procede a anular el pago.<br/>
	 * 4.- Si no figura como pagado, se indica este hecho al cliente del servicio.<br/>
	 * @return Resultado de la operación, de tipo {@link Resultado}
	 * @throws Exception
	 */
	public ResultadoAnulacion ejecutar() throws PasarelaPagoException
	{
		ResultadoAnulacion result = new ResultadoAnulacion();
		long iniTime=0;
		long endTime=0;
		iniTime=System.currentTimeMillis();
		logger.debug("INICIO DE LLAMADA ANULACIÓN");
		//return null;
		// Se recupera el estado del pago en la base de datos.
		try
		{	
			result=inicializarResultado(datosProceso);
			logger.info("Validación de datos de entrada.");
			//Validamos datos de entrada ante todo.
			ValidadorOperacion validador= ValidacionFactory.newValidadorOperacionAnulacion(context);
			validador.validar(this.datosProceso);
			if (validador.isValido())
			{
				logger.info ("Datos de entrada válidos.");
				//Inicializar petición
				logger.info ("Inicio de petición en base de datos.");
				GestorLlamadasBD gestorBD = BDFactory.newGestorLlamadasBD(context);
				ResultadoLlamadaBD resInicioAnulacion= gestorBD.inicioAnulacion(datosProceso);
				if (!resInicioAnulacion.isError())
				{
					String idPasarela=resInicioAnulacion.getDatosPagoBD().getPasarelaPago();
					//Si estuviera vacío, podría ser por pago antiguo. Se selecciona en base a los datos de entrada.
					//Aunque esto sea incorrecto por intentar anular en una pasarela que no corresponde,
					//es la única manera de tratarlo. La pasarela remota ya dirá que no existe el pago, o algo similar.
					if (idPasarela==null || "".equals(idPasarela))
					{
						idPasarela=PasarelasFactory.newSelectorPasarelaPago(context).seleccionarPasarela(datosProceso.getPeticionServicio().getTarjeta(), datosProceso.getPeticionServicio().getCcc());
					}
					ComunicadorPasarela comm=PasarelasFactory.newSelectorComunicadorPasarela(context).seleccionarComunicadorPasarela(idPasarela,datosProceso);
					datosProceso.setFechaPago(resInicioAnulacion.getDatosPagoBD().getFechaPago());
					datosProceso.setPasarelaPagoPeticion(resInicioAnulacion.getDatosPagoBD().getPasarelaPago());
					datosProceso.setNumeroOperacion(resInicioAnulacion.getDatosPagoBD().getNumeroOperacion());
					datosProceso.setNifContribuyente(resInicioAnulacion.getDatosPagoBD().getNifContribuyente());
					datosProceso.setFechaDevengo(resInicioAnulacion.getDatosPagoBD().getFechaDevengo());
					datosProceso.setDatoEspecifico(resInicioAnulacion.getDatosPagoBD().getDatoEspecifico());
					datosProceso.setExpediente(resInicioAnulacion.getDatosPagoBD().getExpediente());
					datosProceso.setNifOperante(resInicioAnulacion.getDatosPagoBD().getNifOperante());
					datosProceso.setImporte(resInicioAnulacion.getDatosPagoBD().getImporte());
					datosProceso.setFechaDevengo(resInicioAnulacion.getDatosPagoBD().getFechaDevengo());
					datosProceso.setJustificante(datosProceso.getPeticionServicio().getJustificante());
					String estado = resInicioAnulacion.getDatosPagoBD().getEstado();
					datosProceso.setEstado(estado);
					//Tratamos el estado encontrado en el inicio de anulación.
					tratarEstadosAnulacion(comm,estado,datosProceso,result, true);
				}
				else
				{
					result.getRespuesta().setMessage(resInicioAnulacion.getCodError(), resInicioAnulacion.getTextoError());
					logger.error("Error en inicio de anulación:"+result.getRespuesta().getMensajeCompuesto());
				}
			}
			else
			{
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
		logger.debug("FINAL ANULACIÓN EN " + Long.toString(endTime-iniTime) + " MS RETORNANDO COMO RESULTADO -> "+result.getRespuesta().getError());
		return result;
	}

	
	/**
	 * Sincroniza el estado de la base de datos con el de la entidad remota.
	 * @param datosProceso
	 * @param result
	 * @return true si la sincronización es correcta.
	 * @throws PasarelaPagoException
	 */
	private boolean sincronizarBDEntidadRemota(ComunicadorPasarela comm,DatosProceso datosProceso, ResultadoAnulacion result) throws PasarelaPagoException
	{
		boolean sincronizado=true;
		//La pasarela se escoge al principio del proceso, y no se vuelve a tratar.
		comm.realizarConsulta();
		DatosComunicacion datosComunicacion= comm.getDatosComunicacion();
		datosProceso.setPasarelaPagoPeticion(comm.getIdPasarela()); // El identificador de pasarela por el que iremos.
		if (!datosComunicacion.isError()) // La consulta termina correctamente.
		{
			logger.debug("Consulta en entidad finalizada con estado:" + datosComunicacion.getEstadoPago());
			logger.debug ("Resultado de la comunicación con entidad remota:" + datosComunicacion.getResultadoComunicacion());
			logger.debug ("Resultado remoto:" + datosComunicacion.getResultadoDescripcionRemoto());
			logger.debug ("Actualizamos.");
			if (datosComunicacion.getEstadoPago().equals(EstadosPago.PAGADO) && 
					(datosComunicacion.getNumeroOperacion()==null  || datosComunicacion.getNrc()==null || datosComunicacion.getFechaPago()==null))
			{
				result.getRespuesta().setMessage(Mensajes.getErrorActualizacionBD());
				sincronizado=false;
			}
			else 
			{
				// Actualizamos en PATE si hay algo que actualizar.
				switch (datosComunicacion.getEstadoPago())
				{
				case PAGADO:
				case ANULADO:
					GestorLlamadasBD ges = BDFactory.newGestorLlamadasBD(context);
					//CRUBENCVS No se actualiza el operante ni la fecha de devengo, porque en este caso los datos con los que se terminó la operación en la entidad remota son los que hay en la tabla.
					//CRUBENCVS 42479. Se añade la fecha de anulación vacía, ya que sólo se está pasando en plataforma de pago UniversalPay
					ResultadoLlamadaBD resActualiza = ges.actualizarBD (null,datosComunicacion.getEstadoPago().getValor(),datosProceso.getPeticionServicio().getJustificante(),datosProceso.getPeticionServicio().getIdentificacion(), datosProceso.getPeticionServicio().getReferencia(),datosComunicacion.getNumeroOperacion(),datosComunicacion.getFechaPago(),datosComunicacion.getNrc(), "","",datosComunicacion.getResultadoComunicacion(),null);
					if (resActualiza.isError())
					{
						result.getRespuesta().setMessage(resActualiza.getCodError(), resActualiza.getTextoError());
						sincronizado=false;
					}
					break;
				}
			}
		}
		else
		{
			result.getRespuesta().setError(datosComunicacion.getCodigoError());
			result.getRespuesta().setResultado(datosComunicacion.getTextoError());
			sincronizado=false;
		}
		return sincronizado;
	}
	
	/**
	 * Tratamiento del estado de tributo anulado.
	 * @param result
	 */
	private void tratarTributoAnulado(ResultadoAnulacion result) {
		result.getRespuesta().setMessage(Mensajes.getTributoNoPagado());
	}
	/**
	 * Tratamiento del estado de tributo pagado.
	 * @param result
	 */
	private void tratarTributoNoPagado(ResultadoAnulacion result) throws PasarelaPagoException{
		result.getRespuesta().setMessage(Mensajes.getErrorEstadoPATE());
	}
	/**
	 * Tratamiento del caso de datos inconsistentes.
	 * @param result
	 */
	private void tratarDatosInconsistentes(ResultadoAnulacion result) {
		DatosEntradaServicio peticion = datosProceso.getPeticionServicio();
		String origen = peticion.getOrigen();
		if (origen.equals(Constantes.getOrigenServicioWeb()) && peticion.getModalidad().equals(Constantes.getModalidadAutoliquidacion()))
		{
			logger.error(Mensajes.getErrorDatosInconsistentes()+".-El pago telematico del servicio '"+peticion.getAplicacion()+"' y numero unico '"+peticion.getNumeroUnico()+"' está dado de alta en la base de datos, con datos diferentes a los de la peticion actual!");
		}
		else if ((origen.equals(Constantes.getOrigenPortal()) || origen.equals(Constantes.getOrigenS1())) &&
				peticion.getModalidad().equals(Constantes.getModalidadAutoliquidacion()))
		{
			logger.error(Mensajes.getErrorDatosInconsistentes()+".-El pago telematico del justificante '"+peticion.getJustificante()+"' está dado de alta en la base de datos, con datos diferentes a los de la peticion actual!");
		}
		else if ((origen.equals(Constantes.getOrigenPortal()) || origen.equals(Constantes.getOrigenS1()))&& 
				peticion.getModalidad().equals(Constantes.getModalidadLiquidacion()))
		{
		logger.error(Mensajes.getErrorDatosInconsistentes()+".-El pago telematico de identificación '"+peticion.getIdentificacion()+"' y referencia '"+peticion.getReferencia()+"' está dado de alta en la base de datos, con datos diferentes a los de la peticion actual!");
		}
		result.getRespuesta().setMessage(Mensajes.getErrorDatosInconsistentes());
	}
	
	/**
	 * Tratamos los posibles estados que se dan en anulación.
	 * @param estado Estado en que se encuentra el pago en la base de datos.
	 * @param datosProceso Datos del proceso. Incluirán los datos de petición y datos que se han ido recuperando de base de datos y otro proceso.
	 * @param result Objeto resultado de anulacion
	 * @param sincronizarConEntidadRemota Indica que si el estado en base de datos no permite saber si el pago está efectuado o no, se sincronizará con la entidad de pago remota. 
	 * @throws PasarelaPagoException
	 */
	private void tratarEstadosAnulacion (ComunicadorPasarela comm,String estado, DatosProceso datosProceso,ResultadoAnulacion result, boolean sincronizarConEntidadRemota) throws PasarelaPagoException
	{
		DatosEntradaServicio peticion = datosProceso.getPeticionServicio();
		GestorLlamadasBD ges = BDFactory.newGestorLlamadasBD(context);
		String justificante="";
		if (sincronizarConEntidadRemota && (estado.equals(EstadosPago.INICIADO.getValor())|| estado.equals(EstadosPago.ANULACION_COMENZADA.getValor()) ||
				estado.equals(EstadosPago.ERROR.getValor())))
		{
				logger.info ("Estado:" + estado + " provoca consulta en entidad remota.");
				//Se sincronia, para saber el estado en la entidad remota.
				if ( sincronizarBDEntidadRemota(comm,datosProceso,result))
				{
					//Se vuelve a consultar y se comprobará entonces el nuevo estado.
					ResultadoLlamadaBD resConsulta = ges.consultaBD(datosProceso.getTipoLlamada(),peticion.getEmisora(),peticion.getJustificante(), peticion.getIdentificacion(), peticion.getReferencia());
					if (!resConsulta.isError())
					{
						estado = resConsulta.getDatosPagoBD().getEstado();
						logger.info ("Después de consulta en entidad remota, el nuevo estado es:" + estado);
					}
					datosProceso.setFechaPago(resConsulta.getDatosPagoBD().getFechaPago());
					datosProceso.setPasarelaPagoPeticion(resConsulta.getDatosPagoBD().getPasarelaPago());
					datosProceso.setNumeroOperacion(resConsulta.getDatosPagoBD().getNumeroOperacion());
					datosProceso.setEstado(estado);
					justificante = peticion.getJustificante();
					if (justificante!=null && !"".equals(justificante))
					{
						result.getPeticion().setNumero_autoliquidacion(justificante);
					}
					//Se trata el nuevo estado, si hubiera.
					tratarEstadosAnulacion (comm,estado, datosProceso, result,false); //Si el estado vuelve a ser indefinido, no se trata ya.
					return;
				}
				else
				{
					//En principio no se continúa.
					logger.error(Mensajes.getErrorActualizacionBD()+".-No se puede verificar el estado de pago en entidad remota.");
					result.getRespuesta().setMessage(Mensajes.getErrorConsulta());
					return;
				}
		}
		/*else if ((estado.equals(EstadosPago.INICIADO.getValor())|| estado.equals(EstadosPago.ANULACION_COMENZADA.getValor()) ||
				estado.equals(EstadosPago.ERROR.getValor()))) //Continúa sin conocerse el estado, es la segunda vez que se pregunta.
		{
			result.getRespuesta().setMessage(Mensajes.getErrorEstadoPATE());
		}*/
		if (estado.equals(EstadosPago.DIFERENTE.getValor()))
		{
			tratarDatosInconsistentes(result);
		}
		else if (estado.equals(EstadosPago.ANULADO.getValor()))
		{
			tratarTributoAnulado(result);
		}
		else if (!estado.equals(EstadosPago.PAGADO.getValor())) //No tiene un estado que permita la anulación
		{
			tratarTributoNoPagado(result);
		}
		else if (estado.equals(EstadosPago.PAGADO.getValor())) 
		{
			logger.info ("Se intenta la anulación en la entidad remota.");
			comm.realizarAnulacion();
			DatosComunicacion datosAnulacion= comm.getDatosComunicacion();
			if (!datosAnulacion.isError())
			{
				if (datosAnulacion.getNumeroOperacion()==null || datosAnulacion.getFechaPago()==null|| datosAnulacion.getNrc()==null)
				{
					logger.error(Mensajes.getErrorActualizacionBD()+".-Error al actualizar la petición en base de datos: se han recibido datos vacíos desde Entidad remota [Nrc-Fecha operación-número operación]");
					result.getRespuesta().setMessage(Mensajes.getErrorActualizacionBD());
				}
				else 
				{
					logger.info ("Tributo anulado en entidad remota.");
					//Anulado.
					estado=EstadosPago.ANULADO.getValor();
					result.getRespuesta().setMessage(Mensajes.getOk());
					result.getRespuesta().setOperacion(datosAnulacion.getNrc());
					//Actualizamos con los datos con los que ha ido a la entidad remota
					//CRUBENCVS 42479. Se añade la fecha de anulación vacía, ya que sólo se está pasando en plataforma de pago UniversalPay
					ResultadoLlamadaBD resActualizar=ges.actualizarBD(null,estado, peticion.getJustificante(), peticion.getIdentificacion(),peticion.getReferencia(), datosAnulacion.getNumeroOperacion(), datosAnulacion.getFechaPago(), datosAnulacion.getNrc(), datosProceso.getPeticionServicio().getNifOperante(),datosProceso.getPeticionServicio().getFechaDevengo(),datosAnulacion.getResultadoComunicacion(),null);
					if (resActualizar.isError())
					{
						logger.error(Mensajes.getErrorActualizacionBD()+".-Error al actualizar la petición en base de datos:"+resActualizar.getCodError());
						result.getRespuesta().setMessage(Mensajes.getErrorActualizacionBD());
					}
				}
			}
			else
			{
				logger.info ("Tributo no anulado en entidad remota:"+datosAnulacion.getResultadoDescripcionRemoto());
				//CRUBENCVS Actualizamos con los datos con los que ha ido a la entidad 
				//CRUBENCVS 42479. Se añade la fecha de anulación vacía, ya que sólo se está pasando en plataforma de pago UniversalPay
				ResultadoLlamadaBD resActualizar=ges.actualizarBD(null,EstadosPago.ANULACION_COMENZADA.getValor(), peticion.getJustificante(), peticion.getIdentificacion(),peticion.getReferencia(), "", "", "", datosProceso.getPeticionServicio().getNifOperante(),datosProceso.getPeticionServicio().getFechaDevengo(),datosAnulacion.getResultadoComunicacion(), null);
				if (resActualizar.isError())
				{
					logger.error(Mensajes.getErrorActualizacionBD()+".-Error al actualizar la petición en base de datos:"+resActualizar.getCodError());
					result.getRespuesta().setMessage(Mensajes.getErrorActualizacionBD());
				}
				result.getRespuesta().setError(datosAnulacion.getCodigoError());
				result.getRespuesta().setResultado(datosAnulacion.getTextoError());
				logger.error(result.getRespuesta().getMensajeCompuesto()+".-Error al realizar la anulación del pago en entidad remota:"+ datosAnulacion.getResultadoDescripcionRemoto());
			}
		}
	}
	
	@Override
	public CallContext getCallContext() {
		return context;
	}

	
	@Override
	public void setCallContext(CallContext ctx) {
		context= ctx;
	}
}
