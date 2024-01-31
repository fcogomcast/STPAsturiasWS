package es.tributasenasturias.webservices.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "universalpay_response", propOrder = {
    "code",
    "description",
    "token",
    "operation"
})
public class UniversalPayTokenRequest_ResponseObject {
	@XmlElement(name="code",required = true)
	protected String code;
	@XmlElement(name="description",required = true)
	protected String description;
	@XmlElement(name="token",required = true)
	protected String token;
	@XmlElement(name="operation", required=true)
	protected String operation;
	/**
	 * @return the code
	 */
	public final String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public final void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the description
	 */
	public final String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public final void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the token
	 */
	public final String getToken() {
		return token;
	}
	/**
	 * @param token the token to set
	 */
	public final void setToken(String token) {
		this.token = token;
	}
	public final String getOperation() {
		return operation;
	}
	public final void setOperation(String operation) {
		this.operation = operation;
	}
	
}
