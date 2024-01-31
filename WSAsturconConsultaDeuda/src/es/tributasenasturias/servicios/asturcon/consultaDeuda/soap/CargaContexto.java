package es.tributasenasturias.servicios.asturcon.consultaDeuda.soap;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
//import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;

import es.tributasenasturias.servicios.asturcon.consultaDeuda.Utils.Constantes;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.Utils.Utils;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.preferencias.Preferencias;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.preferencias.PreferenciasException;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.preferencias.PreferenciasFactory;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.xml.XMLDOMDocumentException;
import es.tributasenasturias.servicios.asturcon.consultaDeuda.xml.XMLDOMUtils;
import es.tributasenasturias.utils.log.LogFactory;
import es.tributasenasturias.utils.log.Logger;


/**
 * Carga datos en el contexto de servicio que serán utilizados
 * en el resto del servicio.
 * Estos objetos serán el log de aplicación, las preferencias, y el id de sesión.
 * 
 * @author crubencvs
 *
 */
public class CargaContexto implements SOAPHandler<SOAPMessageContext> {

	/**
	 * Extraemos el nodo del mensaje XML.
	 * @param context contexto del mensaje SOAP
	 * @return {@link Node} con el nodo del mensaje.
	 */
	private Node extraerMensaje(SOAPMessageContext context) 
	{
		
		return context.getMessage().getSOAPPart();
	}
	/**
	 * Extrae el ID del mensaje de entrada.
	 * Esta función no tiene más utilidad que ayudar en los logs, si se comprueba que no se necesita,
	 * se puede eliminar.
	 * @param mensaje
	 * @return
	 */
	private String getID (Node mensaje)
	{
		String id="";
		if (mensaje!=null)
		{
			Node att=XMLDOMUtils.selectSingleNode(mensaje, "//EJECUCION/@ID");
			if (att!=null)
			{
				id=att.getNodeValue();
			}
		}
		return id;
	}
	/**
	 * Destruye las variables almacenadas en el contexto. Se hace así
	 * para asegurar que se libera toda la memoria.
	 * Estas variables se almacenan en el contexto del hilo de ejecución, con lo cual
	 * estarían vivas un tiempo indeterminado. De esta forma, las marcamos 
	 * para que el recolector de basura las elimine.
	 * Aunque no se hiciera no sería muy grave, porque nunca habría más de una copia
	 * por hilo, y la siguiente petición que entrara al hilo machacaría estos 
	 * datos.
	 * @param context
	 */
	private void destruirContextoSesion(SOAPMessageContext context)
	{
		try{
			Logger log=null;
			if (context.containsKey(Constantes.VAR_LOG_APLICACION))
			{
				log = (Logger) context.get(Constantes.VAR_LOG_APLICACION);
				String id = (String)context.get(Constantes.VAR_ID_MENSAJE);
				//log.info("Final de la petición de consulta de deuda con ID:" + id);
				log.info ("============FIN ID="+id);
			}
		}
		//No nos importan los errores. Si no se puede hacer log, continuamos para que 
		//termine de forma normal.
		catch (Exception ex){};
		//A la salida, nos cargamos los datos de contexto.
		context.remove(Constantes.VAR_ID_SESION);
		context.remove(Constantes.VAR_LOG_APLICACION);
		context.remove(Constantes.VAR_PREFERENCIAS);
		context.remove(Constantes.VAR_XMLSOURCE);
		context.remove(Constantes.VAR_ID_MENSAJE);
	}
	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		//Aunque se haya producido fallo, nos aseguramos de destruir el contexto
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (salida.booleanValue())
		{
		destruirContextoSesion(context);
		}
		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		Logger log=null;
		try
		{
			//A la entrada, cargamos lo necesario, o paramos y no continuamos si hay algún fallo.
			if (!salida.booleanValue())
			{
				//Lo generamos, para que exista.
				String idSesion=Utils.getIdLlamada();
				context.put(Constantes.VAR_ID_SESION, idSesion);
				context.setScope(Constantes.VAR_ID_SESION, MessageContext.Scope.APPLICATION);
				Preferencias pref = PreferenciasFactory.newInstance();
				log=LogFactory.newLogger(pref.getModoLog(), pref.getFicheroLogApp(), idSesion);
				context.put (Constantes.VAR_LOG_APLICACION,log);
				context.put (Constantes.VAR_PREFERENCIAS,pref);
				context.setScope(Constantes.VAR_LOG_APLICACION,MessageContext.Scope.APPLICATION);
				context.setScope(Constantes.VAR_PREFERENCIAS, MessageContext.Scope.APPLICATION);
				Node payload = extraerMensaje (context);
				//String id= getID(payload);
				String textXml = XMLDOMUtils.getXMLText(payload);
				context.put(Constantes.VAR_XMLSOURCE, textXml);
		        context.setScope(Constantes.VAR_XMLSOURCE, MessageContext.Scope.APPLICATION);
		        String id=new RecuperadorIdMensaje(log).getIdEjecucion(context.getMessage());
				//Id de mensaje, para que a la vuelta lo tengamos sin buscarlo.
				context.put(Constantes.VAR_ID_MENSAJE, id);
				context.setScope(Constantes.VAR_ID_MENSAJE, MessageContext.Scope.APPLICATION);
		        log.info("============Inicio ID="+id);
		        //log.info("Inicio de la petición de consulta de deuda con ID:" + id);
			}
			else
			{
				destruirContextoSesion(context);
			}
		}
		catch (PreferenciasException ex)
		{
			//Si no hay preferencias, no debemos tener log.
			System.err.println ("Asturcon Consulta Deuda:: error en preferencias ::"+ex.getMessage());
			ex.printStackTrace();
			Utils.generateSOAPErrMessage(context.getMessage(),"Error en proceso de mensaje SOAP.","0001","Error al crear el entorno de la llamada");
		}
		catch (XMLDOMDocumentException ex)
		{
			if (log!=null)
			{
				log.error ("Error al recuperar el texto del XML de entrada:" + ex.getMessage(),ex);
			}
			else
			{
				System.err.println ("Asturcon Consulta Deuda:: error al recuperar el texto del XML de entrada ::"+ex.getMessage());
				ex.printStackTrace();
			}
			Utils.generateSOAPErrMessage(context.getMessage(),"Error en proceso de mensaje SOAP.","0001","Error al crear el entorno de la llamada");
		}
		return true;
	}

}
