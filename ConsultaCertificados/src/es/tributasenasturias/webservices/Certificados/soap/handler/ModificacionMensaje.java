package es.tributasenasturias.webservices.Certificados.soap.handler;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.tributasenasturias.webservices.Certificados.utils.SOAPFaultHelper;
import es.tributasenasturias.webservices.Certificados.utils.XMLDOMUtils;
import es.tributasenasturias.webservices.Certificados.utils.Log.GenericAppLogger;
import es.tributasenasturias.webservices.Certificados.utils.Log.TributasLogger;
import es.tributasenasturias.webservices.Certificados.validacion.PermisoServicio.TInfoPermisoServicio;

public class ModificacionMensaje implements SOAPHandler<SOAPMessageContext>{


	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	@Override
	public void close(MessageContext context) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean handleFault(SOAPMessageContext context) {
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (salida)
		{
			return true; //Propagamos
		}
		return false;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context){
		//Sólo para entrada.
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		GenericAppLogger log = new TributasLogger();
		if (!salida.booleanValue())
		{
			log.info(">>>Se gestiona la entrada si fuese de ayuntamiento.");
			try
			{
				SOAPMessage msg = context.getMessage();
				String tipo="";
				tipo = getValorTipo (msg.getSOAPBody());
				//Se comprueba si es ayuntamiento
				if (esAyuntamiento (msg.getSOAPHeader()))
				{
					//Si lo es, se ha de averiguar si existe o no un nodo tipo.
					// Si existe debe ser el tipo indicado (¿parametrizar?). 
					// Si no, es un error.
					
				if (tipo!=null) //Existe
					{
						if (!"33090".equals(tipo))
						{
							log.error("La petición es de ayuntamiento, pero el valor de tipo es diferente del esperado.");
							throw SOAPFaultHelper.createSOAPFaultException("Error al procesar los parámetros de entrada del servicio. Error en uno o más parámetros.");
						}
					}
					else
					{
						//Recuperamos del contexto la variable de datos de permiso de servicio.
			             TInfoPermisoServicio info = (TInfoPermisoServicio) context.get("InfoPermisoServicio");
			             context.remove("InfoPermisoServicio");
			             tipo=info.getEspecifico();
						// Si no existe, se ha de insertar el tipo que se ha recuperado de la 
						// consulta del permiso.
						Node nTipo = putTipo (msg.getSOAPBody(),tipo);
						if (nTipo==null)
						{
							log.error("No se ha podido crear el nodo tipo, que no existía en el mensaje original");
							throw SOAPFaultHelper.createSOAPFaultException("Error al procesar los parámetros pasados al servicio web");
						}
					}
				}
				else
				{
					// No es ayuntamiento, no puede estar vacío el tipo. Se pasará el que tenga.
					if (tipo==null)
					{
						log.error("La petición no parece que sea de ayuntamiento, pero no hay tipo.");
						throw SOAPFaultHelper.createSOAPFaultException("Error al procesar los parámetros de entrada del servicio. Faltan uno o más parámetros obligatorios.");
					}
				}
			}
			catch (SOAPFaultException ex)
			{
				throw ex;
			}
			catch (Exception ex)
			{
					log.error("Error al modificar el mensaje: "+ ex.getMessage());
					throw SOAPFaultHelper.createSOAPFaultException("Error al procesar los parámetros pasados al servicio web");
			}
		}
		else
		{
			//Modificamos el mensaje de salida para añadir un nodo SERVICIOWEB y eliminar el que 
			// debería aparecer por defecto, que tiene un namespace que los clientes no entienden.
			try {mensajeSalida(context.getMessage());
			}
			catch (Exception ex)
			{
				log.error("Error al modificar el mensaje de salida para eliminar el namespace: "+ ex.getMessage());
				throw SOAPFaultHelper.createSOAPFaultException("Error al crear el mensaje de salida.");
			}
		}
		return true;
	}
	/**
	 * Comprueba si la petición se envía desde un ayuntamiento. Esto se hace mirando el valor 
	 * de un nodo "esAyuntamiento" en la cabecera del mensaje. Este nodo lo inserta el Proxy.
	 * @param header
	 * @return
	 */
	private boolean esAyuntamiento (SOAPHeader header)
	{
		boolean res=false;
		//Recuperamos de cabecera el dato que nos dice si es o no ayuntamiento, si existe.
		NodeList nodos = header.getElementsByTagNameNS("*", "esAyuntamiento");
		if (nodos.getLength()>0)
		{
			String val =  XMLDOMUtils.getNodeText(nodos.item(0));
			if (val.equalsIgnoreCase("S"))
			{
				res=true;
			}
			else
			{
				res=false;
			}
		}
				
		return res;
	}
	/**
	 * Recupera el valor del nodo "Tipo" de la petición.
	 * @param body Objeto cuerpo del mensaje
	 * @return valor del tipo, o null si no existe el nodo
	 */
	private String getValorTipo (SOAPBody body) 
	{
		String tipo="";
		//Recuperamos el nodo "tipo"
		NodeList nodos = XMLDOMUtils.getAllNodes(body.getOwnerDocument(),"PETICION/CERTIFICADO/TIPO");
		if (nodos.getLength()>0)
		{
			//Valor
			tipo = XMLDOMUtils.getNodeText(nodos.item(0));
		}
		else
		{
			tipo=null;
		}
		return tipo;
	}
	/**
	 * Inserta un valor de tipo.
	 * @param body Objeto cuerpo del mensaje entrante
	 * @param valor valor que se dará al tipo.
	 * @return Referencia al nuevo nodo creado, o nulo si no ha podido.
	 */
	private Node putTipo (SOAPBody body, String valor)
	{
		
		Node nodoTipo=null;
		try
		{
		NodeList nodos = XMLDOMUtils.getAllNodes(body.getOwnerDocument(),"PETICION/CERTIFICADO");
		if (nodos!=null && nodos.getLength()>0) //Si estamos ejecutando esto, debería ser cierto.
		{
			Node certificado = nodos.item(0);
			nodoTipo = certificado.getOwnerDocument().createElement("TIPO");
			certificado.appendChild(nodoTipo);
			XMLDOMUtils.setNodeText(body.getOwnerDocument(), nodoTipo, valor);
		}
		}
		catch (Exception ex)
		{
			GenericAppLogger log = new TributasLogger();
			log.error("Error al crear el nodo Tipo:" + ex.getMessage());
			log.trace(ex.getStackTrace());
		}
		return nodoTipo;
	}
	
