package es.tributasenasturias.plataforma.unipay;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


import es.tributasenasturias.bd.BDFactory;
import es.tributasenasturias.bd.GestorLlamadasBD;
import es.tributasenasturias.bd.ResultadoConsultaPagoBD;
import es.tributasenasturias.bd.ResultadoLlamadaBD;
import es.tributasenasturias.dao.DatosPagoBD;
import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.plataforma.GestorEstadoRemotoPago;
import es.tributasenasturias.plataforma.unipay.MediadorUniversalPay.QueryResponse;
import es.tributasenasturias.plataforma.unipay.MediadorUniversalPay.RefundResponse;
import es.tributasenasturias.plataforma.unipay.MediadorUniversalPay.TokenResponse;
import es.tributasenasturias.plataforma.unipay.MediadorUniversalPay.QueryResponse.RefundData;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.utils.EstadosPago;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.validacion.ValidacionFactory;
import es.tributasenasturias.validacion.plataformasPago.ValidadorInicioPagoTarjeta;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.types.InicioPagoTarjetaRequest;

/**
 * Objeto para la lógica de UniversalPay
 * @author crubencvs
 *
 */
public class UniversalPay {

	private static final String SOLICITUD_TOKEN = "K";
	private Preferencias pref;
	private Logger log;
	private CallContext context;
	private String plataformaPago="UniversalPay";
	private Properties propiedades;
	private MediadorUniversalPay mup;
	
