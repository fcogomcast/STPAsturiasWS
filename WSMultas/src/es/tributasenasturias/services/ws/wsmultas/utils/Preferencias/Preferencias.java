package es.tributasenasturias.services.ws.wsmultas.utils.Preferencias;

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
	static private Preferencias _pref = new Preferencias();
	private Preferences m_preferencias;
        private final static boolean SobreEscribeFichPref = true;
	private final static String FICHERO_PREFERENCIAS = "PreferenciasWSMultas.xml";
	private final static String DIRECTORIO_PREFERENCIAS = "proyectos//WSMultas";
	private HashMap<String, String> tablaPreferencias = new HashMap<String, String>();

	// nombres de las preferencias

	private final static String KEY_PREF_NOM_PROCEDIMIENTO = "NomProcedimiento";
	private final static String KEY_PREF_NOM_PROC_ALTADOC = "NomProcAltaDoc";
	private final static String KEY_PREF_ENDPOINT_ISL = "EndPointISL";
	private final static String KEY_PREF_LOG = "ModoLog";
	private final static String KEY_PREF_ESQUEMA = "Esquema";
	private final static String KEY_PREF_IP_LANZADOR = "IpLanzador";
	private final static String KEY_PREF_CLIENT_LOG_HANDLER_DIR = "ClientLogHandlerDir";
	private final static String KEY_PREF_CLIENT_LOG_HANDLER_FILE = "ClientLogHandlerFile";
	private final static String KEY_PREF_SERVER_LOG_HANDLER_DIR = "ServerLogHandlerDir";
	private final static String KEY_PREF_SERVER_LOG_HANDLER_FILE = "ServerLogHandlerFile";
	private final static String KEY_PREF_COD_RESULTADO_OK = "0";
	private final static String KEY_PREF_PROVIDER_URL = "PROVIDER_URL";
	private final static String KEY_PREF_DATASOURCE = "DATASOURCE";
	private final static String KEY_PREF_DEBUG_SOAP = "DEBUG_SOAP";

	private Preferencias() {
		try {
			CargarPreferencias();
		} catch (Exception e) {
			// Para comprobar posteriormente si se ha creado bien, se comprobará
			// que la
			// variable privada no es estática.
		}
	}

	@SuppressWarnings("static-access")
	protected synchronized void CargarPreferencias() throws Exception {
		if (CompruebaFicheroPreferencias()) {
			// Logger.debug("INICIA CARGA DE PREFERENCIAS DESDE FICHERO");

			FileInputStream inputStream = new FileInputStream(DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
			Preferences.systemNodeForPackage(this.getClass()).importPreferences(inputStream);
			inputStream.close();

			// Logger.debug("Fichero importado");

			m_preferencias = Preferences.systemNodeForPackage(this.getClass());

			String[] keys = m_preferencias.keys();
			String msgKeys = "Leyendo las siguientes claves -> ";
			for (int i = 0; i < keys.length; i++) {
				msgKeys += "[" + keys[i] + "] ";
			}
			// Logger.debug(msgKeys);

			for (int i = 0; i < keys.length; i++) {
				String value = m_preferencias.get(keys[i], "");
				// Logger.debug("Cargando en la tabla ['"+keys[i]+"' -
				// '"+value+"']");

				tablaPreferencias.put(keys[i], value);
			}
			// Logger.debug("FIN CARGA DE PREFERENCIAS DESDE FICHERO");
		}
	}

	private void InicializaTablaPreferencias() {
		// Logger.debug("Cargando tabla con preferencias por defecto");

		tablaPreferencias.clear();
		tablaPreferencias.put(KEY_PREF_ENDPOINT_ISL,"http://bus.prue.epst.pa:7101/WSInternos/ProxyServices/PXLanzador");
		tablaPreferencias.put(KEY_PREF_NOM_PROCEDIMIENTO,"SW_MULTAS.ALTAMULTA");
		tablaPreferencias.put(KEY_PREF_NOM_PROC_ALTADOC,"EXPTRAFICO_CARGAS.ADJUNTARIMAGENESEXP");
		tablaPreferencias.put(KEY_PREF_LOG, "ALL");
		tablaPreferencias.put(KEY_PREF_ESQUEMA, "EXPLOTACION");
		tablaPreferencias.put(KEY_PREF_IP_LANZADOR, "10.112.22.69");
		tablaPreferencias.put(KEY_PREF_CLIENT_LOG_HANDLER_DIR,"WSMultas");
		tablaPreferencias.put(KEY_PREF_CLIENT_LOG_HANDLER_FILE,"SOAP_CLIENT.log");
		tablaPreferencias.put(KEY_PREF_SERVER_LOG_HANDLER_DIR,"WSMultas");
		tablaPreferencias.put(KEY_PREF_SERVER_LOG_HANDLER_FILE,"SOAP_SERVER.log");
		tablaPreferencias.put(KEY_PREF_COD_RESULTADO_OK,"0");
		tablaPreferencias.put(KEY_PREF_PROVIDER_URL, "t3://localhost:7001");
		tablaPreferencias.put(KEY_PREF_DATASOURCE, "gtpaDS");
		tablaPreferencias.put(KEY_PREF_DEBUG_SOAP, "ALL");
	}

	private boolean CompruebaFicheroPreferencias() {
		boolean existeFichero = false;

		File f = new File(DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
		existeFichero = f.exists();
		if (SobreEscribeFichPref || existeFichero == false) {
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
		// Logger.debug("INICIO CREACION FICHERO PREFERENCIAS");

		// preferencias por defecto
		m_preferencias = Preferences.systemNodeForPackage(this.getClass());

		InicializaTablaPreferencias();

		// recorremos la tabla cargada con las preferencias por defecto
		Iterator itr = tablaPreferencias.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, String> e = (Map.Entry) itr.next();
			// Logger.debug("Cargando en fichero preferencias ['"+e.getKey()+"'
			// - '"+e.getValue()+"']");

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
			// Logger.debug("Fichero preferencias creado en disco");
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

		// Logger.debug("FIN CREACION FICHERO PREFERENCIAS");
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

		// Logger.debug("Se ha pedido la preferencia '"+key+"' a lo que el
		// sistema devuelve '"+toReturn+"'");

		return toReturn;
	}

	private synchronized void setValueIntoTablaPreferencias(String key,
			String value) {
		// Logger.debug("Se actualizara el valor de la preferencia '"+key+"' a
		// '"+value+"'");
		tablaPreferencias.put(key, value);
	}

	// Este método devolverá la instancia de clase.
	public synchronized static Preferencias getPreferencias() throws Exception {
		if (_pref == null) {
			throw new Exception("No se han podido recuperar las preferencias.");
		}
		_pref.CargarPreferencias();
		return _pref;
	}

	public String getEsquemaBaseDatos() {
		return getValueFromTablaPreferencias(KEY_PREF_ESQUEMA);
	}

	public void setEsquemaBaseDatos(String esquema) {
		setValueIntoTablaPreferencias(KEY_PREF_ESQUEMA, esquema);
	}

	public String getNomProcedimientoAltaMulta() {
		return getValueFromTablaPreferencias(KEY_PREF_NOM_PROCEDIMIENTO);
	}

	public void setNomProcedimientoAltaMulta(String nomProcedimiento) {
		setValueIntoTablaPreferencias(KEY_PREF_NOM_PROCEDIMIENTO,
				nomProcedimiento);
	}
	public String getNomProcedimientoAltaDocMulta() {
		return getValueFromTablaPreferencias(KEY_PREF_NOM_PROC_ALTADOC);
	}

	public void setNomProcedimientoAltaDocMulta(String nomProcedimiento) {
		setValueIntoTablaPreferencias(KEY_PREF_NOM_PROC_ALTADOC,
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
	public String getProviderURL() {
		return getValueFromTablaPreferencias(KEY_PREF_PROVIDER_URL);
	}
	public void setProviderURL(String providerURL) {
		setValueIntoTablaPreferencias(KEY_PREF_PROVIDER_URL, providerURL);
	}
	public String getDataSource() {
		return getValueFromTablaPreferencias(KEY_PREF_DATASOURCE);
	}
	public void setDataSource(String datasource) {
		setValueIntoTablaPreferencias(KEY_PREF_DATASOURCE, datasource);
	}
	public String getDebugSoap() {
		return getValueFromTablaPreferencias(KEY_PREF_DEBUG_SOAP);
	}
	public void setDebugSoap(String debug) {
		setValueIntoTablaPreferencias(KEY_PREF_DEBUG_SOAP, debug);
	}

}