	private void mensajeSalida (SOAPMessage mensaje) throws SOAPException
	{
		Node body = mensaje.getSOAPBody();
		//Generamos un nodo "SERVICIOWEB".
		Node servicioWeb= body.getOwnerDocument().createElement("SERVICIOWEB");
		Node antiguo;
		Node temp;
		//Pasamos el contenido de SERVICIOWEB con namespaces al SERVICIOWEB sin namespaces.
		for (int i=0;i<body.getChildNodes().getLength();i++)
		{
			//Localizamos el "SERVICIOWEB" con nombre.
			if ("SERVICIOWEB".equals(body.getChildNodes().item(i).getLocalName()))
			{
				antiguo = body.getChildNodes().item(i);
				//Se recorren al revés, del último al primero, ya que en cada iteración eliminará un nodo.
				for (int n=0;n<antiguo.getChildNodes().getLength();n++)
				{
					//Clonamos el nodo, y lo copiamos al nuevo servicioWeb
					temp = antiguo.getChildNodes().item(n).cloneNode(true);
					servicioWeb.appendChild(temp);
				}
				//Copiamos sus atributos.
				for (int n=0;n<antiguo.getAttributes().getLength();n++)
				{
					temp = antiguo.getAttributes().item(n).cloneNode(true);
					servicioWeb.getAttributes().setNamedItem(temp);
				}
				//Eliminamos el nodo.
				body.removeChild(antiguo);
			}
		}
		//Si el nuevo nodo de SERVICIOWEB tiene hijos, se pinta
		if (servicioWeb.getChildNodes().getLength()>0)
		{
			body.appendChild(servicioWeb);
		}
			
	}
}
