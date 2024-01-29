package es.tributasenasturias.webservice;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.BindingProvider;

import es.tributasenasturias.firmadigital.client.WsFirmaDigital;
import es.tributasenasturias.firmadigital.client.FirmaDigital;
import es.tributasenasturias.utils.ConversorParametrosLanzador;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.validacion.CertificadoValidator;
import es.tributasenasturias.webservices.clients.LanzaPL;
import es.tributasenasturias.webservices.clients.LanzaPLService;
import es.tributasenasturias.utils.Utils;

/*******************************************************************************
 * 
 * Clase WebService que obtiene los datos de la petición de cálculo del modelo
 * 600.
 * 
 * Recupera la información, realiza la llamada al procedimiento almacenado que
 * calcula el resultado de la autoliquidación y devuelve el resultado.
 * 
 ******************************************************************************/

@WebService(name = "CalculoModelo600")
@HandlerChain(file = "HandlerChain.xml")
public class CalculoModelo600 {
	private Preferencias pref = new Preferencias();
	private String XML_MSG_ERROR = "<remesa><resultado><codigo></codigo><descripcion></descripcion></resultado></remesa>";	
	private String MSG_ERROR_01_01 = " Error en el XML de entrada (XML Vacío)";	
	private String MSG_ERROR_01_02 = " Error en el XML de entrada (Integridad de la Firma)";
	private String MSG_ERROR_01_03 = " Error en el XML de entrada (Certificado Inválido)";
	private String MSG_ERROR_01_04 = " Error en el XML de entrada (Genérico al validar Firma)";

	/*private String MSG_ERROR_02_01 = " Error en el proceso de pago";
	private String MSG_ERROR_02_02 = " Error en el proceso de alta de expediente (Integración)";
	private String MSG_ERROR_02_03 = " Error en el proceso de alta de expediente (Alta de documento)";
	private String MSG_ERROR_02_04 = " Error en el proceso de alta de expediente (Alta Expediente)";
	*/
		
	public CalculoModelo600() {
		// de este modo, al instalar el webservice, se creara el fichero de
		// preferencias si no existe
		// CompruebaFicheroPreferencias();
		// de este modo, al instalar el webservice, se creara el fichero de
		// preferencias si no existe
		pref.CompruebaFicheroPreferencias();
	}

