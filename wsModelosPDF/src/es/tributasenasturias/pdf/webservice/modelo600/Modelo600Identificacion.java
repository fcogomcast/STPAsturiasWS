package es.tributasenasturias.pdf.webservice.modelo600;

import org.w3c.dom.Node;
import es.tributasenasturias.pdf.utils.XMLUtils;

public class Modelo600Identificacion {

	private Node identificacion;

	private String EXPR_ABREVIADA="";
	private static String EXPR_ABREVIADA_NOTARIO="ExpAbreviada";
	private static String EXPR_ABREVIADA_GENERICO="expAbreviada";
	private String DOC_NOTARIAL="";
	private static String DOC_NOTARIAL_NOTARIO="DocNotarial";
	private static String DOC_NOTARIAL_GENERICO="docNotarial";
	private String DOC_PRIVADO="";
	private static String DOC_PRIVADO_NOTARIO="DocPrivado";
	private static String DOC_PRIVADO_GENERICO="docPrivado";
	private String DOC_MERCANTIL="";
	private static String DOC_MERCANTIL_NOTARIO="DocMercantil";
	private static String DOC_MERCANTIL_GENERICO="docMercantil";
	private String DOC_JUDICIAL="";
	private static String DOC_JUDICIAL_NOTARIO="DocJudicial";
	private static String DOC_JUDICIAL_GENERICO="docJudicial";
	private String DOC_ADMINISTRATIVO="";
	private static String DOC_ADMINISTRATIVO_NOTARIO="DocAdministrativo";
	private static String DOC_ADMINISTRATIVO_GENERICO="docAdministrativo";
	private String NOTARIO_NOMBRE="";
	private static String NOTARIO_NOMBRE_NOTARIO="Notario";
	private static String NOTARIO_NOMBRE_GENERICO="nombreNotario";
	private String NOTARIO_APELLIDOS="";
	private static String NOTARIO_APELLIDOS_NOTARIO="ApellidosNotario";
	private static String NOTARIO_APELLIDOS_GENERICO="apellidosNotario";
	private String PROTOCOLO="";
	private static String PROTOCOLO_NOTARIO="NumProtocolo";
	private static String PROTOCOLO_GENERICO="numProtocolo";
	private String PROTOCOLO_BIS="";
	private static String PROTOCOLO_BIS_NOTARIO="NumProtocoloBis";
	private static String PROTOCOLO_BIS_GENERICO="numProtocoloBis";
	private String CONCEPTO="";
	private static String CONCEPTO_NOTARIO="Concepto";
	private static String CONCEPTO_GENERICO="concepto";
	private String MUNICIPIO_NOTARIO="";
	private static String MUNICIPIO_NOTARIO_NOTARIO="MunicipioNotarioDesc";
	private static String MUNICIPIO_NOTARIO_GENERICO="municipioNotarioDesc";
	private String PROVINCIA_NOTARIO="";
	private static String PROVINCIA_NOTARIO_NOTARIO="ProvinciaNotarioDesc";
	private static String PROVINCIA_NOTARIO_GENERICO="provinciaNotarioDesc";
	private String IDENTIFICACION_BIEN="";
	private static String IDENTIFICACION_BIEN_NOTARIO="IdentificacionAdicional";
	private static String IDENTIFICACION_BIEN_GENERICO="identificacionAdicional";
	private String FECHA_DEVENGO="";
	private static String FECHA_DEVENGO_NOTARIO="FechaDevengo";
	private static String FECHA_DEVENGO_GENERICO="fechaDevengo";
	private String DATO_ESPECIFICO="";
	private static String DATO_ESPECIFICO_NOTARIO="DatoEspecifico";
	private static String DATO_ESPECIFICO_GENERICO="datoEspecifico";

	public Modelo600Identificacion () {}

