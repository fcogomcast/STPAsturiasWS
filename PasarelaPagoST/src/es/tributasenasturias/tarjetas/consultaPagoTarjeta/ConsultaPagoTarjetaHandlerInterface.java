package es.tributasenasturias.tarjetas.consultaPagoTarjeta;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.webservices.types.ResultadoConsultaPagoTarjeta;
/**
 * Interfaz común de las implementaciones de la operación de inicio de pago por tarjeta
 * @author crubencvs
 *
 */
public interface ConsultaPagoTarjetaHandlerInterface {
	/**
	 * Operación común de consulta de pago por tarjeta. 
	 * Las clases que implementen esta interfaz deberán realizar la operación 
	 * que consideren oportuna.
	 * @return
	 */
	ResultadoConsultaPagoTarjeta consultar() throws PasarelaPagoException;
}
