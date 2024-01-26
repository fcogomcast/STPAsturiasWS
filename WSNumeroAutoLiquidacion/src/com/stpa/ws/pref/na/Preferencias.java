package com.stpa.ws.pref.na;

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
	private String m_debug;

    //constantes para trabajar con las preferencias
    private final String FICHERO_PREFERENCIAS = "prefsNumeroAutoLiquidacion.xml";
    private final String DIRECTORIO_PREFERENCIAS = "proyectos//WSNumeroAutoLiquidacion";
    
    private final String wslanzadorwsdlurl = "wslanzador.wsdl.url";
    private final String wslanzadorservicenamespace = "wslanzador.service.namespace";
    private final String wslanzadorservicename = "wslanzador.service.name";
    private final String wslanzadorentornoBDD = "wslanzador.entornoBDD";
    private final String debug = "debug";
    
    //Escribe aqui el valor inicial del debug (0 = no existe debug, 1 = existe debug)
    private final String VALOR_INICIAL_PREF_wslanzadorwsdlurl = "http://bus.desa.epst.pa:7001/WSInternos/ProxyServices/PXLanzador?WSDL";
    private final String VALOR_INICIAL_PREF_wslanzadorservicenamespace = "http://stpa/services";
    private final String VALOR_INICIAL_PREF_wslanzadorservicename = "lanzaPLService";
    private final String VALOR_INICIAL_PREF_wslanzadorentornoBDD = "pseudoreal";
    private final String VALOR_INICIAL_PREF_debug = "1";
	
	public Preferencias(){
		CompruebaFicheroPreferencias();
		CargarPreferencias();
	}
	
	public void CompruebaFicheroPreferencias(){
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
        m_preferencias.put(debug, VALOR_INICIAL_PREF_debug);
        
        FileOutputStream outputStream = null;
        File fichero;
        try{
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
    
 // Obtencion de las preferencias que especificaran el almacen y su contrase�a
    public void CargarPreferencias(){
        File f = new File(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
        if (f.exists()){
            //si existe el fichero de preferencias lo cargamos
            try{
            	FileInputStream inputStream = new FileInputStream(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
            	Preferences.importPreferences(inputStream);
            	inputStream.close();
            }catch(Exception e){
            	CrearFicheroPreferencias();
            }

            m_preferencias = Preferences.systemNodeForPackage(this.getClass());

            //obtenemos las preferencias
            m_wslanzadorwsdlurl = m_preferencias.get(wslanzadorwsdlurl, "");
            m_wslanzadorservicenamespace = m_preferencias.get(wslanzadorservicenamespace, "");
            m_wslanzadorservicename = m_preferencias.get(wslanzadorservicename, "");
            m_wslanzadorentornoBDD= m_preferencias.get(wslanzadorentornoBDD, "");
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

	public String getM_debug() {
		return m_debug;
	}   
}
