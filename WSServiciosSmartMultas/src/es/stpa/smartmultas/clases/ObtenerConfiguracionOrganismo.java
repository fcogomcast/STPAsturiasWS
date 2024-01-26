package es.stpa.smartmultas.clases;

import org.w3c.dom.Document;

import es.stpa.smartmultas.configuracion.utils.ConfiguracionUtils;
import es.stpa.smartmultas.configuracion.utils.Utiles;
import es.stpa.smartmultas.preferencias.Preferencias;
import es.tributasenasturias.log.ILog;

// ServiciosSmartFines.Multas.ObtenerConfiguracionOrganismo
public class ObtenerConfiguracionOrganismo extends A_MultasBase {

	public ObtenerConfiguracionOrganismo(Preferencias pref, String idLlamada, Document datosEntrada, ILog log) {
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
				//revisar!!
				//var datosSesion = SWUtils.JsonToObject<DatosSesion>(request.JsonData);
                //request.JsonData = SWUtils.JsonSerializer(datosSesion);
				
				datosRespuesta = ConfiguracionUtils.ObtenerDetallesConfig(_pref, _idLlamada, _log, _datosEntrada, idSubo, true);
			} 
			catch (Exception ex) 
			{
				datosRespuesta = Utiles.MensajeError(ex.getMessage(), _log);
			}
		}

		return datosRespuesta;
	}
}
