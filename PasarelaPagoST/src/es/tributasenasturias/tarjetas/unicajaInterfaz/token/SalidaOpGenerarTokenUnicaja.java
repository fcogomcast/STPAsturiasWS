package es.tributasenasturias.tarjetas.unicajaInterfaz.token;

public class SalidaOpGenerarTokenUnicaja {
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
	private final String jsonResponse;
	
	private boolean error;

	/**
	 * @param numPedido
	 * @param merchantId
	 * @param idTerminal
	 * @param adquirerBin
	 * @param importe
	 * @param moneda
	 * @param exponente
	 * @param cifrado
	 * @param pagoSoportado
	 * @param exencionSCA
	 * @param urlTpvOk
	 * @param urlTpvKO
	 * @param firma
	 * @param urlTpvCeca
	 * @param httpCode
	 * @param errorCode
	 * @param moreInformation
	 * @param httpMessage
	 */
	public SalidaOpGenerarTokenUnicaja(String numPedido, 
							  String merchantId,
							  String idTerminal, 
							  String adquirerBin,
							  String importe, 
							  String moneda,
							  String exponente,
							  String cifrado, 
							  String pagoSoportado, 
							  String exencionSCA,
							  String urlTpvOk, 
							  String urlTpvKO, 
							  String firma, 
							  String urlTpvCeca,
							  String httpCode, 
							  String errorCode, 
							  String moreInformation,
							  String httpMessage,
							  String jsonResponse) {
		this.numPedido = numPedido;
		this.merchantId = merchantId;
		this.idTerminal = idTerminal;
		this.importe = importe;
		this.moneda = moneda;
		this.adquirerBin= adquirerBin;
		this.exponente = exponente;
		this.cifrado = cifrado;
		this.pagoSoportado = pagoSoportado;
		this.exencionSCA = exencionSCA;
		this.urlTpvOk = urlTpvOk;
		this.urlTpvKO = urlTpvKO;
		this.firma = firma;
		this.urlTpvCeca = urlTpvCeca;
		this.httpCode = httpCode;
		this.errorCode = errorCode;
		this.moreInformation = moreInformation;
		this.httpMessage = httpMessage;
		this.jsonResponse = jsonResponse;
		if (errorCode != null && !"".equals(errorCode.trim())){
			this.error= true;
		} else {
			this.error= false;
		}
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

	
	/**
	 * @return the adquirerBin
	 */
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


	/**
	 * @return the jsonResponse
	 */
	public final String getJsonResponse() {
		return jsonResponse;
	}
	
	
	
}
