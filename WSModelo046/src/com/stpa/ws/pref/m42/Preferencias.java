package com.stpa.ws.pref.m42;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.prefs.Preferences;

import com.stpa.ws.server.util.Logger;

public class Preferencias {

	private Preferences m_preferencias;
	private String m_wslanzadorwsdlurl;
	private String m_wslanzadorservicenamespace;
	private String m_wslanzadorservicename;
	private String m_wslanzadorentornoBDD;
	private String m_pdfmodelo046;
	private String m_plantillamodelo046;
	private String m_apppath;
	private String m_pdfplantillauri;
	private String m_debug;

    //constantes para trabajar con las preferencias
    private final String FICHERO_PREFERENCIAS = "prefsModelo046.xml";
    private final String DIRECTORIO_PREFERENCIAS = "proyectos//WSModelo046";
    
    private final String wslanzadorwsdlurl = "wslanzador.wsdl.url";
    private final String wslanzadorservicenamespace = "wslanzador.service.namespace";
    private final String wslanzadorservicename = "wslanzador.service.name";
    private final String wslanzadorentornoBDD = "wslanzador.entornoBDD";
    private final String pdfmodelo046 = "pdf.modelo.046";
    private final String plantillamodelo046 = "plantilla.modelo.046";
    private final String apppath = "app.path";
    private final String debug = "debug";
    
    //Escribe aqui el valor inicial del debug (0 = no existe debug, 1 = existe debug)
    private final String VALOR_INICIAL_PREF_wslanzadorwsdlurl = "http://bus.desa.epst.pa:7001/WSInternos/ProxyServices/PXLanzador?WSDL";
    private final String VALOR_INICIAL_PREF_wslanzadorservicenamespace = "http://stpa/services";
    private final String VALOR_INICIAL_PREF_wslanzadorservicename = "lanzaPLService";
    private final String VALOR_INICIAL_PREF_wslanzadorentornoBDD = "pseudoreal";
    private final String VALOR_INICIAL_PREF_pdfmodelo046 = "pdf/Modelo 046";
    private final String VALOR_INICIAL_PREF_plantillamodelo046 = "/WSMmodelo046/xml/046";
    private final String VALOR_INICIAL_PREF_apppath = "http://bus.desa.epst.pa:7001/WSModelo046/";
    private final String VALOR_INICIAL_PREF_debug = "1";
	
	public Preferencias(){
    	CompruebaFicheroPreferencias();
		CargarPreferencias();
	};

	public void CompruebaFicheroPreferencias()
    {
        File f = new File(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
        if (f.exists() == false){
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
        m_preferencias.put(wslanzadorwsdlurl, VALOR_INICIAL_PREF_wslanzadorwsdlurl);
        m_preferencias.put(wslanzadorservicenamespace, VALOR_INICIAL_PREF_wslanzadorservicenamespace);
        m_preferencias.put(wslanzadorservicename, VALOR_INICIAL_PREF_wslanzadorservicename);
        m_preferencias.put(wslanzadorentornoBDD, VALOR_INICIAL_PREF_wslanzadorentornoBDD);
        m_preferencias.put(pdfmodelo046, VALOR_INICIAL_PREF_pdfmodelo046);
        m_preferencias.put(plantillamodelo046, VALOR_INICIAL_PREF_plantillamodelo046);
        m_preferencias.put(apppath, VALOR_INICIAL_PREF_apppath);
        m_preferencias.put(debug, VALOR_INICIAL_PREF_debug);
        

        FileOutputStream outputStream = null;
        File fichero;
        try
        {
            fichero = new File(DIRECTORIO_PREFERENCIAS);
            if(fichero.exists() == false)
                fichero.mkdirs();
            
            outputStream = new FileOutputStream(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
            m_preferencias.exportNode(outputStream);
        }catch (Exception e){
        	com.stpa.ws.server.util.Logger.error(e.getMessage(),e,Logger.LOGTYPE.APPLOG);
        }finally{
            try{
                if(outputStream != null)
                    outputStream.close();
            }catch(Exception e){
            	com.stpa.ws.server.util.Logger.error("Error cerrando fichero de preferencias -> " + e.getMessage(),e,Logger.LOGTYPE.APPLOG);
            }
        }
    }
    
 // Obtencion de las preferencias que especificaran el almacen y su contraseña
    public void CargarPreferencias()
    {
        File f = new File(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
        if (f.exists()){
            //si existe el fichero de preferencias lo cargamos
            try{
            	FileInputStream inputStream = new FileInputStream(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
            	Preferences.importPreferences(inputStream);
            	inputStream.close();
            }catch(Exception e){
            	CrearFicheroPreferencias();
                //throw new StpawsException("Debe especificar primero las preferencias en el fichero: " + f.getAbsolutePath() + " (parar el servicio)",null);
            }

            m_preferencias = Preferences.systemNodeForPackage(this.getClass());

            //obtenemos las preferencias
            m_wslanzadorwsdlurl = m_preferencias.get(wslanzadorwsdlurl, "");
            m_wslanzadorservicenamespace = m_preferencias.get(wslanzadorservicenamespace, "");
            m_wslanzadorservicename = m_preferencias.get(wslanzadorservicename, "");
            m_wslanzadorentornoBDD= m_preferencias.get(wslanzadorentornoBDD, "");
            m_pdfmodelo046 = m_preferencias.get(pdfmodelo046, "");
            m_plantillamodelo046 = m_preferencias.get(plantillamodelo046, "");
            m_apppath = m_preferencias.get(apppath, "");
            m_debug = m_preferencias.get(debug, "");
        }else{
            //si no existe el fichero de preferencias lo crearemos
            CrearFicheroPreferencias();
        }
    }

	public Preferences getM_preferencias() {
		return m_preferencias;
	}

	public String getM_wslanzadorwsdlurl() {
		return m_wslanzadorwsdlurl;
	}

	public String getM_wslanzadorservicenamespace() {
		return m_wslanzadorservicenamespace;
	}

	public String getM_wslanzadorservicename() {
		return m_wslanzadorservicename;
	}

	public String getM_wslanzadorentornoBDD() {
		return m_wslanzadorentornoBDD;
	}

	public String getM_pdfmodelo046() {
		return m_pdfmodelo046;
	}

	public String getM_plantillamodelo046() {
		return m_plantillamodelo046;
	}

	public String getM_apppath() {
		return m_apppath;
	}
	
	public String getM_debug() {
		return m_debug;
	}

	public String getM_pdfplantillauri() {
		return m_pdfplantillauri;
	}    
}