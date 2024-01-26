package es.tributasenasturias.traslado.preferencias;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.prefs.Preferences;


public class Preferencias 
{
	private Preferences m_preferencias;
	private final static String DIRECTORIO_SERVICIO = "proyectos/WSTrasladoMaquina";
	private final static String FICHERO_PREFERENCIAS = "prefsTrasladoMaquina.xml";
	private final static String DIRECTORIO_PREFERENCIAS = DIRECTORIO_SERVICIO;
 
	private HashMap<String, String> tablaPreferencias = new HashMap<String, String>();
	
	//nombres de las preferencias
	private final static String KEY_PREF_LOG = "ModoLog";
	private final static String KEY_PREF_ENDPOINT_AUTENTICACION = "EndpointAutenticacion";
	private final static String KEY_PREF_ENDPOINT_LANZADOR = "EndpointLanzador";
	private final static String KEY_PREF_ESQUEMA = "EsquemaBD";
	private final static String KEY_PREF_PROCPERMISO_SERVICIO = "ProcAlmacenadoPermisosServicio";
	private final static String KEY_PREF_PROCEMISION = "ProcAlmacenadoEmisionTraslado";
	private final static String KEY_PREF_LOG_APLICACION = "ficheroLogAplicacion";
	private final static String KEY_PREF_LOG_CLIENT = "ficheroLogClient";
	private final static String KEY_PREF_LOG_SERVER = "ficheroLogServer";
	private final static String KEY_PREF_MENSAJES = "ficheroMensajes";
	private final static String KEY_PREF_FIRMAR_SALIDA = "firmarMensajeSalida";
	private final static String KEY_PREF_VALIDAR_FIRMA = "validarFirmaMensajeEntrada";
	private final static String KEY_PREF_ALIAS_FIRMA = "aliasCertificadoFirma";
	private final static String KEY_PREF_NODO_FIRMAR = "idNodoFirmar";
	private final static String KEY_PREF_NODO_PADRE_FIRMA = "nodoPadreFirmaConsulta";
	private final static String KEY_PREF_NAMESPACE_NODO_PADRE = "namespaceNodoPadreFirma";
	private final static String KEY_PREF_ENDPOINT_FIRMA = "EndpointFirma";
	private final static String KEY_PREF_VALIDA_CERTIFICADO = "validarCertificado";
	private final static String KEY_PREF_VALIDA_PERMISOS = "validarPermisos";
	private final static String KEY_PREF_ALIAS_SERVICIO = "aliasServicio";
	private final static String KEY_PREF_ENDPOINT_IMPRESION = "EndpointImpresion";
	private final static String KEY_PREF_ENDPOINT_DOCUMENTOS = "EndpointAltaConsultaDoc";
	
	
	public Preferencias() throws PreferenciasException 
	{		
		cargarPreferencias();
	}
	public void cargarPreferencias() throws PreferenciasException
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
		        String msgKeys ="Leyendo las siguientes claves -> ";
		        for(int i=0;i<keys.length;i++)
		        {
		        	msgKeys += "["+keys[i]+"] ";
		        }
		        
