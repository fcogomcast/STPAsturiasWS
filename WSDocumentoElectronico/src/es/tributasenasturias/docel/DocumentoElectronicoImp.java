package es.tributasenasturias.docel;

/* INIPETITRIBUTAS-88 ENAVARRO ** 24/04/2020 ** Formato XML del expediente electrónico */
import java.util.List;

import javax.xml.ws.Holder;
/* FINPETITRIBUTAS-88 ENAVARRO ** 24/04/2020 ** Formato XML del expediente electrónico */

import es.tributasenasturias.docel.bd.GestorBD;
import es.tributasenasturias.docel.bd.GestorBD.ResultadoLlamada;
import es.tributasenasturias.docel.exceptions.DocumentoElectronicoException;
import es.tributasenasturias.docel.io.MediadorArchivoDigital;
/* INIPETITRIBUTAS-7 MCMARCO ** 21/04/2020 ** Metadatos */
import es.tributasenasturias.docel.io.MediadorArchivoDigital.Metadatos;
/* FINPETITRIBUTAS-7 MCMARCO ** 21/04/2020 ** Metadatos */
import es.tributasenasturias.docel.io.MediadorArchivoDigital.ResultadoConsultaArchivoDigital;
import es.tributasenasturias.docel.io.MediadorArchivoDigital.ResultadoCustodia;
import es.tributasenasturias.docel.preferencias.Preferencias;
import es.tributasenasturias.log.ILog;


/**
 * Clase de implementación de las operaciones de documento electrónico
 * @author crubencvs
 *
 */
public class DocumentoElectronicoImp {

	private static final String ALGORITMO_HASH = "SHA1";

	private Preferencias pref;

	private String idLlamada;
	
