/**
 * 
 */
package es.tributasenasturias.objetos;

import es.tributasenasturias.plataforma.unipay.UniversalPay;
import es.tributasenasturias.plataforma.unipay.UniversalPay.RespuestaAnulacionTarjeta;
import es.tributasenasturias.plataforma.unipay.UniversalPay.ResultadoSincronizacionUniversalPayBD;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.validacion.ValidacionFactory;
import es.tributasenasturias.validacion.plataformasPago.ValidadorAnulacionPagoTarjeta;

import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.IContextReader;
import es.tributasenasturias.webservices.types.AnulacionPagoTarjetaRequest;
import es.tributasenasturias.webservices.types.AnulacionPagoTarjetaResponse;
import es.tributasenasturias.bd.BDFactory;
import es.tributasenasturias.bd.GestorLlamadasBD;
import es.tributasenasturias.bd.ResultadoConsultaPagoBD;
import es.tributasenasturias.dao.DatosPagoBD;
import es.tributasenasturias.exceptions.PasarelaPagoException;

/**
 * Clase que implementa la funcionalidad de la anulación de un pago con tarjeta por plataforma de pago.
 * 
 * 
 */
public class AnulacionPagoTarjeta implements IContextReader{

	CallContext context;

	private Preferencias preferencias;
	AnulacionPagoTarjetaRequest peticion;
	private Logger logger=null;
	
	@Override
	public CallContext getCallContext() {
		return context;
	}
	@Override
	public void setCallContext(CallContext ctx) {
		this.context=ctx;
	}
	
	private AnulacionPagoTarjeta(AnulacionPagoTarjetaRequest peticion){
		this.peticion = peticion;
	}
	
	/**
	 * Constructor a utilizar en el objeto Factory.
	 * @param datosProceso {@link AnulacionPagoTarjetaRequest}, datos de la petición.
	 * @param context Contexto de llamada.
	 */
	protected AnulacionPagoTarjeta (AnulacionPagoTarjetaRequest peticion, CallContext context) throws PasarelaPagoException
	{
		this (peticion);
		this.context=context;
		logger = (Logger) context.get(CallContextConstants.LOG_APLICACION);
		preferencias=(Preferencias) context.get(CallContextConstants.PREFERENCIAS);
		if (logger==null || preferencias==null)
		{
			throw new PasarelaPagoException ("No se ha inicializado correctamente el contexto de llamada.");
		}
	}
	
