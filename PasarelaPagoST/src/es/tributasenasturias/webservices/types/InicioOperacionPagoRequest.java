package es.tributasenasturias.webservices.types;


import es.tributasenasturias.utils.Varios;


public class InicioOperacionPagoRequest {
	protected String origen; 
	protected String modalidad;
	protected String emisora;
	protected String modelo;
	protected String nifContribuyente;
	protected String fechaDevengo;
	protected String datoEspecifico;
	protected String importe;
	protected String nifOperante;
	protected String aplicacion;
	protected String numeroUnico;
	protected String mac;
	
	
	
	
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
		this.origen = Varios.null2empty(origen);
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
		this.modalidad = Varios.null2empty(modalidad);
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
		this.emisora = Varios.null2empty(emisora);
	}




	/**
	 * @return the modelo
	 */
	public final String getModelo() {
		return modelo;
	}




	/**
	 * @param modelo the modelo to set
	 */
	public final void setModelo(String modelo) {
		this.modelo = Varios.null2empty(modelo);
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
		this.nifContribuyente = Varios.null2empty(nifContribuyente);
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
		this.fechaDevengo = Varios.null2empty(fechaDevengo);
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
		this.datoEspecifico = Varios.null2empty(datoEspecifico);
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
		this.importe = Varios.null2empty(importe);
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
		this.nifOperante = Varios.null2empty(nifOperante);
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
		this.aplicacion = Varios.null2empty(aplicacion);
	}




	/**
	 * @return the numeroUnico
	 */
	public final String getNumeroUnico() {
		return numeroUnico;
	}




	/**
	 * @param numeroUnico the numeroUnico to set
	 */
	public final void setNumeroUnico(String numeroUnico) {
		this.numeroUnico = Varios.null2empty(numeroUnico);
	}




	/**
	 * @return the mac
	 */
	public final String getMac() {
		return mac;
	}




	/**
	 * @param mac the mac to set
	 */
	public final void setMac(String mac) {
		this.mac = Varios.null2empty(mac);
	}




	public InicioOperacionPagoRequest(){}




	/**
	 * @param origen
	 * @param modalidad
	 * @param emisora
	 * @param modelo
	 * @param nifContribuyente
	 * @param fechaDevengo
	 * @param datoEspecifico
	 * @param importe
	 * @param nifOperante
	 * @param aplicacion
	 * @param numeroUnico
	 * @param mac
	 */
	public InicioOperacionPagoRequest(String origen, String modalidad,
			String emisora, String modelo, String nifContribuyente,
			String fechaDevengo, String datoEspecifico, String importe,
			String nifOperante, String aplicacion, String numeroUnico,
			String mac) {
		this.origen = Varios.null2empty(origen);
		this.modalidad = Varios.null2empty(modalidad);
		this.emisora = Varios.null2empty(emisora);
		this.modelo = Varios.null2empty(modelo);
		this.nifContribuyente = Varios.null2empty(nifContribuyente);
		this.fechaDevengo = Varios.null2empty(fechaDevengo);
		this.datoEspecifico = Varios.null2empty(datoEspecifico);
		this.importe = Varios.null2empty(importe);
		this.nifOperante = Varios.null2empty(nifOperante);
		this.aplicacion = Varios.null2empty(aplicacion);
		this.numeroUnico = Varios.null2empty(numeroUnico);
		this.mac = Varios.null2empty(mac);
	}
	
	
	
}
