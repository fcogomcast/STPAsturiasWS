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
 * Convierte el mensaje de respuesta de Caja a un mensaje nuestro.
 */
public class ConversorRespuestaCaja {
	Document doc;
	/**
	 * Constructor
	 */
	public ConversorRespuestaCaja()
	{
		//Cargamos el fichero de mapeo de errores.
		try
		{
		Preferencias pr = Preferencias.getPreferencias();
		java.io.File consFile; //Fichero donde se encuentra el mapeo de errores de caja a nuestros errores.
		consFile = new java.io.File (pr.getFicheroMapeoCaja());
		doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(consFile);
		}
		catch (Exception ex)
		{
			//No se habrá podido cargar el fichero. Logeamos y al no existir el fichero, no habrá documento cargado.
			Logger logger= new Logger();
			logger.error("No se ha podido cargar o parsear el fichero de mapeo de mensajes con caja, error:" + ex.getMessage());
		}
		//conversor.put(key, value)
	}
	/**
	 * Devuelve el mensaje equivalente al de entrada de Caja. Si no hay uno particular, devuelve el error genérico.
	 * @param respuestaCaja
	 * @return
	 */
	public List<MensajesCaja> getErrorCaja (String errorCaja)
	{
		List<MensajesCaja> mapa = null;
		if (doc!=null)
		{
			try
			{
				javax.xml.xpath.XPath xpath=javax.xml.xpath.XPathFactory.newInstance().newXPath();
				NodeList nodos = (NodeList) xpath.evaluate("/mapa_mensajes/mapa_mensaje[remoto='"+errorCaja.trim()+"']",
						doc, javax.xml.xpath.XPathConstants.NODESET); //Sólo debería haber uno, y sólo haremos caso a uno
				if (nodos.getLength()>0) //Hemos encontrado el nodo de nuestro mapeo.
				{
					mapa=new ArrayList<MensajesCaja>();
					//Recuperamos los nodos de código y texto a mostrar.
					String codigo = xpath.evaluate("epst/text()", nodos.item(0));
					String desc = xpath.evaluate("epst_texto/text()", nodos.item(0));
					MensajesCaja men = new MensajesCaja();
					men.setCodigoCaja(errorCaja);
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
	 * Recupera el resultado de caja, recortándolo si fuera demasiado grande para nuestros sistemas,
	 * ya que posterioremente este resultado se almacenará en nuestra base de datos.
	 * @param resultadoCaja
	 * @return
	 */
	public String getResultadoCaja (String resultadoCaja)
	{
		String res=null;
		if (resultadoCaja!=null && resultadoCaja.length()>=Constantes.getLongColumnaRespuesta())
		{
			int idxEspacio = resultadoCaja.indexOf(Constantes.getCaracterSeparacionResultado());
			if (idxEspacio!=-1)
			{
				res=resultadoCaja.substring(0,idxEspacio); // Código de resultado en Liberbank.
				if (res.length()>Constantes.getLongColumnaRespuesta())
				{
					res = res.substring(0,Constantes.getLongColumnaRespuesta());
				}
			}
			else
			{
				if (resultadoCaja.length()>Constantes.getLongColumnaRespuesta())
				{
					res=resultadoCaja.substring(0,Constantes.getLongColumnaRespuesta()); // Código de resultado en Liberbank.
				}
				else
				{
					res=resultadoCaja;
				}
			}
		}
		else if (resultadoCaja!=null) //Existe, pero tiene menos del ancho de la columna, introducimos todo
		{
			res=resultadoCaja;
		}
		if (res!=null){
			return res.trim();
		} else {
			return null;
		}
	}
}
