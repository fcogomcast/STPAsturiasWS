/**
 * 
 */
package es.tributasenasturias.objetos;







import es.tributasenasturias.tarjetas.consultaPagoTarjeta.ConsultaPagoTarjetaContexto;
import es.tributasenasturias.tarjetas.consultaPagoTarjeta.ConsultaPagoTarjetaHandlerFactory;
import es.tributasenasturias.tarjetas.consultaPagoTarjeta.ConsultaPagoTarjetaHandlerInterface;
import es.tributasenasturias.tarjetas.core.PateRecord;
import es.tributasenasturias.tarjetas.core.db.PLPasarelaRepository;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.validacion.ValidacionFactory;
import es.tributasenasturias.validacion.plataformasPago.ValidadorConsultaPagoTarjeta;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.IContextReader;
import es.tributasenasturias.webservices.types.PeticionConsultaPagoTarjeta;
import es.tributasenasturias.webservices.types.ResultadoConsultaPagoTarjeta;
import es.tributasenasturias.bd.BDFactory;
import es.tributasenasturias.bd.GestorLlamadasBD;
import es.tributasenasturias.exceptions.PasarelaPagoException;

/**
 * Clase que implementa la funcionalidad de consulta de pago por tarjeta,
 * en plataformas de pago.
 *  *
 */
public class ConsultaPagoTarjeta implements IContextReader{
	CallContext context;
	private Logger logger=null;
	Preferencias pref;
	String idSesion;
	