	/**
	 * Clase para respuesta de la operación de inicio de pago con tarjeta
	 * @author crubencvs
	 *
	 */
	public static class RespuestaInicioPagoTarjeta{
		private boolean esError;
		private String estadoBD;
		private String operacionBD;
		private String UniversalPayToken;
		private String UniversalPayCode;
		private String UniversalPayDescription;
		private String codigo;
		private String mensaje;
		public final boolean isError() {
			return esError;
		}
		public final void setEsError(boolean esError) {
			this.esError = esError;
		}
		public final String getCodigo() {
			return codigo;
		}
		public final void setCodigo(String codigo) {
			this.codigo = codigo;
		}
		public final String getMensaje() {
			return mensaje;
		}
		public final void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}
		public final String getEstadoBD() {
			return estadoBD;
		}
		public final void setEstadoBD(String estadoBD) {
			this.estadoBD = estadoBD;
		}
		public final String getOperacionBD() {
			return operacionBD;
		}
		public final void setOperacionBD(String numOperacionBD) {
			this.operacionBD = numOperacionBD;
		}
		public final String getUniversalPayToken() {
			return UniversalPayToken;
		}
		public final void setUniversalPayToken(String universalPayToken) {
			UniversalPayToken = universalPayToken;
		}
		public final String getUniversalPayCode() {
			return UniversalPayCode;
		}
		public final void setUniversalPayCode(String universalPayCode) {
			UniversalPayCode = universalPayCode;
		}
		public final String getUniversalPayDescription() {
			return UniversalPayDescription;
		}
		public final void setUniversalPayDescription(String universalPayDescription) {
			UniversalPayDescription = universalPayDescription;
		}
	}
	/**
	 * Clase con los datos de la consulta de pago en UniversalPay
	 * @author crubencvs
	 *
	 */
	public static class RespuestaConsultaPagoTarjetaUniversalPay {
		private boolean error;
		private String fechaPago;
		private String operacion;
		private String nrc;
		private boolean pagoRealizado;
		private boolean pagoAnulado;
		private String fechaAnulacion;
		private RefundData[] refundData;
		//CRUBENCVS 13/05/2022. Para los 
		//códigos de retorno 501, guardo el importe
		//que me permitirá informar la "anulación" 
		//que no nos envía UniversalPay, pero que 
		//queremos devolver en la consulta, ya que el 
		//501 significa "Devolución automática".
		private String importe;
		private String codigoResultado;
		//CRUBENCVS 11/01/2022 Sin número. 
		//Tenemos que saber que desde UniversalPay nos indican que la operación no consta
		//en su sistema. Esto no se considera un error. No es lo mismo que no podamos 
		//conocer el estado, en este caso nos dicen que la que les hemos indicado no consta
		private boolean operacionNoRegistradaUpay;
		
		public final boolean isError() {
			return error;
		}
		public final void setError(boolean error) {
			this.error = error;
		}
		public final String getFechaPago() {
			return fechaPago;
		}
		public final void setFechaPago(String fechaPago) {
			this.fechaPago = fechaPago;
		}
		public final String getOperacion() {
			return operacion;
		}
		public final void setOperacion(String operacion) {
			this.operacion = operacion;
		}
		public final String getNrc() {
			return nrc;
		}
		public final void setNrc(String nrc) {
			this.nrc = nrc;
		}
		public final boolean isPagoRealizado() {
			return pagoRealizado;
		}
		public final void setPagoRealizado(boolean pagoRealizado) {
			this.pagoRealizado = pagoRealizado;
		}
		public final boolean isPagoAnulado() {
			return pagoAnulado;
		}
		public final void setPagoAnulado(boolean pagoAnulado) {
			this.pagoAnulado = pagoAnulado;
		}
		public final String getCodigoResultado() {
			return codigoResultado;
		}
		public final void setCodigoResultado(String codigoResultado) {
			this.codigoResultado = codigoResultado;
		}
		/**
		 * @return the fechaAnulacion
		 */
		public final String getFechaAnulacion() {
			return fechaAnulacion;
		}
		/**
		 * @param fechaAnulacion the fechaAnulacion to set
		 */
		public final void setFechaAnulacion(String fechaAnulacion) {
			this.fechaAnulacion = fechaAnulacion;
		}
		/**
		 * @return the refundData
		 */
		public final RefundData[] getRefundData() {
			return refundData;
		}
		/**
		 * @param refundData the refundData to set
		 */
		public final void setRefundData(RefundData[] refundData) {
			this.refundData = refundData;
		}
		/**
		 * @return the importe
		 */
		public final String getImporte() {
			return importe;
		}
		/**
		 * @param importe the importe to set
		 */
		public final void setImporte(String importe) {
			this.importe = importe;
		}
		/**
		 * @return the operacionNoRegistradaUpay
		 */
		public final boolean isOperacionNoRegistradaUpay() {
			return operacionNoRegistradaUpay;
		}
		/**
		 * @param operacionNoRegistradaUpay the operacionNoRegistradaUpay to set
		 */
		public final void setOperacionNoRegistradaUpay(boolean operacionNoRegistradaUpay) {
			this.operacionNoRegistradaUpay = operacionNoRegistradaUpay;
		}
		
	}
	/** Clase para modelar la respuesta de sincronización de los datos de BD
	 * con universalPay
	 * @author crubencvs
	 *
	 */
	public static class ResultadoSincronizacionUniversalPayBD{
		private boolean error;
		private boolean tributoPagado;
		private boolean tributoAnulado;
		private RespuestaConsultaPagoTarjetaUniversalPay resultadoConsulta= new RespuestaConsultaPagoTarjetaUniversalPay();
		private String codError;
		private String textoError;
		//CRUBENCVS 11/01/2023 Sin número
		private boolean operacionNoRegistradaUpay;
		public final boolean isError() {
			return error;
		}
		public final void setError(boolean error) {
			this.error = error;
		}
		public final boolean isTributoPagado() {
			return tributoPagado;
		}
		public final void setTributoPagado(boolean tributoPagado) {
			this.tributoPagado = tributoPagado;
		}
		public final boolean isTributoAnulado() {
			return tributoAnulado;
		}
		public final void setTributoAnulado(boolean tributoAnulado) {
			this.tributoAnulado = tributoAnulado;
		}
		public final String getCodError() {
			return codError;
		}
		public final void setCodError(String codError) {
			this.codError = codError;
		}
		public final String getTextoError() {
			return textoError;
		}
		public final void setTextoError(String textoError) {
			this.textoError = textoError;
		}
		public final RespuestaConsultaPagoTarjetaUniversalPay getResultadoConsulta() {
			return resultadoConsulta;
		}
		public final void setResultadoConsulta(
				RespuestaConsultaPagoTarjetaUniversalPay resultadoConsulta) {
			this.resultadoConsulta = resultadoConsulta;
		}
		/**
		 * @return the operacionNoRegistradaUpay
		 */
		public final boolean isOperacionNoRegistradaUpay() {
			return operacionNoRegistradaUpay;
		}
		/**
		 * @param operacionNoRegistradaUpay the operacionNoRegistradaUpay to set
		 */
		public final void setOperacionNoRegistradaUpay(boolean operacionNoRegistradaUpay) {
			this.operacionNoRegistradaUpay = operacionNoRegistradaUpay;
		}
		
	}
	
	//Respuesta de anulación de tarjeta
	public static class RespuestaAnulacionTarjeta {
		private boolean esError;
		private String codigo;
		private String mensaje;
		public final boolean isEsError() {
			return esError;
		}
		public final void setEsError(boolean esError) {
			this.esError = esError;
		}
		public final String getCodigo() {
			return codigo;
		}
		public final void setCodigo(String codigo) {
			this.codigo = codigo;
		}
		public final String getMensaje() {
			return mensaje;
		}
		public final void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}
	}
	
	/**
	 * Constructor 
	 * @param context Contexto de la llamada, con preferencias, log e identificador de llamada
	 * @throws PasarelaPagoException
	 */
	public UniversalPay(CallContext context) throws PasarelaPagoException{
		this.context=context;
		pref = (Preferencias) context.get(CallContextConstants.PREFERENCIAS);
		log = (Logger) context.get(CallContextConstants.LOG_APLICACION);
		Properties prop = new Properties();
		try {
			InputStream is=null;
			try {
				is= new FileInputStream(pref.getFicheroPropiedadesUniversalPay());
				prop.load(is);
			} finally{
				if (is!=null){
					try { is.close(); } catch(Exception e){}
				}
			}
			this.propiedades=prop;
			mup= new MediadorUniversalPay(prop, context);
		}catch (Exception e){
			throw new PasarelaPagoException("Error en instanciación de gestor de UniversalPay" + e.getMessage(), e);
		}
	}
	
	
	/**
	 * Operación de inicio de pago con tarjeta
	 * @param peticion
	 * @throws PasarelaPagoException
	 */
	public RespuestaInicioPagoTarjeta iniciarPagoTarjeta(InicioPagoTarjetaRequest peticion) throws PasarelaPagoException{
		log.info(plataformaPago+":Validación de datos de entrada.");
		RespuestaInicioPagoTarjeta response= new RespuestaInicioPagoTarjeta();
		ValidadorInicioPagoTarjeta validador= ValidacionFactory.newValidadorOperacionInicioPagoTarjeta(context);
		validador.validar(peticion);
		if (validador.isValido())
		{
			boolean continuar=true;
			log.info ("Datos de entrada válidos.");
			GestorLlamadasBD gestorBD = BDFactory.newGestorLlamadasBD(context);
			log.info("Consultamos estado del registro en base de datos, si existe");
			//Consulta del registro en base de datos, si hubiese.
			ResultadoConsultaPagoBD rPate= gestorBD.consultaInicioPagoTarjeta(
													peticion.getOrigen(), 
													peticion.getModalidad(), 
													peticion.getEmisora(), 
													peticion.getImporte(), 
													peticion.getNumeroAutoliquidacion(), 
													peticion.getNifContribuyente(), 
													peticion.getFechaDevengo(), 
													peticion.getDatoEspecifico(), 
													peticion.getExpediente(), 
													peticion.getIdentificacion(), 
													peticion.getReferencia(), 
													peticion.getNifOperante(), 
												    peticion.getModelo());
			if(rPate.isConsultaCorrecta()){
				if (rPate.isHayDatos()) {
					if (rPate.isDatosConsistentes()){
						String estado=rPate.getDatosPagoBD().getEstado();
						if (estado==null||"".equals(estado)){
							continuar=true;
						}
						else if ("P".equals(estado)){
							log.info("Tributo ya pagado");
							response.setCodigo(Mensajes.getTributoPagadoYa());
							response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
							response.setEsError(false);
							continuar=false;
						} else if ("A".equals(estado)){
							log.info("Pago de tributo anulado, se permite volver a pagar el tributo");
							// 22/09/21. Dejamos pasar los anulados, se podrán volver a 
							// pagar
							//response.setCodigo(Mensajes.getTributoAnulado());
							//response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
							response.setEsError(false);
							//continuar=false;
							continuar=true;
						} else if ("G".equals(estado)){
							log.info("Pago grabado, no iniciado");
							continuar=true;
						}
						//CRUBENCVS 43788.  18/11/2021
						else if (SOLICITUD_TOKEN.equals(estado)){
							log.info("Pendiente de solicitud de token, no iniciado");
							continuar=true;
						}
						//FIN CRUBENCVS 43788
						else { //Otro estado, que debemos consultar antes de continuar, por si ya estuviera pagado.
							
							//Tenemos que averiguar si el pago existente es de la pasarela nueva o la antigua,
							//para sincronizar con la pasarela antigua o la nueva.
							if (rPate.getDatosPagoBD().isPagoPasarelaNueva()) { //Lo más frecuente...
								log.info("Estado "+ estado + " se intenta sincronizar con plataforma de pago");
								//Resto de casos. Hay que llamar a la consulta y actualización
								//en remoto
								ResultadoSincronizacionUniversalPayBD rupay=this.sincronizarEstadoBDConPlataforma(rPate.getDatosPagoBD());
								if (!rupay.isError()){
									if (rupay.isTributoPagado()){
										log.info("Tributo ya pagado");
										response.setCodigo(Mensajes.getTributoPagadoYa());
										response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
										response.setEsError(false);
										continuar=false;
									}
									else if (rupay.isTributoAnulado()){
										log.info("Tributo anulado");
										response.setCodigo(Mensajes.getTributoAnulado());
										response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
										response.setEsError(false);
										continuar=false;
									}  //Resto de casos, continuamos
								} else {
									//CRUBENCVS 11/01/2023.
									//Si el problema está en que al otro lado
									//no consta la operación, continuamos el pago.
									if (!rupay.isOperacionNoRegistradaUpay()){
										log.info("Error en la sincronización:" + rupay.getCodError() + ":"+rupay.getTextoError());
										response.setEsError(true);
										response.setCodigo(Mensajes.getErrorActualizacionBD());
										response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
										continuar=false; //No sabemos si ha terminado bien, y el estado remoto
									} else {
										continuar=true; //No existía en UniversalPay, seguimos con la operación
									}
									// FIN  CRUBENCVS 11/01/2023
								}
							} else { //Pues por la antigua
								log.info("Estado "+ estado + " se intenta sincronizar con pasarela de pago (antigua)");
								GestorEstadoRemotoPago gEstado= new GestorEstadoRemotoPago(this.context);
								GestorEstadoRemotoPago.ResultadoSincronizacionPasarelaPagoAntigua sincrAntigua= 
												gEstado.sincronizarEstadoBDConPasarelaAntigua(
																peticion.getOrigen(),
																peticion.getModalidad(),
																peticion.getEmisora(),
																peticion.getModelo(),
																peticion.getEntidad(),
																peticion.getNifContribuyente(),
																peticion.getFechaDevengo(),
																peticion.getDatoEspecifico(),
																peticion.getIdentificacion(),
																peticion.getReferencia(),
																peticion.getNumeroAutoliquidacion(),
																peticion.getExpediente(),
																peticion.getImporte(),
																peticion.getNifOperante(),
																rPate.getDatosPagoBD()
												);
								if (!sincrAntigua.isError()){
									if (sincrAntigua.isTributoPagado()){
										log.info("Tributo ya pagado");
										response.setCodigo(Mensajes.getTributoPagadoYa());
										response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
										response.setEsError(false);
										continuar=false;
									}
									else if (sincrAntigua.isTributoAnulado()){
										log.info("Tributo anulado");
										response.setCodigo(Mensajes.getTributoAnulado());
										response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
										response.setEsError(false);
										continuar= false;
									}  //Resto de casos, llamamos a BD para inicializar el estado a "Iniciado" por pasarela nueva
									else {
										//Aquí se incluye el caso en que no haya constancia del intento en la entidad de pago,
										//no comprobamos explícitamente con 
										//if (sincrAntigua.isNoHayDatos())
										//porque no es necesario
										log.info("Detectado pago sin finalizar en pasarela de pago \"antigua\", se convierte a registro de pago en plataforma de pago");
										ResultadoLlamadaBD resConversion= 
													gestorBD.convertirPagoAntiguoAUniversalPay(peticion.getEmisora(), 
																				               peticion.getNumeroAutoliquidacion(), 
																				               peticion.getIdentificacion(), 
																				               peticion.getReferencia(), 
																				               propiedades.getProperty("PROC_ALMACENADO_CONVERTIR_PAGO_ANTIGUO"));
	                                    if (resConversion.isError()){
	                                    	log.info("Error al convertir el registro de pago de la pasarela antigua a la plataforma de pago");
	                                    	response.setEsError(true);
	                                    	response.setCodigo(Mensajes.getErrorActualizacionBD());
	                                    	response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
	                                    	continuar=false; 
	                                    }
									}
								} else {
									log.info("Error en la sincronización:" + sincrAntigua.getCodError() + ":"+sincrAntigua.getTextoError());
									response.setEsError(true);
									response.setCodigo(sincrAntigua.getCodError());
									response.setMensaje(sincrAntigua.getTextoError());
									continuar=false; //No sabemos si ha terminado bien, y el estado remoto
								}
							}
						} 
					} else {
						//No son los mismos datos del pago, se para
						log.info("Error, los datos de pago no coinciden con los almacenados en la base de datos");
						response.setEsError(true);
						response.setCodigo(Mensajes.getErrorDatosInconsistentes());
						response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
						continuar=false;
					}
				}else { //No hay datos, adelante
					continuar= true;
				}
			} else {
				response.setCodigo(Mensajes.getErrorPeticionBD());
				response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
				response.setEsError(true);
				continuar=false;
			}
			
			
			if (continuar){
				log.info (plataformaPago+":Inicio de almacenamiento de datos en base de datos.");
				ResultadoLlamadaBD resInicioBD= gestorBD.iniciarPagoTarjetaUniversalPay(peticion,
																						propiedades.getProperty("ENTIDAD_EQUIVALENTE"),
																						propiedades.getProperty("PROC_ALMACENADO_INICIAR_PAGO_TARJETA"));
				if (!resInicioBD.isError())
				{
					//Recojo el estado de finalización de la operación.
					String estado=resInicioBD.getDatosPagoBD().getEstado();
					String operacion=resInicioBD.getDatosPagoBD().getOperacionEpst();
					response.setEstadoBD(estado);
					response.setOperacionBD(operacion);
					//Tratamos el estado que haya llegado
					//CRUBENCVS 43788. El nuevo estado inicial será el previo 
					//a solicitud de token, pasará a "Iniciado" al recuperar correctamente
					//el token.
					//if ("I".equals(estado)){
					//Durante el pase, tanto "I" como SOLICITUD_TOKEN deben ser válidos,
					//para pedir el token.
					//Una vez se haya hecho el pase, aquí ya no llegará con "I"
					//ningún pago nuevo
					if (SOLICITUD_TOKEN.equals(estado) || "I".equals(estado)){
					//if (SOLICITUD_TOKEN.equals(estado)){
						//Iniciado. Tenemos que llamar a la plataforma para solicitar un token
						TokenResponse tr=mup.tokenRequest(operacion, 
														 peticion.getEmisora(), 
														 peticion.getNumeroAutoliquidacion(), 
														 peticion.getIdentificacion(), 
														 peticion.getReferencia(), 
														 peticion.getImporte(), 
														 peticion.getFechaDevengo(), 
														 peticion.getDatoEspecifico(), 
														 peticion.getNifOperante(), 
														 peticion.getNifContribuyente(), 
														 peticion.getExpediente()
									    			   );
						if ("000".equals(tr.getCode())){
							//Correcto.
							//CRUBENCVS 43788. Lo primero que hemos de hacer es 
							//actualizar el estado a "Iniciado"
							ResultadoLlamadaBD cambioEstado=
												gestorBD.cambiarEstadoAIniciado(propiedades.getProperty("ENTIDAD_EQUIVALENTE"), 
																			    peticion.getNumeroAutoliquidacion(), 
																				peticion.getIdentificacion(), 
																				peticion.getReferencia());
							if (!cambioEstado.isError()){
								response.setUniversalPayToken(tr.getToken());
								response.setUniversalPayCode(tr.getCode());
								response.setUniversalPayDescription(tr.getDescription());
								response.setCodigo(Constantes.getOkCode());
								response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
								response.setEsError(false);
							} else {
								response.setEsError(true);
								response.setCodigo(Mensajes.getErrorActualizacionBD());
								response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
							}
							//FIN CRUBENCVS 43788 18/11/2021
						} else {
							response.setEsError(true);
							response.setUniversalPayCode(tr.getCode());
							response.setUniversalPayDescription(tr.getDescription());
							response.setCodigo(Mensajes.getErrorTokenRequest());
							response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
						}
					} else if ("P".equals(estado)){
						//Ya pagado, tenemos que informar de ello
						response.setEsError(true);
						response.setCodigo(Mensajes.getTributoPagadoYa());
						response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
					} else if ("A".equals(estado)){
						response.setEsError(true);
						response.setCodigo(Mensajes.getTributoAnulado());
						response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
					} else if ("D".equals(estado)) {
						response.setEsError(true);
						response.setCodigo(Mensajes.getErrorDatosInconsistentes());
						response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
					}
				}
				else
				{
					response.setEsError(true);
					response.setCodigo(resInicioBD.getCodError());
					response.setMensaje(resInicioBD.getTextoError());
					log.error(plataformaPago+":Error en inicio de petición de pago:"+response.getMensaje());
				}
			}
		} else
		{
			response.setEsError(true);
			String mensaje= Mensajes.getText(validador.getCodigoError());
			response.setMensaje(mensaje);
			log.error(plataformaPago+":Error en validación:"+mensaje);
		}
		return response;
	}
	
	//CRUBENCVS 02/06/2021. Tratamos las posibles devoluciones.
	//Nos interesa la fecha, si se devuelve, es que cumple las condiciones 
	//para estar anulado
	private String getFechaAnulacion(QueryResponse q) throws PasarelaPagoException{
		boolean anulado=false;
		long totalDevuelto=0;
		Date ultimaFechaDevolucion=null;
		if (q.getRefundData()==null) {
			return null;
		}
		if (!"000".equals(q.getPaymentCode())){
			//Si no está pagado, no está anulado. Espero que en el futuro no lo cambien...
			anulado=false;
		} else {
			//Pagado.
			//Si hay devoluciones, tenemos que ver que el total devuelto coincida con la cantidad pagada,
			//o sea mayor.
			QueryResponse.RefundData[] devoluciones= q.getRefundData();
			if (devoluciones!=null && devoluciones.length>0){
				for (int i=0;i<devoluciones.length;i++){
					if (devoluciones[i]==null){
						continue;
					}
					try {
						totalDevuelto+= Long.parseLong(devoluciones[i].getAmount());
						SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmss");
						String fechaDevolucion=devoluciones[i].getFechaDevolucion(); 
						if (fechaDevolucion!=null && !"".equals(fechaDevolucion)){
							Date fecha=s.parse(fechaDevolucion);
							if (ultimaFechaDevolucion==null){
								ultimaFechaDevolucion=fecha;
							}
							if (ultimaFechaDevolucion.before(fecha)){
								ultimaFechaDevolucion=fecha;
							}
						}
					} catch (NumberFormatException ne){
						throw new PasarelaPagoException ("Error al procesar la cantidad devuelta ("+devoluciones[i].getAmount() +"):"+ ne.getMessage(), ne);
					} catch (ParseException pe){
						throw new PasarelaPagoException ("Error al procesar la fecha de devolución ("+devoluciones[i].getFechaDevolucion()+"):"+pe.getMessage(),pe);
					}
				}
				long pagado;
				try {
					pagado= Long.parseLong(q.getAmount());
				} catch(NumberFormatException ne){
					throw new PasarelaPagoException("Error al procesar las devoluciones, la cantidad pagada ("+q.getAmount()+") no puede convertirse a número:"+ne.getMessage(),ne);
				}
				if (pagado<=totalDevuelto){
					anulado=true;
				}
			}
		}
		if (anulado){
			SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyHHmmss");
			return s.format(ultimaFechaDevolucion);
		} else {
			return null;
		}
	}
	
	/**
	 * Nos indica si el código de error de retorno de la consulta de universalPay está 
	 * dentro de aquellos que indican
	 * que no hay operación registrada, por lo que nunca llegamos ni siquiera
	 * a solicitar el token.
	 * @param errorCode
	 * @return
	 */
	private boolean esOperacionNoRegistrada(String errorCode){
		if (errorCode==null || "".equals(errorCode)){
			return false;
		}
		String[] codigos=new String[]{"211","251"};
		for (int i=0;i<codigos.length;i++){
			if (errorCode.equals(codigos[i])){
				return true;
			}
		}
		return false;
	}
	/**
	 * Consulta de un pago con tarjeta en la plataforma de pago
	 * @param operacionEpst
	 * @return
	 * @throws PasarelaPagoException
	 */
	public RespuestaConsultaPagoTarjetaUniversalPay consultarPagoTarjetaPlataforma(String operacionEpst) throws PasarelaPagoException{
		RespuestaConsultaPagoTarjetaUniversalPay response= new RespuestaConsultaPagoTarjetaUniversalPay();
		boolean consultaCorrecta=false;
		boolean anulado=false;
		boolean pagoRealizado=false;
		// 23/09/21. Si nos indica que está pagado pero no tiene NRC, es un error.
		boolean informaPagadoSinNrc=false;
		//CRUBENCVS 11/01/2023
		boolean operacionNoRegistrada=false;
		String fechaAnulacion="";
		QueryResponse qr= mup.consultaEstadoPago(operacionEpst);
		if ("000".equals(qr.getErrorCode())){
			consultaCorrecta=true;
		} else {
			//CRUBENCVS 11/01/2023 Sin número.
			//Hay códigos en la consulta UniversalPay que  
			//indican que no consta la operación en su sistema.
			//Así sabemos a ciencia cierta que no llegamos a generar ni siquiera token
			operacionNoRegistrada=esOperacionNoRegistrada(qr.getErrorCode());
			//FIN CRUBENCVS 
		}
		if ("000".equals(qr.getPaymentCode())){
			//Podría estar anulado. 
			//Esto nos permite saber si hay una fecha de anulación en que se haya anulado
			//el total, bien sea en una sola operación o en varias de anulación
			fechaAnulacion= getFechaAnulacion(qr);
			if (fechaAnulacion!=null){
				anulado=true;
			} else {
				//22/09/21 . Aunque nos digan que está bien pagado, si no llegó
				//NRC, no lo damos por bueno.
				if (qr.getNrc()==null || "".equals(qr.getNrc())){
					consultaCorrecta=false;
					informaPagadoSinNrc=true;
				}
				pagoRealizado=true;
			}
		}
		if (consultaCorrecta){
			// 02/12/2021. Aquí la "fecha de pago" significa fecha de operación
			//así que se devuelve siempre.
			response.setFechaPago(qr.getFechaPago());
			if (pagoRealizado){
				log.info("Consulta correcta, pago realizado");
				response.setError(false);
				response.setPagoRealizado(true);
				response.setPagoAnulado(false);
				response.setOperacion(qr.getPaymentOperation());
				response.setNrc(qr.getNrc());
				response.setCodigoResultado(qr.getPaymentCode());
			} else if (anulado){
				log.info("Pago realizado y anulado");
				response.setError(false);
				response.setPagoRealizado(false);
				response.setPagoAnulado(true);
				response.setFechaAnulacion(fechaAnulacion);
				response.setCodigoResultado("000"); //Dado que está anulado, cada una de las devoluciones tenía este código "de pago". Siempre devuelven el código de pagado OK....
			} else {
				log.info("Consulta correcta, pago no realizado");
				log.info("Estado de pago en UniversalPay:"+qr.getPaymentCode());
				response.setError(false);
				response.setPagoRealizado(false);
				response.setCodigoResultado(qr.getPaymentCode());
			}
			//CRUBENCVS 01/12/2021 42479. También se devuelven datos de anulaciones realizadas, aunque
			//no conste como anulado completamente.
			response.setRefundData(qr.getRefundData());
			//CRUBENCVS 13/05/2022. Devolvemos el importe, por si en caso de los 
			//códigos 501 de devolución automática tenemos que informar una
			//anulación sobre el total del importe, y no nos la están
			//enviando en el REFUND_DATA
			response.setImporte(qr.getAmount());
		} else  if (!consultaCorrecta){
			// 23/09/21. Si se nos indica que está pagado, pero no hay NRC, es un error.
			if (informaPagadoSinNrc){
				log.info("Error, el tributo figura como pagado pero no se ha devuelto el NRC");
				response.setCodigoResultado("100"); //El error de pago general. En realidad, nos vale cualquier código
			}else {
				if (!operacionNoRegistrada){ //Caso especial. 
					log.info("No consta la operación en UniversalPay:"+qr.getErrorCode());
				} else {
					log.info("Error recibido en consulta UniversalPay:"+qr.getErrorCode());
				}
				response.setCodigoResultado(qr.getErrorCode());
			}
			//CRUBENCS 11/01/2023
			response.setOperacionNoRegistradaUpay(operacionNoRegistrada);
			response.setError(true);
			response.setPagoRealizado(false);
		} 
		return response;
	}
	
	/**
	 * Sincroniza la información entre BD y UniversalPay
	 * @param datosPate
	 * @return
	 * @throws PasarelaPagoException
	 */
	public ResultadoSincronizacionUniversalPayBD sincronizarEstadoBDConPlataforma(DatosPagoBD datosPate) throws PasarelaPagoException{
		ResultadoSincronizacionUniversalPayBD result=new ResultadoSincronizacionUniversalPayBD();
		try {
			RespuestaConsultaPagoTarjetaUniversalPay rcp= this.consultarPagoTarjetaPlataforma(datosPate.getOperacionEpst());
			result.setError(false);
			result.setResultadoConsulta(rcp);
			if (rcp.isError()){
				result.setError(true);
			} else if (rcp.isPagoRealizado()){
				result.setTributoPagado(true);
			} else if (rcp.isPagoAnulado()){
				result.setTributoAnulado(true);
			}
			
			if (!rcp.isError()){
				//Intentamos actualizar el registro con el resultado remoto
				String estadoActualizacion;
				if (rcp.isPagoRealizado()){
					estadoActualizacion="P";
					
				} else if (rcp.isPagoAnulado()) 
				{
					estadoActualizacion="A";
				} else {
					estadoActualizacion="E"; //Otro caso es que el pago no está hecho.
				}
				String fechaPago="";
				try {
					if (rcp.getFechaPago()!=null && !"".equals(rcp.getFechaPago())){
						SimpleDateFormat sd= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						
						Date f= sd.parse(rcp.getFechaPago());
						sd= new SimpleDateFormat("yyyy-MM-dd");
						fechaPago=sd.format(f);
					}
				} catch (Exception e){
					throw new PasarelaPagoException("No se ha podido terminar la sincronización. Problema con el formato de la fecha de pago");
				}
				GestorLlamadasBD gbd= BDFactory.newGestorLlamadasBD(context);
				ResultadoLlamadaBD resBD=gbd.actualizarBD(
						         datosPate.getPasarelaPago(), 
								 estadoActualizacion, 
								 datosPate.getJustificante(), 
								 datosPate.getIdentificacion(), 
								 datosPate.getReferencia(), 
								 rcp.getOperacion(), 
								 fechaPago, 
								 rcp.getNrc(), 
								 datosPate.getNifOperante(), 
								 datosPate.getFechaDevengo(), 
								 rcp.getCodigoResultado(),
								 rcp.getFechaAnulacion()
								 );
				if (resBD.isError()){
					result.setError(true);
					result.setCodError(resBD.getCodError());
					result.setTextoError(resBD.getTextoError());
				} else {
					result.setError(false); //Redundante porque es el valor por defecto
				}
			} else {
				//CRUBENCVS 11/01/2023
				if (rcp.isOperacionNoRegistradaUpay()){
					//Este es un caso especial. No consta operación.
					//Tenemos que seguir considerándolo un error de sincronización,
					//para cambiar el mínimo código, aunque en realidad no lo es como tal...
					//Convierte más bien el "error de sincronización" en "no ha habido sincronización"
					log.error ("No se realiza sincronización con el estado remoto, ya que no consta la operación en UniversalPay");
					result.setError(true);
					result.setOperacionNoRegistradaUpay(true);
				} else {
					log.error ("Error al sincronizar el estado de pago local con el de UniversalPay");
					result.setError(true);
					result.setCodError(Mensajes.getErrorConsulta());
					result.setTextoError(Mensajes.getExternalText(result.getCodError()));
				}
			}
		} catch(Exception e){
			log.error ("Error al sincronizar el estado de pago local con el de UniversalPay:"+ e.getMessage());
			result.setError(true);
			result.setCodError(Mensajes.getErrorConsulta());
			result.setTextoError(Mensajes.getExternalText(result.getCodError()));
		}
		return result;
	}
	
	/**
	 * Valida la firma de la recepción de pago
	 * @param respuesta respuesta Json del servicio
	 * @return
	 */
	private boolean validarSignatureRecepcionPago(PaymentResponseJsonObject respuesta){
		try {
			String datos= respuesta.getMerchantIdentifier()+respuesta.getPaymentAmount()+
						  respuesta.getMerchantOperation() + respuesta.getPaymentOperation()+ 
						  respuesta.getPaymentChannel()+ respuesta.getPaymentDate()
						  +respuesta.getPaymentCode();
			return mup.validarSignature(datos, respuesta.getSignature());
		} catch (Exception e){
			this.log.error("Error en la validación de firma de respuesta de pago:" + e.getMessage());
			return false;
		}
	}
	
	public void recuperaMetadatos(){
		
	}
	/**
	 * Permite invocar a la finalización de pago por tarjeta de un pago de UniversalPay
	 * @param operacionEpst
	 * @param estado
	 * @param resultado
	 * @param numOperacion
	 * @param nrc
	 * @param fechaPago
	 * @param emisora
	 * @param justificante
	 * @param identificacion
	 * @param referencia
	 * @return
	 * @throws PasarelaException
	 */
	public boolean finalizarPagoTarjeta(
					String operacionEpst,
					String estado,
					String resultado,
					String numOperacion,
					String nrc,
					String fechaPago,
					String emisora,
					String justificante,
					String identificacion,
					String referencia
					) throws PasarelaPagoException{
		log.info("Inicio de Actualización de estado de pago");
	    //Actualizo en base de datos. 
	    GestorLlamadasBD gbd= BDFactory.newGestorLlamadasBD(context);
	    ResultadoLlamadaBD resBD= gbd.finalizarPagoTarjeta(
	    						operacionEpst, 
	    						estado,
	    						resultado, 
	    						numOperacion, 
	    						nrc,  
	    						fechaPago,
	    						//CRUBENCVS 11/11/2021
	    						emisora,
	    						justificante,
	    						identificacion,
	    						referencia,
	    						// FIN CRUBENCVS 11/11/2021
	    						propiedades.getProperty("PROC_ALMACENADO_FINALIZAR_PAGO_TARJETA")
	    						);
	    if (!resBD.isError()){
			log.info("Fin de Actualización de estado de pago");
			return true;
		} else {
			log.info("Error de actualización en base de datos:" + resBD.getCodError()+":"+resBD.getTextoError());
			return false;
		}
	}
	/**
	 * Recibe el resultado de una operación de pago, en formato JSON
	 * @param json
	 */
	public String recibirResultadoPago(String json) throws PasarelaPagoException{
		String respuesta="";
		MediadorUniversalPay mup = new MediadorUniversalPay(this.propiedades, this.context);
	    PaymentResponseJsonObject paymentResponse= mup.leerRespuestaPagoResponse(json);
	    //Validar SIGNATURE
	    log.info("Validación de firma");
	    if (validarSignatureRecepcionPago(paymentResponse)){
	    	log.info("Firma Válida");
		    String estado="";
		    if ("000".equals(paymentResponse.getPaymentCode())){
		    	estado="P";
		    }else { 
		    	estado="E";
		    }
		    if (finalizarPagoTarjeta(
						paymentResponse.getMerchantOperation(), 
						estado,
						paymentResponse.getPaymentCode(), 
						paymentResponse.getPaymentOperation(), 
						paymentResponse.getParams().getNrc(),  
						paymentResponse.getPaymentDate(),
						paymentResponse.getMetadatos().getEmisora(),
						paymentResponse.getMetadatos().getJustificante(),
						paymentResponse.getMetadatos().getIdentificacion(),
						paymentResponse.getMetadatos().getReferencia()
						)
				){
		    	respuesta="OK";
		    } else {
		    	respuesta="KO";
		    }
	    } else 
	    {
	    	log.info("Firma no válida");
	    	respuesta="KO";
	    }
		return respuesta;
	}
	
	public RespuestaAnulacionTarjeta anularPagoTarjeta(
									    String emisora,
									    String numeroAutoliquidacion,
									    String identificacion,
									    String referencia,
									    String importe,
									    String paymentOperation
										) throws PasarelaPagoException{
		log.info(plataformaPago+":Anulación de pago con tarjeta.");
		RespuestaAnulacionTarjeta response= new RespuestaAnulacionTarjeta();
		GestorLlamadasBD gbd = BDFactory.newGestorLlamadasBD(context);
		boolean anulado=false;
		String codigoResultado;
		ResultadoLlamadaBD resInicioAnulacion= gbd.inicioAnulacionPagoTarjeta
											   (
											      emisora,
											      numeroAutoliquidacion,
											      identificacion,
											      referencia,
											      this.propiedades.getProperty("PROC_ALMACENADO_INICIAR_ANULACION")
										       );
		if (!resInicioAnulacion.isError()){
			MediadorUniversalPay mup = new MediadorUniversalPay(this.propiedades, this.context);
			//En universalPay, la anulación es en realidad una devolución por el total
			RefundResponse rr= mup.realizaDevolucion(importe, paymentOperation);
			//ErrorCode= 000 . Mensaje intercambiado de forma correcta, pero no tiene por qué haberse anulado
			if ("000".equals(rr.getErrorCode())){
				if ("000".equals(rr.getRefundCode())){
					anulado=true;
					codigoResultado= rr.getRefundCode();
				} else {
					codigoResultado=rr.getRefundCode();
				}
			} else {
				codigoResultado= rr.getErrorCode();
			}
			//Actualizamos el estado
			String estado="";
			if (anulado){
				estado=EstadosPago.ANULADO.getValor();
			} else {
				estado=EstadosPago.ANULACION_COMENZADA.getValor(); //Lo dejamos como comenzado
			}
			ResultadoLlamadaBD resFinalizar= gbd.finalizaAnulacionPagoTarjeta(
												emisora,
											    numeroAutoliquidacion,
											    identificacion,
											    referencia,
											    estado,
											    codigoResultado,
											    this.propiedades.getProperty("PROC_ALMACENADO_FINALIZAR_ANULACION")
												);
			if (!resFinalizar.isError()){
				if (EstadosPago.ANULADO.getValor().equals(estado)){
					log.info ("Tributo anulado en entidad remota.");
					response.setEsError(false);
				} else {
					log.info("Tributo no anulado en entidad remota");
					response.setEsError(true);
				}
			}else {
				log.error(Mensajes.getErrorActualizacionBD()+".-Error al actualizar la petición en base de datos:"+resFinalizar.getCodError());
				response.setCodigo(Mensajes.getErrorActualizacionBD());
				response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
				response.setEsError(true);
			}
		} else {
			log.error(Mensajes.getErrorActualizacionBD()+".-Error al iniciar la anulación en base de datos:"+resInicioAnulacion.getCodError());
			response.setCodigo(Mensajes.getErrorActualizacionBD());
			response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
			response.setEsError(true);
		}
		return response;
	}
	/**
	 * Para conocer cuál es el código de entidad de pago equivalente a UniversalPay.
	 * @return Entidad de pago equivalente
	 */
	public String getEntidadPagoEquivalente(){
		return propiedades.getProperty("ENTIDAD_EQUIVALENTE");
	}
	
}
