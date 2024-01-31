
package es.tributasenasturias.servicios.pago600generico;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for liquidacionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="liquidacionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="valorDeclarado" type="{http://servicios.tributasenasturias.es/Pago600Generico}importe17_2TypeO"/>
 *         &lt;element name="exento" type="{http://servicios.tributasenasturias.es/Pago600Generico}boolTypeO"/>
 *         &lt;element name="exentoProvisional" type="{http://servicios.tributasenasturias.es/Pago600Generico}boolTypeO"/>
 *         &lt;element name="noSujeto" type="{http://servicios.tributasenasturias.es/Pago600Generico}boolTypeO"/>
 *         &lt;element name="prescrito" type="{http://servicios.tributasenasturias.es/Pago600Generico}boolTypeO"/>
 *         &lt;element name="fundamentoLegal" type="{http://servicios.tributasenasturias.es/Pago600Generico}string50TypeO" minOccurs="0"/>
 *         &lt;element name="fundamentoLegalDesc" type="{http://servicios.tributasenasturias.es/Pago600Generico}string50TypeO" minOccurs="0"/>
 *         &lt;element name="liquidacionComplementaria" type="{http://servicios.tributasenasturias.es/Pago600Generico}boolTypeO" minOccurs="0"/>
 *         &lt;element name="fechaPrimeraLiquidacion" type="{http://servicios.tributasenasturias.es/Pago600Generico}fechaTypeO" minOccurs="0"/>
 *         &lt;element name="numJustificantePrimeraLiquidacion" type="{http://servicios.tributasenasturias.es/Pago600Generico}string14TypeO" minOccurs="0"/>
 *         &lt;element name="tipoRenta" type="{http://servicios.tributasenasturias.es/Pago600Generico}tipoRentaTypeEO" minOccurs="0"/>
 *         &lt;element name="importeRenta" type="{http://servicios.tributasenasturias.es/Pago600Generico}importe17_2TypeO" minOccurs="0"/>
 *         &lt;element name="numPeriodosRenta" type="{http://servicios.tributasenasturias.es/Pago600Generico}digito4TypeO" minOccurs="0"/>
 *         &lt;element name="baseImponible" type="{http://servicios.tributasenasturias.es/Pago600Generico}importe17_2TypeO"/>
 *         &lt;element name="reduccion" type="{http://servicios.tributasenasturias.es/Pago600Generico}porcentajeType"/>
 *         &lt;element name="reduccImporte" type="{http://servicios.tributasenasturias.es/Pago600Generico}importe17_2Type"/>
 *         &lt;element name="baseLiquidable" type="{http://servicios.tributasenasturias.es/Pago600Generico}importe17_2TypeO"/>
 *         &lt;element name="tipo" type="{http://servicios.tributasenasturias.es/Pago600Generico}tipoImpositivo600"/>
 *         &lt;element name="cuota" type="{http://servicios.tributasenasturias.es/Pago600Generico}importe17_2TypeO"/>
 *         &lt;element name="reduccionExcesoCuota" type="{http://servicios.tributasenasturias.es/Pago600Generico}importe17_2Type"/>
 *         &lt;element name="cuotaIntAjustada" type="{http://servicios.tributasenasturias.es/Pago600Generico}importe17_2TypeO"/>
 *         &lt;element name="bonificacionCuota" type="{http://servicios.tributasenasturias.es/Pago600Generico}porcentajeType"/>
 *         &lt;element name="bonificacionCuotaImporte" type="{http://servicios.tributasenasturias.es/Pago600Generico}importe17_2Type"/>
 *         &lt;element name="aIngresar" type="{http://servicios.tributasenasturias.es/Pago600Generico}importe17_2TypeO"/>
 *         &lt;element name="recargo" type="{http://servicios.tributasenasturias.es/Pago600Generico}importe17_2Type"/>
 *         &lt;element name="intereses" type="{http://servicios.tributasenasturias.es/Pago600Generico}importe17_2Type"/>
 *         &lt;element name="importeLiquidacionAnterior" type="{http://servicios.tributasenasturias.es/Pago600Generico}importe17_2TypeO" minOccurs="0"/>
 *         &lt;element name="totalIngresar" type="{http://servicios.tributasenasturias.es/Pago600Generico}importe17_2TypeO"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "liquidacionType", propOrder = {
    "valorDeclarado",
    "exento",
    "exentoProvisional",
    "noSujeto",
    "prescrito",
    "fundamentoLegal",
    "fundamentoLegalDesc",
    "liquidacionComplementaria",
    "fechaPrimeraLiquidacion",
    "numJustificantePrimeraLiquidacion",
    "tipoRenta",
    "importeRenta",
    "numPeriodosRenta",
    "baseImponible",
    "reduccion",
    "reduccImporte",
    "baseLiquidable",
    "tipo",
    "cuota",
    "reduccionExcesoCuota",
    "cuotaIntAjustada",
    "bonificacionCuota",
    "bonificacionCuotaImporte",
    "aIngresar",
    "recargo",
    "intereses",
    "importeLiquidacionAnterior",
    "totalIngresar"
})
public class LiquidacionType {

