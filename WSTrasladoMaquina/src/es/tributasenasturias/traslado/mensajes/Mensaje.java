package es.tributasenasturias.traslado.mensajes;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Clase que modela un mensaje concreto
 * @author crubencvs
 *
 */
public class Mensaje {
	private String id;
	private String codigo;
	private String descripcion;
	@XmlAttribute
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}
