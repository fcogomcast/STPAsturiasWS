package es.tributasenasturias.utilsProgramaAyuda;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.prefs.Preferences;

public final class Preferencias {

	private Preferences m_preferencias;
	private String m_debug;
	private String m_entorno;
	private String m_procalmacenado_integra;
	private String m_endpoint_lanzador;
	private String m_endpoint_cifrado;
	private String m_password_cifrado;

	private String m_procalmacenado_altadoc;
	
    //constantes para trabajar con las preferencias
	
	private final String FICHERO_PREFERENCIAS = "prefsProgramaAyuda.xml";
    private final String DIRECTORIO_PREFERENCIAS = "proyectos//wsProgramaAyuda";
    private final String NOMBRE_PREF_DEBUG = "Debug";
    private final String NOMBRE_PREF_ENTORNO = "Entorno";
    private final String NOMBRE_PREF_PROCALMACENADO_INTEGRA = "pAIntegra";
    private final String NOMBRE_PREF_ENDPOINT_LANZADOR = "EndPointLanzador";
    private final String NOMBRE_PREF_ENDPOINT_CIFRADO = "EndPointCifrado";
    private final String NOMBRE_PREF_PASSWORD_CIFRADO = "PassrowsCifrado";
    private final String NOMBRE_PREF_PROCALMACENADO_ALTADOC = "pAAltaDocumento";
    
    
    //Escribe aqui el valor inicial del debug (0 = no existe debug, 1 = existe debug)
    private final String VALOR_INICIAL_PREF_DEBUG = "1";
    private final String VALOR_INICIAL_PREF_ENTORNO = "PSEUDOREAL";
    private final String VALOR_INICIAL_PREF_PROCALMACENADO_INTEGRA = "PROGRAMAS_AYUDA3.insertaClob";
    private final String VALOR_INICIAL_PREF_ENDPOINT_LANZADOR = "http://bus.desa.epst.pa:7001/WSInternos/ProxyServices/PXLanzador";
    private final String VALOR_INICIAL_PREF_ENDPOINT_CIFRADO = "http://bus.desa.epst.pa:7001/WSCifradoPAyuda/ProxyServices/PXCifrado_PA";
    private final String VALOR_INICIAL_PREF_PASSWORD_CIFRADO = "abcdfrgttttyjkxx";
    	
    private final String VALOR_INICIAL_PREF_PROCALMACENADO_ALTADOC = "INTERNET_DOCUMENTOS.AltaDocumento";
    
	public Preferencias() {};
	
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
        m_preferencias.put(NOMBRE_PREF_PROCALMACENADO_INTEGRA, VALOR_INICIAL_PREF_PROCALMACENADO_INTEGRA);
        m_preferencias.put(NOMBRE_PREF_ENDPOINT_LANZADOR, VALOR_INICIAL_PREF_ENDPOINT_LANZADOR);
        m_preferencias.put(NOMBRE_PREF_ENDPOINT_CIFRADO, VALOR_INICIAL_PREF_ENDPOINT_CIFRADO);
        m_preferencias.put(NOMBRE_PREF_PASSWORD_CIFRADO, VALOR_INICIAL_PREF_PASSWORD_CIFRADO);

        m_preferencias.put(NOMBRE_PREF_PROCALMACENADO_ALTADOC, VALOR_INICIAL_PREF_PROCALMACENADO_ALTADOC);
        
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
            m_procalmacenado_integra = m_preferencias.get(NOMBRE_PREF_PROCALMACENADO_INTEGRA, "");
            m_endpoint_lanzador = m_preferencias.get(NOMBRE_PREF_ENDPOINT_LANZADOR, "");
            m_endpoint_cifrado = m_preferencias.get(NOMBRE_PREF_ENDPOINT_CIFRADO, "");
            m_password_cifrado = m_preferencias.get(NOMBRE_PREF_PASSWORD_CIFRADO, "");
            m_procalmacenado_altadoc = m_preferencias.get(NOMBRE_PREF_PROCALMACENADO_ALTADOC, "");
            
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
    public String getPAIntegra () {
    	return this.m_procalmacenado_integra;
    };

    public String getEndpointLanzador () {
    	return this.m_endpoint_lanzador;
    };
    public String getEndpointCifrado () {
    	return this.m_endpoint_cifrado;
    };
    
    public String getPasswordCifrado () {
    	return this.m_password_cifrado;
    };

    public String getPAAltaDocumento () {
    	return this.m_procalmacenado_altadoc;
    };
}