		        for(int i=0;i<keys.length;i++)
		        {
		        	String value = m_preferencias.get(keys[i], "");
		        	tablaPreferencias.put(keys[i], value);
		        }
			}
		}
		catch (Exception ex)
		{
			throw new PreferenciasException ("Error al cargar las preferencias: " +ex.getMessage(),ex);
		}
		
 }
	
	private synchronized void InicializaTablaPreferencias()
	{
		
		tablaPreferencias.clear();
		
		tablaPreferencias.put(KEY_PREF_LOG,										"INFO");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_AUTENTICACION,				    "http://bus:7101/WSAutenticacionEPST/ProxyServices/PXAutenticacionEPST");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_LANZADOR,						"http://bus:7101/WSInternos/ProxyServices/PXLanzador");
		tablaPreferencias.put(KEY_PREF_ESQUEMA,									"EXPLOTACION");
		tablaPreferencias.put(KEY_PREF_ALIAS_SERVICIO,							"TRAS_MAQUI");
		tablaPreferencias.put(KEY_PREF_PROCPERMISO_SERVICIO,					"INTERNET.permisoServicio");
		tablaPreferencias.put(KEY_PREF_LOG_APLICACION,						    DIRECTORIO_SERVICIO+"/logs/Application.log");
		tablaPreferencias.put(KEY_PREF_LOG_CLIENT,						     	DIRECTORIO_SERVICIO+ "/logs/Soap_Client.log");
		tablaPreferencias.put(KEY_PREF_LOG_SERVER ,								DIRECTORIO_SERVICIO+"/logs/Soap_Server.log");
		tablaPreferencias.put(KEY_PREF_MENSAJES,							    DIRECTORIO_SERVICIO+"/mensajes.xml");
		tablaPreferencias.put(KEY_PREF_FIRMAR_SALIDA ,							"S");
		tablaPreferencias.put(KEY_PREF_VALIDAR_FIRMA ,							"S");
		tablaPreferencias.put(KEY_PREF_ALIAS_FIRMA ,							"Tributas");
		tablaPreferencias.put(KEY_PREF_NODO_FIRMAR ,							"Body");
		tablaPreferencias.put(KEY_PREF_NODO_PADRE_FIRMA ,						"Header");
		tablaPreferencias.put(KEY_PREF_NAMESPACE_NODO_PADRE ,					"http://schemas.xmlsoap.org/soap/envelope/");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_FIRMA ,							"http://bus:7101/WSInternos/ProxyServices/PXFirmaDigital");
		tablaPreferencias.put(KEY_PREF_VALIDA_CERTIFICADO ,						"S");
		tablaPreferencias.put(KEY_PREF_VALIDA_PERMISOS ,						"S");
		tablaPreferencias.put(KEY_PREF_PROCEMISION,								"JUEGO_TRASLADOS.traslado_telematico_maquina");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_IMPRESION,					    "http://bus:7101/WSInternos/ProxyServices/PXDocumentos");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_DOCUMENTOS,					    "http://bus:7101/WSInternos/ProxyServices/PXConsultaDoinDocumentos");
		
	}
	private synchronized boolean CompruebaFicheroPreferencias() throws PreferenciasException
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
 private synchronized void CrearFicheroPreferencias() throws PreferenciasException
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
    	throw new PreferenciasException("Error al crear el fichero de preferencias:" + e.getMessage(),e);
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
        	 throw new PreferenciasException ("Error al cerrar el flujo del fichero de preferencias:" + e.getMessage(),e);
         }
     }
 }
 
 public void recargaPreferencias() throws PreferenciasException
 {
 	cargarPreferencias();
 }
 
 private String getValueFromTablaPreferencias(String key)
 {
 	String toReturn="";
 	
 	if(tablaPreferencias.containsKey(key))
 	{
 		toReturn = tablaPreferencias.get(key);
 	}
 	
 	return toReturn;
 }
 
 
	public String getEsquemaBD() {
		return getValueFromTablaPreferencias(KEY_PREF_ESQUEMA);
	}
    public String getEndpointLanzador() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_LANZADOR);
	}
	public String getModoLog() {
		return getValueFromTablaPreferencias(KEY_PREF_LOG);
	}
	public String getEndpointAutenticacion() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_AUTENTICACION);
	}
	public String getAliasServicio() {
		return getValueFromTablaPreferencias(KEY_PREF_ALIAS_SERVICIO);
	}
	public String getProcAlmacenadoPermisosServicio() {
		return getValueFromTablaPreferencias(KEY_PREF_PROCPERMISO_SERVICIO);
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
	
	public String getFirmarSalida() {
		return getValueFromTablaPreferencias(KEY_PREF_FIRMAR_SALIDA);
	}
	public String getValidarFirma() {
		return getValueFromTablaPreferencias(KEY_PREF_VALIDAR_FIRMA);
	}
	public String getAliasFirma() {
		return getValueFromTablaPreferencias(KEY_PREF_ALIAS_FIRMA);
	}
	public String getNodoFirmar() {
		return getValueFromTablaPreferencias(KEY_PREF_NODO_FIRMAR);
	}
	public String getNodoPadreFirma() {
		return getValueFromTablaPreferencias(KEY_PREF_NODO_PADRE_FIRMA);
	}
	public String getNamespaceNodoPadre() {
		return getValueFromTablaPreferencias(KEY_PREF_NAMESPACE_NODO_PADRE);
	}
	public String getFicheroMensajes()  {
		return getValueFromTablaPreferencias(KEY_PREF_MENSAJES);
	}
	public String getEndpointFirma() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_FIRMA);
	}
	public String getValidarCertificado() {
		return getValueFromTablaPreferencias(KEY_PREF_VALIDA_CERTIFICADO);
	}
	public String getValidarPermisos() {
		return getValueFromTablaPreferencias(KEY_PREF_VALIDA_PERMISOS);
	}
	public String getProcAlmacenadoEmisionTraslado() {
		return getValueFromTablaPreferencias(KEY_PREF_PROCEMISION);
	}
	public String getEndpointImpresion() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_IMPRESION);
	}
	public String getEndpointDocumentos() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_DOCUMENTOS);
	}
}
