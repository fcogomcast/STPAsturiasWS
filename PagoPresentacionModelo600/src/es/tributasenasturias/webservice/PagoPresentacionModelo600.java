package es.tributasenasturias.webservice;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService; 
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.ws.BindingProvider;

import es.tributasenasturias.business.AltaEscritura;
import es.tributasenasturias.business.AltaExpediente;
import es.tributasenasturias.business.BOFactory;
import es.tributasenasturias.business.IntegraTributas;
import es.tributasenasturias.business.ModelosPDF;
import es.tributasenasturias.business.Pago;
import es.tributasenasturias.business.SelectorEmisora;
import es.tributasenasturias.docs.AltaDocumento;
import es.tributasenasturias.docs.Comparecencia;
import es.tributasenasturias.docs.DocsFactory;
import es.tributasenasturias.docs.DocumentoDoin;
import es.tributasenasturias.docs.JustificanteCobro;
import es.tributasenasturias.docs.JustificantePresentacion;
import es.tributasenasturias.docs.PDFUtils;
import es.tributasenasturias.docs.DocumentoDoin.TipoDoc;
import es.tributasenasturias.documentos.util.XMLUtils;
import es.tributasenasturias.pagopresentacionmodelo600utils.Preferencias;
import es.tributasenasturias.pagopresentacionmodelo600utils.Utils;

import es.tributasenasturias.firmadigital.client.WsFirmaDigital;
import es.tributasenasturias.firmadigital.client.FirmaDigital;
import es.tributasenasturias.validacion.CertificadoValidator;
import es.tributasenasturias.validacion.ValidacionFactory;
import es.tributasenasturias.validacion.XMLEntValidator;
import es.tributasenasturias.webservice.firma.FirmaFactory;
import es.tributasenasturias.webservice.firma.FirmaHelper;
import es.tributasenasturias.webservice.pagopresentacion.log.LogHelper;

/*******************************************************************************
 * 
 * Clase WebService que obtiene los datos de la petición de cálculo del modelo
 * 600.
 * 
 * Recupera la información, realiza la llamada al procedimiento almacenado que
 * calcula el resultado de la autoliquidación y devuelve el resultado.
 * 
 ******************************************************************************/

@WebService(name = "PagoPresentacionModelo600")
public class PagoPresentacionModelo600 {
	//private String XML_MSG_ERROR = "<ns2:remesa><ns2:resultado><ns2:codigo></ns2:codigo><ns2:descripcion></ns2:descripcion></ns2:resultado></ns2:remesa>";	
	private String XML_MSG_ERROR = "<remesa><resultado><codigo></codigo><descripcion></descripcion></resultado></remesa>";
	private String MSG_ERROR_01_01 = " Error en el XML de entrada (XML Vacío)";	
	private String MSG_ERROR_01_02 = " Error en el XML de entrada (Integridad de la Firma)";
	private String MSG_ERROR_01_03 = " Error en el XML de entrada (Certificado Inválido)";
	private String MSG_ERROR_01_04 = " Error en el XML de entrada (Genérico al validar Firma)";	
	private String MSG_ERROR_02_01 = " Error en el proceso de pago";
	private String MSG_ERROR_02_02 = " Error en el proceso de alta de expediente (Integración)";
	private String MSG_ERROR_02_03 = " Error en el proceso de alta de expediente (Alta de documento)";
	private String MSG_ERROR_02_04 = " Error en el proceso de alta de expediente (Alta Expediente)";
	//private String MSG_ERROR_02_05 = " Error en el proceso de alta de expediente (Integración - Ya existe autoliquidación)";
	
	
	private Preferencias pref = new Preferencias();
	
