package es.tributasenasturias.services.ws.wsepi.utils.log.Preferencias;

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
	private Preferences m_preferencias;

	private final static String FICHERO_PREFERENCIAS = "PreferenciasWSEPI.xml";
	private final static String DIRECTORIO_PREFERENCIAS = "proyectos//WSEPI";
	private HashMap<String, String> tablaPreferencias = new HashMap<String, String>();

	// nombres de las preferencias

	private final static String KEY_PREF_NOM_PROCEDIMIENTO = "NomProcedimiento";
	private final static String KEY_PREF_NOM_PROC_CALLBACK = "NomProcAltaDoc";
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
	private final static String KEY_PREF_PA_PARAMS_FIRMA= "pAParamFirmaReimprimible";

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
		tablaPreferencias.put(KEY_PREF_ENDPOINT_ISL,"http://bus:7101/WSInternos/ProxyServices/PXLanzador");
		tablaPreferencias.put(KEY_PREF_NOM_PROCEDIMIENTO,"EPI.actualizaridadar");
		tablaPreferencias.put(KEY_PREF_NOM_PROC_CALLBACK,"EPI.ejecutarcallback");
		tablaPreferencias.put(KEY_PREF_LOG, "INFO");
		tablaPreferencias.put(KEY_PREF_ESQUEMA, "EXPLOTACION");
		tablaPreferencias.put(KEY_PREF_IP_LANZADOR, "10.112.22.69");
		tablaPreferencias.put(KEY_PREF_CLIENT_LOG_HANDLER_DIR,"proyectos/WSEPI");
		tablaPreferencias.put(KEY_PREF_CLIENT_LOG_HANDLER_FILE,"SOAP_CLIENT.log");
		tablaPreferencias.put(KEY_PREF_SERVER_LOG_HANDLER_DIR,"proyectos/WSEPI");
		tablaPreferencias.put(KEY_PREF_SERVER_LOG_HANDLER_FILE,"SOAP_SERVER.log");
		tablaPreferencias.put(KEY_PREF_COD_RESULTADO_OK,"0");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_ARCHIVO,"http://bus:7101/WSInternos/ProxyServices/PXArchivoDigital");
		tablaPreferencias.put(KEY_PREF_APP_LOG_DIR,"proyectos/WSEPI");
		tablaPreferencias.put(KEY_PREF_APP_LOG_FILE,"Application.log");
		tablaPreferencias.put(KEY_PREF_PA_PARAMS_FIRMA,"GESTOR_DOCUMENTAL2.PARAMETROS_FIRMA_REIMPRIMIBLE");
		
	}

	private boolean CompruebaFicheroPreferencias() {
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

	public String getNomProcedimientoActualizarIdAdar() {
		return getValueFromTablaPreferencias(KEY_PREF_NOM_PROCEDIMIENTO);
	}

	public void setNomProcedimientoActualizarIdAdar(String nomProcedimiento) {
		setValueIntoTablaPreferencias(KEY_PREF_NOM_PROCEDIMIENTO,
				nomProcedimiento);
	}
	public String getNomProcedimientoEjecutarCallback() {
		return getValueFromTablaPreferencias(KEY_PREF_NOM_PROC_CALLBACK);
	}

	public void setNomProcedimientoEjecutarCallback(String nomProcedimiento) {
		setValueIntoTablaPreferencias(KEY_PREF_NOM_PROC_CALLBACK,
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
	public String getParametrosFirma() {
		return getValueFromTablaPreferencias(KEY_PREF_PA_PARAMS_FIRMA);
	}
	
	
}