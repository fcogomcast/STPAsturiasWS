package es.stpa.smartmultas.clases;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import es.stpa.smartmultas.configuracion.utils.ConfiguracionUtils;
import es.stpa.smartmultas.configuracion.utils.Utiles;
import es.stpa.smartmultas.entidades.Propiedad;
import es.stpa.smartmultas.preferencias.Preferencias;
import es.stpa.smartmultas.responses.AltaDocumentoBoletinMultasResponse;
import es.tributasenasturias.log.ILog;
import es.tributasenasturias.xml.XMLDOMUtils;

// ServiciosSmartFines.Multas.AltaDocumentoBoletinMultas
public class AltaDocumentoBoletinMultas extends A_MultasBase {
	
	public Integer Suborga = null;
	
	public AltaDocumentoBoletinMultas(Preferencias pref, String idLlamada, Document datosEntrada, Integer suborga, ILog log) {
		super(pref, idLlamada, datosEntrada, log);
		Suborga = suborga;
	}
	
	@Override
	public String Inicializar() {

		String datosRespuesta = null;

		String identificador = AltaDocumentoMultas(); // Debe devolver el idAdar o error
		
		if(identificador.toLowerCase().contains("error"))
		{
			datosRespuesta = identificador;
		}
		else
		{
			datosRespuesta = XMLDOMUtils.Serialize(new AltaDocumentoBoletinMultasResponse(identificador));
		}
		
		return datosRespuesta;
	}
	
	
	private String AltaDocumentoMultas() {
							
		String datosResultado = ConfiguracionUtils.GetTextoValidacionUDID(_pref, _idLlamada, _datosEntrada, Suborga, _log);
		Propiedad idAdarRes = new Propiedad();

		if (datosResultado == null) 
		{
			try 
			{
				//se modifica para que en lugar de jfif ponga jpg y se vea correctamente la imagen en el sit
				String nomFichero = (_datosEntrada.getElementsByTagName("NombreFichero").item(0).getTextContent() + ".jpg"); 
				String identificador = _datosEntrada.getElementsByTagName("NumeroBoletin").item(0).getTextContent();
				Node auxImgBoletin = _datosEntrada.getElementsByTagName("ImagenBoletin").item(0); 
				Boolean imagenBoletin = (auxImgBoletin != null) ? Boolean.parseBoolean(auxImgBoletin.getTextContent()) : false;
		        // Si el nombre del documento que recibimos empieza por justificante_ se trata del impreso que se le entrega
		        // al denunciante en una denuncia voluntaria y el tipo de documento debe ser J
				String tipoDocumento = nomFichero.toLowerCase().startsWith("justificante_") ? "J" : imagenBoletin ? "B" : "F" ;
				String base64 = _datosEntrada.getElementsByTagName("ImagenB64").item(0).getTextContent(); 
				
				String auxResultado = ConfiguracionUtils.CustodiarArchivo(_pref, _idLlamada, _log, _datosEntrada, identificador, "IMAG", tipoDocumento, nomFichero, base64, idAdarRes);
				datosResultado = (auxResultado != null && !auxResultado.toLowerCase().contains("error")) ? idAdarRes.getValor() : auxResultado; 	// Obtendremos aqui el idadar o error
			} 
			catch (Exception ex) 
			{
				datosResultado = Utiles.MensajeErrorXml(1, "Error al custodiar la imagen", _log);
			}
		}
		else
		{
			datosResultado = "Error: " + datosResultado;
		}
		
		return datosResultado;
	}
}
