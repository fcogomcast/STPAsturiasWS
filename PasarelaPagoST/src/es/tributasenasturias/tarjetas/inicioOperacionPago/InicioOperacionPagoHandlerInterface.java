package es.tributasenasturias.tarjetas.inicioOperacionPago;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.webservices.types.InicioOperacionPagoResponse;


public interface InicioOperacionPagoHandlerInterface {
	/**
	 * Operaci�n com�n de inicio de operaci�n de pago. 
	 * Las clases que implementen esta interfaz deber�n realizar la operaci�n 
	 * que consideren oportuna.
	 * @return
	 */
	InicioOperacionPagoResponse ejecutar() throws PasarelaPagoException;
}
