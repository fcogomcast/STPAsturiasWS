
package es.tributasenasturias.webservices.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import es.tributasenasturias.webservices.types.AnulacionPagoTarjetaResponse;

@XmlRootElement(name = "anularPagoTarjetaResponse", namespace = "http://webservices.tributasenasturias.es/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "anularPagoTarjetaResponse", namespace = "http://webservices.tributasenasturias.es/")
public class AnularPagoTarjetaResponse {

    @XmlElement(name = "return", namespace = "")
    private AnulacionPagoTarjetaResponse _return;

    /**
     * 
     * @return
     *     returns AnulacionPagoTarjetaResponse
     */
    public AnulacionPagoTarjetaResponse getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(AnulacionPagoTarjetaResponse _return) {
        this._return = _return;
    }

}
