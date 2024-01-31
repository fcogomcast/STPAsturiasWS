package es.tributasenasturias.pasarelas.cajarural;

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
 * Datos de preferencias de Caja Rural. Mismos valores que en el fichero de preferencias creado a tal 
 * efecto.
 * @author crubencvs
 *
 */
public final class PreferenciasCajaRural extends PreferenciasPasarela{

	private static final String ERROR_PREFERENCIAS = "Error en lectura de preferencias de Caja Rural:";
	private String entidad;
	private String margenTiempoAnulacion;
	private String endpointPago;
	private String endpointConsulta;
	private String endpointAnulacion;
	private String ipOrigen;
	private String validarFirma;
	private String idNodoFirmar;
	private String nodoContenedorFirma;
	private String nsNodoContenedorFirma;
	private String validaCertificado;
	private String aliasServicio;
	
	/**
	 * @return the aliasServicio
	 */
	public final String getAliasServicio() {
		return aliasServicio;
	}

	/**
	 * @return the validarFirma
	 */
	public final String getValidarFirma() {
		return validarFirma;
	}

	/**
	 * @return the idNodoFirmar
	 */
	public final String getIdNodoFirmar() {
		return idNodoFirmar;
	}

	/**
	 * @return the nodoContenedorFirma
	 */
	public final String getNodoContenedorFirma() {
		return nodoContenedorFirma;
	}

	/**
	 * @return the nsNodoContenedorFirma
	 */
	public final String getNsNodoContenedorFirma() {
		return nsNodoContenedorFirma;
	}

	/**
	 * @return the validaCertificado
	 */
	public final String getValidaCertificado() {
		return validaCertificado;
	}

	/**
	 * @return the ipOrigen
	 */
	public String getIpOrigen() {
		return ipOrigen;
	}
	
	/**
	 * @return the entidad
	 */
	public String getEntidad() {
		return entidad;
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
	 * Constructor. Leerá del fichero de preferencias los datos necesarios.
	 * @param ficheroPreferencias Ruta completa de fichero de preferencias.
	 * @throws PasarelaPagoException En caso de haber algún problema en la lectura del fichero.
	 */
	public PreferenciasCajaRural(String ficheroPreferencias) throws PasarelaPagoException
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
				this.entidad = xpath.evaluate("entry[@key='Entidad']/@value", nodos.item(0));
				this.margenTiempoAnulacion = xpath.evaluate("entry[@key='MargenTiempoAnulacion']/@value", nodos.item(0));
				this.endpointPago = xpath.evaluate("entry[@key='EndPointPago']/@value", nodos.item(0));
				this.endpointAnulacion = xpath.evaluate("entry[@key='EndPointAnulacion']/@value", nodos.item(0));
				this.endpointConsulta = xpath.evaluate("entry[@key='EndPointConsulta']/@value", nodos.item(0));
				this.ipOrigen = xpath.evaluate("entry[@key='IpOrigen']/@value", nodos.item(0));
				this.validarFirma = xpath.evaluate("entry[@key='ValidaFirma']/@value", nodos.item(0));
				this.idNodoFirmar = xpath.evaluate("entry[@key='IDNodoFirmar']/@value", nodos.item(0));
				this.nodoContenedorFirma = xpath.evaluate("entry[@key='NodoContenedorFirma']/@value", nodos.item(0));
				this.nsNodoContenedorFirma = xpath.evaluate("entry[@key='NamespaceNodoContenedorFirma']/@value", nodos.item(0));
				this.validaCertificado = xpath.evaluate("entry[@key='ValidaCertificado']/@value", nodos.item(0));
				this.aliasServicio = xpath.evaluate("entry[@key='AliasServicio']/@value", nodos.item(0));
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
