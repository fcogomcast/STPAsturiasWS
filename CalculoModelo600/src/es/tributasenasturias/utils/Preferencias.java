package es.tributasenasturias.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.prefs.Preferences;

public final class Preferencias {

	private Preferences m_preferencias;
	private String m_debug;
	private String m_entorno;
	private String m_procalmacenado_calculo;
	
	private String m_endpoint_lanzador;
	
	//Variables relacionads con la firma.
	private String m_endpoint_firma;	
	private String m_certificado_firma;
	private String m_clave_firma;
	
	private String m_firma_digital;
	private String m_valida_firma;
	
	
	private String m_endpoint_autenticacion_pa;	
	private String m_xml_autorizacion;
	private String m_ip_autorizacion;
	
	//CRUBENCVS 47084 20/02/2023
	private String m_aliasCertificado;
	private String m_idNodoFirma;
	private String m_nodoPadre;
	private String m_namespaceNodoPadre;
	private String m_algoritmoFirma;
	private String m_algoritmoDigest;
	//FIN CRUBENCVS 47084
	
    //constantes para trabajar con las preferencias
    
    private final String FICHERO_PREFERENCIAS = "prefsCalculoModelo600.xml";
    private final String DIRECTORIO_PREFERENCIAS = "proyectos//WSANCERT_Calculo600";
    
    private final String NOMBRE_PREF_DEBUG = "Debug";
    private final String NOMBRE_PREF_ENTORNO = "Entorno";

    private final String NOMBRE_PREF_PROCALMACENADO_CALCULO = "ProcedimientoAlmacenado";
    
    private final String NOMBRE_PREF_ENDPOINT_LANZADOR = "EndPointLanzador";
   
    private final String NOMBRE_PREF_ENDPOINT_FIRMA = "EndPointFirma";
    private final String NOMBRE_PREF_CERTIFICADO_FIRMA = "CertificadoFirma";
    private final String NOMBRE_PREF_CLAVE_FIRMA = "ClaveFirma";
    
    private final String NOMBRE_PREF_FIRMA_DIGITAL = "firmaDigital";
    private final String NOMBRE_PREF_VALIDA_FIRMA = "validaFirma";
    
                
    private final static String NOMBRE_PREF_ENDPOINT_AUTENTICACION_PA = "EndPointAutenticacionPA";
	private final static String NOMBRE_PREF_XML_AUTORIZACION = "XmlAutorizacion";
	private final static String NOMBRE_PREF_IP_AUTORIZACION = "IpAutorizacion";
    		     
	//CRUBENCVS 47084 20/02/2023
	private final static String NOMBRE_PREF_ALIAS_CERTIFICADO = "aliasCertificadoFirma";
	private final static String NOMBRE_PREF_ID_NODO = "idNodoFirmar";
	private final static String NOMBRE_PREF_NODO_PADRE_FIRMA = "nodoPadreFirmaConsulta";
	private final static String NOMBRE_PREF_NAMESPACE_NODO_PADRE= "namespaceNodoPadreFirmaConsulta";
	private final static String NOMBRE_PREF_ALGORITMO_FIRMA = "AlgoritmoFirmaMensaje";
	private final static String NOMBRE_PREF_ALGORITMO_DIGEST = "AlgoritmoDigestMensaje";
	//FIN  CRUBENCVS 47084
	
	//Escribe aqui el valor inicial del debug (0 = no existe debug, 1 = existe debug)
    private final String VALOR_INICIAL_PREF_DEBUG = "1";
    private final String VALOR_INICIAL_PREF_ENTORNO = "PSEUDOREAL";
    private final String VALOR_INICIAL_PREF_PROCALMACENADO_CALCULO = "ANCERT.wsCalculoModelo600";
    
