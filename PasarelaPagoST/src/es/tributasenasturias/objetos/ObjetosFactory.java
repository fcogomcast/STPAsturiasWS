package es.tributasenasturias.objetos;

import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.types.AnulacionPagoTarjetaRequest;
import es.tributasenasturias.webservices.types.InicioOperacionPagoRequest;
import es.tributasenasturias.webservices.types.InicioPagoTarjetaRequest;

/**
 * Utilizado para construir los objetos que realizarán las peticiones de pago, consulta de cobros y anulación de pago.
 * @author crubencvs
 *
 */
public class ObjetosFactory {
	
	private ObjetosFactory(){}
	/**
	 * Genera un nuevo objeto que permitirá tratar las peticiones de pago.
	 * @param peti Objeto clase DatosEntradaServicio con la información de la petición al servicio.
	 * @param context  El contexto de la llamada.
	 * @return Objeto de tipo PeticionPago
	 * @throws PasarelaPagoException
	 */
	public static PeticionPago newPeticionPago (DatosEntradaServicio peti, CallContext context) throws PasarelaPagoException
	{
		return new PeticionPago(peti,context);
	}
	
	/**
	 * Genera un nuevo objeto que permitirá tratar la consulta de pago
	 * @param peti Objeto  de clase DatosEntradaServicio con la información de la petición.
	 * @param context Context de la llamada.
	 * @return Objeto de tipo ConsultaPago
	 * @throws PasarelaPagoException
	 */
	public static ConsultaPago newConsultaPago (DatosEntradaServicio peti, CallContext context) throws PasarelaPagoException
	{
		return new ConsultaPago(peti,context);
	}
	
	/**
	 * Genera un nuevo objeto que permitirá tratar la anulación de pago
	 * @param peti Objeto de clase DatosEntradaServicio con la información de la petición.
	 * @param context Contexto de la llamada
	 * @return Objeto AnulacionPago
	 * @throws PasarelaPagoException
	 */
	public static AnulacionPago newAnulacionPago (DatosEntradaServicio peti, CallContext context) throws PasarelaPagoException
	{
		return new AnulacionPago (peti, context);
	}
	/**
	 * Genera un nuevo objeto que permitirá tratar el inicio de pago con tarjeta
	 * @param peti Objeto {@link InicioPagoTarjetaRequest} con la información de la petición
	 * @param context
	 * @return
	 * @throws PasarelaPagoException
	 */
	public static InicioPagoTarjeta newIniciaPagoTarjeta (InicioPagoTarjetaRequest peti, CallContext context) throws PasarelaPagoException
	{
		return new InicioPagoTarjeta(peti,context);
	}
	
	/**
	 * Genera un nuevo objeto que permite realizar la consulta de pago con tarjeta por plataforma de pago
	 * @param context
	 * @return
	 * @throws PasarelaPagoException
	 */
	public static ConsultaPagoTarjeta newConsultaPagoTarjeta (CallContext context) throws PasarelaPagoException
	{
		return new ConsultaPagoTarjeta(context);
	}
	
	/**
	 * Genera un nuevo objeto que permite realizar la anulación de pago con tarjeta por plataforma de pago
	 * @param peti {@link AnulacionPagoTarjetaRequest} con los datos de la petición
	 * @param context
	 * @return
	 * @throws PasarelaPagoException
	 */
	public static AnulacionPagoTarjeta newAnulacionPagoTarjeta(AnulacionPagoTarjetaRequest peti, CallContext context) throws PasarelaPagoException
	{
		return new AnulacionPagoTarjeta(peti, context);
	}
	
	public static InicioOperacionPago newInicioOperacionPago(InicioOperacionPagoRequest peti, CallContext context) throws PasarelaPagoException
	{
		return new InicioOperacionPago(peti, context);
	}
}
