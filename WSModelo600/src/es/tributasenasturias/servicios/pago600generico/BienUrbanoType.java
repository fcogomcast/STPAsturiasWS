
package es.tributasenasturias.servicios.pago600generico;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for bienUrbanoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="bienUrbanoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idBien" type="{http://servicios.tributasenasturias.es/Pago600Generico}string5TypeO" minOccurs="0"/>
 *         &lt;element name="transmision" type="{http://servicios.tributasenasturias.es/Pago600Generico}porcentajeTypeO"/>
 *         &lt;element name="caracter" type="{http://servicios.tributasenasturias.es/Pago600Generico}caracterTypeEO"/>
 *         &lt;element name="tipoDesc" type="{http://servicios.tributasenasturias.es/Pago600Generico}tipoUrbanoDescTypeEO"/>
 *         &lt;element name="claveAdquisicion" type="{http://servicios.tributasenasturias.es/Pago600Generico}claveAdqTypeEO"/>
 *         &lt;element name="claveAdquisicionDesc" type="{http://servicios.tributasenasturias.es/Pago600Generico}claveAdqDescTypeEO"/>
 *         &lt;element name="via" type="{http://servicios.tributasenasturias.es/Pago600Generico}viaTypeEO"/>
 *         &lt;element name="viaDesc" type="{http://servicios.tributasenasturias.es/Pago600Generico}viaDescTypeEO"/>
 *         &lt;element name="nombreVia" type="{http://servicios.tributasenasturias.es/Pago600Generico}string50TypeO"/>
 *         &lt;element name="numero" type="{http://servicios.tributasenasturias.es/Pago600Generico}digito5TypeO" minOccurs="0"/>
 *         &lt;element name="escalera" type="{http://servicios.tributasenasturias.es/Pago600Generico}string2TypeO" minOccurs="0"/>
 *         &lt;element name="piso" type="{http://servicios.tributasenasturias.es/Pago600Generico}string3TypeO" minOccurs="0"/>
 *         &lt;element name="puerta" type="{http://servicios.tributasenasturias.es/Pago600Generico}string2TypeO" minOccurs="0"/>
 *         &lt;element name="provincia" type="{http://servicios.tributasenasturias.es/Pago600Generico}provinciaTypeEO"/>
 *         &lt;element name="provinciaDesc" type="{http://servicios.tributasenasturias.es/Pago600Generico}provinciaDescTypeEO"/>
 *         &lt;element name="municipio" type="{http://servicios.tributasenasturias.es/Pago600Generico}digito5TypeO"/>
 *         &lt;element name="municipioDesc" type="{http://servicios.tributasenasturias.es/Pago600Generico}string50TypeO"/>
 *         &lt;element name="codigoPostal" type="{http://servicios.tributasenasturias.es/Pago600Generico}cifras5TypeO"/>
 *         &lt;element name="superficieConstruida" type="{http://servicios.tributasenasturias.es/Pago600Generico}importe17_2TypeO" minOccurs="0"/>
 *         &lt;element name="anyoConstruccion" type="{http://servicios.tributasenasturias.es/Pago600Generico}digito4TypeO" minOccurs="0"/>
 *         &lt;element name="referenciaCatastral" type="{http://servicios.tributasenasturias.es/Pago600Generico}string20TypeO" minOccurs="0"/>
 *         &lt;element name="VPO" type="{http://servicios.tributasenasturias.es/Pago600Generico}boolTypeO"/>
 *         &lt;element name="fechaAdquisicion" type="{http://servicios.tributasenasturias.es/Pago600Generico}fechaTypeO" minOccurs="0"/>
 *         &lt;element name="variasFechas" type="{http://servicios.tributasenasturias.es/Pago600Generico}boolTypeO" minOccurs="0"/>
 *         &lt;element name="valorBien" type="{http://servicios.tributasenasturias.es/Pago600Generico}importe17_2TypeO"/>
 *         &lt;element name="usufTemporal" type="{http://servicios.tributasenasturias.es/Pago600Generico}boolTypeO" minOccurs="0"/>
 *         &lt;element name="anyosUsufTemporal" type="{http://servicios.tributasenasturias.es/Pago600Generico}digito5_2TypeO" minOccurs="0"/>
 *         &lt;element name="usufVitalicio" type="{http://servicios.tributasenasturias.es/Pago600Generico}boolTypeO" minOccurs="0"/>
 *         &lt;element name="fechaNacUsufructuario" type="{http://servicios.tributasenasturias.es/Pago600Generico}fechaTypeO" minOccurs="0"/>
 *         &lt;element name="corrientePagoIBI" type="{http://servicios.tributasenasturias.es/Pago600Generico}importe17_2TypeO" minOccurs="0"/>
 *         &lt;element name="tipoImpositivo" type="{http://servicios.tributasenasturias.es/Pago600Generico}porcentajeTypeO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bienUrbanoType", propOrder = {
    "idBien",
    "transmision",
    "caracter",
    "tipoDesc",
    "claveAdquisicion",
    "claveAdquisicionDesc",
    "via",
    "viaDesc",
    "nombreVia",
    "numero",
    "escalera",
    "piso",
    "puerta",
    "provincia",
    "provinciaDesc",
    "municipio",
    "municipioDesc",
    "codigoPostal",
    "superficieConstruida",
    "anyoConstruccion",
    "referenciaCatastral",
    "vpo",
    "fechaAdquisicion",
    "variasFechas",
    "valorBien",
    "usufTemporal",
    "anyosUsufTemporal",
    "usufVitalicio",
    "fechaNacUsufructuario",
    "corrientePagoIBI",
    "tipoImpositivo"
})
public class BienUrbanoType {

