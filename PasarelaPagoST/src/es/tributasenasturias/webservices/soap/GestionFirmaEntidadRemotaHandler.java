package es.tributasenasturias.webservices.soap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.tributasenasturias.seguridad.servicio.FirmaHelper;
import es.tributasenasturias.seguridad.servicio.InfoCertificado;
import es.tributasenasturias.seguridad.servicio.PropertyConfigurator;
import es.tributasenasturias.seguridad.servicio.SeguridadException;
import es.tributasenasturias.seguridad.servicio.SeguridadFactory;
import es.tributasenasturias.seguridad.servicio.VerificadorCertificado;
import es.tributasenasturias.seguridad.servicio.VerificadorPermisoServicio;
import es.tributasenasturias.utils.Base64;
import es.tributasenasturias.utils.Preferencias;
import es.tributasenasturias.utils.X500PrincipalHelper;
import es.tributasenasturias.xml.XMLDOMDocumentException;
import es.tributasenasturias.xml.XMLDOMUtils;
 
/**
 * Clase manejadora de mensajes soap
 * 
 * @author Andres
 *
 */
public class GestionFirmaEntidadRemotaHandler implements SOAPHandler<SOAPMessageContext> 
{
	
	private String endpointFirma;
	private String paPermisoServicio;
	private String esquema;
	private String endpointLanzador;
	private String idNodoFirma;
	private String nodoPadreFirma;
	private String nsNodoPadreFirma;
	private String aliasFirma;
	private String validaCertificado;
	private String aliasServicio;
	//CRUBENCVS 42479. 14/09/2021.
	private String endpointAutenticacionEPST;
	private String idLlamada;
	public GestionFirmaEntidadRemotaHandler (String idNodo, String nodoPadre, String nsNodoPadre, String validaCertificado, String aliasServicio, String idLlamada)
	{
		try
		{
		Preferencias pref= Preferencias.getPreferencias();
		this.endpointFirma=pref.getEndPointFirma();
		this.endpointLanzador=pref.getEndPointLanzador();
		this.esquema=pref.getEsquemaBaseDatos();
		this.aliasFirma=pref.getAliasCertificadoFirma();
		this.paPermisoServicio=pref.getPAPermisoServicio();
		//CRUBENCVS 42479 14/09/2021
		this.endpointAutenticacionEPST = pref.getEndpointAutenticacionCert();
		}
		catch (Exception ex)
		{
			this.endpointFirma="";//Fallará en el envío.
		}
		this.idNodoFirma=idNodo;
		this.nodoPadreFirma=nodoPadre;
		this.nsNodoPadreFirma=nsNodoPadre;
		this.validaCertificado=validaCertificado;
		this.aliasServicio=aliasServicio;
		//CRUBENCVS 42479 14/09/2021
		this.idLlamada= idLlamada;
	}
	
	
	public static final SOAPFaultException createSOAPFaultException(String pFaultString) throws SOAPException
	 {
		 try
		 {
		 SOAPFaultException sex=null;
		 SOAPFault fault = SOAPFactory.newInstance().createFault();
		 fault.setFaultString(pFaultString);
		 fault.setFaultCode(new QName(SOAPConstants.URI_NS_SOAP_ENVELOPE, "FirmaMensajeSalida"));
		 sex= new SOAPFaultException(fault);
		 return sex;
		 }
		 catch (Exception ex)
		 {
			 throw new SOAPException ("FirmaMensajeSalida:==> Error grave al construir la excepción SOAP." + ex.getMessage());
		 }
	} 
	
    public boolean handleMessage(SOAPMessageContext messageContext) 
    {
    	firma(messageContext);
    	return true;
    }
    /**
     * Tratamos el "mustUnderstand" de Caja Rural.
     */
    public Set<QName> getHeaders() 
    {
        return Collections.emptySet();
    }
 
    public boolean handleFault(SOAPMessageContext messageContext) 
    {
        return true;
    }
 
