package es.tributasenasturias.webservice.escriturasUtils;
/**
 * Utilidades de cadena
 * @author crubencvs
 *
 */
public class StringUtil {
	public static String toOneLine(String multiline) {
	    String[] lines = multiline.split(System.getProperty("line.separator"));
	    StringBuilder builder = new StringBuilder();
	    builder.ensureCapacity(multiline.length()); 
	    for(String line : lines) builder.append(line);
	    return builder.toString();
	}
}
