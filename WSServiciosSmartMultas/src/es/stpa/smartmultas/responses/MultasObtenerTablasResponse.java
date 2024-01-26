package es.stpa.smartmultas.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="MultasObtenerTablasResponse")
public class MultasObtenerTablasResponse {

	@XmlElement
	private String Error;
	@XmlElement
	private String Fecha;
	@XmlElement
	private String FicheroB64;
	
	public MultasObtenerTablasResponse() { }
	
	public MultasObtenerTablasResponse(String error, String fecha, String ficheroB64) {
		Error = error;
		Fecha = fecha;
		FicheroB64 = ficheroB64;
	}

	public String getError() {
        return Error;
    }
	
	public String getFecha() {
        return Fecha;
    }
	
	public String getFicheroB64() {
        return FicheroB64;
    }
	
	public void setError(String Error) {
        this.Error = Error;
    }
	
	public void setFicheroB64(String FicheroB64) {
        this.FicheroB64 = FicheroB64;
    }
	
	public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }
}
