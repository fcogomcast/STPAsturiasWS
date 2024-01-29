package es.tributasenasturias.services.ws.custodiaimagenes.utils.Preferencias;

//Este paquete se comporta como singleton en acceso, es decir,
//s�lo admite una instancia de la clase.
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.prefs.Preferences;


public class Preferencias
{
	//Se hace instancia privada y est�tica.
	static private Preferencias _pref = new Preferencias();
	private Preferences m_preferencias;
	private final static String FICHERO_PREFERENCIAS = "preferenciasCustodiaImagenes.xml";
 private final static String DIRECTORIO_PREFERENCIAS = "proyectos/ArchivoDigital";
 
 private HashMap<String, String> tablaPreferencias = new HashMap<String, String>();
	
	//nombres de las preferencias
	private final static String KEY_PREF_PROVIDER_URL = "PROVIDER_URL";
	private final static String KEY_PREF_DATASOURCE = "DATASOURCE";
	private final static String KEY_PREF_DEBUG_SOAP = "DEBUG_SOAP";
	private final static String KEY_PREF_SERVER_LOG_HANDLER_DIR = "ServerLogHandlerDir";
	private final static String KEY_PREF_SERVER_LOG_HANDLER_FILE = "ServerLogHandlerFile";
	private final static String KEY_PREF_APP_LOG_DIR = "ApplicationLogDir";
	private final static String KEY_PREF_APP_LOG_FILE = "ApplicationLogFile";
	
	private Preferencias() 
	{		
		try
		{
			CargarPreferencias();
		}
		catch (Exception e)
		{
			//Para comprobar posteriormente si se ha creado bien, se comprobar� que la 
			//variable privada no es est�tica.
		}
	}
	protected void CargarPreferencias() throws PreferenciasException
 {
		try
		{
			if(CompruebaFicheroPreferencias())
			{		       
				
		        FileInputStream inputStream = new FileInputStream(DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
		        Preferences.importPreferences(inputStream);
		        inputStream.close();
		        m_preferencias = Preferences.systemNodeForPackage(this.getClass());
		        String[] keys = m_preferencias.keys();
		        for(int i=0;i<keys.length;i++)
		        {
		        	String value = m_preferencias.get(keys[i], "");
		        	tablaPreferencias.put(keys[i], value);
		        }
			}
		}
		catch (Exception ex)
		{
			throw new PreferenciasException (ex.getMessage());
		}
		
 }
	private void InicializaTablaPreferencias()
	{
		tablaPreferencias.clear();
		tablaPreferencias.put(KEY_PREF_PROVIDER_URL,				"t3://localhost:7001");
		tablaPreferencias.put(KEY_PREF_DATASOURCE,					"archivoDS");
		tablaPreferencias.put(KEY_PREF_DEBUG_SOAP,					"ALL");
		tablaPreferencias.put(KEY_PREF_SERVER_LOG_HANDLER_DIR,		DIRECTORIO_PREFERENCIAS);
		tablaPreferencias.put(KEY_PREF_SERVER_LOG_HANDLER_FILE,		"SOAP_SERVER_CustodiaImagenes.log");
		tablaPreferencias.put(KEY_PREF_APP_LOG_DIR,					DIRECTORIO_PREFERENCIAS);
		tablaPreferencias.put(KEY_PREF_APP_LOG_FILE,				"Application_CustodiaImagenes.log");
	}
	
	private boolean CompruebaFicheroPreferencias()
 {
		boolean existeFichero = false;
		
     File f = new File(DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
     existeFichero = f.exists();
     if (existeFichero == false)
     {
         CrearFicheroPreferencias();
     }
     
     return existeFichero;
 }
	 /***********************************************************
  * 
  * Creamos el fichero de preferencias con los valores por 
  * defecto
  * 
  ***************************************************************/
 private void CrearFicheroPreferencias()
 {
     //preferencias por defecto
     m_preferencias = Preferences.systemNodeForPackage(this.getClass());
     
     InicializaTablaPreferencias();
     
     //recorremos la tabla cargada con las preferencias por defecto
     Iterator<Map.Entry<String, String>> itr = tablaPreferencias.entrySet().iterator();
     while(itr.hasNext())
     {
     	Map.Entry<String, String> e = (Map.Entry<String,String>)itr.next();
     	m_preferencias.put(e.getKey(),e.getValue());
     }
     FileOutputStream outputStream = null;
     File fichero;
     try
     {
         fichero = new File(DIRECTORIO_PREFERENCIAS);
         if(fichero.exists() == false)
             if (fichero.mkdirs()==false)
             	{
             	 throw new java.io.IOException ("No se puede crear el directorio de las preferencias.");
             	}
         
         outputStream = new FileOutputStream(DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
         m_preferencias.exportNode(outputStream);
     }
     catch (Exception e)
     {
    	 e.printStackTrace();
     }
     finally
     {
         try
         {
             if(outputStream != null)
                 outputStream.close();
         }
         catch(Exception e)
         {
        	 e.printStackTrace();
         }
     }
 }
 
 public void recargaPreferencias() throws PreferenciasException
 {
 	CargarPreferencias();
 }
 
 private String getValueFromTablaPreferencias(String key)
 {
 	String toReturn="";
 	
 	if(tablaPreferencias.containsKey(key))
 	{
 		toReturn = tablaPreferencias.get(key);
 	}
 	
 	//Logger.debug("Se ha pedido la preferencia '"+key+"' a lo que el sistema devuelve '"+toReturn+"'");
 	
 	return toReturn;
 }
 
 private void setValueIntoTablaPreferencias(String key, String value)
 {
 	//Logger.debug("Se actualizara el valor de la preferencia '"+key+"' a '"+value+"'");
 	tablaPreferencias.put(key, value);
 }
	
	// Este m�todo devolver� la instancia de clase.
 public static Preferencias getPreferencias () throws PreferenciasException
 {
 	if (_pref==null)
 	{
 		throw new PreferenciasException("No se han podido recuperar las preferencias.");
 	}
 	_pref.recargaPreferencias();
 	return _pref;
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
	public String getModoLog() {
		return getValueFromTablaPreferencias(KEY_PREF_DEBUG_SOAP);
	}
	public void setDebugSoap(String debug) {
		setValueIntoTablaPreferencias(KEY_PREF_DEBUG_SOAP, debug);
	}
	
	public String getAppLogDir() {
		return getValueFromTablaPreferencias(KEY_PREF_APP_LOG_DIR);
	}
	
	public String getAppLogFile() {
		return getValueFromTablaPreferencias(KEY_PREF_APP_LOG_FILE);
	}
	
	public String getServerLogHandlerDir() {
		return getValueFromTablaPreferencias(KEY_PREF_SERVER_LOG_HANDLER_DIR);
	}

	public String getServerLogHandlerFile() {
		return getValueFromTablaPreferencias(KEY_PREF_SERVER_LOG_HANDLER_FILE);
	}
	
}