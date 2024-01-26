package es.stpa.smartmultas.requests;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.stpa.smartmultas.entidades.DatosSesion;

@XmlRootElement(name = "ConsultaATEXRequest")
public class ConsultaATEXRequest extends DatosSesion { 

	@XmlElement
	 private String Matricula = "";
	@XmlElement
     private String Bastidor = "";
	@XmlElement
     private String Nif = "";
	@XmlElement
     private Integer Suborganismo;
	@XmlElement
     private String CodAgente = "";
	@XmlElement
     private String NifAgente = "";
	@XmlElement
     private Boolean DatosCompletos = false;
	

	public ConsultaATEXRequest() { }

	public ConsultaATEXRequest(String udid) {
		super(udid);	
	}


	public String getMatricula() { return Matricula; }
	public String getBastidor() { return Bastidor; }
	public String getNif() { return Nif; }
	public Integer getSuborganismo() { return Suborganismo; }
	public String getCodAgente() { return CodAgente; }
	public String getNifAgente() { return NifAgente; }
	public Boolean getDatosCompletos() { return DatosCompletos; }
	

	public void setMatricula(String matricula) { Matricula = matricula; }
	public void setBastidor(String bastidor) { Bastidor = bastidor; }
	public void setNif(String nif) { Nif = nif; }
	public void setSuborganismo(Integer suborganismo) { Suborganismo = suborganismo; }
	public void setCodAgente(String codAgente) { CodAgente = codAgente; }
	public void setNifAgente(String nifAgente) { NifAgente = nifAgente; }
	public void setDatosCompletos(Boolean datosCompletos) { DatosCompletos = datosCompletos; }
}
