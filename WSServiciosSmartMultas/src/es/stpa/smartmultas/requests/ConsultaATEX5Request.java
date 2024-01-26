package es.stpa.smartmultas.requests;

import javax.xml.bind.annotation.XmlElement;

import es.stpa.smartmultas.entidades.DatosSesion;

public class ConsultaATEX5Request extends DatosSesion{

	@XmlElement
	 private String IdOrganismoResponsable;
	@XmlElement
     private String IdResponsable;
	@XmlElement
     private String Nif;
	@XmlElement
     private int Suborganismo;
	@XmlElement
     private String CodAgente;
	@XmlElement
     private String NombreResponsable;
	@XmlElement
     private String IdSolicitante;
	@XmlElement
     private String NombreSolicitante;
	

	public ConsultaATEX5Request() { }

	public ConsultaATEX5Request(String udid, Integer sesionOrga, String sesionId) {
		super(udid, sesionId, sesionOrga);
		
	}
	
	public ConsultaATEX5Request(Integer suborganismo, String codAgente, 
			String idOrganismoResponsable, String idResponsable,
			String udid, String sesionId, Integer sesionOrga) 
	{
		super(udid, sesionId, sesionOrga);
		Suborganismo = suborganismo;
		CodAgente = codAgente;
		IdOrganismoResponsable = idOrganismoResponsable;
		IdResponsable = idResponsable;
}


	/**
	 * @return the idOrganismoResponsable
	 */
	public String getIdOrganismoResponsable() {
		return IdOrganismoResponsable;
	}


	/**
	 * @param idOrganismoResponsable the idOrganismoResponsable to set
	 */
	public void setIdOrganismoResponsable(String idOrganismoResponsable) {
		IdOrganismoResponsable = idOrganismoResponsable;
	}


	/**
	 * @return the idResponsable
	 */
	public String getIdResponsable() {
		return IdResponsable;
	}


	/**
	 * @param idResponsable the idResponsable to set
	 */
	public void setIdResponsable(String idResponsable) {
		IdResponsable = idResponsable;
	}


	/**
	 * @return the nif
	 */
	public String getNif() {
		return Nif;
	}


	/**
	 * @param nif the nif to set
	 */
	public void setNif(String nif) {
		Nif = nif;
	}


	/**
	 * @return the suborganismo
	 */
	public int getSuborganismo() {
		return Suborganismo;
	}


	/**
	 * @param suborganismo the suborganismo to set
	 */
	public void setSuborganismo(int suborganismo) {
		Suborganismo = suborganismo;
	}


	/**
	 * @return the codAgente
	 */
	public String getCodAgente() {
		return CodAgente;
	}


	/**
	 * @param codAgente the codAgente to set
	 */
	public void setCodAgente(String codAgente) {
		CodAgente = codAgente;
	}


	/**
	 * @return the nombreResponsable
	 */
	public String getNombreResponsable() {
		return NombreResponsable;
	}


	/**
	 * @param nombreResponsable the nombreResponsable to set
	 */
	public void setNombreResponsable(String nombreResponsable) {
		NombreResponsable = nombreResponsable;
	}


	/**
	 * @return the idSolicitante
	 */
	public String getIdSolicitante() {
		return IdSolicitante;
	}


	/**
	 * @param idSolicitante the idSolicitante to set
	 */
	public void setIdSolicitante(String idSolicitante) {
		IdSolicitante = idSolicitante;
	}


	/**
	 * @return the nombreSolicitante
	 */
	public String getNombreSolicitante() {
		return NombreSolicitante;
	}


	/**
	 * @param nombreSolicitante the nombreSolicitante to set
	 */
	public void setNombreSolicitante(String nombreSolicitante) {
		NombreSolicitante = nombreSolicitante;
	}
	
	
}
