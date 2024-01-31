package es.tributasenasturias.tarjetas.inicioPagoTarjeta;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.plataforma.unipay.UniversalPay;
import es.tributasenasturias.plataforma.unipay.UniversalPay.RespuestaInicioPagoTarjeta;
import es.tributasenasturias.tarjetas.core.PeticionPagoTarjetaParameters;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.CallContextManager;
import es.tributasenasturias.webservices.types.InicioPagoTarjetaRequest;
import es.tributasenasturias.webservices.types.InicioPagoTarjetaResponse;
import es.tributasenasturias.webservices.types.UniversalPayTokenRequest_ResponseObject;

/**
 * Implementación del inicio de pago por tarjeta para UniversalPay
 * @author crubencvs
 *
 */
class InicioPagoTarjetaUniversalPay extends InicioPagoTarjetaHandler {

	private final String nombrePasarela="UniversalPay";
	
	public InicioPagoTarjetaUniversalPay(InicioPagoTarjetaContexto contexto){
		super(contexto);
	}
	
	private InicioPagoTarjetaRequest convert(PeticionPagoTarjetaParameters ppt){
		return new InicioPagoTarjetaRequest(ppt.getOrigen(), 
										 ppt.getModalidad(), 
										 ppt.getEntidad(), 
										 ppt.getEmisora(), 
										 ppt.getModelo(), 
										 ppt.getNifContribuyente(), 
										 ppt.getFechaDevengo(), 
										 ppt.getDatoEspecifico(), 
										 ppt.getIdentificacion(), 
										 ppt.getReferencia(), 
										 ppt.getNumeroAutoliquidacion(), 
										 ppt.getExpediente(), 
										 ppt.getImporte(), 
										 ppt.getNifOperante(), 
										 ppt.getPlataformaPago());
	}
	
	@Override
	public InicioPagoTarjetaResponse ejecutar() throws PasarelaPagoException{
		//La implementación de UniversalPay necesita un contexto de llamada,
		//con las propiedades necesarias para funcionar. Como no lo voy a 
		//cambiar, creo aquí el contexto.
		CallContext cc = CallContextManager.newCallContext();
		
		cc.setItem(CallContextConstants.ID_SESION, contexto.getIdSesion());
		cc.setItem(CallContextConstants.PREFERENCIAS, contexto.getPref());
		cc.setItem(CallContextConstants.LOG_APLICACION, contexto.getLog());
		
		InicioPagoTarjetaResponse response= new InicioPagoTarjetaResponse();
		
		response.setPlataformaPago(nombrePasarela);
		
		UniversalPay up= new UniversalPay(cc);
		RespuestaInicioPagoTarjeta ript= up.iniciarPagoTarjeta(convert(contexto.getPeticion()));
		if (!ript.isError()){
			response.setEsError(false);
			response.setCodigo(ript.getCodigo());
			response.setMensaje(ript.getMensaje());
			UniversalPayTokenRequest_ResponseObject uniresponse= new UniversalPayTokenRequest_ResponseObject();
			uniresponse.setCode(ript.getUniversalPayCode());
			uniresponse.setDescription(ript.getUniversalPayDescription());
			uniresponse.setToken(ript.getUniversalPayToken());
			uniresponse.setOperation(ript.getOperacionBD());
			response.setUniversalpayResponse(uniresponse);
		} else {
			response.setEsError(true);
			response.setCodigo(ript.getCodigo());
			response.setMensaje(ript.getMensaje());
		}
		return response;
	}
	
	

}
