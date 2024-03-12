/**
 * 
 */
package es.tributasenasturias.seguridad.servicio;

import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.seguridad.servicio.PermisoEjecucion.DatosConexion;


/** Implementa la funcionalidad de creaci�n de logs. Deber�a utilizarse esta, y no la creaci�n directa,
 *  porque se ocupa de la inicializaci�n correctamente.
 * @author crubencvs
 *
 */
public class SeguridadFactory {

	private SeguridadFactory(){};
	/**
	 * Devuelve una instancia {@link VerificadorCertificado} que permite comprobar si el certificado es v�lido
	 * en cuanto a estructura y contenido.
	 * 
	 * @param endpoint Endpoint del servicio de comprobaci�n de certificado. 
	 * @return instancia ComprobadorCertificado
	 */
	public static VerificadorCertificado newVerificadorCertificado(String endpoint) throws SeguridadException
	{
		return new VerificadorCertificado(endpoint);
	}
	/**
	 * Devuelve una instancia {@link VerificadorCertificado} que permite comprobar si el certificado es v�lido
	 * en cuanto a estructura y contenido.
	 * Se indica tambi�n un manejador SOAP que permite realizar operaciones sobre los mensajes que se produzcan
	 * 
	 * @param endpoint Endpoint del servicio de comprobaci�n de certificado. 
	 * @param soapHandler Manejador SOAP que va a realizar las operaciones sobre los mensajes intercambiados.
	 * @return instancia VerificadorCertificado
	 */
	public static VerificadorCertificado newVerificadorCertificado(String endpoint, SOAPHandler<SOAPMessageContext> soapHandler) throws SeguridadException
	{
		return new VerificadorCertificado(endpoint,soapHandler);
	}
	/**
	 * Devuelve una instancia {@link VerificadorPermisoServicio} que permite validar si el certificado es v�lido y 
	 *  tiene permiso para ejecutar el servicio.
	 * @param prc Instancia de {@link PropertyConfigurator} que indica las propiedades con las que se crear�
	 *            el objeto.
	 * @return instancia {@link VerificadorPermisoServicio}
	 */
	public static VerificadorPermisoServicio newVerificadorPermisoServicio(PropertyConfigurator prc)
	{
		VerificadorPermisoServicio obj=new VerificadorPermisoServicio(prc);
		return obj;
	}
	/**
	 * Devuelve una instancia {@link PermisoEjecucion} que permite comprobar los permisos del servicio en base de datos
	 * @param endpointLanzador Endpoint de Lanzador.
	 * @param esquema Esquema de base de datos
	 * @param procPermiso Procedimiento almacenado que consulta el permiso sobre un servicio.
	 * @return instancia PermisoEjecucion
	 * @throws SeguridadException En caso de no poder construir de forma adecuada el objeto.
	 */
	public static PermisoEjecucion newPermisoEjecucion(String endpointLanzador, String esquema, String procPermiso) throws SeguridadException
	{
		DatosConexion datos= new DatosConexion();
		datos.setEndpointLanzador(endpointLanzador);
		datos.setEsquema(esquema);
		datos.setProcedimiento(procPermiso);
		return new PermisoEjecucion(datos);
		
	}
	/**
	 * Devuelve una instancia {@link PermisoEjecucion} que permite comprobar los permisos del servicio en base de datos
	 * @param endpointLanzador endpoint servicio Lanzador.
	 * @param esquema Esquema de base de datos
	 * @param procPermiso Procedimiento almacenado que consulta el permiso sobre un servicio.
	 * @param soapHandler {@link SOAPHandler} sobre {@link SOAPMessageContext} que 
	 * @return instancia PermisoEjecucion
	 * @throws SeguridadException En caso de no poder construir de forma adecuada el objeto.
	 */
	public static PermisoEjecucion newPermisoEjecucion(String endpointLanzador, String esquema, String procPermiso, SOAPHandler<SOAPMessageContext> soapHandler) throws SeguridadException
	{
		DatosConexion datos= new DatosConexion();
		datos.setEndpointLanzador(endpointLanzador);
		datos.setEsquema(esquema);
		datos.setProcedimiento(procPermiso);
		datos.setManejadorMensajes(soapHandler);
		return new PermisoEjecucion(datos);
		
	}
	/**
	 * Devuelve una instancia {@link FirmaHelper} que permite firmar un XML y validar una firma.
	 * @param endpoint Endpoint del servicio de firma digital. 
	 * @return instancia FirmaHelper
	 */
	public static FirmaHelper newFirmaHelper(String endpoint) throws SeguridadException
	{
		return new FirmaHelper(endpoint);
	}
	/**
	 * Devuelve una instancia {@link FirmaHelper} que permite firmar un XML y validar una firma.
	 * Se indica tambi�n un manejador SOAP que permite realizar operaciones sobre los mensajes que se produzcan
	 * 
	 * @param endpoint Endpoint del servicio de firma digital. 
	 * @param soapHandler Manejador SOAP que va a realizar las operaciones sobre los mensajes intercambiados.
	 * @return instancia FirmaHelper
	 */
	public static FirmaHelper newFirmaHelper(String endpoint, SOAPHandler<SOAPMessageContext> soapHandler) throws SeguridadException
	{
		return new FirmaHelper(endpoint,soapHandler);
	}
	/**
	 * Devuelve una instancia {@link InfoPermisosCertificado} que permite almacenar informaci�n sobre
	 * los permisos de un certificado en nuestros sistemas.
	 * 
	 * @return instancia InfoPermisosCertificado
	 */
	public static InfoPermisosCertificado newInfoPermisosCertificado() throws SeguridadException
	{
		return new InfoPermisosCertificado();
	}
	/**
	 * Devuelve una instancia {@link InfoCertificado} con informaci�n sobre el certificado que se pasa.
	 * 
	 * @return instancia InfoCertificado
	 */
	public static InfoCertificado newInfoCertificado() throws SeguridadException
	{
		return new InfoCertificado();
	}
	/**
	 * Devuelve una instancia {@link DatosPermisosServicio} con datos sobre los permisos del servicio.
	 * 
	 * @return instancia DatosPermisosServicio
	 */
	public static DatosPermisosServicio newDatosPermisosServicio() throws SeguridadException
	{
		return new DatosPermisosServicio();
	}
}
