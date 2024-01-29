package es.tributasenasturias.ades.certificados;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.ProtectionParameter;

import es.tributasenasturias.ades.exceptions.AlmacenException;

/**
 * Controlador del acceso a un almacén, según preferencias.
 * @author crubencvs
 *
 */
public class AlmacenController {

	private AlmacenController() {}

	/**
	 * Recupera la entrada de clave privada de un certificado en un almacén
	 * @param almacen Ruta del almacén de certificados
	 * @param tipoAlmacen Tipo de almacén de certificados
	 * @param claveAlmacen Clave del almacén
	 * @param aliasCertificado Alias del certificado cuya parte de clave privada queremos recuperar
	 * @param claveCertificado Clave del certificado
	 * @return PrivateKeyEntry
	 * @throws AlmacenException Ante cualquier error, no nos interesa el tipo de excepción concreta
	 */
	public static final PrivateKeyEntry getPrivateKey(String almacen, String tipoAlmacen, String claveAlmacen, String aliasCertificado, String claveCertificado) throws AlmacenException{
		try {
			KeyStore keystore = KeyStore.getInstance(tipoAlmacen);
	        keystore.load(new FileInputStream(almacen), claveAlmacen.toCharArray());
	        ProtectionParameter protect = new PasswordProtection(claveCertificado.toCharArray());
	        return (PrivateKeyEntry) keystore.getEntry(aliasCertificado, protect);
		}catch (Exception e) //Hay muchos tipos (CertificateException, NoSuchAlgorithmException), no nos interesan todos...
		{
			throw new AlmacenException(e);
		}
	}
}
