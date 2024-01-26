package es.tributasenasturias.servicios.codigobarras;

import java.io.ByteArrayOutputStream;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import es.tributasenasturias.servicios.codigobarras.preferencias.Preferencias;

public class CodigoQRImpl {

	Preferencias pref;
	public CodigoQRImpl(Preferencias preferencias){
		this.pref= preferencias;
	}
	
	public static class DatosQR{
		private boolean error;
		private byte[] imagen;
		//Píxeles
		private int anchoImagen;
		private int altoImagen;
		private String formatoImagen;
		public boolean esError() {
			return error;
		}
		public void setError(boolean error) {
			this.error = error;
		}
		public byte[] getImagen() {
			return imagen;
		}
		public void setImagen(byte[] imagen) {
			this.imagen = imagen;
		}
		public int getAnchoImagen() {
			return anchoImagen;
		}
		public void setAnchoImagen(int anchoImagen) {
			this.anchoImagen = anchoImagen;
		}
		public int getAltoImagen() {
			return altoImagen;
		}
		public void setAltoImagen(int altoImagen) {
			this.altoImagen = altoImagen;
		}
		public String getFormatoImagen() {
			return formatoImagen;
		}
		public void setFormatoImagen(String formatoImagen) {
			this.formatoImagen = formatoImagen;
		}
		
	}
	
	/**
	 * Genera un código QR en función de los datos que se le pasan
	 * @param contenido Contenido a codificar
	 * @param ancho Ancho de la imagen resultante, en píxeles. Puede incluir un margen.
	 * @param alto Alto de la imagen resultante. Por el momento no se utiliza, y es la misma que el ancho
	 * @param formato Formato de la imagen. Si no se indica, se recupera en un formato predeterminado.
	 * @return Datos de la imagen, {@link DatosQR}
	 * @throws Exception
	 */
	public DatosQR generarQR (String contenido, int ancho, int alto, String formato) throws Exception{
		DatosQR datos= new DatosQR();
		QRCodeWriter qcw = new QRCodeWriter();

		int anchoImagen;
		int altoImagen;
		if (ancho!=0){
			anchoImagen=ancho;
		} else {
			anchoImagen= Integer.parseInt(pref.getAnchoImagenPorDefectoQR());
		}
		altoImagen= anchoImagen; //Por el momento, el ancho de la imagen es igual al alto.
		
		String formatoImagen;
		if (formato!=null && !"".equals(formato)){
			formatoImagen= formato;
		} else {
			formatoImagen= pref.getFormatoImagenPorDefectoQR();
		}
		BitMatrix b = qcw.encode(contenido, BarcodeFormat.QR_CODE, anchoImagen, altoImagen );
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(b, formatoImagen, baos);
		
		datos.setError(false);
		datos.setImagen(baos.toByteArray());
		datos.setAltoImagen(altoImagen);
		datos.setAnchoImagen(anchoImagen);
		datos.setFormatoImagen(formatoImagen);
		return datos;
	}
	
}
