package es.tributasenasturias.tarjetas.inicioOperacionPago;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.webservices.types.InicioOperacionPagoResponse;


public interface InicioOperacionPagoHandlerInterface {
	/**
	 * Operación común de inicio de operación de pago. 
	 * Las clases que implementen esta interfaz deberán realizar la operación 
	 * que consideren oportuna.
	 * @return
	 */
	InicioOperacionPagoResponse ejecutar() throws PasarelaPagoException;
}
