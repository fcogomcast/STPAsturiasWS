package es.tributasenasturias.seguridad;

import java.rmi.RemoteException;

import es.tributasenasturias.bd.Datos;
import es.tributasenasturias.excepciones.AutenticacionException;
import es.tributasenasturias.log.Logger;
import es.tributasenasturias.utils.GestorIdLlamada;
import es.tributasenasturias.utils.XMLDOMUtils;


/**
 * Validador de certificado de cabecera
 * @author davidsa
 */
public class CertificadoValidator{

	ResultadoValidacion res;
	private Logger log;

	public String getCertificadoCabecera(String datosXML) {
		String certificado = "";
		// Recuperamos la lista de nodos con certificado. Sólo debería haber uno.
		try {
			javax.xml.parsers.DocumentBuilder db;
			javax.xml.parsers.DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();

			db = fact.newDocumentBuilder();
			fact.setNamespaceAware(true);
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource();
			inStr.setCharacterStream(new java.io.StringReader(datosXML));
			org.w3c.dom.Document m_resultadoXML;
			m_resultadoXML = db.parse(inStr);
			org.w3c.dom.NodeList certificados = XMLDOMUtils.getAllNodes(m_resultadoXML, "X509Certificate");

			if (certificados != null && certificados.getLength() != 0) {
				certificado = certificados.item(0).getTextContent();

			} else {
				certificado = null;
			}
		} catch (Exception ex) {
			certificado = null;
			log.error("Error al parsear el certificado:" + ex.getMessage());
		}
		return certificado;
	}

	public CertificadoValidator() {
		log= new Logger(GestorIdLlamada.getIdLlamada());
		res = new ResultadoValidacion();
	}

	public IResultadoValidacion getResultado() {
		return res;
	}

	public boolean isValid(String datosXML) {		
		boolean valido = false;
		// Recuperamos el certificado.
		String certificado = getCertificadoCabecera(datosXML);
		Datos bd = null;
		
		java.util.Map<String, String> resBD = null;
		
		if (certificado != null && !certificado.equals("")) {
			// Lo enviamos al servicio del Principado, para que nos indique si es válido.
			AutenticacionPAHelper aut = new AutenticacionPAHelper();
			try {
				// Llamamos al servicio de autenticación del Principado
				String strCIF = aut.login(certificado);				
				
				if (strCIF!=null) 
					log.info ("HA SUPERADO LA AUTENTICACION DE CERTIFICADO DEL PRINCIPADO");				

				if (strCIF==null) {					
					log.info ("NO SUPERA AUTENTICACION DEL PRINCIPADO");
				}

				// Validamos el Id que nos han pasado.
				bd = new Datos(GestorIdLlamada.getIdLlamada());
				resBD = bd.permisoServicio(strCIF);

				String autorizacion = resBD.get("AUTORIZACION");
				if (autorizacion.equalsIgnoreCase("01")) {
					res.addMessage("No está autorizado para utilizar el servicio");
				} else if (autorizacion.equalsIgnoreCase("02")) {
					res.addMessage("Error en consulta de la base de datos");
				} else {
					valido = true;
				}
			} catch (AutenticacionException ex) {
				log.error("Error al validar la autorización:"+ ex.getMessage());
				log.trace(ex.getStackTrace());
				res.addMessage("No se ha podido validar la autorización.");
				valido = false;
			} catch (RemoteException ex) {
				log.error("Error al validar la autorización:"+ ex.getMessage());
				log.trace(ex.getStackTrace());
				res.addMessage("No se ha podido validar la autorización.");
				valido = false;
			}
		} else {
			valido = false;
		}
		return valido;
	}

}
