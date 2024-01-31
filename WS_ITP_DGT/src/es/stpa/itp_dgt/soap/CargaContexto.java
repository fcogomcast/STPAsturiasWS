package es.stpa.itp_dgt.soap;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.stpa.itp_dgt.preferencias.Preferencias;
import es.stpa.itp_dgt.preferencias.PreferenciasException;
import es.stpa.itp_dgt.utils.Constantes;
import es.stpa.itp_dgt.utils.Utils;
import es.tributasenasturias.utils.log.LogFactory;
import es.tributasenasturias.utils.log.Logger;




/**
 * Carga datos en el contexto de servicio que ser�n utilizados
 * en el resto del servicio.
 * Estos objetos ser�n el log de aplicaci�n, los mensajes de aplicaci�n, las preferencias, y el id de sesi�n.
 * 
 * @author crubencvs
 *
 */
public class CargaContexto implements SOAPHandler<SOAPMessageContext> {

	
	/**
	 * Destruye las variables almacenadas en el contexto. Se hace as�
	 * para asegurar que se libera toda la memoria.
	 * Estas variables se almacenan en el contexto del hilo de ejecuci�n, con lo cual
	 * estar�an vivas un tiempo indeterminado. De esta forma, las marcamos 
	 * para que el recolector de basura las elimine.
	 * Aunque no se hiciera no ser�a muy grave, porque nunca habr�a m�s de una copia
	 * por hilo, y la siguiente petici�n que entrara al hilo machacar�a estos 
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
		if (!salida.booleanValue())
		{
		destruirContextoSesion(context);
		}
		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		try
		{
			//A la entrada, cargamos lo necesario, o paramos y no continuamos si hay alg�n fallo.
			if (!salida.booleanValue())
			{
				//Lo generamos, para que exista.
				String idLlamada=Utils.getIdLlamada();
				context.put(Constantes.IDSESION, idLlamada);
				context.setScope(Constantes.IDSESION, MessageContext.Scope.APPLICATION);
				Preferencias pref = new Preferencias();
				context.put (Constantes.PREFERENCIAS,pref);
				context.setScope(Constantes.PREFERENCIAS, MessageContext.Scope.APPLICATION);
				Logger log=LogFactory.newLogger(pref.getModoLog(), pref.getFicheroLogApp(), idLlamada);
				context.put (Constantes.LOG, log);
				context.setScope(Constantes.LOG,MessageContext.Scope.APPLICATION);
				
			}
			else
			{
				destruirContextoSesion(context);
			}
		}
		catch (PreferenciasException ex)
		{
			//Si no hay preferencias, no debemos tener log.
			System.err.println ("Servicio ITP DGT: error en preferencias ::"+ex.getMessage());
			ex.printStackTrace();
			Utils.generateSOAPErrMessage(context.getMessage(),"Error en proceso de mensaje SOAP.","0001","Error al crear el entorno de la llamada");
		}
		
		return true;
	}

	


}
