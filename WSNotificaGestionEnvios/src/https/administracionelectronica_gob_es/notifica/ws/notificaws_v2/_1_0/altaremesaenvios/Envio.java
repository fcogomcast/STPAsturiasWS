
package https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.altaremesaenvios;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Envio complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Envio">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="referenciaEmisor" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="titular" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios}Persona"/>
 *         &lt;element name="destinatarios" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios}Destinatarios" minOccurs="0"/>
 *         &lt;element name="entregaPostal" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios}EntregaPostal" minOccurs="0"/>
 *         &lt;element name="entregaDEH" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios}EntregaDEH" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Envio", propOrder = {
    "referenciaEmisor",
    "titular",
    "destinatarios",
    "entregaPostal",
    "entregaDEH"
})
public class Envio {

    protected String referenciaEmisor;
    @XmlElement(required = true)
    protected Persona titular;
    protected Destinatarios destinatarios;
    protected EntregaPostal entregaPostal;
    protected EntregaDEH entregaDEH;

    /**
     * Obtiene el valor de la propiedad referenciaEmisor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenciaEmisor() {
        return referenciaEmisor;
    }

    /**
     * Define el valor de la propiedad referenciaEmisor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenciaEmisor(String value) {
        this.referenciaEmisor = value;
    }

    /**
     * Obtiene el valor de la propiedad titular.
     * 
     * @return
     *     possible object is
     *     {@link Persona }
     *     
     */
    public Persona getTitular() {
        return titular;
    }

    /**
     * Define el valor de la propiedad titular.
     * 
     * @param value
     *     allowed object is
     *     {@link Persona }
     *     
     */
    public void setTitular(Persona value) {
        this.titular = value;
    }

    /**
     * Obtiene el valor de la propiedad destinatarios.
     * 
     * @return
     *     possible object is
     *     {@link Destinatarios }
     *     
     */
    public Destinatarios getDestinatarios() {
        return destinatarios;
    }

    /**
     * Define el valor de la propiedad destinatarios.
     * 
     * @param value
     *     allowed object is
     *     {@link Destinatarios }
     *     
     */
    public void setDestinatarios(Destinatarios value) {
        this.destinatarios = value;
    }

    /**
     * Obtiene el valor de la propiedad entregaPostal.
     * 
     * @return
     *     possible object is
     *     {@link EntregaPostal }
     *     
     */
    public EntregaPostal getEntregaPostal() {
        return entregaPostal;
    }

    /**
     * Define el valor de la propiedad entregaPostal.
     * 
     * @param value
     *     allowed object is
     *     {@link EntregaPostal }
     *     
     */
    public void setEntregaPostal(EntregaPostal value) {
        this.entregaPostal = value;
    }

    /**
     * Obtiene el valor de la propiedad entregaDEH.
     * 
     * @return
     *     possible object is
     *     {@link EntregaDEH }
     *     
     */
    public EntregaDEH getEntregaDEH() {
        return entregaDEH;
    }

    /**
     * Define el valor de la propiedad entregaDEH.
     * 
     * @param value
     *     allowed object is
     *     {@link EntregaDEH }
     *     
     */
    public void setEntregaDEH(EntregaDEH value) {
        this.entregaDEH = value;
    }

}
