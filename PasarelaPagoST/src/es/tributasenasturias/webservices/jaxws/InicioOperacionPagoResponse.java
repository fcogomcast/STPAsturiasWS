
package es.tributasenasturias.webservices.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "iniciaOperacionPagoResponse", namespace = "http://webservices.tributasenasturias.es/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "iniciaOperacionPagoResponse", namespace = "http://webservices.tributasenasturias.es/")
public class InicioOperacionPagoResponse {

    @XmlElement(name = "return", namespace = "")
    private es.tributasenasturias.webservices.types.InicioOperacionPagoResponse _return;

    /**
     * 
     * @return
     *     returns InicioOperacionPagoResponse
     */
    public es.tributasenasturias.webservices.types.InicioOperacionPagoResponse getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(es.tributasenasturias.webservices.types.InicioOperacionPagoResponse _return) {
        this._return = _return;
    }

}
