package es.tributasenasturias.servicios.accesocertificado.soap;

import java.util.Collections;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.servicios.accesocertificado.contextoLlamadas.CallContextConstants;
import es.tributasenasturias.servicios.accesocertificado.log.LogFactory;
import es.tributasenasturias.servicios.accesocertificado.preferencias.Preferencias;
import es.tributasenasturias.servicios.accesocertificado.preferencias.PreferenciasFactory;
import es.tributasenasturias.servicios.accesocertificado.utils.Util;
import es.tributasenasturias.utils.log.Logger;

public class CargaContexto implements SOAPHandler<SOAPMessageContext>{

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
		context.remove(CallContextConstants.IDSESION);
		context.remove(CallContextConstants.PREFERENCIAS);
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
			//A la entrada, cargamos lo necesario, o paramos y no continuamos si hay alg�n fallo.
			if (!salida.booleanValue())
			{
				//Lo generamos, para que exista.
				String idSesion=Util.getIdLlamada();
				context.put(CallContextConstants.IDSESION, idSesion);
				context.setScope(CallContextConstants.IDSESION, MessageContext.Scope.APPLICATION);
				Preferencias pref = PreferenciasFactory.newInstance();
				log=LogFactory.newLogger(pref.getModoLog(), pref.getFicheroLogServer(), idSesion);
				context.put (CallContextConstants.PREFERENCIAS,pref);
				context.setScope(CallContextConstants.PREFERENCIAS, MessageContext.Scope.APPLICATION);
			}
			else
			{
				destruirContextoSesion(context);
			}
		}
		
		catch (Exception ex)
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
			try{
			Util.createSOAPFaultException("Error en el servicio de Acceso por Certificado.");
			}catch (SOAPException e){}
		}
		return true;
	}

}
