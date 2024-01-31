package es.tributasenasturias.modelo600utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import es.tributasenasturias.docs.Base64;

public class PdfComprimidoUtils {

	public static ByteArrayOutputStream descomprimirPDF(String pdfComprimido) throws Exception{
		ByteArrayOutputStream pdfResultado = null;
		if(pdfComprimido != null){
			byte[] decoded = Base64.decode(pdfComprimido.toCharArray());
			pdfResultado = decompress(decoded);
		}		
		return pdfResultado;		
	} 

	public static String comprimirPDF(ByteArrayOutputStream pdf) throws Exception{
		String pdfComprimido = null;
		if(pdf != null){
			byte [] arrayBytes = compress(pdf.toByteArray());
			char[] caracteres64 = Base64.encode(arrayBytes);
			pdfComprimido = new String(caracteres64);
		}
		return pdfComprimido;		
	}

	private static byte[] compress(byte[] input) throws IOException{
		//String msgLog = "PdfComprimidoUtils.compress|";
		
		    // Compressor with highest level of compression
		    Deflater compressor = new Deflater();
		    compressor.setLevel(Deflater.BEST_COMPRESSION);
		    
		    // Give the compressor the data to compress
		    compressor.setInput(input);
		    compressor.finish();
		    
		    // Create an expandable byte array to hold the compressed data.
		    // It is not necessary that the compressed data will be smaller than
		    // the uncompressed data.
		    ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
		    
		    // Compress the data
		    byte[] buf = new byte[1024];
		    while (!compressor.finished()) {
		        int count = compressor.deflate(buf);
		        bos.write(buf, 0, count);
		    }
		    try {
		        bos.close();
		    } catch (IOException e) {}

		    // Get the compressed data
		    byte[] compressedData = bos.toByteArray();		    
		    return compressedData;
	}

	public static ByteArrayOutputStream decompress(byte[] data) throws DataFormatException{
		// buffer para resultado
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		// buffer para descomprimir
		byte[] buffer = new byte[1024];
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		// vuelco los bytes descomprimidos a un stream
		int bytes = 0;
		while((bytes = inflater.inflate(buffer)) > 0){
			outputStream.write(buffer, 0, bytes);
		}
		return outputStream;
	}
}