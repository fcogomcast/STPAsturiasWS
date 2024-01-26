package es.stpa.smartmultas.clases;

import org.w3c.dom.Document;

import es.stpa.smartmultas.configuracion.utils.ConfiguracionUtils;
import es.stpa.smartmultas.configuracion.utils.Utiles;
import es.stpa.smartmultas.preferencias.Preferencias;
import es.stpa.smartmultas.soap.SoapClientHandler;
import es.tributasenasturias.log.ILog;
import es.tributasenasturias.services.lanzador.client.LanzadorFactory;
import es.tributasenasturias.services.lanzador.client.ParamType;
import es.tributasenasturias.services.lanzador.client.ProcedimientoAlmacenado;
import es.tributasenasturias.services.lanzador.client.TLanzador;
import es.tributasenasturias.services.lanzador.client.response.RespuestaLanzador;

// ServiciosSmartFines.Multas.ObtenerConfiguracion
public class ObtenerConfiguracion extends A_MultasBase {
	
	public ObtenerConfiguracion(Preferencias pref, String idLlamada, Document datosEntrada, ILog log) {
		super(pref, idLlamada, datosEntrada, log);
	}
	
	@Override
	public String Inicializar() {

		final String NUME = "NUME_NUMERO";
		String datosRespuesta = null;

		//datos de la sesion //revisar!!!
		/*
			var datosSesion = SWUtils.JsonToObject<DatosSesion>(_request.JsonData);
            datosSesion.SesionOrga = idOrga;
            _request.IdOrganismo = idOrga;
            _request.JsonData = SWUtils.JsonSerializer(datosSesion);
		 * */

		try 
		{
			String codCliente = _datosEntrada.getElementsByTagName("CodigoCliente").item(0).getTextContent();
			String codConfig = _datosEntrada.getElementsByTagName("CodigoConfiguracion").item(0).getTextContent();

			TLanzador lanzador = LanzadorFactory.newTLanzador(_pref.getEndpointLanzador(), new SoapClientHandler(this._idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("SMARTFINES.OBTENERCONFIGSF", _pref.getEsquemaBD());
			Utiles.AgregarCabeceraGeneralPL(proc);

			proc.param(codCliente, ParamType.CADENA);
			proc.param(codConfig, ParamType.CADENA);

			String soapResponse = lanzador.ejecutar(proc);
			RespuestaLanzador response = new RespuestaLanzador(soapResponse);
		
			if (!response.esErronea() && response.getNumFilasEstructura(NUME) > 0) //((mirar esto con rafa, lo puedo dejar asi???)) revisar!!
			{
				Integer idSubo = Integer.parseInt(response.getValue(NUME, 1, "NUMERO_NUME")); //revisar esto
				
				datosRespuesta = ConfiguracionUtils.GetTextoValidacionUDID(_pref, _idLlamada, _datosEntrada, idSubo, _log);
				if (datosRespuesta == null) 
				{	
					datosRespuesta = ConfiguracionUtils.ObtenerDetallesConfig(_pref, _idLlamada, _log, _datosEntrada, idSubo, false);	
				}		
			} 
			else 
			{
				datosRespuesta = Utiles.MensajeErrorXml(0, response.getTextoError(), _log);
			}
		} 
		catch (Exception ex) 
		{
			datosRespuesta = Utiles.MensajeError(ex.getMessage(), _log);
		}
		
		
		return datosRespuesta;
	}
}
