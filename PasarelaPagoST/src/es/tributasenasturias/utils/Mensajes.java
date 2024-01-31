package es.tributasenasturias.utils;
// 
import java.util.HashMap;
import java.util.Map;
public final class Mensajes
{	

    private static HashMap<String,String> tablaMensajes = new HashMap<String,String>();
    private static HashMap<String,String> tablaExternos = new HashMap<String,String>();//Utilizada para mapear mensajes internos a externos.
	// El formato será: clave= código de mensaje interno, valor=código de mensaje externo.
    // Con este código externo buscaremos en la lista de mensajes externos el que hemos de mostrar.
    private static Map<String,String> listaMensExternos = new HashMap<String,String>();//mensajes externos.
	//Códigos de error
	private final static String KEY_0000 = "0000";
	private final static String KEY_0001 = "0001";
	private final static String KEY_9999 = "9999";
	private final static String KEY_9998 = "9998";
	private final static String KEY_9997 = "9997";
	private final static String KEY_9996 = "9996";
	private final static String KEY_9995 = "9995";
	private final static String KEY_9994 = "9994";
	private final static String KEY_9993 = "9993";
	private final static String KEY_9992 = "9992";
	private final static String KEY_9991 = "9991";
	private final static String KEY_9990 = "9990";
	private final static String KEY_9989 = "9989";
	private final static String KEY_9988 = "9988";
	private final static String KEY_9987 = "9987";
	private final static String KEY_9986 = "9986";
	private final static String KEY_9985 = "9985";
	private final static String KEY_9984 = "9984";
	private final static String KEY_9983 = "9983";
	private final static String KEY_9982 = "9982";
	private final static String KEY_9981 = "9981";
	private final static String KEY_9980 = "9980";
	private final static String KEY_9979 = "9979";
	private final static String KEY_9978 = "9978";
	private final static String KEY_9977 = "9977";
	private final static String KEY_9976 = "9976";
	private final static String KEY_9975 = "9975";
	private final static String KEY_9974 = "9974";
	private final static String KEY_9973 = "9973";
	private final static String KEY_9972 = "9972";
	private final static String KEY_9971 = "9971";
	private final static String KEY_9970 = "9970";
	private final static String KEY_9969 = "9969";
	private final static String KEY_9968 = "9968";
	private final static String KEY_9967 = "9967";
	private final static String KEY_9966 = "9966";
	private final static String KEY_9965 = "9965";
	private final static String KEY_9964 = "9964";
	private final static String KEY_9963 = "9963";
	private final static String KEY_9962 = "9962";
	private final static String KEY_9961 = "9961";
	private final static String KEY_9960 = "9960";
	private final static String KEY_9959 = "9959";
	private final static String KEY_9958 = "9958";
	private final static String KEY_9957 = "9957";
	private final static String KEY_9956 = "9956";
	private final static String KEY_9955 = "9955";
	private final static String KEY_9954 = "9954";
	private final static String KEY_9953 = "9953";
	//CRUBENCVS  01/12/2021
	private final static String KEY_9952 = "9952";
	//CRUBENCVS 47535 13/04/2023
	private final static String KEY_9951 = "9951";
	
