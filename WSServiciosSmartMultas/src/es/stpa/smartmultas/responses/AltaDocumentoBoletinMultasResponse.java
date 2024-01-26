package es.stpa.smartmultas.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AltaDocumentoBoletinMultasResponse")
public class AltaDocumentoBoletinMultasResponse {

	private String IdAdar;
	
	
	public AltaDocumentoBoletinMultasResponse() { }
	
	public AltaDocumentoBoletinMultasResponse(String idAdar) {
		super();
		IdAdar = idAdar;
	}

	@XmlElement(name = "IdAdar")
	public String getIdAdar() {
        return IdAdar;
    }
	
	public void setIdAdar(String IdAdar) {
        this.IdAdar = IdAdar;
    }
}
