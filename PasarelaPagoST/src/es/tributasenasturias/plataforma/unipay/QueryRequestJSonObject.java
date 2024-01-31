package es.tributasenasturias.plataforma.unipay;

import com.google.gson.annotations.SerializedName;

public class QueryRequestJSonObject {
	@SerializedName(value="MERCHANT_IDENTIFIER")
	private String merchantIdentifier;
	@SerializedName(value="MERCHANT_OPERATION")
	private String merchantOperation;
	@SerializedName(value="SIGNATURE")
	private String signature;
	public final String getMerchantIdentifier() {
		return merchantIdentifier;
	}
	public final void setMerchantIdentifier(String merchantIdentifier) {
		this.merchantIdentifier = merchantIdentifier;
	}
	public final String getMerchantOperation() {
		return merchantOperation;
	}
	public final void setMerchantOperation(String merchantOperation) {
		this.merchantOperation = merchantOperation;
	}
	public final String getSignature() {
		return signature;
	}
	public final void setSignature(String signature) {
		this.signature = signature;
	}
	
}
