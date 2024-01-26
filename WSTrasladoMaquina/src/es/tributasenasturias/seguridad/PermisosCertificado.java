package es.tributasenasturias.seguridad;

import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.lanzador.LanzadorException;
import es.tributasenasturias.lanzador.LanzadorFactory;
import es.tributasenasturias.lanzador.ParamType;
import es.tributasenasturias.lanzador.ProcedimientoAlmacenado;
import es.tributasenasturias.lanzador.TLanzador;
import es.tributasenasturias.lanzador.response.RespuestaLanzador;

/**
 * Clase que comprueba los permisos de acceso a un servicio por parte de la identidad 
 * de un certificado
 * @author crubencvs
 *
 */
public class PermisosCertificado {

	private SOAPHandler<SOAPMessageContext> handler=null;
	private String endpoint;
	private String esquema;
	private String procVerificacionPermisos;
	/**
	 * Constructor, debe indicarse el endpoint del servicio lanzador, el esquema adecuado y 
	 * el procedimiento de verificación de permisos
	 * @param endpoint Endpoint de servicio lanzador
	 * @param esquema Esquema de base de datos donde se encuentra el procedimiento de verificación
	 * @param proc Procedimiento de verificación de permisos de la identidad
	 * @param handler manejador SOAP para poder interceptar los mensajes entre esta clase y el servicio lanzador
	 */
	public PermisosCertificado(String endpoint, String esquema, String proc,SOAPHandler<SOAPMessageContext> handler){
		this.endpoint= endpoint;
		this.esquema= esquema;
		this.procVerificacionPermisos=proc;
		this.handler= handler;
	}
	/**
	 * Similar al otro constructor, pero en este no se pasa el manejador SOAP
	 * @param endpoint
	 * @param esquema
	 * @param proc
	 */
	public PermisosCertificado(String endpoint, String esquema, String proc) {
		this(endpoint,esquema, proc, null);
	}
	/**
	 * Verifica si la identidad que se pasa tiene permisos sobre el servicio indicado
	 * @param servicio Alias del servicio a comprobar
	 * @param cifnif Identidad de la que se quiere conocer si tiene permisos
	 * @return true si tiene permiso, false si no los tiene o no han podido verificarse
	 * @throws SeguridadException Ante cualquier error
	 */
	public boolean verificarPermisos(String servicio, String cifnif)throws SeguridadException
	{
		try{
			TLanzador lanzador;
			if (this.handler!=null){
				lanzador = LanzadorFactory.newTLanzador(this.endpoint, this.handler);
			} 
			else
			{
				lanzador= LanzadorFactory.newTLanzador(this.endpoint);
			}
			ProcedimientoAlmacenado pa= new ProcedimientoAlmacenado(this.procVerificacionPermisos, this.esquema);
			pa.param(servicio, ParamType.CADENA);
			pa.param(cifnif, ParamType.CADENA);
			pa.param("P", ParamType.CADENA);
			String xml=lanzador.ejecutar(pa);
			RespuestaLanzador r = new RespuestaLanzador(xml);
			if (r.esErronea()){
				throw new SeguridadException ("Error recibido de base de datos al verificar los permisos del servicio " + servicio + " para " + cifnif + ":" + r.getTextoError());
			}
			String autorizacion = r.getValue("CADE_CADENA", 1, "STRING_CADE");
			if ("01".equals(autorizacion) || "02".equals(autorizacion)) //No autorizado o error
			{
				return false;
			}
			return true;
		}
		catch (LanzadorException le){
			throw new SeguridadException ("Error al comprobar los permisos para ejecutar el servicio:" +le.getMessage(),le);
		}
	}
}
