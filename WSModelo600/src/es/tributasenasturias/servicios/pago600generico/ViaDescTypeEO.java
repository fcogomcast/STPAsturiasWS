
package es.tributasenasturias.servicios.pago600generico;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for viaDescTypeEO.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="viaDescTypeEO">
 *   &lt;restriction base="{http://servicios.tributasenasturias.es/Pago600Generico}collapsedStringType">
 *     &lt;enumeration value="ALEDAÑO"/>
 *     &lt;enumeration value="AGREGADO"/>
 *     &lt;enumeration value="ALDEA, ALAMEDA"/>
 *     &lt;enumeration value="AREA, ARRABAL, ARCO"/>
 *     &lt;enumeration value="AUTOPISTA"/>
 *     &lt;enumeration value="AVENIDA"/>
 *     &lt;enumeration value="ARROYO"/>
 *     &lt;enumeration value="BAJADA"/>
 *     &lt;enumeration value="BARRIO"/>
 *     &lt;enumeration value="BARRANCO"/>
 *     &lt;enumeration value="CAÑADA"/>
 *     &lt;enumeration value="COLEGIO, CIGARRAL"/>
 *     &lt;enumeration value="CINTURON"/>
 *     &lt;enumeration value="CALLEJON, CALLEJA"/>
 *     &lt;enumeration value="CALLE"/>
 *     &lt;enumeration value="CAMINO, CARMEN"/>
 *     &lt;enumeration value="COLONIA"/>
 *     &lt;enumeration value="CONCEJO, COLEGIO"/>
 *     &lt;enumeration value="CAMPA, CAMPO"/>
 *     &lt;enumeration value="CARRETERA, CARRERA"/>
 *     &lt;enumeration value="CASERIO"/>
 *     &lt;enumeration value="CUESTA, COSTANILLA"/>
 *     &lt;enumeration value="CONJUNTO"/>
 *     &lt;enumeration value="CHALET"/>
 *     &lt;enumeration value="DETRÁS"/>
 *     &lt;enumeration value="DIPUTACION"/>
 *     &lt;enumeration value="DISEMINADOS"/>
 *     &lt;enumeration value="EDIFICIOS"/>
 *     &lt;enumeration value="EXTRAMURO"/>
 *     &lt;enumeration value="ENTRADA, ENSANCHE"/>
 *     &lt;enumeration value="EXTRARRADIO"/>
 *     &lt;enumeration value="ESCALINATA"/>
 *     &lt;enumeration value="EXPLANADA"/>
 *     &lt;enumeration value="FERROCARRIL"/>
 *     &lt;enumeration value="FINCA"/>
 *     &lt;enumeration value="GLORIETA"/>
 *     &lt;enumeration value="GRUPO"/>
 *     &lt;enumeration value="GLORIETA"/>
 *     &lt;enumeration value="GRAN VIA"/>
 *     &lt;enumeration value="HUERTA, HUERTO"/>
 *     &lt;enumeration value="JARDINES"/>
 *     &lt;enumeration value="LADO, LADERA"/>
 *     &lt;enumeration value="LUGAR"/>
 *     &lt;enumeration value="MERCADO"/>
 *     &lt;enumeration value="MUELLE"/>
 *     &lt;enumeration value="MUNICIPIO"/>
 *     &lt;enumeration value="MASIAS"/>
 *     &lt;enumeration value="MONTE"/>
 *     &lt;enumeration value="MANZANA"/>
 *     &lt;enumeration value="POBLADO"/>
 *     &lt;enumeration value="PARTIDA"/>
 *     &lt;enumeration value="POLIGONO"/>
 *     &lt;enumeration value="PASAJE, PASADIZO"/>
 *     &lt;enumeration value="POLIGONO"/>
 *     &lt;enumeration value="PARAMO"/>
 *     &lt;enumeration value="PARROQUIA, PARQUE"/>
 *     &lt;enumeration value="PROLONGACIÓN"/>
 *     &lt;enumeration value="PASEO"/>
 *     &lt;enumeration value="PUENTE"/>
 *     &lt;enumeration value="PLAZA"/>
 *     &lt;enumeration value="QUINTA"/>
 *     &lt;enumeration value="RAMBLA"/>
 *     &lt;enumeration value="RINCON, RINCONADA"/>
 *     &lt;enumeration value="RONDA"/>
 *     &lt;enumeration value="RAMAL"/>
 *     &lt;enumeration value="RAMPA"/>
 *     &lt;enumeration value="RIERA"/>
 *     &lt;enumeration value="RUA"/>
 *     &lt;enumeration value="SALIDA"/>
 *     &lt;enumeration value="SENDA"/>
 *     &lt;enumeration value="SOLAR"/>
 *     &lt;enumeration value="SALON"/>
 *     &lt;enumeration value="SUBIDA"/>
 *     &lt;enumeration value="TERRENOS"/>
 *     &lt;enumeration value="TORRENTE"/>
 *     &lt;enumeration value="TRAVESIA"/>
 *     &lt;enumeration value="URBANIZACION"/>
 *     &lt;enumeration value="VEREDA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "viaDescTypeEO")
