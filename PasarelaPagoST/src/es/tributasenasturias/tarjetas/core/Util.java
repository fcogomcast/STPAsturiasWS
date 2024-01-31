package es.tributasenasturias.tarjetas.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.tributasenasturias.tarjetas.core.db.PLPasarelaRepositoryException;

public class Util {
	private Util() {}
	/**
	 * Convierte una cadena conteniendo una fecha
	 * @param fecha
	 * @param formato
	 * @return
	 * @throws PLPasarelaRepositoryException
	 */
	public static Date convertToDate(String fecha, String formato) throws PLPasarelaRepositoryException{
		if (fecha==null || "".equals(fecha)){
			return null;
		}
		SimpleDateFormat sd = new SimpleDateFormat(formato);
		try {
			return sd.parse(fecha);
		} catch (ParseException p){
			throw new PLPasarelaRepositoryException("No se ha podido procesar la fecha " + fecha + " con el formato "+ formato);
		}
	}
	/**
	 * Convierte una fecha a una cadena con un formato
	 * @param fecha
	 * @param formato
	 * @return
	 */
	public static String convertFromDate(Date fecha, String formato) {
		if (fecha==null || "".equals(fecha)){
			return null;
		}
		SimpleDateFormat sd = new SimpleDateFormat(formato);
		return sd.format(fecha);
	}
}
