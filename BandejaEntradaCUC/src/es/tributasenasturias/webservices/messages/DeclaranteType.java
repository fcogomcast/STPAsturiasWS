package es.tributasenasturias.webservices.messages;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "declaranteType", propOrder = {
    "nifDeclarante",
    "nombreDeclarante"
})
public class DeclaranteType {

    @XmlElement(name = "NifDeclarante", required = true)
    private String nifDeclarante;
    @XmlElement(name = "NombreDeclarante", required = true)
    private String nombreDeclarante;

    /**
     * Gets the value of the nifDeclarante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public final String getNifDeclarante() {
        return nifDeclarante;
    }

    /**
     * Sets the value of the nifDeclarante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public final void setNifDeclarante(String value) {
        this.nifDeclarante = value;
    }

    /**
     * Gets the value of the nombreDeclarante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public final String getNombreDeclarante() {
        return nombreDeclarante;
    }

    /**
     * Sets the value of the nombreDeclarante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public final void setNombreDeclarante(String value) {
        this.nombreDeclarante = value;
    }
}
