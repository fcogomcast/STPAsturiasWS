
package es.tributasenasturias.seguridad.firma;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InsertarCertificado complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InsertarCertificado">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rutaCertificado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rutaClavePrivada" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="aliasCertificado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "InsertarCertificado", propOrder = {
    "rutaCertificado",
    "rutaClavePrivada",
    "aliasCertificado",
    "passwordCertificado"
})
public class InsertarCertificado {

    protected String rutaCertificado;
    protected String rutaClavePrivada;
    protected String aliasCertificado;
    protected String passwordCertificado;

    /**
     * Gets the value of the rutaCertificado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRutaCertificado() {
        return rutaCertificado;
    }

    /**
     * Sets the value of the rutaCertificado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRutaCertificado(String value) {
        this.rutaCertificado = value;
    }

    /**
     * Gets the value of the rutaClavePrivada property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRutaClavePrivada() {
        return rutaClavePrivada;
    }

    /**
     * Sets the value of the rutaClavePrivada property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRutaClavePrivada(String value) {
        this.rutaClavePrivada = value;
    }

    /**
     * Gets the value of the aliasCertificado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAliasCertificado() {
        return aliasCertificado;
    }

    /**
     * Sets the value of the aliasCertificado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAliasCertificado(String value) {
        this.aliasCertificado = value;
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
