
package https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.altaremesaenvios;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Documento complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Documento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="contenido" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios}Contenido"/>
 *         &lt;element name="hash" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="enlaceDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="metadatos" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="opcionesDocumento" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios}Opciones" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Documento", propOrder = {
    "contenido",
    "hash",
    "enlaceDocumento",
    "metadatos",
    "opcionesDocumento"
})
public class Documento {

    @XmlElement(required = true)
    protected Contenido contenido;
    @XmlElement(required = true)
    protected String hash;
    protected String enlaceDocumento;
    protected String metadatos;
    protected Opciones opcionesDocumento;

    /**
     * Obtiene el valor de la propiedad contenido.
     * 
     * @return
     *     possible object is
     *     {@link Contenido }
     *     
     */
    public Contenido getContenido() {
        return contenido;
    }

    /**
     * Define el valor de la propiedad contenido.
     * 
     * @param value
     *     allowed object is
     *     {@link Contenido }
     *     
     */
    public void setContenido(Contenido value) {
        this.contenido = value;
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
     * Obtiene el valor de la propiedad enlaceDocumento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnlaceDocumento() {
        return enlaceDocumento;
    }

    /**
     * Define el valor de la propiedad enlaceDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnlaceDocumento(String value) {
        this.enlaceDocumento = value;
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
     * Obtiene el valor de la propiedad opcionesDocumento.
     * 
     * @return
     *     possible object is
     *     {@link Opciones }
     *     
     */
    public Opciones getOpcionesDocumento() {
        return opcionesDocumento;
    }

    /**
     * Define el valor de la propiedad opcionesDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link Opciones }
     *     
     */
    public void setOpcionesDocumento(Opciones value) {
        this.opcionesDocumento = value;
    }

}
