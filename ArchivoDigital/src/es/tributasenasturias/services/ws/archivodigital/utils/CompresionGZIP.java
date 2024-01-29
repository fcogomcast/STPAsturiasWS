package es.tributasenasturias.services.ws.archivodigital.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import es.tributasenasturias.services.ws.archivodigital.exception.ArchivoException;

/**
 * Utilidades de compresión/descompresión de GZIP
 *  * @author crubencvs
 *
 */
public class CompresionGZIP {

	private CompresionGZIP(){
	}
	/**
	 * Comprime en GZIP los datos que se le pasen.
	 * @param origen array de bytes con los datos originales
	 * @return array de bytes con los datos comprimidos.
	 * @throws ArchivoException
	 */
	public static byte[] comprimir(byte[] origen) throws ArchivoException{
		ByteArrayOutputStream comprimido= new ByteArrayOutputStream();
		ByteArrayInputStream entrada= new ByteArrayInputStream(origen);
		byte[] buffer= new byte[8192];
		int leido=0;
		try {
			GZIPOutputStream gzip = new GZIPOutputStream(comprimido);
			while ((leido=entrada.read(buffer))!=-1 ) {
				gzip.write(buffer,0,leido);
			}
			gzip.close();
			return comprimido.toByteArray();
		} catch (IOException io) {
			throw new ArchivoException ("Error al comprimir el contenido del archivo:" + io.getMessage());
		}
	}
	/**
	 * Descomprime datos que se le pasan en GZIP
	 * @param origen Array de bytes con los datos comprimidos
	 * @return  Representación en bytes de los datos descomprimidos
	 * @throws ArchivoException
	 */
	public static byte[] descomprimir(byte[] origen) throws ArchivoException{
		ByteArrayOutputStream descomprimido= new ByteArrayOutputStream();
		ByteArrayInputStream entrada= new ByteArrayInputStream(origen);
		byte[] buffer= new byte[8192];
		int leido=0;
		try  {
			GZIPInputStream gzip = new GZIPInputStream(entrada);
			while ((leido=gzip.read(buffer))!=-1 ) {
				descomprimido.write(buffer,0,leido);
			}
			gzip.close();
			return descomprimido.toByteArray();
		} catch (IOException ioe) {
			throw new ArchivoException ("Error al descomprimir el contenido del archivo:"+ ioe.getMessage());
		}
	}
}
