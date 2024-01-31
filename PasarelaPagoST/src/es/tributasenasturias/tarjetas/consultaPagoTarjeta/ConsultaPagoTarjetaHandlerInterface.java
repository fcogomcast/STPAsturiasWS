package es.tributasenasturias.tarjetas.consultaPagoTarjeta;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.webservices.types.ResultadoConsultaPagoTarjeta;
/**
 * Interfaz com�n de las implementaciones de la operaci�n de inicio de pago por tarjeta
 * @author crubencvs
 *
 */
public interface ConsultaPagoTarjetaHandlerInterface {
	/**
	 * Operaci�n com�n de consulta de pago por tarjeta. 
	 * Las clases que implementen esta interfaz deber�n realizar la operaci�n 
	 * que consideren oportuna.
	 * @return
	 */
	ResultadoConsultaPagoTarjeta consultar() throws PasarelaPagoException;
}
