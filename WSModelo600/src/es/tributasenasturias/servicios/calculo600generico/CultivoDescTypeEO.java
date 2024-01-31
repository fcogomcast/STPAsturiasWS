
package es.tributasenasturias.servicios.calculo600generico;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cultivoDescTypeEO.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="cultivoDescTypeEO">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PRADO"/>
 *     &lt;enumeration value="MONTE ALTO"/>
 *     &lt;enumeration value="MONTE BAJO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "cultivoDescTypeEO")
@XmlEnum
public enum CultivoDescTypeEO {

    PRADO("PRADO"),
    @XmlEnumValue("MONTE ALTO")
    MONTE_ALTO("MONTE ALTO"),
    @XmlEnumValue("MONTE BAJO")
    MONTE_BAJO("MONTE BAJO");
    private final String value;

    CultivoDescTypeEO(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CultivoDescTypeEO fromValue(String v) {
        for (CultivoDescTypeEO c: CultivoDescTypeEO.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