	//03/05/2016. Añadido para poder mapear el error de tarjeta de pago cuando no tiene formato válido.
	private final static String KEY_8011 = "8011";
	// Códigos de mensaje externos.
	// Ahora, para pruebas habrá una correspondencia uno a uno.
	private final static String EXT_0000 = "0000";
	private final static String EXT_0001 = "0001";
	private final static String EXT_9999 = "9999";
	private final static String EXT_9998 = "9998";
	private final static String EXT_9997 = "9997";
	private final static String EXT_9996 = "9996";
	private final static String EXT_9995 = "9995";
	private final static String EXT_9994 = "9994";
	private final static String EXT_9993 = "9993";
	private final static String EXT_9992 = "9992";
	private final static String EXT_9991 = "9991";
	private final static String EXT_9990 = "9990";
	private final static String EXT_9989 = "9989";
	private final static String EXT_9988 = "9988";
	private final static String EXT_9987 = "9987";
	private final static String EXT_9986 = "9986";
	private final static String EXT_9985 = "9985";
	private final static String EXT_9984 = "9984";
	private final static String EXT_9983 = "9983";
	private final static String EXT_9982 = "9982";
	private final static String EXT_9981 = "9981";
	private final static String EXT_9980 = "9980";
	private final static String EXT_9979 = "9979";
	private final static String EXT_9978 = "9978";
	private final static String EXT_9977 = "9977";
	private final static String EXT_9976 = "9976";
	private final static String EXT_9975 = "9975";
	private final static String EXT_9974 = "9974";
	private final static String EXT_9973 = "9973";
	private final static String EXT_9972 = "9972";
	private final static String EXT_9971 = "9971";
	private final static String EXT_9970 = "9970";
	private final static String EXT_9969 = "9969";
	private final static String EXT_9968 = "9968";
	private final static String EXT_9967 = "9967";
	private final static String EXT_9966 = "9966";
	private final static String EXT_9965 = "9965";
	private final static String EXT_9964 = "9964";
	private final static String EXT_9963 = "9963";
	private final static String EXT_9962 = "9962";
	private final static String EXT_9961 = "9961";
	private final static String EXT_9960 = "9960";
	private final static String EXT_9959 = "9959";
	private final static String EXT_9958 = "9958";
	private final static String EXT_9957 = "9957";
	private final static String EXT_9956 = "9956";
	private final static String EXT_9955 = "9955";
	private final static String EXT_9954 = "9954";
	private final static String EXT_9953 = "9953";
	//CRUBENCVS 01/12/2021
	private final static String EXT_9952 = "9952";
	
	//CRUBENCVS 47535
	private final static String EXT_9951 = "9951";
	
	private final static String EXT_8011 = "8011";
	static
	{
		inicializaTablaErrores();
	}
	
	private Mensajes() 
	{		
		try
		{
			inicializaTablaErrores();
		}
		catch (Exception e)
		{
			
		}
	}
	
