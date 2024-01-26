
package es.tributasenasturias.seguridad.firma;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Firmar complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Firmar">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="xmlData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="identificadorCertificado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="passwordCertificado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Firmar", propOrder = {
    "xmlData",
    "identificadorCertificado",
    "passwordCertificado"
})
public class Firmar {

    protected String xmlData;
    protected String identificadorCertificado;
    protected String passwordCertificado;

    /**
     * Gets the value of the xmlData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXmlData() {
        return xmlData;
    }

    /**
     * Sets the value of the xmlData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXmlData(String value) {
        this.xmlData = value;
    }

    /**
     * Gets the value of the identificadorCertificado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificadorCertificado() {
        return identificadorCertificado;
    }

    /**
     * Sets the value of the identificadorCertificado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificadorCertificado(String value) {
        this.identificadorCertificado = value;
    }

    /**
     * Gets the value of the passwordCertificado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPasswordCertificado() {
        return passwordCertificado;
    }

    /**
     * Sets the value of the passwordCertificado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPasswordCertificado(String value) {
        this.passwordCertificado = value;
    }

}