    @XmlElement(required = true)
    protected String valorDeclarado;
    @XmlElement(required = true)
    protected String exento;
    @XmlElement(required = true)
    protected String exentoProvisional;
    @XmlElement(required = true)
    protected String noSujeto;
    @XmlElement(required = true)
    protected String prescrito;
    protected String fundamentoLegal;
    protected String fundamentoLegalDesc;
    protected String liquidacionComplementaria;
    protected String fechaPrimeraLiquidacion;
    protected String numJustificantePrimeraLiquidacion;
    protected TipoRentaTypeEO tipoRenta;
    protected String importeRenta;
    protected String numPeriodosRenta;
    @XmlElement(required = true)
    protected String baseImponible;
    @XmlElement(required = true)
    protected String reduccion;
    @XmlElement(required = true)
    protected String reduccImporte;
    @XmlElement(required = true)
    protected String baseLiquidable;
    @XmlElement(required = true)
    protected String tipo;
    @XmlElement(required = true)
    protected String cuota;
    @XmlElement(required = true)
    protected String reduccionExcesoCuota;
    @XmlElement(required = true)
    protected String cuotaIntAjustada;
    @XmlElement(required = true)
    protected String bonificacionCuota;
    @XmlElement(required = true)
    protected String bonificacionCuotaImporte;
    @XmlElement(required = true)
    protected String aIngresar;
    @XmlElement(required = true)
    protected String recargo;
    @XmlElement(required = true)
    protected String intereses;
    protected String importeLiquidacionAnterior;
    @XmlElement(required = true)
    protected String totalIngresar;

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
     * Gets the value of the exento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExento() {
        return exento;
    }

    /**
     * Sets the value of the exento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExento(String value) {
        this.exento = value;
    }

    /**
     * Gets the value of the exentoProvisional property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExentoProvisional() {
        return exentoProvisional;
    }

    /**
     * Sets the value of the exentoProvisional property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExentoProvisional(String value) {
        this.exentoProvisional = value;
    }

    /**
     * Gets the value of the noSujeto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoSujeto() {
        return noSujeto;
    }

    /**
     * Sets the value of the noSujeto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoSujeto(String value) {
        this.noSujeto = value;
    }

    /**
     * Gets the value of the prescrito property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrescrito() {
        return prescrito;
    }

    /**
     * Sets the value of the prescrito property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrescrito(String value) {
        this.prescrito = value;
    }

    /**
     * Gets the value of the fundamentoLegal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFundamentoLegal() {
        return fundamentoLegal;
    }

    /**
     * Sets the value of the fundamentoLegal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFundamentoLegal(String value) {
        this.fundamentoLegal = value;
    }

    /**
     * Gets the value of the fundamentoLegalDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFundamentoLegalDesc() {
        return fundamentoLegalDesc;
    }

    /**
     * Sets the value of the fundamentoLegalDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFundamentoLegalDesc(String value) {
        this.fundamentoLegalDesc = value;
    }

    /**
     * Gets the value of the liquidacionComplementaria property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLiquidacionComplementaria() {
        return liquidacionComplementaria;
    }

    /**
     * Sets the value of the liquidacionComplementaria property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLiquidacionComplementaria(String value) {
        this.liquidacionComplementaria = value;
    }

    /**
     * Gets the value of the fechaPrimeraLiquidacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaPrimeraLiquidacion() {
        return fechaPrimeraLiquidacion;
    }

    /**
     * Sets the value of the fechaPrimeraLiquidacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaPrimeraLiquidacion(String value) {
        this.fechaPrimeraLiquidacion = value;
    }

    /**
     * Gets the value of the numJustificantePrimeraLiquidacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumJustificantePrimeraLiquidacion() {
        return numJustificantePrimeraLiquidacion;
    }

    /**
     * Sets the value of the numJustificantePrimeraLiquidacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumJustificantePrimeraLiquidacion(String value) {
        this.numJustificantePrimeraLiquidacion = value;
    }

    /**
     * Gets the value of the tipoRenta property.
     * 
     * @return
     *     possible object is
     *     {@link TipoRentaTypeEO }
     *     
     */
    public TipoRentaTypeEO getTipoRenta() {
        return tipoRenta;
    }

