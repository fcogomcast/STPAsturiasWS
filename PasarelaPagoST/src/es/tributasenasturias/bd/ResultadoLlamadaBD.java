package es.tributasenasturias.bd;

import es.tributasenasturias.dao.DatosPagoBD;

/**
 * Modela una petición en base de datos.
 * @author crubencvs
 *
 */
public class ResultadoLlamadaBD {
	private boolean error;
	private String codError;
	private String textoError;
	private DatosPagoBD datosPagoBD= new DatosPagoBD();
	/**
	 * @return the error
	 */
	public final boolean isError() {
		return error;
	}
	/**
	 * @param error the error to set
	 */
	public final void setError(boolean error) {
		this.error = error;
	}
	/**
	 * @return the codError
	 */
	public final String getCodError() {
		return codError;
	}
	/**
	 * @param codError the codError to set
	 */
	public final void setCodError(String codError) {
		this.codError = codError;
	}
	/**
	 * @return the textoError
	 */
	public final String getTextoError() {
		return textoError;
	}
	/**
	 * @param textoError the textoError to set
	 */
	public final void setTextoError(String textoError) {
		this.textoError = textoError;
	}
	/**
	 * @return the datosPagoBD
	 */
	public final DatosPagoBD getDatosPagoBD() {
		return datosPagoBD;
	}
	/**
	 * @param datosPagoBD the datosPagoBD to set
	 */
	public final void setDatosPagoBD(DatosPagoBD datosPagoBD) {
		this.datosPagoBD = datosPagoBD;
	}
	
}
