package es.stpa.servicios.clave;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.opensaml.saml2.core.LogoutResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;


import es.stpa.servicios.clave.exceptions.ProcesoException;
import es.stpa.servicios.clave.log.ILog;
import es.stpa.servicios.clave.preferencias.Preferencias;
import es.stpa.servicios.clave.sp.SpProtocolEngineFactory;
import eu.eidas.auth.commons.EidasStringUtil;
import eu.eidas.auth.commons.attribute.AttributeDefinition;
import eu.eidas.auth.commons.attribute.AttributeValue;
import eu.eidas.auth.commons.attribute.impl.LiteralStringAttributeValue;
import eu.eidas.auth.commons.attribute.impl.StringAttributeValue;
import eu.eidas.auth.commons.protocol.IAuthenticationResponseNoMetadata;
import eu.eidas.auth.commons.xml.DocumentBuilderFactoryUtil;
import eu.eidas.auth.engine.ProtocolEngineNoMetadataI;
import eu.eidas.auth.engine.xml.opensaml.SAMLEngineUtils;

import eu.eidas.engine.exceptions.EIDASSAMLEngineException;

public class GestorRespuesta {

	private Preferencias pref;
	private ILog log;
	private String idLlamada;
	
	public GestorRespuesta(Preferencias pref, ILog log, String idLlamada){
		this.pref= pref;
		this.log= log;
		this.idLlamada= idLlamada;
	}
	/**
	 * Datos de la respuesta a enviar al llamante
	 * @author crubencvs
	 *
	 */
	public static class DatosRespuesta {
		private boolean esError=false;
		private String mensajeError;
		private DatosRespuestaType datos= new DatosRespuestaType();
		public boolean esError() {
			return esError;
		}
		public void setEsError(boolean esError) {
			this.esError = esError;
		}
		public String getMensajeError() {
			return mensajeError;
		}
		public void setMensajeError(String mensajeError) {
			this.mensajeError = mensajeError;
		}
		public DatosRespuestaType getDatos() {
			return datos;
		}
		public void setDatos(DatosRespuestaType datos) {
			this.datos = datos;
		}
		
	}
	
    /**
     * Se procesa la respuesta de autenticación o la de logout.
     * Si en la petición original no se han indicado url de destino y aplicación, y por tanto se han informado
     * con valores por defecto, aquí se hará lo mismo para poder trabajar con los mismos valores.
     * @param samlResponse
     * @param logoutResponse
     * @param peticionOriginal
     * @param ipCliente
     * @return
     * @throws ProcesoException
     */
	public DatosRespuesta procesarRespuesta(String samlResponse, String logoutResponse, PeticionGeneracionType peticionOriginal, String ipCliente) throws ProcesoException{
		
		DatosRespuesta respuesta= new DatosRespuesta();
		
		ImmutableMap<AttributeDefinition<?>, ImmutableSet<? extends AttributeValue<?>>> attrMap = null;
		
		//Si no han pasado datos de url de servicio y proveedor de servicio en la petición original,
		//se cogen los valores por defecto.
		String nodeServiceUrl = peticionOriginal.getUrlServicio();
		String providerName = peticionOriginal.getIdProveedorServicio();
		if (nodeServiceUrl==null || "".equals(nodeServiceUrl.trim())){
			nodeServiceUrl = pref.getUrlServicio();
		}
		if (providerName==null || "".equals(providerName.trim())){
			providerName = pref.getProveedorServicio();
		}
		
		log.debug("Instanciamos el motor SAML2");
		ProtocolEngineNoMetadataI protocolEngine = SpProtocolEngineFactory.getSpProtocolEngine(pref);
		if (samlResponse != null && !samlResponse.trim().isEmpty()) {
			log.info("Validación del token de autenticación");
			log.debug("Se decodifica y valida el token");
			byte[] decSamlToken = EidasStringUtil.decodeBytesFromBase64(samlResponse);
			IAuthenticationResponseNoMetadata authnResponse = null;
			try {	
				authnResponse = protocolEngine.unmarshallResponseAndValidate(decSamlToken, ipCliente, 0, 0,peticionOriginal.getUrlRetorno());
				
			} catch (EIDASSAMLEngineException e){
				log.error ("Error en la validación del token de respuesta:"+e.getMessage(),e);
				throw new ProcesoException("Error de validación de token de respuesta:"+ e.getMessage());
			}
			
			if (authnResponse.isFailure()) {
				log.error ("Respuesta SAML fallida:"+authnResponse.getStatusMessage());
				throw new ProcesoException("Respuesta Saml fallida:"+ authnResponse.getStatusMessage());
			} else {
				attrMap = authnResponse.getAttributes().getAttributeMap();
			}
			log.info("Token correcto");
			String samlId = SAMLEngineUtils.generateNCName();
			log.debug("Generamos petición de logout");
			String logoutRequest = generateLogout(peticionOriginal.getUrlRetorno(),
					providerName, 
                    nodeServiceUrl,
					samlId, protocolEngine);
			
			log.debug("Procesamos atributos y los enviamos a la salida");
			respuesta.getDatos().setLogoutRequest(logoutRequest);
			extraerAtributos(attrMap, respuesta.getDatos());
		    
		} else if (logoutResponse != null && !logoutResponse.trim().isEmpty()){
			log.info("Validación del token de logout");
			LogoutResponse logoutResp = null;
			try {
				log.debug("Decodificamos y validamos el token");
				byte[] decSamlToken = EidasStringUtil.decodeBytesFromBase64(logoutResponse);
				//validate SAML Token
				logoutResp = protocolEngine.unmarshallLogoutResponseAndValidate(decSamlToken, 
						ipCliente, 0, 0, peticionOriginal.getUrlRetorno());
			    log.debug ("No falla, luego damos por buena");
			    respuesta.setEsError(false);
			} catch (Exception e) {
				log.error("No se pudo validar la respuesta de logout:"+e.getMessage(),e);
				throw new ProcesoException("No se pudo validar la respuesta de logout:"+ e.getMessage());
			}
			log.debug("Token válido");
		} else {
			log.debug("No se ha indicado un token SAML o Logout");
			throw new ProcesoException ("Es necesario indicar un token SAML o token de Logout");
		}
		return respuesta;
	}
	
