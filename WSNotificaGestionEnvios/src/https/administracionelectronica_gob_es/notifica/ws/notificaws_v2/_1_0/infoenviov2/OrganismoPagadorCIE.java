
package https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para OrganismoPagadorCIE complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="OrganismoPagadorCIE">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoDIR3CIE" type="{https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/infoEnvioV2}CodigoDIR"/>
 *         &lt;element name="fechaVigenciaCIE" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrganismoPagadorCIE", propOrder = {
    "codigoDIR3CIE",
    "fechaVigenciaCIE"
})
public class OrganismoPagadorCIE {

    @XmlElement(required = true)
    protected CodigoDIR codigoDIR3CIE;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar fechaVigenciaCIE;

    /**
     * Obtiene el valor de la propiedad codigoDIR3CIE.
     * 
     * @return
     *     possible object is
     *     {@link CodigoDIR }
     *     
     */
    public CodigoDIR getCodigoDIR3CIE() {
        return codigoDIR3CIE;
    }

    /**
     * Define el valor de la propiedad codigoDIR3CIE.
     * 
     * @param value
     *     allowed object is
     *     {@link CodigoDIR }
     *     
     */
    public void setCodigoDIR3CIE(CodigoDIR value) {
        this.codigoDIR3CIE = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaVigenciaCIE.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaVigenciaCIE() {
        return fechaVigenciaCIE;
    }

    /**
     * Define el valor de la propiedad fechaVigenciaCIE.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaVigenciaCIE(XMLGregorianCalendar value) {
        this.fechaVigenciaCIE = value;
    }

}
