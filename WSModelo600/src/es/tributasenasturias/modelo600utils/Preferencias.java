package es.tributasenasturias.modelo600utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

import es.tributasenasturias.exception.PreferenciasException;

public final class Preferencias {

	private Preferences m_preferencias;
	private String m_debug;
	private String m_entorno;
	private String m_procalmacenado_calculo;
	private String m_procalmacenado_integra;
	private String m_procalmacenado_altaexpte;
	private String m_procalmacenado_altadoc;
	private String m_procalmacenado_valpres;
	private String m_endpoint_altaescritura;
	private String m_endpoint_lanzador;
	private String m_endpoint_pago;
	private String m_endpoint_modeloPDF;
	private String m_proc_codVerif;

	//Permiso servicio
	private String m_procalmacenado_permservicio;
	private String m_procalmacenado_codpermservicio;
	
	//Variables relacionads con la firma.
	private String m_endpoint_firma;	
	private String m_certificado_firma;
	private String m_clave_firma;

	private String m_endpoint_autenticacion_pa;	
	private String m_xml_autorizacion;
	private String m_ip_autorizacion;
	private String m_firma_digital;
	private String m_valida_firma;
	private String m_valida_certificado;
	private String m_procconsulta_documento;
	private String m_dir_app;
	private String m_modo_log;

    //Constantes para trabajar con las preferencias
    private final String FICHERO_PREFERENCIAS = "prefsModelo600.xml";
    private final String DIRECTORIO_PREFERENCIAS = "proyectos//WSModelo600";

    private final String NOMBRE_PREF_DEBUG = "Debug";
    private final String NOMBRE_PREF_ENTORNO = "Entorno";
    private final String NOMBRE_PREF_PROCALMACENADO_CALCULO = "pACalculoModelo600";
    private final String NOMBRE_PREF_PROCALMACENADO_INTEGRA = "pAIntegra";
    private final String NOMBRE_PREF_PROCALMACENADO_ALTAEXPTE = "pAAltaExptediente";
    private final String NOMBRE_PREF_PROCALMACENADO_ALTADOC = "pAAltaDocumento";
    private final String NOMBRE_PREF_PROCALMACENADO_VALIDA_PRESENTACION = "pAValidaPresentacion";
    private final String NOMBRE_PREF_ENDPOINT_ALTAESCRITURA = "EndPointEscritura";
    private final String NOMBRE_PREF_ENDPOINT_LANZADOR = "EndPointLanzador";
    private final String NOMBRE_PREF_ENDPOINT_PAGO = "EndPointPago";
    private final String NOMBRE_PREF_ENDPOINT_MODELOPDF = "EndPointModeloPDF";

    private final String NOMBRE_PREF_ENDPOINT_FIRMA = "EndPointFirma";
    private final String NOMBRE_PREF_CERTIFICADO_FIRMA = "CertificadoFirma";
    private final String NOMBRE_PREF_CLAVE_FIRMA = "ClaveFirma";

    private final static String NOMBRE_PREF_FIRMA_DIGITAL = "firmaDigital";
    private final static String NOMBRE_PREF_VALIDA_FIRMA = "validaFirma";
    private final static String NOMBRE_PREF_VALIDA_CERTIFICADO = "validaCertificado";

    private final static String NOMBRE_PREF_PROC_CODVERIF = "pAObtenerCodVerif";
    
    
    private final static String NOMBRE_PREF_ENDPOINT_AUTENTICACION_PA = "EndPointAutenticacionPA";
	private final static String NOMBRE_PREF_XML_AUTORIZACION = "XmlAutorizacion";
	private final static String NOMBRE_PREF_IP_AUTORIZACION = "IpAutorizacion";

	private final static String NOMBRE_PREF_PROCCONSULTA_DOCUMENTO = "pAConsultaDocumento";
	private final static String NOMBRE_PREF_DIR_APP = "DirectorioApp";
	private final static String NOMBRE_PREF_MODO_LOG = "ModoLog";

	//Permiso servicio
	private String NOMBRE_PREF_PROCALMACENADO_PERMSERVICIO="pAPermisoServicio";
	private String NOMBRE_PREF_CODIGO_PERMSERVICIO="codPermisoServicio";
	private String VALOR_INICIAL_PREF_PROCALMACENADO_PERMSERVICIO="INTERNET.permisoServicio";
	private String VALOR_INICIALPREF_CODIGO_PERMSERVICIO="MODELO600";

