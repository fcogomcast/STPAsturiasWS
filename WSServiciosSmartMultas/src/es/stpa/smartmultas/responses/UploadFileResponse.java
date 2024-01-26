package es.stpa.smartmultas.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UploadFileResponse")
public class UploadFileResponse {

	private String IdFichero;
	private int TrozoGuardado;
	private String Error = null;

	
	public UploadFileResponse() { }
	

	public UploadFileResponse(String idFichero, int trozoGuardado, String error) 
	{
		super();
		IdFichero = idFichero;
		TrozoGuardado = trozoGuardado;
		Error = error;
	}



	@XmlElement(name = "IdFichero")
	public String getIdFichero() { return IdFichero; }
	
	@XmlElement(name = "Error")
	public String getError() { return Error; }
	
	@XmlElement(name = "TrozoGuardado")
	public int getTrozoGuardado() { return TrozoGuardado; }

	
	
	public void setIdFichero(String idFichero) { IdFichero = idFichero;}
	public void setError(String error) { Error = error; }
	public void setTrozoGuardado(int trozoGuardado) { TrozoGuardado = trozoGuardado; }
}
