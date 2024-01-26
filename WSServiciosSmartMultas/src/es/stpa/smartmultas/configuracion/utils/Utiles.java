package es.stpa.smartmultas.configuracion.utils;

import javax.xml.ws.WebServiceContext;

import es.stpa.smartmultas.Constantes;
import es.stpa.smartmultas.entidades.Error;
import es.stpa.smartmultas.preferencias.Preferencias;
import es.stpa.smartmultas.soap.SoapClientHandler;
import es.tributasenasturias.log.ILog;
import es.tributasenasturias.log.TributasLogger;
import es.tributasenasturias.services.lanzador.client.LanzadorFactory;
import es.tributasenasturias.services.lanzador.client.ParamType;
import es.tributasenasturias.services.lanzador.client.ProcedimientoAlmacenado;
import es.tributasenasturias.services.lanzador.client.TLanzador;
import es.tributasenasturias.services.lanzador.client.response.RespuestaLanzador;
import es.tributasenasturias.xml.XMLDOMUtils;

public class Utiles {

	final static String WTEA = "WTEA_ESTRUCTURA_ARQUITECTURA";
	final static String ES05 = "ES05_ESTRUCTURA_UNI05";
	final static String ESUN = "ESUN_ESTRUCTURA_UNIVERSAL";

	
	public static void AgregarCabeceraGeneralPL(ProcedimientoAlmacenado proc) {

		try 
		{
			// Cabecera base para las llamadas a PL
			proc.param("1", ParamType.NUMERO);
			proc.param("1", ParamType.NUMERO);
			proc.param("SMARTFINES", ParamType.CADENA); // Se sustituye USU_WEB_SAC por SMARTFINES
			proc.param("33", ParamType.NUMERO);		// idorga fijo para Asturias
		} 
		catch (Exception e) { }
	}
	
	
	public static String MensajeError(String mensaje, ILog log) {
		// final int MAXLENGTHJSON = 70000000; //epi?  revisar!!
		return MensajeErrorXml(-1, mensaje, log);
	}
	

	public static String MensajeErrorXml(int codigo, String mensaje, ILog log) {
		Error err = new Error(codigo, mensaje);
		log.error(mensaje);
		log.info(mensaje);

		return XMLDOMUtils.Serialize(err);
	}


	public static String ObtenerValorPropiedad(Preferencias pref, String idLlamada, String codPropiedad) {
		
		String valor = null;

		try 
		{
			TLanzador lanzador = LanzadorFactory.newTLanzador(pref.getEndpointLanzador(), new SoapClientHandler(idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("APP_SITSANCIONA.OBTENER_PROPIEDAD_SW", pref.getEsquemaBD());
			Utiles.AgregarCabeceraGeneralPL(proc);

			proc.param(codPropiedad, ParamType.CADENA);
			proc.param("4", ParamType.NUMERO); //Será fijo en Asturias...ServiciosSmartFines.Multas

			String soapResponse = lanzador.ejecutar(proc);
			RespuestaLanzador response = new RespuestaLanzador(soapResponse);

			if (!response.esErronea() && response.getNumFilasEstructura(ESUN) > 0) 
			{
				valor = response.getValue(ESUN, 1, "C1");
			} 			
		} 
		catch (Exception ex) { }

		return valor;
	}

	public static String ObtenerValorPropiedadPago(Preferencias pref, String idLlamada) {
		
		String resultado = null;

		try 
		{
			resultado = ObtenerValorPropiedad(pref, idLlamada, "SMARTFINES.PAGO");
		} 
		catch (Exception ex) { }

		return resultado;
	}

	public static ILog gestionarLog(Preferencias pref, String idLlamada, WebServiceContext wcontext) {

		ILog log = null;
		
		try 
		{
			log = (ILog) wcontext.getMessageContext().get(Constantes.LOG);
			if (log == null) 
			{
				log = new TributasLogger(pref.getModoLog(), pref.getDirectorioRaizLog(), pref.getFicheroLogAplicacion(), "Sesión::" + idLlamada);
			}

			log.info("Comienzo de operación XmlRequest");
		} 
		catch (Exception ex) {}
		
		return log;
	}
	
	
	public static boolean isNumeric(String cadena)
	{
		try 
		{
			Integer.parseInt(cadena);
			return true;
		} 
		catch (NumberFormatException nfe)
		{
			return false;
		}
	}
}
