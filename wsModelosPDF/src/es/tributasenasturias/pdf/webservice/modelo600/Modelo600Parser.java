package es.tributasenasturias.pdf.webservice.modelo600;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import es.tributasenasturias.pdf.utils.Logger;
import es.tributasenasturias.pdf.utils.Preferencias;
import es.tributasenasturias.pdf.utils.SelectorEmisora;
import es.tributasenasturias.pdf.utils.XMLUtils;

public class Modelo600Parser {

	private Document xmlDoc;

	//CRUBENCVS 28/02/2022. Se extrae la emisora como propiedad 
	String emisora;
	
	private static String INTERVINIENTE="interviniente";
	private static String DECLARACION="declaracion";
	private static String TIPO="tipo";
	private String NUMERO_AUTOLIQUIDACION="";
	private static String NUMERO_AUTOLIQUIDACION_NOTARIO="codigo";
	private static String NUMERO_AUTOLIQUIDACION_GENERICO="codigo_declaracion";
	private static String SUJETO_PASIVO="Sujeto Pasivo";
	private static String TRANSMITENTE="Transmitente";
	private String PRESENTADOR="";
	private static String PRESENTADOR_NOTARIO="presentador";
	private static String PRESENTADOR_GENERICO="Presentador";
	private String LIQUIDACION="";
	private static String LIQUIDACION_NOTARIO="Liquidacion";
	private static String LIQUIDACION_GENERICO="liquidacion";
	private String IDENTIFICACION="";
	private static String IDENTIFICACION_NOTARIO="Identificacion";
	private static String IDENTIFICACION_GENERICO="identificacion";
	private String BIEN_URBANO="";
	private static String BIEN_URBANO_NOTARIO="bien_urbano";
	private static String BIEN_URBANO_GENERICO="bienUrbano";
	private String BIEN_RUSTICO="";
	private static String BIEN_RUSTICO_NOTARIO="bien_rustico";
	private static String BIEN_RUSTICO_GENERICO="bienRustico";
	private String CUOTA_INTEGRA_AJUSTADA="";
	private static String CUOTA_INTEGRA_AJUSTADA_NOTARIO="CuotaIntAjustada";
	private static String CUOTA_INTEGRA_AJUSTADA_GENERICO="cuotaIntAjustada";

	//Indica si tratamos el XML de Notarios, o el de pago genérico.
	boolean modelo600Notarios=false;

	// Objeto para manejar árbol DOM.
	javax.xml.parsers.DocumentBuilderFactory fact;
	javax.xml.parsers.DocumentBuilder db;

	// Objetos
	private Modelo600SujetoPasivo [] aModelo600SP;
	private Modelo600Transmitente [] aModelo600TR;
	private Modelo600Presentador [] aModelo600PR;
	private Modelo600Liquidacion modelo600Liquidacion;
	private Modelo600Identificacion modelo600Identificacion;
	private Modelo600BienUrbano [] aModelo600BienUrbano;
	private Modelo600BienRustico [] aModelo600BienRustico; 

	public Modelo600Parser() {}

