package es.tributasenasturias.servicios.RecepcionDocumentos.preferencias;
// Este paquete se comporta como singleton en acceso, es decir,
//sólo admite una instancia de la clase.
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

import es.tributasenasturias.excepciones.PreferenciasException;

//
public class Preferencias 
{
	//Se hace instancia privada y estática.
	static private Preferencias _pref = new Preferencias();
	private Preferences m_preferencias;
	private final static String FICHERO_PREFERENCIAS = "prefRecepcionDocumentos.xml";
    private final static String DIRECTORIO_PREFERENCIAS = "proyectos/WSRecepcionDocumentacion";
    
    private Map<String, String> tablaPreferencias = new HashMap<String, String>();
	
	//nombres de las preferencias
	private final static String KEY_PREF_ESQUEMA = "EsquemaBaseDatos";
	private final static String KEY_PREF_ENDPOINT_LANZADOR = "EndPointLanzador";
	private final static String KEY_PREF_LOG = "ModoLog";
	// Firma
	private final static String KEY_PREF_ENDPOINT_FIRMA = "EndPointFirma";
	private final static String KEY_PREF_FIRMAR_XML = "FirmarSalida";
	private final static String KEY_PREF_VALIDAR_XML = "ValidarFirma";
	//Validación de certificado
	private final static String KEY_PREF_PROCPERMISO_SERVICIO = "PAPermisoServicio";
	private final static String KEY_PREF_ALIAS_FIRMA = "AliasCertificadoFirma";
	private final static String KEY_PREF_VALIDAR_CERTIFICADO = "ValidarCertificado";
	private final static String KEY_PREF_PROCINSERTA_ESCRITURA = "PAInsertarEscritura";
    private final static String KEY_PREF_ENDPOINT_AUTENTICACION_PA = "EndPointAutenticacionPA";
	private final static String KEY_PREF_XML_AUTORIZACION = "XmlAutorizacion";
	private final static String KEY_PREF_IP_AUTORIZACION = "IpAutorizacion";
	private final static String KEY_PREF_PERMISO_SERVICIO = "CodPermisoServicio";

	
	private Preferencias() 
	{		
		try
		{
			cargarPreferencias();
		}
		catch (Exception e)
		{
			//Para comprobar posteriormente si se ha creado bien, se comprobará que la 
			//variable privada no es estática.
		}
	}
	protected synchronized void cargarPreferencias() throws PreferenciasException
    {
		if(CompruebaFicheroPreferencias())
		{		       
	        FileInputStream inputStream;
			try {
				inputStream = new FileInputStream(DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
				Preferences.systemNodeForPackage(this.getClass()).importPreferences(inputStream);
		        inputStream.close();
			
	        
	        //Logger.debug("Fichero importado");
	
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
	        	//Logger.debug("Cargando en la tabla ['"+keys[i]+"' - '"+value+"']");
	        	
	        	tablaPreferencias.put(keys[i], value);
	        }
			} catch (FileNotFoundException e) {
				throw new PreferenciasException ("Fichero de preferencias no encontrado:"+ e.getMessage());
			} catch (IOException e) {
				throw new PreferenciasException ("Error de entrada/salida:"+e.getMessage());
			} catch (InvalidPreferencesFormatException e) {
				throw new PreferenciasException ("Error en el formato del fichero de preferencias:"+e.getMessage());
			} catch (BackingStoreException e) {
				throw new PreferenciasException ("Error en el fichero de preferencias:"+ e.getMessage());

			}
		}
    }
	
