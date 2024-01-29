package es.tributasenasturias.webservices.Certificados.utils.Preferencias;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.prefs.Preferences;

import es.tributasenasturias.webservices.Certificados.Exceptions.PreferenciasException;
public class Preferencias 
{
	static private Preferencias _pref = new Preferencias();
	private Preferences m_preferencias;
	private final static String FICHERO_PREFERENCIAS = "preferenciasConsultaCertificados.xml";
 private final static String DIRECTORIO_PREFERENCIAS = "proyectos/ConsultaCertificados";
 
 private HashMap<String, String> tablaPreferencias = new HashMap<String, String>();
	
	//nombres de las preferencias
	private final static String KEY_PREF_ESQUEMA = "EsquemaBaseDatos";
	private final static String KEY_PREF_LOG = "ModoLog";
	private final static String KEY_PREF_ENDPOINT_LANZADOR = "EndPointLanzador";
	private final static String KEY_PREF_ENDPOINT_FIRMA = "EndPointFirma";
	private final static String KEY_PREF_CERTIFICADO_FIRMA = "CertificadoFirma";
	private final static String KEY_PREF_ENDPOINT_AUTENTICACION_PA = "EndPointAutenticacionPA";
	private final static String KEY_PREF_XML_AUTORIZACION = "XmlAutorizacion";
	private final static String KEY_PREF_IP_AUTORIZACION = "IpAutorizacion";
	private final static String KEY_PREF_PA_CONSULTA="pAConsultaCertificado";
	private final static String KEY_PREF_PA_PERMISOS="pAPermisosServicio";
	private final static String KEY_PREF_ALIAS_PERMISO="aliasPermiso";
	private final static String KEY_PREF_VALIDA_FIRMA="validarFirmaEntrada";
	private final static String KEY_PREF_VALIDA_CERTIFICADO="validarCertificadoEntrada";
	private final static String KEY_PREF_FIRMA_SALIDA="firmarMensajeSalida";
	
	
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
	protected synchronized void CargarPreferencias() throws PreferenciasException
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
		        //Logger.debug(msgKeys);
		        
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
		
		tablaPreferencias.put(KEY_PREF_ESQUEMA,								"EXPLOTACION");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_LANZADOR,					"http://bus:7101/WSInternos/ProxyServices/PXLanzador");
		tablaPreferencias.put(KEY_PREF_LOG,									"ALL");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_FIRMA,						"http://bus:7101/WSInternos/ProxyServices/PXFirmaDigital");
		tablaPreferencias.put(KEY_PREF_CERTIFICADO_FIRMA,					"");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_AUTENTICACION_PA,			"http://bus:7101/WSAutenticacionPA/ProxyServices/PXAutenticacionPA_WS");
		tablaPreferencias.put(KEY_PREF_XML_AUTORIZACION,					"ConsultaCertificados/PeticionAutorizacion.xml");
		tablaPreferencias.put(KEY_PREF_IP_AUTORIZACION,						"10.112.10.35");
		tablaPreferencias.put(KEY_PREF_PA_CONSULTA,							"INTERNET_CERTIFICADOS.SW_consultaCertificado");
		tablaPreferencias.put(KEY_PREF_PA_PERMISOS,							"INTERNET.permisoServicio");
		tablaPreferencias.put(KEY_PREF_ALIAS_PERMISO,						"CERTIFDEUD");
		tablaPreferencias.put(KEY_PREF_VALIDA_FIRMA,						"S");
		tablaPreferencias.put(KEY_PREF_VALIDA_CERTIFICADO,					"S");
		tablaPreferencias.put(KEY_PREF_FIRMA_SALIDA,						"S");
		
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
     	//Logger.debug("Cargando en fichero preferencias ['"+e.getKey()+"' - '"+e.getValue()+"']");
     	
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
	
	// Este método devolverá la instancia de clase.
 public static Preferencias getPreferencias () throws PreferenciasException
 {
 	if (_pref==null)
 	{
 		throw new PreferenciasException("No se han podido recuperar las preferencias.");
 	}
 	_pref.recargaPreferencias();
 	return _pref;
 }
	public String getEsquemaBaseDatos() {
		return getValueFromTablaPreferencias(KEY_PREF_ESQUEMA);
	}
	public void setEsquemaBaseDatos(String esquema) {
		setValueIntoTablaPreferencias(KEY_PREF_ESQUEMA, esquema);
	}
	public String getEndPointLanzador() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_LANZADOR);
	}
	public void setEndPointLanzador(String endPointLanzador) {
		setValueIntoTablaPreferencias(KEY_PREF_ENDPOINT_LANZADOR, endPointLanzador);
	}
	public String getModoLog() {
		return getValueFromTablaPreferencias(KEY_PREF_LOG);
	}
	public void setModoLog(String modo) {
		setValueIntoTablaPreferencias(KEY_PREF_LOG, modo);
	}
	public String getEndPointFirma() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_FIRMA);
	}
	public void setEndPointFirma(String endpointFirma) {
		setValueIntoTablaPreferencias(KEY_PREF_ENDPOINT_FIRMA, endpointFirma);
	}
	public String getCertificadoFirma() {
		return getValueFromTablaPreferencias(KEY_PREF_CERTIFICADO_FIRMA);
	}
	public void setCertificadoFirma(String certificadoFirma) {
		setValueIntoTablaPreferencias(KEY_PREF_CERTIFICADO_FIRMA, certificadoFirma);
	}
	public String getEndPointAutenticacion() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_AUTENTICACION_PA);
	}
	public void setEndPointAutenticacion(String endpointAutenticacion) {
		setValueIntoTablaPreferencias(KEY_PREF_ENDPOINT_FIRMA, endpointAutenticacion);
	}
	public String getXmlAutorizacion() {
		return getValueFromTablaPreferencias(KEY_PREF_XML_AUTORIZACION);
	}
	public void setXmlAutorizacion(String xmlAutorizacion) {
		setValueIntoTablaPreferencias(KEY_PREF_XML_AUTORIZACION, xmlAutorizacion);
	}
	public String getIpAutorizacion() {
		return getValueFromTablaPreferencias(KEY_PREF_IP_AUTORIZACION);
	}
	public void setIpAutorizacion(String ipAutorizacion) {
		setValueIntoTablaPreferencias(KEY_PREF_IP_AUTORIZACION, ipAutorizacion);
	}
	public String getPaConsulta() {
		return getValueFromTablaPreferencias(KEY_PREF_PA_CONSULTA);
	}
	public String getValidaFirma() {
		return getValueFromTablaPreferencias(KEY_PREF_VALIDA_FIRMA);
	}
	public String getValidaCertificado() {
		return getValueFromTablaPreferencias(KEY_PREF_VALIDA_CERTIFICADO);
	}
	public String getFirmarSalida() {
		return getValueFromTablaPreferencias(KEY_PREF_FIRMA_SALIDA);
	}
	public String getPaPermisos() {
		return getValueFromTablaPreferencias(KEY_PREF_PA_PERMISOS);
	}
	public String getAliasServicio() {
		return getValueFromTablaPreferencias(KEY_PREF_ALIAS_PERMISO);
	}
}