	public Modelo600Parser(String doc) {
		try {
			SelectorEmisora x=new SelectorEmisora();
			this.emisora=x.getEmisoraDeModelo("600");
		} catch(Exception e)
		{
			Logger.error("Error al inicializar el objeto parser de Modelo 600--> recuperación de la emisora del modelo 600: "+e.getMessage());
			this.xmlDoc =null;
			return;
		}
		
		try {
			// Inicializamos  los objetos que nos servirán para parsear los resultados.
			fact = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			fact.setNamespaceAware(true);
			db= fact.newDocumentBuilder(); // Lanza javax.xml.parsers.ParserConfigurationException

			org.xml.sax.InputSource inStr = new org.xml.sax.InputSource (); 
			inStr.setCharacterStream(new java.io.StringReader(doc.toString()));// Ya es un string, pero para prevenir futuros cambios.
			try{
				this.xmlDoc = db.parse(inStr);
				if (xmlDoc.getFirstChild().getNamespaceURI()==null || 
					!xmlDoc.getFirstChild().getNamespaceURI().equalsIgnoreCase("http://servicios.tributasenasturias.es/Pago600Generico")){
					modelo600Notarios=true;
				}
				if (modelo600Notarios){
					NUMERO_AUTOLIQUIDACION=NUMERO_AUTOLIQUIDACION_NOTARIO;
					CUOTA_INTEGRA_AJUSTADA=CUOTA_INTEGRA_AJUSTADA_NOTARIO;
					PRESENTADOR=PRESENTADOR_NOTARIO;
					LIQUIDACION=LIQUIDACION_NOTARIO;
					IDENTIFICACION=IDENTIFICACION_NOTARIO;
					BIEN_URBANO=BIEN_URBANO_NOTARIO;
					BIEN_RUSTICO=BIEN_RUSTICO_NOTARIO;
				}else{
					NUMERO_AUTOLIQUIDACION=NUMERO_AUTOLIQUIDACION_GENERICO;
					CUOTA_INTEGRA_AJUSTADA=CUOTA_INTEGRA_AJUSTADA_GENERICO;
					PRESENTADOR=PRESENTADOR_GENERICO;
					LIQUIDACION=LIQUIDACION_GENERICO;
					IDENTIFICACION=IDENTIFICACION_GENERICO;
					BIEN_URBANO=BIEN_URBANO_GENERICO;
					BIEN_RUSTICO=BIEN_RUSTICO_GENERICO;
				}
			}catch (java.io.IOException ex){
				Logger.error("Error al parsear el xml de entrada (IOException): "+ex.getMessage());
				this.xmlDoc =null;
			}catch (org.xml.sax.SAXException ex){
				Logger.error("Error al parsear el xml de entrada (SAXException): "+ex.getMessage());
				this.xmlDoc=null;
			}
		}catch (ParserConfigurationException pce) {
			Logger.error("Error al parsear el xml de entrada (ParserConfigurationException): "+pce.getMessage());
		}
	}

