package es.tributasenasturias.plataforma.unipay;

import com.google.gson.annotations.SerializedName;

public class TokenRequestJSonObject {
	@SerializedName(value="CURRENCY")
	private String currency;
	@SerializedName(value="LOCALE")
    private String locale;
	@SerializedName(value="MERCHANT_IDENTIFIER")
    private String merchantIdentifier;
	@SerializedName(value="AMOUNT")
    private long amount;
	@SerializedName(value="OPERATION")
    private String operation;
	@SerializedName(value="DESCRIPTION")
    private String description;
	@SerializedName(value="URL_KO")
    private String urlKo;
	@SerializedName(value="PARAMS")
    private Params params;
	@SerializedName(value="SIGNATURE")
    private String signature;
	@SerializedName(value="URL_RESPONSE")
    private String urlResponse;
	@SerializedName(value="URL_OK")
    private String urlOk;

    public String getCurrency() { return currency; }
    public void setCurrency(String value) { this.currency = value; }

    public String getLocale() { return locale; }
    public void setLocale(String value) { this.locale = value; }

    public String getMerchantIdentifier() { return merchantIdentifier; }
    public void setMerchantIdentifier(String value) { this.merchantIdentifier = value; }

    public long getAmount() { return amount; }
    public void setAmount(long value) { this.amount = value; }

    
	public String getOperation() { return operation; }
    public void setOperation(String value) { this.operation = value; }

    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }

    public String getURLKo() { return urlKo; }
    public void setURLKo(String value) { this.urlKo = value; }

    public Params getParams() { return params; }
    public void setParams(Params value) { this.params = value; }

    public String getSignature() { return signature; }
    public void setSignature(String value) { this.signature = value; }

    public String getURLResponse() { return urlResponse; }
    public void setURLResponse(String value) { this.urlResponse = value; }

    public String getURLOk() { return urlOk; }
    public void setURLOk(String value) { this.urlOk = value; }
}

class Params {
	@SerializedName(value="PAYMENT_OPERATION")
    private String paymentOperation;
	@SerializedName(value="PAYMENT_CHANNEL")
    private String paymentChannel;
	@SerializedName(value="METADATA")
    private String metadata;
	@SerializedName(value="STYLE_COLOR_LABEL")
	private String styleColorLabel;
	@SerializedName(value="STYLE_BACK_BOTON")
	private String styleBackBoton;
	@SerializedName(value="STYLE_BACK_SECOND_BOTON")
	private String styleBackSecondBoton;
	@SerializedName(value="STYLE_BACK_FRAME")
	private String styleBackFrame;
	@SerializedName(value="STYLE_COLOR_SECOND_BOTON")
	private String styleColorSecondBoton;
	@SerializedName(value="STYLE_COLOR_BOTON")
	private String styleColorBoton;
	@SerializedName(value="STYLE_COLOR_BORDE_SECOND_BOTON")
	private String styleColorBordeSecondBoton;
	@SerializedName(value="STYLE_COLOR_BORDE_BOTON")
	private String styleColorBordeBoton;
	//14/09/2021. Si se pone AMOUNT_MIN y AMOUNT_MAX, 
	// la plataforma no permite modificar la cantidad a pagar
	@SerializedName(value="AMOUNT_MIN")
	private long amount_min;
	@SerializedName(value="AMOUNT_MAX")
	private long amount_max;

    public String getPaymentOperation() { return paymentOperation; }
    public void setPaymentOperation(String value) { this.paymentOperation = value; }

    public String getPaymentChannel() { return paymentChannel; }
    public void setPaymentChannel(String value) { this.paymentChannel = value; }

    public String getMetadata() { return metadata; }
    public void setMetadata(String value) { this.metadata = value; }
	/**
	 * @return the styleColorLabel
	 */
	public final String getStyleColorLabel() {
		return styleColorLabel;
	}
	/**
	 * @param styleColorLabel the styleColorLabel to set
	 */
	public final void setStyleColorLabel(String styleColorLabel) {
		this.styleColorLabel = styleColorLabel;
	}
	/**
	 * @return the styleBackBoton
	 */
	public final String getStyleBackBoton() {
		return styleBackBoton;
	}
	/**
	 * @param styleBackBoton the styleBackBoton to set
	 */
	public final void setStyleBackBoton(String styleBackBoton) {
		this.styleBackBoton = styleBackBoton;
	}
	/**
	 * @return the styleBackSecondBoton
	 */
	public final String getStyleBackSecondBoton() {
		return styleBackSecondBoton;
	}
	/**
	 * @param styleBackSecondBoton the styleBackSecondBoton to set
	 */
	public final void setStyleBackSecondBoton(String styleBackSecondBoton) {
		this.styleBackSecondBoton = styleBackSecondBoton;
	}
	/**
	 * @return the styleBackFrame
	 */
	public final String getStyleBackFrame() {
		return styleBackFrame;
	}
	/**
	 * @param styleBackFrame the styleBackFrame to set
	 */
	public final void setStyleBackFrame(String styleBackFrame) {
		this.styleBackFrame = styleBackFrame;
	}
	/**
	 * @return the styleColorSecondBoton
	 */
	public final String getStyleColorSecondBoton() {
		return styleColorSecondBoton;
	}
	/**
	 * @param styleColorSecondBoton the styleColorSecondBoton to set
	 */
	public final void setStyleColorSecondBoton(String styleColorSecondBoton) {
		this.styleColorSecondBoton = styleColorSecondBoton;
	}
	/**
	 * @return the styleColorBoton
	 */
	public final String getStyleColorBoton() {
		return styleColorBoton;
	}
	/**
	 * @param styleColorBoton the styleColorBoton to set
	 */
	public final void setStyleColorBoton(String styleColorBoton) {
		this.styleColorBoton = styleColorBoton;
	}
	/**
	 * @return the styleColorBordeSecondBoton
	 */
	public final String getStyleColorBordeSecondBoton() {
		return styleColorBordeSecondBoton;
	}
	/**
	 * @param styleColorBordeSecondBoton the styleColorBordeSecondBoton to set
	 */
	public final void setStyleColorBordeSecondBoton(
			String styleColorBordeSecondBoton) {
		this.styleColorBordeSecondBoton = styleColorBordeSecondBoton;
	}
	/**
	 * @return the styleColorBordeBoton
	 */
	public final String getStyleColorBordeBoton() {
		return styleColorBordeBoton;
	}
	/**
	 * @param styleColorBordeBoton the styleColorBordeBoton to set
	 */
	public final void setStyleColorBordeBoton(String styleColorBordeBoton) {
		this.styleColorBordeBoton = styleColorBordeBoton;
	}
	
	/**
	 * @return the amount_min
	 */
	public final long getAmount_min() {
		return amount_min;
	}
	/**
	 * @param amount_min the amount_min to set
	 */
	public final void setAmount_min(long amount_min) {
		this.amount_min = amount_min;
	}
	/**
	 * @return the amount_max
	 */
	public final long getAmount_max() {
		return amount_max;
	}
	/**
	 * @param amount_max the amount_max to set
	 */
	public final void setAmount_max(long amount_max) {
		this.amount_max = amount_max;
	}
    
}