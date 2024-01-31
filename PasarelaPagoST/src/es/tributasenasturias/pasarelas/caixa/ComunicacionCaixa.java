package es.tributasenasturias.pasarelas.caixa;



import org.ejemplo.ANULACIONIN;
import org.ejemplo.ANULACIONOUT;
import org.ejemplo.CONSULTACOBROSIN;
import org.ejemplo.CONSULTACOBROSOUT;
import org.ejemplo.PAGOIN;
import org.ejemplo.PAGOOUT;

import es.tributasenasturias.dao.DatosProceso;

import es.tributasenasturias.exceptions.ConversionExcepcion;
import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.pasarelas.PreferenciasPasarela;
import es.tributasenasturias.pasarelas.comunicacion.ComunicadorPasarela;
import es.tributasenasturias.pasarelas.comunicacion.DatosComunicacion;
import es.tributasenasturias.utils.EstadosPago;
import es.tributasenasturias.utils.GeneradorIBAN;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.IContextReader;

/**
 * Clase que realiza la comunicación del servicio con BBVA para realizar las operaciones necesarias.
 * Cada operación guardará el resultado y estado en las variables de su clase base, {@link ComunicadorPasarela}
 * @author crubencvs
 *
 */
public final class ComunicacionCaixa extends ComunicadorPasarela implements IContextReader{

	private CallContext context;
	/**
	 * Constructor, almacena los datos que se utilizarán para realizar las operaciones.
	 * @param idPasarela Identificador de la pasarela a utilizar.
	 * @param datosProceso Datos del proceso que se utilizarán para formar los mensajes de comunicación.
	 * @param pref Preferencias de la pasarela de pago a utilizar.
	 * @throws PasarelaPagoException
	 */
	public ComunicacionCaixa (String idPasarela,DatosProceso datosProceso, PreferenciasPasarela pref, CallContext context) throws PasarelaPagoException
	{
		super(datosProceso, pref);
		this.idPasarela=idPasarela;
		this.context=context;
	}
	/**
	 * Realiza la operación de pago contra la Caixa
	 */
	public final void realizarPago() throws PasarelaPagoException
	{
		try
		{
			PreferenciasCaixa prefCaixa = (PreferenciasCaixa)this.prefPasarela; //Convertimos al tipo que necesitamos.
			PAGOIN peticion= ConversorCaixa.peticionACaixa(this.datosProceso, prefCaixa);
			if ("S".equals(prefCaixa.getCalcularIBAN())){
				peticion.setCUENTA(GeneradorIBAN.generarIBAN(peticion.getCUENTA())); // Generamos el IBAN, porque en entrada de servicio no lo pasan.
			}
			ProxyCaixa proxy = new ProxyCaixa(prefCaixa.getEndpointPago(), prefCaixa,(String)context.get(CallContextConstants.ID_SESION), (Logger) context.get(CallContextConstants.LOG_APLICACION));
			PAGOOUT out = proxy.realizarPago(peticion);
			this.datosComunicacion= new DatosComunicacion(out);
			if (datosComunicacion.isError())
			{
				ConversorCaixa.resultadoCaixaAServicio(datosComunicacion, Mensajes.getErrorPagoEntidadRemota());
			}
		}
		catch (ConversionExcepcion e)
		{
			throw new PasarelaPagoException (e.getMessage(),e);
		}
	}
	/**
	 * Realiza la operación de consulta contra la Caixa
	 */
	public final void realizarConsulta() throws PasarelaPagoException
	{
		try
		{
			PreferenciasCaixa prefBBVA = (PreferenciasCaixa)this.prefPasarela; //Convertimos al tipo que necesitamos.
			CONSULTACOBROSOUT out; 
			CONSULTACOBROSIN in= ConversorCaixa.consultaACaixa(datosProceso, prefBBVA);
			ProxyCaixa proxy = new ProxyCaixa (prefBBVA.getEndpointConsulta(),prefBBVA,(String)context.get(CallContextConstants.ID_SESION), (Logger) context.get(CallContextConstants.LOG_APLICACION));
			out = proxy.realizarConsulta(in);
			if (!datosProceso.getEstado().equalsIgnoreCase(EstadosPago.ANULACION_COMENZADA.getValor()))
			{
				this.datosComunicacion= new DatosComunicacion(out);
			}
			else
			{
				this.datosComunicacion= new DatosComunicacion(true, out);
			}
			//Si hay error, mapear
			if (datosComunicacion.isError())
			{
				ConversorCaixa.resultadoCaixaAServicio(datosComunicacion, Mensajes.getErrorConsulta());
			}
		}
		catch (ConversionExcepcion e)
		{
			throw new PasarelaPagoException (e.getMessage(),e);
		}
	}
	/**
	 * Realiza la operación de anulación contra LaCaixa.
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final void realizarAnulacion() throws PasarelaPagoException
	{
		try
		{
			PreferenciasCaixa prefLaCaixa = (PreferenciasCaixa)this.prefPasarela; //Convertimos al tipo que necesitamos.
			//Validamos.
			if (ValidacionesPropiasCaixa.validacionesCaixa(this.datosProceso,prefLaCaixa))
			{
				ANULACIONIN peticion=ConversorCaixa.anulacionACaixa(this.datosProceso, prefLaCaixa);
				ProxyCaixa proxy = new ProxyCaixa(prefLaCaixa.getEndpointPago(),prefLaCaixa,(String)context.get(CallContextConstants.ID_SESION),(Logger) context.get(CallContextConstants.LOG_APLICACION));
				ANULACIONOUT out = proxy.realizarAnulacion(peticion);
				this.datosComunicacion= new DatosComunicacion(out);
				if (datosComunicacion.isError())
				{
					ConversorCaixa.resultadoCaixaAServicio(datosComunicacion, Mensajes.getErrorAnulacionPago());
				}
			}
			else
			{
				this.datosComunicacion= new DatosComunicacion();
				//Error.
				datosComunicacion.setError(true);
				datosComunicacion.setCodigoError(Mensajes.getErrorMargenAnulacion());
				datosComunicacion.setTextoError(Mensajes.getExternalText(Mensajes.getErrorMargenAnulacion()));
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
