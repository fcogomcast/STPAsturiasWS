package es.tributasenasturias.webservice;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import es.tributasenasturias.pagopresentacionmodelo600utils.PdfComprimidoUtils;


public class TestDocumentos {	
	public static void main(String[] args) {
		final String fichOrigen  = "Q:\\noexiste_archivos\\David\\FicheroBase64.txt";
	    final String fichDestino = "Q:\\noexiste_archivos\\David\\ficherodestino3.pdf";		
		ByteArrayOutputStream resultado = new ByteArrayOutputStream();
		try {
			FileOutputStream fichero = new FileOutputStream(fichDestino);
			int ch;
			StringBuffer str = new StringBuffer();
			FileInputStream ficheroBase64 = new FileInputStream(fichOrigen);
			while ((ch = ficheroBase64.read()) != -1)
				str.append((char) ch);
			resultado = PdfComprimidoUtils.descomprimirPDF(new String(str));
			fichero.write(resultado.toByteArray());
			System.out.println("Fichero generado"); 
			fichero.close();
		} catch (Exception e) {
			System.out.println("Error:" + e.getMessage());
		}
	}
}
