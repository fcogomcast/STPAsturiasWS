
package es.tributasenasturias.seguridad.firma;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FirmarCIRCEResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FirmarCIRCEResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="contenidoFirmado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FirmarCIRCEResponse", propOrder = {
    "contenidoFirmado"
})
public class FirmarCIRCEResponse {

    protected String contenidoFirmado;

    /**
     * Gets the value of the contenidoFirmado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContenidoFirmado() {
        return contenidoFirmado;
    }

    /**
     * Sets the value of the contenidoFirmado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContenidoFirmado(String value) {
        this.contenidoFirmado = value;
    }

}