	private String dirLog()
	{	
		String dir="";
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat();
		sdf.applyPattern("yyyyMMdd");
		dir=sdf.format (new java.util.Date());
		return dir;
		
	}
	private String nombreLog()
	{
		//Directorio basado en el día de hoy, y nombre de fichero genérico.
		String name=pref.getDirApp()+"/logs/"+dirLog()+"/"+"Application.log";
		return name;
	}
	private String nombreLog(Document doc)
	{
		//Basamos el nombre del log en la fecha actual y en los datos de la identificación.
		//Por defecto habrá un nombre de log basado en la fecha actual.
		String name="";
		if (doc==null)
		{
			name=nombreLog();
		}
		Element[] identificacion = XMLUtils.selectNodes(doc,"//remesa/declaracion/Identificacion");
		if (identificacion.length!=0)
		{
			//Se sacan los datos de notario, protocolo y protocolo bis.
			String notario=XMLUtils.selectSingleNode(identificacion[0], "CodNotario").getTextContent();
			String protocolo = XMLUtils.selectSingleNode(identificacion[0], "NumProtocolo").getTextContent();
			String protocoloBis= XMLUtils.selectSingleNode(identificacion[0], "NumProtocoloBis").getTextContent();
			name=pref.getDirApp()+"/logs/"+dirLog()+"/"+notario+"_"+protocolo+"_"+protocoloBis+".log";
		}
		else
		{
			name=nombreLog();
		}
		return name;
	}
	
