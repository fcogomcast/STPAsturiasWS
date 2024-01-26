package es.stpa.smartmultas.clases;

import org.w3c.dom.Document;

import es.stpa.smartmultas.configuracion.utils.ConfiguracionUtils;
import es.stpa.smartmultas.configuracion.utils.Utiles;
import es.stpa.smartmultas.entidades.Nota;
import es.stpa.smartmultas.preferencias.Preferencias;
import es.stpa.smartmultas.responses.SmartfinesResponse;
import es.tributasenasturias.log.ILog;
import es.tributasenasturias.xml.XMLDOMUtils;

// ServiciosSmartFines.Multas.InsertarNota
public class InsertarNota extends A_MultasBase {

	public InsertarNota(Preferencias pref, String idLlamada, Document datosEntrada, ILog log) {
		super(pref, idLlamada, datosEntrada, log);
	}

	public String Inicializar() {
		
		String datosRespuesta = "";

		try
		{
			String tmpXML = XMLDOMUtils.getXMLText(_datosEntrada);

			Nota nota = XMLDOMUtils.XMLtoObject(tmpXML, Nota.class);

			String result = ConfiguracionUtils.GuardarDatosNotaEnExpediente(_pref, _idLlamada, _log, nota.getAgente(), nota);
		
			if(result.toUpperCase().equals("OK"))
			{
				datosRespuesta = XMLDOMUtils.Serialize(new SmartfinesResponse("Ok"));
			}
			else
			{
				datosRespuesta = Utiles.MensajeErrorXml(1, result, _log);
			}
		}
		catch (Exception ex) 
		{
			datosRespuesta = Utiles.MensajeError(ex.getMessage(), _log);
		}			

		return datosRespuesta;
	}	
}
