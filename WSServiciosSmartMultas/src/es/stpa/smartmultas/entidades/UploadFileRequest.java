package es.stpa.smartmultas.entidades;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UploadFileRequest")
public class UploadFileRequest {

	private String IdFichero;
	private int TrozoActual;
	private int TrozosTotal;
	private String DataB64;
	private String NombreFichero;
	private String NumeroExpediente;
	private Long IdEper;
	private TipoFichero TipoFichero;

	
	public UploadFileRequest() { }
	

	@XmlElement(name = "IdFichero")
	public String getIdFichero() { return IdFichero; }
	
	@XmlElement(name = "NombreFichero")
	public String getNombreFichero() { return NombreFichero; }
	
	@XmlElement(name = "NumeroExpediente")
	public String getNumeroExpediente() { return NumeroExpediente; }
	
	@XmlElement(name = "TrozoActual")
	public int getTrozoActual() { return TrozoActual; }
	
	@XmlElement(name = "TrozosTotal")
	public int getTrozosTotal() { return TrozosTotal; }
	
	@XmlElement(name = "TipoFichero")
	public TipoFichero getTipoFichero() { return TipoFichero; }
	
	@XmlElement(name = "DataB64")
	public String getDataB64() { return DataB64; }
	
	@XmlElement(name = "IdEper")
	public Long getIdEper() { return IdEper; }
	
	
	public void setIdFichero(String idFichero) { IdFichero = idFichero;}
	public void setNombreFichero(String nombreFichero) { NombreFichero = nombreFichero; }
	public void setNumeroExpediente(String numeroExpediente) { NumeroExpediente = numeroExpediente; }
	public void setIdEper(Long idEper) { IdEper = idEper; }
	public void setDataB64(String dataB64) { DataB64 = dataB64; }
	public void setTipoFichero(TipoFichero tipoFichero) { TipoFichero = tipoFichero; }
	public void setTrozoActual(int trozoActual) { TrozoActual = trozoActual; }
	public void setTrozosTotal(int trozosTotal) { TrozosTotal = trozosTotal; }
}
