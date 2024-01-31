package es.tributasenasturias.pasarelas.unicaja;


import es.tributasenasturias.dao.DatosProceso;

import es.tributasenasturias.exceptions.ConversionExcepcion;
import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.pasarelas.PreferenciasPasarela;
import es.tributasenasturias.pasarelas.comunicacion.ComunicadorPasarela;
import es.tributasenasturias.pasarelas.comunicacion.DatosComunicacion;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.IContextReader;
import es.types.unicaja.consulta.CONSULTACOBROSIN;
import es.types.unicaja.consulta.CONSULTACOBROSOUT;
import es.types.unicaja.pago.PAGC3POIN;
import es.types.unicaja.pago.PAGC3POOUT;

/**
 * Clase que realiza la comunicación del servicio con Unicaja para realizar las operaciones necesarias.
 * Cada operación guardará el resultado y estado en las variables de su clase base, {@link ComunicadorPasarela}
 * @author crubencvs
 *
 */
public final class ComunicacionUnicaja extends ComunicadorPasarela implements IContextReader{

	private CallContext context;
	private String idSesion;
	/**
	 * Constructor, almacena los datos que se utilizarán para realizar las operaciones.
	 * @param idPasarela Identificador de la pasarela a utilizar.
	 * @param datosProceso Datos del proceso que se utilizarán para formar los mensajes de comunicación.
	 * @param pref Preferencias de la pasarela de pago a utilizar.
	 * @throws PasarelaPagoException
	 */
	public ComunicacionUnicaja (String idPasarela,DatosProceso datosProceso, PreferenciasPasarela pref, CallContext context) throws PasarelaPagoException
	{
		super(datosProceso, pref);
		this.idPasarela=idPasarela;
		this.idSesion = (String) context.get(CallContextConstants.ID_SESION);
		this.context=context;
	}
	/**
	 * Realiza la operación de pago contra Unicaja
	 */
	public final void realizarPago() throws PasarelaPagoException
	{
		try
		{
			PreferenciasUnicaja prefUnicaja = (PreferenciasUnicaja)this.prefPasarela; //Convertimos al tipo que necesitamos.
			PAGC3POIN peticionUnicaja= ConversorUnicaja.peticionAUnicaja(this.datosProceso);
			ProxyUnicaja proxy = new ProxyUnicaja(prefUnicaja.getEndpointPago(), this.idSesion);
			PAGC3POOUT out = proxy.realizarPagoOAnulacion(peticionUnicaja, ConversorUnicaja.getMessageHeaderPago(prefUnicaja.getAplicacionDestinoPeticion(), prefUnicaja));
			this.datosComunicacion= new DatosComunicacion(out);
			if (datosComunicacion.isError())
			{
				ConversorUnicaja.resultadoUnicajaAServicio(datosComunicacion, Mensajes.getErrorPagoEntidadRemota());
			}
		}
		catch (ConversionExcepcion e)
		{
			throw new PasarelaPagoException (e.getMessage(),e);
		}
	}
	/**
	 * Realiza la operación de consulta contra Unicaja
	 */
	public final void realizarConsulta() throws PasarelaPagoException
	{
		try
		{
			PreferenciasUnicaja prefUnicaja = (PreferenciasUnicaja)this.prefPasarela; //Convertimos al tipo que necesitamos.
			CONSULTACOBROSOUT out; 
			CONSULTACOBROSIN in = ConversorUnicaja.consultaAUnicaja(datosProceso);
			ProxyUnicaja proxy = new ProxyUnicaja (prefUnicaja.getEndpointConsulta(), this.idSesion);
			out = proxy.realizarConsulta(in, ConversorUnicaja.getMessageHeaderConsulta(prefUnicaja.getAplicacionDestinoConsulta(), prefUnicaja));
			this.datosComunicacion= new DatosComunicacion(out);
			//Si hay error, mapear
			if (datosComunicacion.isError())
			{
				ConversorUnicaja.resultadoUnicajaAServicio(datosComunicacion, Mensajes.getErrorConsulta());
			}
		}
		catch (ConversionExcepcion e)
		{
			throw new PasarelaPagoException (e.getMessage(),e);
		}
	}
	/**
	 * Realiza la operación de anulación contra Unicaja.
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final void realizarAnulacion() throws PasarelaPagoException
	{
		try
		{
			PreferenciasUnicaja prefUnicaja = (PreferenciasUnicaja)this.prefPasarela; //Convertimos al tipo que necesitamos.
			//Validamos.
			if (ValidacionesPropiasUnicaja.validacionesUnicaja(this.datosProceso,prefUnicaja))
			{
				PAGC3POIN peticionUnicaja= ConversorUnicaja.anulacionAUnicaja(this.datosProceso);
				ProxyUnicaja proxy = new ProxyUnicaja(prefUnicaja.getEndpointPago(), this.idSesion);
				PAGC3POOUT out = proxy.realizarPagoOAnulacion(peticionUnicaja, ConversorUnicaja.getMessageHeaderPago(prefUnicaja.getAplicacionDestinoAnulacion(), prefUnicaja));
				this.datosComunicacion= new DatosComunicacion(out);
				if (datosComunicacion.isError())
				{
					ConversorUnicaja.resultadoUnicajaAServicio(datosComunicacion, Mensajes.getErrorAnulacionPago());
				}
			}
			else
			{
				this.datosComunicacion= new DatosComunicacion();
				//Error.
				datosComunicacion.setError(true);
				datosComunicacion.setCodigoError(Mensajes.getErrorMargenAnulacion());
				datosComunicacion.setTextoError("");
			}
		}
		catch (ConversionExcepcion e)
		{
			throw new PasarelaPagoException (e.getMessage(),e);
		}
	}

	@Override
	public CallContext getCallContext() {
		return context;
	}
	
	@Override
	public void setCallContext(CallContext ctx) {
		context= ctx;
	}
}
