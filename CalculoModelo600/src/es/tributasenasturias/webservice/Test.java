package es.tributasenasturias.webservice;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import es.tributasenasturias.utils.Base64;
import es.tributasenasturias.validacion.CertificadoValidator;


public class Test {
	public static void main(String[] args) {
		try {
			String xmlData;
			int ch;
			StringBuffer str = new StringBuffer();
			FileInputStream ficheroBase64 = new FileInputStream(
					"Q:\\noexiste_archivos\\David\\pruebaFirma.xml");
			while ((ch = ficheroBase64.read()) != -1)
				str.append((char) ch);

			xmlData = str.toString();

			// System.out.println("valor nl:"+nl);
			// System.out.println("valor nl:"+nl.item(0).getTextContent());

			CertificadoValidator validator = new CertificadoValidator();

			// Volcamos el certificado en un objeto X509Certificate y
			// extraemos el NIF/CIF.
			String certificadoBase64 = validator
					.getCertificadoCabecera(xmlData);
			System.out.print("valor:" + certificadoBase64);
			try {
				X509Certificate cert;
				byte[] arr = Base64.decode(certificadoBase64.toCharArray());
				CertificateFactory cf;
				cf = CertificateFactory.getInstance("X.509");
				ByteArrayInputStream bais = new ByteArrayInputStream(arr);
				cert = (X509Certificate) cf.generateCertificate(bais);

				String principalName = cert.getSubjectDN().getName();							
				principalName = principalName.substring(principalName.indexOf("CIF"),principalName.length());
				principalName = principalName.replaceAll("CIF","");
				String strCIF = principalName.substring(0,principalName.indexOf(",")).toUpperCase(); 							
				
				System.out.println("HOOOOOOOOOOOOOOOOOLA:"+strCIF);
			
				/*System.out.println("valor certificado:"
						+ cert.getSubjectDN().getName());*/

			} catch (CertificateException e) {
				System.out.println(e.getMessage());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			// System.out.println("valor:" +
			// validator.getCertificadoCabecera(msgSOAP.getSOAPBody()));

			if (!validator.isValid(xmlData)) {

				System.out.println("valido:");

			}

		} catch (Exception e1) {
			System.out.println("Error:Validacion Firma Digital:"
					+ e1.getMessage());

		}
	}
}
