package es.stpa.smartmultas.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AltaMultaResponse")
public class AltaMultaResponse {

    private Long IdEper; 

    private String NumBoletin;

    private String NumExpediente;

    private String FechaUltimoPago;

    private String ReferenciaCobro;

    private String ReferenciaLarga;
	
	
	public AltaMultaResponse() { }
	
	public AltaMultaResponse(Long idEper, String numBoletin,
			String numExpediente, String fechaUltimoPago,
			String referenciaCobro, String referenciaLarga) 
	{
		super();
		IdEper = idEper;
		NumBoletin = numBoletin;
		NumExpediente = numExpediente;
		FechaUltimoPago = fechaUltimoPago;
		ReferenciaCobro = referenciaCobro;
		ReferenciaLarga = referenciaLarga;
	}
	
	@XmlElement(name = "IdEper")
	public Long getIdEper() { return IdEper; }
	
	@XmlElement(name = "ReferenciaLarga")
	public String getReferenciaLarga() { return ReferenciaLarga; }

	@XmlElement(name = "NumBoletin")
	public String getNumBoletin() { return NumBoletin; }

	@XmlElement(name = "NumExpediente")
	public String getNumExpediente() { return NumExpediente; }
	
	@XmlElement(name = "FechaUltimoPago")
	public String getFechaUltimoPago() { return FechaUltimoPago; }
	
	@XmlElement(name = "ReferenciaCobro")
	public String getReferenciaCobro() { return ReferenciaCobro; }
	
	
	
	public void setIdEper(Long idEper) { IdEper = idEper; }
	
	public void setNumBoletin(String numBoletin) { NumBoletin = numBoletin; }

	public void setNumExpediente(String numExpediente) { NumExpediente = numExpediente; }
	
	public void setFechaUltimoPago(String fechaUltimoPago) { FechaUltimoPago = fechaUltimoPago; }
	
	public void setReferenciaCobro(String referenciaCobro) { ReferenciaCobro = referenciaCobro; }

	public void setReferenciaLarga(String referenciaLarga) { ReferenciaLarga = referenciaLarga; }
}
