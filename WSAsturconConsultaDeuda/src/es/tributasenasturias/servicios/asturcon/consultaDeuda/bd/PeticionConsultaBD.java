package es.tributasenasturias.servicios.asturcon.consultaDeuda.bd;



import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.services.lanzador.client.LanzadorException;
import es.tributasenasturias.services.lanzador.client.LanzadorFactory;
import es.tributasenasturias.services.lanzador.client.ParamType;
import es.tributasenasturias.services.lanzador.client.ProcedimientoAlmacenado;
import es.tributasenasturias.services.lanzador.client.TLanzador;
import es.tributasenasturias.services.lanzador.client.response.RespuestaLanzador;

public class PeticionConsultaBD {
	private String procedimiento;
	private String esquema;
	private String endpointLanzador;
	private SOAPHandler<SOAPMessageContext> manejadorMensajes = null;

	TLanzador lanzador;
	private boolean peticionValida;

	// Resultado de la petición.
	// VALIDA: Se ha insertado correctamente en la base de datos.
	// DUPLICADA: 
	// NO_VALIDA: No se ha podido insertar en la base de datos. En este caso
	// también se dará de alta
	// un mensaje informativo.
	public static enum ResultadosPeticion {
		VALIDA, NO_VALIDA, DUPLICADA
	};

	private String infoResultadoPeticion;
	private ResultadosPeticion resultadoPeticion;

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
	public PeticionConsultaBD(DatosConexion datos) {
		this.endpointLanzador = datos.endpointLanzador;
		this.procedimiento = datos.procedimiento;
		this.esquema = datos.esquema;
		this.manejadorMensajes = datos.manejadorMensajes;
	}

	/**
	 * Realiza la petición de la consulta a base de datos.
	 * Establece los valores de las variables que indican si hubo error, el resultado concreto 
	 * de la petición(válida, duplicada o no válida), y el texto de información sobre el resultado.
	 * Se pueden recuperar con getResultadoPeticion, isPeticionValida y getInfoResultadoPeticion
	 * @param mensajeEntrada
	 *            Mensaje de entrada a guardar
	 * @throws DatosException
	 *             Si se produce un error al ejecutar la petición.
	 */
	public void solicitarConsulta(String mensajeEntrada)
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
						"Error producido al consignar la petición en base de datos:"
								+ respuesta.getTextoError());
			}
			String resultado = respuesta.getValue("CANU_CADENAS_NUMEROS", 1, "STRING1_CANU");
			String descResultado = respuesta.getValue("CANU_CADENAS_NUMEROS", 1, "STRING2_CANU");
			if (resultado.equals("OK")) {
				// Indicar que ha terminado bien.
				this.peticionValida = true;
				resultadoPeticion = ResultadosPeticion.VALIDA;
				this.infoResultadoPeticion = "OK";
	
			}else if (resultado.equals("DUP")){
				//Duplicada. Es correcto, pero no se ha insertado nada.
				this.infoResultadoPeticion="OK";
				this.peticionValida=true;
				resultadoPeticion = ResultadosPeticion.DUPLICADA;
			}
			else {
				this.peticionValida = false;
				resultadoPeticion = ResultadosPeticion.NO_VALIDA;
				if (!descResultado.equals("")) {
					this.infoResultadoPeticion = descResultado;
				}
			}
		}
		catch (LanzadorException ex)
		{
			throw new DatosException (ex.getMessage(),ex);
		}
	}

	public boolean isPeticionValida() {
		return peticionValida;
	}

	public void setPeticionValida(boolean peticionValida) {
		this.peticionValida = peticionValida;
	}

	public String getInfoResultadoPeticion() {
		return infoResultadoPeticion;
	}

	public void setInfoResultadoPeticion(String infoResultadoPeticion) {
		this.infoResultadoPeticion = infoResultadoPeticion;
	}

	public ResultadosPeticion getResultadoPeticion() {
		return resultadoPeticion;
	}

	public void setResultadoPeticion(ResultadosPeticion resultadoPeticion) {
		this.resultadoPeticion = resultadoPeticion;
	}
}