    private final String VALOR_INICIAL_PREF_ENDPOINT_LANZADOR = "http://bus:7001/WSInternos/ProxyServices/PXLanzador";
    
        
    private final String VALOR_INICIAL_PREF_ENDPOINT_FIRMA = "http://bus:7001/WSInternos/ProxyServices/PXFirmaDigital";
    private final String VALOR_INICIAL_PREF_CERTIFICADO_FIRMA = "";
    private final String VALOR_INICIAL_PREF_CLAVE_FIRMA = "";

    private final String VALOR_INICIAL_PREF_FIRMA_DIGITAL = "S";
	private final String VALOR_INICIAL_PREF_VALIDA_FIRMA = "S";

    
    private final String VALOR_INICIAL_PREF_ENDPOINT_AUTENTICACION_PA = "http://bus:7001/WSAutenticacionPA/ProxyServices/PXAutenticacionPA";
	private final String VALOR_INICIAL_PREF_XML_AUTORIZACION = "ConsultaCertificados/PeticionAutorizacion.xml";
	private final String VALOR_INICIAL_PREF_IP_AUTORIZACION = "10.112.10.35";
           
	//CRUBENCVS 47084 20/02/2023
	private final String VALOR_INICIAL_ALIAS_CERTIFICADO = "Tributas";
	private final String VALOR_INICIAL_ID_NODO = "declaracion";
	private final String VALOR_INICIAL_NODO_PADRE_FIRMA = "remesa";
	private final String VALOR_INICIAL_NAMESPACE_NODO_PADRE = "";
	private final String VALOR_INICIAL_ALGORITMO_FIRMA = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
	private final String VALOR_INICIAL_ALGORITMO_DIGEST = "http://www.w3.org/2001/04/xmlenc#sha256";
	//FIN CRUBENCVS 47084
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
        m_preferencias.put(NOMBRE_PREF_PROCALMACENADO_CALCULO, VALOR_INICIAL_PREF_PROCALMACENADO_CALCULO);        
        m_preferencias.put(NOMBRE_PREF_ENDPOINT_LANZADOR, VALOR_INICIAL_PREF_ENDPOINT_LANZADOR);
        
        m_preferencias.put(NOMBRE_PREF_ENDPOINT_FIRMA, VALOR_INICIAL_PREF_ENDPOINT_FIRMA);       
        m_preferencias.put(NOMBRE_PREF_CERTIFICADO_FIRMA, VALOR_INICIAL_PREF_CERTIFICADO_FIRMA);
        m_preferencias.put(NOMBRE_PREF_CLAVE_FIRMA, VALOR_INICIAL_PREF_CLAVE_FIRMA);
        
        m_preferencias.put(NOMBRE_PREF_FIRMA_DIGITAL,VALOR_INICIAL_PREF_FIRMA_DIGITAL);
        m_preferencias.put(NOMBRE_PREF_VALIDA_FIRMA,VALOR_INICIAL_PREF_VALIDA_FIRMA);
        
        m_preferencias.put(NOMBRE_PREF_ENDPOINT_AUTENTICACION_PA,VALOR_INICIAL_PREF_ENDPOINT_AUTENTICACION_PA);
        m_preferencias.put(NOMBRE_PREF_XML_AUTORIZACION,VALOR_INICIAL_PREF_XML_AUTORIZACION);
        m_preferencias.put(NOMBRE_PREF_IP_AUTORIZACION,VALOR_INICIAL_PREF_IP_AUTORIZACION);
        
      //CRUBENCVS 47084 20/02/2023
        m_preferencias.put(NOMBRE_PREF_ALIAS_CERTIFICADO,VALOR_INICIAL_ALIAS_CERTIFICADO);
        m_preferencias.put(NOMBRE_PREF_ID_NODO,VALOR_INICIAL_ID_NODO);
        m_preferencias.put(NOMBRE_PREF_NODO_PADRE_FIRMA,VALOR_INICIAL_NODO_PADRE_FIRMA);
        m_preferencias.put(NOMBRE_PREF_NAMESPACE_NODO_PADRE,VALOR_INICIAL_NAMESPACE_NODO_PADRE);
        m_preferencias.put(NOMBRE_PREF_ALGORITMO_FIRMA,VALOR_INICIAL_ALGORITMO_FIRMA);
        m_preferencias.put(NOMBRE_PREF_ALGORITMO_DIGEST,VALOR_INICIAL_ALGORITMO_DIGEST);
      //FIN CRUBENCVS 47084 
    	
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
            m_procalmacenado_calculo = m_preferencias.get(NOMBRE_PREF_PROCALMACENADO_CALCULO, "");            
            m_endpoint_lanzador = m_preferencias.get(NOMBRE_PREF_ENDPOINT_LANZADOR, "");            
            