	/**
	 * Genera una petición de logout que puede enviarse a Cl@ve
	 * @param assertionConsumerUrl
	 * @param providerName
	 * @param destination
	 * @param nonce
	 * @param protocolEngine
	 * @return
	 * @throws ProcesoException
	 */
	private String generateLogout(String assertionConsumerUrl, String providerName, 
			String destination, String nonce, ProtocolEngineNoMetadataI protocolEngine) throws ProcesoException{
		try {
			final byte[] token = protocolEngine.generateLogoutRequestMessage(assertionConsumerUrl, providerName,
					destination, nonce);
			return EidasStringUtil.encodeToBase64(token);
		} catch (EIDASSAMLEngineException e) {
			throw new ProcesoException("No se puede generar token para petición SAML:"+
					e.getMessage());
		}
	}
	
	/** 
	 * Extrae los atributos de la respuesta (Apellidos, nombre...) y los devuelve en la estructura
	 * @param attrMap
	 * @param datOut
	 */
	private void extraerAtributos(ImmutableMap<AttributeDefinition<?>, ImmutableSet<? extends AttributeValue<?>>> attrMap,DatosRespuestaType datOut) throws ProcesoException{
		try {
			if (attrMap==null){
				return;
			}
			Map<String, String> atributos= new HashMap<String, String>();
			ImmutableSet<AttributeDefinition<?>> keys=attrMap.keySet();
			for (AttributeDefinition<?> key: keys){
				if (key.getFriendlyName()!=null && !key.getFriendlyName().trim().isEmpty()){
					ImmutableSet<? extends AttributeValue<?>> entry= attrMap.get(key);
					//Esperamos que la entrada tenga un solo elemento, y que sea de tipo StringAttributeValue
					if (entry.size()==1){
						if (entry.toArray()[0] instanceof StringAttributeValue){
							StringAttributeValue val = (StringAttributeValue)entry.toArray()[0];
							atributos.put(key.getFriendlyName().trim(), val.getValue());
						}
						
						//PartialAfirma no es un StringAttributeValue.
						//Podría usar AbstractAttributeValue
						if (entry.toArray()[0] instanceof LiteralStringAttributeValue){
							LiteralStringAttributeValue val= (LiteralStringAttributeValue) entry.toArray()[0];
							atributos.put(key.getFriendlyName().trim(), val.getValue());
						}
						
					}
				}
	
			}
			
			datOut.setApellidos(atributos.get("FamilyName"));
			datOut.setNombre(atributos.get("FirstName"));
			datOut.setIdentificador(atributos.get("PersonIdentifier"));
			datOut.setPrimerApellido(atributos.get("FirstSurname"));
			datOut.setProveedorIdentidad(atributos.get("SelectedIdP"));
			datOut.setRelayState(atributos.get("RelayState"));

			//También lo devuelvo desglosado
			datOut.setDesgloseInfo(new EntradasDesglose());
			List<EntradaDesglose> entradas= datOut.getDesgloseInfo().getEntrada();
			for (Entry<String,String> e: atributos.entrySet()){
				EntradaDesglose entrada= new EntradaDesglose();
				entrada.setClave(e.getKey());
				entrada.setValor(e.getValue());
				entradas.add(entrada);
			}
			
			if (atributos.containsKey("PartialAfirma")){
				Map<String,String> afirma= extraerAFirma(atributos.get("PartialAfirma"));
					if (afirma!=null){
					    for( Entry<String, String> entry: afirma.entrySet()){
					    	EntradaDesglose entrada= new EntradaDesglose();
					    	
					    	entrada.setClave(entry.getKey());
					    	entrada.setValor(entry.getValue());
					    	entradas.add(entrada);
					    }
					}
			}
			
		}
		catch (Exception e){
			throw new ProcesoException(e);
		}
	}
	/**
	 * Extrae los componentes de la respuesta parcial de Afirma que puede venir en la respuesta de Cl@ve
	 * @param base64
	 * @return
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws XPathExpressionException
	 */
	private Map<String,String> extraerAFirma(String base64) throws SAXException, ParserConfigurationException, IOException, XPathExpressionException{
		//No comprobamos errores, sólo devolvemos atributos si hay.
		Map<String,String> m = new HashMap<String,String>();
		Document doc = DocumentBuilderFactoryUtil.parse(EidasStringUtil.decodeBytesFromBase64(base64));
		XPath xpath=XPathFactory.newInstance().newXPath();
		NodeList nodos=(NodeList)xpath.evaluate("/*[local-name()='Partial_Afirma_Response']/*[local-name()='ReadableCertificateInfo']/*[local-name()='ReadableField']", doc,XPathConstants.NODESET);
		for (int i=0;i<nodos.getLength();i++){
			Node n= nodos.item(i);
			String id= (String)xpath.evaluate("*[local-name()='FieldIdentity']/text()", n, XPathConstants.STRING);
			String valor= (String)xpath.evaluate("*[local-name()='FieldValue']/text()", n,XPathConstants.STRING);
			if (id!=null && !id.trim().isEmpty()){
				m.put(id, valor);
			}
		}
		return m;
	}
}
