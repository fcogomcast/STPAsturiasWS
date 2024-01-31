package es.tributasenasturias.pasarelas.cajarural;




import com.ruralserviciosinformaticos.empresa.se_tributosasturiasanulacion.ANULACIONIN;
import com.ruralserviciosinformaticos.empresa.se_tributosasturiasanulacion.ANULACIONOUT;
import com.ruralserviciosinformaticos.empresa.se_tributosasturiasconsulta.CONSULTACOBROSIN;
import com.ruralserviciosinformaticos.empresa.se_tributosasturiasconsulta.CONSULTACOBROSOUT;
import com.ruralserviciosinformaticos.empresa.se_tributosasturiaspago.PAGOIN;
import com.ruralserviciosinformaticos.empresa.se_tributosasturiaspago.PAGOOUT;

import es.tributasenasturias.dao.DatosProceso;

import es.tributasenasturias.exceptions.ConversionExcepcion;
import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.pasarelas.PreferenciasPasarela;
import es.tributasenasturias.pasarelas.comunicacion.ComunicadorPasarela;
import es.tributasenasturias.pasarelas.comunicacion.DatosComunicacion;
import es.tributasenasturias.utils.EstadosPago;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.IContextReader;

/**
 * Clase que realiza la comunicación del servicio con Caja Rural para realizar las operaciones necesarias.
 * Cada operación guardará el resultado y estado en las variables de su clase base, {@link ComunicadorPasarela}
 * @author crubencvs
 *
 */
public final class ComunicacionCajaRural extends ComunicadorPasarela implements IContextReader{

	private CallContext context;
	/**
	 * Constructor, almacena los datos que se utilizarán para realizar las operaciones.
	 * @param idPasarela Identificador de la pasarela a utilizar.
	 * @param datosProceso Datos del proceso que se utilizarán para formar los mensajes de comunicación.
	 * @param pref Preferencias de la pasarela de pago a utilizar.
	 * @throws PasarelaPagoException
	 */
	public ComunicacionCajaRural (String idPasarela,DatosProceso datosProceso, PreferenciasPasarela pref, CallContext context) throws PasarelaPagoException
	{
		super(datosProceso, pref);
		this.idPasarela=idPasarela;
		this.context=context;
	}
	/**
	 * Realiza la operación de pago contra CajaRural
	 */
	public final void realizarPago() throws PasarelaPagoException
	{
		try
		{
			PreferenciasCajaRural prefCRural = (PreferenciasCajaRural)this.prefPasarela; //Convertimos al tipo que necesitamos.
			PAGOIN peticion= ConversorCajaRural.peticionACajaRural(this.datosProceso, prefCRural);
			ProxyCajaRural proxy = new ProxyCajaRural(prefCRural.getEndpointPago(), prefCRural,(String)context.get(CallContextConstants.ID_SESION), (Logger) context.get(CallContextConstants.LOG_APLICACION));
			PAGOOUT out = proxy.realizarPago(peticion);
			this.datosComunicacion= new DatosComunicacion(out);
			if (datosComunicacion.isError())
			{
				ConversorCajaRural.resultadoCajaRuralAServicio(datosComunicacion, Mensajes.getErrorPagoEntidadRemota());
			}
		}
		catch (ConversionExcepcion e)
		{
			throw new PasarelaPagoException (e.getMessage(),e);
		}
	}
	/**
	 * Realiza la operación de consulta contra Caja Rural
	 */
	public final void realizarConsulta() throws PasarelaPagoException
	{
		try
		{
			PreferenciasCajaRural prefCRural = (PreferenciasCajaRural)this.prefPasarela; //Convertimos al tipo que necesitamos.
			CONSULTACOBROSOUT out; 
			CONSULTACOBROSIN in= ConversorCajaRural.consultaACajaRural(datosProceso, prefCRural);
			ProxyCajaRural proxy = new ProxyCajaRural (prefCRural.getEndpointConsulta(),prefCRural,(String)context.get(CallContextConstants.ID_SESION), (Logger) context.get(CallContextConstants.LOG_APLICACION));
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
				ConversorCajaRural.resultadoCajaRuralAServicio(datosComunicacion, Mensajes.getErrorConsulta());
			}
		}
		catch (ConversionExcepcion e)
		{
			throw new PasarelaPagoException (e.getMessage(),e);
		}
	}
	/**
	 * Realiza la operación de anulación contra Caja Rural.
	 * @return
	 * @throws PasarelaPagoException
	 */
	public final void realizarAnulacion() throws PasarelaPagoException
	{
		try
		{
			PreferenciasCajaRural prefCRural = (PreferenciasCajaRural)this.prefPasarela; //Convertimos al tipo que necesitamos.
			//Validamos.
			if (ValidacionesPropiasCajaRural.validacionesCajaRural(this.datosProceso,prefCRural))
			{
				ANULACIONIN peticion=ConversorCajaRural.anulacionACajaRural(this.datosProceso, prefCRural);
				ProxyCajaRural proxy = new ProxyCajaRural(prefCRural.getEndpointAnulacion(),prefCRural,(String)context.get(CallContextConstants.ID_SESION),(Logger) context.get(CallContextConstants.LOG_APLICACION));
				ANULACIONOUT out = proxy.realizarAnulacion(peticion);
				this.datosComunicacion= new DatosComunicacion(out);
				if (datosComunicacion.isError())
				{
					ConversorCajaRural.resultadoCajaRuralAServicio(datosComunicacion, Mensajes.getErrorAnulacionPago());
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
