package es.tributasenasturias.operaciones;

import java.io.StringReader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
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
import es.tributasenasturias.exception.PreferenciasException;
import es.tributasenasturias.exception.PresentacionException;
import es.tributasenasturias.exception.ValidadorEsquemaException;
import es.tributasenasturias.exception.XMLDOMDocumentException;

import es.tributasenasturias.firmadigital.client.FirmaDigital;
import es.tributasenasturias.firmadigital.client.WsFirmaDigital;

import es.tributasenasturias.modelo600utils.ConversorParametrosLanzador;
import es.tributasenasturias.modelo600utils.Utils;
import es.tributasenasturias.modelo600utils.Preferencias;

import es.tributasenasturias.servicios.tercerosmodelo600.CalculoFault;
import es.tributasenasturias.servicios.tercerosmodelo600.CalculoModelo600FaultInfo;
import es.tributasenasturias.servicios.tercerosmodelo600.PagoFault;
import es.tributasenasturias.servicios.tercerosmodelo600.PagoModelo600FaultInfo;
import es.tributasenasturias.validacion.CertificadoValidator;
import es.tributasenasturias.validacion.ValidacionFactory;
import es.tributasenasturias.validacion.XMLDOMUtils;
import es.tributasenasturias.validacion.XMLEntValidator;
import es.tributasenasturias.webservices.clients.LanzaPL;
import es.tributasenasturias.webservices.clients.LanzaPLService;
import es.tributasenasturias.operaciones.firma.FirmaFactory;
import es.tributasenasturias.modelo600utils.log.LogHelper;

/************************************************************************
 * Clase que implemental las operaciones sobre el modelo 600
 ************************************************************************/
public class OperacionesModelo600 {

	private Preferencias pref = new Preferencias();

	//Errores de mensajeria
	private String MSG_ERROR_F001 = " El mensaje recibido no tiene un formato válido: ";
	private String MSG_ERROR_F002 = " Error grave en la mensajería. Contacte con el servicio técnico de STPA.";
	private String MSG_ERROR_XML_VACIO = " Error en el XML de entrada (XML Vacío)";	

	//Mensajes comunes
	private String MSG_INFO_0010 = " La firma recibida en el documento no es válida.";
	private String MSG_INFO_0011 = " El firmante del documento no tiene permisos para ejecutar la operación.";
	private String MSG_INFO_0100 = " No se ha podido completar la operación debido a un error técnico. Inténtelo en unos minutos o contacte con soporte técnico.";

	//Mensajes pago
	private String MSG_INFO_0020 = " Error en la validación de los datos de la petición";
	private String MSG_INFO_0025 = " Se ha producido un error durante la integración en tablas del modelo.";
	private String MSG_INFO_0030 = " No se ha podido realizar el pago en la entidad.";
	private String MSG_INFO_0032 = " Se ha producido un error técnico al generar la carta de pago.";
	private String MSG_INFO_0040 = " Se ha producido un error mientras se realizaba la presentación del modelo.";
	private String MSG_INFO_0041 = " Se ha producido un error técnico durante la generación del documento de comparecencia o de presentación.";
	private String MSG_INFO_0050 = " Se ha producido un error técnico durante el alta de escritura.";

	public OperacionesModelo600() {
		//Al instalar el webservice, se creara el fichero de preferencias si no existe
		pref.CompruebaFicheroPreferencias();
	}
	/**
	 * Clase interna para almacenar los datos de la escritura, aparte del documento.
	 * @author crubencvs
	 *
	 */
	private static class DatosEscritura
	{
		private String docEscritura;
		private String firmaEscritura;
		public String getDocEscritura() {
			return docEscritura;
		}
		public void setDocEscritura(String docEscritura) {
			this.docEscritura = docEscritura;
		}
		public String getFirmaEscritura() {
			return firmaEscritura;
		}
		public void setFirmaEscritura(String firmaEscritura) {
			this.firmaEscritura = firmaEscritura;
		}
		
	}
	
