
package https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.altaremesaenvios;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para altaRemesaEnvios complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="altaRemesaEnvios">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoOrganismoEmisor" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios}CodigoDIR"/>
 *         &lt;element name="tipoEnvio" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios}TipoEnvio"/>
 *         &lt;element name="concepto">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;whiteSpace value="preserve"/>
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="descripcion" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;whiteSpace value="preserve"/>
 *               &lt;maxLength value="1000"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="fechaEnvioProgramado" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="procedimiento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="documento" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios}Documento"/>
 *         &lt;element name="envios" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios}Envios"/>
 *         &lt;element name="opcionesRemesa" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios}Opciones" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "altaRemesaEnvios", propOrder = {
    "codigoOrganismoEmisor",
    "tipoEnvio",
    "concepto",
    "descripcion",
    "fechaEnvioProgramado",
    "procedimiento",
    "documento",
    "envios",
    "opcionesRemesa"
})
public class AltaRemesaEnvios {

    @XmlElement(required = true)
    protected String codigoOrganismoEmisor;
    @XmlElement(required = true)
    protected BigInteger tipoEnvio;
    @XmlElement(required = true)
    protected String concepto;
    protected String descripcion;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar fechaEnvioProgramado;
    protected String procedimiento;
    @XmlElement(required = true)
    protected Documento documento;
    @XmlElement(required = true)
    protected Envios envios;
    protected Opciones opcionesRemesa;

    /**
     * Obtiene el valor de la propiedad codigoOrganismoEmisor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoOrganismoEmisor() {
        return codigoOrganismoEmisor;
    }

    /**
     * Define el valor de la propiedad codigoOrganismoEmisor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoOrganismoEmisor(String value) {
        this.codigoOrganismoEmisor = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoEnvio.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTipoEnvio() {
        return tipoEnvio;
    }

    /**
     * Define el valor de la propiedad tipoEnvio.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTipoEnvio(BigInteger value) {
        this.tipoEnvio = value;
    }

    /**
     * Obtiene el valor de la propiedad concepto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConcepto() {
        return concepto;
    }

    /**
     * Define el valor de la propiedad concepto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConcepto(String value) {
        this.concepto = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Define el valor de la propiedad descripcion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcion(String value) {
        this.descripcion = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaEnvioProgramado.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaEnvioProgramado() {
        return fechaEnvioProgramado;
    }

    /**
     * Define el valor de la propiedad fechaEnvioProgramado.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaEnvioProgramado(XMLGregorianCalendar value) {
        this.fechaEnvioProgramado = value;
    }

    /**
     * Obtiene el valor de la propiedad procedimiento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcedimiento() {
        return procedimiento;
    }

    /**
     * Define el valor de la propiedad procedimiento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcedimiento(String value) {
        this.procedimiento = value;
    }

    /**
     * Obtiene el valor de la propiedad documento.
     * 
     * @return
     *     possible object is
     *     {@link Documento }
     *     
     */
    public Documento getDocumento() {
        return documento;
    }

    /**
     * Define el valor de la propiedad documento.
     * 
     * @param value
     *     allowed object is
     *     {@link Documento }
     *     
     */
    public void setDocumento(Documento value) {
        this.documento = value;
    }

    /**
     * Obtiene el valor de la propiedad envios.
     * 
     * @return
     *     possible object is
     *     {@link Envios }
     *     
     */
    public Envios getEnvios() {
        return envios;
    }

    /**
     * Define el valor de la propiedad envios.
     * 
     * @param value
     *     allowed object is
     *     {@link Envios }
     *     
     */
    public void setEnvios(Envios value) {
        this.envios = value;
    }

    /**
     * Obtiene el valor de la propiedad opcionesRemesa.
     * 
     * @return
     *     possible object is
     *     {@link Opciones }
     *     
     */
    public Opciones getOpcionesRemesa() {
        return opcionesRemesa;
    }

    /**
     * Define el valor de la propiedad opcionesRemesa.
     * 
     * @param value
     *     allowed object is
     *     {@link Opciones }
     *     
     */
    public void setOpcionesRemesa(Opciones value) {
        this.opcionesRemesa = value;
    }

}
