package es.stpa.smartmultas.clases;

import org.w3c.dom.Document;

import es.stpa.smartmultas.configuracion.utils.ConfiguracionUtils;
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

// ServiciosSmartFines.Multas.ArchivarExpediente
public class ArchivarExpediente extends A_MultasBase {
	
	final String ESUN = "ESUN_ESTRUCTURA_UNIVERSAL";
	
	public ArchivarExpediente(Preferencias pref, String idLlamada, Document datosEntrada, ILog log) {
		super(pref, idLlamada, datosEntrada, log);
	}
	
	@Override
	public String Inicializar() {
		
		String datosRespuesta = null;
		
		boolean tienePermisos = ConfiguracionUtils.ValidarPermisos(_pref, _idLlamada, "PERMISOS.ARCHIVAR");
		
		if(!tienePermisos)
		{
			datosRespuesta = Utiles.MensajeErrorXml(1, "El usuario no tiene permiso para utilizar esta opción.", _log);
		}
		else
		{
			Integer idSubo = Integer.parseInt(_datosEntrada.getElementsByTagName("Suborganismo").item(0).getTextContent());		
			datosRespuesta = ConfiguracionUtils.GetTextoValidacionUDID(_pref, _idLlamada, _datosEntrada, idSubo, _log);
			
			if(datosRespuesta == null)
			{	
				try 
				{
					// Date fechaCobro = new Date(Long.MIN_VALUE);	
					datosRespuesta = ObtenerDatosActuacion();
					
					if(datosRespuesta == null) // Si todo ha ido bien grabamos el archivo
					{
						datosRespuesta = grabarArchivoProvisional(); 
							
						if(datosRespuesta == null)
						{
							datosRespuesta = XMLDOMUtils.Serialize(new SmartfinesResponse("Ok"));
						}
					}
				}
				catch (Exception ex) 
				{
					datosRespuesta = Utiles.MensajeErrorXml(1, "Error al archivar el expediente: " + ex.getMessage(), _log);
				}
			} 
		}
		
		return datosRespuesta;
	}
	
	private String ObtenerDatosActuacion(){
		
		String codtact = "ARP";
		String infoExtra = "1";
		String datosResultado = null;
		
		try
		{
			String numExpe = _datosEntrada.getElementsByTagName("NumRege").item(0).getTextContent();
			
			TLanzador lanzador = LanzadorFactory.newTLanzador(_pref.getEndpointLanzador(), new SoapClientHandler(this._idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("EXPTRAFICO_ACTUACIONES.OBTENERDATOSACTUACION", _pref.getEsquemaBD());
			Utiles.AgregarCabeceraGeneralPL(proc);
			
			proc.param(codtact, ParamType.CADENA);
			proc.param(numExpe, ParamType.CADENA);
			proc.param(infoExtra, ParamType.CADENA);
			proc.param(null, ParamType.CADENA);
			proc.param(null, ParamType.CADENA);
			proc.param("P", ParamType.CADENA);
						
			String soapResponse = lanzador.ejecutar(proc);
			RespuestaLanzador response = new RespuestaLanzador(soapResponse);
			
			if (!response.esErronea() && response.getNumFilasEstructura(ESUN) > 0) 
			{
				String archivado = response.getValue(ESUN, 1, "C1");
				if(archivado.equals("N"))
				{
					datosResultado = Utiles.MensajeErrorXml(1, "Error al archivar el expediente: " + response.getValue(ESUN, 1, "C2"), _log);
				}
				else
				{
					String respGrabar = grabarArchivoProvisional();
					
					if(respGrabar == null)
					{
						datosResultado = XMLDOMUtils.Serialize(new SmartfinesResponse("Ok"));
					}
					else
					{
						throw new Exception(respGrabar);
					}
				}
			}
			else
			{
				datosResultado = Utiles.MensajeErrorXml(1, response.getTextoError(), _log);
			}
		}
		catch (Exception ex) 
		{
			datosResultado = Utiles.MensajeErrorXml(1, "Error al archivar el expediente: " + ex.getMessage(), _log);
		}
		
		return datosResultado;
	}
	
	
	private String grabarArchivoProvisional(){
	
		String respGrabar = null;
		
		try
		{
			String numExpe = _datosEntrada.getElementsByTagName("NumRege").item(0).getTextContent();
			String motivo = _datosEntrada.getElementsByTagName("Motivo").item(0).getTextContent();
			
			TLanzador lanzador = LanzadorFactory.newTLanzador(_pref.getEndpointLanzador(), new SoapClientHandler(this._idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("EXPTRAFICO_ACTUACIONES.GRABARARCHIVOPROVISIONAL", _pref.getEsquemaBD());
			Utiles.AgregarCabeceraGeneralPL(proc);
			
			proc.param(numExpe, ParamType.CADENA);
			proc.param(motivo, ParamType.CADENA);
						
			String soapResponse = lanzador.ejecutar(proc);
			RespuestaLanzador response = new RespuestaLanzador(soapResponse);
			
			if(!response.esErronea() && response.getNumFilasEstructura(ESUN) > 0 && response.getValue(ESUN, 1, "C1").toUpperCase().equals("OK"))
			{
				// Si no ha dado error ha ido correctamente
			}
			else
			{
				throw new Exception(response.getTextoError());
			}
		}
		catch (Exception ex) 
		{
			respGrabar = ex.getMessage();
		}
		
		return respGrabar;
	}
}
