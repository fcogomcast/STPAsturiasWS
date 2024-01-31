package es.tributasenasturias.pdf.webservice.modelo600;

import org.w3c.dom.Node;

import es.tributasenasturias.pdf.utils.XMLUtils;

public class Modelo600BienRustico {

	private Node bienRustico;

	private static String MUNICIPIO="municipioDesc";
	private static String PROVINCIA="provinciaDesc";
	private String TIPO_CULTIVO="";
	private static String TIPO_CULTIVO_NOTARIO="TipoCultivoDesc";
	private static String TIPO_CULTIVO_GENERICO="tipoCultivoDesc";
	private String VALOR_DECLARADO="";
	private static String VALOR_DECLARADO_NOTARIO="ValorDeclarado";
	private static String VALOR_DECLARADO_GENERICO="valorDeclarado";
	private String CLAVE_ADQUISICION="";
	private static String CLAVE_ADQUISICION_NOTARIO="ClaveAdquisicion";
	private static String CLAVE_ADQUISICION_GENERICO="claveAdquisicion";
	private String REFERENCIA_CATASTRAL="";
	private static String REFERENCIA_CATASTRAL_NOTARIO="ReferenciaCatastral";
	private static String REFERENCIA_CATASTRAL_GENERICO="referenciaCatastral";
	private static String POLIGONO="poligono";
	private static String PARCELA="parcela";
	private static String PARROQUIA="parroquia";
	private static String SUPERFICIE="superficie";
	private String TRANSMISION="";
	private static String TRANSMISION_NOTARIO="Transmision";
	private static String TRANSMISION_GENERICO="transmision";
	private static String SITUACION="situacion";

	public Modelo600BienRustico() {}

	public Modelo600BienRustico (Node br,boolean notarios) {
		if (notarios){
			TRANSMISION=TRANSMISION_NOTARIO;
			TIPO_CULTIVO=TIPO_CULTIVO_NOTARIO;
			VALOR_DECLARADO=VALOR_DECLARADO_NOTARIO;
			CLAVE_ADQUISICION=CLAVE_ADQUISICION_NOTARIO;
			REFERENCIA_CATASTRAL=REFERENCIA_CATASTRAL_NOTARIO;
		}else{
			TRANSMISION=TRANSMISION_GENERICO;
			TIPO_CULTIVO=TIPO_CULTIVO_GENERICO;
			VALOR_DECLARADO=VALOR_DECLARADO_GENERICO;
			CLAVE_ADQUISICION=CLAVE_ADQUISICION_GENERICO;
			REFERENCIA_CATASTRAL=REFERENCIA_CATASTRAL_GENERICO;
		}
		this.bienRustico = br;
	}	

	public String getMunicipio () {
		return XMLUtils.getItemSimple(this.bienRustico,MUNICIPIO,null,null,true);
	}

	public String getProvincia () {
		return XMLUtils.getItemSimple(this.bienRustico,PROVINCIA,null,null,true);
	}
	
	public String getCultivo () {
		return XMLUtils.getItemSimple(this.bienRustico,TIPO_CULTIVO,null,null,true);
	}
	
	public String getValor () {
		return XMLUtils.getItemSimple(this.bienRustico,VALOR_DECLARADO,null,null,true);
	}
	
	public String getClaveAdquisicion () {
		return XMLUtils.getItemSimple(this.bienRustico,CLAVE_ADQUISICION,null,null,true);
	}
	
	public String getReferenciaCatastral () {		
		return XMLUtils.getItemSimple(this.bienRustico,REFERENCIA_CATASTRAL,null,null,true);
	}
	
	public String getPoligono () {
		return XMLUtils.getItemSimple(this.bienRustico,POLIGONO,null,null,true);
	}

	public String getParcela () {
		return XMLUtils.getItemSimple(this.bienRustico,PARCELA,null,null,true);
	}
	
	public String getParroquia () {
		return XMLUtils.getItemSimple(this.bienRustico,PARROQUIA,null,null,true);
	}

	public String getSuperficieM2 () {
		return XMLUtils.getItemSimple(this.bienRustico,SUPERFICIE,null,null,true);
	}
	
	public String getPorcentajeTitularidad () {
		return XMLUtils.getItemSimple(this.bienRustico,TRANSMISION,null,null,true);
	}
	
	public String getSituacion () {
		return XMLUtils.getItemSimple(this.bienRustico,SITUACION,null,null,true);
	}
}