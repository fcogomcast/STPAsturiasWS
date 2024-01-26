
package es.tributasenasturias.seguridad.firma;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FirmarXML complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FirmarXML">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="xmlData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="aliasCertificado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idNodoAFirmar" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nodoPadre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nsNodoPadre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FirmarXML", propOrder = {
    "xmlData",
    "aliasCertificado",
    "idNodoAFirmar",
    "nodoPadre",
    "nsNodoPadre"
})
public class FirmarXML {

    protected String xmlData;
    protected String aliasCertificado;
    protected String idNodoAFirmar;
    protected String nodoPadre;
    protected String nsNodoPadre;

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

    /**
     * Gets the value of the idNodoAFirmar property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdNodoAFirmar() {
        return idNodoAFirmar;
    }

    /**
     * Sets the value of the idNodoAFirmar property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdNodoAFirmar(String value) {
        this.idNodoAFirmar = value;
    }

    /**
     * Gets the value of the nodoPadre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodoPadre() {
        return nodoPadre;
    }

    /**
     * Sets the value of the nodoPadre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodoPadre(String value) {
        this.nodoPadre = value;
    }

    /**
     * Gets the value of the nsNodoPadre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNsNodoPadre() {
        return nsNodoPadre;
    }

    /**
     * Sets the value of the nsNodoPadre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNsNodoPadre(String value) {
        this.nsNodoPadre = value;
    }

}
