package es.stpa.smartmultas.requests;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "AltaDocumentoMultasRequest")
public class AltaDocumentoMultasRequest extends ConsultaMultasRequest{

	private String NumeroBoletin;
    private String ImagenB64;
    private String Descripcion;
    private String NombreFichero;
    private String FechaCaptura;
    private String HoraCaptura;
    private Long IdEper;
    private String CsvBoletin; // Adjuntar boletín al archivo digital
    private Boolean ImagenBoletin = false;
    private String HashBoletin; // almacenar HASH boletín
    private String BaseCSV;

    public AltaDocumentoMultasRequest(){
    	
    }

	public AltaDocumentoMultasRequest(Integer suborganismo, String codAgente,
			String pwdAgente, String udid, String sesionId, Integer sesionorga) 
	{
		super(suborganismo, codAgente, pwdAgente, udid, sesionId, sesionorga);

	}
	
	public AltaDocumentoMultasRequest(String numeroBoletin, String imagenB64,
			String descripcion, String nombreFichero, String fechaCaptura,
			String horaCaptura, Long idEper,
			Integer suborganismo, String codAgente, String pwdAgente,
			String udid, String sesionId, Integer sesionOrga) 
	{
		super(suborganismo, codAgente, pwdAgente, udid, sesionId, sesionOrga);
		NumeroBoletin = numeroBoletin;
		ImagenB64 = imagenB64;
		Descripcion = descripcion;
		NombreFichero = nombreFichero;
		FechaCaptura = fechaCaptura;
		HoraCaptura = horaCaptura;
		IdEper = idEper;
		ImagenBoletin = false;
	}
	
	@XmlElement(name = "NumeroBoletin")
	public String getNumeroBoletin() {return NumeroBoletin;}
	
	@XmlElement(name = "ImagenB64")
	public String getImagenB64() {return ImagenB64;}
	
	@XmlElement(name = "BaseCSV")
	public String getBaseCSV() {  return BaseCSV;}
	
	@XmlElement(name = "Descripcion")
	public String getDescripcion() { return Descripcion; }
	
	@XmlElement(name = "NombreFichero")
	public String getNombreFichero() { return NombreFichero; }
	
	@XmlElement(name = "FechaCaptura")
	public String getFechaCaptura() { return FechaCaptura; }
	
	@XmlElement(name = "HoraCaptura")
	public String getHoraCaptura() { return HoraCaptura; }	
	
	@XmlElement(name = "IdEper")
	public Long getIdEper() { return IdEper;}
	
	@XmlElement(name = "CsvBoletin")
	public String getCsvBoletin() {return CsvBoletin;}
	
	@XmlElement(name = "ImagenBoletin")
	public Boolean isImagenBoletin() {return ImagenBoletin;}
	
	@XmlElement(name = "HashBoletin")
	public String getHashBoletin() { return HashBoletin;}
	
	
	public void setNumeroBoletin(String numeroBoletin) {NumeroBoletin = numeroBoletin;}
	public void setImagenB64(String imagenB64) { ImagenB64 = imagenB64; }
	public void setDescripcion(String descripcion) { Descripcion = descripcion; }
	public void setNombreFichero(String nombreFichero) { NombreFichero = nombreFichero; }
	public void setFechaCaptura(String fechaCaptura) {FechaCaptura = fechaCaptura; }
	public void setHoraCaptura(String horaCaptura) {HoraCaptura = horaCaptura; }
	public void setIdEper(Long idEper) { IdEper = idEper; }
	public void setCsvBoletin(String csvBoletin) {CsvBoletin = csvBoletin; }
	public void setImagenBoletin(boolean imagenBoletin) {ImagenBoletin = imagenBoletin; }
	public void setHashBoletin(String hashBoletin) { HashBoletin = hashBoletin;}
	public void setBaseCSV(String baseCSV) { BaseCSV = baseCSV; }
}
