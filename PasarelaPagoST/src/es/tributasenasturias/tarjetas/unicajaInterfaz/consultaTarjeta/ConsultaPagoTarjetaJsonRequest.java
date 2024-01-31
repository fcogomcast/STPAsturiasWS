package es.tributasenasturias.tarjetas.unicajaInterfaz.consultaTarjeta;

import com.google.gson.annotations.SerializedName;

public class ConsultaPagoTarjetaJsonRequest {
	@SerializedName(value="referenciaExt")
	private String referenciaExt;

	/**
	 * @return the referenciaExt
	 */
	public final String getReferenciaExt() {
		return referenciaExt;
	}

	/**
	 * @param referenciaExt the referenciaExt to set
	 */
	public final void setReferenciaExt(String referenciaExt) {
		this.referenciaExt = referenciaExt;
	}
	
}
