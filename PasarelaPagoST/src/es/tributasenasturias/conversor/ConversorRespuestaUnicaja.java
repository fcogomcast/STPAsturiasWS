/**
 * 
 */
package es.tributasenasturias.conversor;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.utils.Logger;
import es.tributasenasturias.utils.Preferencias;

/**
 * @author crubencvs
 * Convierte el mensaje de respuesta de Unicaja a un mensaje nuestro.
 */
public class ConversorRespuestaUnicaja {
	Document doc;
	/**
	 * Constructor
	 */
	public ConversorRespuestaUnicaja()
	{
		//Cargamos el fichero de mapeo de errores.
		try
		{
		Preferencias pr = Preferencias.getPreferencias();
		java.io.File consFile; //Fichero donde se encuentra el mapeo de errores de Unicaja a nuestros errores.
		consFile = new java.io.File (pr.getFicheroMapeoUnicaja());
		doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(consFile);
		}
		catch (Exception ex)
		{
			//No se habrá podido cargar el fichero. Logeamos y al no existir el fichero, no habrá documento cargado.
			Logger logger= new Logger();
			logger.error("No se ha podido cargar o parsear el fichero de mapeo de mensajes con Unicaja, error:" + ex.getMessage());
		}
		//conversor.put(key, value)
	}
	/**
	 * Devuelve el mensaje equivalente al de entrada de Unicaja. Si no hay uno particular, devuelve el error genérico.
	 * @param respuestaUnicaja
	 * @return
	 */
	public List<MensajesUnicaja> getErrorUnicaja (String errorUnicaja)
	{
		List<MensajesUnicaja> mapa = null;
		if (doc!=null)
		{
			try
			{
				javax.xml.xpath.XPath xpath=javax.xml.xpath.XPathFactory.newInstance().newXPath();
				NodeList nodos = (NodeList) xpath.evaluate("/mapa_mensajes/mapa_mensaje[remoto='"+errorUnicaja.trim()+"']",
						doc, javax.xml.xpath.XPathConstants.NODESET); //Sólo debería haber uno, y sólo haremos caso a uno
				if (nodos.getLength()>0) //Hemos encontrado el nodo de nuestro mapeo.
				{
					mapa=new ArrayList<MensajesUnicaja>();
					//Recuperamos los nodos de código y texto a mostrar.
					String codigo = xpath.evaluate("epst/text()", nodos.item(0));
					String desc = xpath.evaluate("epst_texto/text()", nodos.item(0));
					MensajesUnicaja men = new MensajesUnicaja();
					men.setCodigoUnicaja(errorUnicaja);
					men.setCodigoEpst(codigo);
					men.setTextoEpst(desc);
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
	 * Recupera el resultado de unicaja, recortándolo si fuera demasiado grande para nuestros sistemas,
	 * ya que posterioremente este resultado se almacenará en nuestra base de datos.
	 * @param resultadoUnicaja
	 * @return
	 */
	public String getResultadoUnicaja (String resultadoUnicaja)
	{
		String res=null;
		if (resultadoUnicaja!=null && resultadoUnicaja.length()>=Constantes.getLongColumnaRespuesta())
		{
			int idxEspacio = resultadoUnicaja.indexOf(" ");
			if (idxEspacio!=-1)
			{
				res=resultadoUnicaja.substring(0,idxEspacio); // Código de resultado en Unicaja.
				if (res.length()>Constantes.getLongColumnaRespuesta())
				{
					res = res.substring(0,Constantes.getLongColumnaRespuesta());
				}
			}
			else
			{
				if (resultadoUnicaja.length()>Constantes.getLongColumnaRespuesta())
				{
					res=resultadoUnicaja.substring(0,Constantes.getLongColumnaRespuesta()); // Código de resultado en Unicaja.
				}
				else
				{
					res=resultadoUnicaja;
				}
			}
		}
		else if (resultadoUnicaja!=null) //Existe, pero tiene menos del ancho de la columna, introducimos todo
		{
			res=resultadoUnicaja;
		}
		if (res!=null){
			return res.trim();
		} else {
			return null;
		}
	}
}
