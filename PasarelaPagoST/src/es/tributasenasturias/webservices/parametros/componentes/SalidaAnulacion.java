package es.tributasenasturias.webservices.parametros.componentes;

public class SalidaAnulacion {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9082524431857778905L;
	private String error;
	private String resultado;
	private String operacion;
	private String fechaPago;
	/**
	 * @return el valor de atributo fechaPago
	 */
	public String getFechaPago() {
		return fechaPago;
	}
	/**
	 * @param fechaPago el valor del atributo fechaPago que se establecerá
	 */
	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}
	/**
	 * @return el valor de atributo operacion
	 */
	public String getOperacion() {
		return operacion;
	}
	/**
	 * @param operacion el valor del atributo operacion que se establecerá
	 */
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	/**
	 * @return el valor de atributo resultado
	 */
	public String getResultado() {
		return resultado;
	}
	/**
	 * @param resultado el valor del atributo resultado que se establecerá
	 */
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	/**
	 * @param codigo código de mensaje a establecer como resultado.
	 */
	public void setMessage (String msg)
	{
		this.error = msg;
		this.resultado=es.tributasenasturias.utils.Mensajes.getExternalText(error);
	}

}
