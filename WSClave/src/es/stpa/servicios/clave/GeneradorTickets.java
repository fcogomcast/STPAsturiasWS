package es.stpa.servicios.clave;

import es.stpa.servicios.clave.exceptions.ProcesoException;
import es.stpa.servicios.clave.log.ILog;
import es.stpa.servicios.clave.preferencias.Preferencias;
import es.stpa.servicios.clave.sp.SpProtocolEngineFactory;
import eu.eidas.auth.commons.EidasStringUtil;
import eu.eidas.auth.commons.attribute.AttributeDefinition;
import eu.eidas.auth.commons.attribute.ImmutableAttributeMap;
import eu.eidas.auth.commons.attribute.PersonType;
import eu.eidas.auth.commons.attribute.impl.StringAttributeValueMarshaller;
import eu.eidas.auth.commons.protocol.IRequestMessageNoMetadata;
import eu.eidas.auth.commons.protocol.eidas.LevelOfAssurance;
import eu.eidas.auth.commons.protocol.eidas.LevelOfAssuranceComparison;
import eu.eidas.auth.commons.protocol.eidas.impl.EidasAuthenticationRequestNoMetadata;
import eu.eidas.auth.commons.protocol.impl.EidasSamlBinding;
import eu.eidas.auth.commons.protocol.impl.SamlNameIdFormat;
import eu.eidas.auth.engine.ProtocolEngineNoMetadataI;
import eu.eidas.auth.engine.xml.opensaml.SAMLEngineUtils;
import eu.eidas.auth.engine.xml.opensaml.SecureRandomXmlIdGenerator;
import eu.eidas.engine.exceptions.EIDASSAMLEngineException;

public class GeneradorTickets {

	private Preferencias pref;
	private ILog log;
	private String idLlamada;
	
	public GeneradorTickets(Preferencias pr, ILog log, String idLlamada){
		this.pref = pr;
		this.log= log;
		this.idLlamada= idLlamada;
	}
	
	/** 
	 * Resultado de la generación
	 * @author crubencvs
	 *
	 */
	public static class ResultadoGeneracion{
		private boolean esError=false;
		private String mensajeError;
		private ResultadoGeneracionType resultado= new ResultadoGeneracionType();
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
		public ResultadoGeneracionType getResultado() {
			return resultado;
		}
		public void setResultado(ResultadoGeneracionType resultado) {
			this.resultado = resultado;
		}
		
		
	}
	