	//Escribe aqui el valor inicial del debug (0 = no existe debug, 1 = existe debug)
    private final String VALOR_INICIAL_PREF_DEBUG = "1";
    private final String VALOR_INICIAL_PREF_ENTORNO = "EXPLOTACION";
    private final String VALOR_INICIAL_PREF_PROCALMACENADO_CALCULO = "MODELO600.calculoModelo600";
    private final String VALOR_INICIAL_PREF_PROCALMACENADO_INTEGRA = "MODELO600.procesoIntegracionM600";
    private final String VALOR_INICIAL_PREF_PROCALMACENADO_ALTAEXPTE = "MODELO600.procesoAltaExpediente";
    private final String VALOR_INICIAL_PREF_PROCALMACENADO_ALTADOC = "INTERNET_DOCUMENTOSV2.AltaDocumento";
    private final String VALOR_INICIAL_PREF_PROCALMACENADO_VALIDA_PRESENTACION = "Modelo600.ValidaPresentacionModelo600";
    private final String VALOR_INICIAL_PREF_ENDPOINT_ALTAESCRITURA = "http://bus:7101/WSANCERT/ProxyServices/Externos/PXEscrituras";
    private final String VALOR_INICIAL_PREF_ENDPOINT_LANZADOR = "http://bus:7101/WSInternos/ProxyServices/PXLanzadorMasivo";
    private final String VALOR_INICIAL_PREF_ENDPOINT_PAGO = "http://bus:7101/WSPasarelaPago/ProxyServices/PXPasarelaPagoST";
    private final String VALOR_INICIAL_PREF_ENDPOINT_MODELOPDF = "http://bus:7101/WSANCERT/ProxyServices/PXModelosPDF";

    private final String VALOR_INICIAL_PREF_PROC_CODVERIF = "INTERNET.obtenerCodVerif";
    
    private final String VALOR_INICIAL_PREF_ENDPOINT_FIRMA = "http://bus:7101/WSInternos/ProxyServices/PXFirmaDigital";
    private final String VALOR_INICIAL_PREF_CERTIFICADO_FIRMA = "";
    private final String VALOR_INICIAL_PREF_CLAVE_FIRMA = "";

    private final String VALOR_INICIAL_PREF_ENDPOINT_AUTENTICACION_PA = "http://bus:7101/WSAutenticacionPA/ProxyServices/PXAutenticacionPA";
	private final String VALOR_INICIAL_PREF_XML_AUTORIZACION = "ConsultaCertificados/PeticionAutorizacion.xml";
	private final String VALOR_INICIAL_PREF_IP_AUTORIZACION = "10.112.10.35";

	private final String VALOR_INICIAL_PREF_FIRMA_DIGITAL = "S";
	private final String VALOR_INICIAL_PREF_VALIDA_FIRMA = "S";
	private final String VALOR_INICIAL_PREF_VALIDA_CERTIFICADO = "S";
	private final String VALOR_INICIAL_PREF_PROCCONSULTA_DOCUMENTO = "INTERNET_DOCUMENTOSV2.consultaDocumento";
	private final String VALOR_INICIAL_PREF_DIR_APP = "proyectos/WSModelo600";
	private final String VALOR_INICIAL_PREF_MODO_LOG = "ALL";

	public Preferencias() {
		//CopiaFicherosImpresos();
	};

