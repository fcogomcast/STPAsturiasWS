package es.tributasenasturias.tarjetas.inicioPagoTarjeta;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.webservices.types.InicioPagoTarjetaResponse;

/**
 * Clase base de las operaciones de inicio de pago por tarjeta
 * @author crubencvs
 *
 */
public abstract class InicioPagoTarjetaHandler implements InicioPagoTarjetaHandlerInterface{
	protected InicioPagoTarjetaContexto contexto;
	
	protected InicioPagoTarjetaHandler(InicioPagoTarjetaContexto contexto){
		this.contexto= contexto;
	}

	@Override
	public abstract InicioPagoTarjetaResponse ejecutar() throws PasarelaPagoException;
	
	
}
