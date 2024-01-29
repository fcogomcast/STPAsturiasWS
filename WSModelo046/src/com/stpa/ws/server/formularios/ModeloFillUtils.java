package com.stpa.ws.server.formularios;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.stpa.ws.pref.m42.Preferencias;
import com.stpa.ws.server.util.WebServicesUtil;
import com.stpa.ws.server.util.XMLUtils;

public class ModeloFillUtils extends FormFillBase {
	
	/**
	 * Determina si el numero de modelo es valido o esta implementado. Usar para lanzar errores de validacion antes de
	 * crear.
	 * 
	 * @param modelo
	 * @return
	 */
	public static boolean esModeloValido(String modelo) {
		String[] lstModelos = new String[] { "001", "002", "007", "008", "009", "043", "044", "046", "048", "049",
				"600", "620", "650", "651", "045", "047", "112", "111", "501", "653", "652", "662", "080", "661", "660"
		};
		for (int i = 0; i < lstModelos.length; i++) {
			if (modelo.equals(lstModelos[i])) {
				return true;
			}
		}
		return false;
	}


	@SuppressWarnings("unchecked")
	public static boolean escribePDF(String XMLIn, String modelo, URL pdfEntrada, OutputStream pdfSalida, String copia) {
		PdfStamper stamp = null;
		Preferencias pref = new Preferencias();
		try {
			
			com.stpa.ws.server.util.Logger.debug("XMLIn: " + XMLIn,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			// leo la plantilla
			PdfReader reader = null;
			if (pdfEntrada == null) {// pathPlantillas
				String uristr = pref.getM_apppath() + pref.getM_pdfmodelo046() + copia + ".pdf";
				
				if ((uristr.contains("http")) || (uristr.contains("HTTP")) || (uristr.contains("https"))
						|| (uristr.contains("HTTPS")))
					reader = new PdfReader(new URL(uristr.replaceAll(" ", "%20")));
				else
					reader = new PdfReader(uristr);	
			} else {
				pdfEntrada.openStream();
				reader = new PdfReader(pdfEntrada);
			}
			//  hay que buscar el capo siga.gestor en el xml (nombre?)
			if (modelo.equals("620")) {
				// El formulario ahora tiene una primera pagina sin campo "Ejemplar para", si en su lugar tuviera el
				// formulario de ayuda usariamos
					reader.selectPages("2-");
			}
			if (pdfSalida == null)//Solo para pruebas
				stamp = new PdfStamper(reader, new FileOutputStream("/home/oracle/domains/Pruebas_domain/WSModelo046/logs/prueba.pdf"));
			else
				stamp = new PdfStamper(reader, pdfSalida);
			
			// relleno los campos
			AcroFields form1 = stamp.getAcroFields();

			// numerodeserie2 es un campo OBLIGATORIO, determina la posicion del codigo de barras
			float[] posCodBarras = form1.getFieldPositions("numerodeserie2");
			if (posCodBarras == null)
				posCodBarras = new float[0];
			Object[] nodos = XMLUtils.getNodes(XMLIn);
			//com.stpa.ws.server.util.Logger.debug("nodos: " + ReflectionToStringBuilder.toString(nodos),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			String numerodeserie = "";
			String emisor = "";
			//com.stpa.ws.server.util.Logger.debug("nodos.length: " + nodos.length,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			if (nodos != null && nodos.length != 0) {
				HashMap<String, Object> maptext = (HashMap<String, Object>) nodos[1];
				HashMap<String, Object> mapatr = (HashMap<String, Object>) nodos[0];
				if (maptext.get("numerodeserie") != null){
					List<Object> lista = (List<Object>) maptext.get("numerodeserie");
					for(int s=0;s<lista.size();s++){
						if(StringUtils.isNotBlank((String)lista.get(s)))
							numerodeserie = (String)lista.get(s);
					}
					
//					com.stpa.ws.server.util.Logger.debug("Paso 1",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
//					numerodeserie = ((List<Object>) maptext.get("numerodeserie")).get(0).toString();
//					if(StringUtils.isBlank(numerodeserie) && ((List<Object>) maptext.get("numerodeserie")).size()>1){
//						com.stpa.ws.server.util.Logger.debug("Paso 2",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
//						numerodeserie = ((List<Object>) maptext.get("numerodeserie")).get(1).toString();
//						if(StringUtils.isBlank(numerodeserie) && ((List<Object>) maptext.get("numerodeserie")).size()>2){
//							com.stpa.ws.server.util.Logger.debug("Paso 3",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
//							numerodeserie = ((List<Object>) maptext.get("numerodeserie")).get(2).toString();
//						}
//					}
				}
				if (maptext.get("emisor") != null){
					com.stpa.ws.server.util.Logger.debug("(String)lista.get(s) - emisor1: " + ((List<Object>) maptext.get("nif")).get(0).toString(),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
					//					emisor = ((List<Object>) maptext.get("nif")).get(0).toString();
					List<Object> lista = (List<Object>) maptext.get("emisora");
					for(int s=0;s<lista.size();s++){
						if(StringUtils.isNotBlank((String)lista.get(s))){
							com.stpa.ws.server.util.Logger.debug("(String)lista.get(s) - emisor2: " + (String)lista.get(s),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
							emisor = (String)lista.get(s);
						}
					}
				}

				//com.stpa.ws.server.util.Logger.debug("numerodeserie: " + numerodeserie,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				//com.stpa.ws.server.util.Logger.debug("emisor: " + emisor,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				
				//recupera los campos fijos y copia los valores del XML
				Object[] nodenames = getCamposFijos(modelo);
				String[] nodespdf = (String[]) nodenames[0];
				String[] nodesxml = (String[]) nodenames[1];
				
				//com.stpa.ws.server.util.Logger.debug("nodespdf: " + ReflectionToStringBuilder.toString(nodespdf),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				//com.stpa.ws.server.util.Logger.debug("nodesxml: " + ReflectionToStringBuilder.toString(nodesxml),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				
				// objLog.debug("CAMPOS FIJOS:" + nodesxml.length);
				for (int i = 0; i < nodesxml.length; i++) {
					List<Object> mapValor = (List<Object>) maptext.get(nodesxml[i]);
					
					if (mapValor != null && !mapValor.isEmpty() && i < nodespdf.length) {
						//com.stpa.ws.server.util.Logger.debug("form1.setField(nodespdf["+i+"],mapValor.get(0)): (" + nodespdf[i]+","+(String) mapValor.get(0)+")",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
						String valor = "";
						for(int s=0;s<mapValor.size();s++){
							if(StringUtils.isNotBlank((String) mapValor.get(s))){
								String aux = ((String) mapValor.get(s));
								if(StringUtils.isNotBlank(aux))
									aux = aux.toUpperCase();
								valor = aux;
							}
						}
						form1.setField(nodespdf[i], valor);
					}
				}

				//recupera los campos fijos y formatea los numeros del XML
				nodenames = getCamposNumericosFijos(modelo);
				nodespdf = (String[]) nodenames[0];
				nodesxml = (String[]) nodenames[1];
				
				//com.stpa.ws.server.util.Logger.debug("nodespdf 2: " + ReflectionToStringBuilder.toString(nodespdf),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				//com.stpa.ws.server.util.Logger.debug("nodesxml 2: " + ReflectionToStringBuilder.toString(nodesxml),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				
				for (int i = 0; i < nodesxml.length; i++) {
					List<Object> mapValor = (List<Object>) maptext.get(nodesxml[i]);
					if (mapValor != null && !mapValor.isEmpty() && i < nodespdf.length) {
						String valor = "";
						for(int s=0;s<mapValor.size();s++){
							if(StringUtils.isNotBlank((String) mapValor.get(s))){
								String mapValores = formateaImporte((String) mapValor.get(s));
								if(StringUtils.isNotBlank(mapValores))
									mapValores = mapValores.toUpperCase();
								valor = mapValores;
							}
						}
						//com.stpa.ws.server.util.Logger.debug("2 - form1.setField(nodespdf["+i+"],mapValor.get(0)): (" + nodespdf[i]+","+(String) valor+")",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
						form1.setField(nodespdf[i], valor);
						//form1.setField(nodespdf[i], (String) mapValores);
					}
				}

				//recupera los campos en listas o formateables del XML
				//nodenames = getCamposCond(modelo, maptext, mapatr, (Document) XMLUtils.compilaXMLObject(XMLIn, null));
				nodenames = getCamposCond(modelo, maptext, mapatr, (Document) XMLUtils.getStringXmlAsDocument(XMLIn));
				nodespdf = (String[]) nodenames[1];
				nodesxml = (String[]) nodenames[0];
				
				//com.stpa.ws.server.util.Logger.debug("nodespdf 3: " + ReflectionToStringBuilder.toString(nodespdf),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				//com.stpa.ws.server.util.Logger.debug("nodesxml 3: " + ReflectionToStringBuilder.toString(nodesxml),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				
				for (int i = 0; i < nodesxml.length; i++) {
					if (i < nodespdf.length){
						//com.stpa.ws.server.util.Logger.debug("2 - form1.setField(nodespdf["+i+"],nodesxml["+i+"]): (" + nodespdf[i]+","+nodesxml[i]+")",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
						//com.stpa.ws.server.util.Logger.debug("i: " + i + " - (nodespdf[i],nodesxml[i]): (" + nodespdf[i] + "," + nodesxml[i] + ")",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
						String aux = nodesxml[i];
						if(StringUtils.isNotBlank(aux))
							aux = aux.toUpperCase();
						form1.setField(nodespdf[i], nodesxml[i]);
					}
				}
				//TOT_V
				//com.stpa.ws.server.util.Logger.debug("TOT_V 1:" + ReflectionToStringBuilder.toString(maptext.get("TOT_V")),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				//com.stpa.ws.server.util.Logger.debug("TOT_V 1:" + maptext.get("TOT_V").getClass().getName(),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				ArrayList list = (ArrayList)maptext.get("TOT_V");
				String tot_v = "";
				if(list!=null && list.size()>0)
					tot_v = (String)list.get(0);
				//com.stpa.ws.server.util.Logger.debug("TOT_V 2:" + tot_v,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				form1.setField("TOT_V",tot_v);
				
				//Listado liquidación
				ArrayList d01 = ((ArrayList)maptext.get("d01_1"));
				//com.stpa.ws.server.util.Logger.debug("d01=" + d01,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				ArrayList d02 = ((ArrayList)maptext.get("d02_1"));
				//com.stpa.ws.server.util.Logger.debug("d02=" + d02,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				ArrayList d03 = ((ArrayList)maptext.get("d03_1"));
				//com.stpa.ws.server.util.Logger.debug("d03=" + d03,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				ArrayList d04 = ((ArrayList)maptext.get("d04_1"));
				//com.stpa.ws.server.util.Logger.debug("d04=" + d04,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				
				for(int s=0;s<d01.size();s++){
					String d1 = "";
					String d2 = "";
					String d3 = "";
					String d4 = "";
					if(StringUtils.isNotBlank((String)d01.get(s)))
						d1 = (String)d01.get(s);
					if(s<d02.size() && StringUtils.isNotBlank((String)d02.get(s)))
						d2 = (String)d02.get(s);
					if(s<d03.size() && StringUtils.isNotBlank((String)d03.get(s)))
						d3 = (String)d03.get(s);
					if(s<d04.size() && StringUtils.isNotBlank((String)d04.get(s)))
						d4 = (String)d04.get(s);
					
					form1.setField("d01_"+(s+1),d1);
					form1.setField("d02_"+(s+1),d2);
					form1.setField("d03_"+(s+1),d3);
					form1.setField("d04_"+(s+1),d4);
				}				
			}
			
			String aa = emisor;
			aa = XMLUtils.borraEspacios(aa);
			
			//Metemos emisora
			//aa = "331004";
			//Emisora se recupera de la base de datos.
			aa=WebServicesUtil.obtenerEmisora();
			
			
			
			String bb = numerodeserie;
			bb = XMLUtils.borraEspacios(bb);
			stamp.setFormFlattening(true);
			for (int h = 0; h < posCodBarras.length; h += 5) {
				float xinit = posCodBarras[h + 1];
				float yinit = posCodBarras[h + 2];
				crearCodigoBarras(aa, bb, "c:\\pdf\\", stamp.getOverContent((int) posCodBarras[h]), xinit, yinit);
				stamp.getOverContent(1).stroke();
			}
			reader.close();
			stamp.close();
			return true;
		} catch (Exception e) {
			com.stpa.ws.server.util.Logger.error("FormFillUtils.escribePDF", e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			try {
				stamp.close();
			} catch (Throwable t) {
				com.stpa.ws.server.util.Logger.error("Error al cerrar el stamper", t,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			}
		}
		return false;

	}

	public static void crearModelo(String modelo, HashMap<String, String> sesionparams, HashMap<String, String> params) {

	}

	/**
	 * Devuelve los campos fijos del modelo de PDF y los campos XML correspondientes
	 * @param modelo
	 * @return
	 */
	public static Object[] getCamposFijos(String modelo) {
		Object[] lstCampos = new Object[] { new String[0], new String[0] };
		lstCampos = rellenar046();
		
		return new Object[] { lstCampos[0], lstCampos[1] };
	}

	/**
	 * Devuelve los campos numericos fijos del PDF para ser formateados y los campos XML correspondientes
	 * @param modelo
	 * @return
	 */
	public static Object[] getCamposNumericosFijos(String modelo) {
		Object[] lstCampos = new Object[] { new String[0], new String[0], new String[0], new String[0] };
		lstCampos = rellenar046();

		return new Object[] { lstCampos[2], lstCampos[3] };
	}

	/**
	 * Devuelve listas de campos en el PDF y de valores a rellenar, no los campos a consultar. Las listas de
	 * transmitentes/bienes... deberian ser implementadas aqui.
	 * 
	 * @see getCamposFijos()
	 * 
	 * @param modelo
	 * @param params
	 * @param params2
	 * @return
	 */
	public static Object[] getCamposCond(String modelo, HashMap<String, Object> params,
			HashMap<String, Object> params2, Document doc) {

		com.stpa.ws.server.util.Logger.debug("doc: " + ReflectionToStringBuilder.toString(doc),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		if (modelo.equals("046")) {
			int id = 1;
			List<String> listnodespdf = new ArrayList<String>();
			List<String> listnodesxml = new ArrayList<String>();
			Node nodo1 = null;
			while ((nodo1 = XMLUtils.obtenerNodo(doc, "datos/modelo/DetalleLiquidacion", "" + id)) != null) {
				listnodespdf.add("d01_" + id);
				com.stpa.ws.server.util.Logger.debug("id: " + id,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				com.stpa.ws.server.util.Logger.debug("XMLUtils.valorNodo(nodo1, 'd01')" + XMLUtils.valorNodo(nodo1, "d01_" + id),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				listnodesxml.add(XMLUtils.valorNodo(nodo1, "d01_" + id));
				listnodespdf.add("d02_" + id);
				com.stpa.ws.server.util.Logger.debug("XMLUtils.valorNodo(nodo1, 'd02')" + XMLUtils.valorNodo(nodo1, "d02_" + id),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				listnodesxml.add(XMLUtils.valorNodo(nodo1, "d02_" + id));
				listnodespdf.add("d03_" + id);
				com.stpa.ws.server.util.Logger.debug("XMLUtils.valorNodo(nodo1, 'd03')" + XMLUtils.valorNodo(nodo1, "d03_" + id),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				listnodesxml.add(XMLUtils.valorNodo(nodo1, "d03_" + id));
				listnodespdf.add("d04_" + id);
				com.stpa.ws.server.util.Logger.debug("XMLUtils.valorNodo(nodo1, 'd04')" + XMLUtils.valorNodo(nodo1, "d04_" + id),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				listnodesxml.add(XMLUtils.valorNodo(nodo1, "d04_" + id));
				id++;
			}

			String[] nodesxml = new String[listnodesxml.size()];
			String[] nodespdf = new String[listnodespdf.size()];
			for (int i = 0; i < nodespdf.length; i++){
				nodespdf[i] = (String) listnodespdf.get(i);
				}
			for (int i = 0; i < nodesxml.length; i++){
				nodesxml[i] = (String) listnodesxml.get(i);
			}
			return new Object[] { nodesxml, nodespdf };
		}

		return new Object[] { new String[0], new String[0] };
	}

	private static Object[] rellenar046() {
		String[] camposPdf = new String[] { "numerodeserie","c01d1", "c01d2", "c01m1", "c01m2", "c01a1", "c01a2", "c01a3", "c01a4",
				"c02a", "c02b", "c02c", "c02d", "c03a", "c03b", "c03c", "c03d", "c03e", "c03f", "c03g", "c03h", "c03i",
				"c03j", "c04a1", "c04a2", "c04a3", "c04a4", "c05a", "c05b", /* sp */"c06", "c07", "c08", "c09", "c10",
				"c11", "c12", "c13", "c14", "c15", "c16", "c17", "c18", "TOT_I", };
		String[] camposPdfNum = new String[] { "TOT_V" };
		String[] camposXml = new String[] { "numerodeserie","c01d1", "c01d2", "c01m1", "c01m2", "c01a1", "c01a2", "c01a3", "c01a4",
				"c02a", "c02b", "c02c", "c02d", "c03a", "c03b", "c03c", "c03d", "c03e", "c03f", "c03g", "c03h", "c03i",
				"c03j", "c04a1", "c04a2", "c04a3", "c04a4", "c05a", "c05b", /* sp */"c06", "c07", "c08", "c09", "c10",
				"c11", "c12", "c13", "descripcionMunicipio", "descripcionProvincia", "c16", "c17", "c18", "TOT_I" };
		String[] camposXmlNum = new String[] { "TOT_V" };

		List<String> pdf = new ArrayList<String>(Arrays.asList(camposPdf));
		pdf.addAll(FormBean046.llenarMapPDF().get(BaseElementsForm.PDF));


		List<String> xml = new ArrayList<String>(Arrays.asList(camposXml));
		xml.addAll(FormBean046.llenarMapPDF().get(BaseElementsForm.MAP));


		String cadenaMapConcat = "";
		String cadenaPdfConct = "";

		for (String string : xml) {
			cadenaMapConcat += " " + string;
		}
		for (String string : pdf) {
			cadenaPdfConct += " " + string;
		}

		return new Object[] { pdf.toArray(new String[] {}), xml.toArray(new String[] {}), camposPdfNum, camposXmlNum };
	}
}