    public void close(MessageContext context) 
    {
    }
    /**
     * Prepara el mensaje de salida para poder ser firmado. Elimina el nodo "Signature" que pudiera
     * haber en el nodo "Header" y añade un id al elemento "Body" del mensaje SOAP.
     * @param mensaje {@link SOAPMessage} que sale.
     * @throws SOAPException
     */
    private void preparaMensaje(SOAPMessage mensaje) throws SOAPException
    {
    	Node header;
    	try
    	{
	    	javax.xml.xpath.XPath xpath=javax.xml.xpath.XPathFactory.newInstance().newXPath();
			NodeList nodos = (NodeList) xpath.evaluate("//*[local-name()='Envelope']/*[local-name()='Header']",
					mensaje.getSOAPPart().getDocumentElement(), javax.xml.xpath.XPathConstants.NODESET);
			if (nodos.getLength()==0)
			{
				//Crear nodo Header.
				mensaje.getSOAPPart().getEnvelope().addHeader();
				nodos = (NodeList) xpath.evaluate("//*[local-name()='Envelope']/*[local-name()='Header']",
						mensaje.getSOAPPart().getDocumentElement(), javax.xml.xpath.XPathConstants.NODESET);
			}
			if (nodos.getLength()>0)
			{
				header = nodos.item(0);
				Node signature=(Node)xpath.evaluate("./*[local-name()='Signature']", header,javax.xml.xpath.XPathConstants.NODE);
				if (signature!=null)
				{
					header.removeChild(signature);
				}
				//Hemos quitado el nodo Signature que podría haber, creamos un id para el cuerpo.
				nodos = (NodeList) xpath.evaluate("//*[local-name()='Envelope']/*[local-name()='Body']",
						mensaje.getSOAPPart().getDocumentElement(), javax.xml.xpath.XPathConstants.NODESET);
				if (nodos.getLength()>0)
				{
					Node body=nodos.item(0);
					NamedNodeMap atts = body.getAttributes();
					if (atts.getLength()>0)
					{
						//Si no hay nodo "ID".
						if (atts.getNamedItem("id")==null && atts.getNamedItem("ID")==null)
						{
							((Element)body).setAttribute("id", this.idNodoFirma);
						}
						else if (atts.getNamedItem("id")!=null)
						{
							((Element)body).setAttribute("id", this.idNodoFirma);
						}
						else if (atts.getNamedItem("ID")!=null)
						{
							((Element)body).setAttribute("ID", this.idNodoFirma);
						}
					}
					else
					{
						((Element)body).setAttribute("id", this.idNodoFirma);
					}
				}
		}
    	}
    	catch (XPathExpressionException e)
    	{
    		throw createSOAPFaultException("Error en modificación de mensaje para firma:"+e.getMessage());
    	}
    }
    /**
     * Firma el mensaje que se intercambiará con otro servicio web.
     * @param messageContext
     */
    private void firma(SOAPMessageContext messageContext)
    { 	
    	
        SOAPMessage msg = messageContext.getMessage();
    	ByteArrayInputStream bi=null;
		ByteArrayOutputStream bo=null;
	     try 
	     {
	    	 Boolean salida = (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
	    	 if (salida.booleanValue())
	    	 {
	    		 preparaMensaje(msg);
	    		 bo=new ByteArrayOutputStream();
				 messageContext.getMessage().writeTo(bo);
	    		 String xmlData=new String(bo.toByteArray());
	    		 FirmaHelper fhelp= SeguridadFactory.newFirmaHelper(this.endpointFirma);
	    		 String xmlFirmado= fhelp.firmaXML(xmlData, this.aliasFirma, this.idNodoFirma, this.nodoPadreFirma, this.nsNodoPadreFirma);
	    		 bi = new ByteArrayInputStream(xmlFirmado.getBytes("UTF-8"));
	    		 //Sustituimos la salida por la salida firmada.
				 messageContext.getMessage().getSOAPPart().setContent(new StreamSource(bi));
	    	 }
	    	 else
	    	 {
	    		 //Validamos la entrada firmada
	    		 bo=new ByteArrayOutputStream();
				 messageContext.getMessage().writeTo(bo);
				 String xmlRecibido = new String(bo.toByteArray());
				 FirmaHelper fhelp=SeguridadFactory.newFirmaHelper(this.endpointFirma);
				 if (!fhelp.esValido(xmlRecibido))
				 {
					 generateSOAPErrMessage(msg, "Error en firma","0002","¡¡¡Firma no válida!!!");
				 }
				 else
				 {
					 //Validamos el certificado.
					 if ("S".equals(this.validaCertificado))
					 {
						 if (!comprobarPermisos(xmlRecibido))
						 {
							 generateSOAPErrMessage(msg, "Error en certificado de firma de entidad remota","0002","¡¡¡Certificado no válido!!!");
						 }
					 }
				 }
	    		 
	    	 }
	     } 
	     catch (SeguridadException ex) 
	     {
	    	 generateSOAPErrMessage(msg, "Error en firma","0002","Error en gestión de firma:"+ex.getMessage());
	     } catch (IOException e) {
	    	 generateSOAPErrMessage(msg, "Error en firma","0002","Error en gestión de firma:"+e.getMessage());
		} catch (SOAPException e) {
			generateSOAPErrMessage(msg, "Error en firma","0002","Error en gestión de firma:"+e.getMessage());
		} 
	     finally
	     {
            
	     }
    }
    /**
     * Genera un mensaje de error SOAP.
     * @param msg Mensaje SOAP
     * @param reason Razón del fallo
     * @param codigo Código de error para el detalle.
     * @param mensaje Mensaje para el detalle.
     */
    @SuppressWarnings("unchecked")
	public static void generateSOAPErrMessage(SOAPMessage msg, String reason, String codigo, String mensaje) {
	       try {
	    	  SOAPEnvelope soapEnvelope= msg.getSOAPPart().getEnvelope();
	          SOAPBody soapBody = msg.getSOAPPart().getEnvelope().getBody();
	          SOAPFault soapFault = soapBody.addFault();
	          soapFault.setFaultString(reason);
	          Detail det= soapFault.addDetail();
	          Name name = soapEnvelope.createName("id");
	          det.addDetailEntry(name);
	          
	          name = soapEnvelope.createName("mensaje");
	          det.addDetailEntry(name);
	          DetailEntry entry;
	          Iterator<DetailEntry> it=det.getDetailEntries();
	          while (it.hasNext())
	          {
	        	  entry=it.next();
	        	  if (entry.getLocalName().equals("id"))
	        	  {	
	        		XMLDOMUtils.setNodeText (entry.getOwnerDocument(),entry, codigo);  
	        	  }
	        	  if (entry.getLocalName().equals("mensaje"))
	        	  {	
	        		XMLDOMUtils.setNodeText (entry.getOwnerDocument(),entry, mensaje);  
	        	  }
	        		  
	          }
	          throw new SOAPFaultException(soapFault); 
	       }
	       catch(SOAPException e) { }
	} 
    /**
     * Extrae el certificado.
     * @param mensaje
     * @return
     * @throws XMLDOMDocumentException
     */
    private String extraerCertificado(String mensaje)
			throws XMLDOMDocumentException {
		Document doc = XMLDOMUtils.parseXml(mensaje);
		Node certificado = XMLDOMUtils
				.selectSingleNode(
						doc,
						"/*[local-name()='Envelope']/*[local-name()='Header']/*[local-name()='Signature']/*[local-name()='KeyInfo']/*[local-name()='X509Data']/*[local-name()='X509Certificate']/text()");
		
		if (certificado==null)
		{
			return "";
		}
		return certificado.getNodeValue();
		
		}
    /**
     * Recupera el certificado del mensaje recibido.
     * @param certificado
     * @return
     * @throws SOAPException
     */
		private String getCIFCertificado (String certificado) throws SeguridadException
		{
		String cif="";
		if (certificado==null || "".equals(certificado))
		{
			throw new SeguridadException("El certificado recuperado del mensaje está vacío, no se puede recuperar su CIF.");
		}
		byte[] decodificado = Base64.decode(certificado.toCharArray());
		CertificateFactory cf;
		try {
			cf = CertificateFactory.getInstance("X.509");
			Certificate cert = cf.generateCertificate(new ByteArrayInputStream(decodificado));
		    X509Certificate cert5 = (X509Certificate) cert;
		    X500PrincipalHelper helper= new X500PrincipalHelper(cert5.getSubjectX500Principal());
		    //En CN viene el CIF a tratar
		    cif=helper.getCN();
		    if (cif==null || "".equals(cif))
		    {
		    	throw new SeguridadException ("No se puede recuperar el cif del certificado recibido.");
		    }
		} catch (CertificateException e) {
			throw new SeguridadException ("Error al recuperar el CIF del certificado recibido:"+ e.getMessage());
		}
		return cif;
		}
		
		/**
		 * Comprueba si la identidad del certificado tiene permisos sobre el servicio.
		 * @param idSesion Id de la sesión
		 * @throws XMLDOMDocumentException En caso de no poder extraer el certificado del mensaje SOAP
		 * @throws SeguridadException En caso de no poder comprobar los permisos.
		 */
		private boolean comprobarPermisos(String xml)
				throws  SeguridadException
		{
			// Extraemos el certificado.
			try
			{
				String certificado = extraerCertificado(xml);
				//14/09/2021. Se comprueban permisos con la infraestructura
				//adecuada, no sacando la identidad del certificado
				VerificadorCertificado ver = SeguridadFactory.newVerificadorCertificado(this.endpointAutenticacionEPST, new es.tributasenasturias.webservices.soap.LogMessageHandlerClient(""));
				InfoCertificado infoCert= ver.login(certificado);
				String nif="";
				if (infoCert.getCif()!=null && !"".equals(infoCert.getCif())){
					nif=infoCert.getCif();
				}
				if (infoCert.getNifNie()!=null && !"".equals(infoCert.getNifNie())){
					nif= infoCert.getNifNie();
				}
				VerificadorPermisoServicio per = SeguridadFactory
				.newVerificadorPermisoServicio(new PropertyConfigurator(
							"", this.endpointLanzador, 
							this.paPermisoServicio, this.esquema));
				return per.tienePermisosCIF(nif, this.aliasServicio);
				/*String cif = getCIFCertificado(certificado);
				// Comprobamos permisos sobre el certificado
				VerificadorPermisoServicio per = SeguridadFactory
					.newVerificadorPermisoServicio(new PropertyConfigurator(
								"", this.endpointLanzador, 
								this.paPermisoServicio, this.esquema));
				 return per.tienePermisosCIF(cif, this.aliasServicio);
				 */
			}
			catch (XMLDOMDocumentException e)
			{
				throw new SeguridadException ("Error al comprobar los permisos del certificado recibido:"+ e.getMessage());
			}
		}
}
