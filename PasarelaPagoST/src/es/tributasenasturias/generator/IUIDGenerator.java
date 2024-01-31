/**
 * 
 */
package es.tributasenasturias.generator;

/**
 * @author crubencvs
 *
 */
public interface IUIDGenerator {
	/**
	 * Genera un UID �nico.
	 * @return Un entero con el UID �nico
	 */
	public int generateUID();
	/**
	 * Genera un UID en formato hexadecimal.
	 * @return
	 */
	public String generateHexUID();
}
