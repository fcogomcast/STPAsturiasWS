
package es.tributasenasturias.services.ws.wsmultas.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for salidaConsulta complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AltaMultaRequestResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codResultado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="1"/>
 *         &lt;element name="resultado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idEper" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="numeroRege" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AltaMultaRequestResponse", propOrder = {
    "codResultado",
	"resultado",
    "idEper",
    "numeroRege"
})
public class Respuesta {

    protected String codResultado;
    protected String resultado;
    protected String idEper;
    protected String numeroRege;
    
    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodresultado() {
        return codResultado;
    }

    /**
     * Gets the value of the resultado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultado() {
        return resultado;
    }
    
    /**
     * Gets the value of the idEper property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdeper() {
        return idEper;
    }

    /**
     * Gets the value of the numeroRege property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumerorege() {
        return numeroRege;
    }

    /**
     * Sets the value of the codResultado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodresultado(String value) {
        this.codResultado = value;
    }
    
    /**
     * Sets the value of the idEper property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdeper(String value) {
        this.idEper = value;
    }

    /**
     * Sets the value of the resultado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumerorege(String value) {
        this.numeroRege = value;
    }
    
    /**
     * Sets the value of the resultado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultado(String value) {
        this.resultado = value;
    }
}

