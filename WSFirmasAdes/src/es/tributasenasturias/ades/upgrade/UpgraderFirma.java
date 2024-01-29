package es.tributasenasturias.ades.upgrade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import oasis.names.tc.dss._1_0.core.schema.DSSSignature;
import oasis.names.tc.dss._1_0.core.schema.DSSSignatureService;
import es.gob.afirma.core.misc.Base64;
import es.tributasenasturias.ades.XMLUtils;
import es.tributasenasturias.ades.exceptions.UpgradeException;
import es.tributasenasturias.ades.preferencias.Preferencias;
import es.tributasenasturias.ades.preferencias.PreferenciasException;
import es.tributasenasturias.ades.soap.Seguridad;
import es.tributasenasturias.log.ILog;

/**
 * Clase para realizar una actualización de firma BES o EPES a una forma superior de AdES.
 * @author crubencvs
 *
 */
public class UpgraderFirma {

	private DSSSignatureService service;
	private DSSSignature port;
	private Preferencias pref;
	private String resultadoUpgrade;
	private String textoResultadoUpgrade;
	private boolean respuestaOk=false;
	
	
	public String getResultadoUpgrade() {
		return resultadoUpgrade;
	}
	public String getTextoResultadoUpgrade() {
		return textoResultadoUpgrade;
	}
	
	public boolean isRespuestaOk() {
		return respuestaOk;
	}
	
