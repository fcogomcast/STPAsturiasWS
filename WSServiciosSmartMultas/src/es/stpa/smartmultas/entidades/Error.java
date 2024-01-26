package es.stpa.smartmultas.entidades;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Error")
public class Error {
	
	private int CodError;
	private String Descripcion;
	
	
	public Error() { }
	
	public Error(String descripcion) {
		super();
		Descripcion = descripcion;
	}

	public Error(int codError, String descripcion) {
		super();
		CodError = codError;
		Descripcion = descripcion;
	}
	

	@XmlElement(name = "CodError")
	public int getCodError() { return CodError; }
	
	@XmlElement(name = "Descripcion")
	public String getDescripcion() { return Descripcion; }
	
	public void setCodError(int codError) { CodError = codError; }
	public void setDescripcion(String descripcion) { Descripcion = descripcion; }
}

