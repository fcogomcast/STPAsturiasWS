package es.tributasenasturias.modelo600utils;

import es.tributasenasturias.webservices.lanzador.clients.LanzaPL;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLService;

public class VerificacionUtils {

	public static class CodigoVerificacion
	{
		public CodigoVerificacion(String codVerificacion, String hashVerificacion)
		{
			this.codigoVerificacion= codVerificacion;
			this.hashCodVerificacion= hashVerificacion;
		}
		private String codigoVerificacion;
		private String hashCodVerificacion;
		public String getCodigoVerificacion() {
			return codigoVerificacion;
		}
		public String getHashCodVerificacion() {
			return hashCodVerificacion;
		}
		
	}
	/**
	 * Recupera el código de verificación de un documento.
	 * @return Objeto Código de verificación
	 * @throws DatosException Si ocurre un error relacionado con la recuperación de datos.
	 * @throws SystemException Si ocurre un error técnico en la recuperación.
	 */
	public static CodigoVerificacion recuperarCodVerificacion(String tipoDoc, String justificante, String nifSP) throws Exception{
		String hash="";
		String codVerificacion="";
		String respuesta="";
		Preferencias pref;
		pref=new Preferencias();
		pref.CargarPreferencias();
		ConversorParametrosLanzador cpl;
		cpl = new ConversorParametrosLanzador();

		cpl
		.setProcedimientoAlmacenado(pref.getpAProcCodVerif());
		cpl.setParametro(tipoDoc,ConversorParametrosLanzador.TIPOS.String);
		cpl.setParametro(justificante, ConversorParametrosLanzador.TIPOS.String);
		cpl.setParametro(nifSP,ConversorParametrosLanzador.TIPOS.String);
		// conexion oracle
		cpl.setParametro("P", ConversorParametrosLanzador.TIPOS.String);

		LanzaPLService  lanzaderaWS = new LanzaPLService();
		LanzaPL lanzaderaPort;
		lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();

		// enlazador de protocolo para el servicio.
		javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;
		// Cambiamos el endpoint
		bpr.getRequestContext().put(
				javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				pref.getEndpointLanzador());
		respuesta = "";
		respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl.Codifica(), "", "", "", "");
		cpl.setResultado(respuesta);
		if (!cpl.getError().equals(""))
		{
			throw new Exception ("Excepción de BD al recuperar el código de verificación:" + cpl.getError());
		}
		if (respuesta.trim().equals(""))
		{
			throw new Exception ("Excepción de BD al recuperar el código de verificación. Se ha recibido una respuesta vacía.");
		}
		hash=cpl.getNodoResultado("STRING1_CANU");
		codVerificacion=cpl.getNodoResultado("STRING2_CANU");
		if (codVerificacion==null || "".equals(codVerificacion))
		{
			throw new Exception("No se ha podido recuperar el código de verificación");
		}
		return new CodigoVerificacion(codVerificacion, hash);
	}
	/** Recupera el código de verificación
	 * 
	 * @param tipoDoc: tipo de documento.
	 * @param justificante: id de justificante
	 * @param nifSP : nif del contribuyente
	 * @return Objeto conteniendo los datos del código de verificación.
	 * @throws Exception
	 */
	public static CodigoVerificacion codigoVerificacion(String tipoDoc, String justificante, String nifSP) throws Exception{
		return recuperarCodVerificacion(tipoDoc, justificante, nifSP);
	}
}
