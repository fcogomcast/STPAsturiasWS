
package es.tributasenasturias.servicios.calculo600generico;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoUrbanoDescTypeEO.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tipoUrbanoDescTypeEO">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="VIVIENDA"/>
 *     &lt;enumeration value="TRASTERO"/>
 *     &lt;enumeration value="OFICINA"/>
 *     &lt;enumeration value="LOCAL COMERCIAL"/>
 *     &lt;enumeration value="ALMACÉN"/>
 *     &lt;enumeration value="NAVE"/>
 *     &lt;enumeration value="TERRENO"/>
 *     &lt;enumeration value="OTROS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tipoUrbanoDescTypeEO")
@XmlEnum
public enum TipoUrbanoDescTypeEO {

    VIVIENDA("VIVIENDA"),
    TRASTERO("TRASTERO"),
    OFICINA("OFICINA"),
    @XmlEnumValue("LOCAL COMERCIAL")
    LOCAL_COMERCIAL("LOCAL COMERCIAL"),
    ALMACÉN("ALMAC\u00c9N"),
    NAVE("NAVE"),
    TERRENO("TERRENO"),
    OTROS("OTROS");
    private final String value;

    TipoUrbanoDescTypeEO(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TipoUrbanoDescTypeEO fromValue(String v) {
        for (TipoUrbanoDescTypeEO c: TipoUrbanoDescTypeEO.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
