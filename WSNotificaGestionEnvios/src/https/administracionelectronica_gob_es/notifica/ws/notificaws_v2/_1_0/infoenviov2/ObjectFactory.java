
package https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2 package. 
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

    private final static QName _ResultadoInfoEnvioV2_QNAME = new QName("https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/infoEnvioV2", "resultadoInfoEnvioV2");
    private final static QName _InfoEnvioV2_QNAME = new QName("https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/infoEnvioV2", "infoEnvioV2");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.infoenviov2
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link InfoEnvioV2 }
     * 
     */
    public InfoEnvioV2 createInfoEnvioV2() {
        return new InfoEnvioV2();
    }

    /**
     * Create an instance of {@link ResultadoInfoEnvioV2 }
     * 
     */
    public ResultadoInfoEnvioV2 createResultadoInfoEnvioV2() {
        return new ResultadoInfoEnvioV2();
    }

    /**
     * Create an instance of {@link Datados }
     * 
     */
    public Datados createDatados() {
        return new Datados();
    }

    /**
     * Create an instance of {@link Opcion }
     * 
     */
    public Opcion createOpcion() {
        return new Opcion();
    }

    /**
     * Create an instance of {@link Datado }
     * 
     */
    public Datado createDatado() {
        return new Datado();
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
     * Create an instance of {@link Certificacion }
     * 
     */
    public Certificacion createCertificacion() {
        return new Certificacion();
    }

    /**
     * Create an instance of {@link Procedimiento }
     * 
     */
    public Procedimiento createProcedimiento() {
        return new Procedimiento();
    }

    /**
     * Create an instance of {@link OrganismoPagadorPostal }
     * 
     */
    public OrganismoPagadorPostal createOrganismoPagadorPostal() {
        return new OrganismoPagadorPostal();
    }

    /**
     * Create an instance of {@link CodigoDIR }
     * 
     */
    public CodigoDIR createCodigoDIR() {
        return new CodigoDIR();
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
     * Create an instance of {@link Destinatario }
     * 
     */
    public Destinatario createDestinatario() {
        return new Destinatario();
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
     * Create an instance of {@link OrganismoPagadorCIE }
     * 
     */
    public OrganismoPagadorCIE createOrganismoPagadorCIE() {
        return new OrganismoPagadorCIE();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultadoInfoEnvioV2 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/infoEnvioV2", name = "resultadoInfoEnvioV2")
    public JAXBElement<ResultadoInfoEnvioV2> createResultadoInfoEnvioV2(ResultadoInfoEnvioV2 value) {
        return new JAXBElement<ResultadoInfoEnvioV2>(_ResultadoInfoEnvioV2_QNAME, ResultadoInfoEnvioV2 .class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InfoEnvioV2 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/infoEnvioV2", name = "infoEnvioV2")
    public JAXBElement<InfoEnvioV2> createInfoEnvioV2(InfoEnvioV2 value) {
        return new JAXBElement<InfoEnvioV2>(_InfoEnvioV2_QNAME, InfoEnvioV2 .class, null, value);
    }

}
