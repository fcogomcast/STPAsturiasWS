package es.tributasenasturias.plataforma;

import es.tributasenasturias.bd.BDFactory;
import es.tributasenasturias.bd.GestorLlamadasBD;
import es.tributasenasturias.bd.ResultadoLlamadaBD;
import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.dao.DatosPagoBD;
import es.tributasenasturias.dao.DatosProceso;
import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.pasarelas.PasarelasFactory;
import es.tributasenasturias.pasarelas.comunicacion.ComunicadorPasarela;
import es.tributasenasturias.pasarelas.comunicacion.DatosComunicacion;
import es.tributasenasturias.utils.EstadosPago;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.CallContextConstants;

/**
 * Gestiona el estado remoto de los pagos en plataforma (consulta de estado de pago en plataforma).
 * Cada una tendrá su propia gestión, esta clase sólo tendrá la lógica para sincronizar 
 * las pasarelas antiguas. No utilizamos la lógica de la pasarela antigua, ya que necesitamos
 * controlar mejor los estados, y no queremos tocar el código antiguo.
 * @author crubencvs
 *
 */
public class GestorEstadoRemotoPago {

	@SuppressWarnings("unused")
	private Preferencias pref;
	private Logger log;
	private CallContext context;
	
	public GestorEstadoRemotoPago (CallContext context){
		this.context=context;
		pref = (Preferencias) context.get(CallContextConstants.PREFERENCIAS);
		log = (Logger) context.get(CallContextConstants.LOG_APLICACION);
	}
	
	//Resultado de sincronización con pasarela de pago antigua
	public static class ResultadoSincronizacionPasarelaPagoAntigua{
		private boolean error;
		private boolean tributoPagado;
		private boolean tributoAnulado;
		private boolean noHayDatos;
		private DatosComunicacion resultadoConsulta;
		private String codError;
		private String textoError;
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
		/**
		 * @return the tributoPagado
		 */
		public final boolean isTributoPagado() {
			return tributoPagado;
		}
		/**
		 * @param tributoPagado the tributoPagado to set
		 */
		public final void setTributoPagado(boolean tributoPagado) {
			this.tributoPagado = tributoPagado;
		}
		/**
		 * @return the tributoAnulado
		 */
		public final boolean isTributoAnulado() {
			return tributoAnulado;
		}
		/**
		 * @param tributoAnulado the tributoAnulado to set
		 */
		public final void setTributoAnulado(boolean tributoAnulado) {
			this.tributoAnulado = tributoAnulado;
		}
		/**
		 * @return the resultadoConsulta
		 */
		public final DatosComunicacion getResultadoConsulta() {
			return resultadoConsulta;
		}
		/**
		 * @param resultadoConsulta the resultadoConsulta to set
		 */
		public final void setResultadoConsulta(DatosComunicacion resultadoConsulta) {
			this.resultadoConsulta = resultadoConsulta;
		}
		/**
		 * @return the codError
		 */
		public final String getCodError() {
			return codError;
		}
		/**
		 * @param codError the codError to set
		 */
		public final void setCodError(String codError) {
			this.codError = codError;
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
		 * @return the noHayDatos
		 */
		public final boolean isNoHayDatos() {
			return noHayDatos;
		}
		/**
		 * @param noHayDatos the noHayDatos to set
		 */
		public final void setNoHayDatos(boolean noHayDatos) {
			this.noHayDatos = noHayDatos;
		}
		
	}
	
