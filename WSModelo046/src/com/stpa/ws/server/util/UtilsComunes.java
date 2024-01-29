package com.stpa.ws.server.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.stpa.ws.pref.m42.Preferencias;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.formularios.CodiDescBean;
import com.stpa.ws.server.formularios.Constantes;
import com.stpa.ws.server.formularios.FormFillBase;
import com.stpa.ws.server.formularios.GenericModeloFormBean;
import com.stpa.ws.server.formularios.IModeloFormBean;
import com.stpa.ws.server.formularios.ModeloTributarioDAO;
import com.stpa.ws.server.formularios.VerificaDocumentosEJB;

import es.trafico.webservices.antecedentesvehiculos.beans.InformeVehiculo;

public class UtilsComunes {

	public static String TIPO_CERTIFICADO_MUNICIPALES = "ND";
	public static String TIPO_CERTIFICADO_COMUNIDAD = "DA";
	public static boolean XML_A_MAYUSCULAS = true;
	
	private static final String cadenaFormat = "###,###,##0.00";

	
	
	
	/**
	 * Metodo para asignar variables en formato path|path|nombre#posicion. NOTA: la posicion pasa a ser el atributo ID
	 * del nodo padre (ver clase JustificanteFillBase)
	 * 
	 * @param modelo
	 * @param mapParams : valores para el nodo Modelo
	 * @param url
	 * @param output
	 * @return
	 * @throws RemoteException
	 * @throws StpawsException 
	 */
	public static String rellenarXMLDatos(String modelo, Map<String, Map<String, Object>> mapParams,String url, OutputStream output)
			throws StpawsException {

		String xmlDoc = getPlantillaXml(modelo, url);
		
		if (mapParams != null) {
			List<String> listNamesTags = new ArrayList<String>(mapParams.keySet());
			for (int i = 0; i < listNamesTags.size(); i++) {
				String tag = listNamesTags.get(i);
				Map<String, Object> mapParamsDatos = mapParams.get(tag);				
				if (Constantes.MODELO.equals(tag)) {
					eliminaParametrosXML(mapParamsDatos);
				}
				xmlDoc = rellenarTagsXMLDoc(xmlDoc, mapParamsDatos, output, tag);			
			}
		}		
		return xmlDoc;
	}
	