	private static void inicializaTablaErrores()
	{
		
		tablaMensajes.clear();
		
		tablaMensajes.put(KEY_0000,"Operacion realizada correctamente.");
		tablaMensajes.put(KEY_0001,"Operacion realizada correctamente.");
		tablaMensajes.put(KEY_9999,"Error no controlado.");
		tablaMensajes.put(KEY_9998,"Se han recibido datos obligatorios vacios [modalidad].");
		tablaMensajes.put(KEY_9997,"Se han recibido datos obligatorios vacios [numero_autoliquidacion].");
		tablaMensajes.put(KEY_9996,"Se han recibido datos obligatorios vacios [identificacion y/o referencia].");
		tablaMensajes.put(KEY_9995,"Error en acceso a base de datos.");
		tablaMensajes.put(KEY_9994,"Error de formateo de datos.");
		tablaMensajes.put(KEY_9993,"Error devuelto en la consulta del pago en Entidad Remota.");
		tablaMensajes.put(KEY_9992,"No se ha podido realizar la actualizacion en la base de datos.");
		tablaMensajes.put(KEY_9991,"Tributo Pagado.");
		tablaMensajes.put(KEY_9990,"El pago del tributo se ha cancelado.");
		tablaMensajes.put(KEY_9989,"Se han recibido datos obligatorios vacios [origen].");
		tablaMensajes.put(KEY_9988,"Se han recibido datos obligatorios vacios [aplicacion/numero unico].");
		tablaMensajes.put(KEY_9987,"El NIF no tiene un formato de NIF/NIE/CIF valido.");
		tablaMensajes.put(KEY_9986,"Error en el proceso en la base de datos.");
		tablaMensajes.put(KEY_9985,"Datos inconsistentes.");
		tablaMensajes.put(KEY_9984,"No se ha podido realizar el pago en la entidad remota.");
		tablaMensajes.put(KEY_9983,"El tipo de origen es incorrecto.");
		tablaMensajes.put(KEY_9982,"Se ha excedido el margen de tiempo entre el pago del tributo y su anulacion.");
		tablaMensajes.put(KEY_9981,"El tributo no figura como pagado.");
		tablaMensajes.put(KEY_9980,"Error devuelto en la anulacion del pago en Entidad Remota.");
		tablaMensajes.put(KEY_9979,"Se han recibido datos obligatorios vacios [fecha devengo]-[dato especifico].");
		tablaMensajes.put(KEY_9978,"El servicio solo da soporte a modelos 046 para el origen indicado.");
		tablaMensajes.put(KEY_9977,"Se han recibido datos obligatorios vacios [mac].");
		tablaMensajes.put(KEY_9976,"Error al generar MAC de respuesta.");
		tablaMensajes.put(KEY_9975,"Error al generar MAC para llamada al servicio web de consulta de cobros.");
		tablaMensajes.put(KEY_9974,"La modalidad ha de ser \"2\" o \"3\".");
		tablaMensajes.put(KEY_9973,"Para el servicio Web no ha de informarse el Codigo Cuenta Cliente.");
		tablaMensajes.put(KEY_9972,"Se han recibido datos obligatorios vacios [tarjeta/fecha de caducidad]");
		tablaMensajes.put(KEY_9971,"El importe no es correcto.");
		tablaMensajes.put(KEY_9970,"Se han recibido datos obligatorios vacios [emisora].");
		tablaMensajes.put(KEY_9969,"Se han recibido datos obligatorios vacios [nif operante].");
		tablaMensajes.put(KEY_9968,"Ha de indicarse o bien [tarjeta, fecha de caducidad] o bien [ccc].");
		tablaMensajes.put(KEY_9967,"Ha de indicarse o bien [nif] o bien [nombre de contribuyente].");
		tablaMensajes.put(KEY_9966,"La modalidad en servicio Web ha de ser \"3\".");
		tablaMensajes.put(KEY_9965,"Se han recibido datos obligatorios vacios [cliente].");
		tablaMensajes.put(KEY_9964,"No ha de indicarse [aplicacion/numero unico].");
		tablaMensajes.put(KEY_9963,"Tributo no Pagado.");
		tablaMensajes.put(KEY_9962,"Con modalidad \"2\" no ha de indicarse el numero de autoliquidacion.");
		tablaMensajes.put(KEY_9961,"Con modalidad \"2\" no ha de indicarse [fecha devengo/dato especifico].");
		tablaMensajes.put(KEY_9960,"Error al iniciar el proceso de anulación en la base de datos.");
		tablaMensajes.put(KEY_9959,"El estado del registro en la base de datos es inconsistente.");
		tablaMensajes.put(KEY_9958,"Se han recibido datos obligatorios vacios [entidad]");
		tablaMensajes.put(KEY_9957,"En la consulta se ha de indicar [aplicacion/numero_unico] o [numero_autoliquidacion] o [identificacion/referencia].");
		tablaMensajes.put(KEY_9956,"La Fecha de devengo no tiene el formado DDMMAAAA");
		tablaMensajes.put(KEY_9955,"Error al recuperar el token de petición en la plataforma de pago");
		tablaMensajes.put(KEY_9954,"No se ha indicado ninguna plataforma de pago");
		tablaMensajes.put(KEY_9953,"Error en la anulación del tributo.");
		//CRUBENCVS 01/12/2021. Para diferenciar cuando realmente no hay datos en PATE para la búsqueda de pago por tarjeta.
		tablaMensajes.put(KEY_9952,"No hay datos para esos criterios de busqueda.");
		tablaMensajes.put(KEY_8011,"Error en la tarjeta de pago.");
		//CRUBENCVS 13/04/2023
		tablaMensajes.put(KEY_9951, "Error al iniciar el pago por tarjeta");
		// Externos.
		tablaExternos.put(KEY_0000,EXT_0000);
		tablaExternos.put(KEY_0001,EXT_0001);
		tablaExternos.put(KEY_9999,EXT_9999);
		tablaExternos.put(KEY_9998,EXT_9998);
		tablaExternos.put(KEY_9997,EXT_9997);
		tablaExternos.put(KEY_9996,EXT_9996);
		tablaExternos.put(KEY_9995,EXT_9995);
		tablaExternos.put(KEY_9994,EXT_9994);
		tablaExternos.put(KEY_9993,EXT_9993);
		tablaExternos.put(KEY_9992,EXT_9992);
		tablaExternos.put(KEY_9991,EXT_9991);
		tablaExternos.put(KEY_9990,EXT_9990);
		tablaExternos.put(KEY_9989,EXT_9989);
		tablaExternos.put(KEY_9988,EXT_9988);
		tablaExternos.put(KEY_9987,EXT_9987);
		tablaExternos.put(KEY_9986,EXT_9986);
		tablaExternos.put(KEY_9985,EXT_9985);
		tablaExternos.put(KEY_9984,EXT_9984);
		tablaExternos.put(KEY_9983,EXT_9983);
		tablaExternos.put(KEY_9982,EXT_9982);
		tablaExternos.put(KEY_9981,EXT_9981);
		tablaExternos.put(KEY_9980,EXT_9980);
		tablaExternos.put(KEY_9979,EXT_9979);
		tablaExternos.put(KEY_9978,EXT_9978);
		tablaExternos.put(KEY_9977,EXT_9977);
		tablaExternos.put(KEY_9976,EXT_9976);
		tablaExternos.put(KEY_9975,EXT_9975);
		tablaExternos.put(KEY_9974,EXT_9974);
		tablaExternos.put(KEY_9973,EXT_9973);
		tablaExternos.put(KEY_9972,EXT_9972);
		tablaExternos.put(KEY_9971,EXT_9971);
		tablaExternos.put(KEY_9970,EXT_9970);
		tablaExternos.put(KEY_9969,EXT_9969);
		tablaExternos.put(KEY_9968,EXT_9968);
		tablaExternos.put(KEY_9967,EXT_9967);
		tablaExternos.put(KEY_9966,EXT_9966);
		tablaExternos.put(KEY_9965,EXT_9965);
		tablaExternos.put(KEY_9964,EXT_9964);
		tablaExternos.put(KEY_9963,EXT_9963);
		tablaExternos.put(KEY_9962,EXT_9962);
		tablaExternos.put(KEY_9961,EXT_9961);
		tablaExternos.put(KEY_9960,EXT_9960);
		tablaExternos.put(KEY_9959,EXT_9959);
		tablaExternos.put(KEY_9958,EXT_9958);
		tablaExternos.put(KEY_9957,EXT_9957);
		tablaExternos.put(KEY_9956,EXT_9956);
		tablaExternos.put(KEY_9955,EXT_9955);
		tablaExternos.put(KEY_9954,EXT_9954);
		tablaExternos.put(KEY_9953,EXT_9953);
		tablaExternos.put(KEY_9952,EXT_9952);
		//CRUBENCVS 47535  13/04/2023
		tablaExternos.put(KEY_9951,EXT_9951);
		
		tablaExternos.put(KEY_8011,EXT_8011);
		

		// Mensajes externos.
		listaMensExternos.put(EXT_0000,"Operacion realizada correctamente.");
		listaMensExternos.put(EXT_0001,"Operacion realizada correctamente.");
		listaMensExternos.put(EXT_9999,"Error no controlado.");
		listaMensExternos.put(EXT_9998,"Se han recibido datos obligatorios vacios [modalidad].");
		listaMensExternos.put(EXT_9997,"Se han recibido datos obligatorios vacios [numero_autoliquidacion].");
		listaMensExternos.put(EXT_9996,"Se han recibido datos obligatorios vacios [identificacion y/o referencia].");
		listaMensExternos.put(EXT_9995,"Error en acceso a base de datos.");
		listaMensExternos.put(EXT_9994,"Error de formateo de datos.");
		listaMensExternos.put(EXT_9993,"Error devuelto en la consulta del pago en Entidad Remota.");
		listaMensExternos.put(EXT_9992,"No se ha podido realizar la actualizacion en la base de datos.");
		listaMensExternos.put(EXT_9991,"Tributo Pagado.");
		listaMensExternos.put(EXT_9990,"El pago del tributo se ha cancelado.");
		listaMensExternos.put(EXT_9989,"Se han recibido datos obligatorios vacios [origen].");
		listaMensExternos.put(EXT_9988,"Se han recibido datos obligatorios vacios [aplicacion/numero unico].");
		listaMensExternos.put(EXT_9987,"El NIF no tiene un formato de NIF/NIE/CIF valido.");
		listaMensExternos.put(EXT_9986,"Error en el proceso en la base de datos.");
		listaMensExternos.put(EXT_9985,"La operación se encuentra registrada con otros datos, no se puede continuar.");
		listaMensExternos.put(EXT_9984,"No se ha podido realizar el pago en la entidad remota.");
		listaMensExternos.put(EXT_9983,"El tipo de origen es incorrecto.");
		listaMensExternos.put(EXT_9982,"Se ha excedido el margen de tiempo entre el pago del tributo y su anulacion.");
		listaMensExternos.put(EXT_9981,"El tributo no figura como pagado.");
		listaMensExternos.put(EXT_9980,"Error devuelto en la anulacion del pago en Entidad Remota.");
		listaMensExternos.put(EXT_9979,"Se han recibido datos obligatorios vacios [fecha devengo]-[dato especifico].");
		listaMensExternos.put(EXT_9978,"El servicio solo da soporte a modelos 046 para el origen indicado.");
		listaMensExternos.put(EXT_9977,"Se han recibido datos obligatorios vacios [mac].");
		listaMensExternos.put(EXT_9976,"Error al generar MAC de respuesta.");
		listaMensExternos.put(EXT_9975,"Error al generar MAC para llamada al servicio web de consulta de cobros.");
		listaMensExternos.put(EXT_9974,"La modalidad ha de ser \"2\" o \"3\".");
		listaMensExternos.put(EXT_9973,"Para el servicio Web no ha de informarse el Codigo Cuenta Cliente.");
		listaMensExternos.put(EXT_9972,"Se han recibido datos obligatorios vacios [tarjeta/fecha de caducidad].");
		listaMensExternos.put(EXT_9971,"El importe no es correcto.");
		listaMensExternos.put(EXT_9970,"Se han recibido datos obligatorios vacios [emisora].");
		listaMensExternos.put(EXT_9969,"Se han recibido datos obligatorios vacios [nif operante].");
		listaMensExternos.put(EXT_9968,"Ha de indicarse o bien [tarjeta, fecha de caducidad] o bien [ccc].");
		listaMensExternos.put(EXT_9967,"Ha de indicarse o bien [nif] o bien [nombre de contribuyente].");
		listaMensExternos.put(EXT_9966,"La modalidad en servicio Web ha de ser \"3\".");
		listaMensExternos.put(EXT_9965,"Se han recibido datos obligatorios vacios [cliente].");
		listaMensExternos.put(EXT_9964,"No ha de indicarse [aplicacion/numero unico].");
		listaMensExternos.put(EXT_9963,"Tributo no Pagado.");
		listaMensExternos.put(EXT_9962,"Con modalidad \"2\" no ha de indicarse el numero de autoliquidacion.");
		listaMensExternos.put(EXT_9961,"Con modalidad \"2\" no ha de indicarse [fecha devengo/dato especifico].");
		listaMensExternos.put(EXT_9960,"Error al iniciar el proceso de anulación en la base de datos.");
		listaMensExternos.put(EXT_9959,"El estado del registro en la base de datos es inconsistente.");
		listaMensExternos.put(EXT_9958,"Se han recibido datos obligatorios vacios [entidad]");
		listaMensExternos.put(EXT_9957,"En la consulta se ha de indicar [aplicacion/numero_unico] o [numero_autoliquidacion] o [identificacion/referencia].");
		listaMensExternos.put(EXT_9956,"La Fecha de devengo no tiene el formato DDMMAAAA");
		listaMensExternos.put(EXT_9955,"Error al recuperar el token de petición en la plataforma de pago");
		listaMensExternos.put(EXT_9954,"No se ha indicado ninguna plataforma de pago");
		listaMensExternos.put(EXT_9953,"Error en la anulación del tributo.");
		//CRUBENCVS 01/12/2021
		listaMensExternos.put(EXT_9952,"No hay datos para ese criterio de búsqueda.");
		listaMensExternos.put( EXT_8011,"Error en la tarjeta de pago.");
		//CRUBENCVS 47535 
		listaMensExternos.put(EXT_9951,"Error al iniciar el pago por tarjeta");
	}
	
    
    private static String getValueFromTablaErrores(String key)
    {
    	String toReturn="";
    	
    	if(tablaMensajes.containsKey(key))
    	{
    		toReturn = key;
    	}
    	return toReturn;
    }
    /**
     * Recupera la clave de mensaje externo a partir de una clave de mensaje interno.
     * @param internalKey la clave interna.
     * @return
     */
    private static String getExternalKey(String internalKey)
    {
    	String toReturn="";
    	
    	if(tablaExternos.containsKey(internalKey))
    	{
    		toReturn = tablaExternos.get(internalKey);
    	}
    	return toReturn;
    }
    