	/**
	 * Sincronización de estado en base de datos con las pasarelas de pago antigua, no con las plataformas (UniversalPay)
	 * @param peticion
	 * @param datosPate
	 * @return
	 * @throws PasarelaPagoException
	 */
	public ResultadoSincronizacionPasarelaPagoAntigua sincronizarEstadoBDConPasarelaAntigua(
														String origen,
														String modalidad,
														String emisora,
														String modelo,
														String entidad,
														String nifContribuyente,
														String fechaDevengo,
														String datoEspecifico,
														String identificacion,
														String referencia,
														String numeroAutoliquidacion,
														String expediente,
														String importe,
														String nifOperante,
														DatosPagoBD datosPate) throws PasarelaPagoException{
		ResultadoSincronizacionPasarelaPagoAntigua res= new ResultadoSincronizacionPasarelaPagoAntigua();
		try {
			//Montamos los datos de proceso según pasarela antigua.
			DatosEntradaServicio datosServicio= new DatosEntradaServicio(
														origen, 
														modalidad, 
														"", //Cliente. No existe en la nueva
														emisora, 
														modelo, 
														entidad,
														nifContribuyente, 
														"", //Nombre de contribuyente. No existe en la nueva
														fechaDevengo, 
														datoEspecifico, 
														identificacion,
														referencia, 
														numeroAutoliquidacion, 
														expediente,
														importe, 
														"", //Tarjeta 
														"", //Fecha de caducidad de tarjeta
														"",  //CCC 
														nifOperante, 
														"", //Aplicación
														"", //Número único 
														"", //Número aplicación 
														"", //Libre 
														"" //MAC
														);
			DatosProceso datosProceso= new DatosProceso(datosServicio);

			datosProceso.setDatoEspecifico(datosPate.getDatoEspecifico());
			datosProceso.setEstado(datosPate.getEstado());
			datosProceso.setExpediente(datosPate.getExpediente());
			datosProceso.setFechaDevengo(datosPate.getFechaDevengo());
			datosProceso.setJustificante(datosPate.getJustificante());
			datosProceso.setPasarelaPagoPeticion(datosPate.getPasarelaPago());
			datosProceso.setImporte(datosPate.getImporte());
			datosProceso.setNifContribuyente(datosPate.getNifContribuyente());
			datosProceso.setNifOperante(datosPate.getNifOperante());
			
			ComunicadorPasarela comm=PasarelasFactory.newSelectorComunicadorPasarela(context).seleccionarComunicadorPasarela(datosPate.getPasarelaPago(),datosProceso);
			//Si hemos llegado aquí, es que el estado no es "P","A" o "G" (que además este último sólo existe para plataforma de pago)
			comm.realizarConsulta();
			//Aquí ya tendremos el resultado de sincronización. A ver qué es.
			DatosComunicacion datosComunicacion= comm.getDatosComunicacion();
			if (!datosComunicacion.isError()) // La consulta termina correctamente.
			{
				log.debug("Consulta en entidad finalizada con estado:" + datosComunicacion.getEstadoPago());
				log.debug ("Resultado de la comunicación con entidad remota:" + datosComunicacion.getResultadoComunicacion());
				log.debug ("Resultado remoto:" + datosComunicacion.getResultadoDescripcionRemoto());
				if (EstadosPago.NO_PAGADO.equals(datosComunicacion.getEstadoPago())){
					//No hay datos de pago. 
					log.debug("No hay datos de pago en entidad remota");
					res.setError(false);
					res.setNoHayDatos(true);
				}
				else if (datosComunicacion.getEstadoPago().equals(EstadosPago.PAGADO) && 
						(datosComunicacion.getNumeroOperacion()==null  || datosComunicacion.getNrc()==null || datosComunicacion.getFechaPago()==null))
				{
					log.debug("Hay datos de pago en entidad remota, pero no parecen consistentes. Falta número operación/NRC/fecha de pago");
					res.setError(true);
					res.setCodError(Mensajes.getErrorActualizacionBD());
					res.setTextoError(Mensajes.getExternalText(res.getCodError()));
				} else 
				{
					res.setResultadoConsulta(datosComunicacion);
					if (EstadosPago.PAGADO.equals(datosComunicacion.getEstadoPago())){
						log.debug("Tributo pagado en entidad remota");
						res.setTributoPagado(true);
					} else if (EstadosPago.ANULADO.equals(datosComunicacion.getEstadoPago())){
						log.debug("Tributo anulado en entidad remota");
						res.setTributoAnulado(true);
					}
					// Actualizamos en PATE siempre. Al menos, que nos indique qué ha fallado.
					//Esto es diferente a la pasarela antigua, que actualizaba sólo en caso de 
					//pagado o anulado. Aquí, queremos que nos deje cuál es el fallo actual.
					
					GestorLlamadasBD ges = BDFactory.newGestorLlamadasBD(context);
					//En la llamada no actualizamos operante ni fecha de devengo porque si está pagado o anulado, lo está con el operante y fecha que consta en la tabla, el de la última llamada.
					//En otro caso, por ejemplo para error, actualizamos con los que nos han puesto
					String nifOperanteActualizar= "";
					String fechaDevengoActualizar="";
					if (datosComunicacion.getEstadoPago()!=EstadosPago.PAGADO && 
					    datosComunicacion.getEstadoPago()!=EstadosPago.ANULADO){
						nifOperanteActualizar= nifOperante;
						fechaDevengoActualizar= fechaDevengo;
					} 
					log.debug ("Actualizamos.");
					ResultadoLlamadaBD resActualiza = 
							ges.actualizarBD (comm.getIdPasarela(),
											  datosComunicacion.getEstadoPago().getValor(),
											  datosProceso.getJustificante(),
											  datosProceso.getPeticionServicio().getIdentificacion()
											  , datosProceso.getPeticionServicio().getReferencia(),
											  datosComunicacion.getNumeroOperacion(),
											  datosComunicacion.getFechaPago(),
											  datosComunicacion.getNrc(), 
											  nifOperanteActualizar,
											  fechaDevengoActualizar,
											  datosComunicacion.getResultadoComunicacion(),
											  null); //En las pasarelas antiguas no se pasa la fecha de anulación, se considera la fecha actual
					if (resActualiza.isError())
					{
						log.error("No se puede sincronizar el estado de la base de datos con el de la entidad remota.");
						res.setCodError(resActualiza.getCodError());
						res.setTextoError(resActualiza.getTextoError());
						res.setError(true);
					} else {
						res.setError(false);
					}
					
				}
			} else {
				log.error("No se puede sincronizar el estado de la base de datos con el de la entidad remota.");
				res.setCodError(datosComunicacion.getCodigoError());
				res.setTextoError(datosComunicacion.getTextoError());
				res.setError(true);
			}
		} catch(Exception e){
			log.error ("Error al sincronizar el estado de pago local con el de entidad remota:"+ e.getMessage());
			res.setError(true);
			res.setCodError(Mensajes.getErrorConsulta());
			res.setTextoError(Mensajes.getExternalText(res.getCodError()));
		}
		
		return res;
	}
}