    protected String idBien;
    @XmlElement(required = true)
    protected String transmision;
    @XmlElement(required = true)
    protected CaracterTypeEO caracter;
    @XmlElement(required = true)
    protected TipoUrbanoDescTypeEO tipoDesc;
    @XmlElement(required = true)
    protected ClaveAdqTypeEO claveAdquisicion;
    @XmlElement(required = true)
    protected ClaveAdqDescTypeEO claveAdquisicionDesc;
    @XmlElement(required = true)
    protected ViaTypeEO via;
    @XmlElement(required = true)
    protected ViaDescTypeEO viaDesc;
    @XmlElement(required = true)
    protected String nombreVia;
    protected String numero;
    protected String escalera;
    protected String piso;
    protected String puerta;
    @XmlElement(required = true)
    protected String provincia;
    @XmlElement(required = true)
    protected ProvinciaDescTypeEO provinciaDesc;
    @XmlElement(required = true)
    protected String municipio;
    @XmlElement(required = true)
    protected String municipioDesc;
    @XmlElement(required = true)
    protected String codigoPostal;
    protected String superficieConstruida;
    protected String anyoConstruccion;
    protected String referenciaCatastral;
    @XmlElement(name = "VPO", required = true)
    protected String vpo;
    protected String fechaAdquisicion;
    protected String variasFechas;
    @XmlElement(required = true)
    protected String valorBien;
    protected String usufTemporal;
    protected String anyosUsufTemporal;
    protected String usufVitalicio;
    protected String fechaNacUsufructuario;
    protected String corrientePagoIBI;
    protected String tipoImpositivo;

    /**
     * Gets the value of the idBien property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdBien() {
        return idBien;
    }

    /**
     * Sets the value of the idBien property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdBien(String value) {
        this.idBien = value;
    }

    /**
     * Gets the value of the transmision property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransmision() {
        return transmision;
    }

    /**
     * Sets the value of the transmision property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransmision(String value) {
        this.transmision = value;
    }

    /**
     * Gets the value of the caracter property.
     * 
     * @return
     *     possible object is
     *     {@link CaracterTypeEO }
     *     
     */
    public CaracterTypeEO getCaracter() {
        return caracter;
    }

    /**
     * Sets the value of the caracter property.
     * 
     * @param value
     *     allowed object is
     *     {@link CaracterTypeEO }
     *     
     */
    public void setCaracter(CaracterTypeEO value) {
        this.caracter = value;
    }

    /**
     * Gets the value of the tipoDesc property.
     * 
     * @return
     *     possible object is
     *     {@link TipoUrbanoDescTypeEO }
     *     
     */
    public TipoUrbanoDescTypeEO getTipoDesc() {
        return tipoDesc;
    }

    /**
     * Sets the value of the tipoDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoUrbanoDescTypeEO }
     *     
     */
    public void setTipoDesc(TipoUrbanoDescTypeEO value) {
        this.tipoDesc = value;
    }

