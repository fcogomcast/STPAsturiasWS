package es.stpa.smartmultas.entidades;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Nota")
public class Nota {

	@XmlElement
    private String Descripcion;
	@XmlElement
    private String Usuario;
	@XmlElement
    private String NumRege;
	@XmlElement
    private String FechaAgenda;
	@XmlElement
    private String HoraDesde;
	@XmlElement
    private String HoraHasta;
	@XmlElement
    private String Agente;
    
    public Nota() { }
    
	public Nota(String descripcion, String usuario, String numRege,
			String fechaAgenda, String horaDesde, String horaHasta,
			String agente) 
	{
		super();
		Descripcion = descripcion;
		Usuario = usuario;
		NumRege = numRege;
		FechaAgenda = fechaAgenda;
		HoraDesde = horaDesde;
		HoraHasta = horaHasta;
		Agente = agente;
	}
		
	
	public Nota(String numRege, String agente) {
		super();
		NumRege = numRege;
		Agente = agente;
	}


	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return Descripcion;
	}
	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}
	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return Usuario;
	}
	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		Usuario = usuario;
	}
	/**
	 * @return the numRege
	 */
	public String getNumRege() {
		return NumRege;
	}
	/**
	 * @param numRege the numRege to set
	 */
	public void setNumRege(String numRege) {
		NumRege = numRege;
	}
	/**
	 * @return the fechaAgenda
	 */
	public String getFechaAgenda() {
		return FechaAgenda;
	}
	/**
	 * @param fechaAgenda the fechaAgenda to set
	 */
	public void setFechaAgenda(String fechaAgenda) {
		FechaAgenda = fechaAgenda;
	}
	/**
	 * @return the horaDesde
	 */
	public String getHoraDesde() {
		return HoraDesde;
	}
	/**
	 * @param horaDesde the horaDesde to set
	 */
	public void setHoraDesde(String horaDesde) {
		HoraDesde = horaDesde;
	}
	/**
	 * @return the horaHasta
	 */
	public String getHoraHasta() {
		return HoraHasta;
	}
	/**
	 * @param horaHasta the horaHasta to set
	 */
	public void setHoraHasta(String horaHasta) {
		HoraHasta = horaHasta;
	}
	/**
	 * @return the agente
	 */
	public String getAgente() {
		return Agente;
	}
	/**
	 * @param agente the agente to set
	 */
	public void setAgente(String agente) {
		Agente = agente;
	}


	
}
