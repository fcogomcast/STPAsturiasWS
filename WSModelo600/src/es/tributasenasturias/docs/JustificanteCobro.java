package es.tributasenasturias.docs;

import java.io.OutputStream;
import java.rmi.RemoteException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import es.tributasenasturias.exception.PresentacionException;
import es.tributasenasturias.documentos.DatosSalidaImpresa;
import es.tributasenasturias.documentos.util.NumberUtil;
import es.tributasenasturias.documentos.util.XMLUtils;
import es.tributasenasturias.modelo600utils.ConversorParametrosLanzador;
import es.tributasenasturias.modelo600utils.Preferencias;
import es.tributasenasturias.modelo600utils.Utils;
import es.tributasenasturias.modelo600utils.VerificacionUtils;
import es.tributasenasturias.modelo600utils.log.ILoggable;
import es.tributasenasturias.modelo600utils.log.LogHelper;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPL;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLService;

public class JustificanteCobro extends PdfBase implements ILoggable{

	private String numeroAutoliquidacion = "";
	private String codVerificacion = "";
	private String hashVerificacion ="";
	private String docCumplimentado = "";
	private ConversorParametrosLanzador cpl;
	
	//Datos del justificante
	private String fechaOperacion;
	private String codEntidad;
	private String importe;
	private String nifContribuyente;
	private String numOperacion;
	private String nrc;
	
	//Log
	private LogHelper log;
	private Preferencias pref = new Preferencias();
	
	
	protected JustificanteCobro(String p_numAutoliq)  throws Exception{
		pref.CargarPreferencias();			
		Session.put("cgestor", "");
		plantilla = "recursos//impresos//xml//justificanteCobroInternet2.xml";
		this.numeroAutoliquidacion = p_numAutoliq;
		cargarInformacion();
	}
	
	public String getPlantilla() {
		return plantilla;
	}

	public void compila(String id, String xml, String xsl, OutputStream output) throws RemoteException {
		try {
			justificanteCobroInternet(id, xml, xsl, output);
		} catch (Exception e) {
			throw new RemoteException ("Error al compilar el documento de justificante de cobro.");
		}
		
	}
	
	public void justificanteCobroInternet(String id, String xml, String xsl, OutputStream output) throws PresentacionException{
		
		DatosSalidaImpresa s = new DatosSalidaImpresa(xml, xsl, output);
		s.setLogger(log);
		// llamar al servicio que obtiene los datos a mostrar.
		recuperarDatosJustificanteCobro();
		if (!cpl.getResultado().equals(null)) {
			// una vez obtenidos los datos y comprobado que la respuesta no es nula
			// se debe inluir el xml con los datos en la base de datos zipped
			// y se pinta el pdf.
			try {

				Document docRespuesta = (Document) XMLUtils.compilaXMLObject(cpl.getResultado(), null);
				
				Element[] rsCanu = XMLUtils.selectNodes(docRespuesta, "//estruc[@nombre='CANU_CADENAS_NUMEROS']/fila");
				String nifSP=XMLUtils.selectSingleNode(rsCanu[1], "STRING3_CANU").getTextContent();
				String autoliquidacion= XMLUtils.selectSingleNode(rsCanu[0], "STRING4_CANU").getTextContent();
				String lineas[] = new String[27];
				lineas[0]="FECHA OPERACIÓN";
				lineas[1]=":"; 
				lineas[2]=XMLUtils.selectSingleNode(rsCanu[0], "STRING1_CANU").getTextContent()+" "+XMLUtils.selectSingleNode(rsCanu[0], "STRING2_CANU").getTextContent();
				lineas[3]="CÓDIGO ENTIDAD"; 
				lineas[4]=":"; 
				lineas[5]=XMLUtils.selectSingleNode(rsCanu[0], "STRING3_CANU").getTextContent()+" - Servicios Tributarios del Principado de Asturias";
				lineas[6]="MODELO"; 
				lineas[7]=":"; 
				lineas[8]="600. Autoliquidación del Impuesto sobre Transmisiones Patrimoniales y Actos Jurídicos Documentados";
				lineas[9]="IMPORTE"; 
				lineas[10]=":";
				lineas[11]= NumberUtil.getImporteFormateado(XMLUtils.selectSingleNode(rsCanu[0], "STRING5_CANU").getTextContent())+"€";
				lineas[12]="Nº OPERACIÓN"; 
				lineas[13]=":"; 
				lineas[14]=XMLUtils.selectSingleNode(rsCanu[1], "STRING1_CANU").getTextContent();
				lineas[15]="Nº AUTOLIQUIDACIÓN"; 
				lineas[16]=":"; 
				lineas[17]= autoliquidacion;
				lineas[18]="NIF CONTRIBUYENTE"; 
				lineas[19]=":"; 
				lineas[20]=nifSP;	
				lineas[21]="NRC"; 
				lineas[22]=":"; 
				lineas[23]=XMLUtils.selectSingleNode(rsCanu[1], "STRING4_CANU").getTextContent();
				lineas[24]="Cód. verificación"; 
				lineas[25]=":";
				VerificacionUtils.CodigoVerificacion codigo= VerificacionUtils.codigoVerificacion("P",autoliquidacion,nifSP); 
				this.codVerificacion = codigo.getCodigoVerificacion();
				this.hashVerificacion = codigo.getHashCodVerificacion();
				lineas[26]=this.codVerificacion;
				
				int desplazamiento = 23;
				s.Fila("linea", lineas, desplazamiento);
				s.Campo("fechaCobro", "PAGADO EL "+XMLUtils.selectSingleNode(rsCanu[0], "STRING1_CANU").getTextContent());

			} catch (Exception e) {
				throw new PresentacionException ("Error al generar el justificante de cobro:" + e.getMessage(),e);
			}
		}
		
		s.Mostrar();
		this.docCumplimentado = Utils.DOM2String(s.getXmlDatos());
		
	}
	
