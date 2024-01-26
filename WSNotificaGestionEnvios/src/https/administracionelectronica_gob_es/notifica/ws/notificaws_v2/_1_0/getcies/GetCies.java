
package https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.getcies;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para getCies complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="getCies">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="organismoEmisor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCies", propOrder = {
    "organismoEmisor"
})
public class GetCies {

    @XmlElement(required = true)
    protected String organismoEmisor;

    /**
     * Obtiene el valor de la propiedad organismoEmisor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganismoEmisor() {
        return organismoEmisor;
    }

    /**
     * Define el valor de la propiedad organismoEmisor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganismoEmisor(String value) {
        this.organismoEmisor = value;
    }

}