    public void CompruebaFicheroPreferencias(){
        File f = new File(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
        if (f.exists() == false){
            CrearFicheroPreferencias();
        }
    }

    /***********************************************************
     * Creamos el fichero de preferencias con los valores por defecto
     ***************************************************************/
    private void CrearFicheroPreferencias(){
        m_preferencias = Preferences.systemNodeForPackage(this.getClass());

        m_preferencias.put(NOMBRE_PREF_DEBUG, VALOR_INICIAL_PREF_DEBUG);
        m_preferencias.put(NOMBRE_PREF_ENTORNO, VALOR_INICIAL_PREF_ENTORNO);
        m_preferencias.put(NOMBRE_PREF_PROCALMACENADO_CALCULO, VALOR_INICIAL_PREF_PROCALMACENADO_CALCULO);
        m_preferencias.put(NOMBRE_PREF_PROCALMACENADO_INTEGRA, VALOR_INICIAL_PREF_PROCALMACENADO_INTEGRA);
        m_preferencias.put(NOMBRE_PREF_PROCALMACENADO_ALTAEXPTE, VALOR_INICIAL_PREF_PROCALMACENADO_ALTAEXPTE);
        m_preferencias.put(NOMBRE_PREF_PROCALMACENADO_ALTADOC, VALOR_INICIAL_PREF_PROCALMACENADO_ALTADOC);
        m_preferencias.put(NOMBRE_PREF_PROCALMACENADO_VALIDA_PRESENTACION, VALOR_INICIAL_PREF_PROCALMACENADO_VALIDA_PRESENTACION);
        m_preferencias.put(NOMBRE_PREF_ENDPOINT_ALTAESCRITURA, VALOR_INICIAL_PREF_ENDPOINT_ALTAESCRITURA);
        m_preferencias.put(NOMBRE_PREF_ENDPOINT_LANZADOR, VALOR_INICIAL_PREF_ENDPOINT_LANZADOR);
        m_preferencias.put(NOMBRE_PREF_ENDPOINT_PAGO, VALOR_INICIAL_PREF_ENDPOINT_PAGO);
        m_preferencias.put(NOMBRE_PREF_ENDPOINT_MODELOPDF, VALOR_INICIAL_PREF_ENDPOINT_MODELOPDF);

        m_preferencias.put(NOMBRE_PREF_ENDPOINT_FIRMA, VALOR_INICIAL_PREF_ENDPOINT_FIRMA);       
        m_preferencias.put(NOMBRE_PREF_CERTIFICADO_FIRMA, VALOR_INICIAL_PREF_CERTIFICADO_FIRMA);
        m_preferencias.put(NOMBRE_PREF_CLAVE_FIRMA, VALOR_INICIAL_PREF_CLAVE_FIRMA);
        
        m_preferencias.put(NOMBRE_PREF_ENDPOINT_AUTENTICACION_PA,VALOR_INICIAL_PREF_ENDPOINT_AUTENTICACION_PA);
        m_preferencias.put(NOMBRE_PREF_XML_AUTORIZACION,VALOR_INICIAL_PREF_XML_AUTORIZACION);
        m_preferencias.put(NOMBRE_PREF_IP_AUTORIZACION,VALOR_INICIAL_PREF_IP_AUTORIZACION);
        
        m_preferencias.put(NOMBRE_PREF_FIRMA_DIGITAL,VALOR_INICIAL_PREF_FIRMA_DIGITAL);
        m_preferencias.put(NOMBRE_PREF_VALIDA_FIRMA,VALOR_INICIAL_PREF_VALIDA_FIRMA);
        m_preferencias.put(NOMBRE_PREF_VALIDA_CERTIFICADO,VALOR_INICIAL_PREF_VALIDA_CERTIFICADO);
        m_preferencias.put(NOMBRE_PREF_PROCCONSULTA_DOCUMENTO,VALOR_INICIAL_PREF_PROCCONSULTA_DOCUMENTO);
        m_preferencias.put(NOMBRE_PREF_DIR_APP,VALOR_INICIAL_PREF_DIR_APP);
        m_preferencias.put(NOMBRE_PREF_MODO_LOG,VALOR_INICIAL_PREF_MODO_LOG);

        m_preferencias.put(NOMBRE_PREF_PROCALMACENADO_PERMSERVICIO, VALOR_INICIAL_PREF_PROCALMACENADO_PERMSERVICIO);
        m_preferencias.put(NOMBRE_PREF_CODIGO_PERMSERVICIO, VALOR_INICIALPREF_CODIGO_PERMSERVICIO);
        m_preferencias.put(NOMBRE_PREF_PROC_CODVERIF,VALOR_INICIAL_PREF_PROC_CODVERIF);
        
        FileOutputStream outputStream = null;
        File fichero;
        try{
            fichero = new File(DIRECTORIO_PREFERENCIAS);
            if(fichero.exists() == false)
                fichero.mkdirs();
            outputStream = new FileOutputStream(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
            m_preferencias.exportNode(outputStream);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(outputStream != null)
                    outputStream.close();
            }catch(Exception e){
                System.out.println("Error cerrando fichero de preferencias -> " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    

    // Obtencion de las preferencias que especificaran el almacen y su contraseña
    public void CargarPreferencias() throws PreferenciasException{
        File f = new File(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
        try
        {
	        if (f.exists()){
	            //si existe el fichero de preferencias lo cargamos
	            FileInputStream inputStream;
				inputStream = new FileInputStream(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
				Preferences.importPreferences(inputStream);
	            inputStream.close();
	            m_preferencias = Preferences.systemNodeForPackage(this.getClass());
	
	            //Obtenemos las preferencias
	            m_debug = m_preferencias.get(NOMBRE_PREF_DEBUG, "");
	            m_entorno = m_preferencias.get(NOMBRE_PREF_ENTORNO, "");
	            m_procalmacenado_calculo = m_preferencias.get(NOMBRE_PREF_PROCALMACENADO_CALCULO, "");            
	            m_procalmacenado_integra = m_preferencias.get(NOMBRE_PREF_PROCALMACENADO_INTEGRA, "");
	            m_procalmacenado_altaexpte = m_preferencias.get(NOMBRE_PREF_PROCALMACENADO_ALTAEXPTE, "");
	            m_procalmacenado_altadoc = m_preferencias.get(NOMBRE_PREF_PROCALMACENADO_ALTADOC, "");
	            m_procalmacenado_valpres = m_preferencias.get(NOMBRE_PREF_PROCALMACENADO_VALIDA_PRESENTACION, "");
	            m_endpoint_altaescritura = m_preferencias.get(NOMBRE_PREF_ENDPOINT_ALTAESCRITURA,"");
	            m_endpoint_lanzador = m_preferencias.get(NOMBRE_PREF_ENDPOINT_LANZADOR, "");
	            m_endpoint_pago = m_preferencias.get(NOMBRE_PREF_ENDPOINT_PAGO, "");
	            m_endpoint_modeloPDF = m_preferencias.get(NOMBRE_PREF_ENDPOINT_MODELOPDF, "");
	            
	            m_endpoint_firma = m_preferencias.get(NOMBRE_PREF_ENDPOINT_FIRMA, "");
	            m_certificado_firma = m_preferencias.get(NOMBRE_PREF_CERTIFICADO_FIRMA, "");
	            m_clave_firma = m_preferencias.get(NOMBRE_PREF_CLAVE_FIRMA, "");
	            
				m_endpoint_autenticacion_pa = m_preferencias.get(NOMBRE_PREF_ENDPOINT_AUTENTICACION_PA, "");	
				m_xml_autorizacion =m_preferencias.get(NOMBRE_PREF_XML_AUTORIZACION, "");
				m_ip_autorizacion=m_preferencias.get(NOMBRE_PREF_IP_AUTORIZACION, "");
				m_firma_digital=m_preferencias.get(NOMBRE_PREF_FIRMA_DIGITAL,"");
				m_valida_firma =m_preferencias.get(NOMBRE_PREF_VALIDA_FIRMA,"");
				m_valida_certificado =m_preferencias.get(NOMBRE_PREF_VALIDA_CERTIFICADO,"");
				m_procconsulta_documento =m_preferencias.get(NOMBRE_PREF_PROCCONSULTA_DOCUMENTO,"");
				m_dir_app = m_preferencias.get(NOMBRE_PREF_DIR_APP, "");
				m_modo_log = m_preferencias.get(NOMBRE_PREF_MODO_LOG, "");
				
				m_proc_codVerif=m_preferencias.get(NOMBRE_PREF_PROC_CODVERIF,"");
	
				m_procalmacenado_permservicio=m_preferencias.get(NOMBRE_PREF_PROCALMACENADO_PERMSERVICIO,"");
				m_procalmacenado_codpermservicio=m_preferencias.get(NOMBRE_PREF_CODIGO_PERMSERVICIO,"");
	        }else{
	            //si no existe el fichero de preferencias lo crearemos
	            CrearFicheroPreferencias();
	            throw new PreferenciasException("Debe especificar primero las preferencias en el fichero: " + f.getAbsolutePath() + " (parar el servicio)");
	        }
        }catch (FileNotFoundException e)
        {
        	throw new PreferenciasException ("Error en la carga de preferencias: no se puede encontrar el fichero de preferencias.");
        } catch (IOException e)
        {
        	throw new PreferenciasException ("Error en la carga de preferencias: error de entrada/salida :"+e.getMessage());
        } catch (InvalidPreferencesFormatException e)
        {
        	throw new PreferenciasException ("Error en la carga de preferencias: formato no válido: " + e.getMessage());
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
    
    public String getPAIntegra () {
    	return this.m_procalmacenado_integra;
    };

    public String getPAAltaexpediente () {
    	return this.m_procalmacenado_altaexpte;
    };
    
    public String getPAAltaDocumento () {
    	return this.m_procalmacenado_altadoc;
    };

	public String getPAValidarPresentacion(){
		return this.m_procalmacenado_valpres;
	}

    public String getEndpointAltaEscritura () {
    	return this.m_endpoint_altaescritura;
    };

    public String getEndpointLanzador () {
    	return this.m_endpoint_lanzador;
    };
    public String getEndpointPago () {
    	return this.m_endpoint_pago;
    };
    
    public String getEndpointModeloPDF () {
    	return this.m_endpoint_modeloPDF;
    };

    public String getPAPermisoServicio () {
    	return this.m_procalmacenado_permservicio;
    };

    public String getCodigoPermisoServicio () {
    	return this.m_procalmacenado_codpermservicio;
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

    public String getEndPointAutenticacion() {
		return this.m_endpoint_autenticacion_pa;
	};
    
    public String getXmlAutorizacion() {
    	return this.m_xml_autorizacion;
	};

	public String getIpAutorizacion() {
		return this.m_ip_autorizacion;
	};

	public String getFirmaDigital() {
		return this.m_firma_digital;
	};    

	public String getValidaFirma() {
		return this.m_valida_firma;
	};
	
	public String getValidaCertificado() {
		return this.m_valida_certificado;
	};
	public String getPAConsultaDocumento(){
		return this.m_procconsulta_documento;
	}

	public String getDirApp(){
		return this.m_dir_app;
	}

	public String getModoLog(){
		return this.m_modo_log;
	}

	public String getpAProcCodVerif() {
		return this.m_proc_codVerif;
	};
}