package es.stpa.smartmultas.clases;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.w3c.dom.Document;

import es.stpa.smartmultas.configuracion.utils.ConfiguracionUtils;
import es.stpa.smartmultas.configuracion.utils.Utiles;
import es.stpa.smartmultas.preferencias.Preferencias;
import es.stpa.smartmultas.responses.ConsultaFechaHoraResponse;
import es.stpa.smartmultas.soap.SoapClientHandler;
import es.tributasenasturias.log.ILog;
import es.tributasenasturias.services.lanzador.client.LanzadorFactory;
import es.tributasenasturias.services.lanzador.client.ProcedimientoAlmacenado;
import es.tributasenasturias.services.lanzador.client.TLanzador;
import es.tributasenasturias.services.lanzador.client.response.RespuestaLanzador;
import es.tributasenasturias.xml.XMLDOMUtils;

// ServiciosSmartFines.Multas.ConsultaFechaHora
public class ConsultaFechaHora extends A_MultasBase  {
	
	public ConsultaFechaHora(Preferencias pref, String idLlamada, Document datosEntrada, ILog log) {
		super(pref, idLlamada, datosEntrada, log);
	}
	
	@Override
	public String Inicializar() {
		
		final String ESUN = "ESUN_ESTRUCTURA_UNIVERSAL";

		ConsultaFechaHoraResponse auxFecha = new ConsultaFechaHoraResponse();
		String datosRespuesta = null;
		
		try 
		{
			Integer idSubo = Integer.parseInt(_datosEntrada.getElementsByTagName("Suborganismo").item(0).getTextContent());		
			datosRespuesta = ConfiguracionUtils.GetTextoValidacionUDID(_pref, _idLlamada, _datosEntrada, idSubo, _log);
			
			if (datosRespuesta == null) 
			{
				TLanzador lanzador = LanzadorFactory.newTLanzador(_pref.getEndpointLanzador(), new SoapClientHandler(this._idLlamada));
				ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("APP_SITSANCIONA.OBTENERFECHAHORA", _pref.getEsquemaBD());
				Utiles.AgregarCabeceraGeneralPL(proc);
			
				String soapResponse = lanzador.ejecutar(proc);
				RespuestaLanzador response = new RespuestaLanzador(soapResponse);
				
				if (!response.esErronea() && response.getNumFilasEstructura(ESUN) > 0) 
				{
					String fechaPL = response.getValue(ESUN, 1, "C1");
					
					auxFecha.setFecha(fechaPL);
					datosRespuesta = XMLDOMUtils.Serialize(auxFecha);
				} 
				else { throw new Exception(); }		
			}
		} 
		catch (Exception ex) 
		{
			// Si hay error devolvemos fecha actual
			datosRespuesta = actualizarFecha();
		}
		
		return datosRespuesta;
	}
	
	
	private String actualizarFecha() {
		
		ConsultaFechaHoraResponse datosRespuestaAux = new ConsultaFechaHoraResponse();
		SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String strDate = format1.format((Calendar.getInstance()).getTime());
		datosRespuestaAux.setFecha(strDate);
		
		return XMLDOMUtils.Serialize(datosRespuestaAux);
	}
}
