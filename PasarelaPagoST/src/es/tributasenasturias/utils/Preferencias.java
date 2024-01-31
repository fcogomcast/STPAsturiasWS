package es.tributasenasturias.utils;
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

import es.tributasenasturias.exceptions.PreferenciasException;
//
public class Preferencias 
{
	private Preferences m_preferencias;
	private final String FICHERO_PREFERENCIAS = "preferenciasPasarelaPagoST.xml";
    private final String DIRECTORIO_PREFERENCIAS = "proyectos/PasarelaPagoST";
    
    private Map<String, String> tablaPreferencias = new HashMap<String, String>();
	
	//nombres de las preferencias
	private final String KEY_PREF_USUARIO_APLICACION = "UsuarioAplicacion";
	private final String KEY_PREF_ESQUEMA = "EsquemaBaseDatos";
	private final String KEY_PREF_ENDPOINT_LANZADOR = "EndPointLanzador";
	private final String KEY_PREF_LOG = "ModoLog";
	private final String KEY_PREF_VALIDA_MAC = "ValidarMac";
	private final String KEY_PREF_ERRORES_CAJA = "FicheroErroresInfoCaja"; //Mapeo de errores del servicio de Cajastur a errores nuestros.
	private final String KEY_PREF_ERRORES_BD = "FicheroErroresInfoBD"; //Mapeo de errores controlados de BD a errores de nuestro servicio.
	private final String KEY_PREF_ERRORES_BBVA = "FicheroErroresBBVA"; //Mapeo de errores del servicio de BBVA a errores nuestros.
	private final String KEY_PREF_ERRORES_CAJARURAL = "FicheroErroresCajaRural"; //Mapeo de errores del servicio de Caja Rural a errores nuestros.
	private final String KEY_PREF_ERRORES_CAIXA = "FicheroErroresLaCaixa"; //Mapeo de errores del servicio de LaCaixa a errores nuestros.
	private final String KEY_PREF_PASARELAS = "FicheroSelectorPasarela"; //Selección de la pasarela en función de la tarjeta o cuenta
	private final String KEY_PREF_CAJASTUR = "FicheroPreferenciasCajastur"; //Preferencias de Cajastur
	private final String KEY_PREF_ID_CAJASTUR = "IdPasarelaCajastur"; //Identificador de Cajastur
	private final String KEY_PREF_ID_BBVA = "IdPasarelaBBVA"; //Identificador de bbva
	private final String KEY_PREF_ID_CAJARURAL = "IdPasarelaCajaRural"; //Identificador de Caja Rural
	private final String KEY_PREF_ID_CAIXA = "IdPasarelaLaCaixa"; //Identificador de LaCaixa
	private final String KEY_PREF_PASARELA_DEFECTO = "IdPasarelaPorDefecto"; //Identificador de la pasarela por defecto
	private final String KEY_PREF_BBVA = "FicheroPreferenciasBBVA"; //Preferencias de BBVA
	private final String KEY_PREF_CAJA_RURAL = "FicheroPreferenciasCajaRural"; //Preferencias de Caja Rural
	private final String KEY_PREF_CAIXA = "FicheroPreferenciasLaCaixa"; //Preferencias de LaCaixa
	
