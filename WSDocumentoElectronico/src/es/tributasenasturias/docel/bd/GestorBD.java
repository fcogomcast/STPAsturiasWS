package es.tributasenasturias.docel.bd;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.tributasenasturias.docel.exceptions.DocumentoElectronicoException;
import es.tributasenasturias.docel.preferencias.Preferencias;
import es.tributasenasturias.docel.soap.SoapClientHandler;
import es.tributasenasturias.lanzador.LanzadorException;
import es.tributasenasturias.lanzador.LanzadorFactory;
import es.tributasenasturias.lanzador.ParamType;
import es.tributasenasturias.lanzador.ProcedimientoAlmacenado;
import es.tributasenasturias.lanzador.TLanzador;
import es.tributasenasturias.lanzador.response.RespuestaLanzador;
 /**
  * Clase de interfaz con la Base de Datos
  * @author crubencvs
  *
  */
public class GestorBD {
	private static final String ESUN_ESTRUCTURA_UNIVERSAL = "ESUN_ESTRUCTURA_UNIVERSAL";
	private Preferencias pref;
	private String idLlamada;
	public static class ResultadoLlamada {
		private boolean esError;
		private String descripcion;
		private Map<String, String> valoresDevueltos;
		public boolean isError() {
			return esError;
		}
		public void setEsError(boolean esError) {
			this.esError = esError;
		}
		public String getDescripcion() {
			return descripcion;
		}
		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}
		public Map<String, String> getValoresDevueltos() {
			if (this.valoresDevueltos==null){
				valoresDevueltos=new HashMap<String,String>();
			}
			return valoresDevueltos;
		}
		
	}
	
	private GestorBD(Preferencias pref, String idLlamada)
	{
		this.pref= pref;
		this.idLlamada= idLlamada;
	}
	public static GestorBD newInstance(Preferencias pref, String idLlamada) {
		return new GestorBD(pref, idLlamada);
	}
	
	/**
	 * Genera una nueva entrada de documento electrónico en la Base de datos
	 * @param idDocArchivoDigital
	 * @param nombreFichero
	 * @param nombreFormato
	 * @param fechaCaptura
	 * @param origenCiudadano
	 * @param estadoElaboracion
	 * @param idDocumentoOrg
	 * @param tipoDocumental
	 * @param nombreNatural
	 * @param descripcion
	 * @param codUsuario
	 * @param idGdre
	 * @param csvAdar
	 * @param hashAdar
	 * @param funcionHash
	 * @param idEnl
	 * @return
	 * @throws DocumentoElectronicoException
	 */
	public ResultadoLlamada generarDocumento (int idDocArchivoDigital,
											  String nombreFichero,
											  String nombreFormato, 
											  Calendar fechaCaptura, 
											  String origenCiudadano,
											  String estadoElaboracion, 
											  String idDocumentoOrg, 
											  String tipoDocumental, 
											  String nombreNatural,
											  String descripcion, 
											  String codUsuario
											  /* INIPETITRIBUTAS-88 MCMARCO ** 07/05/2020 ** Formato XML del expediente electrónico */
											  , int idGdre
											  , String csvAdar
											  , String hashAdar
											  , String funcionHash
											  /* INIPETITRIBUTAS-88 ENAVARRO ** 14/05/2020 ** Formato XML del expediente electrónico */
											  , int idEnl
											  /* FINPETITRIBUTAS-88 ENAVARRO ** 14/05/2020 ** Formato XML del expediente electrónico */
											  /* FINPETITRIBUTAS-88 MCMARCO ** 07/05/2020 ** Formato XML del expediente electrónico */
			) throws DocumentoElectronicoException
	{
		try {
			ResultadoLlamada resultado =new ResultadoLlamada();
			TLanzador lanzador= LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(this.idLlamada));
			ProcedimientoAlmacenado pA= new ProcedimientoAlmacenado(pref.getPAGenerarDocumento(), pref.getEsquemaBD());
			pA.param("1", ParamType.NUMERO);
			pA.param("1", ParamType.NUMERO);
			pA.param(codUsuario,ParamType.CADENA);
			pA.param("33", ParamType.NUMERO);
			pA.param(String.valueOf(idDocArchivoDigital), ParamType.NUMERO);
			pA.param(nombreFichero, ParamType.CADENA);
			pA.param(nombreFormato, ParamType.CADENA);
			String fecha= new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(fechaCaptura.getTime());
			pA.param(fecha, ParamType.FECHA,"DD-MM-YYYY HH24:MI:SS");
			pA.param(origenCiudadano, ParamType.CADENA);
			pA.param(estadoElaboracion, ParamType.CADENA);
			pA.param(idDocumentoOrg, ParamType.CADENA);
			pA.param(tipoDocumental, ParamType.CADENA);
			pA.param(nombreNatural, ParamType.CADENA);
			pA.param(descripcion, ParamType.CADENA);
 			/* INIPETITRIBUTAS-88 MCMARCO ** 07/05/2020 ** Formato XML del expediente electrónico */
 			//CRUBENCVS 42858. El csv y el hash se le pasan siempre.
 			if(idGdre > 0)
 			{
	 			pA.param(String.valueOf(idGdre), ParamType.NUMERO);
	 			//pA.param(csvAdar, ParamType.CADENA);
				//pA.param(hashAdar, ParamType.CADENA);
 			}
 			/* FINPETITRIBUTAS-88 MCMARCO ** 07/05/2020 ** Formato XML del expediente electrónico */
 			/* INIPETITRIBUTAS-88 ENAVARRO ** 14/05/2020 ** Formato XML del expediente electrónico */
 			else {
 				pA.param("", ParamType.NUMERO); 	// p_in_id_gdre
 	 			//pA.param("", ParamType.CADENA); 	// p_in_csv_adar
 				//pA.param("", ParamType.CADENA);	// p_in_hash_adar
 			}
 			pA.param(csvAdar==null?"":csvAdar, ParamType.CADENA);
			pA.param(hashAdar==null?"":hashAdar, ParamType.CADENA);
			pA.param(funcionHash==null?"":funcionHash, ParamType.CADENA);
			//FIN CRUBENCVS 42858
 			if(idEnl > 0)
 			{
 				pA.param(String.valueOf(idEnl), ParamType.NUMERO);	// p_in_id_enl_docu
 			} else {
 				pA.param("", ParamType.NUMERO);
 			}
 			/* FINPETITRIBUTAS-88 ENAVARRO ** 14/05/2020 ** Formato XML del expediente electrónico */
 			pA.param("P", ParamType.CADENA);
			RespuestaLanzador respuesta= new RespuestaLanzador(lanzador.ejecutar(pA));
			if (!respuesta.esErronea()){
				resultado.setEsError(false);
				//Recuperamos su información
				String codigo = respuesta.getValue("CANU_CADENAS_NUMEROS",1,"STRING1_CANU");
				String id_documento= respuesta.getValue("CANU_CADENAS_NUMEROS", 1, "NUME1_CANU");
				resultado.getValoresDevueltos().put("id_documento", id_documento);
				resultado.getValoresDevueltos().put("codigo", codigo);
			} else  {
				resultado.setEsError(true);
				resultado.setDescripcion(respuesta.getTextoError());
			}
			return resultado;
		}catch (LanzadorException le) {
			throw new DocumentoElectronicoException ("Error en comunicación con base de datos:" + le.getMessage());
		}
	}
	
	public ResultadoLlamada consultarDocumento (int idDocumentoElectronico, String usuario) throws DocumentoElectronicoException
	{
		try {
			ResultadoLlamada resultado =new ResultadoLlamada();
			TLanzador lanzador= LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(this.idLlamada));
			ProcedimientoAlmacenado pA= new ProcedimientoAlmacenado(pref.getPAConsultaDocumento(), pref.getEsquemaBD());
			pA.param("1", ParamType.NUMERO);
			pA.param("1", ParamType.NUMERO);
			pA.param(usuario,ParamType.CADENA);
			pA.param("33", ParamType.NUMERO);
			pA.param(String.valueOf(idDocumentoElectronico), ParamType.NUMERO);
 			pA.param("P", ParamType.CADENA);
			
			RespuestaLanzador respuesta= new RespuestaLanzador(lanzador.ejecutar(pA));
			if (!respuesta.esErronea()){
				resultado.setEsError(false);
				String idArchivoDigital= respuesta.getValue(ESUN_ESTRUCTURA_UNIVERSAL,1,"N1");
				String nombreFormato= respuesta.getValue(ESUN_ESTRUCTURA_UNIVERSAL,1,"C1");
				String versionNti=respuesta.getValue(ESUN_ESTRUCTURA_UNIVERSAL,1,"C2");
				String identificadorENI=respuesta.getValue(ESUN_ESTRUCTURA_UNIVERSAL,1,"C3");
				String organo= respuesta.getValue(ESUN_ESTRUCTURA_UNIVERSAL,1,"C4");
				String fechaCaptura=respuesta.getValue(ESUN_ESTRUCTURA_UNIVERSAL,1,"C5");
				String origen= respuesta.getValue(ESUN_ESTRUCTURA_UNIVERSAL,1,"C6");
				String estadoElaboracion= respuesta.getValue(ESUN_ESTRUCTURA_UNIVERSAL,1,"C7");
				String identificadorDocOrigen=respuesta.getValue(ESUN_ESTRUCTURA_UNIVERSAL,1,"C8");
				String tipoDocumental= respuesta.getValue(ESUN_ESTRUCTURA_UNIVERSAL,1,"C9");
				String nombreNatural= respuesta.getValue(ESUN_ESTRUCTURA_UNIVERSAL,1,"C10");
				String descripcion= respuesta.getValue(ESUN_ESTRUCTURA_UNIVERSAL,1,"CG1");
				String nombreFichero= respuesta.getValue(ESUN_ESTRUCTURA_UNIVERSAL,1,"C11");
				String definicionCsv = respuesta.getValue(ESUN_ESTRUCTURA_UNIVERSAL, 1, "C12");
				
				resultado.getValoresDevueltos().put("idArchivoDigital", idArchivoDigital);
				resultado.getValoresDevueltos().put("nombreFormato", nombreFormato);
				resultado.getValoresDevueltos().put("versionNti", versionNti);
				resultado.getValoresDevueltos().put("identificadorENI", identificadorENI);
				resultado.getValoresDevueltos().put("organo", organo);
				resultado.getValoresDevueltos().put("fechaCaptura", fechaCaptura);
				resultado.getValoresDevueltos().put("origen", origen);
				resultado.getValoresDevueltos().put("estadoElaboracion", estadoElaboracion);
				resultado.getValoresDevueltos().put("identificadorDocOrigen", identificadorDocOrigen);
				resultado.getValoresDevueltos().put("tipoDocumental", tipoDocumental);
				resultado.getValoresDevueltos().put("nombreNatural", nombreNatural);
				resultado.getValoresDevueltos().put("descripcion", descripcion);
				resultado.getValoresDevueltos().put("nombreFichero", nombreFichero);
				resultado.getValoresDevueltos().put("definicionCsv", definicionCsv);
				
			} else  {
				resultado.setEsError(true);
				resultado.setDescripcion(respuesta.getTextoError());
			}
			return resultado;
		}catch (LanzadorException le) {
			throw new DocumentoElectronicoException ("Error en comunicación con base de datos:" + le.getMessage());
		}
	}
}
