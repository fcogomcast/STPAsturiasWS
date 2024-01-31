
package es.tributasenasturias.servicios.calculo600generico;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for intervinienteType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="intervinienteType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idInterviniente" type="{http://servicios.tributasenasturias.es/Calculo600Generico}nifCifNieTypeO"/>
 *         &lt;element name="nombreRazonSocial" type="{http://servicios.tributasenasturias.es/Calculo600Generico}string60TypeO"/>
 *         &lt;element name="pais" type="{http://servicios.tributasenasturias.es/Calculo600Generico}string2TypeO"/>
 *         &lt;element name="paisDesc" type="{http://servicios.tributasenasturias.es/Calculo600Generico}string30TypeO"/>
 *         &lt;element name="localiFueraEsp" type="{http://servicios.tributasenasturias.es/Calculo600Generico}string50TypeO" minOccurs="0"/>
 *         &lt;element name="provincia" type="{http://servicios.tributasenasturias.es/Calculo600Generico}provinciaTypeEO"/>
 *         &lt;element name="provinciaDesc" type="{http://servicios.tributasenasturias.es/Calculo600Generico}provinciaDescTypeEO"/>
 *         &lt;element name="municipio" type="{http://servicios.tributasenasturias.es/Calculo600Generico}digito5TypeO"/>
 *         &lt;element name="municipioDesc" type="{http://servicios.tributasenasturias.es/Calculo600Generico}string50TypeO"/>
 *         &lt;element name="via" type="{http://servicios.tributasenasturias.es/Calculo600Generico}viaTypeEO"/>
 *         &lt;element name="viaDesc" type="{http://servicios.tributasenasturias.es/Calculo600Generico}viaDescTypeEO"/>
 *         &lt;element name="nombreVia" type="{http://servicios.tributasenasturias.es/Calculo600Generico}string50TypeO"/>
 *         &lt;element name="codigoPostal" type="{http://servicios.tributasenasturias.es/Calculo600Generico}cifras5TypeO"/>
 *         &lt;element name="numero" type="{http://servicios.tributasenasturias.es/Calculo600Generico}digito5TypeO" minOccurs="0"/>
 *         &lt;element name="escalera" type="{http://servicios.tributasenasturias.es/Calculo600Generico}string2TypeO" minOccurs="0"/>
 *         &lt;element name="piso" type="{http://servicios.tributasenasturias.es/Calculo600Generico}string3TypeO" minOccurs="0"/>
 *         &lt;element name="puerta" type="{http://servicios.tributasenasturias.es/Calculo600Generico}string2TypeO" minOccurs="0"/>
 *         &lt;element name="telefono" type="{http://servicios.tributasenasturias.es/Calculo600Generico}digito14TypeO" minOccurs="0"/>
 *         &lt;element name="particIgual" type="{http://servicios.tributasenasturias.es/Calculo600Generico}boolTypeO" minOccurs="0"/>
 *         &lt;element name="grdoPartic" type="{http://servicios.tributasenasturias.es/Calculo600Generico}porcentajeTypeO" minOccurs="0"/>
 *         &lt;element name="relacion" type="{http://servicios.tributasenasturias.es/Calculo600Generico}tipoRelPresentadorSPEO" minOccurs="0"/>
 *         &lt;element name="fecNacimiento" type="{http://servicios.tributasenasturias.es/Calculo600Generico}fechaTypeO" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="tipo" use="required" type="{http://servicios.tributasenasturias.es/Calculo600Generico}tipoIntervinienteTypeEO" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "intervinienteType", propOrder = {
    "idInterviniente",
    "nombreRazonSocial",
    "pais",
    "paisDesc",
    "localiFueraEsp",
    "provincia",
    "provinciaDesc",
    "municipio",
    "municipioDesc",
    "via",
    "viaDesc",
    "nombreVia",
    "codigoPostal",
    "numero",
    "escalera",
    "piso",
    "puerta",
    "telefono",
    "particIgual",
    "grdoPartic",
    "relacion",
    "fecNacimiento"
})
public class IntervinienteType {

    @XmlElement(required = true)
    protected String idInterviniente;
    @XmlElement(required = true)
    protected String nombreRazonSocial;
    @XmlElement(required = true)
    protected String pais;
    @XmlElement(required = true)
    protected String paisDesc;
    protected String localiFueraEsp;
    @XmlElement(required = true)
    protected String provincia;
    @XmlElement(required = true)
    protected ProvinciaDescTypeEO provinciaDesc;
    @XmlElement(required = true)
    protected String municipio;
    @XmlElement(required = true)
    protected String municipioDesc;
    @XmlElement(required = true)
    protected ViaTypeEO via;
    @XmlElement(required = true)
    protected ViaDescTypeEO viaDesc;
    @XmlElement(required = true)
    protected String nombreVia;
    @XmlElement(required = true)
    protected String codigoPostal;
    protected String numero;
    protected String escalera;
    protected String piso;
    protected String puerta;
    protected String telefono;
    protected String particIgual;
    protected String grdoPartic;
    protected TipoRelPresentadorSPEO relacion;
    protected String fecNacimiento;
    @XmlAttribute(required = true)
    protected TipoIntervinienteTypeEO tipo;

