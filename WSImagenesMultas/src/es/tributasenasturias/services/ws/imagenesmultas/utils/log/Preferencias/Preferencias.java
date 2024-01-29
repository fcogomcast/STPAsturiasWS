package es.tributasenasturias.services.ws.imagenesmultas.utils.log.Preferencias;

//Este paquete se comporta como singleton en acceso, es decir,
//sólo admite una instancia de la clase.
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.prefs.Preferences;

public class Preferencias {
	// Se hace instancia privada y estática.
	//private Preferencias _pref = new Preferencias();
	private Preferences m_preferencias;

	private final static String FICHERO_PREFERENCIAS = "PreferenciasWSImagenesMultas.xml";
	private final static String DIRECTORIO_PREFERENCIAS = "proyectos//WSImagenesMultas";
	private HashMap<String, String> tablaPreferencias = new HashMap<String, String>();

	// nombres de las preferencias

	private final static String KEY_PREF_NOM_PROCEDIMIENTO = "NomProcedimiento";
		private final static String KEY_PREF_ENDPOINT_ISL = "EndPointISL";
	private final static String KEY_PREF_LOG = "ModoLog";
	private final static String KEY_PREF_ESQUEMA = "Esquema";
	private final static String KEY_PREF_IP_LANZADOR = "IpLanzador";
	private final static String KEY_PREF_CLIENT_LOG_HANDLER_DIR = "ClientLogHandlerDir";
	private final static String KEY_PREF_CLIENT_LOG_HANDLER_FILE = "ClientLogHandlerFile";
	private final static String KEY_PREF_SERVER_LOG_HANDLER_DIR = "ServerLogHandlerDir";
	private final static String KEY_PREF_SERVER_LOG_HANDLER_FILE = "ServerLogHandlerFile";
	private final static String KEY_PREF_COD_RESULTADO_OK = "0";
	private final static String KEY_PREF_ENDPOINT_ARCHIVO = "EndPointArchivoDigital";
	private final static String KEY_PREF_APP_LOG_DIR = "ApplicationLogDir";
	private final static String KEY_PREF_APP_LOG_FILE = "ApplicationLogFile";

	private Preferencias() throws Exception{
		CargarPreferencias();
	}

	@SuppressWarnings("static-access")
	protected void CargarPreferencias() throws Exception {
		if (CompruebaFicheroPreferencias()) {
			// Logger.debug("INICIA CARGA DE PREFERENCIAS DESDE FICHERO");

			FileInputStream inputStream = new FileInputStream(DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
			Preferences.systemNodeForPackage(this.getClass()).importPreferences(inputStream);
			inputStream.close();

			m_preferencias = Preferences.systemNodeForPackage(this.getClass());

			String[] keys = m_preferencias.keys();
			String msgKeys = "Leyendo las siguientes claves -> ";
			for (int i = 0; i < keys.length; i++) {
				msgKeys += "[" + keys[i] + "] ";
			}

			for (int i = 0; i < keys.length; i++) {
				String value = m_preferencias.get(keys[i], "");
				// Logger.debug("Cargando en la tabla ['"+keys[i]+"' -
				// '"+value+"']");

				tablaPreferencias.put(keys[i], value);
			}

		}
	}

	private void InicializaTablaPreferencias() {

		tablaPreferencias.clear();
		tablaPreferencias.put(KEY_PREF_ENDPOINT_ISL,"http://bus:7101/WSInternos/ProxyServices/PXLanzador");
		tablaPreferencias.put(KEY_PREF_NOM_PROCEDIMIENTO,"internet_multas.obtenerimagenesmultas");
		tablaPreferencias.put(KEY_PREF_LOG, "INFO");
		tablaPreferencias.put(KEY_PREF_ESQUEMA, "EXPLOTACION");
		tablaPreferencias.put(KEY_PREF_IP_LANZADOR, "10.112.22.69");
		tablaPreferencias.put(KEY_PREF_CLIENT_LOG_HANDLER_DIR,"proyectos/WSImagenesMultas");
		tablaPreferencias.put(KEY_PREF_CLIENT_LOG_HANDLER_FILE,"SOAP_CLIENT.log");
		tablaPreferencias.put(KEY_PREF_SERVER_LOG_HANDLER_DIR,"proyectos/WSImagenesMultas");
		tablaPreferencias.put(KEY_PREF_SERVER_LOG_HANDLER_FILE,"SOAP_SERVER.log");
		tablaPreferencias.put(KEY_PREF_COD_RESULTADO_OK,"0");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_ARCHIVO,"http://bus:7101/WSInternos/ProxyServices/PXArchivoDigital");
		tablaPreferencias.put(KEY_PREF_APP_LOG_DIR,"proyectos/WSImagenesMultas");
		tablaPreferencias.put(KEY_PREF_APP_LOG_FILE,"Application.log");
	}

