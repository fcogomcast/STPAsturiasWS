
package https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Procedimiento complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Procedimiento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoSia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descripcionSia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Procedimiento", propOrder = {
    "codigoSia",
    "descripcionSia"
})
public class Procedimiento {

    @XmlElement(required = true)
    protected String codigoSia;
    @XmlElement(required = true)
    protected String descripcionSia;

    /**
     * Obtiene el valor de la propiedad codigoSia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoSia() {
        return codigoSia;
    }

    /**
     * Define el valor de la propiedad codigoSia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoSia(String value) {
        this.codigoSia = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcionSia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionSia() {
        return descripcionSia;
    }

    /**
     * Define el valor de la propiedad descripcionSia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionSia(String value) {
        this.descripcionSia = value;
    }

}
