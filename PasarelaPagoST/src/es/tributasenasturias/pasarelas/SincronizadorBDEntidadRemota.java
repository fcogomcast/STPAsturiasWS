package es.tributasenasturias.pasarelas;



import es.tributasenasturias.bd.BDFactory;
import es.tributasenasturias.bd.GestorLlamadasBD;
import es.tributasenasturias.bd.ResultadoLlamadaBD;
import es.tributasenasturias.dao.DatosProceso;
import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.pasarelas.comunicacion.ComunicadorPasarela;
import es.tributasenasturias.pasarelas.comunicacion.DatosComunicacion;
import es.tributasenasturias.utils.EstadosPago;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;
import es.tributasenasturias.webservices.context.IContextReader;

/**
 * Sincroniza el estado de Base de datos con el estado existente en la entidad remota.
 * @author crubencvs
 *
 */
public class SincronizadorBDEntidadRemota implements IContextReader{


	
	private CallContext context;
	Logger logger;
	Preferencias pref;
	DatosPagoEntidadRemota datosEntidadRemota;
	public SincronizadorBDEntidadRemota(CallContext context) throws PasarelaPagoException
	{
		datosEntidadRemota= new DatosPagoEntidadRemota();
		this.context= context;
		logger = (Logger) context.get(CallContextConstants.LOG_APLICACION);
		pref = (Preferencias) context.get(CallContextConstants.PREFERENCIAS);
		if (logger==null)
		{
			throw new PasarelaPagoException ("No se ha inicializado correctamente el contexto de llamada en "+SincronizadorBDEntidadRemota.class.getName());
		}
	}
	private static class DatosPagoEntidadRemota
	{
		private EstadosPago estado;
		private String numeroOperacion;
		private String nrc;
		private String fechaPago;
		private String codigoError;
		private String textoError;
		private boolean error;
		/**
		 * @return the estado
		 */
		public final EstadosPago getEstado() {
			return estado;
		}
		/**
		 * @param estado the estado to set
		 */
		public final void setEstado(EstadosPago estado) {
			this.estado = estado;
		}
		/**
		 * @return the numeroOperacion
		 */
		public final String getNumeroOperacion() {
			return numeroOperacion;
		}
		/**
		 * @param numeroOperacion the numeroOperacion to set
		 */
		public final void setNumeroOperacion(String numeroOperacion) {
			this.numeroOperacion = numeroOperacion;
		}
		/**
		 * @return the nrc
		 */
		public final String getNrc() {
			return nrc;
		}
		/**
		 * @param nrc the nrc to set
		 */
		public final void setNrc(String nrc) {
			this.nrc = nrc;
		}
		/**
		 * @return the fechaPago
		 */
		public final String getFechaPago() {
			return fechaPago;
		}
		/**
		 * @param fechaPago the fechaPago to set
		 */
		public final void setFechaPago(String fechaPago) {
			this.fechaPago = fechaPago;
		}
		/**
		 * @return the codigoError
		 */
		public final String getCodigoError() {
			return codigoError;
		}
		/**
		 * @param codigoError the codigoError to set
		 */
		public final void setCodigoError(String codigoError) {
			this.codigoError = codigoError;
		}
		/**
		 * @return the textoError
		 */
		public final String getTextoError() {
			return textoError;
		}
		/**
		 * @param textoError the textoError to set
		 */
		public final void setTextoError(String textoError) {
			this.textoError = textoError;
		}
		/**
		 * @return the error
		 */
		public final boolean isError() {
			return error;
		}
		/**
		 * @param error the error to set
		 */
		public final void setError(boolean error) {
			this.error = error;
		}
	}
	/**
	 * Sincronizar los datos en base de datos con los datos en la entidad remota.
	 * @param datosProceso
	 * @throws PasarelaPagoException
	 */
	public void sincronizar(String idPasarela,DatosProceso datosProceso) throws PasarelaPagoException
	{
		this.datosEntidadRemota.setError(false);
		//String idPasarela = datosProceso.getPeticionServicio().getEntidad();
		if (idPasarela==null || "".equals(idPasarela))
		{
			idPasarela=PasarelasFactory.newSelectorPasarelaPago(context).seleccionarPasarela(datosProceso.getPeticionServicio().getTarjeta(), datosProceso.getPeticionServicio().getCcc());
		}
		ComunicadorPasarela comm=PasarelasFactory.newSelectorComunicadorPasarela(context).seleccionarComunicadorPasarela(idPasarela,datosProceso);
		
		comm.realizarConsulta();
		DatosComunicacion datosPasarela = comm.getDatosComunicacion();
		logger.debug ("Resultado de la comunicación con entidad remota:" + datosPasarela.getResultadoComunicacion());
		logger.debug ("Resultado remoto:" + datosPasarela.getResultadoDescripcionRemoto());
		String estadoBD= datosProceso.getEstado();
		//Según el estado, realizamos una acción u otra.
		if (!datosPasarela.isError())
		{
			logger.debug("Consulta en entidad finalizada con estado:" + datosPasarela.getEstadoPago());
			if (datosPasarela.getEstadoPago().equals(EstadosPago.PAGADO) || 
				datosPasarela.getEstadoPago().equals(EstadosPago.ANULADO))
			{
				this.datosEntidadRemota.setNumeroOperacion(datosPasarela.getNumeroOperacion());
				this.datosEntidadRemota.setNrc(datosPasarela.getNrc());
				this.datosEntidadRemota.setFechaPago(datosPasarela.getFechaPago());
			}
			logger.debug ("Se actualiza el estado en PATE.");
			switch (datosPasarela.getEstadoPago())
			{
			case PAGADO:
			case ANULADO:
			case ERROR:
				estadoBD=datosPasarela.getEstadoPago().getValor();
				break;
			default:
				break;
			}
			// Actualizamos en PATE
			GestorLlamadasBD ges = BDFactory.newGestorLlamadasBD(context);
			//CRUBENCVS. No se actualiza nif de operante o fecha de devengo, ya que el estado en la entidad remota se produjo con los que había en la tabla.
			//CRUBENCVS 42479. Se incluye la fecha de anulación como parámetro, aunque sólo tendrá valor para las plataformas de pago
			ResultadoLlamadaBD resActualiza = ges.actualizarBD (null,estadoBD,datosProceso.getJustificante(),datosProceso.getPeticionServicio().getIdentificacion(), datosProceso.getPeticionServicio().getReferencia(),datosPasarela.getNumeroOperacion(),datosPasarela.getFechaPago(),datosPasarela.getNrc(), "","", datosPasarela.getResultadoComunicacion(), null);
			if (resActualiza.isError())
			{
				this.datosEntidadRemota.setError(true);
				this.datosEntidadRemota.setCodigoError(resActualiza.getCodError());
				this.datosEntidadRemota.setTextoError("");
			}
		}
		else
		{
			this.datosEntidadRemota.setError(true);
			this.datosEntidadRemota.setCodigoError(datosPasarela.getCodigoError());
			this.datosEntidadRemota.setTextoError(datosPasarela.getTextoError());
		}
		this.datosEntidadRemota.setEstado(datosPasarela.getEstadoPago());
	}
	