    /**
     * Gets the value of the idInterviniente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdInterviniente() {
        return idInterviniente;
    }

    /**
     * Sets the value of the idInterviniente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdInterviniente(String value) {
        this.idInterviniente = value;
    }

    /**
     * Gets the value of the nombreRazonSocial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreRazonSocial() {
        return nombreRazonSocial;
    }

    /**
     * Sets the value of the nombreRazonSocial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreRazonSocial(String value) {
        this.nombreRazonSocial = value;
    }

    /**
     * Gets the value of the pais property.
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
     * Sets the value of the pais property.
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
     * Gets the value of the paisDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaisDesc() {
        return paisDesc;
    }

    /**
     * Sets the value of the paisDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaisDesc(String value) {
        this.paisDesc = value;
    }

    /**
     * Gets the value of the localiFueraEsp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocaliFueraEsp() {
        return localiFueraEsp;
    }

    /**
     * Sets the value of the localiFueraEsp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocaliFueraEsp(String value) {
        this.localiFueraEsp = value;
    }

    /**
     * Gets the value of the provincia property.
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
     * Sets the value of the provincia property.
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
     * Gets the value of the provinciaDesc property.
     * 
     * @return
     *     possible object is
     *     {@link ProvinciaDescTypeEO }
     *     
     */
    public ProvinciaDescTypeEO getProvinciaDesc() {
        return provinciaDesc;
    }

    /**
     * Sets the value of the provinciaDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProvinciaDescTypeEO }
     *     
     */
    public void setProvinciaDesc(ProvinciaDescTypeEO value) {
        this.provinciaDesc = value;
    }

    /**
     * Gets the value of the municipio property.
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
     * Sets the value of the municipio property.
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
     * Gets the value of the municipioDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMunicipioDesc() {
        return municipioDesc;
    }

    /**
     * Sets the value of the municipioDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMunicipioDesc(String value) {
        this.municipioDesc = value;
    }

    /**
     * Gets the value of the via property.
     * 
     * @return
     *     possible object is
     *     {@link ViaTypeEO }
     *     
     */
    public ViaTypeEO getVia() {
        return via;
    }

    /**
     * Sets the value of the via property.
     * 
     * @param value
     *     allowed object is
     *     {@link ViaTypeEO }
     *     
     */
    public void setVia(ViaTypeEO value) {
        this.via = value;
    }

    /**
     * Gets the value of the viaDesc property.
     * 
     * @return
     *     possible object is
     *     {@link ViaDescTypeEO }
     *     
     */
    public ViaDescTypeEO getViaDesc() {
        return viaDesc;
    }

    /**
     * Sets the value of the viaDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link ViaDescTypeEO }
     *     
     */
    public void setViaDesc(ViaDescTypeEO value) {
        this.viaDesc = value;
    }

    /**
     * Gets the value of the nombreVia property.
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
     * Sets the value of the nombreVia property.
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
     * Gets the value of the codigoPostal property.
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
     * Sets the value of the codigoPostal property.
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
     * Gets the value of the numero property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Sets the value of the numero property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumero(String value) {
        this.numero = value;
    }

    /**
     * Gets the value of the escalera property.
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
     * Sets the value of the escalera property.
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
     * Gets the value of the piso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPiso() {
        return piso;
    }

    /**
     * Sets the value of the piso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPiso(String value) {
        this.piso = value;
    }

    /**
     * Gets the value of the puerta property.
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
     * Sets the value of the puerta property.
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
     * Gets the value of the telefono property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Sets the value of the telefono property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefono(String value) {
        this.telefono = value;
    }

    /**
     * Gets the value of the particIgual property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParticIgual() {
        return particIgual;
    }

    /**
     * Sets the value of the particIgual property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParticIgual(String value) {
        this.particIgual = value;
    }

    /**
     * Gets the value of the grdoPartic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrdoPartic() {
        return grdoPartic;
    }

    /**
     * Sets the value of the grdoPartic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrdoPartic(String value) {
        this.grdoPartic = value;
    }

    /**
     * Gets the value of the relacion property.
     * 
     * @return
     *     possible object is
     *     {@link TipoRelPresentadorSPEO }
     *     
     */
    public TipoRelPresentadorSPEO getRelacion() {
        return relacion;
    }

    /**
     * Sets the value of the relacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoRelPresentadorSPEO }
     *     
     */
    public void setRelacion(TipoRelPresentadorSPEO value) {
        this.relacion = value;
    }

    /**
     * Gets the value of the fecNacimiento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFecNacimiento() {
        return fecNacimiento;
    }

    /**
     * Sets the value of the fecNacimiento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFecNacimiento(String value) {
        this.fecNacimiento = value;
    }

    /**
     * Gets the value of the tipo property.
     * 
     * @return
     *     possible object is
     *     {@link TipoIntervinienteTypeEO }
     *     
     */
    public TipoIntervinienteTypeEO getTipo() {
        return tipo;
    }

    /**
     * Sets the value of the tipo property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoIntervinienteTypeEO }
     *     
     */
    public void setTipo(TipoIntervinienteTypeEO value) {
        this.tipo = value;
    }

}
