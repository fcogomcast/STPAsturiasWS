package es.tributasenasturias.validacion;

import es.tributasenasturias.exception.DatosException;
import es.tributasenasturias.exception.ValidacionException;
import es.tributasenasturias.validacion.Datos;
import es.tributasenasturias.validacion.AutenticacionPAHelper;
import es.tributasenasturias.modelo600utils.log.ILoggable;
import es.tributasenasturias.modelo600utils.log.LogHelper;
import es.tributasenasturias.modelo600utils.Preferencias;

/**
 * Validador de certificado de cabecera
 * @author davidsa
 */
public class CertificadoValidator implements IValidator<String>, ILoggable{

	private Preferencias pref = new Preferencias();
	ResultadoValidacion res;
	private LogHelper log;

	public String getCertificadoCabecera(String datosXML) {
		String certificado = "";
		// Recuperamos la lista de nodos de la cabecera con certificado. S�lo deber�a haber uno.
		try {
			javax.xml.parsers.DocumentBuilder db;
			javax.xml.parsers.DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();

			db = fact.newDocumentBuilder();
			fact.setNamespaceAware(true);
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource();
			inStr.setCharacterStream(new java.io.StringReader(datosXML));
			org.w3c.dom.Document m_resultadoXML;
			m_resultadoXML = db.parse(inStr);
			//org.w3c.dom.NodeList certificados = m_resultadoXML.getElementsByTagNameNS("*","X509Certificate");
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
			// Lo enviamos al servicio del Principado, para que nos indique si es v�lido.
			AutenticacionPAHelper aut = new AutenticacionPAHelper();
			aut.setLogger(log);
			try {
				// Llamamos al servicio de autenticaci�n del Principado
				String strCIF = aut.login(certificado);				
				
				if (strCIF!=null) 
					log.info ("HA SUPERADO LA AUTENTICACION DE CERTIFICADO DEL PRINCIPADO:");				

				if (strCIF==null) {					
					log.info ("NO SUPERA AUTENTICACION DEL PRINCIPADO");
				}

				// Validamos el Id que nos han pasado.
				bd = new Datos();
				resBD = bd.permisoServicio(strCIF);

				String autorizacion = resBD.get("AUTORIZACION");
				if (autorizacion.equalsIgnoreCase("01")) {
					res.addMessage("No est� autorizado para utilizar el servicio");
				} else if (autorizacion.equalsIgnoreCase("02")) {
					res.addMessage("Error en consulta de la base de datos");
				} else {
					valido = true;
				}
			} catch (ValidacionException ex) {
				log.error("Error al validar la autorizaci�n:"+ ex.getMessage());
				log.trace(ex.getStackTrace());
				res.addMessage("No se ha podido validar la autorizaci�n.");
				valido = false;
			} catch (DatosException ex) {
				log.error("Error al validar la autorizaci�n:"+ ex.getMessage());
				log.trace(ex.getStackTrace());
				res.addMessage("No se ha podido validar la autorizaci�n.");
				valido = false;
			}
		} else {
			valido = false;
		}
		return valido;
	}

	@Override
	public void setLogger(LogHelper log){
		this.log = log;
	}

	@Override 
	public LogHelper getLogger(){
		return log;
	}
}
