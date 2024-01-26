package es.stpa.servicios.clave.sp;



import eu.eidas.auth.commons.EidasErrorKey;
import eu.eidas.auth.engine.ProtocolEngineNoMetadata;
import eu.eidas.auth.engine.configuration.ProtocolConfigurationAccessorNoMetadata;
import eu.eidas.auth.engine.xml.opensaml.CorrelatedResponse;
import eu.eidas.engine.exceptions.EIDASSAMLEngineException;

/**
 * SpProtocolEngine
 */
public final class SpProtocolEngine extends ProtocolEngineNoMetadata implements SpProtocolEngineI {



    public SpProtocolEngine(ProtocolConfigurationAccessorNoMetadata configurationAccessor) {
        super(configurationAccessor);
    }

    /**
     * Decrypt and validate saml respons
     *
     * @param responseBytes
     * @return
     * @throws EIDASSAMLEngineException
     */
    @Override
    public byte[] checkAndDecryptResponse(byte[] responseBytes) throws EIDASSAMLEngineException {
        // This decrypts the given responseBytes:
        CorrelatedResponse response = (CorrelatedResponse) unmarshallResponse(responseBytes);

        // validateUnmarshalledResponse(samlResponse, userIP, skewTimeInMillis);

        try {
            // re-transform the decrypted bytes to another byte array, without signing:
            return marshall(response.getResponse());
        } catch (EIDASSAMLEngineException e) {
            throw new EIDASSAMLEngineException(EidasErrorKey.INTERNAL_ERROR.errorCode(),
                                               EidasErrorKey.INTERNAL_ERROR.errorMessage(), e);
        }
    }
}
