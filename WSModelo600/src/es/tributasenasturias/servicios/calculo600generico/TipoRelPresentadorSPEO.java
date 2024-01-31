
package es.tributasenasturias.servicios.calculo600generico;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoRelPresentadorSPEO.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tipoRelPresentadorSPEO">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CO"/>
 *     &lt;enumeration value="GE"/>
 *     &lt;enumeration value="IN"/>
 *     &lt;enumeration value="OF"/>
 *     &lt;enumeration value="OT"/>
 *     &lt;enumeration value="RR"/>
 *     &lt;enumeration value="TT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tipoRelPresentadorSPEO")
@XmlEnum
public enum TipoRelPresentadorSPEO {

    CO,
    GE,
    IN,
    OF,
    OT,
    RR,
    TT;

    public String value() {
        return name();
    }

    public static TipoRelPresentadorSPEO fromValue(String v) {
        return valueOf(v);
    }

}
