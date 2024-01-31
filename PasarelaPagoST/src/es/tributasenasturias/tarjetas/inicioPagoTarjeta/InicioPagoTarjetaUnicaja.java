package es.tributasenasturias.tarjetas.inicioPagoTarjeta;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.tarjetas.core.PateRecord;
import es.tributasenasturias.tarjetas.core.PeticionPagoTarjetaParameters;
import es.tributasenasturias.tarjetas.core.db.InicioPagoTarjetaException;
import es.tributasenasturias.tarjetas.core.db.PLPasarelaRepository;
import es.tributasenasturias.tarjetas.core.db.PLPasarelaRepositoryException;
import es.tributasenasturias.tarjetas.core.sync.SincronizacionException;
import es.tributasenasturias.tarjetas.unicajaInterfaz.inicioPago.InicioPagoUnicajaExecutor;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.webservices.types.InicioPagoTarjetaResponse;
import es.tributasenasturias.webservices.types.UnicajaValidacionPagoResponse;

/**
 * Implementación del inicio de pago por tarjeta para UniversalPay
 * @author crubencvs
 *
 */
class InicioPagoTarjetaUnicaja extends InicioPagoTarjetaHandler {

	private final String nombrePasarela="unicaja";
	
	public InicioPagoTarjetaUnicaja(InicioPagoTarjetaContexto contexto){
		super(contexto);
	}
	
	
	
	@Override
	public InicioPagoTarjetaResponse ejecutar() throws PasarelaPagoException{
		
		String idSesion = contexto.getIdSesion();
		Logger log= contexto.getLog();
		Preferencias pref= contexto.getPref(); 
		InicioPagoTarjetaResponse response=null;
		EstadoProceso estadoProceso= EstadoProceso.COMIENZO;
		
		PeticionPagoTarjetaParameters peticion = contexto.getPeticion();
		PateRecord pate=null;
		PLPasarelaRepository repository= new PLPasarelaRepository(pref, log, contexto.getIdSesion());
		int numIteraciones=0; //Para evitar errores de programación en el bucle de estados
		
		try {
			//Si hubiese un error de programación, evitamos que entre en bucle infinito.
			while (estadoProceso!=EstadoProceso.FIN ){
				if (numIteraciones++ >= 10){
					throw new InicioPagoTarjetaException("9999","Error de programación. No se resuelve correctamente el bucle de estados de la operación de inicio de pago");
				}
				
					switch (estadoProceso){
						case COMIENZO:{
							log.info("COMIENZO del proceso contra Unicaja Banco");
							//Recupero los datos del registro en la base, si existe.
							//Recupero una versión que ya sé que está sincronizada con la entidad remota,
							//para así trabajar con los datos más actuales.
							pate= repository.getRegistroSincronizadoBD(contexto.getPeticion().getEmisora(),
																				  contexto.getPeticion().getNumeroAutoliquidacion(),
																				  contexto.getPeticion().getIdentificacion(),
																				  contexto.getPeticion().getReferencia(),
																				  contexto.getPeticion().getAplicacion(),
																				  contexto.getPeticion().getNumeroUnico()
							);
							estadoProceso= EstadoProceso.SINCRONIZADO;
							break;
						}
						case SINCRONIZADO:{
							log.info ("Recuperado el registro de pago SINCRONIZADO con Unicaja Banco");
							if (pate!=null){
								//Al recuperar antes el registro sincronizado, si los datos no son consistentes,
								//puede haber habido un acceso a la entidad remota para comprobar el estado.
								//Esto puede ser correcto según la lógica que se quiera tener. Si se comprueba
								//primero la consistencia, hay que acceder a base de datos, comprobar la consistencia,
								//y después acceder a la entidad de pago para sincronizar el estado.
								if (!datosConsistentes(pate)){
									estadoProceso = EstadoProceso.DATOS_INCONSISTENTES;
								}
								else if ("P".equalsIgnoreCase(pate.getEstado())){
									estadoProceso= EstadoProceso.PAGADO;
								} else {
									estadoProceso= EstadoProceso.GENERACION_TOKEN;
								}
							} else {
								estadoProceso= EstadoProceso.GENERACION_TOKEN;
							}
							break;
						}
						case DATOS_INCONSISTENTES:{
							log.error("DATOS INCONSISTENTES");
							response= datosInconsistentes();
							estadoProceso= EstadoProceso.FIN;
							break;
						}
						case PAGADO:{
							log.info ("El tributo ya estaba PAGADO");
							response=tributoYaPagado();
							estadoProceso= EstadoProceso.FIN;
							break;
						}
						case GENERACION_TOKEN:{
							log.info ("GENERACIÓN DE TOKEN");
							InicioPagoUnicajaExecutor exec = new InicioPagoUnicajaExecutor(pref, log, idSesion);
							InicioPagoUnicajaExecutor.ResultadoInicioPago resExecutor = exec.iniciarPagoUnicaja(peticion);
							PateRecord pateIniciado = resExecutor.getPateRecord();
							
							if ("P".equalsIgnoreCase(pateIniciado.getEstado())){
								response=tributoYaPagado();
							} else {
								//El resto de casos deberían tener un resultado de petición de token
								UnicajaValidacionPagoResponse uvr= resExecutor.getUnicajaResponse();
								if (uvr!=null){
									if (uvr.getErrorCode()!=null && !"".equals(uvr.getErrorCode().trim())){
										log.error ("Error en la generación de token(número de pedido):" + uvr.getMoreInformation());
										response= errorInicioPago();
									} else {
										response=OperacionOk();
										response.setUnicajaResponse(uvr);
									}
								} else {
									log.error ("Error en la generación de token(número de pedido), no se han recibido datos");
									response= errorInicioPago();
								}
							}
							estadoProceso = EstadoProceso.FIN;
							break;
						}
						case FIN:{ //Este ya no lo tratará, se supone
							break;
						}
					}
			}
			response.setPlataformaPago(nombrePasarela);
			
		} catch (SincronizacionException se){
			log.error("No se ha podido sincronizar el estado de BD con la entidad remota. Revise el log para más información:" + se.getMessage());
			response= errorConsultaEntidad();
		} catch (PLPasarelaRepositoryException r){
			log.error ("Error en acceso a BD:"+ r.getMessage());
			response= errorBD();
		} catch (InicioPagoTarjetaException ini){
			if (ini.getCodigoError()!=null && !"".equals(ini.getCodigoError())){
				log.error ("Error en el inicio de pago por tarjeta:" + ini.getCodigoError() + " " + ini.getMensajeError());
			} else {
				log.error ("Error en el inicio de pago por tarjeta: " + ini.getMessage());
			}
			response= errorInicioPago();  
		}
		
		return response;
	}

