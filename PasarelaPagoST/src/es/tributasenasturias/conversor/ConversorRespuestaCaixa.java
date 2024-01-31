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
 * Convierte el mensaje de respuesta de LaCaixa a un mensaje nuestro.
 */
public class ConversorRespuestaCaixa {
	Document doc;
	/**
	 * Constructor
	 */
	public ConversorRespuestaCaixa()
	{
		//Cargamos el fichero de mapeo de errores.
		try
		{
		Preferencias pr = Preferencias.getPreferencias();
		java.io.File consFile; //Fichero donde se encuentra el mapeo de errores de bbva a nuestros errores.
		consFile = new java.io.File (pr.getFicheroMapeoLaCaixa());
		doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(consFile);
		}
		catch (Exception ex)
		{
			//No se habr� podido cargar el fichero. Logeamos y al no existir el fichero, no habr� documento cargado.
			Logger logger= new Logger();
			logger.error("No se ha podido cargar o parsear el fichero de mapeo de mensajes con BBVA, error:" + ex.getMessage());
		}
		//conversor.put(key, value)
	}
	/**
	 * Devuelve el mensaje equivalente al de entrada de LaCaixa. Si no hay uno particular, devuelve el error gen�rico.
	 * @param errorLaCaixa
	 * @return
	 */
	public List<MensajesCaixa> getErrorCaixa (String errorCaixa)
	{
		List<MensajesCaixa> mapa = null;
		if (doc!=null)
		{
			try
			{
				javax.xml.xpath.XPath xpath=javax.xml.xpath.XPathFactory.newInstance().newXPath();
				NodeList nodos = (NodeList) xpath.evaluate("/mapa_mensajes/mapa_mensaje[remoto='"+errorCaixa.trim()+"']",
						doc, javax.xml.xpath.XPathConstants.NODESET); //S�lo deber�a haber uno, y s�lo haremos caso a uno
				if (nodos.getLength()>0) //Hemos encontrado el nodo de nuestro mapeo.
				{
					mapa=new ArrayList<MensajesCaixa>();
					//Recuperamos los nodos de c�digo y texto a mostrar.
					String codigo = xpath.evaluate("epst/text()", nodos.item(0));
					String desc = xpath.evaluate("epst_texto/text()", nodos.item(0));
					MensajesCaixa men = new MensajesCaixa();
					men.setCodigoCaixa(errorCaixa);
					men.setCodigoEpst(codigo);
					men.setTextoEpst(desc);
					mapa.add(men);
				}
			}
			catch (javax.xml.xpath.XPathExpressionException ex)
			{
				Logger logger= new Logger();
				logger.error("No se ha podido recorrer el fichero de mapeo de mensajes.Se utiliza el c�digo de error por defecto; error:" + ex.getMessage());
			}
		}
		return mapa;
	}
	/**
	 * Recupera el resultado en nuestros sistemas equivalente al resultado recibido de la Caixa.
	 * @param resultadoCaixa
	 * @return
	 */
	public String getResultadoCaixa (String resultadoCaixa)
	{
		String res=null;
		if (resultadoCaixa!=null && resultadoCaixa.length()>=Constantes.getLongColumnaRespuesta())
		{
			int idxEspacio = resultadoCaixa.indexOf(Constantes.getCaracterSeparacionResultado());
			if (idxEspacio!=-1)
			{
				res=resultadoCaixa.substring(0,idxEspacio); // C�digo de resultado en la Caixa.
				if (res.length()>Constantes.getLongColumnaRespuesta())
				{
					res = res.substring(0,Constantes.getLongColumnaRespuesta());
				}
			}
			else
			{
				if (resultadoCaixa.length()>Constantes.getLongColumnaRespuesta())
				{
					res=resultadoCaixa.substring(0,Constantes.getLongColumnaRespuesta()); // C�digo de resultado en la Caixa.
				}
				else
				{
					res=resultadoCaixa;
				}
			}
		}
		else if (resultadoCaixa!=null) //Existe, pero tiene menos del ancho de la columna, introducimos todo
		{
			res=resultadoCaixa;
		}
		return res;
	}
}
