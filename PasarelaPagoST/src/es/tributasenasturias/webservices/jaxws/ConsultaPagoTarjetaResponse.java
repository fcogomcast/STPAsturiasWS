
package es.tributasenasturias.webservices.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import es.tributasenasturias.webservices.types.ResultadoConsultaPagoTarjeta;

@XmlRootElement(name = "ConsultaPagoTarjetaResponse", namespace = "http://webservices.tributasenasturias.es/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConsultaPagoTarjetaResponse", namespace = "http://webservices.tributasenasturias.es/")
public class ConsultaPagoTarjetaResponse {

    @XmlElement(name = "return", namespace = "")
    private ResultadoConsultaPagoTarjeta _return;

    /**
     * 
     * @return
     *     returns ResultadoConsultaPagoTarjeta
     */
    public ResultadoConsultaPagoTarjeta getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(ResultadoConsultaPagoTarjeta _return) {
        this._return = _return;
    }

}