	public String getModeloPDF(Preferencias prefs){
		try {
			prefs.CargarPreferencias();

			PdfReader reader;
			PdfStamper stamper;

			reader = new PdfReader(prefs.getRutaPlantillas()+"//"+prefs.getPlatillaModelo600());
			ByteArrayOutputStream oSalida = new ByteArrayOutputStream();
			stamper = new PdfStamper(reader, oSalida);
			stamper.setFullCompression();

			AcroFields form = stamper.getAcroFields();

			// Obtener el número de serie (número de autoliquidacion)
			form.setField("numerodeserie", this.getNumAutoliquidacion());

			// Se introduce el código de barras en la primera hoja
			setCodigoBarras(stamper, 1, this.getNumAutoliquidacion(), true);
			setCodigoBarras(stamper, 5, this.getNumAutoliquidacion(), true);
			setCodigoBarras(stamper, 9, this.getNumAutoliquidacion(), true);
			
			//CRUBENCVS  28/02/2022
			//Se recuperan todos los intervinientes de una sola vez
			recuperaTransmitentes();
			recuperaPresentadores();
			recuperaSujetosPasivos();
			//Lo mismo para los bienes
			recuperaBienesUrbanos();
			recuperaBienesRustica();
			int numSp = aModelo600SP.length;
			form.setField("12", String.valueOf(numSp));
			// Mostramos los datos del primer sujeto pasivo
			if (numSp > 0) {
				Modelo600SujetoPasivo sp = aModelo600SP[0];
				form.setField("1", sp.getNif());	
				form.setField("2", sp.getApellidosNombre());							
				form.setField("3", sp.getTelefono()==null?"":sp.getTelefono());				
				form.setField("4a", sp.getSiglas());
				form.setField("4", sp.getNombreVia());
				form.setField("5", sp.getNumero());
				form.setField("6", sp.getEscalera());
				form.setField("7", sp.getPiso());
				form.setField("8", sp.getPuerta());
				form.setField("9", sp.getMunicipio());
				form.setField("10", sp.getProvincia());
				form.setField("11", sp.getCodigoPostal());
			}
			
			int numTr = aModelo600TR.length;
			form.setField("24", String.valueOf(numTr));
			// Mostramos los datos del primer transmitente
			if (numTr > 0) {
				
				Modelo600Transmitente tr= aModelo600TR[0];
				form.setField("13", tr.getNif());
				form.setField("14", tr.getApellidosNombre());					
				form.setField("15", tr.getTelefono()==null?"":tr.getTelefono());				
				form.setField("16a", tr.getSiglas());
				form.setField("16", tr.getNombreVia());
				form.setField("17", tr.getNumero());
				form.setField("18", tr.getEscalera());
				form.setField("19", tr.getPiso());
				form.setField("20", tr.getPuerta());
				form.setField("21", tr.getMunicipio());
				form.setField("22", tr.getProvincia());
				form.setField("23", tr.getCodigoPostal());
			}
			
			int numPr = aModelo600PR.length;
			// Mostramos los datos del presentador
			if (numPr > 0) {
				Modelo600Presentador pr= aModelo600PR[0];
				form.setField("25", pr.getNif());
				form.setField("26", pr.getApellidosNombre());
				form.setField("28a", pr.getSiglas());
				form.setField("28", pr.getNombreVia());
				form.setField("29", pr.getNumero());
				form.setField("30", pr.getEscalera());
				form.setField("31", pr.getPiso());
				form.setField("32", pr.getPuerta());
				form.setField("33", pr.getMunicipio());
				form.setField("34", pr.getProvincia());
				form.setField("35", pr.getCodigoPostal());
			}
			Modelo600Identificacion id= this.getIdentificacion();
			// Introducimos los datos de la identificación del acto.
			form.setField("36", id.getTipoDocumento());
			form.setField("41", id.getExpresionAbreviada());
			form.setField("42", id.getNotario());
			form.setField("43", id.getProtocolo());
			form.setField("43c", id.getProtocoloBis());
			form.setField("44", id.getConcepto());
			form.setField("45", id.getMunicipioNotario());
			form.setField("46", id.getProvinciaNotario());
			form.setField("46bis", id.getIdentificacionDelBien());
			form.setField("fecha_devengo",id.getFechaDevengo());

						
			form.setField("fecha_presentacion", getFechaActual());
			try{
				String datoEspecifico=id.getDatoEspecifico();
				form.setField("de1", datoEspecifico.substring(0,1));
				form.setField("de2", datoEspecifico.substring(1,2));
			}catch (StringIndexOutOfBoundsException e){
				//No debería ocurrir, a menos que la expresión abreviada tenga menos de dos caracteres.
				 //Forma más lenta de conseguir lo mismo.
				String datoEspecifico=id.getDatoEspecifico();
				try{form.setField("de1", datoEspecifico.substring(0,1));}catch (StringIndexOutOfBoundsException ex){}
				try{form.setField("de2", datoEspecifico.substring(1,2));}catch (StringIndexOutOfBoundsException ex){}
			}
			Modelo600Liquidacion liq= this.getLiquidacion();
			// Introducimos los datos de la liquidacion
			form.setField("47", liq.getValorDeclarado());
			form.setField("56", liq.getBaseImponible());
			form.setField("57", liq.getPorcentajeReduccion());
			form.setField("58", liq.getReduccion());
			form.setField("59", liq.getBaseLiquidable());
			form.setField("60", liq.getTipoImpositivo());

			if (XMLUtils.selectSingleNode(liq.getNodeLiquidacion(),CUOTA_INTEGRA_AJUSTADA,0)!=null){
				form.setField("61",liq.getCuota());
				form.setField("61_R", liq.getReducExcesoCuota());
				form.setField("61_A", liq.getCuotaIntAjustada());
			}else{
				//Si devuelve "", consideramos que no hay valor.
				double cuotaIntegra = (Double.parseDouble(liq.getBaseLiquidable().replaceAll("[^0-9]", ""))/100) * 
						(Double.parseDouble(liq.getTipoImpositivo().replaceAll("[^0-9]",""))/100)/100; //El tipo viene como ZX00
				double reduccExceso = cuotaIntegra - (Double.parseDouble(liq.getCuota().replaceAll("[^0-9]",""))/100);
				
				//Incluyo los símbolos de miles y decimales como los otros.
				//Se usará para formatear los números en formulario.
				//No utilizo el de "locale" porque depende de varias cosas, como lenguajes instalados y
				//lenguaje por defecto. Prefiero cambiarlos yo, porque esta es una solución temporal.
				java.text.DecimalFormatSymbols symb = new java.text.DecimalFormatSymbols();
				symb.setGroupingSeparator('.'); //Separador para los miles
				symb.setDecimalSeparator(','); // Separador de decimales.
				//Formateador
				java.text.DecimalFormat fmt = new java.text.DecimalFormat("###,###,###,##0.00");
				fmt.setDecimalFormatSymbols(symb);
				form.setField("61",fmt.format(cuotaIntegra));
				form.setField("61_R", fmt.format(reduccExceso));
				form.setField("61_A", this.getLiquidacion().getCuota());
			}

			form.setField("62", liq.getPorcentajeBonificacionCuota());
			form.setField("63", liq.getBonificacionCuota());
			form.setField("64", liq.getAIngresar());
			form.setField("65", liq.getRecargo());
			form.setField("66", liq.getInteresesDemora());
			form.setField("67", liq.getImporteIngresadoLiquidacionAnterior());
			form.setField("68", liq.getTotalAIngresar());			
			form.setField("48", liq.getTipoLiquidacion());
			form.setField("52", liq.getFundamento());
			form.setField("53", liq.getAutoliquidacionComplementaria());

			if (liq.getAutoliquidacionComplementaria().equalsIgnoreCase("X")) {
				// Fecha de presentacion liquidacion anterior				
				form.setField("fecha_anterior", liq.getFechaPresentacionLiquidacionAnterior());
				form.setField("55", liq.getNumeroPrimeraAutoliquidacion());
			}

			// Se introduce el código de barras en la segunda hoja
			setCodigoBarras(stamper, 2, this.getNumAutoliquidacion(), false);
			setCodigoBarras(stamper, 6, this.getNumAutoliquidacion(), false);

			// Ahora se introducen el resto de sujetos pasivos.
			for (int i=1; i<numSp; i++) {
				Modelo600SujetoPasivo sp = aModelo600SP[i];
				form.setField("1_"+i, sp.getNif());
				form.setField("2_"+i, sp.getApellidosNombre());							
				form.setField("2_"+i+"a", sp.getTelefono()==null?"":sp.getTelefono());				
				form.setField("3_"+i, sp.getSiglas());
				form.setField("4_"+i, sp.getNombreVia());
				form.setField("5_"+i, sp.getNumero());
				form.setField("6_"+i, sp.getEscalera());
				form.setField("7_"+i, sp.getPiso());
				form.setField("8_"+i, sp.getPuerta());
				form.setField("9_"+i, sp.getMunicipio());
				form.setField("10_"+i, sp.getProvincia());
				form.setField("11_"+i, sp.getCodigoPostal());
				form.setField("12_"+i, sp.getCoeficienteParticipacion());
			}

			// Se introduce el código de barras en la tercera hoja
			setCodigoBarras(stamper, 3, this.getNumAutoliquidacion(), false);
			setCodigoBarras(stamper, 7, this.getNumAutoliquidacion(), false);
			// Ahora se introducen los transmitentes
			for (int i=1; i<numTr; i++) {
				Modelo600Transmitente tr= aModelo600TR[i];
				form.setField("13_"+i, tr.getNif());
				form.setField("14_"+i, tr.getApellidosNombre());							
				form.setField("14_"+i+"a", tr.getTelefono()==null?"":tr.getTelefono());				
				form.setField("15_"+i, tr.getSiglas());
				form.setField("16_"+i, tr.getNombreVia());
				form.setField("17_"+i, tr.getNumero());
				form.setField("18_"+i, tr.getEscalera());
				form.setField("19_"+i, tr.getPiso());
				form.setField("20_"+i, tr.getPuerta());
				form.setField("21_"+i, tr.getMunicipio());
				form.setField("22_"+i, tr.getProvincia());
				form.setField("23_"+i, tr.getCodigoPostal());
				form.setField("24_"+i, tr.getCoeficienteParticipacion());
			}
			// Se introduce el código de barras en la cuarta hoja
			setCodigoBarras(stamper, 4, this.getNumAutoliquidacion(), false);
			setCodigoBarras(stamper, 8, this.getNumAutoliquidacion(), false);

			// Introducimos ahora los bienes urbanos.
			int numBU = aModelo600BienUrbano.length;
			int cont = 1;
			for (int i=0; i<numBU; i++) {
				Modelo600BienUrbano ur= this.getBienUrbano(i);
				form.setField("144_"+(cont), ur.getSiglas());
				form.setField("133_"+(cont), ur.getNombreVia());
				form.setField("145_"+(cont), ur.getNumero());
				form.setField("156_"+(cont), ur.getEscalera());
				form.setField("147_"+(cont), ur.getPiso());
				form.setField("148_"+(cont), ur.getPuerta());
				form.setField("135_"+(cont), ur.getMunicipio());
				form.setField("134_"+(cont), ur.getProvincia());
				form.setField("136_"+(cont), ur.getCodigoPostal());
				form.setField("143_"+(cont), ur.getValor());
				form.setField("138_"+(cont), ur.getClaveAdquisicion());				
				form.setField("139_"+(cont), ur.getReferenciaCatastral()==null?"":ur.getReferenciaCatastral());				
				form.setField("149_"+(cont), ur.getFechaAdquisicion()==null?"":ur.getFechaAdquisicion());			
				form.setField("140_"+(cont), ur.getClaseBien());
				form.setField("141_"+(cont), ur.getAnyoConstruccion());
				form.setField("142_"+(cont), ur.getVPO());
				form.setField("137_"+(cont), ur.getPorcentajeTitularidad());
				cont++;
			}

			// Introducimos ahora los bienes rusticos.
			int numBR = aModelo600BienRustico.length;
			cont = 1;
			for (int i=0; i<numBR; i++) {
				Modelo600BienRustico ru = this.getBienRustico(i);
				form.setField("210_"+(cont), ru.getMunicipio());
				form.setField("211_"+(cont), ru.getProvincia());
				form.setField("212_"+(cont), ru.getCultivo());
				form.setField("213_"+(cont), ru.getPorcentajeTitularidad());
				form.setField("214_"+(cont), ru.getClaveAdquisicion());
				form.setField("215_"+(cont), ru.getReferenciaCatastral());
				form.setField("216_"+(cont), ru.getPoligono());
				form.setField("217_"+(cont), ru.getParcela());
				form.setField("220_"+(cont), ru.getValor());
				form.setField("218_"+(cont), ru.getSituacion());
				form.setField("219_"+(cont), ru.getSuperficieM2());
				form.setField("221_"+(cont), ru.getParroquia());
				cont++;
			}
			stamper.setFormFlattening(true);
			stamper.close();
			reader.close();
			char[] c = es.tributasenasturias.pdf.utils.Base64.encode(oSalida.toByteArray());							
			return new String(c);
		} catch (IOException e) {
			Logger.error(e.getMessage());
			return e.getMessage();
		} catch (DocumentException e) {
			Logger.error(e.getMessage());
			return e.getMessage();
		} catch (Exception e) {
			Logger.error(e.getMessage());
			return e.getMessage();
		}
	}

