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
 * Convierte el mensaje de respuesta de BBVA a un mensaje nuestro.
 */
public class ConversorRespuestaBBVA {
	Document doc;
	/**
	 * Constructor
	 */
	public ConversorRespuestaBBVA()
	{
		//Cargamos el fichero de mapeo de errores.
		try
		{
		Preferencias pr = Preferencias.getPreferencias();
		java.io.File consFile; //Fichero donde se encuentra el mapeo de errores de bbva a nuestros errores.
		consFile = new java.io.File (pr.getFicheroMapeoBBVA());
		doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(consFile);
		}
		catch (Exception ex)
		{
			//No se habrá podido cargar el fichero. Logeamos y al no existir el fichero, no habrá documento cargado.
			Logger logger= new Logger();
			logger.error("No se ha podido cargar o parsear el fichero de mapeo de mensajes con BBVA, error:" + ex.getMessage());
		}
		//conversor.put(key, value)
	}
	/**
	 * Devuelve el mensaje equivalente al de entrada de BBVA. Si no hay uno particular, devuelve el error genérico.
	 * @param errorBBVA
	 * @return
	 */
	public List<MensajesBBVA> getErrorBBVA (String errorBBVA)
	{
		List<MensajesBBVA> mapa = null;
		if (doc!=null)
		{
			try
			{
				javax.xml.xpath.XPath xpath=javax.xml.xpath.XPathFactory.newInstance().newXPath();
				NodeList nodos = (NodeList) xpath.evaluate("/mapa_mensajes/mapa_mensaje[remoto='"+errorBBVA.trim()+"']",
						doc, javax.xml.xpath.XPathConstants.NODESET); //Sólo debería haber uno, y sólo haremos caso a uno
				if (nodos.getLength()>0) //Hemos encontrado el nodo de nuestro mapeo.
				{
					mapa=new ArrayList<MensajesBBVA>();
					//Recuperamos los nodos de código y texto a mostrar.
					String codigo = xpath.evaluate("epst/text()", nodos.item(0));
					String desc = xpath.evaluate("epst_texto/text()", nodos.item(0));
					MensajesBBVA men = new MensajesBBVA();
					men.setCodigoBBVA(errorBBVA);
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
	 * Recupera el resultado en nuestros sistemas equivalente al resultado recibido del BBVA.
	 * @param resultadoBBVA
	 * @return
	 */
	public String getResultadoBBVA (String resultadoBBVA)
	{
		String res=null;
		if (resultadoBBVA!=null && resultadoBBVA.length()>=Constantes.getLongColumnaRespuesta())
		{
			int idxEspacio = resultadoBBVA.indexOf(Constantes.getCaracterSeparacionResultado());
			if (idxEspacio!=-1)
			{
				res=resultadoBBVA.substring(0,idxEspacio); // Código de resultado en BBVA.
				if (res.length()>Constantes.getLongColumnaRespuesta())
				{
					res = res.substring(0,Constantes.getLongColumnaRespuesta());
				}
			}
			else
			{
				if (resultadoBBVA.length()>Constantes.getLongColumnaRespuesta())
				{
					res=resultadoBBVA.substring(0,Constantes.getLongColumnaRespuesta()); // Código de resultado en BBVA.
				}
				else
				{
					res=resultadoBBVA;
				}
			}
		}
		else if (resultadoBBVA!=null) //Existe, pero tiene menos del ancho de la columna, introducimos todo
		{
			res=resultadoBBVA;
		}
		return res;
	}
}
