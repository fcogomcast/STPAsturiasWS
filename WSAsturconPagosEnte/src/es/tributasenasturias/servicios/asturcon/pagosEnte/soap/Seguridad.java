package es.tributasenasturias.servicios.asturcon.pagosEnte.soap;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.x500.X500Principal;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.tributasenasturias.seguridad.servicio.FirmaHelper;
import es.tributasenasturias.seguridad.servicio.PropertyConfigurator;
import es.tributasenasturias.seguridad.servicio.SeguridadException;
import es.tributasenasturias.seguridad.servicio.SeguridadFactory;
import es.tributasenasturias.seguridad.servicio.VerificadorPermisoServicio;
import es.tributasenasturias.servicios.asturcon.pagosEnte.Utils.Base64;
import es.tributasenasturias.servicios.asturcon.pagosEnte.Utils.Utils;
import es.tributasenasturias.servicios.asturcon.pagosEnte.preferencias.Preferencias;
import es.tributasenasturias.servicios.asturcon.pagosEnte.wssecurity.WSSecurityFactory;
import es.tributasenasturias.servicios.asturcon.pagosEnte.xml.XMLDOMDocumentException;
import es.tributasenasturias.servicios.asturcon.pagosEnte.xml.XMLDOMUtils;

import es.tributasenasturias.utils.log.Logger;

public class Seguridad implements SOAPHandler<SOAPMessageContext> {

	
	/**
	 * Extraemos el nodo del mensaje XML de vuelta.
	 * @param context contexto del mensaje SOAP
	 * @return {@link Node} con el nodo del mensaje.
	 */
	private Node extraerMensaje(SOAPMessageContext context) 
	{
		Node payload=null;
		try
		{
			SOAPMessage msg = context.getMessage();
			NodeList mensajes= msg.getSOAPBody().getElementsByTagName("MENSAJE");
			if (mensajes.getLength()>0)
			{
				payload=mensajes.item(0);
			}
		}
		catch (javax.xml.soap.SOAPException ex)
		{
			Utils.generateSOAPErrMessage(context.getMessage(), "Error en proceso de mensaje SOAP."	, "0001", "Error al interpretar el mensaje de entrada para crear el entorno de la llamada");
		}
		return payload;
	}
	/**
	 * Indica si la firma es válida
	 * @param mensajeFirmado Cadena con xml firmado
	 * @param pref Referencia a las preferencias del proyecto.
	 * @return
	 */
	private boolean validaFirma (String mensajeFirmado, Preferencias pref)
	{
		try
			{
				FirmaHelper firma= SeguridadFactory.newFirmaHelper(pref.getEndPointFirma());
				return firma.esValido(mensajeFirmado);
			}
			catch (SeguridadException ex)
			{
				return false;
			}
	}
	
