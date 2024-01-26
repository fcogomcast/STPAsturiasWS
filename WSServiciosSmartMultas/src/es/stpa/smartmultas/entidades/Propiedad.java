package es.stpa.smartmultas.entidades;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Propiedad {
	
	@XmlElement
	private String Nombre;
	@XmlElement
	private String Valor;

	public Propiedad(){ }
	
	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}


	public String getValor() {
		return Valor;
	}

	public void setValor(String valor) {
		Valor = valor;
	}
}

