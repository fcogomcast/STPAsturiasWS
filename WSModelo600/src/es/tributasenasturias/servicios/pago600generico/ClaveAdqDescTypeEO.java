
package es.tributasenasturias.servicios.pago600generico;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for claveAdqDescTypeEO.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="claveAdqDescTypeEO">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Usufructo"/>
 *     &lt;enumeration value="Uso y Habitación"/>
 *     &lt;enumeration value="Nuda Propiedad"/>
 *     &lt;enumeration value="Pleno Dominio"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "claveAdqDescTypeEO")
@XmlEnum
public enum ClaveAdqDescTypeEO {

    @XmlEnumValue("Usufructo")
    USUFRUCTO("Usufructo"),
    @XmlEnumValue("Uso y Habitaci\u00f3n")
    USO_Y_HABITACIÓN("Uso y Habitaci\u00f3n"),
    @XmlEnumValue("Nuda Propiedad")
    NUDA_PROPIEDAD("Nuda Propiedad"),
    @XmlEnumValue("Pleno Dominio")
    PLENO_DOMINIO("Pleno Dominio");
    private final String value;

    ClaveAdqDescTypeEO(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ClaveAdqDescTypeEO fromValue(String v) {
        for (ClaveAdqDescTypeEO c: ClaveAdqDescTypeEO.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
