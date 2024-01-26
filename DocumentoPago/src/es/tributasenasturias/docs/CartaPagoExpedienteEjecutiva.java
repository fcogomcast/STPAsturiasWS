package es.tributasenasturias.docs;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

import es.tributasenasturias.documentopagoutils.Base64;
import es.tributasenasturias.documentopagoutils.DatosSalidaImpresa;
import es.tributasenasturias.documentopagoutils.Logger;
import es.tributasenasturias.documentopagoutils.PdfComprimidoUtils;
import es.tributasenasturias.documentopagoutils.Preferencias;
import es.tributasenasturias.documentos.util.ConversorParametrosLanzador;
import es.tributasenasturias.documentos.util.XMLUtils;
import es.tributasenasturias.soap.handler.HandlerUtil;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivo;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLMasivoService;

/**
 * 
 * @author crubencvs
 *
 */
public class CartaPagoExpedienteEjecutiva extends PdfBase{

	private Preferencias pref = new Preferencias();
	private String ideper;
	private String tipo;
	private int importe;
	private String nifContribuyente;
	private String referenciaCobro;
	
	private ConversorParametrosLanzador cpl;
	
	public String getReferencia() {
		return this.referenciaCobro;
	}
	
	public String getNifContribuyente() {
		return this.nifContribuyente;
	}
	private CartaPagoExpedienteEjecutiva (String ideper, String tipo, int importe) throws Exception
	{
		pref.CargarPreferencias();
		this.ideper= ideper;
		this.tipo= tipo;
		this.importe= importe;
		cpl= new ConversorParametrosLanzador();
		this.plantilla = pref.getPlantillaCartaPagoExpediente();
		session.put("cgestor", "");
	}
	
