package es.stpa.smartmultas.entidades;

import javax.xml.bind.annotation.XmlElement;

public class DatosSesion {

	private String UDID;
	private String SesionId;
	private Integer SesionOrga = 33;
	private String SesionUsua;
	private Integer NumEjercicios;

	
	public DatosSesion() {
		super();
	}
	
	
	public DatosSesion(String udid) {
		super();
		UDID = udid;
	}


	public DatosSesion(String udid, String sesionId, Integer sesionOrga) {
		UDID = udid;
		SesionId = sesionId;
		SesionOrga = sesionOrga;
	}

	@XmlElement(name = "UDID")
	public String getUDID() { return UDID; }
	
	@XmlElement(name = "SesionOrga")
	public Integer getSesionOrga() { return SesionOrga; }
	
	@XmlElement(name = "SesionId")
	public String getSesionId() { return SesionId; }
	
	@XmlElement(name = "SesionUsua")
	public String getSesionUsua() { return SesionUsua; }
	
	@XmlElement(name = "NumEjercicios")
	public Integer getNumEjercicios() { return NumEjercicios; }
	
	
	public void setUDID(String udid) { UDID = udid;}
	public void setSesionOrga(Integer sesionOrga) { SesionOrga = sesionOrga; }
	public void setSesionUsua(String sesionUsua) { SesionUsua = sesionUsua; }
	public void setSesionId(String sesionId) { SesionId = sesionId; }
	public void setNumEjercicios(Integer numEjercicios) { NumEjercicios = numEjercicios; }
}
