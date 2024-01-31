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
	 * Recupera el c�digo de verificaci�n de un documento.
	 * @return Objeto C�digo de verificaci�n
	 * @throws DatosException Si ocurre un error relacionado con la recuperaci�n de datos.
	 * @throws SystemException Si ocurre un error t�cnico en la recuperaci�n.
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
			throw new Exception ("Excepci�n de BD al recuperar el c�digo de verificaci�n:" + cpl.getError());
		}
		if (respuesta.trim().equals(""))
		{
			throw new Exception ("Excepci�n de BD al recuperar el c�digo de verificaci�n. Se ha recibido una respuesta vac�a.");
		}
		hash=cpl.getNodoResultado("STRING1_CANU");
		codVerificacion=cpl.getNodoResultado("STRING2_CANU");
		if (codVerificacion==null || "".equals(codVerificacion))
		{
			throw new Exception("No se ha podido recuperar el c�digo de verificaci�n");
		}
		return new CodigoVerificacion(codVerificacion, hash);
	}
	/** Recupera el c�digo de verificaci�n
	 * 
	 * @param tipoDoc: tipo de documento.
	 * @param justificante: id de justificante
	 * @param nifSP : nif del contribuyente
	 * @return Objeto conteniendo los datos del c�digo de verificaci�n.
	 * @throws Exception
	 */
	public static CodigoVerificacion codigoVerificacion(String tipoDoc, String justificante, String nifSP) throws Exception{
		return recuperarCodVerificacion(tipoDoc, justificante, nifSP);
	}
}
