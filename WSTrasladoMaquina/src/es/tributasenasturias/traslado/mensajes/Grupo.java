package es.tributasenasturias.traslado.mensajes;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Modela los grupos de mensajes. Todos los mensajes estarán contenidos en un grupo, por ejemplo, aplicación,
 * base de datos
 * @author crubencvs
 *
 */
public class Grupo {
	private String id;
	private List<Mensaje> mensajes = new ArrayList<Mensaje>();
	private List<Mapeo> mapeos= new ArrayList<Mapeo>();
	
	@XmlAttribute
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@XmlElement(name="mensaje")
	public List<Mensaje> getMensajes() {
		return mensajes;
	}
	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}
	@XmlElement(name="mapeo")
	public List<Mapeo> getMapeos() {
		return mapeos;
	}
	public void setMapeos(List<Mapeo> mapeos) {
		this.mapeos = mapeos;
	}
	
}
