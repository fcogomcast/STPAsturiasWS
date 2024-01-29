package es.tributasenasturias.services.ws.archivodigital.utils.Preferencias;

//Este paquete se comporta como singleton en acceso, es decir,
//sólo admite una instancia de la clase.
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


public class Preferencias
{
	private Preferences m_preferencias;
	private final String FICHERO_PREFERENCIAS = "preferenciasArchivoDigital.xml";
	private final String DIRECTORIO_PREFERENCIAS = "proyectos/ArchivoDigital";
 
 private HashMap<String, String> tablaPreferencias = new HashMap<String, String>();
	
	//nombres de las preferencias
	private final String KEY_PREF_PROVIDER_URL = "PROVIDER_URL";
	private final String KEY_PREF_DATASOURCE = "DATASOURCE";
	private final String KEY_PREF_DEBUG_SOAP = "DEBUG_SOAP";
	private final String KEY_PREF_FIRMANTE= "firmantePorDefecto";
	private final String KEY_PREF_ALGORITMO_HASH_CSV= "algoritmoHashCSV";
	private final String KEY_PREF_ALGORITMO_HASH_ARCHIVO= "algoritmoHashArchivo";
	private final String KEY_PREF_DIR3= "DIR3_STPA";
	private final String KEY_PREF_TEXTO_CSV= "textoLineaCSV";
	private final String KEY_PREF_SERVER_LOG_HANDLER_DIR = "ServerLogHandlerDir";
	private final String KEY_PREF_SERVER_LOG_HANDLER_FILE = "ServerLogHandlerFile";
	private final String KEY_PREF_CLIENT_LOG_HANDLER_DIR = "ClientLogHandlerDir";
	private final String KEY_PREF_CLIENT_LOG_HANDLER_FILE = "ClientLogHandlerFile";
	private final String KEY_PREF_APP_LOG_DIR = "ApplicationLogDir";
	private final String KEY_PREF_APP_LOG_FILE = "ApplicationLogFile";
	private final String KEY_PREF_FIRMAS_ADES="EndpointFirmarAdes";
	private final String KEY_PREF_PADES_ENI="ficheroConfiguracionPades";
	// CRUBENCVS 41435. Los procedimientos almacenados a los que se llaman se guardan en preferencias
	private final String KEY_PREF_PA_GUARDAR_ARCHIVO="pAGuardarArchivo";
	private final String KEY_PREF_PA_OBTENER_ARCHIVO="pAObtenerArchivo";
	private final String KEY_PREF_PA_EXISTE_CSV="pAExisteCSV";
	private final String KEY_PREF_PA_OBTENER_ARCHIVO_POR_CSV="pAObtenerArchivoPorCSV";
	private final String KEY_PREF_PA_OBTENER_ARCHIVO_POR_HASH="pAObtenerArchivoPorHash";
	private final String KEY_PREF_PA_GUARDAR_FIRMA="pAGuardarFirma";
	private final String KEY_PREF_PA_GUARDAR_METADATOS="pAGuardaMetadatos";
	private final String KEY_PREF_PA_VACIAR_FIRMAS="pAVaciarFirmas";
	private final String KEY_PREF_PA_CUSTODIAR_VERSION="pACustodiarVersion";
	//CRUBENCVS 21/08/2023. 48601 Nombre de firmante por defecto, para que no incluya el usuario
	//como nombre de firmante.
	private final String KEY_PREF_NOMBRE_FIRMANTE="nombreFirmanteDefecto";
	
