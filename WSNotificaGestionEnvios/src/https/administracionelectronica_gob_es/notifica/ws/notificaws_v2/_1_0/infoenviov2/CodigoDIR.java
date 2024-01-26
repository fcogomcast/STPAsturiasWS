
package https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para CodigoDIR complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="CodigoDIR">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigo">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="9"/>
 *               &lt;whiteSpace value="collapse"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="descripcionCodigoDIR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nifDIR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CodigoDIR", propOrder = {
    "codigo",
    "descripcionCodigoDIR",
    "nifDIR"
})
public class CodigoDIR {

    @XmlElement(required = true)
    protected String codigo;
    protected String descripcionCodigoDIR;
    protected String nifDIR;

    /**
     * Obtiene el valor de la propiedad codigo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Define el valor de la propiedad codigo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigo(String value) {
        this.codigo = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcionCodigoDIR.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionCodigoDIR() {
        return descripcionCodigoDIR;
    }

    /**
     * Define el valor de la propiedad descripcionCodigoDIR.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionCodigoDIR(String value) {
        this.descripcionCodigoDIR = value;
    }

    /**
     * Obtiene el valor de la propiedad nifDIR.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNifDIR() {
        return nifDIR;
    }

    /**
     * Define el valor de la propiedad nifDIR.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNifDIR(String value) {
        this.nifDIR = value;
    }

}
