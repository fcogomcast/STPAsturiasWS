package es.tributasenasturias.validacion;

public class ResultadoIValidador {

	private String modulo;
	private boolean valido;
	private String codigoResultado;
	/**
	 * @return Identificador del m�dulo de validaci�n
	 */
	public final String getModulo() {
		return modulo;
	}
	/**
	 * @param modulo Identificador del m�dulo de validaci�n
	 */
	public final void setModulo(String modulo) {
		this.modulo = modulo;
	}
	/**
	 * @return El objeto es v�lido.
	 */
	public final boolean isValido() {
		return valido;
	}
	/**
	 * @param valido El objeto no es v�lido.
	 */
	public final void setValido(boolean valido) {
		this.valido = valido;
	}
	/**
	 * @return el c�digo de resultado
	 */
	public final String getCodigoResultado() {
		return codigoResultado;
	}
	/**
	 * @param codigoResultado el c�digo de resultado
	 */
	public final void setCodigoResultado(String codigoResultado) {
		this.codigoResultado = codigoResultado;
	}
	
}
