package es.tributasenasturias.utils;

import es.tributasenasturias.webservices.context.CallContextConstants;

/**
 * Clase de constantes.
 * 
 *
 */
public class Constantes {
	private Constantes(){};
	final static private String errorCode = "9999"; // Código de error utilizado en la función.
	final static private String okCode = "0000";
	final static private String errorGenericoPago = "Error inesperado al procesar su pago telemático. El pago no puede realizarse en este momento.";
	final static private String errorGenericoConsulta = "Error inesperado al procesar su consulta. La consulta no puede realizarse en este momento.";
	final static private String errorGenericoAnulacion = "Error inesperado al procesar su anulación de pago telemático. La anulación no puede realizarse en este momento.";
	final static private String prefijoNIF="N"; // Prefijo a poner a los NIF enviados a Entidad de Pago.
	final static private String prefijoCIF="C"; // Prefijo a poner a los CIF enviados a Entidad de Pago.
	final static private String prefijoNIE="R"; // Prefijo a poner a los NIE enviados a Entidad de Pago.
	final static private String esquemaPruebas="PSEUDOREAL"; // Esquema de pruebas. En este alguna lógica, como la de validación de MAC, funcionará diferente.
	final static private String estadoAnulado="ANULADO";
	final static private String modalidadLiquidacion="2";
	final static private String modalidadAutoliquidacion="3";
	final static private String origenSW="SW";
	final static private String origenPT="PT";
	final static private String origenS1="S1";
	final static private String noPagadoLiberbank="NAA0016";//Código de error de Entidad de Pago que consideramos como tributo no pagado.
	final static private String noDatosUnicaja="NAA0016";//Código de "No hay datos" en Unicaja
	final static private String literalTarjeta="TARJETA"; //Literal de pago con tarjeta desde Entidad de PAgo
	final static private String literalCCC = "CUENTA DE CARGO"; // Literal de pago con cuenta corriente desde Entidad de Pago.
	final static private String SI = "SI"; // Literal de SI
	final static private String NO = "NO"; // Literal de NO
	final static private int longCodigoRespNormal=7;
	final static private int longColumnaRespuesta=10;
	final static private String caracterSeparacionResultado="-";
	//Constantes de contexto, de SOAP y de llamada.
	public static final String PREFERENCIAS=CallContextConstants.PREFERENCIAS;
	public static final String LOG_APLICACION=CallContextConstants.LOG_APLICACION;
	public static final String ID_SESION=CallContextConstants.ID_SESION;
	public static final String ESTADO_PAGADO = "PAGADO";
	public static final String ESTADO_ANULADO= "ANULADO";
	public static final String ESTADO_ERROR = "ERROR";
	public static final String CODIGO_NO_PAGADO_BBVA = "71";
	public static final String OK_BBVA = "00";
	public static final String ERROR_SOAP_FAULT_BBVA = "99999";
	public static final String ERROR_SOAP_FAULT_CAIXA = "99999";
	public static final String OK_CRURAL = "0";
	public static final String CODIGO_NO_PAGADO_CRURAL = "0000071";
	public static final String ERROR_SOAP_FAULT_CRURAL = "9999";
	public static final String CONSULTA_OK_CRURAL="OK";
	public static final String ANULACION_OK_CRURAL="OK";
	public static final String ANULACION_OK_CAIXA="OK";
	public static final String CONSULTA_OK_CAIXA="OK";
	public static final String CODIGO_NO_PAGADO_CAIXA = "999990";
	public static final String OK_CAIXA = "OK";
	
	
	public enum ModalidadOrigen {SW3,PT2,PT3,S12,S13};//modalidades en base al origen de entrada a los servicios
	/**
	 * SW3 -> Servicio Web, modalidad 3
	 * PT2 -> Portal Tributario, modalidad 2
	 * PT3 -> Portal Tributario, modalidad 3
	 * S12  -> Servicio 1, modalidad 2
	 * S13  -> Servicio 1, modalidad 3 
	 */
	public enum Modalidad {M2,M3} // Modalidades posibles
	/**
	 * M2 -> Modalidad 2
	 * M2 -> Modalidad 3
	 */
	public enum EstadoAnulacion {A,B,C} // Estados que puede devolver una pasarela en la anulación
	/**
	 * ANULADO  -> Cobrado
	 * B  -> Anulado
	 * C  -> Liquidado
	 */
	/**
	 * Método que devuelve la constante de código de operación fallida genérico.
	 * @return constante de código de operación fallida genérico.
	 */
	public static String getErrorCode() {
		return errorCode;
	}
	/**
	 * Método que devuelve la constante de código de operación válida.
	 * @return Constante de código de operación válida.
	 */
	public static String getOkCode() {
		return okCode;
	} 
	/**
	 * Método que devuelve la cadena de descripción de error genérico en el pago.
	 * @return Cadena de descripción de error genérico en el pago
	 */
	public static String getErrorGenericoPago() {
		return errorGenericoPago;
	}
	/**
	 * Método que devuelve la cadena de descripción de error genérico en la consulta.
	 * @return Cadena de descripción de error genérico en el pago
	 */
	public static String getErrorGenericoConsulta() {
		return errorGenericoConsulta;
	}
	/**
	 * Método que devuelve la cadena de descripción de error genérico en la anulación de pago.
	 * @return Cadena de descripción de error genérico en el pago
	 */
	public static String getErrorGenericoAnulacion() {
		return errorGenericoAnulacion;
	}
	/**
	 * Método que devuelve el prefijo a concatenar a los NIF enviados a Entidad de Pago.
	 * @return Cadena de prefijo de NIF
	 */
	public static String getPrefijoNIF() {
		return prefijoNIF;
	}
	/**
	 * Método que devuelve el prefijo a concatenar a los CIF enviados a Entidad de Pago.
	 * @return Cadena de prefijo de CIF
	 */
	public static String getPrefijoCIF() {
		return prefijoCIF;
	}
	/**
	 * Método que devuelve el prefijo a concatenar a los NIE enviados a Entidad de Pago.
	 * @return Cadena de prefijo de NIE
	 */
	public static String getPrefijoNIE() {
		return prefijoNIE;
	}
	/**
	 * Método que devuelve el nombre de esquema de pruebas en el que cierta funcionalidad será diferente, como validaciones, etc.
	 * @return Cadena de esquema de pruebas.
	 */
	public static String getEsquemaPruebas() {
		return esquemaPruebas;
	}
	/**
	 * Método que devuelve el código de estado en el que se encuentra un pago anulado.
	 * @return Constante de estado anulado
	 */
	public static String getEstadoAnulado() {
		return estadoAnulado;
	}
	/**
	 * Método que devuelve el código de la modalidad 2
	 * @return el valor de atributo mod2
	 */
	public static String getModalidadLiquidacion() {
		return modalidadLiquidacion;
	}
	/**
	 * Método que devuelve el código de la modalidad 3
	 * @return el valor de atributo mod3
	 */
	public static String getModalidadAutoliquidacion() {
		return modalidadAutoliquidacion;
	}
	/**
	 * Método que devuelve el código de origen de Servicio Web
	 * @return el valor de atributo origenSW
	 */
	public static String getOrigenServicioWeb() {
		return origenSW;
	}
	/**
	 * Método que devuelve el código de origen de Portal Tributario
	 * @return el valor de atributo origenPT
	 */
	public static String getOrigenPortal() {
		return origenPT;
	}
	/**
	 * @return el valor de atributo noPagadoEntidad de Pago
	 */
	public static String getNoPagadoLiberbank() {
		return noPagadoLiberbank;
	}
	/**
	 * @return el valor de atributo literalTarjeta
	 */
	public static String getLiteralTarjeta() {
		return literalTarjeta;
	}
	/**
	 * @return el valor de atributo literalCCC
	 */
	public static String getLiteralCCC() {
		return literalCCC;
	}
	/**
	 * @return el valor de atributo sI
	 */
	public static String getSI() {
		return SI;
	}
	/**
	 * @return el valor de atributo nO
	 */
	public static String getNO() {
		return NO;
	}
	/**
	 * @return el valor de atributo origenSI
	 */
	public static String getOrigenS1() {
		return origenS1;
	}
	/**
	 * @return the longCodigo
	 */
	public static int getLongCodigoRespNormal() {
		return longCodigoRespNormal;
	}
	/**
	 * @return the longColumnaRespuesta
	 */
	public static int getLongColumnaRespuesta() {
		return longColumnaRespuesta;
	}
	/**
	 * @return the caracterSeparacionResultado
	 */
	public static String getCaracterSeparacionResultado() {
		return caracterSeparacionResultado;
	}
	// CRUBENCVS 08/04/2022
	public static String getNoDatosUnicaja() {
		return noDatosUnicaja;
	}
}
