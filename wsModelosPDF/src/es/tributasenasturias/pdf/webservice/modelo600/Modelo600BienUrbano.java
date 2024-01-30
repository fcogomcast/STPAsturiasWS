package es.tributasenasturias.pdf.webservice.modelo600;

import org.w3c.dom.Node;

import es.tributasenasturias.pdf.utils.XMLUtils;

public class Modelo600BienUrbano {

	private Node bienUrbano;

	private static String VIA_SIGLAS="via";
	private String VIA_NOMBRE="";
	private static String VIA_NOMBRE_NOTARIO="NombreVia";
	private static String VIA_NOMBRE_GENERICO="nombreVia";
	private static String NUMERO="numero";
	private static String ESCALERA="escalera";
	private static String PISO="piso";
	private static String PUERTA="puerta";
	private static String MUNICIPIO="municipioDesc";
	private static String PROVINCIA="provinciaDesc";
	private static String CODIGO_POSTAL="codigoPostal";
	private String VALOR_BIEN="";
	private static String VALOR_BIEN_NOTARIO="ValorBien";
	private static String VALOR_BIEN_GENERICO="valorBien";
	private String CLAVE_ADQUISICION="";
	private static String CLAVE_ADQUISICION_NOTARIO="ClaveAdquisicion";
	private static String CLAVE_ADQUISICION_GENERICO="claveAdquisicion";
	private String REFERENCIA_CATASTRAL="";
	private static String REFERENCIA_CATASTRAL_NOTARIO="ReferenciaCatastral";
	private static String REFERENCIA_CATASTRAL_GENERICO="referenciaCatastral";
	private String FECHA_ADQUISICION="";
	private static String FECHA_ADQUISICION_NOTARIO="FechaAdquisicion";
	private static String FECHA_ADQUISICION_GENERICO="fechaAdquisicion";
	private static String CLASE_BIEN="tipoDesc";
	private String AÑO_CONSTRUCCION="";
	private static String AÑO_CONSTRUCCION_NOTARIO="anoConstruccion";
	private static String AÑO_CONSTRUCCION_GENERICO="anyoConstruccion";
	private static String VPO="VPO";
	private String TRANSMISION="";
	private static String TRANSMISION_NOTARIO="Transmision";
	private static String TRANSMISION_GENERICO="transmision";

	public Modelo600BienUrbano() {}

	public Modelo600BienUrbano (Node bu,boolean notarios) {
		if (notarios){
			VIA_NOMBRE=VIA_NOMBRE_NOTARIO;
			VALOR_BIEN=VALOR_BIEN_NOTARIO;
			CLAVE_ADQUISICION=CLAVE_ADQUISICION_NOTARIO;
			REFERENCIA_CATASTRAL=REFERENCIA_CATASTRAL_NOTARIO;
			FECHA_ADQUISICION=FECHA_ADQUISICION_NOTARIO;
			AÑO_CONSTRUCCION=AÑO_CONSTRUCCION_NOTARIO;
			TRANSMISION=TRANSMISION_NOTARIO;
		}else{
			VIA_NOMBRE=VIA_NOMBRE_GENERICO;
			VALOR_BIEN=VALOR_BIEN_GENERICO;
			CLAVE_ADQUISICION=CLAVE_ADQUISICION_GENERICO;
			REFERENCIA_CATASTRAL=REFERENCIA_CATASTRAL_GENERICO;
			FECHA_ADQUISICION=FECHA_ADQUISICION_GENERICO;
			AÑO_CONSTRUCCION=AÑO_CONSTRUCCION_GENERICO;
			TRANSMISION=TRANSMISION_GENERICO;
		}
		this.bienUrbano = bu;
	}

	public String getSiglas () {
		return XMLUtils.getItemSimple(this.bienUrbano,VIA_SIGLAS,null,null,true);
	}
	
	public String getNombreVia () {
		return XMLUtils.getItemSimple(this.bienUrbano,VIA_NOMBRE,null,null,true);
	}
	
	public String getNumero () {
		return XMLUtils.getItemSimple(this.bienUrbano,NUMERO,null,null,true);
	}	
	
	public String getEscalera () {
		return XMLUtils.getItemSimple(this.bienUrbano,ESCALERA,null,null,true);
	}	
	
	public String getPiso () {
		return XMLUtils.getItemSimple(this.bienUrbano,PISO,null,null,true);
	}	
	
	public String getPuerta () {
		return XMLUtils.getItemSimple(this.bienUrbano,PUERTA,null,null,true);
	}	
	
	public String getMunicipio () {
		return XMLUtils.getItemSimple(this.bienUrbano,MUNICIPIO,null,null,true);
	}	
	
	public String getProvincia () {
		return XMLUtils.getItemSimple(this.bienUrbano,PROVINCIA,null,null,true);

	}
	
	public String getCodigoPostal () {
		return XMLUtils.getItemSimple(this.bienUrbano,CODIGO_POSTAL,null,null,true);
	}
	
	public String getValor () {
		return XMLUtils.getItemSimple(this.bienUrbano,VALOR_BIEN,null,null,true);
	}
	
	public String getClaveAdquisicion () {
		return XMLUtils.getItemSimple(this.bienUrbano,CLAVE_ADQUISICION,null,null,true);
	}
	
	public String getReferenciaCatastral () {
		return XMLUtils.getItemSimple(this.bienUrbano,REFERENCIA_CATASTRAL,null,null,true);
	}
	
	public String getFechaAdquisicion () {
		return XMLUtils.getItemSimple(this.bienUrbano,FECHA_ADQUISICION,null,null,true);
	}

	public String getClaseBien () {
		return XMLUtils.getItemSimple(this.bienUrbano,CLASE_BIEN,null,null,true);
	}
	
	public String getAnyoConstruccion () {
		return XMLUtils.getItemSimple(this.bienUrbano,AÑO_CONSTRUCCION,null,null,true);
	}

	public String getVPO () {
		return XMLUtils.getItemSimple(this.bienUrbano,VPO,null,null,true);
	}
	
	public String getPorcentajeTitularidad () {
		return XMLUtils.getItemSimple(this.bienUrbano,TRANSMISION,null,null,true);
	}
}