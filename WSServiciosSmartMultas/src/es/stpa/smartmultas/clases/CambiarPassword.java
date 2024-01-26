package es.stpa.smartmultas.clases;

import org.w3c.dom.Document;

import es.stpa.smartmultas.configuracion.utils.Utiles;
import es.stpa.smartmultas.preferencias.Preferencias;
import es.stpa.smartmultas.responses.SmartfinesResponse;
import es.stpa.smartmultas.soap.SoapClientHandler;
import es.tributasenasturias.log.ILog;
import es.tributasenasturias.services.lanzador.client.LanzadorFactory;
import es.tributasenasturias.services.lanzador.client.ParamType;
import es.tributasenasturias.services.lanzador.client.ProcedimientoAlmacenado;
import es.tributasenasturias.services.lanzador.client.TLanzador;
import es.tributasenasturias.services.lanzador.client.response.RespuestaLanzador;
import es.tributasenasturias.xml.XMLDOMUtils;

// ServiciosSmartFines.Multas.CambiarPassword
public class CambiarPassword extends A_MultasBase {
	
	public CambiarPassword(Preferencias pref, String idLlamada, Document datosEntrada, ILog log) {
		super(pref, idLlamada, datosEntrada, log);
	}
	
	@Override
	public String Inicializar() {
		final String ES05 = "ES05_ESTRUCTURA_UNI05";
		
		String datosRespuesta = null;
		
		try 
		{	
			String idSubo = _datosEntrada.getElementsByTagName("SubOrganismo").item(0).getTextContent();
			String agente = _datosEntrada.getElementsByTagName("NumeroAgente").item(0).getTextContent();
			String passw = _datosEntrada.getElementsByTagName("Password").item(0).getTextContent(); 	// password cifrada
			
			TLanzador lanzador = LanzadorFactory.newTLanzador(_pref.getEndpointLanzador(), new SoapClientHandler(this._idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("APP_SITSANCIONA.UPDATEPASSAGEMULTAS", _pref.getEsquemaBD());
			Utiles.AgregarCabeceraGeneralPL(proc);
			
			proc.param(idSubo, ParamType.NUMERO);
			proc.param(agente, ParamType.CADENA);
			proc.param(passw, ParamType.CADENA);
		
			String soapResponse = lanzador.ejecutar(proc);
			RespuestaLanzador response = new RespuestaLanzador(soapResponse);
			
			if (!response.esErronea() &&  response.getNumFilasEstructura(ES05) > 0) 
			{
				String resp = response.getValue(ES05, 1, "C1");
				
				if(resp.toUpperCase().equals("OK"))
				{
					datosRespuesta = XMLDOMUtils.Serialize(new SmartfinesResponse(resp));
				}
				else 
				{
					datosRespuesta = Utiles.MensajeErrorXml(1, resp, _log);
				}
			} 
			else 
			{
				datosRespuesta = Utiles.MensajeErrorXml(1, response.getTextoError(), _log);
			}
		}
		catch (Exception ex) 
		{
			datosRespuesta = Utiles.MensajeErrorXml(1, "Error: " + ex.getMessage(), _log);	
		}

		return datosRespuesta;
	}
}
