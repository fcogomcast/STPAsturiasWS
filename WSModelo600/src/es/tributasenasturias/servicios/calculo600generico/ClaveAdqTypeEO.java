
package es.tributasenasturias.servicios.calculo600generico;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for claveAdqTypeEO.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="claveAdqTypeEO">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="US"/>
 *     &lt;enumeration value="UH"/>
 *     &lt;enumeration value="NP"/>
 *     &lt;enumeration value="PD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "claveAdqTypeEO")
@XmlEnum
public enum ClaveAdqTypeEO {

    US,
    UH,
    NP,
    PD;

    public String value() {
        return name();
    }

    public static ClaveAdqTypeEO fromValue(String v) {
        return valueOf(v);
    }

}