            m_endpoint_firma = m_preferencias.get(NOMBRE_PREF_ENDPOINT_FIRMA, "");
            m_certificado_firma = m_preferencias.get(NOMBRE_PREF_CERTIFICADO_FIRMA, "");
            m_clave_firma = m_preferencias.get(NOMBRE_PREF_CLAVE_FIRMA, "");

            m_firma_digital=m_preferencias.get(NOMBRE_PREF_FIRMA_DIGITAL,"");
			m_valida_firma =m_preferencias.get(NOMBRE_PREF_VALIDA_FIRMA,"");
            
			m_endpoint_autenticacion_pa = m_preferencias.get(NOMBRE_PREF_ENDPOINT_AUTENTICACION_PA, "");	
			m_xml_autorizacion =m_preferencias.get(NOMBRE_PREF_XML_AUTORIZACION, "");
			m_ip_autorizacion=m_preferencias.get(NOMBRE_PREF_IP_AUTORIZACION, "");
			
			//CRUBENCVS 47084 20/02/2023
			m_aliasCertificado = m_preferencias.get(NOMBRE_PREF_ALIAS_CERTIFICADO,"");
			m_idNodoFirma = m_preferencias.get(NOMBRE_PREF_ID_NODO,"");
			m_nodoPadre =m_preferencias.get(NOMBRE_PREF_NODO_PADRE_FIRMA,"");
			m_namespaceNodoPadre= m_preferencias.get(NOMBRE_PREF_NAMESPACE_NODO_PADRE,"");
			m_algoritmoFirma = m_preferencias.get(NOMBRE_PREF_ALGORITMO_FIRMA,"");
			m_algoritmoDigest = m_preferencias.get(NOMBRE_PREF_ALGORITMO_DIGEST,"");
			//FIN CRUBENCVS 47084
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
    public String getPACalculo() {
    	return this.m_procalmacenado_calculo;
    };
    
           
    public String getEndpointLanzador () {
    	return this.m_endpoint_lanzador;
    };
    
    //Métodos relacionados con la firma digital.
    public String getEndpointFirma () {
    	return this.m_endpoint_firma;
    };
    
    public String getCertificadoFirma () {
    	return this.m_certificado_firma;
    };
    
    public String getClaveFirma () {
    	return this.m_clave_firma;
    };

    
	public String getFirmaDigital() {
		return this.m_firma_digital;
	};    
    
	public String getValidaFirma() {
		return this.m_valida_firma;
	};
    
    public String getEndPointAutenticacion() {
		return this.m_endpoint_autenticacion_pa;
	};
    
    public String getXmlAutorizacion() {
    	return this.m_xml_autorizacion;
	};
	
	public String getIpAutorizacion() {
		return this.m_ip_autorizacion;
	};
	
	//INI  CRUBENCVS 47084 20/02/2023
	
	public String getAliasCertificado() {
    	return this.m_aliasCertificado;
	};
	
	public String getIdNodoFirma() {
    	return this.m_idNodoFirma;
	};
	
	public String getNodoPadre() {
    	return this.m_nodoPadre;
	};
	
	public String getNamespaceNodoPadre() {
    	return this.m_namespaceNodoPadre;
	};
	
	public String getUriAlgoritmoFirma() {
    	return this.m_algoritmoFirma;
	};
	
	public String getUriAlgoritmoDigest() {
    	return this.m_algoritmoDigest;
	};
	//FIN CRUBENCVs 47084
    
    
}

