package es.stpa.itp_dgt.wssecurity;


import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.stpa.itp_dgt.preferencias.Preferencias;



/**
 * Construye objetos para devolver respuestas del servicio.
 * @author crubencvs
 *
 */
public class WSSecurityFactory {
	
	private WSSecurityFactory(){};
	/**
	 * Devuelve una instancia  {@link GestorWSSecurity} que permite construir manejar la seguridad WS Security
	 * @param context Objeto para realizar log
	 * @param preferencias Objeto de preferencias.
	 * @param manejador {@link SOAPHandler} que permitirá interceptar mensajes realizados a los servicios llamados desde este objeto.
	 * @return instancia ComprobadorWS
	 */
	public static GestorWSSecurity newConstructorResultado(Preferencias pref, SOAPHandler<SOAPMessageContext> manejador) throws WSSecurityException
	{
		return new GestorWSSecurity(pref, manejador);
	}

}
