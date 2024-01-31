
package es.tributasenasturias.servicios.pago600generico;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for escrituraType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="escrituraType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codNotario" type="{http://servicios.tributasenasturias.es/Pago600Generico}string30TypeO"/>
 *         &lt;element name="codNotaria" type="{http://servicios.tributasenasturias.es/Pago600Generico}string30TypeO"/>
 *         &lt;element name="numProtocolo" type="{http://servicios.tributasenasturias.es/Pago600Generico}digito13TypeO"/>
 *         &lt;element name="numProtocoloBis" type="{http://servicios.tributasenasturias.es/Pago600Generico}digito4TypeO"/>
 *         &lt;element name="fechaAutorizacion" type="{http://servicios.tributasenasturias.es/Pago600Generico}fechaTypeO"/>
 *         &lt;element name="documentoEscritura" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="firmaEscritura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "escrituraType", propOrder = {
    "codNotario",
    "codNotaria",
    "numProtocolo",
    "numProtocoloBis",
    "fechaAutorizacion",
    "documentoEscritura",
    "firmaEscritura"
})
public class EscrituraType {

    @XmlElement(required = true)
    protected String codNotario;
    @XmlElement(required = true)
    protected String codNotaria;
    @XmlElement(required = true)
    protected String numProtocolo;
    @XmlElement(required = true)
    protected String numProtocoloBis;
    @XmlElement(required = true)
    protected String fechaAutorizacion;
    @XmlElement(required = true)
    protected String documentoEscritura;
    protected String firmaEscritura;

    /**
     * Gets the value of the codNotario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodNotario() {
        return codNotario;
    }

    /**
     * Sets the value of the codNotario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodNotario(String value) {
        this.codNotario = value;
    }

    /**
     * Gets the value of the codNotaria property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodNotaria() {
        return codNotaria;
    }

    /**
     * Sets the value of the codNotaria property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodNotaria(String value) {
        this.codNotaria = value;
    }

    /**
     * Gets the value of the numProtocolo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumProtocolo() {
        return numProtocolo;
    }

    /**
     * Sets the value of the numProtocolo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumProtocolo(String value) {
        this.numProtocolo = value;
    }

    /**
     * Gets the value of the numProtocoloBis property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumProtocoloBis() {
        return numProtocoloBis;
    }

    /**
     * Sets the value of the numProtocoloBis property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumProtocoloBis(String value) {
        this.numProtocoloBis = value;
    }

    /**
     * Gets the value of the fechaAutorizacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    /**
     * Sets the value of the fechaAutorizacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaAutorizacion(String value) {
        this.fechaAutorizacion = value;
    }

    /**
     * Gets the value of the documentoEscritura property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentoEscritura() {
        return documentoEscritura;
    }

    /**
     * Sets the value of the documentoEscritura property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentoEscritura(String value) {
        this.documentoEscritura = value;
    }

    /**
     * Gets the value of the firmaEscritura property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirmaEscritura() {
        return firmaEscritura;
    }

    /**
     * Sets the value of the firmaEscritura property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirmaEscritura(String value) {
        this.firmaEscritura = value;
    }

}