	private Preferencias() 
	{		
		try
		{
			CargarPreferencias();
		}
		catch (Exception e)
		{
			//Para comprobar posteriormente si se ha creado bien, se comprobará que la 
			//variable privada no es estática.
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
		tablaPreferencias.put(KEY_PREF_DEBUG_SOAP,					"INFO");
		tablaPreferencias.put(KEY_PREF_FIRMANTE,					"Q3300312J");
		tablaPreferencias.put(KEY_PREF_ALGORITMO_HASH_CSV,			"SHA-1");
		tablaPreferencias.put(KEY_PREF_ALGORITMO_HASH_ARCHIVO,		"SHA-1");
		tablaPreferencias.put(KEY_PREF_DIR3,						"A03005853");
		tablaPreferencias.put(KEY_PREF_TEXTO_CSV,					"Código Seguro Verificación: #CSV# Autenticidad verificable en www.tributasenasturias.es.");
		tablaPreferencias.put(KEY_PREF_SERVER_LOG_HANDLER_DIR,		DIRECTORIO_PREFERENCIAS);
		tablaPreferencias.put(KEY_PREF_SERVER_LOG_HANDLER_FILE,		"SOAP_SERVER_ArchivoDigital.log");
		tablaPreferencias.put(KEY_PREF_CLIENT_LOG_HANDLER_DIR,		DIRECTORIO_PREFERENCIAS);
		tablaPreferencias.put(KEY_PREF_CLIENT_LOG_HANDLER_FILE,		"SOAP_CLIENT_ArchivoDigital.log");
		tablaPreferencias.put(KEY_PREF_APP_LOG_DIR,					DIRECTORIO_PREFERENCIAS);
		tablaPreferencias.put(KEY_PREF_APP_LOG_FILE,				"Application.log");
		tablaPreferencias.put(KEY_PREF_FIRMAS_ADES,					"http://bus:7101/WSFirmasAdes/ProxyServices/PXFirmasAdes");
		tablaPreferencias.put(KEY_PREF_PADES_ENI,					DIRECTORIO_PREFERENCIAS+"/configuracionPadesENI.prop");
		// CRUBENCVS 41435. Procedimientos almacenados en Preferencias
		tablaPreferencias.put(KEY_PREF_PA_GUARDAR_ARCHIVO,			"archivo_digital.guardararchivo");
		tablaPreferencias.put(KEY_PREF_PA_OBTENER_ARCHIVO,			"archivo_digital.obtenerxmldatosarchivo");
		tablaPreferencias.put(KEY_PREF_PA_EXISTE_CSV,				"archivo_digital.existecsv");
		tablaPreferencias.put(KEY_PREF_PA_OBTENER_ARCHIVO_POR_CSV,	"archivo_digital.obtenerarchivoporcsv");
		tablaPreferencias.put(KEY_PREF_PA_OBTENER_ARCHIVO_POR_HASH,	"archivo_digital.obtenerarchivosporhash");
		tablaPreferencias.put(KEY_PREF_PA_GUARDAR_FIRMA,			"archivo_digital.guardarfirma");
		tablaPreferencias.put(KEY_PREF_PA_GUARDAR_METADATOS,		"archivo_digital.guardametadatos");
		tablaPreferencias.put(KEY_PREF_PA_VACIAR_FIRMAS,			"archivo_digital.vaciarFirmasBiometricas");
		tablaPreferencias.put(KEY_PREF_PA_CUSTODIAR_VERSION,		"archivo_digital.custodiar_version");
		//CRUBENCVS 21/08/2023. 48601 Nombre de firmante por defecto
		tablaPreferencias.put(KEY_PREF_NOMBRE_FIRMANTE,				"SERVICIOS TRIBUTARIOS PRINCIPADO DE ASTURIAS");			
		
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
	

 public static Preferencias getPreferencias () throws PreferenciasException
 {
 	return new Preferencias();
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
	
	public String getFirmantePorDefecto () {
		return getValueFromTablaPreferencias(KEY_PREF_FIRMANTE);
	}
	public String getAlgoritmoHashArchivo () {
		return getValueFromTablaPreferencias(KEY_PREF_ALGORITMO_HASH_ARCHIVO);
	}
	public String getAlgoritmoHashCSV () {
		return getValueFromTablaPreferencias(KEY_PREF_ALGORITMO_HASH_CSV);
	}
	public String getServerLogHandlerFile() {
		return getValueFromTablaPreferencias(KEY_PREF_SERVER_LOG_HANDLER_FILE);
	}
	public String getServerLogHandlerDir() {
		return getValueFromTablaPreferencias(KEY_PREF_SERVER_LOG_HANDLER_DIR);
	}

	public String getAppLogDir() {
		return getValueFromTablaPreferencias(KEY_PREF_APP_LOG_DIR);
	}
	
	public String getAppLogFile() {
		return getValueFromTablaPreferencias(KEY_PREF_APP_LOG_FILE);
	}
	public String getDIR3 () {
		return getValueFromTablaPreferencias(KEY_PREF_DIR3);
	}
	public String getTextoCsv () {
		return getValueFromTablaPreferencias(KEY_PREF_TEXTO_CSV);
	}
	public String getModoLog() {
		return getValueFromTablaPreferencias(KEY_PREF_DEBUG_SOAP);
	}
	
	public String getEndpointFirmasAdes() {
		return getValueFromTablaPreferencias(KEY_PREF_FIRMAS_ADES);
	}
	
	public Properties getPreferenciasPadesENI() throws PreferenciasException{
		Properties p= new Properties();
		String ruta= getValueFromTablaPreferencias(KEY_PREF_PADES_ENI);
		BufferedInputStream bis=null;
		try{
			bis=new BufferedInputStream(new FileInputStream(ruta));
			p.load(bis);
			return p;
		} catch (IOException i){
			throw new PreferenciasException("Error al cargar preferencias de Pades:"+i.getMessage(),i);
		} finally {
			if (bis!=null){
				try { bis.close();} catch (Exception ex) {}
			}
		}
	}

	public String getClientLogHandlerFile() {
		return getValueFromTablaPreferencias(KEY_PREF_CLIENT_LOG_HANDLER_FILE);
	}
	
	public String getClientLogHandlerDir() {
		return getValueFromTablaPreferencias(KEY_PREF_CLIENT_LOG_HANDLER_DIR);
	}
	
	public String getPAGuardarArchivo(){
		return getValueFromTablaPreferencias(KEY_PREF_PA_GUARDAR_ARCHIVO);
	}
	
	public String getPAObtenerArchivo(){
		return getValueFromTablaPreferencias(KEY_PREF_PA_OBTENER_ARCHIVO);
	}
	
	public String getPAExisteCSV(){
		return getValueFromTablaPreferencias(KEY_PREF_PA_EXISTE_CSV);
	}
	
	public String getPAObtenerArchivoCSV(){
		return getValueFromTablaPreferencias(KEY_PREF_PA_OBTENER_ARCHIVO_POR_CSV);
	}
	
	public String getPAObtenerArchivoHash(){
		return getValueFromTablaPreferencias(KEY_PREF_PA_OBTENER_ARCHIVO_POR_HASH);
	}
	
	public String getPAGuardarFirma(){
		return getValueFromTablaPreferencias(KEY_PREF_PA_GUARDAR_FIRMA);
	}
	
	public String getPAGuardarMetadatos(){
		return getValueFromTablaPreferencias(KEY_PREF_PA_GUARDAR_METADATOS);
	}
	
	public String getPAVaciarFirmas(){
		return getValueFromTablaPreferencias(KEY_PREF_PA_VACIAR_FIRMAS);
	}
	
	public String getPACustodiarVersion(){
		return getValueFromTablaPreferencias(KEY_PREF_PA_CUSTODIAR_VERSION);
	}
	
	//CRUBENCVS 21/08/2023  48601 Nombre de firmante por defecto
	public String getNombreFirmanteDefecto(){
		return getValueFromTablaPreferencias(KEY_PREF_NOMBRE_FIRMANTE);
	}

}