package es.tributasenasturias.tarjetas.consultaPagoTarjeta;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.tarjetas.core.DiscriminadorPasarelaPago;
import es.tributasenasturias.tarjetas.core.PateRecord;

/**
 * Clase para crear instancias de las clases que implementan la operaci�n de consulta de 
 * pago por tarjeta
 * @author crubencvs
 *
 */
public class ConsultaPagoTarjetaHandlerFactory {

	/**
	 * Devuelve el objeto que permitir� realizar la operaci�n de inicio de pago,
	 * seg�n la pasarela
	 * @param contexto {@link InicioPagoTarjetaContexto}
	 * @param pateRecord Registro de base de datos, si hubiese
	 * @return {@link InicioPagoTarjetaHandler} que podr� usarse para acceder a la implementaci�n de inicio de pago por tarjeta
	 * @throws PasarelaPagoException
	 */
	public static ConsultaPagoTarjetaHandlerInterface getConsultaPagoTarjetaHandler(ConsultaPagoTarjetaContexto contexto, PateRecord pateRecord) throws PasarelaPagoException{
		ConsultaPagoTarjetaHandler handler;
		if (contexto==null){
			throw new PasarelaPagoException("Error t�cnico. En "+ ConsultaPagoTarjetaHandlerFactory.class.getName() + " no se ha recibido correctamente el contexto de consulta de pago por tarjeta");
		}
		DiscriminadorPasarelaPago disc = new DiscriminadorPasarelaPago(pateRecord);
		//Internamente UniversalPAy va a repetir la consulta, aunque sea redundante,
		//ser� una operaci�n que desaparecer�. Por tanto, no importa que durante
		//un tiempo haga una operaci�n m�s..
		//Necesitamos la misma l�gica que hasta ahora, as� que simplemente llamo a esa l�gica.
		if (disc.isUniversalPay()){
			handler = new ConsultaPagoTarjetaUniversalPay(contexto);
		} 
		else if (disc.isUnicajaTarjeta()){
			handler = new ConsultaPagoTarjetaUnicaja(contexto, pateRecord);
		}
		else {
			//Error, no soportada
			throw new PasarelaPagoException( "Error, pasarela " + contexto.getPasarelaUtilizada() + " no soportada");
		}
		return handler;
	}
}
