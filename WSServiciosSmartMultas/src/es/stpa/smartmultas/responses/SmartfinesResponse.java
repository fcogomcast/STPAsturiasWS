package es.stpa.smartmultas.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SmartfinesResponse")
public class SmartfinesResponse { 

	private String Resultado;
	
	public SmartfinesResponse() { }

	public SmartfinesResponse(String resultado) {
		super();
		Resultado = resultado;
	}

	@XmlElement(name="Resultado")
	public String getResultado() {
        return Resultado;
    }
	
	public void setResultado(String Resultado) {
        this.Resultado = Resultado;
    }
}