    /**
     * Sets the value of the tipoRenta property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoRentaTypeEO }
     *     
     */
    public void setTipoRenta(TipoRentaTypeEO value) {
        this.tipoRenta = value;
    }

    /**
     * Gets the value of the importeRenta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImporteRenta() {
        return importeRenta;
    }

    /**
     * Sets the value of the importeRenta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImporteRenta(String value) {
        this.importeRenta = value;
    }

    /**
     * Gets the value of the numPeriodosRenta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumPeriodosRenta() {
        return numPeriodosRenta;
    }

    /**
     * Sets the value of the numPeriodosRenta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumPeriodosRenta(String value) {
        this.numPeriodosRenta = value;
    }

    /**
     * Gets the value of the baseImponible property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseImponible() {
        return baseImponible;
    }

    /**
     * Sets the value of the baseImponible property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseImponible(String value) {
        this.baseImponible = value;
    }

    /**
     * Gets the value of the reduccion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReduccion() {
        return reduccion;
    }

    /**
     * Sets the value of the reduccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReduccion(String value) {
        this.reduccion = value;
    }

    /**
     * Gets the value of the reduccImporte property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReduccImporte() {
        return reduccImporte;
    }

    /**
     * Sets the value of the reduccImporte property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReduccImporte(String value) {
        this.reduccImporte = value;
    }

    /**
     * Gets the value of the baseLiquidable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseLiquidable() {
        return baseLiquidable;
    }

    /**
     * Sets the value of the baseLiquidable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseLiquidable(String value) {
        this.baseLiquidable = value;
    }

    /**
     * Gets the value of the tipo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Sets the value of the tipo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipo(String value) {
        this.tipo = value;
    }

    /**
     * Gets the value of the cuota property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCuota() {
        return cuota;
    }

    /**
     * Sets the value of the cuota property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCuota(String value) {
        this.cuota = value;
    }

    /**
     * Gets the value of the reduccionExcesoCuota property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReduccionExcesoCuota() {
        return reduccionExcesoCuota;
    }

    /**
     * Sets the value of the reduccionExcesoCuota property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReduccionExcesoCuota(String value) {
        this.reduccionExcesoCuota = value;
    }

    /**
     * Gets the value of the cuotaIntAjustada property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCuotaIntAjustada() {
        return cuotaIntAjustada;
    }

    /**
     * Sets the value of the cuotaIntAjustada property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCuotaIntAjustada(String value) {
        this.cuotaIntAjustada = value;
    }

    /**
     * Gets the value of the bonificacionCuota property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBonificacionCuota() {
        return bonificacionCuota;
    }

    /**
     * Sets the value of the bonificacionCuota property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBonificacionCuota(String value) {
        this.bonificacionCuota = value;
    }

    /**
     * Gets the value of the bonificacionCuotaImporte property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBonificacionCuotaImporte() {
        return bonificacionCuotaImporte;
    }

    /**
     * Sets the value of the bonificacionCuotaImporte property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBonificacionCuotaImporte(String value) {
        this.bonificacionCuotaImporte = value;
    }

    /**
     * Gets the value of the aIngresar property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAIngresar() {
        return aIngresar;
    }

    /**
     * Sets the value of the aIngresar property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAIngresar(String value) {
        this.aIngresar = value;
    }

    /**
     * Gets the value of the recargo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecargo() {
        return recargo;
    }

    /**
     * Sets the value of the recargo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecargo(String value) {
        this.recargo = value;
    }

    /**
     * Gets the value of the intereses property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIntereses() {
        return intereses;
    }

    /**
     * Sets the value of the intereses property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIntereses(String value) {
        this.intereses = value;
    }

    /**
     * Gets the value of the importeLiquidacionAnterior property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImporteLiquidacionAnterior() {
        return importeLiquidacionAnterior;
    }

    /**
     * Sets the value of the importeLiquidacionAnterior property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImporteLiquidacionAnterior(String value) {
        this.importeLiquidacionAnterior = value;
    }

    /**
     * Gets the value of the totalIngresar property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalIngresar() {
        return totalIngresar;
    }

    /**
     * Sets the value of the totalIngresar property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalIngresar(String value) {
        this.totalIngresar = value;
    }

}