	/**
	 * Comprueba si los datos de la petición son consistentes con los recuperados de la base de datos
	 * @param pate
	 * @return
	 */
	private boolean datosConsistentes(PateRecord pate){
		boolean consistente=true;
		if (pate!=null){
			consistente &= contexto.getPeticion().getEmisora().equals(pate.getEmisora());
			consistente &= Long.valueOf(contexto.getPeticion().getImporte()).equals(Long.valueOf(pate.getImporte()));
			consistente &= contexto.getPeticion().getNumeroAutoliquidacion().equals( pate.getJustificante());
			consistente &= contexto.getPeticion().getIdentificacion().equals(pate.getIdentificacion());
			consistente &= contexto.getPeticion().getReferencia().equals(pate.getReferencia());
			consistente &= contexto.getPeticion().getNifContribuyente().toUpperCase().equals(pate.getNif().toUpperCase());
			consistente &= contexto.getPeticion().getDatoEspecifico().toUpperCase().equals(pate.getDatoEspecifico().toUpperCase());
			consistente &= contexto.getPeticion().getExpediente().toUpperCase().equals(pate.getExpediente().toUpperCase());
		}
		return consistente;
	}
	
	private InicioPagoTarjetaResponse setResponse(boolean esError,
            String codigo,
            String mensaje
            )
	{
		InicioPagoTarjetaResponse response= new InicioPagoTarjetaResponse();
		response.setEsError(esError);
		response.setCodigo(codigo);
		response.setMensaje(mensaje);
		return response;
	}
	
	private InicioPagoTarjetaResponse OperacionOk(){
	
		return setResponse(false, Mensajes.getOk(),Mensajes.getExternalText(Mensajes.getOk()));
	}
	
	
	private InicioPagoTarjetaResponse datosInconsistentes(){
		return setResponse(true, Mensajes.getErrorDatosInconsistentes(),
		Mensajes.getExternalText(Mensajes.getErrorDatosInconsistentes())
		);
	}
	
	private InicioPagoTarjetaResponse errorConsultaEntidad(){
		return setResponse(true, Mensajes.getErrorConsulta(),
		Mensajes.getExternalText(Mensajes.getErrorConsulta())
		);
	}
	
	private InicioPagoTarjetaResponse errorBD(){
		return setResponse(true, Mensajes.getErrorBD(),
		Mensajes.getExternalText(Mensajes.getErrorBD())
		); 
	}
	
	private InicioPagoTarjetaResponse tributoYaPagado(){
		return setResponse(true, Mensajes.getTributoPagadoYa(),
		Mensajes.getExternalText(Mensajes.getTributoPagadoYa()));
	}
	
	private InicioPagoTarjetaResponse errorInicioPago(){
		return setResponse(true, Mensajes.getErrorInicioPagoTarjeta(),
		Mensajes.getExternalText(Mensajes.getErrorInicioPagoTarjeta()));
	}
	
	private static enum EstadoProceso{
		COMIENZO,
		SINCRONIZADO,
		DATOS_INCONSISTENTES,
		PAGADO,
		GENERACION_TOKEN,
		FIN
	}
	
}
