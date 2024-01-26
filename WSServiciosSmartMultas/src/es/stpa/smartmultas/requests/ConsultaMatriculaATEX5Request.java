package es.stpa.smartmultas.requests;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ConsultaMatriculaATEX5Request")
public class ConsultaMatriculaATEX5Request extends ConsultaATEX5Request {

	@XmlElement
	private String Matricula = "";
	@XmlElement
	private String Bastidor = "";
	@XmlElement
	private String Nive = "";

	public ConsultaMatriculaATEX5Request() { }
	
	public ConsultaMatriculaATEX5Request(String udid, Integer sesionOrga, String sesionId) 
	{
		super(udid, sesionOrga, sesionId);
	}


	public ConsultaMatriculaATEX5Request(String matricula, String bastidor,
			Integer suborganismo, String codAgente, 
			String idOrganismoResponsable, String idResponsable,
			String udid, String sesionId, Integer sesionOrga) 
	{
		super(suborganismo, codAgente, idOrganismoResponsable, idResponsable, udid, sesionId, sesionOrga);
		Matricula = matricula;
		Bastidor = bastidor;
	}


	public String getMatricula() { return Matricula; }

	public void setMatricula(String matricula) { Matricula = matricula; }

	public String getBastidor() { return Bastidor; }

	public void setBastidor(String bastidor) { Bastidor = bastidor; }

	public String getNive() { return Nive;}

	public void setNive(String nive) { Nive = nive;}
}
