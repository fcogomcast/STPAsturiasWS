package es.tributasenasturias.tarjetas.unicajaInterfaz.inicioPago;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.pasarelas.unicaja.PreferenciasUnicaja;
import es.tributasenasturias.tarjetas.core.PateRecord;
import es.tributasenasturias.tarjetas.core.PeticionPagoTarjetaParameters;
import es.tributasenasturias.tarjetas.core.db.InicioPagoTarjetaException;
import es.tributasenasturias.tarjetas.core.db.PLPasarelaRepository;
import es.tributasenasturias.tarjetas.core.db.PLPasarelaRepositoryException;
import es.tributasenasturias.tarjetas.unicajaInterfaz.MediacionUnicajaException;
import es.tributasenasturias.tarjetas.unicajaInterfaz.MediadorUnicaja;
import es.tributasenasturias.tarjetas.unicajaInterfaz.token.SalidaOpGenerarTokenUnicaja;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.webservices.types.UnicajaValidacionPagoResponse;

/**
 * Clase para implementar el inicio de pago de Unicaja
 * @author crubencvs
 *
 */
public class InicioPagoUnicajaExecutor {

	private Preferencias preferencias;
	private Logger log;
	private String idSesion;
	
	public InicioPagoUnicajaExecutor(Preferencias preferencias, Logger log, String idSesion){
		this.preferencias= preferencias;
		this.log= log;
		this.idSesion= idSesion;
	}
	
	/**
	 *  Resultado de la operación de inicio de pago
	 * @author crubencvs
	 *
	 */
	public static class ResultadoInicioPago{
		private final PateRecord pateRecord;
		private final UnicajaValidacionPagoResponse unicajaResponse;
		
		private  ResultadoInicioPago(PateRecord pateRecord, UnicajaValidacionPagoResponse response){
			this.pateRecord= pateRecord;
			this.unicajaResponse= response;
		}


		public final PateRecord getPateRecord() {
			return pateRecord;
		}

		public final UnicajaValidacionPagoResponse getUnicajaResponse() {
			return unicajaResponse;
		}
		
	}

	/**
	 * Convierte del objeto con los datos de la validación de pago, {@link SalidaOpGenerarTokenUnicaja} a 
	 * objeto que entiende quien llama a esta operación, {@link UnicajaValidacionPagoResponse}
	 * @param respuesta Respuesta de unicaja
	 * @return
	 */
	private UnicajaValidacionPagoResponse convert (SalidaOpGenerarTokenUnicaja respuesta){
		UnicajaValidacionPagoResponse r = new UnicajaValidacionPagoResponse();
		r.setNumPedido(respuesta.getNumPedido());
		r.setMerchantId(respuesta.getMerchantId());
		r.setIdTerminal(respuesta.getIdTerminal());
		r.setAdquirerBin(respuesta.getAdquirerBin());
		r.setImporte(respuesta.getImporte());
		r.setMoneda(respuesta.getMoneda());
		r.setExponente(respuesta.getExponente());
		r.setCifrado(respuesta.getCifrado());
		r.setPagoSoportado(respuesta.getPagoSoportado());
		r.setExencionSCA(respuesta.getExencionSCA());
		r.setUrlTpvOk(respuesta.getUrlTpvOk());
		r.setUrlTpvKO(respuesta.getUrlTpvKO());
		r.setUrlTpvCeca(respuesta.getUrlTpvCeca());
		r.setFirma(respuesta.getFirma());
		r.setHttpCode(respuesta.getHttpCode());
		r.setErrorCode(respuesta.getErrorCode());
		r.setMoreInformation(respuesta.getMoreInformation());
		r.setHttpMessage(respuesta.getHttpMessage());
		return r;
	}
	/**
	 * Solicita el número de pedido a Unicaja
	 * @param peticion
	 * @return
	 * @throws InicioPagoTarjetaException
	 */
	public ResultadoInicioPago iniciarPagoUnicaja(PeticionPagoTarjetaParameters peticion) throws InicioPagoTarjetaException{
		PreferenciasUnicaja pu;
		ResultadoInicioPago res;
		try {
			 pu = new PreferenciasUnicaja(preferencias.getFicheroPreferenciasUnicaja());
		} catch (PasarelaPagoException pe){
			throw new InicioPagoTarjetaException (pe);
		}
		PLPasarelaRepository repository= new PLPasarelaRepository(preferencias, log, idSesion);
		try {
			PateRecord pr= repository.iniciarPagoUnicaja(peticion, pu.getEntidad());
			if (!"P".equalsIgnoreCase(pr.getEstado())){
				//En principio sólo debería tener "Pagado" o "No pagado" en este momento.
				MediadorUnicaja med = new MediadorUnicaja(pu.getEndpointGeneracionToken(),pu.getEndpointConsultaTarjeta(), idSesion);
				SalidaOpGenerarTokenUnicaja token=med.generaToken(peticion.getModalidad(), 
							   									  peticion.getEmisora(), 
							   									  peticion.getIdentificacion(), 
							   									  "2".equals(peticion.getModalidad())?peticion.getReferencia():peticion.getNumeroAutoliquidacion(), 
							   									  peticion.getImporte(), 
							   									  peticion.getNifContribuyente(), 
							   									  peticion.getFechaDevengo(), 
							   									  peticion.getDatoEspecifico(), 
							   									  peticion.getExpediente(), 
							   									  pr.getOperacionEpst());
				
				pr.setResultado(token.getErrorCode()); //Código de resultado, E0000 o E0400
				pr.setEstado(token.isError()?"E": "I");
				if (!token.isError()){
					pr.setToken(token.getNumPedido());
				}
				pr.setXmlServicio(token.getJsonResponse());
				repository.actualizarRegistroPago(pr);
				res = new ResultadoInicioPago(pr, convert(token));
			} else {
				res = new ResultadoInicioPago(pr, null);
			}
			return res;
		} catch (PLPasarelaRepositoryException pl){
			throw new InicioPagoTarjetaException (pl);
		} catch (MediacionUnicajaException mu){
			throw new InicioPagoTarjetaException(mu);
		}
	}
}