	private boolean CompruebaFicheroPreferencias() {
		boolean existeFichero = false;

		File f = new File(DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
		existeFichero = f.exists();
		if (existeFichero == false) {
			// Logger.debug("El fichero de preferencias
			// ("+DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS+") no
			// existe!");
			CrearFicheroPreferencias();
		}

		return existeFichero;
	}

	/***************************************************************************
	 * 
	 * Creamos el fichero de preferencias con los valores por defecto
	 * 
	 **************************************************************************/
	@SuppressWarnings("unchecked")
	private synchronized void CrearFicheroPreferencias() {

		// preferencias por defecto
		m_preferencias = Preferences.systemNodeForPackage(this.getClass());

		InicializaTablaPreferencias();

		// recorremos la tabla cargada con las preferencias por defecto
		Iterator itr = tablaPreferencias.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, String> e = (Map.Entry) itr.next();

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
			 //logger.error(e.getMessage());
			// logger.trace(e.getStackTrace());
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (Exception e) {
				// logger.error("Error cerrando fichero de preferencias -> " +
				// e.getMessage());
				// logger.trace(e.getStackTrace());
			}
		}
	}

	public void recargaPreferencias() throws Exception {
		// Logger.debug("Recarga del fichero de preferencias");

		CargarPreferencias();
	}

	private String getValueFromTablaPreferencias(String key) {
		String toReturn = "";

		if (tablaPreferencias.containsKey(key)) {
			toReturn = tablaPreferencias.get(key);
		}

		return toReturn;
	}

	private void setValueIntoTablaPreferencias(String key,
			String value) {
		tablaPreferencias.put(key, value);
	}

	// Este método devolverá la instancia de clase.
	public static Preferencias getPreferencias() throws Exception {
		Preferencias _pref = new Preferencias();
		_pref.CargarPreferencias();
		return _pref;
	}

	public String getEsquemaBaseDatos() {
		return getValueFromTablaPreferencias(KEY_PREF_ESQUEMA);
	}

	public void setEsquemaBaseDatos(String esquema) {
		setValueIntoTablaPreferencias(KEY_PREF_ESQUEMA, esquema);
	}

	public String getNomProcedimiento() {
		return getValueFromTablaPreferencias(KEY_PREF_NOM_PROCEDIMIENTO);
	}

	public void setNomProcedimiento(String nomProcedimiento) {
		setValueIntoTablaPreferencias(KEY_PREF_NOM_PROCEDIMIENTO,
				nomProcedimiento);
	}

	public String getEndPointISL() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_ISL);
	}

	public void setEndPointISL(String endPointISL) {
		setValueIntoTablaPreferencias(KEY_PREF_ENDPOINT_ISL, endPointISL);
	}

	public String getModoLog() {
		return getValueFromTablaPreferencias(KEY_PREF_LOG);
	}

	public void setModoLog(String modo) {
		setValueIntoTablaPreferencias(KEY_PREF_LOG, modo);
	}

	public String getIPLanzador() {
		return getValueFromTablaPreferencias(KEY_PREF_IP_LANZADOR);
	}

	public void setIPLanzador(String ipLanzador) {
		setValueIntoTablaPreferencias(KEY_PREF_IP_LANZADOR, ipLanzador);
	}

	public String getServerLogHandlerFile() {
		return getValueFromTablaPreferencias(KEY_PREF_SERVER_LOG_HANDLER_FILE);
	}

	public void setServerLogHandlerFile(String serverLogHandlerFile) {
		setValueIntoTablaPreferencias(KEY_PREF_SERVER_LOG_HANDLER_FILE,
				serverLogHandlerFile);
	}

	public String getServerLogHandlerDir() {
		return getValueFromTablaPreferencias(KEY_PREF_SERVER_LOG_HANDLER_DIR);
	}

	public void setServerLogHandlerDir(String serverLogHandlerDir) {
		setValueIntoTablaPreferencias(KEY_PREF_SERVER_LOG_HANDLER_DIR,
				serverLogHandlerDir);
	}

	public String getClientLogHandlerFile() {
		return getValueFromTablaPreferencias(KEY_PREF_CLIENT_LOG_HANDLER_FILE);
	}

	public void setClientLogHandlerFile(String clientLogHandlerFile) {
		setValueIntoTablaPreferencias(KEY_PREF_CLIENT_LOG_HANDLER_FILE,
				clientLogHandlerFile);
	}

	public String getClientLogHandlerDir() {
		return getValueFromTablaPreferencias(KEY_PREF_CLIENT_LOG_HANDLER_DIR);
	}

	public void setClientLogHandlerDir(String clientLogHandlerDir) {
		setValueIntoTablaPreferencias(KEY_PREF_CLIENT_LOG_HANDLER_DIR,
				clientLogHandlerDir);
	}

	public String getCodResultadoOK() {
		return getValueFromTablaPreferencias(KEY_PREF_COD_RESULTADO_OK);
	}

	public void setCodResultadoOK(String cod) {
		setValueIntoTablaPreferencias(KEY_PREF_COD_RESULTADO_OK, cod);
	}
	
	public String getEndpointArchivoDigital() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_ARCHIVO);
	}
	
	public String getAppLogDir() {
		return getValueFromTablaPreferencias(KEY_PREF_APP_LOG_DIR);
	}
	
	public String getAppLogFile() {
		return getValueFromTablaPreferencias(KEY_PREF_APP_LOG_FILE);
	}
}