package es.tributasenasturias.webservices.parametros.componentes;

public class SalidaPeticion {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2772713706106324379L;
	
	private String error;
	private String operacion;
	private String resultado;
	private String fechaPago;
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
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getOperacion() {
		return operacion;
	}
	public void setOperacion(String operacion) {
		this.operacion = operacion;
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
