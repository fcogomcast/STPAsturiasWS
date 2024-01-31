package es.tributasenasturias.webservices.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "anulacionPagoTarjetaResponse", propOrder = {
    "esError",
    "codigo",
    "mensaje",
    "plataformaPago",
    "mac"
})
public class AnulacionPagoTarjetaResponse {
	@XmlElement(name="esError",required = true)
    protected boolean esError;
	@XmlElement(name="codigo",required = true)
	protected String codigo;
    @XmlElement(name="mensaje",required = true)
    protected String mensaje;
    @XmlElement(name="plataformaPago",required = true)
    protected String plataformaPago;
    @XmlElement(name="mac", required=false)
    protected String mac;
    
	/**
	 * @return the esError
	 */
	public final boolean isEsError() {
		return esError;
	}
	/**
	 * @param esError the esError to set
	 */
	public final void setEsError(boolean esError) {
		this.esError = esError;
	}
	/**
	 * @return the mensaje
	 */
	public final String getMensaje() {
		return mensaje;
	}
	/**
	 * @param mensaje the mensaje to set
	 */
	public final void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	/**
	 * @return the pasarela
	 */
	public final String getPlataformaPago() {
		return plataformaPago;
	}
	/**
	 * @param pasarela the pasarela to set
	 */
	public final void setPlataformaPago(String plataformaPago) {
		this.plataformaPago = plataformaPago;
	}
	public final String getCodigo() {
		return codigo;
	}
	public final void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public final String getMac() {
		return mac;
	}
	public final void setMac(String mac) {
		this.mac = mac;
	} 
    
	
}