	/**
	 * Operación de anulación de pago con tarjeta
	 * @return
	 */
	public AnulacionPagoTarjetaResponse ejecutar()
	{
		long start=System.currentTimeMillis();
		AnulacionPagoTarjetaResponse response= new AnulacionPagoTarjetaResponse();
		String plataforma="";
		boolean intentarAnulacion=false;
		DatosPagoBD datosPate=new DatosPagoBD();
		logger.info("INICIO PETICIÓN DE ANULACIÓN DE PAGO POR PLATAFORMA.");
		
		try {
			ValidadorAnulacionPagoTarjeta validador= ValidacionFactory.newValidadorAnulacionPagoTarjeta(context);
			validador.validar(peticion);
			if (validador.isValido()) {
				//Consultamos el estado en base de datos. En este punto aún no necesitamos saber
				//cuál es la plataforma de pago
				logger.info ("Datos de entrada válidos.");
				GestorLlamadasBD gestorBD = BDFactory.newGestorLlamadasBD(context);
				logger.info("Consultamos estado del registro en base de datos, si existe");
				//Consulta del registro en base de datos, si hubiese.
				ResultadoConsultaPagoBD rPate= gestorBD.consultaPagoTarjetaBD(
											   peticion.getOrigen(),
											   peticion.getModalidad(), 
											   peticion.getEmisora(), 
											   peticion.getNumeroAutoliquidacion(), 
											   peticion.getIdentificacion(), 
											   peticion.getReferencia(),
											   peticion.getAplicacion(),
											   peticion.getNumeroUnico());
				if(rPate.isConsultaCorrecta()){
					
					datosPate= rPate.getDatosPagoBD(); 
					if (datosPate.getOperacionEpst()!=null && !"".equals(datosPate.getOperacionEpst())
							&& "T".equals(datosPate.getMedioPago())){
						plataforma="UniversalPay";
					}
					String estado=datosPate.getEstado();
					if (estado==null||"".equals(estado)){
						response.setCodigo(Mensajes.getErrorAnulacionPago());
						response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
						response.setEsError(true);
					} else if ("A".equals(estado)){
						logger.info("Tributo ya anulado");
						//No sé si dar un error, o simplemente decir que se anuló.
						response.setCodigo(Mensajes.getTributoNoPagado());
						response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
						response.setEsError(true);
					} else if ("G".equals(estado)){
						logger.info("Tributo grabado, no iniciado");
						//En este caso, sabemos que no pudo estar pagado porque no se inició la operación
						response.setCodigo(Mensajes.getTributoNoPagado());
						response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
						response.setEsError(true);
					} else if (!"P".equals(estado)){
						//El estado es diferente a pagado, tenemos que ir a la plataforma a consultar.
						//Como por el momento tenemos sólo una, será universal pay, pero tenemos que
						//intentar averiguarlo igualmente.
						//FIXME: Para saber la plataforma de pago, como no la guardamos, sólo 
						//podemos mirar esto. En este momento, si tiene operación y  pago con tarjeta
						//suponemos UniversalPay
						if ("UniversalPay".equalsIgnoreCase(plataforma)){
							UniversalPay upay= new UniversalPay(context);
							ResultadoSincronizacionUniversalPayBD rupay= upay.sincronizarEstadoBDConPlataforma(datosPate);
							if (rupay.isError()){
								response.setCodigo(Mensajes.getErrorConsulta());
								response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
								response.setEsError(true);
							} else if (rupay.isTributoPagado()){
								//OK, lo vamos a intentar anular
								intentarAnulacion=true;
							}
							else {
								response.setCodigo(Mensajes.getTributoNoPagado());
								response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
								response.setEsError(true);
							}
						}
					} else if ("P".equals(estado)){
						intentarAnulacion=true;
					}
					
				} else {
					response.setCodigo(Mensajes.getErrorPeticionBD());
					response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
					response.setEsError(true);
				}
				if (intentarAnulacion){
					if ("UniversalPay".equalsIgnoreCase(plataforma)){
						UniversalPay upay= new UniversalPay(context);
						RespuestaAnulacionTarjeta ra= upay.anularPagoTarjeta(
								               peticion.getEmisora(), 
											   peticion.getNumeroAutoliquidacion(), 
											   peticion.getIdentificacion(), 
											   peticion.getReferencia(), 
											   datosPate.getImporte(), 
											   datosPate.getNumeroOperacion()
											   );	
						if (!ra.isEsError()){
							response.setCodigo(Mensajes.getTributoAnulado());
							response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
							response.setEsError(false);
						} else {
							response.setEsError(true);
							response.setCodigo(Mensajes.getErrorAnulacion());
							response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
						}
					} else {
						response.setCodigo(Mensajes.getErrorNoPlataforma());
						response.setMensaje(Mensajes.getExternalText(response.getCodigo()));
						response.setEsError(true);
					}
				}
				
			} else { //No valida
				response.setEsError(true);
				response.setMensaje(Mensajes.getText(validador.getCodigoError()));
				response.setCodigo(validador.getCodigoError());
				logger.error("Error en validación:"+response.getCodigo()+":" + response.getMensaje());
			}
		}
		catch (PasarelaPagoException ex)
		{
			logger.error(ex.getMessage());
			response.setCodigo(Constantes.getErrorCode());
			response.setMensaje(Mensajes.getFatalError());
			response.setEsError(true);
			logger.error (response.getMensaje());
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage());
			logger.trace(ex.getStackTrace());
			response.setCodigo(Constantes.getErrorCode());
			response.setMensaje(Mensajes.getFatalError());
			logger.error(response.getMensaje());
		}
		long endTime=System.currentTimeMillis();
		logger.debug("FINAL PETICIÓN EN " + Long.toString((endTime-start)) + " MS RETORNANDO COMO RESULTADO -> "+response.getCodigo() + " "+ response.getMensaje());
		return response;
	}
	
	
	
	
}
