package es.tributasenasturias.webservices.types;

public class RespuestaConsultaPagoTarjeta {

	private String error;
	private String resultado;
	private String operacion;
	private String fechaPago;
	private String doc_justificante;
	private String getNotNullValue (String arg)
	 {
		 return (arg==null)?"":arg;
	 }
	public String getError() {
		return getNotNullValue(error);
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getOperacion() {
		return getNotNullValue(operacion);
	}
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}
	public String getFechaPago() {
		return getNotNullValue(fechaPago);
	}
	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}
	/**
	 * @return el valor de atributo resultado
	 */
	public String getResultado() {
		return getNotNullValue(resultado);
	}
	/**
	 * @param resultado el valor del atributo resultado que se establecerá
	 */
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	
	/**
	 * @return the doc_justificante
	 */
	public final String getDoc_justificante() {
		return doc_justificante;
	}
	/**
	 * @param doc_justificante the doc_justificante to set
	 */
	public final void setDoc_justificante(String doc_justificante) {
		this.doc_justificante = doc_justificante;
	}
	/**
	 * @param codigo código de mensaje a establecer como resultado.
	 */
	public void setMessage (String codigo)
	{
		setMessage (codigo,null);
	}
	/**
	 * Incorpora al mensaje de salida un código de error y opcionalmente un texto.
	 * @param codigo Código de error
	 * @param texto Texto de error, opcional. Si no existe, se tomará en base al código.
	 */
	public void setMessage (String codigo, String texto)
	{
		this.error = codigo;
		if (texto!=null && !"".equals(texto))
		{
			this.resultado=texto;
		}
		else
		{
			this.resultado=es.tributasenasturias.utils.Mensajes.getExternalText(error);
		}
	}
	
	public String getMensajeCompuesto()
	{
		return error +"-"+resultado;
	}
}