@XmlEnum
public enum ViaDescTypeEO {

    ALEDAÑO("ALEDA\u00d1O"),
    AGREGADO("AGREGADO"),
    @XmlEnumValue("ALDEA, ALAMEDA")
    ALDEA_ALAMEDA("ALDEA, ALAMEDA"),
    @XmlEnumValue("AREA, ARRABAL, ARCO")
    AREA_ARRABAL_ARCO("AREA, ARRABAL, ARCO"),
    AUTOPISTA("AUTOPISTA"),
    AVENIDA("AVENIDA"),
    ARROYO("ARROYO"),
    BAJADA("BAJADA"),
    BARRIO("BARRIO"),
    BARRANCO("BARRANCO"),
    CAÑADA("CA\u00d1ADA"),
    @XmlEnumValue("COLEGIO, CIGARRAL")
    COLEGIO_CIGARRAL("COLEGIO, CIGARRAL"),
    CINTURON("CINTURON"),
    @XmlEnumValue("CALLEJON, CALLEJA")
    CALLEJON_CALLEJA("CALLEJON, CALLEJA"),
    CALLE("CALLE"),
    @XmlEnumValue("CAMINO, CARMEN")
    CAMINO_CARMEN("CAMINO, CARMEN"),
    COLONIA("COLONIA"),
    @XmlEnumValue("CONCEJO, COLEGIO")
    CONCEJO_COLEGIO("CONCEJO, COLEGIO"),
    @XmlEnumValue("CAMPA, CAMPO")
    CAMPA_CAMPO("CAMPA, CAMPO"),
    @XmlEnumValue("CARRETERA, CARRERA")
    CARRETERA_CARRERA("CARRETERA, CARRERA"),
    CASERIO("CASERIO"),
    @XmlEnumValue("CUESTA, COSTANILLA")
    CUESTA_COSTANILLA("CUESTA, COSTANILLA"),
    CONJUNTO("CONJUNTO"),
    CHALET("CHALET"),
    DETRÁS("DETR\u00c1S"),
    DIPUTACION("DIPUTACION"),
    DISEMINADOS("DISEMINADOS"),
    EDIFICIOS("EDIFICIOS"),
    EXTRAMURO("EXTRAMURO"),
    @XmlEnumValue("ENTRADA, ENSANCHE")
    ENTRADA_ENSANCHE("ENTRADA, ENSANCHE"),
    EXTRARRADIO("EXTRARRADIO"),
    ESCALINATA("ESCALINATA"),
    EXPLANADA("EXPLANADA"),
    FERROCARRIL("FERROCARRIL"),
    FINCA("FINCA"),
    GLORIETA("GLORIETA"),
    GRUPO("GRUPO"),
    @XmlEnumValue("GRAN VIA")
    GRAN_VIA("GRAN VIA"),
    @XmlEnumValue("HUERTA, HUERTO")
    HUERTA_HUERTO("HUERTA, HUERTO"),
    JARDINES("JARDINES"),
    @XmlEnumValue("LADO, LADERA")
    LADO_LADERA("LADO, LADERA"),
    LUGAR("LUGAR"),
    MERCADO("MERCADO"),
    MUELLE("MUELLE"),
    MUNICIPIO("MUNICIPIO"),
    MASIAS("MASIAS"),
    MONTE("MONTE"),
    MANZANA("MANZANA"),
    POBLADO("POBLADO"),
    PARTIDA("PARTIDA"),
    POLIGONO("POLIGONO"),
    @XmlEnumValue("PASAJE, PASADIZO")
    PASAJE_PASADIZO("PASAJE, PASADIZO"),
    PARAMO("PARAMO"),
    @XmlEnumValue("PARROQUIA, PARQUE")
    PARROQUIA_PARQUE("PARROQUIA, PARQUE"),
    PROLONGACIÓN("PROLONGACI\u00d3N"),
    PASEO("PASEO"),
    PUENTE("PUENTE"),
    PLAZA("PLAZA"),
    QUINTA("QUINTA"),
    RAMBLA("RAMBLA"),
    @XmlEnumValue("RINCON, RINCONADA")
    RINCON_RINCONADA("RINCON, RINCONADA"),
    RONDA("RONDA"),
    RAMAL("RAMAL"),
    RAMPA("RAMPA"),
    RIERA("RIERA"),
    RUA("RUA"),
    SALIDA("SALIDA"),
    SENDA("SENDA"),
    SOLAR("SOLAR"),
    SALON("SALON"),
    SUBIDA("SUBIDA"),
    TERRENOS("TERRENOS"),
    TORRENTE("TORRENTE"),
    TRAVESIA("TRAVESIA"),
    URBANIZACION("URBANIZACION"),
    VEREDA("VEREDA");
    private final String value;

    ViaDescTypeEO(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ViaDescTypeEO fromValue(String v) {
        for (ViaDescTypeEO c: ViaDescTypeEO.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
