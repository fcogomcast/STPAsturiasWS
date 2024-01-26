package es.stpa.smartmultas.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ConsultaMatriculaOCRResponse")
public class ConsultaMatriculaOCRResponse { 

	private String Matricula;
	private String Acierto;
	
	public ConsultaMatriculaOCRResponse() { }

	public ConsultaMatriculaOCRResponse(String matricula) {
		super();
		Matricula = matricula;
	}

	@XmlElement(name="Matricula")
	public String getMatricula() {
        return Matricula;
    }
	
	public void setMatricula(String matricula) {
        this.Matricula = matricula;
    }
	
	@XmlElement(name="Acierto")
	public String getAcierto() {
        return Acierto;
    }
	
	public void setAcierto(String acierto) {
        this.Acierto = acierto;
    }
}
