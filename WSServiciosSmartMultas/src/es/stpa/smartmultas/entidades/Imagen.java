package es.stpa.smartmultas.entidades;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Imagen")
public class Imagen {

	@XmlElement
	private String Nombre;
	@XmlElement
	private String Base64;
	
	public Imagen(){ }
	
	public Imagen(String nombre, String base64) {
		super();
		Nombre = nombre;
		Base64 = base64;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public String getBase64() {
		return Base64;
	}

	public void setBase64(String base64) {
		Base64 = base64;
	}
}