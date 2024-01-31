
package es.tributasenasturias.servicios.pago600generico;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for pagoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pagoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idOperante" type="{http://servicios.tributasenasturias.es/Pago600Generico}nifCifNieTypeO"/>
 *         &lt;element name="numTarjeta" type="{http://servicios.tributasenasturias.es/Pago600Generico}tarjetaTypeO" minOccurs="0"/>
 *         &lt;element name="fechaCaducidadTarjeta" type="{http://servicios.tributasenasturias.es/Pago600Generico}cifras4TypeO" minOccurs="0"/>
 *         &lt;element name="ccc" type="{http://servicios.tributasenasturias.es/Pago600Generico}cccTypeO" minOccurs="0"/>
 *         &lt;element name="nrc" type="{http://servicios.tributasenasturias.es/Pago600Generico}string22TypeOut" minOccurs="0"/>
 *         &lt;element name="justificantePago" type="{http://servicios.tributasenasturias.es/Pago600Generico}pdfB64TypeOut"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pagoType", propOrder = {
    "idOperante",
    "numTarjeta",
    "fechaCaducidadTarjeta",
    "ccc",
    "nrc",
    "justificantePago"
})
public class PagoType {

    @XmlElement(required = true)
    protected String idOperante;
    protected String numTarjeta;
    protected String fechaCaducidadTarjeta;
    protected String ccc;
    protected String nrc;
    @XmlElement(required = true, nillable = true)
    protected byte[] justificantePago;

    /**
     * Gets the value of the idOperante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdOperante() {
        return idOperante;
    }

    /**
     * Sets the value of the idOperante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdOperante(String value) {
        this.idOperante = value;
    }

    /**
     * Gets the value of the numTarjeta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumTarjeta() {
        return numTarjeta;
    }

    /**
     * Sets the value of the numTarjeta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumTarjeta(String value) {
        this.numTarjeta = value;
    }

    /**
     * Gets the value of the fechaCaducidadTarjeta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaCaducidadTarjeta() {
        return fechaCaducidadTarjeta;
    }

    /**
     * Sets the value of the fechaCaducidadTarjeta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaCaducidadTarjeta(String value) {
        this.fechaCaducidadTarjeta = value;
    }

    /**
     * Gets the value of the ccc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCcc() {
        return ccc;
    }

    /**
     * Sets the value of the ccc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCcc(String value) {
        this.ccc = value;
    }

    /**
     * Gets the value of the nrc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNrc() {
        return nrc;
    }

    /**
     * Sets the value of the nrc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNrc(String value) {
        this.nrc = value;
    }

    /**
     * Gets the value of the justificantePago property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getJustificantePago() {
        return justificantePago;
    }

    /**
     * Sets the value of the justificantePago property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setJustificantePago(byte[] value) {
        this.justificantePago = ((byte[]) value);
    }

}
