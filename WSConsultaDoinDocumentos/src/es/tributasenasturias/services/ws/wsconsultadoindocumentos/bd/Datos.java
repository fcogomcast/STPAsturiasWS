package es.tributasenasturias.services.ws.wsconsultadoindocumentos.bd;


import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import stpa.services.LanzaPL;
import stpa.services.LanzaPLService;

//import es.tributasenasturias.services.client.lanzador.ParamType;
//import es.tributasenasturias.services.client.lanzador.TLanzador;
//import es.tributasenasturias.services.client.lanzador.TParam;
import es.tributasenasturias.services.ws.wsconsultadoindocumentos.utils.Base64;
import es.tributasenasturias.services.ws.wsconsultadoindocumentos.utils.ConversorParametrosLanzador;
import es.tributasenasturias.services.ws.wsconsultadoindocumentos.utils.PdfComprimidoUtils;
import es.tributasenasturias.services.ws.wsconsultadoindocumentos.utils.SalidaConsulta;
import es.tributasenasturias.services.ws.wsconsultadoindocumentos.utils.Log.Logger;
import es.tributasenasturias.services.ws.wsconsultadoindocumentos.utils.Preferencias.Preferencias;


public class Datos {
	private Preferencias preferencias;
	private Logger logger = null;
	//private TLanzador lanzadorWS = null;
	private ConversorParametrosLanzador cpl;

	// Definición de Constantes
	private final String STRING_CADE = "STRING_CADE";
	private final String STRING_PDF = "pdf";


	public Datos() {

		logger = new Logger();
		try {
			logger.debug("INICIO CONSTRUCTOR DE DATOS");
			
			preferencias = Preferencias.getPreferencias();

			logger.debug("FIN CONSTRUCTOR DE DATOS");
		} catch (Exception ex) {
			logger.error("En constructor de clase Datos: " + ex.getMessage());
		}
	}

	/**
	 * Construye el lanzador
	 * 
	 * @param nomProc
	 * @return
	 */
	/*private void InicializarLanzador(String nomProc) {
		logger.trace("INICIO INICIALIZARLANZADOR");
		
		String endPointISL = preferencias.getEndPointISL();
		lanzadorWS = new TLanzador();
		lanzadorWS.setEndPoint(endPointISL);
		lanzadorWS.getPeticion().setProcName(nomProc);
		
		logger.trace("Inicializado Lanzador con EndpointISL " + endPointISL
				+ " y nombre procedimiento = " + nomProc);
	}*/


	/**
	 * Obtiene el contenido del documento identificado por el nombre y el tipo
	 * @param nombre
	 * @param tipo
	 * @return
	 */
	public SalidaConsulta GetDocumentoDoin(String nombre, String tipo) throws Exception{
		logger.debug("ENTRADA GETDOCUMENTODOIN CON PARAMETROS Nombre: "
				+ nombre + " y tipo: " + tipo);

		/*
		// Establecemos el nombre del procedimiento
		InicializarLanzador(preferencias.getNomProcedimientoRecuperarDoc());

		// Establecemos los parámetros
		lanzadorWS.getPeticion().addParam(
				new TParam(ParamType.CADENA, nombre, ""));
		lanzadorWS.getPeticion().addParam(
				new TParam(ParamType.CADENA, tipo, ""));
		
		logger.trace("WSDL " + preferencias.getEndPointISL());
		logger.trace("Procedimiento " + lanzadorWS.getPeticion().getProcName());
		logger.trace("Peticion: " + lanzadorWS.getPeticion().toXml());
		logger.trace("EsquemaBD: " + preferencias.getEsquemaBaseDatos());
		logger.trace("IP_Lanzador: " + preferencias.getIPLanzador());
*/
		LanzaPLService lanzaderaWS = new LanzaPLService();
		LanzaPL lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
		try{
			//Enlazador de protocolo para el servicio.
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;
			//Cambiamos el endpoint
			logger.debug("Se actualiza el endpoint a:"+preferencias.getEndPointISL());
			bpr.getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,preferencias.getEndPointISL());
			cpl = new ConversorParametrosLanzador();
			cpl.setProcedimientoAlmacenado(preferencias.getNomProcedimientoRecuperarDoc());
			cpl.setParametro(nombre, ConversorParametrosLanzador.TIPOS.String); 
			cpl.setParametro(tipo, ConversorParametrosLanzador.TIPOS.String); 
		}catch (javax.xml.parsers.ParserConfigurationException e){
			logger.error("Fallo al preparar la consulta:"+e.getMessage());
			throw new Exception ("Fallo al preparar la consulta:" + e.getMessage());
		}

