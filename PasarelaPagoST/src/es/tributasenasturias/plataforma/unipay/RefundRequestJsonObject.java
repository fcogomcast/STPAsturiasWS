package es.tributasenasturias.plataforma.unipay;

import com.google.gson.annotations.SerializedName;

public class RefundRequestJsonObject {
	@SerializedName(value="merchantIdentifier")
	private String merchantIdentifier;
	@SerializedName(value="amount")
	private long amount;
	@SerializedName(value="paymentOperation")
	private String paymentOperation;
	@SerializedName(value="signature")
	private String signature;
	public final String getMerchantIdentifier() {
		return merchantIdentifier;
	}
	public final void setMerchantIdentifier(String merchantIdentifier) {
		this.merchantIdentifier = merchantIdentifier;
	}
	public final long getAmount() {
		return amount;
	}
	public final void setAmount(long amount) {
		this.amount = amount;
	}
	public final String getPaymentOperation() {
		return paymentOperation;
	}
	public final void setPaymentOperation(String paymentOperation) {
		this.paymentOperation = paymentOperation;
	}
	public final String getSignature() {
		return signature;
	}
	public final void setSignature(String signature) {
		this.signature = signature;
	}
	
	

}
