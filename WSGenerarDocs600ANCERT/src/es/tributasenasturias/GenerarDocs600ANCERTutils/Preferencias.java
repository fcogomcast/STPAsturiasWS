package es.tributasenasturias.GenerarDocs600ANCERTutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.prefs.Preferences;

public final class Preferencias {

	private Preferences m_preferencias;
	private String m_debug;
	private String m_entorno;
	private String m_procalmacenado_integra;
	private String m_procalmacenado_altaexpte;
	private String m_procalmacenado_altadoc;
	private String m_endpoint_altaescritura;
	private String m_endpoint_lanzador;
	private String m_endpoint_pago;
	private String m_endpoint_modeloPDF;
	
	//Variables relacionads con la firma.
	private String m_endpoint_firma;	
	private String m_certificado_firma;
	private String m_clave_firma;

	private String m_endpoint_autenticacion_pa;	
	private String m_xml_autorizacion;
	private String m_ip_autorizacion;
	private String m_firma_digital;
	private String m_valida_firma;
	
    //constantes para trabajar con las preferencias
    private final String FICHERO_PREFERENCIAS = "prefsGenerarDocsANCERT.xml";
    private final String DIRECTORIO_PREFERENCIAS = "proyectos//WSGenerarDocs600ANCERT";
    private final String NOMBRE_PREF_DEBUG = "Debug";
    private final String NOMBRE_PREF_ENTORNO = "Entorno";
    private final String NOMBRE_PREF_PROCALMACENADO_ALTADOC = "pAAltaDocumento";
    private final String NOMBRE_PREF_ENDPOINT_LANZADOR = "EndPointLanzador";
    private final String NOMBRE_PREF_ENDPOINT_MODELOPDF = "EndPointModeloPDF";
    
	//Escribe aqui el valor inicial del debug (0 = no existe debug, 1 = existe debug)
    private final String VALOR_INICIAL_PREF_DEBUG = "1";
    private final String VALOR_INICIAL_PREF_ENTORNO = "PSEUDOREAL";
    private final String VALOR_INICIAL_PREF_PROCALMACENADO_ALTADOC = "INTERNET_DOCUMENTOSV2.AltaDocumento";
    private final String VALOR_INICIAL_PREF_ENDPOINT_LANZADOR = "http://bus:7001/WSInternos/ProxyServices/PXLanzadorMasivo";
    private final String VALOR_INICIAL_PREF_ENDPOINT_MODELOPDF = "http://bus:7001/WSANCERT/ProxyServices/PXModelosPDF";
        
                
	public Preferencias() {
		//CopiaFicherosImpresos();
	};
	
    public void CompruebaFicheroPreferencias()
    {
    	
        File f = new File(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
        if (f.exists() == false)
        {
            CrearFicheroPreferencias();
        }
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
        m_preferencias.put(NOMBRE_PREF_DEBUG, VALOR_INICIAL_PREF_DEBUG);
        m_preferencias.put(NOMBRE_PREF_ENTORNO, VALOR_INICIAL_PREF_ENTORNO);
        m_preferencias.put(NOMBRE_PREF_PROCALMACENADO_ALTADOC, VALOR_INICIAL_PREF_PROCALMACENADO_ALTADOC);
        m_preferencias.put(NOMBRE_PREF_ENDPOINT_LANZADOR, VALOR_INICIAL_PREF_ENDPOINT_LANZADOR);
        m_preferencias.put(NOMBRE_PREF_ENDPOINT_MODELOPDF, VALOR_INICIAL_PREF_ENDPOINT_MODELOPDF);
        

        FileOutputStream outputStream = null;
        File fichero;
        try
        {
            fichero = new File(DIRECTORIO_PREFERENCIAS);
            if(fichero.exists() == false)
                fichero.mkdirs();
            
            outputStream = new FileOutputStream(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
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
                System.out.println("Error cerrando fichero de preferencias -> " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    

 // Obtencion de las preferencias que especificaran el almacen y su contraseña
    public void CargarPreferencias() throws Exception
    {
        File f = new File(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
        if (f.exists())
        {
            //si existe el fichero de preferencias lo cargamos
            FileInputStream inputStream = new FileInputStream(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
            Preferences.importPreferences(inputStream);
            inputStream.close();

            m_preferencias = Preferences.systemNodeForPackage(this.getClass());

            //obtenemos las preferencias
            m_debug = m_preferencias.get(NOMBRE_PREF_DEBUG, "");
            m_entorno = m_preferencias.get(NOMBRE_PREF_ENTORNO, "");
            m_procalmacenado_altadoc = m_preferencias.get(NOMBRE_PREF_PROCALMACENADO_ALTADOC, "");
            m_endpoint_lanzador = m_preferencias.get(NOMBRE_PREF_ENDPOINT_LANZADOR, "");
            m_endpoint_modeloPDF = m_preferencias.get(NOMBRE_PREF_ENDPOINT_MODELOPDF, "");
            
            			
            
        }
        else
        {
            //si no existe el fichero de preferencias lo crearemos
            CrearFicheroPreferencias();

            throw new Exception("Debe especificar primero las preferencias en el fichero: " + f.getAbsolutePath() + " (parar el servicio)");
        }
    }	
	
    public String getDebug () {
    	return this.m_debug;
    };
    
    public String getEntorno () {
    	return this.m_entorno;
    };
    public String getPAAltaDocumento () {
    	return this.m_procalmacenado_altadoc;
    };
    
           
    public String getEndpointLanzador () {
    	return this.m_endpoint_lanzador;
    };
    
    public String getEndpointModeloPDF () {
    	return this.m_endpoint_modeloPDF;
    };
    
	
	
	public String getFirmaDigital() {
		return this.m_firma_digital;
	};    
    
	public String getValidaFirma() {
		return this.m_valida_firma;
	};
	
}
