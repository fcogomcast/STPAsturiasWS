package es.tributasenasturias.tarjetas.inicioPagoTarjeta;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.webservices.types.InicioPagoTarjetaResponse;
/**
 * Interfaz com�n de las implementaciones de la operaci�n de inicio de pago por tarjeta
 * @author crubencvs
 *
 */
public interface InicioPagoTarjetaHandlerInterface {
	/**
	 * Operaci�n com�n de inicio de pago por tarjeta. 
	 * Las clases que implementen esta interfaz deber�n realizar la operaci�n 
	 * que consideren oportuna.
	 * @return
	 */
	InicioPagoTarjetaResponse ejecutar() throws PasarelaPagoException;
}