	public PagoPresentacionModelo600() {
		// de este modo, al instalar el webservice, se creara el fichero de
		// preferencias si no existe
		pref.CompruebaFicheroPreferencias();
	}
	/**
	 * Elimina los nodos relativos a datos de escritura, para aligerar el xml con el que se trabaja.
	 * Se deben haber sacado antes los datos de la firma de escritura y documento de escritura.
	 * @param docEntrada Documento de entrada
	 * @return documento sin datos de escrituras.
	 */
	private Document eliminaEscritura(Document docEntrada)
	{
		Document doc;
		doc=docEntrada;
		Element[] escritura = XMLUtils.selectNodes(doc,"//remesa/declaracion/Escritura");
		if (escritura.length != 0) {
			Element firmaEscritura = XMLUtils.selectSingleNode(escritura[0],"firmaDocumentoEscritura");
			Element docEscritura   = XMLUtils.selectSingleNode(escritura[0],"documentoEscritura");
			if (firmaEscritura!=null){
				escritura[0].removeChild(firmaEscritura);
			}
			if (docEscritura!=null)
			{
				escritura[0].removeChild(docEscritura);
			}
		}
		
		return doc;
	}
	private Document eliminaFirma (Document docEntrada)
	{
		Element[] remesas = XMLUtils.selectNodes(docEntrada,"//remesa");
		if (remesas.length!=0)
		{
			Element firma = XMLUtils.selectSingleNode(remesas[0], "*[local-name='Signature']");
			if (firma!=null)
			{
				remesas[0].removeChild(firma);
			}
		}
		return docEntrada;
	}
	private String mensIn(java.util.Calendar cal)
	{
		return "=========INICIO DE PETICIÓN EL " + cal.get(java.util.Calendar.DAY_OF_MONTH) + "/"+
		cal.get(java.util.Calendar.MONTH)+"/"+cal.get(java.util.Calendar.YEAR) + " "+ 
		cal.get(java.util.Calendar.HOUR_OF_DAY) + ":"+ cal.get(java.util.Calendar.MINUTE) + ":"+
		cal.get(java.util.Calendar.SECOND);
	}
	private String mensOut (java.util.Calendar cal)
	{
		return "=========FINAL DE LA PETICIÓN INICIADA EL " + cal.get(java.util.Calendar.DAY_OF_MONTH) + "/"+
		cal.get(java.util.Calendar.MONTH)+"/"+cal.get(java.util.Calendar.YEAR) + " "+ 
		cal.get(java.util.Calendar.HOUR_OF_DAY) + ":"+ cal.get(java.util.Calendar.MINUTE) + ":"+
		cal.get(java.util.Calendar.SECOND);
	}
	/**
	 * getPagoPresentacionModelo600 realiza el pago y presentación del modelo
	 * 600 incluido en el xml de entrada
	 * 
	 * @param xmlData -
	 *            Requiere el contenido de un XML con los datos de la
	 *            autoliquidación del modelo 600
	 * @return Devuelve un xml el resultado del pago y presentación del modelo
	 *         600.
	 */
	@WebMethod()
	public String getPagoPresentacionModelo600(@WebParam(name = "xmlData")
	String xmlData) {
		LogHelper log = null;
		java.util.Calendar cal=null;
		try {
			//Xml de respuestra del servicio.
			Document docRespuesta = null;
			//Cadena que contiene el xml de entrada pero aligerado para trabajar mejor con él.
			String xmlWork="";
			String firmaEscritura = "";
			String docEscritura = "";
			//Log de esta llamada. 
			String nameLog="";
			// Para mostrar el inicio y final de log.
			cal = new java.util.GregorianCalendar(java.util.TimeZone.getTimeZone("Europe/Madrid")) ; //Hora de Madrid, independiente de configuración.
			//Factoría para recuperar los objetos relacionados con Documentos
			DocsFactory docFactory=new DocsFactory();
			//Factoría para objetos del negocio.
			BOFactory bFactory = new BOFactory();
			//Factoría de firma
			FirmaFactory frFactory= new FirmaFactory();
			//Validación
			ValidacionFactory validFactory = new ValidacionFactory();
			//Hemos pasado las siguientes variables "v_nifSP", "v_nifPR" y "v_numAutoLi" porque 
			//estaban a nivel de clase, y como el servidor mantiene sólo una instancia de esta
			//clase en cada momento, podía haber problemas de concurrencia, como que una llamada
			//comenzara con un número de autoliquidación, y al ejecutarse otra, acabase con
			//un número diferente.
			String v_nifSP = "";
			String v_nifPr = "";
			String v_numAutoliq = "";
			// cargamos los datos del almacen de un fichero xml preferencias
			pref.CargarPreferencias();
			
			//Validamos la entrada
			XMLEntValidator eValidator = (XMLEntValidator)validFactory.newParameterValidator();
			if (!eValidator.isValid(xmlData))
			{
				log=new LogHelper(nombreLog());
				log.info(mensIn(cal));
				log.info(xmlData);
				log.error("****Error al validar el documento de entrada. Detalles a continuación:");
				log.error(eValidator.getResultado().toString());
				return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_01_01, "01");
			}
			//Si ha pasado, recogemos el documento.
			docRespuesta= eValidator.getXMLValidado();
			if (docRespuesta!=null)
			{
				nameLog=nombreLog(docRespuesta);
			}
			else
			{
				nameLog =nombreLog();
			}
			log = new LogHelper(nameLog);
			docFactory.setLogger(log);
			bFactory.setLogger(log);
			frFactory.setLogger(log);
			validFactory.setLogger(log);
			log.info(mensIn(cal));
		
			log.info (" ==== XML DE ENTRADA AL SERVICIO ");
			log.info(xmlData);
										
			// ==============================
			// Validación de la firma DIGITAL
			// ==============================
			log.info("====Entrada validación firma de documento.");
			if ("S".equals(pref.getValidaFirma())) {
				try {

					boolean integridadXML = false;
					WsFirmaDigital srv = new WsFirmaDigital();					
					FirmaDigital srPort = srv.getServicesPort();
					try {
						// Se modifica el endpoint
						String endpointFirma = pref.getEndpointFirma();
						if (!"".equals(endpointFirma)) {
							BindingProvider bpr = (BindingProvider) srPort;
							bpr.getRequestContext().put(
									BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
									endpointFirma);
						}

						try {
							integridadXML = srPort.validar(xmlData);
							if (!integridadXML)
							{
								log.info("====2 - FIRMA INCORRECTA");
							}
							else
							{
								log.info("====2 - FIRMA CORRECTA");
							}
								
						} catch (Exception ex) {
							log.error("****Error al validar la firma 1 :"
									+ ex.getMessage());
							log.trace(ex.getStackTrace());
							integridadXML = false;
						}
					} catch (Exception ex) {
						log.error("****Error al recuperar el endpoint del servicio de firma:"+ ex.getMessage());
						log.trace(ex.getStackTrace());
						integridadXML = false;
					}
					if (!integridadXML) {
						log.error ("****La integridad del documento firmada no es valida.");
						return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_01_02, "01");
					} else {
						
						CertificadoValidator validator = (CertificadoValidator) validFactory.newCertificadoValidator();
						if (!validator.isValid(xmlData)) {
							log.error("****El certificado no es válido");
							return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_01_03, "01");
						}
					}

				} catch (Exception e1) {
					log.error("****No se ha podido validar la firma:"
							+ e1.getMessage());
					log.trace(e1.getStackTrace());
					return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_01_04, "01");
				}
			}
			log.info("====Salida: Validación firma de documento.");
			//==============================================================
			//Se recuperan los datos de la escritura y se limpia el XML de  
			//entrada para que sea más ligero.
			//==============================================================
			try
			{
				Element[] escritura = XMLUtils.selectNodes(docRespuesta,"//remesa//declaracion//Escritura");
				if (escritura.length!=0)
				{
					Element nfirmaEscritura=XMLUtils.selectSingleNode(escritura[0],"firmaDocumentoEscritura");
					Element ndocEscritura=XMLUtils.selectSingleNode(escritura[0],"documentoEscritura");
					if (nfirmaEscritura!=null) //Según el esquema de xml de entrada no puede pasar, pero quien sabe en un futuro.
					{
						firmaEscritura = nfirmaEscritura.getTextContent();
					}
					if (ndocEscritura!=null)
					{
						docEscritura   = ndocEscritura.getTextContent();
					}
				}
				docRespuesta=eliminaEscritura(docRespuesta);
				docRespuesta=eliminaFirma(docRespuesta);
				xmlWork = Utils.DOM2String(docRespuesta);
			}
			catch (Exception e)
			{   
				log.error("****Excepción.No se ha podido eliminar la escritura para el paso a integración: "+ e.getMessage());
				log.trace(e.getStackTrace());
				return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_02_02, "02");
			}
			
			// ===============================================================
			// Se realiza la llamada al procedimiento almacenado para integrar
			// la petición en las tablas temporales de tributas.
			// ===============================================================
			String resultadoIntegracion = "";
			IntegraTributas it;
			log.info("====Entrada: Integración de datos en tablas del modelo.");
			try{
				it = bFactory.newIntegraTributas();
				it.setDatosXml(xmlWork); //Se pasa la versión ligera del documento de entrada.
							
				boolean resultadoOK = it.Ejecuta();			
				
				// Si el resultado NO es ok, se retorna el resultado y no se
				// continua con el pago.
				if (!resultadoOK) {
					log.error("****Error: no se ha podido integrar en tablas.");
					return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_02_02, "02");
				}
				resultadoIntegracion = it.getResultado();
				// Volcamos los datos en las variables que se utilizaran
				// para dar de alta los documentos.
				v_numAutoliq = it.getNumAutoliquidacion();
				v_nifSP = it.getNifSp();
				v_nifPr = it.getNifPr();
				if (!"00".equalsIgnoreCase(it.getCodigoResultado())) {	
					log.error("****Error: no se ha podido integrar en tablas.");
					return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_02_02, "02");
				}
				log.info("====Salida: Integración en tablas del modelo realizada.");
			}
			catch (Exception ex)
			{
				log.error("****Excepción al integrar en tablas de modelo :"+ex.getMessage());
				log.trace(ex.getStackTrace());
				return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_02_02, "02");
			}
			// Alta de la escritura:
			try {
				// Alta de la escritura:
				if (firmaEscritura.length()!=0 || docEscritura.length()!=0) {
					log.info("====Entrada: Alta de escritura.");
					if (docEscritura.length()!=0) {
						AltaEscritura altaEscritura = bFactory.newAltaEscritura();
						if (altaEscritura.Inserta(it.getCodNotario(), it
								.getCodNotaria(), it.getNumProtocolo(), it
								.getProtocoloBis(), it.getFechaAutorizacion(),
								firmaEscritura, docEscritura))
						{
							log.info("=====Se da de alta la escritura.");
						}
						else
						{
							log.info  ("====No se ha podido dar de alta la escritura.");
							return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_02_03, "02");
						}
					}
					else
					{
						log.info("=====No hay documento de escritura, no se da de alta.");
					}
				}
				else
				{
					log.info("=====No hay documentos de escritura, no se da de alta.");
				}
				log.info("====Salida: Alta de escritura.");
			} catch (Exception e) {
				log.error("****Excepción durante el alta de escrituras: "+ e.getMessage());
				log.trace(e.getStackTrace());
				return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_02_03, "02");
			}
			
			// ======================
			// Continuar con el pago
			// ======================
			boolean requierePago;
			if (("0,00".equalsIgnoreCase(it.getImporte()))
					|| ("0".equalsIgnoreCase(it.getImporte()))
					|| (it.getImporte().isEmpty())) {
				requierePago = false;
			} else {
				requierePago = true;
			}
			log.info(" ====Entrada: Pago telemático");
			if (requierePago) {
				// Se debe obtener el número de autoliquidación.
				Pago pagoModelo600 = bFactory.newPago();
				try {
					pagoModelo600.getPagoVO().setOrigen("S1");
					pagoModelo600.getPagoVO().setModalidad("3");
					//La emisora dependerá del modelo.
					pagoModelo600.getPagoVO().setEmisora(new SelectorEmisora().getEmisoraDeModelo("600"));
					v_nifSP = it.getNifSp();
					v_nifPr = it.getNifPr();
					pagoModelo600.getPagoVO().setNifSp(v_nifSP);
					pagoModelo600.getPagoVO().setFechaDevengo(Utils.eliminaFormatoFecha(it.getFechaDevengo()));
					pagoModelo600.getPagoVO().setDatoEspecifico(it.getDatosEspecifico());
					v_numAutoliq = it.getNumAutoliquidacion();
					pagoModelo600.getPagoVO().setNumeroAutoliquidacion(v_numAutoliq);
					pagoModelo600.getPagoVO().setImporte(Utils.eliminaFormatoImporte(it.getImporte()));			
					pagoModelo600.getPagoVO().setNifOperante(it.getNifOperante());
					pagoModelo600.getPagoVO().setCCC(it.getCCC());
					pagoModelo600.pagarAutoliquidacion();
					//Se muestran los posibles mensajes que haya generasdo el pago
					if (!"0000".equals(pagoModelo600.getCodError()) && !"0001".equals(pagoModelo600.getCodError())) {
						log.error("****Error recibido en el proceso de pago:"+ pagoModelo600.getRespuestaErrorPago());
						return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_02_01, "03");
					}
					else
					{
						log.info("*******Pago realizado correctamente.");
					}
				} catch (Exception e) {
					log.error("****Excepción en el proceso de pago:" + e.getMessage());
					log.trace(e.getStackTrace());
					return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_02_01, "03");
				}
				log.info("====Salida: Pago telemático.");
				// una vez se realiza el alta, se devuelven los datos de los
				// documentos.
				try {
					log.info("====Entrada: Alta de justificante de pago.");
					// Cambios 11/11/10. 
					//Se comprueba si ya existía el justificante de pago para esa autoliquidación.
					//Si ya existía, se recupera. 
					//De otra forma, se genera.
					JustificanteCobro oJustificanteCobro = docFactory.newJustificanteCobro(
							it.getNumAutoliquidacion());
					DocumentoDoin docBD = docFactory.newDocumentoDoin(it.getNumAutoliquidacion());
					docBD.recuperarDocumento(TipoDoc.PAGO);
					//Si hay mensajes, los logeamos.
					if (!docBD.isGenerado())
					{
						String pdfJustificanteCobro = PDFUtils
								.generarPdf(oJustificanteCobro);
						AltaDocumento ad = docFactory.newAltaDocumento();
						
						// P : se trata del justificante de pago.
						ad.setDocumento("P", v_numAutoliq, oJustificanteCobro
								.getHashVerificacion(), v_nifSP, v_nifPr,
								pdfJustificanteCobro, "PDF");
						resultadoIntegracion = Utils.setJustificantePagoDocument(
								resultadoIntegracion, pdfJustificanteCobro);
						log.info("====Salida: Alta de justificante de pago..");
					}
					else
					{
						resultadoIntegracion = Utils.setJustificantePagoDocument(
								resultadoIntegracion, docBD.getDocumento());
						log.info("====Salida: El justificante de pago ya estaba dado de alta.");
					}
				} catch (Exception e) {
					log.error("Excepción en el alta de justificante de pasgo: "+ e.getMessage());
					log.trace(e.getStackTrace());
					return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_02_03, "02");
				}
			}
			else
			{
				log.info("====Salida:no es necesario el pago.");
			}

			//========================
			// ALMACENAMOS XML FIRMADO
			//========================
			try {
				DocumentoDoin docBD=docFactory.newDocumentoDoin(it.getNumAutoliquidacion());
				docBD.recuperarDocumento(TipoDoc.XML);
				log.info("====Entrada: alta de xml de entrada en base de datos.");
				if (!docBD.isGenerado())
				{
					AltaDocumento ad = docFactory.newAltaDocumento();
					// M : Se trada del modelo en.				
					ad.setDocumento("M", v_numAutoliq, null, v_nifSP,						
							v_nifPr, "<![CDATA["+xmlData+"]]>", "XML");
					log.info("=====Salida: documento de xml de entrada dado de alta.");
				}//Si ya existe, no es necesario darlo de alta de nuevo.
				else
				{
					log.info("====Salida: el documento xml de entrada ya estaba dado de alta.");
				}
			} catch (Exception e) {
				log.error("****Excepción al insertar el documento xml de entrada en la base de datos:"+ e.getMessage());
				log.trace(e.getStackTrace());
				return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_02_03, "02");
			}
							
			// ==============================================================
			// Si el pago es correcto, hay que dar de alta el expediente
			// Se realiza la llamada al procedimiento almacenado para dar de
			// alta el expediente.
			// ==============================================================
			String resultadoAltaExpediente = "";
			try
			{	
				log.info("====Entrada: alta de expediente.");
				AltaExpediente altaExpediente = bFactory.newAltaExpediente();
				altaExpediente.setDatosXml(resultadoIntegracion);
				boolean resultadoAltaOK = altaExpediente.Ejecuta();
				resultadoAltaExpediente = altaExpediente.getResultado();
				if (resultadoAltaOK) {
					log.info("====Salida:Alta de expediente realizado de forma correcta.");
				}
				else
				{
					log.error("****Error alta de expediente. El resultado ha sido:" + altaExpediente.getTextResultado());
					return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_02_04, "02");
				}
			}
			catch (Exception e)
			{
				log.error("****Excepción durante alta de expediente: "+ e.getMessage());
				log.trace(e.getStackTrace());
				return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_02_04, "02");
			}
			//=============================
			// ALTA DOCUMENTO COMPARECENCIA
			//=============================
			try {
				log.info("====Entrada: documento de alta de comparecencia.");
				DocumentoDoin docBD= docFactory.newDocumentoDoin(it.getNumAutoliquidacion());
				docBD.recuperarDocumento(TipoDoc.COMPARECENCIA);
				if (!docBD.isGenerado())
				{
					Comparecencia oDocComparecencia = docFactory.newComparecencia(it.getNumAutoliquidacion());
					String pdfDocComparecencia = PDFUtils.generarPdf(oDocComparecencia);
					AltaDocumento ad = docFactory.newAltaDocumento();
					// J : Se trada del documento de comparecencia.
					resultadoAltaExpediente = Utils.setDocumentoComparecencia(resultadoAltaExpediente, pdfDocComparecencia);
					ad.setDocumento("J", v_numAutoliq, oDocComparecencia.getHashVerificacion(), v_nifSP, v_nifPr,pdfDocComparecencia, "PDF");
					log.info("====Salida: se ha dado de alta el documento de comparecencia.");
				}
				else
				{
					resultadoAltaExpediente = Utils.setDocumentoComparecencia(resultadoAltaExpediente, docBD.getDocumento());
					log.info("====Salida: el documento de comparecencia ya estaba dado de alta.");
				}
			} catch (Exception e) {
				log.error("****Excepción en el alta de documento de comparecencia:"+ e.getMessage());
				log.trace(e.getStackTrace());
				return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_02_03, "02");
			}

			//=========================================
			// ALTA DOCUMENTO JUSTIFICANTE PRESENTACION
			//=========================================
			try {
				log.info("====Entrada: alta de justificante de presentación.");
				DocumentoDoin docBD = docFactory.newDocumentoDoin(it.getNumAutoliquidacion());
				docBD.recuperarDocumento(TipoDoc.PRESENTACION);
				if (!docBD.isGenerado())
				{
					JustificantePresentacion oJustificantePre = docFactory.newJustificantePresentacion(it.getNumAutoliquidacion());
					String pdfJustificantePre = PDFUtils.generarPdf(oJustificantePre);
					AltaDocumento ad = docFactory.newAltaDocumento();
					
					// C : Se trata del justificante de pago y presentación.
					resultadoAltaExpediente = Utils.setJustificantePresentacion(resultadoAltaExpediente, pdfJustificantePre);
					ad.setDocumento("C", v_numAutoliq, oJustificantePre.getHashVerificacion(), v_nifSP, v_nifPr,pdfJustificantePre, "PDF");
					log.info("====Salida: se ha dado de alta el justificante de presentación.");
				}
				else
				{
					resultadoAltaExpediente = Utils.setJustificantePresentacion(resultadoAltaExpediente, docBD.getDocumento());
					log.info("====Salida: el justificante de presentación ya estaba dado de alta.");
				}
			} catch (Exception e) {
				log.error("****Excepción en el alta de justificante de la presentación:"+ e.getMessage());
				log.trace(e.getStackTrace());
				return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_02_03, "02");
			}

			//================
			// ALTA MODELO 600
			//================
			try {
				DocumentoDoin docBD = docFactory.newDocumentoDoin(it.getNumAutoliquidacion());
				docBD.recuperarDocumento(TipoDoc.MODELO);
				log.info("====Entrada: alta de modelo 600 en pdf.");
				if (!docBD.isGenerado())
				{
					ModelosPDF modelo600 = bFactory.newModelosPdf();
					boolean resultModelo600 = modelo600
							.generarModelo600(resultadoAltaExpediente);
							
								
					if (!resultModelo600) {
						log.error("****Error en el alta de modelo 600 en pdf, resultado: "+ modelo600.getResultado());
						return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_02_03, "02");					
					} else { // Almacenamos el modelo en base de datos.
	
						AltaDocumento ad = docFactory.newAltaDocumento();										
						ad.setDocumento("M", v_numAutoliq, modelo600.getCodigoVerificacion("M"+v_numAutoliq+v_nifSP),v_nifSP, v_nifPr, modelo600.getResultado(), "PDF");
						log.info("====Salida: se ha dado de alta el modelo 600 en pdf.");
					}
				}
				else
				{
					log.info("====Salida: el modelo 600 en pdf ya estaba dado de alta.");
				}
				//No se hace nada si no.
			} catch (Exception e) {
				log.error("****Excepción producida durante el alta de modelo 600 en pdf: "+ e.getMessage());
				log.trace(e.getStackTrace());
				return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_02_03, "02");
			}
			
			//=============================
			// Firmamos salida del Servicio
			//=============================
			log.info("===Entrada: firma de mensaje de salida.");
			if ("S".equals(pref.getFirmaDigital())) {
				try {
					FirmaHelper firmaDigital = frFactory.newFirmaHelper();
					resultadoAltaExpediente = firmaDigital
							.firmaMensaje(resultadoAltaExpediente);
					log.info("=====Mensaje de salida:"+ resultadoAltaExpediente);
				} catch (RuntimeException e) {
					log.error("****Error al firmar el mensaje de salida:" + e.getMessage());
					log.trace(e.getStackTrace());
				}
			}
			return resultadoAltaExpediente;
			
		} catch (Exception e) {
			if (log!=null) // En este punto podría serlo.
			{
				log.error("****Excepción inesperada: ".concat(e.getMessage()));
				log.trace(e.getStackTrace());
			}
			else // Lo intentamos con el log por defecto..
			{
				log = new LogHelper(nombreLog());
				log.error("****Excepción inesperada: " + e.getMessage());
				log.trace (e.getStackTrace());
			}
				
			return "Error: ".concat(e.getMessage());
		}
		finally 
		{
			if (log==null)
			{
				try {
					log=new LogHelper(nombreLog()); // Log por defecto, no se pudo construir el otro
				}
				catch (Exception ex)
				{
					System.err.println("Error al construir el log en el bloque finally:" + ex.getMessage());
				}
			}
			if (log!=null && cal!=null)
			{
				log.info(mensOut(cal));
			}
		}
	}
}
