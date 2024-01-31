package es.tributasenasturias.tarjetas.consultaPagoTarjeta;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.tarjetas.core.DiscriminadorPasarelaPago;
import es.tributasenasturias.tarjetas.core.PateRecord;

/**
 * Clase para crear instancias de las clases que implementan la operación de consulta de 
 * pago por tarjeta
 * @author crubencvs
 *
 */
public class ConsultaPagoTarjetaHandlerFactory {

	/**
	 * Devuelve el objeto que permitirá realizar la operación de inicio de pago,
	 * según la pasarela
	 * @param contexto {@link InicioPagoTarjetaContexto}
	 * @param pateRecord Registro de base de datos, si hubiese
	 * @return {@link InicioPagoTarjetaHandler} que podrá usarse para acceder a la implementación de inicio de pago por tarjeta
	 * @throws PasarelaPagoException
	 */
	public static ConsultaPagoTarjetaHandlerInterface getConsultaPagoTarjetaHandler(ConsultaPagoTarjetaContexto contexto, PateRecord pateRecord) throws PasarelaPagoException{
		ConsultaPagoTarjetaHandler handler;
		if (contexto==null){
			throw new PasarelaPagoException("Error técnico. En "+ ConsultaPagoTarjetaHandlerFactory.class.getName() + " no se ha recibido correctamente el contexto de consulta de pago por tarjeta");
		}
		DiscriminadorPasarelaPago disc = new DiscriminadorPasarelaPago(pateRecord);
		//Internamente UniversalPAy va a repetir la consulta, aunque sea redundante,
		//será una operación que desaparecerá. Por tanto, no importa que durante
		//un tiempo haga una operación más..
		//Necesitamos la misma lógica que hasta ahora, así que simplemente llamo a esa lógica.
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