	private void setCodigoBarras(PdfStamper oStamper, Integer numeroPagina, String numeroAutoliquidacion, boolean esPrimeraHoja) {
		try {
			Barcode128 code128 = new Barcode128();
			//CRUBENCVS  28/02/2022. La emisora ya no se recupera en cada llamada
		    code128.setCode("90523"+this.emisora+numeroAutoliquidacion);
		    code128.setAltText("");
		    
		    PdfContentByte cb = oStamper.getOverContent(numeroPagina);
		    Image image128 = code128.createImageWithBarcode(cb, null, null);
		    if (esPrimeraHoja) {
		    	image128.setAbsolutePosition(305, 625);
		    } else {
		    	image128.setAbsolutePosition(305, 670);
		    }
		    image128.setWidthPercentage(150f);
		    cb.addImage(image128);
		} catch (Exception e) {
			Logger.error("Error al generar el código de barras: "+e.getMessage());
		}
	}

	public String getNumAutoliquidacion() {
		return XMLUtils.selectSingleNode(xmlDoc, DECLARACION,0).getAttributes().getNamedItem(NUMERO_AUTOLIQUIDACION).getNodeValue();
	}

	public Modelo600Liquidacion getLiquidacion() {
		this.modelo600Liquidacion = new Modelo600Liquidacion(XMLUtils.selectSingleNode(xmlDoc, LIQUIDACION,0),this.modelo600Notarios);
		return this.modelo600Liquidacion;
	}

