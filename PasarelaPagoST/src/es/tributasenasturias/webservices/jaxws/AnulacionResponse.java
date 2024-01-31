
package es.tributasenasturias.webservices.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import es.tributasenasturias.webservices.parametros.ResultadoAnulacion;

@XmlRootElement(name = "AnulacionResponse", namespace = "http://webservices.tributasenasturias.es/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnulacionResponse", namespace = "http://webservices.tributasenasturias.es/")
public class AnulacionResponse {

    @XmlElement(name = "return", namespace = "")
    private ResultadoAnulacion _return;

    /**
     * 
     * @return
     *     returns ResultadoAnulacion
     */
    public ResultadoAnulacion getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(ResultadoAnulacion _return) {
        this._return = _return;
    }

}
