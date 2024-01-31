package es.tributasenasturias.webservices.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "unicaja_response", propOrder = {
    "numPedido",
    "merchantId",
    "idTerminal",
    "adquirerBin",
    "importe",
    "moneda",
    "exponente",
    "cifrado",
    "pagoSoportado",
    "exencionSCA",
    "urlTpvOk",
    "urlTpvKO",
    "firma",
    "urlTpvCeca",
    "httpCode",
    "errorCode",
    "moreInformation",
    "httpMessage"
})
public class UnicajaValidacionPagoResponse {

	@XmlElement(name="numPedido",required = true)
	protected String numPedido;
	@XmlElement(name="merchantId",required = true)
	protected String merchantId;
	@XmlElement(name="idTerminal",required = true)
	protected String idTerminal;
	@XmlElement(name="adquirerBin",required = true)
	protected String adquirerBin;
	@XmlElement(name="importe",required = true)
	protected String importe;
	@XmlElement(name="moneda",required = true)
	protected String moneda;
	@XmlElement(name="exponente",required = true)
	protected String exponente;
	@XmlElement(name="cifrado",required = true)
	protected String cifrado;
	@XmlElement(name="pagoSoportado",required = true)
	protected String pagoSoportado;
	@XmlElement(name="exencionSCA",required = true)
	protected String exencionSCA;
	@XmlElement(name="urlTpvOk",required = true)
	protected String urlTpvOk;
	@XmlElement(name="urlTpvKO",required = true)
	protected String urlTpvKO;
	@XmlElement(name="firma",required = true)
	protected String firma;
	@XmlElement(name="urlTpvCeca",required = true)
	protected String urlTpvCeca;
	@XmlElement(name="httpCode",required = true)
	protected String httpCode;
	@XmlElement(name="errorCode",required = true)
	protected String errorCode;
	@XmlElement(name="moreInformation",required = true)
	protected String moreInformation;
	@XmlElement(name="httpMessage",required = true)
	protected String httpMessage;
	/**
	 * @return the numPedido
	 */
	public final String getNumPedido() {
		return numPedido;
	}
	/**
	 * @param numPedido the numPedido to set
	 */
	public final void setNumPedido(String numPedido) {
		this.numPedido = numPedido;
	}
	/**
	 * @return the merchantId
	 */
	public final String getMerchantId() {
		return merchantId;
	}
	/**
	 * @param merchantId the merchantId to set
	 */
	public final void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	/**
	 * @return the idTerminal
	 */
	public final String getIdTerminal() {
		return idTerminal;
	}
	/**
	 * @param idTerminal the idTerminal to set
	 */
	public final void setIdTerminal(String idTerminal) {
		this.idTerminal = idTerminal;
	}
	
	
	/**
	 * @return the adquirerBin
	 */
	public final String getAdquirerBin() {
		return adquirerBin;
	}
	/**
	 * @param adquirerBin the adquirerBin to set
	 */
	public final void setAdquirerBin(String adquirerBin) {
		this.adquirerBin = adquirerBin;
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
	 * @return the moneda
	 */
	public final String getMoneda() {
		return moneda;
	}
	/**
	 * @param moneda the moneda to set
	 */
	public final void setMoneda(String moneda) {
		this.moneda = moneda;
	}
	/**
	 * @return the exponente
	 */
	public final String getExponente() {
		return exponente;
	}
	/**
	 * @param exponente the exponente to set
	 */
	public final void setExponente(String exponente) {
		this.exponente = exponente;
	}
	/**
	 * @return the cifrado
	 */
	public final String getCifrado() {
		return cifrado;
	}
	/**
	 * @param cifrado the cifrado to set
	 */
	public final void setCifrado(String cifrado) {
		this.cifrado = cifrado;
	}
	/**
	 * @return the pagoSoportado
	 */
	public final String getPagoSoportado() {
		return pagoSoportado;
	}
	/**
	 * @param pagoSoportado the pagoSoportado to set
	 */
	public final void setPagoSoportado(String pagoSoportado) {
		this.pagoSoportado = pagoSoportado;
	}
	/**
	 * @return the exencionSCA
	 */
	public final String getExencionSCA() {
		return exencionSCA;
	}
	/**
	 * @param exencionSCA the exencionSCA to set
	 */
	public final void setExencionSCA(String exencionSCA) {
		this.exencionSCA = exencionSCA;
	}
	/**
	 * @return the urlTpvOk
	 */
	public final String getUrlTpvOk() {
		return urlTpvOk;
	}
	/**
	 * @param urlTpvOk the urlTpvOk to set
	 */
	public final void setUrlTpvOk(String urlTpvOk) {
		this.urlTpvOk = urlTpvOk;
	}
	/**
	 * @return the urlTpvKO
	 */
	public final String getUrlTpvKO() {
		return urlTpvKO;
	}
	/**
	 * @param urlTpvKO the urlTpvKO to set
	 */
	public final void setUrlTpvKO(String urlTpvKO) {
		this.urlTpvKO = urlTpvKO;
	}
	/**
	 * @return the firma
	 */
	public final String getFirma() {
		return firma;
	}
	/**
	 * @param firma the firma to set
	 */
	public final void setFirma(String firma) {
		this.firma = firma;
	}
	/**
	 * @return the urlTpvCeca
	 */
	public final String getUrlTpvCeca() {
		return urlTpvCeca;
	}
	/**
	 * @param urlTpvCeca the urlTpvCeca to set
	 */
	public final void setUrlTpvCeca(String urlTpvCeca) {
		this.urlTpvCeca = urlTpvCeca;
	}
	/**
	 * @return the httpCode
	 */
	public final String getHttpCode() {
		return httpCode;
	}
	/**
	 * @param httpCode the httpCode to set
	 */
	public final void setHttpCode(String httpCode) {
		this.httpCode = httpCode;
	}
	/**
	 * @return the errorCode
	 */
	public final String getErrorCode() {
		return errorCode;
	}
	/**
	 * @param errorCode the errorCode to set
	 */
	public final void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * @return the moreInformation
	 */
	public final String getMoreInformation() {
		return moreInformation;
	}
	/**
	 * @param moreInformation the moreInformation to set
	 */
	public final void setMoreInformation(String moreInformation) {
		this.moreInformation = moreInformation;
	}
	/**
	 * @return the httpMessage
	 */
	public final String getHttpMessage() {
		return httpMessage;
	}
	/**
	 * @param httpMessage the httpMessage to set
	 */
	public final void setHttpMessage(String httpMessage) {
		this.httpMessage = httpMessage;
	}

	
	
}
