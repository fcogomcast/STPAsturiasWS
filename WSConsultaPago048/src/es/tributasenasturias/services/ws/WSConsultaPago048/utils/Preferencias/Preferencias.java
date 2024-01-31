package es.tributasenasturias.services.ws.WSConsultaPago048.utils.Preferencias;

//Este paquete se comporta como singleton en acceso, es decir,
//sólo admite una instancia de la clase.
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.prefs.Preferences;

//TODO ¿Guardar preferencias?
public class Preferencias 
{
	//Se hace instancia privada y estática.
	static private Preferencias _pref = new Preferencias();
	private Preferences m_preferencias;

	private final static String FICHERO_PREFERENCIAS = "PreferenciasConsultaPago048.xml";
	private final static String DIRECTORIO_PREFERENCIAS = "WSConsultaPago048";
	private HashMap<String, String> tablaPreferencias = new HashMap<String, String>();

	//nombres de las preferencias

	private final static String KEY_PREF_NOM_PROCEDIMIENTO_VAL_AUTOL_Y_BAST = "NomProcedimientoValAutolYBast";
	private final static String KEY_PREF_NOM_PROCEDIMIENTO_VAL_AUTOL = "NomProcedimientoValAutol";
	private final static String KEY_PREF_ENDPOINT_ISL = "EndPointISL";
	private final static String KEY_PREF_LOG = "ModoLog";
	private final static String KEY_PREF_ENTIDAD = "Entidad";
	private final static String KEY_PREF_MODALIDAD = "Modalidad";
	private final static String KEY_PREF_ORIGEN = "Origen";
	private final static String KEY_PREF_ENDPOINT_PASARELA_PAGO_ST = "EndPointPasarelaPagoST";
	private final static String KEY_PREF_WSDL_LOCATION_PASARELA_PAGO_ST = "WSDLLocationPasarelaPagoST";
	private final static String KEY_PREF_ESQUEMA = "Esquema";
	private final static String KEY_PREF_IP_LANZADOR="IpLanzador";
	private final static String KEY_PREF_CLIENT_LOG_HANDLER_DIR = "ClientLogHandlerDir";
	private final static String KEY_PREF_CLIENT_LOG_HANDLER_FILE = "ClientLogHandlerFile";
	private final static String KEY_PREF_SERVER_LOG_HANDLER_DIR = "ServerLogHandlerDir";
	private final static String KEY_PREF_SERVER_LOG_HANDLER_FILE = "ServerLogHandlerFile";
	private final static String KEY_PREF_MSG_ERROR_VALIDACION_AUTOLIQUIDACION_Y_BASTIDOR = "MsgErrorValidacionAutoliquidacionYBastidor";
	private final static String KEY_PREF_COD_ERROR_VALIDACION_AUTOLIQUIDACION_Y_BASTIDOR = "CodErrorValidacionAutoliquidacionYBastidor";
	private final static String KEY_PREF_MSG_ERROR_NO_EXISTE_AUTOLIQUIDACION = "MsgErrorNoExisteAutoliquidacion";
	private final static String KEY_PREF_COD_ERROR_NO_EXISTE_AUTOLIQUIDACION = "CodErrorNoExisteAutoliquidacion";
	private final static String KEY_PREF_MSG_TRIBUTO_PAGADO = "MsgTributoPagado";
	private final static String KEY_PREF_COD_TRIBUTO_PAGADO = "CodTributoPagado";
	private final static String KEY_PREF_MSG_TRIBUTO_NO_PAGADO = "MsgTributoNoPagado";
	private final static String KEY_PREF_COD_TRIBUTO_NO_PAGADO = "CodTributoNoPagado";
	private final static String KEY_PREF_MSG_OTROS_ERRORES = "MsgOtrosErrores";
	private final static String KEY_PREF_COD_OTROS_ERRORES = "CodOtrosErrores";
	private final static String KEY_PREF_COD_PASARELA_TRIBUTO_PAGADO = "CodPasarelaTributoPagado";
	private final static String KEY_PREF_COD_PASARELA_TRIBUTO_NO_PAGADO = "CodPasarelaTributoNoPagado";


