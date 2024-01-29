package es.tributasenasturias.docs;

import java.io.OutputStream;
import java.rmi.RemoteException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import es.tributasenasturias.Exceptions.PresentacionException;
import es.tributasenasturias.documentos.DatosSalidaImpresa;
import es.tributasenasturias.documentos.util.NumberUtil;
import es.tributasenasturias.documentos.util.VerificacionUtils;
import es.tributasenasturias.documentos.util.XMLUtils;
import es.tributasenasturias.pagopresentacionmodelo600utils.ConversorParametrosLanzador;
import es.tributasenasturias.pagopresentacionmodelo600utils.Preferencias;
import es.tributasenasturias.pagopresentacionmodelo600utils.Utils;
import es.tributasenasturias.webservice.pagopresentacion.log.ILoggable;
import es.tributasenasturias.webservice.pagopresentacion.log.LogHelper;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPL;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLService;

public class JustificanteCobro extends PdfBase implements ILoggable{

	private String numeroAutoliquidacion = "";
	private String codVerificacion = "";
	private String hashVerificacion="";
	private String docCumplimentado = "";
	private ConversorParametrosLanzador cpl;
	
	//Log
	private LogHelper log;
	private Preferencias pref = new Preferencias();
	
	
	protected JustificanteCobro(String p_numAutoliq)  throws Exception{
		pref.CargarPreferencias();			
		Session.put("cgestor", "");
		plantilla = "recursos//impresos//xml//justificanteCobroInternet2.xml";
		this.numeroAutoliquidacion = p_numAutoliq;
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
				lineas[17]=XMLUtils.selectSingleNode(rsCanu[0], "STRING4_CANU").getTextContent();
				lineas[18]="NIF CONTRIBUYENTE"; 
				lineas[19]=":"; 
				lineas[20]=XMLUtils.selectSingleNode(rsCanu[1], "STRING3_CANU").getTextContent();	
				lineas[21]="NRC"; 
				lineas[22]=":"; 
				lineas[23]=XMLUtils.selectSingleNode(rsCanu[1], "STRING4_CANU").getTextContent();
				lineas[24]="Cód. verificación"; 
				lineas[25]=":";
				String nifSP = XMLUtils.selectSingleNode(rsCanu[1], "STRING3_CANU").getTextContent();
				String justificante= XMLUtils.selectSingleNode(rsCanu[0], "STRING4_CANU").getTextContent();
				VerificacionUtils.CodigoVerificacion codigo= VerificacionUtils.codigoVerificacion("P",justificante,nifSP);
				this.codVerificacion = codigo.getCodigoVerificacion();
				this.hashVerificacion = codigo.getHashCodVerificacion();
				//this.codVerificacion = codigoVerificacion("P"+XMLUtils.selectSingleNode(rsCanu[0], "STRING4_CANU").getTextContent()+XMLUtils.selectSingleNode(rsCanu[1], "STRING3_CANU").getTextContent()); 
				//lineas[26]="P"+XMLUtils.selectSingleNode(rsCanu[0], "STRING4_CANU").getTextContent()+"-"
				//			+this.codVerificacion;
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

	/* No usado, se recupera la clave de la base de datos */
	/*private String codigoVerificacion(String valor) throws Exception{
			String resultado = SHAUtils.hex_hmac_sha1("clave               ", valor);
			return resultado.substring(resultado.length() -16, resultado.length());
	}*/
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
