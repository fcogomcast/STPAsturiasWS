package es.tributasenasturias.pasarelas.unicaja;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.tributasenasturias.exceptions.PasarelaPagoException;
import es.tributasenasturias.pasarelas.PreferenciasPasarela;

/**
 * Datos de preferencias de Unicaja. Mismos valores que en el fichero de preferencias creado a tal 
 * efecto.
 * @author crubencvs
 *
 */
public final class PreferenciasUnicaja extends PreferenciasPasarela{

	private final String ERROR_PREFERENCIAS = "Error en lectura de preferencias de Unicaja:";
	private String aplicacionDestinoAnulacion;
	private String aplicacionDestinoPeticion;
	private String aplicacionDestinoConsulta;
	private String aplicacionOrigen;
	private String entorno;
	private String entidad;
	private String idioma;
	private String canal;
	private String usuarioAplicacion;
	private String margenTiempoAnulacion;
	private String endpointPago;
	private String endpointConsulta;
	private String endpointAnulacion;
	private String ipOrigen;
	//CRUBENCVS 47535
	private String validaCertificadoResultadoPago;
	private String permisoResultadoPago;
	private String pARecibirResultadoPago;
	private String endpointGeneracionToken;
	private String endpointConsultaTarjeta;
	/**
	 * @return the ipOrigen
	 */
	public String getIpOrigen() {
		return ipOrigen;
	}
	/**
	 * @return the aplicacionDestinoAnulacion
	 */
	public String getAplicacionDestinoAnulacion() {
		return aplicacionDestinoAnulacion;
	}
	/**
	 * @return the aplicacionDestinoPeticion
	 */
	public String getAplicacionDestinoPeticion() {
		return aplicacionDestinoPeticion;
	}
	/**
	 * @return the aplicacionDestinoConsulta
	 */
	public String getAplicacionDestinoConsulta() {
		return aplicacionDestinoConsulta;
	}
	/**
	 * @return the aplicacionOrigen
	 */
	public String getAplicacionOrigen() {
		return aplicacionOrigen;
	}
	/**
	 * @return the entorno
	 */
	public String getEntorno() {
		return entorno;
	}
	/**
	 * @return the entidad
	 */
	public String getEntidad() {
		return entidad;
	}
	/**
	 * @return the idioma
	 */
	public String getIdioma() {
		return idioma;
	}
	/**
	 * @return the canal
	 */
	public String getCanal() {
		return canal;
	}
	/**
	 * @return the usuarioAplicacion
	 */
	public String getUsuarioAplicacion() {
		return usuarioAplicacion;
	}
	/**
	 * @return the margenTiempoAnulacion
	 */
	public String getMargenTiempoAnulacion() {
		return margenTiempoAnulacion;
	}
	/**
	 * @return the endpointPago
	 */
	public String getEndpointPago() {
		return endpointPago;
	}
	/**
	 * @return the endpointConsulta
	 */
	public String getEndpointConsulta() {
		return endpointConsulta;
	}
	
	
	/**
	 * @return the validaCertificadoResultadoPago
	 */
	public final String getValidaCertificadoResultadoPago() {
		return validaCertificadoResultadoPago;
	}
	
	
	/**
	 * @return the permisoResultadoPago
	 */
	public final String getPermisoResultadoPago() {
		return permisoResultadoPago;
	}
	
	
	/**
	 * @return the pARecibirResultadoPago
	 */
	public final String getPARecibirResultadoPago() {
		return pARecibirResultadoPago;
	}
	/**
	 * @param recibirResultadoPago the pARecibirResultadoPago to set
	 */
	public final void setPARecibirResultadoPago(String recibirResultadoPago) {
		pARecibirResultadoPago = recibirResultadoPago;
	}
	
	
	
	
	/**
	 * @return the endpointGeneracionToken
	 */
	public final String getEndpointGeneracionToken() {
		return endpointGeneracionToken;
	}
	/**
	 * @param endpointGeneracionToken the endpointGeneracionToken to set
	 */
	public final void setEndpointGeneracionToken(String endpointGeneracionToken) {
		this.endpointGeneracionToken = endpointGeneracionToken;
	}
	
	
	/**
	 * @return the endpointConsultaTarjeta
	 */
	public final String getEndpointConsultaTarjeta() {
		return endpointConsultaTarjeta;
	}
	/**
	 * @param endpointConsultaTarjeta the endpointConsultaTarjeta to set
	 */
	public final void setEndpointConsultaTarjeta(String endpointConsultaTarjeta) {
		this.endpointConsultaTarjeta = endpointConsultaTarjeta;
	}
	/**
	 * Constructor. Leerá del fichero de preferencias los datos necesarios.
	 * @param ficheroPreferencias Ruta completa de fichero de preferencias.
	 * @throws PasarelaPagoException En caso de haber algún problema en la lectura del fichero.
	 */
	public PreferenciasUnicaja(String ficheroPreferencias) throws PasarelaPagoException
	{
		super ();
		try
		{
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new java.io.File (ficheroPreferencias));
			javax.xml.xpath.XPath xpath=javax.xml.xpath.XPathFactory.newInstance().newXPath();
			NodeList nodos = (NodeList) xpath.evaluate("/preferencias",
					doc, javax.xml.xpath.XPathConstants.NODESET); //Sólo debería haber uno, y sólo haremos caso a uno
			if (nodos.getLength()>0)
			{
				this.aplicacionDestinoAnulacion = xpath.evaluate("entry[@key='AplicacionDestinoAnulacion']/@value", nodos.item(0));
				this.aplicacionDestinoPeticion = xpath.evaluate("entry[@key='AplicacionDestinoPeticion']/@value", nodos.item(0));
				this.aplicacionDestinoConsulta = xpath.evaluate("entry[@key='AplicacionDestinoConsulta']/@value", nodos.item(0));
				this.entorno = xpath.evaluate("entry[@key='Entorno']/@value", nodos.item(0));
				this.entidad = xpath.evaluate("entry[@key='Entidad']/@value", nodos.item(0));
				this.aplicacionOrigen = xpath.evaluate("entry[@key='AplicacionOrigen']/@value", nodos.item(0));
				this.idioma = xpath.evaluate("entry[@key='Idioma']/@value", nodos.item(0));
				this.canal = xpath.evaluate("entry[@key='Canal']/@value", nodos.item(0));
				this.usuarioAplicacion = xpath.evaluate("entry[@key='UsuarioAplicacion']/@value", nodos.item(0));
				this.margenTiempoAnulacion = xpath.evaluate("entry[@key='MargenTiempoAnulacion']/@value", nodos.item(0));
				this.endpointPago = xpath.evaluate("entry[@key='EndPointPago']/@value", nodos.item(0));
				this.endpointAnulacion = this.endpointPago;
				this.endpointConsulta = xpath.evaluate("entry[@key='EndPointConsulta']/@value", nodos.item(0));
				this.ipOrigen = xpath.evaluate("entry[@key='IpOrigen']/@value", nodos.item(0));
				//CRUBENCVS 47535 05/04/2023
				this.validaCertificadoResultadoPago= xpath.evaluate("entry[@key='ValidaCertificadoResultadoPago']/@value", nodos.item(0));
				this.permisoResultadoPago= xpath.evaluate("entry[@key='PermisoResultadoPago']/@value", nodos.item(0));
				this.pARecibirResultadoPago = xpath.evaluate("entry[@key='PARecepcionResultadoPagoTarjeta']/@value", nodos.item(0)); 
				this.endpointGeneracionToken = xpath.evaluate("entry[@key='EndpointGeneracionToken']/@value", nodos.item(0));
				this.endpointConsultaTarjeta = xpath.evaluate("entry[@key='EndpointConsultaTarjeta']/@value", nodos.item(0));
				//FIN CRUBENCVS 47535
			}
		}
		catch (ParserConfigurationException e)
		{
			throw new PasarelaPagoException (ERROR_PREFERENCIAS+e.getMessage(),e);
		}
		catch (SAXException e)
		{
			throw new PasarelaPagoException (ERROR_PREFERENCIAS+e.getMessage(),e);
		}
		catch (IOException e)
		{
			throw new PasarelaPagoException (ERROR_PREFERENCIAS+e.getMessage(),e);
		}
		catch (XPathExpressionException e)
		{
			throw new PasarelaPagoException (ERROR_PREFERENCIAS+e.getMessage(),e);
		}
		
	}
	/**
	 * @return the endpointAnulacion
	 */
	public String getEndpointAnulacion() {
		return endpointAnulacion;
	}
	
	
}
