/**
 * 
 */
package es.tributasenasturias.utils;

/**
 * @author crubencvs
 * Genera un nuevo UUID único.
 */
public class UUIDGenerator {
	/**
	 * Genera una cadena hexadecimal representando un ID único.
	 * @return Cadena representando el valor hexadecimal del ID.
	 */
	public String generateHexUID() {
		
		return Integer.toHexString(generateUID());
	}
	/**
	 * Genera un ID único.
	 * @return ID único.
	 */
	public int generateUID() {
		java.util.UUID uid = java.util.UUID.randomUUID();
		return uid.hashCode();
	}

	
}
