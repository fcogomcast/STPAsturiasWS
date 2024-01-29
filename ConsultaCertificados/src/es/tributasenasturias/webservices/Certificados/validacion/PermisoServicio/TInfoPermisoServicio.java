package es.tributasenasturias.webservices.Certificados.validacion.PermisoServicio;

/**
 * Tipo de datos de permiso de servicio.<br/>
 * Contiene los datos que debemos guardar acerca del permiso de servicio. 
 * @author crubencvs
 *
 */

public class TInfoPermisoServicio {
	//Resultado de validación
	private String autorizacion="";
	private String especifico="";
	public String getAutorizacion() {
		return autorizacion;
	}
	public void setAutorizacion(String autorizacion) {
		this.autorizacion = autorizacion;
	}
	public String getEspecifico() {
		return especifico;
	}
	public void setEspecifico(String tipo) {
		this.especifico = tipo;
	}
	public TInfoPermisoServicio(String autorizacion, String tipo) {
		super();
		this.autorizacion = autorizacion;
		this.especifico = tipo;
	}
}