	private UpgraderFirma(Preferencias pref,ILog log, String idLlamada) {
		this.pref= pref;
	}
	/**
	 * Genera una nueva instancia de UpgraderFirma. Realizará toda la configuración necesaria para su funcionamiento.
	 * @param pref Preferencias
	 * @param log Log del servicio
	 * @param manejador Manejador a añadir en las comunicaciones con el servicio de actualización de firma
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static UpgraderFirma newInstance(Preferencias pref, ILog log, String idLlamada,SOAPHandler<SOAPMessageContext> manejador){
		UpgraderFirma up= new UpgraderFirma(pref, log, idLlamada);
		up.service= new DSSSignatureService();
		up.port= up.service.getDSSAfirmaVerify();
		BindingProvider bp= (BindingProvider) up.port;
		String endpoint= pref.getEndpointUpgradeAfirma();
		if (endpoint!=null && !"".equals(endpoint)){
			bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
		}
		if (manejador!=null) {
			Binding b= bp.getBinding();
			List<Handler> hns= (List<Handler>)b.getHandlerChain();
			if (hns==null) {
				hns= new ArrayList<Handler>();
			}
			//Se añade siempre el de seguridad de mensaje, antes del de log
			hns.add(new Seguridad(pref, log, idLlamada));
			hns.add(manejador);
			b.setHandlerChain(hns);
		}
		return up;
	}
	
	/*
	 * Construye una petición en base al formato
	 * <dss:VerifyRequest
		Profile="urn:afirma:dss:1.0:profile:XSS"
		xmlns:dss="urn:oasis:names:tc:dss:1.0:core:schema"
		xmlns:afxp="urn:afirma:dss:1.0:profile:XSS:schema"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		    <dss:InputDocuments><dss:Document ID="xxxxxx-xx"><dss:Base64XML></dss:Base64XML></dss:Document></dss:InputDocuments>
			<dss:OptionalInputs>
				<dss:ClaimedIdentity>
					<dss:Name></dss:Name>
				</dss:ClaimedIdentity>
				<dss:ReturnUpdatedSignature Type=""/>
			</dss:OptionalInputs>
			<dss:SignatureObject>
				<dss:SignaturePtr WhichDocument="xxxxxx-xx"></dss:SignaturePtr>
			</dss:SignatureObject>
		</dss:VerifyRequest>
	 */
	/*private String construirPeticionXades(byte[] xmlFirmado, String uri) throws IOException,JAXBException{
		String uid= UUID.randomUUID().toString().substring(0, 4);
		VerifyRequest verifyRequest= new VerifyRequest();
		//Input Document
		verifyRequest.setInputDocuments(new InputDocuments());
		DocumentType document= new DocumentType();
		document.setBase64XML(xmlFirmado);
		document.setID(uid);
		verifyRequest.getInputDocuments().getDocumentOrTransformedDataOrDocumentHash().add(document);
		//OptionalInputs. Identidad de la petición y tipo AdES al que queremos llegar
		AnyType optionalInputs = new AnyType();
		ClaimedIdentity claimedIdentity= new ClaimedIdentity();
		NameIdentifierType name= new NameIdentifierType();
		name.setValue(pref.getIdAplicacion());
		claimedIdentity.setName(name);
		ReturnUpdatedSignature returnUpdatedSignature= new ReturnUpdatedSignature();
		returnUpdatedSignature.setType(uri);
		optionalInputs.getAny().add(claimedIdentity);
		optionalInputs.getAny().add(returnUpdatedSignature);
		//Para que a_firma no valide el certificado
		JAXBElement<String> updatedSignatureMethod=new JAXBElement<String>(new QName("urn:afirma:dss:1.0:profile:XSS:schema", "UpdatedSignatureMode"), String.class, null, "urn:afirma:dss:1.0:profile:XSS:upgrade:NoCertificateValidation");
		// Y para que no compruebe el periodo de gracia correspondiente a la política de firma según la cual se ha generado, y que si lo comprueba, el verificador puede esperar ese tiempo, que puede ser al menos el de refresco de la CRL para saber que no está revocado
		JAXBElement<String> ignoreGracePeriod=new JAXBElement<String>(new QName("urn:afirma:dss:1.0:profile:XSS:schema","IgnoreGracePeriod"), String.class, null);
		optionalInputs.getAny().add(updatedSignatureMethod);
		optionalInputs.getAny().add(ignoreGracePeriod);
		verifyRequest.setOptionalInputs(optionalInputs);
		//Referencia al xml indicado en Input Document
		SignatureObject signatureObject= new SignatureObject();
		SignaturePtr sigptr= new SignaturePtr();
		sigptr.setWhichDocument(document);
		signatureObject.setSignaturePtr(sigptr);
		verifyRequest.setSignatureObject(signatureObject);
		//Transformamos a texto
		JAXBContext ctx= JAXBContext.newInstance(VerifyRequest.class.getPackage().getName());
		Marshaller marshaller= ctx.createMarshaller();
		ByteArrayOutputStream bos= new ByteArrayOutputStream();
		marshaller.marshal(verifyRequest, bos);
		String pet= new String(bos.toByteArray(),Charset.forName("UTF-8"));
		bos.close();
		return pet;
	}
	*/
	private String construirPeticionXades(byte[] xmlFirmado, String uri) throws PreferenciasException, IOException, TransformerException{
		Document plantilla= pref.getPlantillaUpgradeXades();
		Node rus= XMLUtils.selectSingleNode(plantilla, "/*[local-name()='VerifyRequest']/*[local-name()='OptionalInputs']/*[local-name()='ReturnUpdatedSignature']");
		//Tipo de upgrade. 
		XMLUtils.setAttributeValue(rus, "Type", uri);
		//Contenido del fichero
		String b64 = Base64.encode(xmlFirmado);
		Node pdf= XMLUtils.selectSingleNode(plantilla, "/*[local-name()='VerifyRequest']/*[local-name()='InputDocuments']/*[local-name()='Document']/*[local-name()='Base64XML']");
		XMLUtils.setNodeText(pdf, b64);
		//Identificador del documento
		String uid= UUID.randomUUID().toString().substring(0, 4);
		String idDoc= "doc"+uid;
		Node document= XMLUtils.selectSingleNode(plantilla, "/*[local-name()='VerifyRequest']/*[local-name()='InputDocuments']/*[local-name()='Document']");
		XMLUtils.setAttributeValue(document, "ID", idDoc);
		Node sdoc = XMLUtils.selectSingleNode(plantilla, "/*[local-name()='VerifyRequest']/*[local-name()='SignatureObject']/*[local-name()='SignaturePtr']/@WhichDocument");
		sdoc.setNodeValue(idDoc);
		String pet= XMLUtils.getXMLText(plantilla);
		return pet;
	}
	/*
	 * Construye una petición en base al formato
	 * <dss:VerifyRequest Profile="urn:afirma:dss:1.0:profile:XSS" xmlns:dss="urn:oasis:names:tc:dss:1.0:core:schema" xmlns:afxp="urn:afirma:dss:1.0:profile:XSS:schema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		<dss:OptionalInputs>
			<dss:ClaimedIdentity>
				<dss:Name></dss:Name>
			</dss:ClaimedIdentity>
			<dss:ReturnUpdatedSignature Type=""/>
		</dss:OptionalInputs>
		<dss:SignatureObject>
			<dss:Base64Signature></dss:Base64Signature>
		</dss:SignatureObject>
		</dss:VerifyRequest>
	 */
	//Ver "resources/ejemplo_plantillaUpdatePades.xml para un ejemplo
	private String construirPeticionPades(byte[] pdfFirmado, String uri) throws TransformerException,PreferenciasException{
		
		Document plantilla= pref.getPlantillaUpgradePades();
		Node rus= XMLUtils.selectSingleNode(plantilla, "/*[local-name()='VerifyRequest']/*[local-name()='OptionalInputs']/*[local-name()='ReturnUpdatedSignature']");
		//Tipo de upgrade. 
		XMLUtils.setAttributeValue(rus, "Type", uri);
		//Contenido del fichero
		String b64 = Base64.encode(pdfFirmado);
		Node pdf= XMLUtils.selectSingleNode(plantilla, "/*[local-name()='VerifyRequest']/*[local-name()='SignatureObject']/*[local-name()='Base64Signature']");
		XMLUtils.setNodeText(pdf, b64);
		String pet= XMLUtils.getXMLText(plantilla);
		return pet;
	}
	
