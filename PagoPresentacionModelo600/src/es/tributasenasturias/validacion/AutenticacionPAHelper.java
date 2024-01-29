package es.tributasenasturias.validacion;

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

import es.tributasenasturias.Exceptions.ValidacionException;
import es.tributasenasturias.Exceptions.XMLDOMDocumentException;
import es.tributasenasturias.pagopresentacionmodelo600utils.Preferencias;
import es.tributasenasturias.webservice.pagopresentacion.log.ILoggable;
import es.tributasenasturias.webservice.pagopresentacion.log.LogHelper;

/**
 * 
 * @author crubencvs
 * Clase que encapsular� la llamada al servicio de autenticaci�n del Principado de Asturias.
 */
public class AutenticacionPAHelper implements ILoggable{

	private Preferencias pref = new Preferencias();
	private LogHelper log;
	
	protected AutenticacionPAHelper() {		
		pref.CompruebaFicheroPreferencias();
	}
	
	private String getIdentificadorRespuesta (String xml) throws XMLDOMDocumentException 
	{
		Document doc =null; 
		NodeList nodos=null;
		Node nodoId=null;
		String identificador =null;
		doc = XMLDOMUtils.parseXml (doc,xml);
		//Recuperamos el nodo "item" que contiene el identificador. Probamos primero con CIF.
		nodos = XMLDOMUtils.getAllNodes(doc,"item[contains(name,'CIF')]");
		//Si no existe, probamos con NIF
		if (nodos.getLength()!=0) 
		{
			Node nodo = nodos.item(0);//S�lo se espera uno.
			nodoId = XMLDOMUtils.getFirstChildNode(nodo, "value");
			identificador = XMLDOMUtils.getNodeText(nodoId);
		}
		else //NIF
		{
			nodos = XMLDOMUtils.getAllNodes(doc,"item[contains(name,'NIF')]");
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
		return identificador;
		
	}
	/**
	 * Construye la petici�n en funci�n del esqueleto XML que tendremos en fichero.
	 * @return String que representa el XML de la petici�n a enviar al servicio de autenticaci�n.
	 */
	private String construyePeticion(String certificado)
	{
		String xml=null;
		try
		{
			pref.CargarPreferencias();					
			
			String nombreFichero = pref.getXmlAutorizacion();
						
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
					Node nodo=nodos.item(0); //S�lo esperamos un "item" que contenga un "name" "IP".
					Node nodoIpValor = XMLDOMUtils.getFirstChildNode(nodo, "value");
					XMLDOMUtils.setNodeText(doc,nodoIpValor, pref.getIpAutorizacion());
					
				}
				xml = XMLDOMUtils.getXMLText(doc);
				
			}
		}
		
		catch (XMLDOMDocumentException ex)
		{			
			log.error("Error de XML:" + ex.getMessage());
			xml=null;
		}
		catch (Exception ex)
		{
			xml=null;
		}
		
		return xml;
	}
	/**
	 * Realiza la llamada a la funci�n "login" del servicio de autenticaci�n.
	 * @param certificado
	 * @return
	 */
	public String login (String certificado) throws ValidacionException
	{
		InputStreamReader in = null;
		OutputStream out=null;
        BufferedReader buf = null;
		try
		{									 					
			String peti = construyePeticion(certificado);																
			pref.CargarPreferencias();							
			String dirServicio = pref.getEndPointAutenticacion(); 										
			
			log.debug("LOGIN::Certificado:" +certificado);
			log.debug("endopint autentica:" +dirServicio);
			log.debug("Peticion construida para certificado:" +peti);			 
			
			if (dirServicio!=null && !dirServicio.equals(""))
			{
					URL url= new URL(dirServicio);
					HttpURLConnection con = (HttpURLConnection) url.openConnection(); 
					//se trae el contenido del fichero a pasar como petici�n.
			        //Se recuperan los bytes que conforman la petici�n.
					byte []petiB = peti.getBytes("UTF-8");
					//Se monta la conexi�n
			        con.setRequestProperty( "Content-Length", String.valueOf( petiB.length ) );
			        con.setRequestProperty("Content-Type","text/xml; charset=utf-8");
			        con.setRequestMethod( "POST" );
			        con.setDoOutput(true); //Se usa la conexi�n para salida
			        con.setDoInput(true); // Se usa paraa entrada.
			        //Se env�an los datos a la URL remota.
			        out = con.getOutputStream();
			        out.write(petiB); //Enviamos la petici�n.
			        out.close(); 
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
			        //Recuperamos el CIF o NIF.
			        String id=getIdentificadorRespuesta(xmlResp.toString());
			        return id;
			}
		}
		catch (MalformedURLException ex)
		{
			
			log.error("Error al verificar la autenticaci�n.Excepti�n de servicio." + ex.getMessage());
			throw new ValidacionException ("Error al verificar la autenticaci�n. "+ ex.getMessage(), ex);
		}		
		catch (IOException ex)
		{			
			log.error("Error al verificar la autenticaci�n.Excepti�n de servicio." + ex.getMessage());
			throw new ValidacionException ("Error al verificar la autenticaci�n. " + ex.getMessage(),ex);
		}
		catch (Exception ex)
		{			
			log.error("Error al verificar la autenticaci�n.Excepti�n de servicio." + ex.getMessage());
			throw new ValidacionException ("Error al verificar la autenticaci�n. "+ex.getMessage(),ex);
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
		return null;
	}
	@Override
	public void setLogger(LogHelper log)
	{
		this.log = log;
	}
	@Override 
	public LogHelper getLogger()
	{
		return log;
	}
}
