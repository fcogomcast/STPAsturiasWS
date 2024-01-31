package es.tributasenasturias.webservices.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import es.tributasenasturias.utils.Varios;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "anulacionPagoTarjeta", propOrder = {
		"origen",
		"modalidad",
		"cliente",
		"emisora",
		"identificacion",
		"referencia",
		"numeroAutoliquidacion",
		"aplicacion",
		"numeroUnico",
		"mac"
		
})


@XmlRootElement(name="anulacionPagoTarjeta")
public class AnulacionPagoTarjetaRequest {
	@XmlElement(name="origen")
	protected String origen;
	@XmlElement(name="modalidad")
	protected String modalidad;
	@XmlElement(name="cliente")
	protected String cliente;
	@XmlElement(name="emisora")
	protected String emisora;
	@XmlElement(name="identificacion")
	protected String identificacion;
	@XmlElement(name="referencia")
	protected String referencia;
	@XmlElement(name="numero_autoliquidacion")
	protected String numeroAutoliquidacion;
	@XmlElement(name="aplicacion")
	protected String aplicacion;
	@XmlElement(name="numero_unico")
	protected String numeroUnico;
	@XmlElement(name="mac")
	protected String mac;
	
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
	public final String getEmisora() {
		return emisora;
	}
	public final void setEmisora(String emisora) {
		this.emisora = emisora;
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
	
	public final String getCliente() {
		return cliente;
	}
	public final void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public final String getAplicacion() {
		return aplicacion;
	}
	public final void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}
	public final String getNumeroUnico() {
		return numeroUnico;
	}
	public final void setNumeroUnico(String numeroUnico) {
		this.numeroUnico = numeroUnico;
	}
	public final String getMac() {
		return mac;
	}
	public final void setMac(String mac) {
		this.mac = mac;
	}
	public AnulacionPagoTarjetaRequest(){}
	/**
	 * @param origen
	 * @param modalidad
	 * @param cliente
	 * @param emisora
	 * @param identificacion
	 * @param referencia
	 * @param numeroAutoliquidacion
	 * @param aplicacion
	 * @param numeroUnico
	 * @param mac
	 */
	public AnulacionPagoTarjetaRequest(String origen, String modalidad,
			String cliente, String emisora, String identificacion,
			String referencia, String numeroAutoliquidacion, String aplicacion,
			String numeroUnico, String mac) {
		this.origen = Varios.null2empty(origen);
		this.modalidad = Varios.null2empty(modalidad);
		this.cliente = Varios.null2empty(cliente);
		this.emisora = Varios.null2empty(emisora);
		this.identificacion = Varios.null2empty(identificacion);
		this.referencia = Varios.null2empty(referencia);
		this.numeroAutoliquidacion = Varios.null2empty(numeroAutoliquidacion);
		this.aplicacion = Varios.null2empty(aplicacion);
		this.numeroUnico = Varios.null2empty(numeroUnico);
		this.mac = Varios.null2empty(mac);
	}
	
	
	
	
}
