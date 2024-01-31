
package es.tributasenasturias.servicios.pago600generico;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for presentacionTypeOut complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="presentacionTypeOut">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="numeroExpediente" type="{http://servicios.tributasenasturias.es/Pago600Generico}numeroExpedienteTypeOut"/>
 *         &lt;element name="justificanteComparecencia" type="{http://servicios.tributasenasturias.es/Pago600Generico}pdfB64TypeOut"/>
 *         &lt;element name="certificadoPresentacionPago" type="{http://servicios.tributasenasturias.es/Pago600Generico}pdfB64TypeOut"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "presentacionTypeOut", propOrder = {
    "numeroExpediente",
    "justificanteComparecencia",
    "certificadoPresentacionPago"
})
public class PresentacionTypeOut {

    @XmlElement(required = true)
    protected String numeroExpediente;
    @XmlElement(required = true)
    protected byte[] justificanteComparecencia;
    @XmlElement(required = true)
    protected byte[] certificadoPresentacionPago;

    /**
     * Gets the value of the numeroExpediente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    /**
     * Sets the value of the numeroExpediente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroExpediente(String value) {
        this.numeroExpediente = value;
    }

    /**
     * Gets the value of the justificanteComparecencia property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getJustificanteComparecencia() {
        return justificanteComparecencia;
    }

    /**
     * Sets the value of the justificanteComparecencia property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setJustificanteComparecencia(byte[] value) {
        this.justificanteComparecencia = ((byte[]) value);
    }

    /**
     * Gets the value of the certificadoPresentacionPago property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getCertificadoPresentacionPago() {
        return certificadoPresentacionPago;
    }

    /**
     * Sets the value of the certificadoPresentacionPago property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setCertificadoPresentacionPago(byte[] value) {
        this.certificadoPresentacionPago = ((byte[]) value);
    }

}