	public Modelo600Identificacion getIdentificacion() {
		this.modelo600Identificacion = new Modelo600Identificacion(XMLUtils.selectSingleNode(xmlDoc, IDENTIFICACION,0),this.modelo600Notarios);
		return this.modelo600Identificacion;
	}			

	public Integer getNumSujetosPasivos() {
		Integer cont = Integer.valueOf(0);
		int longitud=XMLUtils.selectNodes(xmlDoc, INTERVINIENTE).length;
		for (int i=0; i<longitud ; i++) {
			if (XMLUtils.selectSingleNode(xmlDoc, INTERVINIENTE,i).getAttributes().getNamedItem(TIPO).getNodeValue().equalsIgnoreCase(SUJETO_PASIVO)) {
				cont++;
			}
		}
		aModelo600SP = new Modelo600SujetoPasivo[cont];
		return cont;
	}

	public Modelo600SujetoPasivo getSujetoPasivo(Integer num) {
		Integer cont = Integer.valueOf(0);
		int longitud=XMLUtils.selectNodes(xmlDoc, INTERVINIENTE).length;
		for (int i=0; i<longitud; i++) {
			if (XMLUtils.selectSingleNode(xmlDoc, INTERVINIENTE,i).getAttributes().getNamedItem(TIPO).getNodeValue().equalsIgnoreCase(SUJETO_PASIVO)) {
				if (cont.equals(num)) {
					this.aModelo600SP[num] = new Modelo600SujetoPasivo(XMLUtils.selectSingleNode(xmlDoc, INTERVINIENTE,i),this.modelo600Notarios);
					return this.aModelo600SP[num];
				}
				cont++;
			}
		}
		return null;
	}
	//CRUBENCVS 28/02/2022. 
	// Recuperamos todos los sujetos pasivos, para no hacerlo N veces
	public void recuperaSujetosPasivos(){
		List<Modelo600SujetoPasivo> l= new ArrayList<Modelo600SujetoPasivo>();
		Element[] elementos =XMLUtils.selectNodes(xmlDoc, INTERVINIENTE);
		int longitud=elementos.length;
		for (int i=0; i<longitud; i++) {
			if (XMLUtils.selectSingleNode(xmlDoc, INTERVINIENTE,i).getAttributes().getNamedItem(TIPO).getNodeValue().equalsIgnoreCase(SUJETO_PASIVO)) {
					//this.aModelo600SP[i] = new Modelo600SujetoPasivo(elementos[i],this.modelo600Notarios);
					l.add(new Modelo600SujetoPasivo(elementos[i],this.modelo600Notarios));
			}
		}
		this.aModelo600SP= new Modelo600SujetoPasivo[l.size()];
		this.aModelo600SP= l.toArray(this.aModelo600SP);
	}
	public void recuperaPresentadores(){
		List<Modelo600Presentador> l= new ArrayList<Modelo600Presentador>();
		Element[] elementos= XMLUtils.selectNodes(xmlDoc, INTERVINIENTE);
		int longitud= elementos.length;
		for (int i=0; i<longitud; i++) {
			if (XMLUtils.selectSingleNode(xmlDoc, INTERVINIENTE,i).getAttributes().getNamedItem(TIPO).getNodeValue().equalsIgnoreCase(PRESENTADOR)) {
					//this.aModelo600PR[i] = new Modelo600Presentador(elementos[i],this.modelo600Notarios);
					l.add(new Modelo600Presentador(elementos[i],this.modelo600Notarios));
			}
		}
		this.aModelo600PR= new Modelo600Presentador[l.size()];
		this.aModelo600PR= l.toArray(this.aModelo600PR);
	}
	//Recupera todos los transmitentes
	public void recuperaTransmitentes() {
		List<Modelo600Transmitente> l= new ArrayList<Modelo600Transmitente>();
		Element[] elementos= XMLUtils.selectNodes(xmlDoc, INTERVINIENTE);
		int longitud= elementos.length;
		for (int i=0;i<longitud;i++){
			if (elementos[i].getAttributes().getNamedItem(TIPO).getNodeValue().equalsIgnoreCase(TRANSMITENTE)){
				//this.aModelo600TR[i] = new Modelo600Transmitente(elementos[i],this.modelo600Notarios);
				l.add(new Modelo600Transmitente(elementos[i],this.modelo600Notarios));
			}
		}
		this.aModelo600TR= new Modelo600Transmitente[l.size()];
		this.aModelo600TR= l.toArray(this.aModelo600TR);
	}
	public Integer getNumTransmitentes() {
		Integer cont = Integer.valueOf(0);
		int longitud=XMLUtils.selectNodes(xmlDoc, INTERVINIENTE).length;
		for (int i=0; i<longitud; i++) {
			if (XMLUtils.selectSingleNode(xmlDoc, INTERVINIENTE,i).getAttributes().getNamedItem(TIPO).getNodeValue().equalsIgnoreCase(TRANSMITENTE)) {
				cont++;
			}
		}
		aModelo600TR = new Modelo600Transmitente[cont];
		return cont;
	}

