package es.stpa.smartmultas.requests;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ConsultaDOIATEX5Request")
public class ConsultaDOIATEX5Request extends ConsultaATEX5Request { 

	@XmlElement
	 private String DOI = ""; // Documento Oficial de Identificación (DNI/NIF/CIF).
	@XmlElement
     private String RazonSocial = "";
	@XmlElement
     private String PrimerApellido = "";
	@XmlElement
     private String SegundoApellido;
	@XmlElement
     private String Nombre = "";
	@XmlElement
     private String FechaNacimiento = "";
	@XmlElement
     private String AnyoNacimiento = "";
	

	public ConsultaDOIATEX5Request() { }
	
	


	public ConsultaDOIATEX5Request(String doi, 
			Integer suborganismo, String codAgente, 
			String idOrganismoResponsable, String idResponsable,
			String udid, String sesionId, Integer sesionOrga) 
	{
		super(suborganismo, codAgente, idOrganismoResponsable, idResponsable, udid, sesionId, sesionOrga);
		DOI = doi;
	}




	public String getDOI() { return DOI; }
	public String getRazonSocial() { return RazonSocial; }
	public String getPrimerApellido() { return PrimerApellido; }
	public String getSegundoApellido() { return SegundoApellido; }
	public String getNombre() { return Nombre; }
	public String getFechaNacimiento() { return FechaNacimiento; }
	public String getAnyoNacimiento() { return AnyoNacimiento; }
	

	public void setDOI(String doi) { DOI = doi; }
	public void setRazonSocial(String razonSocial) { RazonSocial = razonSocial; }
	public void setPrimerApellido(String primerApellido) { PrimerApellido = primerApellido; }
	public void setSegundoApellido(String segundoApellido) { SegundoApellido = segundoApellido; }
	public void setNombre(String nombre) { Nombre = nombre; }
	public void setFechaNacimiento(String fechaNacimiento) { FechaNacimiento = fechaNacimiento; }
	public void setAnyoNacimiento(String anyoNacimiento) { AnyoNacimiento = anyoNacimiento; }
}
