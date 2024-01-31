/**
 * 
 */
package es.tributasenasturias.objetos;

import es.tributasenasturias.tarjetas.core.PeticionPagoTarjetaParameters;
import es.tributasenasturias.tarjetas.inicioPagoTarjeta.InicioPagoTarjetaContexto;
import es.tributasenasturias.tarjetas.inicioPagoTarjeta.InicioPagoTarjetaHandlerFactory;
import es.tributasenasturias.tarjetas.inicioPagoTarjeta.InicioPagoTarjetaHandlerInterface;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.validacion.ValidacionFactory;
import es.tributasenasturias.validacion.plataformasPago.ValidadorInicioPagoTarjeta;

import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.IContextReader;
import es.tributasenasturias.webservices.types.InicioPagoTarjetaRequest;
import es.tributasenasturias.webservices.types.InicioPagoTarjetaResponse;
import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.exceptions.PasarelaPagoException;

/**
 * Clase que implementa la funcionalidad del inicio de un pago con tarjeta por plataforma de pago.
 * 
 * 
 */
public class InicioPagoTarjeta implements IContextReader{

	CallContext context;

	private Preferencias preferencias;
	InicioPagoTarjetaRequest peticion;
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
	
	private InicioPagoTarjeta(InicioPagoTarjetaRequest peticion){
		this.peticion = peticion;
	}
	
	/**
	 * Constructor a utilizar en el objeto Factory.
	 * @param datosProceso {@link DatosEntradaServicio}, datos de la petición.
	 * @param context Contexto de llamada.
	 */
	protected InicioPagoTarjeta (InicioPagoTarjetaRequest peticion, CallContext context) throws PasarelaPagoException
	{
		this (peticion);
		this.context=context;
		logger = (Logger) context.get(CallContextConstants.LOG_APLICACION);
		preferencias=(Preferencias) context.get(CallContextConstants.PREFERENCIAS);
		idSesion= (String) context.get(CallContextConstants.ID_SESION);
		if (logger==null || preferencias==null)
		{
			throw new PasarelaPagoException ("No se ha inicializado correctamente el contexto de llamada.");
		}
	}
	
	public InicioPagoTarjetaResponse ejecutar()
	{
		long start=System.currentTimeMillis();
		InicioPagoTarjetaResponse response= new InicioPagoTarjetaResponse();
		logger.info("INICIO PETICIÓN DE PAGO CON TARJETA.");
		
		try {
			logger.info("Validación de datos de entrada.");
			ValidadorInicioPagoTarjeta validador= ValidacionFactory.newValidadorOperacionInicioPagoTarjeta(preferencias, logger);
			validador.validar(peticion);
			if (validador.isValido())
			{
				
				InicioPagoTarjetaContexto inicioPagoTarjetaContexto = 
							new InicioPagoTarjetaContexto.ContextoInicioPagoTarjetaBuilder()
							.setPreferencias(this.preferencias)
							.setLog(this.logger)
							.setIdSesion(this.idSesion)
							.setPeticion(new PeticionPagoTarjetaParameters(this.peticion))
							.build();
				
				//Escogeremos automáticamente UniversalPay o Unicaja en base al contexto
				InicioPagoTarjetaHandlerInterface pagoHandler= InicioPagoTarjetaHandlerFactory.getInicioPagoTarjetaHandler(inicioPagoTarjetaContexto);
				response= pagoHandler.ejecutar();
				
			} else {
				response.setEsError(true);
				String mensaje= Mensajes.getText(validador.getCodigoError());
				response.setMensaje(mensaje);
				logger.error("Error en validación:"+mensaje);
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