	// Firma
	private final String KEY_PREF_ENDPOINT_FIRMA = "EndPointFirma";
	//Validación de certificado
	private final String KEY_PREF_PROCPERMISO_SERVICIO = "PAPermisoServicio";
	private final String KEY_PREF_ALIAS_FIRMA = "AliasCertificadoFirma";
	private final String KEY_PREF_AUTENTICACION_CERT = "EndPointAutenticacionEPST";
	//Endpoint de generación de justificante de pago
	private final String KEY_PREF_ENDPOINT_JUST_PAGO = "EndpointJustificantePago";
	//Endpoint de consulta de documentos
	private final String KEY_PREF_ENDPOINT_CONSULTA_DOC = "EndpointConsultaDoc";
	//Para pago por UniversalPay
	private final String KEY_PREF_PROPS_UNIVERSAL_PAY = "FicheroPropiedadesUniversalPay"; 
	private final String KEY_PREF_PLATAFORMA_DEFECTO = "PlataformaPago_Defecto";
	private final String KEY_PREF_PA_VALIDAR_MAC = "PAValidarMAC";
	private final String KEY_PREF_PA_INICIO_OPERACION_PAGO = "PAInicioOperacionPago";
	private final String KEY_PREF_PA_GENERAR_MAC = "PAGenerarMAC";
	private final String KEY_PREF_PA_CONSULTA_INICIO_PAGO_TARJETA = "PAConsultaInicioPagoTarjeta";
	private final String KEY_PREF_PA_CONSULTA_INICIO_OPERACION_PAGO = "PAConsultaInicioOperacionPago";
	private final String KEY_PREF_PA_CONSULTA_PAGO_TARJETA = "PAConsultaPagoTarjetaBD";
	
	//CRUBENCVS 44613 05/04/2022
	private final String KEY_PREF_ID_UNICAJA = "IdPasarelaUnicaja"; //Identificador de Unicaja
	private final String KEY_PREF_UNICAJA = "FicheroPreferenciasUnicaja"; //Preferencias de Unicaja
	private final String KEY_PREF_ERRORES_UNICAJA = "FicheroErroresUnicaja"; // Mapeo de errores
	
	//CRUBENCVS 03/04/2023 47535
	private final String KEY_PREF_PA_GET_REGISTRO_PATE = "ProcAlmacenadoConsultaRegistroPate";
	private final String KEY_PREF_PA_INICIO_PAGO_TARJETA = "ProcAlmacenadoInicioPagoTarjeta";
	private final String KEY_PREF_PA_ACTUALIZAR_PATE    = "ProcAlmacenadoActualizarPate";

