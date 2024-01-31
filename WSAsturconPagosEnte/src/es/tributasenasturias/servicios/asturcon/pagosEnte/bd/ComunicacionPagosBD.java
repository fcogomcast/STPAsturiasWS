package es.tributasenasturias.servicios.asturcon.pagosEnte.bd;



import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.services.lanzador.client.LanzadorException;
import es.tributasenasturias.services.lanzador.client.LanzadorFactory;
import es.tributasenasturias.services.lanzador.client.ParamType;
import es.tributasenasturias.services.lanzador.client.ProcedimientoAlmacenado;
import es.tributasenasturias.services.lanzador.client.TLanzador;
import es.tributasenasturias.services.lanzador.client.response.RespuestaLanzador;

public class ComunicacionPagosBD {
	private String procedimiento;
	private String esquema;
	private String endpointLanzador;
	private SOAPHandler<SOAPMessageContext> manejadorMensajes = null;

	TLanzador lanzador;
	private boolean comunicacionValida;

	// Resultado de la comunicación de pagos y anulaciones.
	// VALIDA: Se ha comunicado correctamente
	// NO_VALIDA: No se ha podido insertar en la base de datos. En este caso
	// también se dará de alta
	// un mensaje informativo.
	public static enum ResultadoComunicacion {
		VALIDO, NO_VALIDO
	};

	private String infoResultadoComunicacion;
	private ResultadoComunicacion resultadoComunicacion;

	public static class DatosConexion {
		private String procedimiento;
		private String esquema;
		private String endpointLanzador;
		private SOAPHandler<SOAPMessageContext> manejadorMensajes;

		public String getProcedimiento() {
			return procedimiento;
		}

		public void setProcedimiento(String procedimiento) {
			this.procedimiento = procedimiento;
		}

		public String getEsquema() {
			return esquema;
		}

		public void setEsquema(String esquema) {
			this.esquema = esquema;
		}

		public String getEndpointLanzador() {
			return endpointLanzador;
		}

		public void setEndpointLanzador(String endpointLanzador) {
			this.endpointLanzador = endpointLanzador;
		}

		public SOAPHandler<SOAPMessageContext> getManejadorMensajes() {
			return manejadorMensajes;
		}

		public void setManejadorMensajes(
				SOAPHandler<SOAPMessageContext> manejadorMensajes) {
			this.manejadorMensajes = manejadorMensajes;
		}

	}

	@SuppressWarnings("unchecked")
	public ComunicacionPagosBD(DatosConexion datos) {
		this.endpointLanzador = datos.endpointLanzador;
		this.procedimiento = datos.procedimiento;
		this.esquema = datos.esquema;
		this.manejadorMensajes = datos.manejadorMensajes;
	}

	/**
	 * Realiza la inserción de los pagos y anulaciones de traba en la base de datos.
	 * Establece los valores de las variables que indican si hubo error, el resultado concreto 
	 * , y el texto de información sobre el resultado.
	 * Se pueden recuperar con getResultadoPeticion, isPeticionValida y getInfoResultadoPeticion
	 * @param mensajeEntrada
	 *            Mensaje de entrada a guardar
	 * @throws DatosException
	 *             Si se produce un error al ejecutar la petición.
	 */
	public void comunicarPagos(String mensajeEntrada)
			throws DatosException {
		
		ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado(this.procedimiento,this.esquema);
		proc.param(mensajeEntrada, ParamType.CLOB);
		proc.param("P", ParamType.CADENA);
		try
		{
			if (this.manejadorMensajes!=null)
			{
				lanzador = LanzadorFactory.newTLanzador(this.endpointLanzador, manejadorMensajes);
			}
			else
			{
				lanzador = LanzadorFactory.newTLanzador(this.endpointLanzador);
			}
			String resultadoEjecutarPL=lanzador.ejecutar(proc);
			RespuestaLanzador respuesta = new RespuestaLanzador(resultadoEjecutarPL);
			if (respuesta.esErronea())
			{
				throw new DatosException(
						"Error producido al consignar los pagos y anulaciones en base de datos:"
								+ respuesta.getTextoError());
			}
			String resultado = respuesta.getValue("CANU_CADENAS_NUMEROS", 1, "STRING1_CANU");
			String descResultado = respuesta.getValue("CANU_CADENAS_NUMEROS", 1, "STRING2_CANU");
			if (resultado.equals("OK")) {
				// Indicar que ha terminado bien.
				this.comunicacionValida = true;
				resultadoComunicacion = ResultadoComunicacion.VALIDO;
				this.infoResultadoComunicacion = "OK";
	
			}else {
				this.comunicacionValida = false;
				resultadoComunicacion = ResultadoComunicacion.NO_VALIDO;
				if (!descResultado.equals("")) {
					this.infoResultadoComunicacion = descResultado;
				}
			}
		}
		catch (LanzadorException ex)
		{
			throw new DatosException (ex.getMessage(),ex);
		}
	}

	public boolean isComunicacionValida() {
		return comunicacionValida;
	}

	public void setComunicacionValida(boolean comunicacionValida) {
		this.comunicacionValida = comunicacionValida;
	}

	public String getInfoResultadoComunicacion() {
		return infoResultadoComunicacion;
	}

	public void setInfoResultadoComunicacion(String infoResultadoComunicacion) {
		this.infoResultadoComunicacion = infoResultadoComunicacion;
	}

	public ResultadoComunicacion getResultadoComunicacion() {
		return resultadoComunicacion;
	}

	public void setResultadoComunicacion(ResultadoComunicacion resultadoComunicacion) {
		this.resultadoComunicacion = resultadoComunicacion;
	}
}
