
package es.tributasenasturias.seguridad.firma;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the es.tributasenasturias.seguridad.firma package. 
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

    private final static QName _InsertarCertificado_QNAME = new QName("http://localhost:7001", "InsertarCertificado");
    private final static QName _FirmarAncert_QNAME = new QName("http://localhost:7001", "FirmarAncert");
    private final static QName _FirmarXMLResponse_QNAME = new QName("http://localhost:7001", "FirmarXMLResponse");
    private final static QName _FirmarCIRCEResponse_QNAME = new QName("http://localhost:7001", "FirmarCIRCEResponse");
    private final static QName _FirmarAncertResponse_QNAME = new QName("http://localhost:7001", "FirmarAncertResponse");
    private final static QName _FirmarSOAPResponse_QNAME = new QName("http://localhost:7001", "FirmarSOAPResponse");
    private final static QName _FirmarResponse_QNAME = new QName("http://localhost:7001", "FirmarResponse");
    private final static QName _Firmar_QNAME = new QName("http://localhost:7001", "Firmar");
    private final static QName _InsertarCertificadoResponse_QNAME = new QName("http://localhost:7001", "InsertarCertificadoResponse");
    private final static QName _Validar_QNAME = new QName("http://localhost:7001", "Validar");
    private final static QName _ValidarResponse_QNAME = new QName("http://localhost:7001", "ValidarResponse");
    private final static QName _Exception_QNAME = new QName("http://localhost:7001", "Exception");
    private final static QName _FirmarCIRCE_QNAME = new QName("http://localhost:7001", "FirmarCIRCE");
    private final static QName _FirmarSOAP_QNAME = new QName("http://localhost:7001", "FirmarSOAP");
    private final static QName _FirmarXML_QNAME = new QName("http://localhost:7001", "FirmarXML");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: es.tributasenasturias.seguridad.firma
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link InsertarCertificado }
     * 
     */
    public InsertarCertificado createInsertarCertificado() {
        return new InsertarCertificado();
    }

    /**
     * Create an instance of {@link FirmarSOAP }
     * 
     */
    public FirmarSOAP createFirmarSOAP() {
        return new FirmarSOAP();
    }

    /**
     * Create an instance of {@link ValidarResponse }
     * 
     */
    public ValidarResponse createValidarResponse() {
        return new ValidarResponse();
    }

    /**
     * Create an instance of {@link FirmarSOAPResponse }
     * 
     */
    public FirmarSOAPResponse createFirmarSOAPResponse() {
        return new FirmarSOAPResponse();
    }

    /**
     * Create an instance of {@link FirmarAncert }
     * 
     */
    public FirmarAncert createFirmarAncert() {
        return new FirmarAncert();
    }

    /**
     * Create an instance of {@link FirmarXML }
     * 
     */
    public FirmarXML createFirmarXML() {
        return new FirmarXML();
    }

    /**
     * Create an instance of {@link Validar }
     * 
     */
    public Validar createValidar() {
        return new Validar();
    }

    /**
     * Create an instance of {@link FirmarXMLResponse }
     * 
     */
    public FirmarXMLResponse createFirmarXMLResponse() {
        return new FirmarXMLResponse();
    }

    /**
     * Create an instance of {@link FirmarAncertResponse }
     * 
     */
    public FirmarAncertResponse createFirmarAncertResponse() {
        return new FirmarAncertResponse();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link FirmarResponse }
     * 
     */
    public FirmarResponse createFirmarResponse() {
        return new FirmarResponse();
    }

    /**
     * Create an instance of {@link InsertarCertificadoResponse }
     * 
     */
    public InsertarCertificadoResponse createInsertarCertificadoResponse() {
        return new InsertarCertificadoResponse();
    }

    /**
     * Create an instance of {@link FirmarCIRCEResponse }
     * 
     */
    public FirmarCIRCEResponse createFirmarCIRCEResponse() {
        return new FirmarCIRCEResponse();
    }

    /**
     * Create an instance of {@link Firmar }
     * 
     */
    public Firmar createFirmar() {
        return new Firmar();
    }

    /**
     * Create an instance of {@link FirmarCIRCE }
     * 
     */
    public FirmarCIRCE createFirmarCIRCE() {
        return new FirmarCIRCE();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InsertarCertificado }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost:7001", name = "InsertarCertificado")
    public JAXBElement<InsertarCertificado> createInsertarCertificado(InsertarCertificado value) {
        return new JAXBElement<InsertarCertificado>(_InsertarCertificado_QNAME, InsertarCertificado.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FirmarAncert }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost:7001", name = "FirmarAncert")
    public JAXBElement<FirmarAncert> createFirmarAncert(FirmarAncert value) {
        return new JAXBElement<FirmarAncert>(_FirmarAncert_QNAME, FirmarAncert.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FirmarXMLResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost:7001", name = "FirmarXMLResponse")
    public JAXBElement<FirmarXMLResponse> createFirmarXMLResponse(FirmarXMLResponse value) {
        return new JAXBElement<FirmarXMLResponse>(_FirmarXMLResponse_QNAME, FirmarXMLResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FirmarCIRCEResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost:7001", name = "FirmarCIRCEResponse")
    public JAXBElement<FirmarCIRCEResponse> createFirmarCIRCEResponse(FirmarCIRCEResponse value) {
        return new JAXBElement<FirmarCIRCEResponse>(_FirmarCIRCEResponse_QNAME, FirmarCIRCEResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FirmarAncertResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost:7001", name = "FirmarAncertResponse")
    public JAXBElement<FirmarAncertResponse> createFirmarAncertResponse(FirmarAncertResponse value) {
        return new JAXBElement<FirmarAncertResponse>(_FirmarAncertResponse_QNAME, FirmarAncertResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FirmarSOAPResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost:7001", name = "FirmarSOAPResponse")
    public JAXBElement<FirmarSOAPResponse> createFirmarSOAPResponse(FirmarSOAPResponse value) {
        return new JAXBElement<FirmarSOAPResponse>(_FirmarSOAPResponse_QNAME, FirmarSOAPResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FirmarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost:7001", name = "FirmarResponse")
    public JAXBElement<FirmarResponse> createFirmarResponse(FirmarResponse value) {
        return new JAXBElement<FirmarResponse>(_FirmarResponse_QNAME, FirmarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Firmar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost:7001", name = "Firmar")
    public JAXBElement<Firmar> createFirmar(Firmar value) {
        return new JAXBElement<Firmar>(_Firmar_QNAME, Firmar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InsertarCertificadoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost:7001", name = "InsertarCertificadoResponse")
    public JAXBElement<InsertarCertificadoResponse> createInsertarCertificadoResponse(InsertarCertificadoResponse value) {
        return new JAXBElement<InsertarCertificadoResponse>(_InsertarCertificadoResponse_QNAME, InsertarCertificadoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Validar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost:7001", name = "Validar")
    public JAXBElement<Validar> createValidar(Validar value) {
        return new JAXBElement<Validar>(_Validar_QNAME, Validar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidarResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost:7001", name = "ValidarResponse")
    public JAXBElement<ValidarResponse> createValidarResponse(ValidarResponse value) {
        return new JAXBElement<ValidarResponse>(_ValidarResponse_QNAME, ValidarResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost:7001", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FirmarCIRCE }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost:7001", name = "FirmarCIRCE")
    public JAXBElement<FirmarCIRCE> createFirmarCIRCE(FirmarCIRCE value) {
        return new JAXBElement<FirmarCIRCE>(_FirmarCIRCE_QNAME, FirmarCIRCE.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FirmarSOAP }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost:7001", name = "FirmarSOAP")
    public JAXBElement<FirmarSOAP> createFirmarSOAP(FirmarSOAP value) {
        return new JAXBElement<FirmarSOAP>(_FirmarSOAP_QNAME, FirmarSOAP.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FirmarXML }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://localhost:7001", name = "FirmarXML")
    public JAXBElement<FirmarXML> createFirmarXML(FirmarXML value) {
        return new JAXBElement<FirmarXML>(_FirmarXML_QNAME, FirmarXML.class, null, value);
    }

}
