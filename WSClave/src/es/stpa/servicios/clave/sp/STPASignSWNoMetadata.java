package es.stpa.servicios.clave.sp;

/*
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * http://www.osor.eu/eupl/european-union-public-licence-eupl-v.1.1
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * Licence for the specific language governing permissions and limitations under
 * the Licence.
 */

/* Licencia que puede que aplique porque hemos copiado partes del código de la librería */
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.opensaml.security.SAMLSignatureProfileValidator;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.x509.X509Credential;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.SignableXMLObject;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureValidator;
import org.opensaml.xml.validation.ValidationException;


import eu.eidas.auth.commons.EidasErrorKey;
import eu.eidas.auth.engine.core.ProtocolSignerI;
import eu.eidas.auth.engine.core.impl.KeyStoreProtocolSignerNoMetadata;
import eu.eidas.auth.engine.xml.opensaml.CertificateUtil;
import eu.eidas.engine.exceptions.EIDASSAMLEngineException;
import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * A {@link ProtocolSignerI} implementation which uses the same signature configuration file to load another keyStore
 * used to sign the MetaData content.
 * Implementación que permite implementar la validación sin comprobar si el certificado está
 * en el almacén de certificados, sino que lo comprobaremos enviando a AutenticacionEPST.
 * El resto de la validación será igual.
 * La firma se realiza según la implementación de las clases de las que hereda.
 * Para activar esta clase, hay que indicar que es la implementación del módulo de firma en
 * SPSamlEngine.xml
 * usando 
 * <configuration name="SignatureConf">
       <!-- Specific signature module -->
       <parameter name="class" value="es.stpa.servicios.clave.sp.STPASignSWNoMetadata"/>
 */
public class STPASignSWNoMetadata extends KeyStoreProtocolSignerNoMetadata {

    public STPASignSWNoMetadata(Map<String, String> properties, String defaultPath) throws EIDASSAMLEngineException {
        super(properties, defaultPath);
    }
    
    //Redefinimos la validación de firma para poder modificarla. 
    @Override
    /**
     * Copiado exactamente de AbstractProtocolSignedNoMetadata
     */
    public <T extends SignableXMLObject> T validateSignature(T signedObject,
               Collection<X509Certificate> trustedCertificateCollection)
            throws EIDASSAMLEngineException {
        List<? extends Credential> trustedCreds;

        // 2) Verify the cryptographic signature:
        if (CollectionUtils.isEmpty(trustedCertificateCollection)) {
            trustedCreds = getTrustedCredentials();
        } else {
            trustedCreds = CertificateUtil.getListOfCredential(trustedCertificateCollection);
        }
        return validateSignatureWithCredentials(signedObject, trustedCreds);
    }
    
    /**
     * Copiado exactamente de AbstractProtocolSignedNoMetadata. 
     * @param <T>
     * @param signedObject
     * @param trustedCredentialList
     * @return
     * @throws EIDASSAMLEngineException
     */
    private <T extends SignableXMLObject> T validateSignatureWithCredentials(T signedObject,
            List<? extends Credential> trustedCredentialList)
    	throws EIDASSAMLEngineException {
    	// 1) Validate the structure of the SAML signature:
    	validateSamlSignatureStructure(signedObject);
    	
    	// 2) Verify the cryptographic signature:
    	verifyCryptographicSignature(signedObject.getSignature(), trustedCredentialList);

    	return signedObject;
    }
    
    /**
     * Copiado exactamente de AbstractProtocolSignedNoMetadata
     * @param signableObject
     * @return
     * @throws EIDASSAMLEngineException
     */
    private SAMLSignatureProfileValidator validateSamlSignatureStructure(SignableXMLObject signableObject)
    throws EIDASSAMLEngineException {
    		SAMLSignatureProfileValidator sigProfValidator = new SAMLSignatureProfileValidator();
    			try {
    				// Indicates signature id conform to SAML Signature profile
    				sigProfValidator.validate(signableObject.getSignature());
    			} catch (ValidationException e) {
  
    				throw new EIDASSAMLEngineException(e);
    			}
    			return sigProfValidator;
    }
    
    /**
     * Copiado exactamente de AbstractProtocolSignedNoMetadata
     * @param signature
     * @param trustedCredentialList
     * @throws EIDASSAMLEngineException
     */
    private void verifyCryptographicSignature(Signature signature,
            List<? extends Credential> trustedCredentialList)
    	throws EIDASSAMLEngineException {
    		// 1) check that we accept the signature algorithm
    		String signatureAlgorithmVal = signature.getSignatureAlgorithm();

    		if (!isAlgorithmAllowedForVerifying(signatureAlgorithmVal)) {

    			throw new EIDASSAMLEngineException(EidasErrorKey.INVALID_SIGNATURE_ALGORITHM.errorCode());
    		}

    		// 2) check that we trust the signing certificate
    		X509Credential entityX509Cred = getTrustedCertificate(signature, trustedCredentialList);

    		// 3) verify the XML Digital Signature itself (XML-DSig)
    		// DOM information related to the signature should be still available at this point
    		SignatureValidator sigValidator = new SignatureValidator(entityX509Cred);
    		try {
    			sigValidator.validate(signature);
    		} catch (ValidationException e) {

    			throw new EIDASSAMLEngineException(e);
    		}
    }
    
    /**
     * En esta función es en donde modificamos, para poder cambiar lo que consideramos certificado "confiable",
     * que no tiene por qué ser el que está en el almacén.
     * @param signature
     * @param trustedCredentialList
     * @return
     * @throws EIDASSAMLEngineException
     */
    private X509Credential getTrustedCertificate(Signature signature,
            List<? extends Credential> trustedCredentialList)
    throws EIDASSAMLEngineException {
    	X509Certificate cert = getSignatureCertificate(signature);
    	// Exist only one certificate
    	X509Credential entityX509Cred = CertificateUtil.toCredential(cert);

    	//Lo damos por bueno, por ahora siempre.
    	//CertificateUtil.checkTrust(entityX509Cred, trustedCredentialList);
    	checkCertificateValidityPeriod(cert);
    	checkCertificateIssuer(cert);
    	return entityX509Cred;
    }
    
    /**
     * Copiado exactamente de AbstractProtocolSignedNoMetadata
     * @param signature
     * @return
     * @throws EIDASSAMLEngineException
     */
    private static X509Certificate getSignatureCertificate(Signature signature) throws EIDASSAMLEngineException {
        KeyInfo keyInfo = signature.getKeyInfo();
        return CertificateUtil.toCertificate(keyInfo);
    }
    /** 
     * Copiado exactamente de AbstractProtocolSignedNoMetadata
     * @param signatureAlgorithm
     * @return
     */
    private boolean isAlgorithmAllowedForVerifying(String signatureAlgorithm) {
        return isNotBlank(signatureAlgorithm) && getSignatureAlgorithmWhiteList().contains(
                signatureAlgorithm.trim());
    }
}
