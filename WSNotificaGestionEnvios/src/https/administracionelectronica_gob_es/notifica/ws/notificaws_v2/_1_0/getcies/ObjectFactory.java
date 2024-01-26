
package https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.getcies;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.getcies package. 
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

    private final static QName _GetCies_QNAME = new QName("https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/getCies", "getCies");
    private final static QName _ResultadoGetCies_QNAME = new QName("https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/getCies", "resultadoGetCies");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: https.administracionelectronica_gob_es.notifica.ws.notificaws_v2._1_0.getcies
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetCies }
     * 
     */
    public GetCies createGetCies() {
        return new GetCies();
    }

    /**
     * Create an instance of {@link ResultadoGetCies }
     * 
     */
    public ResultadoGetCies createResultadoGetCies() {
        return new ResultadoGetCies();
    }

    /**
     * Create an instance of {@link Cies }
     * 
     */
    public Cies createCies() {
        return new Cies();
    }

    /**
     * Create an instance of {@link Cie }
     * 
     */
    public Cie createCie() {
        return new Cie();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCies }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/getCies", name = "getCies")
    public JAXBElement<GetCies> createGetCies(GetCies value) {
        return new JAXBElement<GetCies>(_GetCies_QNAME, GetCies.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultadoGetCies }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://administracionelectronica.gob.es/notifica/ws/notificaws_v2/1.0/getCies", name = "resultadoGetCies")
    public JAXBElement<ResultadoGetCies> createResultadoGetCies(ResultadoGetCies value) {
        return new JAXBElement<ResultadoGetCies>(_ResultadoGetCies_QNAME, ResultadoGetCies.class, null, value);
    }

}
