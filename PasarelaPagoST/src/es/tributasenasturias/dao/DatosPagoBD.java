package es.tributasenasturias.dao;

/**
 * Modela la información de un pago de la tabla PATE.
 * @author crubencvs
 *
 */
public class DatosPagoBD {
	private String numeroOperacion;
	private String emisora;
	private String importe;
	private String referencia;
	private String identificacion;
	private String justificante;
	private String nifContribuyente;
	private String nombreContribuyente;
	private String fechaDevengo;
	private String datoEspecifico;
	private String expediente;
	private String modalidad;
	private String fechaOperacion;
	private String origen;
	private String estado;
	private String nrc;
	private String fechaPago;
	private String resultado;
	private String aplicacion;
	private String numeroUnico;
	private String nifOperante;
	private String fechaAnulacion;
	private String pasarelaPago;
	//Plataformas de pago
	private String hashDatos;
	private String operacionEpst;
	private String medioPago;
	private boolean pagoPasarelaNueva;
	/**
	 * @return the pagoPasarelaNueva
	 */
	public final boolean isPagoPasarelaNueva() {
		return pagoPasarelaNueva;
	}
	/**
	 * @param pagoPasarelaNueva the pagoPasarelaNueva to set
	 */
	public final void setPagoPasarelaNueva(boolean pagoPasarelaNueva) {
		this.pagoPasarelaNueva = pagoPasarelaNueva;
	}
	/**
	 * @return the numeOper
	 */
	public final String getNumeroOperacion() {
		return numeroOperacion;
	}
	/**
	 * @param numeOper the numeOper to set
	 */
	public final void setNumeroOperacion(String numeroOperacion) {
		this.numeroOperacion = numeroOperacion;
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
	 * @return the importe
	 */
	public final String getImporte() {
		return importe;
	}
	/**
	 * @param importe the importe to set
	 */
	public final void setImporte(String importe) {
		this.importe = importe;
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
	 * @return the justificante
	 */
	public final String getJustificante() {
		return justificante;
	}
	/**
	 * @param justificante the justificante to set
	 */
	public final void setJustificante(String justificante) {
		this.justificante = justificante;
	}
	/**
	 * @return the nifContribuyente
	 */
	public final String getNifContribuyente() {
		return nifContribuyente;
	}
	/**
	 * @param nifContribuyente the nifContribuyente to set
	 */
	public final void setNifContribuyente(String nifContribuyente) {
		this.nifContribuyente = nifContribuyente;
	}
	/**
	 * @return the nombreContribuyente
	 */
	public final String getNombreContribuyente() {
		return nombreContribuyente;
	}
	/**
	 * @param nombreContribuyente the nombreContribuyente to set
	 */
	public final void setNombreContribuyente(String nombreContribuyente) {
		this.nombreContribuyente = nombreContribuyente;
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
	 * @return the modalidad
	 */
	public final String getModalidad() {
		return modalidad;
	}
	/**
	 * @param modalidad the modalidad to set
	 */
	public final void setModalidad(String modalidad) {
		this.modalidad = modalidad;
	}
	/**
	 * @return the fechaOperacion
	 */
	public final String getFechaOperacion() {
		return fechaOperacion;
	}
	/**
	 * @param fechaOperacion the fechaOperacion to set
	 */
	public final void setFechaOperacion(String fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}
	/**
	 * @return the origen
	 */
	public final String getOrigen() {
		return origen;
	}
	/**
	 * @param origen the origen to set
	 */
	public final void setOrigen(String origen) {
		this.origen = origen;
	}
	/**
	 * @return the estado
	 */
	public final String getEstado() {
		return estado;
	}
	/**
	 * @param estado the estado to set
	 */
	public final void setEstado(String estado) {
		this.estado = estado;
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
	/**
	 * @return the fechaPago
	 */
	public final String getFechaPago() {
		return fechaPago;
	}
	/**
	 * @param fechaPago the fechaPago to set
	 */
	public final void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
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
	 * @return the numUnico
	 */
	public final String getNumeroUnico() {
		return numeroUnico;
	}
	/**
	 * @param numUnico the numUnico to set
	 */
	public final void setNumeroUnico(String numeroUnico) {
		this.numeroUnico = numeroUnico;
	}
	/**
	 * @return the nifOperante
	 */
	public final String getNifOperante() {
		return nifOperante;
	}
	/**
	 * @param nifOperante the nifOperante to set
	 */
	public final void setNifOperante(String nifOperante) {
		this.nifOperante = nifOperante;
	}
	/**
	 * @return the fechaAnulacion
	 */
	public final String getFechaAnulacion() {
		return fechaAnulacion;
	}
	/**
	 * @param fechaAnulacion the fechaAnulacion to set
	 */
	public final void setFechaAnulacion(String fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}
	/**
	 * @return the pasarelaPago
	 */
	public final String getPasarelaPago() {
		return pasarelaPago;
	}
	/**
	 * @param pasarelaPago the pasarelaPago to set
	 */
	public final void setPasarelaPago(String pasarelaPago) {
		this.pasarelaPago = pasarelaPago;
	}
	/**
	 * @return the aplicacion
	 */
	public final String getAplicacion() {
		return aplicacion;
	}
	/**
	 * @param aplicacion the aplicacion to set
	 */
	public final void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}
	public final String getHashDatos() {
		return hashDatos;
	}
	public final void setHashDatos(String hashDatos) {
		this.hashDatos = hashDatos;
	}
	public final String getOperacionEpst() {
		return operacionEpst;
	}
	public final void setOperacionEpst(String operacionEpst) {
		this.operacionEpst = operacionEpst;
	}
	public final String getMedioPago() {
		return medioPago;
	}
	public final void setMedioPago(String medioPago) {
		this.medioPago = medioPago;
	}
	
	//CRUBENCVS 31/03/2023
	// 47535
	//Necesito mantener el constructor sin parámetros por compatibilidad.
	public DatosPagoBD(){
		
	}
	//Y este, el que me interesa
	/**
	 * @param numeroOperacion
	 * @param emisora
	 * @param importe
	 * @param referencia
	 * @param identificacion
	 * @param justificante
	 * @param nifContribuyente
	 * @param nombreContribuyente
	 * @param fechaDevengo
	 * @param datoEspecifico
	 * @param expediente
	 * @param modalidad
	 * @param fechaOperacion
	 * @param origen
	 * @param estado
	 * @param nrc
	 * @param fechaPago
	 * @param resultado
	 * @param aplicacion
	 * @param numeroUnico
	 * @param nifOperante
	 * @param fechaAnulacion
	 * @param pasarelaPago
	 * @param hashDatos
	 * @param operacionEpst
	 * @param medioPago
	 * @param pagoPasarelaNueva
	 */
	public DatosPagoBD(
					   String origen,
					   String modalidad,
					   String emisora,
					   String numeroOperacion, 
					   String importe,
					   String referencia, 
					   String identificacion, 
					   String justificante,
					   String nifContribuyente, 
					   String nombreContribuyente,
					   String fechaDevengo, 
					   String datoEspecifico, 
					   String expediente,
					   String fechaOperacion, 
					   String estado, 
					   String nrc, 
					   String fechaPago, 
					   String resultado,
					   String aplicacion, 
					   String numeroUnico, 
					   String nifOperante,
					   String fechaAnulacion, 
					   String pasarelaPago, 
					   String hashDatos,
					   String operacionEpst, 
					   String medioPago, 
					   boolean pagoPasarelaNueva) {
		this.numeroOperacion = numeroOperacion;
		this.emisora = emisora;
		this.importe = importe;
		this.referencia = referencia;
		this.identificacion = identificacion;
		this.justificante = justificante;
		this.nifContribuyente = nifContribuyente;
		this.nombreContribuyente = nombreContribuyente;
		this.fechaDevengo = fechaDevengo;
		this.datoEspecifico = datoEspecifico;
		this.expediente = expediente;
		this.modalidad = modalidad;
		this.fechaOperacion = fechaOperacion;
		this.origen = origen;
		this.estado = estado;
		this.nrc = nrc;
		this.fechaPago = fechaPago;
		this.resultado = resultado;
		this.aplicacion = aplicacion;
		this.numeroUnico = numeroUnico;
		this.nifOperante = nifOperante;
		this.fechaAnulacion = fechaAnulacion;
		this.pasarelaPago = pasarelaPago;
		this.hashDatos = hashDatos;
		this.operacionEpst = operacionEpst;
		this.medioPago = medioPago;
		this.pagoPasarelaNueva = pagoPasarelaNueva;
	}
	
	
	
	
	
	
}
