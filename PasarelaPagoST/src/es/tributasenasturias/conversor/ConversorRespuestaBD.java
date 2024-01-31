/**
 * 
 */
package es.tributasenasturias.conversor;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;

/**
 * @author crubencvs
 * Convierte el mensaje de respuesta de BD a un mensaje de servicio.
 */
public class ConversorRespuestaBD {
	Document doc;
	/**
	 * Constructor
	 */
	public ConversorRespuestaBD()
	{
		//Cargamos el fichero de mapeo de errores.
		try
		{
		Preferencias pr = Preferencias.getPreferencias();
		java.io.File consFile; //Fichero donde se encuentra el mapeo de errores de BD a nuestros errores.
		consFile = new java.io.File (pr.getFicheroMapeoBD());
		doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(consFile);
		}
		catch (Exception ex)
		{
			//No se habrá podido cargar el fichero. Logeamos y al no existir el fichero, no habrá documento cargado.
			Logger logger= new Logger();
			logger.error("No se ha podido cargar o parsear el fichero de mapeo de mensajes con BD, error:" + ex.getMessage());
		}
		//conversor.put(key, value)
	}
	/**
	 * Devuelve una lista de mensajes equivalentes al que devuelve la base de datos, según 
	 * el fichero de mapas de mensajes que indican la equivalencia entre base de datos y servicio.
	 * @param codMensajeBD Código de mensaje recibido de base de datos.
	 * @return
	 */
	private List<MensajesBD> getErrorBD (String codMensajeBD)
	{
		List<MensajesBD> mapa = null;
		if (doc!=null)
		{
			try
			{
				javax.xml.xpath.XPath xpath=javax.xml.xpath.XPathFactory.newInstance().newXPath();
				NodeList nodos = (NodeList) xpath.evaluate("/mapa_mensajes/mapa_mensaje[remoto='"+codMensajeBD.trim()+"']",
						doc, javax.xml.xpath.XPathConstants.NODESET); //Sólo debería haber uno, y sólo haremos caso a uno
				if (nodos.getLength()>0) //Hemos encontrado el nodo de nuestro mapeo.
				{
					mapa=new ArrayList<MensajesBD>();
					//Recuperamos los nodos de código y texto a mostrar.
					String codigo = xpath.evaluate("epst/text()", nodos.item(0));
					String desc = xpath.evaluate("epst_texto/text()", nodos.item(0));
					MensajesBD men = new MensajesBD();
					men.setCodigoBD(codMensajeBD);
					men.setCodigoServicio(codigo);
					men.setTextoServicio(desc);
					mapa.add(men);
				}
			}
			catch (javax.xml.xpath.XPathExpressionException ex)
			{
				Logger logger= new Logger();
				logger.error("No se ha podido recorrer el fichero de mapeo de mensajes.Se utiliza el código de error por defecto; error:" + ex.getMessage());
			}
		}
		return mapa;
	}
	/**
	 * Recupera el mensaje de resultado de la base de datos
	 * @param codMensajeBD Código de mensaje recibido de base de datos.
	 * @return
	 */
	public MensajesBD getResultadoBD (String codMensajeBD)
	{
		MensajesBD mensaje=null;
		if (codMensajeBD!=null)
		{
			List<MensajesBD> mapa = getErrorBD(codMensajeBD);
			if (mapa!=null && mapa.size()>0)
			{
				mensaje=mapa.get(0);
			}
		}
		return mensaje;
	}
}