	private String firmaMensaje (String mensaje, Preferencias pref, String id) throws SeguridadException
	{
			FirmaHelper firma= SeguridadFactory.newFirmaHelper(pref.getEndPointFirma(), new SoapClientHandler("Nueva sesión"));
			return firma.firmaXML(mensaje, pref.getAliasCertificadoFirma(), id, pref.getNodoContenedorFirma(), pref.getNsNodoContenedorFirma());
	}
	/**
	 * Extrae el contenido del certificado de la firma de WSSecurity
	 * @param mensaje Mensaje firmado.
	 * @return Cadena que contiene el certificado de la firma.
	 * @throws XMLDOMDocumentException
	 */
	private String extraerCertificado(String mensaje)
			throws XMLDOMDocumentException {
		Document doc = XMLDOMUtils.parseXml(mensaje);
		Node certificado = XMLDOMUtils
				.selectSingleNode(
						doc,
						"/*[local-name()='Envelope']/*[local-name()='Header']/*[local-name()='Security']/*[local-name()='BinarySecurityToken']/text()");
		if (certificado==null)
		{
			return "";
		}
		return certificado.getNodeValue();

	}
	/**
	 * Comprueba si el certificado es válido y tiene permisos sobre el servicio.
	 * @param pref Preferencias
	 * @param context Contexto de mensaje SOAP
	 * @param log Logger
	 * @param idSesion Id de la sesión
	 * @throws XMLDOMDocumentException En caso de no poder extraer el certificado del mensaje SOAP
	 * @throws SeguridadException En caso de no poder comprobar los permisos.
	 */
	private boolean comprobarCertificado(String xml,Preferencias pref, SOAPMessageContext context,Logger log,String idSesion)
			throws XMLDOMDocumentException, SeguridadException
	{
		// Extraemos el certificado.
		String certificado = extraerCertificado(xml);
		// Comprobamos permisos sobre el certificado
		VerificadorPermisoServicio per = SeguridadFactory
			.newVerificadorPermisoServicio(new PropertyConfigurator(
						pref.getEndPointAutenticacion(), pref.getEndPointLanzador(), 
						pref.getPAPermisoServicio(), pref.getEsquemaBaseDatos(),
						new SoapClientHandler(idSesion)));
		 return per.tienePermisosCertificado(certificado, pref.getAliasServicio());
	}
	private String getCIFCertificadoAsturcon (String certificado) throws SeguridadException
	{
		String cif="";
		if (certificado==null || "".equals(certificado))
		{
			throw new SeguridadException ("El certificado recuperado del mensaje está vacío, no se puede recuperar su CIF.");
		}
		byte[] decodificado = Base64.decode(certificado.toCharArray());
		CertificateFactory cf;
		try {
			cf = CertificateFactory.getInstance("X.509");
			Certificate cert = cf.generateCertificate(new ByteArrayInputStream(decodificado));
	        X509Certificate cert5 = (X509Certificate) cert;
	        X500Principal principal = cert5.getSubjectX500Principal();
	        //Del principal, tomamos el nombre según el RFC1779, de tal forma que podremos
	        //leer el SerialNumber como "OID.2.5.4.5", y en caracteres legibles.
	        //Si no usásemos RFC1779, tendríamos que decodificarlo.
	        String name=principal.getName(X500Principal.RFC1779);
	        int idx = name.indexOf("OID.2.5.4.5");
	        if (idx!=-1)
	        {
	        	//Tiene serialNumber, podemos leerlo. 
	        	cif = name.substring(idx+12, name.indexOf(",",idx));
	        }
	        else
	        {
	        	throw new SeguridadException ("No se puede recuperar el cif del certificado de Asturcon.");
	        }
		} catch (CertificateException e) {
			throw new SeguridadException ("Error al recuperar el CIF del certificado Asturcon:"+ e.getMessage(),e);
		}
		return cif;
	}
	/**
	 * Comprueba si la identidad del certificado tiene permisos sobre el servicio.
	 * @param pref Preferencias
	 * @param context Contexto de mensaje SOAP
	 * @param log Logger
	 * @param idSesion Id de la sesión
	 * @throws XMLDOMDocumentException En caso de no poder extraer el certificado del mensaje SOAP
	 * @throws SeguridadException En caso de no poder comprobar los permisos.
	 */
	private boolean comprobarPermisos(String xml,Preferencias pref, SOAPMessageContext context,Logger log,String idSesion)
			throws XMLDOMDocumentException, SeguridadException
	{
		// Extraemos el certificado.
		String certificado = extraerCertificado(xml);
		String cif = getCIFCertificadoAsturcon(certificado);
		// Comprobamos permisos sobre el certificado
		VerificadorPermisoServicio per = SeguridadFactory
			.newVerificadorPermisoServicio(new PropertyConfigurator(
						pref.getEndPointAutenticacion(), pref.getEndPointLanzador(), 
						pref.getPAPermisoServicio(), pref.getEsquemaBaseDatos(),
						new SoapClientHandler(idSesion)));
		 return per.tienePermisosCIF(cif, pref.getAliasServicio());
	}
	/**
	 * Valida la seguridad del servicio si se ha configurado de esa forma en preferencias.
	 * Valida firma y permisos del firmante sobre el servicio.
	 * @param context
	 */
	private void validacion(SOAPMessageContext context) throws SOAPFaultException{
		Logger log = null;
		try {
			String idSesion = "Sin sesión conocida";
			//Recuperamos de contexto los objetos que necesitamos.
			//Si no se puede traer alguno de ellos, es un error.
			Preferencias pref = (Preferencias) context.get("Preferencias"); 
			//PreferenciasFactory.newInstance();
			idSesion = (String) context.get("IdSesion");
			log = (Logger) context.get("LogAplicacion");
			if (pref==null || log == null)
			{
				throw new Exception ("Error. No se encuentran los objetos de preferencias o log en contexto de llamada SOAP.");
			}
			Boolean salida = (Boolean) context
					.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
			if (!salida
					&& ("S".equals(pref.getValidaPermisosServicio()) || 
						"S".equals (pref.getValidaCertificado()) ||
						"S".equals(pref.getValidaFirma()))) 
			{
				// Recuperamos de contexto la fuente XML.
				String xml = (String) context.get("XMLSource");
				if ("S".equalsIgnoreCase(pref.getValidaFirma()))
				{
					log.info("Se procede a validar la firma de mensaje");
					boolean firmaCorrecta = WSSecurityFactory.newConstructorResultado(log, pref,new SoapClientHandler(idSesion)).validaFirmaSinCertificado(xml);
					if (firmaCorrecta)
					{
						log.info("Firma validada");
					}
					else
					{
						log.info("Firma no válida");
//						//Soap Fault, la seguridad no deja pasar el mensaje.
						Utils.generateSOAPErrMessage(context.getMessage(),
								"Error en proceso de mensaje SOAP.", "0002", "Seguridad");	
					}
				}

				if ("S".equalsIgnoreCase(pref.getValidaCertificado()))
				{
					log.info ("Se procede a comprobar si el certificado de la firma es válido.");
					boolean cerValido=comprobarCertificado(xml,pref,context,log,idSesion);
					if (cerValido)
					{
						log.info ("Certificado válido");
					}
					else
					{
						log.info ("Certificado no válido");
						//Soap Fault, la seguridad no deja pasar el mensaje.
						Utils.generateSOAPErrMessage(context.getMessage(),
								"Error en proceso de mensaje SOAP.", "0002", "Seguridad");
					}
				}
				if ("S".equalsIgnoreCase(pref.getValidaPermisosServicio()))
				{
					log.info ("Se procede a comprobar si la persona asociada con el certificado tiene permisos sobre el servicio.");
					boolean cerValido=comprobarPermisos(xml,pref,context,log,idSesion);
					if (cerValido)
					{
						log.info ("Existen permisos sobre el servicio.");
					}
					else
					{
						log.info ("No existen permisos sobre el servicio.");
						//Soap Fault, la seguridad no deja pasar el mensaje.
						Utils.generateSOAPErrMessage(context.getMessage(),
								"Error en proceso de mensaje SOAP.", "0002", "Seguridad");
					}
				}
			}
			else if (salida && "S".equalsIgnoreCase(pref.getValidaFirma())){ //En salida, firmamos si a la entrada comprobamos.
				String mensajeSalida = XMLDOMUtils.getPrettyXMLText(context.getMessage().getSOAPPart());
				mensajeSalida=WSSecurityFactory.newConstructorResultado(log, pref,new SoapClientHandler(idSesion)).firmaMensaje(mensajeSalida);
				context.getMessage().getSOAPPart().setContent(new StreamSource(new StringReader(mensajeSalida)));
			}
//			else if (salida && "S".equalsIgnoreCase(pref.getValidaFirma())){ //En salida, firmamos si a la entrada comprobamos.
//				//TODO: Poner una preferencia propia para esto
//				String mensajeSalida = XMLDOMUtils.getPrettyXMLText(context.getMessage().getSOAPPart());
//				String id = (String)context.get("ID_Mensaje");
//				//Firmamos el mensaje.
//				mensajeSalida = firmaMensaje(mensajeSalida, pref, id);
//				context.getMessage().getSOAPPart().setContent(new StreamSource(new StringReader(mensajeSalida)));
//			}
		} catch (XMLDOMDocumentException ex) {
			log.error("Error en las comprobaciones de seguridad del mensaje:"
					+ ex.getMessage(), ex);
			Utils.generateSOAPErrMessage(context.getMessage(),
					"Error en proceso de mensaje SOAP.", "0002", "Seguridad");
		}
		catch (SeguridadException ex) {
			log.error("Error en las comprobaciones de seguridad del mensaje:"
					+ ex.getMessage(), ex);
			Utils.generateSOAPErrMessage(context.getMessage(),
					"Error en proceso de mensaje SOAP.", "0002", "Seguridad");
		}
		catch (Exception ex)
		{
			if (!(ex instanceof SOAPFaultException))
			{
				if (log!=null)
				{
					log.error("Error en las comprobaciones de seguridad del mensaje:"
							+ ex.getMessage(), ex);
				}
				else
				{
					System.err.println (new java.util.Date().toString()+"::Asturcon Consulta Deuda::error en las comprobaciones de seguridad del mensaje::"+ex.getMessage());
					ex.printStackTrace();
					
				}
				Utils.generateSOAPErrMessage(context.getMessage(),
						"Error en proceso de mensaje SOAP.", "0002", "Seguridad");
			}
			else
			{
				throw (SOAPFaultException)ex;
			}
		}
	}

	@Override
	public Set<QName> getHeaders() {
		//Indicamos que entendemos la cabecera de seguridad de WS-Security.
		QName security= new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd","Security");
		HashSet<QName> headersEntendidos= new HashSet<QName>();
		headersEntendidos.add(security);
		return headersEntendidos;
	}

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		validacion(context);
		return true;
	}

}
