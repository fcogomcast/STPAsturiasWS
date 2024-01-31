package es.tributasenasturias.validacion;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.validacion.anulacion.ValidadorOperacionAnulacion;
import es.tributasenasturias.validacion.consulta.ValidadorOperacionConsulta;
import es.tributasenasturias.validacion.pago.ValidadorOperacionPago;
import es.tributasenasturias.validacion.plataformasPago.ValidadorAnulacionPagoTarjeta;
import es.tributasenasturias.validacion.plataformasPago.ValidadorConsultaPagoTarjeta;
import es.tributasenasturias.validacion.plataformasPago.ValidadorInicioOperacionPago;
import es.tributasenasturias.validacion.plataformasPago.ValidadorInicioPagoTarjeta;
import es.tributasenasturias.webservices.context.CallContext;

/**
 * Clase que crear� los objetos de validaci�n.
 * @author crubencvs
 *
 */
public class ValidacionFactory {
	
	private ValidacionFactory(){};
	/**
	 * Devuelve un nuevo validador de pasarela de pago. Este validador se encargar� de las validaciones de pago, y 
	 * al finalizar indicar� si las validaciones han sido correctas o se devuelve alg�n c�digo de error.
	 * @param context Contexto de llamada. Necesitar� que se pase en �l el log y las preferencias.
	 * @return {@link ValidadorOperacion}
	 * @throws PasarelaPagoException Si hay alg�n problema al construir el objeto.
	 */
	public static ValidadorOperacion newValidadorOperacionPago (CallContext context) throws PasarelaPagoException
	{
		return new ValidadorOperacionPago(context);
	}
	/**
	 * Devuelve un nuevo validador de pasarela de pago, consulta. Este validador se encargar� de las validaciones de consulta
	 * y podr� indicar si las validaciones han sido correctas o se devuelve alg�n c�digo de error. 
	 * @param context Contexto de llamada. 
	 * @return {@link ValidadorOperacionConsulta}
	 * @throws PasarelaPagoException Si hay alg�n problema al construir el objeto.
	 */
	public static ValidadorOperacion newValidadorOperacionConsulta (CallContext context) throws PasarelaPagoException
	{
		return new ValidadorOperacionConsulta(context);
	}
	/**
	 * Devuelve un nuevo validador de pasarela de pago. Este validador se encargar� de las validaciones de la anulaci�n de pago
	 * y al finalizar indicar� si la validaci�n ha sido correcta o se devuelve alg�n c�digo de error.
	 * @param context Contexto de llamada. Necesitar� que se pase en �l el log y las preferencias.
	 * @return {@link ValidadorOperacion}
	 * @throws PasarelaPagoException Si ha alg�n problema al construir el objeto.
	 */
	public static ValidadorOperacion newValidadorOperacionAnulacion (CallContext context) throws PasarelaPagoException
	{
		return new ValidadorOperacionAnulacion(context);
	}
	
	/**
	 * Devuelve un nuevo validador de inicio de de pago con tarjeta. Este validador podr� indicar si las validaciones han sido correctas o se devuelve alg�n c�digo de error. 
	 * @param context Contexto de llamada. 
	 * @return {@link ValidadorInicioPagoTarjeta}
	 * @throws PasarelaPagoException Si hay alg�n problema al construir el objeto.
	 */
	public static ValidadorInicioPagoTarjeta newValidadorOperacionInicioPagoTarjeta(CallContext context) throws PasarelaPagoException
	{
		return new ValidadorInicioPagoTarjeta(context);
	}
	
	//CRUBENCVS 29/03/2023. Mismo constructor, pasando directamente los objetos
	/**
	 * Devuelve un nuevo validador de inicio de de pago con tarjeta. Este validador podr� indicar si las validaciones han sido correctas o se devuelve alg�n c�digo de error.
	 */
	public static ValidadorInicioPagoTarjeta newValidadorOperacionInicioPagoTarjeta(Preferencias pref, Logger log) throws PasarelaPagoException
	{
		return new ValidadorInicioPagoTarjeta(pref, log);
	}
	/**
	 * Devuelve un nuevo validador de anulaci�n de pago con tarjeta
	 * @param context
	 * @return
	 * @throws PasarelaPagoException
	 */
	public static ValidadorAnulacionPagoTarjeta newValidadorAnulacionPagoTarjeta(CallContext context) throws PasarelaPagoException{
		return new ValidadorAnulacionPagoTarjeta(context);
	}
	/**
	 * Devuelve un nuevo validador de inicio de operaci�n de pago
	 * @param context
	 * @return
	 * @throws PasarelaPagoException
	 */
	public static ValidadorInicioOperacionPago newValidadorInicioOperacionPago(CallContext context) throws PasarelaPagoException{
		return new ValidadorInicioOperacionPago(context);
	}
	/**
	 * Devuelve un nuevo validador de consulta de pago con tarjeta
	 * @param context
	 * @return
	 * @throws PasarelaPagoException
	 */
	public static ValidadorConsultaPagoTarjeta newValidadorConsultaPagoTarjeta(CallContext context) throws PasarelaPagoException{
		return new ValidadorConsultaPagoTarjeta(context);
	}
}
