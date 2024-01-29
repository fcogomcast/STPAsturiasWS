package es.tributasenasturias.validacion;

/*
import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
*/
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import es.tributasenasturias.Exception.DatosException;
import es.tributasenasturias.Exception.ValidacionException;
import es.tributasenasturias.validacion.Datos;
//import es.tributasenasturias.utils.Base64;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;

/**
 * Validador de certificado de cabecera
 * 
 * @author davidsa
 * 
 */
public class CertificadoValidator implements IValidator<String> {
	private Preferencias pref = new Preferencias();
	ResultadoValidacion res;

	public String getCertificadoCabecera(String datosXML) {
		String certificado = "";
		// Recuperamos la lista de nodos de la cabecera con certificado. Sólo
		// debería haber uno.
		try {
			javax.xml.parsers.DocumentBuilder db;
			javax.xml.parsers.DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory
					.newInstance();

			db = fact.newDocumentBuilder();
			fact.setNamespaceAware(true);
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource();
			inStr.setCharacterStream(new java.io.StringReader(datosXML));
			org.w3c.dom.Document m_resultadoXML;
			m_resultadoXML = db.parse(inStr);
			//CRUBENCVS 47084 22/02/2023. 
			//Esto puede fallar, ya que busca un prefijo de namespace concreto.
			//org.w3c.dom.NodeList certificados = m_resultadoXML
			//		.getElementsByTagName("dsig:X509Certificate");

			/*if (certificados != null && certificados.getLength() != 0) {
				certificado = certificados.item(0).getTextContent();

			} else {
				certificado = null;
			}
			*/
			XPath xpath= XPathFactory.newInstance().newXPath();
			certificado= (String)xpath.evaluate("/remesa/*[local-name()='Signature']/*[local-name()='KeyInfo']/*[local-name()='X509Data']/*[local-name()='X509Certificate']/text()", m_resultadoXML, XPathConstants.STRING);
			//FIN CRUBENCVS 47084

		} catch (Exception ex) {
			certificado = null;
			Logger.error("Error al parsear el certificado:" + ex.getMessage());
		}

		return certificado;
	}
	

	public CertificadoValidator() {
		pref.CompruebaFicheroPreferencias();
		res = new ResultadoValidacion();
	}

	@Override
	public IResultadoValidacion getResultado() {
		return res;
	}

	@Override
	public boolean isValid(String datosXML) {		
		try {
			pref.CargarPreferencias();
		} catch (Exception e1) {			
			Logger.error("Error al cargar las preferencias en metodo IsValid");
		}
		boolean valido = false;
		// Recuperamos el certificado.
		String certificado = getCertificadoCabecera(datosXML);
		Datos bd = null;
		
		java.util.Map<String, String> resBD = null;
		
		if (certificado != null && !certificado.equals("")) {
			// Lo enviamos al servicio del Principado, para que nos indique si
			// es válido.
			AutenticacionPAHelper aut = new AutenticacionPAHelper();

			try {
				// Llamamos al servicio de autenticación del Principado
				String strCIF = aut.login(certificado);				
				
				if (pref.getDebug().equals("1")) { 
					if (strCIF!=null) 
						Logger.info ("HA SUPERADO LA AUTENTICACION DE CERTIFICADO DEL PRINCIPADO:");				
				}				

				if (strCIF==null) {					
					if (pref.getDebug().equals("1")) 
						Logger.info ("NO SUPERA AUTENTICACION DEL PRINCIPADO");
/*					
					String certificadoBase64 = certificado;
					X509Certificate cert;
					byte[] arr = Base64.decode(certificadoBase64.toCharArray());
					CertificateFactory cf;
					cf = CertificateFactory.getInstance("X.509");
					ByteArrayInputStream bais = new ByteArrayInputStream(arr);
					cert = (X509Certificate) cf.generateCertificate(bais);

					String principalName = cert.getSubjectDN().getName();
					principalName = principalName.substring(principalName
							.indexOf("CIF"), principalName.length());
					principalName = principalName.replaceAll("CIF", "").trim();

					strCIF = principalName.substring(0,
							principalName.indexOf(",")).toUpperCase();
*/
				}			

				// Validamos el Id que nos han pasado.
				bd = new Datos();
				resBD = bd.permisoServicio(strCIF);

				String autorizacion = resBD.get("AUTORIZACION");
				if (autorizacion.equalsIgnoreCase("01")) {
					res
							.addMessage("No está autorizado para utilizar el servicio");
				} else if (autorizacion.equalsIgnoreCase("02")) {
					res.addMessage("Error en consulta de la base de datos");
				} else {
					valido = true;
				}
			} catch (ValidacionException ex) {
				Logger.error("Error al validar la autorización:"
						+ ex.getMessage());
				res.addMessage("No se ha podido validar la autorización.");
				valido = false;
			} catch (DatosException ex) {
				Logger.error("Error al validar la autorización:"
						+ ex.getMessage());
				res.addMessage("No se ha podido validar la autorización.");
				valido = false;
			}

		} else {
			valido = false;
		}
		return valido;
	}
}