	/**
	 * getCalculoModelo600 realiza el cálculo del modelo 600 en base a los datos de la autoliquidación de entrada
	 * 
	 * @param xmlData - Requiere el contenido de un XML con los datos de la autoliquidación del modelo 600
	 * @return Devuelve un xml el resutado del cálculo del modelo 600.
	 */
	public String getCalculoModelo600(String xmlData) throws CalculoFault {
		LogHelper log = null;
		java.util.Calendar cal=null;
		try {
			//Cargamos los datos del almacen de un fichero xml preferencias
			pref.CargarPreferencias();
			//Para mostrar el inicio y final de log.
			cal = new java.util.GregorianCalendar(java.util.TimeZone.getTimeZone("Europe/Madrid")) ; //Hora de Madrid, independiente de configuración.

			//Factorias
			FirmaFactory frFactory= new FirmaFactory(); //objetos de firma
			ValidacionFactory validFactory = new ValidacionFactory(); //objetos de validacion

			//-- INICIALIZACION OPERACION --
			log = logPorDefecto();
			Document docRespuesta=validarXmlEntrada(xmlData, log, validFactory, cal);
			log = logBasadoEnPeticion(docRespuesta);
			inicializarLogs(log,frFactory,validFactory,null,null);
			if (log==null){
	    		CalculoModelo600FaultInfo info=new CalculoModelo600FaultInfo();
	    		info.setId("F001");
	    		info.setMensaje(MSG_ERROR_F001+MSG_ERROR_XML_VACIO);
	    		CalculoFault fallo=new CalculoFault(MSG_ERROR_XML_VACIO,info);
	    		throw fallo;
			}
			log.info (" ==== XML DE ENTRADA DEL CÁLCULO.");
			log.info(xmlData);
			//-- VALIDACIÓN DE LA FIRMA DIGITAL --
			String validarFirma=this.validarFirma(log, xmlData, validFactory);
			if (validarFirma!=null)
				return validarFirma;

			//Se prepara la llamada al procedimiento almacenado
			ConversorParametrosLanzador cpl = new ConversorParametrosLanzador();
			cpl.setProcedimientoAlmacenado(pref.getPACalculo());
			String xmlDatos = new String(); //XmlDatos = "600";
			xmlDatos = ("<![CDATA[".concat(xmlData)).concat("]]>");	
			cpl.setParametro(xmlDatos, ConversorParametrosLanzador.TIPOS.Clob);
			cpl.setParametro("P", ConversorParametrosLanzador.TIPOS.String); //Conexion oracle
			log.debug(("metidos parametros ").concat(cpl.Codifica()));
			LanzaPLService pA = new LanzaPLService();
			LanzaPL lpl;

			if (!pref.getEndpointLanzador().equals("")) {
				log.debug("Se utiliza el endpoint de lanzadera: "+ pref.getEndpointLanzador());
				lpl = pA.getLanzaPLSoapPort();
				//Enlazador del protocolo para el servicio
				javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lpl;
				bpr.getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointLanzador()); // Cambiamos el endpoint
			}else{
				log.debug("Se utiliza el endpoint de lanzadera por defecto: "+ javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
				lpl = pA.getLanzaPLSoapPort();
				//Enlazador del protocolo para el servicio
				javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lpl; 
				log.debug("Se utiliza el endpoint de lanzadera por defecto: "+ bpr.getRequestContext().get(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
			}

			String respuesta = new String();
			try {
				respuesta = lpl.executePL(pref.getEntorno(), cpl.Codifica(),"", "", "", "");
				cpl.setResultado(respuesta);
				String coderror = cpl.getNodoResultadoX("STRING1_CANU");
				String canu = cpl.getNodoResultadoX("STRING2_CANU");
				if (!canu.equals("") && !"0020".equals(coderror) && !"0100".equals(coderror)){
		    		CalculoModelo600FaultInfo info=new CalculoModelo600FaultInfo();
		    		info.setId("F001");
		    		info.setMensaje(MSG_ERROR_F001+canu);
		    		CalculoFault fallo=new CalculoFault(canu,info);
		    		throw fallo;
				}
				String error = cpl.getNodoResultadoX("error");
				if (!error.equals("")){
					log.error("Error recibido del calculo de base de datos del lanzador:" + error);
					return Utils.setErrores(xmlData,MSG_INFO_0100,"0100");
				}
				respuesta = cpl.getNodoResultadoX("CLOB_DATA");
			}catch (Exception ex) {
				log.error("LANZADOR: ".concat(ex.getMessage()));
				log.trace(ex.getStackTrace());
				return Utils.setErrores(xmlData,MSG_INFO_0100,"0100");
			}

			log.debug("respuesta: ".concat(respuesta));
			return respuesta;
		} catch (Exception e) {
			log.error("error".concat(e.getMessage()));
			log.trace(e.getStackTrace());
			return Utils.setErrores(xmlData,MSG_INFO_0100,"0100");		
		}
	}

	/**
	 * getPagoPresentacionModelo600 realiza el pago y presentación del modelo
	 * 600 incluido en el xml de entrada
	 * 
	 * @param xmlData - Requiere el contenido de un XML con los datos de la
	 *            		autoliquidación del modelo 600
	 * @return Devuelve un xml el resultado del pago y presentación del modelo 600.
	 */
	public String getPagoPresentacionModelo600(String xmlData) throws PagoFault  {
		LogHelper log = null;
		java.util.Calendar cal=null;
		String v_nifSP = "";
		String v_nifPr = "";
		String v_numAutoliq = "";
		String xmlLite=""; //XML "ligero", sin escritura ni firma digital para pasar a la base de datos para validación e integración.
		//Factorias
		FirmaFactory frFactory; //objetos de firma
		ValidacionFactory validFactory; //objetos de validacion
		BOFactory bFactory; //objetos del negocio
		DocsFactory docFactory; //objetos relacionados con documentos
		boolean existeEscritura=false;
		//Documento XML de salida
		Document docRespuesta;
		try {
			//Cargamos los datos del almacen de un fichero xml preferencias
			pref.CargarPreferencias();
			//Para mostrar el inicio y final de log.
			cal = new java.util.GregorianCalendar(java.util.TimeZone.getTimeZone("Europe/Madrid")) ; //Hora de Madrid, independiente de configuración.

			//Factorias
			frFactory= new FirmaFactory(); //objetos de firma
			validFactory = new ValidacionFactory(); //objetos de validacion
			bFactory = new BOFactory(); //objetos del negocio
			docFactory=new DocsFactory(); //objetos relacionados con documentos

			//-- INICIALIZACION OPERACION --
			log= logPorDefecto();
			docRespuesta=validarXmlEntrada(xmlData, log, validFactory, cal);
			log=logBasadoEnPeticion(docRespuesta);
			inicializarLogs(log,frFactory,validFactory,bFactory,docFactory);
			if (log==null){
	    		PagoModelo600FaultInfo info=new PagoModelo600FaultInfo();
	    		info.setId("F001");
	    		info.setMensaje(MSG_ERROR_F001+MSG_ERROR_XML_VACIO);
	    		PagoFault fallo=new PagoFault(MSG_ERROR_XML_VACIO,info);
	    		throw fallo;
			}
			log.info (" ==== XML DE ENTRADA DE LA PRESENTACIÓN.");
			log.info(xmlData);
			//-- VALIDACION DE LA FIRMA DIGITAL --
			String validarFirma=this.validarFirma(log, xmlData, validFactory);
			if (validarFirma!=null)
				return validarFirma;

			//Aligeramos el xml, para que se maneje mejor en la base de datos.
			DatosEscritura datEscritura = getEscritura(docRespuesta);
			xmlLite = Utils.aligerarXML(docRespuesta);
			log.info(xmlLite);
			//Autoliquidacion, debe estar. Como en el esquema es opcional, por ahora se hace aquí,
			//debería pasarse a la validación de esquema
			
			if ("".equals(getNumAutoliquidacion(xmlLite, log)))
			{
				log.error("****Error: no se ha podido integrar en tablas.");
				return Utils.setErrores(xmlData,MSG_INFO_0020, "0020");
			}
			//-- VALIDACION DE ESQUEMA --
			String validarEsquema=this.validarEsquema(log, xmlLite);
			if (validarEsquema!=null)
				return validarEsquema;

			// ===============================================================
			// Se realiza la llamada al procedimiento almacenado para integrar
			// la petición en las tablas temporales de tributas.
			// ===============================================================
			String resultadoIntegracion = "";
			IntegraTributas it;
			log.info("====Entrada: Integración de datos en tablas del modelo.");
			try{
				it = bFactory.newIntegraTributas();
				it.setDatosXml(xmlLite);
				boolean resultadoOK = it.Ejecuta();

				//Si el resultado NO es ok, se retorna el resultado y no se continua con el pago
				if (!resultadoOK) {
					log.error("****Error: no se ha podido integrar en tablas.");
					return Utils.setErrores(xmlData,MSG_INFO_0025, "0025");
				}
				resultadoIntegracion = it.getResultado();
				//Volcamos datos en las variables que se utilizaran para dar de alta los documentos
				v_numAutoliq = it.getNumAutoliquidacion();
				v_nifSP = it.getNifSp();
				v_nifPr = it.getNifPr();
				if (!"00".equalsIgnoreCase(it.getCodigoResultado())) {	
					log.error("****Error: no se ha podido integrar en tablas. Resultado obtenido:"+it.getCodigoResultado());
					return Utils.setErrores(xmlData,MSG_INFO_0025, "0025");
				}
				log.info("====Salida: Integración en tablas del modelo realizada.");
			}catch (Exception ex){
				log.error("****Excepción al integrar en tablas de modelo :"+ex.getMessage());
				log.trace(ex.getStackTrace());
				return Utils.setErrores(xmlData,MSG_INFO_0025, "0025");
			}

			//-- CONTINUAR CON EL PAGO --
			boolean requierePago;
			if (("0,00".equalsIgnoreCase(it.getImporte())) || 
				("0".equalsIgnoreCase(it.getImporte()))	|| 
				(it.getImporte().isEmpty())) {
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
					if (it.getCCC()==null || "".equals(it.getCCC())){
						pagoModelo600.getPagoVO().setNumTarjeta(it.getNumTarjeta());
						pagoModelo600.getPagoVO().setFechaCaducidadTarjeta(it.getFechaCaducidadTarjeta());
					}else
						pagoModelo600.getPagoVO().setCCC(it.getCCC());
				
					pagoModelo600.pagarAutoliquidacion();
					//Se muestran los posibles mensajes que haya generasdo el pago
					if (!"0000".equals(pagoModelo600.getCodError()) && !"0001".equals(pagoModelo600.getCodError())) {
						log.error("****Error recibido en el proceso de pago:"+ pagoModelo600.getRespuestaErrorPago());
						return Utils.setErrores(xmlData,MSG_INFO_0030, "0030");
					}else{
						log.info("*******Pago realizado correctamente.");
					}
				}catch (Exception e) {
					log.error("****Excepción en el proceso de pago:" + e.getMessage());
					log.trace(e.getStackTrace());
					return Utils.setErrores(xmlData,MSG_INFO_0030, "0030");
				}
				log.info("====Salida: Pago telemático.");

				//Una vez se realiza el alta, se devuelven los datos de los documentos
				try {
					log.info("====Entrada: Alta de justificante de pago.");
					//Cambios 11/11/10. 
					//Se comprueba si ya existía el justificante de pago para esa autoliquidación.
					//Si ya existía, se recupera sino se genera.
					JustificanteCobro oJustificanteCobro = docFactory.newJustificanteCobro(it.getNumAutoliquidacion());
					//Indicamos el código nrc
					resultadoIntegracion=Utils.setNRCPago(resultadoIntegracion, oJustificanteCobro.getNrc());
					DocumentoDoin docBD = docFactory.newDocumentoDoin(it.getNumAutoliquidacion());
					docBD.recuperarDocumento(TipoDoc.PAGO);
					if (!docBD.isGenerado()){
						String pdfJustificanteCobro = PDFUtils.generarPdf(oJustificanteCobro);
						AltaDocumento ad = docFactory.newAltaDocumento();

						//P : se trata del justificante de pago.
						ad.setDocumento("P", v_numAutoliq, oJustificanteCobro.getHashVerificacion(), v_nifSP, v_nifPr,pdfJustificanteCobro, "PDF");
						resultadoIntegracion = Utils.setJustificantePagoDocument(resultadoIntegracion, pdfJustificanteCobro);
						log.info("====Salida: Alta de justificante de pago.");
					}else{
						resultadoIntegracion = Utils.setJustificantePagoDocument(resultadoIntegracion, docBD.getDocumento());
						log.info("====Salida: El justificante de pago ya estaba dado de alta.");
					}
				}catch (Exception e) {
					log.error("Excepción en el alta de justificante de pago: "+ e.getMessage());
					log.trace(e.getStackTrace());
					return Utils.setErrores(xmlData,MSG_INFO_0032, "0032");
				}
			}else{
				log.info("====Salida:no es necesario el pago.");
			}
			
			//-- ALMACENAMOS XML FIRMADO --
			try {
				DocumentoDoin docBD=docFactory.newDocumentoDoin(it.getNumAutoliquidacion());
				docBD.recuperarDocumento(TipoDoc.XML);
				log.info("====Entrada: alta de xml de entrada en base de datos.");
				if (!docBD.isGenerado()){
					AltaDocumento ad = docFactory.newAltaDocumento();

					//M : Se trada del modelo en.				
					ad.setDocumento("M", v_numAutoliq, null, v_nifSP,v_nifPr, "<![CDATA["+xmlData+"]]>", "XML");
					log.info("=====Salida: documento de xml de entrada dado de alta.");
				}else{//Si ya existe, no es necesario darlo de alta de nuevo.
					log.info("====Salida: el documento xml de entrada ya estaba dado de alta.");
				}
			}catch (Exception e) {
				log.error("****Excepción al insertar el documento xml de entrada en la base de datos:"+ e.getMessage());
				log.trace(e.getStackTrace());
				return Utils.setErrores(xmlData,MSG_INFO_0100, "0100");
			}

			// ==============================================================
			// Si el pago es correcto, hay que dar de alta el expediente
			// Se realiza la llamada al procedimiento almacenado para dar de alta el expediente.
			// ==============================================================
			String resultadoAltaExpediente = "";
			try{	
				log.info("====Entrada: alta de expediente.");
				AltaExpediente altaExpediente = bFactory.newAltaExpediente();
				altaExpediente.setDatosXml(resultadoIntegracion);
				boolean resultadoAltaOK = altaExpediente.Ejecuta();
				resultadoAltaExpediente = altaExpediente.getResultado();
				if (resultadoAltaOK) {
					log.info("====Salida:Alta de expediente realizado de forma correcta o ya estaba dado de alta.");
				}else{
					log.error("****Error alta de expediente. El resultado ha sido:" + altaExpediente.getTextResultado());
					return Utils.setErrores(xmlData,MSG_INFO_0040, "0040");
				}
			}catch (Exception e){
				log.error("****Excepción durante alta de expediente: "+ e.getMessage());
				log.trace(e.getStackTrace());
				return Utils.setErrores(xmlData,MSG_INFO_0040, "0040");
			}
			//Alta de escritura
			log.info("====Entrada:Alta de escritura");
			AltaEscritura.EstadosAltaEscritura estado=altaEscritura(it,datEscritura,bFactory);
			switch (estado)
			{
			case CORRECTA:
				log.info("====Salida: alta de escritura correcta.");
				break;
			case YA_EXISTE:
				log.info("====Salida: la escritura ya está dada de alta.");
				existeEscritura=true;
				break;
			case  NO_NECESARIA_ALTA:
				log.info("====Salida: no hay escritura que dar de alta.");
				break;
			case ERROR_TECNICO:
				log.error("****Error técnico durante el alta de escritura.");
				return Utils.setErrores(xmlData, MSG_INFO_0050, "0050");
			}
			//-- ALTA DOCUMENTO COMPARECENCIA --
			try {
				log.info("====Entrada: documento de alta de comparecencia.");
				DocumentoDoin docBD= docFactory.newDocumentoDoin(it.getNumAutoliquidacion());
				docBD.recuperarDocumento(TipoDoc.COMPARECENCIA);
				if (!docBD.isGenerado()){
					Comparecencia oDocComparecencia = docFactory.newComparecencia(it.getNumAutoliquidacion());
					String pdfDocComparecencia = PDFUtils.generarPdf(oDocComparecencia);
					AltaDocumento ad = docFactory.newAltaDocumento();
					resultadoAltaExpediente = Utils.setDocumentoComparecencia(resultadoAltaExpediente, pdfDocComparecencia);

					//J : Se trada del documento de comparecencia.
					ad.setDocumento("J", v_numAutoliq, oDocComparecencia.getHashVerificacion(), v_nifSP, v_nifPr,pdfDocComparecencia, "PDF");
					log.info("====Salida: se ha dado de alta el documento de comparecencia.");
				}else{
					resultadoAltaExpediente = Utils.setDocumentoComparecencia(resultadoAltaExpediente, docBD.getDocumento());
					log.info("====Salida: el documento de comparecencia ya estaba dado de alta.");
				}
			} catch (Exception e) {
				log.error("****Excepción en el alta de documento de comparecencia:"+ e.getMessage());
				log.trace(e.getStackTrace());
				return Utils.setErrores(xmlData,MSG_INFO_0041, "0041");
			}

			//-- ALTA DOCUMENTO JUSTIFICANTE PRESENTACION --
			try {
				log.info("====Entrada: alta de justificante de presentación.");
				DocumentoDoin docBD = docFactory.newDocumentoDoin(it.getNumAutoliquidacion());
				docBD.recuperarDocumento(TipoDoc.PRESENTACION);
				if (!docBD.isGenerado()){
					JustificantePresentacion oJustificantePre = docFactory.newJustificantePresentacion(it.getNumAutoliquidacion());
					String pdfJustificantePre = PDFUtils.generarPdf(oJustificantePre);
					AltaDocumento ad = docFactory.newAltaDocumento();
					resultadoAltaExpediente = Utils.setJustificantePresentacion(resultadoAltaExpediente, pdfJustificantePre);

					//C : Se trata del justificante de pago y presentación.
					ad.setDocumento("C", v_numAutoliq, oJustificantePre.getHashVerificacion(), v_nifSP, v_nifPr,pdfJustificantePre, "PDF");
					log.info("====Salida: se ha dado de alta el justificante de presentación.");
				}else{
					resultadoAltaExpediente = Utils.setJustificantePresentacion(resultadoAltaExpediente, docBD.getDocumento());
					log.info("====Salida: el justificante de presentación ya estaba dado de alta.");
				}
			} catch (Exception e) {
				log.error("****Excepción en el alta de justificante de la presentación:"+ e.getMessage());
				log.trace(e.getStackTrace());
				return Utils.setErrores(xmlData,MSG_INFO_0041, "0041");
			}

			//-- ALTA MODELO 600 --
			try{
				DocumentoDoin docBD = docFactory.newDocumentoDoin(it.getNumAutoliquidacion());
				docBD.recuperarDocumento(TipoDoc.MODELO);
				log.info("====Entrada: alta de modelo 600 en pdf.");
				if (!docBD.isGenerado()){
					ModelosPDF modelo600 = bFactory.newModelosPdf();
					boolean resultModelo600 = modelo600.generarModelo600(resultadoAltaExpediente);

					if (!resultModelo600) {
						log.error("****Error en el alta de modelo 600 en pdf, resultado: "+ modelo600.getResultado());
						return Utils.setErrores(xmlData,MSG_INFO_0100, "0100");					
					} else { // Almacenamos el modelo en base de datos.
						AltaDocumento ad = docFactory.newAltaDocumento();										
						if (modelo600.getResultado()!=null){
							ad.setDocumento("M", v_numAutoliq, modelo600.getCodigoVerificacion("M"+v_numAutoliq+v_nifSP),v_nifSP, v_nifPr, modelo600.getResultado(), "PDF");
							log.info("====Salida: se ha dado de alta el modelo 600 en pdf.");
						}else{
							log.error("****Error en el alta de modelo 600 en pdf, resultado es nulo");
							return Utils.setErrores(xmlData,MSG_INFO_0100, "0100");					
						}
					}
				}else{
					log.info("====Salida: el modelo 600 en pdf ya estaba dado de alta.");
				}
				//No se hace nada si no.
			}catch (Exception e) {
				log.error("****Excepción producida durante el alta de modelo 600 en pdf: "+ e.getMessage());
				log.trace(e.getStackTrace());
				return Utils.setErrores(xmlData,MSG_INFO_0100, "0100");
			}
			if (!existeEscritura)
			{
				return Utils.setErrores(resultadoAltaExpediente, "Presentación realizada correctamente.", "0000");
			}
			else
			{
				return Utils.setErrores(resultadoAltaExpediente, "Presentación realizada correctamente. La escritura no se ha dado de alta por existir previamente.", "0005");
			}
		} catch (ValidadorEsquemaException e)
		{
			log= new LogHelper(nombreLog());
			log.error ("****Error controlado:"+e.getMessage());
			PagoModelo600FaultInfo info = new PagoModelo600FaultInfo();
    		info.setId("F001");
    		info.setMensaje(MSG_ERROR_F001+e.getMessage());
    		PagoFault fallo = new PagoFault(e.getMessage(),info);
    		throw fallo;
		}
		catch (Exception e) {
			if (log!=null){ // En este punto podría serlo.
				log.error("****Excepción inesperada: ".concat(e.getMessage()));
				log.trace(e.getStackTrace());
			}else{// Lo intentamos con el log por defecto..
				log = new LogHelper(nombreLog());
				log.error("****Excepción inesperada: " + e.getMessage());
				log.trace (e.getStackTrace());
			}
			return "Error: ".concat(e.getMessage());
		}finally {
			if (log==null){
				try {
					log=new LogHelper(nombreLog()); // Log por defecto, no se pudo construir el otro
				}catch (Exception ex){
					System.err.println("Error al construir el log en el bloque finally:" + ex.getMessage());
				}
			}
			if (log!=null && cal!=null){
				log.info(mensOut(cal));
			}
		}
	}
	public Document getXmlEntrada(String xmlData,ValidacionFactory validFactory, java.util.Calendar cal) throws PreferenciasException, PresentacionException
	{
		//Cargamos los datos del almacen de un fichero xml preferencias
		pref.CargarPreferencias();

		//Validacion XML
		XMLEntValidator eValidator = (XMLEntValidator)validFactory.newParameterValidator();
		Document docRespuesta = null;
		LogHelper log = new LogHelper(nombreLog());

		if (!eValidator.isValid(xmlData)){
			log.info(mensIn(cal));
			log.info(xmlData);
			log.error("****Error al validar el documento de entrada. Detalles a continuación:");
			log.error(eValidator.getResultado().toString());
			return null;
		}
		return docRespuesta;
	}
	
	/**
	 * Inicializa el log al nombre por defecto.
	 * @return Nombre de log por defecto.
	 */
	private LogHelper logPorDefecto()
	{
		return new LogHelper(nombreLog());
	}
	private Document validarXmlEntrada( String xmlData, LogHelper log,ValidacionFactory validFactory, java.util.Calendar cal) throws PresentacionException
	{
		//Validacion XML
		XMLEntValidator eValidator = (XMLEntValidator)validFactory.newParameterValidator();
		Document docRespuesta = null;

		if (!eValidator.isValid(xmlData)){
			log.info(mensIn(cal));
			log.info(xmlData);
			log.error("****Error al validar el documento de entrada. Detalles a continuación:");
			log.error(eValidator.getResultado().toString());
			return null;
		}
		log.info(mensIn(cal));
		docRespuesta= eValidator.getXMLValidado();
		return docRespuesta;
	}
	/**
	 * Inicializa el log basado en los datos de la petición.
	 * @param doc
	 * @return
	 */
	private LogHelper logBasadoEnPeticion(Document doc)
	{
		String nameLog;
		if (doc!=null){
			nameLog=nombreLog(doc);
		}else{
			nameLog =nombreLog();
		}
		return new LogHelper(nameLog);
	}
	/**
	 * Inicializa los log de la petición.
	 * @param log Log a usar
	 * @param frFactory Factoría de firma
	 * @param validFactory Factoría de validación
	 * @param bFactory Factoría de objetos de negocio.
	 * @param docFactory Factoría de documentos
	 * @throws Exception
	 */
	private void inicializarLogs(LogHelper log,FirmaFactory frFactory,ValidacionFactory validFactory,BOFactory bFactory, DocsFactory docFactory) throws Exception{

		frFactory.setLogger(log);
		validFactory.setLogger(log);
		if (bFactory!=null) bFactory.setLogger(log);
		if (docFactory!=null) docFactory.setLogger(log);
		return;
	}

	private String validarFirma(LogHelper log,String xmlData,ValidacionFactory validFactory){
		//-- VALIDACION DE LA FIRMA DIGITAL --
		if ("S".equals(pref.getValidaFirma())) {
			log.info("====Entrada validación firma de documento.");
			try {
				boolean integridadXML = false;
				WsFirmaDigital srv = new WsFirmaDigital();					
				FirmaDigital srPort = srv.getServicesPort();
				try {
					//Se modifica el endpoint
					String endpointFirma = pref.getEndpointFirma();
					if (!"".equals(endpointFirma)) {
						BindingProvider bpr = (BindingProvider) srPort;
						bpr.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,endpointFirma);
					}
					try {
						integridadXML = srPort.validar(xmlData);
						if (!integridadXML){
							log.info("====2 - FIRMA INCORRECTA");
						}else{
							log.info("====2 - FIRMA CORRECTA");
						}
					} catch (Exception ex) {
						log.error("****Error al validar la firma 1 :"+ ex.getMessage());
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
					return Utils.setErrores(xmlData,MSG_INFO_0010,"0010");
				}else{
					if ("S".equals(pref.getValidaCertificado()))
					{
						CertificadoValidator validator = (CertificadoValidator) validFactory.newCertificadoValidator();
						validator.setLogger(log);
						if (!validator.isValid(xmlData)) {
							log.error("****El certificado no es válido");
							return Utils.setErrores(xmlData,MSG_INFO_0011,"0011");
						}
					}
				}
			} catch (Exception e1) {
				log.error("****No se ha podido validar la firma:"+ e1.getMessage());
				log.trace(e1.getStackTrace());
				return Utils.setErrores(xmlData,MSG_INFO_0100,"0100");
			}
			log.info("====Salida: Validación firma de documento.");
		}
		return null;
	}

	private String validarEsquema(LogHelper log,String xmlData)throws ValidadorEsquemaException, ParserConfigurationException{
		//Se prepara la llamada al procedimiento almacenado
		ConversorParametrosLanzador cpl = new ConversorParametrosLanzador();
		cpl.setProcedimientoAlmacenado(pref.getPAValidarPresentacion());
		String xmlDatos = new String(); //XmlDatos = "600";
		xmlDatos = ("<![CDATA[".concat(xmlData)).concat("]]>");	
		cpl.setParametro(xmlDatos, ConversorParametrosLanzador.TIPOS.Clob);
		cpl.setParametro("P", ConversorParametrosLanzador.TIPOS.String); //Conexion oracle
		log.debug(("metidos parametros ").concat(cpl.Codifica()));
		LanzaPLService pA = new LanzaPLService();
		LanzaPL lpl;

		if (!pref.getEndpointLanzador().equals("")) {
			log.debug("Se utiliza el endpoint de lanzadera: "+ pref.getEndpointLanzador());
			lpl = pA.getLanzaPLSoapPort();
			//Enlazador del protocolo para el servicio
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lpl;
			bpr.getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointLanzador()); // Cambiamos el endpoint
		}else{
			log.debug("Se utiliza el endpoint de lanzadera por defecto: "+ javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
			lpl = pA.getLanzaPLSoapPort();
			//Enlazador del protocolo para el servicio
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lpl; 
			log.debug("Se utiliza el endpoint de lanzadera por defecto: "+ bpr.getRequestContext().get(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
		}
		String respuesta = "";
		log.info("====Entrada validación esquema de documento.");

			respuesta = lpl.executePL(pref.getEntorno(), cpl.Codifica(),"", "", "", "");
			cpl.setResultado(respuesta);
			String error = cpl.getNodoResultadoX("error");
			if (!error.equals("")){
				log.error("Error recibido del validador de esquema:" + error);
				return Utils.setErrores(xmlData,MSG_INFO_0100,"0100");
			}
			String coderror = cpl.getNodoResultadoX("STRING1_CANU");
			String canu = cpl.getNodoResultadoX("STRING2_CANU");
			if (!canu.equals("") && !"0100".equals(coderror) && !"0020".equals(coderror)){
				throw new ValidadorEsquemaException (canu);
			}
			if ("0020".equals(coderror)){
				log.error("Error recibido del validador de esquema:" + error);
				String razon="";
				if (canu!=null)
					razon="Razón: "+canu;
				return Utils.setErrores(xmlData,MSG_INFO_0020+razon,"0020");					
			}else
				return null;
	}

	
	public es.tributasenasturias.servicios.calculo600generico.Remesa stringToRemesaCalculo(String xmlRemesa)throws CalculoFault {
		es.tributasenasturias.servicios.calculo600generico.Remesa remesa=null;
		try {
			JAXBContext context=JAXBContext.newInstance(es.tributasenasturias.servicios.calculo600generico.Remesa.class.getPackage().getName());
			Unmarshaller unmars=context.createUnmarshaller();
			remesa=(es.tributasenasturias.servicios.calculo600generico.Remesa)unmars.unmarshal(new StringReader(xmlRemesa));
			return remesa;
		} catch (JAXBException e) {
    		CalculoModelo600FaultInfo info=new CalculoModelo600FaultInfo();
    		info.setId("F002");
    		info.setMensaje(MSG_ERROR_F002);
    		CalculoFault fallo=new CalculoFault(e.getMessage(),info);
    		throw fallo;
		}
	}

	public es.tributasenasturias.servicios.pago600generico.Remesa stringToRemesaPago(String xmlRemesa)throws PagoFault {
		es.tributasenasturias.servicios.pago600generico.Remesa remesa=null;
		try {
			JAXBContext context=JAXBContext.newInstance(es.tributasenasturias.servicios.pago600generico.Remesa.class.getPackage().getName());
			Unmarshaller unmars=context.createUnmarshaller();
			remesa=(es.tributasenasturias.servicios.pago600generico.Remesa)unmars.unmarshal(new StringReader(xmlRemesa));
			return remesa;
		} catch (JAXBException e) {
    		PagoModelo600FaultInfo info=new PagoModelo600FaultInfo();
    		info.setId("F002");
    		info.setMensaje(MSG_ERROR_F002);
    		PagoFault fallo=new PagoFault(e.getMessage(),info);
    		throw fallo;
		}
	}

	private String dirLog(){
		String dir="";
		java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat();
		sdf.applyPattern("yyyyMMdd");
		dir=sdf.format (new java.util.Date());
		return dir;
	}

	private String nombreLog(){
		//Directorio basado en el día de hoy, y nombre de fichero genérico.
		String name=pref.getDirApp()+"/logs/"+dirLog()+"/"+"Application.log";
		return name;
	}

	private String getNumAutoliquidacion(String doc, LogHelper log)
	{
		String numAutoliquidacion="";
		try {
			Document docs = XMLDOMUtils.parseXml(doc);
			numAutoliquidacion = getNumAutoliquidacion(docs);
		} catch (XMLDOMDocumentException e) {
			log.error("El documento no es válido, no tiene número de autoliquidación.");
		}
		return numAutoliquidacion;
	}
	private String getNumAutoliquidacion(Document doc)
	{
		String numAutoliquidacion="";
		org.w3c.dom.NodeList nl= XMLDOMUtils.getAllNodesXPath(doc,"/*[local-name()='remesa']/*[local-name()='declaracion']");
		if (nl.getLength()>0){
			org.w3c.dom.NamedNodeMap nnm=nl.item(0).getAttributes();
			org.w3c.dom.Node codigo = nnm.getNamedItem("codigo_declaracion");
			if (codigo!=null)
			{
				numAutoliquidacion=codigo.getNodeValue();
			}
		}
		return numAutoliquidacion;
	}
	private String nombreLog(Document doc){
		//Basamos el nombre del log en la fecha actual y en los datos de la identificación.
		//Por defecto habrá un nombre de log basado en la fecha actual.
		String name="";
		if (doc==null){
			name=nombreLog();
		}
		String numAutoliquidacion=getNumAutoliquidacion(doc);
		if (!"".equals(numAutoliquidacion))
		{
			name=pref.getDirApp()+"/logs/"+dirLog()+"/"+numAutoliquidacion+".log";
		}
		else
		{
			name=nombreLog();
		}
		return name;
	}

	private String mensIn(java.util.Calendar cal){
		return "=========INICIO DE PETICIÓN EL " + cal.get(java.util.Calendar.DAY_OF_MONTH) + "/"+
		cal.get(java.util.Calendar.MONTH)+"/"+cal.get(java.util.Calendar.YEAR) + " "+ 
		cal.get(java.util.Calendar.HOUR_OF_DAY) + ":"+ cal.get(java.util.Calendar.MINUTE) + ":"+
		cal.get(java.util.Calendar.SECOND);
	}

	private String mensOut (java.util.Calendar cal){
		return "=========FINAL DE LA PETICIÓN INICIADA EL " + cal.get(java.util.Calendar.DAY_OF_MONTH) + "/"+
		cal.get(java.util.Calendar.MONTH)+"/"+cal.get(java.util.Calendar.YEAR) + " "+ 
		cal.get(java.util.Calendar.HOUR_OF_DAY) + ":"+ cal.get(java.util.Calendar.MINUTE) + ":"+
		cal.get(java.util.Calendar.SECOND);
	}
	/**
	 * Recupera los datos de la escritura, si existe.
	 * @param xml Xml de  entrada del servicio de pago y presentación.
	 * @return
	 */
	private DatosEscritura getEscritura(Document xml)
	{
		Element[] escritura = XMLUtils.selectNodes(xml,"//*[local-name()='declaracion']/*[local-name()='escritura']");
		DatosEscritura des= new DatosEscritura();
		if (escritura.length!=0)
		{
			Element nfirmaEscritura=XMLUtils.selectSingleNode(escritura[0],"*[local-name()='firmaEscritura']");
			Element ndocEscritura=XMLUtils.selectSingleNode(escritura[0],"*[local-name()='documentoEscritura']");
			if (nfirmaEscritura!=null) 
			{
				des.setFirmaEscritura(nfirmaEscritura.getTextContent());
			}
			else
			{
				des.setFirmaEscritura("");
			}
			if (ndocEscritura!=null)
			{
				des.setDocEscritura(ndocEscritura.getTextContent());
			}
			else
			{
				des.setDocEscritura("");
			}
		}
		return des;
	}
	/**
	 * Realiza el alta de una escritura
	 * @param it Datos de la integración con tributas.
	 * @param datEscritura Datos de la escritura.
	 * @param bFactory Factory de objetos de negocio.
	 * @return Estado del alta de la escritura.
	 * @throws PresentacionException
	 */
	private AltaEscritura.EstadosAltaEscritura altaEscritura(IntegraTributas it, DatosEscritura datEscritura, BOFactory bFactory) throws PresentacionException
	{
		
		// Alta de la escritura:
		if (datEscritura.getDocEscritura()!=null && datEscritura.getDocEscritura().length()!=0) {
			AltaEscritura altaEscritura = bFactory.newAltaEscritura();
			return altaEscritura.Inserta(it.getCodNotarioEscritura(), it
					.getCodNotariaEscritura(), it.getNumProtocoloEscritura(), it
					.getProtocoloBisEscritura(), it.getFechaAutorizacionEscritura(),
					datEscritura.getFirmaEscritura(), datEscritura.getDocEscritura());
			
		}
		return AltaEscritura.EstadosAltaEscritura.NO_NECESARIA_ALTA;
	}
}