package es.stpa.smartmultas.entidades;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FileStatusData")
public class FileStatusData {

	private String IdFichero;
	private String NombreFichero;
	private String NumeroExpediente;
	private Long IdEper;
	private Long IdAdar;
	private TipoFichero TipoFichero;
	private String Custodiado;
	private String Error;

	
	public FileStatusData() { }
	

	public FileStatusData(String idFichero, String custodiado, String error) {
		super();
		IdFichero = idFichero;
		Custodiado = custodiado;
		Error = error;
	}


	public FileStatusData(String idFichero, String nombreFichero,
			String numeroExpediente, Long idEper, Long idAdar,
			TipoFichero tipoFichero, String custodiado) 
	{
		super();
		IdFichero = idFichero;
		NombreFichero = nombreFichero;
		NumeroExpediente = numeroExpediente;
		IdEper = idEper;
		IdAdar = idAdar;
		TipoFichero = tipoFichero;
		Custodiado = custodiado;
	}


	@XmlElement(name = "IdFichero")
	public String getIdFichero() { return IdFichero; }
	
	@XmlElement(name = "NombreFichero")
	public String getNombreFichero() { return NombreFichero; }
	
	@XmlElement(name = "NumeroExpediente")
	public String getNumeroExpediente() { return NumeroExpediente; }
	
	@XmlElement(name = "IdAdar")
	public Long getIdAdar() { return IdAdar; }
	
	@XmlElement(name = "IdEper")
	public Long getIdEper() { return IdEper; }
	
	@XmlElement(name = "TipoFichero")
	public TipoFichero getTipoFichero() { return TipoFichero; }
	
	@XmlElement(name = "Custodiado")
	public String getCustodiado() { return Custodiado; }
	
	@XmlElement(name = "Error")
	public String getError() { return Error; }
	
	
	public void setIdFichero(String idFichero) { IdFichero = idFichero;}
	public void setNombreFichero(String nombreFichero) { NombreFichero = nombreFichero; }
	public void setNumeroExpediente(String numeroExpediente) { NumeroExpediente = numeroExpediente; }
	public void setIdAdar(Long idAdar) { IdAdar = idAdar; }
	public void setIdEper(Long idEper) { IdEper = idEper; }
	public void setCustodiado(String custodiado) { Custodiado = custodiado; }
	public void setError(String error) { Error = error; }
	public void setTipoFichero(TipoFichero tipoFichero) { TipoFichero = tipoFichero; }
}
