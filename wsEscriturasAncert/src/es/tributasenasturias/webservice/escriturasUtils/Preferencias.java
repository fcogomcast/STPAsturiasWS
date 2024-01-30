package es.tributasenasturias.webservice.escriturasUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.prefs.Preferences;

public final class Preferencias {

	private Preferences m_preferencias;
	private String m_debug;
	private String m_entorno;
	private String m_procalmacenado_consulta_autoliq;
	private String m_procalmacenado_inserta_datosEscr;
	private String m_endpoint_lanzador;
	private String m_procalmacenado_inserta_datosEscrOrigen;
	private String m_procalmacenado_inserta_escrPlusvalia;

    //constantes para trabajar con las preferencias
    private final String FICHERO_PREFERENCIAS = "prefsEscriturasAncert.xml";
    private final String DIRECTORIO_PREFERENCIAS = "proyectos//WSEscriturasAncert";
    private final String NOMBRE_PREF_DEBUG = "Debug";
    private final String NOMBRE_PREF_ENTORNO = "Entorno";
    private final String NOMBRE_PREF_PROCALMACENADO_CONSULTA_AUTOLIQ = "pAConsultaDatosEscritura";
    private final String NOMBRE_PREF_PROCALMACENADO_INSERTA_DATOSESCR = "pAInsertaDatosEscritura";
    private final String NOMBRE_PREF_ENDPOINT_LANZADOR = "EndPointLanzador";
    private final String NOMBRE_PREF_PROCALMACENADO_INSERTA_DATOSESCR_ORIGEN = "pAInsertaEscrituraConOrigen";
    private final String NOMBRE_PREF_PROCALMACENADO_INSERTA_PLUSVALIA = "pAInsertaEscrituraPlusvalia";
    
    //Escribe aqui el valor inicial del debug (0 = no existe debug, 1 = existe debug)
    private final String VALOR_INICIAL_PREF_DEBUG = "1";
    private final String VALOR_INICIAL_PREF_ENTORNO = "PSEUDOREAL";
    private final String VALOR_INICIAL_PREF_PROCALMACENADO_CONSULTA_AUTOLIQ = "ANCERT.wsConsultaEscritura";
    private final String VALOR_INICIAL_PREF_PROCALMACENADO_INSERTA_DATOSESCR = "ANCERT.wsInsertaEscrituraDatosEscr";
    private final String VALOR_INICIAL_PREF_ENDPOINT_LANZADOR = "http://bus:7101/WSInternos/ProxyServices/PXLanzador";
    private final String VALOR_INICIAL_PREF_PROCALMACENADO_INSERTA_DATOSESCR_ORIGEN = "ANCERT.wsInsertaEscrituraOrigen";
    private final String VALOR_INICIAL_PREF_PROCALMACENADO_INSERTA_PLUSVALIA = "ANCERT.wsInsertaEscrituraPlusvalias";
    	
	
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
        m_preferencias.put(NOMBRE_PREF_PROCALMACENADO_CONSULTA_AUTOLIQ, VALOR_INICIAL_PREF_PROCALMACENADO_CONSULTA_AUTOLIQ);
        m_preferencias.put(NOMBRE_PREF_PROCALMACENADO_INSERTA_DATOSESCR, VALOR_INICIAL_PREF_PROCALMACENADO_INSERTA_DATOSESCR);
        m_preferencias.put(NOMBRE_PREF_ENDPOINT_LANZADOR, VALOR_INICIAL_PREF_ENDPOINT_LANZADOR);
        m_preferencias.put(NOMBRE_PREF_PROCALMACENADO_INSERTA_DATOSESCR_ORIGEN, VALOR_INICIAL_PREF_PROCALMACENADO_INSERTA_DATOSESCR_ORIGEN);
        m_preferencias.put(NOMBRE_PREF_PROCALMACENADO_INSERTA_PLUSVALIA, VALOR_INICIAL_PREF_PROCALMACENADO_INSERTA_PLUSVALIA);

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
            m_procalmacenado_consulta_autoliq = m_preferencias.get(NOMBRE_PREF_PROCALMACENADO_CONSULTA_AUTOLIQ, "");
            m_procalmacenado_inserta_datosEscr = m_preferencias.get(NOMBRE_PREF_PROCALMACENADO_INSERTA_DATOSESCR, "");
            m_endpoint_lanzador = m_preferencias.get(NOMBRE_PREF_ENDPOINT_LANZADOR, "");
            m_procalmacenado_inserta_datosEscrOrigen = m_preferencias.get(NOMBRE_PREF_PROCALMACENADO_INSERTA_DATOSESCR_ORIGEN, "");
            m_procalmacenado_inserta_escrPlusvalia = m_preferencias.get(NOMBRE_PREF_PROCALMACENADO_INSERTA_PLUSVALIA, "");
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
    public String getPAConsultaEscritura () {
    	return this.m_procalmacenado_consulta_autoliq;
    };
    public String getPAInsertaDatosEscr () {
    	return this.m_procalmacenado_inserta_datosEscr;
    };
    public String getEndpointLanzador () {
    	return this.m_endpoint_lanzador;
    };
    public String getPAInsertaDatosEscrOrigen () {
    	return this.m_procalmacenado_inserta_datosEscrOrigen;
    };
    public String getPAInsertaEscrituraPlusvalia () {
    	return this.m_procalmacenado_inserta_escrPlusvalia;
    };
}
