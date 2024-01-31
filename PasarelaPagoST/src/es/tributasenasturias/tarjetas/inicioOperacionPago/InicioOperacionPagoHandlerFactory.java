package es.tributasenasturias.tarjetas.inicioOperacionPago;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.tarjetas.core.PateRecord;
/**
 * Permite recuperar una clase que permite realizar el inicio de la operación de pago
 * @author crubencvs
 *
 */
public class InicioOperacionPagoHandlerFactory {
	public static InicioOperacionPagoHandlerInterface getInicioOperacionPagoHandler (InicioOperacionPagoContexto contexto, PateRecord pateRecord) throws PasarelaPagoException{
		return new InicioOperacionPagoHandler(contexto, pateRecord);
	}
}
