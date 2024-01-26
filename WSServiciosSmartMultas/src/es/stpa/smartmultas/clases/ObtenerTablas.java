package es.stpa.smartmultas.clases;

import org.w3c.dom.Document;

import es.stpa.smartmultas.configuracion.utils.ConfiguracionUtils;
import es.stpa.smartmultas.configuracion.utils.Utiles;
import es.stpa.smartmultas.preferencias.Preferencias;
import es.stpa.smartmultas.responses.MultasObtenerTablasResponse;
import es.stpa.smartmultas.soap.SoapClientHandler;
import es.tributasenasturias.log.ILog;
import es.tributasenasturias.services.lanzador.client.LanzadorFactory;
import es.tributasenasturias.services.lanzador.client.ParamType;
import es.tributasenasturias.services.lanzador.client.ProcedimientoAlmacenado;
import es.tributasenasturias.services.lanzador.client.TLanzador;
import es.tributasenasturias.services.lanzador.client.response.RespuestaLanzador;
import es.tributasenasturias.xml.XMLDOMUtils;

// ServiciosSmartFines.Multas.ObtenerTablas
public class ObtenerTablas extends A_MultasBase {

	public ObtenerTablas(Preferencias pref, String idLlamada, Document datosEntrada, ILog log) {
		super(pref, idLlamada, datosEntrada, log);
	}

	@Override
	public String Inicializar() {
		
		final String WTEA = "WTEA_ESTRUCTURA_ARQUITECTURA";
		String datosRespuesta = null;

		try 
		{
			Integer idSubo = Integer.parseInt(_datosEntrada.getElementsByTagName("Municipio").item(0).getTextContent());		
			datosRespuesta = ConfiguracionUtils.GetTextoValidacionUDID(_pref, _idLlamada, _datosEntrada, idSubo, _log);
			
			if(datosRespuesta == null) 
			{
				String fecha = _datosEntrada.getElementsByTagName("Fecha").item(0).getTextContent();
				String tabla = _datosEntrada.getElementsByTagName("Tabla").item(0).getTextContent();

				TLanzador lanzador = LanzadorFactory.newTLanzador(this._pref.getEndpointLanzador(), new SoapClientHandler(this._idLlamada));
				ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("APP_SITSANCIONA.MULTASEXTERNASOBTENERXMLTABLA", this._pref.getEsquemaBD());
				Utiles.AgregarCabeceraGeneralPL(proc);

				proc.param(idSubo + "", ParamType.NUMERO);
				proc.param(fecha, ParamType.CADENA);
				proc.param(tabla, ParamType.CADENA);

				String soapResponse = lanzador.ejecutar(proc);
				RespuestaLanzador response = new RespuestaLanzador(soapResponse);

				if (!response.esErronea() && response.getNumFilasEstructura(WTEA) > 0) 
				{
					String datosXML = response.getValue(WTEA, 1, "CL1");	
					MultasObtenerTablasResponse respuesta = XMLDOMUtils.XMLtoObject(datosXML, MultasObtenerTablasResponse.class);
					
					datosRespuesta = XMLDOMUtils.Serialize(respuesta);
				} 
				else 
				{
					datosRespuesta = Utiles.MensajeErrorXml(0, response.getTextoError(), _log);
				}
			}
		} 
		catch (Exception ex) 
		{
			datosRespuesta = Utiles.MensajeError(ex.getMessage(), _log);
		}
		
		return datosRespuesta;
	}
}
