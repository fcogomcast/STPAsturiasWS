
package https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.getcies;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para resultadoGetCies complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="resultadoGetCies">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cies" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/getCies}Cies"/>
 *         &lt;element name="codigoRespuesta" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descripcionRespuesta" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resultadoGetCies", propOrder = {
    "cies",
    "codigoRespuesta",
    "descripcionRespuesta"
})
public class ResultadoGetCies {

    @XmlElement(required = true)
    protected Cies cies;
    @XmlElement(required = true)
    protected String codigoRespuesta;
    @XmlElement(required = true)
    protected String descripcionRespuesta;

    /**
     * Obtiene el valor de la propiedad cies.
     * 
     * @return
     *     possible object is
     *     {@link Cies }
     *     
     */
    public Cies getCies() {
        return cies;
    }

    /**
     * Define el valor de la propiedad cies.
     * 
     * @param value
     *     allowed object is
     *     {@link Cies }
     *     
     */
    public void setCies(Cies value) {
        this.cies = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoRespuesta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoRespuesta() {
        return codigoRespuesta;
    }

    /**
     * Define el valor de la propiedad codigoRespuesta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoRespuesta(String value) {
        this.codigoRespuesta = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcionRespuesta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionRespuesta() {
        return descripcionRespuesta;
    }

    /**
     * Define el valor de la propiedad descripcionRespuesta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionRespuesta(String value) {
        this.descripcionRespuesta = value;
    }

}
