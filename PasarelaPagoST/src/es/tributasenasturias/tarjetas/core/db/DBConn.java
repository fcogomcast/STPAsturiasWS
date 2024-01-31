package es.tributasenasturias.tarjetas.core.db;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import stpa.services.LanzaPLService;
import es.tributasenasturias.webservices.soap.LogMessageHandlerClient;

/**
 * Clase de conexión a la base de datos. 
 * @author crubencvs
 *
 */
public class DBConn {

	private final stpa.services.LanzaPLService lanzaderaWS; 
	private final stpa.services.LanzaPL lanzaderaPort; 
	
	private String esquemaBD;
	
	@SuppressWarnings("unchecked")
	public DBConn(String endpointLanzador, String esquemaBD, LogMessageHandlerClient soapHandler){
		
		lanzaderaWS= new LanzaPLService();
		lanzaderaPort= lanzaderaWS.getLanzaPLSoapPort();
		
		this.esquemaBD=  esquemaBD;
		BindingProvider bpr= (BindingProvider) lanzaderaPort;
		bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,endpointLanzador);
		Binding bi = bpr.getBinding();
		List <Handler> handlerList = bi.getHandlerChain();
		if (handlerList == null)
		{
		   handlerList = new ArrayList<Handler>();
		}
		handlerList.add(soapHandler);
		bi.setHandlerChain(handlerList);
	}
	
	/**
	 * Devuelve los datos de la estructura de resultado de lanzador.
	 * Todos los nombres de estructuras y campos se almacenan en mayúsculas
	 * @param xmlEstructuras
	 * @return
	 * @throws DBConnException
	 */
	public Map<String, List<Map<String,String>>> getResults(String xmlEstructuras) throws DBConnException{
			DocumentBuilderFactory dbf= DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true); //No esperamos namespace de todas maneras
			try {
				Document doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(xmlEstructuras.getBytes()));
				Map<String, List<Map<String,String>>> results= new HashMap<String, List<Map<String,String>>>();
				//Si en lugar de "estruc" tenemos "error", pues se ha producido algún error.
				NodeList errores= doc.getElementsByTagName("error");
				//Si hay una estructura "ERROR", es un caso especial, no tiene filas.
				//Creo una fila con un campo "ERROR" y su valor, por coherencia
				if (errores.getLength()!=0){
					Element elError = (Element) errores.item(0);
					String causaError = elError.getTextContent();
					List<Map<String,String>> filasEstr= new ArrayList<Map<String,String>>();
					Map<String,String> campoMap = new HashMap<String, String>();
					campoMap.put("ERROR", causaError);
					filasEstr.add(campoMap);
					results.put("ERROR", filasEstr);
					//Paro en cuanto encuentre la estructura "ERROR".
					return results;
				}
				NodeList estructuras= doc.getElementsByTagName("estruc");
				for (int i=0;i<estructuras.getLength();i++){
					Node estructuraNode= estructuras.item(i);
					if (estructuraNode.getNodeType()== Node.ELEMENT_NODE){
						Element estEl= (Element) estructuraNode;
						String nombreEstructura= estEl.getAttribute("nombre").toUpperCase();
						List<Map<String,String>> filasEstr= new ArrayList<Map<String,String>>();
						NodeList filas= estEl.getElementsByTagName("fila");
						for (int j=0;j< filas.getLength();j++){
							Node filaNode= filas.item(j);
							if (filaNode.getNodeType()==Node.ELEMENT_NODE){
								Element filaEl = (Element) filaNode;
								NodeList campos= filaEl.getChildNodes();
								Map<String,String> campoMap = new HashMap<String, String>();
								for (int k=0;k<campos.getLength();k++){
									Node campoNode= campos.item(k);
									if (campoNode.getNodeType()==Node.ELEMENT_NODE){
										Element campoElement= (Element) campoNode;
										String nombreCampo = campoElement.getTagName().toUpperCase();
										String valor = campoElement.getTextContent();
										campoMap.put(nombreCampo, valor);
									}
								}
								filasEstr.add(campoMap);
							}
						}
						results.put(nombreEstructura, filasEstr);
					}
				}
				return results;
			} catch (SAXException e) {
				throw new DBConnException("Error al parsear el XML de resultado de lanzador:"+e.getMessage(),e);
			} catch (IOException e) {
				throw new DBConnException("Error al leer el XML de resultado de lanzador:"+ e.getMessage(),e);
			} catch (ParserConfigurationException e) {
				throw new DBConnException("Error al contruir el objeto DOM con el resultado de lanzador:"+ e.getMessage(),e);
			}
		
	}
	/**
	 * Ejecuta una petición y devuelve el resultado
	 * @param xmlLanzador
	 * @param esquema
	 * @return
	 * @throws DBConnException
	 */
	public Map<String, List<Map<String,String>>> ejecutar(String xmlLanzador) throws DBConnException{
		try {
			return getResults(lanzaderaPort.executePL(
					esquemaBD,
					xmlLanzador,
					"", "", "", ""));
		} catch (Exception e){
			throw new DBConnException ("Error al ejecutar la petición en el lanzador:"+ e.getMessage(), e);
		}
	}
	
	
	
	 
	
	
	
	
	
}