	public String getDocXml() {
		return this.docCumplimentado;
	}
	
	public String getCodVerificacion () {
		return this.codVerificacion;
	}
	
	public String getHashVerificacion() {
		return hashVerificacion;
	}

	private void recuperarDatosJustificanteCobro () throws PresentacionException{
		try {
			// llamar al servicio PXLanzador para ejecutar el procedimiento almacenado de alta de expediente
			// Se prepara la llamada al procedimiento almacenado
			cpl = new ConversorParametrosLanzador();

	        cpl.setProcedimientoAlmacenado("PROGRAMAS_AYUDA4.obtenerDatosPago");
	        // conexion
	        cpl.setParametro("1",ConversorParametrosLanzador.TIPOS.Integer);
	        // peticion
	        cpl.setParametro("1",ConversorParametrosLanzador.TIPOS.Integer);
	        // usuario
	        cpl.setParametro("USU_WEB_SAC",ConversorParametrosLanzador.TIPOS.String);
	        // organismo
	        cpl.setParametro("33",ConversorParametrosLanzador.TIPOS.Integer);	        
	        // numAutoliquidacion
	        cpl.setParametro(this.numeroAutoliquidacion,ConversorParametrosLanzador.TIPOS.String);
	        // conexion oracle
	        cpl.setParametro("P",ConversorParametrosLanzador.TIPOS.String);            
	        
	        LanzaPLService lanzaderaWS = new LanzaPLService();
	        LanzaPL lanzaderaPort;
			lanzaderaPort =lanzaderaWS.getLanzaPLSoapPort();
	        
			// enlazador de protocolo para el servicio.
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort; 
			// Cambiamos el endpoint
			bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointLanzador()); 
	            
			
	        String respuesta = new String();
        	respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl.Codifica(), "", "", "", "");
        	cpl.setResultado(respuesta);
		} catch (Exception e) {
			throw new PresentacionException ("Excepción al recuperar los datos del justificante de cobro:" + e.getMessage(),e);
		}
	}
	/**
	 * Carga la información del justificante en las variables internas.
	 * @throws PresentacionException
	 */
	private void cargarInformacion() throws PresentacionException
	{
		recuperarDatosJustificanteCobro();
		if (!cpl.getResultado().equals(null)) {
			try {

				Document docRespuesta = (Document) XMLUtils.compilaXMLObject(cpl.getResultado(), null);
				
				Element[] rsCanu = XMLUtils.selectNodes(docRespuesta, "//estruc[@nombre='CANU_CADENAS_NUMEROS']/fila");
				fechaOperacion=XMLUtils.selectSingleNode(rsCanu[0], "STRING1_CANU").getTextContent()+" "+XMLUtils.selectSingleNode(rsCanu[0], "STRING2_CANU").getTextContent();
				codEntidad=XMLUtils.selectSingleNode(rsCanu[0], "STRING3_CANU").getTextContent()+" - Servicios Tributarios del Principado de Asturias";
				importe= NumberUtil.getImporteFormateado(XMLUtils.selectSingleNode(rsCanu[0], "STRING5_CANU").getTextContent())+"€";
				numOperacion=XMLUtils.selectSingleNode(rsCanu[1], "STRING1_CANU").getTextContent();
				nifContribuyente=XMLUtils.selectSingleNode(rsCanu[1], "STRING3_CANU").getTextContent();	
				nrc=XMLUtils.selectSingleNode(rsCanu[1], "STRING4_CANU").getTextContent();

			} catch (Exception e) {
				throw new PresentacionException ("Error al recuperar datos del justificante:" + e.getMessage(),e);
			}
		}
		
	}
	
	public String getFechaOperacion() {
		return fechaOperacion;
	}

	public void setFechaOperacion(String fechaOperacion) {
		this.fechaOperacion = fechaOperacion;
	}

	public String getCodEntidad() {
		return codEntidad;
	}

	public void setCodEntidad(String codEntidad) {
		this.codEntidad = codEntidad;
	}

	public String getImporte() {
		return importe;
	}

	public void setImporte(String importe) {
		this.importe = importe;
	}
    
	public String getNifContribuyente() {
		return nifContribuyente;
	}

	public void setNifContribuyente(String nifContribuyente) {
		this.nifContribuyente = nifContribuyente;
	}

	public String getNumOperacion() {
		return numOperacion;
	}

	public void setNumOperacion(String numOperacion) {
		this.numOperacion = numOperacion;
	}
	

	public String getNrc() {
		return nrc;
	}

	public void setNrc(String nrc) {
		this.nrc = nrc;
	}

	@Override
	public void setLogger(LogHelper log)
	{
		this.log = log;
	}
	@Override 
	public LogHelper getLogger()
	{
		return log;
	}
}
