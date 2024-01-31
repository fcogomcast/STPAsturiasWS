
package es.tributasenasturias.servicios.calculo600generico;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for provinciaDescTypeEO.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="provinciaDescTypeEO">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ALAVA"/>
 *     &lt;enumeration value="ALBACETE"/>
 *     &lt;enumeration value="ALICANTE"/>
 *     &lt;enumeration value="ALMERIA"/>
 *     &lt;enumeration value="ASTURIAS"/>
 *     &lt;enumeration value="AVILA"/>
 *     &lt;enumeration value="BADAJOZ"/>
 *     &lt;enumeration value="BALEARES"/>
 *     &lt;enumeration value="BARCELONA"/>
 *     &lt;enumeration value="BURGOS"/>
 *     &lt;enumeration value="CACERES"/>
 *     &lt;enumeration value="CADIZ"/>
 *     &lt;enumeration value="CANTABRIA"/>
 *     &lt;enumeration value="CASTELLON"/>
 *     &lt;enumeration value="CEUTA"/>
 *     &lt;enumeration value="CIUDAD REAL"/>
 *     &lt;enumeration value="CORDOBA"/>
 *     &lt;enumeration value="CORUÑA (LA)"/>
 *     &lt;enumeration value="CUENCA"/>
 *     &lt;enumeration value="EXTRANJERO"/>
 *     &lt;enumeration value="GIRONA"/>
 *     &lt;enumeration value="GRANADA"/>
 *     &lt;enumeration value="GUADALAJARA"/>
 *     &lt;enumeration value="GUIPUZCOA"/>
 *     &lt;enumeration value="HUELVA"/>
 *     &lt;enumeration value="HUESCA"/>
 *     &lt;enumeration value="JAEN"/>
 *     &lt;enumeration value="LA RIOJA"/>
 *     &lt;enumeration value="LEON"/>
 *     &lt;enumeration value="LUGO"/>
 *     &lt;enumeration value="LLEIDA"/>
 *     &lt;enumeration value="MADRID"/>
 *     &lt;enumeration value="MALAGA"/>
 *     &lt;enumeration value="MELILLA"/>
 *     &lt;enumeration value="MURCIA"/>
 *     &lt;enumeration value="NAVARRA"/>
 *     &lt;enumeration value="OURENSE"/>
 *     &lt;enumeration value="PALENCIA"/>
 *     &lt;enumeration value="PALMAS (LAS)"/>
 *     &lt;enumeration value="PONTEVEDRA"/>
 *     &lt;enumeration value="S.C. TENERIFE"/>
 *     &lt;enumeration value="SALAMANCA"/>
 *     &lt;enumeration value="SEGOVIA"/>
 *     &lt;enumeration value="SEVILLA"/>
 *     &lt;enumeration value="SORIA"/>
 *     &lt;enumeration value="TARRAGONA"/>
 *     &lt;enumeration value="TERUEL"/>
 *     &lt;enumeration value="TOLEDO"/>
 *     &lt;enumeration value="VALENCIA"/>
 *     &lt;enumeration value="VALLADOLID"/>
 *     &lt;enumeration value="VIZCAYA"/>
 *     &lt;enumeration value="ZAMORA"/>
 *     &lt;enumeration value="ZARAGOZA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "provinciaDescTypeEO")
@XmlEnum
public enum ProvinciaDescTypeEO {

    ALAVA("ALAVA"),
    ALBACETE("ALBACETE"),
    ALICANTE("ALICANTE"),
    ALMERIA("ALMERIA"),
    ASTURIAS("ASTURIAS"),
    AVILA("AVILA"),
    BADAJOZ("BADAJOZ"),
    BALEARES("BALEARES"),
    BARCELONA("BARCELONA"),
    BURGOS("BURGOS"),
    CACERES("CACERES"),
    CADIZ("CADIZ"),
    CANTABRIA("CANTABRIA"),
    CASTELLON("CASTELLON"),
    CEUTA("CEUTA"),
    @XmlEnumValue("CIUDAD REAL")
    CIUDAD_REAL("CIUDAD REAL"),
    CORDOBA("CORDOBA"),
    @XmlEnumValue("CORU\u00d1A (LA)")
    CORUÑA_LA("CORU\u00d1A (LA)"),
    CUENCA("CUENCA"),
    EXTRANJERO("EXTRANJERO"),
    GIRONA("GIRONA"),
    GRANADA("GRANADA"),
    GUADALAJARA("GUADALAJARA"),
    GUIPUZCOA("GUIPUZCOA"),
    HUELVA("HUELVA"),
    HUESCA("HUESCA"),
    JAEN("JAEN"),
    @XmlEnumValue("LA RIOJA")
    LA_RIOJA("LA RIOJA"),
    LEON("LEON"),
    LUGO("LUGO"),
    LLEIDA("LLEIDA"),
    MADRID("MADRID"),
    MALAGA("MALAGA"),
    MELILLA("MELILLA"),
    MURCIA("MURCIA"),
    NAVARRA("NAVARRA"),
    OURENSE("OURENSE"),
    PALENCIA("PALENCIA"),
    @XmlEnumValue("PALMAS (LAS)")
    PALMAS_LAS("PALMAS (LAS)"),
    PONTEVEDRA("PONTEVEDRA"),
    @XmlEnumValue("S.C. TENERIFE")
    S_C_TENERIFE("S.C. TENERIFE"),
    SALAMANCA("SALAMANCA"),
    SEGOVIA("SEGOVIA"),
    SEVILLA("SEVILLA"),
    SORIA("SORIA"),
    TARRAGONA("TARRAGONA"),
    TERUEL("TERUEL"),
    TOLEDO("TOLEDO"),
    VALENCIA("VALENCIA"),
    VALLADOLID("VALLADOLID"),
    VIZCAYA("VIZCAYA"),
    ZAMORA("ZAMORA"),
    ZARAGOZA("ZARAGOZA");
    private final String value;

    ProvinciaDescTypeEO(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ProvinciaDescTypeEO fromValue(String v) {
        for (ProvinciaDescTypeEO c: ProvinciaDescTypeEO.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