	private Preferencias() 
	{		
		try
		{
			CargarPreferencias();
		}
		catch (Exception e)
		{
			//Para comprobar posteriormente si se ha creado bien, se comprobará que la 
			//variable privada no es estática.
		}
	}
	protected synchronized void CargarPreferencias() throws Exception
	{
		if(CompruebaFicheroPreferencias())
		{		       
			//Logger.debug("INICIA CARGA DE PREFERENCIAS DESDE FICHERO");

			FileInputStream inputStream = new FileInputStream(DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
			Preferences.systemNodeForPackage(this.getClass()).importPreferences(inputStream);
			inputStream.close();
	        
	        //Logger.debug("Fichero importado");

			m_preferencias = Preferences.systemNodeForPackage(this.getClass());

			String[] keys = m_preferencias.keys();
			String msgKeys ="Leyendo las siguientes claves -> ";
			for(int i=0;i<keys.length;i++)
			{
				msgKeys += "["+keys[i]+"] ";
			}
	        //Logger.debug(msgKeys);

			for(int i=0;i<keys.length;i++)
			{
				String value = m_preferencias.get(keys[i], "");
	        	//Logger.debug("Cargando en la tabla ['"+keys[i]+"' - '"+value+"']");

				tablaPreferencias.put(keys[i], value);
			}
	        //Logger.debug("FIN CARGA DE PREFERENCIAS DESDE FICHERO");
		}
	}

	private void InicializaTablaPreferencias()
	{
		//Logger.debug("Cargando tabla con preferencias por defecto");

		tablaPreferencias.clear();
		tablaPreferencias.put(KEY_PREF_ENTIDAD, 							"331004");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_ISL,						"http://bus.desa.epst.pa:7001/WSInternos/ProxyServices/PXLanzador");
		tablaPreferencias.put(KEY_PREF_NOM_PROCEDIMIENTO_VAL_AUTOL_Y_BAST, 	"INTERNET_SIGA.validarautoliqybastidor");
		tablaPreferencias.put(KEY_PREF_NOM_PROCEDIMIENTO_VAL_AUTOL,         "INTERNET_SIGA.existeautoliquidacion");
		tablaPreferencias.put(KEY_PREF_LOG,							  		"ALL");
		tablaPreferencias.put(KEY_PREF_MODALIDAD,							"3");
		tablaPreferencias.put(KEY_PREF_ORIGEN,							  	"PT");
		tablaPreferencias.put(KEY_PREF_ENDPOINT_PASARELA_PAGO_ST,			"http://bus.desa.epst.pa:7001/PasarelaPagoST/PasarelaPagoST");
		tablaPreferencias.put(KEY_PREF_WSDL_LOCATION_PASARELA_PAGO_ST, 		"http://bus.desa.epst.pa:7001/PasarelaPagoST/PasarelaPagoST?wsdl");
		tablaPreferencias.put(KEY_PREF_ESQUEMA,								"PSEUDOREAL");
		tablaPreferencias.put(KEY_PREF_IP_LANZADOR,							"10.112.31.45");
		tablaPreferencias.put(KEY_PREF_CLIENT_LOG_HANDLER_DIR , 			"WSConsultaPago048");
		tablaPreferencias.put(KEY_PREF_CLIENT_LOG_HANDLER_FILE , 			"SOAP_CLIENT.log");
		tablaPreferencias.put(KEY_PREF_SERVER_LOG_HANDLER_DIR , 			"WSConsultaPago048");
		tablaPreferencias.put(KEY_PREF_SERVER_LOG_HANDLER_FILE , 			"SOAP_SERVER.log");
		tablaPreferencias.put(KEY_PREF_MSG_ERROR_VALIDACION_AUTOLIQUIDACION_Y_BASTIDOR, "Inconsistencia en los datos enviados.");
		tablaPreferencias.put(KEY_PREF_COD_ERROR_VALIDACION_AUTOLIQUIDACION_Y_BASTIDOR, "9997");
		tablaPreferencias.put(KEY_PREF_MSG_ERROR_NO_EXISTE_AUTOLIQUIDACION, "El tributo no se encuentra registrado en nuestro sistema.");
		tablaPreferencias.put(KEY_PREF_COD_ERROR_NO_EXISTE_AUTOLIQUIDACION, "9996");
		tablaPreferencias.put(KEY_PREF_COD_TRIBUTO_PAGADO, "0000");
		tablaPreferencias.put(KEY_PREF_MSG_TRIBUTO_PAGADO, "Tributo pagado.");
		tablaPreferencias.put(KEY_PREF_COD_TRIBUTO_NO_PAGADO, "9999");
		tablaPreferencias.put(KEY_PREF_MSG_TRIBUTO_NO_PAGADO, "El tributo no figura como pagado.");
		tablaPreferencias.put(KEY_PREF_COD_OTROS_ERRORES, "9998");
		tablaPreferencias.put(KEY_PREF_MSG_OTROS_ERRORES, "Se ha producido un error al realizar la consulta de pago.");
		tablaPreferencias.put(KEY_PREF_COD_PASARELA_TRIBUTO_PAGADO, "9991");
		tablaPreferencias.put(KEY_PREF_COD_PASARELA_TRIBUTO_NO_PAGADO, "9981");
		
	}

