package es.stpa.smartmultas.clases;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import es.stpa.smartmultas.configuracion.utils.ConfiguracionUtils;
import es.stpa.smartmultas.configuracion.utils.Utiles;
import es.stpa.smartmultas.entidades.ExpedientesList;
import es.stpa.smartmultas.preferencias.Preferencias;
import es.stpa.smartmultas.responses.ExpedientePdtRatificar;
import es.stpa.smartmultas.responses.ExpedientesPdtRatificarListResponse;
import es.stpa.smartmultas.responses.SmartfinesResponse;
import es.stpa.smartmultas.soap.SoapClientHandler;
import es.tributasenasturias.log.ILog;
import es.tributasenasturias.services.lanzador.client.LanzadorFactory;
import es.tributasenasturias.services.lanzador.client.ParamType;
import es.tributasenasturias.services.lanzador.client.ProcedimientoAlmacenado;
import es.tributasenasturias.services.lanzador.client.TLanzador;
import es.tributasenasturias.services.lanzador.client.response.RespuestaLanzador;
import es.tributasenasturias.xml.XMLDOMUtils;

public class ConsultaExpedientesPendientesRatificar extends A_MultasBase {
	
	public ConsultaExpedientesPendientesRatificar(Preferencias pref, String idLlamada, Document datosEntrada, ILog log) {
		super(pref, idLlamada, datosEntrada, log);
	}
	
	@Override
	public String Inicializar() {
		
		String datosRespuesta = null;
		
		final String ESUN = "ESUN_ESTRUCTURA_UNIVERSAL";
		final String NUME = "NUME_NUMERO";
		
		try 
		{				
			String agente = _datosEntrada.getElementsByTagName("Agente").item(0).getTextContent();
			String contador = _datosEntrada.getElementsByTagName("Contador").item(0).getTextContent();
			
			TLanzador lanzador = LanzadorFactory.newTLanzador(_pref.getEndpointLanzador(), new SoapClientHandler(_idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("EXPTRAFICO_ACTUACIONES.PENDIENTESRATIFICAR", _pref.getEsquemaBD());
			Utiles.AgregarCabeceraGeneralPL(proc);
			
			proc.param(agente, ParamType.CADENA);
		
			String soapResponse = lanzador.ejecutar(proc);
			RespuestaLanzador response = new RespuestaLanzador(soapResponse);
			
			if (!response.esErronea()) 
			{				
				if(Boolean.parseBoolean(contador) && response.getNumFilasEstructura(NUME) > 0)
				{
					SmartfinesResponse datosRespuestaAux = new SmartfinesResponse(response.getValue(NUME, 1, "NUMERO_NUME"));

					datosRespuesta = XMLDOMUtils.Serialize(datosRespuestaAux);					
				}
				else if(!Boolean.parseBoolean(contador)  && response.getNumFilasEstructura(ESUN) > 0)
				{
					ExpedientesPdtRatificarListResponse respExpedientes = new ExpedientesPdtRatificarListResponse();
					ExpedientesList listado = new ExpedientesList();
					List<ExpedientePdtRatificar> expedientes = new ArrayList<ExpedientePdtRatificar>();
					
					int numFilas = response.getNumFilasEstructura(ESUN);
					for (int i = 1; i <= numFilas; i++) 
					{
						String auxGdel = response.getValue(ESUN, i, "N4");
						String auxGdre = response.getValue(ESUN, i, "N5");
						
						ExpedientePdtRatificar exp  = new ExpedientePdtRatificar(
								response.getValue(ESUN, i, "C1"),	// NumExp
								response.getValue(ESUN, i, "C2"),	// ModeloVehiculo
								response.getValue(ESUN, i, "C3"),	// Matricula
								response.getValue(ESUN, i, "C4"),	// Marca
								response.getValue(ESUN, i, "C5"),	// Nif
								response.getValue(ESUN, i, "C6"),	// Tipo
								response.getValue(ESUN, i, "N1"),	// IdEper
								response.getValue(ESUN, i, "N3"),	// IdEperPropietario
								auxGdel,							// Gdel
								auxGdre,							// Gdre
								response.getValue(ESUN, i, "C7")	// PropietarioNombre
						);
						
						String base64 = ConfiguracionUtils.obtenerReimpresionPorGdre(_pref, _idLlamada, auxGdre);
						
						if(base64 != null && !base64.isEmpty()) 
						{
							exp.setPdf(base64);
						}
						
						expedientes.add(exp);
					}
					
					if(numFilas > 0)
					{
						listado.setItems(expedientes);
						respExpedientes.setExpedientes(listado);
					}
					
					datosRespuesta = XMLDOMUtils.Serialize(respExpedientes);
				}
				else
				{
					datosRespuesta = Utiles.MensajeErrorXml(1, "No se indicó el modo para la recuperación de los datos.", _log);
				}
			} 
			else 
			{
				datosRespuesta = Utiles.MensajeErrorXml(1, response.getTextoError(), _log);
			}			
		} 
		catch (Exception ex) 
		{
			datosRespuesta = Utiles.MensajeErrorXml(1, ex.getMessage(), _log);
		}
		
		return datosRespuesta;
	}
}
