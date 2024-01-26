package es.stpa.smartmultas.clases;

import org.w3c.dom.Document;

//import com.openalpr.jni.Alpr;
//import com.openalpr.jni.AlprPlate;
//import com.openalpr.jni.AlprPlateResult;
//import com.openalpr.jni.AlprResults;

import es.stpa.smartmultas.configuracion.utils.ConfiguracionUtils;
import es.stpa.smartmultas.configuracion.utils.Utiles;
import es.stpa.smartmultas.preferencias.Preferencias;
import es.stpa.smartmultas.responses.ConsultaMatriculaOCRResponse;
import es.tributasenasturias.log.ILog;
import es.tributasenasturias.xml.XMLDOMUtils;

// ServiciosSmartFines.Multas.ConsultaMatriculaOCR
public class ConsultaMatriculaOCR extends A_MultasBase { //revisar
	
	public ConsultaMatriculaOCR(Preferencias pref, String idLlamada, Document datosEntrada, ILog log) {
		super(pref, idLlamada, datosEntrada, log);
	}
	
	@Override
	public String Inicializar() {
		
		String datosRespuesta = null;
		String matricula = "No encontrada";
		
		Integer idSubo = Integer.parseInt(_datosEntrada.getElementsByTagName("Suborganismo").item(0).getTextContent());		
		datosRespuesta = ConfiguracionUtils.GetTextoValidacionUDID(_pref, _idLlamada, _datosEntrada, idSubo, _log);
		
		if(datosRespuesta == null)
		{			
			try 
			{	
				// Pasamos a la librería de OPENALPR la imagen con la matrícula que hemos recibido  
				// String nombre = _datosEntrada.getElementsByTagName("Nombre").item(0).getTextContent();
				String base64 = _datosEntrada.getElementsByTagName("Base64").item(0).getTextContent();	
		
				matricula = consultarMatriculaOCR(base64);
					
				datosRespuesta = XMLDOMUtils.Serialize(new ConsultaMatriculaOCRResponse (matricula));
			}
			catch (Exception ex) 
			{
				datosRespuesta = Utiles.MensajeErrorXml(1, "Error al buscar matrícula.", _log);	
			}
		}

		return datosRespuesta;
	}
	
	
	private String consultarMatriculaOCR(String matricula) {
		
		
		String resultado = "No encontrada";
		
		try
		{
			// Pasamos a la librería de OPENALPR la imagen con la matrícula que hemos recibido
			/*Alpr alpr = new Alpr("eu", "/openalpr.conf", "/runtime_data");

			if(alpr.isLoaded())
			{
				AlprResults results = alpr.recognize(matricula);
				
				// Si encontramos alguna matrícula, devolvemos la primera que tengamos
				for (AlprPlateResult result : results.getPlates())
				{
					// Set top N candidates returned to 20
				    for (AlprPlate plate : result.getTopNPlates()) 
				    {
				        if (plate.isMatchesTemplate())
				        {
				        	resultado = plate.getCharacters();
				        }
				    }
				}
			}	*/		
		}
		catch (Exception ex) 
		{

		}
		
		return resultado;
	}
}
