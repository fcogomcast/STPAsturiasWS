package com.stpa.ws.server.formularios;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.regex.Pattern;

import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.util.XMLUtils;
	/**
	 * Clase base para los formularios de Modelos,Justificantes y Registros
	 * @author david.gimenez.banos
	 *
	 */
public abstract class FormFillBase {
	
	public static String pathSalidaBase = null;
	protected static DecimalFormat formateador = null;
	protected static DecimalFormatSymbols simbolos = null;

	public static String rellenaXml(String xml, HashMap<String, Object> p_params) {
		return xml;
	}

	public static HashMap<String, String> getMapaCampos(String modelo) {
		HashMap<String, String> mapa = new HashMap<String, String>();
		String[] camposPdf = null;
		String[] camposXml = null;
		camposPdf = new String[0];
		camposXml = new String[0];
		for (int i = 0; i < camposPdf.length && i < camposXml.length; i++) {
			mapa.put(camposPdf[i], camposXml[i]);
		}
		return mapa;
	}

	public static String getContents(URL aFile, String encoding) {
		ByteArrayOutputStream contents = new ByteArrayOutputStream();
		try {
			if (aFile == null)
				return "";
			InputStream is = aFile.openStream();
			byte[] buf = new byte[512];
			int len;
			while ((len = is.read(buf)) > 0) {
				contents.write(buf, 0, len);
			}
		} catch (IOException ex) {
			com.stpa.ws.server.util.Logger.error("Error al recuperar la plantilla: " + ex.getMessage(),ex,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}
		try {
			return contents.toString(encoding);
		} catch (UnsupportedEncodingException e) {
			com.stpa.ws.server.util.Logger.error("Encoding no soportado: " + e.getMessage(),e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}
		return contents.toString();
	}

	/* intenta cargar el archivo desde local, y luego desde una URL */
	public static String getContents(String aFile) {
		String encoding = "UTF-8";
		return getContents(aFile, encoding);
	}

	public static String getContents(String aFile, String encoding) {
		StringBuilder contents = new StringBuilder();

		try {
			try{
				if (!new File(aFile).exists()) {
					URL url = new URL(aFile);
					return getContents(url, encoding);
				}
			}catch(Exception e){
				//Continuamos ya que no es una url
				com.stpa.ws.server.util.Logger.debug("Continuamos ya que no es una url "+e.getMessage(),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			}
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(aFile));
			ByteArrayOutputStream output = new ByteArrayOutputStream();

			byte[] buf = new byte[512];
			int len;
			while ((len = bis.read(buf)) > 0) {
				output.write(buf, 0, len);
			}
			output.close();
			bis.close();
			return output.toString(encoding);

		} catch (IOException ex) {
			com.stpa.ws.server.util.Logger.error("Error al recuperar el contenido de la plantilla: " + ex.getMessage(), ex,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}

		return contents.toString();
	}


	/**
	 * Formatea un valor numerico al formato $$$.$$$.$$$,$$
	 * @param importe
	 * @return
	 */
	public static String formateaImporte(String importe) {
		if (!Pattern.matches("[+-?][0-9,.]+", importe))
			importe = "";
		if (importe != null && !importe.equals("")) {
			String valorcadena = importe.replace(",", ".");
			int comma = valorcadena.lastIndexOf(".");
			int commalast = valorcadena.length() - comma - 1;
			valorcadena = valorcadena.replace(".", "");
			if (comma != -1) {
				valorcadena = valorcadena.substring(0, valorcadena.length() - commalast) + "."
						+ valorcadena.substring(valorcadena.length() - commalast, valorcadena.length());
			}
			Double valor = Double.valueOf(valorcadena);
			if (simbolos == null) {
				simbolos = new DecimalFormatSymbols();
				simbolos.setDecimalSeparator(',');
				simbolos.setGroupingSeparator('.');
			}
			if (formateador == null)
				formateador = new DecimalFormat("0.00", simbolos);
			if (valor == null || valor.isNaN())
				return "0.00";
			if (comma == -1) {
				double valorImp = valor.doubleValue();
				formateador.setMaximumFractionDigits(0);
				valorcadena = formateador.format(valorImp);
				formateador.setMaximumFractionDigits(2);
			} else {
				formateador.setMaximumFractionDigits(2);
				formateador.setGroupingUsed(true);
				formateador.setGroupingSize(3);
				double exponente = Math.pow(10, 2);
				double valorImp = valor.doubleValue() * exponente;
				valorImp = Math.round(valorImp);
				valorImp = valorImp / exponente;
				valorcadena = formateador.format(valorImp);
			}
			return valorcadena;
		} else
			return importe;
	}

	public static void crearCodigoBarras(String emisor, String numeroserie, String temppath, PdfContentByte pcb,
			float xinit, float yinit) throws StpawsException {
		com.stpa.ws.server.util.Logger.debug("crearCodigoBarras(), emisor-numeroserie:" + emisor +"-"+ numeroserie,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		numeroserie = XMLUtils.borraEspacios(numeroserie);
		emisor = XMLUtils.borraEspacios(emisor);
		if (emisor == null)
			emisor = "";
		if (numeroserie == null)
			numeroserie = "";
		
		String datoCodBarras = "90523" + emisor + numeroserie;
		Document docu = new Document();
		docu.setPageSize(new Rectangle(150, 25));
		PdfWriter pdfwrite = null;
		if (pcb == null) {
			pcb = null;
			try {
				pdfwrite = PdfWriter.getInstance(docu, new FileOutputStream(temppath + datoCodBarras + ".pdf"));
				docu.open();
				pcb = pdfwrite.getDirectContent();
			} catch (Throwable t) {
				throw new StpawsException("Error en creacion de codigo de barras", t);
			}
		}
		setFont("Helvetica", 15, pcb);
		float x = 0 + xinit;
		float y = 0 + yinit;
		String codigo = "";
		codigo = codBarras(datoCodBarras);
		float h = 25;
		float w = (float) 0.72;
		String c = "";
		pcb.setLineWidth(w);

		for (int ixd = 0; ixd < codigo.length(); ixd++) {
			c = codigo.substring(ixd, ixd + 1);
			if (c.equals("|")) {
				linea(x, y, x, y + h, pcb);
			}
			x = x + w;
		}
		if (pdfwrite != null) {
			docu.close();
			pdfwrite.close();
		}
		return;
	}

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
		int[] patronesB = new int[] { 16, 17, 18, 19, 20, 21, 22, 23, 24, 25 };
		int[] patronesC = new int[] { 100, 101, 102, 103, 104, 105, 106 };

		String quiet = "          ";
		int check = (patronesC[5]) + (patronesC[2]);

		String codigo = quiet + patrones[patronesC[5]] + patrones[patronesC[2]];

		int i = 0, j = 2, l = (dato.length() / 2) * 2, num;

		while (i < l) {
			if (Pattern.matches("[0-9]+", dato.substring(i, i + 2)))
				num = Integer.valueOf(dato.substring(i, i + 2), 10);
			else
				num = 0;
			check += j * num;
			codigo += patrones[num];
			i += 2;
			j += 1;
		}

		if (dato.length() > l) {
			if (Pattern.matches("[0-9]+", dato.substring(i, dato.length())))
				num = Integer.valueOf(dato.substring(l, dato.length()));
			else
				num = 0;
			check += j * patronesC[0];
			check += (j + 1) * patronesB[num];
			codigo += patrones[patronesC[0]] + patrones[patronesB[num]];
		}

		check = check % 103;
		codigo += patrones[check] + patrones[patronesC[6]] + quiet;

		return codigo;
	}
	
	protected static BaseFont setFont(String fuente, int s, PdfContentByte pcb) {
		try {
			BaseFont bf = BaseFont.createFont(fuente, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			pcb.setFontAndSize(bf, (float) s);
			return bf;
		} catch (Exception e) {
			com.stpa.ws.server.util.Logger.error("Error al aplicar los estilos de las fuentes: " + e.getMessage(),e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}
		return null;
	}
	
	protected static void linea(float x1, float y1, float x2, float y2, PdfContentByte pcb) {
		pcb.moveTo(x1, y1);
		pcb.lineTo(x2, y2);
		pcb.stroke();
	}
}
