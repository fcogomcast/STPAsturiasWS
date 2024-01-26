
package es.tributasenasturias.seguridad.firma;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FirmarAncertResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FirmarAncertResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="xmlFirmado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FirmarAncertResponse", propOrder = {
    "xmlFirmado"
})
public class FirmarAncertResponse {

    protected String xmlFirmado;

    /**
     * Gets the value of the xmlFirmado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXmlFirmado() {
        return xmlFirmado;
    }

    /**
     * Sets the value of the xmlFirmado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXmlFirmado(String value) {
        this.xmlFirmado = value;
    }

}
