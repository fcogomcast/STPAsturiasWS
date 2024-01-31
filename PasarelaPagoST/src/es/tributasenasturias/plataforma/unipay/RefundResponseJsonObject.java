package es.tributasenasturias.plataforma.unipay;

import com.google.gson.annotations.SerializedName;

public class RefundResponseJsonObject {
	@SerializedName(value="errorCode")
	private String errorCode;
	@SerializedName(value="refundCode")
	private String refundCode;
	@SerializedName(value="refundOperation")
	private String refundOperation;
	@SerializedName(value="authorizationCode")
	private String authorizationCode;
	@SerializedName(value="signature")
	private String signature;
	public final String getErrorCode() {
		return errorCode;
	}
	public final void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public final String getRefundCode() {
		return refundCode;
	}
	public final void setRefundCode(String refundCode) {
		this.refundCode = refundCode;
	}
	public final String getRefundOperation() {
		return refundOperation;
	}
	public final void setRefundOperation(String refundOperation) {
		this.refundOperation = refundOperation;
	}
	public final String getAuthorizationCode() {
		return authorizationCode;
	}
	public final void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}
	public final String getSignature() {
		return signature;
	}
	public final void setSignature(String signature) {
		this.signature = signature;
	}
	
}
