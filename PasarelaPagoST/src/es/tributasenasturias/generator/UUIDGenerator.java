/**
 * 
 */
package es.tributasenasturias.generator;

/**
 * @author crubencvs
 * Genera un nuevo UUID único.
 */
public class UUIDGenerator implements IUIDGenerator {

	/* (non-Javadoc)
	 * @see es.tributasenasturias.generator.IUIDGenerator#generateHexUID()
	 */
	@Override
	public String generateHexUID() {
		
		return Integer.toHexString(generateUID());
	}

	/* (non-Javadoc)
	 * @see es.tributasenasturias.generator.IUIDGenerator#generateUID()
	 */
	@Override
	public int generateUID() {
		java.util.UUID uid = java.util.UUID.randomUUID();
		return uid.hashCode();
	}

	
}
