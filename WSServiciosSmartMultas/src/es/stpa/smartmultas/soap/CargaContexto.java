package es.stpa.smartmultas.soap;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.stpa.smartmultas.Constantes;
import es.stpa.smartmultas.preferencias.Preferencias;
import es.stpa.smartmultas.preferencias.PreferenciasException;
import es.tributasenasturias.log.ILog;
import es.tributasenasturias.log.TributasLogger;




/**
 * Carga datos en el contexto de servicio que serán utilizados
 * en el resto del servicio.
 * Estos objetos serán el log de aplicación, los mensajes de aplicación, las preferencias, y el id de sesión.
 * 
 * @author crubencvs
 *
 */
public class CargaContexto implements SOAPHandler<SOAPMessageContext> {

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
		//A la salida, nos cargamos los datos de contexto.
		context.remove(Constantes.IDSESION);
		context.remove(Constantes.PREFERENCIAS);
		context.remove(Constantes.LOG);
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
			ILog log = (ILog) context.get(Constantes.LOG);
			if (log!=null){
				log.info("Final de llamada a servicio");
			}
			destruirContextoSesion(context);
		}
		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		try
		{
			//A la entrada, cargamos lo necesario, o paramos y no continuamos si hay algún fallo.
			if (!salida.booleanValue())
			{
				//Lo generamos, para que exista.
				String idLlamada=SOAPUtils.getIdLlamada();
				context.put(Constantes.IDSESION, idLlamada);
				context.setScope(Constantes.IDSESION, MessageContext.Scope.APPLICATION);
				Preferencias pref = new Preferencias();
				context.put (Constantes.PREFERENCIAS,pref);
				context.setScope(Constantes.PREFERENCIAS, MessageContext.Scope.APPLICATION);
				ILog log= new TributasLogger(pref.getModoLog(),pref.getDirectorioRaizLog(),pref.getFicheroLogAplicacion(),idLlamada);
				context.put (Constantes.LOG, log);
				context.setScope(Constantes.LOG,MessageContext.Scope.APPLICATION);
				//Primera entrada del servicio
				log.info("Inicio de llamada a servicio");
			}
			else
			{
				ILog log = (ILog) context.get(Constantes.LOG);
				if (log!=null){
					log.info("Final de llamada a servicio");
				}
				destruirContextoSesion(context);
			}
		}
		catch (PreferenciasException ex)
		{
			//Si no hay preferencias, no debemos tener log.
			System.err.println ("Servicios Smart Multas:: error en preferencias ::"+ex.getMessage());
			ex.printStackTrace();
			SOAPUtils.generateSOAPErrMessage(context.getMessage(),"Error en proceso de mensaje SOAP.","0001","Error al crear el entorno de la llamada", true);
		}
		return true;
	}

	


}
