package es.tributasenasturias.pdf.webservice.modelo600;

import org.w3c.dom.Node;
import es.tributasenasturias.pdf.utils.XMLUtils;

public class Modelo600Interviniente {

	protected Node nodoInterviniente;

	private String NIF="";
	private static String NIF_NOTARIO="nif";
	private static String NIF_GENERICO="idInterviniente";
	private String APELLIDOS_NOMBRE="";
	private static String APELLIDOS_NOMBRE_NOTARIO="nombre_razonsocial";
	private static String APELLIDOS_NOMBRE_GENERICO="nombreRazonSocial";
	private String SIGLAS="";
	private static String SIGLAS_NOTARIO="siglas";
	private static String SIGLAS_GENERICO="via";
	private String VIA_NOMBRE="";
	private static String VIA_NOMBRE_NOTARIO="nombre_via";
	private static String VIA_NOMBRE_GENERICO="nombreVia";
	private static String VIA_NUMERO="numero";
	private static String ESCALERA="escalera";
	private static String PISO="piso";
	private static String PUERTA="puerta";
	private static String MUNICIPIO="municipioDesc";
	private static String PROVINCIA="provinciaDesc";
	private String CODIGO_POSTAL="";
	private static String CODIGO_POSTAL_NOTARIO="cp";
	private static String CODIGO_POSTAL_GENERICO="codigoPostal";
	private String TELEFONO="";
	private static String TELEFONO_NOTARIO="telf";
	private static String TELEFONO_GENERICO="telefono";
	private String COEFICIENTE_PARTICIPACION="";
	private static String COEFICIENTE_PARTICIPACION_NOTARIO="grdo_partic";
	private static String COEFICIENTE_PARTICIPACION_GENERICO="grdoPartic";

	public Modelo600Interviniente() {}

	public Modelo600Interviniente(Node nodoInterviniente,boolean notarios) {
		if (notarios){
			NIF=NIF_NOTARIO;
			APELLIDOS_NOMBRE=APELLIDOS_NOMBRE_NOTARIO;
			SIGLAS=SIGLAS_NOTARIO;
			VIA_NOMBRE=VIA_NOMBRE_NOTARIO;
			CODIGO_POSTAL=CODIGO_POSTAL_NOTARIO;
			TELEFONO=TELEFONO_NOTARIO;
			COEFICIENTE_PARTICIPACION=COEFICIENTE_PARTICIPACION_NOTARIO;
		}else{
			NIF=NIF_GENERICO;
			APELLIDOS_NOMBRE=APELLIDOS_NOMBRE_GENERICO;
			SIGLAS=SIGLAS_GENERICO;
			VIA_NOMBRE=VIA_NOMBRE_GENERICO;
			CODIGO_POSTAL=CODIGO_POSTAL_GENERICO;
			TELEFONO=TELEFONO_GENERICO;
			COEFICIENTE_PARTICIPACION=COEFICIENTE_PARTICIPACION_GENERICO;
		}
		this.nodoInterviniente = nodoInterviniente;
	}

	public String getNif () {
		return XMLUtils.getItemSimple(this.nodoInterviniente,NIF,null,null,true);
	}

	public String getApellidosNombre () {
		return XMLUtils.getItemSimple(this.nodoInterviniente,APELLIDOS_NOMBRE,null,null,true);
	}

	public String getSiglas () {
		return XMLUtils.getItemSimple(this.nodoInterviniente,SIGLAS,null,null,true);
	}

	public String getNombreVia () {
		return XMLUtils.getItemSimple(this.nodoInterviniente,VIA_NOMBRE,null,null,true);
	}

	public String getNumero () {
		return XMLUtils.getItemSimple(this.nodoInterviniente,VIA_NUMERO,null,null,true);
	}

	public String getEscalera () {
		return XMLUtils.getItemSimple(this.nodoInterviniente,ESCALERA,null,null,true);
	}

	public String getPiso () {
		return XMLUtils.getItemSimple(this.nodoInterviniente,PISO,null,null,true);
	}	

	public String getPuerta () {
		return XMLUtils.getItemSimple(this.nodoInterviniente,PUERTA,null,null,true);
	}	

	public String getMunicipio () {
		return XMLUtils.getItemSimple(this.nodoInterviniente,MUNICIPIO,null,null,true);
	}	

	public String getProvincia () {
		return XMLUtils.getItemSimple(this.nodoInterviniente,PROVINCIA,null,null,true);
	}

	public String getCodigoPostal () {
		return XMLUtils.getItemSimple(this.nodoInterviniente,CODIGO_POSTAL,null,null,true);
	}

	public String getTelefono () {
		return XMLUtils.getItemSimple(this.nodoInterviniente,TELEFONO,null,null,true);
	}

	public String getCoeficienteParticipacion () {
		return XMLUtils.getItemSimple(this.nodoInterviniente,COEFICIENTE_PARTICIPACION,null,null,true);
	}
}