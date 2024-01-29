
package es.tributasenasturias.seguridad.firma;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * Oracle JAX-WS 2.1.3-06/19/2008 07:03 PM(bt)
 * Generated source version: 2.1
 * 
 */
@WebService(name = "FirmaDigital", targetNamespace = "http://localhost:7001")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface FirmaDigital {


    /**
     * 
     * @param passwordCertificado
     * @param identificadorCertificado
     * @param xmlData
     * @return
     *     returns java.lang.String
     * @throws Exception_Exception
     */
    @WebMethod(operationName = "Firmar")
    @WebResult(name = "xmlFirmado", targetNamespace = "")
    @RequestWrapper(localName = "Firmar", targetNamespace = "http://localhost:7001", className = "es.tributasenasturias.seguridad.firma.Firmar")
    @ResponseWrapper(localName = "FirmarResponse", targetNamespace = "http://localhost:7001", className = "es.tributasenasturias.seguridad.firma.FirmarResponse")
    public String firmar(
        @WebParam(name = "xmlData", targetNamespace = "")
        String xmlData,
        @WebParam(name = "identificadorCertificado", targetNamespace = "")
        String identificadorCertificado,
        @WebParam(name = "passwordCertificado", targetNamespace = "")
        String passwordCertificado)
        throws Exception_Exception
    ;

    /**
     * 
     * @param xmlData
     * @return
     *     returns boolean
     * @throws Exception_Exception
     */
    @WebMethod(operationName = "Validar")
    @WebResult(name = "esValido", targetNamespace = "")
    @RequestWrapper(localName = "Validar", targetNamespace = "http://localhost:7001", className = "es.tributasenasturias.seguridad.firma.Validar")
    @ResponseWrapper(localName = "ValidarResponse", targetNamespace = "http://localhost:7001", className = "es.tributasenasturias.seguridad.firma.ValidarResponse")
    public boolean validar(
        @WebParam(name = "xmlData", targetNamespace = "")
        String xmlData)
        throws Exception_Exception
    ;

    /**
     * 
     * @param passwordCertificado
     * @param rutaCertificado
     * @param aliasCertificado
     * @param rutaClavePrivada
     * @throws Exception_Exception
     */
    @WebMethod(operationName = "InsertarCertificado")
    @RequestWrapper(localName = "InsertarCertificado", targetNamespace = "http://localhost:7001", className = "es.tributasenasturias.seguridad.firma.InsertarCertificado")
    @ResponseWrapper(localName = "InsertarCertificadoResponse", targetNamespace = "http://localhost:7001", className = "es.tributasenasturias.seguridad.firma.InsertarCertificadoResponse")
    public void insertarCertificado(
        @WebParam(name = "rutaCertificado", targetNamespace = "")
        String rutaCertificado,
        @WebParam(name = "rutaClavePrivada", targetNamespace = "")
        String rutaClavePrivada,
        @WebParam(name = "aliasCertificado", targetNamespace = "")
        String aliasCertificado,
        @WebParam(name = "passwordCertificado", targetNamespace = "")
        String passwordCertificado)
        throws Exception_Exception
    ;

    /**
     * 
     * @param xmlData
     * @param aliasCertificado
     * @param resultado
     * @param xmlFirmado
     */
    @WebMethod(operationName = "FirmarSOAP")
    @RequestWrapper(localName = "FirmarSOAP", targetNamespace = "http://localhost:7001", className = "es.tributasenasturias.seguridad.firma.FirmarSOAP")
    @ResponseWrapper(localName = "FirmarSOAPResponse", targetNamespace = "http://localhost:7001", className = "es.tributasenasturias.seguridad.firma.FirmarSOAPResponse")
    public void firmarSOAP(
        @WebParam(name = "xmlData", targetNamespace = "")
        String xmlData,
        @WebParam(name = "aliasCertificado", targetNamespace = "")
        String aliasCertificado,
        @WebParam(name = "xmlFirmado", targetNamespace = "", mode = WebParam.Mode.OUT)
        Holder<String> xmlFirmado,
        @WebParam(name = "resultado", targetNamespace = "", mode = WebParam.Mode.OUT)
        Holder<String> resultado);

    /**
     * 
     * @param passwordCertificado
     * @param identificadorCertificado
     * @param xmlData
     * @return
     *     returns java.lang.String
     * @throws Exception_Exception
     */
    @WebMethod(operationName = "FirmarAncert")
    @WebResult(name = "xmlFirmado", targetNamespace = "")
    @RequestWrapper(localName = "FirmarAncert", targetNamespace = "http://localhost:7001", className = "es.tributasenasturias.seguridad.firma.FirmarAncert")
    @ResponseWrapper(localName = "FirmarAncertResponse", targetNamespace = "http://localhost:7001", className = "es.tributasenasturias.seguridad.firma.FirmarAncertResponse")
    public String firmarAncert(
        @WebParam(name = "xmlData", targetNamespace = "")
        String xmlData,
        @WebParam(name = "identificadorCertificado", targetNamespace = "")
        String identificadorCertificado,
        @WebParam(name = "passwordCertificado", targetNamespace = "")
        String passwordCertificado)
        throws Exception_Exception
    ;

    /**
     * 
     * @param passwordCertificado
     * @param identificadorCertificado
     * @param xmlData
     * @param firmarComoBinario
     * @return
     *     returns java.lang.String
     * @throws Exception_Exception
     */
    @WebMethod(operationName = "FirmarCIRCE")
    @WebResult(name = "contenidoFirmado", targetNamespace = "")
    @RequestWrapper(localName = "FirmarCIRCE", targetNamespace = "http://localhost:7001", className = "es.tributasenasturias.seguridad.firma.FirmarCIRCE")
    @ResponseWrapper(localName = "FirmarCIRCEResponse", targetNamespace = "http://localhost:7001", className = "es.tributasenasturias.seguridad.firma.FirmarCIRCEResponse")
    public String firmarCIRCE(
        @WebParam(name = "xmlData", targetNamespace = "")
        String xmlData,
        @WebParam(name = "identificadorCertificado", targetNamespace = "")
        String identificadorCertificado,
        @WebParam(name = "passwordCertificado", targetNamespace = "")
        String passwordCertificado,
        @WebParam(name = "firmarComoBinario", targetNamespace = "")
        boolean firmarComoBinario)
        throws Exception_Exception
    ;

    /**
     * 
     * @param nodoPadre
     * @param idNodoAFirmar
     * @param nsNodoPadre
     * @param xmlData
     * @param aliasCertificado
     * @return
     *     returns java.lang.String
     * @throws Exception_Exception
     */
    @WebMethod(operationName = "FirmarXML")
    @WebResult(name = "xmlFirmado", targetNamespace = "")
    @RequestWrapper(localName = "FirmarXML", targetNamespace = "http://localhost:7001", className = "es.tributasenasturias.seguridad.firma.FirmarXML")
    @ResponseWrapper(localName = "FirmarXMLResponse", targetNamespace = "http://localhost:7001", className = "es.tributasenasturias.seguridad.firma.FirmarXMLResponse")
    public String firmarXML(
        @WebParam(name = "xmlData", targetNamespace = "")
        String xmlData,
        @WebParam(name = "aliasCertificado", targetNamespace = "")
        String aliasCertificado,
        @WebParam(name = "idNodoAFirmar", targetNamespace = "")
        String idNodoAFirmar,
        @WebParam(name = "nodoPadre", targetNamespace = "")
        String nodoPadre,
        @WebParam(name = "nsNodoPadre", targetNamespace = "")
        String nsNodoPadre)
        throws Exception_Exception
    ;

}