	private void InicializaTablaPreferencias()
	{
		
		tablaPreferencias.clear();
		
		tablaPreferencias.put(KEY_PREF_ESQUEMA,								"EXPLOTACION");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_LANZADOR,					"http://bus:7101/WSInternos/ProxyServices/PXLanzadorMasivo");
		tablaPreferencias.put(KEY_PREF_LOG,							  		"INFO");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_FIRMA,						"http://bus:7101/WSInternos/ProxyServices/PXFirmaDigital");
		tablaPreferencias.put(KEY_PREF_PROCPERMISO_SERVICIO,				"INTERNET.permisoServicio");
		tablaPreferencias.put(KEY_PREF_ALIAS_FIRMA,							"Tributas");
		tablaPreferencias.put(KEY_PREF_FIRMAR_XML,						    "S");
		tablaPreferencias.put(KEY_PREF_VALIDAR_XML,						    "S");
		tablaPreferencias.put(KEY_PREF_VALIDAR_CERTIFICADO,					"S");
		tablaPreferencias.put(KEY_PREF_PROCINSERTA_ESCRITURA,				"Recepcion_documentos.recibirEscritura");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_AUTENTICACION_PA,			"http://bus:7101/WSAutenticacionPA/ProxyServices/PXAutenticacionPA");
		tablaPreferencias.put(KEY_PREF_XML_AUTORIZACION,					"proyectos/ConsultaCertificados/PeticionAutorizacion.xml");
		tablaPreferencias.put(KEY_PREF_IP_AUTORIZACION,						"10.112.10.35");
		tablaPreferencias.put(KEY_PREF_PERMISO_SERVICIO,					"RECDOCUM");

		
		
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
    private synchronized void CrearFicheroPreferencias()
    {
        //preferencias por defecto
        m_preferencias = Preferences.systemNodeForPackage(this.getClass());
        
        InicializaTablaPreferencias();
        
        //recorremos la tabla cargada con las preferencias por defecto
        Iterator itr = tablaPreferencias.entrySet().iterator();
        while(itr.hasNext())
        {
        	Map.Entry<String, String> e = (Map.Entry<String, String>)itr.next();
        	//Logger.debug("Cargando en fichero preferencias ['"+e.getKey()+"' - '"+e.getValue()+"']");
        	
        	m_preferencias.put(e.getKey(),e.getValue());
        }

        FileOutputStream outputStream = null;
        File fichero;
        try
        {
            fichero = new File(DIRECTORIO_PREFERENCIAS);
            if(fichero.exists() == false)
            {
                if (fichero.mkdirs()==false)
                	{
                	 throw new java.io.IOException ("No se puede crear el directorio de las preferencias.");
                	}
            }
            outputStream = new FileOutputStream(DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
            m_preferencias.exportNode(outputStream);
        }
        catch (Exception e)
        {

        }
        finally
        {
            try
            {
                if(outputStream != null)
                {
                    outputStream.close();
                }
            }
            catch(Exception e)
            {
            }
        }
        
    }
    
    public void recargaPreferencias() throws Exception
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
    	
    	//Logger.debug("Se ha pedido la preferencia '"+key+"' a lo que el sistema devuelve '"+toReturn+"'");
    	
    	return toReturn;
    }
    
    private synchronized void setValueIntoTablaPreferencias(String key, String value)
    {
    	//Logger.debug("Se actualizara el valor de la preferencia '"+key+"' a '"+value+"'");
    	tablaPreferencias.put(key, value);
    }
	
	// Este método devolverá la instancia de clase.
    public synchronized static Preferencias getPreferencias () throws PreferenciasException
    {
    	if (_pref==null)
    	{
    		throw new PreferenciasException("No se han podido recuperar las preferencias.");
    	}
  		_pref.cargarPreferencias();
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
	public void setEndPointAutenticacion(String endpointAutenticacion) {
		setValueIntoTablaPreferencias(KEY_PREF_ENDPOINT_FIRMA, endpointAutenticacion);
	}
	public String getPAPermisoServicio() {
		return getValueFromTablaPreferencias(KEY_PREF_PROCPERMISO_SERVICIO);
	}
	public void setPAPermisoServicio(String pAPermisoServicio) {
		setValueIntoTablaPreferencias(KEY_PREF_PROCPERMISO_SERVICIO, pAPermisoServicio);
	}
	public String getAliasCertificadoFirma() {
		return getValueFromTablaPreferencias(KEY_PREF_ALIAS_FIRMA);
	}
	public String getFirmarSalida() {
		return getValueFromTablaPreferencias(KEY_PREF_FIRMAR_XML);
	}
	public String getValidarFirma() {
		return getValueFromTablaPreferencias(KEY_PREF_VALIDAR_XML);
	}
	public String getValidarCertificado() {
		return getValueFromTablaPreferencias(KEY_PREF_VALIDAR_CERTIFICADO);
	}
	public String getPAInsertarEscritura() {
		return getValueFromTablaPreferencias(KEY_PREF_PROCINSERTA_ESCRITURA);
	}
	public String getEndPointAutenticacion() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_AUTENTICACION_PA);
	}
    
    public String getXmlAutorizacion() {
    	return getValueFromTablaPreferencias(KEY_PREF_XML_AUTORIZACION);
	}

	public String getIpAutorizacion() {
		return getValueFromTablaPreferencias(KEY_PREF_IP_AUTORIZACION);
	}
	public String getCodigoPermisoServicio() {
    	return getValueFromTablaPreferencias(KEY_PREF_PERMISO_SERVICIO);
	}
}
