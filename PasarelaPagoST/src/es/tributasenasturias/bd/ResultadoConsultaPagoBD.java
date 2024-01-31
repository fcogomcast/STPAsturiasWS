package es.tributasenasturias.bd;

import es.tributasenasturias.dao.DatosPagoBD;

/**
 * Clase para la consulta de datos de pago, utilizada en las consultas de la nueva plataforma de pago
 * @author crubencvs
 *
 */
public class ResultadoConsultaPagoBD {
	private boolean consultaCorrecta;
	private boolean hayDatos;
	private boolean codError;
	private boolean textoError;
	private DatosPagoBD datosPagoBD= new DatosPagoBD();
	private boolean datosConsistentes;
	public final boolean isConsultaCorrecta() {
		return consultaCorrecta;
	}
	public final void setConsultaCorrecta(boolean consultaCorrecta) {
		this.consultaCorrecta = consultaCorrecta;
	}
	public final boolean isHayDatos() {
		return hayDatos;
	}
	public final void setHayDatos(boolean hayDatos) {
		this.hayDatos = hayDatos;
	}
	public final boolean isCodError() {
		return codError;
	}
	public final void setCodError(boolean codError) {
		this.codError = codError;
	}
	public final boolean isTextoError() {
		return textoError;
	}
	public final void setTextoError(boolean textoError) {
		this.textoError = textoError;
	}
	public final DatosPagoBD getDatosPagoBD() {
		return datosPagoBD;
	}
	public final void setDatosPagoBD(DatosPagoBD datosPagoBD) {
		this.datosPagoBD = datosPagoBD;
	}
	/**
	 * @return the datosConsistentes
	 */
	public final boolean isDatosConsistentes() {
		return datosConsistentes;
	}
	/**
	 * @param datosConsistentes the datosConsistentes to set
	 */
	public final void setDatosConsistentes(boolean datosConsistentes) {
		this.datosConsistentes = datosConsistentes;
	}
	
}
