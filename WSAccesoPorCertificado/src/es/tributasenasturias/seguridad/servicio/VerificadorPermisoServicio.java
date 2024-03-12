package es.tributasenasturias.seguridad.servicio;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


/**
 * Validador de certificado de cabecera
 * 
 * 
 * 
 */
public class VerificadorPermisoServicio{
	
	PropertyConfigurator configuracion;
	protected VerificadorPermisoServicio(PropertyConfigurator prop) {
		configuracion=prop;
	}
	/** @deprecated **/
	public String getCertificadoCabecera(String datosXML) {
		String certificado = "";
		// Recuperamos la lista de nodos de la cabecera con certificado. S�lo
		// deber�a haber uno.
		try {
			javax.xml.parsers.DocumentBuilder db;
			javax.xml.parsers.DocumentBuilderFactory fact = javax.xml.parsers.DocumentBuilderFactory
					.newInstance();

			db = fact.newDocumentBuilder();
			fact.setNamespaceAware(true);
			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource();
			inStr.setCharacterStream(new java.io.StringReader(datosXML));
			org.w3c.dom.Document m_resultadoXML;
			m_resultadoXML = db.parse(inStr);
			//org.w3c.dom.NodeList certificados = m_resultadoXML
			//		.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "X509Certificate");
			org.w3c.dom.NodeList certificados = m_resultadoXML
					.getElementsByTagName("dsig:X509Certificate");
			if (certificados != null && certificados.getLength() != 0) {
				certificado = certificados.item(0).getTextContent();

			} else {
				certificado = null;
			}

		} catch (ParserConfigurationException ex) {
			certificado = null;
		}
		catch (SAXException ex) {
			certificado = null;
		} catch (IOException ex) {
			certificado = null;
		}

		return certificado;
	}
	/**
	 * Comprueba si el certificado es v�lido y tiene permisos para ejecutar el servicio.
	 * @param certificado Texto del certificado en Base64 que se usar� para comprobar permisos
	 * @param aliasServicio Alias del servicio que se comprobar� en base de datos.
	 * @return
	 */
	public InfoPermisosCertificado tienePermisosCertificado(String certificado,String aliasServicio) throws SeguridadException{		
		InfoPermisosCertificado info = SeguridadFactory.newInfoPermisosCertificado();

		PermisoEjecucion bd = null;
		DatosPermisosServicio  autorizacion = null;
		
		if (certificado != null && !certificado.equals("")) {
			// Lo enviamos al servicio del Principado, para que nos indique si
			// el certificado es v�lido.
			VerificadorCertificado aut;
			if (configuracion.getSoapHandler()!=null)
			{
				aut = SeguridadFactory.newVerificadorCertificado(configuracion.getEndpointServicioAutenticacion(),configuracion.getSoapHandler());
			}
			else
			{
				aut = SeguridadFactory.newVerificadorCertificado(configuracion.getEndpointServicioAutenticacion());
			}
			// Llamamos al servicio de autenticaci�n del Principado
			InfoCertificado inCert = aut.login(certificado);
			info.setInfoCertificado(inCert);
			String strCIFNIF=null;
			//Se comprueban siempre permisos contra el NIF, porque en este servicio
			//el NIF/NIE tiene prioridad sobre el CIF.
			/*if (inCert.getCif()!=null && !"".equals(inCert.getCif()))
			{
				strCIFNIF = inCert.getCif();
			}*/
			if (inCert.getNifNie()!=null && !"".equals(inCert.getNifNie()))
			{
				strCIFNIF = inCert.getNifNie();
			}
			else
			{
				throw new SeguridadException("No se encuentra el NIF/CIF asociado al certificado.");
			}
			if (strCIFNIF != null && 
					InfoCertificado.Validez.VALIDO.equals(inCert.getValidez()))
			{
				info.setCertificadoValido(true);
				// Validamos el Id que nos han pasado.
				bd =SeguridadFactory.newPermisoEjecucion(configuracion.getEndpointLanzadorBD(), 
														 configuracion.getEsquemaBD(), configuracion.getProcBDPermisoServicio());
				if (configuracion.getSoapHandler()!=null)
				{
					bd.setManejadorMensajes(configuracion.getSoapHandler());
				}
				autorizacion = bd.permisoServicio(aliasServicio,strCIFNIF);
				if (DatosPermisosServicio.AutorizacionServicio.NO_AUTORIZADO.equals( autorizacion.getAutorizacion())) {
					info.setCertificadoAutorizado(false);
				} else if (DatosPermisosServicio.AutorizacionServicio.ERROR.equals(autorizacion.getAutorizacion())) {
					info.setCertificadoAutorizado(false);
				} else {
					info.setCertificadoAutorizado(true);
					info.setListaUsuarios(autorizacion.getListaUsuarios());
				}
			}
			else { //No haya nif, o sea inv�lido (deber�an ser equivalentes) se trata de error.					
					info.setCertificadoValido(false);
			}
		} else {
			info.setCertificadoValido(false);
		}

		return info;
	}
	
	/**
	 * Comprueba si el CIF o NIF pasado por par�metro tiene permisos para ejecutar el servicio.
	 * @param CIFNIF CIF o NIF a comprobar
	 * @param aliasServicio Alias del servicio que se comprobar� en base de datos.
	 * @return
	 */
	public boolean tienePermisosCIF(String CIFNIF,String aliasServicio) throws SeguridadException{		
		boolean valido = false;

		PermisoEjecucion bd = null;
		DatosPermisosServicio autorizacion = null;
		if (CIFNIF!=null && !CIFNIF.equals(""))
		{
			// Validamos el Id que nos han pasado.
			bd =SeguridadFactory.newPermisoEjecucion(configuracion.getEndpointLanzadorBD(), 
													 configuracion.getEsquemaBD(), configuracion.getProcBDPermisoServicio());
			if (configuracion.getSoapHandler()!=null)
			{
				bd.setManejadorMensajes(configuracion.getSoapHandler());
			}
			autorizacion = bd.permisoServicio(aliasServicio,CIFNIF);
			if (DatosPermisosServicio.AutorizacionServicio.NO_AUTORIZADO.equals( autorizacion.getAutorizacion())) {
				valido=false;
			} else if (DatosPermisosServicio.AutorizacionServicio.ERROR.equals(autorizacion.getAutorizacion())) {
				valido=false;
			} else {
				valido = true;
			}
		}
		else { //No haya nif, o sea inv�lido (deber�an ser equivalentes) se trata de error.					
				valido=false;
		}

		return valido;
	}
}
