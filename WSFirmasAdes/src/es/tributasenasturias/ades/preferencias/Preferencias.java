package es.tributasenasturias.ades.preferencias;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.prefs.Preferences;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import es.tributasenasturias.ades.XMLUtils;

public class Preferencias {
	private Preferences m_preferencias;
	private final static String DIRECTORIO_SERVICIO = "proyectos/WSFirmasAdes";
	private final static String FICHERO_PREFERENCIAS = "prefsFirmasAdes.xml";
	private final static String DIRECTORIO_PREFERENCIAS = DIRECTORIO_SERVICIO;

	private HashMap<String, String> tablaPreferencias = new HashMap<String, String>();

	// nombres de las preferencias
	private final static String KEY_PREF_LOG = "ModoLog";
	private final static String KEY_PREF_SOAP_LOG="IncluirLogSOAP";
	private final static String KEY_PREF_ESQUEMA = "EsquemaBD";
	private final static String KEY_PREF_LOG_APLICACION = "ficheroLogAplicacion";
	private final static String KEY_PREF_LOG_CLIENT = "ficheroLogClient";
	private final static String KEY_PREF_LOG_SERVER = "ficheroLogServer";
	private final static String KEY_PREF_ALMACEN = "AlmacenCertificados";
	private final static String KEY_PREF_TIPO_ALMACEN="TipoAlmacenCertificados";
	private final static String KEY_PREF_ALMACEN_PASSWORD = "ClaveAlmacenCertificados";
	private final static String KEY_PREF_CERTIFICADO = "Certificado";
	private final static String KEY_PREF_CERTIFICADO_PASSWORD = "ClaveCertificado";
	private final static String KEY_PREF_SEGURIDAD_SW = "EndpointSeguridad";
	private final static String KEY_PREF_ALIAS_SEGURIDAD_SW="aliasCertificadoWSSecurity";
	private final static String KEY_PREF_APLICACION = "IdAplicacion";
	private final static String KEY_PREF_URI_XADES="ficheroUriFormsXades";
	private final static String KEY_PREF_UPGRADE_AFIRMA="EndpointUpgradeAfirma";
	private final static String KEY_PREF_URI_PADES="ficheroUriFormsPades";
	private final static String KEY_PREF_PADES_XML="plantillaUpgradePades";
	private final static String KEY_PREF_XADES_XML="plantillaUpgradeXades";

	public Preferencias() throws PreferenciasException {
		cargarPreferencias();
	}

