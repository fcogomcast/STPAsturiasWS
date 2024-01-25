
package es.tributasenasturias.seguridad.autenticacion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="esError" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="idError" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mensajeError" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CIF" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NIF_NIE" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="razon_social" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="apellido1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="apellido2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tipo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "esError",
    "idError",
    "mensajeError",
    "cif",
    "nifnie",
    "razonSocial",
    "apellido1",
    "apellido2",
    "nombre",
    "tipo"
})
@XmlRootElement(name = "loginResponse")
public class LoginResponse {

    protected boolean esError;
    @XmlElement(required = true)
    protected String idError;
    @XmlElement(required = true)
    protected String mensajeError;
    @XmlElement(name = "CIF", required = true)
    protected String cif;
    @XmlElement(name = "NIF_NIE", required = true)
    protected String nifnie;
    @XmlElement(name = "razon_social", required = true)
    protected String razonSocial;
    @XmlElement(required = true)
    protected String apellido1;
    @XmlElement(required = true)
    protected String apellido2;
    @XmlElement(required = true)
    protected String nombre;
    @XmlElement(required = true)
    protected String tipo;

    /**
     * Gets the value of the esError property.
     * 
     */
    public boolean isEsError() {
        return esError;
    }

    /**
     * Sets the value of the esError property.
     * 
     */
    public void setEsError(boolean value) {
        this.esError = value;
    }

    /**
     * Gets the value of the idError property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdError() {
        return idError;
    }

    /**
     * Sets the value of the idError property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdError(String value) {
        this.idError = value;
    }

    /**
     * Gets the value of the mensajeError property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMensajeError() {
        return mensajeError;
    }

    /**
     * Sets the value of the mensajeError property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMensajeError(String value) {
        this.mensajeError = value;
    }

    /**
     * Gets the value of the cif property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCIF() {
        return cif;
    }

    /**
     * Sets the value of the cif property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCIF(String value) {
        this.cif = value;
    }

    /**
     * Gets the value of the nifnie property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNIFNIE() {
        return nifnie;
    }

    /**
     * Sets the value of the nifnie property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNIFNIE(String value) {
        this.nifnie = value;
    }

    /**
     * Gets the value of the razonSocial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     * Sets the value of the razonSocial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRazonSocial(String value) {
        this.razonSocial = value;
    }

    /**
     * Gets the value of the apellido1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApellido1() {
        return apellido1;
    }

    /**
     * Sets the value of the apellido1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApellido1(String value) {
        this.apellido1 = value;
    }

    /**
     * Gets the value of the apellido2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApellido2() {
        return apellido2;
    }

    /**
     * Sets the value of the apellido2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApellido2(String value) {
        this.apellido2 = value;
    }

    /**
     * Gets the value of the nombre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the value of the nombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Gets the value of the tipo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Sets the value of the tipo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipo(String value) {
        this.tipo = value;
    }

}