	public static CartaPagoExpedienteEjecutiva getInstance(String ideper, String tipo, int importe) throws Exception{
		return new CartaPagoExpedienteEjecutiva(ideper, tipo, importe);
	}
	@Override
	public void compila(String id, String xml, String xsl, OutputStream output)
			throws RemoteException {
		try {			
			generarInforme(id, xml, xsl, output);			
		} catch (Exception e) {
			Logger.error(e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
	}

	@Override
	public String getPlantilla() {
		return this.plantilla;
	}
	
	/**
	 * Equivalente a la función "campo" de javascript
	 * @param nodo
	 * @param nombreCampo
	 * @return
	 */
	public String campo(Element nodo, String nombreCampo) {
		if (nodo==null || nombreCampo==null || "".equals(nombreCampo)) {
			return "";
		}
		//Vienen siempre en mayúsculas
		Element n=XMLUtils.selectSingleNode(nodo, nombreCampo.toUpperCase());
		if (n!=null) {
			return n.getTextContent();
		} else  {
			return "";
		}
	}
	
	/**
	 * Equivalente al método campoFecha de Javascript
	 * @param nodo
	 * @param nombreCampo
	 * @return
	 */
	public String campoFecha(Element nodo, String nombreCampo) {
		String szFecha = campo(nodo, nombreCampo);
		if (szFecha == null || "".equals(szFecha)) {
			return "";
		} else {
			try {
				String formatoEntrada = "dd/MM/yyyy";				
				if(szFecha.length() == 8){
					formatoEntrada = "dd/MM/yy";
				}
	
				DateFormat df = new SimpleDateFormat(formatoEntrada); 
				Date fecha = df.parse(szFecha);  
				
				if (fecha == null)
					return "";
				SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
				return formateador.format(fecha);
			} catch (Exception e) {
				return "";
			}
		}
	}
	
	/**
	 * Genera el informe 
	 * @param id
	 * @param xml
	 * @param xsl
	 * @param output
	 */
	public void generarInforme (String id, String xml, String xsl, OutputStream output) {
		DatosSalidaImpresa s = new DatosSalidaImpresa(xml, xsl, output);
		getDatosInforme(ideper, tipo, importe);
		if (!cpl.getResultado().equals(null)) {
			// una vez obtenidos los datos y comprobado que la respuesta no es nula
			// se debe inluir el xml con los datos en la base de datos zipped
			// y se pinta el pdf.
			
			try {
				
				Document docRespuesta = (Document) XMLUtils.compilaXMLObject(cpl.getResultado(), null);
				Element[] rsReex = XMLUtils.selectNodes(docRespuesta, "//estruc[@nombre='REEX_RECIBO_EXPEDIENTE']/fila");
				
				Element[] rsCade = XMLUtils.selectNodes(docRespuesta, "//estruc[@nombre='CADE_CADENA']/fila");
				Element reex=rsReex[0];
				
				s.Campo("nombreSP", campo(reex, "nombre_reex"));
				s.Campo("nifSP",campo(reex,"nif_reex"));
				s.Campo("expEje",campo(reex,"expediente_reex"));
				for (int i=0; i< rsCade.length; i++){
					if (i==0) {
						s.Campo("detalleDeuda", campo(rsCade[i],"string_cade"));
					}
					else
					{
						s.Campo("detalle"+i, campo(rsCade[i], "string_cade"));
					}
				}
				
				s.Campo("totalPrincipal",DatosSalidaImpresa.toEuro(campo(reex,"principal_reex")));
				s.Campo("recargo",DatosSalidaImpresa.toEuro(campo(reex,"recargo_reex")));
				s.Campo("intereses",DatosSalidaImpresa.toEuro(campo(reex,"intereses_reex")));
				s.Campo("costas",DatosSalidaImpresa.toEuro(campo(reex,"costas_reex")));
				s.Campo("costasExp",DatosSalidaImpresa.toEuro(campo(reex,"costas_exp_reex")));
				s.Campo("ingCta",DatosSalidaImpresa.toEuro(campo(reex,"ingresoscuenta_reex")));
				s.Campo("importeIngresar1",DatosSalidaImpresa.toEuro(campo(reex,"total_reex"))+" euros");
				
				s.Campo("fechaCaducidad",campoFecha(reex,"diapago_reex"));	
				
				s.Campo("EtiquetaReferencia","Referencia de cobro: "+DatosSalidaImpresa.Right("000000000000"+ campo(reex,"referencia_reex"),12));	
				
				s.Campo("ultimoDiaPago",campoFecha(reex,"diapago_reex"));	
				s.Campo("emisora",campo(reex,"emisora_reex"));	
				s.Campo("modoEmision",campo(reex,"modo_emis_reex"));	
				s.Campo("referenciaCobro",DatosSalidaImpresa.Right("000000000000"+ campo(reex,"referencia_reex"),12));	
				s.Campo("identificacion",campo(reex,"identificacion_reex"));	
				s.Campo("importeIngresar2",DatosSalidaImpresa.toEuro(campo(reex,"total_reex")+""));
				
				s.Campo("titular",campo(reex,"nombre_reex"));
				s.Campo("nifTitular",campo(reex,"nif_reex"));	
				
				s.Campo("nombreDest",campo(reex,"nombre_reex"));
				// CRUBENCVS 33799. Se elimina la dirección del contribuyente 
				/*s.Campo("calleDest",campo(reex,"sigla_reex")+" "+campo(reex,"calle_reex"));	
				s.Campo("ampliacionCalle",campo(reex,"ampl_cia_reex"));	
				s.Campo("municipioDest",campo(reex,"cp_reex")+" "+campo(reex,"municipio_reex"));
				s.Campo("provinciaDest",campo(reex,"provincia_reex")); */
				s.Campo("calleDest","");
				s.Campo("ampliacionCalle","");
				s.Campo("municipioDest","");
				s.Campo("provinciaDest","");
				// FIN CRUBENCVS 33799
				s.Campo("CodBarras2",campo(reex,"ref_larga_reex"));
				s.Campo("referenciaLarga",campo(reex,"ref_larga_reex"));
				s.Mostrar();
					
			} catch (Exception e) {
				Logger.error("Error al incluir datos en el pdf: "+e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			}
		}
	}
	/**
	 * Procesa el informe, devolviendo bien sea una respuesta en que lo incluye, o 
	 * información acerca de por qué no ha podido procesarlo.
	 * @param idEper Id eper del expediente
	 * @param tipo Tipo de carta de pago a generar
	 * @param importe Importe de la carta de pago
	 * @param comprimido S si se desea que el contenido del informe esté comprimido, N si no
	 * @return Objeto de tipo {@link RetornoCartaPagoExpedienteEjecutiva} con el informe o datos de por qué no se ha podido generar
	 * @throws Exception No es lo ideal, debería indicar errores más precisos, pero por el momento no se han definido
	 */
	public static RetornoCartaPagoExpedienteEjecutiva procesar (String idEper, String tipo, int importe, String comprimido) throws Exception{
		RetornoCartaPagoExpedienteEjecutiva retorno = new RetornoCartaPagoExpedienteEjecutiva();
		//Seguimos con el proceso.
		String pdfPago;
		Logger.info("Entramos a generar el pdf de carta de pago de ejecutiva",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		CartaPagoExpedienteEjecutiva oDocumentoPago = CartaPagoExpedienteEjecutiva.getInstance(idEper, tipo, importe);
										
		pdfPago = PDFUtils.generarPdf(oDocumentoPago);
		
		if (pdfPago.equals(null) || "".equalsIgnoreCase(pdfPago))
		{	
			retorno.setEsError(true);
			retorno.setCodigoResultado("0100");
			retorno.setDescripcionResultado("No se ha podido generar el documento");
			Logger.debug("No se ha podido generar el pdf" ,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
		else
		{																	
			/*
			 * Alta documento
			 */
			
			Logger.debug("Se va a dar de alta el documento:" ,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			
			AltaDocumento docu = new AltaDocumento ();								
			docu.setDocumento("R", oDocumentoPago.getReferencia(), null, oDocumentoPago.getNifContribuyente(),oDocumentoPago.getNifContribuyente(), pdfPago, "PDF");
			
			
			Logger.debug("Finalizado el alta de documento:" + idEper,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			
			Logger.debug("Se comprueba si es necesario comprimir el pdf",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			
			if (!(comprimido==null))
			{
				Logger.debug("Se comprime el pdf",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				if (comprimido.equalsIgnoreCase("S") && !retorno.isEsError()) {
					byte[] docDecodificado = Base64.decode(pdfPago.toCharArray());
					String documentzippeado = "";
					ByteArrayOutputStream resulByteArray = new ByteArrayOutputStream();
					resulByteArray.write(docDecodificado);
					documentzippeado = PdfComprimidoUtils.comprimirPDF(resulByteArray);
					pdfPago = documentzippeado; 
				}
				
			}
			retorno.setEsError(false);
			retorno.setCodigoResultado("0000");
			retorno.setDocumento(pdfPago);
			retorno.setDescripcionResultado("Proceso terminado correctamente");
			Logger.debug("Se finaliza correctamente.",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}																						
		
		return retorno;
	}
	
	
	
	private void getDatosInforme (String idEper, String tipo, int importe) {
		try {
			cpl = new ConversorParametrosLanzador();
	        cpl.setProcedimientoAlmacenado(this.pref.getPaCartaPagoExpediente());

	        cpl.setParametro(idEper,ConversorParametrosLanzador.TIPOS.String);
	        cpl.setParametro(tipo,ConversorParametrosLanzador.TIPOS.String);	        
	        cpl.setParametro(String.valueOf(importe),ConversorParametrosLanzador.TIPOS.Integer);

	        LanzaPLMasivoService lanzaderaWS = new LanzaPLMasivoService();					
			LanzaPLMasivo lanzaderaPort;			
			lanzaderaPort = lanzaderaWS.getLanzaPLMasivoSoapPort();


			// enlazador de protocolo para el servicio.
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;

			// Cambiamos el endpoint
			bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointLanzador());

	        String respuesta = "";	
	        //Vinculamos con el Handler	        
	        HandlerUtil.setHandlerClient((javax.xml.ws.BindingProvider) lanzaderaPort);

	        
	        try {	        	
	        	respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl.Codifica(), "", "", "", "");
	        	cpl.setResultado(respuesta);	    
	        }catch (Exception ex) {
		        	Logger.error ("Error en lanzadera al recuperar datos del documento de pago: "+ex.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		        	Logger.trace(ex.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
	        }
		} catch (Exception e) {
			Logger.error("Excepcion generica al recuperar los datos del documento de pago: "+e.getMessage(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			Logger.trace(e.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
	}

}
