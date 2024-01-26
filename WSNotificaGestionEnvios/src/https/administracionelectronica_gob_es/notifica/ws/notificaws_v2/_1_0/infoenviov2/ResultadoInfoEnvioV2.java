
package https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para resultadoInfoEnvioV2 complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="resultadoInfoEnvioV2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identificador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
 *         &lt;element name="codigoOrganismoEmisor" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/infoEnvioV2}CodigoDIR"/>
 *         &lt;element name="codigoOrganismoEmisorRaiz" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/infoEnvioV2}CodigoDIR" minOccurs="0"/>
 *         &lt;element name="tipoEnvio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fechaCreacion" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="fechaPuestaDisposicion" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="fechaCaducidad" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="retardo" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="procedimiento" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/infoEnvioV2}Procedimiento" minOccurs="0"/>
 *         &lt;element name="documento" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/infoEnvioV2}Documento"/>
 *         &lt;element name="referenciaEmisor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="titular" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/infoEnvioV2}Persona"/>
 *         &lt;element name="destinatarios" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/infoEnvioV2}Destinatarios" minOccurs="0"/>
 *         &lt;element name="entregaPostal" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/infoEnvioV2}EntregaPostal" minOccurs="0"/>
 *         &lt;element name="entregaDEH" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/infoEnvioV2}EntregaDEH" minOccurs="0"/>
 *         &lt;element name="datados" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/infoEnvioV2}Datados" minOccurs="0"/>
 *         &lt;element name="certificacion" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/infoEnvioV2}Certificacion" minOccurs="0"/>
 *         &lt;element name="opcionesEnvio" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/infoEnvioV2}Opciones" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resultadoInfoEnvioV2", propOrder = {
    "identificador",
    "estado",
    "concepto",
    "descripcion",
    "codigoOrganismoEmisor",
    "codigoOrganismoEmisorRaiz",
    "tipoEnvio",
    "fechaCreacion",
    "fechaPuestaDisposicion",
    "fechaCaducidad",
    "retardo",
    "procedimiento",
    "documento",
    "referenciaEmisor",
    "titular",
    "destinatarios",
    "entregaPostal",
    "entregaDEH",
    "datados",
    "certificacion",
    "opcionesEnvio"
})
public class ResultadoInfoEnvioV2 {

    @XmlElement(required = true)
    protected String identificador;
    @XmlElement(required = true)
    protected String estado;
    @XmlElement(required = true)
    protected String concepto;
    protected String descripcion;
    @XmlElement(required = true)
    protected CodigoDIR codigoOrganismoEmisor;
    protected CodigoDIR codigoOrganismoEmisorRaiz;
    @XmlElement(required = true)
    protected String tipoEnvio;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaCreacion;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaPuestaDisposicion;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar fechaCaducidad;
    protected BigInteger retardo;
    protected Procedimiento procedimiento;
    @XmlElement(required = true)
    protected Documento documento;
    protected String referenciaEmisor;
    @XmlElement(required = true)
    protected Persona titular;
    protected Destinatarios destinatarios;
    protected EntregaPostal entregaPostal;
    protected EntregaDEH entregaDEH;
    protected Datados datados;
    protected Certificacion certificacion;
    protected Opciones opcionesEnvio;

    /**
     * Obtiene el valor de la propiedad identificador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * Define el valor de la propiedad identificador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificador(String value) {
        this.identificador = value;
    }

    /**
     * Obtiene el valor de la propiedad estado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Define el valor de la propiedad estado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstado(String value) {
        this.estado = value;
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
     * Obtiene el valor de la propiedad codigoOrganismoEmisor.
     * 
     * @return
     *     possible object is
     *     {@link CodigoDIR }
     *     
     */
    public CodigoDIR getCodigoOrganismoEmisor() {
        return codigoOrganismoEmisor;
    }

    /**
     * Define el valor de la propiedad codigoOrganismoEmisor.
     * 
     * @param value
     *     allowed object is
     *     {@link CodigoDIR }
     *     
     */
    public void setCodigoOrganismoEmisor(CodigoDIR value) {
        this.codigoOrganismoEmisor = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoOrganismoEmisorRaiz.
     * 
     * @return
     *     possible object is
     *     {@link CodigoDIR }
     *     
     */
    public CodigoDIR getCodigoOrganismoEmisorRaiz() {
        return codigoOrganismoEmisorRaiz;
    }

    /**
     * Define el valor de la propiedad codigoOrganismoEmisorRaiz.
     * 
     * @param value
     *     allowed object is
     *     {@link CodigoDIR }
     *     
     */
    public void setCodigoOrganismoEmisorRaiz(CodigoDIR value) {
        this.codigoOrganismoEmisorRaiz = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoEnvio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoEnvio() {
        return tipoEnvio;
    }

    /**
     * Define el valor de la propiedad tipoEnvio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoEnvio(String value) {
        this.tipoEnvio = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaCreacion.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Define el valor de la propiedad fechaCreacion.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaCreacion(XMLGregorianCalendar value) {
        this.fechaCreacion = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaPuestaDisposicion.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaPuestaDisposicion() {
        return fechaPuestaDisposicion;
    }

    /**
     * Define el valor de la propiedad fechaPuestaDisposicion.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaPuestaDisposicion(XMLGregorianCalendar value) {
        this.fechaPuestaDisposicion = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaCaducidad.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaCaducidad() {
        return fechaCaducidad;
    }

    /**
     * Define el valor de la propiedad fechaCaducidad.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaCaducidad(XMLGregorianCalendar value) {
        this.fechaCaducidad = value;
    }

    /**
     * Obtiene el valor de la propiedad retardo.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRetardo() {
        return retardo;
    }

    /**
     * Define el valor de la propiedad retardo.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRetardo(BigInteger value) {
        this.retardo = value;
    }

    /**
     * Obtiene el valor de la propiedad procedimiento.
     * 
     * @return
     *     possible object is
     *     {@link Procedimiento }
     *     
     */
    public Procedimiento getProcedimiento() {
        return procedimiento;
    }

    /**
     * Define el valor de la propiedad procedimiento.
     * 
     * @param value
     *     allowed object is
     *     {@link Procedimiento }
     *     
     */
    public void setProcedimiento(Procedimiento value) {
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

    /**
     * Obtiene el valor de la propiedad datados.
     * 
     * @return
     *     possible object is
     *     {@link Datados }
     *     
     */
    public Datados getDatados() {
        return datados;
    }

    /**
     * Define el valor de la propiedad datados.
     * 
     * @param value
     *     allowed object is
     *     {@link Datados }
     *     
     */
    public void setDatados(Datados value) {
        this.datados = value;
    }

    /**
     * Obtiene el valor de la propiedad certificacion.
     * 
     * @return
     *     possible object is
     *     {@link Certificacion }
     *     
     */
    public Certificacion getCertificacion() {
        return certificacion;
    }

    /**
     * Define el valor de la propiedad certificacion.
     * 
     * @param value
     *     allowed object is
     *     {@link Certificacion }
     *     
     */
    public void setCertificacion(Certificacion value) {
        this.certificacion = value;
    }

    /**
     * Obtiene el valor de la propiedad opcionesEnvio.
     * 
     * @return
     *     possible object is
     *     {@link Opciones }
     *     
     */
    public Opciones getOpcionesEnvio() {
        return opcionesEnvio;
    }

    /**
     * Define el valor de la propiedad opcionesEnvio.
     * 
     * @param value
     *     allowed object is
     *     {@link Opciones }
     *     
     */
    public void setOpcionesEnvio(Opciones value) {
        this.opcionesEnvio = value;
    }

}