	/**
	 * Metodo para asignar variables en formato path|path|nombre#posicion. 
	 * @param xmlDoc
	 * @param mapParamsDatos
	 * @param output
	 * @param tag
	 * @return
	 * @throws RemoteException
	 */
	private static String rellenarTagsXMLDoc(String xmlDoc, Map<String, Object> mapParamsDatos, OutputStream output,
			String tag) throws StpawsException {
		
		String BAR = "|";
		String HASH = "#";

		List<String> listNames = new ArrayList<String>(mapParamsDatos.keySet());
		Collections.sort(listNames);
		int max_names = mapParamsDatos.size();

		max_names = listNames.size();
		String[] nodeNames = new String[max_names];
		for (int i = 0; i < max_names; i++) {
			nodeNames[i] = listNames.get(i);
			if (nodeNames[i] != null && nodeNames[i].endsWith("|"))
				nodeNames[i] = nodeNames[i].substring(0, nodeNames[i].length() - 1);
		}
		listNames.clear();
		try {
			Document doc = (Document) XMLUtils.compilaXMLObject(xmlDoc, null);
			if (doc != null) {
				Element[] c = XMLUtils.selectNodes(doc, "datos/" + tag);
				Element node = null;
				Element[] nodes = null;
				if (c == null || c.length == 0) {
					c = XMLUtils.selectNodes(doc, "datos");
					if (c == null || c.length == 0) {
						XMLUtils.addNode(doc, "datos");
						c = XMLUtils.selectNodes(doc, "datos");
					}
					XMLUtils.addNode(c[0], tag);
					c = XMLUtils.selectNodes(doc, "datos/" + tag);
				}
				for (int i = 0; i < nodeNames.length; i++) {
					if (nodeNames[i].contains(BAR))
						continue;
					String currentName = nodeNames[i];
					String id = "";
					currentName = currentName.replace(" ", "");
					if (currentName.contains(HASH)) {
						id = currentName.substring(currentName.lastIndexOf(HASH) + 1, currentName.length());
						currentName = currentName.substring(0, currentName.lastIndexOf(HASH));
					}
					node = null;
					nodes = XMLUtils.selectNodes(c[0], currentName);
					if (nodes != null && nodes.length != 0) {
						for (int j = 0; j < nodes.length; j++) {
							if (ValidatorUtils.isEmpty(id)
									|| (nodes[j].getAttribute("id") != null && nodes[j].getAttribute("id").equals(id))) {
								node = nodes[j];
								break;
							}
						}
					}
					if (node == null) {
						try {
							node = (Element) XMLUtils.addNode(c[0], currentName);
						} catch (DOMException e) {
							com.stpa.ws.server.util.Logger.debug("currentname " + currentName,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
							com.stpa.ws.server.util.Logger.debug("nodebase " + c[0].getNodeName(),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
							com.stpa.ws.server.util.Logger.debug("xml " + xmlDoc,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
							com.stpa.ws.server.util.Logger.debug("datos " + mapParamsDatos,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
							com.stpa.ws.server.util.Logger.error("UtilsComunes.rellenarTagsXMLDoc",e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
						}
						if (!id.equals(""))
							node.setAttribute("id", id);
					}
					try {
						Object valor = mapParamsDatos.get(nodeNames[i]);
						if (valor instanceof String) {
							String str = (String) valor;
							XMLUtils.CambiaNodo(c[0], currentName, encodeUTF8(str));
						} else if (valor instanceof String[]) {
							String[] lstValor = (String[]) valor;
							if (lstValor != null && lstValor.length != 0) {
								XMLUtils.CambiaNodo(node, ((String[]) valor)[0]);
								for (int j = 1; j < lstValor.length; j++) {
									node = (Element) XMLUtils.addNode(c[0], currentName);
									String str = (String) (((String[]) valor)[j]);
									XMLUtils.CambiaNodo(node, encodeUTF8(str));
								}
							}
						}
					} catch (Exception e) {
						com.stpa.ws.server.util.Logger.error("UtilsComunes.rellenarTagsXMLDoc",e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
						throw new RemoteException("", e);
					}
				}
				// los nodos hijos deben encontrar el subnodo adecuado
				// dentro de datos/modelo
				for (int i = 0; i < nodeNames.length; i++) {
					if (!nodeNames[i].contains(BAR))
						continue;
					String currentName = nodeNames[i].replace(BAR, "/");
					String id = "";
					currentName = currentName.replace(" ", "");
					if (currentName.contains(HASH)) {
						id = currentName.substring(currentName.lastIndexOf(HASH) + 1, currentName.length());
						currentName = currentName.substring(0, currentName.lastIndexOf(HASH));
					}
					node = null;
					nodes = XMLUtils.selectNodes(c[0], currentName);
					if (nodes != null && nodes.length != 0) {
						for (int j = 0; j < nodes.length; j++) {
							if (ValidatorUtils.isEmpty(id)) {
								node = nodes[j];
								break;
							} else if (nodes[j].getParentNode().getAttributes() != null
									&& nodes[j].getParentNode().getAttributes().getNamedItem("id") != null
									&& nodes[j].getParentNode().getAttributes().getNamedItem("id").equals(id)) {
								node = nodes[j];
								break;
							}
						}
					}
					String[] nodopaths = currentName.split("/");
					if (node == null) {
						Element nodolocal = c[0];
						for (int j = 0; j < nodopaths.length; j++) {
							nodes = null;
							// gotta select the right id!
							Element temp = null;
							nodes = XMLUtils.selectNodes(nodolocal, nodopaths[j]);
							if (nodes == null || nodes.length == 0)
								temp = (Element) XMLUtils.addNode(nodolocal, nodopaths[j]);
							else if (j == nodopaths.length - 2 && !id.equals("")) {
								for (int k = 0; k < nodes.length; k++)
									if (nodes[k].getAttribute("id") != null && nodes[k].getAttribute("id").equals(id))
										temp = nodes[k];
								if (temp == null)
									temp = (Element) XMLUtils.addNode(nodolocal, nodopaths[j]);
							} else {
								temp = nodes[0];
							}
							nodolocal = temp;
							if (j == nodopaths.length - 2 && !id.equals("")) {
								nodolocal.setAttribute("id", id);
							} else if (j == nodopaths.length - 1)
								node = nodolocal;
						}
						// node = XMLUtils.selectSingleNode(nodolocal,
						// currentName);
					}
					if (node == null)
						continue;
					// String nodeName = nodopaths[nodopaths.length - 1];
					try {
						Object valor = mapParamsDatos.get(nodeNames[i]);
						if (valor instanceof String) {
							String str = (String) valor;
							XMLUtils.CambiaNodo(node, encodeUTF8(str));
						} else if (valor instanceof String[]) {
							String[] lstValor = (String[]) valor;
							if (lstValor != null && lstValor.length != 0) {
								XMLUtils.CambiaNodo(node, ((String[]) valor)[0]);
								for (int j = 1; j < lstValor.length; j++) {
									node = (Element) XMLUtils.addNode(node.getParentNode(), nodeNames[i]);
									String str = (String) ((String[]) valor)[i];
									XMLUtils.CambiaNodo(node, encodeUTF8(str));
								}
							}
						}
					} catch (Exception e) {
						com.stpa.ws.server.util.Logger.error("UtilsComunes.rellenarTagsXMLDoc",e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
					}
				}
				TransformerFactory tfactory = TransformerFactory.newInstance();
				DOMSource source = new DOMSource(doc);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				StreamResult result = new StreamResult(bos);
				try {
					Transformer transformer = tfactory.newTransformer();// intro
					// param
					// para
					// xsl!
					transformer.transform(source, result);
					String resultdata = bos.toString("UTF-8");
					xmlDoc = resultdata;
				} catch (TransformerException e) {
					com.stpa.ws.server.util.Logger.error("UtilsComunes.rellenarTagsXMLDoc",e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				} catch (UnsupportedEncodingException e) {
					com.stpa.ws.server.util.Logger.error("UtilsComunes.rellenarTagsXMLDoc",e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				}
			}
			if (output != null) {
				byte[] bytes = xmlDoc.getBytes();
				try {
					output.write(bytes);
				} catch (IOException e) {
					com.stpa.ws.server.util.Logger.error("UtilsComunes.rellenarTagsXMLDoc",e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
					throw new RemoteException("", e);
				}
			}
		} catch (RemoteException e) {
			com.stpa.ws.server.util.Logger.error("No se ha podido rellenar la plantilla XML",e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException("No se ha podido rellenar la plantilla XML", e);
		}
		return xmlDoc;
	}

	/**
	 * Devuelve la plantilla XML para los modelos de formularios.
	 * @param modelo Modelo a recuperar
	 * @param url Path a la carpeta de datos para PDF
	 * @return
	 * @throws StpawsException 
	 */
	private static String getPlantillaXml(String modelo, String url) throws StpawsException {
		Preferencias pref = new Preferencias();
		String xmlDoc = "";
		String encoding = "";
		if (url != null) {
			//String pathCompleto = PropertiesUtils.getValorConfiguracion(Modelo046Constantes.PROPERTIES_STPAWS,"plantilla.modelo.046")+ ".xml";
			String pathCompleto = pref.getM_plantillamodelo046() + ".xml";
			pathCompleto = StringUtils.replace(pathCompleto, " ", "%20");
			xmlDoc = FormFillBase.getContents(pathCompleto, "UTF-8");
			encoding = XMLUtils.getEncoding(xmlDoc);
			if (encoding != null && !encoding.equals("") && !encoding.equals("UTF-8"))
				xmlDoc = FormFillBase.getContents(pathCompleto, encoding);
			com.stpa.ws.server.util.Logger.debug("PUNTO 1: RECUPEAR XML DOC" + xmlDoc + " CON ENCODING " + encoding,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			if (xmlDoc.length() == 0)
				return xmlDoc;
		} else {
			xmlDoc = "<datos></datos>";
			com.stpa.ws.server.util.Logger.debug("PUNTO 1: RECUPEAR XML DOC QUE NO EXISTE" + xmlDoc,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}
		
		return xmlDoc;
	}
	
	/**
	 * Renombra los campos especificados de un Mapa.
	 * @param paramsEntrada Mapa a renombrar
	 * @param nombreEntrada Lista de parametros a renombrar. Si no existe, no se renombra.
	 * @param nombreSalida Lista de nuevos parametros. Sobreescribe los parametros existentes.
	 * @return
	 */
	public static Map<String, Object> renombrarXMLDocMapa(Map<String, Object> paramsEntrada,
			List<String> nombreSalida, List<String> nombreEntrada) {

		if (paramsEntrada == null || nombreEntrada == null || nombreSalida == null || paramsEntrada.isEmpty())
			return paramsEntrada;
		for (int i = 0; i < nombreEntrada.size() && i < nombreSalida.size(); i++) {
			if (nombreEntrada.get(i).equals(nombreSalida.get(i)) || !paramsEntrada.containsKey(nombreEntrada.get(i)))
				continue;
			paramsEntrada.put(nombreSalida.get(i), paramsEntrada.get(nombreEntrada.get(i)));
			paramsEntrada.remove(nombreEntrada.get(i));
		}
		return paramsEntrada;
	}
	
	/**
	 * Rellena un Mapa con los parametros de Session y de un FormBean.
	 * @param form
	 * @param sessionParams
	 * @return
	 */
	public static HashMap<String, Object> rellenarXMLDocMapa(IModeloFormBean form, Map<String, String[]> sessionParams) {

		Field[] lstVariable = form.getClass().getFields();
		HashMap<String, Object> mapParam = rellenarXMLDocMapaBaseRecursiu(form, sessionParams, lstVariable);
			
		return mapParam;
	}
	
	/**
	 * Metodo para asignar variables en formato path|path|nombre#posicion. NOTA: la posicion pasa a ser el atributo ID
	 * del nodo padre (ver clase JustificanteFillBase)
	 * 
	 * @param modelo
	 * @param mapParams
	 * @param url
	 * @param output
	 * @return
	 * @throws RemoteException
	 */
	public static String rellenarXMLDoc(String modelo, Map<String, Object> mapParams, String url, OutputStream output)
			throws StpawsException {
		
		Map<String, Map<String, Object>> map = new HashMap<String, Map<String,Object>>();
		map.put("modelo", mapParams);
		
		return rellenarXMLDatos(modelo, map, url, output);
	}
	
	/**
	 * Rellena con parametros de la session y del bean de forma recursivo para beans que extienden de otras beans.
	 * @param form
	 * @param sessionParams
	 * @return
	 */
	public static HashMap<String, Object> rellenarXMLDocMapaRecursiu(IModeloFormBean form, Map<String, String[]> sessionParams) {

		Field[] lstVariable = form.getClass().getFields();
	
		 HashMap<String, Object> mapSalida = new HashMap<String, Object>(); 
		 mapSalida.putAll(rellenarXMLDocMapaBaseRecursiu(form, sessionParams, lstVariable));
		 Class<?> pare = form.getClass().getSuperclass();
		while (pare != null) {
			lstVariable = pare.getDeclaredFields();
			 mapSalida.putAll(rellenarXMLDocMapaBaseRecursiu(form, sessionParams, lstVariable));
			  pare = pare.getSuperclass(); 			  
		 }
		 
		 return mapSalida;
	}
	

	/**
	 * Rellena un Mapa con los parametros de Session y las variables escogidas de un FormBean.
	 * @param form
	 * @param sessionParams
	 * @param lstVariable
	 * @return
	 */
	private static HashMap<String, Object> rellenarXMLDocMapaBaseRecursiu(IModeloFormBean form, Map<String, String[]> sessionParams,
			Field[] lstVariable) {
	
		HashMap<String, Object> mapaXML = new HashMap<String, Object>();

		if (sessionParams != null) {
			Iterator<String> it = sessionParams.keySet().iterator();
			while (it.hasNext()) {
				String campo = (String) it.next();
				String[] obj = sessionParams.get(campo);
				if (obj != null && obj.length != 0) {
					mapaXML.put(campo, ((String[]) sessionParams.get(campo))[0]);
				}
			}
		}
		// hay campos de Portal que no hay que incluir en el XML
		String[] paramNoXml = Constantes.PARAM_NO_XML;
		
		for (int i = 0; i < lstVariable.length; i++) {
			String nombre = lstVariable[i].getName();
			Object valor = null;
			try {
				valor = lstVariable[i].get(form);
			} catch (IllegalAccessException e) {
				com.stpa.ws.server.util.Logger.debug("UtilsComunes.rellenarXMLDocMapaBaseRecursiu",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			}
			com.stpa.ws.server.util.Logger.debug("(nombre,valor):("+nombre+","+valor+")" ,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			mapaXML.put(nombre, valor);
		}
		for (int i = 0; i < paramNoXml.length; i++) {
			mapaXML.remove(paramNoXml[i]);
		}

		return mapaXML;
	}

	/**
	 * Recupera el CodiDescBean dado un codigo.
	 * 
	 * @param p_codigo Codigo a recuperar
	 * @param p_lista Set o TreeSet de beans
	 * @return CodiDescBean
	 */
	public static CodiDescBean getCodiDescBeanPerCodigo(String p_codigo, Set<CodiDescBean> p_lista) {
		if (p_lista != null && !StringUtils.isBlank(p_codigo)) {
			Iterator<CodiDescBean> it = p_lista.iterator();
			while (it.hasNext()) {
				CodiDescBean elem = (CodiDescBean) it.next();
				if (p_codigo.equals(elem.getCodi())) {
					return elem;
				}
			}
		}
		return null;
	}

	
	/**
	 * Devuelve la lista de meses del calendario.
	 * @return
	 */
	public static Set<CodiDescBean> crearListaMeses() {
		
		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>(new Comparator<CodiDescBean>() {
		
			public int compare(CodiDescBean o1, CodiDescBean o2) {
				return o1.codi.compareTo(o2.codi);
			}
		});
		
		retorn.add(new CodiDescBean("01", "Enero"));
		retorn.add(new CodiDescBean("02", "Febrero"));
		retorn.add(new CodiDescBean("03", "Marzo"));
		retorn.add(new CodiDescBean("04", "Abril"));
		retorn.add(new CodiDescBean("05", "Mayo"));
		retorn.add(new CodiDescBean("06", "Junio"));
		retorn.add(new CodiDescBean("07", "Julio"));
		retorn.add(new CodiDescBean("08", "Agosto"));
		retorn.add(new CodiDescBean("09", "Septiembre"));
		retorn.add(new CodiDescBean("10", "Octubre"));
		retorn.add(new CodiDescBean("11", "Noviembre"));
		retorn.add(new CodiDescBean("12", "Diciembre"));
		
		return retorn;
	}
	
	/**
	 * Devuelve la lista de centros gestores.
	 * @return
	 */
	public static Set<CodiDescBean> crearListaCentroGestor() {
		
		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>(new Comparator<CodiDescBean>() {
		
			public int compare(CodiDescBean o1, CodiDescBean o2) {
				return o1.codi.compareTo(o2.codi);
			}
		});
		
		retorn.add(new CodiDescBean("11.01.","11.01. Consejería de la Presidencia. BOPA"));
        retorn.add(new CodiDescBean("12.01.","12.01. Consejería de Economía y Admón Pública. Oficina Presupuestaria"));
        retorn.add(new CodiDescBean("12.02.","12.02. Consejería de Economía y Admón Pública. I.A.A.P. Adolfo Posada"));
        retorn.add(new CodiDescBean("12.03.","12.03. Consejería de Economía y Admón Pública. Entidades Jurídicas"));
        retorn.add(new CodiDescBean("12.04.","12.04. Consejería de Economía y Admón Pública. Servicio de Política Financiera"));
        retorn.add(new CodiDescBean("13.01.","13.01. Consejería de Justicia, Seguridad P. Y R. Exteriores. Servicio Seguridad Pública"));
        retorn.add(new CodiDescBean("14.01.","14.01. Consejería de Cultura, Comunicac. Social y Turismo. Oficina Presupuestaria"));
        retorn.add(new CodiDescBean("14.02.","14.02. Consejería de Cultura, Comunicac. Social y Turismo. Estación Invernal de Pajares"));
        retorn.add(new CodiDescBean("14.03.","14.03. Consejería de Cultura, Comunicac. Social y Turismo. Instalaciones El Cristo"));
        retorn.add(new CodiDescBean("14.04.","14.04. Consejería de Cultura, Comunicac. Social y Turismo. Área de Instalaciones y Equipamientos"));
        retorn.add(new CodiDescBean("14.05.","14.05. Consejería de Cultura, Comunicac. Social y Turismo. Instituto Asturiano de Juventud"));
        retorn.add(new CodiDescBean("14.06.","14.06. Consejería de Cultura, Comunicac. Social y Turismo. Servicio de Patrimonio Histórico"));
        retorn.add(new CodiDescBean("14.07.","14.07. Consejería de Cultura, Comunicac. Social y Turismo. Bibliotecas de Patrimonio Histórico"));
        retorn.add(new CodiDescBean("14.08.","14.08. Consejería de Cultura, Comunicac. Social y Turismo. Registro de la Propiedad Intelectual"));
        retorn.add(new CodiDescBean("14.09.","14.09. Consejería de Cultura, Comunicac. Social y Turismo. Ciudad Vacaciones Perlora"));
        retorn.add(new CodiDescBean("15.01.","15.01. Consejería de Educación y Ciencia. Conservatorio Superior de Música"));
        retorn.add(new CodiDescBean("15.02.","15.02. Consejería de Educación y Ciencia. Instituto de Teatro y Artes Escénicas"));
        retorn.add(new CodiDescBean("15.03.","15.03. Consejería de Educación y Ciencia. Escuela Universitaria de Trabajo Social"));
        retorn.add(new CodiDescBean("15.04.","15.04. Consejería de Educación y Ciencia. Escuela Superior de Arte"));
        retorn.add(new CodiDescBean("15.05.","15.05. Consejería de Educación y Ciencia. Conservatorio de Gijón"));
		retorn.add(new CodiDescBean("15.06.","15.06. Consejería de Educación y Ciencia. Escuela Oficial de Idiomas de Avilés"));
		retorn.add(new CodiDescBean("15.07.","15.07. Consejería de Educación y Ciencia. Escuela Oficial de Idiomas de Gijón"));
		retorn.add(new CodiDescBean("15.08.","15.08. Consejería de Educación y Ciencia. Escuela Oficial de Idiomas de Langreo"));
		retorn.add(new CodiDescBean("15.09.","15.09. Consejería de Educación y Ciencia. Escuela Oficial de Idiomas de Luarca"));
		retorn.add(new CodiDescBean("15.10.","15.10. Consejería de Educación y Ciencia. Escuela Oficial de Idiomas de Llanes"));
		retorn.add(new CodiDescBean("15.11.","15.11. Consejería de Educación y Ciencia. Escuela Oficial de Idiomas de Mieres"));
		retorn.add(new CodiDescBean("15.12.","15.12. Consejería de Educación y Ciencia. Escuela Oficial de Idiomas de Oviedo"));
		retorn.add(new CodiDescBean("15.13.","15.13. Consejería de Educación y Ciencia. Centros Educativos"));
		retorn.add(new CodiDescBean("15.14.","15.14. Consejería de Educación y Ciencia. Otros"));
		retorn.add(new CodiDescBean("15.15.","15.15. Consejería de Educación y Ciencia. Escuela del Deporte"));
        retorn.add(new CodiDescBean("16.01.","16.01. Consejería de Vivienda y Bienestar Social. Servicio de Gestión de Centros"));
        retorn.add(new CodiDescBean("16.02.","16.02. Consejería de Vivienda y Bienestar Social. Vivienda"));
        retorn.add(new CodiDescBean("17.01.","17.01. Consejería de M. Ambiente, Ord. Del Territorio e Infraestructuras. Transportes"));
        retorn.add(new CodiDescBean("17.02.","17.02. Consejería de M. Ambiente, Ord. Del Territorio e Infraestructuras. Puertos"));
        retorn.add(new CodiDescBean("17.03.","17.03. Consejería de M. Ambiente, Ord. Del Territorio e Infraestructuras. Carreteras"));
        retorn.add(new CodiDescBean("17.04.","17.04. Consejería de M. Ambiente, Ord. Del Territorio e Infraestructuras. Laboratorio de Materiales"));
        retorn.add(new CodiDescBean("17.05.","17.05. Consejería de M. Ambiente, Ord. Del Territorio e Infraestructuras. L.A.C.E."));
        retorn.add(new CodiDescBean("17.06.","17.06. Consejería de M. Ambiente, Ord. Del Territorio e Infraestructuras. Urbanismo"));
        retorn.add(new CodiDescBean("17.11.","17.11. Consejería de M. Ambiente, Ord. Del Territorio e Infraestructuras. Caza y Pesca"));
        retorn.add(new CodiDescBean("18.01.","18.01. Consejería de Medio Rural y Pesca. Modernización o Fomento Asociativo"));
        retorn.add(new CodiDescBean("18.02.","18.02. Consejería de Medio Rural y Pesca. Montes"));
        retorn.add(new CodiDescBean("18.03.","18.03. Consejería de Medio Rural y Pesca. Industrias y Comercialización Agraria"));
        retorn.add(new CodiDescBean("18.04.","18.04. Consejería de Medio Rural y Pesca. Producción Ganadera y Agrícola"));
        retorn.add(new CodiDescBean("18.05.","18.05. Consejería de Medio Rural y Pesca. Asuntos Generales"));
        retorn.add(new CodiDescBean("18.06.","18.06. Consejería de Medio Rural y Pesca.  Pesca"));
        retorn.add(new CodiDescBean("19.02.","19.02. Consejería de Industria y Empleo"));
        retorn.add(new CodiDescBean("20.01.","20.01. Consejería de Salud y Servicios Sanitarios"));
        retorn.add(new CodiDescBean("20.04.","20.04. Cons Salud.Dir General calidad innovación servicio sanitario"));
		
		return retorn;
	}
	
	/**
	 * Devuelve la lista de aplicaciones de un bien.
	 * @return
	 */
	public static Set<CodiDescBean> crearListaAplicacion() {
		
		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>(new Comparator<CodiDescBean>() {
		
			public int compare(CodiDescBean o1, CodiDescBean o2) {
				return o1.codi.compareTo(o2.codi);
			}
		});
	
		retorn.add(new CodiDescBean("300000", "300000. Venta de libros de órdenes y visitas"));
        retorn.add(new CodiDescBean("302000", "302000. Venta de impresos a entidades locales"));
        retorn.add(new CodiDescBean("302001", "302001. Venta de publicaciones propias"));
        retorn.add(new CodiDescBean("306000", "306000. Venta de libros de órdenes y visitas"));
        retorn.add(new CodiDescBean("306001", "306001. Realización de fotocopias y reproducciones planos"));
        retorn.add(new CodiDescBean("307001", "307001. Realización de fotocopias y reproducciones planos"));
        retorn.add(new CodiDescBean("308000", "308000. Aprovechamientos Forestales"));
        retorn.add(new CodiDescBean("310001", "310001. Ingresos por servicios a terceros"));
        retorn.add(new CodiDescBean("314000", "314000. Ciudad de Vacaciones de Perlora"));
        retorn.add(new CodiDescBean("314001", "314001. Canon de empresas turísticas"));
        retorn.add(new CodiDescBean("314002", "314002. Estación Invernal de Pajares"));
        retorn.add(new CodiDescBean("314003", "314003. Instalaciones Deportivas El Cristo"));
        retorn.add(new CodiDescBean("314004", "314004. Polideportivo de Riaño (Langreo)"));
        retorn.add(new CodiDescBean("314005", "314005. Centro Regional de Deportes La Morgal"));
        retorn.add(new CodiDescBean("314006", "314006. Parque Deportivo de La Felguera"));
        retorn.add(new CodiDescBean("314007", "314007. Albergues y Residencias Juveniles"));
        retorn.add(new CodiDescBean("314008", "314008. Verano Joven"));
        retorn.add(new CodiDescBean("314009", "314009. Carnet Joven"));
        retorn.add(new CodiDescBean("314010", "314010. Carnet ISYC, REAJ, RYTO, TEACHER"));
        retorn.add(new CodiDescBean("314011", "314011. Entrada y visita al Museo Jurásico de Asturias"));
        retorn.add(new CodiDescBean("315001", "315001. Enseñanzas régimen especial obtención título técnico deportivo"));
        retorn.add(new CodiDescBean("315005", "315005. Enseñanza de Idiomas, Artísticas, Conser.Bienes"));
        retorn.add(new CodiDescBean("315006", "315006. Instituto del Teatro y las Artes Escénicas"));
        retorn.add(new CodiDescBean("315007", "315007. Escuela Universitaria de Trabajo Social"));
        retorn.add(new CodiDescBean("316000", "316000. Jardines de Infancia"));
        retorn.add(new CodiDescBean("316001", "316001. Servicios en Centros para personas Discapacitadas"));
        retorn.add(new CodiDescBean("316002", "316002. Servicios en Centros de Día"));
        retorn.add(new CodiDescBean("317000", "317000. Transportes Viajeros y Mercancías Funicular de Bulnes"));
        retorn.add(new CodiDescBean("318000", "318000. Servicios en Materia Forestal y de Montes"));
        retorn.add(new CodiDescBean("320000", "320000. Servicios de Salud e Inspecciones Sanitarias de Salud Pública"));
        retorn.add(new CodiDescBean("320001", "320001. Inspección y Control Sanitario de Carnes Frescas"));
        retorn.add(new CodiDescBean("320002", "320002. Expedición hojas de Reclamaciones"));
        retorn.add(new CodiDescBean("321000", "321000. Espectáculos"));
        retorn.add(new CodiDescBean("322000", "322000. Boletín Oficial del Principado de Asturias"));
        retorn.add(new CodiDescBean("322001", "322001. Asociaciones"));
        retorn.add(new CodiDescBean("322002", "322002. Tasa diploma mediador de seguros"));
        retorn.add(new CodiDescBean("322003", "322003. Inscripción en Pruebas de Acceso a la Función Pública"));
        retorn.add(new CodiDescBean("322004", "322004. Servicios Administrativos: Casinos, Juegos y Apuestas"));
        retorn.add(new CodiDescBean("324001", "324001. Entradas y Visitas a Cuevas y Yacimientos Prehistóricos"));
        retorn.add(new CodiDescBean("324002", "324002. Prestación de Servicios del Registro de la Propiedad Intelectual"));
        retorn.add(new CodiDescBean("325002", "325002. Expedición de Títulos, Certificados y Diplomas"));
        retorn.add(new CodiDescBean("325004", "325004. Inscripción en Pruebas de Acceso a Cuerpos Docentes"));
        retorn.add(new CodiDescBean("326000", "326000. Expedición de Cédulas de Habitabilidad"));
        retorn.add(new CodiDescBean("326001", "326001. Concesión Certificados V.P.O. y precio máximo de venta"));
        retorn.add(new CodiDescBean("326002", "326002. Concesión de Calificaciones V.P.A."));
        retorn.add(new CodiDescBean("327000", "327000. Licencias de Obras y Aprovechamiento Red de Carreteras"));
        retorn.add(new CodiDescBean("327001", "327001. Puertos"));
        retorn.add(new CodiDescBean("327002", "327002. Ordenación de Transportes Carreteras y otras Actuaciones Facultativas"));
        retorn.add(new CodiDescBean("327005", "327005. Laboratorios Control de Calidad de la Edificación"));
        retorn.add(new CodiDescBean("327006", "327006. Trabajos en el Centro de Estudios de Calidad de la Edificación"));
        retorn.add(new CodiDescBean("327007", "327007. Prospecciones, control de obra y ensayo de materiales"));
        retorn.add(new CodiDescBean("327008", "327008. Servicios de Información Cartográfica"));
        retorn.add(new CodiDescBean("327009", "327009. Licencias de Pesca Continental"));
        retorn.add(new CodiDescBean("327010", "327010. Permisos de Pesca"));
        retorn.add(new CodiDescBean("327011", "327011. Licencias de Caza y matrícula de Cotos de Caza"));
        retorn.add(new CodiDescBean("327012", "327012. Permiso Caza Reservas dependientes de Administración"));
        retorn.add(new CodiDescBean("328000", "328000. Servicios Facultativos Veterinarios"));
        retorn.add(new CodiDescBean("328001", "328001. Pesca marítima"));
        retorn.add(new CodiDescBean("328002", "328002. Ordenación y defensa de las Industrias Agrícolas, Forestales, Pecuarias y Alimentarias"));
        retorn.add(new CodiDescBean("328003", "328003. Gestión de Servicios Facultativos de los Servicios Agronómicos"));
        retorn.add(new CodiDescBean("328004", "328004. Servicios y Trabajos en Materia Forestal"));
        retorn.add(new CodiDescBean("328005", "328005. Expedición de Titulaciones y Tarjetas Actividades Subacuáticas"));
        retorn.add(new CodiDescBean("329000", "329000. Tasa de Minas"));
        retorn.add(new CodiDescBean("329001", "329001. Tasa de Industria"));
        retorn.add(new CodiDescBean("332000", "332000. Juegos de suerte, envite y azar"));
        retorn.add(new CodiDescBean("382000", "382000. Reintegros de Ejercicios Cerrados"));
        retorn.add(new CodiDescBean("382001", "382001. Reintegros del Ejercicio Corriente"));
        retorn.add(new CodiDescBean("388000", "388000. Reintegros del FEGA"));
        retorn.add(new CodiDescBean("390000", "390000. Multas y Sanciones (en materia de salud y servicios sanitarios)"));
        retorn.add(new CodiDescBean("390001", "390001. Aportación para medicamentos extranjeros"));
        retorn.add(new CodiDescBean("390004", "390004. Inscripciones y aportaciones jornadas ciencias de salud"));                
        retorn.add(new CodiDescBean("391000", "391000. Multas y Sanciones (en materia de Justicia, Seguridad Pública y Relaciones Exteriores)"));
        retorn.add(new CodiDescBean("392000", "392000. Recargos sobre apremios"));
        retorn.add(new CodiDescBean("392001", "392001. Intereses de demora"));
        retorn.add(new CodiDescBean("392002", "392002. Multas y Sanciones (Economía y Administración Pública)"));
        retorn.add(new CodiDescBean("392003", "392003. Multas y Sanciones (Presidencia)"));
        retorn.add(new CodiDescBean("392004", "392004. Otros ingresos no previstos"));
        retorn.add(new CodiDescBean("392006", "392006. Canon explotación Cafeteria (Presidencia, Economía y Administración Pública)"));
        retorn.add(new CodiDescBean("392007", "392007. Ingresos diversos"));
        retorn.add(new CodiDescBean("394000", "394000. Multas y Sanciones (Cultura, Comunicación Social y Turismo)"));
        retorn.add(new CodiDescBean("394001", "394001. Canon Explotación Cafeterías (Cultura, Comunicación Social y Turismo)"));
        retorn.add(new CodiDescBean("394002", "394002. Canon contratos de gestión de servicios públicos"));
        retorn.add(new CodiDescBean("395000", "395000. Otros ingresos"));
        retorn.add(new CodiDescBean("395004", "395004. Ingresos en concepto de gestión matrículas. Becas Escuela Univ. de Trabajo Social"));
        retorn.add(new CodiDescBean("396000", "396000. Canon Servicios en Centros"));
        retorn.add(new CodiDescBean("396001", "396001. Multas y Sanciones (Vivienda y Bienestar Social)"));
        retorn.add(new CodiDescBean("396002", "396002. Otros ingresos"));
        retorn.add(new CodiDescBean("396003", "396003. Canón convenio SEDES"));
        retorn.add(new CodiDescBean("396004", "396004. Traspaso de locales"));
        retorn.add(new CodiDescBean("397000", "397000. Multas y Sanciones"));
        retorn.add(new CodiDescBean("397002", "397002. Otros Cánones"));
        retorn.add(new CodiDescBean("397003", "397003. Cánones Cotos Regionales de Caza"));
        retorn.add(new CodiDescBean("398000", "398000. Multas y Sanciones"));
        retorn.add(new CodiDescBean("399000", "399000. Aportaciones para Ferias de Artesanía"));
        retorn.add(new CodiDescBean("399001", "399001. Multas y Sanciones"));
				
		return retorn;
		
	}
	
	

	/**
	 * Devuelve la lista de relaciones entre varios sujetos.
	 * @return
	 */
	public static Set<CodiDescBean> findRelaciones() {

		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>();

		CodiDescBean elem = new CodiDescBean("CO", "Cónyuge");
		retorn.add(elem);
		CodiDescBean elem2 = new CodiDescBean("GE", "Gestor");
		retorn.add(elem2);
		CodiDescBean elem3 = new CodiDescBean("IN", "Interesado");
		retorn.add(elem3);
		CodiDescBean elem4 = new CodiDescBean("RR", "Representante");
		retorn.add(elem4);
		CodiDescBean elem5 = new CodiDescBean("OT", "Un tercero");
		retorn.add(elem5);

		return retorn;
	}

	/**
	 * Para modelo 006. Devuelve la lista de motivos de solucitud.
	 * 
	 * @return
	 */
	public static Set<CodiDescBean> findMotivoSolicitud() {
		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>();

		CodiDescBean elem = new CodiDescBean("MC1", "CONTRATAR CON LA ADMINISTRACIÓN PÚBLICA");
		retorn.add(elem);

		CodiDescBean elem2 = new CodiDescBean("MC2", "OBTENER SUBVENCIONES Y AYUDAS");
		retorn.add(elem2);

		CodiDescBean elem3 = new CodiDescBean("MC3", "OBTENER AUTORIZACIONES ADMINISTRATIVAS");
		retorn.add(elem3);

		return retorn;
	}

	/**
	 * Devuelve la lista de tipos de certificados de tributos.
	 * @return
	 */
	public static Set<CodiDescBean> findTiposCertificado() {
		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>();

		CodiDescBean elem = new CodiDescBean("ND", "Tributos Municipales");
		retorn.add(elem);
		CodiDescBean elem2 = new CodiDescBean("DA", "Tributos de la Comunidad Autónoma del Principado de Asturias");
		retorn.add(elem2);

		return retorn;
	}

	/**
	 * Devuelve la lista de grados de parentesco entre sujetos.
	 * @return
	 */
	public static Set<CodiDescBean> findParentesco() {
		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>();
		CodiDescBean elem;
		elem = new CodiDescBean("AA-II", "Ascendientes y adoptantes");
		retorn.add(elem);
		elem = new CodiDescBean("AD-III", "Ascendientes y descendientes por afinidad");
		retorn.add(elem);
		elem = new CodiDescBean("C2-III", "Colaterales de 2º grado (hermanos)");
		retorn.add(elem);
		elem = new CodiDescBean("C3-III", "Colaterales de 3º grado (sobrinos, tíos)");
		retorn.add(elem);
		elem = new CodiDescBean("C4-IV", "Colaterales de 4º grado (primos)");
		retorn.add(elem);
		elem = new CodiDescBean("CO-II", "Cónyuges");
		retorn.add(elem);
		elem = new CodiDescBean("DA-II", "Descendientes y adoptados de 21 o más años");
		retorn.add(elem);
		elem = new CodiDescBean("DA-I", "Descendientes y adoptados menores de 21 años");
		retorn.add(elem);
		elem = new CodiDescBean("GD-IV", "Grados más distantes y extraños");
		retorn.add(elem);

		return retorn;
	}
	
	/**
	 * Devuelve la lista de grados de minusvalia.
	 * @return
	 */
	public static Set<CodiDescBean> findMinusvalia() {
		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>();
		CodiDescBean elem;
		elem = new CodiDescBean("A", "Menos de 33%");
		retorn.add(elem);
		elem = new CodiDescBean("B", "Mas de 33%");
		retorn.add(elem);
		elem = new CodiDescBean("N", "Sin minusvalia");
		retorn.add(elem);

		return retorn;
	}

	/**
	 * Devuelve la lista de periodos: meses, agrupaciones y anual.
	 * @return
	 */
	public static List<CodiDescBean> findPeriodos() {
		List<CodiDescBean> retorn = new ArrayList<CodiDescBean>();
		//Primero van los meses
		CodiDescBean elem01 = new CodiDescBean("01", "Enero");
		retorn.add(elem01);
		CodiDescBean elem02 = new CodiDescBean("02", "Febrero");
		retorn.add(elem02);
		CodiDescBean elem03 = new CodiDescBean("03", "Marzo");
		retorn.add(elem03);
		CodiDescBean elem04 = new CodiDescBean("04", "Abril");
		retorn.add(elem04);
		CodiDescBean elem05 = new CodiDescBean("05", "Mayo");
		retorn.add(elem05);
		CodiDescBean elem06 = new CodiDescBean("06", "Junio");
		retorn.add(elem06);
		CodiDescBean elem07 = new CodiDescBean("07", "Julio");
		retorn.add(elem07);
		CodiDescBean elem08 = new CodiDescBean("08", "Agosto");
		retorn.add(elem08);
		CodiDescBean elem09 = new CodiDescBean("09", "Septiembre");
		retorn.add(elem09);
		CodiDescBean elem10 = new CodiDescBean("10", "Octubre");
		retorn.add(elem10);
		CodiDescBean elem11 = new CodiDescBean("11", "Noviembre");
		retorn.add(elem11);
		CodiDescBean elem12 = new CodiDescBean("12", "Diciembre");
		retorn.add(elem12);
		//luego los bimestres
		CodiDescBean elem1b = new CodiDescBean("1B", "Primer bimestre");
		retorn.add(elem1b);
		CodiDescBean elem2b = new CodiDescBean("2B", "Segundo bimestre");
		retorn.add(elem2b);
		CodiDescBean elem3b = new CodiDescBean("3B", "Tercer bimestre");
		retorn.add(elem3b);
		CodiDescBean elem4b = new CodiDescBean("4B", "Cuarto bimestre");
		retorn.add(elem4b);
		CodiDescBean elem5b = new CodiDescBean("5B", "Quinto bimestre");
		retorn.add(elem5b);
		CodiDescBean elem6b = new CodiDescBean("6B", "Sexto bimestre");
		retorn.add(elem6b);
		//luego los trimestres
		CodiDescBean elem1t = new CodiDescBean("1T", "Primer trimestre");
		retorn.add(elem1t);
		CodiDescBean elem2t = new CodiDescBean("2T", "Segundo trimestre");
		retorn.add(elem2t);
		CodiDescBean elem3t = new CodiDescBean("3T", "Tercer trimestre");
		retorn.add(elem3t);
		CodiDescBean elem4t = new CodiDescBean("4T", "Cuarto trimestre");
		retorn.add(elem4t);
		//luego los cuatrimestres
		CodiDescBean elem1c = new CodiDescBean("1C", "Primer cuatrimestre");
		retorn.add(elem1c);
		CodiDescBean elem2c = new CodiDescBean("2C", "Segundo cuatrimestre");
		retorn.add(elem2c);
		CodiDescBean elem3c = new CodiDescBean("3C", "Tercer cuatrimestre");
		retorn.add(elem3c);
		//luego los semestres
		CodiDescBean elem1s = new CodiDescBean("1S", "Primer semestre");
		retorn.add(elem1s);
		CodiDescBean elem2s = new CodiDescBean("2S", "Segundo semestre");
		retorn.add(elem2s);
		//finalmente el anyo
		CodiDescBean elem = new CodiDescBean("0A", "Anual");
		retorn.add(elem);
		return retorn;
	}


	/**
	 * Devuelve la lista de trimestres.
	 * @return
	 */
	public static Set<CodiDescBean> findPeriodosSimples() {
		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>();
		CodiDescBean elem = new CodiDescBean("1T", "1T");
		retorn.add(elem);
		CodiDescBean elem2 = new CodiDescBean("2T", "2T");
		retorn.add(elem2);
		CodiDescBean elem3 = new CodiDescBean("3T", "3T");
		retorn.add(elem3);
		CodiDescBean elem4 = new CodiDescBean("4T", "4T");
		retorn.add(elem4);
     	return retorn;
	}

	/**
	 * Devuelve la lista de tipos de ejercicios.
	 * @return
	 */
	public static Set<CodiDescBean> findAplicacion() {

		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>();

		CodiDescBean elem = new CodiDescBean("383000", "383000 - De ejercicios cerrados");
		retorn.add(elem);
		CodiDescBean elem2 = new CodiDescBean("383001", "383001 - De ejercicio corriente");
		retorn.add(elem2);
		return retorn;
	}

	/**
	 * Devuelve la lista abreviada de claves de bienes.
	 * @return
	 */
	public static Set<CodiDescBean> findListaClaveBien() {

		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>();
		CodiDescBean elem = new CodiDescBean("P", "P");
		retorn.add(elem);
		elem = new CodiDescBean("N", "N");
		retorn.add(elem);
		elem = new CodiDescBean("U", "U");
		retorn.add(elem);
		elem = new CodiDescBean("T", "T");
		retorn.add(elem);
		return retorn;
	}
	
	/**
	 * Devuelve la lista de clave de bienes. No se corresponde con la lista abreviada.
	 * @return
	 */
	public static Set<CodiDescBean> findListaClaveBienDet() {

		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>();
		CodiDescBean elem = new CodiDescBean("PD", "Pleno Dominio");
		retorn.add(elem);
		elem = new CodiDescBean("U", "Usufructo");
		retorn.add(elem);
		elem = new CodiDescBean("ND", "Nuda propiedad");
		retorn.add(elem);
		elem = new CodiDescBean("UH", "Derecho uso y habitación");
		retorn.add(elem);
		return retorn;
	}

	/**
	 * Devuelve la lista abreviada de tipos de cultivo para bienes rurales.
	 * @return
	 */
	public static Set<CodiDescBean> findTipoCultivo() {

		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>();
		CodiDescBean elem = new CodiDescBean("MA", "MA");
		retorn.add(elem);
		elem = new CodiDescBean("MB", "MB");
		retorn.add(elem);
		elem = new CodiDescBean("P", "P");
		retorn.add(elem);
		return retorn;
	}
	
	/**
	 * Devuelve la lista de tipos de cultivo para bienes rurales.
	 * @return
	 */
	public static Set<CodiDescBean> findTipoCultivoDet() {

		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>();
		retorn.add(new CodiDescBean("MA", "MONTE ALTO"));
		retorn.add(new CodiDescBean("MB", "MONTE BAJO"));
		retorn.add(new CodiDescBean("P", "PRADO"));
		return retorn;
	}
	
	/**
	 * Devuelve la lista de clase de bien para bienes urbanos.
	 * @return
	 */
	public static Set<CodiDescBean> findTipoClaseBien() {
		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>();
		retorn.add(new CodiDescBean("VIVIENDA", "VIVIENDA"));
		retorn.add(new CodiDescBean("TRASTERO", "TRASTERO"));
		retorn.add(new CodiDescBean("OFICINA", "OFICINA"));
		retorn.add(new CodiDescBean("LOCAL COMERCIAL", "LOCAL COMERCIAL"));
		retorn.add(new CodiDescBean("ALMACEN", "ALMACEN"));
		retorn.add(new CodiDescBean("NAVE", "NAVE"));
		retorn.add(new CodiDescBean("TERRENO", "TERRENO"));
		retorn.add(new CodiDescBean("OTROS", "OTROS"));
		return retorn;
	}
	
	/**
	 * Devuelve la lista de expresiones abreviadas del tipo de documento, y asigna los coeficientes de imposicion en un mapa.
	 * @param valorCompleto Mapa a rellenar con los valores (cod, porcentaje deduccion, importe deduccion, tipo de documento )
	 * @return
	 */
	public static Set<CodiDescBean> findExpresionAbrev(HashMap<String, String[]> valorCompleto) {
		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>();
		
		String[] listaexpr = new String[]{/**/
				/**/"AD0",	"Transmisión de acciones, derechos de suscripción obligaciones y titulos análogos.",	"Escala",	"",	"TO",
				/**/"AP0",	"Anotaciones preventivas",	"0,5",	"",	"AJ",
				/**/"AR0",	"Arrendamientos fincas rústicas",	"Escala",	"",	"TO",
				/**/"AU0",	"Arrendamientos fincas urbanas",	"Escala",	"",	"TO",
				/**/"CA0",	"Concesiones administrativas y otros conceptos",	"4",	"",	"TO",
				/**/"DC0",	"Documentos Notariales: documentos en los que se renuncia a la exención del IVA",	"1,5",	"",	"AJ",
				/**/"DG0",	"Derechos reales de garantía",	"1",	"",	"TO",
				/**/"DH0",	"Documentos Notariales, operaciones hipotecarias (prestamos, cancelaciones,subrogaciones ...)",	"1",	"",	"AJ",
				/**/"DN0",	"Documentos notariales: segregación",	"1",	"",	"AJ",
				/**/"DN1",	"Documentos notariales: agrupación",	"1",	"",	"AJ",
				/**/"DN2",	"Documentos notariales: declaración obra nueva",	"1",	"",	"AJ",
				/**/"DN3",	"Documentos notariales: división horizontal",	"1",	"",	"AJ",
				/**/"DN4",	"Documentos notariales: entregas sujetas al IVA",	"1",	"",	"AJ",
				/**/"DN5",	"Documentos notariales: otros documentos notariales",	"1",	"",	"AJ",
				/**/"DN6",	"Documentos notariales: viviendas protegidas",	"0,3",	"",	"AJ",
				/**/"EP0",	"Explotaciones agrarias prioritarias",	"3",	"",	"TO",
				/**/"FZ0",	"Fianza",	"1",	"",	"TO",
				/**/"GS1",	"Grandeza sin títulos: transmisión directa",	"",	"1613,25",	"AJ",
				/**/"GS2",	"Grandeza sin títulos: transmisión transversal",	"",	"4045,65",	"AJ",
				/**/"GS3",	"Grandeza sin títulos: rehabilitación y reconocimiento de títulos extranjeros",	"",	"9685,79",	"AJ",
				/**/"NS0",	"Documentos no sujetos",	"0",	"",	"TO",
				/**/"PN0",	"Pensiones",	"1",	"",	"TO",
				/**/"PO0",	"Préstamos y obligaciones",	"1",	"",	"TO",
				/**/"SO0",	"Sociedades no Anónimas: constitución",	"1",	"",	"SO",
				/**/"SO1",	"Sociedades no Anónimas: ampliación de capital",	"1",	"",	"SO",
				/**/"SO2",	"Sociedades no Anónimas: disminución de capital",	"1",	"",	"SO",
				/**/"SO3",	"Sociedades no Anónimas: disolución y/o liquidación",	"1",	"",	"SO",
				/**/"SO4",	"Sociedades no Anónimas: disolución sin liquidación",	"1",	"",	"SO",
				/**/"SO5",	"Sociedades no Anónimas: fusión",	"1",	"",	"SO",
				/**/"SO6",	"Sociedades no Anónimas: escisión",	"1",	"",	"SO",
				/**/"SO7",	"Sociedades no Anónimas: aportaciones de socios por pérdidas sociales",	"1",	"",	"SO",
				/**/"SO8",	"Sociedades no Anónimas: traslado a España de sede o domicilio social",	"1",	"",	"SO",
				/**/"SO9",	"Sociedades no anónimas: liquidación de sociedad",	"1",	"",	"SO",
				/**/"SX0",	"Sociedades Anónimas: constitución",	"1",	"",	"SO",
				/**/"SX1",	"Sociedades Anónimas: ampliación de capital",	"1",	"",	"SO",
				/**/"SX2",	"Sociedades Anónimas: disminución de capital",	"1",	"",	"SO",
				/**/"SX3",	"Sociedades Anónimas: disolución y/o liquidación",	"1",	"",	"SO",
				/**/"SX4",	"Sociedades Anónimas: disolución sin liquidación",	"1",	"",	"SO",
				/**/"SX5",	"Sociedades Anónimas: fusión",	"1",	"",	"SO",
				/**/"SX6",	"Sociedades Anónimas: escisión",	"1",	"",	"SO",
				/**/"SX7",	"Sociedades Anónimas: aportaciones de socios por pérdidas sociales",	"1",	"",	"SO",
				/**/"SX8",	"Sociedades Anónimas: traslado a España de sede o domicilio social",	"1",	"",	"SO",
				/**/"SX9",	"Sociedades Anónimas: disolución sin liquidación",	"1",	"",	"SO",
				/**/"TA0",	"Transmisión de acciones, participaciones sociales, derechos de suscripción y otros valores mobiliarios",	"4",	"",	"TO",
				/**/"TG1",	"Títulos con grandeza: transmisión directa",	"",	"2257,3",	"AJ",
				/**/"TG2",	"Títulos con grandeza: transmisión transversal",	"",	"5658,9",	"AJ",
				/**/"TG3",	"Títulos con grandeza: rehabilitación y reconocimiento de títulos extranjeros",	"",	"13568,87",	"AJ",
				/**/"TI0",	"Transmisión de inmuebles con negocio",	"3",	"",	"TO",
				/**/"TM0",	"Transmisión y derechos reales sobre muebles excepto automóviles y valores mobiliarios",	"4",	"",	"TO",
				/**/"TP0",	"Transmisión de viviendas calificadas como protegidas.",	"3",	"",	"TO",
				/**/"TR0",	"Transmisión y derechos reales sobre inmuebles rústicos",	"7",	"",	"TO",
				/**/"TS0",	"Transmisión con derecho a renunciar a la exención de iva, sin efectuar dicha renuncia",	"2",	"",	"TO",
				/**/"TS1",	"Títulos sin grandeza: transmisión directa",	"",	"644,05",	"AJ",
				/**/"TS2",	"Títulos sin grandeza: transmisión transversal",	"",	"1613,25",	"AJ",
				/**/"TS3",	"Títulos sin grandeza: rehabilitación y reconocimiento de títulos extranjeros",	"",	"3883,08",	"AJ",
				/**/"TU0",	"Transmisión y derechos reales sobre inmuebles urbanos: solares",	"7",	"",	"TO",
				/**/"TU1",	"Transmisión y derechos reales sobre inmuebles urbanos: viviendas",	"7",	"",	"TO",
				/**/"TU2",	"Transmisión y derechos reales sobre inmuebles urbanos: locales y otras edificaciones",	"7",	"",	"TO",
				/**/"TV0",	"Transmisión de valores y derechos de suscripción y de las letras del Tesoro para no residentes",	"6",	"",	"TO"

				};
		for (int i = 0; i < listaexpr.length; i += 5) {
			retorn.add(new CodiDescBean(listaexpr[i], listaexpr[i + 1]));
		}
		if (valorCompleto != null) {
			valorCompleto.clear();
			for (int i = 0; i < listaexpr.length; i += 5) {
				valorCompleto.put(listaexpr[i], new String[] { listaexpr[i + 2], listaexpr[i + 3], listaexpr[i + 4] });// mas...
			}
		}
		return retorn;
	}

	/**
	 * Devuelve la lista de parametros para la expresion abreviada seleccionada.
	 * @param tipoExprAbrev Tipo de expresion
	 * @return	tipo de bienes admitidos-Rural,Urbano,X <br/>
	 * 			tipoAbreviado-ignoran el tipo o base imponible si no vacio <br/>
	 *			tipoFijo-son Exentas o No Sujetas a partir del anyo adjunto <br/>
	 *			tipoTransmitente-obligatorio(defecto),permitido,prohibido-S,P,N <br/>
	 *			tipoRefcatastral-obligatorio,recomendado,opcional(defecto)-S,P,N )
	 */
	public static String[] findExpresionAbrevParams(String tipoExprAbrev) {
		String[] retorn = null;
		if (tipoExprAbrev == null)
			return retorn;
		String[] listaexpr = new String[]{/**/
				/**/"AD0",	"X",	"",	"E",	"S",	"",
				/**/"AP0",	"X",	"",	"",	"N",	"",
				/**/"AR0",	"R",	"",	"",	"S",	"P",
				/**/"AU0",	"U",	"",	"",	"S",	"S",
				/**/"CA0",	"", 	"",	"",	"S",	"",
				/**/"DC0",	"RU",	"",	"",	"S",	"P",
				/**/"DG0",	"", 	"",	"",	"S",	"",
				/**/"DH0",	"", 	"",	"",	"S",	"",
				/**/"DN0",	"RU",	"",	"",	"N",	"",
				/**/"DN1",	"RU",	"",	"",	"N",	"",
				/**/"DN2",	"U",	"",	"",	"N",	"",
				/**/"DN3",	"U",	"",	"",	"N",	"",
				/**/"DN4",	"RU",	"",	"",	"S",	"P",
				/**/"DN5",	"X",	"",	"",	"N",	"",
				/**/"DN6",	"U",	"",	"",	"",	"S",
				/**/"EP0",	"R",	"",	"",	"S",	"P",
				/**/"FZ0",	"X",	"",	"",	"S",	"",
				/**/"GS1",	"X",	"X",	"",	"P",	"",
				/**/"GS2",	"X",	"X",	"",	"P",	"",
				/**/"GS3",	"X",	"X",	"",	"P",	"",
				/**/"NS0",	"X",	"",	"NS",	"N",	"",
				/**/"PN0",	"X",	"",	"",	"S",	"",
				/**/"PO0",	"X",	"",	"E",	"S",	"",
				/**/"SO0",	"",	"",	"",	"P",	"",
				/**/"SO1",	"",	"",	"",	"P",	"",
				/**/"SO2",	"",	"",	"",	"P",	"",
				/**/"SO3",	"",	"",	"",	"P",	"",
				/**/"SO4",	"",	"",	"",	"P",	"",
				/**/"SO5",	"",	"",	"NS2009",	"P",	"",
				/**/"SO6",	"",	"",	"NS2009",	"P",	"",
				/**/"SO7",	"",	"",	"",	"P",	"",
				/**/"SO8",	"",	"",	"",	"P",	"",
				/**/"SO9",	"",	"",	"",	"P",	"",
				/**/"SX0",	"",	"",	"",	"P",	"",
				/**/"SX1",	"",	"",	"",	"P",	"",
				/**/"SX2",	"",	"",	"",	"P",	"",
				/**/"SX3",	"",	"",	"",	"P",	"",
				/**/"SX4",	"",	"",	"",	"P",	"",
				/**/"SX5",	"",	"",	"NS2009",	"P",	"",
				/**/"SX6",	"",	"",	"NS2009",	"P",	"",
				/**/"SX7",	"",	"",	"",	"P",	"",
				/**/"SX8",	"",	"",	"",	"P",	"",
				/**/"SX9",	"",	"",	"",	"P",	"",
				/**/"TA0",	"RU",	"",	"",	"S",	"",
				/**/"TG1",	"X",	"X",	"",	"P",	"",
				/**/"TG2",	"X",	"X",	"",	"P",	"",
				/**/"TG3",	"X",	"X",	"",	"P",	"",
				/**/"TI0",	"RU",	"",	"",	"S",	"S",
				/**/"TM0",	"X",	"",	"",	"S",	"",
				/**/"TP0",	"U",	"",	"",	"S",	"S",
				/**/"TR0",	"R",	"",	"",	"S",	"P",
				/**/"TS0",	"RU",	"",	"",	"S",	"P",
				/**/"TS1",	"X",	"X",	"",	"P",	"",
				/**/"TS2",	"X",	"X",	"",	"P",	"",
				/**/"TS3",	"X",	"X",	"",	"P",	"",
				/**/"TU0",	"U",	"",	"",	"S",	"S",
				/**/"TU1",	"U",	"",	"",	"S",	"S",
				/**/"TU2",	"U",	"",	"",	"S",	"S",
				/**/"TV0",	"",	"",	"",	"S",	""

				};
		for (int i = 0; i < listaexpr.length; i += 6) {
			if (tipoExprAbrev.equals(listaexpr[i])) {
				retorn = new String[6];
				retorn[0] = listaexpr[i];
				retorn[1] = listaexpr[i + 1];
				retorn[2] = listaexpr[i + 2];
				retorn[3] = listaexpr[i + 3];
				retorn[4] = listaexpr[i + 4];
				retorn[5] = listaexpr[i + 5];
				break;
			}
		}
		return retorn;
	}
	
	/**
	 * Devuelve la lista de fundamentos de exencion del tipo de documento, y asigna los coeficientes de imposicion en un mapa.
	 * @param tipoDocumento El tipo de documento de la expresión abreviada escogida (SO,TO,AJ)
	 * @return 
	 */
	public static Set<CodiDescBean> findFundaExencion(String tipoDocumento) {
		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>();
		String[] listaexpr = new String[]{/**/
				/**/"45.I.A.a",	"Estado y Administraciones Públicas",	"S",	"S",
				/**/"45.I.A.b",	"Entidades sin fines lucrativos",	"S",	"S",
				/**/"45.I.A.c",	"Cajas de Ahorro",	"S",	"S",
				/**/"45.I.A.d",	"Iglesias",	"S",	"S",
				/**/"45.I.A.e",	"Instituto de España y Reales Academias",	"S",	"S",
				/**/"45.I.A.f",	"Cruz Roja y ONCE",	"S",	"S",
				/**/"45.I.A.g",	"Obra Pia Santos Lugares",	"S",	"S",
				/**/"45.I.B.1",	"Tratados o Convenios Internacionales",	"S",	"S",
				/**/"45.I.B.2",	"Transm. Realiz. ejercicio retracto legal",	"S",	"S",
				/**/"45.I.B.3",	"Ap., Adj. Transm. Sdad Conyugal",	"S",	"S",
				/**/"45.I.B.4",	"Dinero y Act. Entrga Ent.  Financieras",	"S",	"S",
				/**/"45.I.B.5",	"Anticip. sin Int. Edo y Adm. Públicas",	"N",	"N",
				/**/"45.I.B.6",	"Concentraciones Parcelarias ",	"N",	"N",
				/**/"45.I.B.7",	"Transm. aport. a Juntas de Compensación",	"S",	"S",
				/**/"45.I.B.8",	"Garantias tutores en ejercicio cargo",	"N",	"N",
				/**/"45.I.B.9",	"Tr. Valores(Art. 108 Ley M. de Valores)",	"S",	"S",
				/**/"45.I.B.10",	"Oper. Soc.(Regimen Fiscal Ley 29/91)",	"S",	"N",
				/**/"45.I.B.11",	"Oper. Soc. Regul. Balances Autorizadas",	"N",	"N",
				/**/"45.I.B.12",	"V.P.O.",	"S",	"S",
				/**/"45.I.B.13",	"Tr. salvar inefic. ant. nulas y sujetos",	"N",	"N",
				/**/"45.I.B.14",	"Ceuta y Melilla",	"N",	"N",
				/**/"45.I.B.15",	"Depósitos Efectivo, Préstamos y similar.",	"S",	"S",
				/**/"45.I.B.16",	"Tr. Empresas Arrendamiento Financiero",	"S",	"S",
				/**/"45.I.B.17",	"Tr. Veh. Usados a Empr. de Compra Venta",	"N",	"N",
				/**/"45.I.B.18",	"Cancelaciones de Hipotecas",	"N",	"S",
				/**/"45.I.B.19",	"Ampliac. Capital Personas En Concurso",	"S",	"N",
				/**/"45.I.B.20",	"Soc. Inv Cap Var, F. Inv Fin., Col. Inm",	"N",	"N",
				/**/"45.I.B.21",	"Aportaciones a patrimonios protegidos",	"N",	"N",
				/**/"45.I.C.",	"Otras normas",	"",	"",
				/**/"",	"Otros Supuestos de Exención o Bonificación",	"",	""

				};
		boolean esSoPo = (tipoDocumento != null)
				&& (tipoDocumento.equalsIgnoreCase("SO") || tipoDocumento.equalsIgnoreCase("PO"));
		boolean esAj = (tipoDocumento != null) && (tipoDocumento.equalsIgnoreCase("AJ"));
		for (int i = 0; i < listaexpr.length; i += 4) {
			if ((listaexpr[i + 2].equals("N") && esSoPo) || (listaexpr[i + 3].equals("N") && esAj))
				continue;
			retorn.add(new CodiDescBean(listaexpr[i], listaexpr[i + 1]));
		}
		return retorn;
	}
	
	//nuevas listas
	//
	/**
	 * Devuelve la lista de países.
	 * @return
	 */
	public Set<CodiDescBean> findPaises() {
		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>();
		String[] listaexpr = new String[]{/**/
				"ES",	"ESPAÑA",
				"AL",	"ALEMANIA",
				"FR",	"FRANCIA",
				"PO",	"PORTUGAL",
				"RE",	"REINO UNIDO",
				"IT",	"ITALIA",
				"BE",	"BÉLGICA",
				"CH",	"CHILE",
				"AR",	"ARGENTINA",
				"ME",	"MEJICO",
				"VE",	"VENEZUELA",
				"EC",	"ECUADOR",
				"CO",	"COLOMBIA",
				"UR",	"URUGUAY",
				"CU",	"CUBA",
				"CZ",	"REPUBLICA CHECA",
				"PL",	"POLONIA",
				"RM",	"RUMANIA",
				"AU",	"AUSTRIA"};
		for (int i = 0; i < listaexpr.length; i += 2) {
			retorn.add(new CodiDescBean(listaexpr[i], listaexpr[i + 1]));
		}
		return retorn;
	}
	
	/**
	 * Devuelve la lista para deplegables de Si/No.
	 * @return
	 */
	public static Set<CodiDescBean> findSiNo() {

		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>();
		CodiDescBean elem = new CodiDescBean("S", "Sí");
		retorn.add(elem);
		elem = new CodiDescBean("N", "No");
		retorn.add(elem);
		return retorn;
	}


	/**
	 * Devuelve una lista de CodiDescBean a partir de los datos.
	 * @param lstCodigo Lista de codigos.
	 * @param lstValor Lista de valores.
	 * @return
	 */
	public static Set<CodiDescBean> rellenarCodiDesc(String[] lstCodigo, String[] lstValor) {
		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>();
		if (lstValor != null && lstValor.length != 0)
			for (int i = 0; i < lstCodigo.length && i < lstValor.length; i++) {
				CodiDescBean elem = new CodiDescBean(lstCodigo[i], lstValor[i]);
				retorn.add(elem);
			}
		else if (lstCodigo != null)
			for (int i = 0; i < lstCodigo.length; i++) {
				CodiDescBean elem = new CodiDescBean(lstCodigo[i], lstCodigo[i]);
				retorn.add(elem);
			}
		return retorn;
	}


	/**
	 * Determina si dos cadenas son identicas.
	 * @param campo1
	 * @param campo2
	 * @return
	 */
	public static boolean isCampoModif(String campo1, String campo2) {

		if ((campo1 == null && campo2 == null) || (campo1 != null && campo2 != null && campo1.equals(campo2)))
			return false;
		return true;
	}

	/**
	 * Informa una variable de un formulario con la descripcion correspondiente de un CodiDescBean.
	 * @param mapParams Mapa con las variables del formulario.
	 * @param campoCodigo Campo del mapa con el codigo
	 * @param campoDesc Campo donde introducir la descripcion.
	 * @param setDesplegable Set de CodiDescBeans correspondientes al campo a rellenar.
	 */
	public static void asignarCampoDesplegable(Map<String, Object> mapParams, String campoCodigo, String campoDesc,
			Set<CodiDescBean> setDesplegable) {
		String valor = (String) mapParams.get(campoCodigo);
		mapParams.put(campoDesc, "");
		if (!StringUtils.isBlank(valor) && setDesplegable != null) {
			Iterator<CodiDescBean> it = setDesplegable.iterator();
			while (it.hasNext()) {
				CodiDescBean obj = (CodiDescBean) it.next();
				if (valor.equals(obj.getCodi())) {
					mapParams.put(campoDesc, StringEscapeUtils.unescapeXml(obj.getDesc()));
					break;
				}
			}
		}
	}
	
	/**
	 * Informa una variable de un formulario con la descripcion correspondiente de un CodiDescBean.
	 * @param mapParams Mapa con las variables del formulario.
	 * @param campoCodigo Campo del mapa con el codigo
	 * @param campoDesc Campo donde introducir la descripcion.
	 * @param lstCodiDesc Array de CodiDescBeans correspondientes al campo a rellenar.
	 */
	public static void asignarCampoDesplegable(Map<String, Object> mapParams, String campoCodigo, String campoDesc,
			CodiDescBean[] lstCodiDesc) {
		String valor = (String) mapParams.get(campoCodigo);
		mapParams.put(campoDesc, "");
		if (!StringUtils.isBlank(valor) && lstCodiDesc != null) {
			for (int i = 0; i < lstCodiDesc.length; i++) {
				if (valor.equals(lstCodiDesc[i].getCodi())) {
					mapParams.put(campoDesc, StringEscapeUtils.unescapeXml(lstCodiDesc[i].getDesc()));
					break;
				}
			}
		}
	}

	/**
	 * 
	 * Informa una variable de un formulario con la descripcion correspondiente de un CodiDescBean.
	 * @param mapParams Mapa con las variables del formulario.
	 * @param campoCodigo Campo del mapa con el codigo
	 * @param campoDesc Campo donde introducir la descripcion.
	 * @param lstCodigo Lista de codigos posibles para el campo
	 * @param lstDesc Lista de descripciones posibles para el campo
	 */
	public static void asignarCampoDesplegable(Map<String, Object> mapParams, String campoCodigo, String campoDesc,
			String[] lstCodigo, String[] lstDesc) {
		String valor = (String) mapParams.get(campoCodigo);
		mapParams.put(campoDesc, "");
		if (!StringUtils.isBlank(valor) && lstCodigo != null && lstDesc != null) {
			for (int i = 0; i < lstCodigo.length && i < lstDesc.length; i++) {
				if (valor.equals(lstCodigo[i])) {
					mapParams.put(campoDesc, StringEscapeUtils.unescapeXml(lstDesc[i]));
					break;
				}
			}
		}
		return;
	}


	/**
	 * Determina si hay acceso al servicio de WebService segun el archivo de configuracion.
	 * @return 
	 */
	public static boolean isAccesoWebservices() {
		boolean isAccesoWebservice = false;
		try {
			isAccesoWebservice = true;
		} catch (Exception e) {
			isAccesoWebservice = false;
			com.stpa.ws.server.util.Logger.error("UtilsComunes.isAccesoWebservices:" + e.getMessage(),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}
		return isAccesoWebservice;
	}
	
	/**
	 * Determina si hay acceso al servicio de WebService de la DGT segun el archivo de configuracion.
	 * @return
	 */
	public static boolean isAccesoWebservicesDGT() {
		boolean isAccesoWebservice = false;
		try {
			isAccesoWebservice = true;
		} catch (Exception e) {
			isAccesoWebservice = false;
			com.stpa.ws.server.util.Logger.error("UtilsComunes.isAccesoWebservices:" + e.getMessage(),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}
		return isAccesoWebservice;
	}
	/**
	 * Devuelve el tipo de entorno (pruebas/real) segun el archivo de configuracion.
	 * @return
	 */
	public static String getEntornoBDD() {
		Preferencias pref = new Preferencias();
		String accesoWebservice = null;
		try {
			//accesoWebservice = PropertiesUtils.getValorConfiguracion(Modelo046Constantes.PROPERTIES_STPAWS,"wslanzador.entornoBDD");
			accesoWebservice = pref.getM_wslanzadorentornoBDD();
		} catch (Exception e) {
			accesoWebservice = null;
			com.stpa.ws.server.util.Logger.error("UtilsComunes.getEntornoBDD:" + e.getMessage(),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}
		return accesoWebservice;
	}


	/**
	 * Convierte una cadena en un valor int.
	 * @param s Cadena a convertir. Todos los decimales y signos de puntuacion se eliminan, y si comienza por 'Ox' se considera como hexadecimal. 
	 * @return Valor de la cadena, por defecto 0 si no es decimal o hexadecimal.
	 */
	public static int parseInt(String s) {
		try {
			Integer num = null;
			// ERROR: parseInt NO admite decimales
			if (s.indexOf(".") != -1)
				s = s.substring(0, s.indexOf("."));
			if (s.indexOf(",") != -1)
				s = s.substring(0, s.indexOf(","));
			if (s.startsWith("0x") || s.startsWith("0X"))
				num = Integer.parseInt(s, 16);
			else
				num = Integer.parseInt(s);
			if (num != null)
				return num.intValue();
		} catch (NumberFormatException e) {
			
		}
		return 0;
	}

	/**
	 * Redondea un valor numerico a 2 decimales.
	 * @param campo Campo con el formato ###,### o ###.##
	 * @return Campo con el formato ####,##
	 */
	public static String redondear(String campo) {
		String numero = campo;
		numero = ("" + numero).replace(',', '.');
		char separator = '.';
		if (NumberUtil.getDoubleAsString((double) 1.1).indexOf(",") != -1)
			separator = ',';
		final int dec = 2;
		double num = Math.round(parseNum(numero) * Math.pow(10, dec)) / Math.pow(10, dec);
		String ret = NumberUtil.getDoubleAsString(num);
		if (ret.indexOf(separator) == -1)
			ret = ret + separator + "0";
		if (ret.indexOf(separator) != -1)
			while (ret.indexOf(separator) > ret.length() - dec - 1)
			ret = ret + "0";
		ret = ret.replace(separator, ',');
		return ret;
	}

	/**
	 * Convierte una cadena en un double. NOTA: solo devuelve datos validos para cadenas con el separador '.'; el
	 * separador ',' no se admite
	 * 
	 * @param s Cadena a informar
	 * @return
	 */
	public static double parseNum(String s) {
		try {
			Double num = null;
			num = //Double.parseDouble(s);
				NumberUtil.getDoubleFromBaseString(s);
			if (num != null)
				return num.doubleValue();
		} catch (NumberFormatException e) {
			;
		}
		return 0;
	}

	/**
	 * Valida que los datos esten en el formato de Java; ver parseNum().
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isNumericDecimal(String text) {
		return (Pattern.matches("-?[0-9]+", text.trim()) || (text.indexOf(".") != -1 && Pattern.matches(
				"-?[0-9]+.[0-9]+", text.trim())));
	}

	
	public static boolean validarFormatoMatricula(String matricula) {
//		O9999X 		5-1
//		O0974AX 	5-2
//		OU0002X 	2-4-1
//		OU0000AX 	2-4-2
//		0123BCX 	4-3
//		M000001 	1-6
//		C1234BBB 	1-4-3
//		ZA12345VE 	2-5-2
//		S00123R 	1-5-1
//		E0123CDS 	1-4-3
//		1839FBS		
		if (matricula == null || matricula.length() < 5 || matricula.length() > 10)
			return false;
		return true;
	}

	
	/**
	 * Determina el vehiculo tiene la matricula informada.
	 * @param matricula Matricula, preferentemente en mayusculas
	 * @param vehiculo InformeVehiculo
	 * @return
	 */
	public static boolean isMatriculaValida(String matricula, InformeVehiculo vehiculo) {
		if (vehiculo == null)
			return false;
		return isMatriculaValida(matricula,vehiculo,null,null);
	}

	/**
	 * Determina si el vehiculo tiene la matricula, NIF del titular y fecha de matriculacion informados.
	 * @param matricula Matricula, preferentemente en mayusculas
	 * @param vehiculo InformeVehiculo
	 * @param nifcif NIF del Titular
	 * @param fecha Fecha de la primera matriculacion, en formato dd/MM/yyyy
	 * @return FALSE, si no existe el vehiculo.
	 */
	public static boolean isMatriculaValida(String matricula, InformeVehiculo vehiculo, String nifcif, String fecha) {
		InformeVehiculo resultado = vehiculo;
		boolean valid = true;
		if (resultado == null)
			return false;
		valid = valid
				&& ((resultado.getMatriculacion() != null && resultado.getMatriculacion().getMatricula().equals(
						matricula)) || ((resultado.getMatriculacion() == null || ValidatorUtils.isEmpty(resultado
						.getMatriculacion().getMatricula()))
						&& resultado.getMatriculatemporal() != null && resultado.getMatriculatemporal().equals(
						matricula)));

		if (!ValidatorUtils.isEmpty(nifcif))
			valid = valid
					&& (resultado.getTitular() == null || (resultado.getTitular().getDni() != null && resultado
							.getTitular().getDni().toUpperCase().equals(nifcif.toUpperCase())));
		//hay que considerar que solo informamos dia,mes,anyo
		if (!ValidatorUtils.isEmpty(fecha)) {
			String fecha1 = DateUtil.getFechaAsFormatString(fecha, "dd/MM/yyyy", "dd/MM/yyyy");
			String fecha2 = fecha1;
			if (resultado.getFechaprimeramatriculacion() != null)
				fecha2 = DateUtil.getDateAsFormatString(resultado.getFechaprimeramatriculacion().getTime(),
						"dd/MM/yyyy");
			valid = valid && (fecha1 != null && fecha2 != null && fecha1.compareTo(fecha2) == 0);
		}
	
		return valid;
	}
	

	public static String encodeUTF8(String p_data) throws RemoteException {
		return p_data;
	}

	
	/**
	 * Convierte una cadena en un Number.
	 * @param p_cadena Cadena en formato ###.###.###,###
	 * @return double
	 */
	public static Number convertirStringNumber(String p_cadena) {

		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator(',');
		simbolos.setGroupingSeparator('.');
		DecimalFormat df = new DecimalFormat(cadenaFormat, simbolos);

		Number numero = new Double(0);
		if (!ValidatorUtils.isEmpty(p_cadena)) {

			try {
				numero = (Number) df.parse(p_cadena);
			} catch (ParseException e) {
				try {
					numero = new Double(p_cadena);
				} catch (Exception e1) {
					numero = new Double(0);
				}

			} catch (Exception e1) {
				numero = new Double(0);
			}

		}

		return numero.doubleValue();

	}
	
	/**
	 * Formatea un Number en String en formato ###.###.###,## .
	 * @param p_number Number a formatear, no null.
	 * @return
	 */
	public static String convertirNumberString(Number p_number) {

		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator(',');
		simbolos.setGroupingSeparator('.');
		DecimalFormat df = new DecimalFormat(cadenaFormat, simbolos);

		return df.format(p_number);

	}
	
	/**
	 * Convierte a double y suma los valores de un array de Strings.
	 * @param p_array Lista de valores, con formato ###.###.###,## ;  por defecto, valor 0.
	 * @return String con formato ###.###.###,##
	 */
	public static String sumarStrings(String[] p_array) {

		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator(',');
		simbolos.setGroupingSeparator('.');
		DecimalFormat df = new DecimalFormat(cadenaFormat, simbolos);

		Double tot = new Double(0);
		for (String string : p_array) {

			if (!ValidatorUtils.isEmpty(string)) {
				Number numero = new Double(0);
				try {
					numero = (Number) df.parse(string);
				} catch (ParseException e) {
					try {
						numero = new Double(string);
					} catch (Exception e1) {
						numero = new Double(0);
					}

				} catch (Exception e1) {
					numero = new Double(0);
				}
			
				tot += numero.doubleValue();
			}
		}

		return df.format(tot);
	}
	
	/**
	 * Convierte una cadena en un Double.
	 * @param elem String con formato ###.###.###,##
	 * @return
	 */
	public static Number getDouble(String elem) {
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator(',');
		simbolos.setGroupingSeparator('.');
		DecimalFormat df = new DecimalFormat(cadenaFormat, simbolos);
		if (!ValidatorUtils.isEmpty(elem)) {
			Number numero = new Double(0);
			try {
				numero = (Number) df.parse(elem);
			} catch (ParseException e) {
				try {
					numero = new Double(elem);
				} catch (Exception e1) {
					numero = new Double(0);
				}

			} catch (Exception e1) {
				numero = new Double(0);
			}
		
			return numero;
		}
		return new Double(0);
	}
	
	
	/**
	 * Devuelve el primer codigo correspondiente al valor de un CodiDescBean.
	 * @param list Set de CodiDescBean
	 * @param desc Descripcion a encontrar
	 * @return
	 */
	public static String recuperarCodigo(Set<CodiDescBean> list, String desc) {
		if (list != null) {
			Iterator<CodiDescBean> it = list.iterator();
			while (it.hasNext()) {
				CodiDescBean elem = it.next();
				if (desc.equals(elem.getDesc())) {
					return elem.getCodi();
				}
			}
		}
		return null;
	}
	
	public static String getXMLFormulario(IModeloFormBean formulario, String modelo) throws StpawsException {
		GenericModeloFormBean elem = (GenericModeloFormBean) formulario;
		if (ValidatorUtils.isEmpty(elem.getCodigoVerificacion())) {
			String doc_just_nif = elem.getTipoDocumento() 
					+ elem.getNumerodeserie() + elem.getNifSujetoPasivo();
			VerificaDocumentosEJB ejb = new VerificaDocumentosEJB();
			String codVerif = "";
			try {
				codVerif = ejb.recuperarCodigoVerificacion(doc_just_nif, false);
			} catch (Exception e) {
				com.stpa.ws.server.util.Logger.error("Error en el codigo de verificación:" + doc_just_nif,e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				throw new StpawsException("Error al recuperar el codigo de verificación",e);
			}
			elem.setCodigoVerificacion(codVerif);
		}

		Map<String, Object> mapParams= new HashMap<String, Object>();
		mapParams.put(Constantes.MODELO, modelo);

		if (modelo == null) {
			modelo = formulario.getClass().getName();
			modelo = modelo.substring(modelo.length() - 3, modelo.length());
		}
				
		//Recuperamos los maps con campos para cada uno de los nodos del xml.
		Map<String, Map<String, Object>> map = new HashMap<String, Map<String,Object>>();		
		map.put(Constantes.MODELO, mapParams);
				
		Map<String, Object> mapParamsPago = elem.getCamposXmlPagos();		
		if (mapParamsPago != null) {
			map.put(Constantes.PAGO, mapParamsPago);
		}

		Map<String, Object> mapParamsPresentacion = elem.getCamposXmlPresentacion();
		if (mapParamsPresentacion != null) {
			map.put(Constantes.PRESENTACION, mapParamsPresentacion);
		}
		
		String xml = "";
		xml = UtilsComunes.rellenarXMLDatos(modelo, map, null, null);
		((GenericModeloFormBean) formulario).setXml(xml);
		((GenericModeloFormBean) formulario).setXml(xml);
				
		return xml;
	}
	
	
	public static String getDestinoVolver() throws StpawsException{
		String destinoVolver = null;
		if (destinoVolver == null) {
			String keyAccion = "canal.inicio";
			try {
				destinoVolver = PropertiesUtils.getValorConfiguracion("",keyAccion);
				if (destinoVolver != null) {
					if (destinoVolver.indexOf("/") != 0) {
						destinoVolver = "/" + destinoVolver;
					}
				}
			} catch (Exception e) {			
				com.stpa.ws.server.util.Logger.error("UtilsComunes.getDestinoVolver",e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				throw new StpawsException("Error al recuperar el destino",e);
			}
		}
		return destinoVolver;
	}
		
	
	public static void eliminaParametrosXML(Map<String, Object> mapaXML) {
		
		String[] paramNoXml = Constantes.PARAM_NO_XML;
		for (int i = 0; i < paramNoXml.length; i++) {
			mapaXML.remove(paramNoXml[i]);
		}
	}
	
	/**
	 * Lista de tarifas validas segun el anyo de devengo. Tabla con 6 columnas, 16 filas por fecha.
	 * Fecha de validez inicial, orden, cantidad minima/HASTA, couta integra/VARCA, resto max liquidable, tipo aplicable/PORC
	 */
	private static String[] tablaTarifas = new String[]{
		/**/"01/01/1990",	"1",	"0",	"0",	"649995",	"7,65",
		/**/"01/01/1990",	"2",	"649995",	"49725",	"649994",	"8,50",
		/**/"01/01/1990",	"3",	"1299989",	"104974",	"649995",	"9,35",
		/**/"01/01/1990",	"4",	"1949984",	"165749",	"649994",	"10,20",
		/**/"01/01/1990",	"5",	"2599978",	"232048",	"649995",	"11,05",
		/**/"01/01/1990",	"6",	"3249973",	"303872",	"649995",	"11,90",
		/**/"01/01/1990",	"7",	"3899968",	"381222",	"649994",	"12,75",
		/**/"01/01/1990",	"8",	"4549962",	"464096",	"649995",	"13,60",
		/**/"01/01/1990",	"9",	"5199957",	"552495",	"649994",	"14,45",
		/**/"01/01/1990",	"10",	"5849951",	"646420",	"649995",	"15,30",
		/**/"01/01/1990",	"11",	"6499946",	"745869",	"3249973",	"16,15",
		/**/"01/01/1990",	"12",	"9749919",	"1270740",	"3249973",	"18,70",
		/**/"01/01/1990",	"13",	"12999892",	"1878484",	"6499946",	"21,25",
		/**/"01/01/1990",	"14",	"19499838",	"3259723",	"12999892",	"25,50",
		/**/"01/01/1990",	"15",	"32499730",	"6574883",	"32499729",	"29,75",
		/**/"01/01/1990",	"16",	"64999459",	"16243365",	"999999999999999",	"34,00",
		/**/"01/01/1991",	"1",	"0",	"0",	"682494",	"7,65",
		/**/"01/01/1991",	"2",	"682494",	"52211",	"682495",	"8,50",
		/**/"01/01/1991",	"3",	"1364989",	"110223",	"682494",	"9,35",
		/**/"01/01/1991",	"4",	"2047483",	"174036",	"682494",	"10,20",
		/**/"01/01/1991",	"5",	"2729977",	"243650",	"682495",	"11,05",
		/**/"01/01/1991",	"6",	"3412472",	"319066",	"682494",	"11,90",
		/**/"01/01/1991",	"7",	"4094966",	"400283",	"682494",	"12,75",
		/**/"01/01/1991",	"8",	"4777460",	"487301",	"682495",	"13,60",
		/**/"01/01/1991",	"9",	"5459955",	"580120",	"682494",	"14,45",
		/**/"01/01/1991",	"10",	"6142449",	"678740",	"682494",	"15,30",
		/**/"01/01/1991",	"11",	"6824943",	"783162",	"3412472",	"16,15",
		/**/"01/01/1991",	"12",	"10237415",	"1334276",	"3412471",	"18,70",
		/**/"01/01/1991",	"13",	"13649886",	"1972409",	"6824944",	"21,25",
		/**/"01/01/1991",	"14",	"20474830",	"3422709",	"13649886",	"25,50",
		/**/"01/01/1991",	"15",	"34124716",	"6903430",	"34124716",	"29,75",
		/**/"01/01/1991",	"16",	"68249432",	"17055533",	"999999999999999",	"34,00",
		/**/"01/01/1992",	"1",	"0",	"0",	"717007",	"7,65",
		/**/"01/01/1992",	"2",	"717007",	"54851",	"717008",	"8,50",
		/**/"01/01/1992",	"3",	"1434015",	"115797",	"717007",	"9,35",
		/**/"01/01/1992",	"4",	"2151022",	"182837",	"717008",	"10,20",
		/**/"01/01/1992",	"5",	"2868030",	"255972",	"717007",	"11,05",
		/**/"01/01/1992",	"6",	"3585037",	"335201",	"717008",	"11,90",
		/**/"01/01/1992",	"7",	"4302045",	"420525",	"717007",	"12,75",
		/**/"01/01/1992",	"8",	"5019052",	"511943",	"717008",	"13,60",
		/**/"01/01/1992",	"9",	"5736060",	"609456",	"717007",	"14,45",
		/**/"01/01/1992",	"10",	"6453067",	"713064",	"717007",	"15,30",
		/**/"01/01/1992",	"11",	"7170074",	"822766",	"3582033",	"16,15",
		/**/"01/01/1992",	"12",	"10752107",	"1401265",	"3582032",	"18,70",
		/**/"01/01/1992",	"13",	"14334139",	"2071105",	"7167069",	"21,25",
		/**/"01/01/1992",	"14",	"21501208",	"3594106",	"14334139",	"25,50",
		/**/"01/01/1992",	"15",	"35835347",	"7249312",	"35835346",	"29,75",
		/**/"01/01/1992",	"16",	"71670693",	"17910328",	"999999999999999",	"34,00",
		/**/"01/01/1995",	"1",	"0",	"0",	"742250",	"7,65",
		/**/"01/01/1995",	"2",	"742250",	"56782",	"742250",	"8,50",
		/**/"01/01/1995",	"3",	"1484500",	"119874",	"742250",	"9,35",
		/**/"01/01/1995",	"4",	"2226750",	"189274",	"742250",	"10,20",
		/**/"01/01/1995",	"5",	"2969000",	"264983",	"742250",	"11,05",
		/**/"01/01/1995",	"6",	"3711250",	"347002",	"742250",	"11,90",
		/**/"01/01/1995",	"7",	"4453500",	"435330",	"742250",	"12,75",
		/**/"01/01/1995",	"8",	"5195750",	"529966",	"742250",	"13,60",
		/**/"01/01/1995",	"9",	"5938000",	"630912",	"742250",	"14,45",
		/**/"01/01/1995",	"10",	"6680250",	"738168",	"742249",	"15,30",
		/**/"01/01/1995",	"11",	"7422499",	"851732",	"3708245",	"16,15",
		/**/"01/01/1995",	"12",	"11130744",	"1450614",	"3708245",	"18,70",
		/**/"01/01/1995",	"13",	"14838989",	"2144055",	"7418292",	"21,25",
		/**/"01/01/1995",	"14",	"22257281",	"3720442",	"14832378",	"25,50",
		/**/"01/01/1995",	"15",	"37089659",	"7502699",	"37089659",	"29,75",
		/**/"01/01/1995",	"16",	"74179318",	"18536872",	"999999999999999",	"34,00",
		/**/"01/01/1996",	"1",	"0",	"0",	"769295",	"7,65",
		/**/"01/01/1996",	"2",	"769295",	"58851",	"769296",	"8,50",
		/**/"01/01/1996",	"3",	"1538591",	"124241",	"769295",	"9,35",
		/**/"01/01/1996",	"4",	"2307886",	"196170",	"769296",	"10,20",
		/**/"01/01/1996",	"5",	"3077182",	"274638",	"769295",	"11,05",
		/**/"01/01/1996",	"6",	"3846477",	"359646",	"769296",	"11,90",
		/**/"01/01/1996",	"7",	"4615773",	"451192",	"769295",	"12,75",
		/**/"01/01/1996",	"8",	"5385068",	"549277",	"769296",	"13,60",
		/**/"01/01/1996",	"9",	"6154364",	"653901",	"769296",	"14,45",
		/**/"01/01/1996",	"10",	"6923659",	"765064",	"769296",	"15,30",
		/**/"01/01/1996",	"11",	"7692955",	"882767",	"3840467",	"16,15",
		/**/"01/01/1996",	"12",	"11533422",	"1503002",	"3840468",	"18,70",
		/**/"01/01/1996",	"13",	"15373890",	"2221169",	"7680934",	"21,25",
		/**/"01/01/1996",	"14",	"23054824",	"3853368",	"15349849",	"25,50",
		/**/"01/01/1996",	"15",	"38404673",	"7767580",	"38404674",	"29,75",
		/**/"01/01/1996",	"16",	"76809347",	"19192970",	"999999999999999",	"34,00",
		/**/"01/01/1999",	"1",	"0",	"0",	"783119",	"7,65",
		/**/"01/01/1999",	"2",	"783119",	"59909",	"783119",	"8,50",
		/**/"01/01/1999",	"3",	"1566238",	"126474",	"783118",	"9,35",
		/**/"01/01/1999",	"4",	"2349356",	"199695",	"783119",	"10,20",
		/**/"01/01/1999",	"5",	"3132475",	"279573",	"783119",	"11,05",
		/**/"01/01/1999",	"6",	"3915594",	"366108",	"783119",	"11,90",
		/**/"01/01/1999",	"7",	"4698713",	"459299",	"783118",	"12,75",
		/**/"01/01/1999",	"8",	"5481831",	"559147",	"783119",	"13,60",
		/**/"01/01/1999",	"9",	"6264950",	"665651",	"783119",	"14,45",
		/**/"01/01/1999",	"10",	"7048069",	"778812",	"783119",	"15,30",
		/**/"01/01/1999",	"11",	"7831188",	"898629",	"3909583",	"16,15",
		/**/"01/01/1999",	"12",	"11740771",	"1530027",	"3909584",	"18,70",
		/**/"01/01/1999",	"13",	"15650355",	"2261119",	"7819168",	"21,25",
		/**/"01/01/1999",	"14",	"23469523",	"3922692",	"15626314",	"25,50",
		/**/"01/01/1999",	"15",	"39095837",	"7907402",	"39095838",	"29,75",
		/**/"01/01/1999",	"16",	"78191675",	"19538414",	"999999999999999",	"34,00",
		/**/"01/01/2000",	"1",	"0",	"0",	"799346",	"7,65",
		/**/"01/01/2000",	"2",	"799346",	"61150",	"798745",	"8,50",
		/**/"01/01/2000",	"3",	"1598091",	"129043",	"798745",	"9,35",
		/**/"01/01/2000",	"4",	"2396836",	"203726",	"798745",	"10,20",
		/**/"01/01/2000",	"5",	"3195581",	"285198",	"798745",	"11,05",
		/**/"01/01/2000",	"6",	"3994326",	"373459",	"798746",	"11,90",
		/**/"01/01/2000",	"7",	"4793072",	"468510",	"798745",	"12,75",
		/**/"01/01/2000",	"8",	"5591817",	"570350",	"798745",	"13,60",
		/**/"01/01/2000",	"9",	"6390562",	"678979",	"798745",	"14,45",
		/**/"01/01/2000",	"10",	"7189307",	"794398",	"798745",	"15,30",
		/**/"01/01/2000",	"11",	"7988052",	"916606",	"3987715",	"16,15",
		/**/"01/01/2000",	"12",	"11975767",	"1560622",	"3987716",	"18,70",
		/**/"01/01/2000",	"13",	"15963483",	"2306325",	"7975430",	"21,25",
		/**/"01/01/2000",	"14",	"23938913",	"4001104",	"15938841",	"25,50",
		/**/"01/01/2000",	"15",	"39877754",	"8065508",	"39877754",	"29,75",
		/**/"01/01/2000",	"16",	"79755508",	"19929140",	"999999999999999",	"34,00"};
	
	/**
	 * Recupera el bloque con datos de tarifas para un anyo de devengo.
	 * @param anyoDev String de 4 cifras.
	 * @return
	 */
	public static String[] getBloqueTarifas(String anyoDev) {
		int anyoinit = 0;
		int anyofin = 0;
		if (ValidatorUtils.isEmpty(anyoDev))
			anyoDev = DateUtil.getTodayAsString("yyyy");
		if (anyoDev.indexOf("/") != -1) {
			anyoDev = anyoDev.substring(anyoDev.lastIndexOf("/")+1, anyoDev.length());
		}
		int anyodevengo = NumberUtil.getIntegerFromString(anyoDev);
		int indexIni = 0;
		int indexFin = 0;
		for (int i = 0; i < tablaTarifas.length; i += 6) {
			String anyo = tablaTarifas[i];
			anyo = anyo.substring(anyo.length() - 4, anyo.length());
			int anyonum = NumberUtil.getIntegerFromString(anyo);
			if (anyonum == anyodevengo) {
				indexIni = i;
				anyoinit = anyonum;
				anyofin = anyonum;
				break;
			} else if (anyonum < anyodevengo && anyonum > anyoinit) {
				anyoinit = anyonum;
				indexIni = i;
			} else if (anyonum > anyodevengo & anyofin == 0) {
				anyofin = anyonum;
			}
			if (anyoinit != 0 && anyofin != 0) {
				break;
			}
		}
		// punto de inicio,busca punto fin
		if (anyoinit == 0 && anyofin != 0) {
			indexIni = 0;
		}
		if (anyoinit != 0 && anyofin == 0) {
			;
		}
		String anyotemp = tablaTarifas[indexIni].substring(tablaTarifas[indexIni].length() - 4, tablaTarifas[indexIni]
				.length());
		for (int i = indexIni; i < tablaTarifas.length; i += 6) {
			String anyo = tablaTarifas[i];
			anyo = anyo.substring(anyo.length() - 4, anyo.length());
			if (!anyotemp.equals(anyo)) {
				indexFin = i;
				break;
			}
		}
		if (indexFin == 0)
			indexFin = tablaTarifas.length;
		String[] bloqueTarifas = new String[indexFin - indexIni];
		int j = 0;
		for (int i = indexIni; i < indexFin; i++)
			bloqueTarifas[j++] = tablaTarifas[i];
		return bloqueTarifas;
	}
	
	/**
	 * Recupera el limite, cuota y porcentaje de tarifas. EJEMPLO: {@code
	 * [hasta,cuota,porc]=getTarifas("500,67","1999",bloqueTarifas); }
	 * 
	 * @param cantidad Importe con decimales
	 * @param anyoDev Anyo de tarifacion.
	 * @param bloqueTarifas Bloque de tarifas ya recuperado (mejora el rendimiento)
	 * @return String[] con valores para campos HASTA1/HASTA2, VARCA/VARCB, PORC1/PORC2
	 */
	public static String[] getTarifas(String cantidad, String anyoDev, String[] bloqueTarifas) {
		if (bloqueTarifas == null || bloqueTarifas.length < 6)
			return null;
		double importe = 0;
		if (NumberUtil.isDouble(cantidad))
			importe = NumberUtil.getDoubleFromBaseString(cantidad);
		if (ValidatorUtils.isEmpty(anyoDev))
			anyoDev = DateUtil.getTodayAsString("yyyy");
		if (anyoDev.indexOf("/") != -1) {
			anyoDev = anyoDev.substring(anyoDev.lastIndexOf("/")+1, anyoDev.length());
		}
		importe = NumberUtil.getDoubleFromBaseString(cantidad);
		// bloque de 6 columnas
		double hastanum = 0;
		int index = -1;
		for (int i = 0; i < bloqueTarifas.length; i += 6) {
			String hasta = bloqueTarifas[i + 2];
			hastanum = NumberUtil.getIntegerFromString(hasta) / 100;
			if (hastanum > importe) {
				index = i;
				break;
			}
		}
		if (index < 0)
			index = bloqueTarifas.length;
		index -= 6;
		if (index < 0)
			index = 0;
		String hasta = bloqueTarifas[index + 2];
		String cuota = bloqueTarifas[index + 3];
		String porc = bloqueTarifas[index + 5];
		// poner decimales
		while (hasta.length() < 3) {
			hasta = "0" + hasta;
		}
		while (cuota.length() < 3) {
			cuota = "0" + cuota;
		}
		hasta = hasta.substring(0, hasta.length() - 2) + "," + hasta.substring(hasta.length() - 2, hasta.length());
		cuota = cuota.substring(0, cuota.length() - 2) + "," + cuota.substring(cuota.length() - 2, cuota.length());
		com.stpa.ws.server.util.Logger.debug("limites para año " + anyoDev + " y " + cantidad + " :" + hasta + "_" + cuota + "_" + porc,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return new String[] { hasta, cuota, porc };
	}
	
	/**
	 * Recupera el limite, cuota y porcentaje de tarifas. Recupera su propio bloque de tarifas.
	 * @param cantidad Importe con decimales
	 * @param anyoDev Anyo de tarifacion.
	 * @return String[] con valores para campos HASTA1/HASTA2, VARCA/VARCB, PORC1/PORC2
	 */
	public static String[] getTarifas(String cantidad, String anyoDev) {
		if (ValidatorUtils.isEmpty(anyoDev))
			anyoDev = DateUtil.getTodayAsString("yyyy");
		if (anyoDev.indexOf("/") != -1) {
			anyoDev = anyoDev.substring(anyoDev.lastIndexOf("/")+1, anyoDev.length());
		}
		String[] bloqueTarifas = getBloqueTarifas(anyoDev);
		return getTarifas(cantidad, anyoDev, bloqueTarifas);
	}
	
	/**
	 * Recupera el limite, cuota y porcentaje de tarifas para el anyo actual. Recupera su propio bloque de tarifas.
	 * @param cantidad Importe con decimales
	 * @return String[] con valores para campos HASTA1/HASTA2, VARCA/VARCB, PORC1/PORC2
	 */
	public static String[] getTarifas(String cantidad) {
		String anyoDev = DateUtil.getTodayAsString("yyyy");
		String[] bloqueTarifas = getBloqueTarifas(anyoDev);
		return getTarifas(cantidad, anyoDev, bloqueTarifas);
	}
	
	/**
	 * Devuelve el coeficiente multiplicador segun el parentesco y patrimonio del sujeto pasivo.
	 * @param grup Grupo del donante, ver findListaParentesco()
	 * @param patrimonio Importe del patrimonio ya existente.
	 * @return coeficiente en formate #,####
	 */
	public static String getCoeficienteMultiplicador(String grup, String patrimonio) {
		String coef = "";
		if (grup == null)
			grup = "";
		grup = grup.toUpperCase();
		double patrim = 0;

		if (patrimonio != null && NumberUtil.isBaseDouble(patrimonio))
			patrim = UtilsComunes.parseNum(patrimonio) * 100;
		else if (patrimonio != null) {
			patrim = UtilsComunes.parseNum(patrimonio.replace(",", "."));
			patrim = patrim * 100;
		}
		
		if (grup.equals("I") || grup.equals("II")) {
			if (patrim <= 40267811)
				coef = "1,0000";
			else {
				if (patrim <= 200738043)
					coef = "1,0500";
				else {
					if (patrim <= 402077098)
						coef = "1,1000";
					else {
						if (patrim > 402077098)
							coef = "1,2000";
					}
				}
			}
		}
		if (grup.equals("III")) {
			if (patrim <= 40267811)
				coef = "1,5882";
			else {
				if (patrim <= 200738043)
					coef = "1,6676";
				else {
					if (patrim <= 402077098)
						coef = "1,7471";
					else {
						if (patrim > 402077098)
							coef = "1,9059";
					}
				}
			}
		}
		if (grup.equals("IV")) {
			if (patrim <= 40267811)
				coef = "2,0000";
			else {
				if (patrim <= 200738043)
					coef = "2,1000";
				else {
					if (patrim <= 402077098)
						coef = "2,2000";
					else {
						if (patrim > 402077098)
							coef = "2,4000";
					}
				}
			}
		}
		return coef;
		
	}
	
	/**
	 * Devuelve el importe a gravar para expresiones del tipo ESCALA
	 * @param cantidad
	 * @return
	 */
	public static double getImporteExprAbrev(String cantidad) {
		if (!NumberUtil.isDouble(cantidad))
			return 0;
		double importe = NumberUtil.getDoubleFromString(cantidad) * 100;
		double impuesto = 0;
		if (importe == 0.0)
			return impuesto;
		double[] escalaValor = new double[] {
			000,3005,9,
			3006,6010,18,
			6011,12020,39,
			12021,24040,78,
			24041,48081,168,
			48082,96162,337,
			96163,192324,721,
			192325,384648,1442,
			384649,769295,3077 };
		for (int i = 0; i < escalaValor.length; i += 3) {
			if (importe <= escalaValor[i + 1]) {
				impuesto = escalaValor[i + 2] / 100;
				break;
			}
		}
		if (importe > 769295) {
			// imponer 0,024040 por 6.01 euros
			double coef = 2.4040;
			impuesto = importe - 769295;
			impuesto /= 601;
			impuesto = Math.ceil(impuesto);
			impuesto = 769295 + impuesto * coef;
		}
		com.stpa.ws.server.util.Logger.debug("IMPUESTO ES"+impuesto,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return impuesto / 100;
	}
	
	/**
	 * Devuelve la lista de municipios para una provincia.
	 * @param codigoProvincia Codigo de la provincia segun la lista de provincias.
	 * @return
	 */
	public static Set<CodiDescBean> findMunicipios(String codigoProvincia) {
		//  para CEUTA y MELILLA no devuelve municipios; tocamos BBDD o tocamos metodo para dar
		// siempre un resultado?
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("P", "P");
		params.put("provincia", codigoProvincia);
		Object[] obj = find(ModeloTributarioDAO.MUNICIPIO, params);
		Set<CodiDescBean> toReturn = new TreeSet<CodiDescBean>();
		if (obj != null) {
			for (int i = 0; i < obj.length; i++) {
				String[] municipio = (String[]) obj[i];
				if (municipio.length > 1) {
					CodiDescBean elem = new CodiDescBean(municipio[0], municipio[1]);
					toReturn.add(elem);
				}
			}
		}
		return toReturn;
	}
	
	/**
	 * Devuelve la lista de provincias espanolas.
	 * @return
	 */
	public static Set<CodiDescBean> findProvincias() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("P", "P");
		// por especificaciones del ws devuelve 1 LISTA DE CODIGOS Y 1 LISTA DE
		// DESCRIPCIONES
		Object[] obj = find(ModeloTributarioDAO.PROVINCIA, params);
		Set<CodiDescBean> retorn = new TreeSet<CodiDescBean>();
		if (obj != null && obj.length > 1) {
			retorn = rellenarCodiDesc((String[]) obj[0], (String[]) obj[1]);
		}
		return retorn;
	}
	
	/**
	 * Devuelve los datos de listas de desplegables en los formularios. Utilizado por las otras funciones de busqueda de UtilsComunes.
	 * @see ModeloTributarioDAO
	 * @param method Nombre del dato, constante en ModeloTributarioDAO
	 * @param params Mapa de parametros.
	 * @return Object[]{String[] codigos,String[] nombres,...}
	 */
	public static Object[] find(String method, HashMap<String, String> params)  {
		params.put("nombreProceso", method);
		try {
			ModeloTributarioDAO dao = new ModeloTributarioDAO();
			return dao.find(params);
		} catch (StpawsException e) {
			com.stpa.ws.server.util.Logger.error("UtilsComunes.find",e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			return null;
		}
	}
}