	public ResultadoGeneracion generarTicket(PeticionGeneracionType peticion) throws ProcesoException
	{
		log.info("Inicio de la generación de ticket");
		ResultadoGeneracion result = new ResultadoGeneracion();
		String nodeServiceUrl = peticion.getUrlServicio();
		String providerName = peticion.getIdProveedorServicio();
		String spApplication = peticion.getNombreAplicacion();
		String returnUrl = peticion.getUrlRetorno();
		String eidasloa = peticion.getNivelCalidadAutenticacion();
		String nameIDPolicy = peticion.getPoliticaNombres();
		String relayState="";
		
		if (nodeServiceUrl==null || "".equals(nodeServiceUrl.trim())){
			nodeServiceUrl = pref.getUrlServicio();
		}
		if (providerName==null || "".equals(providerName.trim())){
			providerName = pref.getProveedorServicio();
		}
		//En principio no se falla por no poner ningún proveedor. Generará una petición que no
		//permitirá validar en Cl@ve.

		ImmutableAttributeMap.Builder reqAttrMapBuilder = new ImmutableAttributeMap.Builder();
		log.debug("Construcción de lista de proveedores de identidad admitidos");
		if (peticion.getIdP()!=null){
			if (!peticion.getIdP().isAFirma()) {
				log.debug("No admitimos certificado");
	        	reqAttrMapBuilder.put(new AttributeDefinition.Builder<String>().nameUri("http://es.minhafp.clave/AFirmaIdP")
	        			.friendlyName("AFirmaIdP")
	        			.personType(PersonType.NATURAL_PERSON)
	        			.required(false)
	        			.uniqueIdentifier(true)
	        			.xmlType("http://www.w3.org/2001/XMLSchema", "AFirmaIdPType", "cl")
	        			.attributeValueMarshaller(new StringAttributeValueMarshaller())
	        			.build());
	        }
	        if  (!peticion.getIdP().isClavePermanente()) {
	        	log.debug("No admitimos clave permanente");
	        	reqAttrMapBuilder.put(new AttributeDefinition.Builder<String>().nameUri("http://es.minhafp.clave/GISSIdP")
	        			.friendlyName("GISSIdP")
	        			.personType(PersonType.NATURAL_PERSON)
	        			.required(false)
	        			.uniqueIdentifier(true)
	        			.xmlType("http://www.w3.org/2001/XMLSchema", "GISSIdPType", "cl")
	        			.attributeValueMarshaller(new StringAttributeValueMarshaller())
	        			.build());
	        }
	        if (!peticion.getIdP().isPin24H()) {
	        	log.debug("No admitimos pin 24h");
	        	reqAttrMapBuilder.put(new AttributeDefinition.Builder<String>().nameUri("http://es.minhafp.clave/AEATIdP")
	        			.friendlyName("AEATIdP")
	        			.personType(PersonType.NATURAL_PERSON)
	        			.required(false)
	        			.uniqueIdentifier(true)
	        			.xmlType("http://www.w3.org/2001/XMLSchema", "AEATIdPType", "cl")
	        			.attributeValueMarshaller(new StringAttributeValueMarshaller())
	        			.build());
	        }
	        if (!peticion.getIdP().isEIDAS()) {
	        	log.debug("No admitimos ciudadano europeo");
	        	reqAttrMapBuilder.put(new AttributeDefinition.Builder<String>().nameUri("http://es.minhafp.clave/EIDASIdP")
	        			.friendlyName("EIDASIdP")
	        			.personType(PersonType.NATURAL_PERSON)
	        			.required(false)
	        			.uniqueIdentifier(true)
	        			.xmlType("http://www.w3.org/2001/XMLSchema", "EIDASIdP", "cl")
	        			.attributeValueMarshaller(new StringAttributeValueMarshaller())
	        			.build());
	        }
		}
		log.debug("Construcción del relayState");
        relayState=SecureRandomXmlIdGenerator.INSTANCE.generateIdentifier(8);
        reqAttrMapBuilder.putPrimaryValues(new AttributeDefinition.Builder<String>().nameUri("http://es.minhafp.clave/RelayState")
    			.friendlyName("RelayState")
    			.personType(PersonType.NATURAL_PERSON)
    			.required(false)
    			.uniqueIdentifier(true)
    			.xmlType("http://eidas.europa.eu/attributes/naturalperson", "PersonIdentifierType", "eidas-natural")
    			.attributeValueMarshaller(new StringAttributeValueMarshaller())
    			.build(), relayState);
        
        log.debug("Construcción del resto de datos del envío: destination, returnUrl, providerName, eidasloa, nameIDPolicy");
        EidasAuthenticationRequestNoMetadata.Builder reqBuilder = new EidasAuthenticationRequestNoMetadata.Builder();
        reqBuilder.destination(nodeServiceUrl);
        reqBuilder.providerName(providerName);
        reqBuilder.requestedAttributes(reqAttrMapBuilder.build());
        if (LevelOfAssurance.getLevel(eidasloa) == null) {
            reqBuilder.levelOfAssurance(LevelOfAssurance.LOW.stringValue());
        } else {
            reqBuilder.levelOfAssurance(eidasloa);
        }
        
        reqBuilder.levelOfAssuranceComparison(LevelOfAssuranceComparison.fromString("minimum").stringValue());
        
        if (nameIDPolicy != null) {
        	reqBuilder.nameIdFormat(nameIDPolicy);
        } else {
        	reqBuilder.nameIdFormat(SamlNameIdFormat.UNSPECIFIED.getNameIdFormat());
        }
        
        reqBuilder.binding(EidasSamlBinding.EMPTY.getName());
        reqBuilder.assertionConsumerServiceURL(returnUrl);
        reqBuilder.forceAuth(true); //Siempre forzamos la autenticación
        
        reqBuilder.spApplication(spApplication);
        log.debug("Generación de token");
        IRequestMessageNoMetadata binaryRequestMessage = null;
        EidasAuthenticationRequestNoMetadata authRequest = null;
        ProtocolEngineNoMetadataI protocolEngine = SpProtocolEngineFactory.getSpProtocolEngine(pref);
        String samlId;
        try {
        	samlId=SAMLEngineUtils.generateNCName();
            reqBuilder.id(samlId);
            authRequest = reqBuilder.build();
            binaryRequestMessage = protocolEngine.generateRequestMessage(authRequest, true);
        } catch (EIDASSAMLEngineException e) {
            throw new ProcesoException("Could not generate token for Saml Request");
        }
        log.debug("Codificación en base64 del token");
        String samlRequest = EidasStringUtil.encodeToBase64(binaryRequestMessage.getMessageBytes());
        
        result.getResultado().setSamlRequest(samlRequest);
        result.getResultado().setRelayState(relayState);
        result.getResultado().setUrlServicio(nodeServiceUrl);
        result.getResultado().setIdToken(samlId);
        log.info("Token generado");
        return result;
	}
	
	
	
}
