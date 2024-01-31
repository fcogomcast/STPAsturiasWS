
package es.tributasenasturias.webservice.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "consultaEscritura", namespace = "http://webservice.tributasenasturias.es/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultaEscritura", namespace = "http://webservice.tributasenasturias.es/", propOrder = {
    "codNotario",
    "codNotaria",
    "numProtocolo",
    "protocoloBis",
    "fechaDevengo",
    "codUsuario"
})
public class ConsultaEscritura {

    @XmlElement(name = "codNotario", namespace = "")
    private String codNotario;
    @XmlElement(name = "codNotaria", namespace = "")
    private String codNotaria;
    @XmlElement(name = "numProtocolo", namespace = "")
    private String numProtocolo;
    @XmlElement(name = "protocoloBis", namespace = "")
    private String protocoloBis;
    @XmlElement(name = "fechaDevengo", namespace = "")
    private String fechaDevengo;
    @XmlElement(name = "codUsuario", namespace = "")
    private String codUsuario;

    /**
     * 
     * @return
     *     returns String
     */
    public String getCodNotario() {
        return this.codNotario;
    }

    /**
     * 
     * @param codNotario
     *     the value for the codNotario property
     */
    public void setCodNotario(String codNotario) {
        this.codNotario = codNotario;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getCodNotaria() {
        return this.codNotaria;
    }

    /**
     * 
     * @param codNotaria
     *     the value for the codNotaria property
     */
    public void setCodNotaria(String codNotaria) {
        this.codNotaria = codNotaria;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getNumProtocolo() {
        return this.numProtocolo;
    }

    /**
     * 
     * @param numProtocolo
     *     the value for the numProtocolo property
     */
    public void setNumProtocolo(String numProtocolo) {
        this.numProtocolo = numProtocolo;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getProtocoloBis() {
        return this.protocoloBis;
    }

    /**
     * 
     * @param protocoloBis
     *     the value for the protocoloBis property
     */
    public void setProtocoloBis(String protocoloBis) {
        this.protocoloBis = protocoloBis;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getFechaDevengo() {
        return this.fechaDevengo;
    }

    /**
     * 
     * @param fechaDevengo
     *     the value for the fechaDevengo property
     */
    public void setFechaDevengo(String fechaDevengo) {
        this.fechaDevengo = fechaDevengo;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getCodUsuario() {
        return this.codUsuario;
    }

    /**
     * 
     * @param codUsuario
     *     the value for the codUsuario property
     */
    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

}
