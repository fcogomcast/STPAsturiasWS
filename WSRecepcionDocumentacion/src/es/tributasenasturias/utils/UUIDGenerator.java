/**
 * 
 */
package es.tributasenasturias.utils;

/**
 * @author crubencvs
 * Genera un nuevo UUID �nico.
 */
public class UUIDGenerator {
	/**
	 * Genera una cadena hexadecimal representando un ID �nico.
	 * @return Cadena representando el valor hexadecimal del ID.
	 */
	public String generateHexUID() {
		
		return Integer.toHexString(generateUID());
	}
	/**
	 * Genera un ID �nico.
	 * @return ID �nico.
	 */
	public int generateUID() {
		java.util.UUID uid = java.util.UUID.randomUUID();
		return uid.hashCode();
	}

	
}
