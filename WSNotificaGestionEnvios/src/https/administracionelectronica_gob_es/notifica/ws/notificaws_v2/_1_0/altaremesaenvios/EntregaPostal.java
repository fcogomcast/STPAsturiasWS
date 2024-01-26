
package https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.altaremesaenvios;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para EntregaPostal complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="EntregaPostal">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="organismoPagadorPostal" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios}OrganismoPagadorPostal" minOccurs="0"/>
 *         &lt;element name="organismoPagadorCIE" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios}OrganismoPagadorCIE" minOccurs="0"/>
 *         &lt;element name="tipoDomicilio" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios}TipoDomicilio"/>
 *         &lt;element name="tipoVia" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="nombreVia" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="numeroCasa" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="puntoKilometrico" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="5"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="portal" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="puerta" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="escalera" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="planta" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bloque" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="complemento" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="40"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="calificadorNumero" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="codigoPostal">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="apartadoCorreos" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="municipio" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="[0-9]{6}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="provincia" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="[0-9]{2}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="pais" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="poblacion" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="linea1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="linea2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="opcionesCIE" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios}Opciones" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntregaPostal", propOrder = {
    "organismoPagadorPostal",
    "organismoPagadorCIE",
    "tipoDomicilio",
    "tipoVia",
    "nombreVia",
    "numeroCasa",
    "puntoKilometrico",
    "portal",
    "puerta",
    "escalera",
    "planta",
    "bloque",
    "complemento",
    "calificadorNumero",
    "codigoPostal",
    "apartadoCorreos",
    "municipio",
    "provincia",
    "pais",
    "poblacion",
    "linea1",
    "linea2",
    "opcionesCIE"
})
public class EntregaPostal {

    protected OrganismoPagadorPostal organismoPagadorPostal;
    protected OrganismoPagadorCIE organismoPagadorCIE;
    @XmlElement(required = true)
    protected BigInteger tipoDomicilio;
    protected String tipoVia;
    protected String nombreVia;
    protected String numeroCasa;
    protected String puntoKilometrico;
    protected String portal;
    protected String puerta;
    protected String escalera;
    protected String planta;
    protected String bloque;
    protected String complemento;
    protected String calificadorNumero;
    @XmlElement(required = true)
    protected String codigoPostal;
    protected String apartadoCorreos;
    protected String municipio;
    protected String provincia;
    protected String pais;
    protected String poblacion;
    protected String linea1;
    protected String linea2;
    protected Opciones opcionesCIE;

    /**
     * Obtiene el valor de la propiedad organismoPagadorPostal.
     * 
     * @return
     *     possible object is
     *     {@link OrganismoPagadorPostal }
     *     
     */
    public OrganismoPagadorPostal getOrganismoPagadorPostal() {
        return organismoPagadorPostal;
    }

    /**
     * Define el valor de la propiedad organismoPagadorPostal.
     * 
     * @param value
     *     allowed object is
     *     {@link OrganismoPagadorPostal }
     *     
     */
    public void setOrganismoPagadorPostal(OrganismoPagadorPostal value) {
        this.organismoPagadorPostal = value;
    }

    /**
     * Obtiene el valor de la propiedad organismoPagadorCIE.
     * 
     * @return
     *     possible object is
     *     {@link OrganismoPagadorCIE }
     *     
     */
    public OrganismoPagadorCIE getOrganismoPagadorCIE() {
        return organismoPagadorCIE;
    }

