
package es.tributasenasturias.webservices.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "recibirNotificacionPagoUnicaja", namespace = "http://webservices.tributasenasturias.es/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recibirNotificacionPagoUnicaja", namespace = "http://webservices.tributasenasturias.es/", propOrder = {
    "certificadoCliente",
    "json"
})
public class RecibirNotificacionPagoUnicaja {

    @XmlElement(name = "certificadoCliente", namespace = "")
    private String certificadoCliente;
    @XmlElement(name = "json", namespace = "")
    private String json;

    /**
     * 
     * @return
     *     returns String
     */
    public String getCertificadoCliente() {
        return this.certificadoCliente;
    }

    /**
     * 
     * @param certificadoCliente
     *     the value for the certificadoCliente property
     */
    public void setCertificadoCliente(String certificadoCliente) {
        this.certificadoCliente = certificadoCliente;
    }

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
