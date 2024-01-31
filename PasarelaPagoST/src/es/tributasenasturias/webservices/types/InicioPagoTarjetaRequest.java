package es.tributasenasturias.webservices.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import es.tributasenasturias.utils.Varios;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "iniciaPagoTarjeta", propOrder = {
		"origen",
		"modalidad",
		"entidad",
		"emisora",
		"modelo",
		"nifContribuyente",
		"fechaDevengo",
		"datoEspecifico",
		"identificacion",
		"referencia",
		"numeroAutoliquidacion",
		"expediente",
		"importe",
		"nifOperante",
		"numeroPeticion",
		"plataformaPago"
})


@XmlRootElement(name="iniciaPagoTarjeta")
public class InicioPagoTarjetaRequest {
	@XmlElement(name="origen")
	protected String origen;
	@XmlElement(name="modalidad")
	protected String modalidad;
	@XmlElement(name="entidad")
	protected String entidad;
	@XmlElement(name="emisora")
	protected String emisora;
	@XmlElement(name="modelo")
	protected String modelo;
	@XmlElement(name="nifContribuyente")
	protected String nifContribuyente;
	@XmlElement(name="fechaDevengo")
	protected String fechaDevengo;
	@XmlElement(name="datoEspecifico")
	protected String datoEspecifico;
	@XmlElement(name="identificacion")
	protected String identificacion;
	@XmlElement(name="referencia")
	protected String referencia;
	@XmlElement(name="numeroAutoliquidacion")
	protected String numeroAutoliquidacion;
	@XmlElement(name="expediente")
	protected String expediente;
	@XmlElement(name="importe")
	protected String importe;
	@XmlElement(name="nifOperante")
	protected String nifOperante;
	@XmlElement(name="numeroPeticion")
	protected String numeroPeticion;
	@XmlElement(name="plataformaPago")
	protected String plataformaPago;
	
	
	public final String getOrigen() {
		return origen;
	}
	public final void setOrigen(String origen) {
		this.origen = origen;
	}
	public final String getModalidad() {
		return modalidad;
	}
	public final void setModalidad(String modalidad) {
		this.modalidad = modalidad;
	}
	public final String getEntidad() {
		return entidad;
	}
	public final void setEntidad(String entidad) {
		this.entidad = entidad;
	}
	public final String getEmisora() {
		return emisora;
	}
	public final void setEmisora(String emisora) {
		this.emisora = emisora;
	}
	public final String getModelo() {
		return modelo;
	}
	public final void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public final String getNifContribuyente() {
		return nifContribuyente;
	}
	public final void setNifContribuyente(String nifContribuyente) {
		this.nifContribuyente = nifContribuyente;
	}
	public final String getFechaDevengo() {
		return fechaDevengo;
	}
	public final void setFechaDevengo(String fechaDevengo) {
		this.fechaDevengo = fechaDevengo;
	}
	public final String getDatoEspecifico() {
		return datoEspecifico;
	}
	public final void setDatoEspecifico(String datoEspecifico) {
		this.datoEspecifico = datoEspecifico;
	}
	public final String getIdentificacion() {
		return identificacion;
	}
	public final void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}
	public final String getReferencia() {
		return referencia;
	}
	public final void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public final String getNumeroAutoliquidacion() {
		return numeroAutoliquidacion;
	}
	public final void setNumeroAutoliquidacion(String numeroAutoliquidacion) {
		this.numeroAutoliquidacion = numeroAutoliquidacion;
	}
	public final String getExpediente() {
		return expediente;
	}
	public final void setExpediente(String expediente) {
		this.expediente = expediente;
	}
	public final String getImporte() {
		return importe;
	}
	public final void setImporte(String importe) {
		this.importe = importe;
	}
	public final String getNifOperante() {
		return nifOperante;
	}
	public final void setNifOperante(String nifOperante) {
		this.nifOperante = nifOperante;
	}
	public final String getNumeroPeticion() {
		return numeroPeticion;
	}
	public final void setNumeroPeticion(String numeroPeticion) {
		this.numeroPeticion = numeroPeticion;
	}
	public final String getPlataformaPago() {
		return plataformaPago;
	}
	public final void setPlataformaPago(String plataformaPago) {
		this.plataformaPago = plataformaPago;
	}
	public InicioPagoTarjetaRequest(){}
	/**
	 * @param origen
	 * @param modalidad
	 * @param cliente
	 * @param entidad
	 * @param emisora
	 * @param modelo
	 * @param nif
	 * @param fecha_devengo
	 * @param dato_especifico
	 * @param identificacion
	 * @param referencia
	 * @param numero_autoliquidacion
	 * @param expediente
	 * @param importe
	 * @param nif_operante
	 * @param numero_peticion
	 * @param plataformaPago
	 */
	public InicioPagoTarjetaRequest(String origen, 
									String modalidad,
									String entidad,
									String emisora,
									String modelo,
									String nifContribuyente,
									String fechaDevengo,
									String dato_especifico, 
									String identificacion, 
									String referencia,
									String numeroAutoliquidacion, 
									String expediente, 
									String importe,
									String nifOperante, 
									String plataformaPago) {
		this.origen = Varios.null2empty(origen);
		this.modalidad = Varios.null2empty(modalidad);
		this.entidad = Varios.null2empty(entidad);
		this.emisora = Varios.null2empty(emisora);
		this.modelo = Varios.null2empty(modelo);
		this.nifContribuyente = Varios.null2empty(nifContribuyente);
		this.fechaDevengo = Varios.null2empty(fechaDevengo);
		this.datoEspecifico = Varios.null2empty(dato_especifico);
		this.identificacion = Varios.null2empty(identificacion);
		this.referencia = Varios.null2empty(referencia);
		this.numeroAutoliquidacion = Varios.null2empty(numeroAutoliquidacion);
		this.expediente = Varios.null2empty(expediente);
		this.importe = Varios.null2empty(importe);
		this.nifOperante = Varios.null2empty(nifOperante);
		this.plataformaPago = Varios.null2empty(plataformaPago);
	}
	
	
}
