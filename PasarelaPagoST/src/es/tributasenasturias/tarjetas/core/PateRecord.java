package es.tributasenasturias.tarjetas.core;

import java.util.Date;

public class PateRecord {

	private String numeroOperacion;
	private final String emisora;
	private final String importe;
	private final String referencia;
	private final String identificacion;
	private final String justificante;
	private final String nif;
	private final Date fechaDevengo;
	private final String datoEspecifico;
	private final String expediente;
	private final String modalidad;
	private Date fechaOperacion;
	private final String origen;
	private String nrc;
	private String estado;
	private Date fechaPago;
	//No incluyo XML_SERVICIO
	private String xmlServicio;
	private String resultado;
	private final String servicio;
	private final String numeroUnico;
	private final String nifOperante;
	private final Date fechaAnulacion;
	private final String nombreContribuyente;
	private final String pasarelaPago;
	private final String procesado;
	private final String operacionEpst;
	private final String medioPago;
	private final String hashDatos;
	private String token;
	/**
	 * @param numeroOperacion
	 * @param emisora
	 * @param importe
	 * @param referencia
	 * @param identificacion
	 * @param justificante
	 * @param nif
	 * @param fechaDevengo
	 * @param datoEspecifico
	 * @param expediente
	 * @param modalidad
	 * @param fechaOperacion
	 * @param origen
	 * @param nrc
	 * @param estado
	 * @param fechaPago
	 * @param xmlServicio
	 * @param resultado
	 * @param servicio
	 * @param numeroUnico
	 * @param nifOperante
	 * @param fechaAnulacion
	 * @param nombreContribuyente
	 * @param pasarelaPago
	 * @param procesado
	 * @param operacionEpst
	 * @param medioPago
	 * @param hashDatos
	 */
	public PateRecord(String numeroOperacion, 
					  String emisora, 
					  String importe,
					  String referencia, 
					  String identificacion, 
					  String justificante,
					  String nif, 
					  Date fechaDevengo, 
					  String datoEspecifico,
					  String expediente, 
					  String modalidad, 
					  Date fechaOperacion,
					  String origen, 
					  String nrc, 
					  String estado, 
					  Date fechaPago,
					  String xmlServicio,
					  String resultado, 
					  String servicio, 
					  String numeroUnico,
					  String nifOperante, 
					  Date fechaAnulacion,
					  String nombreContribuyente, 
					  String pasarelaPago, 
					  String procesado,
					  String operacionEpst, 
					  String medioPago, 
					  String hashDatos
					 ) {
		this.numeroOperacion = numeroOperacion;
		this.emisora = emisora;
		this.importe = importe;
		this.referencia = referencia;
		this.identificacion = identificacion;
		this.justificante = justificante;
		this.nif = nif;
		this.fechaDevengo = fechaDevengo;
		this.datoEspecifico = datoEspecifico;
		this.expediente = expediente;
		this.modalidad = modalidad;
		this.fechaOperacion = fechaOperacion;
		this.origen = origen;
		this.nrc = nrc;
		this.estado = estado;
		this.fechaPago = fechaPago;
		this.xmlServicio = xmlServicio;
		this.resultado = resultado;
		this.servicio = servicio;
		this.numeroUnico = numeroUnico;
		this.nifOperante = nifOperante;
		this.fechaAnulacion = fechaAnulacion;
		this.nombreContribuyente = nombreContribuyente;
		this.pasarelaPago = pasarelaPago;
		this.procesado = procesado;
		this.operacionEpst = operacionEpst;
		this.medioPago = medioPago;
		this.hashDatos = hashDatos;
	}
	/**
	 * @return the numeroOperacion
	 */
	public final String getNumeroOperacion() {
		return numeroOperacion;
	}
	/**
	 * @return the emisora
	 */
	public final String getEmisora() {
		return emisora;
	}
	/**
	 * @return the importe
	 */
	public final String getImporte() {
		return importe;
	}
	/**
	 * @return the referencia
	 */
	public final String getReferencia() {
		return referencia;
	}
	/**
	 * @return the identificacion
	 */
	public final String getIdentificacion() {
		return identificacion;
	}
	/**
	 * @return the justificante
	 */
	public final String getJustificante() {
		return justificante;
	}
	/**
	 * @return the nif
	 */
	public final String getNif() {
		return nif;
	}
	/**
	 * @return the fechaDevengo
	 */
	public final Date getFechaDevengo() {
		return fechaDevengo;
	}
	/**
	 * @return the datoEspecifico
	 */
	public final String getDatoEspecifico() {
		return datoEspecifico;
	}
	/**
	 * @return the expediente
	 */
	public final String getExpediente() {
		return expediente;
	}
	/**
	 * @return the modalidad
	 */
	public final String getModalidad() {
		return modalidad;
	}
	/**
	 * @return the fechaOperacion
	 */
	public final Date getFechaOperacion() {
		return fechaOperacion;
	}
	/**
	 * @return the origen
	 */
	public final String getOrigen() {
		return origen;
	}
	/**
	 * @return the nrc
	 */
	public final String getNrc() {
		return nrc;
	}
	/**
	 * @return the estado
	 */
	public final String getEstado() {
		return estado;
	}
	/**
	 * @return the fechaPago
	 */
	public final Date getFechaPago() {
		return fechaPago;
	}
	/**
	 * @return the resultado
	 */
	public final String getResultado() {
		return resultado;
	}
	/**
	 * @return the servicio
	 */
	public final String getServicio() {
		return servicio;
	}
	/**
	 * @return the numeroUnico
	 */
	public final String getNumeroUnico() {
		return numeroUnico;
	}
	/**
	 * @return the nifOperante
	 */
	public final String getNifOperante() {
		return nifOperante;
	}
	/**
	 * @return the fechaAnulacion
	 */
	public final Date getFechaAnulacion() {
		return fechaAnulacion;
	}
	/**
	 * @return the nombreContribuyente
	 */
	public final String getNombreContribuyente() {
		return nombreContribuyente;
	}
	/**
	 * @return the pasarelaPago
	 */
	public final String getPasarelaPago() {
		return pasarelaPago;
	}
	/**
	 * @return the procesado
	 */
	public final String getProcesado() {
		return procesado;
	}
	/**
	 * @return the operacionEpst
	 */
	public final String getOperacionEpst() {
		return operacionEpst;
	}
	/**
	 * @return the medioPago
	 */
	public final String getMedioPago() {
		return medioPago;
	}
	/**
	 * @return the hashDatos
	 */
	public final String getHashDatos() {
		return hashDatos;
	}
	public final String getToken() {
		return token;
	}
	public final void setToken(String token) {
		this.token = token;
	}
	public final void setEstado(String estado) {
		this.estado = estado;
	}
	public final void setResultado(String resultado) {
		this.resultado = resultado;
	}
	/**
	 * @param numeroOperacion the numeroOperacion to set
	 */
	public final void setNumeroOperacion(String numeroOperacion) {
		this.numeroOperacion = numeroOperacion;
	}
	/**
	 * @param nrc the nrc to set
	 */
	public final void setNrc(String nrc) {
		this.nrc = nrc;
	}
	/**
	 * @param fechaPago the fechaPago to set
	 */
	public final void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}
	/**
	 * @param fechaOperacion the fechaOperacion to set
	 */
	public final void setFechaOperacion(Date fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}
	/**
	 * @return the xmlServicio
	 */
	public final String getXmlServicio() {
		return xmlServicio;
	}
	/**
	 * @param xmlServicio the xmlServicio to set
	 */
	public final void setXmlServicio(String xmlServicio) {
		this.xmlServicio = xmlServicio;
	}
	
	
	
	
	
}
