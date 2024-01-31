package es.tributasenasturias.bd;

import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.dao.DatosProceso;
import es.tributasenasturias.dao.TipoLlamada;
import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.webservices.context.CallContext;
import es.tributasenasturias.webservices.context.IContextReader;

/**
 * Consulta de un pago telemático en la base de datos.
 * @author crubencvs
 *
 */
public class ConsultaPagoBD implements IContextReader{

	private GestorLlamadasBD ges;
	private CallContext context;
	
	/**
	 * La clase que indicará el resultado de una consulta de pago a BD
	 * @author crubencvs
	 *
	 */
	public static class Resultado
	{
		private String codigo;
		private String message;
		private boolean error;
		//CRUBENCVS 04/03/2021. Se permite acceder al resultado completo, por 
		//si interesa
		private ResultadoLlamadaBD resultadoBD;
		
		/**
		 * @return the codigo
		 */
		public final String getCodigo() {
			return codigo;
		}
		/**
		 * @param codigo the codigo to set
		 */
		public final void setCodigo(String codigo) {
			this.codigo = codigo;
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
		/**
		 * @return the message
		 */
		public final String getMessage() {
			return message;
		}
		/**
		 * @param message the message to set
		 */
		public final void setMessage(String message) {
			this.message = message;
		}
		public final ResultadoLlamadaBD getResultadoBD() {
			return resultadoBD;
		}
		public final void setResultadoBD(ResultadoLlamadaBD resultadoBD) {
			this.resultadoBD = resultadoBD;
		}
		
	}
	
	public ConsultaPagoBD(CallContext context) {
		this.context = context; 
		ges = BDFactory.newGestorLlamadasBD(context);
	}
	
	private ResultadoLlamadaBD consultaJustificanteReferencia(TipoLlamada tipoLlamada, String emisora, String justificante, String identificacion, String referencia) throws PasarelaPagoException
	{
		ResultadoLlamadaBD resConsultaPate = ges.consultaBD(tipoLlamada,emisora,justificante,identificacion,referencia);
		return resConsultaPate;
	}
	
	private ResultadoLlamadaBD consultaNumeroUnico(TipoLlamada tipoLlamada, String aplicacion, String numeroUnico) throws PasarelaPagoException
	{
		ResultadoLlamadaBD resConsultaPate = ges.consultaBD(tipoLlamada,aplicacion, numeroUnico);
		return resConsultaPate;
	}
	public Resultado consultarPagoBD(DatosProceso datosProceso) throws PasarelaPagoException
	{
		Resultado res= new Resultado();
		DatosEntradaServicio peticion = datosProceso.getPeticionServicio();
		ResultadoLlamadaBD resConsultaPate=null;
		if (peticion.getAplicacion()!=null && !"".equals(peticion.getAplicacion())
			&& peticion.getNumeroUnico()!=null && !"".equals(peticion.getNumeroUnico()))
		{
			resConsultaPate = consultaNumeroUnico(datosProceso.getTipoLlamada(),peticion.getAplicacion(), peticion.getNumeroUnico());
		}
		else
		{
			resConsultaPate = consultaJustificanteReferencia(datosProceso.getTipoLlamada(),peticion.getEmisora(),peticion.getJustificante(),peticion.getIdentificacion(),peticion.getReferencia());
		}
		res.setResultadoBD(resConsultaPate);
		if (!resConsultaPate.isError())
		{
			datosProceso.setFechaOperacion(resConsultaPate.getDatosPagoBD().getFechaOperacion());
			datosProceso.setFechaPago(resConsultaPate.getDatosPagoBD().getFechaPago());
			datosProceso.setEstado(resConsultaPate.getDatosPagoBD().getEstado());
			datosProceso.setNrc(resConsultaPate.getDatosPagoBD().getNrc());
			datosProceso.setNifContribuyente(resConsultaPate.getDatosPagoBD().getNifContribuyente());
			datosProceso.setNifOperante(resConsultaPate.getDatosPagoBD().getNifOperante());
			datosProceso.setDatoEspecifico(resConsultaPate.getDatosPagoBD().getDatoEspecifico());
			datosProceso.setExpediente(resConsultaPate.getDatosPagoBD().getExpediente());
			datosProceso.setImporte(resConsultaPate.getDatosPagoBD().getImporte());
			datosProceso.setFechaDevengo(resConsultaPate.getDatosPagoBD().getFechaDevengo());
			if (resConsultaPate.getDatosPagoBD().getJustificante()!=null &&  
					!"".equals(resConsultaPate.getDatosPagoBD().getJustificante())) //Puede haberse recuperado.
			{
				datosProceso.setJustificante(resConsultaPate.getDatosPagoBD().getJustificante());
			}
			else
			{
				datosProceso.setJustificante(peticion.getJustificante()); //Si no se ha recuperado, el de la entrada si hubiera
			}
			datosProceso.setNumeroOperacion(resConsultaPate.getDatosPagoBD().getNumeroOperacion());
			datosProceso.setPasarelaPagoPeticion(resConsultaPate.getDatosPagoBD().getPasarelaPago());
			res.setCodigo(resConsultaPate.getCodError());
			res.setError(false);
		}
		else
		{
			res.setCodigo(resConsultaPate.getCodError());
			res.setMessage(resConsultaPate.getTextoError());
			res.setError(true);
		}
		return res;
	}
	
	
	@Override
	public CallContext getCallContext() {
		
		return context ;
	}
	@Override
	public void setCallContext(CallContext ctx) {
		context= ctx;
	}
	
}

