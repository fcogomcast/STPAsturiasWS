package es.tributasenasturias.pdf.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;

import es.tributasenasturias.pdf.utils.Preferencias;
import es.tributasenasturias.pdf.webservice.modelo600.Modelo600Parser;

@WebService
public class ModelosPDF {

	

	@WebMethod
	public String createPDF600(String dato) {
		Preferencias prefs = new Preferencias();
		prefs.CompruebaFicheroPreferencias();
		Modelo600Parser modelo=new Modelo600Parser(dato);
		return modelo.getModeloPDF(prefs);
	}
}