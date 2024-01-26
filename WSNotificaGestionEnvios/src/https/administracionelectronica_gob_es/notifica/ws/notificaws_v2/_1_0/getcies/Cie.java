
package https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.getcies;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Cie complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Cie">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="centroImpresion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fechaVigencia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codigoUnidadRelacionada" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nombreUnidadRelacionada" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="orden" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Cie", propOrder = {
    "id",
    "centroImpresion",
    "fechaVigencia",
    "codigoUnidadRelacionada",
    "nombreUnidadRelacionada",
    "orden"
})
public class Cie {

    protected int id;
    @XmlElement(required = true)
    protected String centroImpresion;
    @XmlElement(required = true)
    protected String fechaVigencia;
    @XmlElement(required = true)
    protected String codigoUnidadRelacionada;
    @XmlElement(required = true)
    protected String nombreUnidadRelacionada;
    protected int orden;

    /**
     * Obtiene el valor de la propiedad id.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Define el valor de la propiedad id.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Obtiene el valor de la propiedad centroImpresion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCentroImpresion() {
        return centroImpresion;
    }

    /**
     * Define el valor de la propiedad centroImpresion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCentroImpresion(String value) {
        this.centroImpresion = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaVigencia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaVigencia() {
        return fechaVigencia;
    }

    /**
     * Define el valor de la propiedad fechaVigencia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaVigencia(String value) {
        this.fechaVigencia = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoUnidadRelacionada.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoUnidadRelacionada() {
        return codigoUnidadRelacionada;
    }

    /**
     * Define el valor de la propiedad codigoUnidadRelacionada.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoUnidadRelacionada(String value) {
        this.codigoUnidadRelacionada = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreUnidadRelacionada.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreUnidadRelacionada() {
        return nombreUnidadRelacionada;
    }

    /**
     * Define el valor de la propiedad nombreUnidadRelacionada.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreUnidadRelacionada(String value) {
        this.nombreUnidadRelacionada = value;
    }

    /**
     * Obtiene el valor de la propiedad orden.
     * 
     */
    public int getOrden() {
        return orden;
    }

    /**
     * Define el valor de la propiedad orden.
     * 
     */
    public void setOrden(int value) {
        this.orden = value;
    }

}