	private Preferencias() 
	{		
		try
		{
			cargarPreferencias();
		}
		catch (Exception e)
		{
			
		}
	}
	protected void cargarPreferencias() throws PreferenciasException
    {
		if(CompruebaFicheroPreferencias())
		{		       
	        FileInputStream inputStream=null;
			try {
				inputStream = new FileInputStream(DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
				Preferences.systemNodeForPackage(this.getClass()).importPreferences(inputStream);
	
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

			} finally {
				if (inputStream!=null){
					try {inputStream.close();} catch(Exception e){}
				}
			}
		}
    }
	
	private void InicializaTablaPreferencias()
	{
		//Logger.debug("Cargando tabla con preferencias por defecto");
		
		tablaPreferencias.clear();
		
		tablaPreferencias.put(KEY_PREF_USUARIO_APLICACION, 					"STPA");
		tablaPreferencias.put(KEY_PREF_ESQUEMA,								"EXPLOTACION");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_LANZADOR,					"");
		tablaPreferencias.put(KEY_PREF_LOG,							  		"ALL");
		tablaPreferencias.put(KEY_PREF_VALIDA_MAC,							"SI");
		tablaPreferencias.put(KEY_PREF_ERRORES_CAJA,						DIRECTORIO_PREFERENCIAS+"/mapeoErroresCaja.xml");
		tablaPreferencias.put(KEY_PREF_ERRORES_BD,							DIRECTORIO_PREFERENCIAS+"/mapeoErroresBD.xml");
		tablaPreferencias.put(KEY_PREF_PASARELAS,							DIRECTORIO_PREFERENCIAS+"/pasarelas.xml");
		tablaPreferencias.put(KEY_PREF_CAJASTUR,							DIRECTORIO_PREFERENCIAS+"/prefsCajastur.xml");
		tablaPreferencias.put(KEY_PREF_ID_CAJASTUR,							"cajastur");
		tablaPreferencias.put(KEY_PREF_ID_BBVA,								"bbva");
		tablaPreferencias.put(KEY_PREF_PASARELA_DEFECTO,					"cajastur");
		tablaPreferencias.put(KEY_PREF_ERRORES_BBVA,						DIRECTORIO_PREFERENCIAS+"/mapeoErroresBBVA.xml");
		tablaPreferencias.put(KEY_PREF_BBVA,								DIRECTORIO_PREFERENCIAS+"/prefsBBVA.xml");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_FIRMA,						"http://bus:7101/WSInternos/ProxyServices/PXFirmaDigital");
		//CRUBENCVS 14/09/2021
		tablaPreferencias.put(KEY_PREF_AUTENTICACION_CERT,					"http://bus:7101/WSAutenticacionLocalEPST/ProxyServices/PXAutenticacionEPST");
		tablaPreferencias.put(KEY_PREF_PROCPERMISO_SERVICIO,				"INTERNET.permisoServicio");
		tablaPreferencias.put(KEY_PREF_ALIAS_FIRMA,							"Tributas");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_JUST_PAGO,					"http://bus:7101/WSInternos/ProxyServices/PXDocumentosPago");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_CONSULTA_DOC,				"http://bus:7101/WSInternos/ProxyServices/PXConsultaDoinDocumentos");
		tablaPreferencias.put(KEY_PREF_ERRORES_CAJARURAL,					DIRECTORIO_PREFERENCIAS+"/mapeoErroresCajaRural.xml");
		tablaPreferencias.put(KEY_PREF_CAJA_RURAL,							DIRECTORIO_PREFERENCIAS+"/prefsCajaRural.xml");
		tablaPreferencias.put(KEY_PREF_ID_CAJARURAL,						"cajarural");
		tablaPreferencias.put(KEY_PREF_ERRORES_CAIXA,						DIRECTORIO_PREFERENCIAS+"/mapeoErroresLaCaixa.xml");
		tablaPreferencias.put(KEY_PREF_CAIXA,								DIRECTORIO_PREFERENCIAS+"/prefsLaCaixa.xml");
		tablaPreferencias.put(KEY_PREF_ID_CAIXA,							"lacaixa");
		//CRUBENCVS  04/03/2021. Pago con Universal_Pay
		tablaPreferencias.put(KEY_PREF_PROPS_UNIVERSAL_PAY,					DIRECTORIO_PREFERENCIAS+"/universalPay.prop");
		tablaPreferencias.put(KEY_PREF_PLATAFORMA_DEFECTO,					"UniversalPay");
		tablaPreferencias.put(KEY_PREF_PA_VALIDAR_MAC,						"SW_PASARELA2.valida_mac");
		tablaPreferencias.put(KEY_PREF_PA_INICIO_OPERACION_PAGO,			"SW_PASARELA2.inicio_operacion_pago");
		tablaPreferencias.put(KEY_PREF_PA_GENERAR_MAC,						"SW_PASARELA2.genera_mac");
		tablaPreferencias.put(KEY_PREF_PA_CONSULTA_INICIO_PAGO_TARJETA,		"SW_PASARELA2.consulta_inicio_pago_tarjeta");
		tablaPreferencias.put(KEY_PREF_PA_CONSULTA_INICIO_OPERACION_PAGO,	"SW_PASARELA2.consulta_inicio_operacion_pago");
		tablaPreferencias.put(KEY_PREF_PA_CONSULTA_PAGO_TARJETA,			"SW_PASARELA2.consulta_pago_tarjeta");
		//CRUBENCVS 44613 05/04/2022
		tablaPreferencias.put(KEY_PREF_ID_UNICAJA,							"unicaja");
		tablaPreferencias.put(KEY_PREF_UNICAJA,								DIRECTORIO_PREFERENCIAS+"/prefsUnicaja.xml");
		tablaPreferencias.put(KEY_PREF_ERRORES_UNICAJA,						DIRECTORIO_PREFERENCIAS+"/mapeoErroresUnicaja.xml");
		//CRUBENCVS 03/04/2023 47535
		tablaPreferencias.put(KEY_PREF_PA_GET_REGISTRO_PATE,				"SW_PASARELA2.get_registro_pate");
		tablaPreferencias.put(KEY_PREF_PA_INICIO_PAGO_TARJETA,				"SW_PASARELA2.iniciar_pago_tarjeta");
		tablaPreferencias.put(KEY_PREF_PA_ACTUALIZAR_PATE,					"SW_PASARELA2.actualizarPate");
		
		
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
    @SuppressWarnings("unchecked")
	private synchronized void CrearFicheroPreferencias()
    {
    	//Logger.debug("INICIO CREACION FICHERO PREFERENCIAS");
    	
        //preferencias por defecto
        m_preferencias = Preferences.systemNodeForPackage(this.getClass());
        
        InicializaTablaPreferencias();
        
        //recorremos la tabla cargada con las preferencias por defecto
        Iterator itr = tablaPreferencias.entrySet().iterator();
        while(itr.hasNext())
        {
        	Map.Entry<String, String> e = (Map.Entry)itr.next();
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
    
    private void setValueIntoTablaPreferencias(String key, String value)
    {
    	//Logger.debug("Se actualizara el valor de la preferencia '"+key+"' a '"+value+"'");
    	tablaPreferencias.put(key, value);
    }
	
    public static Preferencias getPreferencias () throws PreferenciasException
    {
    	return new Preferencias();
    }

	public String getUsuarioAplicacion() {
		return getValueFromTablaPreferencias(KEY_PREF_USUARIO_APLICACION);
	}
	public void setUsuarioAplicacion(String usuarioAplicacion) {
		setValueIntoTablaPreferencias(KEY_PREF_USUARIO_APLICACION, usuarioAplicacion);
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
	public String getValidaMac() {
		return getValueFromTablaPreferencias(KEY_PREF_VALIDA_MAC);
	}
	public void setValidaMac(String validaMac) {
		setValueIntoTablaPreferencias(KEY_PREF_VALIDA_MAC, validaMac);
	}
	public String getFicheroMapeoCaja() {
		return getValueFromTablaPreferencias(KEY_PREF_ERRORES_CAJA);
	}
	public String getFicheroMapeoBBVA() {
		return getValueFromTablaPreferencias(KEY_PREF_ERRORES_BBVA);
	}
	public String getFicheroMapeoBD() {
		return getValueFromTablaPreferencias(KEY_PREF_ERRORES_BD);
	}
	public String getFicheroPasarelas() {
		return getValueFromTablaPreferencias(KEY_PREF_PASARELAS);
	}
	public String getFicheroPreferenciasCajastur() {
		return getValueFromTablaPreferencias(KEY_PREF_CAJASTUR);
	}
	public String getIdPasarelaCajastur() {
		return getValueFromTablaPreferencias(KEY_PREF_ID_CAJASTUR);
	}
	public String getIdPasarelaBBVA() {
		return getValueFromTablaPreferencias(KEY_PREF_ID_BBVA);
	}
	public String getIdPasarelaPorDefecto() {
		return getValueFromTablaPreferencias(KEY_PREF_PASARELA_DEFECTO);
	}
	public String getFicheroPreferenciasBBVA() {
		return getValueFromTablaPreferencias(KEY_PREF_BBVA);
	}
	public String getEndPointFirma() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_FIRMA);
	}
	public void setEndPointFirma(String endpointFirma) {
		setValueIntoTablaPreferencias(KEY_PREF_ENDPOINT_FIRMA, endpointFirma);
	}
	public String getEndpointJustificantePago() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_JUST_PAGO);
	}
	public String getPAPermisoServicio() {
		return getValueFromTablaPreferencias(KEY_PREF_PROCPERMISO_SERVICIO);
	}
	public void setPAPermisoServicio(String pAPermisoServicio) {
		setValueIntoTablaPreferencias(KEY_PREF_PROCPERMISO_SERVICIO, pAPermisoServicio);
	}
	//CRUBENCVS 42479. 14/09/2021
	public String getEndpointAutenticacionCert() {
		return getValueFromTablaPreferencias(KEY_PREF_AUTENTICACION_CERT);
	}
	public String getAliasCertificadoFirma() {
		return getValueFromTablaPreferencias(KEY_PREF_ALIAS_FIRMA);
	}
	public String getEndpointConsultaDocumentos() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_CONSULTA_DOC);
	}
	public String getFicheroPreferenciasCajaRural() {
		return getValueFromTablaPreferencias(KEY_PREF_CAJA_RURAL);
	}
	public String getFicheroMapeoCajaRural() {
		return getValueFromTablaPreferencias(KEY_PREF_ERRORES_CAJARURAL);
	}
	public String getIdPasarelaCajaRural() {
		return getValueFromTablaPreferencias(KEY_PREF_ID_CAJARURAL);
	}
	public String getIdPasarelaLaCaixa() {
		return getValueFromTablaPreferencias(KEY_PREF_ID_CAIXA);
	}
	public String getFicheroPreferenciasLaCaixa() {
		return getValueFromTablaPreferencias(KEY_PREF_CAIXA);
	}
	public String getFicheroMapeoLaCaixa() {
		return getValueFromTablaPreferencias(KEY_PREF_ERRORES_CAIXA);
	}
	//CRUBENCVS 04/03/2021
	//Pago con Universal_Pay
	public String getFicheroPropiedadesUniversalPay() {
		return getValueFromTablaPreferencias(KEY_PREF_PROPS_UNIVERSAL_PAY);
	}
	
	public String getPlataformaPagoDefecto() {
		return getValueFromTablaPreferencias(KEY_PREF_PLATAFORMA_DEFECTO);
	}
	
	public String getPAValidarMac() {
		return getValueFromTablaPreferencias(KEY_PREF_PA_VALIDAR_MAC);
	}
	
	public String getPAInicioOperacionPago() {
		return getValueFromTablaPreferencias(KEY_PREF_PA_INICIO_OPERACION_PAGO);
	}
	
	public String getPAGenerarMAC() {
		return getValueFromTablaPreferencias(KEY_PREF_PA_GENERAR_MAC);
	}
	
	public String getPAConsultaInicioPagoTarjeta() {
		return getValueFromTablaPreferencias(KEY_PREF_PA_CONSULTA_INICIO_PAGO_TARJETA);
	}
	
	public String getPAConsultaInicioOperacionPago() {
		return getValueFromTablaPreferencias(KEY_PREF_PA_CONSULTA_INICIO_OPERACION_PAGO);
	}
	// CRUBENCVS 01/12/2021
	public String getPAConsultaPagoTarjeta() {
		return getValueFromTablaPreferencias(KEY_PREF_PA_CONSULTA_PAGO_TARJETA);
	}
	
	// CRUBENCVS 44613 05/04/2022
	
	public String getIdPasarelaUnicaja() {
		return getValueFromTablaPreferencias(KEY_PREF_ID_UNICAJA);
	}
	public String getFicheroPreferenciasUnicaja() {
		return getValueFromTablaPreferencias(KEY_PREF_UNICAJA);
	}
	public String getFicheroMapeoUnicaja() {
		return getValueFromTablaPreferencias(KEY_PREF_ERRORES_UNICAJA);
	}
	
	//CRUBENCVS 47535 03/04/2023
	public String getPAConsultaRegistroPate() {
		return getValueFromTablaPreferencias(KEY_PREF_PA_GET_REGISTRO_PATE);
	}
	
	public String getPAIniciarPagoTarjeta() {
		return getValueFromTablaPreferencias(KEY_PREF_PA_INICIO_PAGO_TARJETA);
	}
	
	public String getPAActualizarPate() {
		return getValueFromTablaPreferencias(KEY_PREF_PA_ACTUALIZAR_PATE);
	}
	
}
