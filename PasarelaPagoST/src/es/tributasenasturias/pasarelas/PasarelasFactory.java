package es.tributasenasturias.pasarelas;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.webservices.context.CallContext;

/** Constructor de objetos de uso externo al paquete es.tributasenasturias.pasarelas
 * 
 * @author crubencvs
 *
 */
public class PasarelasFactory {
	
	private PasarelasFactory(){}
	/**
	 * Devuelve un objeto SelectorComunicadorEntidadRemota.
	 * @param context Contexto de la llamada, de donde tomará los valores de preferencias necesarios.
	 * @return {@link SelectorComunicadorEntidadRemota}
	 * @throws PasarelaPagoException Si no se puede generar un nuevo objeto.
	 */
	public static SelectorComunicadorEntidadRemota newSelectorComunicadorPasarela (CallContext context) throws PasarelaPagoException
	{
		return new SelectorComunicadorEntidadRemota(context);
	}
	
	/**
	 * Devuelve un objeto SelectorPasarelaPago.
	 * @param context Contexto de la llamada, de donde tomará los valores de preferencias necesarios.
	 * @return {@link SelectorPasarelaPago}
	 * @throws PasarelaPagoException Si no se puede generar el nuevo objeto.
	 */
	public static SelectorPasarelaPago newSelectorPasarelaPago(CallContext context) throws PasarelaPagoException
	{
		return new SelectorPasarelaPago(context);
	}
}
