
package es.tributasenasturias.webservices.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import es.tributasenasturias.webservices.parametros.ResultadoConsulta;

@XmlRootElement(name = "ConsultaCobrosResponse", namespace = "http://webservices.tributasenasturias.es/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConsultaCobrosResponse", namespace = "http://webservices.tributasenasturias.es/")
public class ConsultaResponse {

    @XmlElement(name = "return", namespace = "")
    private ResultadoConsulta _return;

    /**
     * 
     * @return
     *     returns ResultadoConsulta
     */
    public ResultadoConsulta getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(ResultadoConsulta _return) {
        this._return = _return;
    }

}
