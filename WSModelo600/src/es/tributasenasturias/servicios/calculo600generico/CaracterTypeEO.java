
package es.tributasenasturias.servicios.calculo600generico;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for caracterTypeEO.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="caracterTypeEO">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="G"/>
 *     &lt;enumeration value="P"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "caracterTypeEO")
@XmlEnum
public enum CaracterTypeEO {

    G,
    P;

    public String value() {
        return name();
    }

    public static CaracterTypeEO fromValue(String v) {
        return valueOf(v);
    }

}
