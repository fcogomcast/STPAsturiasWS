/**
 * 
 */
package es.tributasenasturias.objetos;
import es.tributasenasturias.bd.BDFactory;

import es.tributasenasturias.bd.GestorLlamadasBD;
import es.tributasenasturias.bd.ResultadoLlamadaBD;
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
import es.tributasenasturias.webservices.parametros.ResultadoPeticion;
import es.tributasenasturias.dao.DatosProceso;
import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.exceptions.PasarelaPagoException;

/**
 * Clase que implementa la funcionalidad de la petición de pago.
 * Llamada directamente desde la operación {@link Peticion} de la clase principal del servicio.
 * 
 */
public class PeticionPago implements IContextReader{

	CallContext context;

	DatosProceso datosProceso;
	private Logger logger=null;
	
	@Override
	public CallContext getCallContext() {
		return context;
	}
	@Override
	public void setCallContext(CallContext ctx) {
		this.context=ctx;
	}
	
	private PeticionPago(DatosEntradaServicio peticion){
		this.datosProceso = new DatosProceso(peticion);
		//TODO: esto se tiene que eliminar, existe sólo porque en BBVA 
		//se necesitan todos los datos del pago también para la consulta. Debería modificarse para devolverlos
		//en iniciarPeticion, pero ahora no se puede porque machacaría ciertos datos.
		//Lo corregiré más adelante.
		//*************Eliminar desde aquí***************
		datosProceso.setNifContribuyente(peticion.getNifContribuyente());
		datosProceso.setNifOperante(peticion.getNifOperante());
		datosProceso.setDatoEspecifico(peticion.getDatoEspecifico());
		datosProceso.setImporte(peticion.getImporte());
		datosProceso.setExpediente(peticion.getExpediente());
		//*********Hasta aquí************
	}
	/**
	 * Inicializa la variable de resultado.
	 * @param datosProceso Datos de la petición.
	 * @return
	 */
	private ResultadoPeticion inicializarResultado(DatosProceso peticion)
	{
		ResultadoPeticion result= new ResultadoPeticion();
		DatosEntradaServicio peticionServicio = peticion.getPeticionServicio();
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
	 * Constructor a utilizar en el objeto Factory.
	 * @param datosProceso {@link DatosEntradaServicio}, datos de la petición.
	 * @param context Contexto de llamada.
	 */
	protected PeticionPago (DatosEntradaServicio peticion, CallContext context) throws PasarelaPagoException
	{
		this (peticion);
		this.context=context;
		Preferencias preferencias;
		logger = (Logger) context.get(CallContextConstants.LOG_APLICACION);
		preferencias=(Preferencias) context.get(CallContextConstants.PREFERENCIAS);
		if (logger==null || preferencias==null)
		{
			throw new PasarelaPagoException ("No se ha inicializado correctamente el contexto de llamada.");
		}
	}
	public ResultadoPeticion ejecutar()
	{
		ResultadoPeticion result= inicializarResultado (this.datosProceso);
		long start=System.currentTimeMillis();
		logger.info("INICIO PETICIÓN DE PAGO.");
		try
		{
			logger.info("Validación de datos de entrada.");
			//Validamos datos de entrada ante todo.
			ValidadorOperacion validador= ValidacionFactory.newValidadorOperacionPago(context);
			validador.validar(this.datosProceso);
			if (validador.isValido())
			{
				logger.info ("Datos de entrada válidos.");
				//Inicializar petición
				logger.info ("Inicio de petición en base de datos.");
				logger.debug ("Selección de pasarela.");
				String idPasarela= PasarelasFactory.newSelectorPasarelaPago(context).seleccionarPasarela(datosProceso.getPeticionServicio().getTarjeta(), datosProceso.getPeticionServicio().getCcc());
				logger.info ("Pasarela seleccionada según datos de entrada:"+idPasarela);
				GestorLlamadasBD gestorBD = BDFactory.newGestorLlamadasBD(context);
				ResultadoLlamadaBD resPeticionPate= gestorBD.iniciarPeticionBD(idPasarela,datosProceso);
				if (!resPeticionPate.isError())
				{
					String estado = resPeticionPate.getDatosPagoBD().getEstado();
					logger.debug("Estado de petición en base de datos:"+ estado);
					//Puede habernos dado el justificante, si es de tipo Servicio Web, Autoliquidación.
					String justificante = resPeticionPate.getDatosPagoBD().getJustificante();
					result.getPeticion().setNumero_autoliquidacion(justificante);
					datosProceso.setJustificante(justificante);
					datosProceso.setEstado(estado);
					String idPasarelaBD =resPeticionPate.getDatosPagoBD().getPasarelaPago();
					//TODO: Si la pasarela está vacía puede ser un pago iniciado antes del cambio.
					//¿Lo pasamos por la pasarela por defecto?.
					datosProceso.setPasarelaPagoPeticion(idPasarela);
					logger.info("Pasarela recuperada de base de datos:"+idPasarelaBD);
					logger.debug ("Tratamos el estado.");
					tratarEstadosPago (idPasarela, idPasarelaBD,estado,datosProceso,result, true);
				}
				else
				{
					result.getRespuesta().setMessage(resPeticionPate.getCodError(), resPeticionPate.getTextoError());
					logger.error("Error en inicio de petición de pago:"+result.getRespuesta().getMensajeCompuesto());
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
		catch (PasarelaPagoException ex)
		{
			logger.error(ex.getMessage());
			result.getRespuesta().setMessage(Mensajes.getFatalError());
			logger.error (result.getRespuesta().getMensajeCompuesto());
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			logger.trace(ex.getStackTrace());
			result.getRespuesta().setMessage(Mensajes.getFatalError());
			logger.error(result.getRespuesta().getMensajeCompuesto());
		}
		long endTime=System.currentTimeMillis();
		logger.debug("FINAL PETICIÓN EN " + Long.toString((endTime-start)) + " MS RETORNANDO COMO RESULTADO -> "+result.getRespuesta().getError());
		return result;
	}

	/**
	 * Tratamiento del estado de tributo anulado.
	 * @param result
	 */
	private void tratarTributoAnulado(ResultadoPeticion result) {
		DatosEntradaServicio peticion = datosProceso.getPeticionServicio();
		String origen = peticion.getOrigen();
		//Anulado. No se continúa el pago.
		if (origen.equals(Constantes.getOrigenServicioWeb()) && peticion.getModalidad().equals(Constantes.getModalidadAutoliquidacion()))
		{
			logger.error(Mensajes.getTributoAnulado()+ ".-El pago telematico del servicio '"+peticion.getAplicacion()+"' y numero unico '"+peticion.getNumeroUnico()+"' consta como anulado.");
		}
		else if ((origen.equals(Constantes.getOrigenPortal()) || origen.equals(Constantes.getOrigenS1())) &&
				peticion.getModalidad().equals(Constantes.getModalidadAutoliquidacion()))
		{
			logger.error(Mensajes.getTributoAnulado()+".-El pago telematico del justificante '"+peticion.getJustificante()+"' consta como anulado.");
		}
		else if ((origen.equals(Constantes.getOrigenPortal()) || origen.equals(Constantes.getOrigenS1()))&& 
				peticion.getModalidad().equals(Constantes.getModalidadLiquidacion()))
		{
			logger.error(Mensajes.getTributoAnulado()+".-El pago telematico de identificación '"+peticion.getIdentificacion()+"' y referencia '"+peticion.getReferencia()+"' consta como anulado.");
		}
		result.getRespuesta().setMessage(Mensajes.getTributoAnulado());
	}
	
	/**
	 * Tratamiento del estado de tributo pagado.
	 * @param result
	 */
	private void tratarTributoPagado(DatosProceso datosProceso,ResultadoPeticion result) throws PasarelaPagoException{
		GestorLlamadasBD gestorBD = BDFactory.newGestorLlamadasBD(context);
		DatosEntradaServicio peticion = datosProceso.getPeticionServicio();
		ResultadoLlamadaBD resConsulta= gestorBD.obtenerEstadoBD(datosProceso.getTipoLlamada(), datosProceso.getJustificante(),peticion.getIdentificacion(),peticion.getReferencia());
		if (!resConsulta.isError())
		{
			result.getRespuesta().setMessage(Mensajes.getTributoPagadoYa());
			result.getRespuesta().setOperacion(resConsulta.getDatosPagoBD().getNrc());
		}
		else
		{
			result.getRespuesta().setMessage(resConsulta.getCodError(), resConsulta.getTextoError());
		}
	}
	/**
	 * Tratamiento del caso de datos inconsistentes.
	 * @param result
	 */
	private void tratarDatosInconsistentes(ResultadoPeticion result) {
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
	 * Sincroniza el estado de la base de datos con el de la entidad remota.
	 * @param datosProceso
	 * @param result
	 * @return true si la sincronización es correcta.
	 * @throws PasarelaPagoException
	 */
	private boolean sincronizarBDEntidadRemota(ComunicadorPasarela comm,DatosProceso datosProceso, ResultadoPeticion result) throws PasarelaPagoException
	{
		boolean sincronizado=true;
		comm.realizarConsulta();
		DatosComunicacion datosComunicacion= comm.getDatosComunicacion();
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
					//CRUBENCVS En la llamada no actualizamos operante ni fecha de devengo porque en este caso estaba pagado con el valor que consta en la tabla, el de la última llamada.
					//CRUBENCVS 42479. Se añade la fecha de anulación vacía, ya que sólo se está pasando en plataforma de pago UniversalPay
					ResultadoLlamadaBD resActualiza = ges.actualizarBD (comm.getIdPasarela(),datosComunicacion.getEstadoPago().getValor(),datosProceso.getJustificante(),datosProceso.getPeticionServicio().getIdentificacion(), datosProceso.getPeticionServicio().getReferencia(),datosComunicacion.getNumeroOperacion(),datosComunicacion.getFechaPago(),datosComunicacion.getNrc(), "","",datosComunicacion.getResultadoComunicacion(),null);
					if (resActualiza.isError())
					{
						result.getRespuesta().setMessage(resActualiza.getCodError(), resActualiza.getTextoError());
						logger.error("No se puede sincronizar el estado de la base de datos con el de la entidad remota.");
						sincronizado=false;
					}
					break;
				}
			}
		}
		else
		{
			logger.error("No se puede sincronizar el estado de la base de datos con el de la entidad remota.");
			result.getRespuesta().setError(datosComunicacion.getCodigoError());
			result.getRespuesta().setResultado(datosComunicacion.getTextoError());
			logger.error(result.getRespuesta().getMensajeCompuesto()+".No se puede sincronizar el estado de la base de datos con el de la entidad remota.");
			sincronizado=false;
		}
		return sincronizado;
	}
	/**
	 * Trata los diferentes estados de pago en base de datos del tributo. 
	 * @param estado
	 * @param datosProceso
	 * @param result
	 * @param sincronizarConEntidadRemota. Indica si en caso de no saber con exactitud el estado de pago en la entidad remota, se sincronizará el valor de la base de datos con aquella. 
	 * @throws PasarelaPagoException
	 */
	private void tratarEstadosPago (String idPasarela, String idPasarelaBD,String estado, DatosProceso datosProceso,ResultadoPeticion result, boolean sincronizarConEntidadRemota) throws PasarelaPagoException
	{
		DatosEntradaServicio peticion = datosProceso.getPeticionServicio();
		GestorLlamadasBD ges = BDFactory.newGestorLlamadasBD(context);
		String justificante="";
		if (sincronizarConEntidadRemota && (estado.equals(EstadosPago.INICIADO.getValor())|| estado.equals(EstadosPago.ANULACION_COMENZADA.getValor()) ||
				estado.equals(EstadosPago.ERROR.getValor())))
		{
				logger.info ("Estado:" + estado + " provoca consulta en entidad remota.");
				//El pago podría haberse realizado, pero no tener constancia. Se sincroniza el estado con el de la entidad remot
				//Se usa la pasarela seleccionada en base de datos.
				ComunicadorPasarela comm=PasarelasFactory.newSelectorComunicadorPasarela(context).seleccionarComunicadorPasarela(idPasarelaBD,datosProceso);
				if ( sincronizarBDEntidadRemota(comm,datosProceso,result))
				{
					//Se vuelve a consultar y se comprobará entonces el nuevo estado.
					ResultadoLlamadaBD resConsulta = ges.consultaBD(datosProceso.getTipoLlamada(),peticion.getEmisora(),datosProceso.getJustificante(), peticion.getIdentificacion(), peticion.getReferencia());
					if (!resConsulta.isError())
					{
						estado = resConsulta.getDatosPagoBD().getEstado();
						logger.info ("Después de consulta en entidad remota, el nuevo estado es:" + estado);
					}
					datosProceso.setEstado(estado);
					justificante = datosProceso.getJustificante();
					if (justificante!=null && !"".equals(justificante))
					{
						result.getPeticion().setNumero_autoliquidacion(justificante);
					}
					//Se trata el nuevo estado, si hubiera.
					tratarEstadosPago (idPasarela, idPasarelaBD,estado, datosProceso, result,false); //Si el estado vuelve a ser indefinido, no se trata ya.
					return;
				}
				else
				{
					//En principio no se continúa.
					//El resultado ya lo devuelve la sincronización.
					return;
				}
		}
		if (estado.equals(EstadosPago.DIFERENTE.getValor()))
		{
			//alguno de los datos es diferente
			//este caso no deberia de darse nunca
			tratarDatosInconsistentes(result);
		}
		else if (estado.equals(EstadosPago.ANULADO.getValor()))
		{
			tratarTributoAnulado(result);
		}
		else if (estado.equals(EstadosPago.PAGADO.getValor()))
		{
			tratarTributoPagado(datosProceso,result);
		}
		else if (estado.equals(EstadosPago.ANULACION_COMENZADA.getValor())) 
		{
			// No se trata más, si después de consultarlo anteriormente aún
			// está en estado de anulación comenzada, no pagamos, obviamente.
			logger.error(Mensajes.getErrorEstadoPATE()+".-Estado de anulación comenzada. No se continúa.");
			result.getRespuesta().setMessage(Mensajes.getErrorEstadoPATE());
		}
		else if (!estado.equals(EstadosPago.PAGADO.getValor()))
		{
			logger.info ("Se intenta el pago en la entidad remota.");
			//Recuperamos el gestor de comunicaciones con la pasarela correspondiente.
			//Si se ha entrada igualmente al pago, se debe de ir por la pasarela que corresponda
			//a los datos de entrada, sea esta igual o no a la registrada en Base de datos.
			ComunicadorPasarela comm= PasarelasFactory.newSelectorComunicadorPasarela(context).seleccionarComunicadorPasarela(idPasarela,datosProceso);
			//Si la pasarela que está en datos de proceso es distinta de la recuperada en función
			// de la petición al servicio, provoca un cambio de pasarela en base de datos.
			if (!idPasarelaBD.equalsIgnoreCase(idPasarela))
			{
				//Ahora deberemos actualizar en BD la pasarela por la que vamos a realizar el pago.
				ResultadoLlamadaBD resCambioPasarela= ges.cambioPasarelaBD(idPasarela, datosProceso);
				if (resCambioPasarela.isError())
				{
					logger.error(Mensajes.getErrorActualizacionBD()+".-Error al reiniciar la petición con otra pasarela en base de datos:"+resCambioPasarela.getCodError());
					result.getRespuesta().setMessage(Mensajes.getErrorActualizacionBD());
					return;
				}
				datosProceso.setPasarelaPagoPeticion(idPasarela);
			}
			comm.realizarPago();
			DatosComunicacion datosPago= comm.getDatosComunicacion();
			if (!datosPago.isError())
			{
				if (datosPago.getNumeroOperacion()==null || datosPago.getFechaPago()==null|| datosPago.getNrc()==null)
				{
					logger.error(Mensajes.getErrorActualizacionBD()+".-Error al actualizar la petición en base de datos: se han recibido datos vacíos desde Entidad remota [Nrc-Fecha operación-número operación]");
					result.getRespuesta().setMessage(Mensajes.getErrorActualizacionBD());

				}
				else 
				{
					logger.info ("Tributo pagado en entidad remota.");
					//Pagado.
					estado=EstadosPago.PAGADO.getValor();
					result.getRespuesta().setMessage(Mensajes.getOk());
					result.getRespuesta().setOperacion(datosPago.getNrc());
					//Actualizamos el pago con el operante y fecha de devengo que venga en la petición, porque es con la que se ha producido el pago.
					//CRUBENCVS 42479. Se añade la fecha de anulación vacía, ya que sólo se está pasando en plataforma de pago UniversalPay
					ResultadoLlamadaBD resActualizar=ges.actualizarBD(idPasarela,estado, datosProceso.getJustificante(), peticion.getIdentificacion(),peticion.getReferencia(), datosPago.getNumeroOperacion(), datosPago.getFechaPago(), datosPago.getNrc(), datosProceso.getPeticionServicio().getNifOperante(), datosProceso.getPeticionServicio().getFechaDevengo(),datosPago.getResultadoComunicacion(), null);
					if (resActualizar.isError())
					{
						logger.error(resActualizar.getTextoError()+" -Error al actualizar la petición en base de datos:"+resActualizar.getCodError());
						result.getRespuesta().setMessage(resActualizar.getCodError(), resActualizar.getTextoError());
					}
				}
			}
			else
			{
				estado= EstadosPago.ERROR.getValor();
				result.getRespuesta().setError(datosPago.getCodigoError());
				result.getRespuesta().setResultado(datosPago.getTextoError());
				logger.error(result.getRespuesta().getMensajeCompuesto()+" -Error al realizar el pago en entidad remota:" + datosPago.getResultadoDescripcionRemoto());
				//También actualizamos.
				//Indicamos el nif de operante y fecha de devengo actual con el que se ha intentado la operación de pago.
				//CRUBENCVS 42479. Se añade la fecha de anulación vacía, ya que sólo se está pasando en plataforma de pago UniversalPay
				ResultadoLlamadaBD resActualizar=ges.actualizarBD(idPasarela,estado, datosProceso.getJustificante(), peticion.getIdentificacion(),peticion.getReferencia(), "", "", "", datosProceso.getPeticionServicio().getNifOperante(),datosProceso.getPeticionServicio().getFechaDevengo(), datosPago.getResultadoComunicacion(),null);
				if (resActualizar.isError())
				{
					logger.error(resActualizar.getTextoError()+" -Error al actualizar la petición en base de datos:"+resActualizar.getCodError());
					result.getRespuesta().setMessage(resActualizar.getCodError(), resActualizar.getTextoError());
				}
			}
		}
	}
}
