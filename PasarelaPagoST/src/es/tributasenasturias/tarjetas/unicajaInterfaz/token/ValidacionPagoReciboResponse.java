package es.tributasenasturias.tarjetas.unicajaInterfaz.token;
/**
 * Respuesta de la validación de pago de Unicaja hacia las capas superiores
 * @author crubencvs
 *
 */
public class ValidacionPagoReciboResponse {

	private final boolean error;
	private final String numPedido;
	private final String merchantId;
	private final String idTerminal;
	private final String adquirerBin;
	private final String importe;
	private final String moneda;
	private final String exponente;
	private final String cifrado;
	private final String pagoSoportado;
	private final String exencionSCA;
	private final String urlTpvOk;
	private final String urlTpvKO;
	private final String firma;
	private final String urlTpvCeca;
	private final String httpCode;
	private final String errorCode;
	private final String moreInformation;
	private final String httpMessage;
	
	
	public ValidacionPagoReciboResponse(ValidacionPagoReciboJSonResponse jsonResponse){
		this.numPedido= jsonResponse.getNumPedido();
		this.merchantId = jsonResponse.getMerchantId();
		this.idTerminal= jsonResponse.getIdTerminal();
		this.adquirerBin= jsonResponse.getAdquirerBin();
		this.importe= jsonResponse.getImporte();
		this.moneda= jsonResponse.getMoneda();
		this.exponente= jsonResponse.getExponente();
		this.cifrado= jsonResponse.getCifrado();
		this.pagoSoportado= jsonResponse.getPagoSoportado();
		this.exencionSCA= jsonResponse.getExencionSCA();
		this.urlTpvOk = jsonResponse.getUrlTpvOk();
		this.urlTpvKO= jsonResponse.getUrlTpvKO();
		this.firma= jsonResponse.getFirma();
		this.urlTpvCeca= jsonResponse.getUrlTpvCeca();
		this.httpCode= jsonResponse.getHttpCode();
		this.errorCode= jsonResponse.getErrorCode();
		this.moreInformation= jsonResponse.getMoreInformation();
		this.httpMessage= jsonResponse.getHttpMessage();
		
		this.error= (this.httpCode!=null || !"".equals(this.httpCode));
		 
		
	}
	
	/**
	 * @return the numPedido
	 */
	public final String getNumPedido() {
		return numPedido;
	}
	
	/**
	 * @return the merchantId
	 */
	public final String getMerchantId() {
		return merchantId;
	}
	
	/**
	 * @return the idTerminal
	 */
	public final String getIdTerminal() {
		return idTerminal;
	}
	
	public final String getAdquirerBin() {
		return adquirerBin;
	}

	/**
	 * @return the importe
	 */
	public final String getImporte() {
		return importe;
	}
	
	/**
	 * @return the moneda
	 */
	public final String getMoneda() {
		return moneda;
	}
	
	/**
	 * @return the exponente
	 */
	public final String getExponente() {
		return exponente;
	}
	
	/**
	 * @return the cifrado
	 */
	public final String getCifrado() {
		return cifrado;
	}
	
	/**
	 * @return the pagoSoportado
	 */
	public final String getPagoSoportado() {
		return pagoSoportado;
	}
	
	/**
	 * @return the exencionSCA
	 */
	public final String getExencionSCA() {
		return exencionSCA;
	}
	
	/**
	 * @return the urlTpvOk
	 */
	public final String getUrlTpvOk() {
		return urlTpvOk;
	}
	
	/**
	 * @return the urlTpvKO
	 */
	public final String getUrlTpvKO() {
		return urlTpvKO;
	}
	
	/**
	 * @return the firma
	 */
	public final String getFirma() {
		return firma;
	}
	
	/**
	 * @return the urlTpvCeca
	 */
	public final String getUrlTpvCeca() {
		return urlTpvCeca;
	}
	
	/**
	 * @return the httpCode
	 */
	public final String getHttpCode() {
		return httpCode;
	}
	
	/**
	 * @return the errorCode
	 */
	public final String getErrorCode() {
		return errorCode;
	}
	
	/**
	 * @return the moreInformation
	 */
	public final String getMoreInformation() {
		return moreInformation;
	}
	
	/**
	 * @return the httpMessage
	 */
	public final String getHttpMessage() {
		return httpMessage;
	}

	/**
	 * @return the error
	 */
	public final boolean isError() {
		return error;
	}
	
	
	
}