	public Modelo600Identificacion (Node iden,boolean notarios) {
		if (notarios){
			EXPR_ABREVIADA=EXPR_ABREVIADA_NOTARIO;
			DOC_NOTARIAL=DOC_NOTARIAL_NOTARIO;
			DOC_PRIVADO=DOC_PRIVADO_NOTARIO;
			DOC_MERCANTIL=DOC_MERCANTIL_NOTARIO;
			DOC_JUDICIAL=DOC_JUDICIAL_NOTARIO;
			DOC_ADMINISTRATIVO=DOC_ADMINISTRATIVO_NOTARIO;
			NOTARIO_NOMBRE=NOTARIO_NOMBRE_NOTARIO;
			NOTARIO_APELLIDOS=NOTARIO_APELLIDOS_NOTARIO;
			PROTOCOLO=PROTOCOLO_NOTARIO;
			PROTOCOLO_BIS=PROTOCOLO_BIS_NOTARIO;
			CONCEPTO=CONCEPTO_NOTARIO;
			MUNICIPIO_NOTARIO=MUNICIPIO_NOTARIO_NOTARIO;
			PROVINCIA_NOTARIO=PROVINCIA_NOTARIO_NOTARIO;
			IDENTIFICACION_BIEN=IDENTIFICACION_BIEN_NOTARIO;
			FECHA_DEVENGO=FECHA_DEVENGO_NOTARIO;
			DATO_ESPECIFICO=DATO_ESPECIFICO_NOTARIO;
		}else{
			EXPR_ABREVIADA=EXPR_ABREVIADA_GENERICO;
			DOC_NOTARIAL=DOC_NOTARIAL_GENERICO;
			DOC_PRIVADO=DOC_PRIVADO_GENERICO;
			DOC_MERCANTIL=DOC_MERCANTIL_GENERICO;
			DOC_JUDICIAL=DOC_JUDICIAL_GENERICO;
			DOC_ADMINISTRATIVO=DOC_ADMINISTRATIVO_GENERICO;
			NOTARIO_NOMBRE=NOTARIO_NOMBRE_GENERICO;
			NOTARIO_APELLIDOS=NOTARIO_APELLIDOS_GENERICO;
			PROTOCOLO=PROTOCOLO_GENERICO;
			PROTOCOLO_BIS=PROTOCOLO_BIS_GENERICO;
			CONCEPTO=CONCEPTO_GENERICO;
			MUNICIPIO_NOTARIO=MUNICIPIO_NOTARIO_GENERICO;
			PROVINCIA_NOTARIO=PROVINCIA_NOTARIO_GENERICO;
			IDENTIFICACION_BIEN=IDENTIFICACION_BIEN_GENERICO;
			FECHA_DEVENGO=FECHA_DEVENGO_GENERICO;
			DATO_ESPECIFICO=DATO_ESPECIFICO_GENERICO;
		}
		this.identificacion = iden;
	}

	public String getExpresionAbreviada() {
		return XMLUtils.getItemSimple(this.identificacion,EXPR_ABREVIADA,null,null,false);
	}

	public String getTipoDocumento() {
		return XMLUtils.getItemIterador(this.identificacion,new String[]{DOC_NOTARIAL,DOC_PRIVADO,DOC_MERCANTIL,DOC_JUDICIAL,DOC_ADMINISTRATIVO},
															new String[]{"S","S","S","S","S"},new String[]{"N","P","M","J","A"},false,false);
	}	

	public String getNotario() {
		return XMLUtils.getItemIterador(this.identificacion,new String[]{NOTARIO_NOMBRE,NOTARIO_APELLIDOS},null,null,true,true);
	}
	
	public String getProtocolo() {
		return XMLUtils.getItemSimple(this.identificacion,PROTOCOLO,null,null,true);
	}
	
	public String getProtocoloBis() {
		return XMLUtils.getItemSimple(this.identificacion,PROTOCOLO_BIS,"S","X",true);
	}
	
	public String getConcepto() {
		return XMLUtils.getItemSimple(this.identificacion,CONCEPTO,null,null,true);
	}
	
	public String getMunicipioNotario() {
		return XMLUtils.getItemSimple(this.identificacion,MUNICIPIO_NOTARIO,null,null,true);
	}
	
	public String getProvinciaNotario() {
		return XMLUtils.getItemSimple(this.identificacion,PROVINCIA_NOTARIO,null,null,true);
	}
	
	public String getIdentificacionDelBien() {
		return XMLUtils.getItemSimple(this.identificacion,IDENTIFICACION_BIEN,null,null,true);
	}
	
	public String getFechaDevengo() {
		return XMLUtils.getItemSimple(this.identificacion,FECHA_DEVENGO,null,null,true);
	}

	//INI**CRUBENCVS**Sin número**Introducimos el dato específico como nodo de identificación
	public String getDatoEspecifico(){
		return XMLUtils.getItemSimple(this.identificacion,DATO_ESPECIFICO,null,null,true);
	}
	//FIN**CRUBENCVS**Sin número**Introducimos el dato específico como nodo de identificación			
}
