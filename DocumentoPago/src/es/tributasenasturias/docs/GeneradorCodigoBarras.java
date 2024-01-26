package es.tributasenasturias.docs;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.ws.Holder;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import es.tributasenasturias.Exceptions.ImpresionGDException;
import es.tributasenasturias.documentopagoutils.Preferencias;
import es.tributasenasturias.servicios.codigobarras.CodigoBarras;
import es.tributasenasturias.servicios.codigobarras.CodigoBarras_Service;
import es.tributasenasturias.soap.handler.HandlerUtil;

/**
 * Clase para generar un código de barras
 * @author crubencvs
 *
 */
public class GeneradorCodigoBarras {

	private Preferencias pref;
	private CodigoBarras port;
	
	/**
	 * Constructor
	 */
	public GeneradorCodigoBarras(){
		this.pref= new Preferencias();
		CodigoBarras_Service srv= new CodigoBarras_Service();
		this.port= srv.getCodigoBarrasSOAP();
	}
	/**
	 * Genera una imagen de código QR de pago por referencia para una referencia, con un ancho
	 * y en un formato de imagen determinado. 
	 * Dado que el código QR que se va a generar es cuadrado, se pasa sólo el ancho de la imagen
	 * @param referencia Referencia de la que generar el código
	 * @param anchoImagen Ancho de la imagen en píxeles. Número entero
	 * @param formatoImg Formato de la imagen (JPG, PNG)
	 * @return contenido binario de la imagen
	 */
	private byte[] generaQRPagoReferencia(String referencia, int anchoImagen, int altoImagen, String formatoImg){
		javax.xml.ws.BindingProvider bpr = (javax.xml.ws.BindingProvider) port;
		bpr.getRequestContext().put (javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,pref.getEndpointCodBarras());
		HandlerUtil.setHandlerClient((javax.xml.ws.BindingProvider) port);
		
		Holder<Integer> anchoImagenPixeles=new Holder<Integer>();
		Holder<String> formatoImagen=new Holder<String>();
		Holder<byte[]> contenidoImagen=new Holder<byte[]>();
		Holder<Integer> altoImagenPixelesOut= new Holder<Integer>();
		Holder<String> textoEquivalenteQr= new Holder<String>();
		Holder<Boolean> esError= new Holder<Boolean>();
		Holder<String>mensajeError= new Holder<String>();
		
		anchoImagenPixeles.value=anchoImagen;

		formatoImagen.value=formatoImg;
		port.generaQRPorTipo(pref.getQRPagoVoluntariaTipo(), 
							 referencia, 
							 anchoImagenPixeles, 
							 altoImagen, 
							 formatoImagen, 
							 contenidoImagen, 
							 altoImagenPixelesOut, 
							 textoEquivalenteQr, 
							 esError, 
							 mensajeError);
		if (!esError.value){
			return contenidoImagen.value;
		} else {
			return new byte[0];
		}
	}
	/**
	 * Incrusta una imagen en un pdf y lo devuelve en un stream de bytes
	 * @param referencia Referencia de la que generar el código QR
	 * @param pdf OutputStream con el contenido del pdf. En realidad debería ser siempre un ByteArrayOutputStream, ya que las clases de PDF generan eso.
	 * @param pagina Página en la que mostrar el código QR
	 * @param x Coordenada x del código QR, como entero
	 * @param y Coordenad y del código QR, como entero
	 * @param ancho Ancho en píxeles de la imagen
	 * @param alto Alto en píxeles de la imagen
	 * @return
	 * @throws ImpresionGDException
	 */
	public void incrustaCodigoQRPagoReferencia(String referencia, OutputStream pdf, int pagina, int x, int y, int ancho, int alto) throws ImpresionGDException{
		if (pdf instanceof ByteArrayOutputStream){
			try {
				ByteArrayOutputStream bPdf= (ByteArrayOutputStream) pdf;
				byte[] imagen= generaQRPagoReferencia(referencia, ancho, alto, pref.getQRPagoVoluntariaFormatoImagen());
				if (imagen.length>0){
					PdfReader reader= new PdfReader(bPdf.toByteArray());
					PdfStamper st = new PdfStamper(reader,pdf);
					Image img = Image.getInstance(imagen);
					img.setAbsolutePosition(x, y);
					st.getUnderContent(pagina).addImage(img); //UnderContent, para que no "machaque" los elementos de PDF sobre los que se imprime.
					//st.getOverContent(pagina).addImage(img);
					st.close();
				} else {
				}
			} catch (Exception e){
				throw new ImpresionGDException("Error al incrustar el código QR:" + e.getMessage(),e);
			}
		}
	}
}
