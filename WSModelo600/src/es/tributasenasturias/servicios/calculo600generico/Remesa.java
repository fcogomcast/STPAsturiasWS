
package es.tributasenasturias.servicios.calculo600generico;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.w3._2000._09.xmldsig_.SignatureType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="declaracion">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="identificacion" type="{http://servicios.tributasenasturias.es/Calculo600Generico}identificacionType"/>
 *                   &lt;element name="listaIntervinientes">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="interviniente" type="{http://servicios.tributasenasturias.es/Calculo600Generico}intervinienteType" maxOccurs="199"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="listaBienesUrbanos" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="bienUrbano" type="{http://servicios.tributasenasturias.es/Calculo600Generico}bienUrbanoType" maxOccurs="unbounded"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="listaBienesRusticos" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="bienRustico" type="{http://servicios.tributasenasturias.es/Calculo600Generico}bienRusticoType" maxOccurs="unbounded"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="liquidacion" type="{http://servicios.tributasenasturias.es/Calculo600Generico}liquidacionType"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" use="required" type="{http://servicios.tributasenasturias.es/Calculo600Generico}idFirmaTypeO" />
 *                 &lt;attribute name="codigo_declaracion" type="{http://servicios.tributasenasturias.es/Calculo600Generico}cifras13TypeOut" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="resultado" type="{http://servicios.tributasenasturias.es/Calculo600Generico}resultadoTypeOut" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="fecha_emision" use="required" type="{http://servicios.tributasenasturias.es/Calculo600Generico}fechaTypeO" />
 *       &lt;attribute name="version_xsd" use="required" type="{http://servicios.tributasenasturias.es/Calculo600Generico}versionTypeO" />
 *       &lt;attribute name="version_aux" type="{http://servicios.tributasenasturias.es/Calculo600Generico}cifras2TypeO" />
 *       &lt;attribute name="codigo_remesa" type="{http://servicios.tributasenasturias.es/Calculo600Generico}cifras5TypeO" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "declaracion",
    "resultado",
    "signature"
})
@XmlRootElement(name = "remesa")
public class Remesa {

    @XmlElement(required = true)
    protected Remesa.Declaracion declaracion;
    protected ResultadoTypeOut resultado;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected SignatureType signature;
    @XmlAttribute(name = "fecha_emision", required = true)
    protected String fechaEmision;
    @XmlAttribute(name = "version_xsd", required = true)
    protected String versionXsd;
    @XmlAttribute(name = "version_aux")
    protected String versionAux;
    @XmlAttribute(name = "codigo_remesa")
    protected String codigoRemesa;

    /**
     * Gets the value of the declaracion property.
     * 
     * @return
     *     possible object is
     *     {@link Remesa.Declaracion }
     *     
     */
    public Remesa.Declaracion getDeclaracion() {
        return declaracion;
    }

    /**
     * Sets the value of the declaracion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Remesa.Declaracion }
     *     
     */
    public void setDeclaracion(Remesa.Declaracion value) {
        this.declaracion = value;
    }

    /**
     * Gets the value of the resultado property.
     * 
     * @return
     *     possible object is
     *     {@link ResultadoTypeOut }
     *     
     */
    public ResultadoTypeOut getResultado() {
        return resultado;
    }

    /**
     * Sets the value of the resultado property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultadoTypeOut }
     *     
     */
    public void setResultado(ResultadoTypeOut value) {
        this.resultado = value;
    }

