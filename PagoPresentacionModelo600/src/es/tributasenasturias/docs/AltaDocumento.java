package es.tributasenasturias.docs;

import java.io.ByteArrayOutputStream; //import java.io.ByteArrayInputStream;

//import es.tributasenasturias.documentos.util.Base64;
import es.tributasenasturias.Exceptions.PresentacionException;
import es.tributasenasturias.docs.Base64; //import es.tributasenasturias.documentos.util.XMLUtils; //import es.tributasenasturias.pagopresentacionmodelo600utils.ConversorParametrosLanzador;
import es.tributasenasturias.pagopresentacionmodelo600utils.ConversorParametrosLanzador;
import es.tributasenasturias.pagopresentacionmodelo600utils.Preferencias;
import es.tributasenasturias.webservice.pagopresentacion.log.ILoggable;
import es.tributasenasturias.webservice.pagopresentacion.log.LogHelper;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPL;
import es.tributasenasturias.webservices.lanzador.clients.LanzaPLService;
import es.tributasenasturias.pagopresentacionmodelo600utils.PdfComprimidoUtils;

public class AltaDocumento implements ILoggable{

	private ConversorParametrosLanzador cpl;

	private Preferencias pref = new Preferencias();

	//Log
	private LogHelper log;
	protected AltaDocumento() throws Exception{
			pref.CargarPreferencias();
	};

	public String setDocumento(String p_tipo, String p_nombre,
			String p_codVerif, String p_nifSp, String p_nifPr, String doc,
			String p_tipoDoc) throws PresentacionException{
		// Llamar al lanzador para que llame al procedimiento almacenado
		try {
			// llamar al servicio PXLanzador para ejecutar el procedimiento
			// almacenado de alta de expediente
			// Se prepara la llamada al procedimiento almacenado
			cpl = new ConversorParametrosLanzador();			
			cpl.setProcedimientoAlmacenado(pref.getPAAltaDocumento());
			// tipo
			cpl.setParametro(p_tipo, ConversorParametrosLanzador.TIPOS.String);
			// nombre
			cpl
					.setParametro(p_nombre,
							ConversorParametrosLanzador.TIPOS.String);
			// codigo verificacion
			cpl.setParametro(p_codVerif,
					ConversorParametrosLanzador.TIPOS.String);
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

			// Decodificamos documento.
			if (p_tipoDoc != "XML") {
				byte[] docDecodificado = Base64.decode(doc.toCharArray());
				String documentzippeado = new String();
				ByteArrayOutputStream resulByteArray = new ByteArrayOutputStream();
				resulByteArray.write(docDecodificado);
				documentzippeado = PdfComprimidoUtils
						.comprimirPDF(resulByteArray);
				
				cpl.setParametro(documentzippeado,
						ConversorParametrosLanzador.TIPOS.Clob);
				
			} else {
				cpl.setParametro(doc, ConversorParametrosLanzador.TIPOS.Clob);
			}

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
			respuesta = lanzaderaPort.executePL(pref.getEntorno(), cpl
					.Codifica(), "", "", "", "");
			cpl.setResultado(respuesta);
			//log.debug("**** LLAMADA PROCEDIMIENTO ALTA DOCUMENTO ****");
			//log.debug("Procedimiento:" + pref.getPAAltaDocumento());
			//log.debug("Entorno:" + pref.getEntorno());
			//log.debug("EndPointLanzador:" + pref.getEndpointLanzador());
			return "ok";
		} catch (Exception e) {
			throw new PresentacionException ("Excepción en el alta de documento:" + e.getMessage(),e);
			//return "ko";
		}
	}
	@Override
	public void setLogger(LogHelper log)
	{
		this.log = log;
	}
	@Override 
	public LogHelper getLogger()
	{
		return log;
	}
}