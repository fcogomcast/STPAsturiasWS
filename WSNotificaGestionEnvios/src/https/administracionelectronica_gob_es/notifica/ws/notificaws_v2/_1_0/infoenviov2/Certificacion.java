
package https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para Certificacion complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Certificacion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="contenidoCertificacion" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/infoEnvioV2}Contenido"/>
 *         &lt;element name="hash" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fechaCertificacion" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="origen" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="csv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="size" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="metadatos" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="opcionesCertificacion" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/infoEnvioV2}Opciones" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Certificacion", propOrder = {
    "contenidoCertificacion",
    "hash",
    "fechaCertificacion",
    "origen",
    "csv",
    "mime",
    "size",
    "metadatos",
    "opcionesCertificacion"
})
public class Certificacion {

    @XmlElement(required = true)
    protected Contenido contenidoCertificacion;
    @XmlElement(required = true)
    protected String hash;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaCertificacion;
    @XmlElement(required = true)
    protected String origen;
    protected String csv;
    @XmlElement(required = true)
    protected String mime;
    @XmlElement(required = true)
    protected String size;
    protected String metadatos;
    protected Opciones opcionesCertificacion;

    /**
     * Obtiene el valor de la propiedad contenidoCertificacion.
     * 
     * @return
     *     possible object is
     *     {@link Contenido }
     *     
     */
    public Contenido getContenidoCertificacion() {
        return contenidoCertificacion;
    }

    /**
     * Define el valor de la propiedad contenidoCertificacion.
     * 
     * @param value
     *     allowed object is
     *     {@link Contenido }
     *     
     */
    public void setContenidoCertificacion(Contenido value) {
        this.contenidoCertificacion = value;
    }

    /**
     * Obtiene el valor de la propiedad hash.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHash() {
        return hash;
    }

    /**
     * Define el valor de la propiedad hash.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHash(String value) {
        this.hash = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaCertificacion.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaCertificacion() {
        return fechaCertificacion;
    }

    /**
     * Define el valor de la propiedad fechaCertificacion.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaCertificacion(XMLGregorianCalendar value) {
        this.fechaCertificacion = value;
    }

    /**
     * Obtiene el valor de la propiedad origen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * Define el valor de la propiedad origen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigen(String value) {
        this.origen = value;
    }

    /**
     * Obtiene el valor de la propiedad csv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCsv() {
        return csv;
    }

    /**
     * Define el valor de la propiedad csv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCsv(String value) {
        this.csv = value;
    }

    /**
     * Obtiene el valor de la propiedad mime.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMime() {
        return mime;
    }

    /**
     * Define el valor de la propiedad mime.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMime(String value) {
        this.mime = value;
    }

    /**
     * Obtiene el valor de la propiedad size.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSize() {
        return size;
    }

    /**
     * Define el valor de la propiedad size.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSize(String value) {
        this.size = value;
    }

    /**
     * Obtiene el valor de la propiedad metadatos.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMetadatos() {
        return metadatos;
    }

    /**
     * Define el valor de la propiedad metadatos.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMetadatos(String value) {
        this.metadatos = value;
    }

    /**
     * Obtiene el valor de la propiedad opcionesCertificacion.
     * 
     * @return
     *     possible object is
     *     {@link Opciones }
     *     
     */
    public Opciones getOpcionesCertificacion() {
        return opcionesCertificacion;
    }

    /**
     * Define el valor de la propiedad opcionesCertificacion.
     * 
     * @param value
     *     allowed object is
     *     {@link Opciones }
     *     
     */
    public void setOpcionesCertificacion(Opciones value) {
        this.opcionesCertificacion = value;
    }

}
