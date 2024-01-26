
package es.tributasenasturias.seguridad.firma;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FirmarSOAP complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FirmarSOAP">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="xmlData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="aliasCertificado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FirmarSOAP", propOrder = {
    "xmlData",
    "aliasCertificado"
})
public class FirmarSOAP {

    protected String xmlData;
    protected String aliasCertificado;

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

}
