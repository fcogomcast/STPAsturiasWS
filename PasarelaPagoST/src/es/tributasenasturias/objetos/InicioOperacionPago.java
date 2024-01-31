/**
 * 
 */
package es.tributasenasturias.objetos;


import es.tributasenasturias.tarjetas.core.PateRecord;
import es.tributasenasturias.tarjetas.core.db.PLPasarelaRepository;
import es.tributasenasturias.tarjetas.inicioOperacionPago.InicioOperacionPagoContexto;
import es.tributasenasturias.tarjetas.inicioOperacionPago.InicioOperacionPagoHandlerFactory;
import es.tributasenasturias.tarjetas.inicioOperacionPago.InicioOperacionPagoHandlerInterface;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.validacion.ValidacionFactory;
import es.tributasenasturias.validacion.plataformasPago.ValidadorInicioOperacionPago;

import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.IContextReader;
import es.tributasenasturias.webservices.types.InicioOperacionPagoRequest;
import es.tributasenasturias.webservices.types.InicioOperacionPagoResponse;
import es.tributasenasturias.bd.BDFactory;
import es.tributasenasturias.bd.GestorLlamadasBD;
import es.tributasenasturias.exceptions.PasarelaPagoException;

/**
 * Clase que implementa la funcionalidad del inicio de operación de pago para PA
 * 
 * 
 */
public class InicioOperacionPago implements IContextReader{

	CallContext context;

	private Preferencias preferencias;
	InicioOperacionPagoRequest peticion;
	private Logger logger=null;
	private String idSesion;
	
	@Override
	public CallContext getCallContext() {
		return context;
	}
	@Override
	public void setCallContext(CallContext ctx) {
		this.context=ctx;
	}
	
	private InicioOperacionPago(InicioOperacionPagoRequest peticion){
		this.peticion = peticion;
	}
	
	/**
	 * Constructor a utilizar en el objeto Factory.
	 * @param datosProceso {@link InicioOperacionPagoRequest}, datos de la petición.
	 * @param context Contexto de llamada.
	 */
	protected InicioOperacionPago (InicioOperacionPagoRequest peticion, CallContext context) throws PasarelaPagoException
	{
		this (peticion);
		this.context=context;
		logger = (Logger) context.get(CallContextConstants.LOG_APLICACION);
		preferencias=(Preferencias) context.get(CallContextConstants.PREFERENCIAS);
		idSesion = (String) context.get(CallContextConstants.ID_SESION);
		if (logger==null || preferencias==null)
		{
			throw new PasarelaPagoException ("No se ha inicializado correctamente el contexto de llamada.");
		}
	}
	/**
	 * Genera la MAC para la respuesta
	 * @param origen Origen de la petición, para saber la clave que se ha de aplicar para generar la MAC
	 * @param r Respuesta de la operación "Ejecutar"
	 * @return
	 */
	private String generarMACResponse(String origen, InicioOperacionPagoResponse r){
		GestorLlamadasBD gestorBD = BDFactory.newGestorLlamadasBD(context);
		try {
			String justificante= r.getJustificante()==null?"":r.getJustificante();
			String nrc= r.getNrc()==null?"":r.getNrc();
			String hash= r.getHash()==null?"":r.getHash();
			String pagado= r.getPagado()==null?"":r.getPagado();
			String numeroOperacion= r.getNumeroOperacion()==null?"":r.getNumeroOperacion();
			String fechaPago= r.getFechaPago()==null?"":r.getFechaPago();
			return gestorBD.generarMac(origen,
					                    r.getEsError()+
										r.getCodigo()+
										r.getMensaje()+
										pagado+
										fechaPago+
										justificante+
										nrc+
										numeroOperacion+
										hash
										);
		} catch (Exception e){
			logger.error("Error al generar la MAC:"+e.getMessage());
			return "";
		}
	}
	/**
	 * Operación de inicio de Operación de Pago
	 * @return
	 */
	/**
	 * Operación de inicio de Operación de Pago
	 * @return
	 */
	public InicioOperacionPagoResponse ejecutar()
	{
		long start=System.currentTimeMillis();
		InicioOperacionPagoResponse response;
		logger.info("INICIO PETICIÓN DE OPERACIÓN DE PAGO.");
		try {
			logger.info("Validación de datos");
			ValidadorInicioOperacionPago validador= ValidacionFactory.newValidadorInicioOperacionPago(context);
			validador.validar(peticion);
			if (validador.isValido()) {
				InicioOperacionPagoContexto inicioOperacionContexto = new InicioOperacionPagoContexto.InicioOperacionPagoBuilder()
													   .setIdSesion(this.idSesion)
													   .setPreferencias(this.preferencias)
													   .setLog(this.logger)
													   .setPeticion(this.peticion)
													   .build();
				logger.debug("Consulta del registro de base de datos, si hubiese, sincronizando con la pasarela remota");
				PLPasarelaRepository plRepository= new PLPasarelaRepository(this.preferencias, this.logger, this.idSesion);
				PateRecord pr=plRepository.getRegistroSincronizadoBD(peticion.getEmisora(), 
																	 "", 
																	 "", 
																	 "", 
																	 peticion.getAplicacion(), 
																	 peticion.getNumeroUnico());
				InicioOperacionPagoHandlerInterface handler= InicioOperacionPagoHandlerFactory.getInicioOperacionPagoHandler(inicioOperacionContexto, pr);
				response= handler.ejecutar();
			} else { //No valida
				response= new InicioOperacionPagoResponse();
				response.setEsError("S");
				response.setMensaje(Mensajes.getText(validador.getCodigoError()));
				response.setCodigo(validador.getCodigoError());
				logger.error ("Validación finalizada. Mensaje no válido:" + response.getCodigo() + "=="+response.getMensaje());
			}
		} catch (Exception e){
			response= new InicioOperacionPagoResponse();
			response.setCodigo(Constantes.getErrorCode());
			response.setMensaje(Mensajes.getFatalError());
			response.setEsError("S");
			logger.error (response.getMensaje());
		}
		long endTime=System.currentTimeMillis();
		logger.debug("FINAL PETICIÓN EN " + Long.toString((endTime-start)) + " MS RETORNANDO COMO RESULTADO -> "+response.getCodigo() + " "+ response.getMensaje());
		response.setMac(generarMACResponse(peticion.getOrigen(), response));
		return response;
	}
	
	
	
	
}