	/**
	 * Inicializa la variable de resultado.
	 * @param datosProceso Datos del proceso de consulta.
	 * @return
	 */
	private ResultadoConsultaPagoTarjeta inicializarResultado(String origen,
															  String modalidad,
															  String emisora,
															  String identificacion,
															  String referencia,
															  String justificante,
															  String aplicacion,
															  String numeroUnico,
															  String mac)
	{
		ResultadoConsultaPagoTarjeta result= new ResultadoConsultaPagoTarjeta();
		result.getPeticion().setReferencia(referencia);
		result.getPeticion().setAplicacion(aplicacion);
		result.getPeticion().setNumero_unico(numeroUnico);
		result.getPeticion().setIdentificacion(identificacion);
		result.getPeticion().setNumero_autoliquidacion(justificante);
		result.getPeticion().setEmisora(emisora);
		result.getPeticion().setModalidad(modalidad);
		result.getPeticion().setOrigen(origen);
		result.getPeticion().setMac(mac);
		return result;
	}
	
	
	/**
	 * Constructor
	 * @param datosProceso Datos de la petición de pago.
	 * @param context  Contexto de la llamada.
	 * @throws PasarelaPagoException
	 */
	public ConsultaPagoTarjeta(CallContext context) throws PasarelaPagoException{
		
		this.context=context;
		logger = (Logger) context.get(CallContextConstants.LOG_APLICACION);
		this.pref=(Preferencias) context.get(CallContextConstants.PREFERENCIAS);
		this.idSesion= (String) context.get(CallContextConstants.ID_SESION);
		if (logger==null || pref==null)
		{
			throw new PasarelaPagoException ("No se ha inicializado correctamente el contexto de llamada en la construcción de "+ConsultaPagoTarjeta.class.getName());
		}
	}
	
	
	
	
	/**
	 * Método que realiza la consulta del pago. <br/>
	 * Las operaciones que realiza son:<br/>
	 * 1.- Comprueba si en nuestro sistema está registrado el pago.<br/>
	 * 2.- Si está registrado correctamente, se indica que el tributo ya está pagado.<br/>
	 * 3.- Si no está registrado, o lo está pero figura como no pagado, se consulta su estado a través de la plataforma de pago.<br/>
	 * 4.- Con el resultado que devuelve el servicio, se actualiza el estado del pago en el sistema y se devuelve el mismo.<br/>
	 * @return Resultado de la operación, en un objeto {@link Resultado}<br/>
	 */
	/**
	 * @return
	 */
	public ResultadoConsultaPagoTarjeta ejecutar(
			String origen,
			String modalidad,
			String emisora,
			String identificacion,
			String referencia,
			String justificante,
			String aplicacion,
			String numeroUnico,
			String mac)
	{
		ResultadoConsultaPagoTarjeta result = inicializarResultado(origen, 
																   modalidad, 
																   emisora,
																   identificacion,
																   referencia,
																   justificante,
																   aplicacion,
																   numeroUnico,
																   mac);
		long iniTime=0;
		long endTime=0;
		iniTime=System.currentTimeMillis();
		logger.debug("INICIO DE LLAMADA CONSULTA de PAGO CON TARJETA EN PLATAFORMAS DE PAGO");
		
		try
		{	
			
			//Validamos datos de entrada ante todo.
			logger.info("Validación de mensaje de entrada");
			ValidadorConsultaPagoTarjeta validador= ValidacionFactory.newValidadorConsultaPagoTarjeta(context);
			validador.validar(origen, 
					   		  modalidad, 
					   		  emisora,
					   		  identificacion,
					   		  referencia,
					   		  justificante,
					   		  aplicacion,
					   		  numeroUnico,
					   		  mac);
			if (validador.isValido())
			{
				logger.info ("Validación finalizada. Mensaje válido.");
				logger.debug("Construcción de objetos de contexto de la llamada.");
				PeticionConsultaPagoTarjeta peticion= new PeticionConsultaPagoTarjeta.PeticionConsultaPagoBuilder()
													  .setOrigen(origen)
													  .setModalidad(modalidad)
													  .setEmisora(emisora)
													  .setIdentificacion(identificacion)
													  .setReferencia(referencia)
													  .setNumeroAutoliquidacion(justificante)
													  .setAplicacion(aplicacion)
													  .setNumeroUnico(numeroUnico)
													  .setMac(mac)
													  .build();
				
				ConsultaPagoTarjetaContexto consultaPagoContexto= 
								new ConsultaPagoTarjetaContexto.ContextoInicioPagoTarjetaBuilder()
									.setIdSesion(idSesion)
									.setPreferencias(pref)
									.setLog(logger)
									.setPeticion(peticion)
									.build();
				
				logger.debug("Selección de manejador, en función de la pasarela del registro");
				PLPasarelaRepository plRepository= new PLPasarelaRepository(pref, logger, idSesion);
				PateRecord pr=plRepository.getRegistroSincronizadoBD(emisora, justificante, identificacion, referencia, aplicacion, numeroUnico);
				if (pr==null){
					//No hay pago alguno, pues ni siquiera seguimos consultando
					logger.info ("No hay registro en base de datos para ese pago");
					result.getRespuesta().setMessage(Mensajes.getNoHayDatosConsulta());
					
				} else {
					//Técnicamente, ya tendremos el pago sincronizado con la pasarela externa.
					//Lo que ocurre es que para que no se noten cambios respecto a la interfaz
					//de UniversalPay, tengo que consultar igualmente ya que además de sincronizar,
					//se han de devolver las operaciones, etc. En Unicaja ya no es necesario, porque tendremos
					//el estado de pago.
					logger.info ("Hay registro en base de datos.");
					//Aquí tendremos UniversalPay o Unicaja, en el pago con tarjeta no hay más

					ConsultaPagoTarjetaHandlerInterface consultaHandler= ConsultaPagoTarjetaHandlerFactory.getConsultaPagoTarjetaHandler(consultaPagoContexto, pr);
					result= consultaHandler.consultar();
				}
				logger.debug("Ha finalizado la consulta.");
				
			}
			else
			{
				logger.debug ("Validación finalizada. Mensaje no válido:" + validador.getCodigoError() + "=="+Mensajes.getText(validador.getCodigoError()));
				result.getRespuesta().setMessage(validador.getCodigoError());
			}
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			logger.trace(ex.getStackTrace());
			result.getRespuesta().setMessage(Mensajes.getFatalError());
			logger.error(result.getRespuesta().getMensajeCompuesto());
		}
		//Generación de MAC
		if (Constantes.getOrigenServicioWeb().equalsIgnoreCase(origen) 
				&& Constantes.getModalidadAutoliquidacion().equalsIgnoreCase(modalidad))
		{
			try
			{
				result.setMac(generarMACResponse(origen , result));
			}
			catch (Exception ex)
			{
				logger.debug("Rellenamos el objeto a devolver");
				logger.error(Mensajes.getErrorMacGeneracion() + ".-Error al generar la MAC de respuesta:" + ex.getMessage());
				logger.trace(ex.getStackTrace());
				result.getRespuesta().setMessage(Mensajes.getErrorMacGeneracion());
			}
		}
		endTime=System.currentTimeMillis();
		logger.debug("FINAL CONSULTA PAGO CON TARJETA EN " + Long.toString(endTime-iniTime) + " MS RETORNANDO COMO RESULTADO -> "+result.getRespuesta().getError());
		
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
	 * Genera la MAC para la respuesta
	 * @param origen Origen de la petición, para saber la clave que se ha de aplicar para generar la MAC
	 * @param r Respuesta de la operación "Consultar"
	 * @return
	 */
	private String generarMACResponse(String origen, ResultadoConsultaPagoTarjeta r){
		GestorLlamadasBD gestorBD = BDFactory.newGestorLlamadasBD(context);
		try {
			return gestorBD.generarMac(origen,
										r.getPeticion().getEmisora()+
										r.getPeticion().getNumero_autoliquidacion()+
										r.getPeticion().getAplicacion()+
										r.getPeticion().getNumero_unico()+
										r.getRespuesta().getError()+
										r.getRespuesta().getResultado()+
										r.getRespuesta().getOperacion()+
										r.getRespuesta().getFechaPago()
										);
		} catch (Exception e){
			logger.error("Error al generar la MAC:"+e.getMessage());
			return "";
		}
	}
	
}
