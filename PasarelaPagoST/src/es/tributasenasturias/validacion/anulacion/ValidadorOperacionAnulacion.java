package es.tributasenasturias.validacion.anulacion;



import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.validacion.IValidador;
import es.tributasenasturias.validacion.ValidadorOperacion;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.IContextReader;
/**
 * Gestiona los validadores de la operación de anulación
 * @author crubencvs
 *
 */
public final class ValidadorOperacionAnulacion extends ValidadorOperacion implements IContextReader{
	
	
	private CallContext context;
	@SuppressWarnings("unused")
	private ValidadorOperacionAnulacion() {}
	/**
	 * 
	 * @param context
	 * @throws PasarelaPagoException
	 */
	public ValidadorOperacionAnulacion (CallContext context) throws PasarelaPagoException
	{
		this.context=context;
		Preferencias pref;
		Logger logger;
		if (context==null)
		{
			throw new PasarelaPagoException ("No se puede crear el objeto " + ValidadorOperacionAnulacion.class.getName() + " faltan datos de contexto.");
		}
		logger = (Logger) context.get(CallContextConstants.LOG_APLICACION);
		pref = (Preferencias) context.get(CallContextConstants.PREFERENCIAS);
		if (logger==null || pref==null)
		{
			throw new PasarelaPagoException ("No se puede crear el objeto " + ValidadorOperacionAnulacion.class.getName()+" porque faltan datos en el contexto de llamada.");
		}
		//Añadimos los validadores.
		if (pref.getValidaMac().equalsIgnoreCase(Constantes.getSI()))
		{
			IValidador<DatosEntradaServicio> validadorMac = new ValidadorMAC();
			ges.addValidador(validadorMac);
		}
		//Validador comunes
		IValidador <DatosEntradaServicio> validadorComun = new ValidadorComunesAnulacion();
		ges.addValidador(validadorComun);
		//Validador para portal.
		IValidador <DatosEntradaServicio> validadorPortal = new ValidadorOrigenPortalAnulacion();
		ges.addValidador(validadorPortal);
		//Validador para servicio web
		IValidador<DatosEntradaServicio> validadorServicio = new ValidadorOrigenServicioAnulacion();
		ges.addValidador(validadorServicio);
	}
	
	@Override
	public CallContext getCallContext() {
		return context;
	}

	
	@Override
	public void setCallContext(CallContext ctx) {
		this.context= ctx;
	}
}
