package es.stpa.smartmultas.preferencias;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.prefs.Preferences;

public class Preferencias {
	
	private Preferences m_preferencias;
	private final static String DIRECTORIO_SERVICIO = "proyectos/WSServiciosSmartMultas";
	private final static String FICHERO_PREFERENCIAS = "prefsServiciosSmartMultas.xml";
	private final static String DIRECTORIO_PREFERENCIAS = DIRECTORIO_SERVICIO;

	private HashMap<String, String> tablaPreferencias = new HashMap<String, String>();

	// nombres de las preferencias
	private final static String KEY_PREF_LOG = "ModoLog";
	private final static String KEY_PREF_DIR_LOG = "directorioRaizLogs";
	private final static String KEY_PREF_LOG_APLICACION = "ficheroLogAplicacion";
	private final static String KEY_PREF_LOG_CLIENT = "ficheroLogClient";
	private final static String KEY_PREF_LOG_SERVER = "ficheroLogServer";
	private final static String KEY_PREF_ESQUEMA = "EsquemaBD";
	private final static String KEY_PREF_ENDPOINT_LANZADOR = "EndPointLanzador";
	private final static String KEY_PREF_ENDPOINT_LANZADOR_MASIVO = "EndPointLanzadorMasivo";
	private final static String KEY_PREF_USUARIO_LOGIN = "UsuarioLogin";
	private final static String KEY_PREF_PASSWORD_LOGIN = "PasswordLogin";

	
	public Preferencias() throws PreferenciasException {
		cargarPreferencias();
	}

	
	public void cargarPreferencias() throws PreferenciasException {
		
		FileInputStream inputStream = null;
		try 
		{
			if (CompruebaFicheroPreferencias()) 
			{

				inputStream = new FileInputStream(DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
				Preferences.importPreferences(inputStream);
				inputStream.close();

				m_preferencias = Preferences.systemNodeForPackage(this.getClass());

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
		} 
		catch (Exception ex) 
		{
			throw new PreferenciasException("Error al cargar las preferencias: " + ex.getMessage(), ex);
		} 
		finally 
		{
			if (inputStream != null) 
			{
				try 
				{
					inputStream.close();
				} 
				catch (Exception e) { }
			}
		}
	}

	private void InicializaTablaPreferencias() {

		tablaPreferencias.clear();

		tablaPreferencias.put(KEY_PREF_LOG, "INFO");
		tablaPreferencias.put(KEY_PREF_DIR_LOG, DIRECTORIO_SERVICIO + "/logs");
		tablaPreferencias.put(KEY_PREF_LOG_APLICACION, "Application.log");
		tablaPreferencias.put(KEY_PREF_LOG_CLIENT, "Soap_Client.log");
		tablaPreferencias.put(KEY_PREF_LOG_SERVER, "Soap_Server.log");
		//tablaPreferencias.put(KEY_PREF_ENDPOINT_LANZADOR, "http://bus.desa.epst.pa:7101/WSInternos/ProxyServices/PXLanzador");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_LANZADOR_MASIVO, "http://bus.prue.epst.pa:7101/WSInternos/ProxyServices/PXLanzadorMasivo");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_LANZADOR, "http://bus.prue.epst.pa:7101/WSInternos/ProxyServices/PXLanzador");
		tablaPreferencias.put(KEY_PREF_ESQUEMA, "EXPLOTACION");
		tablaPreferencias.put(KEY_PREF_USUARIO_LOGIN, "smartfines");
		tablaPreferencias.put(KEY_PREF_PASSWORD_LOGIN, "$Smartfines_16");
	}

	private boolean CompruebaFicheroPreferencias() throws PreferenciasException {
		
		boolean existeFichero = false;

		File f = new File(DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
		existeFichero = f.exists();
		if (existeFichero == false) 
		{
			CrearFicheroPreferencias();
		}

		return existeFichero;
	}

	/***************************************************************************
	 * 
	 * Creamos el fichero de preferencias con los valores por defecto
	 * 
	 **************************************************************************/
	private void CrearFicheroPreferencias() throws PreferenciasException {

		// preferencias por defecto
		m_preferencias = Preferences.systemNodeForPackage(this.getClass());

		InicializaTablaPreferencias();

		// recorremos la tabla cargada con las preferencias por defecto
		Iterator<Map.Entry<String, String>> itr = tablaPreferencias.entrySet().iterator();
		while (itr.hasNext()) 
		{
			Map.Entry<String, String> e = (Map.Entry<String, String>) itr.next();

			m_preferencias.put(e.getKey(), e.getValue());
		}

		FileOutputStream outputStream = null;
		File fichero;
		
		try 
		{
			fichero = new File(DIRECTORIO_PREFERENCIAS);
			if (fichero.exists() == false)
				if (fichero.mkdirs() == false) {
					throw new java.io.IOException("No se puede crear el directorio de las preferencias.");
				}

			outputStream = new FileOutputStream(DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
			m_preferencias.exportNode(outputStream);
		} 
		catch (Exception e) 
		{
			throw new PreferenciasException("Error al crear el fichero de preferencias:" + e.getMessage(), e);
		} 
		finally 
		{
			try 
			{
				if (outputStream != null)
					outputStream.close();
			} 
			catch (Exception e) 
			{
				throw new PreferenciasException("Error al cerrar el flujo del fichero de preferencias:" + e.getMessage(), e);
			}
		}
	}

	public void recargaPreferencias() throws PreferenciasException 
	{
		cargarPreferencias();
	}

	private String getValueFromTablaPreferencias(String key) {
		String toReturn = "";

		if (tablaPreferencias.containsKey(key)) 
		{
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

	public String getEndpointLanzador() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_LANZADOR);
	}
	
	public String getEndpointLanzadorMasivo() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_LANZADOR_MASIVO);
	}

	public String getEsquemaBD() {
		return getValueFromTablaPreferencias(KEY_PREF_ESQUEMA);
	}

	public String getDirectorioRaizLog() {
		return getValueFromTablaPreferencias(KEY_PREF_DIR_LOG);
	}

	public String getUsuarioLogin() {
		return getValueFromTablaPreferencias(KEY_PREF_USUARIO_LOGIN);
	}

	public String getPasswordLogin() {
		return getValueFromTablaPreferencias(KEY_PREF_PASSWORD_LOGIN);
	}
}
