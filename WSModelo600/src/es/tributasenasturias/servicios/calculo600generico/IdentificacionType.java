
package es.tributasenasturias.servicios.calculo600generico;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for identificacionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="identificacionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="numSujetos" type="{http://servicios.tributasenasturias.es/Calculo600Generico}digito2TypeO"/>
 *         &lt;element name="numTransmitentes" type="{http://servicios.tributasenasturias.es/Calculo600Generico}digito2TypeO"/>
 *         &lt;element name="fechaDevengo" type="{http://servicios.tributasenasturias.es/Calculo600Generico}fechaTypeO"/>
 *         &lt;element name="fechaDocumento" type="{http://servicios.tributasenasturias.es/Calculo600Generico}fechaTypeO"/>
 *         &lt;element name="expAbreviada" type="{http://servicios.tributasenasturias.es/Calculo600Generico}expAbreviadaTypeEO"/>
 *         &lt;element name="concepto" type="{http://servicios.tributasenasturias.es/Calculo600Generico}conceptoTypeEO"/>
 *         &lt;element name="datoEspecifico" type="{http://servicios.tributasenasturias.es/Calculo600Generico}datoEspecificoTypeOut"/>
 *         &lt;element name="docNotarial" type="{http://servicios.tributasenasturias.es/Calculo600Generico}boolTypeO"/>
 *         &lt;element name="docJudicial" type="{http://servicios.tributasenasturias.es/Calculo600Generico}boolTypeO"/>
 *         &lt;element name="docPrivado" type="{http://servicios.tributasenasturias.es/Calculo600Generico}boolTypeO"/>
 *         &lt;element name="docAdministrativo" type="{http://servicios.tributasenasturias.es/Calculo600Generico}boolTypeO"/>
 *         &lt;element name="docMercantil" type="{http://servicios.tributasenasturias.es/Calculo600Generico}boolTypeO"/>
 *         &lt;element name="nifNotario" type="{http://servicios.tributasenasturias.es/Calculo600Generico}nifTypeO" minOccurs="0"/>
 *         &lt;element name="nombreNotario" type="{http://servicios.tributasenasturias.es/Calculo600Generico}string80TypeO" minOccurs="0"/>
 *         &lt;element name="apellidosNotario" type="{http://servicios.tributasenasturias.es/Calculo600Generico}string80TypeO" minOccurs="0"/>
 *         &lt;element name="codNotario" type="{http://servicios.tributasenasturias.es/Calculo600Generico}string30TypeOut" minOccurs="0"/>
 *         &lt;element name="codNotaria" type="{http://servicios.tributasenasturias.es/Calculo600Generico}string30TypeOut" minOccurs="0"/>
 *         &lt;element name="provinciaNotario" type="{http://servicios.tributasenasturias.es/Calculo600Generico}provinciaTypeEO" minOccurs="0"/>
 *         &lt;element name="provinciaNotarioDesc" type="{http://servicios.tributasenasturias.es/Calculo600Generico}provinciaDescTypeEO" minOccurs="0"/>
 *         &lt;element name="municipioNotario" type="{http://servicios.tributasenasturias.es/Calculo600Generico}digito5TypeO" minOccurs="0"/>
 *         &lt;element name="municipioNotarioDesc" type="{http://servicios.tributasenasturias.es/Calculo600Generico}string50TypeO" minOccurs="0"/>
 *         &lt;element name="numProtocolo" type="{http://servicios.tributasenasturias.es/Calculo600Generico}digito13TypeO" minOccurs="0"/>
 *         &lt;element name="numProtocoloBis" type="{http://servicios.tributasenasturias.es/Calculo600Generico}digito4TypeO" minOccurs="0"/>
 *         &lt;element name="identificacionAdicional" type="{http://servicios.tributasenasturias.es/Calculo600Generico}string70TypeO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "identificacionType", propOrder = {
    "numSujetos",
    "numTransmitentes",
    "fechaDevengo",
    "fechaDocumento",
    "expAbreviada",
    "concepto",
    "datoEspecifico",
    "docNotarial",
    "docJudicial",
    "docPrivado",
    "docAdministrativo",
    "docMercantil",
    "nifNotario",
    "nombreNotario",
    "apellidosNotario",
    "codNotario",
    "codNotaria",
    "provinciaNotario",
    "provinciaNotarioDesc",
    "municipioNotario",
    "municipioNotarioDesc",
    "numProtocolo",
    "numProtocoloBis",
    "identificacionAdicional"
})
public class IdentificacionType {

