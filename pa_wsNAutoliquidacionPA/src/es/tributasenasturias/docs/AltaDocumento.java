package es.tributasenasturias.docs;

import es.tributasenasturias.utilsProgramaAyuda.ConversorParametrosLanzador;
import es.tributasenasturias.utilsProgramaAyuda.Logger;
import es.tributasenasturias.utilsProgramaAyuda.Preferencias;
import es.tributasenasturias.webservices.clients.lanzadera.LanzaPL;
import es.tributasenasturias.webservices.clients.lanzadera.LanzaPLService;

public class AltaDocumento {

	private ConversorParametrosLanzador cpl;

	private Preferencias pref = new Preferencias();
	public AltaDocumento() {
		try {
			pref.CargarPreferencias();
		} catch (Exception e) {
			Logger
					.error("Error al cargar preferencias al dar de alta el documento. "
							+ e.getMessage());
		}
	};

	public String setDocumento(String p_tipo, String p_nombre,
			String p_codVerif, String p_nifSp, String p_nifPr, String doc,
			String p_tipoDoc) {
		// Llamar al lanzador para que llame al procedimiento almacenado
		try {
			// llamar al servicio PXLanzador para ejecutar el procedimiento
			// almacenado de alta de expediente
			// Se prepara la llamada al procedimiento almacenado
			cpl = new ConversorParametrosLanzador();
			if (pref.getDebug().equals("1"))
				Logger.debug(pref.getPAAltaDocumento());
			cpl.setProcedimientoAlmacenado(pref.getPAAltaDocumento());
			// tipo
			cpl.setParametro(p_tipo,ConversorParametrosLanzador.TIPOS.String);
			// nombre
			cpl.setParametro(p_nombre,ConversorParametrosLanzador.TIPOS.String);
			// codigo verificacion
			cpl.setParametro(p_codVerif,ConversorParametrosLanzador.TIPOS.String);
			// sp
			cpl.setParametro(p_nifSp, ConversorParametrosLanzador.TIPOS.String);
			// presentador
			cpl.setParametro(p_nifPr, ConversorParametrosLanzador.TIPOS.String);
			// sesion
			cpl.setParametro("", ConversorParametrosLanzador.TIPOS.String);
			// origen
			cpl.setParametro("P", ConversorParametrosLanzador.TIPOS.String);
			// libre
			cpl.setParametro("", ConversorParametrosLanzador.TIPOS.String);
			// publicable = 'S'
			cpl.setParametro("S", ConversorParametrosLanzador.TIPOS.String);						  		
							
			cpl.setParametro("<![CDATA["+doc+"]]>",
					ConversorParametrosLanzador.TIPOS.Clob);

			// Tipo Document XML o PDF
			cpl.setParametro(p_tipoDoc,
					ConversorParametrosLanzador.TIPOS.String);
			// conoracle = 'P'
			cpl.setParametro("P", ConversorParametrosLanzador.TIPOS.String);

			LanzaPLService lanzaderaWS = new LanzaPLService();
			LanzaPL lanzaderaPort;

			lanzaderaPort = lanzaderaWS.getLanzaPLSoapPort();
			// enlazador de protocolo para el servicio.
			javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) lanzaderaPort;
			// Cambiamos el endpoint
			bpr.getRequestContext().put(
					javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
					pref.getEndpointLanzador());

			String respuesta = new String();
			try {
				respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl
						.Codifica(), "", "", "", "");
				cpl.setResultado(respuesta);
			} catch (Exception ex) {
				if (pref.getDebug().equals("1")) {
					Logger.error("LANZADOR: ".concat(ex.getMessage()));
				}
			}
			
			if (pref.getDebug().equals("1")) {
				Logger.debug("**** LLAMADA PROCEDIMIENTO ALTA DOCUMENTO ****");
				Logger.debug("Procedimiento:"+pref.getPAAltaDocumento());
				Logger.debug("Entorno:"+pref.getEntorno());			
				Logger.debug("Codifica:"+cpl.Codifica());
				Logger.debug("EndPointLanzador:"+pref.getEndpointLanzador());
				Logger.debug("Respuesta: ".concat(cpl.getResultado()));
			}
				
					
			return "ok";
		} catch (Exception e) {
			Logger.error("Excepcion generica" + e.getMessage());
			return "ko";
		}
	}

	
}
