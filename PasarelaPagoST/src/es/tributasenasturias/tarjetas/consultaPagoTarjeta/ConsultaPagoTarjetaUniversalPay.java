package es.tributasenasturias.tarjetas.consultaPagoTarjeta;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.tributasenasturias.bd.BDFactory;
import es.tributasenasturias.bd.GestorLlamadasBD;
import es.tributasenasturias.bd.ResultadoLlamadaBD;
import es.tributasenasturias.bd.Datos.ResultadoConsultaPagoTarjetaBD;
import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.plataforma.unipay.UniversalPay;
import es.tributasenasturias.plataforma.unipay.UniversalPay.RespuestaConsultaPagoTarjetaUniversalPay;
import es.tributasenasturias.utils.EstadosPago;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.CallContextManager;
import es.tributasenasturias.webservices.types.ResultadoConsultaPagoTarjeta;
import es.tributasenasturias.webservices.types.HistoricoOperacionesPago.AnulacionRealizada;
import es.tributasenasturias.webservices.types.HistoricoOperacionesPago.Operacion;

public class ConsultaPagoTarjetaUniversalPay extends ConsultaPagoTarjetaHandler {

	public ConsultaPagoTarjetaUniversalPay(ConsultaPagoTarjetaContexto contexto){
		super(contexto);
	}
	
