package es.tributasenasturias.pasarelas.cajastur;


import es.infocaja.schemas.consulta_cobros.CONSULTACOBROSIN;
import es.infocaja.schemas.consulta_cobros.CONSULTACOBROSOUT;
import es.infocaja.schemas.pag_c3po.PAGC3POIN;
import es.infocaja.schemas.pag_c3po.PAGC3POOUT;
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

/**
 * Clase que realiza la comunicación del servicio con Liberbank para realizar las operaciones necesarias.
 * Cada operación guardará el resultado y estado en las variables de su clase base, {@link ComunicadorPasarela}
 * @author crubencvs
 *
 */
public final class ComunicacionCajastur extends ComunicadorPasarela implements IContextReader{

	private CallContext context;
	private String idSesion;
	/**
	 * Constructor, almacena los datos que se utilizarán para realizar las operaciones.
	 * @param idPasarela Identificador de la pasarela a utilizar.
	 * @param datosProceso Datos del proceso que se utilizarán para formar los mensajes de comunicación.
	 * @param pref Preferencias de la pasarela de pago a utilizar.
	 * @throws PasarelaPagoException
	 */
	public ComunicacionCajastur (String idPasarela,DatosProceso datosProceso, PreferenciasPasarela pref, CallContext context) throws PasarelaPagoException
	{
		super(datosProceso, pref);
		this.idPasarela=idPasarela;
		this.idSesion = (String) context.get(CallContextConstants.ID_SESION);
		this.context=context;
	}
	/**
	 * Realiza la operación de pago contra Cajastur
	 */
	public final void realizarPago() throws PasarelaPagoException
	{
		try
		{
			PreferenciasCajastur prefCajastur = (PreferenciasCajastur)this.prefPasarela; //Convertimos al tipo que necesitamos.
			PAGC3POIN peticionLiberbank= ConversorCajastur.peticionACajastur(this.datosProceso);
			ProxyCajastur proxy = new ProxyCajastur(prefCajastur.getEndpointPago(), this.idSesion);
			PAGC3POOUT out = proxy.realizarPagoOAnulacion(peticionLiberbank, ConversorCajastur.getMessageHeader(prefCajastur.getAplicacionDestinoPeticion(), prefCajastur));
			this.datosComunicacion= new DatosComunicacion(out);
			if (datosComunicacion.isError())
			{
				ConversorCajastur.resultadoCajasturAServicio(datosComunicacion, Mensajes.getErrorPagoEntidadRemota());
			}
		}
		catch (ConversionExcepcion e)
		{
			throw new PasarelaPagoException (e.getMessage(),e);
		}
	}
	/**
	 * Realiza la operación de consulta contra Cajastur
	 */
	public final void realizarConsulta() throws PasarelaPagoException
	{
		try
		{
			PreferenciasCajastur prefCajastur = (PreferenciasCajastur)this.prefPasarela; //Convertimos al tipo que necesitamos.
			CONSULTACOBROSOUT out; 
			CONSULTACOBROSIN in = ConversorCajastur.consultaACajastur(datosProceso);
			ProxyCajastur proxy = new ProxyCajastur (prefCajastur.getEndpointConsulta(), this.idSesion);
			out = proxy.realizarConsulta(in, ConversorCajastur.getMessageHeader(prefCajastur.getAplicacionDestinoConsulta(), prefCajastur));
			this.datosComunicacion= new DatosComunicacion(out);
			//Si hay error, mapear
			if (datosComunicacion.isError())
			{
				ConversorCajastur.resultadoCajasturAServicio(datosComunicacion, Mensajes.getErrorConsulta());
			}
		}
		catch (ConversionExcepcion e)
		{
			throw new PasarelaPagoException (e.getMessage(),e);
		}
	}
	/**
	 * Realiza la operación de anulación contra Cajastur.
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final void realizarAnulacion() throws PasarelaPagoException
	{
		try
		{
			PreferenciasCajastur prefCajastur = (PreferenciasCajastur)this.prefPasarela; //Convertimos al tipo que necesitamos.
			//Validamos.
			if (ValidacionesPropiasCajastur.validacionesCajastur(this.datosProceso,prefCajastur))
			{
				PAGC3POIN peticionLiberbank= ConversorCajastur.anulacionACajastur(this.datosProceso);
				ProxyCajastur proxy = new ProxyCajastur(prefCajastur.getEndpointPago(), this.idSesion);
				PAGC3POOUT out = proxy.realizarPagoOAnulacion(peticionLiberbank, ConversorCajastur.getMessageHeader(prefCajastur.getAplicacionDestinoAnulacion(), prefCajastur));
				this.datosComunicacion= new DatosComunicacion(out);
				if (datosComunicacion.isError())
				{
					ConversorCajastur.resultadoCajasturAServicio(datosComunicacion, Mensajes.getErrorAnulacionPago());
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
