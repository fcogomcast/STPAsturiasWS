package es.stpa.notificagestionenvios.preferencias;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.prefs.Preferences;




public class Preferencias {
	private Preferences m_preferencias;
	private final String DIRECTORIO_SERVICIO = "proyectos/WSNotificaGestionEnvios";
	private final String FICHERO_PREFERENCIAS = "prefsNotificaGestionEnvios.xml";
	private final String DIRECTORIO_PREFERENCIAS = DIRECTORIO_SERVICIO;

	private HashMap<String, String> tablaPreferencias = new HashMap<String, String>();

	// nombres de las preferencias
	private final String KEY_PREF_LOG = "ModoLog";
	private final String KEY_PREF_LOG_APLICACION = "ficheroLogAplicacion";
	private final String KEY_PREF_LOG_CLIENT = "ficheroLogClient";
	private final String KEY_PREF_LOG_SERVER = "ficheroLogServer";
	private final String KEY_PREF_ENDPOINT_ARCHIVO_DIGITAL="EndpointArchivoDigital";
	private final String KEY_PREF_NOTIFICA_API_KEY="NotificaAPIKey"; 
	private final String KEY_PREF_ENDPOINT_NOTIFICA="EndpointNotificaGestionEnvios";	
	private final String KEY_PREF_ENDPOINT_SINCRONIZA_NOTIFICA="EndpointNotificaSincronizarEnvio";
	private final String KEY_PREF_PROC_DATADO = "ProcAlmacenadoDatadoNotificacion";
	private final String KEY_PREF_PROC_CERTIFICACION= "ProcAlmacenadoCertificacion";
	private final String KEY_PREF_ENDPOINT_LANZADOR= "EndPointLanzador";
	private final String KEY_PREF_ESQUEMA_BD="EsquemaBD";
	//CRUBENCVS 46641 23/11/2022
	//Soporte para los envíos por SOAP with Attachments
	private final String KEY_PREF_ENVIO_FICHERO_ADJUNTO="EnvioFicheroAltaAdjunto";
	private final String KEY_PREF_ENDPOINT_NOTIFICA_SwA="EndpointNotificaGestionEnvios_FicheroAdjunto";
	//FIN CRUBENCVS 46641 23/11/2022
	
	
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

	private void InicializaTablaPreferencias() {

		tablaPreferencias.clear();

		tablaPreferencias.put(KEY_PREF_LOG, 				 		"INFO");
		tablaPreferencias.put(KEY_PREF_LOG_APLICACION,	 	 		DIRECTORIO_SERVICIO	+ "/logs/Application.log");
		tablaPreferencias.put(KEY_PREF_LOG_CLIENT, 		 	 		DIRECTORIO_SERVICIO	+ "/logs/Soap_Client.log");
		tablaPreferencias.put(KEY_PREF_LOG_SERVER, 			 		DIRECTORIO_SERVICIO	+ "/logs/Soap_Server.log");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_ARCHIVO_DIGITAL,	"http://bus:7101/WSInternos/ProxyServices/PXArchivoDigital");
		tablaPreferencias.put(KEY_PREF_NOTIFICA_API_KEY,			"MjY5Mzg2NTQxMjc5NTY5MjY3Mg==");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_NOTIFICA, 			"http://bus:7101/WSNotifica/ProxyServices/PXGestionEnvios_V21_Notifica");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_SINCRONIZA_NOTIFICA,"http://bus:7101/WSNotifica/ProxyServices/PXSincronizarEnvio");
		tablaPreferencias.put(KEY_PREF_PROC_DATADO,		 			"SERVICIOSWEB_ENTRADA.DATADONOTIFICACION");
		tablaPreferencias.put(KEY_PREF_PROC_CERTIFICACION,			"SERVICIOSWEB_ENTRADA.CERTIFICACIONNOTIFICA");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_LANZADOR, 	 		"http://bus:7101/WSInternos/ProxyServices/PXLanzador");
		tablaPreferencias.put(KEY_PREF_ESQUEMA_BD,					"EXPLOTACION");
		//CRUBENCVS 46641 23/11/2022
		//Soporte para los envíos por SOAP with Attachments
		tablaPreferencias.put(KEY_PREF_ENVIO_FICHERO_ADJUNTO,		"N");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_NOTIFICA_SwA,		"http://bus:7101/WSNotifica/ProxyServices/PXGestionEnvios_V2_SwA_Notifica");
		//FIN CRUBENCVS 46641 23/11/2022
	}

	private boolean CompruebaFicheroPreferencias()
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


	public String getModoLog() {
		return getValueFromTablaPreferencias(KEY_PREF_LOG);
	}

	public String getFicheroLogAplicacion() {
		return getValueFromTablaPreferencias(KEY_PREF_LOG_APLICACION);
	}
	public String getFicheroLogClient() {
		return getValueFromTablaPreferencias(KEY_PREF_LOG_CLIENT);
	}

	public String getFicheroLogServer() {
		return getValueFromTablaPreferencias(KEY_PREF_LOG_SERVER);
	}
	
	public String getNotificaAPIKey() {
		return getValueFromTablaPreferencias(KEY_PREF_NOTIFICA_API_KEY);
	}
	
	public String getEndpointGestionEnvioNotifica() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_NOTIFICA);
	}
	public String getEndpointArchivoDigital(){
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_ARCHIVO_DIGITAL);
	}
	
	public String getEndpointNotificaSincronizarEnvio() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_SINCRONIZA_NOTIFICA);
	}
	
	public String getProcDatado() {
		return getValueFromTablaPreferencias(KEY_PREF_PROC_DATADO);
	}
	
	public String getProcCertificacion(){
		return getValueFromTablaPreferencias(KEY_PREF_PROC_CERTIFICACION);
	}
	
	public String getEndpointLanzador() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_LANZADOR);
	}
	
	public String getEsquemaBD() {
		return getValueFromTablaPreferencias(KEY_PREF_ESQUEMA_BD);
	}
	//CRUBENCVS 46641 23/11/2022
	//Soporte para los envíos por SOAP with Attachments
	public String getEndpointGestionEnviosNotificaSwA(){
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_NOTIFICA_SwA);
	}
	public String getEnvioFicheroAltaAdjunto(){
		return getValueFromTablaPreferencias(KEY_PREF_ENVIO_FICHERO_ADJUNTO);
	}
	//FIN CRUBENCVS 46641 23/11/2022
}
