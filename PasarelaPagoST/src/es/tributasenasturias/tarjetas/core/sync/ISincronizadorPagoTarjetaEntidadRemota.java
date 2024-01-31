package es.tributasenasturias.tarjetas.core.sync;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.tarjetas.core.PateRecord;


/**
 * Funcionalidad común de todos los objetos de sincronización de estado  de pago
 * de base de datos con la entidad Remota.
 * @author crubencvs
 *
 */
public interface ISincronizadorPagoTarjetaEntidadRemota {
	boolean sincronizar(PateRecord pateRecord) throws PasarelaPagoException;
}
