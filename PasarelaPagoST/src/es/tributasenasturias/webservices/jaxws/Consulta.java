
package es.tributasenasturias.webservices.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ConsultaCobros", namespace = "http://webservices.tributasenasturias.es/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConsultaCobros", namespace = "http://webservices.tributasenasturias.es/", propOrder = {
    "origen",
    "modalidad",
    "cliente",
    "emisora",
    "identificacion",
    "referencia",
    "numeroAutoliquidacion",
    "aplicacion",
    "numeroUnico",
    "envioJustificante",
    "libre",
    "mac"
})
public class Consulta {

    @XmlElement(name = "origen", namespace = "")
    private String origen;
    @XmlElement(name = "modalidad", namespace = "")
    private String modalidad;
    @XmlElement(name = "cliente", namespace = "")
    private String cliente;
    @XmlElement(name = "emisora", namespace = "")
    private String emisora;
    @XmlElement(name = "identificacion", namespace = "")
    private String identificacion;
    @XmlElement(name = "referencia", namespace = "")
    private String referencia;
    @XmlElement(name = "numero_autoliquidacion", namespace = "")
    private String numeroAutoliquidacion;
    @XmlElement(name = "aplicacion", namespace = "")
    private String aplicacion;
    @XmlElement(name = "numero_unico", namespace = "")
    private String numeroUnico;
    @XmlElement(name = "envio_justificante", namespace = "")
    private String envioJustificante;
    @XmlElement(name = "libre", namespace = "")
    private String libre;
    @XmlElement(name = "mac", namespace = "")
    private String mac;

    /**
     * 
     * @return
     *     returns String
     */
    public String getOrigen() {
        return this.origen;
    }

    /**
     * 
     * @param origen
     *     the value for the origen property
     */
    public void setOrigen(String origen) {
        this.origen = origen;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getModalidad() {
        return this.modalidad;
    }

    /**
     * 
     * @param modalidad
     *     the value for the modalidad property
     */
    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getCliente() {
        return this.cliente;
    }

    /**
     * 
     * @param cliente
     *     the value for the cliente property
     */
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getEmisora() {
        return this.emisora;
    }

    /**
     * 
     * @param emisora
     *     the value for the emisora property
     */
    public void setEmisora(String emisora) {
        this.emisora = emisora;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getIdentificacion() {
        return this.identificacion;
    }

    /**
     * 
     * @param identificacion
     *     the value for the identificacion property
     */
    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getReferencia() {
        return this.referencia;
    }

    /**
     * 
     * @param referencia
     *     the value for the referencia property
     */
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getNumeroAutoliquidacion() {
        return this.numeroAutoliquidacion;
    }

    /**
     * 
     * @param numeroAutoliquidacion
     *     the value for the numeroAutoliquidacion property
     */
    public void setNumeroAutoliquidacion(String numeroAutoliquidacion) {
        this.numeroAutoliquidacion = numeroAutoliquidacion;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getAplicacion() {
        return this.aplicacion;
    }

    /**
     * 
     * @param aplicacion
     *     the value for the aplicacion property
     */
    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getNumeroUnico() {
        return this.numeroUnico;
    }

    /**
     * 
     * @param numeroUnico
     *     the value for the numeroUnico property
     */
    public void setNumeroUnico(String numeroUnico) {
        this.numeroUnico = numeroUnico;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getEnvioJustificante() {
        return this.envioJustificante;
    }

    /**
     * 
     * @param envioJustificante
     *     the value for the envioJustificante property
     */
    public void setEnvioJustificante(String envioJustificante) {
        this.envioJustificante = envioJustificante;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getLibre() {
        return this.libre;
    }

    /**
     * 
     * @param libre
     *     the value for the libre property
     */
    public void setLibre(String libre) {
        this.libre = libre;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getMac() {
        return this.mac;
    }

    /**
     * 
     * @param mac
     *     the value for the mac property
     */
    public void setMac(String mac) {
        this.mac = mac;
    }

}
