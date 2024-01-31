
package es.tributasenasturias.webservices.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "recibirResultadoPagoUniversalPay", namespace = "http://webservices.tributasenasturias.es/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recibirResultadoPagoUniversalPay", namespace = "http://webservices.tributasenasturias.es/")
public class RecibirResultadoPagoUniversalPay {

    @XmlElement(name = "json", namespace = "")
    private String json;

    /**
     * 
     * @return
     *     returns String
     */
    public String getJson() {
        return this.json;
    }

    /**
     * 
     * @param json
     *     the value for the json property
     */
    public void setJson(String json) {
        this.json = json;
    }

}
