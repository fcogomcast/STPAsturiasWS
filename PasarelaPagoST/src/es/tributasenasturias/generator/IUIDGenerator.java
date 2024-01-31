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
	 * Genera un UID único.
	 * @return Un entero con el UID único
	 */
	public int generateUID();
	/**
	 * Genera un UID en formato hexadecimal.
	 * @return
	 */
	public String generateHexUID();
}
