
package es.tributasenasturias.seguridad.firma;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ValidarResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ValidarResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="esValido" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ValidarResponse", propOrder = {
    "esValido"
})
public class ValidarResponse {

    protected boolean esValido;

    /**
     * Gets the value of the esValido property.
     * 
     */
    public boolean isEsValido() {
        return esValido;
    }

    /**
     * Sets the value of the esValido property.
     * 
     */
    public void setEsValido(boolean value) {
        this.esValido = value;
    }

}
