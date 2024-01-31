package es.tributasenasturias.validacion;

public class ResultadoIValidador {

	private String modulo;
	private boolean valido;
	private String codigoResultado;
	/**
	 * @return Identificador del módulo de validación
	 */
	public final String getModulo() {
		return modulo;
	}
	/**
	 * @param modulo Identificador del módulo de validación
	 */
	public final void setModulo(String modulo) {
		this.modulo = modulo;
	}
	/**
	 * @return El objeto es válido.
	 */
	public final boolean isValido() {
		return valido;
	}
	/**
	 * @param valido El objeto no es válido.
	 */
	public final void setValido(boolean valido) {
		this.valido = valido;
	}
	/**
	 * @return el código de resultado
	 */
	public final String getCodigoResultado() {
		return codigoResultado;
	}
	/**
	 * @param codigoResultado el código de resultado
	 */
	public final void setCodigoResultado(String codigoResultado) {
		this.codigoResultado = codigoResultado;
	}
	
}
