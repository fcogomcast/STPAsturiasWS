package es.tributasenasturias.services.ws.archivodigital.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
/* INIPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */
import java.util.Calendar;
import java.util.GregorianCalendar;
/* FINPETITRIBUTAS-5 ENAVARRO ** 23/10/2019 ** Compulsa */


import es.tributasenasturias.services.ws.archivodigital.exception.ArchivoException;
import es.tributasenasturias.services.ws.archivodigital.utils.Preferencias.Preferencias;

/**
 * Clase para la generaci�n del CSV
 * @author crubencvs
 *
 */
public class GeneradorCSV {
	
	private GeneradorCSV() {
		
	}
	public static class DatosCSV {
		private String hashArchivo;
		private String baseCSV;
		private String hashCSV;
		private String CSV;
		public String getHashArchivo() {
			return hashArchivo;
		}
		public void setHashArchivo(String hashArchivo) {
			this.hashArchivo = hashArchivo;
		}
		public String getBaseCSV() {
			return baseCSV;
		}
		public void setBaseCSV(String baseCSV) {
			this.baseCSV = baseCSV;
		}
		public String getHashCSV() {
			return hashCSV;
		}
		public void setHashCSV(String hashCSV) {
			this.hashCSV = hashCSV;
		}
		public String getCSV() {
			return CSV;
		}
		public void setCSV(String csv) {
			CSV = csv;
		}
		
		
	}
	