	/*public Modelo600Transmitente getTransmitente(Integer num) {
		Integer cont = Integer.valueOf(0);
		int longitud=XMLUtils.selectNodes(xmlDoc, INTERVINIENTE).length;
		for (int i=0; i<longitud; i++) {
			if (XMLUtils.selectSingleNode(xmlDoc, INTERVINIENTE,i).getAttributes().getNamedItem(TIPO).getNodeValue().equalsIgnoreCase(TRANSMITENTE)) {
				if (cont.equals(num)) {
					this.aModelo600TR[num] = new Modelo600Transmitente(XMLUtils.selectSingleNode(xmlDoc, INTERVINIENTE,i),this.modelo600Notarios);
					return this.aModelo600TR[num];
				}
				cont++;
			}
		}
		return null;
	}*/
	public Modelo600Transmitente getTransmitente(int num) {
		int cont = 0;
		Element[] elementos= XMLUtils.selectNodes(xmlDoc, INTERVINIENTE);
		int longitud= elementos.length;
		for (int i=0;i<longitud;i++){
			if (elementos[i].getAttributes().getNamedItem(TIPO).getNodeValue().equalsIgnoreCase(TRANSMITENTE)){
				if (cont==num) {
					this.aModelo600TR[num] = new Modelo600Transmitente(elementos[i],this.modelo600Notarios);
					return this.aModelo600TR[num];
				}
				cont++;
			}
		}
		return null;
	}

