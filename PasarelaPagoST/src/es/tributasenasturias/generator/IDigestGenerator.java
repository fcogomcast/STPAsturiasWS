/**
 * 
 */
package es.tributasenasturias.generator;

import es.tributasenasturias.exceptions.DigestGeneratorException;

/**
 * @author crubencvs
 * Interfaz de los generadores de resúmenes de mensajes.
 */
public interface IDigestGenerator {

	public String getDigest (String data) throws DigestGeneratorException;
}
