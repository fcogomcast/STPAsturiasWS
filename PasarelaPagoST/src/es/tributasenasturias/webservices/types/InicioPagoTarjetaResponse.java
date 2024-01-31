package es.tributasenasturias.webservices.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "inicioPagoTarjetaResponse", propOrder = {
    "esError",
    "codigo",
    "mensaje",
    "plataformaPago",
    "universalpayResponse",
    "unicajaResponse"
})
public class InicioPagoTarjetaResponse {
	@XmlElement(name="esError",required = true)
    protected boolean esError;
	@XmlElement(name="codigo",required = true)
	protected String codigo;
    @XmlElement(name="mensaje",required = true)
    protected String mensaje;
    @XmlElement(name="plataformaPago",required = true)
    protected String plataformaPago;
    @XmlElement(name="universalpayResponse",required = false)
    protected UniversalPayTokenRequest_ResponseObject universalpayResponse;
    @XmlElement(name="unicajaResponse",required = false)
    protected UnicajaValidacionPagoResponse unicajaResponse;
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
	/**
	 * @return the universalpayResponse
	 */
	public final UniversalPayTokenRequest_ResponseObject getUniversalpayResponse() {
		return universalpayResponse;
	}
	/**
	 * @param universalpayResponse the universalpayResponse to set
	 */
	public final void setUniversalpayResponse(
			UniversalPayTokenRequest_ResponseObject universalpayResponse) {
		this.universalpayResponse = universalpayResponse;
	}
	
	
	public final UnicajaValidacionPagoResponse getUnicajaResponse() {
		return unicajaResponse;
	}
	public final void setUnicajaResponse(
			UnicajaValidacionPagoResponse unicajaResponse) {
		this.unicajaResponse = unicajaResponse;
	}
	public final String getCodigo() {
		return codigo;
	}
	public final void setCodigo(String codigo) {
		this.codigo = codigo;
	} 
    
	
}
