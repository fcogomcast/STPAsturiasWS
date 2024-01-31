package es.tributasenasturias.plataforma.unipay;



import com.google.gson.annotations.SerializedName;

public class QueryResponseJSonObject {
	@SerializedName(value="ERROR_CODE")
	private String errorCode;
	@SerializedName(value="PAYMENT_CODE")
    private String paymentCode;
	@SerializedName(value="PAYMENT_AMOUNT")
    private String paymentAmount;
	@SerializedName(value="PAYMENT_OPERATION")
    private String paymentOperation;
	@SerializedName(value="BANK_OPERATION")
    private String bankOperation;
	@SerializedName(value="PAYMENT_CHANNEL")
    private String paymentChannel;
	@SerializedName(value="PAYMENT_DATE")
    private String paymentDate;
	@SerializedName(value="AUTHORISATION_CODE")
    private String authorisationCode;
	@SerializedName(value="METADATA")
    private String metadata;
	@SerializedName(value="CARD_NUMBER")
    private String cardNumber;
	@SerializedName(value="PARAMS")
	private QueryResponseParams params;
	
	@SerializedName(value="REFUNDS")
	private Refund[] refunds;
    
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String value) { this.errorCode = value; }

    public String getPaymentCode() { return paymentCode; }
    public void setPaymentCode(String value) { this.paymentCode = value; }

    public String getPaymentAmount() { return paymentAmount; }
    public void setPaymentAmount(String value) { this.paymentAmount = value; }

    public String getPaymentOperation() { return paymentOperation; }
    public void setPaymentOperation(String value) { this.paymentOperation = value; }

    public String getBankOperation() { return bankOperation; }
    public void setBankOperation(String value) { this.bankOperation = value; }

    public String getPaymentChannel() { return paymentChannel; }
    public void setPaymentChannel(String value) { this.paymentChannel = value; }

    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String value) { this.paymentDate = value; }

    public String getAuthorisationCode() { return authorisationCode; }
    public void setAuthorisationCode(String value) { this.authorisationCode = value; }

    public String getMetadata() { return metadata; }
    public void setMetadata(String value) { this.metadata = value; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String value) { this.cardNumber = value; }
   
    public final QueryResponseParams getParams() {
		return params;
	}
	public final void setParams(QueryResponseParams params) {
		this.params = params;
	}
     
	public final Refund[] getRefunds() {
		return refunds;
	}

	public final void setRefunds(Refund[] refunds) {
		this.refunds = refunds;
	}
	
    static class QueryResponseParams {
    	@SerializedName(value="NRC")
        private String nrc;

		/**
		 * @return the nrc
		 */
		public final String getNrc() {
			return nrc;
		}

		/**
		 * @param nrc the nrc to set
		 */
		public final void setNrc(String nrc) {
			this.nrc = nrc;
		}
    	
    	
    }
    
    static class Refund {
    	@SerializedName(value="refundoperation")
    	private String refundoperation;
    	@SerializedName(value="bankoperation")
    	private String bankoperation;
    	@SerializedName(value="amount")
    	private String amount;
    	@SerializedName(value="refundcode")
    	private String refundcode;
    	@SerializedName(value="refunddescription")
    	private String refunddescription;
    	@SerializedName(value="authorizationcode")
    	private String authorizationcode;
    	@SerializedName(value="refunddate")
    	private String refunddate;
    	@SerializedName(value="confirmationdate")
    	private String confirmationdate;
		/**
		 * @return the refundoperation
		 */
		public final String getRefundoperation() {
			return refundoperation;
		}
		/**
		 * @param refundoperation the refundoperation to set
		 */
		public final void setRefundoperation(String refundoperation) {
			this.refundoperation = refundoperation;
		}
		/**
		 * @return the bankoperation
		 */
		public final String getBankoperation() {
			return bankoperation;
		}
		/**
		 * @param bankoperation the bankoperation to set
		 */
		public final void setBankoperation(String bankoperation) {
			this.bankoperation = bankoperation;
		}
		/**
		 * @return the amount
		 */
		public final String getAmount() {
			return amount;
		}
		/**
		 * @param amount the amount to set
		 */
		public final void setAmount(String amount) {
			this.amount = amount;
		}
		/**
		 * @return the refundcode
		 */
		public final String getRefundcode() {
			return refundcode;
		}
		/**
		 * @param refundcode the refundcode to set
		 */
		public final void setRefundcode(String refundcode) {
			this.refundcode = refundcode;
		}
		/**
		 * @return the refunddescription
		 */
		public final String getRefunddescription() {
			return refunddescription;
		}
		/**
		 * @param refunddescription the refunddescription to set
		 */
		public final void setRefunddescription(String refunddescription) {
			this.refunddescription = refunddescription;
		}
		/**
		 * @return the authorizationcode
		 */
		public final String getAuthorizationcode() {
			return authorizationcode;
		}
		/**
		 * @param authorizationcode the authorizationcode to set
		 */
		public final void setAuthorizationcode(String authorizationcode) {
			this.authorizationcode = authorizationcode;
		}
		/**
		 * @return the refunddate
		 */
		public final String getRefunddate() {
			return refunddate;
		}
		/**
		 * @param refunddate the refunddate to set
		 */
		public final void setRefunddate(String refunddate) {
			this.refunddate = refunddate;
		}
		/**
		 * @return the confirmationdate
		 */
		public final String getConfirmationdate() {
			return confirmationdate;
		}
		/**
		 * @param confirmationdate the confirmationdate to set
		 */
		public final void setConfirmationdate(String confirmationdate) {
			this.confirmationdate = confirmationdate;
		}
    	
    	
    	
    }

	

	
	
}