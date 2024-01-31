package es.tributasenasturias.pasarelas.comunicacion;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.tributasenasturias.exceptions.PasarelaPagoException;

/**
 * Selecciona la pasarela de pago a utilizar en función del medio de pago utilizado.
 * @author crubencvs
 *
 */
public final class SelectorPasarelaPorMedioPago implements ISelectorPasarela {

	String pasarelaPorDefecto;
	String tarjeta;
	String cuenta;
	Document doc=null;
	/**Constructor
	 * 
	 * @param datosProceso Datos de petición a la pasarela.
	 */
	public SelectorPasarelaPorMedioPago (String tarjeta, String cuenta, String ficheroSeleccionPasarelas, String pasarelaPorDefecto) throws PasarelaPagoException
	{
		this.pasarelaPorDefecto= pasarelaPorDefecto;
		this.tarjeta=tarjeta;
		this.cuenta=cuenta;
		try
		{
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new java.io.File (ficheroSeleccionPasarelas));
		}
		catch (SAXException e)
		{
			throw new PasarelaPagoException ("Error seleccionando la pasarela a utilizar:"+e.getMessage(),e);
		}
		catch (IOException e)
		{
			throw new PasarelaPagoException ("Error seleccionando la pasarela a utilizar:"+e.getMessage(),e);
		}
		catch (ParserConfigurationException e)
		{
			throw new PasarelaPagoException ("Error seleccionando la pasarela a utilizar:"+e.getMessage(),e);
		}
	}
	/**
	 * Recupera la pasarela que corresponde.
	 */
	public final String getPasarela() throws PasarelaPagoException
	{
		String pasarela="";
		try
		{
			if (!"".equals(this.tarjeta)) //Buscamos entre las tarjetas.
			{
				pasarela = buscarPasarelaPorTarjeta(this.tarjeta.substring(0,6));
			}
			else if (!"".equals(this.cuenta))
			{
				pasarela = buscarPasarelaPorCuenta(this.cuenta.substring(0,4));
			}
			else //Por defecto y hasta nueva orden, cajastur, ya que si no las consultas antiguas no funcionarían.
			{
				if (this.pasarelaPorDefecto!=null && !"".equals(this.pasarelaPorDefecto))
				{
					pasarela=this.pasarelaPorDefecto;
				}
				else
				{
					throw new PasarelaPagoException ("No se puede saber qué pasarela utilizar, ya que no hay datos de tarjeta ni de cuenta.");
				}
			}
		}
		catch (XPathExpressionException e)
		{
			throw new PasarelaPagoException ("Error al seleccionar la pasarela a utilizar:"+e.getMessage(),e);
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new PasarelaPagoException ("Error al seleccionar la pasarela a utilizar. El formato de tarjeta/cuenta podría ser incorrecto:"+e.getMessage(),e);
		}
		return pasarela;
	}
	/**
	 * Busca la pasarela que dará servicio según el bin de la tarjeta.
	 * @param bin BIN de la tarjeta, primeros 6 dígitos.
	 * @return Identificador de la pasarela que dará servicio a la petición.
	 * @throws XPathExpressionException
	 * @throws PasarelaPagoException
	 */
	private String buscarPasarelaPorTarjeta(String bin) throws XPathExpressionException,
			PasarelaPagoException {
		NodeList nodos;
		String pasarela;
		javax.xml.xpath.XPath xpath=javax.xml.xpath.XPathFactory.newInstance().newXPath();
		nodos = (NodeList) xpath.evaluate("/pasarelas/tarjetas/tarjeta[./bin='"+ bin+"']",
				doc, javax.xml.xpath.XPathConstants.NODESET); 
		if (nodos.getLength()>0) //Se ha encontrado el BIN, recuperamos el código de pasarela.
		{
			pasarela = xpath.evaluate("pasarela/text()", nodos.item(0));
		}
		else //Recuperamos la tarjeta "default"
		{
			nodos = (NodeList) xpath.evaluate("/pasarelas/tarjetas/tarjeta[@id='default']",
					doc, javax.xml.xpath.XPathConstants.NODESET);
			if (nodos.getLength()>0)
			{
				pasarela = xpath.evaluate("pasarela/text()", nodos.item(0));
			}
			else
			{
				throw new PasarelaPagoException ("No se ha encontrado correspondencia de pasarela con la tarjeta introducida.");
			}
		}
		return pasarela;
	}
	/**
	 * Busca en el fichero una cuenta que corresponda a la entidad que se pasa por parámetro.
	 * @param entidad Código de entidad (primeros cuatro dígitos del Código cuenta cliente)
	 * @return Código de pasarela que dará servicio a esa cuenta.
	 * @throws XPathExpressionException
	 * @throws PasarelaPagoException
	 */
	private String buscarPasarelaPorCuenta(String entidad)
			throws XPathExpressionException, PasarelaPagoException {
		String pasarela;
		javax.xml.xpath.XPath xpath=javax.xml.xpath.XPathFactory.newInstance().newXPath();
		NodeList nodos;
		nodos = (NodeList) xpath.evaluate("/pasarelas/cuentas/cuenta[./entidad='"+ entidad+"']",
				doc, javax.xml.xpath.XPathConstants.NODESET);
		if (nodos.getLength()>0)
		{
			pasarela= xpath.evaluate("pasarela/text()", nodos.item(0));
		}
		else //Cuenta por defecto.
		{
			nodos = (NodeList) xpath.evaluate("/pasarelas/cuentas/cuenta[@id='default']",
					doc, javax.xml.xpath.XPathConstants.NODESET);
			if (nodos.getLength()>0)
			{
				pasarela=xpath.evaluate("pasarela/text()", nodos.item(0));
			}
			else
			{
				throw new PasarelaPagoException ("No se ha encontrado correspondencia de pasarela con la cuenta introducida.");
			}
		}
		return pasarela;
	}
}
