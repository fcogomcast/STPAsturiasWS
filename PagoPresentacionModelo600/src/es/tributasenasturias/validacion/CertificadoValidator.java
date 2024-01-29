package es.tributasenasturias.validacion;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import es.tributasenasturias.Exceptions.DatosException;
import es.tributasenasturias.Exceptions.ValidacionException;
import es.tributasenasturias.validacion.Datos;
import es.tributasenasturias.validacion.AutenticacionPAHelper;
import es.tributasenasturias.webservice.pagopresentacion.log.ILoggable;
import es.tributasenasturias.webservice.pagopresentacion.log.LogHelper;
import es.tributasenasturias.pagopresentacionmodelo600utils.Preferencias;

/**
 * Validador de certificado de cabecera
 * 
 * @author davidsa
 * 
 */
public class CertificadoValidator implements IValidator<String>, ILoggable{
	private Preferencias pref = new Preferencias();
	ResultadoValidacion res;
	private LogHelper log;
	
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
			log.error("Error al parsear el certificado:" + ex.getMessage());
		}

		return certificado;
	}

	protected CertificadoValidator() {
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
			log.error("Error al cargar las preferencias en metodo IsValid");
		}
		boolean valido = false;
		// Recuperamos el certificado.
		String certificado = getCertificadoCabecera(datosXML);
		Datos bd = null;
		
		java.util.Map<String, String> resBD = null;
		
		if (certificado != null && !certificado.equals("")) {
			// Lo enviamos al servicio del Principado, para que nos indique si
			// es válido.
			AutenticacionPAHelper aut =new AutenticacionPAHelper();
			aut.setLogger(this.log);

			try {
				// Llamamos al servicio de autenticación del Principado
				String strCIF = aut.login(certificado);				
				
				if (strCIF!=null)
				{
					log.info ("HA SUPERADO LA AUTENTICACION DE CERTIFICADO DEL PRINCIPADO:");
				}
				
				if (strCIF==null) {					
						log.info ("NO SUPERA AUTENTICACION DEL PRINCIPADO");
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
				log.error("Error al validar la autorización:"
						+ ex.getMessage());
				log.trace(ex.getStackTrace());
				res.addMessage("No se ha podido validar la autorización.");
				valido = false;
			} catch (DatosException ex) {
				log.error("Error al validar la autorización:"
						+ ex.getMessage());
				log.trace(ex.getStackTrace());
				res.addMessage("No se ha podido validar la autorización.");
				valido = false;
			}
		} else {
			valido = false;
		}
		return valido;
	}
	@Override
	public void setLogger(LogHelper log)
	{
		this.log = log;
	}
	@Override 
	public LogHelper getLogger()
	{
		return log;
	}
}
