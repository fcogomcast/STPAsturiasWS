package es.tributasenasturias.webservices.messages;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "declaracionType", propOrder = {
    "clave",
    "referencia",
    "tipoRespuesta"
})
public class DeclaracionType {

    @XmlElement(required = true)
    private String clave;
    @XmlElement(required = true)
    private String referencia;
    @XmlElement(required = true)
    private String tipoRespuesta;

    /**
     * Gets the value of the clave property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public final String getClave() {
        return clave;
    }

    /**
     * Sets the value of the clave property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public final void setClave(String value) {
        this.clave = value;
    }

    /**
     * Gets the value of the referencia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public final String getReferencia() {
        return referencia;
    }

    /**
     * Sets the value of the referencia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public final void setReferencia(String value) {
        this.referencia = value;
    }

    /**
     * Gets the value of the tipoRespuesta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public final String getTipoRespuesta() {
        return tipoRespuesta;
    }

    /**
     * Sets the value of the tipoRespuesta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public final void setTipoRespuesta(String value) {
        this.tipoRespuesta = value;
    }

}