	/**
	 * Codificaci�n del resultado. Se codificar� en hexadecimal a un solo car�cter por d�gito, es decir,
	 * el "3" ser� "3" y no "03". Ser� el equivalente a una codificaci�n a hexadecimal
	 * pero incrementado los d�gitos una posici�n en el c�digo Ascii, es decir
	 * el 0 ser� el 1, el 1 el 2, etc. Se hace as� para que no exista el n�mero "0" 
	 * que podr�a llevar a confusi�n entre ese d�gito y la "O".
	 * @param aInput
	 * @return
	 */
	private static String encode( byte[] aInput){
		int MASK_15 = 0x0f;
	    StringBuilder result = new StringBuilder();
	    char[] digits = {'1', '2', '3', '4','5','6','7','8','9','A','B','C','D','E','F','G'};
	    for ( int idx = 0; idx < aInput.length; ++idx) {
	      byte b = aInput[idx];
	      result.append( digits[ b&MASK_15] ); //M�scara del 15, podr�a sustituirse por un m�dulo 16 (y no 15 porque 15%15=0). S�lo sirve para acotar en el array 
	    }
	    return result.toString();
	  }
	/**
	 * Devuelve una cadena AAAAMMDDHH24:MI:SS uso horario UTC
	 * @return
	 */
	private static String fechaActualUTC() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH:mm:ss-z");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(new Date());
	}
	/**
	 * Calcula el hash de un contenido binario seg�n un algoritmo.
	 * @param contenido Contenido binario
	 * @param algoritmo Algoritmo, en texto, de entre lo soportados por la implementaci�n: MD5, SHA-1, SHA-256, SHA-512
	 * @return bytes del hash
	 * @throws ArchivoException
	 */
	public static byte[] calcularHash(byte[] contenido, String algoritmo) throws ArchivoException{
		ByteArrayInputStream bais = new ByteArrayInputStream(contenido);
		byte []buffer= new byte[8192];
		int leido=0;
		try {
			MessageDigest md = MessageDigest.getInstance(algoritmo);
			while ((leido=bais.read(buffer))!=-1) {
				md.update(buffer,0,leido);
			}
			return md.digest();
		} catch (NoSuchAlgorithmException nsae) {
			throw new ArchivoException("Excepci�n en c�lculo de hash:" + nsae.getMessage(),nsae);
		} catch (IOException ioe) {
			throw new ArchivoException ("Error al leer el contenido del fichero para calcular hash:" + ioe.getMessage(),ioe);
		}
	}
	/**
	 * Devuelve el hash del contenido binario como una cadena en Base64.
	 * @param contenido Contenido binario
	 * @param algoritmo Algoritmo, en texto, de entra los soportados por la implementaci�n: MD5, SHA-1, SHA-256, SHA-512
	 * @return Cadena en base64 que representa el hash.
	 * @throws ArchivoException
	 */
	public static String getStringHash(byte[] contenido, String algoritmo) throws ArchivoException {
		return String.valueOf(Base64.encode(calcularHash(contenido, algoritmo)));
	}
	/**
	 * * Generaci�n del CSV de un archivo seg�n el algoritmo:<p/>
	 * 1. Calcular el hash del contenido seg�n el hash indicado en Preferencias<p/>
	 * 2. Formar el baseCSV como el hash del paso anterior, concatenado con el firmante,
	 *    , concatenado con la fecha en zona horaria UTC, concatenado con un entero aleatorio. <p/>
	 *    Utilizar� el separado "_" entre cada elemento
	 * 3. Calcular el hash de esta cadena en UTF-8 seg�n el algoritmo indicado en Preferencias "algoritmoHashArchivo" <p/>
	 * 4. Extraer� los 16 �ltimos caracteres. <p/>
	 * 5. Concatenar� el id DIR3 de STPA con las 16 �ltimas posiciones del hash de CSV, separado por un "_" <p/>     
	 * @param contenido Contenido del fichero 
	 * @param firmante Firmante Firmante del documento
	 * @param pref Preferencias. Tomar� el algoritmo de hash de archivo, el de hash de CSV, el firmante por defecto y el dir3 de organismo
	 * @return
	 * @throws ArchivoException
	 */
	public static DatosCSV generarCSV(byte[] contenido, String firmante, Preferencias pref) throws ArchivoException{
		DatosCSV datos= new DatosCSV();
		char separador='_';
		String algoritmoHashArchivo= pref.getAlgoritmoHashArchivo();
		String algoritmoHashCSV= pref.getAlgoritmoHashCSV();
		String organismo= pref.getDIR3();
		String idFirmante= firmante;
		try {
			if (firmante==null || "".equals(firmante)) {
				idFirmante= pref.getFirmantePorDefecto();
			}
			String hashArchivo=String.valueOf(Base64.encode(calcularHash (contenido, algoritmoHashArchivo))); 
			long pseudoaleatorio= (long)(Math.random()*100000000);
			String baseCSV= hashArchivo +separador + idFirmante + separador + fechaActualUTC() +separador+ pseudoaleatorio;
			/* INIPETITRIBUTAS-5 ENAVARRO ** 09/09/2020 ** Compulsa */
			if(baseCSV.length() > 100) {
				baseCSV = baseCSV.substring(baseCSV.length()-100, baseCSV.length()); // Para que su tama�o sea 100
			}
			/* FINPETITRIBUTAS-5 ENAVARRO ** 09/09/2020 ** Compulsa */
			String hashCSV= encode(calcularHash(baseCSV.getBytes("UTF-8"), algoritmoHashCSV));
			datos.hashArchivo=hashArchivo;
			datos.setBaseCSV(baseCSV);
			datos.hashCSV=hashCSV; //Devolvemos el completo, por si nos interesara.
			datos.setCSV(organismo+"-"+hashCSV.substring(hashCSV.length()-16, hashCSV.length()));
			return datos;
		} catch (ArchivoException ae) {
			throw ae;
		} catch (UnsupportedEncodingException uee) {
			throw new ArchivoException("Error en generaci�n de CSV, codificaci�n de basecsv no v�lida:" + uee.getMessage(),uee);
		}
	}
	
	/* INIPETITRIBUTAS-2 ENAVARRO ** 04/09/2019 ** Custodia */
	public static DatosCSV generarCSV(String hashFirma, String firmante, Preferencias pref) throws ArchivoException{
		DatosCSV datos= new DatosCSV();
		char separador='_';
		String algoritmoHashCSV= pref.getAlgoritmoHashCSV();
		String organismo= pref.getDIR3();
		String idFirmante= firmante;
		try {
			if (firmante==null || "".equals(firmante)) {
				idFirmante= pref.getFirmantePorDefecto();
			}
			long pseudoaleatorio= (long)(Math.random()*100000000);
			String baseCSV= hashFirma +separador + idFirmante + separador + fechaActualUTC() +separador+ pseudoaleatorio;
			baseCSV = baseCSV.substring(baseCSV.length()-100, baseCSV.length()); // Para que su tama�o sea 100
			String hashCSV= encode(calcularHash(baseCSV.getBytes("UTF-8"), algoritmoHashCSV));
			datos.hashArchivo=hashFirma;
			datos.setBaseCSV(baseCSV);
			datos.hashCSV=hashCSV; //Devolvemos el completo, por si nos interesara.
			datos.setCSV(organismo+"-"+hashCSV.substring(hashCSV.length()-16, hashCSV.length()));
			return datos;
		} catch (ArchivoException ae) {
			throw ae;
		} catch (UnsupportedEncodingException uee) {
			throw new ArchivoException("Error en generaci�n de CSV, codificaci�n de basecsv no v�lida:" + uee.getMessage(),uee);
		}
	}
	/* FINPETITRIBUTAS-2 ENAVARRO ** 04/09/2019 ** Custodia */
	/* INIPETITRIBUTAS-5 ENAVARRO ** 22/10/2019 ** Compulsa */
	public static String obtenerFechaActual() {
        //Instanciamos el objeto Calendar
        //en fecha obtenemos la fecha y hora del sistema
        Calendar fecha = new GregorianCalendar();
        //Obtenemos el valor del a�o, mes, d�a,
        //hora, minuto y segundo del sistema
        //usando el m�todo get y el par�metro correspondiente
        String a�o = comprobarTamAdecuado(4, fecha.get(Calendar.YEAR));
        int auxMes = fecha.get(Calendar.MONTH);
        auxMes++;
        String mes = comprobarTamAdecuado(2, auxMes);
        String dia = comprobarTamAdecuado(2, fecha.get(Calendar.DAY_OF_MONTH));
        String hora = comprobarTamAdecuado(2, fecha.get(Calendar.HOUR_OF_DAY));
        String minuto = comprobarTamAdecuado(2, fecha.get(Calendar.MINUTE));
        String segundo = comprobarTamAdecuado(2, fecha.get(Calendar.SECOND));
        String actual = dia+"/"+(mes+1)+"/"+a�o+" "+hora+":"+minuto+":"+segundo;
        return actual;
    }
	
	public static String comprobarTamAdecuado(int tam, int num) {
		String numCadena = String.valueOf(num);
		if(numCadena.length() < tam) {
			for(int i = numCadena.length(); i < tam; i++) {
				numCadena = "0" + numCadena;
			}	
		}
		return numCadena;
	}
	/* FINPETITRIBUTAS-5 ENAVARRO ** 22/10/2019 ** Compulsa */
	
}
