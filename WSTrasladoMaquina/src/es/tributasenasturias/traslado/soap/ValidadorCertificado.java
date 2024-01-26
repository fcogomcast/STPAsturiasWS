package es.tributasenasturias.traslado.soap;


import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;


import org.w3c.dom.Node;

import es.tributasenasturias.log.TributasLogger;
import es.tributasenasturias.seguridad.AutenticacionCertificado;
import es.tributasenasturias.seguridad.PermisosCertificado;
import es.tributasenasturias.seguridad.SeguridadException;
import es.tributasenasturias.seguridad.AutenticacionCertificado.ResultadoAutenticacion;
import es.tributasenasturias.traslado.preferencias.Preferencias;
import es.tributasenasturias.traslado.preferencias.PreferenciasException;
import es.tributasenasturias.traslado.util.Constantes;
import es.tributasenasturias.traslado.util.Utils;
import es.tributasenasturias.traslado.util.XMLDOMDocumentException;
import es.tributasenasturias.traslado.util.XMLDOMUtils;


public class ValidadorCertificado implements ISOAPHandler{

	/**
	 * Extrae el contenido del certificado de la firma de WSSecurity
	 * @param mensaje Mensaje firmado.
	 * @return Cadena que contiene el certificado de la firma.
	 * @throws XMLDOMDocumentException
	 */
	private String extraerCertificado(Node mensaje)
			throws XMLDOMDocumentException {
		Node certificado = XMLDOMUtils
				.selectSingleNode(
						mensaje,
						"/*[local-name()='Envelope']/*[local-name()='Header']/*[local-name()='Signature']//*[local-name()='X509Data']/*[local-name()='X509Certificate']/text()");
		if (certificado==null)
		{
			return "";
		}
		return certificado.getNodeValue();

	}
	/**
	 * Valida el certificado incluido en el mensaje de entrada, si así se indica en parámetros.
	 * @param context
	 */
	@Override
	public void process(SOAPMessageContext context)
	{
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		TributasLogger logAplicacion=null;
		if (!salida.booleanValue())
		{
			Preferencias pref = (Preferencias) context.get(Constantes.PREFERENCIAS);
			try
			{
				//Recuperamos si es necesario validar el certificado.
				if (pref==null)
				{
					pref=new Preferencias();
				}
				String idSesion = (String) context.get(Constantes.IDSESION);
				if (idSesion==null)
				{
					idSesion = "Sin sesión";
				}
				//Generamos el log de aplicación, escribirá a un fichero diferente al anterior, que es el de mensajes SOAP.
				if (logAplicacion==null)
				{
					logAplicacion= new TributasLogger(pref.getModoLog(), pref.getFicheroLogAplicacion(), idSesion);
				}
				if ("S".equalsIgnoreCase(pref.getValidarCertificado()))
				{
					String certificado="";
					certificado = extraerCertificado(context.getMessage().getSOAPPart());
					if ("".equals(certificado))
					{
						logAplicacion.error ("Manejador SOAP ["+ ValidadorCertificado.class.getName() + "] Certificado vacío o no se encuentra en el nodo adecuado.");
						Utils.generateSOAPErrMessage(context.getMessage(), "Error en seguridad de mensaje.", "0002", "SOAP Handler", false);
					}
					logAplicacion.info("Manejador SOAP ["+ValidadorCertificado.class.getName()+"]. Se valida el certificado.");
					//Lanzamos la validación.
					AutenticacionCertificado ac = new AutenticacionCertificado(pref.getEndpointAutenticacion(), new SoapClientHandler(idSesion));
					ResultadoAutenticacion r = ac.comprobarCertificado(certificado);
					if (!r.esValido()){
						
					}
					if ("S".equals(pref.getValidarPermisos()))
						{
							logAplicacion.debug("Manejador SOAP ["+ValidadorCertificado.class.getName()+"]. Se valida que la identidad del certificado tiene permiso sobre el servicio.");
							String idSolicitante = r.getCif()!=null?r.getCif():r.getNifnie();
							logAplicacion.debug ("Manejador SOAP [" + ValidadorCertificado.class.getName()+"].Extraemos CIF/NIF del certificado:"+certificado);
							logAplicacion.debug ("Manejador SOAP [" + ValidadorCertificado.class.getName()+"].Verificamos permisos del servicio para cif/nif:"+idSolicitante);
							PermisosCertificado pc = new PermisosCertificado(pref.getEndpointLanzador(), pref.getEsquemaBD(), pref.getProcAlmacenadoPermisosServicio(), new SoapClientHandler(idSesion));
							if (!pc.verificarPermisos(pref.getAliasServicio(), idSolicitante))
							{
								
								logAplicacion.error ("Manejador SOAP [" + ValidadorCertificado.class.getName()+"].El id:" + idSolicitante + " no tiene permisos para ejecutar el servicio " + pref.getAliasServicio());
								Utils.generateSOAPErrMessage(context.getMessage(), "Error en seguridad de mensaje.", "0002", "SOAP Handler", false);
							}
							logAplicacion.debug ("Manejador SOAP [" + ValidadorCertificado.class.getName()+"].El id:" + idSolicitante + " tiene permisos para ejecutar el servicio " + pref.getAliasServicio());
						}
					//Es válido.
				}
			}catch (PreferenciasException e)
			{
				//Redundante. Si preferencias no existe, no puede haberse dado de alta el log. Lo dejo por si
				//un día se pone un valor por defecto para las preferencias de log.
				if (logAplicacion!=null) {logAplicacion.error("Manejador SOAP " + ValidadorCertificado.class.getName()+".Error en preferencias:"+e.getMessage());}
				else {System.err.println("Servicio Traslado Máquina: Error en preferencias : "+ e.getMessage());};
				Utils.generateSOAPErrMessage(context.getMessage(), "Error en seguridad de mensaje.", "0002", "SOAP Handler", true);
			} catch (SeguridadException e)
			{
				if (logAplicacion!=null) {logAplicacion.error("Manejador SOAP [" + ValidadorCertificado.class.getName()+"].Error:"+e.getMessage());};
				Utils.generateSOAPErrMessage(context.getMessage(), "Error en seguridad de mensaje.", "0002", "SOAP Handler", true);
			}
			 catch (XMLDOMDocumentException e)
			 {
				 if (logAplicacion!=null) {logAplicacion.error("Manejador SOAP [" + ValidadorCertificado.class.getName()+"].Error:"+e.getMessage());};
					Utils.generateSOAPErrMessage(context.getMessage(), "Error en seguridad de mensaje.", "0002", "SOAP Handler", true);
			 }
		}
	}

}
