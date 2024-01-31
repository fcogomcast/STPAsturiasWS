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
import es.tributasenasturias.utils.Varios;

/**
 * @author crubencvs
 * Convierte el mensaje de respuesta de CajaRural a un mensaje nuestro.
 */
public class ConversorRespuestaCajaRural {
	Document doc;
	/**
	 * Constructor
	 */
	public ConversorRespuestaCajaRural()
	{
		//Cargamos el fichero de mapeo de errores.
		try
		{
		Preferencias pr = Preferencias.getPreferencias();
		java.io.File consFile; //Fichero donde se encuentra el mapeo de errores de caja rural a nuestros errores.
		consFile = new java.io.File (pr.getFicheroMapeoCajaRural());
		doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(consFile);
		}
		catch (Exception ex)
		{
			//No se habrá podido cargar el fichero. Logeamos y al no existir el fichero, no habrá documento cargado.
			Logger logger= new Logger();
			logger.error("No se ha podido cargar o parsear el fichero de mapeo de mensajes con Caja Rural, error:" + ex.getMessage());
		}
		//conversor.put(key, value)
	}
	/**
	 * Devuelve el mensaje equivalente al de entrada de Caja Rural. Si no hay uno particular, devuelve el error genérico.
	 * @param errorCajaRural
	 * @return
	 */
	public List<MensajesCajaRural> getErrorCajaRural (String errorCajaRural)
	{
		List<MensajesCajaRural> mapa = null;
		if (doc!=null)
		{
			try
			{
				javax.xml.xpath.XPath xpath=javax.xml.xpath.XPathFactory.newInstance().newXPath();
				//Eliminamos 0 no significativos por la izquierda, para realizar el mapeo.
				NodeList nodos = (NodeList) xpath.evaluate("/mapa_mensajes/mapa_mensaje[remoto='"+Varios.ltrim(errorCajaRural.trim(),'0')+"']",
						doc, javax.xml.xpath.XPathConstants.NODESET); //Sólo debería haber uno, y sólo haremos caso a uno
				if (nodos.getLength()>0) //Hemos encontrado el nodo de nuestro mapeo.
				{
					mapa=new ArrayList<MensajesCajaRural>();
					//Recuperamos los nodos de código y texto a mostrar.
					String codigo = xpath.evaluate("epst/text()", nodos.item(0));
					String desc = xpath.evaluate("epst_texto/text()", nodos.item(0));
					MensajesCajaRural men = new MensajesCajaRural();
					men.setCodigoCajaRural(errorCajaRural);
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
	 * Recupera el resultado en nuestros sistemas equivalente al resultado recibido de Caja Rural.
	 * @param resultadoCajaRural
	 * @return
	 */
	public String getResultadoCajaRural (String resultadoCajaRural)
	{
		String res=null;
		if (resultadoCajaRural!=null && resultadoCajaRural.length()>=Constantes.getLongColumnaRespuesta())
		{
			int idxEspacio = resultadoCajaRural.indexOf(Constantes.getCaracterSeparacionResultado());
			if (idxEspacio!=-1)
			{
				res=resultadoCajaRural.substring(0,idxEspacio); // Código de resultado en Caja Rural.
				if (res.length()>Constantes.getLongColumnaRespuesta())
				{
					res = res.substring(0,Constantes.getLongColumnaRespuesta());
				}
			}
			else
			{
				if (resultadoCajaRural.length()>Constantes.getLongColumnaRespuesta())
				{
					res=resultadoCajaRural.substring(0,Constantes.getLongColumnaRespuesta()); // Código de resultado en Caja Rural
				}
				else
				{
					res=resultadoCajaRural;
				}
			}
		}
		else if (resultadoCajaRural!=null) //Existe, pero tiene menos del ancho de la columna, introducimos todo
		{
			res=resultadoCajaRural;
		}
		return res;
	}
}