	private boolean CompruebaFicheroPreferencias()
	{
		boolean existeFichero = false;

		File f = new File(DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
		existeFichero = f.exists();
		if (existeFichero == false)
		{
        	//Logger.debug("El fichero de preferencias ("+DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS+") no existe!");
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
				if (fichero.mkdirs()==false)
				{
					throw new java.io.IOException ("No se puede crear el directorio de las preferencias.");
				}

			outputStream = new FileOutputStream(DIRECTORIO_PREFERENCIAS + "/" + FICHERO_PREFERENCIAS);
			m_preferencias.exportNode(outputStream);
            //Logger.debug("Fichero preferencias creado en disco");
		}
		catch (Exception e)
		{
//				logger.error(e.getMessage());
//				logger.trace(e.getStackTrace());
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
//					logger.error("Error cerrando fichero de preferencias -> " + e.getMessage());
//					logger.trace(e.getStackTrace());
            }
        }
        
        //Logger.debug("FIN CREACION FICHERO PREFERENCIAS");
	}

	public void recargaPreferencias() throws Exception
	{
    	//Logger.debug("Recarga del fichero de preferencias");

		CargarPreferencias();
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

	private synchronized void setValueIntoTablaPreferencias(String key, String value)
	{
    	//Logger.debug("Se actualizara el valor de la preferencia '"+key+"' a '"+value+"'");
		tablaPreferencias.put(key, value);
	}

	// Este método devolverá la instancia de clase.
	public synchronized static Preferencias getPreferencias () throws Exception
	{
		if (_pref==null)
		{
			throw new Exception("No se han podido recuperar las preferencias.");
		}
		_pref.CargarPreferencias();
		return _pref;
	}
	public String getEntidad() {
		return getValueFromTablaPreferencias(KEY_PREF_ENTIDAD);
	}
	public void setEntidad(String entidad) {
		setValueIntoTablaPreferencias(KEY_PREF_ENTIDAD, entidad);
	}
	public String getEsquemaBaseDatos() {
		return getValueFromTablaPreferencias(KEY_PREF_ESQUEMA);
	}
	public void setEsquemaBaseDatos(String esquema) {
		setValueIntoTablaPreferencias(KEY_PREF_ESQUEMA, esquema);
	}
	public String getNomProcedimientoValAutolYBastidor() {
		return getValueFromTablaPreferencias(KEY_PREF_NOM_PROCEDIMIENTO_VAL_AUTOL_Y_BAST);
	}
	public void setNomProcedimientoValAutolYBastidor(String nomProcedimiento) {
		setValueIntoTablaPreferencias(KEY_PREF_NOM_PROCEDIMIENTO_VAL_AUTOL_Y_BAST, nomProcedimiento);
	}
	public String getNomProcedimientoValAutol() {
		return getValueFromTablaPreferencias(KEY_PREF_NOM_PROCEDIMIENTO_VAL_AUTOL);
	}
	public void setNomProcedimientoValAutol(String nomProcedimiento) {
		setValueIntoTablaPreferencias(KEY_PREF_NOM_PROCEDIMIENTO_VAL_AUTOL, nomProcedimiento);
	}
	
	public String getEndPointISL() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_ISL);
	}
	public void setEndPointISL(String endPointISL) {
		setValueIntoTablaPreferencias(KEY_PREF_ENDPOINT_ISL, endPointISL);
	}
	public String getModoLog() {
		return getValueFromTablaPreferencias(KEY_PREF_LOG);
	}
	public void setModoLog(String modo) {
		setValueIntoTablaPreferencias(KEY_PREF_LOG, modo);
	}
	public String getEndPointPasarelaPagoST() {
		return getValueFromTablaPreferencias(KEY_PREF_ENDPOINT_PASARELA_PAGO_ST);
	}
	public void setEndPointPasarelaPagoST(String endPointPasarelaPagoST) {
		setValueIntoTablaPreferencias(KEY_PREF_ENDPOINT_PASARELA_PAGO_ST, endPointPasarelaPagoST);
	}
	public String getOrigen() {
		return getValueFromTablaPreferencias(KEY_PREF_ORIGEN);
	}
	public void setOrigen(String origen) {
		setValueIntoTablaPreferencias(KEY_PREF_ORIGEN, origen);
	}
	public String getModalidad() {
		return getValueFromTablaPreferencias(KEY_PREF_MODALIDAD);
	}
	public void setModalidad(String modalidad) {
		setValueIntoTablaPreferencias(KEY_PREF_MODALIDAD, modalidad);
	}

	
	public String getIPLanzador() {
		return getValueFromTablaPreferencias(KEY_PREF_IP_LANZADOR);
	}
	public void setIPLanzador(String ipLanzador) {
		setValueIntoTablaPreferencias(KEY_PREF_IP_LANZADOR, ipLanzador);
	}
	
	public String getWSDLLocationPasarelaPagoST() {
		return getValueFromTablaPreferencias(KEY_PREF_WSDL_LOCATION_PASARELA_PAGO_ST);
	}
	public void setWSDLLocationPasarelaPagoST(String wsdlLocationPasarelaPagoST) {
		setValueIntoTablaPreferencias(KEY_PREF_WSDL_LOCATION_PASARELA_PAGO_ST, wsdlLocationPasarelaPagoST);
	}
	
	public String getServerLogHandlerFile() {
		return getValueFromTablaPreferencias(KEY_PREF_SERVER_LOG_HANDLER_FILE);
	}
	public void setServerLogHandlerFile(String serverLogHandlerFile) {
		setValueIntoTablaPreferencias(KEY_PREF_SERVER_LOG_HANDLER_FILE, serverLogHandlerFile);
	}
	
	public String getServerLogHandlerDir() {
		return getValueFromTablaPreferencias(KEY_PREF_SERVER_LOG_HANDLER_DIR);
	}
	public void setServerLogHandlerDir(String serverLogHandlerDir) {
		setValueIntoTablaPreferencias(KEY_PREF_SERVER_LOG_HANDLER_DIR, serverLogHandlerDir);
	}

	public String getClientLogHandlerFile() {
		return getValueFromTablaPreferencias(KEY_PREF_CLIENT_LOG_HANDLER_FILE);
	}
	public void setClientLogHandlerFile(String clientLogHandlerFile) {
		setValueIntoTablaPreferencias(KEY_PREF_CLIENT_LOG_HANDLER_FILE, clientLogHandlerFile);
	}
	
	public String getClientLogHandlerDir() {
		return getValueFromTablaPreferencias(KEY_PREF_CLIENT_LOG_HANDLER_DIR);
	}
	public void setClientLogHandlerDir(String clientLogHandlerDir) {
		setValueIntoTablaPreferencias(KEY_PREF_CLIENT_LOG_HANDLER_DIR, clientLogHandlerDir);
	}
	
	public String getMsgErrorValidacionAutoliquidacionYBastidor() {
		return getValueFromTablaPreferencias(KEY_PREF_MSG_ERROR_VALIDACION_AUTOLIQUIDACION_Y_BASTIDOR);
	}
	public void setMsgErrorValidacionAutoliquidacionYBastidor(String msgError) {
		setValueIntoTablaPreferencias(KEY_PREF_MSG_ERROR_VALIDACION_AUTOLIQUIDACION_Y_BASTIDOR, msgError);
	}
	
	public String getCodErrorValidacionAutoliquidacionYBastidor() {
		return getValueFromTablaPreferencias(KEY_PREF_COD_ERROR_VALIDACION_AUTOLIQUIDACION_Y_BASTIDOR);
	}
	public void setCodErrorValidacionAutoliquidacionYBastidor(String codError) {
		setValueIntoTablaPreferencias(KEY_PREF_COD_ERROR_VALIDACION_AUTOLIQUIDACION_Y_BASTIDOR, codError);
	}
	
	public String getMsgErrorNoExisteAutoliquidacion() {
		return getValueFromTablaPreferencias(KEY_PREF_MSG_ERROR_NO_EXISTE_AUTOLIQUIDACION);
	}
	public void setMsgErrorNoExisteAutoliquidacion(String msgError) {
		setValueIntoTablaPreferencias(KEY_PREF_MSG_ERROR_NO_EXISTE_AUTOLIQUIDACION, msgError);
	}
	
	public String getCodErrorNoExisteAutoliquidacion() {
		return getValueFromTablaPreferencias(KEY_PREF_COD_ERROR_NO_EXISTE_AUTOLIQUIDACION);
	}
	public void setCodErrorNoExisteAutoliquidacion(String codError) {
		setValueIntoTablaPreferencias(KEY_PREF_COD_ERROR_NO_EXISTE_AUTOLIQUIDACION, codError);
	}
	
	public String getMsgOtrosErrores() {
		return getValueFromTablaPreferencias(KEY_PREF_MSG_OTROS_ERRORES);
	}
	public void setMsgOtrosErrores(String msgError) {
		setValueIntoTablaPreferencias(KEY_PREF_MSG_OTROS_ERRORES, msgError);
	}
	
	public String getCodOtrosErrores() {
		return getValueFromTablaPreferencias(KEY_PREF_COD_OTROS_ERRORES);
	}
	public void setCodOtrosErrores(String codError) {
		setValueIntoTablaPreferencias(KEY_PREF_COD_OTROS_ERRORES, codError);
	}
	
	public String getMsgTributoNoPagado() {
		return getValueFromTablaPreferencias(KEY_PREF_MSG_TRIBUTO_NO_PAGADO);
	}
	public void setMsgTributoNoPagado(String msg) {
		setValueIntoTablaPreferencias(KEY_PREF_MSG_TRIBUTO_NO_PAGADO, msg);
	}
	
	public String getCodTributoNoPagado() {
		return getValueFromTablaPreferencias(KEY_PREF_COD_TRIBUTO_NO_PAGADO);
	}
	public void setCodTributoNoPagado(String cod) {
		setValueIntoTablaPreferencias(KEY_PREF_COD_TRIBUTO_NO_PAGADO, cod);
	}

	public String getMsgTributoPagado() {
		return getValueFromTablaPreferencias(KEY_PREF_MSG_TRIBUTO_PAGADO);
	}
	public void setMsgTributoPagado(String msg) {
		setValueIntoTablaPreferencias(KEY_PREF_MSG_TRIBUTO_PAGADO, msg);
	}
	
	public String getCodTributoPagado() {
		return getValueFromTablaPreferencias(KEY_PREF_COD_TRIBUTO_PAGADO);
	}
	public void setCodTributoPagado(String cod) {
		setValueIntoTablaPreferencias(KEY_PREF_COD_TRIBUTO_PAGADO, cod);
	}
	
	public String getCodPasarelaTributoPagado() {
		return getValueFromTablaPreferencias(KEY_PREF_COD_PASARELA_TRIBUTO_PAGADO);
	}
	public void setCodPasarelaTributoPagado(String cod) {
		setValueIntoTablaPreferencias(KEY_PREF_COD_PASARELA_TRIBUTO_PAGADO, cod);
	}
	
	public String getCodPasarelaTributoNoPagado() {
		return getValueFromTablaPreferencias(KEY_PREF_COD_PASARELA_TRIBUTO_NO_PAGADO);
	}
	public void setCodPasarelaTributoNoPagado(String cod) {
		setValueIntoTablaPreferencias(KEY_PREF_COD_PASARELA_TRIBUTO_NO_PAGADO, cod);
	}

	



}