	public Integer getNumPresentadores() {
		Integer cont = Integer.valueOf(0);
		int longitud=XMLUtils.selectNodes(xmlDoc, INTERVINIENTE).length;
		for (int i=0; i<longitud; i++) {
			if (XMLUtils.selectSingleNode(xmlDoc, INTERVINIENTE,i).getAttributes().getNamedItem(TIPO).getNodeValue().equalsIgnoreCase(PRESENTADOR)) {
				cont++;
			}
		}
		aModelo600PR = new Modelo600Presentador[cont];
		return cont;
	}

	public Modelo600Presentador getPresentador(Integer num) {
		Integer cont = Integer.valueOf(0);
		int longitud=XMLUtils.selectNodes(xmlDoc, INTERVINIENTE).length;
		for (int i=0; i<longitud; i++) {
			if (XMLUtils.selectSingleNode(xmlDoc, INTERVINIENTE,i).getAttributes().getNamedItem(TIPO).getNodeValue().equalsIgnoreCase(PRESENTADOR)) {
				if (cont.equals(num)) {
					this.aModelo600PR[num] = new Modelo600Presentador(XMLUtils.selectSingleNode(xmlDoc, INTERVINIENTE,i),this.modelo600Notarios);
					return this.aModelo600PR[num];
				}
				cont++;
			}
		}
		return null;
	}

