package es.stpa.smartmultas.clases;

import org.w3c.dom.Document;

import es.stpa.smartmultas.configuracion.ATEX.ConsultasATEX_service;
import es.stpa.smartmultas.configuracion.utils.ConfiguracionUtils;
import es.stpa.smartmultas.configuracion.utils.Utiles;
import es.stpa.smartmultas.preferencias.Preferencias;
import es.stpa.smartmultas.requests.ConsultaATEXRequest;
import es.stpa.smartmultas.requests.ConsultaMatriculaATEX5Request;
import es.tributasenasturias.log.ILog;
import es.tributasenasturias.xml.XMLDOMUtils;

// ServiciosSmartFines.Multas.ConsultaMatriculaATEX
public class ConsultaMatriculaATEX extends A_MultasBase {

	public ConsultaMatriculaATEX(Preferencias pref, String idLlamada, Document datosEntrada, ILog log) {
		super(pref, idLlamada, datosEntrada, log);
	}

	@Override
	public String Inicializar() {

		String datosRespuesta = null;

		Integer idSubo = Integer.parseInt(_datosEntrada.getElementsByTagName("Suborganismo").item(0).getTextContent());		
		datosRespuesta = ConfiguracionUtils.GetTextoValidacionUDID(_pref, _idLlamada, _datosEntrada, idSubo, _log);

		if (datosRespuesta == null) 
		{
			try 
			{
				datosRespuesta = gestionarDatosAtex(idSubo);
			} 
			catch (Exception ex) 
			{
				datosRespuesta = Utiles.MensajeErrorXml(1, ex.getMessage(), _log);
			}
		}

		return datosRespuesta;
	}

	
	private String gestionarDatosAtex(Integer idSubo) { // Tienen ATEX 5 integrado
		
		String datosResultado = "";
		
		try
		{
			String tmpXML = XMLDOMUtils.getXMLText(_datosEntrada);
			ConsultaATEXRequest datos = XMLDOMUtils.XMLtoObject(tmpXML, ConsultaATEXRequest.class);
			
			String IdResponsable = (datos.getNifAgente() != null && !datos.getNifAgente().isEmpty() ? datos.getNifAgente() : datos.getCodAgente());
			
			ConsultaMatriculaATEX5Request requestAtex5 = new ConsultaMatriculaATEX5Request(
					datos.getMatricula().toUpperCase(), 
					datos.getBastidor(),
					datos.getSuborganismo(), 
					datos.getCodAgente(),
					"GTT", 
					IdResponsable,
					datos.getUDID(),
					datos.getSesionId(),
					datos.getSesionOrga()
			);
			//throw new Exception("SALIDA::ejemplooooo");
			datosResultado = ConsultasATEX_service.ConsultaVehiculoATEX5(_pref, _idLlamada, _log, requestAtex5, idSubo);
			
		}
		catch (Exception ex) 
		{
			datosResultado = Utiles.MensajeErrorXml(1, ex.getMessage(), _log);
		}
		
		return datosResultado;
	}
}