	public static String getOk() 
	{
		return getValueFromTablaErrores(KEY_0000);
	}
	public static String getTributoPagadoYa() 
	{
		return getValueFromTablaErrores(KEY_0001);
	}
	public static String getFatalError() 
	{
		return getValueFromTablaErrores(KEY_9999);
	}
	public static String getModalidadVacia() 
	{
		return getValueFromTablaErrores(KEY_9998);
	}
	public static String getJustificanteVacio() 
	{
		return getValueFromTablaErrores(KEY_9997);
	}
	public static String getIdenRefVacio() 
	{
		return getValueFromTablaErrores(KEY_9996);
	}
	public static String getErrorBD() 
	{
		return getValueFromTablaErrores(KEY_9995);
	}
	public static String getErrorFormateo() 
	{
		return getValueFromTablaErrores(KEY_9994);
	}
	public static String getErrorConsulta() 
	{
		return getValueFromTablaErrores(KEY_9993);
	}
	public static String getErrorActualizacionBD() 
	{
		return getValueFromTablaErrores(KEY_9992);
	}
	public static String getTributoPagado() 
	{
		return getValueFromTablaErrores(KEY_9991);
	}
	public static String getTributoAnulado() 
	{
		return getValueFromTablaErrores(KEY_9990);
	}
	public static String getOrigenVacio() 
	{
		return getValueFromTablaErrores(KEY_9989);
	}
	public static String getAplicacionNumVacio() 
	{
		return getValueFromTablaErrores(KEY_9988);
	}
	public static String getNIFInvalido() 
	{
		return getValueFromTablaErrores(KEY_9987);
	}
	public static String getErrorPeticionBD() 
	{
		return getValueFromTablaErrores(KEY_9986);
	}
	public static String getErrorDatosInconsistentes() 
	{
		return getValueFromTablaErrores(KEY_9985);
	}
	public static String getErrorPagoEntidadRemota() 
	{
		return getValueFromTablaErrores(KEY_9984);
	}
	public static String getOrigenInvalido() 
	{
		return getValueFromTablaErrores(KEY_9983);
	}
	public static String getErrorMargenAnulacion() 
	{
		return getValueFromTablaErrores(KEY_9982);
	}
	public static String getTributoNoPagado() 
	{
		return getValueFromTablaErrores(KEY_9981);
	}
	public static String getErrorAnulacionPago() 
	{
		return getValueFromTablaErrores(KEY_9980);
	}
	public static String getErrorDevengoEspecificoVacio() 
	{
		return getValueFromTablaErrores(KEY_9979);
	}
	public static String getErrorModelo046() 
	{
		return getValueFromTablaErrores(KEY_9978);
	}
	public static String getErrorMacVacia() 
	{
		return getValueFromTablaErrores(KEY_9977);
	}
	public static String getErrorMacGeneracion() 
	{
		return getValueFromTablaErrores(KEY_9976);
	}
	public static String getErrorMacConsulta() 
	{
		return getValueFromTablaErrores(KEY_9975);
	}
	public static String getErrorModalidadErronea() 
	{
		return getValueFromTablaErrores(KEY_9974);
	}
	public static String getErrorCccSw() 
	{
		return getValueFromTablaErrores(KEY_9973);
	}
	public static String getErrorTarjetaFechaVacia() 
	{
		return getValueFromTablaErrores(KEY_9972);
	}
	public static String getErrorImporte() 
	{
		return getValueFromTablaErrores(KEY_9971);
	}
	public static String getErrorEmisoraVacia() 
	{
		return getValueFromTablaErrores(KEY_9970);
	}
	public static String getErrorNifOperanteVacio() 
	{
		return getValueFromTablaErrores(KEY_9969);
	}
	public static String getErrorTarjetaCCC() 
	{
		return getValueFromTablaErrores(KEY_9968);
	}
	public static String getErrorNifNombre() 
	{
		return getValueFromTablaErrores(KEY_9967);
	}
	public static String getErrorModalidadSW() 
	{
		return getValueFromTablaErrores(KEY_9966);
	}
	public static String getClienteVacio() 
	{
		return getValueFromTablaErrores(KEY_9965);
	}
	public static String getAplNumUnicoInnecesario() 
	{
		return getValueFromTablaErrores(KEY_9964);
	}
	public static String getJustificanteInnecesario() 
	{
		return getValueFromTablaErrores(KEY_9962);
	}
	public static String getDevengoEspecificoInnecesario() 
	{
		return getValueFromTablaErrores(KEY_9961);
	}
	public static String getErrorInicioAnulacion() 
	{
		return getValueFromTablaErrores(KEY_9960);
	}
	public static String getErrorEstadoPATE() 
	{
		return getValueFromTablaErrores(KEY_9959);
	}
	public static String getEntidadVacia() 
	{
		return getValueFromTablaErrores(KEY_9958);
	}
	
