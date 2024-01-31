package es.tributasenasturias.pasarelas;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.pasarelas.comunicacion.ISelectorPasarela;
import es.tributasenasturias.pasarelas.comunicacion.SelectorPasarelaPorMedioPago;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.IContextReader;

/**
 * Selecciona la pasarela de pago por la que se realizarán las comunicaciones con entidades remotas.
 * @author crubencvs
 *
 */
public class SelectorPasarelaPago implements IContextReader{

	private CallContext context;
	private Preferencias pref;
	
	/**
	 * Constructor, recuperará de contexto de llamada las preferencias.
	 * @param context
	 */
	SelectorPasarelaPago(CallContext context) throws PasarelaPagoException
	{
		this.context = context;
		this.pref=(Preferencias) context.get(CallContextConstants.PREFERENCIAS);
		if (pref==null)
		{
			throw new PasarelaPagoException ("No se ha inicializado correctamente el contexto de llamada en "+SelectorPasarelaPago.class.getName());
		}
	}
	
	/**
	 * Selecciona la pasarela de pago en función de los medios de pago (tarjeta o cuenta).
	 * @param tarjeta Tarjeta de pago
	 * @param cuenta Cuenta de pago.
	 * @return
	 * @throws PasarelaPagoException
	 */
	public String seleccionarPasarela (String tarjeta, String cuenta) throws PasarelaPagoException
	{
		ISelectorPasarela sel = new SelectorPasarelaPorMedioPago (tarjeta, cuenta, pref.getFicheroPasarelas(), pref.getIdPasarelaPorDefecto());
		return sel.getPasarela();
	}

	@Override
	public CallContext getCallContext() {
		return context;
	}

	
	@Override
	public void setCallContext(CallContext ctx) {
		context=ctx;
	}	
}
