package es.tributasenasturias.webservices.types;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "inicioOperacionPagoResponse", propOrder = {
	    "esError",
	    "codigo",
	    "mensaje",
	    "pagado",
	    "fechaPago",
	    "justificante",
	    "nrc",
	    "numeroOperacion",
	    "hash",
	    "mac"
	})
public class InicioOperacionPagoResponse {

	
	protected String esError;

	protected String codigo;

	protected String mensaje;

	protected String hash;

	protected String pagado;

	protected String justificante;

	protected String nrc;

	protected String numeroOperacion;

	protected String fechaPago;

	protected String mac;

	/**
	 * @return the esError
	 */
	public final String getEsError() {
		return esError;
	}

	/**
	 * @param esError
	 *            the esError to set
	 */
	public final void setEsError(String esError) {
		this.esError = esError;
	}

	/**
	 * @return the codigo
	 */
	public final String getCodigo() {
		return codigo==null?"":codigo;
	}

	/**
	 * @param codigo
	 *            the codigo to set
	 */
	public final void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the mensaje
	 */
	public final String getMensaje() {
		return mensaje==null?"":mensaje;
	}

	/**
	 * @param mensaje
	 *            the mensaje to set
	 */
	public final void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the hash
	 */
	public final String getHash() {
		return hash;
	}

	/**
	 * @param hash
	 *            the hash to set
	 */
	public final void setHash(String hash) {
		this.hash = hash;
	}

	/**
	 * @return the pagado
	 */
	public final String getPagado() {
		return pagado;
	}

	/**
	 * @param pagado
	 *            the pagado to set
	 */
	public final void setPagado(String pagado) {
		this.pagado = pagado;
	}

	/**
	 * @return the justificante
	 */
	public final String getJustificante() {
		return justificante;
	}

	/**
	 * @param justificante
	 *            the justificante to set
	 */
	public final void setJustificante(String justificante) {
		this.justificante = justificante;
	}

	/**
	 * @return the nrc
	 */
	public final String getNrc() {
		return nrc;
	}

	/**
	 * @param nrc
	 *            the nrc to set
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
	 * @param numeroOperacion
	 *            the numeroOperacion to set
	 */
	public final void setNumeroOperacion(String numeroOperacion) {
		this.numeroOperacion = numeroOperacion;
	}

	/**
	 * @return the fechaPago
	 */
	public final String getFechaPago() {
		return fechaPago;
	}

	/**
	 * @param fechaPago
	 *            the fechaPago to set
	 */
	public final void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}

	/**
	 * @return the mac
	 */
	public final String getMac() {
		return mac;
	}

	/**
	 * @param mac
	 *            the mac to set
	 */
	public final void setMac(String mac) {
		this.mac = mac;
	}

}
