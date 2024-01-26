package es.tributasenasturias.documentopagoutils;


import es.tributasenasturias.documentopagoutils.NumberUtil;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import es.tributasenasturias.documentopagoutils.StpawsException;
import es.tributasenasturias.documentopagoutils.DateUtil;
import es.tributasenasturias.documentopagoutils.InorInfoOrga;
import es.tributasenasturias.documentopagoutils.ListadoUtils;
import es.tributasenasturias.documentopagoutils.Oficina;
import es.tributasenasturias.documentopagoutils.Organismo;
import es.tributasenasturias.documentopagoutils.ValidatorUtils;

import es.tributasenasturias.docs.XMLUtils;
import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;

/**
 * Rellena un xml plantilla con una lista de datos o una clase de negocio, y rellena el PDF de salida para los
 * formularios de recibos.
 */

@SuppressWarnings("unchecked")
public class DatosSalidaImpresa {
	/*
	 * NOTA: en el portal antiguo los datos que determinan el tipo de recibo
	 * (ver FormPDFBase.getClaseFormulario()) vienen de la queryes pasadas a
	 * InfoParticularWS. Los parametros venian en una lista para un redirect por
	 * JS, llamando a TRIBUTAS/DatosSalida.inc que imprime PDFs y HTMLs. Tanto
	 * DatosSalida como DatosSalidaImpresa utilizan la variable "s".
	 * 
	 * ver domiciliacion2internet.asp, DetallePersInternet.asp, DatosSalida.inc,
	 * emitirExpediente.asp, 0099.asp, InformacionParticular.java
	 */

	public HashMap<String, Object> rs_master = null;// NOTA: usa un ResultSet
	private com.lowagie.text.Document oPDF;
	private PdfContentByte pcb;
	private Document fuente = null;
	private PdfWriter pdfwriter = null;
	private Font ff = null;
	static Map<String, String> Session = new HashMap<String, String>();// solo se utiliza para pasar el codigo de
	// organismo
	public static String pathSalidaBase = null;// "http://localhost:8080";
	public OutputStream pdfoutput = null;
	
	
	public DatosSalidaImpresa() {
		oPDF = pcbLoad();
	}

	/*
	 * Error de sintaxis en el fuente xml
	 */
	private static String reportParseError(Exception error) {
		String errormsg = "";
		String s = "";
		for (int i = 1; i < error.getStackTrace().length; i++) {// error.linepos,.url,.reason,.line,.line,.linepos,.srcText
			s += " ";
			String r = "<font face=Verdana size=2><font size=4>XML Error loading '"
					+ error.getStackTrace()[i].getClassName() + "'</font>" + "<P><B>"
					+ error.getStackTrace()[i].getFileName() + "</B></P></font>";
			if (i > 0)
				r += "<font size=3><XMP>" + "at line " + error.getStackTrace()[i].getLineNumber() + ", character " + 0
						+ "\n" + error.getStackTrace()[i].getMethodName() + "\n" + s + "^" + "</XMP></font>";
			errormsg += r;
		}
		return errormsg;
	}

	/*
	 * Error de ejecucion de XSL
	 */
	public static String reportRuntimeError(Exception exception) {
		String errormsg = "";
		errormsg += ("<font face=Verdana size=2><font size=4>XSL Runtime Error</font>" + "<P><B>"
				+ exception.getMessage() + "</B></P></font>");
		return errormsg;
	}

