
package https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para EntregaDEH complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="EntregaDEH">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="obligado" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="codigoProcedimiento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntregaDEH", propOrder = {
    "obligado",
    "codigoProcedimiento"
})
public class EntregaDEH {

    protected boolean obligado;
    protected String codigoProcedimiento;

    /**
     * Obtiene el valor de la propiedad obligado.
     * 
     */
    public boolean isObligado() {
        return obligado;
    }

    /**
     * Define el valor de la propiedad obligado.
     * 
     */
    public void setObligado(boolean value) {
        this.obligado = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoProcedimiento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoProcedimiento() {
        return codigoProcedimiento;
    }

    /**
     * Define el valor de la propiedad codigoProcedimiento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoProcedimiento(String value) {
        this.codigoProcedimiento = value;
    }

}