    /**
     * Define el valor de la propiedad organismoPagadorCIE.
     * 
     * @param value
     *     allowed object is
     *     {@link OrganismoPagadorCIE }
     *     
     */
    public void setOrganismoPagadorCIE(OrganismoPagadorCIE value) {
        this.organismoPagadorCIE = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoDomicilio.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTipoDomicilio() {
        return tipoDomicilio;
    }

    /**
     * Define el valor de la propiedad tipoDomicilio.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTipoDomicilio(BigInteger value) {
        this.tipoDomicilio = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoVia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoVia() {
        return tipoVia;
    }

    /**
     * Define el valor de la propiedad tipoVia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoVia(String value) {
        this.tipoVia = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreVia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreVia() {
        return nombreVia;
    }

    /**
     * Define el valor de la propiedad nombreVia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreVia(String value) {
        this.nombreVia = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroCasa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroCasa() {
        return numeroCasa;
    }

    /**
     * Define el valor de la propiedad numeroCasa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroCasa(String value) {
        this.numeroCasa = value;
    }

    /**
     * Obtiene el valor de la propiedad puntoKilometrico.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPuntoKilometrico() {
        return puntoKilometrico;
    }

    /**
     * Define el valor de la propiedad puntoKilometrico.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPuntoKilometrico(String value) {
        this.puntoKilometrico = value;
    }

    /**
     * Obtiene el valor de la propiedad portal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortal() {
        return portal;
    }

    /**
     * Define el valor de la propiedad portal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortal(String value) {
        this.portal = value;
    }

    /**
     * Obtiene el valor de la propiedad puerta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPuerta() {
        return puerta;
    }

    /**
     * Define el valor de la propiedad puerta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPuerta(String value) {
        this.puerta = value;
    }

    /**
     * Obtiene el valor de la propiedad escalera.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEscalera() {
        return escalera;
    }

    /**
     * Define el valor de la propiedad escalera.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEscalera(String value) {
        this.escalera = value;
    }

    /**
     * Obtiene el valor de la propiedad planta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlanta() {
        return planta;
    }

    /**
     * Define el valor de la propiedad planta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlanta(String value) {
        this.planta = value;
    }

    /**
     * Obtiene el valor de la propiedad bloque.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBloque() {
        return bloque;
    }

    /**
     * Define el valor de la propiedad bloque.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBloque(String value) {
        this.bloque = value;
    }

    /**
     * Obtiene el valor de la propiedad complemento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComplemento() {
        return complemento;
    }

    /**
     * Define el valor de la propiedad complemento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComplemento(String value) {
        this.complemento = value;
    }

    /**
     * Obtiene el valor de la propiedad calificadorNumero.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCalificadorNumero() {
        return calificadorNumero;
    }

    /**
     * Define el valor de la propiedad calificadorNumero.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCalificadorNumero(String value) {
        this.calificadorNumero = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoPostal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoPostal() {
        return codigoPostal;
    }

    /**
     * Define el valor de la propiedad codigoPostal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoPostal(String value) {
        this.codigoPostal = value;
    }

    /**
     * Obtiene el valor de la propiedad apartadoCorreos.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApartadoCorreos() {
        return apartadoCorreos;
    }

    /**
     * Define el valor de la propiedad apartadoCorreos.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApartadoCorreos(String value) {
        this.apartadoCorreos = value;
    }

    /**
     * Obtiene el valor de la propiedad municipio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMunicipio() {
        return municipio;
    }

    /**
     * Define el valor de la propiedad municipio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMunicipio(String value) {
        this.municipio = value;
    }

    /**
     * Obtiene el valor de la propiedad provincia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * Define el valor de la propiedad provincia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvincia(String value) {
        this.provincia = value;
    }

    /**
     * Obtiene el valor de la propiedad pais.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPais() {
        return pais;
    }

    /**
     * Define el valor de la propiedad pais.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPais(String value) {
        this.pais = value;
    }

    /**
     * Obtiene el valor de la propiedad poblacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoblacion() {
        return poblacion;
    }

    /**
     * Define el valor de la propiedad poblacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoblacion(String value) {
        this.poblacion = value;
    }

    /**
     * Obtiene el valor de la propiedad linea1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinea1() {
        return linea1;
    }

    /**
     * Define el valor de la propiedad linea1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinea1(String value) {
        this.linea1 = value;
    }

    /**
     * Obtiene el valor de la propiedad linea2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinea2() {
        return linea2;
    }

    /**
     * Define el valor de la propiedad linea2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinea2(String value) {
        this.linea2 = value;
    }

    /**
     * Obtiene el valor de la propiedad opcionesCIE.
     * 
     * @return
     *     possible object is
     *     {@link Opciones }
     *     
     */
    public Opciones getOpcionesCIE() {
        return opcionesCIE;
    }

    /**
     * Define el valor de la propiedad opcionesCIE.
     * 
     * @param value
     *     allowed object is
     *     {@link Opciones }
     *     
     */
    public void setOpcionesCIE(Opciones value) {
        this.opcionesCIE = value;
    }

}
