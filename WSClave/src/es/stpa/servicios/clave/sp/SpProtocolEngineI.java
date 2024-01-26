package es.stpa.servicios.clave.sp;

import eu.eidas.auth.engine.ProtocolEngineNoMetadataI;
import eu.eidas.engine.exceptions.EIDASSAMLEngineException;

/**
 * SpProtocolEngineI
 */
public interface SpProtocolEngineI extends ProtocolEngineNoMetadataI {

    byte[] checkAndDecryptResponse(byte[] responseBytes) throws EIDASSAMLEngineException;
}
