
package es.tributasenasturias.webservices.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import es.tributasenasturias.webservices.types.InicioPagoTarjetaResponse;

@XmlRootElement(name = "iniciaPagoTarjetaResponse", namespace = "http://webservices.tributasenasturias.es/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "iniciaPagoTarjetaResponse", namespace = "http://webservices.tributasenasturias.es/")
public class IniciaPagoTarjetaResponse {

    @XmlElement(name = "return", namespace = "")
    private InicioPagoTarjetaResponse _return;

    /**
     * 
     * @return
     *     returns InicioPagoTarjetaResponse
     */
    public InicioPagoTarjetaResponse getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(InicioPagoTarjetaResponse _return) {
        this._return = _return;
    }

}
