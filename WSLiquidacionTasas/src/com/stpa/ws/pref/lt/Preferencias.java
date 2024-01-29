package com.stpa.ws.pref.lt;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.prefs.Preferences;
import com.stpa.ws.server.util.Logger;
//import com.stpa.ws.server.util.Logger.LOGTYPE;

public class Preferencias {

	private Preferences m_preferencias;
	

	private String m_wslanzadorentornoBDD;	
	private String m_debug;
	
	private String m_wsdocumentopagoendpoint;
	
	//C.R.V. 26/09/2011. Validación de MAC
	private String m_validarMAC;
	//Fin C.R.V. 26/09/2011
	private String m_wslanzadorendpoint;
	
    //constantes para trabajar con las preferencias
    private final String FICHERO_PREFERENCIAS = "prefsLiquidacionTasas.xml";
    private final String DIRECTORIO_PREFERENCIAS = "proyectos//WSLiquidacionTasas";
    
    private final String wslanzadorentornoBDD = "wslanzador.entornoBDD";
    
    
    private final String debug = "debug";
    //C.R.V. 26/09/2011. Validación de MAC
    private final String validarMAC = "validar.MAC";
    //Fin C.R.V. 26/09/2011
    private final String wslanzadorendpoint = "wslanzador.endpoint";
    private final String wsdocumentopagoendpoint = "wsdocumentopago.endpoint";
    
    //Escribe aqui el valor inicial del debug (0 = no existe debug, 1 = existe debug)
    private final String VALOR_INICIAL_PREF_wslanzadorentornoBDD = "EXPLOTACION";
    private final String VALOR_INICIAL_PREF_debug = "1";
    private final String VALOR_INICIAL_PREF_wslanzadorendpoint = "http://bus:7101/WSInternos/ProxyServices/PXLanzador";
    //C.R.V. 26/09/2011. Validación de MAC
    private final String VALOR_INICIAL_PREF_validarMAC = "S";
    //Fin C.R.V. 26/09/2011
    private final String VALOR_INICIAL_PREF_wsdocumentopagoendpoint = "http://bus:7101/WSInternos/ProxyServices/PXDocumentosPago";    
    	
//    private final String VALOR_INICIAL_PREF_wsendpointdocumentopago = "http://bus:7001/DocumentoPago/DocumentoPagoService";
	
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
        m_preferencias.put(wslanzadorentornoBDD, VALOR_INICIAL_PREF_wslanzadorentornoBDD);
        m_preferencias.put(validarMAC,VALOR_INICIAL_PREF_validarMAC);
                                     
        m_preferencias.put(wslanzadorendpoint, VALOR_INICIAL_PREF_wslanzadorendpoint);
        m_preferencias.put(wsdocumentopagoendpoint, VALOR_INICIAL_PREF_wsdocumentopagoendpoint);
        
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
        	com.stpa.ws.server.util.Logger.errors(e.getMessage(),e,Logger.LOGTYPE.APPLOG);
        }finally{
            try{
                if(outputStream != null)
                    outputStream.close();
            }catch(Exception e){
            	com.stpa.ws.server.util.Logger.errors("Error cerrando fichero de preferencias -> " + e.getMessage(),e,Logger.LOGTYPE.APPLOG);
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
            }

            m_preferencias = Preferences.systemNodeForPackage(this.getClass());

            //obtenemos las preferencias
            m_wslanzadorentornoBDD= m_preferencias.get(wslanzadorentornoBDD, "");
            
            m_debug = m_preferencias.get(debug, "");
                                   
            //C.R.V. 26/09/2011. Se elimina la referencia al servicio de CORECA
            //m_wscorecawsdlurl = m_preferencias.get(wscorecawsdlurl, "");
            //m_wscorecaservicenamespace =m_preferencias.get(wscorecaservicenamespace, "");
            //m_wscorecaservicename =m_preferencias.get(wscorecaservicename, "");
            //Fin C.R.V. 26/09/2011
            //C.R.V. 26/09/2011. Validación de MAC
            m_validarMAC = m_preferencias.get(validarMAC, "");
            //Fin C.R.V. 26/09/2011
            m_wslanzadorendpoint= m_preferencias.get(wslanzadorendpoint, "");
            m_wsdocumentopagoendpoint= m_preferencias.get(wsdocumentopagoendpoint, "");
        }else{
            //si no existe el fichero de preferencias lo crearemos
            CrearFicheroPreferencias();
        }
    }

	public Preferences getM_preferencias() {
		return m_preferencias;
	}

	public String getM_wslanzadorentornoBDD() {
		return m_wslanzadorentornoBDD;
	}

	public String getM_debug() {
		return m_debug;
	}  
	//C.R.V. 26/09/2011. Se elimina la referencia al servicio de CORECA
	/*public String getM_wscorecawsdlurl() {
		return m_wscorecawsdlurl;		
	}

	public String getM_wscorecaservicenamespace() {
		return m_wscorecaservicenamespace;
	}

	public String getM_wscorecaservicename() {
		return m_wscorecaservicename;
	}*/
	//Fin C.R.V. 26/09/2011	
	public String getM_wsdocumentopagoendpoint() {
		return m_wsdocumentopagoendpoint;
	}
	//C.R.V. 26/09/2011. Validación de MAC
	public String getM_validarMAC() {
		return m_validarMAC;
	}
	//Fin C.R.V. 26/09/2011
	public String getM_wslanzadorendpoint() {
		return m_wslanzadorendpoint;
	}
}





