package es.tributasenasturias.webservices.Certificados.validacion.Certificado;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.tributasenasturias.webservices.Certificados.Exceptions.PreferenciasException;
import es.tributasenasturias.webservices.Certificados.Exceptions.ValidacionException;
import es.tributasenasturias.webservices.Certificados.Exceptions.XMLDOMDocumentException;
import es.tributasenasturias.webservices.Certificados.soap.handler.ClientLogHandler;
import es.tributasenasturias.webservices.Certificados.utils.XMLDOMUtils;
import es.tributasenasturias.webservices.Certificados.utils.Log.GenericAppLogger;
import es.tributasenasturias.webservices.Certificados.utils.Log.TributasLogger;
import es.tributasenasturias.webservices.Certificados.utils.Preferencias.Preferencias;

/**
 * 
 * @author crubencvs
 * Clase que encapsulará la llamada al servicio de autenticación del Principado de Asturias.
 */
public class AutenticacionPAHelper {

	/**
	 * Recupera el CIF de la persona del XML de respuesta. 
	 * @param xml Xml de respuesta del servicio de autenticación.
	 * @return
	 */
	private String getCIF (String xml) 
	{
		Document doc =null; 
		NodeList nodos=null;
		Node nodoId=null;
		String identificador =null;
		try{
			doc = XMLDOMUtils.parseXml (doc,xml);
			//Recuperamos el nodo "item" que contiene el identificador. Probamos primero con CIF.
			nodos = XMLDOMUtils.getAllNodes(doc,"item[contains(name,'CIF')]");
			//Si no existe, probamos con NIF
			if (nodos.getLength()!=0) 
			{
				Node nodo = nodos.item(0);//Sólo se espera uno.
				nodoId = XMLDOMUtils.getFirstChildNode(nodo, "value");
				identificador = XMLDOMUtils.getNodeText(nodoId);
			}
		}
		catch (XMLDOMDocumentException ex)
		{
			GenericAppLogger log = new TributasLogger();
			log.error("No se ha podido recuperar el identificador (CIF) de la persona por un error en XML:" +  ex.getMessage());
			identificador=null;
		}
		return identificador;
		
	}
	/**
	 * Comprueba si en la respuesta de autenticación desde el principado se nos ha devuelto un error en
	 * /loginResponse/return/exception/id.
	 * @param xml Cadena con el texto del xml a comprobar.
	 * @return Código del error, o null si ninguno.
	 */
	private String getError (String xml) 
	{
		Document doc =null; 
		NodeList nodos=null;
		String error=null;
		try{
			doc = XMLDOMUtils.parseXml (doc,xml);
			//Recuperamos el nodo "id" que contiene error
			nodos = XMLDOMUtils.getAllNodes(doc,"return/exception/id");
			if (nodos.getLength()!=0) 
			{
				error=XMLDOMUtils.getNodeText(nodos.item(0));
			}
		}
		catch (XMLDOMDocumentException ex)
		{
			GenericAppLogger log = new TributasLogger();
			log.error("No se ha podido recuperar el identificador de error de la respuesta:" +  ex.getMessage());
			error=null;
		}
		return error;
		
	}
	/**
	 * Recupera el NIF de la persona del XML de respuesta.
	 * @param xml Cadena que contiene el texto del xml.
	 * @return
	 */
	private String getNIF (String xml) 
	{
		Document doc =null; 
		NodeList nodos=null;
		Node nodoId=null;
		String identificador =null;
		try{
			doc = XMLDOMUtils.parseXml (doc,xml);
			nodos = XMLDOMUtils.getAllNodes(doc,"item[contains(name,'NIF/NIE')]");
				if (nodos.getLength()!=0)
				{
					Node nodo = nodos.item(0);
					nodoId = XMLDOMUtils.getFirstChildNode(nodo, "value");
					identificador = XMLDOMUtils.getNodeText(nodoId);
				}
				else
				{
					identificador=null;
				}
		}
		catch (XMLDOMDocumentException ex)
		{
			GenericAppLogger log = new TributasLogger();
			log.error("No se ha podido recuperar el identificador (NIF/NIE) de la persona por un error en XML:" +  ex.getMessage());
			identificador=null;
		}
		return identificador;
		
	}
	/**
	 * Recupera la razón social del certificado
	 * @param xml
	 * @return
	 */
	private String getRazonSocial (String xml) 
	{
		Document doc =null; 
		NodeList nodos=null;
		Node nodoId=null;
		String razon =null;
		try{
			doc = XMLDOMUtils.parseXml (doc,xml);
			nodos = XMLDOMUtils.getAllNodes(doc,"item[contains(name,'RAZON SOCIAL')]");
				if (nodos.getLength()!=0)
				{
					Node nodo = nodos.item(0);
					nodoId = XMLDOMUtils.getFirstChildNode(nodo, "value");
					razon = XMLDOMUtils.getNodeText(nodoId);
				}
				else
				{
					razon=null;
				}
		}
		catch (XMLDOMDocumentException ex)
		{
			GenericAppLogger log = new TributasLogger();
			log.error("No se ha podido recuperar la razón social por un error en XML:" +  ex.getMessage());
			razon=null;
		}
		return razon;
		
	}
	/**
	 * Construye la petición en función del esqueleto XML que tendremos en fichero.
	 * @return String que representa el XML de la petición a enviar al servicio de autenticación.
	 */
	private String construyePeticion(String certificado)
	{
		String xml=null;
		try
		{
			Preferencias _pr = Preferencias.getPreferencias();
			String nombreFichero = _pr.getXmlAutorizacion();
			if (nombreFichero!=null && !nombreFichero.equals(""))
			{
				File fXml = new File (nombreFichero);
				Document doc =null; 
				doc = XMLDOMUtils.parseXML(doc,fXml);
				NodeList nodos = XMLDOMUtils.getAllNodes(doc,"certificate");
				Node nodoCer=null;
				if (nodos.getLength()!=0)
				{
					nodoCer = nodos.item(0);
					XMLDOMUtils.setNodeText(doc,nodoCer, certificado);
				}
				//Ahora se sustituye el valor de la IP.
				nodos = XMLDOMUtils.getAllNodes(doc,"item[contains(name,'IP')]"); 
				//Dentro de los objetos "item", queremos aquel que tenga un hijo "Name" con valor "IP".
				if (nodos.getLength()!=0)
				{
					Node nodo=nodos.item(0); //Sólo esperamos un "item" que contenga un "name" "IP".
					Node nodoIpValor = XMLDOMUtils.getFirstChildNode(nodo, "value");
					XMLDOMUtils.setNodeText(doc,nodoIpValor, _pr.getIpAutorizacion());
					
				}
				xml = XMLDOMUtils.getXMLText(doc);
				
			}
		}
		catch (PreferenciasException ex)
		{
			GenericAppLogger log = new TributasLogger();
			log.error("No se ha podido contruir la petición por un error en la Preferencias:" + ex.getError() + "-" +ex.getMessage());
			xml=null;
		}
		catch (XMLDOMDocumentException ex)
		{
			GenericAppLogger log = new TributasLogger();
			log.error("No se ha podido contruir la petición por un error en XML:" + ex.getMessage());
			xml=null;
		}
		
		
		return xml;
	}
	/**
	 * Realiza la llamada a la función "login" del servicio de autenticación.
	 * @param certificado
	 * @return
	 */
	public TInfoCertificado login (String certificado) throws ValidacionException
	{
		InputStreamReader in = null;
		OutputStream out=null;
        BufferedReader buf = null;
        TInfoCertificado info = new TInfoCertificado();
		try
		{
			
			String peti = construyePeticion(certificado);
			Preferencias _pr = Preferencias.getPreferencias();
			String dirServicio = _pr.getEndPointAutenticacion();
			if (dirServicio!=null && !dirServicio.equals(""))
			{
					URL url= new URL(dirServicio);
					HttpURLConnection con = (HttpURLConnection) url.openConnection(); 
					ClientLogHandler logger = new ClientLogHandler();
					//se trae el contenido del fichero a pasar como petición.
			        //Se recuperan los bytes que conforman la petición.
					byte []petiB = peti.getBytes("UTF-8");
					//Se monta la conexión
			        con.setRequestProperty( "Content-Length", String.valueOf( petiB.length ) );
			        con.setRequestProperty("Content-Type","text/xml; charset=utf-8");
			        con.setRequestMethod( "POST" );
			        con.setDoOutput(true); //Se usa la conexión para salida
			        con.setDoInput(true); // Se usa paraa entrada.
			        logger.doLog (peti, "Envío");
			        //Se envían los datos a la URL remota.
			        out = con.getOutputStream();
			        out.write(petiB); //Enviamos la petición.
			        out.close();
			        if (con.getResponseCode()==200 /*&& con.getResponseMessage()=="OK"*/)
			        {
				        //Leemos lo que nos ha devuelto.
				        in = new InputStreamReader(con.getInputStream());
				        buf = new BufferedReader (in);
				        StringBuilder xmlResp = new StringBuilder();
				        String linea;
				        while ((linea=buf.readLine())!=null)
				        {
				        	xmlResp.append(linea);
				        }
				        buf.close();
				      //Recuperamos datos del certificado.
				        String xmlString = xmlResp.toString();
				        logger.doLog (xmlString, "Recepción");
				        String error =getError(xmlString);
				        if (error==null)
				        {
				        	info.setCif(getCIF(xmlString));
				        	info.setNifNie(getNIF(xmlString));
				        	info.setRazonSocial(getRazonSocial(xmlString));
				        }
				        else
				        {
				        	GenericAppLogger log = new TributasLogger();
				        	log.error("El servicio de autenticación ha devuelto un error de id:" + error);
				        	info.setCif(null);
				        	info.setNifNie(null);
				        	info.setRazonSocial(null);
				        }
			        }
			        else //No es 200, OK no consideramos que ha terminado bien.
			        {
			        	GenericAppLogger log = new TributasLogger();
			        	log.error("Al conectar con el servicio de autenticación se ha devuelto un resultado: " 
			        			+ con.getResponseCode() + "-" + con.getResponseMessage());
			        }
			        return info;
			}
			else
			{
				throw new PreferenciasException ("Dirección de autenticación vacía.",null);
			}
		}
		catch (MalformedURLException ex)
		{
			throw new ValidacionException ("Error al verificar la autenticación debido a un problema en la URL.",ex);
		}
		catch (PreferenciasException ex)
		{
			throw new ValidacionException ("Error al verificar la autenticación debido a un problema con Preferencias.",ex);
		}
		catch (IOException ex)
		{
			throw new ValidacionException ("Error al verificar la autenticación debido a un problema de ES.",ex);
		}
		finally
		{
			if  (buf!=null)
			{
				try
				{
					buf.close();
				}
				catch (IOException ex)
				{
					
				}
			}
			if (in!=null)
			{
				try
				{
					in.close();
				}
				catch (IOException ex)
				{
					
				}
			}
			if (out!=null)
			{
				try
				{
					out.close();
				}
				catch (IOException ex)
				{
					
				}
			}
		}
	}
	
