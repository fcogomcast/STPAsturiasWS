package es.tributasenasturias.tarjetas.inicioPagoTarjeta;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.webservices.types.InicioPagoTarjetaResponse;
/**
 * Interfaz común de las implementaciones de la operación de inicio de pago por tarjeta
 * @author crubencvs
 *
 */
public interface InicioPagoTarjetaHandlerInterface {
	/**
	 * Operación común de inicio de pago por tarjeta. 
	 * Las clases que implementen esta interfaz deberán realizar la operación 
	 * que consideren oportuna.
	 * @return
	 */
	InicioPagoTarjetaResponse ejecutar() throws PasarelaPagoException;
}