	/**
	 * Actualiza una firma BES o EPES al nivel que le indiquemos
	 * @param xmlFirmado XML firmado con XADES BES o XADES EPES
	 * @param nuevoPerfilXades Nuevo perfil Xades al que llegar (ES-T, ES-X,...)
	 * @return XML firmado con el nivel que le indicamos
	 * @throws UpgradeException
	 */
	public byte[] upgradeXades (byte[] xmlFirmado, String nuevoPerfilXades) throws UpgradeException{
		try {
			Properties formsXades= pref.getUriServicioXades();
			if (formsXades.containsKey(nuevoPerfilXades)){
				
				String uri=formsXades.getProperty(nuevoPerfilXades);
				
				String peti= construirPeticionXades(xmlFirmado, uri);
				String response=port.verify(peti);
				byte[] xml= procesaRespuestaXades(response);
				if (xml==null){
					throw new UpgradeException("Error en actualización:"+ this.textoResultadoUpgrade);
				}
				return xml;
			}
			else 
			{
				throw new UpgradeException("No se reconoce la uri para la forma Xades "+nuevoPerfilXades);
			}
		}catch (PreferenciasException pe){
			throw new UpgradeException ("Error al recuperar las uri de formas Xades:"+ pe.getMessage(),pe);
		} catch (TransformerException te) {
			throw new UpgradeException ("Error de entrada salida en la actualización de firma:"+ te.getMessage(),te);
		}catch (SAXException sx) {
			throw new UpgradeException ("Error de entrada salida en la actualización de firma:"+ sx.getMessage(),sx);
		}catch (IOException io) {
			throw new UpgradeException ("Error de entrada salida en la actualización de firma:"+ io.getMessage(),io);
		} catch (ParserConfigurationException pe) {
			throw new UpgradeException ("Error de entrada salida en la actualización de firma:"+ pe.getMessage(),pe);
		}
	}
	/**
	 * Actualiza una firma BES o EPES al nivel que se indica, en realidad siempre a LTV
	 * @param pdfFirmado Pdf firmado que se desea actualizar
	 * @param nuevoPerfilPades Nuevo perfil
	 * @return
	 * @throws UpgradeException
	 */
	public byte[] upgradePades (byte[] pdfFirmado, String nuevoPerfilPades) throws UpgradeException{
		try {
			Properties formsPades= pref.getUriServicioPades();
			if (formsPades.containsKey(nuevoPerfilPades)){
				
				String uri=formsPades.getProperty(nuevoPerfilPades);
				
				String peti= construirPeticionPades(pdfFirmado, uri);
				String response=port.verify(peti);
				byte[] xml= procesaRespuestaPades(response);
				if (xml==null){
					throw new UpgradeException("Error en actualización:"+ this.textoResultadoUpgrade);
				}
				return xml;
			}
			else 
			{
				throw new UpgradeException("No se reconoce la uri para la forma Pades "+nuevoPerfilPades);
			}
		}catch (PreferenciasException pe){
			throw new UpgradeException ("Error al en preferencias al actualizar la firma Pades:"+ pe.getMessage(),pe);
		} catch(ParserConfigurationException pe) {
			throw new UpgradeException ("Error al transformar XML:" + pe.getMessage() ,pe);
		} catch (IOException i) {
			throw new UpgradeException ("Error al transformar XML:" + i.getMessage() ,i);
		} catch(SAXException s) {
			throw new UpgradeException ("Error al transformar XML:" + s.getMessage() ,s);
		}catch (TransformerException te) {
			throw new UpgradeException ("Error al transformar XML:" + te.getMessage() ,te);
		}
	}
	
