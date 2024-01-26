
package https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.altaremesaenvios;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.altaremesaenvios package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ResultadoAltaRemesaEnvios_QNAME = new QName("https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios", "resultadoAltaRemesaEnvios");
    private final static QName _AltaRemesaEnvios_QNAME = new QName("https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios", "altaRemesaEnvios");
    private final static QName _ResultadoAltaRemesaEnviosFechaCreacion_QNAME = new QName("https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios", "fechaCreacion");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.altaremesaenvios
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ResultadoAltaRemesaEnvios }
     * 
     */
    public ResultadoAltaRemesaEnvios createResultadoAltaRemesaEnvios() {
        return new ResultadoAltaRemesaEnvios();
    }

    /**
     * Create an instance of {@link AltaRemesaEnvios }
     * 
     */
    public AltaRemesaEnvios createAltaRemesaEnvios() {
        return new AltaRemesaEnvios();
    }

    /**
     * Create an instance of {@link Opcion }
     * 
     */
    public Opcion createOpcion() {
        return new Opcion();
    }

    /**
     * Create an instance of {@link Envios }
     * 
     */
    public Envios createEnvios() {
        return new Envios();
    }

    /**
     * Create an instance of {@link Persona }
     * 
     */
    public Persona createPersona() {
        return new Persona();
    }

    /**
     * Create an instance of {@link EntregaPostal }
     * 
     */
    public EntregaPostal createEntregaPostal() {
        return new EntregaPostal();
    }

    /**
     * Create an instance of {@link OrganismoPagadorPostal }
     * 
     */
    public OrganismoPagadorPostal createOrganismoPagadorPostal() {
        return new OrganismoPagadorPostal();
    }

    /**
     * Create an instance of {@link Envio }
     * 
     */
    public Envio createEnvio() {
        return new Envio();
    }

    /**
     * Create an instance of {@link Contenido }
     * 
     */
    public Contenido createContenido() {
        return new Contenido();
    }

    /**
     * Create an instance of {@link Opciones }
     * 
     */
    public Opciones createOpciones() {
        return new Opciones();
    }

    /**
     * Create an instance of {@link Destinatarios }
     * 
     */
    public Destinatarios createDestinatarios() {
        return new Destinatarios();
    }

    /**
     * Create an instance of {@link Documento }
     * 
     */
    public Documento createDocumento() {
        return new Documento();
    }

    /**
     * Create an instance of {@link EntregaDEH }
     * 
     */
    public EntregaDEH createEntregaDEH() {
        return new EntregaDEH();
    }

    /**
     * Create an instance of {@link ResultadoEnvio }
     * 
     */
    public ResultadoEnvio createResultadoEnvio() {
        return new ResultadoEnvio();
    }

    /**
     * Create an instance of {@link OrganismoPagadorCIE }
     * 
     */
    public OrganismoPagadorCIE createOrganismoPagadorCIE() {
        return new OrganismoPagadorCIE();
    }

    /**
     * Create an instance of {@link ResultadoEnvios }
     * 
     */
    public ResultadoEnvios createResultadoEnvios() {
        return new ResultadoEnvios();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultadoAltaRemesaEnvios }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios", name = "resultadoAltaRemesaEnvios")
    public JAXBElement<ResultadoAltaRemesaEnvios> createResultadoAltaRemesaEnvios(ResultadoAltaRemesaEnvios value) {
        return new JAXBElement<ResultadoAltaRemesaEnvios>(_ResultadoAltaRemesaEnvios_QNAME, ResultadoAltaRemesaEnvios.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AltaRemesaEnvios }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios", name = "altaRemesaEnvios")
    public JAXBElement<AltaRemesaEnvios> createAltaRemesaEnvios(AltaRemesaEnvios value) {
        return new JAXBElement<AltaRemesaEnvios>(_AltaRemesaEnvios_QNAME, AltaRemesaEnvios.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/altaRemesaEnvios", name = "fechaCreacion", scope = ResultadoAltaRemesaEnvios.class)
    public JAXBElement<XMLGregorianCalendar> createResultadoAltaRemesaEnviosFechaCreacion(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ResultadoAltaRemesaEnviosFechaCreacion_QNAME, XMLGregorianCalendar.class, ResultadoAltaRemesaEnvios.class, value);
    }

}
