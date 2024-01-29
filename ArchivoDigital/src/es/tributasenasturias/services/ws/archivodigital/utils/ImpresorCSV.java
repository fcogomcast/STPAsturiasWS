package es.tributasenasturias.services.ws.archivodigital.utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import es.tributasenasturias.services.ws.archivodigital.exception.ArchivoException;
/**
 * Clase que modificará un documento y le añadirá un CSV. Por el momento se 
 * añade solo a los PDF.
 * @author crubencvs
 *
 */
public class ImpresorCSV {

	private final static int FONT_SIZE=9;
	private ImpresorCSV(){
	}
	/**
	 * Recupera la línea de CSV formateada.
	 * @param textoLineaCSV Texto de la línea del CSV tal como se parametriza en el servicio
	 * 						La línea se espera que sea texto #CSV#  texto
	 * @param CSV Valor del CSV
	 * @return Objeto de línea a imprimir
	 * @throws DocumentException
	 */
	private static Phrase getLineaCSV(String textoCSV, String CSV) throws DocumentException, IOException{
		Phrase phr= new Phrase();
		Pattern pt = Pattern.compile("(.*)(#CSV#)(.*)");
		Matcher mc= pt.matcher(textoCSV);
		if (mc.matches()){
			for (int m=1;m<=mc.groupCount();m++) {
				String texto=mc.group(m);
				Chunk c=null;
				if ("#CSV#".equalsIgnoreCase(texto)){
					c = new Chunk(CSV, new Font(BaseFont.createFont("Helvetica-Bold", BaseFont.WINANSI, BaseFont.NOT_EMBEDDED),FONT_SIZE));
				} else
				{
					c = new Chunk(texto, new Font(BaseFont.createFont("Helvetica", BaseFont.WINANSI, BaseFont.NOT_EMBEDDED),FONT_SIZE));
				}
				if (c!=null) {
					phr.add(c);
				}
			}
		} else
		{
			Chunk c = new Chunk(textoCSV, new Font(BaseFont.createFont("Helvetica", BaseFont.WINANSI, BaseFont.NOT_EMBEDDED), FONT_SIZE));
			phr.add(c);
		}
		return phr;
	}
	/**
	 * Añade el CSV a cada página del documento PDF
	 * @param contenido Contenido del documento
	 * @param textoCSV Texto de la línea de CSV. Vendrá en el formato texto #CSV# texto. 
	 * @param CSV CSV
	 * @return bytes del archivo modificado
	 * @throws ArchivoException
	 */
	public static byte[] addPdfCsv(byte[] contenido, 
								    String textoCSV, 
								   String CSV,
								   //CRUBENCS 43599 21/10/2021
								   Float posXCsv,
								   Float posYCsv,
								   List<Integer> paginasCsv
								   ) throws ArchivoException{
		try   {
			//CRUBENCVS 43599. 21/10/2021. Posición dinámica del CSV
			Float posX=posXCsv;
			Float posY=posYCsv;
			List<Integer> paginas= paginasCsv;
			//FIN CRUBENCVS 43599
			if (paginas==null){
				paginas= new ArrayList<Integer>();
			}
			PdfReader reader= new PdfReader(contenido);
			ByteArrayOutputStream bos= new ByteArrayOutputStream(65536);
			PdfStamper s = new PdfStamper(reader, bos);
			Phrase p= getLineaCSV(textoCSV, CSV);
			int nofPages=reader.getNumberOfPages();
			for (int i=1; i<=nofPages;i++) {
				//CRUBENCVS 43599 21/10/2021. Sólo imprimimos las páginas en la lista,
				//si hay alguna. Si no, pues todas
				if (paginas.size()==0 || paginas.contains(Integer.valueOf(i))){
					PdfContentByte pb = s.getOverContent(i);
					Rectangle box= reader.getPageSize(i);
					if (posX==null){
						posX=box.getWidth()/2;
					}
					if (posY==null){
						posY= Float.valueOf(20f);
					}
					ColumnText.showTextAligned(pb, Element.ALIGN_CENTER,p ,posX, posY, 0);
				}
			}
			s.close();
			reader.close();
			
			return bos.toByteArray();
		} catch (IOException ioe) {
			throw new ArchivoException ("Error en impresión de CSV:" + ioe.getMessage(),ioe);
		}  catch (DocumentException de) {
			throw new ArchivoException ("Error en Impresión de CSV:" + de.getMessage(),de);
		}
	}
}
