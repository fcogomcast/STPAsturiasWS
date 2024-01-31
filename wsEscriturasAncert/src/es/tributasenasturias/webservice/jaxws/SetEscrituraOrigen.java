
package es.tributasenasturias.webservice.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "setEscrituraOrigen", namespace = "http://webservice.tributasenasturias.es/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setEscrituraOrigen", namespace = "http://webservice.tributasenasturias.es/", propOrder = {
    "codNotario",
    "codNotaria",
    "numProtocolo",
    "protocoloBis",
    "fechaAutorizacion",
    "firmaEscritura",
    "docEscritura",
    "origenEscritura"
})
public class SetEscrituraOrigen {

    @XmlElement(name = "codNotario", namespace = "")
    private String codNotario;
    @XmlElement(name = "codNotaria", namespace = "")
    private String codNotaria;
    @XmlElement(name = "numProtocolo", namespace = "")
    private String numProtocolo;
    @XmlElement(name = "protocoloBis", namespace = "")
    private String protocoloBis;
    @XmlElement(name = "fechaAutorizacion", namespace = "")
    private String fechaAutorizacion;
    @XmlElement(name = "firmaEscritura", namespace = "")
    private String firmaEscritura;
    @XmlElement(name = "docEscritura", namespace = "")
    private String docEscritura;
    @XmlElement(name = "origenEscritura", namespace = "")
    private String origenEscritura;

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
    public String getFechaAutorizacion() {
        return this.fechaAutorizacion;
    }

    /**
     * 
     * @param fechaAutorizacion
     *     the value for the fechaAutorizacion property
     */
    public void setFechaAutorizacion(String fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getFirmaEscritura() {
        return this.firmaEscritura;
    }

    /**
     * 
     * @param firmaEscritura
     *     the value for the firmaEscritura property
     */
    public void setFirmaEscritura(String firmaEscritura) {
        this.firmaEscritura = firmaEscritura;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getDocEscritura() {
        return this.docEscritura;
    }

    /**
     * 
     * @param docEscritura
     *     the value for the docEscritura property
     */
    public void setDocEscritura(String docEscritura) {
        this.docEscritura = docEscritura;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getOrigenEscritura() {
        return this.origenEscritura;
    }

    /**
     * 
     * @param origenEscritura
     *     the value for the origenEscritura property
     */
    public void setOrigenEscritura(String origenEscritura) {
        this.origenEscritura = origenEscritura;
    }

}
