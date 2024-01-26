package es.tributasenasturias.traslado.mensajes;

import javax.xml.bind.annotation.XmlElement;
 /**
  * Clase que modela el mapeo de un código arbitrario a un mensaje de aplicación concreto o 
  * bien un código arbitrario al mensaje que nos venga 
  * @author crubencvs
  *
  */
public class Mapeo {
	private String codigo;
	private String mensaje;
	private String mostrarOriginal;
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	@XmlElement(required=false)
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	@XmlElement(required=false,name="mostrar_original")
	public String getMostrarOriginal() {
		return mostrarOriginal;
	}
	public void setMostrarOriginal(String mostrarOriginal) {
		this.mostrarOriginal = mostrarOriginal;
	}
	
	
	
	
}
