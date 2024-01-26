package es.stpa.smartmultas.requests;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.stpa.smartmultas.entidades.DatosSesion;

@XmlRootElement
public class ConsultaMultasRequest extends DatosSesion {

	private String CodAgente;
	private String PwdAgente;
	private Integer Suborganismo;
	
	
	public ConsultaMultasRequest() {
		super();
	}

	public ConsultaMultasRequest(Integer suborganismo, String codAgente, String pwdAgente,
								String udid, String sesionId, Integer sesionorga) 
	{
		super(udid, sesionId, sesionorga);
		Suborganismo = suborganismo;
		CodAgente = codAgente;
		PwdAgente = pwdAgente;
	}
	
	@XmlElement(name = "CodAgente")
	public String getCodAgente() { return CodAgente;}
	
	@XmlElement(name = "PwdAgente")
	public String getPwdAgente() { return PwdAgente; }
	
	@XmlElement(name = "Suborganismo")
	public Integer getSuborganismo() { return Suborganismo; }
	
	
	public void setCodAgente(String codAgente) { CodAgente = codAgente;}
	public void setPwdAgente(String pwdAgente) { PwdAgente = pwdAgente; }
	public void setSuborganismo(Integer suborganismo) { Suborganismo = suborganismo;}
}