    /**
     * Gets the value of the claveAdquisicion property.
     * 
     * @return
     *     possible object is
     *     {@link ClaveAdqTypeEO }
     *     
     */
    public ClaveAdqTypeEO getClaveAdquisicion() {
        return claveAdquisicion;
    }

    /**
     * Sets the value of the claveAdquisicion property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClaveAdqTypeEO }
     *     
     */
    public void setClaveAdquisicion(ClaveAdqTypeEO value) {
        this.claveAdquisicion = value;
    }

    /**
     * Gets the value of the claveAdquisicionDesc property.
     * 
     * @return
     *     possible object is
     *     {@link ClaveAdqDescTypeEO }
     *     
     */
    public ClaveAdqDescTypeEO getClaveAdquisicionDesc() {
        return claveAdquisicionDesc;
    }

    /**
     * Sets the value of the claveAdquisicionDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClaveAdqDescTypeEO }
     *     
     */
    public void setClaveAdquisicionDesc(ClaveAdqDescTypeEO value) {
        this.claveAdquisicionDesc = value;
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
     * Gets the value of the superficieConstruida property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSuperficieConstruida() {
        return superficieConstruida;
    }

    /**
     * Sets the value of the superficieConstruida property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSuperficieConstruida(String value) {
        this.superficieConstruida = value;
    }

    /**
     * Gets the value of the anyoConstruccion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnyoConstruccion() {
        return anyoConstruccion;
    }

    /**
     * Sets the value of the anyoConstruccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnyoConstruccion(String value) {
        this.anyoConstruccion = value;
    }

    /**
     * Gets the value of the referenciaCatastral property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenciaCatastral() {
        return referenciaCatastral;
    }

    /**
     * Sets the value of the referenciaCatastral property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenciaCatastral(String value) {
        this.referenciaCatastral = value;
    }

    /**
     * Gets the value of the vpo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVPO() {
        return vpo;
    }

    /**
     * Sets the value of the vpo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVPO(String value) {
        this.vpo = value;
    }

    /**
     * Gets the value of the fechaAdquisicion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    /**
     * Sets the value of the fechaAdquisicion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaAdquisicion(String value) {
        this.fechaAdquisicion = value;
    }

    /**
     * Gets the value of the variasFechas property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVariasFechas() {
        return variasFechas;
    }

    /**
     * Sets the value of the variasFechas property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVariasFechas(String value) {
        this.variasFechas = value;
    }

    /**
     * Gets the value of the valorBien property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValorBien() {
        return valorBien;
    }

    /**
     * Sets the value of the valorBien property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValorBien(String value) {
        this.valorBien = value;
    }

    /**
     * Gets the value of the usufTemporal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsufTemporal() {
        return usufTemporal;
    }

    /**
     * Sets the value of the usufTemporal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsufTemporal(String value) {
        this.usufTemporal = value;
    }

    /**
     * Gets the value of the anyosUsufTemporal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnyosUsufTemporal() {
        return anyosUsufTemporal;
    }

    /**
     * Sets the value of the anyosUsufTemporal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnyosUsufTemporal(String value) {
        this.anyosUsufTemporal = value;
    }

    /**
     * Gets the value of the usufVitalicio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsufVitalicio() {
        return usufVitalicio;
    }

    /**
     * Sets the value of the usufVitalicio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsufVitalicio(String value) {
        this.usufVitalicio = value;
    }

    /**
     * Gets the value of the fechaNacUsufructuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaNacUsufructuario() {
        return fechaNacUsufructuario;
    }

    /**
     * Sets the value of the fechaNacUsufructuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaNacUsufructuario(String value) {
        this.fechaNacUsufructuario = value;
    }

    /**
     * Gets the value of the corrientePagoIBI property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorrientePagoIBI() {
        return corrientePagoIBI;
    }

    /**
     * Sets the value of the corrientePagoIBI property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorrientePagoIBI(String value) {
        this.corrientePagoIBI = value;
    }

    /**
     * Gets the value of the tipoImpositivo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoImpositivo() {
        return tipoImpositivo;
    }

    /**
     * Sets the value of the tipoImpositivo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoImpositivo(String value) {
        this.tipoImpositivo = value;
    }

}