	//CRUBENCVS  28/02/2022. Reduzco el número de operaciones
	//necesarias para procesar los elementos.
	public void recuperaBienesUrbanos(){
		List<Modelo600BienUrbano> l= new ArrayList<Modelo600BienUrbano>();
		Element[] elementos=XMLUtils.selectNodes(xmlDoc, BIEN_URBANO);
		int longitud= elementos.length;
		for (int i=0; i<longitud; i++) {
			//this.aModelo600BienUrbano[i] = new Modelo600BienUrbano(elementos[i],this.modelo600Notarios);
			l.add(new Modelo600BienUrbano(elementos[i],this.modelo600Notarios));
		}
		this.aModelo600BienUrbano= new Modelo600BienUrbano[l.size()];
		this.aModelo600BienUrbano= l.toArray(this.aModelo600BienUrbano);
	}
	
	public void recuperaBienesRustica(){
		List<Modelo600BienRustico> l= new ArrayList<Modelo600BienRustico>();
		Element[] elementos=XMLUtils.selectNodes(xmlDoc, BIEN_RUSTICO);
		int longitud= elementos.length;
		for (int i=0; i<longitud; i++) {
			//this.aModelo600BienRustico[i] = new Modelo600BienRustico(elementos[i],this.modelo600Notarios);
			l.add(new Modelo600BienRustico(elementos[i],this.modelo600Notarios));
		}
		this.aModelo600BienRustico= new Modelo600BienRustico[l.size()];
		this.aModelo600BienRustico= l.toArray(this.aModelo600BienRustico);
	}
	public Integer getNumBienesUrbanos() {
		Integer cont = Integer.valueOf(XMLUtils.selectNodes(xmlDoc, BIEN_URBANO).length);//new Integer(this.xmlDoc.getElementsByTagName("bien_urbano").getLength());
		aModelo600BienUrbano = new Modelo600BienUrbano[cont];
		Logger.info("getNumBienesUrbanos:"+cont);
		return cont;
	}

	public Modelo600BienUrbano getBienUrbano(Integer num) {
		Integer cont = Integer.valueOf(0);
		int longitud=XMLUtils.selectNodes(xmlDoc, BIEN_URBANO).length;
		for (int i=0; i<longitud; i++) {
			if (cont.equals(num)) {
				this.aModelo600BienUrbano[num] = new Modelo600BienUrbano(XMLUtils.selectSingleNode(xmlDoc, BIEN_URBANO,i),this.modelo600Notarios);
				return this.aModelo600BienUrbano[num];
			}
			cont++;
		}
		return null;
	}

	public Integer getNumBienesRusticos() {
		Integer cont = Integer.valueOf(XMLUtils.selectNodes(xmlDoc, BIEN_RUSTICO).length);
		aModelo600BienRustico = new Modelo600BienRustico[cont];
		return cont;
	}

	public Modelo600BienRustico getBienRustico(Integer num) {
		Integer cont = Integer.valueOf(0);
		int longitud=XMLUtils.selectNodes(xmlDoc, BIEN_RUSTICO).length;
		for (int i=0; i<longitud; i++) {
			if (cont.equals(num)) {
				this.aModelo600BienRustico[num] = new Modelo600BienRustico(XMLUtils.selectSingleNode(xmlDoc, BIEN_RUSTICO,i),this.modelo600Notarios);
				return this.aModelo600BienRustico[num];
			}
			cont++;
		}
		return null;
	}
	
	private String getFechaActual()
	{
		String fecha;
		GregorianCalendar cal = new GregorianCalendar(java.util.TimeZone.getTimeZone("Europe/Madrid"));
		fecha = String.format ("%1$02d/%2$02d/%3$4d",cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.MONTH)+1,cal.get(Calendar.YEAR));
		return fecha;

	}
}