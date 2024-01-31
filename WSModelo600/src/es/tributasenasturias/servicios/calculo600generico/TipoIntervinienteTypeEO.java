
package es.tributasenasturias.servicios.calculo600generico;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tipoIntervinienteTypeEO.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tipoIntervinienteTypeEO">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Presentador"/>
 *     &lt;enumeration value="Sujeto Pasivo"/>
 *     &lt;enumeration value="Transmitente"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tipoIntervinienteTypeEO")
@XmlEnum
public enum TipoIntervinienteTypeEO {

    @XmlEnumValue("Presentador")
    PRESENTADOR("Presentador"),
    @XmlEnumValue("Sujeto Pasivo")
    SUJETO_PASIVO("Sujeto Pasivo"),
    @XmlEnumValue("Transmitente")
    TRANSMITENTE("Transmitente");
    private final String value;

    TipoIntervinienteTypeEO(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TipoIntervinienteTypeEO fromValue(String v) {
        for (TipoIntervinienteTypeEO c: TipoIntervinienteTypeEO.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
