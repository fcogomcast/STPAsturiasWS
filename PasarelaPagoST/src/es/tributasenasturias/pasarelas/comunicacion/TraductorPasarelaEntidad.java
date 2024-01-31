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
 * Realiza la traducción entre identificadores de pasarela y códigos de entidad.
 * @author crubencvs
 *
 */
public class TraductorPasarelaEntidad {

	Document doc=null;
	
	/**
	 * Inicializa la traducción en función del fichero de pasarelas indicado.
	 * @param ficheroPasarelas
	 * @throws PasarelaPagoException
	 */
	public TraductorPasarelaEntidad(String ficheroPasarelas) throws PasarelaPagoException
	{
		try
		{
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new java.io.File (ficheroPasarelas));
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
	 * Recupera la pasarela asociada a la entidad indicada.
	 * @param entidad Identificador de entidad
	 * @return Identificador de Pasarela
	 * @throws PasarelaPagoException
	 */
	public String getPasarelaFromEntidad (String entidad) throws PasarelaPagoException
	{
		NodeList nodos;
		String pasarela;
		try
		{
			javax.xml.xpath.XPath xpath=javax.xml.xpath.XPathFactory.newInstance().newXPath();
			nodos = (NodeList) xpath.evaluate("pasarelas/cuentas/cuenta[./entidad='"+ entidad+"']",
					doc, javax.xml.xpath.XPathConstants.NODESET); 
			if (nodos.getLength()>0) //Se ha encontrado la entidad
			{
				pasarela = xpath.evaluate("pasarela/text()", nodos.item(0));
			}
			else
			{
				throw new PasarelaPagoException ("No se ha encontrado correspondencia de pasarela con la entidad:"+entidad);
			}
			return pasarela;
		}
		catch (XPathExpressionException e)
		{
			throw new PasarelaPagoException ("No se ha podido recuperar la pasarela para la entidad:"+ entidad+" por un error:"+e.getMessage());
		}
	}
	/**
	 * Recupera la entidad asociada a una pasarela.
	 * @param pasarela Identificador de pasarela para el que devolver la entidad asociada.
	 * @return Identificador de entidad
	 * @throws PasarelaPagoException
	 */
	public String getEntidadFromPasarela (String pasarela) throws PasarelaPagoException
	{
		NodeList nodos;
		String entidad;
		try
		{
			javax.xml.xpath.XPath xpath=javax.xml.xpath.XPathFactory.newInstance().newXPath();
			nodos = (NodeList) xpath.evaluate("pasarelas/cuentas/cuenta[./pasarela='"+ pasarela+"']",
					doc, javax.xml.xpath.XPathConstants.NODESET); 
			if (nodos.getLength()>0) //Se ha encontrado la entidad
			{
				entidad = xpath.evaluate("entidad/text()", nodos.item(0));
			}
			else
			{
				//Podría ser el de la entidad, y no el nombre
				xpath=javax.xml.xpath.XPathFactory.newInstance().newXPath();
				nodos = (NodeList) xpath.evaluate("pasarelas/cuentas/cuenta[./entidad='"+ pasarela+"']",
						doc, javax.xml.xpath.XPathConstants.NODESET); 
				if (nodos.getLength()>0) //Se ha encontrado la entidad
				{
					entidad= pasarela;
				}else {
					throw new PasarelaPagoException ("No se ha encontrado correspondencia de entidad con la pasarela:"+pasarela);
				}
			}
			return entidad;
		}
		catch (XPathExpressionException e)
		{
			throw new PasarelaPagoException ("No se ha podido recuperar la entidad para la pasarela:"+ pasarela+" por un error:"+e.getMessage());
		}
	}
}
