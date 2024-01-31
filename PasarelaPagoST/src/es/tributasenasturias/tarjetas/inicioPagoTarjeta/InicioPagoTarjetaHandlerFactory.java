package es.tributasenasturias.tarjetas.inicioPagoTarjeta;

import es.tributasenasturias.exceptions.PasarelaPagoException;

/**
 * Clase para crear instancias de las clases que implementan la operación de inicio de 
 * pago por tarjeta
 * @author crubencvs
 *
 */
public class InicioPagoTarjetaHandlerFactory {

	/**
	 * Devuelve el objeto que permitirá realizar la operación de inicio de pago,
	 * según la pasarela
	 * @param contexto {@link InicioPagoTarjetaContexto}
	 * @return {@link InicioPagoTarjetaHandlerInterface} que podrá usarse para acceder a la implementación de inicio de pago por tarjeta
	 * @throws PasarelaPagoException
	 */
	public static InicioPagoTarjetaHandlerInterface getInicioPagoTarjetaHandler(InicioPagoTarjetaContexto contexto) throws PasarelaPagoException{
		InicioPagoTarjetaHandler handler;
		if (contexto==null){
			throw new PasarelaPagoException("Error técnico. En "+ InicioPagoTarjetaHandlerFactory.class.getName() + " no se ha recibido correctamente el contexto de inicio de pago por tarjeta");
		}
		
		if ("UniversalPay".equalsIgnoreCase(contexto.getPasarelaUtilizada())){
			handler = new InicioPagoTarjetaUniversalPay(contexto);
		} else if ("unicaja".equalsIgnoreCase(contexto.getPasarelaUtilizada())){
			handler = new InicioPagoTarjetaUnicaja(contexto);
		} else {
			//Error, no soportada
			throw new PasarelaPagoException( "Error, pasarela " + contexto.getPasarelaUtilizada() + " no soportada");
		}
		return handler;
	}
}
