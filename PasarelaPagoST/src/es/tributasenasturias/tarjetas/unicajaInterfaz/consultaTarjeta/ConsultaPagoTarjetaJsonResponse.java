package es.tributasenasturias.tarjetas.unicajaInterfaz.consultaTarjeta;

import com.google.gson.annotations.SerializedName;

public class ConsultaPagoTarjetaJsonResponse {
	@SerializedName(value="referenciaMunicipal")
	private String referenciaMunicipal;
	@SerializedName(value="estado")
	private int estado;
	@SerializedName(value="referenciaUnica")
	private String referenciaUnica;
	@SerializedName(value="fechaHora")
	private String fechaHora;
	@SerializedName(value="nrc")
	private String nrc;
	/**
	 * @return the referenciaMunicipal
	 */
	public final String getReferenciaMunicipal() {
		return referenciaMunicipal;
	}
	/**
	 * @param referenciaMunicipal the referenciaMunicipal to set
	 */
	public final void setReferenciaMunicipal(String referenciaMunicipal) {
		this.referenciaMunicipal = referenciaMunicipal;
	}
	/**
	 * @return the estado
	 */
	public final int getEstado() {
		return estado;
	}
	/**
	 * @param estado the estado to set
	 */
	public final void setEstado(int estado) {
		this.estado = estado;
	}
	/**
	 * @return the referenciaUnica
	 */
	public final String getReferenciaUnica() {
		return referenciaUnica;
	}
	/**
	 * @param referenciaUnica the referenciaUnica to set
	 */
	public final void setReferenciaUnica(String referenciaUnica) {
		this.referenciaUnica = referenciaUnica;
	}
	/**
	 * @return the fechaHora
	 */
	public final String getFechaHora() {
		return fechaHora;
	}
	/**
	 * @param fechaHora the fechaHora to set
	 */
	public final void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
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
	
}
