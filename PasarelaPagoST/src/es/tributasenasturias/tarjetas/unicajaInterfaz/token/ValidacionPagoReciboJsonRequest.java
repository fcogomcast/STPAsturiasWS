package es.tributasenasturias.tarjetas.unicajaInterfaz.token;

import com.google.gson.annotations.SerializedName;

public class ValidacionPagoReciboJsonRequest {
	@SerializedName(value="tipoPantalla")
	private String tipoPantalla;
	@SerializedName(value="emisora")
	private String emisora;
	@SerializedName(value="sufijo")
	private String sufijo;
	@SerializedName(value="referencia")
	private String referencia;
	@SerializedName(value="identificacion")
	private String identificacion;
	@SerializedName(value="importe")
	private double importe;
	@SerializedName(value="fechaDevengo")
	private String fechaDevengo;
	@SerializedName(value="nif")
	private String nif;
	@SerializedName(value="expediente")
	private String expediente;
	@SerializedName(value="datoEspecifico")
	private String datoEspecifico;
	@SerializedName(value="referenciaExt")	
	private String referenciaExterna;
	/**
	 * @return the tipoPantalla
	 */
	public final String getTipoPantalla() {
		return tipoPantalla;
	}
	/**
	 * @param tipoPantalla the tipoPantalla to set
	 */
	public final void setTipoPantalla(String tipoPantalla) {
		this.tipoPantalla = tipoPantalla;
	}
	/**
	 * @return the emisora
	 */
	public final String getEmisora() {
		return emisora;
	}
	/**
	 * @param emisora the emisora to set
	 */
	public final void setEmisora(String emisora) {
		this.emisora = emisora;
	}
	/**
	 * @return the sufijo
	 */
	public final String getSufijo() {
		return sufijo;
	}
	/**
	 * @param sufijo the sufijo to set
	 */
	public final void setSufijo(String sufijo) {
		this.sufijo = sufijo;
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
	 * @return the identificacion
	 */
	public final String getIdentificacion() {
		return identificacion;
	}
	/**
	 * @param identificacion the identificacion to set
	 */
	public final void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}
	/**
	 * @return the importe
	 */
	public final double getImporte() {
		return importe;
	}
	/**
	 * @param importe the importe to set
	 */
	public final void setImporte(double importe) {
		this.importe = importe;
	}
	/**
	 * @return the fechaDevengo
	 */
	public final String getFechaDevengo() {
		return fechaDevengo;
	}
	/**
	 * @param fechaDevengo the fechaDevengo to set
	 */
	public final void setFechaDevengo(String fechaDevengo) {
		this.fechaDevengo = fechaDevengo;
	}
	/**
	 * @return the nif
	 */
	public final String getNif() {
		return nif;
	}
	/**
	 * @param nif the nif to set
	 */
	public final void setNif(String nif) {
		this.nif = nif;
	}
	/**
	 * @return the expediente
	 */
	public final String getExpediente() {
		return expediente;
	}
	/**
	 * @param expediente the expediente to set
	 */
	public final void setExpediente(String expediente) {
		this.expediente = expediente;
	}
	/**
	 * @return the datoEspecifico
	 */
	public final String getDatoEspecifico() {
		return datoEspecifico;
	}
	/**
	 * @param datoEspecifico the datoEspecifico to set
	 */
	public final void setDatoEspecifico(String datoEspecifico) {
		this.datoEspecifico = datoEspecifico;
	}
	/**
	 * @return the referenciaExterna
	 */
	public final String getReferenciaExterna() {
		return referenciaExterna;
	}
	/**
	 * @param referenciaExterna the referenciaExterna to set
	 */
	public final void setReferenciaExterna(String referenciaExterna) {
		this.referenciaExterna = referenciaExterna;
	}
	
	
}
