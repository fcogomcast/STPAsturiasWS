package es.tributasenasturias.tarjetas.unicajaInterfaz.resultado;

import com.google.gson.GsonBuilder;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.pasarelas.unicaja.PreferenciasUnicaja;
import es.tributasenasturias.tarjetas.core.db.PLPasarelaRepository;
import es.tributasenasturias.tarjetas.core.db.PLPasarelaRepositoryException;
import es.tributasenasturias.tarjetas.unicajaInterfaz.resultado.ValidadorEntradaResultadoPago.ValidarResultadoPagoException;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;

public class RecepcionResultadoPagoUnicaja {

	private Preferencias preferencias;
	private Logger log;
	private String idSesion;
	
	public RecepcionResultadoPagoUnicaja(Preferencias pref, Logger log, String idSesion){
		this.preferencias= pref;
		this.log= log;
		this.idSesion= idSesion;
	}
	/**
	 * Construcción del json de respuesta
	 * @param resultado
	 * @param resultadoDescripcion
	 * @return
	 */
	private String convertirRespuesta(String resultado, String resultadoDescripcion){
		RecepcionResultadoPagoJsonResponse response = new RecepcionResultadoPagoJsonResponse(resultado, resultadoDescripcion);
		return new GsonBuilder().create().toJson(response);
	}
	/**
	 * Conversión de json a objeto de recepción de resultado de pago
	 * @param json
	 * @return
	 */
	private RecepcionResultadoPagoJsonRequest convertirResultado(String json){
		return new GsonBuilder().create().fromJson(json, RecepcionResultadoPagoJsonRequest.class);
	}
	public String recibirResultadoPago(String certificado, String json){
		
		PreferenciasUnicaja prefUnicaja;
		boolean valido=false;
		//Dado que el certificado viene, o puede venir, en un bloque BEGIN y END, lo tengo en cuenta
		String certificadoNormalizado= certificado.replace("-----BEGIN CERTIFICATE-----", "").replace("-----END CERTIFICATE-----", "").trim();
		try {
			prefUnicaja= new PreferenciasUnicaja(preferencias.getFicheroPreferenciasUnicaja());
			if ("S".equalsIgnoreCase(prefUnicaja.getValidaCertificadoResultadoPago())){
				log.info("Validamos los permisos del certificado sobre esta operación");
				ValidadorPermisoCertificado validador= new ValidadorPermisoCertificado(preferencias, idSesion);
				valido=validador.tienePermiso(certificadoNormalizado, prefUnicaja.getPermisoResultadoPago());
			} else {
				valido=true;
			}
		} catch (PasarelaPagoException e){
			log.error ("Error en la validación de permisos de certificado");
			log.trace(e.getStackTrace());
			return convertirRespuesta("0400", "Error técnico al comprobar los permisos sobre el servicio");
		}

		if (valido){
			log.info("El certificado tiene permisos para ejecutar la operación. Validamos la entrada");
			
			RecepcionResultadoPagoJsonRequest request;
			try {
				request= convertirResultado(json);
			} catch (Exception e){
				log.error ("Error al interpretar el json de entrada:"+ e.getMessage());
				return convertirRespuesta("0400", "Error al interpretar el json de entrada");
			}
			
			try {
				ValidadorEntradaResultadoPago.validar(request);
			} catch (ValidarResultadoPagoException val){
				log.error ("Error de validación:"+ val.getErrorValidacion());
				return convertirRespuesta("0400", val.getErrorValidacion());
			}
			
			log.info("Entrada válida. Recogemos y grabamos el resultado");
			
			PLPasarelaRepository repository = new PLPasarelaRepository (this.preferencias, this.log, this.idSesion);
			
			try {
				if (repository.recibirResultadoPagoUnicaja(prefUnicaja.getPARecibirResultadoPago(),
											           "P", //Por el momento, el estado es siempre "Pagado", ya que de otra forma no se recibe 
											           request.getResultado(), 
											           request.getNumeroPedido(), 
											           request.getFechaPago(), 
											           request.getNrc(),
											           request.getNumeroOperacion(),
											           json)
				){
					return convertirRespuesta("0000", "Respuesta Recibida");
				} else {
					return convertirRespuesta("0400", "Error en la recepción de la respuesta.");
				}
			} catch (PLPasarelaRepositoryException pl){
				log.error("Error en acceso a datos:"+pl.getMessage());
				log.trace(pl.getStackTrace());
				return convertirRespuesta("0400", "Error técnico en la grabación de la respuesta.");
			}
		} else {
			log.info("El certificado no tiene permisos para ejecutar la operación");
			return convertirRespuesta("0400", "No tiene permisos para ejecutar este servicio.");
		}
	}
}