	/**
	 * getCalculoModelo600 realiza el cálculo del modelo 600 en base a los datos
	 * de la autoliquidación de entrada
	 * 
	 * @param xmlData -
	 *            Requiere el contenido de un XML con los datos de la
	 *            autoliquidación del modelo 600
	 * @return Devuelve un xml el resutado del cálculo del modelo 600.
	 */
	@WebMethod()
	public String getCalculoModelo600(@WebParam(name = "xmlData")
	String xmlData) {
		try {
			// cargamos los datos del almacen de un fichero xml preferencias
			// CargarPreferencias();
			pref.CargarPreferencias();

			// Se comprueban los datos de entrada
			if (xmlData == null || xmlData.length() == 0) {								 		
				return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_01_01, "01");
			}

			/*
			 * if (pref.getDebug().equals("1")) 
				 Logger.info(("XML DE ENTRADA:"+xmlData));
			*/

			// ==============================
			// Validación de la firma DIGITAL
			// ==============================
			if (pref.getValidaFirma().equals("S")) {							
				try {
					
					boolean integridadXML = false;															
					///==========================================
					WsFirmaDigital srv = new WsFirmaDigital();					
					FirmaDigital srPort = srv.getServicesPort();					
					try
					{
						//Se modifica el endpoint						
						String endpointFirma = pref.getEndpointFirma();
						if (!endpointFirma.equals(""))
						{
							BindingProvider bpr = (BindingProvider) srPort;
							bpr.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,endpointFirma);
						}

						try
						{						
							integridadXML = srPort.validar(xmlData);
						
							if (pref.getDebug().equals("1")) {
								if (!integridadXML)
									Logger.info("2 - FIRMA INCORRECTA");
								else
									Logger.info("2 - FIRMA CORRECTA");
							}														
						}
						catch (Exception ex)
						{					
							Logger.error ("Error al validar la firma 1 :" + ex.getMessage());							
							integridadXML=false;						
						}
					}
					catch (Exception ex)
					{						
						Logger.error ("Error al recuperar el endpoint del servicio de firma:" + ex.getMessage());						
						integridadXML=false;
					}
														
					if (!integridadXML) {
						Logger.error("La integridad del documento firmada no es valida.");
						return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_01_02, "01");
					} 
					else 
					{											
						CertificadoValidator validator = new CertificadoValidator();						
						if (!validator.isValid(xmlData)) {
							Logger.error("El certificado no es válido");
							return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_01_03, "01");
						}												
					}
				
				} catch (Exception e1) {
					Logger.error("No se ha podido validar la firma:"+ e1.getMessage());
					return Utils.setErrores(XML_MSG_ERROR,MSG_ERROR_01_04, "01");
				}
			}
		 
			
			//Se prepara la llamada al procedimiento almacenado
			ConversorParametrosLanzador cpl = new ConversorParametrosLanzador();
			cpl.setProcedimientoAlmacenado(pref.getPACalculo());
			// xml
			String xmlDatos = new String();
			xmlDatos = ("<![CDATA[".concat(xmlData)).concat("]]>");	
			// xmlDatos = "600";
			cpl.setParametro(xmlDatos, ConversorParametrosLanzador.TIPOS.Clob);
			// conexion oracle
			cpl.setParametro("P", ConversorParametrosLanzador.TIPOS.String);
			
			if (pref.getDebug().equals("1")) 
			 Logger.info(("metidos parametros ").concat(cpl.Codifica()));

			LanzaPLService pA = new LanzaPLService();
			LanzaPL lpl;

			if (!pref.getEndpointLanzador().equals("")) {
				if (pref.getDebug().equals("1")) {
					Logger.debug("Se utiliza el endpoint de lanzadera: "
							+ pref.getEndpointLanzador());
				}
				lpl = pA.getLanzaPLSoapPort();
				javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lpl; // enlazador
																						// de
																						// protocolo
																						// para
																						// el
																						// servicio.
				bpr.getRequestContext().put(
						javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
						pref.getEndpointLanzador()); // Cambiamos el endpoint
			} else {
				if (pref.getDebug().equals("1")) {
					Logger
							.debug("Se utiliza el endpoint de lanzadera por defecto: "
									+ javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
				}
				lpl = pA.getLanzaPLSoapPort();
				javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lpl; // enlazador
																						// de
																						// protocolo
																						// para
																						// el
																						// servicio.
				if (pref.getDebug().equals("1")) {
					Logger
							.debug("Se utiliza el endpoint de lanzadera por defecto: "
									+ bpr
											.getRequestContext()
											.get(
													javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
				}
			}

			String respuesta = new String();
			try {
				respuesta = lpl.executePL(pref.getEntorno(), cpl.Codifica(),
						"", "", "", "");
				cpl.setResultado(respuesta);

				// Firmamos salida del Servicio
				if (pref.getFirmaDigital().equals("S")) {
					try {
						
						FirmaHelper firmaDigital = new FirmaHelper();
						if (pref.getDebug().equals("1")) {
						Logger.info("LO QUE MANDO A FIRMAR:"
								+ cpl.getNodoResultado("CLOB_DATA"));
						}
						respuesta = firmaDigital.firmaMensaje(cpl
								.getNodoResultado("CLOB_DATA"));

						if (pref.getDebug().equals("1")) {
							Logger.info("RESPUESTA:"+respuesta);							
						}
						
						
					} catch (RuntimeException e) {
						Logger.error("Error en firma:" + e.getMessage());
						e.printStackTrace();
					}
				} else {
					respuesta = cpl.getNodoResultado("CLOB_DATA");
				}
			} catch (Exception ex) {
				if (pref.getDebug().equals("1")) {
					Logger.error("LANZADOR: ".concat(ex.getMessage()));
				}
				throw ex;
			}

			if (pref.getDebug().equals("1")) {
				Logger.info("respuesta: ".concat(respuesta));
			}
			
			return respuesta;
			
		} catch (Exception e) {
			if (pref.getDebug().equals("1")) {
				Logger.error("error".concat(e.getMessage()));
			}
			return "error".concat(e.getMessage());
		}
	}

}