	public static String getErrorCriterioConsulta() 
	{
		return getValueFromTablaErrores(KEY_9957);
	}
	
	public static String getErrorFormatoFechaDevengo(){
		return getValueFromTablaErrores(KEY_9956);
	}
	
	public static String getErrorTokenRequest(){
		return getValueFromTablaErrores(KEY_9955);
	}
	
	public static String getErrorNoPlataforma(){
		return getValueFromTablaErrores(KEY_9954);
	}
	
	public static String getErrorAnulacion(){
		return getValueFromTablaErrores(KEY_9953);
	}
	
	public static String getNoHayDatosConsulta(){
		return getValueFromTablaErrores(KEY_9952);
	}
	//Añadido para controlar el error de formato de tarjeta inválido.
	public static String getErrorTarjetaPago()
	{
		return getValueFromTablaErrores(KEY_8011);
	}
	
	//CRUBENCVS 47535 
	public static String getErrorInicioPagoTarjeta()
	{
		return getValueFromTablaErrores(KEY_9951);
	}
	
	//FIN CRUBENCVS 47535
	/** Texto de mensaje externo
	 * 
	 * @param key
	 * @return
	 */
	public static String getExternalText(String internalKey)
	{
		String toReturn="";
		String externalKey=getExternalKey(internalKey);
    	
    	if(listaMensExternos.containsKey(externalKey))
    	{
    		toReturn = listaMensExternos.get(externalKey);
    	}
    	return toReturn;
	}
	/** Texto de mensaje interno
	 * 
	 * @param key
	 * @return
	 */
	public static String getText(String key)
	{
		String toReturn="";
    	
    	if(tablaMensajes.containsKey(key))
    	{
    		toReturn = tablaMensajes.get(key);
    	}
    	return toReturn;
	}
	
}