    @XmlElement(required = true)
    protected String numSujetos;
    @XmlElement(required = true)
    protected String numTransmitentes;
    @XmlElement(required = true)
    protected String fechaDevengo;
    @XmlElement(required = true)
    protected String fechaDocumento;
    @XmlElement(required = true)
    protected ExpAbreviadaTypeEO expAbreviada;
    @XmlElement(required = true)
    protected ConceptoTypeEO concepto;
    @XmlElement(required = true)
    protected String datoEspecifico;
    @XmlElement(required = true)
    protected String docNotarial;
    @XmlElement(required = true)
    protected String docJudicial;
    @XmlElement(required = true)
    protected String docPrivado;
    @XmlElement(required = true)
    protected String docAdministrativo;
    @XmlElement(required = true)
    protected String docMercantil;
    protected String nifNotario;
    protected String nombreNotario;
    protected String apellidosNotario;
    protected String codNotario;
    protected String codNotaria;
    protected String provinciaNotario;
    protected ProvinciaDescTypeEO provinciaNotarioDesc;
    protected String municipioNotario;
    protected String municipioNotarioDesc;
    protected String numProtocolo;
    protected String numProtocoloBis;
    protected String identificacionAdicional;

    /**
     * Gets the value of the numSujetos property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumSujetos() {
        return numSujetos;
    }

    /**
     * Sets the value of the numSujetos property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumSujetos(String value) {
        this.numSujetos = value;
    }

    /**
     * Gets the value of the numTransmitentes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumTransmitentes() {
        return numTransmitentes;
    }

    /**
     * Sets the value of the numTransmitentes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumTransmitentes(String value) {
        this.numTransmitentes = value;
    }

    /**
     * Gets the value of the fechaDevengo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaDevengo() {
        return fechaDevengo;
    }

    /**
     * Sets the value of the fechaDevengo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaDevengo(String value) {
        this.fechaDevengo = value;
    }

    /**
     * Gets the value of the fechaDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaDocumento() {
        return fechaDocumento;
    }

    /**
     * Sets the value of the fechaDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaDocumento(String value) {
        this.fechaDocumento = value;
    }

    /**
     * Gets the value of the expAbreviada property.
     * 
     * @return
     *     possible object is
     *     {@link ExpAbreviadaTypeEO }
     *     
     */
    public ExpAbreviadaTypeEO getExpAbreviada() {
        return expAbreviada;
    }

    /**
     * Sets the value of the expAbreviada property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExpAbreviadaTypeEO }
     *     
     */
    public void setExpAbreviada(ExpAbreviadaTypeEO value) {
        this.expAbreviada = value;
    }

    /**
     * Gets the value of the concepto property.
     * 
     * @return
     *     possible object is
     *     {@link ConceptoTypeEO }
     *     
     */
    public ConceptoTypeEO getConcepto() {
        return concepto;
    }

    /**
     * Sets the value of the concepto property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConceptoTypeEO }
     *     
     */
    public void setConcepto(ConceptoTypeEO value) {
        this.concepto = value;
    }

