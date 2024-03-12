package es.tributasenasturias.servicios.accesocertificado.certificados;

import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Utilidad para recuperar diferentes valores de un certificado.
 * @author crubencvs
 *
 */
public class CertificadoHelper {

	/**
	 * Devuelve el "Principal" (DN) de una cadena que contiene un certificado en Base 64, interpretándolo como X509
	 * @param certificado Certificado en Base64
	 * @return Cadena que contiene el DN del certificado o null si no se ha recuperado
	 * @throws CertificateException Si se produce una excepción al interpretar el certificado.
	 */
	public static String getX509Principal (String certificado) throws CertificateException
	{
		byte[] decodificado = Base64.decode(certificado.toCharArray());
        CertificateFactory cf;
		cf = CertificateFactory.getInstance("X.509");
		Certificate cert = cf.generateCertificate(new ByteArrayInputStream(decodificado));
        X509Certificate cert509 = (X509Certificate)cert;
        if (cert509 == null)
        {
        	return null;
        }
        return cert509.getSubjectX500Principal().toString();
        
	}
}
