package es.tributasenasturias.tarjetas.unicajaInterfaz.resultado;

import com.google.gson.annotations.SerializedName;

/**
 * Clase de las peticiones que nos llegan informando de la finalización de un pago.
 * @author crubencvs
 *
 */
public class RecepcionResultadoPagoJsonRequest {

	@SerializedName(value="resultado")
	private String resultado;
	@SerializedName(value="resultadoDescripcion")
	private String resultadoDescripcion;
	@SerializedName(value="numPedido")
	private String numeroPedido;
	@SerializedName(value="referencia")
	private String referencia;
	@SerializedName(value="importe")
	private String importe;
	@SerializedName(value="moneda")
	private String moneda;
	@SerializedName(value="fechaPago")
	private String fechaPago;
	@SerializedName(value="NRC")
	private String nrc;
	@SerializedName(value="numOper")
	private String numeroOperacion;
	@SerializedName(value="tarjeta")
	private String tarjeta;
	/**
	 * @return the resultado
	 */
	public final String getResultado() {
		return resultado;
	}
	/**
	 * @param resultado the resultado to set
	 */
	public final void setResultado(String resultado) {
		this.resultado = resultado;
	}
	/**
	 * @return the resultadoDescripcion
	 */
	public final String getResultadoDescripcion() {
		return resultadoDescripcion;
	}
	/**
	 * @param resultadoDescripcion the resultadoDescripcion to set
	 */
	public final void setResultadoDescripcion(String resultadoDescripcion) {
		this.resultadoDescripcion = resultadoDescripcion;
	}
	/**
	 * @return the numeroPedido
	 */
	public final String getNumeroPedido() {
		return numeroPedido;
	}
	/**
	 * @param numeroPedido the numeroPedido to set
	 */
	public final void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}
	/**
	 * @return the referencia
	 */
	public final String getReferencia() {
		return referencia;
	}
	/**
	 * @param referencia the referencia to set
	 */
	public final void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	/**
	 * @return the importe
	 */
	public final String getImporte() {
		return importe;
	}
	/**
	 * @param importe the importe to set
	 */
	public final void setImporte(String importe) {
		this.importe = importe;
	}
	/**
	 * @return the moneda
	 */
	public final String getMoneda() {
		return moneda;
	}
	/**
	 * @param moneda the moneda to set
	 */
	public final void setMoneda(String moneda) {
		this.moneda = moneda;
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
	 * @return the tarjeta
	 */
	public final String getTarjeta() {
		return tarjeta;
	}
	/**
	 * @param tarjeta the tarjeta to set
	 */
	public final void setTarjeta(String tarjeta) {
		this.tarjeta = tarjeta;
	}
	
	
}
