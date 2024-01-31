package es.tributasenasturias.pdf.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.prefs.Preferences;

public final class Preferencias {

	private Preferences m_preferencias;
	private String m_debug;
	private String m_entorno;
	private String m_rutamodelos;
	private String m_plantillamodelo600;
	private String m_endpoint_lanzador;

	//constantes para trabajar con las preferencias
    private final String FICHERO_PREFERENCIAS = "prefsModelosPDF.xml";
    private final String DIRECTORIO_PREFERENCIAS = "proyectos//WSModelosPDF";
    private final String NOMBRE_PREF_DEBUG = "Debug";
    private final String NOMBRE_PREF_ENTORNO = "Entorno";
    private final String NOMBRE_PREF_RUTAMODELOS = "rutaModelos";
    private final String NOMBRE_PREF_PLANTILLAMODELO600 = "plantillaModelo600";
    private final String NOMBRE_PREF_ENDPOINT_LANZADOR = "EndPointLanzador";
    
    //Escribe aqui el valor inicial del debug (0 = no existe debug, 1 = existe debug)
    private final String VALOR_INICIAL_PREF_DEBUG = "1";
    private final String VALOR_INICIAL_PREF_ENTORNO = "PSEUDOREAL";
    private final String VALOR_INICIAL_PREF_RUTAMODELOS = "recursos//impresos//pdf";
    private final String VALOR_INICIAL_PREF_PLANTILLAMODELO600 = "Modelo600.pdf";
    private final String VALOR_INICIAL_PREF_ENDPOINT_LANZADOR = "http://bus:7001/WSInternos/ProxyServices/PXLanzador";
   	
	
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
        m_preferencias.put(NOMBRE_PREF_RUTAMODELOS, VALOR_INICIAL_PREF_RUTAMODELOS);
        m_preferencias.put(NOMBRE_PREF_PLANTILLAMODELO600, VALOR_INICIAL_PREF_PLANTILLAMODELO600);
        m_preferencias.put(NOMBRE_PREF_ENDPOINT_LANZADOR, VALOR_INICIAL_PREF_ENDPOINT_LANZADOR);

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
            m_rutamodelos = m_preferencias.get(NOMBRE_PREF_RUTAMODELOS, "");
            m_plantillamodelo600 = m_preferencias.get(NOMBRE_PREF_PLANTILLAMODELO600, "");
            m_endpoint_lanzador = m_preferencias.get(NOMBRE_PREF_ENDPOINT_LANZADOR, "");
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
    
    public String getRutaPlantillas () {
    	return this.m_rutamodelos;
    };
    
    public String getPlatillaModelo600 () {
    	return this.m_plantillamodelo600;
    };        

    public String getEndpointLanzador () {
    	return this.m_endpoint_lanzador;
    };
}