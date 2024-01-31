
package es.tributasenasturias.servicios.calculo600generico;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for bienRusticoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="bienRusticoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idBien" type="{http://servicios.tributasenasturias.es/Calculo600Generico}string5TypeO" minOccurs="0"/>
 *         &lt;element name="transmision" type="{http://servicios.tributasenasturias.es/Calculo600Generico}porcentajeTypeO"/>
 *         &lt;element name="caracter" type="{http://servicios.tributasenasturias.es/Calculo600Generico}caracterTypeEO"/>
 *         &lt;element name="claveAdquisicion" type="{http://servicios.tributasenasturias.es/Calculo600Generico}claveAdqTypeEO"/>
 *         &lt;element name="claveAdquisicionDesc" type="{http://servicios.tributasenasturias.es/Calculo600Generico}claveAdqDescTypeEO"/>
 *         &lt;element name="provincia" type="{http://servicios.tributasenasturias.es/Calculo600Generico}provinciaTypeEO"/>
 *         &lt;element name="provinciaDesc" type="{http://servicios.tributasenasturias.es/Calculo600Generico}provinciaDescTypeEO"/>
 *         &lt;element name="municipio" type="{http://servicios.tributasenasturias.es/Calculo600Generico}digito5TypeO"/>
 *         &lt;element name="municipioDesc" type="{http://servicios.tributasenasturias.es/Calculo600Generico}string50TypeO"/>
 *         &lt;element name="situacion" type="{http://servicios.tributasenasturias.es/Calculo600Generico}string60TypeO" minOccurs="0"/>
 *         &lt;element name="parroquia" type="{http://servicios.tributasenasturias.es/Calculo600Generico}string60TypeO" minOccurs="0"/>
 *         &lt;element name="poligono" type="{http://servicios.tributasenasturias.es/Calculo600Generico}digito3TypeO" minOccurs="0"/>
 *         &lt;element name="parcela" type="{http://servicios.tributasenasturias.es/Calculo600Generico}digito5TypeO" minOccurs="0"/>
 *         &lt;element name="tipoCultivo" type="{http://servicios.tributasenasturias.es/Calculo600Generico}cultivoTypeEO" minOccurs="0"/>
 *         &lt;element name="tipoCultivoDesc" type="{http://servicios.tributasenasturias.es/Calculo600Generico}cultivoDescTypeEO" minOccurs="0"/>
 *         &lt;element name="superficie" type="{http://servicios.tributasenasturias.es/Calculo600Generico}importe17_2TypeO" minOccurs="0"/>
 *         &lt;element name="referenciaCatastral" type="{http://servicios.tributasenasturias.es/Calculo600Generico}string20TypeO" minOccurs="0"/>
 *         &lt;element name="valorDeclarado" type="{http://servicios.tributasenasturias.es/Calculo600Generico}importe17_2TypeO"/>
 *         &lt;element name="usufTemporal" type="{http://servicios.tributasenasturias.es/Calculo600Generico}boolTypeO" minOccurs="0"/>
 *         &lt;element name="anyosUsufTemporal" type="{http://servicios.tributasenasturias.es/Calculo600Generico}digito5_2TypeO" minOccurs="0"/>
 *         &lt;element name="usufVitalicio" type="{http://servicios.tributasenasturias.es/Calculo600Generico}boolTypeO" minOccurs="0"/>
 *         &lt;element name="fechaNacUsufructuario" type="{http://servicios.tributasenasturias.es/Calculo600Generico}fechaTypeO" minOccurs="0"/>
 *         &lt;element name="corrientePagoIBI" type="{http://servicios.tributasenasturias.es/Calculo600Generico}importe17_2TypeO" minOccurs="0"/>
 *         &lt;element name="tipoImpositivo" type="{http://servicios.tributasenasturias.es/Calculo600Generico}porcentajeTypeOut" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bienRusticoType", propOrder = {
    "idBien",
    "transmision",
    "caracter",
    "claveAdquisicion",
    "claveAdquisicionDesc",
    "provincia",
    "provinciaDesc",
    "municipio",
    "municipioDesc",
    "situacion",
    "parroquia",
    "poligono",
    "parcela",
    "tipoCultivo",
    "tipoCultivoDesc",
    "superficie",
    "referenciaCatastral",
    "valorDeclarado",
    "usufTemporal",
    "anyosUsufTemporal",
    "usufVitalicio",
    "fechaNacUsufructuario",
    "corrientePagoIBI",
    "tipoImpositivo"
})
public class BienRusticoType {

    protected String idBien;
    @XmlElement(required = true)
    protected String transmision;
    @XmlElement(required = true)
    protected CaracterTypeEO caracter;
    @XmlElement(required = true)
    protected ClaveAdqTypeEO claveAdquisicion;
    @XmlElement(required = true)
    protected ClaveAdqDescTypeEO claveAdquisicionDesc;
    @XmlElement(required = true)
    protected String provincia;
    @XmlElement(required = true)
    protected ProvinciaDescTypeEO provinciaDesc;
    @XmlElement(required = true)
    protected String municipio;
    @XmlElement(required = true)
    protected String municipioDesc;
    protected String situacion;
    protected String parroquia;
    protected String poligono;
    protected String parcela;
    protected CultivoTypeEO tipoCultivo;
    protected CultivoDescTypeEO tipoCultivoDesc;
    protected String superficie;
    protected String referenciaCatastral;
    @XmlElement(required = true)
    protected String valorDeclarado;
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
     * Gets the value of the situacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSituacion() {
        return situacion;
    }

    /**
     * Sets the value of the situacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSituacion(String value) {
        this.situacion = value;
    }

    /**
     * Gets the value of the parroquia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParroquia() {
        return parroquia;
    }

    /**
     * Sets the value of the parroquia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParroquia(String value) {
        this.parroquia = value;
    }

    /**
     * Gets the value of the poligono property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoligono() {
        return poligono;
    }

    /**
     * Sets the value of the poligono property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoligono(String value) {
        this.poligono = value;
    }

    /**
     * Gets the value of the parcela property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParcela() {
        return parcela;
    }

    /**
     * Sets the value of the parcela property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParcela(String value) {
        this.parcela = value;
    }

    /**
     * Gets the value of the tipoCultivo property.
     * 
     * @return
     *     possible object is
     *     {@link CultivoTypeEO }
     *     
     */
    public CultivoTypeEO getTipoCultivo() {
        return tipoCultivo;
    }

    /**
     * Sets the value of the tipoCultivo property.
     * 
     * @param value
     *     allowed object is
     *     {@link CultivoTypeEO }
     *     
     */
    public void setTipoCultivo(CultivoTypeEO value) {
        this.tipoCultivo = value;
    }

    /**
     * Gets the value of the tipoCultivoDesc property.
     * 
     * @return
     *     possible object is
     *     {@link CultivoDescTypeEO }
     *     
     */
    public CultivoDescTypeEO getTipoCultivoDesc() {
        return tipoCultivoDesc;
    }

    /**
     * Sets the value of the tipoCultivoDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link CultivoDescTypeEO }
     *     
     */
    public void setTipoCultivoDesc(CultivoDescTypeEO value) {
        this.tipoCultivoDesc = value;
    }

    /**
     * Gets the value of the superficie property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSuperficie() {
        return superficie;
    }

    /**
     * Sets the value of the superficie property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSuperficie(String value) {
        this.superficie = value;
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
     * Gets the value of the valorDeclarado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValorDeclarado() {
        return valorDeclarado;
    }

    /**
     * Sets the value of the valorDeclarado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValorDeclarado(String value) {
        this.valorDeclarado = value;
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
