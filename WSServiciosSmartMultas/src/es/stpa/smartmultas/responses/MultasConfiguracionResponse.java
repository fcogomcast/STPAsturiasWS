package es.stpa.smartmultas.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.stpa.smartmultas.entidades.EtiquetasList;

@XmlRootElement(name = "MultasConfiguracionResponse")
public class MultasConfiguracionResponse {
	
	private String Idioma;
	
	private Integer Municipio;
	
	private Integer TiempoRetencion;
	
	private String DatosConexion;
	
	private String Impresion;
	
	private String TextoGeneral;
	
	private String TextoAyuntamiento;
	
	private String LogoIniBase64;
	
	private String LogoBase64;
	
	private String CodDispositivo;
	
	private String UltimoNumeroBoletin;
	
	private String UrlCsv;
	
	private String Pago;

	private EtiquetasList Etiquetas;// en nuestro caso solo debe haber una
	
	private String IdentificadorAplicacionC603;
	
	private String TipoFormatoC603;
	
	private String EmisoraC603;
	
	private String CodigoTributoC603;
	
	private Integer IndicadorCapturarC603;
	
	private Integer UltimoC603;

	
	public MultasConfiguracionResponse() { }

	public MultasConfiguracionResponse(String idioma, Integer municipio,
			Integer tiempoRetencion, String datosConexion, String impresion,
			String textoGeneral, String textoAyuntamiento, String logoBase64,
			String codDispositivo, String ultimoNumeroBoletin, String urlCsv,
			String pago) {
		super();
		Idioma = idioma;
		Municipio = municipio;
		TiempoRetencion = tiempoRetencion;
		DatosConexion = datosConexion;
		Impresion = impresion;
		TextoGeneral = textoGeneral;
		TextoAyuntamiento = textoAyuntamiento;
		LogoBase64 = logoBase64;
		CodDispositivo = codDispositivo;
		UltimoNumeroBoletin = ultimoNumeroBoletin;
		UrlCsv = urlCsv;
		Pago = pago;
	}

	@XmlElement(name = "Idioma")
	public String getIdioma() { return Idioma; }
	
	@XmlElement(name = "Municipio")
	public Integer getMunicipio() { return Municipio;}
	
	@XmlElement(name = "TiempoRetencion")
	public Integer getTiempoRetencion() {return TiempoRetencion; }
	
	@XmlElement(name = "DatosConexion")
	public String getDatosConexion() { return DatosConexion; }
	
	@XmlElement(name = "Impresion")
	public String getImpresion() { return Impresion; }
	
	@XmlElement(name = "TextoGeneral")
	public String getTextoGeneral() { return TextoGeneral; }
	
	@XmlElement(name = "TextoAyuntamiento")
	public String getTextoAyuntamiento() { return TextoAyuntamiento; }
	
	@XmlElement(name = "LogoBase64")
	public String getLogoBase64() { return LogoBase64; }
	
	@XmlElement(name = "CodDispositivo")
	public String getCodDispositivo() { return CodDispositivo; }
	
	@XmlElement(name = "UltimoNumeroBoletin")
	public String getUltimoNumeroBoletin() { return UltimoNumeroBoletin; }
	
	@XmlElement(name = "UrlCsv")
	public String getUrlCsv() { return UrlCsv; }
	
	@XmlElement(name = "Pago")
	public String getPago() { return Pago; }
	
	@XmlElement(name = "LogoIniBase64")
	public String getLogoIniBase64() { return LogoIniBase64; }
	
	@XmlElement(name = "IdentificadorAplicacionC603")
	public String getIdentificadorAplicacionC603() { return IdentificadorAplicacionC603; }
	
	@XmlElement(name = "TipoFormatoC603")
	public String getTipoFormatoC603() { return TipoFormatoC603; }
	
	@XmlElement(name = "EmisoraC603")
	public String getEmisoraC603() { return EmisoraC603; }
	
	@XmlElement(name = "CodigoTributoC603")
	public String getCodigoTributoC603() { return CodigoTributoC603; }
	
	@XmlElement(name = "IndicadorCapturarC603")
	public Integer getIndicadorCapturarC603() { return IndicadorCapturarC603; }
	
	@XmlElement(name = "UltimoC603")
	public Integer getUltimoC603() { return UltimoC603; }

	@XmlElement(name = "Etiquetas")
	public EtiquetasList getEtiquetas() { return Etiquetas; }
	
	

	public void setIdioma(String idioma) {
		Idioma = idioma;
	}

	public void setMunicipio(Integer municipio) {
		Municipio = municipio;
	}

	public void setTiempoRetencion(Integer tiempoRetencion) {
		TiempoRetencion = tiempoRetencion;
	}

	public void setDatosConexion(String datosConexion) {
		DatosConexion = datosConexion;
	}

	public void setImpresion(String impresion) {
		Impresion = impresion;
	}

	public void setTextoGeneral(String textoGeneral) {
		TextoGeneral = textoGeneral;
	}

	public void setTextoAyuntamiento(String textoAyuntamiento) {
		TextoAyuntamiento = textoAyuntamiento;
	}

	public void setLogoBase64(String logoBase64) {
		LogoBase64 = logoBase64;
	}

	public void setCodDispositivo(String codDispositivo) {
		CodDispositivo = codDispositivo;
	}

	public void setUltimoNumeroBoletin(String ultimoNumeroBoletin) {
		UltimoNumeroBoletin = ultimoNumeroBoletin;
	}

	public void setUrlCsv(String urlCsv) {
		UrlCsv = urlCsv;
	}

	public void setPago(String pago) {
		Pago = pago;
	}

	public void setLogoIniBase64(String logoIniBase64) {
		LogoIniBase64 = logoIniBase64;
	}

	public void setIdentificadorAplicacionC603(String identificadorAplicacionC603) {
		IdentificadorAplicacionC603 = identificadorAplicacionC603;
	}

	public void setTipoFormatoC603(String tipoFormatoC603) {
		TipoFormatoC603 = tipoFormatoC603;
	}

	public void setEmisoraC603(String emisoraC603) {
		EmisoraC603 = emisoraC603;
	}

	public void setCodigoTributoC603(String codigoTributoC603) {
		CodigoTributoC603 = codigoTributoC603;
	}

	public void setIndicadorCapturarC603(Integer indicadorCapturarC603) {
		IndicadorCapturarC603 = indicadorCapturarC603;
	}

	public void setUltimoC603(Integer ultimoC603) {
		UltimoC603 = ultimoC603;
	}

	public void setEtiquetas(EtiquetasList etiquetas) {
		Etiquetas = etiquetas;
	}
}