	/**
	 * Formatea la fecha anulación recibida de la consulta. Si no puede, devuelve 
	 * la cadena original.
	 * @param fecha Formato yyyyMMddHHmmss
	 * @return Fecha en formato yyyy-MM-dd HH:mm:ss
	 */
	private String formateaFechaAnulacion(String fecha){
		if (fecha==null){
			return null;
		}
		try {
			SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmss");
			Date f=s.parse(fecha);
			s= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return s.format(f);
		} catch(ParseException pe){
			return fecha;
		}
		
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
	
	/**
	 * 47535 30/03/2023
	 * Copia del código que existía como código de la consulta de pago por tarjeta,
	 * que sólo tenía sentido para UniversalPay 
	 */
	@Override
	public ResultadoConsultaPagoTarjeta consultar()
			throws PasarelaPagoException {
		
		Preferencias pref= this.consultaPagoContexto.getPref();
		Logger log = this.consultaPagoContexto.getLog();
		ResultadoConsultaPagoTarjeta result = new ResultadoConsultaPagoTarjeta(consultaPagoContexto.getPeticion());
		boolean continuar=true;
		
		GestorLlamadasBD bd = BDFactory.newGestorLlamadasBD(this.consultaPagoContexto.getPref(), this.consultaPagoContexto.getIdSesion(), this.consultaPagoContexto.getLog());
		ResultadoConsultaPagoTarjetaBD resConsulta 
					= bd.consultaPagoTarjeta(
							consultaPagoContexto.getPeticion().getOrigen(),
							consultaPagoContexto.getPeticion().getEmisora(),
							consultaPagoContexto.getPeticion().getNumero_autoliquidacion(),
							consultaPagoContexto.getPeticion().getIdentificacion(),
							consultaPagoContexto.getPeticion().getReferencia(),
							consultaPagoContexto.getPeticion().getAplicacion(),
							consultaPagoContexto.getPeticion().getNumero_unico()
		);
		
		if (!resConsulta.isError())
		{
			if (resConsulta.hayDatos()) {
				log.debug ("Pasarela de pago recuperada de base de datos:"+ resConsulta.getPasarelaPago());
				String estadoBD = resConsulta.getEstado();
				String estado= estadoBD;
				log.debug("Estado de pago en BD:" + estadoBD);
				boolean actualizadoEstadoAPagado=false;
				String operacionPago=""; //Almacenaremos el PRIMER identificador operación de pago (operacionEpst) correcto que encontremos
				String fechaPago="";
				String numeroOperacion="";
				String nrc="";
				String paymentCode="";
				
				//30/03/2023. 47535 
				//Como UniversalPay necesita un objeto de contexto de invocación, lo creo
				//Se podría hacer un nuevo constructor, pero no quiero modificar más de lo necesario
				CallContext wsContextoInvocacion = CallContextManager.newCallContext();
				
				wsContextoInvocacion.setItem(CallContextConstants.ID_SESION, consultaPagoContexto.getIdSesion());
				wsContextoInvocacion.setItem(CallContextConstants.PREFERENCIAS, pref);
				wsContextoInvocacion.setItem(CallContextConstants.LOG_APLICACION, log);
				//Si el estado es "G", "Generado hash", ni siquiera habremos ido a 
				//la plataforma, con lo cual se da por no pagado y no hay que consultar
				//CRUBENCVS  13/05/2022. En realidad sí vamos a consultar, aunque 
				//el estado sea "GENERADO", porque puede que haya habido un error,
				//generen nuevo hash y nunca pase de ese estado.
				//Pero, dado que los de estado "GENERADO" pueden no tener operación_epst
				//si son realmente nuevos, consultaré los de estado "GENERADO" si tienen
				//operación EPST.
				if (!EstadosPago.GENERADO_HASH.getValor().equalsIgnoreCase(estado) ||
						(EstadosPago.GENERADO_HASH.getValor().equalsIgnoreCase(estado) &&
						resConsulta.getOperacionEpst()!=null &&
						!"".equals(resConsulta.getOperacionEpst()))){
					//Consultamos la lista de operaciones_epst en la entidad remota
					UniversalPay upay = new UniversalPay(wsContextoInvocacion);
					//Marcamos si está pagado, que será siempre que haya
					//un pago no anulado en el histórico. Si nos viene más de un pago correcto,
					//cosa que no debería, se cogen datos de cualquiera de ellos,
					//no podemos asegurar cuál es el bueno.
					boolean pagadoEntidadRemota=false; 
					//Por si lo necesito después, y para no consultar dos veces,
					//guardo el resultado de consulta de la operación que hay en PATE,
					//la resConsulta.operacionEpst
					RespuestaConsultaPagoTarjetaUniversalPay estadoRemotoOperacionActual=null;
					int totalOperacionesEpst= resConsulta.getNumerosOperacionEpst().length;
					if (totalOperacionesEpst>0){
						for (int i=0;i<totalOperacionesEpst;i++){
							String operacionEpst= resConsulta.getNumerosOperacionEpst()[i];
							RespuestaConsultaPagoTarjetaUniversalPay consultaUpay=upay.consultarPagoTarjetaPlataforma(operacionEpst);
							//¿Qué hago ante errores en la consulta remota?
							if (!consultaUpay.isError()){
								//Almaceno la correspondiente al a operación de PATE actual
								//Lo hago para no tener que consultar de nuevo posteriormente.
								if (operacionEpst.equals(resConsulta.getOperacionEpst())){
									estadoRemotoOperacionActual= consultaUpay;
								}
								//Erróneas o no, recogemos su estado. Como desde BD suben ordenados, se devolverá ordenado
								//por número de operación, aunque podríamos reordenarlo aquí para asegurarnos.
								
								Operacion operacion = new Operacion();
								operacion.setIdentificador(operacionEpst);
								operacion.setFecha(consultaUpay.getFechaPago());
								
								//
								operacion.setResultado("000".equals(consultaUpay.getCodigoResultado())?"OK":"KO");
								
								if (consultaUpay.getRefundData()!=null && consultaUpay.getRefundData().length>0){
									for (int j=0;j<consultaUpay.getRefundData().length;j++){
										AnulacionRealizada anulacionRealizada = new AnulacionRealizada();
										anulacionRealizada.setFecha(formateaFechaAnulacion(consultaUpay.getRefundData()[j].getFechaDevolucion()));
										anulacionRealizada.setImporte(consultaUpay.getRefundData()[j].getAmount());
										operacion.getAnulaciones().add(anulacionRealizada);
									}
								} else {
									// CRUBENCVS  13/05/2022
									//Si el código de resultado es un "501", que significa devolución automática,
									//insertamos una devolución con la fecha de pago y el importe de pago,
									//porque se supone que se ha hecho esa devolución.
									if ("501".equals(consultaUpay.getCodigoResultado())){
										AnulacionRealizada anulacionRealizada = new AnulacionRealizada();
										anulacionRealizada.setFecha(consultaUpay.getFechaPago()); //Dado que no tenemos fecha de anulación
										anulacionRealizada.setImporte(consultaUpay.getImporte());
										operacion.getAnulaciones().add(anulacionRealizada);
									}
								}
								result.getOperaciones().getListaOperaciones().add(operacion);
								//Para el caso de "G", nunca debería haberse realizado el pago,
								//ya que de todas maneras antes de pasar a "G" debería
								//haberse consultado
								if (consultaUpay.isPagoRealizado()){
									if ("".equals(operacionPago)){
										operacionPago= operacionEpst;
										fechaPago= consultaUpay.getFechaPago();
										nrc= consultaUpay.getNrc();
										numeroOperacion= consultaUpay.getOperacion();
									    paymentCode=consultaUpay.getCodigoResultado();
									    pagadoEntidadRemota=true;
									}
								} 
							} else {
								result.getRespuesta().setMessage(Mensajes.getErrorConsulta(), Mensajes.getExternalText(Mensajes.getErrorConsulta()));
								log.error("Error en consulta de pago:"+result.getRespuesta().getMensajeCompuesto());
								continuar=false;
							}
						}
					}
				
					if (pagadoEntidadRemota && !EstadosPago.PAGADO.getValor().equals(resConsulta.getEstado())){
						log.info("El estado en base de datos no era pagado, pero en la plataforma consta como pagado, se cambia a pagado");
						boolean finalizado= upay.finalizarPagoTarjeta(operacionPago, 
																	  EstadosPago.PAGADO.getValor(), 
															          paymentCode, 
															          numeroOperacion, 
															          nrc, 
															          fechaPago, 
															          consultaPagoContexto.getPeticion().getEmisora(), 
															          consultaPagoContexto.getPeticion().getNumero_autoliquidacion(), 
															          consultaPagoContexto.getPeticion().getIdentificacion(), 
															          consultaPagoContexto.getPeticion().getReferencia());
						
						//Si va bien, se da por hecho que lo ha actualizado a "Pagado"
						if (finalizado){
							log.info ("Estado cambiado a \"Pagado\"");
							actualizadoEstadoAPagado=true;
							estado=EstadosPago.PAGADO.getValor();
						} else {
							result.getRespuesta().setMessage(Mensajes.getErrorActualizacionBD(), Mensajes.getExternalText(Mensajes.getErrorActualizacionBD()));
							log.error("Error en actualización de estado generada por consulta de pago:"+result.getRespuesta().getMensajeCompuesto());
							continuar=false;
						}
					} else {
						//No hay una manera "limpia" de poner esto, tengo que consultar de nuevo el
						//estado de la operacion_epst principal (la de PATE) en la entidad remota,
						//para recuperar los datos actuales.
						//Actualizamos al estado que haya venido, que no sea pagado
						//Dado que el estado "G" que haya entrado por aquí es uno que 
						//tenía otro estado, pero se ha iniciado de nuevo una operación de pago
						//contra él, en caso de ser estado "G", no actualizamos, ya que
						//podría actualizarlo a "Error", y si han iniciado una nueva operación
						//es mejor dejarles continuar
						if (estadoRemotoOperacionActual!=null
								&& !EstadosPago.GENERADO_HASH.getValor().equalsIgnoreCase(estado)){
							String estadoActualizacion;
							//Nos vale que esté pagado el actual en PATE o que se haya actualizado con lo que vino de la entidad,
							//cualquiera de ellos es correcto
							//En cualquiera de esos casos tendrá el valor correcto en "ESTADO"
							if (!EstadosPago.PAGADO.getValor().equalsIgnoreCase(estado) ){
								if (estadoRemotoOperacionActual.isPagoAnulado()) 
								{
									estadoActualizacion="A";
								} else {
									estadoActualizacion="E"; 
								}
							ResultadoLlamadaBD resBD=bd.actualizarBD(
											         resConsulta.getPasarelaPago(), 
													 estadoActualizacion, 
													 resConsulta.getJustificante(), 
													 resConsulta.getIdentificacion(), 
													 resConsulta.getReferencia(), 
													 estadoRemotoOperacionActual.getOperacion(), 
													 null, 
													 estadoRemotoOperacionActual.getNrc(), 
													 resConsulta.getNifOperante(), 
													 resConsulta.getFechaDevengo(), 
													 estadoRemotoOperacionActual.getCodigoResultado(),
													 estadoRemotoOperacionActual.getFechaAnulacion()
													 );
								if (resBD.isError()){
									result.getRespuesta().setMessage(Mensajes.getErrorActualizacionBD(), Mensajes.getExternalText(Mensajes.getErrorActualizacionBD()));
									log.error("Error en actualización de estado en BD generada por consulta de pago:"+result.getRespuesta().getMensajeCompuesto());
									continuar=false;
								}
							}
						} else {
							//¿Podría no ser un error el que no haya datos en la entidad? Deberíamos tener un
							//número de operación que no consta en remoto
						}
					}
				} 
				if (continuar){
					if (estado.equals(EstadosPago.PAGADO.getValor()))
					{
						result.getRespuesta().setMessage(Mensajes.getTributoPagado());
						result.getPeticion().setNumero_autoliquidacion(resConsulta.getJustificante());
						if (!actualizadoEstadoAPagado){
							//Se devuelve el número justificante, por si se ha consultado en base al número de aplicación y servicio, porque entonces no está en la petición
							result.getRespuesta().setOperacion(resConsulta.getNrc());
							result.getRespuesta().setFechaPago(formateaFechaSalida(resConsulta.getFechaPago()));
						} else {
							// NRC y fecha se tienen en los datos con los que se actualizó la base de datos
							// No se vuelve a consultar (por ahora)
							result.getRespuesta().setOperacion(nrc);
							result.getRespuesta().setFechaPago(fechaPago);
						}
						log.info("Tributo pagado.");
						
					}
					else if (EstadosPago.ANULADO.getValor().equals(estado)) 
					{
						result.getRespuesta().setMessage(Mensajes.getTributoAnulado());
						log.info ("Tributo anulado.");
					} else {
						result.getRespuesta().setMessage(Mensajes.getTributoNoPagado());
						log.info ("Tributo no pagado.");
					}
				}
			} else {
				result.getRespuesta().setMessage(Mensajes.getNoHayDatosConsulta(), Mensajes.getExternalText(Mensajes.getNoHayDatosConsulta()));
				log.error("Error en consulta de pago:"+result.getRespuesta().getMensajeCompuesto());
			}
		}
		else
		{
			result.getRespuesta().setMessage(Mensajes.getErrorConsulta(), Mensajes.getExternalText(Mensajes.getErrorConsulta()));
			log.error("Error en consulta de pago:"+result.getRespuesta().getMensajeCompuesto());
		}
		return result;
	}

}
