package es.tributasenasturias.plataforma.unipay;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.annotations.SerializedName;

public class PaymentResponseJsonObject {
	
		public static class Metadatos{
			private String emisora="";
			private String justificante="";
			private String identificacion="";
			private String referencia="";
			private String fechaDevengo="";
			private String nifOperante="";
			private String nifContribuyente="";
			
			
			public Metadatos(String metadata){
				if (metadata==null || metadata.equals("")){
					return;
				}
				Pattern patternJustificante= Pattern.compile("JUSTIFICANTE:(\\d+);");
				Pattern patternEmisora= Pattern.compile("EMISORA:(\\d+);");
				Pattern patternFecDevengo= Pattern.compile("FEC_DEVENGO:(.+?);");
				Pattern patternNifOperante= Pattern.compile("NIF_OPERANTE:(.+?);");
				Pattern patternNifContribuyente= Pattern.compile("COD_IDENTIF:(.+?);");
				Pattern patternIdentificacion= Pattern.compile("IDENTIFICATIVO:(\\d+);");
				Pattern patternReferencia= Pattern.compile("REFERENCIA:(\\d+);");
				
				Matcher m= patternJustificante.matcher(metadata);
				if (m.find()){
					if (m.groupCount()>0){
						justificante= m.group(1);
					}
				}
				
				m= patternEmisora.matcher(metadata);
				if (m.find()){
					if (m.groupCount()>0){
						emisora= m.group(1);
					}
				}
				
				m= patternFecDevengo.matcher(metadata);
				if (m.find()){
					if (m.groupCount()>0){
						fechaDevengo= m.group(1);
					}
				}
				
				m= patternIdentificacion.matcher(metadata);
				if (m.find()){
					if (m.groupCount()>0){
						identificacion= m.group(1);
					}
				}
				
				m= patternReferencia.matcher(metadata);
				if (m.find()){
					if (m.groupCount()>0){
						referencia= m.group(1);
					}
				}
				
				m= patternNifOperante.matcher(metadata);
				if (m.find()){
					if (m.groupCount()>0){
						nifOperante= m.group(1);
					}
				}
				
				m= patternNifContribuyente.matcher(metadata);
				if (m.find()){
					if (m.groupCount()>0){
						nifContribuyente= m.group(1);
					}
				}
			}
			/**
			 * @return the emisora
			 */
			public final String getEmisora() {
				return emisora;
			}
			
			/**
			 * @return the justificante
			 */
			public final String getJustificante() {
				return justificante;
			}
			
			/**
			 * @return the identificacion
			 */
			public final String getIdentificacion() {
				return identificacion;
			}
			
			/**
			 * @return the referencia
			 */
			public final String getReferencia() {
				return referencia;
			}
			
			/**
			 * @return the fechaDevengo
			 */
			public final String getFechaDevengo() {
				return fechaDevengo;
			}
			
			/**
			 * @return the nifOperante
			 */
			public final String getNifOperante() {
				return nifOperante;
			}
			
			/**
			 * @return the nifContribuyente
			 */
			public final String getNifContribuyente() {
				return nifContribuyente;
			}
			
			
		}
		@SerializedName(value="MERCHANT_IDENTIFIER")
	 	private String merchantIdentifier;
	    @SerializedName(value="PAYMENT_AMOUNT")
	    private String paymentAmount;
	    @SerializedName(value="MERCHANT_OPERATION")
	    private String merchantOperation;
	    @SerializedName(value="PAYMENT_OPERATION")
	    private String paymentOperation;
	    @SerializedName(value="PAYMENT_CHANNEL")
	    private String paymentChannel;
	    @SerializedName(value="PAYMENT_DATE")
	    private String paymentDate;
	    @SerializedName(value="PAYMENT_CODE")
	    private String paymentCode;
	    @SerializedName(value="SIGNATURE")
	    private String signature;
	    @SerializedName(value="CURRENCY")
	    private String currency;
	    @SerializedName(value="PARAMS")
	    private PaymentResponseParamsJsonObject params;