	public void cargarPreferencias() throws PreferenciasException {
		try {
			if (CompruebaFicheroPreferencias()) {

				FileInputStream inputStream = new FileInputStream(
						DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
				Preferences.importPreferences(inputStream);
				inputStream.close();

				m_preferencias = Preferences.systemNodeForPackage(this
						.getClass());

				String[] keys = m_preferencias.keys();
				String msgKeys = "Leyendo las siguientes claves -> ";
				for (int i = 0; i < keys.length; i++) {
					msgKeys += "[" + keys[i] + "] ";
				}

				for (int i = 0; i < keys.length; i++) {
					String value = m_preferencias.get(keys[i], "");
					tablaPreferencias.put(keys[i], value);
				}
			}
		} catch (Exception ex) {
			throw new PreferenciasException(
					"Error al cargar las preferencias: " + ex.getMessage(), ex);
		}

	}

	private synchronized void InicializaTablaPreferencias() {

		tablaPreferencias.clear();

		tablaPreferencias.put(KEY_PREF_LOG, 				"INFO");
		tablaPreferencias.put(KEY_PREF_SOAP_LOG,			"S");
		tablaPreferencias.put(KEY_PREF_ESQUEMA, 			"EXPLOTACION");
		tablaPreferencias.put(KEY_PREF_LOG_APLICACION,	 	DIRECTORIO_SERVICIO	+ "/logs/Application.log");
		tablaPreferencias.put(KEY_PREF_LOG_CLIENT, 		 	DIRECTORIO_SERVICIO	+ "/logs/Soap_Client.log");
		tablaPreferencias.put(KEY_PREF_LOG_SERVER, 			DIRECTORIO_SERVICIO	+ "/logs/Soap_Server.log");
		tablaPreferencias.put(KEY_PREF_ALMACEN,				"/opt/oracle/Middleware/certificados/jks/certificados_CMP.jks");
		tablaPreferencias.put(KEY_PREF_TIPO_ALMACEN,		"JKS");		
		tablaPreferencias.put(KEY_PREF_ALMACEN_PASSWORD, 	"hola123");
		tablaPreferencias.put(KEY_PREF_CERTIFICADO, 		"tributassello");
		tablaPreferencias.put(KEY_PREF_CERTIFICADO_PASSWORD,"hola123");
		tablaPreferencias.put(KEY_PREF_APLICACION,			 "princast.stpa.tributas");
		tablaPreferencias.put(KEY_PREF_SEGURIDAD_SW,		"http://bus:7101/WSInternos/ProxyServices/PXSeguridadWS");
		tablaPreferencias.put(KEY_PREF_ALIAS_SEGURIDAD_SW,	"tributas");
		tablaPreferencias.put(KEY_PREF_UPGRADE_AFIRMA,		"http://bus:7101/WSAFirma/ProxyServices/PXDSSAfirmaVerify");
		tablaPreferencias.put(KEY_PREF_URI_XADES,			DIRECTORIO_SERVICIO+"/uriFormsXades.prop");			
		tablaPreferencias.put(KEY_PREF_URI_PADES,			DIRECTORIO_SERVICIO+"/uriFormsPades.prop");
		tablaPreferencias.put(KEY_PREF_PADES_XML,			DIRECTORIO_SERVICIO+"/plantillaUpgradePades.xml");
		tablaPreferencias.put(KEY_PREF_XADES_XML,			DIRECTORIO_SERVICIO+"/plantillaUpgradeXades.xml");

	}

	private synchronized boolean CompruebaFicheroPreferencias()
			throws PreferenciasException {
		boolean existeFichero = false;

		File f = new File(DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
		existeFichero = f.exists();
		if (existeFichero == false) {
			CrearFicheroPreferencias();
		}

		return existeFichero;
	}

	/***************************************************************************
	 * 
	 * Creamos el fichero de preferencias con los valores por defecto
	 * 
	 **************************************************************************/
	private synchronized void CrearFicheroPreferencias()
			throws PreferenciasException {

		// preferencias por defecto
		m_preferencias = Preferences.systemNodeForPackage(this.getClass());

		InicializaTablaPreferencias();

		// recorremos la tabla cargada con las preferencias por defecto
		Iterator<Map.Entry<String, String>> itr = tablaPreferencias.entrySet()
				.iterator();
		while (itr.hasNext()) {
			Map.Entry<String, String> e = (Map.Entry<String, String>) itr
					.next();

			m_preferencias.put(e.getKey(), e.getValue());
		}

		FileOutputStream outputStream = null;
		File fichero;
		try {
			fichero = new File(DIRECTORIO_PREFERENCIAS);
			if (fichero.exists() == false)
				if (fichero.mkdirs() == false) {
					throw new java.io.IOException(
							"No se puede crear el directorio de las preferencias.");
				}

			outputStream = new FileOutputStream(DIRECTORIO_PREFERENCIAS + "/"
					+ FICHERO_PREFERENCIAS);
			m_preferencias.exportNode(outputStream);
		} catch (Exception e) {
			throw new PreferenciasException(
					"Error al crear el fichero de preferencias:"
							+ e.getMessage(), e);
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (Exception e) {
				throw new PreferenciasException(
						"Error al cerrar el flujo del fichero de preferencias:"
								+ e.getMessage(), e);
			}
		}
	}

	public void recargaPreferencias() throws PreferenciasException {
		cargarPreferencias();
	}

	private String getValueFromTablaPreferencias(String key) {
		String toReturn = "";

		if (tablaPreferencias.containsKey(key)) {
			toReturn = tablaPreferencias.get(key);
		}

		return toReturn;
	}

	public String getEsquemaBD() {
		return getValueFromTablaPreferencias(KEY_PREF_ESQUEMA);
	}

	public String getModoLog() {
		return getValueFromTablaPreferencias(KEY_PREF_LOG);
	}

	public String getFicheroLogAplicacion() {
		return getValueFromTablaPreferencias(KEY_PREF_LOG_APLICACION);
	}
	public String getHacerLogSoap() {
		return getValueFromTablaPreferencias(KEY_PREF_SOAP_LOG);
	}
	public String getEndpointUpgradeAfirma(){
		return getValueFromTablaPreferencias(KEY_PREF_UPGRADE_AFIRMA);
	}
	public String getFicheroLogClient() {
		return getValueFromTablaPreferencias(KEY_PREF_LOG_CLIENT);
	}

	public String getFicheroLogServer() {
		return getValueFromTablaPreferencias(KEY_PREF_LOG_SERVER);
	}
	public String getAlmacen() {
		return getValueFromTablaPreferencias(KEY_PREF_ALMACEN);
	}

	public String getCertificado() {
		return getValueFromTablaPreferencias(KEY_PREF_CERTIFICADO);
	}
	public String getTipoAlmacen() {
		return getValueFromTablaPreferencias(KEY_PREF_TIPO_ALMACEN);
	}
	public String getClaveAlmacen() {
		return getValueFromTablaPreferencias(KEY_PREF_ALMACEN_PASSWORD);
	}
	
	public String getClaveCertificado() {
		return getValueFromTablaPreferencias(KEY_PREF_CERTIFICADO_PASSWORD);
	}
	public String getAliasCertificadoWSSecurity() {
		return getValueFromTablaPreferencias(KEY_PREF_ALIAS_SEGURIDAD_SW);
	}
	public String getEndpointSeguridadSW() {
		return getValueFromTablaPreferencias(KEY_PREF_SEGURIDAD_SW);
	}
	public Properties getUriServicioXades() throws PreferenciasException{
		Properties p= new Properties();
		String ruta= getValueFromTablaPreferencias(KEY_PREF_URI_XADES);
		BufferedInputStream bis=null;
		try{
			bis=new BufferedInputStream(new FileInputStream(ruta));
			p.load(bis);
			return p;
		} catch (IOException i){
			throw new PreferenciasException("Error al cargar uris de formas Xades:"+i.getMessage(),i);
		} finally {
			if (bis!=null){
				try { bis.close();} catch (Exception ex) {}
			}
		}
	}
	
	public Properties getUriServicioPades() throws PreferenciasException{
		Properties p= new Properties();
		String ruta= getValueFromTablaPreferencias(KEY_PREF_URI_PADES);
		BufferedInputStream bis=null;
		try{
			bis= new BufferedInputStream(new FileInputStream(ruta));
			p.load(bis);
			return p;
		} catch (IOException i){
			throw new PreferenciasException("Error al cargar uris de formas Pades:"+i.getMessage(),i);
		} finally  {
			if ( bis!= null) {
				try { bis.close();} catch (Exception e) {}
			}
		}
	}
	public String getIdAplicacion() {
		return getValueFromTablaPreferencias(KEY_PREF_APLICACION);
	}

	/**
	 * Recupera la plantilla de actualización de firma pades marcada por la preferencia
	 * @return
	 * @throws PreferenciasException
	 */
	public Document getPlantillaUpgradePades() throws PreferenciasException{
		String ruta= getValueFromTablaPreferencias(KEY_PREF_PADES_XML);
		try{
			return XMLUtils.readXmlFile(ruta);
			
		} catch (ParserConfigurationException pce) {
			throw new PreferenciasException("Error al cargar plantilla de upgrade Pades:"+pce.getMessage(),pce);
		} catch (SAXException s) {
			throw new PreferenciasException("Error al cargar plantilla de upgrade Pades:"+s.getMessage(),s);
		}
		catch (IOException i){
			throw new PreferenciasException("Error al cargar plantilla de upgrade Pades:"+i.getMessage(),i);
		} 
	}
	
	/**
	 * Recupera la plantilla de actualización de firma pades marcada por la preferencia
	 * @return
	 * @throws PreferenciasException
	 */
	public Document getPlantillaUpgradeXades() throws PreferenciasException{
		String ruta= getValueFromTablaPreferencias(KEY_PREF_XADES_XML);
		try{
			return XMLUtils.readXmlFile(ruta);
			
		} catch (ParserConfigurationException pce) {
			throw new PreferenciasException("Error al cargar plantilla de upgrade Xades:"+pce.getMessage(),pce);
		} catch (SAXException s) {
			throw new PreferenciasException("Error al cargar plantilla de upgrade Xades:"+s.getMessage(),s);
		}
		catch (IOException i){
			throw new PreferenciasException("Error al cargar plantilla de upgrade Xades:"+i.getMessage(),i);
		} 
	}
	
	
	
}
