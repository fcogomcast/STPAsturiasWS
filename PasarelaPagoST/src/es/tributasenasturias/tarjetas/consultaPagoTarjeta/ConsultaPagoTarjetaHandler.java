package es.tributasenasturias.tarjetas.consultaPagoTarjeta;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.webservices.types.ResultadoConsultaPagoTarjeta;

/**
 * Clase base de las operaciones de inicio de pago por tarjeta
 * @author crubencvs
 *
 */
public abstract class ConsultaPagoTarjetaHandler implements ConsultaPagoTarjetaHandlerInterface{
	protected ConsultaPagoTarjetaContexto consultaPagoContexto;
	
	protected ConsultaPagoTarjetaHandler(ConsultaPagoTarjetaContexto consultaPagoContexto){
		this.consultaPagoContexto= consultaPagoContexto;
	}

	@Override
	public abstract ResultadoConsultaPagoTarjeta consultar() throws PasarelaPagoException;
	
	
}