	    public String getMerchantIdentifier() { return merchantIdentifier; }
	    public void setMerchantIdentifier(String value) { this.merchantIdentifier = value; }

	    public String getPaymentAmount() { return paymentAmount; }
	    public void setPaymentAmount(String value) { this.paymentAmount = value; }

	    public String getMerchantOperation() { return merchantOperation; }
	    public void setMerchantOperation(String value) { this.merchantOperation = value; }

	    public String getPaymentOperation() { return paymentOperation; }
	    public void setPaymentOperation(String value) { this.paymentOperation = value; }

	    public String getPaymentChannel() { return paymentChannel; }
	    public void setPaymentChannel(String value) { this.paymentChannel = value; }

	    public String getPaymentDate() { return paymentDate; }
	    public void setPaymentDate(String value) { this.paymentDate = value; }

	    public String getPaymentCode() { return paymentCode; }
	    public void setPaymentCode(String value) { this.paymentCode = value; }

	    public String getSignature() { return signature; }
	    public void setSignature(String value) { this.signature = value; }

	    public String getCurrency() { return currency; }
	    public void setCurrency(String value) { this.currency = value; }

	    public PaymentResponseParamsJsonObject getParams() { return params; }
	    public void setParams(PaymentResponseParamsJsonObject value) { this.params = value; }
	    
	    public Metadatos getMetadatos(){
	    	if (this.params==null){
	    		return new Metadatos(""); //Siempre devolvemos un objeto, no queremos comprobar NULL
	    	}
	    	return new Metadatos(this.params.getMetadata()); 
	    }
}


class PaymentResponseParamsJsonObject{
	@SerializedName( value="CARD_COUNTRY")
	private String cardCountry;
	@SerializedName( value="BANK_OPERATION")
    private String bankOperation;
	@SerializedName( value="PAYMENT_SECURE_REASON")
    private String paymentSecureReason;
	@SerializedName( value="RECURRENT")
    private String recurrent;
	@SerializedName( value="CARD_HOLDER")
    private String cardholder;
	@SerializedName( value="PAYMENT_SECURE")
    private String paymentSecure;
	@SerializedName( value="AUTHORISATION_CODE")
    private String authorisationCode;
	@SerializedName( value="CARD_TOKEN")
    private String cardToken;
	@SerializedName( value="CARD_TYPE")
    private String cardType;
	@SerializedName( value="METADATA")
    private String metadata;
	@SerializedName( value="EXPIRY_DATE")
    private String expiryDate;
	@SerializedName( value="CARD_NUMBER")
    private String cardNumber;
	@SerializedName( value="NRC")
    private String nrc;

    public String getCardCountry() { return cardCountry; }
    public void setCardCountry(String value) { this.cardCountry = value; }

    public String getBankOperation() { return bankOperation; }
    public void setBankOperation(String value) { this.bankOperation = value; }

    public String getPaymentSecureReason() { return paymentSecureReason; }
    public void setPaymentSecureReason(String value) { this.paymentSecureReason = value; }

    public String getRecurrent() { return recurrent; }
    public void setRecurrent(String value) { this.recurrent = value; }

    public String getCardholder() { return cardholder; }
    public void setCardholder(String value) { this.cardholder = value; }

    public String getPaymentSecure() { return paymentSecure; }
    public void setPaymentSecure(String value) { this.paymentSecure = value; }

    public String getAuthorisationCode() { return authorisationCode; }
    public void setAuthorisationCode(String value) { this.authorisationCode = value; }

    public String getCardToken() { return cardToken; }
    public void setCardToken(String value) { this.cardToken = value; }

    public String getCardType() { return cardType; }
    public void setCardType(String value) { this.cardType = value; }

    public String getMetadata() { return metadata; }
    public void setMetadata(String value) { this.metadata = value; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String value) { this.expiryDate = value; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String value) { this.cardNumber = value; }
    
    public String getNrc() { return nrc; }
    public void setNrc(String value) { this.nrc = value; }
    
}
