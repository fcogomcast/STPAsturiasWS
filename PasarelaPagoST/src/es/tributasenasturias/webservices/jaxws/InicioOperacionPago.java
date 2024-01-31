
package es.tributasenasturias.webservices.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "iniciaOperacionPago", namespace = "http://webservices.tributasenasturias.es/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "iniciaOperacionPago", namespace = "http://webservices.tributasenasturias.es/", propOrder = {
    "origen",
    "modalidad",
    "emisora",
    "modelo",
    "nifContribuyente",
    "fechaDevengo",
    "datoEspecifico",
    "importe",
    "nifOperante",
    "aplicacion",
    "numeroUnico",
    "mac"
})
public class InicioOperacionPago {

    @XmlElement(name = "origen", namespace = "")
    private String origen;
    @XmlElement(name = "modalidad", namespace = "")
    private String modalidad;
    @XmlElement(name = "emisora", namespace = "")
    private String emisora;
    @XmlElement(name = "modelo", namespace = "")
    private String modelo;
    @XmlElement(name = "nif_contribuyente", namespace = "")
    private String nifContribuyente;
    @XmlElement(name = "fecha_devengo", namespace = "")
    private String fechaDevengo;
    @XmlElement(name = "dato_especifico", namespace = "")
    private String datoEspecifico;
    @XmlElement(name = "importe", namespace = "")
    private String importe;
    @XmlElement(name = "nif_operante", namespace = "")
    private String nifOperante;
    @XmlElement(name = "aplicacion", namespace = "")
    private String aplicacion;
    @XmlElement(name = "numero_unico", namespace = "")
    private String numeroUnico;
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
    public String getModelo() {
        return this.modelo;
    }

    /**
     * 
     * @param modelo
     *     the value for the modelo property
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getNifContribuyente() {
        return this.nifContribuyente;
    }

    /**
     * 
     * @param nifContribuyente
     *     the value for the nifContribuyente property
     */
    public void setNifContribuyente(String nifContribuyente) {
        this.nifContribuyente = nifContribuyente;
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
    public String getDatoEspecifico() {
        return this.datoEspecifico;
    }

    /**
     * 
     * @param datoEspecifico
     *     the value for the datoEspecifico property
     */
    public void setDatoEspecifico(String datoEspecifico) {
        this.datoEspecifico = datoEspecifico;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getImporte() {
        return this.importe;
    }

    /**
     * 
     * @param importe
     *     the value for the importe property
     */
    public void setImporte(String importe) {
        this.importe = importe;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getNifOperante() {
        return this.nifOperante;
    }

    /**
     * 
     * @param nifOperante
     *     the value for the nifOperante property
     */
    public void setNifOperante(String nifOperante) {
        this.nifOperante = nifOperante;
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
