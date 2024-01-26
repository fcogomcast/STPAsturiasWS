package es.stpa.smartmultas.clases;

import org.w3c.dom.Document;

import es.stpa.smartmultas.configuracion.utils.Utiles;
import es.stpa.smartmultas.entidades.FileStatusData;
import es.stpa.smartmultas.preferencias.Preferencias;
import es.stpa.smartmultas.soap.SoapClientHandler;
import es.tributasenasturias.log.ILog;
import es.tributasenasturias.services.lanzador.client.LanzadorFactory;
import es.tributasenasturias.services.lanzador.client.ParamType;
import es.tributasenasturias.services.lanzador.client.ProcedimientoAlmacenado;
import es.tributasenasturias.services.lanzador.client.TLanzador;
import es.tributasenasturias.services.lanzador.client.response.RespuestaLanzador;
import es.tributasenasturias.xml.XMLDOMUtils;

// ServiciosSmartFines.Multas.GetFileStatus
public class GetFileStatus extends A_MultasBase {
	
	final static String WTEA = "WTEA_ESTRUCTURA_ARQUITECTURA";
	
	public GetFileStatus(Preferencias pref, String idLlamada, Document datosEntrada, ILog log) {
		super(pref, idLlamada, datosEntrada, log);
	}
	
	@Override
	public String Inicializar() { 	// GetFileStatus(FileStatusData fileStatus)
		
		FileStatusData custodiado = null;
		FileStatusData datosEntrada = null;
		
		try
		{
			String tmpXML = XMLDOMUtils.getXMLText(_datosEntrada);

			datosEntrada = XMLDOMUtils.XMLtoObject(tmpXML, FileStatusData.class);
			custodiado = new FileStatusData(datosEntrada.getIdFichero(), "N", "No encontrado"); // valor por defecto si no encontramos el registro
			
			TLanzador lanzador = LanzadorFactory.newTLanzador(_pref.getEndpointLanzador(), new SoapClientHandler(_idLlamada));
			ProcedimientoAlmacenado proc = new ProcedimientoAlmacenado("APP_SITSANCIONA.OBTENER_ESTADO_FICHERO", _pref.getEsquemaBD());
			Utiles.AgregarCabeceraGeneralPL(proc);
			
			proc.param(datosEntrada.getIdFichero(), ParamType.CADENA);
		
			String soapResponse = lanzador.ejecutar(proc);
			RespuestaLanzador response = new RespuestaLanzador(soapResponse);
			
			if (!response.esErronea() && response.getNumFilasEstructura(WTEA) > 0) 
			{				
				String idFicheroCus = response.getValue(WTEA, 1, "C1");
				String custodiadoCus = response.getValue(WTEA, 1, "C2");
				String errorCus = response.getValue(WTEA, 1, "C3");
				custodiado = new FileStatusData(idFicheroCus, custodiadoCus, errorCus);
			}	
		} 
		catch (Exception ex) 
		{
			custodiado = new FileStatusData(datosEntrada.getIdFichero(), "N", ex.getMessage());
		}
		
		return XMLDOMUtils.Serialize(custodiado);
	}
}