    /**
     * Gets the value of the datoEspecifico property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatoEspecifico() {
        return datoEspecifico;
    }

    /**
     * Sets the value of the datoEspecifico property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatoEspecifico(String value) {
        this.datoEspecifico = value;
    }

    /**
     * Gets the value of the docNotarial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocNotarial() {
        return docNotarial;
    }

    /**
     * Sets the value of the docNotarial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocNotarial(String value) {
        this.docNotarial = value;
    }

    /**
     * Gets the value of the docJudicial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocJudicial() {
        return docJudicial;
    }

    /**
     * Sets the value of the docJudicial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocJudicial(String value) {
        this.docJudicial = value;
    }

    /**
     * Gets the value of the docPrivado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocPrivado() {
        return docPrivado;
    }

    /**
     * Sets the value of the docPrivado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocPrivado(String value) {
        this.docPrivado = value;
    }

    /**
     * Gets the value of the docAdministrativo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocAdministrativo() {
        return docAdministrativo;
    }

    /**
     * Sets the value of the docAdministrativo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocAdministrativo(String value) {
        this.docAdministrativo = value;
    }

    /**
     * Gets the value of the docMercantil property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocMercantil() {
        return docMercantil;
    }

    /**
     * Sets the value of the docMercantil property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocMercantil(String value) {
        this.docMercantil = value;
    }

    /**
     * Gets the value of the nifNotario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNifNotario() {
        return nifNotario;
    }

    /**
     * Sets the value of the nifNotario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNifNotario(String value) {
        this.nifNotario = value;
    }

    /**
     * Gets the value of the nombreNotario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreNotario() {
        return nombreNotario;
    }

    /**
     * Sets the value of the nombreNotario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreNotario(String value) {
        this.nombreNotario = value;
    }

    /**
     * Gets the value of the apellidosNotario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApellidosNotario() {
        return apellidosNotario;
    }

    /**
     * Sets the value of the apellidosNotario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApellidosNotario(String value) {
        this.apellidosNotario = value;
    }

    /**
     * Gets the value of the codNotario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodNotario() {
        return codNotario;
    }

    /**
     * Sets the value of the codNotario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodNotario(String value) {
        this.codNotario = value;
    }

    /**
     * Gets the value of the codNotaria property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodNotaria() {
        return codNotaria;
    }

    /**
     * Sets the value of the codNotaria property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodNotaria(String value) {
        this.codNotaria = value;
    }

    /**
     * Gets the value of the provinciaNotario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvinciaNotario() {
        return provinciaNotario;
    }

    /**
     * Sets the value of the provinciaNotario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvinciaNotario(String value) {
        this.provinciaNotario = value;
    }

    /**
     * Gets the value of the provinciaNotarioDesc property.
     * 
     * @return
     *     possible object is
     *     {@link ProvinciaDescTypeEO }
     *     
     */
    public ProvinciaDescTypeEO getProvinciaNotarioDesc() {
        return provinciaNotarioDesc;
    }

    /**
     * Sets the value of the provinciaNotarioDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProvinciaDescTypeEO }
     *     
     */
    public void setProvinciaNotarioDesc(ProvinciaDescTypeEO value) {
        this.provinciaNotarioDesc = value;
    }

    /**
     * Gets the value of the municipioNotario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMunicipioNotario() {
        return municipioNotario;
    }

    /**
     * Sets the value of the municipioNotario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMunicipioNotario(String value) {
        this.municipioNotario = value;
    }

    /**
     * Gets the value of the municipioNotarioDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMunicipioNotarioDesc() {
        return municipioNotarioDesc;
    }

    /**
     * Sets the value of the municipioNotarioDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMunicipioNotarioDesc(String value) {
        this.municipioNotarioDesc = value;
    }

    /**
     * Gets the value of the numProtocolo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumProtocolo() {
        return numProtocolo;
    }

    /**
     * Sets the value of the numProtocolo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumProtocolo(String value) {
        this.numProtocolo = value;
    }

    /**
     * Gets the value of the numProtocoloBis property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumProtocoloBis() {
        return numProtocoloBis;
    }

    /**
     * Sets the value of the numProtocoloBis property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumProtocoloBis(String value) {
        this.numProtocoloBis = value;
    }

    /**
     * Gets the value of the identificacionAdicional property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificacionAdicional() {
        return identificacionAdicional;
    }

    /**
     * Sets the value of the identificacionAdicional property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificacionAdicional(String value) {
        this.identificacionAdicional = value;
    }

}
