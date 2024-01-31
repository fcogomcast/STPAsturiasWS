
package es.tributasenasturias.webservices.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Peticion", namespace = "http://webservices.tributasenasturias.es/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Peticion", namespace = "http://webservices.tributasenasturias.es/", propOrder = {
    "origen",
    "modalidad",
    "cliente",
    "entidad",
    "emisora",
    "modelo",
    "nif",
    "nombreContribuyente",
    "fechaDevengo",
    "datoEspecifico",
    "identificacion",
    "referencia",
    "numeroAutoliquidacion",
    "expediente",
    "importe",
    "tarjeta",
    "fechaCaducidad",
    "ccc",
    "nifOperante",
    "aplicacion",
    "numeroUnico",
    "numeroPeticion",
    "libre",
    "mac"
})
public class Peticion {

    @XmlElement(name = "origen", namespace = "")
    private String origen;
    @XmlElement(name = "modalidad", namespace = "")
    private String modalidad;
    @XmlElement(name = "cliente", namespace = "")
    private String cliente;
    @XmlElement(name = "entidad", namespace = "")
    private String entidad;
    @XmlElement(name = "emisora", namespace = "")
    private String emisora;
    @XmlElement(name = "modelo", namespace = "")
    private String modelo;
    @XmlElement(name = "nif", namespace = "")
    private String nif;
    @XmlElement(name = "nombreContribuyente", namespace = "")
    private String nombreContribuyente;
    @XmlElement(name = "fecha_devengo", namespace = "")
    private String fechaDevengo;
    @XmlElement(name = "dato_especifico", namespace = "")
    private String datoEspecifico;
    @XmlElement(name = "identificacion", namespace = "")
    private String identificacion;
    @XmlElement(name = "referencia", namespace = "")
    private String referencia;
    @XmlElement(name = "numero_autoliquidacion", namespace = "")
    private String numeroAutoliquidacion;
    @XmlElement(name = "expediente", namespace = "")
    private String expediente;
    @XmlElement(name = "importe", namespace = "")
    private String importe;
    @XmlElement(name = "tarjeta", namespace = "")
    private String tarjeta;
    @XmlElement(name = "fecha_caducidad", namespace = "")
    private String fechaCaducidad;
    @XmlElement(name = "ccc", namespace = "")
    private String ccc;
    @XmlElement(name = "nif_operante", namespace = "")
    private String nifOperante;
    @XmlElement(name = "aplicacion", namespace = "")
    private String aplicacion;
    @XmlElement(name = "numero_unico", namespace = "")
    private String numeroUnico;
    @XmlElement(name = "numero_peticion", namespace = "")
    private String numeroPeticion;
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
    public String getEntidad() {
        return this.entidad;
    }

    /**
     * 
     * @param entidad
     *     the value for the entidad property
     */
    public void setEntidad(String entidad) {
        this.entidad = entidad;
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
    public String getNif() {
        return this.nif;
    }

    /**
     * 
     * @param nif
     *     the value for the nif property
     */
    public void setNif(String nif) {
        this.nif = nif;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getNombreContribuyente() {
        return this.nombreContribuyente;
    }

    /**
     * 
     * @param nombreContribuyente
     *     the value for the nombreContribuyente property
     */
    public void setNombreContribuyente(String nombreContribuyente) {
        this.nombreContribuyente = nombreContribuyente;
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
    public String getExpediente() {
        return this.expediente;
    }

    /**
     * 
     * @param expediente
     *     the value for the expediente property
     */
    public void setExpediente(String expediente) {
        this.expediente = expediente;
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
    public String getTarjeta() {
        return this.tarjeta;
    }

    /**
     * 
     * @param tarjeta
     *     the value for the tarjeta property
     */
    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getFechaCaducidad() {
        return this.fechaCaducidad;
    }

    /**
     * 
     * @param fechaCaducidad
     *     the value for the fechaCaducidad property
     */
    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getCcc() {
        return this.ccc;
    }

    /**
     * 
     * @param ccc
     *     the value for the ccc property
     */
    public void setCcc(String ccc) {
        this.ccc = ccc;
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
    public String getNumeroPeticion() {
        return this.numeroPeticion;
    }

    /**
     * 
     * @param numeroPeticion
     *     the value for the numeroPeticion property
     */
    public void setNumeroPeticion(String numeroPeticion) {
        this.numeroPeticion = numeroPeticion;
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