	public static class ResultadoConsultaDocumento {
		private byte[] contenido;
		private String valorHuella;
		private String funcionResumen;
		private String nombreFormato;
		private String versionNTI;
		private String identificadorENI;
		private String organo;
		private String origen;
		private String fechaCaptura;
		private String estadoElaboracion;
		private String identificadorDocumentoOrigen;
		private String tipoDocumental;
		private String nombreNatural;
		private String nombreFichero;
		private String descripcion;
		private String tipoFirma;
		private String csv;
		private String definicionCsv;
		public byte[] getContenido() {
			return contenido;
		}
		public void setContenido(byte[] contenido) {
			this.contenido = contenido;
		}
		public String getValorHuella() {
			return valorHuella;
		}
		public void setValorHuella(String valorHuella) {
			this.valorHuella = valorHuella;
		}
		public String getFuncionResumen() {
			return funcionResumen;
		}
		public void setFuncionResumen(String funcionResumen) {
			this.funcionResumen = funcionResumen;
		}
		public String getNombreFormato() {
			return nombreFormato;
		}
		public void setNombreFormato(String nombreFormato) {
			this.nombreFormato = nombreFormato;
		}
		public String getVersionNTI() {
			return versionNTI;
		}
		public void setVersionNTI(String versionNTI) {
			this.versionNTI = versionNTI;
		}
		public String getIdentificadorENI() {
			return identificadorENI;
		}
		public void setIdentificadorENI(String identificadorENI) {
			this.identificadorENI = identificadorENI;
		}
		public String getOrgano() {
			return organo;
		}
		public void setOrgano(String organo) {
			this.organo = organo;
		}
		public String getFechaCaptura() {
			return fechaCaptura;
		}
		public void setFechaCaptura(String fechaCaptura) {
			this.fechaCaptura = fechaCaptura;
		}
		public String getEstadoElaboracion() {
			return estadoElaboracion;
		}
		public void setEstadoElaboracion(String estadoElaboracion) {
			this.estadoElaboracion = estadoElaboracion;
		}
		public String getIdentificadorDocumentoOrigen() {
			return identificadorDocumentoOrigen;
		}
		public void setIdentificadorDocumentoOrigen(String identificadorDocumentoOrigen) {
			this.identificadorDocumentoOrigen = identificadorDocumentoOrigen;
		}
		public String getTipoDocumental() {
			return tipoDocumental;
		}
		public void setTipoDocumental(String tipoDocumental) {
			this.tipoDocumental = tipoDocumental;
		}
		public String getNombreNatural() {
			return nombreNatural;
		}
		public void setNombreNatural(String nombreNatural) {
			this.nombreNatural = nombreNatural;
		}
		public String getNombreFichero() {
			return nombreFichero;
		}
		public void setNombreFichero(String nombreFichero) {
			this.nombreFichero = nombreFichero;
		}
		public String getDescripcion() {
			return descripcion;
		}
		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}
		public String getOrigen() {
			return origen;
		}
		public void setOrigen(String origen) {
			this.origen = origen;
		}
		public String getTipoFirma() {
			return tipoFirma;
		}
		public void setTipoFirma(String tipoFirma) {
			this.tipoFirma = tipoFirma;
		}
		public String getCsv() {
			return csv;
		}
		public void setCsv(String csv) {
			this.csv = csv;
		}
		public String getDefinicionCsv() {
			return definicionCsv;
		}
		public void setDefinicionCsv(String definicionCsv) {
			this.definicionCsv = definicionCsv;
		}
		
		
		
	}
	/**
	 * No se puede utilizar para construir objetos, tendrán que pasar por el método estático
	 * @param pref
	 * @param log
	 * @param idLlamada
	 */
	private DocumentoElectronicoImp(Preferencias pref, ILog log,String idLlamada) {
		this.pref= pref;
		this.idLlamada= idLlamada;
	}
	
	/**
	 * Construcción estática de un objeto de {@link DocumentoElectronicoImp}
	 * @param pref Preferencias de la llamada
	 * @param idLlamada Identificador único de la llamada
	 * @return
	 */
	public static DocumentoElectronicoImp newInstance(Preferencias pref, ILog log,String idLlamada){
		return new DocumentoElectronicoImp(pref, log, idLlamada);
	}
	
	private void validarEntrada(DocumentoType documento, AccionesGeneracionDocumentoType acciones  , MetadatosDocumentoType metadatos, TrazabilidadType trazabilidad) throws DocumentoElectronicoException{
		if (documento.getContenido()==null) {
			throw new DocumentoElectronicoException("No se ha recibido el contenido del documento");
		}
		if (documento.getNombreFormato()==null || "".equals(documento.getNombreFormato())){
			throw new DocumentoElectronicoException("No se ha recibido nombre de formato");
		}
		
		if (!"PDF".equalsIgnoreCase(documento.getNombreFormato())){
			throw new DocumentoElectronicoException("Sólo se admiten documentos en formato PDF");
		}
		
		if (documento.getNombreFichero()==null || "".equals(documento.getNombreFichero())){
			throw new DocumentoElectronicoException("Debe indicar el nombre de fichero");
		}
		String[] tokens= documento.getNombreFichero().split("\\.");
		if (tokens.length>0 && !tokens[tokens.length-1].equalsIgnoreCase("pdf")) {
			throw new DocumentoElectronicoException("La extensión de fichero ha de ser .pdf");
		}
		if (acciones==null)
		{
			throw new DocumentoElectronicoException ("No se han indicado acciones a realizar durante la generación del documento");
		}
		
		if (acciones.getFirmas()==null) {
			
			throw new DocumentoElectronicoException ("Debe indicar si se firmará el documento durante la generación, y el criterio de firma");
		}
		if (metadatos.getFechaCaptura()==null) {
			throw new DocumentoElectronicoException("Ha de indicar la fecha de captura del documento");
		}
		if (metadatos.getNombreNatural()==null || "".equals(metadatos.getNombreNatural())) {
			throw new DocumentoElectronicoException("Ha de indicar el nombre natural del documento");
		}
		if (metadatos.getDescripcion()==null || "".equals(metadatos.getDescripcion())){
			throw new DocumentoElectronicoException("Ha de indicar una descripción para el documento");
		}
		
		if (metadatos.getEstadoElaboracion().getValorEstadoElaboracion()==null ||
				"".equals(metadatos.getEstadoElaboracion().getValorEstadoElaboracion())) {
			throw new DocumentoElectronicoException ("Ha de indicarse el estado de elaboración del documento");
		}
		
		if (metadatos.getTipoDocumental()==null || "".equals(metadatos.getTipoDocumental())) {
			throw new DocumentoElectronicoException("Ha de indicar el tipo documental");
		}
		
		if (metadatos.getOrigenDocumento()==null || metadatos.getOrigenDocumento().value()==null){
			throw new DocumentoElectronicoException("Ha de indicar el origen del documento (C) o (A)");
		}
		if (trazabilidad.getUsuario()==null || 
			  "".equals(trazabilidad.getUsuario())) {
			throw new DocumentoElectronicoException("Ha de indicar el usuario que va a generar el documento");
		}
	}
	
	/* INIPETITRIBUTAS-7 MCMARCO ** 21/04/2020 ** Metadatos */
	private String obtenerMetadatosAD(MetadatosDocumentoType metadatos)
	{
		String metaXML = "";
		String origen;
		metaXML += "<versionNTI>"+ pref.getVersionNTIMetadatosAD() +"</versionNTI>";	
		if(metadatos.getOrigenDocumento().value()=="C")
			origen = "0";
		else 
			origen = "1";
		metaXML += "<origen>" + origen + "</origen>";
		metaXML += "<estadoElaboracion>" + metadatos.getEstadoElaboracion().getValorEstadoElaboracion() + "</estadoElaboracion>";
		metaXML += "<tipoDocumental>"+ metadatos.getTipoDocumental() + "</tipoDocumental>";	
		metaXML += "<organo>"+ pref.getOrganoMetadatosAD() + "</organo>";	
		return metaXML;
	}
	/* FINPETITRIBUTAS-7 MCMARCO ** 21/04/2020 ** Metadatos */

	public void generarDocumento (DocumentoType documento,
			AccionesGeneracionDocumentoType acciones, MetadatosDocumentoType metadatos,
			TrazabilidadType trazabilidad, ResultadoGenerarType resultado) throws DocumentoElectronicoException{
		validarEntrada(documento, acciones,metadatos, trazabilidad);
		byte[] contenido;
		contenido= documento.getContenido();
		//Ya no comprobamos si existe o no el documento, se debe enviar al alta de archivo,
		//y al grabar comprobar si el número de archivo ya estaba grabado en 
		//documento electrónico.
		String idDocumento;
		MediadorArchivoDigital archivo = MediadorArchivoDigital.newInstance(pref, idLlamada);
		String firmaCSV="";
		String firmante="";
		String firmaCertificado="";
		String firmaExterna="";
		String ltv="";
		FirmasInfoType ft=acciones.getFirmas();
		//Si hemos de firmar, sacamos cómo
		if ("S".equalsIgnoreCase(ft.getFirmarDocumento())) {
			firmaCSV= ft.getCSV().getFirmaCSV();
			firmante= ft.getCSV().getFirmante();
			firmaCertificado= ft.getCertificado().getFirmaCertificado();
			firmaExterna= ft.getCertificado().getFirmaExterna();
			ltv= ft.getCertificado().getLTV();
		}
		//TODO: El tipo de elemento debería recuperarse en función del formato.
		//También si ha de ir comprimido o no
		/* INIPETITRIBUTAS-88 MCMARCO ** 07/05/2020 ** Formato XML del expediente electrónico */
		int idGdre = 0;
		String csv = "";
		String hash = "";
		/* FINPETITRIBUTAS-88 MCMARCO ** 07/05/2020 ** Formato XML del expediente electrónico */
		/* INIPETITRIBUTAS-88 ENAVARRO ** 24/04/2020 ** Formato XML del expediente electrónico */
		int idAdarDocumento = 0;
		if(documento.getIdAdar() ==  null || documento.getIdAdar() <= 0) {
		/* FINPETITRIBUTAS-88 ENAVARRO ** 24/04/2020 ** Formato XML del expediente electrónico */
			ResultadoCustodia retCustodia= archivo.custodiar(trazabilidad.getUsuario(), "", pref.getTipoElementoArchivoPDF(), documento.getNombreFichero()
					, "N", contenido, "", 
					/* INIPETITRIBUTAS-7 MCMARCO ** 21/04/2020 ** Metadatos */				
					//"", 
					obtenerMetadatosAD(metadatos),
					/* FINPETITRIBUTAS-7 MCMARCO ** 21/04/2020 ** Metadatos */
					firmaExterna, firmaCSV, firmante, firmaCertificado, ltv);
			if (retCustodia.isError()) {
				/* INIPETITRIBUTAS-88 ENAVARRO ** 24/04/2020 ** Formato XML del expediente electrónico */
				//throw new DocumentoElectronicoException ("Error en la custodia del documento en el Archivo Digital:"+ retCustodia.getMensaje());
				String parametrosEntrada = "Firma Externa: " + firmaExterna + " Firmante: " + firmante + " Firma CSV: " + firmaCSV + " Firma Certificado: " + firmaCertificado + " LTV: " + ltv;
				throw new DocumentoElectronicoException ("Error en la custodia del documento en el Archivo Digital:"+ retCustodia.getMensaje() + parametrosEntrada);
				/* FINPETITRIBUTAS-88 ENAVARRO ** 24/04/2020 ** Formato XML del expediente electrónico */
			}
			/* INIPETITRIBUTAS-88 ENAVARRO ** 24/04/2020 ** Formato XML del expediente electrónico */
			idAdarDocumento = retCustodia.getIdDocumento();
			/* INIPETITRIBUTAS-88 MCMARCO ** 07/05/2020 ** Formato XML del expediente electrónico */
			//CRUBENCVS 42858. 25/06/2021. Recupero siempre el CSV y el hash, no sólo cuando es de reimprimible 
			if(documento.getIdGdre()!= null && documento.getIdGdre() > 0)
			{
				idGdre = documento.getIdGdre();
				//csv = retCustodia.getCsv();
			}
			csv = retCustodia.getCsv();
			hash= retCustodia.getHash();
			//FIN CRUBENCVS 42858. 25/06/2021
			/* FINPETITRIBUTAS-88 MCMARCO ** 07/05/2020 ** Formato XML del expediente electrónico */
		}
		else {
			try {
				idAdarDocumento = documento.getIdAdar();
				/* INIPETITRIBUTAS-88 MCMARCO ** 30/04/2020 ** Formato XML del expediente electrónico */
				String meta="";
				ResultadoConsultaArchivoDigital r = new ResultadoConsultaArchivoDigital();				
				meta = obtenerMetadatosAD(metadatos);
				r = archivo.guardarMetadatos(trazabilidad.getUsuario(), idAdarDocumento, meta);
				if (r.esError())
				{
					throw new DocumentoElectronicoException ("Error al guardar los metadatos. Error: " + r.getError());
				}							
				/* FINPETITRIBUTAS-88 MCMARCO ** 30/04/2020 ** Formato XML del expediente electrónico */
				//INI CRUBENCVS 42858.  25/06/2021. Recupero los datos del archivo digital, porque necesito
				//el csv y el hash
				r= archivo.obtenerArchivoPorId("USU_WEB_SAC", idAdarDocumento, "N");
				if (!r.esError()){
					csv= r.getCsv();
					hash= r.getHash();
				} else {
					throw new DocumentoElectronicoException ("Error al recuperar los datos del archivo con id adar:"+ idAdarDocumento+":"+r.getError());
				}
				//FIN CRUBENCVS 42868
			} 
			catch(Exception ex) {
				throw new DocumentoElectronicoException("Error en la custodia de los metadatos en el Archivo Digital. No se ha pasado un valor numérico en idAdar: " + idAdarDocumento+ " Exception:" + ex.getMessage());
			}
		}
		/* FINPETITRIBUTAS-88 ENAVARRO ** 24/04/2020 ** Formato XML del expediente electrónico */
		GestorBD bd= GestorBD.newInstance(pref, idLlamada);
		//Está en archivo digital, guardamos sus datos en la tabla
		/* INIPETITRIBUTAS-88 ENAVARRO ** 24/04/2020 ** Formato XML del expediente electrónico */
		//ResultadoLlamada resultadoGuardar= bd.generarDocumento(retCustodia.getIdDocumento(), documento.getNombreFichero(),  
		ResultadoLlamada resultadoGuardar= bd.generarDocumento(idAdarDocumento, 
															   documento.getNombreFichero(),  
															   /* FINPETITRIBUTAS-88 ENAVARRO ** 24/04/2020 ** Formato XML del expediente electrónico */
															   documento.getNombreFormato(), 
															   metadatos.getFechaCaptura().toGregorianCalendar(),
															   metadatos.getOrigenDocumento().value(), 
															   metadatos.getEstadoElaboracion().getValorEstadoElaboracion(), metadatos.getEstadoElaboracion().getIdDocumentoOrigen(),
															   /* INIPETITRIBUTAS-88 MCMARCO ** 07/05/2020 ** Formato XML del expediente electrónico */
															   //metadatos.getTipoDocumental(), metadatos.getNombreNatural(), metadatos.getDescripcion(), trazabilidad.getUsuario());
															   metadatos.getTipoDocumental(), metadatos.getNombreNatural(), metadatos.getDescripcion(), trazabilidad.getUsuario()
															   /* INIPETITRIBUTAS-88 ENAVARRO ** 14/05/2020 ** Formato XML del expediente electrónico */
															   //,idGdre, csv, hash);
															   ,idGdre, 
															   csv, 
															   hash, 
															   ALGORITMO_HASH,
															   trazabilidad.getIdEnl());
															  /* FINPETITRIBUTAS-88 ENAVARRO ** 14/05/2020 ** Formato XML del expediente electrónico */
															  /* FINPETITRIBUTAS-88 MCMARCO ** 07/05/2020 ** Formato XML del expediente electrónico */
		if (resultadoGuardar.isError()) {
			/* INIPETITRIBUTAS-88 ENAVARRO ** 24/04/2020 ** Formato XML del expediente electrónico */
			//throw new DocumentoElectronicoException("Error al guardar los datos del documento en base de datos. El documento con id:" + retCustodia.getIdDocumento() + " en Archivo Digital puede haber quedado huérfano");
			throw new DocumentoElectronicoException("Error al guardar los datos del documento en base de datos. El documento con id:" + idAdarDocumento + " en Archivo Digital puede haber quedado huérfano");
			/* FINPETITRIBUTAS-88 ENAVARRO ** 24/04/2020 ** Formato XML del expediente electrónico */
		}
		idDocumento= resultadoGuardar.getValoresDevueltos().get("id_documento");
		resultado.setCodigo("0000");
		resultado.setEsError(false);
		resultado.setIdDocumentoElectronico(idDocumento);
		resultado.setMensaje("Documento generado correctamente");
		
	}
	
	/**
	 * Consulta de un documento electrónico
	 * @param idDocumentoElectronico Identificador de documento electrónico
	 * @param usuarioConsulta Usuario que realiza la consulta
	 * @return Datos del documento electrónico
	 * @throws DocumentoElectronicoException
	 */
	public ResultadoConsultaDocumento consultaDocumento (int idDocumentoElectronico, String usuarioConsulta, String obtenerContenidoDocumento) throws DocumentoElectronicoException{
		ResultadoConsultaDocumento resultado= new ResultadoConsultaDocumento();
		MediadorArchivoDigital archivo = MediadorArchivoDigital.newInstance(pref, idLlamada);
		GestorBD bd= GestorBD.newInstance(pref, idLlamada);
		ResultadoLlamada resultadoConsultar= bd.consultarDocumento(idDocumentoElectronico, usuarioConsulta);
		if (resultadoConsultar.isError()) {
			throw new DocumentoElectronicoException("Error al consultar datos del documento");
		}
		String idArchivoDigital= resultadoConsultar.getValoresDevueltos().get("idArchivoDigital");
		ResultadoConsultaArchivoDigital r = archivo.obtenerArchivoPorId(usuarioConsulta, Integer.valueOf(idArchivoDigital).intValue(), obtenerContenidoDocumento);
		byte[] contenido=null;
		if ("S".equalsIgnoreCase(obtenerContenidoDocumento)){
			if (!r.esError()) {
				contenido= r.getFichero();
				if (contenido==null) {
					throw new DocumentoElectronicoException("No se ha podido consultar el archivo digital");
				}
			} else {
				throw new DocumentoElectronicoException("No se ha podido consultar el archivo digital, error: " + r.getError());
			}
		}		
		resultado.setContenido(contenido);
		/* INIPETITRIBUTAS-88 MCMARCO ** 05/05/2020 ** Formato XML del expediente electrónico */
		//resultado.setIdentificadorENI(resultadoConsultar.getValoresDevueltos().get("identificadorENI"));
		//resultado.setNombreFormato(resultadoConsultar.getValoresDevueltos().get("nombreFormato"));
		//resultado.setVersionNTI(resultadoConsultar.getValoresDevueltos().get("versionNti"));		
		//resultado.setOrgano(resultadoConsultar.getValoresDevueltos().get("organo"));
		//resultado.setFechaCaptura(resultadoConsultar.getValoresDevueltos().get("fechaCaptura"));
		//resultado.setOrigen(resultadoConsultar.getValoresDevueltos().get("origen"));
		//resultado.setEstadoElaboracion(resultadoConsultar.getValoresDevueltos().get("estadoElaboracion"));
		resultado.setIdentificadorDocumentoOrigen(resultadoConsultar.getValoresDevueltos().get("identificadorDocOrigen"));
		resultado.setTipoDocumental(resultadoConsultar.getValoresDevueltos().get("tipoDocumental"));
		resultado.setNombreNatural(resultadoConsultar.getValoresDevueltos().get("nombreNatural"));
		resultado.setDescripcion(resultadoConsultar.getValoresDevueltos().get("descripcion"));
		resultado.setNombreFichero(resultadoConsultar.getValoresDevueltos().get("nombreFichero"));		
		if(r.getMetadatosClass() != null)
		{
			Metadatos meta = r.getMetadatosClass();
			resultado.setVersionNTI(meta.getVersionNTI());
			resultado.setIdentificadorENI(meta.getIdentificador());
			resultado.setNombreFormato(meta.getTipoArchivo());
			resultado.setOrgano(meta.getOrgano());
			resultado.setFechaCaptura(meta.getFechaAlta());
			resultado.setOrigen(meta.getOrigen());
			resultado.setEstadoElaboracion(meta.getEstadoElaboracion());
			resultado.setTipoDocumental(meta.getTipoDocumental());
		}		
		/* INIPETITRIBUTAS-88 MCMARCO ** 05/05/2020 ** Formato XML del expediente electrónico */
		resultado.setValorHuella(r.getHash());
		resultado.setFuncionResumen(r.getFuncionResumen()); 
		resultado.setTipoFirma(r.getTipoFirma());
		resultado.setCsv(r.getCsv());
		resultado.setDefinicionCsv(resultadoConsultar.getValoresDevueltos().get("definicionCsv"));
		return resultado;
	}
	
	/* INIPETITRIBUTAS-60 ENAVARRO ** 10/06/2020 ** Act. FOLE. Generación de expdte en formato XML */
	public void generarTodosDocumentos (String usuario, List<DatosDocumentoType> datosDocumento, Holder<String> salida, Holder<String> error)
		throws DocumentoElectronicoException{
		
		// No se firmara ningun documento que se custodie
		String firmaCSV="N";
		String firmante="N";
		String firmaCertificado="N";
		String firmaExterna="N";
		String ltv="N";
		byte[] contenido;
		MediadorArchivoDigital archivo = MediadorArchivoDigital.newInstance(pref, idLlamada);
		
		String idDocumento = "";

		for(int i = 0; i < datosDocumento.size(); i++) {
			DocumentoType documento = datosDocumento.get(i).getDocumento();
			MetadatosDocumentoType metadatos = datosDocumento.get(i).getMetadatos();
			contenido= documento.getContenido();
			//TODO: El tipo de elemento debería recuperarse en función del formato.
			//También si ha de ir comprimido o no
			int idGdre = 0;
			String csv = "";
			String hash = "";
			int idAdarDocumento = 0;
			// Si es mayor a 1, agregamos el separador
			if(i > 0) {
				idDocumento += "#";
			}
			
			// Si no tiene idAdar, lo custodiamos y llamamos a generar documento electronico en la bd
			if(documento.getIdAdar() ==  null || documento.getIdAdar() <= 0) {
				// Si el nombre no tiene la extension, dara error, asi que lo agregamos en caso de que no este
				String nombreEnviar = documento.getNombreFichero();
				if(!nombreEnviar.contains(documento.getNombreFormato()) && !nombreEnviar.contains(documento.getNombreFormato())) {
					nombreEnviar += "." + documento.getNombreFormato().toLowerCase();
				}
				ResultadoCustodia retCustodia= archivo.custodiar(usuario, "", pref.getTipoElementoArchivoPDF(), nombreEnviar
						, "N", contenido, "", 
						obtenerMetadatosAD(metadatos),
						firmaExterna, firmaCSV, firmante, firmaCertificado, ltv);
				if (retCustodia.isError()) {
					String parametrosEntrada = "Firma Externa: " + firmaExterna + " Firmante: " + firmante + " Firma CSV: " + firmaCSV + 
						" Firma Certificado: " + firmaCertificado + " LTV: " + ltv;
					throw new DocumentoElectronicoException ("Error en la custodia del documento en el Archivo Digital:"+ retCustodia.getMensaje() + parametrosEntrada);
				}
				idAdarDocumento = retCustodia.getIdDocumento();
				if(documento.getIdGdre()!= null && documento.getIdGdre() > 0)
				{
					idGdre = documento.getIdGdre();
					//INI CRUBENCVS 42858. El CSV y el HASH lo vamos a recuperar siempre
					//csv = retCustodia.getCsv();
				}
				csv= retCustodia.getCsv();
				hash= retCustodia.getHash();
				//FIN CRUBENCVS 42858
				try {
					GestorBD bd= GestorBD.newInstance(pref, idLlamada);
					//Está en archivo digital, guardamos sus datos en la tabla
					ResultadoLlamada resultadoGuardar= bd.generarDocumento(idAdarDocumento, 
																		   documento.getNombreFichero(), 
																		   documento.getNombreFormato(), 
																		   metadatos.getFechaCaptura().toGregorianCalendar(),
																		   metadatos.getOrigenDocumento().value(), 
																		   metadatos.getEstadoElaboracion().getValorEstadoElaboracion(),
																		   metadatos.getEstadoElaboracion().getIdDocumentoOrigen(),
																		   metadatos.getTipoDocumental(), 
																		   metadatos.getNombreNatural(), 
																		   metadatos.getDescripcion(), 
																		   usuario ,
																		   idGdre, 
																		   csv, 
																		   hash,
																		   ALGORITMO_HASH,
																		   datosDocumento.get(i).getIdEnl()); // Le pongo 0 porque el IdEnl de un documento NO custodiado con anerioridad, siempre sera null
					if (resultadoGuardar.isError()) {
						throw new DocumentoElectronicoException("Error al guardar los datos del documento en base de datos. El documento con id:" + 
								idAdarDocumento + " en Archivo Digital puede haber quedado huérfano");
					}
					idDocumento += resultadoGuardar.getValoresDevueltos().get("id_documento");
				}
				catch(Exception ex) {
					throw new DocumentoElectronicoException("Error en generar: " + ex.getMessage());
				}
			}
			else {
				try {
					// Si tiene idAdar, tiene que haberse generado el iddoel ya, y nos lo tienen que haber pasado por aqui
					idDocumento += datosDocumento.get(i).getIdEnl();
					idAdarDocumento = documento.getIdAdar();
					String meta="";
					ResultadoConsultaArchivoDigital r = new ResultadoConsultaArchivoDigital();				
					meta = obtenerMetadatosAD(metadatos);
					r = archivo.guardarMetadatos(usuario, idAdarDocumento, meta);
					if (r.esError())
					{
						throw new DocumentoElectronicoException ("Error al guardar los metadatos. Error: " + r.getError());
					}	
					//INI CRUBENCVS 42858.  25/06/2021. Recupero los datos del archivo digital, porque necesito
					//el csv y el hash
					ResultadoConsultaArchivoDigital retConsulta= archivo.obtenerArchivoPorId("USU_WEB_SAC", idAdarDocumento, "N");
					if (!retConsulta.esError()){
						csv= retConsulta.getCsv();
						hash= retConsulta.getHash();
					} else {
						throw new DocumentoElectronicoException ("Error al recuperar los datos del archivo con id adar:"+ idAdarDocumento+":"+retConsulta.getError());
					}
					//FIN CRUBENCVS 42868
				} 
				catch(Exception ex) {
					throw new DocumentoElectronicoException("Error en la custodia de los metadatos en el Archivo Digital. No se ha pasado " + 
							"un valor numérico en idAdar: " + idAdarDocumento+ " Exception:" + ex.getMessage());
				}
			}
		}
		salida.value = idDocumento;
		
	}
	/* FINPETITRIBUTAS-60 ENAVARRO ** 10/06/2020 ** Act. FOLE. Generación de expdte en formato XML */

}