		String resultadoEjecutarPL="";
		try{
			//Recogemos el resultado (->xml) y parseamos el nodo que nos interesa
			resultadoEjecutarPL = lanzaderaPort.executePL(preferencias.getEsquemaBaseDatos(), cpl.Codifica(),
				preferencias.getIPLanzador(), "", "", "");
			//logger.trace("Procedimiento " + lanzadorWS.getPeticion().getProcName());
		}catch (Exception e){
			logger.error("Fallo al ejecutar la consulta:"+e.getMessage());
			throw new Exception ("Fallo al ejecutar la consulta:" + e.getMessage());
		}
		logger.debug("Resultado: " + resultadoEjecutarPL);
		
		SalidaConsulta resultadoConsulta = new SalidaConsulta();
		resultadoConsulta = ParsearResultado(resultadoEjecutarPL);
		
		return resultadoConsulta;

	}
	
	/**
	 * Guarda el contenido del documento
	 * @param nombre
	 * @param tipo
	 * @return
	 * @throws Exception 
	 */
	public SalidaConsulta SetDocumentoDoin(String nombre, String tipo, String codVerif, String nifSp, String nifPr, String doc, String tipoDoc, String idSesion,boolean comprimir) throws Exception {
		logger.debug("ENTRADA SETDOCUMENTODOIN");


		LanzaPLService lanzaderaWS = new LanzaPLService();
		LanzaPL lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
		try{
			//Llamar al servicio PXLanzador para ejecutar el procedimiento almacenado de alta de expediente
			cpl = new ConversorParametrosLanzador();			
			cpl.setProcedimientoAlmacenado(preferencias.getPAAltaDocumento());
			cpl.setParametro(tipo, ConversorParametrosLanzador.TIPOS.String); // tipo
			cpl.setParametro(nombre,ConversorParametrosLanzador.TIPOS.String); // nombre
			cpl.setParametro(codVerif,ConversorParametrosLanzador.TIPOS.String); // codigo verificacion
			cpl.setParametro(nifSp, ConversorParametrosLanzador.TIPOS.String); // sp
			cpl.setParametro(nifPr, ConversorParametrosLanzador.TIPOS.String); // presentador
			cpl.setParametro(idSesion, ConversorParametrosLanzador.TIPOS.String); // sesion
			cpl.setParametro("P", ConversorParametrosLanzador.TIPOS.String); // origen
			cpl.setParametro("", ConversorParametrosLanzador.TIPOS.String); // libre
			cpl.setParametro("S", ConversorParametrosLanzador.TIPOS.String); // publicable = 'S'
	
			//Guardar documento (comprimido o no)
			cpl.setParametro(this.ComprimeYcodifica(doc, tipoDoc, comprimir), ConversorParametrosLanzador.TIPOS.Clob);
	
			//Tipo Document XML o PDF
			cpl.setParametro(tipoDoc,ConversorParametrosLanzador.TIPOS.String);
			cpl.setParametro("P", ConversorParametrosLanzador.TIPOS.String); // con oracle = 'P'
	
			//Enlazador de protocolo para el servicio.
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;
			//Cambiamos el endpoint
			bpr.getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,preferencias.getEndPointISL());
		}catch (javax.xml.parsers.ParserConfigurationException e){
			logger.error("Fallo al preparar el alta:"+e.getMessage());
			throw new Exception ("Fallo al preparar el alta:" + e.getMessage());
		}

		String respuesta = new String();
		try{
			respuesta = lanzaderaPort.executePL(preferencias.getEsquemaBaseDatos(), cpl.Codifica(), "", "", "", "");
			cpl.setResultado(respuesta);
		}catch (Exception e){
			logger.error("Fallo al ejecutar el alta:"+e.getMessage());
			throw new Exception ("Fallo al ejecutar el alta:" + e.getMessage());
		}
		SalidaConsulta resultadoConsulta = ParsearResultado(respuesta);
		
		logger.debug("SALIDA SETDOCUMENTODOIN");
		return resultadoConsulta;
	}

	private String ComprimeYcodifica(String documento, String tipoDoc, boolean comprimir) throws Exception {
		logger.trace("Inicio ComprimeYcodifica");
		try {
			if (comprimir && tipoDoc != "XML") {
				byte[] docDecodificado = Base64.decode(documento.toCharArray());
				String documentzippeado = new String();
				ByteArrayOutputStream resulByteArray = new ByteArrayOutputStream();
				resulByteArray.write(docDecodificado);
				documentzippeado = PdfComprimidoUtils.comprimirPDF(resulByteArray);
				return documentzippeado;
			} else {
				return documento;
			}
		} catch (Exception e) {
			logger.error("Error ComprimeYcodifica:"+e.getMessage());
			throw e;
		} finally {
			logger.trace("Fin ComprimeYcodifica");
		}
	}	
	
	/**
	 * Establece los valores de retorno en función de los obtenidos
	 * @param resultado
	 * @return
	 */
	private SalidaConsulta ParsearResultado(String resultado) {
		logger.debug("INI PARSEARRESULTADO");
		SalidaConsulta resultadoParseado = new SalidaConsulta();
		try {

			String result = GetValorNodo(resultado, STRING_CADE);
			logger.debug("Código: " +  result);
			// Parseamos el resultado
			if (result.equals("00")) {
				resultadoParseado.setDocumento(GetValorNodo(resultado,
						STRING_PDF));
				resultadoParseado.setError(preferencias.getCodResultadoOK());
				resultadoParseado
						.setResultado(preferencias.getMsgResultadoOK());
				logger.trace("Recuperado documento correctamente. Valor: "
						+ resultadoParseado.getResultado());

			}

			else if (result.equals("01")) {
				resultadoParseado.setDocumento("");
				resultadoParseado.setError(preferencias
						.getCodResultadoNoDataFound());
				resultadoParseado.setResultado(preferencias
						.getMsgResultadoNoDataFound());
				logger.debug("No existen datos para la consulta realizada.");

			} else {
				resultadoParseado.setDocumento("");
				resultadoParseado.setError(preferencias.getCodResultadoNoOK());
				resultadoParseado.setResultado(preferencias
						.getMsgResultadoNoOK());
				logger.debug("Recuperación de documento errónea.");
			}

			return resultadoParseado;
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			return resultadoParseado;
		} finally {
			logger.debug("FIN GETDOCUMENTODOIN");
		}
	}

	
	/**
	 * Obtiene el valor del nodoABuscar dentro del xmlParsear
	 * @param xmlParsear
	 * @param nodoABuscar
	 * @return
	 */
	private String GetValorNodo(String xmlParsear, String nodoABuscar) {
		logger.debug("INICIO GETVALORNODO");
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlParsear));

			Document doc = db.parse(is);
			
			// Obtenemos el valor del nodo
			NodeList nodes = doc.getElementsByTagName(nodoABuscar);
			logger.debug("NumElementos " + nodoABuscar + ": "+ nodes.getLength());
			String resultado = "";
			if (nodes.getLength() > 0)
				// Sólo hay un nodo de ese tipo
				resultado = (nodes.item(0).getFirstChild().getNodeValue());
			else
				resultado = "";

			logger.debug("Resultado " + this.STRING_CADE + ": " + resultado);
			logger.debug("FIN GETVALORNODO");
			return resultado;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "";
		}
	}


}
