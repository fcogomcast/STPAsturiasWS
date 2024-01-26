package es.tributasenasturias.documentopagoutils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.PropertyResourceBundle;

//import com.stpa.ws.server.constantes.LiquidacionTasasConstantes;
import es.tributasenasturias.documentopagoutils.StpawsException;

public class PropertiesUtils {
	private final static String FICHERO_CONFIG = "es.tributasenasturias.configuracion.messages";
	private static final HashMap storedProp = new HashMap();
	
	static {
		try {
			cargarProperties(FICHERO_CONFIG);
		} catch (StpawsException e) {
			es.tributasenasturias.documentopagoutils.Logger.error(e.getMessage(),e,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
	}

	public static void cargarProperties(String rutaFicheroProperties) throws StpawsException {
		cargarProperties(rutaFicheroProperties, null);
	}

	public static synchronized void cargarProperties(String rutaFicheroProperties, Locale locale) throws StpawsException {
		String key = rutaFicheroProperties;

		if (!(storedProp.containsKey(key))) {
			PropertyResourceBundle propertyRB = null;
			Properties tablaProperties = new Properties();
			try {
				if (locale == null) {
					propertyRB = (PropertyResourceBundle) PropertyResourceBundle.getBundle(rutaFicheroProperties);
				} else {
					propertyRB = (PropertyResourceBundle) PropertyResourceBundle.getBundle(rutaFicheroProperties, locale);
				}
			} catch (MissingResourceException me) {
				es.tributasenasturias.documentopagoutils.Logger.error(me.getMessage(),me,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			}

			Enumeration keys = propertyRB.getKeys();
			String aKey = null;

			while (keys.hasMoreElements()) {
				aKey = (String) keys.nextElement();
				tablaProperties.put(aKey, propertyRB.getString(aKey));
			}
			storedProp.put(key, tablaProperties);
		}
	}

	public static Hashtable getCacheProperties(String rutaFicheroProperties) {
		return getCacheProperties(rutaFicheroProperties, null);
	}

	public static Hashtable getCacheProperties(String rutaFicheroProperties, Locale locale) {
		String key = rutaFicheroProperties;
		if (locale != null)
			key = key + locale.getLanguage();
		if (storedProp.containsKey(key))
			return ((Hashtable) ((Hashtable) storedProp.get(key)).clone());

		return null;
	}

	public static String getValorConfiguracion(String rutaFicheroProperties, String keyValorConfiguracion) throws StpawsException {
		return getValorConfiguracion(rutaFicheroProperties, keyValorConfiguracion, null);
	}
	
	public static String getValorConfiguracion(String keyValorConfiguracion) throws StpawsException {
		return getValorConfiguracion(FICHERO_CONFIG, keyValorConfiguracion, null);
	}

	public static String getValorConfiguracion(String rutaFicheroProperties, String keyValorConfiguracion, Locale locale) throws StpawsException {
		String key = rutaFicheroProperties;
		if (!(storedProp.containsKey(key)))
			cargarProperties(key, locale);
		return ((Properties) storedProp.get(key)).getProperty(keyValorConfiguracion);
	}
	
	public static String getValorConfiguracionLang(String rutaFicheroProperties, String keyValorConfiguracion, Locale locale) throws StpawsException {
		return getValorConfiguracion(rutaFicheroProperties, keyValorConfiguracion, locale);
	}
}