	/**
	 * Devuelve el valor de s en base radix como un entero (+-2^15-1), 0 si excede los limites.
	 * @param s
	 * @param radix
	 * @return
	 */
	public static int parseInt(String s, int radix) {
		try {
			// ERROR: parseInt NO admite decimales
			if (s.indexOf(".") != -1)
				s = s.substring(0, s.indexOf("."));
			if (s.indexOf(",") != -1)
				s = s.substring(0, s.indexOf(","));
			if (s.indexOf(" ") != -1) {
				int index = 0;
				for (index = 0; index < s.length(); index++)
					if (s.charAt(index) != ' ')
						break;
				s = s.substring(index, s.length());
				for (index = s.length() - 1; index > 0; index--)
					if (s.charAt(index) != ' ')
						break;
				s = s.substring(0, index + 1);
			}
			Integer num = Integer.parseInt(s, radix);
			if (num != null)
				return num.intValue();
		} catch (NumberFormatException e) {
			Logger.trace(e.getStackTrace(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			return 0;
		}
		return 0;
	}
	
	/**
	 * Devuelve el valor de s en base 16 si empieza por 'Ox' o en base 10 por defecto. Devuelve un entero (+-2^15-1), 0 si excede los limites.
	 * @param s
	 * @return
	 */
	public static int parseInt(String s) {
		try {
			Integer num = null;
			// ERROR: parseInt NO admite decimales
			if (s.indexOf(".") != -1)
				s = s.substring(0, s.indexOf("."));
			if (s.indexOf(",") != -1)
				s = s.substring(0, s.indexOf(","));
			if (s.indexOf(" ") != -1) {
				int index = 0;
				for (index = 0; index < s.length(); index++)
					if (s.charAt(index) != ' ')
						break;
				s = s.substring(index, s.length());
				for (index = s.length() - 1; index > 0; index--)
					if (s.charAt(index) != ' ')
						break;
				s = s.substring(0, index + 1);
			}
			Double nummax = Double.parseDouble(s);
			if (s.startsWith("0x") || s.startsWith("0X"))
				num = Integer.parseInt(s, 16);
			else
				num = Integer.parseInt(s);
			if (num != null)
				return num.intValue();
		} catch (NumberFormatException e) {
			Logger.trace(e.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			return 0;
		}
		return 0;
	}

	/**
	 * Funcion que cuenta cuantas lineas con una longitud maxima tiene una cadena.
	 * @param cadena
	 * @param length Longitud maxima de una cadena
	 * @return Numero de cambios de linea + numero de subcadenas de longitud <=length presentes
	 */
	public static int cuentaLineas(String cadena, int length) {
		int lineas = 0;
		if (validString(cadena)) {
			lineas++;
			int j = 0;
			for (int i = 0; i < cadena.length(); i++) {
				if (cadena.charAt(i) == '\n' || j >= length) {
					lineas++;
					j = 0;
				}
				j++;
			}
		}
		return lineas;
	}

	/**
	 * Devuelve el valor del mapa asociado a la clave.
	 * @param rs Mapa de campos
	 * @param nombre Clave a buscar
	 * @return rs.get(nombre) si rs y nombre son validos; "" si rs,nombre o rs.get(nombre) no son validos
	 */
	protected static String campo(Map rs, String nombre) {
		if (rs == null || rs.isEmpty())
			return "";// ("El recordSet " + nombre + " no contiene ningun valor");
		if (rs.get(nombre) != null)
			return rs.get(nombre) + "";
		else if (rs.get(nombre.toUpperCase()) != null)
			return rs.get(nombre.toUpperCase()) + "";
		else
			return "";
	}
	
	/**
	 * Devuelve el valor numerico del mapa asociado a la clave.
	 * @param rs Mapa de campos
	 * @param nombre Clave a buscar
	 * @return rs.get(nombre) si rs y nombre son validos; "0" si rs,nombre o rs.get(nombre) no son validos
	 */
	protected static String vac(Map rs, String nombre) {
		if (rs == null || rs.isEmpty())
			return ("0");
		if (rs.get(nombre) != null)
			return rs.get(nombre) + "";
		else if (rs.get(nombre.toUpperCase()) != null)
			return rs.get(nombre.toUpperCase()) + "";
		else
			return "0";
	}

	/**
	 * Devuelve el valor del mapa asociado a la clave. Ver campo(rs,nombre)
	 * @param rs 
	 * @param nombre
	 * @return Si el valor es null, devuelve una cadena vacia.
	 */
	protected static String nvl(Map rs, String nombre) {
		if (rs == null || rs.isEmpty())
			return "";
		return campo(rs, nombre);
	}

	/**
	 * Devuelve la primera linea de un Map<Integer,Map<>>.
	 * @param rs 
	 * @return
	 */
	protected static Map primeraLinea(Map rs) {
		if (rs == null)
			return null;
		return (Map) rs.get(Integer.valueOf(0));
	}

	/**
	 * Informa si una cadena es valida.
	 * @param cadena
	 * @return boolean
	 */
	public static boolean validString(String cadena) {
		return cadena != null && cadena.length() != 0;
	}

	/**
	 * Deberia traducir literales, pero no se ha parametrizado la traduccion, por lo que se anula la llamada.
	 * 
	 * 
	 * @param dato Cadena a traducir
	 * @param idioma Idioma. "CA","ES" devuelven la cadena original; para traducir a otros idiomas es necesario el fichero
	 *            properties del idioma (ACCESS_*.properties)
	 * @return String
	 */
	public static String traduc(String dato, String idioma) {		
		return dato;
	}

	/**
	 * Combina el numero de referencia y el codigo de la entidad con un codigo de paridad para el formulario de Recibos
	 * voluntarios online.
	 * 
	 * @param referencia 
	 * @param entidad 
	 * @return
	 */
	public static String referencia_dc(String referencia, String entidad) {
		String b = "";
		String ref_dc;
		entidad = entidad.replaceAll(" ", "");
		double referencianum = Math.floor(Val(referencia));
		if (referencianum > 0 & entidad.length() > 0) {
			referencianum = (int) (referencianum / 100);
			b = trimDecimales(referencianum + (int) Val(entidad));
			b = (b.indexOf(".") != -1) ? b.substring(0, b.indexOf(".")) : b;
			b = Formato((b) + "", 11);
			int[] multiple = new int[] { 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
			float x = 0;
			for (int i = 0; i < 11; i++) {
				x += Integer.valueOf("" + b.charAt(i)).intValue() * multiple[i];
			}
			x = x % 11;
			if (x == 10)
				x = 0;
			ref_dc = Formato(trimDecimales(referencianum * 10 + x), 12);
		} else
			ref_dc = Formato("" + trimDecimales(referencianum), 12);
		return (ref_dc);
	}

	//elimina los decimales despues del punto (formato Java)
	private static String trimDecimales(double d) {
		Double dd = new Double(d);
		return trimDecimales(dd.longValue() + "");
	}

	//elimina los decimales despues del punto (formato Java)
	private static String trimDecimales(String d) {
		if (d == null || d.equals(""))
			return "";
		return (d.indexOf(".") != -1) ? d.substring(0, d.indexOf(".")) : d;
	}

	/**
	 * Devuelve el número presente en una cadena tipo char como un número. Nota: sólo para números en base decimal. Si la
	 * cadena contiene espacios los elimina.
	 * 
	 * @param input
	 * @return
	 */
	public static double Val(String input) {
		double valor = 0;
		try {
			Double value = NumberUtil.getDoubleFromString(input);
			if (value != null)
				valor = value.doubleValue();
		} catch (NumberFormatException e) {
			es.tributasenasturias.documentopagoutils.Logger.error("Error:"+e.getMessage(),e,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
		
		return valor; 
	}

	/**
	 * Devuelve el valor absoluto de Val(numero).
	 * @param numero
	 * @return
	 */
	public static String Abs(String numero) {
		return Math.abs(Val(numero)) + "";
	}

	/**
	 * Elimina los espacios y '& nbsp;' al comienzo y final de una cadena.
	 * @param cadena
	 * @return
	 */
	public static String Trim(String cadena) {
		int length = cadena.length();
		int count = 0;
		while ((count < length) && (cadena.charAt(count) == ' ' || cadena.startsWith("&nbsp;", count)))
			count++;
		int ini = count;
		count = cadena.length() - 1;
		while ((count > ini) && (cadena.charAt(count) == ' ' || cadena.startsWith("&nbsp;", count)))
			count--;
		int fin = count;
		return cadena.substring(ini, fin + 1);
	}

	/**
	 * Devuelve los primeros length caracteres de la cadena o toda la cadena.
	 * @param cadena
	 * @param length
	 * @return
	 */
	public static String Left(String cadena, int length) {
		if (cadena == null || cadena.length() < length || length < 0)
			return cadena;
		return cadena.substring(0, length);
	}

	/**
	 * Devuelve los ultimos length caracteres de la cadena o toda la cadena.
	 * @param cadena
	 * @param length
	 * @return
	 */
	public static String Right(String cadena, int length) {
		if (cadena == null || cadena.length() < length || length < 0)
			return cadena;
		return cadena.substring(cadena.length() - length, cadena.length());
	}

	/**
	 * Si expr==true, devuelve cadena1; sino cadena2.
	 * @param expr 
	 * @param cadena1 
	 * @param cadena2
	 * @return String
	 */
	public static String IIf(boolean expr, String cadena1, String cadena2) {
		return expr ? cadena1 : cadena2;
	}

	/**
	 * Devuelve una cadena de al menos length caracteres, insertando '0' al principio de la cadena hasta que sea asi.
	 * 
	 * @param cadena
	 * @param length
	 * @return cadena si tiene al menos length caracteres; 0...0cadena de lo contrario.
	 */
	public static String Formato(String cadena, int length) {
		int longitud = cadena.length();
		if (longitud < length) {
			char[] subcadena = new char[length - longitud];
			for (int i = 0; i < length - longitud; i++)
				subcadena[i] = '0';
			cadena = new String(subcadena).concat(cadena);
		}
		return cadena;
	}

	/**
	 * Devuelve una cadena de longitud cadena.length(), ver Formato(cadena,cadena.length()).
	 * 
	 * @param cadena 
	 * @return cadena
	 */
	public static String Formato(String cadena) {
		return cadena;
	}

	/**
	 * Devuelve el valor del mapa asociado como una fecha en formato dd/MM/yyyy.
	 * 
	 * @param rs Mapa de campos
	 * @param nombre Clave a buscar
	 * @return fecha en formato dd/MM/yyyy para fechas validas;"" por defecto.
	 */
	public static String campoFecha(Map rs, String nombre) {
		String szFecha = campo(rs, nombre);
		if (szFecha == null || szFecha.equals("")) {
			return "";
		} else {
			try {
				String formatoEntrada = "dd/MM/yyyy";				
				if(szFecha.length() == 8){
					formatoEntrada = "dd/MM/yy";
				}
	
				DateFormat df = new SimpleDateFormat(formatoEntrada); 
				Date fecha = df.parse(szFecha);  
				
				if (fecha == null)
					return "";
				SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
				return formateador.format(fecha);
			} catch (Exception e) {
				Logger.trace(e.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				return "";
			}
		}
	}

	/**
	 * Devuelve el valor del mapa asociado como una fecha en formato hh:mm:ss.
	 * 
	 * @param rs Mapa de campos
	 * @param nombre Clave a buscar
	 * @return fecha en formato hh:mm:ss para fechas validas;"" por defecto.
	 */
	protected static String campoHora(Map rs, String nombre) {
		if (campo(rs, nombre).equals(""))
			return "";
		else {
			try {
				Date fecha = new Date(campo(rs, nombre));
				if (fecha == null)
					return "";
				SimpleDateFormat formateador = new SimpleDateFormat("hh:mm:ss");
				return formateador.format(fecha);
			} catch (Exception e) {
				Logger.trace(e.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				return "";
			}
		}
	}

	/**
	 * Convierte una fecha del formato ingles yyyy/mmm/dd (2009/Jan/01) al castellano dd/mmm/yyyy (01/Ene/2009). Tambien
	 * acepta yy/mmm/dd.
	 * 
	 * @param dato
	 * @return
	 */
	public static String dameFecha(String dato) {
		if (dato == null || dato.equals(""))
			return "";
		int aux = dato.lastIndexOf(" ", 0);
		String cadena = "";
		if (aux != -1) {
			if (aux > dato.length() - 2)
				cadena = "";
			else
				cadena = dato.substring(aux + 2, dato.length());
		} else
			cadena = dato;
		
		String dia = (cadena.length() >= 10) ? cadena.substring(8, 8 + 2) : "";
		int inianyo = cadena.length() - 4;
		if (inianyo < 0)
			inianyo = 0;
		String anyo = cadena.substring(inianyo, cadena.length() - 0);

		String mes = (cadena.length() >= 7) ? cadena.substring(4, 4 + 3) : "";
		if (mes.equals("Jan"))
			mes = "01";
		else if (mes.equals("Feb"))
			mes = "02";
		else if (mes.equals("Mar"))
			mes = "03";
		else if (mes.equals("Apr"))
			mes = "04";
		else if (mes.equals("May"))
			mes = "05";
		else if (mes.equals("Jun"))
			mes = "06";
		else if (mes.equals("Jul"))
			mes = "07";
		else if (mes.equals("Aug"))
			mes = "08";
		else if (mes.equals("Sep"))
			mes = "09";
		else if (mes.equals("Oct"))
			mes = "10";
		else if (mes.equals("Nov"))
			mes = "11";
		else if (mes.equals("Dec"))
			mes = "12";

		String resultado = dia + "/" + mes + "/" + anyo;
		return resultado;
	}

	/**
	 * Convierte una fecha del formato ingles yyyy/mmm/dd (2009/Jan/01) al castellano 'd de m de y' (01 de Enero de
	 * 2009). Tambien acepta yy/mmm/dd.
	 * 
	 * @param dato Fecha a convertir
	 * @return
	 */
	public static String dameFecha2(String dato) {
		if (dato == null || dato.equals(""))
			return "";
		String cadena = dato;
		String dia = (cadena.length() >= 10) ? cadena.substring(8, 8 + 2) : "";
		int inianyo = cadena.length() - 4;
		if (inianyo < 0)
			inianyo = 0;
		String anyo = cadena.substring(inianyo, cadena.length() - 0);

		String mes = (cadena.length() >= 7) ? cadena.substring(4, 4 + 3) : "";
		if (mes.equals("Jan"))
			mes = "Enero";
		else if (mes.equals("Feb"))
			mes = "Febrero";
		else if (mes.equals("Mar"))
			mes = "Marzo";
		else if (mes.equals("Apr"))
			mes = "Abril";
		else if (mes.equals("May"))
			mes = "Mayo";
		else if (mes.equals("Jun"))
			mes = "Junio";
		else if (mes.equals("Jul"))
			mes = "Julio";
		else if (mes.equals("Aug"))
			mes = "Agosto";
		else if (mes.equals("Sep"))
			mes = "Septiembre";
		else if (mes.equals("Oct"))
			mes = "Octubre";
		else if (mes.equals("Nov"))
			mes = "Noviembre";
		else if (mes.equals("Dec"))
			mes = "Diciembre";

		String resultado = dia + " de " + mes + " de " + anyo;
		return resultado;
	}

	/**
	 * Convierte una fecha del formato dd/mm/yyyy (01/01/2009) al castellano 'd de m de y' (01 de enero de
	 * 2009), meses en minusculas. Tambien acepta dd/mm/yy.
	 * 
	 * @param dato Fecha a convertir
	 * @return
	 */
	public static String dameFecha3(String dato) {
		if (dato == null || dato.equals(""))
			return "";
		String cadena = dato;
		String dia = (cadena.length() >= 2) ? cadena.substring(0, 2) : "";
		int inianyo = cadena.length() - 4;
		if (inianyo < 0)
			inianyo = 0;
		String anyo = cadena.substring(inianyo, cadena.length() - 0);

		String mes = (cadena.length() >= 5) ? cadena.substring(3, 3 + 2) : "";
		int mesnum = parseInt(mes);
		switch (mesnum) {
		case 1:
			mes = "enero";
			break;
		case 2:
			mes = "febrero";
			break;
		case 3:
			mes = "marzo";
			break;
		case 4:
			mes = "abril";
			break;
		case 5:
			mes = "mayo";
			break;
		case 6:
			mes = "junio";
			break;
		case 7:
			mes = "julio";
			break;
		case 8:
			mes = "agosto";
			break;
		case 9:
			mes = "septiembre";
			break;
		case 10:
			mes = "octubre";
			break;
		case 11:
			mes = "noviembre";
			break;
		case 12:
			mes = "diciembre";
			break;
		default:
			break;
		}

		String resultado = dia + " de " + mes + " de " + anyo + ".";
		return resultado;
	}

	/**
	 * Devuelve una query de WS, implementada como recuperacion de parametros del Map rs_master con la 
	 * informacion del XML de entrada.
	 * 
	 * @param structSelect Estructuras a recuperar
	 * @param paramSelect
	 * @param oldSelect
	 * @return Map donde Map.get(new Integer(n-1)) devuelve la (n)-esima linea de datos
	 */
	public Map<Integer, Map<String, String>> findWs(String[] structSelect, String[] paramSelect, String oldSelect) {
		Map<Integer, Map<String, String>> lstRes = new TreeMap<Integer, Map<String, String>>();// Connect.execQueryAsMap(select);
		// combinar con setRegister(n) para usar 1 solo mapa
		if (!rs_master.isEmpty() && structSelect != null) {
			for (int i = 0; i < structSelect.length; i++) {
				Map<Integer, Map<String, String>> mapEstruct = (Map<Integer, Map<String, String>>) rs_master
						.get(structSelect[i].toLowerCase());
				if (mapEstruct == null || mapEstruct.isEmpty())
					mapEstruct = (Map<Integer, Map<String, String>>) rs_master.get(structSelect[i].toUpperCase());
				if (mapEstruct == null || mapEstruct.isEmpty())
					continue;
				int numKeys = mapEstruct.keySet().size();
				for (int j = 0; j < numKeys; j++) {
					Integer key = Integer.valueOf(j);
					if (lstRes.containsKey(key)) {
						Map<String, String> mapParams = lstRes.get(key);
						mapParams.putAll(mapEstruct.get(key));
					} else {
						lstRes.put(key, mapEstruct.get(key));
					}
				}
			}
		}
		return lstRes;
	}

	/**
	 * Constructor con salida al fichero "plantilla_rellena.pdf".
	 * @param xml XML obtenido desde WebService
	 * @param xsl Plantilla XML obtenida con FormPDFBase.getPlantilla()
	 */
	public DatosSalidaImpresa(String xml, String xsl) {
		this(xml, xsl, null);
	}

	/**
	 * Constructor principal.
	 * 
	 * @param xml XML obtenido desde WebService
	 * @param xsl Plantilla XML obtenida con FormPDFBase.getPlantilla()
	 * @param output
	 *            Stream donde escribir el PDF. Si output no es un OutputStream, se escribe en el fichero
	 *            "plantilla_rellena.pdf" en la carpeta base del portlet de WebServices.
	 */
	public DatosSalidaImpresa(String xml, String xsl, Object output) {
		if (output instanceof OutputStream) {			
			pdfoutput = (OutputStream) output;
		} else {
			if (output != null)
				
				es.tributasenasturias.documentopagoutils.Logger.debug("OUTPUT CON DATOS",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);							
		}
		oPDF = pcbLoad();
		try {			
			fuente = (Document) XMLUtils.compilaXMLObject(xsl, null);
			// Borrado de informacion de desarrollo
			Element[] campos = XMLUtils.selectNodes(fuente, ".//texto[@type='109']");
			for (int j = 0; j < campos.length; j++)
				campos[j].setTextContent("");
			campos = XMLUtils.selectNodes(fuente, ".//textobox[@type='109']");
			for (int j = 0; j < campos.length; j++)
				campos[j].setTextContent("");
		} catch (RemoteException e) {
			Logger.trace(e.getStackTrace(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
		
		/*
		try {
			HashMap<String, Object> mapResults = XMLUtils.compilaXMLDoc(xml, null, null, true);
			String key = "";
			Object[] keylst = mapResults.keySet().toArray();
			for (int i = 0; i < keylst.length; i++) {
				key = (String) keylst[i];
				if (key.equalsIgnoreCase("ERROR"))
					continue;
				Object[] lstStringArray = (Object[]) mapResults.get(key);
				HashMap<Integer, Map<String, String>> mapSalida = XMLUtils
						.convertirObjectStringArrayHashMap(lstStringArray);
				mapResults.put(key, mapSalida);
			}
			rs_master = mapResults;
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		*/
		Session.put("organismo", "33");
		
		dependenciasOrganismo(fuente);
	}

	/**
	 * Convierte los nodos <icono/>, <firma/> ,<cornamusa/>  en referencias a imagenes.
	 * @param doc Document de la plantilla XML
	 */
	public static void dependenciasOrganismo(Document doc) {
		Element[] campos = XMLUtils.selectNodes(doc, ".//orga");
		String organismo = "" + Session.get("organismo");
		final String pathTributas = "/TRIBUTAS/IMAGES";
		final String pathPortal = "/BDCliente";
		for (int j = 0; j < campos.length; j++) {
			Element c = campos[j];
			if (c.getTextContent().equals("icono")) {				
				String literal = es.tributasenasturias.documentopagoutils.LiteralesUtils.obtenerLiteralFromInorInfoOrga(organismo, "IL");
				Node p = c.getParentNode();
				p.removeChild(c);
				if(ValidatorUtils.isEmpty(literal)){
					p.setTextContent(pathPortal + "/SINLOGO.JPG");
				}else{
					p.setTextContent(literal);
				}
				
				if (organismo.equals("33") && ValidatorUtils.isEmpty(literal))
					p.setTextContent(pathPortal + "/logoorga33.jpg");
				
			} else if (c.getTextContent().equals("firma")) {
				String literal = LiteralesUtils.obtenerLiteralFromInorInfoOrga(organismo, "IF");
				Node p = c.getParentNode();
				p.removeChild(c);
				if(ValidatorUtils.isEmpty(literal)){
					p.setTextContent(pathPortal + "/FIRMABLANCO.JPG");
				}
				else {
					p.setTextContent(literal);
				}
				if (organismo.equals("33") && ValidatorUtils.isEmpty(literal))
					p.setTextContent(pathPortal + "/firma33.jpg");
				
			} else if (c.getTextContent().equals("cornamusa")) {
				String literal = LiteralesUtils.obtenerLiteralFromInorInfoOrga(organismo, "IC");
				Node p = c.getParentNode();
				p.removeChild(c);
				if (ValidatorUtils.isEmpty(literal))
					p.setTextContent(pathPortal + "/FIRMABLANCO.JPG");
				else {
					p.setTextContent(literal);
				}
			}
		}
	}

	/**
	 * Formatea la parte entera de cad1 según indica cad2. EL formato de cad2 es: #-> representa un dígito cualquiera.
	 * El resto de los caracteres se introducen a "pelo" en la cad2 Ejemplos para cad2:
	 * 
	 * "#######" "###.###" -> El punto se meterá "a pelo" en la posición indicada por cad2 en la cad1 "####A#" -> La A
	 * se meterá "a pelo" en la posición indicada por cad2 en la cad1
	 * 
	 * @param cad1 Cadena a modificar
	 * @param cad2 Formato
	 * @return
	 */
	public static String Format(String cad1, String cad2) {
		if (cad1 == null)
			cad1 = "";
		if (cad2 == null)
			cad2 = "";
		String cad3 = "";
		String cad4 = "";
		String cad5 = "";
		String cad6 = "";
		int long1, long2;
		int pos = cad1.indexOf(",", 0);

		if (pos != -1) {
			cad3 = cad1.substring(pos, cad1.length());
			cad1 = cad1.substring(0, pos);
		}
		long1 = cad1.length();
		long2 = cad2.length();

		cad4 = Cadreverse(cad1, long1);
		cad5 = Cadreverse(cad2, long2);

		int i = 0;
		int j = 0;

		while ((i < long1) && (j < long2)) {
			if (cad5.charAt(j) == '#') {
				cad6 = cad6 + cad4.charAt(i);
				i++;
			} else
				cad6 = cad6 + cad5.charAt(j);
			j++;
		}

		if (i < long1 - 1)
			cad6 = cad6 + cad4.substring(i, long1 - 1);

		cad6 = Cadreverse(cad6, cad6.length());
		return (cad6 + cad3);
	}

	/**
	 * Devuelve la cadena de entrada al reves.
	 * @param cadena
	 * @param longitud
	 * @return
	 */
	public static String Cadreverse(String cadena, int longitud) {
		String cad1 = "";
		for (int i = longitud - 1; i >= 0; i--)
			cad1 = cad1 + cadena.charAt(i);
		return cad1;
	}

	/**
	 * Devuelve el nombre de la divisa.
	 * @param codMone Codigo de la moneda
	 * @param tipo Formato abreviado o completo
	 * @return Pesetas("P",0),ptas("P",*) o Euros.
	 */
	private static String NombreMoneda(String codMone, int tipo) {
		String nombre;
		if (codMone.equals("P")) {
			if (tipo == 0)
				nombre = "ptas.";
			else
				nombre = "pesetas.";
		} else
			nombre = "Euros";
		return nombre;
	}

	/**
	 * Llama ConvEuro(cadena,100,decimales).
	 * @param cadena
	 * @param decimales
	 * @return
	 */
	public static String toEuro(String cadena, int decimales) {

		return ConvEuro(cadena, 100, decimales);
	}

	/**
	 * Llama ConvEuro(cadena,100,2).
	 * @param cadena
	 * @return
	 */
	public static String toEuro(String cadena) {
		return ConvEuro(cadena, 100, 2);
	}

	/**
	 * Convierte una cantidad a Euros truncando los decimales. Si el número de decimales exigido es menor que los de
	 * entrada se añaden ceros hasta completar el número de decimales. Acepta cadenas del tipo ".1", "1.", "1","1.23234"
	 *
	 * @param cadena
	 * @param factor Si factor==100, convierte a centimos; si factor==166.386, convierte a Euros
	 * @param decimales
	 * @return
	 */
	public static String ConvEuro(String cadena, double factor, int decimales) {
		if (cadena == null)
			cadena = "";
		String cad = "";
		String cad1, cad2; // cadenas auxiliares
		int i1, i2;
		cadena = (Val(cadena) / factor) + "";

		// Cambia el punto por una coma
		int pos = cadena.indexOf(".", 0);

		if (pos != -1) {
			cad1 = cadena.substring(0, pos);
			cad2 = cadena.substring(pos + 1, cadena.length());
			cadena = cad1 + "," + cad2;
		}

		cadena = Format(cadena, "###.###.###");

		// Ahora vamos con los decimales

		pos = cadena.indexOf(",", 0);

		if (decimales == 0) {
			if (pos > 0)
				return (cadena.substring(0, pos));
			else
				return (cadena);
		} else {
			if (pos == -1) {
				cad = ",";
				for (i1 = 0; i1 < decimales; i1++) {
					cad = cad + "0";
				}
				return (cadena + cad);
			}

			if ((pos + 1 + decimales) <= (cadena.length())) {
				return (cadena.substring(0, pos + decimales + 1));
			} else { // añade ceros para completar decimales
				i1 = (pos + 1 + decimales) - (cadena.length());
				cad = "";
				for (i2 = 0; i2 < i1; i2++)
					cad = cad + "0";
				return (cadena + cad);
			}
		}
	}

	/**
	 * Devuelve si la moneda es el euro o no.
	 * @return TRUE
	 */
	public static boolean euro() {
		return true;
	}

	/**
	 * Devuelve el nombre de la divisa.
	 * @return euros
	 */
	public static String nombreMoneda() {
		if (!euro())
			return "ptas.";
		else
			return "euros";
	}

	/**
	 * Devuelve el nombre de la divisa.
	 * @param codMone Codigo de la moneda
	 * @param tipo Formato abreviado o completo
	 * @return Pesetas("P",0),ptas("P",*) o Euros.
	 */
	public static String nombreMoneda(String codMone, int tipo) {
		String texto;
		if (codMone != null && codMone.equalsIgnoreCase("P")) {
			if (tipo == 0)
				texto = "ptas.";
			else
				texto = "pesetas.";
		} else
			texto = "Euros";
		return (texto);
	}
    
	public static String rellenaTexto(int idText, int idOrga, Organismo organismo) {
		return rellenaTexto("" + idText, "" + idOrga, organismo);
	}

	public static String rellenaTexto(int idText, double idOrga, Organismo organismo) {
		return rellenaTexto("" + idText, "" + idOrga, organismo);
	}

	/**
	 * Devuelve el texto correspondiente a los parametros en 2 queries a SQL.
	 * @param idText id del texto
	 * @param idOrga organismo (ASTURIAS==33)
	 * @return String
	 */
	public static String rellenaTexto(String idText, String idOrga,Organismo organismo) {

		String textoBase = "";
		String textoRelleno = "";
		String codTiio = "";
		String codTiioMin = "";
		int estado;
		int i;
		String c;
		int pos;
		String patron;
		String rellenaTexto1 = "";
		
		//es.tributasenasturias.documentopagoutils.Logger.error("Dentro 1:",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		
		String literal = LiteralesUtils.obtenerLiteralFromTextTextos(idText);
		if (idText != null && !idText.equals("")) {
			if(!ValidatorUtils.isEmpty(literal)){
				textoBase = literal;
			}
			else {
				textoBase = "";
			}
		}
		
		//es.tributasenasturias.documentopagoutils.Logger.error("Dentro 2:",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		//C.R.V. 19/12/2011 . Se reduce el número de accesos a base de datos 
		//Organismo organismo = es.tributasenasturias.documentopagoutils.ListadoUtils.obtenerOrganismo(idOrga);
		
		if(organismo == null){
			rellenaTexto1 = "Parametro incorrecto en texto";						
		}
		else {
			//es.tributasenasturias.documentopagoutils.Logger.error("Dentro 3:",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			estado = 0;
			i = 1;
			while (i <= textoBase.length()) {
				c = (textoBase.substring(i - 1, i));
				if (c.equals("#")) { // if6
					estado = 1 - estado;
					if (estado == 1)
						codTiio = "";
					else { // If5
						//es.tributasenasturias.documentopagoutils.Logger.error("Dentro 4:",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
						if (codTiio.equals(""))
							textoRelleno = textoRelleno + "#";
						else { // If4
							//es.tributasenasturias.documentopagoutils.Logger.error("Dentro 5:",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
							codTiioMin = codTiio.toLowerCase();
							String textoCampo = null;
							
							if (codTiioMin.equals("nombre_orga")){
								textoCampo = organismo.getNombreOrga();
							} 
							else if (codTiioMin.equals("nombre_l_orga")){
								textoCampo = organismo.getNombreLOrga();
							}
							else if (codTiioMin.equals("nif_orga")){
								textoCampo = organismo.getNifOrga();
							}
							else if (codTiioMin.equals("cod_eemi")){
								textoCampo = organismo.getCodEemi();
							}
							else if(codTiioMin.equals("director_orga")){
								textoCampo = organismo.getDirectorOrga();
							}
							else if (codTiioMin.equals("max_fracc_orga")) {
								textoCampo = organismo.getMaxFraccOrga();
							}
							else if(codTiioMin.equals("min_fraccionar_orga")){
								textoCampo = organismo.getMinFraccionarOrga();
							}
							else if(codTiioMin.equals("min_fraccion_orga")) {
								textoCampo = organismo.getMinFraccionOrga();
							}
							else { 
								InorInfoOrga inorInfo = es.tributasenasturias.documentopagoutils.ListadoUtils.obtenerInorInfoOrga(idOrga, codTiio);
								
								if (inorInfo == null){
									rellenaTexto1 = "Parametro incorrecto en texto";
								}
								else { 
									if (!ValidatorUtils.isEmpty(inorInfo.getDescrInor())){
										textoCampo = inorInfo.getDescrInor();
									}
									else if (!ValidatorUtils.isEmpty(inorInfo.getNumInor())){
										textoCampo = inorInfo.getNumInor();
									}
									else if (!ValidatorUtils.isEmpty(inorInfo.getFechaInor())){
										textoCampo = inorInfo.getFechaInor();
									}
									else if (!ValidatorUtils.isEmpty(inorInfo.getBoolInor())){
										textoCampo = inorInfo.getBoolInor();
									}
								} 
							}
							
							if(ValidatorUtils.isEmpty(textoCampo)){
								rellenaTexto1 = "Parametro incorrecto en texto";
							}else{
								//es.tributasenasturias.documentopagoutils.Logger.error("Dentro 11:",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
								textoRelleno = textoRelleno + textoCampo;
							}
							
						} 
					}
				}else{
					//es.tributasenasturias.documentopagoutils.Logger.error("Dentro 12:",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
					if (estado == 1)
						codTiio = codTiio + c;
					else
						textoRelleno = textoRelleno + c;
				}
				i = i + 1;
			}	
		}
		rellenaTexto1 = textoRelleno;
	//	es.tributasenasturias.documentopagoutils.Logger.error("Dentro 13:",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		pos = 1;
		pos = rellenaTexto1.indexOf("#FDL#", pos);
		patron = "/#FDL#/g";
		patron = "#FDL#";
		if (pos > 0)
			rellenaTexto1 = rellenaTexto1.replace(patron, "   " + "\n");

		return (rellenaTexto1);

	}
	
	/**
	 * Devuelve el texto correspondiente al número fijo indicado, utilizando el mapa cargado anteriormente.
	 * @param idText id del texto
	 * @param textos Mapa conteniendo los textos fijos precargados.
	 * @return String
	 */
	public static String rellenaTexto(int idText, Map<Integer,String> textos) {

		int pos=1;
		String texto="";
		String patron;
		if (textos.containsKey(idText))
		{
			texto = textos.get(idText);
		}
		pos = texto.indexOf("#FDL#", pos);
		patron = "#FDL#";
		if (pos > 0)
			texto = texto.replace(patron, "   " + "\n");

		return (texto);

	}

	/**
	 * Devuelve una lista de datos de las oficinas del organismo.
	 * @param idOrga organismo (ASTURIAS==33)
	 * @param orden devuelve los primeros 9 registros si order=1, o el resto
	 * @return
	 */
	public static String rellenaOficinas(String idOrga, int orden, List<Oficina> oficinas) {
		/*
		String texto = "";
		String b13 = "			 ";
		String b9 = "		 ";
		*/
				
		String texto = "";
		//String b13 = "			 ";
		String b13 = "           ";
		
		//String b9 = "		 ";
		String b9 = "        ";

		//List<Oficina> oficinas = ListadoUtils.obtenerOficinasBySaleRecibo("S");
		if (oficinas != null && oficinas.size() > 0 ){
			int idxIni = 0;
			int idxFin = Math.min(9,oficinas.size());
			if(orden != 1){
				idxIni = 9;
				idxFin = oficinas.size();
			}
			
			for(int i = idxIni; i < idxFin; i++){
				Oficina ofic = (Oficina)oficinas.get(i);
				texto = texto + Left(Left(ofic.getNombreOfic(), 13) + b13, 13) + "    "
				+ Left(Left(ofic.getDirCortaOfi(), 20) + b13 + "       ", 20) + "   "
				+ Left(Left(ofic.getTelefonoOfic(), 9) + b9, 9) + " \n";							
			}			
		}
		return texto;

	}

	/**
	 * Devuelve una lista de datos de las oficinas liquidadoras del organismo.
	 * @param idOrga organismo (ASTURIAS==33)
	 * @param orden devuelve los primeros 9 registros si order=1, o el resto
	 * @return
	 */
	public static String rellenaOficinasLiquidadoras(String idOrga, int orden) {

		String texto = "";
		String b13 = "			 ";
		String b9 = "		 ";

		
		List<Oficina> oficinasLiq = ListadoUtils.obtenerOficinasLiquidadoras();
		// si orden es 1, volcar al texto los datos de las tres primeras oficinas
		// sino volcar los datos de las 3 restantes, si las hay
		// lo hago con tamaños fijos, medidos a ojo
		if (oficinasLiq != null && oficinasLiq.size() > 0 ){
			int idxIni = 0;
			int idxFin = Math.min(9,oficinasLiq.size());
			if(orden != 1){
				idxIni = 9;
				idxFin = oficinasLiq.size();
			}
			
			for(int i = idxIni; i < idxFin; i++){
				Oficina ofic = (Oficina)oficinasLiq.get(i);
				String nombreOfic = "";
				
				if (("36").equals(ofic.getIdOfic())){
					nombreOfic = "OL OVIEDO";
				}
				else if (("39").equals(ofic.getIdOfic())){
					nombreOfic = "OL GIJÓN";					
				}
				else {
					nombreOfic = ofic.getNombreOfic();
				}
				
				texto = texto + Left(Left(nombreOfic, 13) + b13, 13) + "	  "
				+ Left(Left(ofic.getDirCortaOfi(), 20) + b13 + "	   ", 20) + "	"
				+ Left(Left(ofic.getTelefonoOfic(), 9) + b9, 9) + " \n";
			}	
		}
		return texto;
	}

	/**
	 * Elimina los espacios y retornos de linea al principio y fin de una cadena.
	 * @param cadena Cadena a formatear
	 * @return
	 */
	private static String TrimLineas(String cadena) {
		if (cadena.indexOf("\n") == -1 && cadena.indexOf(" ") == -1)
			return cadena;
		else {
			int ini = cadena.length();
			int fin = 0;
			for (int i = 0; i < cadena.length(); i++) {
				if (cadena.charAt(i) == ' ')
					continue;
				if (cadena.charAt(i) == '\n')
					continue;
				ini = i;
				break;
			}
			for (int i = cadena.length() - 1; i >= ini; i--) {
				if (cadena.charAt(i) == ' ')
					continue;
				if (cadena.charAt(i) == '\n')
					continue;
				fin = i;
				break;
			}
			if (ini > fin)
				cadena = "";
			else
				cadena = cadena.substring(ini, fin + 1);
		}
		return cadena;
	}

	// ----------------------------------------------------------------------
	// funciones del objeto que se pasa a la xsl. habra que hacer un include
	// ----------------------------------------------------------------------
	/**
	 * Abre e inserta una imagen en el PDF. Si el nombre de archivo no existe, busca el nombre en minusculas.
	 * @param node Nodo con TextContent = URL o path relativo del fichero
	 * @return
	 */
	public String pdf_open_image_file(Node node) {

		Node n1 = node;
		String fichero="";
		Node n2=null;
		if (n1!=null)
		{
			fichero = n1.getTextContent();
			n2 = n1.getFirstChild();
		}
		while (n1 != null && n1 != n2 && (!validString(fichero) || !validString(Trim(fichero)))) {
			fichero = n2.getTextContent();
			n1 = n2;
			n2 = n1.getFirstChild();
		}
		// eliminar los espacios
		n1 = node;
		String tipo = n1.getAttributes().getNamedItem("tipo").getTextContent();
		Node padre = n1.getParentNode();
		float x, y, scale_x, scale_y, r, escalado;
		if (padre.getNodeName().equals("bloque")
				&& (padre.getAttributes().getNamedItem("x") != null && padre.getAttributes().getNamedItem("y") != null)) {
			x = parseInt(n1.getAttributes().getNamedItem("x").getNodeValue())
					+ parseInt(padre.getAttributes().getNamedItem("x").getNodeValue());
			y = -parseInt(n1.getAttributes().getNamedItem("y").getNodeValue())
					+ parseInt(padre.getAttributes().getNamedItem("y").getNodeValue());
		} else {
			x = parseInt(n1.getAttributes().getNamedItem("x").getNodeValue());
			y = parseInt(n1.getAttributes().getNamedItem("y").getNodeValue());
		}
		Image pdfimage = null;
		try {
			String portalUrl = "";
			if (pathSalidaBase != null)
				portalUrl = pathSalidaBase;
			fichero = fichero.trim();
			String imagepath = portalUrl + fichero;
			try {
				pdfimage = Image.getInstance(imagepath);
			} catch (FileNotFoundException e) {
				int i = fichero.lastIndexOf("/");
				fichero = fichero.substring(0, i + 1) + fichero.substring(i + 1, fichero.length()).toLowerCase();
				imagepath = portalUrl + fichero;
				pdfimage = Image.getInstance(imagepath);
			}
		} catch (MalformedURLException e) {
			Logger.error("ERROR. No se puede abrir el fichero " + fichero,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			Logger.trace(e.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		} catch (Exception e) {
			Logger.error("ERROR. No se puede cargar la imagen " + fichero,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			Logger.trace(e.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
		if (pdfimage == null) {
			Logger.error("ERROR. No se puede abrir la imagen " + fichero,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			return "error";
		} else {
			pdfimage.setAlignment(Image.TEXTWRAP);
			r = fichero.toLowerCase().indexOf("logoorga");
			if (r == -1) {
				r = fichero.indexOf("FIRMA");
				if (r == -1) {
					scale_x = (140 / pdfimage.getWidth());
					scale_y = (76 / pdfimage.getHeight());
				} else {
					scale_x = (76 / pdfimage.getWidth());
					scale_y = (76 / pdfimage.getHeight());
				}
			} else {
				scale_x = (86 / pdfimage.getWidth());
				scale_y = (156 / pdfimage.getWidth());
			}

			if (scale_x >= scale_y)
				escalado = scale_y;
			else
				escalado = scale_x;

			pdfimage.setAbsolutePosition(x, y);
			pdfimage.scalePercent(escalado * 100);
			PdfContentByte pcb2 = pdfwriter.getDirectContentUnder();

			try {
				pcb2.addImage(pdfimage);
				pcb2.stroke();

			} catch (Throwable e) {
				Logger.error("No se ha podido insertar la imagen",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				Logger.trace(e.getStackTrace(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			}
		}
		return "ok";
	}

	/**
	 * 
	 * @param xml_data
	 * @param pdf_data
	 */
	public static void make_pdf(String xml_data, String pdf_data) {		
		es.tributasenasturias.documentopagoutils.Logger.error("MAKE_PDF usado:funcion no habilitada",es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
	}

	/**
	 * Abre el Documento donde escribir el PDF.
	 * @return
	 */
	public String pdf_open_file() {
		if (oPDF == null)
			oPDF = pcbLoad();
		return "ok";
	}
	
	/**
	 * Abre el Document donde escribir el PDF.
	 * @return
	 */
	public String pdf_open_file(Node x) {
		if (oPDF == null)
			oPDF = pcbLoad();
		return "ok";
	}

	/**
	 * Inserta una nueva pagina en el documento con la orientacion y tipo de papel informados. Las 
	 * coordenadas empiezan en la esquina inferior izquierda
	 * @param node Nodo con atributos "orientacion"(v,h) y "papel"; por defecto, "v","A4"
	 * @return
	 */
	public String pdf_begin_page(Node node) {

		String giro = "v";
		Node n1 = node;// .getNextSibling();
		if (n1.getAttributes().getNamedItem("orientacion") != null)
			giro = n1.getAttributes().getNamedItem("orientacion").getNodeValue();

		if (n1.getAttributes().getNamedItem("papel") != null) {
			String papel = n1.getAttributes().getNamedItem("papel").getTextContent();
			papel = papel.toUpperCase();
			if (papel.equals("A0")) {
				if (giro.equals("v"))
					oPDF.setPageSize(new Rectangle(2380, 3368));
				else
					oPDF.setPageSize(new Rectangle(3368, 2380));
			} else if (papel.equals("A1")) {
				if (giro.equals("v"))
					oPDF.setPageSize(new Rectangle(1684, 2380));
				else
					oPDF.setPageSize(new Rectangle(2380, 1684));
			} else if (papel.equals("A2")) {
				if (giro.equals("v"))
					oPDF.setPageSize(new Rectangle(1190, 1684));
				else
					oPDF.setPageSize(new Rectangle(1684, 1190));
			} else if (papel.equals("A3")) {
				if (giro.equals("v"))
					oPDF.setPageSize(new Rectangle(842, 1190));
				else
					oPDF.setPageSize(new Rectangle(1190, 842));
			} else if (papel.equals("A4")) {
				if (giro.equals("v"))
					oPDF.setPageSize(new Rectangle(595, 842));
				else
					oPDF.setPageSize(new Rectangle(842, 595));
			} else if (papel.equals("A5")) {
				if (giro.equals("v"))
					oPDF.setPageSize(new Rectangle(421, 595));
				else
					oPDF.setPageSize(new Rectangle(595, 421));
			} else if (papel.equals("A6")) {
				if (giro.equals("v"))
					oPDF.setPageSize(new Rectangle(297, 421));
				else
					oPDF.setPageSize(new Rectangle(421, 297));
			} else if (papel.equals("B5")) {
				if (giro.equals("v"))
					oPDF.setPageSize(new Rectangle(501, 709));
				else
					oPDF.setPageSize(new Rectangle(709, 501));
			} else if (papel.equals("LETTER")) {
				if (giro.equals("v"))
					oPDF.setPageSize(new Rectangle(612, 792));
				else
					oPDF.setPageSize(new Rectangle(792, 612));
			} else if (papel.equals("LEGAL")) {
				if (giro.equals("v"))
					oPDF.setPageSize(new Rectangle(612, 1008));
				else
					oPDF.setPageSize(new Rectangle(1008, 612));
			} else if (papel.equals("LEDGER")) {
				if (giro.equals("v"))
					oPDF.setPageSize(new Rectangle(1224, 792));
				else
					oPDF.setPageSize(new Rectangle(792, 1224));
			} else if (papel.equals("11X17")) {
				if (giro.equals("v"))
					oPDF.setPageSize(new Rectangle(792, 1224));
				else
					oPDF.setPageSize(new Rectangle(1224, 792));
			} else {
				if (giro.equals("v"))
					oPDF.setPageSize(new Rectangle(595, 842));
				else
					oPDF.setPageSize(new Rectangle(842 * 2, 595 * 2));
			}
		}
		//siempre se inicia una pagina antes de insertar contenido
		//open() ya crea la primera pagina
		boolean docNuevo = oPDF != null && !oPDF.isOpen();
		if (docNuevo) {
			oPDF.open();
			pcb = pdfwriter.getDirectContent();
			pcb.setLineWidth(1);
		}
		if (!docNuevo)
			oPDF.newPage();
		return "ok";
	}

	/**
	 * Cierra una pagina del PDF.
	 * @param node Nodo de la pagina a cerrar
	 * @return
	 */
	public String pdf_end_page(Node node) {
		return "ok";
	}

	/**
	 * Cierra el Documento donde escribir el PDF y el PDFWriter asociado.
	 * @return
	 */
	public String pdf_close() {
		if (oPDF.isOpen())
			oPDF.close();
		pdfwriter.close();
		return "ok";
	}

	/**
	 * Asigna creador, autor y titulo al Documento PDF.
	 * @param creador
	 * @param autor
	 * @param titulo
	 * @return
	 */
	public String pdf_creador(String creador, String autor, String titulo) {
		oPDF.addCreator(creador);
		oPDF.addAuthor(autor);
		oPDF.addTitle(titulo);
		return "ok";
	}

	/**
	 * Cambia el tipo de fuente a escribir.
	 * @param node Nodo con atributos "fuente"(F1-8) y "size"; por defecto, "HelveticaBold", 12
	 * @return
	 */
	public String pdf_setfont(Node node){

		Node n1 = node;
		String fuentes = "";
		String fuenteXML = "";
		int s = 12;
		if (n1 != null) {
			fuenteXML = n1.getAttributes().getNamedItem("fuente").getTextContent();
			if (n1.getAttributes().getNamedItem("size") != null)
				s = parseInt(n1.getAttributes().getNamedItem("size").getTextContent());
		}
		if (fuenteXML.equals("F1"))
			fuentes = "Helvetica";
		else if (fuenteXML.equals("F2"))
			fuentes = "Helvetica-Bold";
		else if (fuenteXML.equals("F3"))
			fuentes = "Helvetica-Oblique";
		else if (fuenteXML.equals("F4"))
			fuentes = "Helvetica-BoldOblique";
		else if (fuenteXML.equals("F5"))
			fuentes = "Courier";
		else if (fuenteXML.equals("F6"))
			fuentes = "Courier-Bold";
		else if (fuenteXML.equals("F7"))
			fuentes = "Courier-Oblique";
		else if (fuenteXML.equals("F8"))
			fuentes = "Courier-BoldOblique";
		else
			fuentes = BaseFont.HELVETICA_BOLD;
		String encoding = BaseFont.CP1252;// "winansi";
		try {
			BaseFont bf = BaseFont.createFont(fuentes, encoding, BaseFont.NOT_EMBEDDED);
			pcb.setFontAndSize(bf, s);
			ff = new Font(bf);
			ff.setSize(s);
			ff.setFamily(encoding);
		} catch (Exception e) {
			Logger.trace(e.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
		return "ok";
	}

	/**
	 * Muestra una cadena de texto alineada a la izquierda.
	 * @param node
	 * @return
	 */
	public String pdf_show_text(Node node) {
		Node n1 = node;
		String texto = n1.getTextContent();
		if (node.getFirstChild() != null)
			texto = node.getFirstChild().getTextContent();
		if (!validString(texto))
			texto = "";
		if (Trim(texto).endsWith("\n")) {
			int p = texto.lastIndexOf("\n");
			texto = texto.substring(0, p) + texto.substring(p + 1, texto.length());
		}
		String regex = "&#([0-9]+);/ig";
		Pattern ree = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		if (Pattern.matches(regex, texto)) {
			if (texto.contains("&#8364")) {
				String cb = "€";
				texto = texto.replaceAll(regex, cb);
			}
		}
		Node padre = n1.getParentNode();
		float x = 0, y = 0;

		// si forman parte de un bloque sus coordenadas seran relativas al bloque

		if (padre.getNodeName().equals("bloque")
				&& (padre.getAttributes().item(1) != null && padre.getAttributes().item(2) != null)) {
			x = parseInt(n1.getAttributes().getNamedItem("x").getTextContent())
					+ parseInt(padre.getAttributes().item(1).getTextContent());
			y = parseInt(n1.getAttributes().getNamedItem("y").getTextContent())
					+ parseInt(padre.getAttributes().item(2).getTextContent());
		} else {
			x = parseInt(n1.getAttributes().getNamedItem("x").getTextContent());
			y = parseInt(n1.getAttributes().getNamedItem("y").getTextContent());
		}
		float x1 = (int) (oPDF.getPageSize().getWidth() - x - oPDF.rightMargin() / 2);
		float y1 = (int) (ff.getSize()) + 2;
		if (n1.getAttributes().getNamedItem("x1") != null)
			x1 = parseInt(n1.getAttributes().getNamedItem("x1").getTextContent());
		if (n1.getAttributes().getNamedItem("y1") != null)
			y1 = parseInt(n1.getAttributes().getNamedItem("y1").getTextContent());

		Node codigo = n1.getAttributes().getNamedItem("codigo");
		Node modo = n1.getAttributes().getNamedItem("modo");
		int modostr = 0;
		if (modo != null)
			modostr = parseInt(n1.getAttributes().getNamedItem("modo").getNodeValue());
		if (modostr < 0)
			modostr = 0;
		if (modostr == -1) {
			modostr = PdfContentByte.ALIGN_LEFT;
		} else if (modostr == 1) {
			modostr = PdfContentByte.ALIGN_LEFT;
		} else if (modostr == 2) {
			modostr = PdfContentByte.ALIGN_LEFT;
		} else if (modostr == 3) {
			modostr = PdfContentByte.ALIGN_LEFT;
		} else if (modostr == 6) {
			/*ASB alinear centnro excepción **/
			modostr = PdfContentByte.ALIGN_CENTER;
		}

		y -= 2;
		if (y < 0)
			y = 0;

		if (n1.getAttributes().getNamedItem("codigo") != null) {
			int textnum = parseInt(texto);
			int b1 = textnum / 256;
			b1 = (b1 > 128) ? b1 - 256 : b1;
			int b2 = textnum % 256;
			b2 = (b2 > 128) ? b2 - 256 : b2;
			try {
				texto = new String(new byte[] { (byte) b1, (byte) b2 }, "UTF-16");
			} catch (Exception e) {
				Logger.trace(e.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			}
		} else {
			// prueba de dimensiones
			y = y - y1 + ff.getSize() + 2;
		}
		ColumnText ct = new ColumnText(pcb);
		ct.setSimpleColumn(new Phrase(texto, ff), x, y, x + x1, y + y1, ff.getSize(), modostr);

		if (true || codigo != null) {
			try {
				ct.go();
			} catch (Throwable t) {
				Logger.trace(t.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			}
		} else {
			int i, j;
			i = 0;
			j = texto.indexOf("\n");
			if (j == -1)
				j = texto.length();
			pcb.beginText();//
			pcb.setTextMatrix(x, y);
			pcb.showText(texto.substring(i, j));
			PdfTemplate gg;
			i = j + 1;
			while (j != texto.length()) {
				j = texto.indexOf("\n", i);
				if (j == -1)
					j = texto.length();
				pcb.setTextMatrix(x, y += 12);
				pcb.showText(texto.substring(i, j));
				i = j + 1;
			}
		pcb.endText();
		}
		return "ok";
	}

	/**
	 * Muestra una cadena de texto.
	 * @param node
	 * @return
	 */
	public String pdf_show_text_continue(Node node) {

		Node n1 = node;
		String texto = n1.getTextContent();
		if (node.getFirstChild() != null)
			texto = node.getFirstChild().getTextContent();
		if (texto == null || texto.length() == 0)
			texto = "";
		String regex = "&#([0-9]+);/ig";
		Pattern ree = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		if (Pattern.matches(regex, texto)) {// texto.matches(ree)!=null){
			String cb = "€";
			texto = texto.replaceAll(regex, cb);
		}
		pcb.beginText();
		if (texto == null || texto.length() == 0)
			texto = "";
		pcb.showText(texto);
		pcb.endText();
		return "ok";
	}

	/**
	 * Muestra una cadena de texto dentro de una caja.
	 * @param node
	 * @return
	 */
	public String pdf_show_boxed(Node node) {

		Node n1 = node;
		String texto = n1.getTextContent();
		if (node.getFirstChild() != null)
			texto = node.getFirstChild().getTextContent();
		if (texto == null || texto.length() == 0)
			texto = "";
		if (Trim(texto).endsWith("\n")) {
			int p = texto.lastIndexOf("\n");
			texto = texto.substring(0, p) + texto.substring(p + 1, texto.length());
		}

		Node padre = n1.getParentNode();
		float l = 0;
		float t = 0;
		// si forman parter de un bloque sus coordenadas seran relativas al bloque
		if (padre.getNodeName().equals("bloque")
				&& (padre.getAttributes().item(1) != null && padre.getAttributes().item(2) != null)) {
			l = parseInt(n1.getAttributes().getNamedItem("x").getTextContent())
					+ parseInt(padre.getAttributes().item(1).getTextContent());
			t = parseInt(n1.getAttributes().getNamedItem("y").getTextContent())
					+ parseInt(padre.getAttributes().item(2).getTextContent());

		} else {
			l = parseInt(n1.getAttributes().getNamedItem("x").getTextContent());
			t = parseInt(n1.getAttributes().getNamedItem("y").getTextContent());
		}

		float w = oPDF.getPageSize().getWidth() - l - oPDF.rightMargin() / 2;
		if (n1.getAttributes().getNamedItem("x1") != null)
			w = parseInt(n1.getAttributes().getNamedItem("x1").getTextContent());
		float h = ff.getSize() + 2;
		if (n1.getAttributes().getNamedItem("y1") != null)
			h = parseInt(n1.getAttributes().getNamedItem("y1").getTextContent());
		t = t - h / 2;
		int modo = parseInt(n1.getAttributes().getNamedItem("modo").getTextContent());
		int modo1 = 0;
		if (modo == 1) {
			modo1 = PdfContentByte.ALIGN_LEFT;
		} else if (modo == 2) {
			modo1 = PdfContentByte.ALIGN_CENTER;
		} else if (modo == 3) {
			modo1 = PdfContentByte.ALIGN_RIGHT;
		} else if (modo == 4) {
			modo1 = PdfContentByte.TEXT_RENDER_MODE_FILL;
		} else if (modo == 6) {
			/*ASB alinear centnro excepción **/
			modo1 = PdfContentByte.ALIGN_CENTER;
		}else {
			modo1 = PdfContentByte.TEXT_RENDER_MODE_FILL;
		}
		ColumnText ct = new ColumnText(pcb);
		ct.setSimpleColumn(new Phrase(texto, ff), l, t, l + w, t + h, ff.getSize(), modo1);

		try {
			ct.go();
		} catch (DocumentException e) {
			Logger.trace(e.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
		return "ok";
	}

	/**
	 * Dibuja un rectangulo con las coordenadas especificadas.
	 * @param node Nodo con atributos "x","y","x1","y1"
	 * @return
	 */
	public String pdf_rect(Node node) {

		Node n1 = node;
		float x = parseInt(n1.getAttributes().getNamedItem("x").getTextContent());
		float y = parseInt(n1.getAttributes().getNamedItem("y").getTextContent());
		float dx = parseInt(n1.getAttributes().getNamedItem("x1").getTextContent());
		float dy = -parseInt(n1.getAttributes().getNamedItem("y1").getTextContent());
		pcb.moveTo(x, y);
		pcb.rectangle(x, y, dx, dy);
		pcb.stroke();
		return "ok";
	}

	/**
	 * Dibuja una linea entre las coordenadas (x1,y1)-(x2,y2) .
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public String pdf_line(String x1, String y1, String x2, String y2) {

		pcb.moveTo(parseInt(x1), parseInt(y1));
		pcb.lineTo(parseInt(x2), parseInt(y2));
		pcb.stroke();
		return "ok";
	}

	/**
	 * Convierte un numero en un codigo de barras.
	 * @param dato Codigo numerico; de lo contrario se considera vacio.
	 * @return Codigo de barras
	 */
	public static String codBarras(String dato) {
		String[] patrones = new String[] { "|| ||  ||  ", "||  || ||  ", "||  ||  || ", "|  |  ||   ", "|  |   ||  ",
				"|   |  ||  ", "|  ||  |   ", "|  ||   |  ", "|   ||  |  ", "||  |  |   ", "||  |   |  ",
				"||   |  |  ", "| ||  |||  ", "|  || |||  ", "|  ||  ||| ", "| |||  ||  ", "|  ||| ||  ",
				"|  |||  || ", "||  |||  | ", "||  | |||  ", "||  |  ||| ", "|| |||  |  ", "||  ||| |  ",
				"||| || ||| ", "||| |  ||  ", "|||  | ||  ", "|||  |  || ", "||| ||  |  ", "|||  || |  ",
				"|||  ||  | ", "|| || ||   ", "|| ||   || ", "||   || || ", "| |   ||   ", "|   | ||   ",
				"|   |   || ", "| ||   |   ", "|   || |   ", "|   ||   | ", "|| |   |   ", "||   | |   ",
				"||   |   | ", "| || |||   ", "| ||   ||| ", "|   || ||| ", "| ||| ||   ", "| |||   || ",
				"|   ||| || ", "||| ||| || ", "|| |   ||| ", "||   | ||| ", "|| ||| |   ", "|| |||   | ",
				"|| ||| ||| ", "||| | ||   ", "||| |   || ", "|||   | || ", "||| || |   ", "||| ||   | ",
				"|||   || | ", "||| |||| | ", "||  |    | ", "||||   | | ", "| |  ||    ", "| |    ||  ",
				"|  | ||    ", "|  |    || ", "|    | ||  ", "|    |  || ", "| ||  |    ", "| ||    |  ",
				"|  || |    ", "|  ||    | ", "|    || |  ", "|    ||  | ", "||    |  | ", "||  | |    ",
				"|||| ||| | ", "||    | |  ", "|   |||| | ", "| |  ||||  ", "|  | ||||  ", "|  |  |||| ",
				"| ||||  |  ", "|  |||| |  ", "|  ||||  | ", "|||| |  |  ", "||||  | |  ", "||||  |  | ",
				"|| || |||| ", "|| |||| || ", "|||| || || ", "| | ||||   ", "| |   |||| ", "|   | |||| ",
				"| |||| |   ", "| ||||   | ", "|||| | |   ", "|||| |   | ", "| ||| |||| ", "| |||| ||| ",
				"||| | |||| ", "|||| | ||| ", "|| |    |  ", "|| |  |    ", "|| |  |||  ", "||   ||| | ||" };
		Map<String, Integer> patronesB = new HashMap<String, Integer>();
		patronesB.put("0", Integer.valueOf(16));
		patronesB.put("1", Integer.valueOf(17));
		patronesB.put("2", Integer.valueOf(18));
		patronesB.put("3", Integer.valueOf(19));
		patronesB.put("4", Integer.valueOf(20));
		patronesB.put("5", Integer.valueOf(21));
		patronesB.put("6", Integer.valueOf(22));
		patronesB.put("7", Integer.valueOf(23));
		patronesB.put("8", Integer.valueOf(24));
		patronesB.put("9", Integer.valueOf(25));
		Map<String, Integer> patronesC = new HashMap<String, Integer>();
		patronesC.put("CODEB", Integer.valueOf(100));
		patronesC.put("CODEA", Integer.valueOf(101));
		patronesC.put("FNC1", Integer.valueOf(102));
		patronesC.put("STARTA", Integer.valueOf(103));
		patronesC.put("STARTB", Integer.valueOf(104));
		patronesC.put("STARTC", Integer.valueOf(105));
		patronesC.put("STOP", Integer.valueOf(106));

		String quiet = "          ";
		//el patron ha de ser un numero entero
		if (dato == null)
			dato = "";
		try {
			dato = dato.replace(" ", "");
			if (dato.length() > 0)
				if (!StringUtils.isNumeric(dato))
					dato = "";
		} catch (NumberFormatException e) {
			Logger.trace(e.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			dato = "";
		}
		int check = ((Integer) patronesC.get("STARTC")).intValue() + ((Integer) patronesC.get("FNC1")).intValue();
		String codigo = quiet + patrones[((Integer) patronesC.get("STARTC")).intValue()]
				+ patrones[((Integer) patronesC.get("FNC1")).intValue()];
		int i = 0, j = 2, l = (int) (dato.length() / 2) * 2, num;
		String numstr;
		while (i < l) {
			num = parseInt(dato.substring(i, i + 2), 10);
			check += j * num;
			codigo += patrones[num];
			i += 2;
			j += 1;
		}
		if (dato.length() > l) {
			numstr = dato.substring(l, dato.length());
			check += j * ((Integer) patronesC.get("CODEB")).intValue();
			check += (j + 1) * ((Integer) patronesB.get(numstr)).intValue();
			codigo += patrones[((Integer) patronesC.get("CODEB")).intValue()]
					+ patrones[((Integer) patronesB.get(numstr)).intValue()];
		}
		check = check % 103;
		codigo += patrones[check] + patrones[((Integer) patronesC.get("STOP")).intValue()] + quiet;

		return (codigo);
	}

	/**
	 * Dibuja un codigo de barras en el Documento.
	 * @param nodo Nodo con los atributos "x","y" y un valor; por defecto, 0,0,""
	 * @return
	 */
	public String pdf_barras(Node nodo) {
		Node n = nodo;
		String dato = n.getTextContent();
		if (!validString(dato) && n.hasChildNodes())
			dato = n.getFirstChild().getTextContent();
		float x = parseInt(n.getAttributes().getNamedItem("x").getTextContent());
		float y = parseInt(n.getAttributes().getNamedItem("y").getTextContent());
		if (validString(dato)) {
			dato = dato.replaceAll(" ", "");
			dato = dato.replaceAll("\n", "");
			dato = dato.replaceAll("\t", "");
		}
		String codigo = codBarras(dato);
		float h = 25; // alto de cada barra
		float w = (float) 0.72;
		pcb.setLineWidth(w);
		for (int i = 0; i < codigo.length(); i++) {
			char c = codigo.charAt(i);
			if (c == '|') {
				pcb.moveTo(x, y);
				pcb.lineTo(x, y + h);
				pcb.stroke();
			}
			x += w;
		}
		pcb.setLineWidth(1);

		return "ok";
	}

	/**
	 * Inserta una linea en el Documento.
	 * @param nodo Nodo con los atributos "x","y","x1","y1","grosor","discontinuo"; por defecto, 0,0,0,0,1,-
	 * @return Mensajes de error
	 */
	public String pdf_linea(Node nodo) {

		Node n = nodo;// .getNextSibling();
		Node nxy = n.getAttributes().getNamedItem("x");
		int error = 0;
		float x = 0;
		float y = 0;
		float x1 = 0;
		float y1 = 0;
		String errormsg = "";
		if (nxy != null)
			x = parseInt(nxy.getNodeValue());
		else {
			errormsg += ("Debe introducirse un valor x para la linea" + "<br>\n");
			error = 1;
		}

		nxy = n.getAttributes().getNamedItem("y");
		if (nxy != null)
			y = parseInt(nxy.getNodeValue());
		else {
			errormsg += ("Debe introducirse un valor y para la linea" + "<br>\n");
			error = 1;
		}

		nxy = n.getAttributes().getNamedItem("x1");
		if (nxy != null)
			x1 = parseInt(nxy.getNodeValue());
		else {
			errormsg += ("Debe introducirse un valor x1 para la linea" + "<br>\n");
			error = 1;
		}

		nxy = n.getAttributes().getNamedItem("y1");
		if (nxy != null)
			y1 = parseInt(nxy.getNodeValue());
		else {
			errormsg += ("Debe introducirse un valor y1 para la linea" + "<br>\n");
			errormsg += ("Debe introducirse un valor y1 para la linea\n");
			error = 1;
		}

		pcb.saveState();
		Node ngrosor = n.getAttributes().getNamedItem("grosor");
		if (ngrosor != null) {
			int grosor = parseInt(ngrosor.getNodeValue());
			pcb.setLineWidth(grosor);
		}
		Node ndiscontinuo = n.getAttributes().getNamedItem("discontinuo");
		if (ndiscontinuo != null) {
			int discontinuo = parseInt(ndiscontinuo.getNodeValue());
			if (discontinuo == 1)
				pcb.setLineDash(2, 2);
		}

		if (error == 0) {
			pcb.moveTo(x, y);
			pcb.lineTo(x1, y1);
			pcb.stroke();
		}
		if (error != 0) {
			es.tributasenasturias.documentopagoutils.Logger.error(errormsg,es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			//objLog.debug(errormsg);
		}

		pcb.restoreState();
		pcb.setLineWidth(1);
		return "ok";
	}

	// metodos para construir el xml local
	/**
	 * Asigna valor al nodo con el ID especificado.
	 * @param nombre Nombre del nodo
	 * @param v Valor a asignar
	 */
	public void Campo(String nombre, String v) {
		Node nodo = selectSingleNode(fuente, ".//*[@ID='" + nombre + "']");
		if (nodo != null) {
			//MAC: Incidencia 32
			//if(StringUtils.isNotBlank(v)) v = v.toUpperCase();
			nodo.setTextContent(v);
			nodo.setNodeValue(v);
		}
	}

	/**
	 * Borra el nodo con el ID especificado.
	 * @param nombre Nombre del nodo
	 */
	public void BorrarCampo(String nombre) {
		Node nodo = selectSingleNode(fuente, ".//*[@ID='" + nombre + "']");
		if (nodo != null) {
			Node p = nodo.getParentNode();
			p.removeChild(nodo);
		}
	}

	/**
	 * Funcion sin utilidad actual(ver codigo antiguo). Mueve los nodos fuera de los limites de la pagina de vuelta a la
	 * misma (en formato A4).
	 * 
	 * @deprecated
	 */
	public void Paginas() {
		Element[] campos = selectNodes(fuente, ".//*/*/*");
		for (int j = 0; j < campos.length; j++) {
			String nombreNodo = campos[j].getNodeName();
			if (!nombreNodo.equals("bloque") && !nombreNodo.equals("Fila") && !nombreNodo.equals("grupo")
					&& !nombreNodo.equals("orga")) {
				int n = 0;
				if (n < 0) {

					Node nodo = selectSingleNode(fuente, ".//*[@ID='"
							+ campos[j].getAttributes().getNamedItem("ID").getNodeValue() + "']");
					campos[j].getAttributes().getNamedItem("y").setNodeValue(
							campos[j].getAttributes().getNamedItem("y").getNodeValue() + 800);
				}
			}
		}

		for (int j = 0; j < campos.length; j++) {
			String nombreNodo = campos[j].getNodeName();
			if (!nombreNodo.equals("bloque") && !nombreNodo.equals("Fila") && !nombreNodo.equals("grupo")
					&& !nombreNodo.equals("orga")) {
				String n = campos[j].getAttributes().getNamedItem("y").getNodeValue();
				String t = campos[j].getTextContent();
			}
		}
	}

	/**
	 * Repagina un documento en A4.
	 * @param espacio Margen superior de la pagina; por defecto 780.
	 * @param desplazamiento Margen inferior de la pagina; por defecto 50.
	 * @param w Ancho de linea; por defecto 8
	 * @return
	 */
	public int Paginas2(String espacio, String desplazamiento, String w) {

		int salto; // Margen superior
		if (validString(desplazamiento))
			salto = parseInt(desplazamiento, 10);
		else
			salto = 780;

		int limite; // margen inferior
		if (validString(espacio))
			limite = parseInt(espacio, 10);
		else
			limite = 40;

		int ancho; // ancho de linea
		if (validString(w))
			ancho = parseInt(w, 10);
		else
			ancho = 8;

		int despl = 0;
		Element[] campos = selectNodes(fuente, ".//*");
		Node nuevapag = null;
		Element[] seleccion;
		int pag = 1; // Pagina en la que nos encontramos
		int offset = 0;
		int n1 = limite;

		Node pagina = selectSingleNode(fuente, ".//Pagina");
		Element[] pie = selectNodes(fuente, ".//pie");

		for (int j = 0; j < campos.length; j++) {

			String nombreNodo = campos[j].getNodeName();

			if (!nombreNodo.equals("bloque") && !nombreNodo.equals("Fila") && !nombreNodo.equals("grupo")
					&& !nombreNodo.equals("orga") && !nombreNodo.equals("pie") && !nombreNodo.equals("Paginas")
					&& !nombreNodo.equals("Pagina") && !nombreNodo.equals("imagenes")) {

				String n = campos[j].getAttributes().getNamedItem("y").getNodeValue();
				if (nombreNodo.equals("cajatexto")) {
					offset = (cuentaLineas(campos[j].getTextContent(), 110) * ancho); // 8=grosor
					n1 = parseInt(n) - offset;

				} else if (nombreNodo.equals("cuadro")) {
					String offset_n = campos[j].getAttributes().getNamedItem("y1").getNodeValue();
					offset = parseInt(offset_n);
					n1 = parseInt(n) - offset;
				} else {
					offset = 0;
					n1 = limite;
				}
				String t = campos[j].getTextContent();
				// Si pertenece a otra pagina modificamos el nodo, en caso contrario no hacemos nada
				if (campos[j].getParentNode().getNodeName().equals("pie"))
					n = "" + limite;
				int n_num = parseInt(n);
				
				// Si no cabe en la primera pagina
				if (n_num < limite || n1 < limite) {
					// Crear nueva pagina
					if (((parseInt(n) + (despl)) < limite) || (((n1) + (despl)) < limite)) {
						nuevapag = pagina.cloneNode(true);
						seleccion = selectNodes(nuevapag, ".//*");
						removeAll(seleccion);// nuevo metodo
						pagina.getParentNode().insertBefore(nuevapag, pagina.getNextSibling());
						pagina = nuevapag;
						pag++;

						if (pie != null) {
							for (int l = 0; l < pie.length; l++) {
								Node nuevopie = pie[l].cloneNode(true);
								nuevapag.appendChild(nuevopie);
							}
						}
						despl = salto - parseInt(n);
					}
					// Si el nodo tiene el atributo salto distinto de null, si se cambia de pagina se cambian todos los
					// nodos del bloque al que pertenece

					if (campos[j].getAttributes().getNamedItem("salto") != null) {
						Element[] bloque = selectNodes(campos[j].getParentNode(), ".//*");

						for (int k = 0; k < bloque.length; k++) {
							nombreNodo = bloque[k].getNodeName();
							if (bloque[k].getAttributes().getNamedItem("y") != null)
								bloque[k].getAttributes().getNamedItem("y").setNodeValue(
										"" + Val(bloque[k].getAttributes().getNamedItem("y").getNodeValue()) + (despl));// -
							if (nombreNodo.equals("linea"))
								bloque[k].getAttributes().getNamedItem("y1")
										.setNodeValue(
												"" + Val(bloque[k].getAttributes().getNamedItem("y1").getNodeValue())
														+ (despl));
							if (nuevapag != null)
								nuevapag.appendChild(bloque[k]);
						}

					} else {
						campos[j].getAttributes().getNamedItem("y").setNodeValue(
								"" + Val(campos[j].getAttributes().getNamedItem("y").getNodeValue()) + (despl));
						if (nombreNodo.equals("linea")) {
							campos[j].getAttributes().getNamedItem("y1").setNodeValue(
									"" + Val(campos[j].getAttributes().getNamedItem("y1").getNodeValue()) + (despl));
						}
						nuevapag.appendChild(campos[j]);
					}
				}
			}
		}
		return pag;
	}

	/**
	 * Compila y escribe el Documento al OutputStream informado.
	 * @return
	 */
	public String Mostrar() {

		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("autor", "autor");
			params.put("titulo", this.getClass().getName());
			PDFParserHandler myparser = new PDFParserHandler(this, fuente);
			myparser.parse();
			return "ok";
		} catch (Exception eee) {
			Logger.trace(eee.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			return "error";
		}
	}

	/**
	 * Compila y escribe el Documento al OutputStream informado, asigna los parametros informados.
	 * @param params HashMap con elementos "creador,"autor","titulo"
	 * @return
	 */
	public String Mostrar(HashMap<String, String> params) {

		try {
			PDFParserHandler myparser = new PDFParserHandler(this, fuente, params);
			myparser.parse();
			return "ok";
		} catch (Exception eee) {
			Logger.trace(eee.getStackTrace(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			return "error";
		}
	}
	
	
	/**
	 * Funcion que inserta los datos del vector que se le pasa como parametro (v) en la fila con identificador 'id'. Si
	 * hubiese mas de una linea, se duplica la fila y se inserta. La separacion entre lineas se le pasa como
	 * parametro(desplazamiento). la funcion devuelve el numero de lineas que forman la Fila. Si dentro de la fila
	 * existe un cuadro que rodee a todos los campos de la fila se alarga.
	 * 
	 * @param id ID de la fila doonde insertar
	 * @param v Lista de datos a insertar
	 * @param desplazamiento separacion entre lineas
	 * @return
	 */
	public String Fila(String id, String[] v, int desplazamiento) {
		return Fila(id, v, desplazamiento + "");
	}

	/**
	 * Funcion que inserta los datos del vector que se le pasa como parametro (v) en la fila con identificador 'id'. Si
	 * hubiese mas de una linea, se duplica la fila y se inserta. La separacion entre lineas se le pasa como
	 * parametro(desplazamiento). la funcion devuelve el numero de lineas que forman la Fila. Si dentro de la fila
	 * existe un cuadro que rodee a todos los campos de la fila se alarga.
	 * 
	 * @param id
	 * @param v
	 * @param desplazamiento
	 * @return
	 */
	public String Fila(String id, String[] v, String desplazamiento) {

		float despl = 10;
		if (validString(desplazamiento))
			despl = parseInt(desplazamiento, 10);

		// fila con los datos a sustituir
		Node fila = selectSingleNode(fuente, ".//Fila[@ID='" + id + "']");
		Node filaOrigen = fila;

		// campos con los datos a sustituir
		Element[] campos = selectNodes(fila, ".//*");
		if (campos == null || campos.length == 0)
			return "error";

		// adaptar el tamanyo del vector v
		int restaV = 0;
		if (campos.length != 0)
			restaV = v.length % campos.length;
		for (int i = v.length - restaV; i < v.length; i++)
			v[i] = "";
		
		// si existe el cuadro lo sacamos de la fila, haciendo que sea hijo del padre de la
		// fila
		Node cuadro = selectSingleNode(fila, ".//cuadro");
		if (cuadro != null) {
			cuadro.getParentNode().getParentNode().appendChild(cuadro);
			campos = selectNodes(fila, ".//*");
			if (campos == null || campos.length == 0)
				return "error";
			String ca = "SI";
		}

		// sustitucion
		int i = 0, k = 0;
		for (k = 0; k < v.length / campos.length; k++) {
			// se duplica el nodo si hay mas lineas
			if (k > 0) {
				Node l = filaOrigen.cloneNode(true);
				try {
					fila.getParentNode().insertBefore(l, fila.getNextSibling());
				} catch (Exception exception) {
					String r = reportRuntimeError(exception);
					Logger.trace(exception.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				}
				fila = l;
			}

			Element[] c = campos;
			// modificar el texto y la posicion
			for (int j = 0; j < campos.length; j++) {
				c[j].setTextContent(v[i++]);
				float y = parseInt(c[j].getAttributes().getNamedItem("y").getNodeValue());
				Node linea = selectSingleNode(fila, ".//linea");
				if (k > 0) {
					c[j].getAttributes().getNamedItem("y").setNodeValue("" + (y - despl));
					// si hay una linea horizontal modificamos y1
					if (c[j].getNodeName().equals("linea")) {
						c[j].getAttributes().getNamedItem("y1").setNodeValue(
								c[j].getAttributes().getNamedItem("y").getNodeValue());
					}
				}
			}

		}
		// Si existe el cuadro lo alargamos

		if (cuadro != null) {
			float y1 = parseInt(cuadro.getAttributes().getNamedItem("y1").getNodeValue());
			cuadro.getAttributes().getNamedItem("y1").setNodeValue("" + (y1 + despl * (k - 1)));
		}
		return "" + (k - 1);
	}
	
	/**
	 * Funcion que inserta los datos del vector que se le pasa como parametro (v) en la fila con identificador 'id'. Si
	 * hubiese mas de una linea, se duplica la fila y se inserta. La separacion entre lineas se le pasa como
	 * parametro(desplazamiento). la funcion devuelve el numero de lineas que forman la Fila. Si dentro de la fila
	 * existe un cuadro que rodee a todos los campos de la fila se alarga.
	 * 
	 * @param id
	 * @param v
	 * @param desplazamiento
	 * @return
	 */
	public String Fila2(String id, String[] v, String desplazamiento, String dup) {
		int despl;
		if (validString(desplazamiento))
			despl = parseInt(desplazamiento, 10);
		else
			despl = 10;
		// fila con los datos a sustituir

		int copia;
		if (!validString(dup))
			copia = 1;
		else
			copia = 0;

		Node fila = selectSingleNode(fuente, ".//Fila[@ID='" + id + "']");
		// campos con los datos a sustituir
		Element[] campos = selectNodes(fila, ".//*");
		// adaptar el tamanyo del vector v
		while (v.length % campos.length != 0 || v.length == 0)
			v[v.length] = "";

		// si existe el cuadro lo sacamos de la fila, haciendo que sea hijo del padre de la
		// fila
		Node cuadro = null;// 0;

		if (copia == 0) {
			cuadro = selectSingleNode(fila, ".//cuadro");
			if (cuadro != null) {
				cuadro.getParentNode().getParentNode().appendChild(cuadro);
				campos = selectNodes(fila, ".//*");
				String ca = "SI";
			}
		}
		// sustitucion
		int i = 0, k = 0;
		for (k = 0; k < v.length / campos.length; k++) {
			// se duplica el nodo si hay mas lineas
			if (k > 0) {
				Node l = fila.cloneNode(true);
				try {
					fila.getParentNode().insertBefore(l, fila.getNextSibling());
				} catch (Exception exception) {
					String r = reportRuntimeError(exception);
					Logger.trace(exception.getStackTrace(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				}
				fila = l;
			}

			Element[] c = selectNodes(fila, ".//*"); // /campos;
			// modificar el texto y la posicion
			for (int j = 0; j < campos.length; j++) {
				c[j].setTextContent(v[i++]);
				float y = parseInt(c[j].getAttributes().getNamedItem("y").getNodeValue());
				Node linea = selectSingleNode(fila, ".//linea");
				if (k > 0) {
					c[j].getAttributes().getNamedItem("y").setNodeValue("" + (y - despl));
					// si hay una linea horizontal modificamos y1
					if (c[j].getNodeName().equals("linea")) {
						c[j].getAttributes().getNamedItem("y1").setNodeValue(
								"" + (parseInt(c[j].getAttributes().getNamedItem("y1").getNodeValue()) - despl));
					}
				}
			}
		}

		// Si existe el cuadro lo alargamos
		if (cuadro != null && copia == 0) {
			float y1 = parseInt(cuadro.getAttributes().getNamedItem("y1").getNodeValue());
			cuadro.getAttributes().getNamedItem("y1").setNodeValue("" + (y1 + despl * (k - 1) + (5)));
		}

		return "" + (k - 1);
	}

	/**
	 * Funcion que crea una copia del Bloque con el ID especificado y lo desplaza tantas unidades como indique el
	 * parámetro desplazamiento. El nuevo bloque tiene ID=id+coletilla
	 * 
	 * @param id
	 * @param desplazamiento
	 * @param coletilla 
	 */
	public void CopiaBloque(String id, String desplazamiento, String coletilla) {
		int despl;
		if (validString(desplazamiento) && !desplazamiento.equals("0"))
			despl = parseInt(desplazamiento, 10);
		else
			despl = 10;

		Node bloque = selectSingleNode(fuente, ".//bloque[@ID='" + id + "']");
		Element[] campos = selectNodes(bloque, ".//*");
		Element newbloque = (Element) bloque.cloneNode(true);
		newbloque.setAttribute("ID", newbloque.getAttributes().getNamedItem("ID").getNodeValue() + coletilla);
		try {
			bloque.getParentNode().insertBefore(newbloque, bloque.getNextSibling());
		} catch (Exception exception) {
			String r = reportRuntimeError(exception);
			Logger.trace(exception.getStackTrace(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}

		campos = selectNodes(newbloque, ".//*");

		for (int j = 0; j < campos.length; j++) {
			campos[j].setAttribute("ID", campos[j].getAttributes().getNamedItem("ID").getNodeValue() + coletilla);
			// Borro el contenido de los campos y los desplazo
			if (!campos[j].getNodeName().equals("Fila") && !campos[j].getNodeName().equals("bloque")) {
				if (campos[j].getAttributes().getNamedItem("type").getNodeValue().equals("109"))
					campos[j].setTextContent("");

				if (!campos[j].getNodeName().equals("linea")) {
					float y = parseInt(campos[j].getAttributes().getNamedItem("y").getNodeValue());
					campos[j].getAttributes().getNamedItem("y").setNodeValue("" + (y - despl));
				} else {
					float y = parseInt(campos[j].getAttributes().getNamedItem("y").getNodeValue());
					campos[j].getAttributes().getNamedItem("y").setNodeValue("" + (y - despl));
					float y1 = parseInt(campos[j].getAttributes().getNamedItem("y1").getNodeValue());
					campos[j].getAttributes().getNamedItem("y1").setNodeValue("" + (y1 - despl));

				}

			}
		}

		campos = selectNodes(newbloque, ".//Fila");
		if (campos.length > 0) {
			while (campos.length > 1) {
				Node p = campos[1].getParentNode();
				p.removeChild(campos[1]);
				campos = selectNodes(newbloque, ".//Fila");
			}
		}
	}

	/**
	 * Funcion que desplaza un bloque hacia abajo una determinada distancia.
	 * @param nombre Nombre del bloque
	 * @param desplazamiento Distancia; por defecto, 0
	 */
	public void MueveBloque(String nombre, String desplazamiento) {
		int despl = 0;
		if (validString(desplazamiento) && !desplazamiento.equals("0"))
			despl = parseInt(desplazamiento, 10);
		int inicioPagina = 800;
		// seleccionamos todos los nodos del bloque
		Node bloque = selectSingleNode(fuente, ".//bloque[@ID='" + nombre + "']");
		Element[] nodos = selectNodes(bloque, ".//*");

		// modificamos el atributo y de cada nodo (si es una linea,
		// ademas habra que modificar la propiedad y1)

		for (int j = 0; j < nodos.length; j++) {
			Node n = nodos[j];

			Node at = n.getAttributes().getNamedItem("y");
			if (at != null)
				at.setNodeValue("" + (parseInt(at.getNodeValue()) - despl));

			if (n.getNodeName().equals("linea")) {
				at = n.getAttributes().getNamedItem("y1");
				if (at != null)
					at.setNodeValue("" + (parseInt(at.getNodeValue()) - despl));
			}
		}
	}

	/**
	 * Funcion que busca en cada una de las paginas del documento el nodo con el ID que le pasamos como parametro; se
	 * introduce en el texto "Pagina #num_pag# de #pag_totales#".
	 * 
	 * @param paginas #pag_totales#
	 * @param nombre ID del Nodo del pie de pagina
	 * @param inicio valor inicial de #num_pag#
	 */
	public void RellenaNumPag(int paginas, String nombre, String inicio) {
		int pag = 1;
		if (validString(inicio))
			pag = parseInt(inicio, 10);
		Element[] nodos = selectNodes(fuente, ".//Pagina");
		for (int j = 0; j < nodos.length; j++) {
			Node n = nodos[j];
			Node pie = selectSingleNode(n, ".//*[@ID='" + nombre + "']");
			if (pie != null) {
				pie.setTextContent("Página " + (pag) + " de " + paginas);
				pag = pag + 1;
			}

		}
	}

	// FUNCION QUE GENERA UN NUEVO NODO
	/**
	 * Copia e inserta en el Documento la pagina con el ID especificado, o la primera pagina existente.
	 * @param id
	 */
	public void NuevaPagina(String id) {
		Node nodo = null;
		if (validString(id)) {
			nodo = selectSingleNode(fuente, ".//Pagina[@ID='" + id + "']");
		}
		if (nodo == null || !validString(id))
			nodo = selectSingleNode(fuente, ".//Pagina");
		if (nodo == null)
			return;
		Node clon = nodo.cloneNode(true);
		try {
			nodo.getParentNode().insertBefore(clon, nodo.getNextSibling());
		} catch (Exception exception) {
			String r = reportRuntimeError(exception);
			Logger.trace(exception.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
	}

	/**
	 * Borra la pagina con el ID especificado.
	 * @param id
	 */
	public void BorraPagina(String id) {
		Node nodo = null;
		if (validString(id))
			nodo = selectSingleNode(fuente, ".//Pagina[@ID='" + id + "']");
		else
			nodo = selectSingleNode(fuente, ".//Pagina");
		try {
			if (nodo != null) {
				Node p = nodo.getParentNode();
				p.removeChild(nodo);
			}
		} catch (Exception exception) {
			String r = reportRuntimeError(exception);
			Logger.trace(exception.getStackTrace(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
		}
	}

	// FUNCION QUE GENERA UN NUEVO NODO
	/**
	 * Copia el Grupo con el ID especificado y lo desplaza una determinada distancia.
	 * @param nombre
	 * @param registro
	 * @param desplazamiento Distancia a desplazar el nodo; por defecto, 50
	 */
	public void Grupo(String nombre, int[] registro, String desplazamiento) {
		int despl;
		if (validString(desplazamiento))
			despl = parseInt(desplazamiento, 10);
		else
			despl = 50;
		int inicioPagina = 800;
		if (oPDF != null)
			inicioPagina = (int) oPDF.getPageSize().getHeight();

		Node grupo = selectSingleNode(fuente, ".//grupo[@ID='" + nombre + "']");
		Element[] nodos = selectNodes(grupo, ".//*");

		// copiar nodos del grupo e introducir textos
		for (int j = 0; j < nodos.length; j++) {
			Element n = nodos[j];
			Node c = n.cloneNode(true);
			Node at = c.getAttributes().getNamedItem("ID");
			if (at != null) {
				int atval = parseInt(at.getNodeValue());
				if (validString(at.getNodeValue()) && atval < registro.length && registro[atval] != -1)
					c.setTextContent("" + registro[atval]);
			}
			n.getParentNode().getParentNode().appendChild(c);
		}

		// mover el grupo de pagina si procede
		boolean nuevaPagina = false;
		for (int j = 0; j < nodos.length && !nuevaPagina; j++) {
			Node n = nodos[j];

			Node at = n.getAttributes().getNamedItem("y");
			if (at != null) {
				if (parseInt(at.getNodeValue()) < despl) {
					nuevaPagina = true;
					despl = despl - inicioPagina;

					Node vpag = grupo.getParentNode();
					Node npag = fuente.createElement("Pagina");
					npag.appendChild(grupo);// insertar en el documento actual
					vpag.getParentNode().insertBefore(npag, vpag.getNextSibling());
				}
			}
		}

		// desplazar el grupo hacia abajo
		for (int j = 0; j < nodos.length; j++) {
			Node n = nodos[j];

			Node at = n.getAttributes().getNamedItem("y");
			if (at != null)
				at.setNodeValue("" + (parseInt(at.getNodeValue()) - despl));

			if (n.getNodeName().equals("linea")) {
				at = n.getAttributes().getNamedItem("y1");
				if (at != null)
					at.setNodeValue("" + (parseInt(at.getNodeValue()) - despl));
			}
		}
	}

	// metodos para implementar XPATH en los nodos
	/**
	 * Devuelve el unico o primer elemento unido al nodo por una condicion XPath.
	 * @param doc 
	 * @param nodename condicion de tipo XPath
	 * @return
	 */
	public static Element selectSingleNode(Node doc, String nodename) {
		if (doc != null) {
			Element[] result = selectNodes(doc, nodename);
			if (result.length > 0)
				return result[0];
		}
		return null;
	}

	/**
	 * Devuelve una lista de elementos unidos al nodo por una condicion XPath.
	 * @param doc 
	 * @param nodename condicion de tipo XPath
	 * @return
	 */
	public static Element[] selectNodes(Node doc, String nodename) {
		if (doc != null) {
			XPathFactory xpfact = XPathFactory.newInstance();
			XPath xp = xpfact.newXPath();
			try {
				DTMNodeList o = (DTMNodeList) xp.evaluate(nodename, doc, XPathConstants.NODESET);
				Element[] result = new Element[o.getLength()];
				for (int i = 0; i < result.length; i++)
					result[i] = (Element) o.item(i);
				return result;
			} catch (XPathExpressionException e) {
				Logger.trace(e.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				return new Element[0];
			}
		}
		return new Element[0];
	}

	/**
	 * Elimina los elementod de la lista.
	 * @param lstElem
	 * @return
	 */
	public static boolean removeAll(Element[] lstElem) {

		boolean ok = true;
		if (lstElem == null)
			return ok;
		for (int i = 0; i < lstElem.length; i++) {
			Node parent = lstElem[i].getParentNode();
			try {
				if (parent != null)
					parent.removeChild(lstElem[i]);
			} catch (DOMException e) {
				Logger.trace(e.getStackTrace(), es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
				ok = false;
			}
		}
		return ok;
	}

	/**
	 * Crea e inicializa el Documento y PDFWriter asociado al OutputStream informado.
	 * @return Documento
	 */
	public com.lowagie.text.Document pcbLoad() {
		com.lowagie.text.Document docu = new com.lowagie.text.Document();
		try {
			if (pdfoutput == null) {
				pdfwriter = PdfWriter.getInstance(docu, new FileOutputStream("plantilla_rellena.pdf"));
			} else
				pdfwriter = PdfWriter.getInstance(docu, pdfoutput);
			// pdfwriter.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
			return docu;
		} catch (Throwable t) {
			Logger.trace(t.getStackTrace(),es.tributasenasturias.documentopagoutils.Logger.LOGTYPE.APPLOG);
			return null;
		}
	}

	/**
	 * Devuelve la fecha actual en formato dd/MM/yyyy.
	 * @return
	 */
	public static String FechaActual() {
		return DateUtil.getTodayAsString("dd/MM/yyyy");
	}

	/**
	 * Une una lista de cadenas con un separador. Ver StringUtils.join(lstElem,separator).
	 * @param lstElem
	 * @param separator
	 * @return
	 */
	private static String join(String[] lstElem, String separator) {
		String cadena = "";
		for (int i = 0; i < lstElem.length; i++) {
			if (lstElem[i] != null && lstElem[i].length() != 0) {
				cadena += lstElem[i] + separator;
			}
		}
		if (cadena.indexOf(separator) != -1)
			cadena = cadena.substring(0, cadena.length() - separator.length());
		return cadena;
	}

	/**
	 * Elimina los caracteres al inicio y final de la cadena iguales al espaciador.
	 * @param cadena
	 * @param espaciador caracter a eliminar; si es una cadena, se toma el primer caracter
	 * @return
	 */
	public static String Trim(String cadena, String espaciador) {
		int length = cadena.length();
		char space = espaciador.charAt(0);
		int count = 0;
		while ((count < length) && (cadena.charAt(count) == space))
			count++;
		int ini = count;
		count = cadena.length() - 1;
		while ((count > ini) && (cadena.charAt(count) == space))
			count--;
		int fin = count;
		return cadena.substring(ini, fin + 1);
	}

}
