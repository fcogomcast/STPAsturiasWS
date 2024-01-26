package es.stpa.smartmultas.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ConsultaFechaHoraResponse")
public class ConsultaFechaHoraResponse {
	
	private String Fecha;
	
	public ConsultaFechaHoraResponse() { }
	
	@XmlElement(name = "Fecha")
	public String getFecha() {
        return Fecha;
    }
	
	public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }
}
