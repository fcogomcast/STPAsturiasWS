package es.tributasenasturias.tarjetas.core.sync;


import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.tarjetas.core.DiscriminadorPasarelaPago;
import es.tributasenasturias.tarjetas.core.PateRecord;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;

/**
 * Gestiona la sincronización del estado de un pago por tarjeta en la entidad
 * remota
 * @author crubencvs
 *
 */
public class SincronizadorEstadoPagoTarjeta {

	private Preferencias preferencias;
	private Logger log;
	private String idSesion;
	
	
	public SincronizadorEstadoPagoTarjeta(Preferencias preferencias,  Logger log, String idSesion){
		this.preferencias = preferencias;
		this.log= log;
		this.idSesion= idSesion;
	}
	
	/**
	 * Indica si es necesario sincronizar el estado del registro.
	 * @param pateBD
	 * @return
	 */
	private boolean necesitaSincronizar(PateRecord pateBD){
		String estado= pateBD.getEstado();
		boolean necesario=false;
		if ("P".equals(estado) || "A".equals(estado) || "G".equals(estado)){
			necesario=false;
		} else if ("I".equals(estado) || "E".equals(estado)|| "T".equals(estado)){
			necesario=true;
		}
		return necesario;
	}
	/**
	 * Sincroniza el estado de base de datos con el de la entidad remota
	 * @param pateBD  Registro de pago en la base de datos
	 * @return true si ha podido sincronizar, false si no
	 * @throws PasarelaPagoException
	 */
	private boolean sincronizar (PateRecord pateBD) throws PasarelaPagoException{
		ISincronizadorPagoTarjetaEntidadRemota sincronizador;
		//Otra opción para hacer esto sería devolver el nombre de la pasarela
		//desde la base de datos
		DiscriminadorPasarelaPago discriminador = new DiscriminadorPasarelaPago(pateBD);
		if (discriminador.isUniversalPay()){
			sincronizador= new SincronizadorEstadoUniversalPay(preferencias, log, idSesion);
		} else if (discriminador.isUnicajaTarjeta()){
			sincronizador= new SincronizadorEstadoUnicaja(preferencias,log,idSesion);
		} else if (discriminador.isCuenta()) {
			sincronizador= new SincronizadorEstadoCuenta(preferencias,log,idSesion);
		} else {
			throw new PasarelaPagoException("Pasarela no soportada para la sincronización");
		}
		
		return sincronizador.sincronizar(pateBD);
	}
	/**
	 * Realiza la sincronización del registro de base de datos con la entidad de pago
	 * correspondiente
	 * @param pateBD Registro de pago en base de datos
	 * @return true si ha posido sincronizar, false si no
	 * @throws SincronizacionException En caso de una excepción
	 */
	public boolean realizaSincronizacion(PateRecord pateBD) throws SincronizacionException{
		try {
			if (necesitaSincronizar(pateBD)){
				return sincronizar(pateBD);
			}  else {
				return true;
			}
		} catch (Exception e){
			throw new SincronizacionException ("Error al sincronizar el registro de base de datos con la entidad:" + e.getMessage(), e);
		}
	}
}
