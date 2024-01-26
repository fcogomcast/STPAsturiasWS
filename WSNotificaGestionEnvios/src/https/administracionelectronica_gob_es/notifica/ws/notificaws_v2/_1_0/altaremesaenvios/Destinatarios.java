
package https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.altaremesaenvios;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Destinatarios complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Destinatarios">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="100">
 *         &lt;element name="destinatario" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios}Persona"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Destinatarios", propOrder = {
    "destinatario"
})
public class Destinatarios {

    @XmlElement(required = true)
    protected List<Persona> destinatario;

    /**
     * Gets the value of the destinatario property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the destinatario property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDestinatario().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Persona }
     * 
     * 
     */
    public List<Persona> getDestinatario() {
        if (destinatario == null) {
            destinatario = new ArrayList<Persona>();
        }
        return this.destinatario;
    }

}