    /**
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link SignatureType }
     *     
     */
    public SignatureType getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureType }
     *     
     */
    public void setSignature(SignatureType value) {
        this.signature = value;
    }

    /**
     * Gets the value of the fechaEmision property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaEmision() {
        return fechaEmision;
    }

    /**
     * Sets the value of the fechaEmision property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaEmision(String value) {
        this.fechaEmision = value;
    }

    /**
     * Gets the value of the versionXsd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionXsd() {
        return versionXsd;
    }

    /**
     * Sets the value of the versionXsd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionXsd(String value) {
        this.versionXsd = value;
    }

    /**
     * Gets the value of the versionAux property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionAux() {
        return versionAux;
    }

    /**
     * Sets the value of the versionAux property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionAux(String value) {
        this.versionAux = value;
    }

    /**
     * Gets the value of the codigoRemesa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoRemesa() {
        return codigoRemesa;
    }

    /**
     * Sets the value of the codigoRemesa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoRemesa(String value) {
        this.codigoRemesa = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="identificacion" type="{http://servicios.tributasenasturias.es/Calculo600Generico}identificacionType"/>
     *         &lt;element name="listaIntervinientes">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="interviniente" type="{http://servicios.tributasenasturias.es/Calculo600Generico}intervinienteType" maxOccurs="199"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="listaBienesUrbanos" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="bienUrbano" type="{http://servicios.tributasenasturias.es/Calculo600Generico}bienUrbanoType" maxOccurs="unbounded"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="listaBienesRusticos" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="bienRustico" type="{http://servicios.tributasenasturias.es/Calculo600Generico}bienRusticoType" maxOccurs="unbounded"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="liquidacion" type="{http://servicios.tributasenasturias.es/Calculo600Generico}liquidacionType"/>
     *       &lt;/sequence>
     *       &lt;attribute name="id" use="required" type="{http://servicios.tributasenasturias.es/Calculo600Generico}idFirmaTypeO" />
     *       &lt;attribute name="codigo_declaracion" type="{http://servicios.tributasenasturias.es/Calculo600Generico}cifras13TypeOut" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "identificacion",
        "listaIntervinientes",
        "listaBienesUrbanos",
        "listaBienesRusticos",
        "liquidacion"
    })
    public static class Declaracion {

        @XmlElement(required = true)
        protected IdentificacionType identificacion;
        @XmlElement(required = true)
        protected Remesa.Declaracion.ListaIntervinientes listaIntervinientes;
        protected Remesa.Declaracion.ListaBienesUrbanos listaBienesUrbanos;
        protected Remesa.Declaracion.ListaBienesRusticos listaBienesRusticos;
        @XmlElement(required = true)
        protected LiquidacionType liquidacion;
        @XmlAttribute(required = true)
        protected String id;
        @XmlAttribute(name = "codigo_declaracion")
        protected String codigoDeclaracion;

        /**
         * Gets the value of the identificacion property.
         * 
         * @return
         *     possible object is
         *     {@link IdentificacionType }
         *     
         */
        public IdentificacionType getIdentificacion() {
            return identificacion;
        }

        /**
         * Sets the value of the identificacion property.
         * 
         * @param value
         *     allowed object is
         *     {@link IdentificacionType }
         *     
         */
        public void setIdentificacion(IdentificacionType value) {
            this.identificacion = value;
        }

        /**
         * Gets the value of the listaIntervinientes property.
         * 
         * @return
         *     possible object is
         *     {@link Remesa.Declaracion.ListaIntervinientes }
         *     
         */
        public Remesa.Declaracion.ListaIntervinientes getListaIntervinientes() {
            return listaIntervinientes;
        }

        /**
         * Sets the value of the listaIntervinientes property.
         * 
         * @param value
         *     allowed object is
         *     {@link Remesa.Declaracion.ListaIntervinientes }
         *     
         */
        public void setListaIntervinientes(Remesa.Declaracion.ListaIntervinientes value) {
            this.listaIntervinientes = value;
        }

        /**
         * Gets the value of the listaBienesUrbanos property.
         * 
         * @return
         *     possible object is
         *     {@link Remesa.Declaracion.ListaBienesUrbanos }
         *     
         */
        public Remesa.Declaracion.ListaBienesUrbanos getListaBienesUrbanos() {
            return listaBienesUrbanos;
        }

        /**
         * Sets the value of the listaBienesUrbanos property.
         * 
         * @param value
         *     allowed object is
         *     {@link Remesa.Declaracion.ListaBienesUrbanos }
         *     
         */
        public void setListaBienesUrbanos(Remesa.Declaracion.ListaBienesUrbanos value) {
            this.listaBienesUrbanos = value;
        }

        /**
         * Gets the value of the listaBienesRusticos property.
         * 
         * @return
         *     possible object is
         *     {@link Remesa.Declaracion.ListaBienesRusticos }
         *     
         */
        public Remesa.Declaracion.ListaBienesRusticos getListaBienesRusticos() {
            return listaBienesRusticos;
        }

        /**
         * Sets the value of the listaBienesRusticos property.
         * 
         * @param value
         *     allowed object is
         *     {@link Remesa.Declaracion.ListaBienesRusticos }
         *     
         */
        public void setListaBienesRusticos(Remesa.Declaracion.ListaBienesRusticos value) {
            this.listaBienesRusticos = value;
        }

        /**
         * Gets the value of the liquidacion property.
         * 
         * @return
         *     possible object is
         *     {@link LiquidacionType }
         *     
         */
        public LiquidacionType getLiquidacion() {
            return liquidacion;
        }

        /**
         * Sets the value of the liquidacion property.
         * 
         * @param value
         *     allowed object is
         *     {@link LiquidacionType }
         *     
         */
        public void setLiquidacion(LiquidacionType value) {
            this.liquidacion = value;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setId(String value) {
            this.id = value;
        }

        /**
         * Gets the value of the codigoDeclaracion property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodigoDeclaracion() {
            return codigoDeclaracion;
        }

        /**
         * Sets the value of the codigoDeclaracion property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodigoDeclaracion(String value) {
            this.codigoDeclaracion = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="bienRustico" type="{http://servicios.tributasenasturias.es/Calculo600Generico}bienRusticoType" maxOccurs="unbounded"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "bienRustico"
        })
        public static class ListaBienesRusticos {

            @XmlElement(required = true)
            protected List<BienRusticoType> bienRustico;

            /**
             * Gets the value of the bienRustico property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the bienRustico property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getBienRustico().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link BienRusticoType }
             * 
             * 
             */
            public List<BienRusticoType> getBienRustico() {
                if (bienRustico == null) {
                    bienRustico = new ArrayList<BienRusticoType>();
                }
                return this.bienRustico;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="bienUrbano" type="{http://servicios.tributasenasturias.es/Calculo600Generico}bienUrbanoType" maxOccurs="unbounded"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "bienUrbano"
        })
        public static class ListaBienesUrbanos {

            @XmlElement(required = true)
            protected List<BienUrbanoType> bienUrbano;

            /**
             * Gets the value of the bienUrbano property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the bienUrbano property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getBienUrbano().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link BienUrbanoType }
             * 
             * 
             */
            public List<BienUrbanoType> getBienUrbano() {
                if (bienUrbano == null) {
                    bienUrbano = new ArrayList<BienUrbanoType>();
                }
                return this.bienUrbano;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="interviniente" type="{http://servicios.tributasenasturias.es/Calculo600Generico}intervinienteType" maxOccurs="199"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "interviniente"
        })
        public static class ListaIntervinientes {

            @XmlElement(required = true)
            protected List<IntervinienteType> interviniente;

            /**
             * Gets the value of the interviniente property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the interviniente property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getInterviniente().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link IntervinienteType }
             * 
             * 
             */
            public List<IntervinienteType> getInterviniente() {
                if (interviniente == null) {
                    interviniente = new ArrayList<IntervinienteType>();
                }
                return this.interviniente;
            }

        }

    }

}