	/**
	 * Indica si hubo un error en la operación de sincronización.
	 * @return
	 */
	public boolean huboError()
	{
		return this.datosEntidadRemota.isError();
	}
	/**
	 * Recupera el estado en que ha terminado el pago después de la sincronización.
	 * @return
	 */
	public EstadosPago getEstado()
	{
		return this.datosEntidadRemota.getEstado();
	}
	/**
	 * Recupera el número de operación recuperado tras la sincronización, si hay.
	 * @return
	 */
	public String getNumeroOperacion()
	{
		return this.datosEntidadRemota.getNumeroOperacion();
	}
	/**
	 * Recupera el número de NRC recuperado tras la sincronización, si hay.
	 * @return
	 */
	public String getNrc()
	{
		return this.datosEntidadRemota.getNrc();
	}
	/**
	 * Recupera la fecha de pago recuperada tras la sincronización, si hay.
	 * @return
	 */
	public String getFechaPago()
	{
		return this.datosEntidadRemota.getFechaPago();
	}
	/**
	 * Recupera el código de error producido, si hay.
	 * @return
	 */
	public String getCodError()
	{
		return this.datosEntidadRemota.getCodigoError();
	}
	/**
	 * Recupera el texto de error producido, si hay.
	 * @return
	 */
	public String getTextoError()
	{
		return this.datosEntidadRemota.getTextoError();
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
