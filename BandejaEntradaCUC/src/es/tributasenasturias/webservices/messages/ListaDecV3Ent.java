package es.tributasenasturias.webservices.messages;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListaDecV3Ent", propOrder = {
    "declarante"
})
public class ListaDecV3Ent {

    @XmlElement(required = true)
    private DeclaranteType declarante;

    /**
     * Gets the value of the declarante property.
     * 
     * @return
     *     possible object is
     *     {@link DeclaranteType }
     *     
     */
    public final DeclaranteType getDeclarante() {
        return declarante;
    }

    /**
     * Sets the value of the declarante property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeclaranteType }
     *     
     */
    public final void setDeclarante(DeclaranteType value) {
        this.declarante = value;
    }

}

