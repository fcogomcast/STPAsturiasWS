package es.stpa.notifica.adviser.archivodigital;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.handler.Handler;

import org.w3c.dom.Document;

import es.stpa.notifica.adviser.exceptions.AdviserException;
import es.stpa.notifica.adviser.preferencias.Preferencias;
import es.stpa.notifica.adviser.soap.SoapClientHandler;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.ArchivoDigital;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.ArchivoDigital_Service;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.CSVType;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.CertificadoType;
import es.tributasenasturias.services.ws.archivodigital.archivodigital.FirmaType;
import es.tributasenasturias.xml.XMLDOMDocumentException;
import es.tributasenasturias.xml.XMLDOMUtils;

/**
 * Gestiona operaciones con el Archivo Digital
 * @author crubencvs
 *
 */
public class MediadorArchivoDigital {

	private Preferencias pref;
	private String idLlamada;
	private ArchivoDigital port;
	
	public static class ResultadoCustodia {
		private boolean error;
		private String mensaje;
		private int idDocumento;
		private String csv;
		public String getCsv() {
			return csv;
		}
		public void setCsv(String csv) {
			this.csv = csv;
		}
		public boolean isError() {
			return error;
		}
		public void setEsError(boolean esError) {
			this.error = esError;
		}
		public String getMensaje() {
			return mensaje;
		}
		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}
		public int getIdDocumento() {
			return idDocumento;
		}
		public void setIdDocumento(int idDocumento) {
			this.idDocumento = idDocumento;
		}
		
	}
	/* INIPETITRIBUTAS-88 MCMARCO ** 05/05/2020 ** Formato XML del expediente electrónico */
	public static class Metadatos {
		private String versionNTI;
		private String fechaAlta;
		private String origen;
		private String estadoElaboracion;
		private String tipoArchivo;
		private String tipoDocumental;
		private String identificador;
		private String tipoRubrica;
		private String formatoFirma;
		private String valorCSV;
		private String definicionCSV;        
		private String organo;
		public String getVersionNTI() {
			return versionNTI;
		}
		public void setVersionNTI(String versionNTI) {
			this.versionNTI = versionNTI;
		}
		public String getFechaAlta() {
			return fechaAlta;
		}
		public void setFechaAlta(String fechaAlta) {
			this.fechaAlta = fechaAlta;
		}
		public String getOrigen() {
			return origen;
		}
		public void setOrigen(String origen) {
			this.origen = origen;
		}
		public String getEstadoElaboracion() {
			return estadoElaboracion;
		}
		public void setEstadoElaboracion(String estadoElaboracion) {
			this.estadoElaboracion = estadoElaboracion;
		}
		public String getTipoArchivo() {
			return tipoArchivo;
		}
		public void setTipoArchivo(String tipoArchivo) {
			this.tipoArchivo = tipoArchivo;
		}
		public String getTipoDocumental() {
			return tipoDocumental;
		}
		public void setTipoDocumental(String tipoDocumental) {
			this.tipoDocumental = tipoDocumental;
		}
		public String getIdentificador() {
			return identificador;
		}
		public void setIdentificador(String identificador) {
			this.identificador = identificador;
		}
		public String getTipoRubrica() {
			return tipoRubrica;
		}
		public void setTipoRubrica(String tipoRubrica) {
			this.tipoRubrica = tipoRubrica;
		}
		public String getFormatoFirma() {
			return formatoFirma;
		}
		public void setFormatoFirma(String formatoFirma) {
			this.formatoFirma = formatoFirma;
		}
		public String getValorCSV() {
			return valorCSV;
		}
		public void setValorCSV(String valorCSV) {
			this.valorCSV = valorCSV;
		}
		public String getDefinicionCSV() {
			return definicionCSV;
		}
		public void setDefinicionCSV(String definicionCSV) {
			this.definicionCSV = definicionCSV;
		}
		public String getOrgano() {
			return organo;
		}
		public void setOrgano(String organo) {
			this.organo = organo;
		}		
	}
	/* FINPETITRIBUTAS-88 MCMARCO ** 05/05/2020 ** Formato XML del expediente electrónico */
	
	public static class ResultadoConsultaArchivoDigital {
		private boolean esError;
		private byte[] fichero;
		private String datosArchivo;
		private String error;
		private String identificador;
		private String tipoElemento;
		private String tipoArchivo;
		private String fechaGeneracion;
		private String nombreFichero;
		private int size;
		private String hash;
		private String csv;
		private String metadatos;
		private String funcionResumen;
		private String tipoFirma;
		private String fechaValidezCertificado;
		/* INIPETITRIBUTAS-88 MCMARCO ** 05/05/2020 ** Formato XML del expediente electrónico */
		private Metadatos metadatosClass;
		/* FINPETITRIBUTAS-88 MCMARCO ** 05/05/2020 ** Formato XML del expediente electrónico */
		public boolean esError() {
			return esError;
		}
		public void setEsError(boolean esError) {
			this.esError = esError;
		}
		public byte[] getFichero() {
			return fichero;
		}
		public void setFichero(byte[] fichero) {
			this.fichero = fichero;
		}
		public String getDatosArchivo() {
			return datosArchivo;
		}
		public void setDatosArchivo(String datosArchivo) {
			this.datosArchivo = datosArchivo;
		}
		public String getError() {
			return error;
		}
		public void setError(String error) {
			this.error = error;
		}
		public String getIdentificador() {
			return identificador;
		}
		public void setIdentificador(String identificador) {
			this.identificador = identificador;
		}
		public String getTipoElemento() {
			return tipoElemento;
		}
		public void setTipoElemento(String tipoElemento) {
			this.tipoElemento = tipoElemento;
		}
		public String getTipoArchivo() {
			return tipoArchivo;
		}
		public void setTipoArchivo(String tipoArchivo) {
			this.tipoArchivo = tipoArchivo;
		}
		public String getFechaGeneracion() {
			return fechaGeneracion;
		}
		public void setFechaGeneracion(String fechaGeneracion) {
			this.fechaGeneracion = fechaGeneracion;
		}
		public String getNombreFichero() {
			return nombreFichero;
		}
		public void setNombreFichero(String nombreFichero) {
			this.nombreFichero = nombreFichero;
		}
		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
		public String getHash() {
			return hash;
		}
		public void setHash(String hash) {
			this.hash = hash;
		}
		public String getCsv() {
			return csv;
		}
		public void setCsv(String csv) {
			this.csv = csv;
		}
		public String getMetadatos() {
			return metadatos;
		}
		public void setMetadatos(String metadatos) {
			this.metadatos = metadatos;
		}
		public String getFuncionResumen() {
			return funcionResumen;
		}
		public void setFuncionResumen(String funcionResumen) {
			this.funcionResumen = funcionResumen;
		}
		public String getTipoFirma() {
			return tipoFirma;
		}
		public void setTipoFirma(String tipoFirma) {
			this.tipoFirma = tipoFirma;
		}
		public String getFechaValidezCertificado() {
			return fechaValidezCertificado;
		}
		public void setFechaValidezCertificado(String fechaValidezCertificado) {
			this.fechaValidezCertificado = fechaValidezCertificado;
		}		
		/* INIPETITRIBUTAS-88 MCMARCO ** 05/05/2020 ** Formato XML del expediente electrónico */
		public Metadatos getMetadatosClass() {
			return metadatosClass;
		}
		public void setMetadatosClass(Metadatos metadatosClass) {
			this.metadatosClass = metadatosClass;
		}	
		/* FINPETITRIBUTAS-88 MCMARCO ** 05/05/2020 ** Formato XML del expediente electrónico */
	}
	/**
	 * No se puede invocar para crear un objeto
	 * @param pref
	 * @param idLlamada
	 */
	private MediadorArchivoDigital(Preferencias pref, String idLlamada) {
		this.pref= pref;
		this.idLlamada= idLlamada;
	}
	/**
	 * Método para crear un nuevo objeto de  esta clase
	 * @param pref
	 * @param idLlamada
	 * @return
	 */
	public static MediadorArchivoDigital newInstance (Preferencias pref, String idLlamada) {
		return new MediadorArchivoDigital(pref, idLlamada);
	}
	
	/**
	 * Inicializa la interfaz de acceso a archivo digital
	 */
	@SuppressWarnings("unchecked")
	private final void inicializaInterfaz(){
		ArchivoDigital_Service srv = new ArchivoDigital_Service();
		port= srv.getArchivoDigitalSOAP();
		String endpoint= pref.getEndpointArchivoDigital();
		BindingProvider bprovider= (BindingProvider) port;
		if (!"".equals(endpoint)) {
			bprovider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
		}
		Binding bi= bprovider.getBinding();
		List<Handler> manejadores= bi.getHandlerChain();
		if (manejadores==null){
			manejadores= new ArrayList<Handler>();
		}
		manejadores.add(new SoapClientHandler(this.idLlamada));
		bi.setHandlerChain(manejadores);
	}
	/**
	 * Envía un documento a custodia.
	 * @param codUsuario
	 * @param identificador
	 * @param tipoElemento
	 * @param nombreFichero
	 * @param comprimido
	 * @param contenido
	 * @param hashArchivo
	 * @param metadatos
	 * @return Resultado de la custodia, como objeto interno
	 */
	public ResultadoCustodia custodiar(String codUsuario, String identificador, String tipoElemento, String nombreFichero,
				String comprimido, byte[] contenido, String hashArchivo, String metadatos, String firmaExterna,String firmaCSV, String firmante, String firmaCertificado, String ltv) {
		
		Holder<String> error= new Holder<String>();
		Holder<Integer> idArchivo= new Holder<Integer>();
		Holder<String> csv= new Holder<String>();
		Holder<String> hash= new Holder<String>();
		ResultadoCustodia resul= new ResultadoCustodia();
		inicializaInterfaz();
		FirmaType firmaType=null;
		if ("S".equalsIgnoreCase(firmaCSV) ||  "S".equalsIgnoreCase(firmaCertificado) || "S".equalsIgnoreCase(firmaExterna)) {
			firmaType= new FirmaType();
			if ("S".equalsIgnoreCase(firmaCSV)) {
				CSVType csvType= new CSVType();
				csvType.setFirmaCSV(firmaCSV);
				csvType.setFirmante(firmante);
				firmaType.setCSV(csvType);
			} 
			if ("S".equalsIgnoreCase(firmaCertificado) || "S".equalsIgnoreCase(firmaExterna)) {
				CertificadoType certificadoType= new CertificadoType();
				certificadoType.setFirmaCertificado(firmaCertificado);
				certificadoType.setLTV(ltv);
				firmaType.setFirmaExterna(firmaExterna);
				firmaType.setCertificado(certificadoType);
			}
		}
		
		port.custodia(codUsuario, 
					  identificador, 
					  tipoElemento, 
					  nombreFichero, 
					  comprimido, 
					  contenido, 
					  hashArchivo, 
					  metadatos, 
					  firmaType,
					  idArchivo, 
					  csv, 
					  hash,
					  error);
		//Condiciones de error.
		if (idArchivo.value==0) {
			resul.setEsError(true);
			resul.setMensaje(error.value);
		} else {
			resul.setEsError(false);
			resul.setIdDocumento(idArchivo.value.intValue());
			resul.setCsv(csv.value);
		}
		return resul;
	}

	/**
	 * Recupera datos del archivo digital, que vienen como XML en texto
	 * @param datosArchivo
	 * @param r
	 * @throws AdviserException
	 */
	private void recuperaDatosArchivo (String datosArchivo, ResultadoConsultaArchivoDigital r) throws AdviserException{
		try 
		{
			Document doc=XMLDOMUtils.parseXml(datosArchivo);
			r.setIdentificador(XMLDOMUtils.selectSingleNodeText(doc, "/ROWSET/ROW/Identificador"));
			r.setTipoElemento(XMLDOMUtils.selectSingleNodeText(doc, "/ROWSET/ROW/TipoElemento"));
			r.setTipoArchivo(XMLDOMUtils.selectSingleNodeText(doc, "/ROWSET/ROW/TipoArchivo"));
			r.setFechaGeneracion(XMLDOMUtils.selectSingleNodeText(doc, "/ROWSET/ROW/FechaGeneracion"));
			r.setNombreFichero(XMLDOMUtils.selectSingleNodeText(doc, "/ROWSET/ROW/NombreFichero"));
			String tamanio= XMLDOMUtils.selectSingleNodeText(doc, "/ROWSET/ROW/Tamanyo");
			if (tamanio!=null) {
				r.setSize(Integer.parseInt(tamanio));
			}			
			r.setHash(XMLDOMUtils.selectSingleNodeText(doc, "/ROWSET/ROW/HashFichero"));
			r.setCsv(XMLDOMUtils.selectSingleNodeText(doc, "/ROWSET/ROW/csv"));
			r.setMetadatos(XMLDOMUtils.selectSingleNodeText(doc, "/ROWSET/ROW/Metadatos"));
			if(r.getMetadatos()!=null)
			{
				Metadatos meta = new Metadatos();
				Document docMeta=XMLDOMUtils.parseXml("<root>" + XMLDOMUtils.selectSingleNode(doc, "/ROWSET/ROW/Metadatos").getTextContent() + "</root>");
				meta.setVersionNTI(XMLDOMUtils.selectSingleNodeText(docMeta, "/root/versionNTI"));	
				meta.setFechaAlta(r.getFechaGeneracion());
				meta.setOrigen(XMLDOMUtils.selectSingleNodeText(docMeta, "/root/origen"));
				meta.setEstadoElaboracion(XMLDOMUtils.selectSingleNodeText(docMeta, "/root/estadoElaboracion"));
				meta.setTipoArchivo(r.getTipoArchivo());
				meta.setTipoDocumental(XMLDOMUtils.selectSingleNodeText(docMeta, "/root/tipoDocumental"));
				meta.setIdentificador(XMLDOMUtils.selectSingleNodeText(docMeta, "/root/identificador"));
				r.setMetadatosClass(meta);
			}
			r.setFuncionResumen(XMLDOMUtils.selectSingleNodeText(doc, "/ROWSET/ROW/FuncionHash"));
			r.setTipoFirma(XMLDOMUtils.selectSingleNodeText(doc, "/ROWSET/ROW/TipoFirma"));
			r.setFechaValidezCertificado(XMLDOMUtils.selectSingleNodeText(doc, "/ROWSET/ROW/FechaValidezCertificado"));
		} catch (XMLDOMDocumentException x)  {
			throw new AdviserException ("Error al interpretar los datos del archivo digital:" + x.getMessage());
		}
	}
	/**
	 * Recupera el contenido de un fichero según su id, o bien nulo si no se encuentra ningún
	 * fichero con ese identificador
	 * @param usuario Código de usuario que va a recuperar el archivo
	 * @param id  Identificador del archivo
	 * @return  contenido del archivo
	 */
	public ResultadoConsultaArchivoDigital obtenerArchivoPorId(String usuario, int id, String obtenerContenidoDocumento) throws AdviserException{
		inicializaInterfaz();
		Holder<byte[]> fichero= new Holder<byte[]>();
		Holder<String> datosArchivo= new Holder<String>();
		Holder<String> error = new Holder<String>();
		String obtenerSoloDatos="N";
		if (obtenerContenidoDocumento!=null && "N".equalsIgnoreCase(obtenerContenidoDocumento)){
			obtenerSoloDatos="S";
		}
		port.obtieneArchivoPorId(usuario, id, obtenerSoloDatos,fichero, datosArchivo, error);
		ResultadoConsultaArchivoDigital r = new ResultadoConsultaArchivoDigital();
		if (error.value!=null && !"".equals(error.value)) {
			r.setEsError(true);
			r.setError(error.value);
		} else {
			r.setEsError(false);
			recuperaDatosArchivo(datosArchivo.value,r);
		}
		r.setDatosArchivo(datosArchivo.value);
		r.setFichero(fichero.value);
		return r;
	}
	
	
	 /**
     * 
     * @param error
     * @param idArchivo
     * @param metadatos
     * @param datosArchivo
     * @param codigoUsuario
     */
    public ResultadoConsultaArchivoDigital guardarMetadatos(String codigoUsuario, int idArchivo, String metadatos) {
    	Holder<String> datosArchivo= new Holder<String>();
		Holder<String> error = new Holder<String>();		
		ResultadoConsultaArchivoDigital r = new ResultadoConsultaArchivoDigital();
		inicializaInterfaz();
		try
		{
			port.guardarMetadatos(codigoUsuario, idArchivo, metadatos, datosArchivo, error);
			if (error!= null && error.value!=null && !"".equals(error.value)) {
				r.setEsError(true);
				r.setError(error.value);
			} else {
				r.setEsError(false);
				recuperaDatosArchivo(datosArchivo.value,r);
				r.setDatosArchivo(datosArchivo.value);
			}				
		} 
		catch (Exception e)
		{
			String datosError = " Codigo Usuario: " + codigoUsuario + " IdAdar: " + idArchivo; 
			r.setEsError(true);
			r.setError("ERROR al guardar los metadatos, datos: " + datosError + "Exception: " + e.toString() + " Error: " + error.value);			
		}		
		return r;
    }
}
