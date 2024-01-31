
package es.tributasenasturias.webservices.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import es.tributasenasturias.webservices.parametros.ResultadoPeticion;

@XmlRootElement(name = "PeticionResponse", namespace = "http://webservices.tributasenasturias.es/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PeticionResponse", namespace = "http://webservices.tributasenasturias.es/")
public class PeticionResponse {

    @XmlElement(name = "return", namespace = "")
    private ResultadoPeticion _return;

    /**
     * 
     * @return
     *     returns ResultadoPeticion
     */
    public ResultadoPeticion getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(ResultadoPeticion _return) {
        this._return = _return;
    }

}