	/**
	 * Procesa la respuesta Xades recibida, y devuelve el documento firmado en su caso.
	 * Si no ha podido firmar el documento, indicará el resultado en las variables de resultado de la clase
	 * @param respuesta recibida de @firma
	 * @return 
	 * @throws UpgradeException
	 * @throws JAXBException
	 */
	public byte[] procesaRespuestaXades(String respuesta) throws SAXException, ParserConfigurationException,IOException,UpgradeException{
		Document docRespuesta= XMLUtils.string2Xml(respuesta);
		
		Node n= XMLUtils.selectSingleNode(docRespuesta, "/*[local-name()='VerifyResponse']/*[local-name()='Result']/*[local-name()='ResultMajor']");
		String resultMajor= XMLUtils.getNodeText(n);
		if (resultMajor==null || "".equals(resultMajor)) {
			throw new UpgradeException("No se ha recuperado resultado de la llamada según el esquema esperado.");
		}
		if ("urn:oasis:names:tc:dss:1.0:resultmajor:Success".equals(resultMajor)) {
			//Ha terminado bien
			n= XMLUtils.selectSingleNode(docRespuesta, "/*[local-name()='VerifyResponse']/*[local-name()='OptionalOutputs']/*[local-name()='DocumentWithSignature']/*[local-name()='Document']/*[local-name()='Base64XML']");
			respuestaOk=true;
			String b64= XMLUtils.getNodeText(n);
			return Base64.decode(b64);
		} else {
			respuestaOk=false;
			this.resultadoUpgrade=resultMajor;
			n= XMLUtils.selectSingleNode(docRespuesta, "/*[local-name()='VerifyResponse']/*[local-name()='Result']/*[local-name()='ResultMessage']");
			String resultMessage= XMLUtils.getNodeText(n);
			if (resultMessage!=null) {
				this.textoResultadoUpgrade= resultMessage;
			}
			return null;
		}
	}
	/**
	 * Procesa la respuesta Pades y devuelve el documento firmado. Pone el resultado en las variables <strong>resultadoUpgrade</strong> y <strong>textoResultadoUpgrade</strong> 
	 * @param respuesta Respuesta de @firma
	 * @return PDF firmado o null si no se ha podido firmar
	 * @throws UpgradeException
	 * @throws JAXBException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public byte[] procesaRespuestaPades(String respuesta) throws ParserConfigurationException, SAXException,IOException, UpgradeException{
		Document docRespuesta= XMLUtils.string2Xml(respuesta);
		
		Node n= XMLUtils.selectSingleNode(docRespuesta, "/*[local-name()='VerifyResponse']/*[local-name()='Result']/*[local-name()='ResultMajor']");
		String resultMajor= XMLUtils.getNodeText(n);
		if (resultMajor==null || "".equals(resultMajor)) {
			throw new UpgradeException("No se ha recuperado resultado de la llamada según el esquema esperado.");
		}
		if ("urn:oasis:names:tc:dss:1.0:resultmajor:Success".equals(resultMajor)) {
			//Ha terminado bien
			n= XMLUtils.selectSingleNode(docRespuesta, "/*[local-name()='VerifyResponse']/*[local-name()='OptionalOutputs']/*[local-name()='UpdatedSignature']/*[local-name()='SignatureObject']/*[local-name()='Base64Signature']");
			respuestaOk=true;
			String b64= XMLUtils.getNodeText(n);
			return Base64.decode(b64);
		} else {
			respuestaOk=false;
			this.resultadoUpgrade=resultMajor;
			n= XMLUtils.selectSingleNode(docRespuesta, "/*[local-name()='VerifyResponse']/*[local-name()='Result']/*[local-name()='ResultMessage']");
			String resultMessage= XMLUtils.getNodeText(n);
			if (resultMessage!=null) {
				this.textoResultadoUpgrade= resultMessage;
			}
			return null;
		}
	}
}
