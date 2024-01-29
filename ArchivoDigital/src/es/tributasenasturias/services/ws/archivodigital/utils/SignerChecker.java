package es.tributasenasturias.services.ws.archivodigital.utils;

/**
 * Comprueba si un tipo de fichero admite firma. 
 * @author crubencvs
 *
 */
public class SignerChecker {

	private SignerChecker() {
	}
	/**
	 * Recupera la extensión de un fichero
	 * @param nombreFichero Nombre de fichero
	 * @return Cadena con la extensión, sin el "."
	 */
	private static String getExtensionFichero(String nombreFichero) {
		String temp= nombreFichero.replace('\\', '/');
		if (temp.indexOf(".")==-1) { //Sin extensión no podemos saber el tipo
			return null;
		}
		if (temp.indexOf("/")!=-1) {
			temp = temp.substring(temp.lastIndexOf("/")+1);
		}
		String extension= temp.substring(temp.lastIndexOf(".")+1).toUpperCase();
		return extension;
	}
	/**
	 * Coprueba si el fichero es un tipo que admite firma por CSV. Por el momento 
	 * sólo por nombre de fichero.
	 * @param nombreFichero Nombre del fichero a comprobar
	 * @return true si admite firma por CSV, false si no
	 */
	public static boolean admiteFirmaCSV(String nombreFichero){
		String extension= getExtensionFichero(nombreFichero);
		if ("PDF".equalsIgnoreCase(extension)){
			return true;
		}
		return false;
	}
	/**
	 * Comprueba si el fichero es un tipo que admite firma por certificado. 
	 * Por el momento sólo por nombre de fichero
	 * @param nombreFichero Nombre de fichero a comprobar
	 * @return true si admite firma por CSV, false si no
	 */
	public static boolean admiteFirmaCertificado(String nombreFichero) {
		String extension= getExtensionFichero(nombreFichero);
		if ("PDF".equalsIgnoreCase(extension)) {
			return true;
		}
		
		return false;
	}
}
