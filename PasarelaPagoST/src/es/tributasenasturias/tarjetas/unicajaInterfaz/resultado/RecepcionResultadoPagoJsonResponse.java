package es.tributasenasturias.tarjetas.unicajaInterfaz.resultado;

import com.google.gson.annotations.SerializedName;

public class RecepcionResultadoPagoJsonResponse {
	@SerializedName(value="resultado")
	private String resultado;
	@SerializedName(value="resultadoDescripcion")
	private String resultadoDescripcion;
	
	public RecepcionResultadoPagoJsonResponse(String resultado, String resultadoDescripcion){
		this.resultado= resultado;
		this.resultadoDescripcion= resultadoDescripcion;
	}

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
	
	
}
