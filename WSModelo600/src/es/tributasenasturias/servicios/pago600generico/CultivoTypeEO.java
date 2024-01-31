
package es.tributasenasturias.servicios.pago600generico;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cultivoTypeEO.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="cultivoTypeEO">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="P"/>
 *     &lt;enumeration value="MA"/>
 *     &lt;enumeration value="MB"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "cultivoTypeEO")
@XmlEnum
public enum CultivoTypeEO {

    P,
    MA,
    MB;

    public String value() {
        return name();
    }

    public static CultivoTypeEO fromValue(String v) {
        return valueOf(v);
    }

}
