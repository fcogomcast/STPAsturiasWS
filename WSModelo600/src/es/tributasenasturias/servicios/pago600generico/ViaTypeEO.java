
package es.tributasenasturias.servicios.pago600generico;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for viaTypeEO.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="viaTypeEO">
 *   &lt;restriction base="{http://servicios.tributasenasturias.es/Pago600Generico}collapsedStringType">
 *     &lt;enumeration value="AD"/>
 *     &lt;enumeration value="AG"/>
 *     &lt;enumeration value="AL"/>
 *     &lt;enumeration value="AR"/>
 *     &lt;enumeration value="AU"/>
 *     &lt;enumeration value="AV"/>
 *     &lt;enumeration value="AY"/>
 *     &lt;enumeration value="BJ"/>
 *     &lt;enumeration value="BO"/>
 *     &lt;enumeration value="BR"/>
 *     &lt;enumeration value="CA"/>
 *     &lt;enumeration value="CG"/>
 *     &lt;enumeration value="CI"/>
 *     &lt;enumeration value="CJ"/>
 *     &lt;enumeration value="CL"/>
 *     &lt;enumeration value="CM"/>
 *     &lt;enumeration value="CN"/>
 *     &lt;enumeration value="CO"/>
 *     &lt;enumeration value="CP"/>
 *     &lt;enumeration value="CR"/>
 *     &lt;enumeration value="CS"/>
 *     &lt;enumeration value="CT"/>
 *     &lt;enumeration value="CU"/>
 *     &lt;enumeration value="CH"/>
 *     &lt;enumeration value="DE"/>
 *     &lt;enumeration value="DP"/>
 *     &lt;enumeration value="DS"/>
 *     &lt;enumeration value="ED"/>
 *     &lt;enumeration value="EM"/>
 *     &lt;enumeration value="EN"/>
 *     &lt;enumeration value="ER"/>
 *     &lt;enumeration value="ES"/>
 *     &lt;enumeration value="EX"/>
 *     &lt;enumeration value="FC"/>
 *     &lt;enumeration value="FN"/>
 *     &lt;enumeration value="GL"/>
 *     &lt;enumeration value="GR"/>
 *     &lt;enumeration value="GT"/>
 *     &lt;enumeration value="GV"/>
 *     &lt;enumeration value="HT"/>
 *     &lt;enumeration value="JR"/>
 *     &lt;enumeration value="LD"/>
 *     &lt;enumeration value="LG"/>
 *     &lt;enumeration value="MC"/>
 *     &lt;enumeration value="ML"/>
 *     &lt;enumeration value="MN"/>
 *     &lt;enumeration value="MS"/>
 *     &lt;enumeration value="MT"/>
 *     &lt;enumeration value="MZ"/>
 *     &lt;enumeration value="PB"/>
 *     &lt;enumeration value="PD"/>
 *     &lt;enumeration value="PG"/>
 *     &lt;enumeration value="PJ"/>
 *     &lt;enumeration value="PL"/>
 *     &lt;enumeration value="PM"/>
 *     &lt;enumeration value="PQ"/>
 *     &lt;enumeration value="PR"/>
 *     &lt;enumeration value="PS"/>
 *     &lt;enumeration value="PT"/>
 *     &lt;enumeration value="PZ"/>
 *     &lt;enumeration value="QT"/>
 *     &lt;enumeration value="RB"/>
 *     &lt;enumeration value="RC"/>
 *     &lt;enumeration value="RD"/>
 *     &lt;enumeration value="RM"/>
 *     &lt;enumeration value="RP"/>
 *     &lt;enumeration value="RR"/>
 *     &lt;enumeration value="RU"/>
 *     &lt;enumeration value="SA"/>
 *     &lt;enumeration value="SD"/>
 *     &lt;enumeration value="SL"/>
 *     &lt;enumeration value="SN"/>
 *     &lt;enumeration value="SU"/>
 *     &lt;enumeration value="TN"/>
 *     &lt;enumeration value="TO"/>
 *     &lt;enumeration value="TR"/>
 *     &lt;enumeration value="UR"/>
 *     &lt;enumeration value="VR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "viaTypeEO")
@XmlEnum
public enum ViaTypeEO {

    AD,
    AG,
    AL,
    AR,
    AU,
    AV,
    AY,
    BJ,
    BO,
    BR,
    CA,
    CG,
    CI,
    CJ,
    CL,
    CM,
    CN,
    CO,
    CP,
    CR,
    CS,
    CT,
    CU,
    CH,
    DE,
    DP,
    DS,
    ED,
    EM,
    EN,
    ER,
    ES,
    EX,
    FC,
    FN,
    GL,
    GR,
    GT,
    GV,
    HT,
    JR,
    LD,
    LG,
    MC,
    ML,
    MN,
    MS,
    MT,
    MZ,
    PB,
    PD,
    PG,
    PJ,
    PL,
    PM,
    PQ,
    PR,
    PS,
    PT,
    PZ,
    QT,
    RB,
    RC,
    RD,
    RM,
    RP,
    RR,
    RU,
    SA,
    SD,
    SL,
    SN,
    SU,
    TN,
    TO,
    TR,
    UR,
    VR;

    public String value() {
        return name();
    }

    public static ViaTypeEO fromValue(String v) {
        return valueOf(v);
    }

}