	/**
	 * @param port
	 */
	/*private void cambiarEndPoint (SecurityServerRemoteInterfacePortType port)
	{
		try
		{
			Preferencias _pr = Preferencias.getPreferencias();
			if (!_pr.getEndPointAutenticacion().equals(""))
			{
				((Stub) port)._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, _pr.getEndPointAutenticacion());
			}
		}
		catch (Exception ex) // Por defecto, se queda como está en el wsdl
		{
			TributasLogger log = new TributasLogger();
			log.error ("Error al modificar el endpoint de autenticación :"+ ex.getMessage());
		}
	}
	*//**
	 * Construye los parámetros fijos que se enviarán en la petición.
	 * Habrá que incluirlo en preferencias para que sean más fáciles de cambiar.
	 * @return un array de tipo Es_Princast_Framework_Modules_Security_Corp_Ws_WSParam con los parámetros.
	 *//*
	private Es_Princast_Framework_Modules_Security_Corp_Ws_WSParam[] buildParams()
	{
		ArrayList<Es_Princast_Framework_Modules_Security_Corp_Ws_WSParam> arrParam = new ArrayList<Es_Princast_Framework_Modules_Security_Corp_Ws_WSParam>();
		
		Es_Princast_Framework_Modules_Security_Corp_Ws_WSParam param=new Es_Princast_Framework_Modules_Security_Corp_Ws_WSParam();
		//Primer parámetro. LOGIN LEVEL=2. Pasar después a Preferencias.
		param.setName("LOGIN LEVEL");
		param.setValue("2");
		arrParam.add(param);
		//Segundo parámetrol.CHANNEL=PORTAL
		param = new Es_Princast_Framework_Modules_Security_Corp_Ws_WSParam();
		param.setName("CHANNEL");
		param.setValue("PORTAL");
		arrParam.add(param);
		//Tercer parámetro. ACTOR TYPE=CITIZEN
		param = new Es_Princast_Framework_Modules_Security_Corp_Ws_WSParam();
		param.setName("ACTOR TYPE");
		param.setValue("CITIZEN");
		arrParam.add(param);
		//Cuarto parámetro. ID APP= TRIBUTAS
		param = new Es_Princast_Framework_Modules_Security_Corp_Ws_WSParam();
		param.setName("ID APP");
		param.setValue("TRIBUTAS");
		arrParam.add(param);
		//Quinto parámetro. IP
		param = new Es_Princast_Framework_Modules_Security_Corp_Ws_WSParam();
		param.setName("IP");
		param.setValue("10.112.10.25");
		arrParam.add(param);
		//Array de salida
		Es_Princast_Framework_Modules_Security_Corp_Ws_WSParam[]arr=new Es_Princast_Framework_Modules_Security_Corp_Ws_WSParam[arrParam.size()];
		arr = arrParam.toArray(arr);
		TributasLogger log = new TributasLogger();
		log.info("Arr tiene " + arr[0] +" elm");
		return arr;
	}
	*//**
	 * Realiza la llamada a la función "login" del servicio de autenticación.
	 * @param certificado
	 * @return
	 *//*
	public String login (String certificado)
	{
		TributasLogger log = new TributasLogger();
		String resultado=null;
		try
		{
			SecurityServerRemoteInterfacePortType port = null;
			SecurityServerRemoteInterface serv = new SecurityServerRemoteInterface_Impl();
			
			if (serv.getPorts().hasNext())
			{
				port = serv.getSecurityServerRemoteInterfacePort();
				cambiarEndPoint (port); 
				//((Stub) port)._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, "http:/citrix.epst.pa:4444/WSAutenticacionPA/ProxyServices/PXAutenticacionPA");
				//Construimos los parámetros.
				Es_Princast_Framework_Modules_Security_Corp_Ws_WSParam[] params = buildParams();
				//Preparamos la entrada
				Es_Princast_Framework_Modules_Security_Corp_Ws_LoginRequest request = new Es_Princast_Framework_Modules_Security_Corp_Ws_LoginRequest();
				request.setCertificate(certificado.getBytes());
				request.setParams(params);
				//Recogemos la entrada
				Es_Princast_Framework_Modules_Security_Corp_Ws_LoginResponse resp = 
					port.login(request);
				//Array de resultados donde se encontrará el CIF o el NIF.
				Es_Princast_Framework_Modules_Security_Corp_Ws_WSParam[] paramResp = resp.getPrincipals();
				for (Es_Princast_Framework_Modules_Security_Corp_Ws_WSParam par : paramResp)
				{
					if (par.getName()=="CIF" && !par.getValue().equals("") && par.getValue()!=null)
					{
						resultado=par.getValue();
						break;
					}
					else if (par.getName()=="NIF" && !par.getValue().equals("") && par.getValue()!=null)
					{
						resultado = par.getValue();
						break;
					}
				}
			}
			else
			{
				resultado=null;
			}
		}
		catch (ServiceException ex)
		{
			log.error("Error al construir la llamada al servicio de autenticación.Exceptión de servicio." + ex.getMessage());
		}
		catch (RemoteException ex)
		{
			log.error("Error al construir la llamada al servicio de autenticación.Excepción en llamada remota." + ex.getMessage());
		}
		return resultado;
	}
*/}
