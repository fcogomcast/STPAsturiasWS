/**
 * 
 */
package es.tributasenasturias.webservices.Certificados.sesion;

/**
 * @author crubencvs
 * Genera un nuevo UUID único.
 */
public class UUIDGenerator {

	public String generateHexUID() {
		
		return Integer.toHexString(generateUID());
	}

	public int generateUID() {
		java.util.UUID uid = java.util.UUID.randomUUID();
		return uid.hashCode();
	}

	
}
