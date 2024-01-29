package com.stpa.ws.server.util;

import java.io.ByteArrayOutputStream;
//import java.net.InetAddress;
//import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;


//import com.stp.webservices.ClienteWebServices;
import com.stpa.ws.pref.lt.Preferencias;
import com.stpa.ws.server.bean.LiquidacionTasas;
import com.stpa.ws.server.bean.LiquidacionTasasDetalleLiquidacion;
import com.stpa.ws.server.bean.ValidacionPersonas;
import com.stpa.ws.server.constantes.LiquidacionTasasConstantes;
import com.stpa.ws.server.exception.StpawsException;


public final class WebServicesUtils {
	
	private WebServicesUtils(){};
	/*
	 * Devuelve el PDF. 
	 */
	public static String obtenerDocumento(String ideper, String notificacion) throws StpawsException {					
		String respuesta;
		
		Preferencias pref = new Preferencias();
		pref.CargarPreferencias();
		try {
		    //Ejecutamos		 
		    if (notificacion.equalsIgnoreCase("S")) {
		    	respuesta = ClienteWebServices.obtenerDocumentoPago(ideper,"NT","N");
		    }
		    else {
		    	respuesta = ClienteWebServices.obtenerDocumentoPago(ideper,"","N");
		    }
		    	
		} /*catch (MalformedURLException mfe) {
			com.stpa.ws.server.util.Logger.error(mfe.getMessage(), mfe,
					com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(
					LiquidacionTasasConstantes.MSJ_PROP,
					"webservices.error.call.invalid.url"), mfe);
		} */catch (Exception wse) {
			Logger log= new Logger(GestorIdLlamada.getIdLlamada());
			log.error(wse.getMessage(), wse,
					com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(
					LiquidacionTasasConstantes.MSJ_PROP,
					"webservices.error.call.no.connect"), wse);
		}
	    												
		return respuesta;					
	}
	
	
	public static String obtenerClave() throws StpawsException {
		String clave = "";
		String peticion = generarPeticionClave();
		String xmlOut = wsCall(peticion);
		try {
			clave = wsResponseClave_AsList(xmlOut);
		} catch (Exception e) {
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(
					"com.stpa.ws.server.configuracion.messages",
					"msg.err.gen.num"), e);
		}
		return clave;
	}

	public static String generarPeticionClave() throws StpawsException {
		XMLUtils xmlutils = new XMLUtils();
		xmlutils = iniGenerarPeticion(xmlutils,
				"INTERNET.obtenerclaveconsejerias");
		return finGenerarPeticion(xmlutils, 6);
	}

	public static String wsResponseClave_AsList(String respuestaWebService)
			throws StpawsException {

		String[] columnasARecuperar = new String[] { "STRING_CADE" };
		String[] estructurasARecuperar = new String[] { "CADE_CADENA" };
		Map<String, Object> respuestaAsMap = new HashMap<String, Object>();
		String clave = "";
		try {
			respuestaAsMap = XMLUtils.compilaXMLDoc(respuestaWebService,
					estructurasARecuperar, columnasARecuperar, false);
		} catch (RemoteException re) {
			throw new StpawsException(
					PropertiesUtils.getValorConfiguracion(
							"com.stpa.ws.server.configuracion.messages",
							"msg.err.gen"), re);
		}
		Logger log= new Logger(GestorIdLlamada.getIdLlamada());
		Object[] objcol = (Object[]) respuestaAsMap.get("CADE_CADENA");
		if (objcol != null) {
			log.debug(
					"Rellenamos objeto de respuesta...",
					com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			for (int i = 0; i < objcol.length; i++) {
				String[] objrow = (String[]) objcol[i];
				if (objrow != null && !objrow[0].equals(""))
					clave = objrow[0];
			}
			log.debug(
					"Fin rellenado objeto de respuesta.",
					com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}

		return clave;
	}

	/**
	 * @param peticion
	 * @return
	 * @throws StpawsException
	 */
	public static String wsCall(String peticion) throws StpawsException {
		Logger log= new Logger(GestorIdLlamada.getIdLlamada());
		log.debug("Ini wsCall(String peticion)",
				com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		Preferencias pref = new Preferencias();
		String xmlOut = "";
		try {
			// Podría dar problemas de concurrencia, según parece.
			//InetAddress addr = InetAddress.getLocalHost();
			//String hostaddress = addr.getHostAddress();
			String accesoWebservice = pref.getM_wslanzadorentornoBDD();
			log.debug("accesoWebservice: "
					+ accesoWebservice,
					com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
			log.debug("peticion: " + peticion,
					com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
			xmlOut = ClienteWebServices.executePL(accesoWebservice,
					peticion, "", "", "", "");
			log.debug("respuesta: " + xmlOut,
					com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
		} catch (Exception wse) {
			log.error(wse.getMessage(), wse,
					com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(
					LiquidacionTasasConstantes.MSJ_PROP,
					"webservices.error.call.no.connect"), wse);
		}
		log.debug("Fin wsCall(String peticion)",
				com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return xmlOut;
	}

	/**
	 * @param xmlutils
	 * @param accion
	 * @return
	 * @throws StpawsException
	 */
	private static XMLUtils iniGenerarPeticion(XMLUtils xmlutils, String accion)
			throws StpawsException {
		try {
			xmlutils.crearXMLDoc();
		} catch (RemoteException re) {
			Logger log= new Logger(GestorIdLlamada.getIdLlamada());
			log.error(re.getMessage(), re,
					com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(
					LiquidacionTasasConstantes.MSJ_PROP,
					"webservices.error.ini.generar.peticion"), re);
		}
		xmlutils.crearNode("peti", "", null, null);
		xmlutils.reParentar(1);
		xmlutils.crearNode("proc", "", new String[] { "nombre" },
				new String[] { accion });
		xmlutils.reParentar(1);

		// Parametros de cabecera
		// mac 2/2/10
		if (!accion.equalsIgnoreCase("INTERNET.obtenerclaveconsejerias")
				&& !accion.equalsIgnoreCase("CorecaV4Service.CorecaV4")
				&& !accion
						.equalsIgnoreCase("INTERNET_DOCUMENTOSV2.AltaDocumento")) {
			fillPeti(xmlutils, "1", "2", null, "1");
			fillPeti(xmlutils, "1", "2", null, "2");
			fillPeti(xmlutils, "USU_WEB_SAC", "1", null, "3");
			fillPeti(xmlutils, "33", "2", null, "4");
		}
		return xmlutils;
	}

	/**
	 * @param xmlutils
	 * @param accion
	 * @return
	 * @throws StpawsException
	 */
	private static XMLUtils iniGenerarPeticion2(XMLUtils xmlutils, String accion)
			throws StpawsException {
		try {
			xmlutils.crearXMLDoc();
		} catch (RemoteException re) {
			Logger log= new Logger(GestorIdLlamada.getIdLlamada());
			log.error(re.getMessage(), re,
					com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(
					"Error en la creaciÃ³n del xml para la peticiÃ³n: "
							+ re.getMessage(), re);
		}
		xmlutils.crearNode("peti", "", null, null);
		xmlutils.reParentar(1);
		xmlutils.crearNode("proc", "", new String[] { "nombre" },
				new String[] { accion });
		xmlutils.reParentar(1);

		// Parametros de cabecera
		fillPeti(xmlutils, "33", "2", null, "1");

		return xmlutils;
	}
	
	/**
	 * Crea una petición a lanzador vacía.
	 * @param xmlutils
	 * @param accion
	 * @return
	 * @throws StpawsException
	 */
	private static XMLUtils iniGenerarPeticion3(XMLUtils xmlutils, String accion)
			throws StpawsException {
		try {
			xmlutils.crearXMLDoc();
		} catch (RemoteException re) {
			Logger log= new Logger(GestorIdLlamada.getIdLlamada());
			log.error(re.getMessage(), re,
					com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(
					"Error en la creación del xml para la petición: "
							+ re.getMessage(), re);
		}
		xmlutils.crearNode("peti", "", null, null);
		xmlutils.reParentar(1);
		xmlutils.crearNode("proc", "", new String[] { "nombre" },
				new String[] { accion });
		xmlutils.reParentar(1);

		// Parametros de cabecera

		return xmlutils;
	}

	/**
	 * @param xmlutils
	 * @param posActual
	 * @return
	 * @throws StpawsException
	 */
	private static String finGenerarPeticion(XMLUtils xmlutils, int posActual)
			throws StpawsException {
		// Identificacion
		fillPeti(xmlutils, "P", "1", null, String.valueOf(posActual));

		xmlutils.reParentar(-1);
		xmlutils.reParentar(-1);
		String xmlIn = "";
		try {
			xmlIn = xmlutils.informarXMLDoc();
		} catch (RemoteException re) {
			Logger log= new Logger(GestorIdLlamada.getIdLlamada());
			log.error(re.getMessage(), re,
					com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(
					LiquidacionTasasConstantes.MSJ_PROP,
					"webservices.error.fin.generar.peticion"), re);
		}

		return xmlIn;
	}

	/**
	 * @param xmlutils
	 * @param valor
	 * @param tipo
	 * @param formato
	 * @param orden
	 */
	private static void fillPeti(XMLUtils xmlutils, String valor, String tipo,
			String formato, String orden) {
		xmlutils.crearNode("param", "", new String[] { "id" },
				new String[] { orden });
		xmlutils.reParentar(1);
		xmlutils.crearNode("valor", valor);
		xmlutils.crearNode("tipo", tipo);
		if (formato != null)
			xmlutils.crearNode("formato", formato);
		else
			xmlutils.crearNode("formato", "");
		xmlutils.reParentar(-1);
	}

	/**
	 * Parametrizaje de la llamada al WS "INTERNET_TASAS.recuperarliqtasa"
	 * 
	 * @param lt
	 * @return
	 * @throws StpawsException
	 */
	public static String generarConsultaLiquidacionTasa(LiquidacionTasas lt)
			throws StpawsException {
		XMLUtils xmlutils = new XMLUtils();
		xmlutils = iniGenerarPeticion(xmlutils,
				"INTERNET_TASAS.recuperarliqtasa");

		// Modelo
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getCentroGestor(),
				LiquidacionTasasConstantes.WS_CADENA, null, "5");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getCodTasa(),
				LiquidacionTasasConstantes.WS_CADENA, null, "6");
		fillPeti(xmlutils, lt.getPeticion().getNumeroUnico(),
				LiquidacionTasasConstantes.WS_CADENA, null, "7");

		return finGenerarPeticion(xmlutils, 8);
	}

	/**
	 * Parametrizaje de la llamada al WS "INTERNET_TASAS.anularnotificacion"
	 * 
	 * @param lt
	 * @return
	 * @throws StpawsException
	 */
	public static String generarAnularNotificacion(LiquidacionTasas lt)
			throws StpawsException {
		XMLUtils xmlutils = new XMLUtils();
		xmlutils = iniGenerarPeticion(xmlutils,
				"INTERNET_TASAS.anularnotificacion");

		// Modelo
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getCentroGestor(),
				LiquidacionTasasConstantes.WS_CADENA, null, "5");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getCodTasa(),
				LiquidacionTasasConstantes.WS_CADENA, null, "6");
		fillPeti(xmlutils, lt.getPeticion().getNumeroUnico(),
				LiquidacionTasasConstantes.WS_CADENA, null, "7");

		return finGenerarPeticion(xmlutils, 8);
	}

	/**
	 * @param lt
	 * @param validacionPersonas
	 * @return
	 * @throws StpawsException
	 */
	public static String generarAltaLiquidacionTasa(LiquidacionTasas lt,
			ValidacionPersonas validacionPersonas) throws StpawsException {
		XMLUtils xmlutils = new XMLUtils();
		xmlutils = iniGenerarPeticion(xmlutils, "INTERNET_TASAS.generarliqtasa");

		
	
		
		// Detalle Liquidaciï¿½n
		LiquidacionTasasDetalleLiquidacion[] detLiquidacion = lt.getPeticion()
				.getLiquidacion().getDetalleLiquidacion();// ;[0].getCodTarifa()
		String auxCodTarifa = "";
		String auxUnidadBase = "";
		for (int i = 0; i < detLiquidacion.length; i++) {
			if (detLiquidacion[i].getCodTarifa() != null) {
				auxCodTarifa = auxCodTarifa + detLiquidacion[i].getCodTarifa()
						+ "##";
				auxUnidadBase = auxUnidadBase
						+ detLiquidacion[i].getNumUnidades() + "##";
			}
		}

		// Modelo
		fillPeti(xmlutils, lt.getPeticion().getNumeroUnico(),
				LiquidacionTasasConstantes.WS_CADENA, null, "7");

		// Datos Liquidaciï¿½n
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getNotificacion(),
				LiquidacionTasasConstantes.WS_CADENA, null, "8");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getFechaDevengo(),
				LiquidacionTasasConstantes.WS_CADENA, null, "9");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getCentroGestor(),
				LiquidacionTasasConstantes.WS_CADENA, null, "10");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getCodTasa(),
				LiquidacionTasasConstantes.WS_CADENA, null, "11");

		// Detalle Liquidaciï¿½n
		fillPeti(xmlutils, auxCodTarifa, LiquidacionTasasConstantes.WS_CADENA,
				null, "12");
		fillPeti(xmlutils, auxUnidadBase, LiquidacionTasasConstantes.WS_CADENA,
				null, "13");

		// Datos Liquidaciï¿½n
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getExpGestion(),
				LiquidacionTasasConstantes.WS_CADENA, null, "14");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getExpExterno(),
				LiquidacionTasasConstantes.WS_CADENA, null, "15");

		// Datos SP
	
		fillPeti(xmlutils, validacionPersonas.getPersSP(),
				LiquidacionTasasConstantes.WS_NUMERO, null, "16");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getDatosSP()
				.getIdFiscal(), LiquidacionTasasConstantes.WS_CADENA, null,
				"17");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getDatosSP()
				.getNombreApellidos(), LiquidacionTasasConstantes.WS_CADENA,
				null, "18");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getDatosSP()
				.getTelefono(), LiquidacionTasasConstantes.WS_CADENA, null,
				"19");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getDatosSP()
				.getDireccion(), LiquidacionTasasConstantes.WS_CADENA, null,
				"20");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getDatosSP()
				.getCP(), LiquidacionTasasConstantes.WS_NUMERO, null, "21");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getDatosSP()
				.getMunicipio(), LiquidacionTasasConstantes.WS_NUMERO, null,
				"22");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getDatosSP()
				.getProvincia(), LiquidacionTasasConstantes.WS_NUMERO, null,
				"23");

		// Datos Presentador
		fillPeti(xmlutils, validacionPersonas.getPersPR(),
				LiquidacionTasasConstantes.WS_NUMERO, null, "24");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion()
				.getDatosPresentador().getIdFiscal(),
				LiquidacionTasasConstantes.WS_CADENA, null, "25");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion()
				.getDatosPresentador().getNombreApellidos(),
				LiquidacionTasasConstantes.WS_CADENA, null, "26");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion()
				.getDatosPresentador().getTelefono(),
				LiquidacionTasasConstantes.WS_CADENA, null, "27");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion()
				.getDatosPresentador().getDireccion(),
				LiquidacionTasasConstantes.WS_CADENA, null, "28");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion()
				.getDatosPresentador().getCP(),
				LiquidacionTasasConstantes.WS_NUMERO, null, "29");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion()
				.getDatosPresentador().getMunicipio(),
				LiquidacionTasasConstantes.WS_NUMERO, null, "30");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion()
				.getDatosPresentador().getProvincia(),
				LiquidacionTasasConstantes.WS_NUMERO, null, "31");

		// Datos Liquidaciï¿½n
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getPeriodo(),
				LiquidacionTasasConstantes.WS_CADENA, null, "32");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion()
				.getPorcBonificacion(), LiquidacionTasasConstantes.WS_NUMERO,
				null, "33");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getPorcRecargo(),
				LiquidacionTasasConstantes.WS_NUMERO, null, "34");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getImpRecargo(),
				LiquidacionTasasConstantes.WS_NUMERO, null, "35");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getImpIntereses(),
				LiquidacionTasasConstantes.WS_NUMERO, null, "36");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getFechaAcuerdo(),
				LiquidacionTasasConstantes.WS_CADENA, null, "37");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion()
				.getObjLiquidacion(), LiquidacionTasasConstantes.WS_CADENA,
				null, "38");

		return finGenerarPeticion(xmlutils, 39);
	}

	/**
	 * Parametrizaje de la llamada al WS "INTERNET_TASAS.validarpersona"
	 * 
	 * @param lt
	 * @return
	 * @throws StpawsException
	 */
	public static String generarValidacionPersonas(LiquidacionTasas lt)
			throws StpawsException {
		XMLUtils xmlutils = new XMLUtils();
		xmlutils = iniGenerarPeticion2(xmlutils,
				"INTERNET_TASAS.validarpersonas");

		// Modelo
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getDatosSP()
				.getIdFiscal(), LiquidacionTasasConstantes.WS_CADENA, null, "2");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion().getDatosSP()
				.getNombreApellidos(), LiquidacionTasasConstantes.WS_CADENA,
				null, "3");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion()
				.getDatosPresentador().getIdFiscal(),
				LiquidacionTasasConstantes.WS_CADENA, null, "4");
		fillPeti(xmlutils, lt.getPeticion().getLiquidacion()
				.getDatosPresentador().getNombreApellidos(),
				LiquidacionTasasConstantes.WS_CADENA, null, "5");

		return finGenerarPeticion(xmlutils, 6);
	}

	/**
	 * Esta llamada deberï¿½ devolver en la estructura canu_cadenas_numeros, el
	 * valor del parï¿½metro persSP en nume1_canu y en nume2_canu el valor del
	 * parï¿½metro persPR. En caso en que alguno de ellos no devuelva dato alguno
	 * se devolverï¿½ un error en R_RESULTADO igual a '0012'. En caso en que el
	 * procedimiento almacenado devuelva algï¿½n valor en string1_canu de la
	 * estructura canu_cadenas_numeros, ï¿½ste se devolverï¿½ en R_RESULTADO.
	 * 
	 * @param respuestaWebService
	 * @param lt
	 * @return
	 * @throws StpawsException
	 */
	public static HashMap<String, String> wsResponseValidarPersona(
			String respuestaWebService, LiquidacionTasas lt)
			throws StpawsException {
		HashMap<String, String> resultado = new HashMap<String, String>();
		String[] columnasARecuperar = new String[] { "STRING1_CANU",
				"NUME1_CANU", "NUME2_CANU" };
		String[] estructurasARecuperar = new String[] { "CANU_CADENAS_NUMEROS",
				"CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS" };

		Map<String, Object> respuestaAsMap = null;
		try {
			respuestaAsMap = XMLUtils.compilaXMLDoc(respuestaWebService,
					estructurasARecuperar, columnasARecuperar, false);
		} catch (RemoteException re) {
			Logger log= new Logger(GestorIdLlamada.getIdLlamada());
			log.error(re.getMessage(), re,
					com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(
					LiquidacionTasasConstantes.MSJ_PROP,
					"webservices.error.respuesta.validar.persona"), re);
		}
		Object[] objcol = null;
		objcol = (Object[]) respuestaAsMap.get("CANU_CADENAS_NUMEROS");
		if (objcol != null) {
			String[] objStr = (String[]) objcol[0];
			resultado.put("STRING1_CANU", objStr[0]);
			resultado.put("NUME1_CANU", objStr[1]);
			resultado.put("NUME2_CANU", objStr[2]);
		}

		return resultado;
	}

	/**
	 * @param respuestaWebService
	 * @param lt
	 * @return
	 * @throws StpawsException
	 */
	public static HashMap<String, String> wsResponseAltaLiquidacionTasa(
			String respuestaWebService, LiquidacionTasas lt)
			throws StpawsException {
		Logger log= new Logger(GestorIdLlamada.getIdLlamada());
		log.debug(
						"Ini wsResponseAltaLiquidacionTasa(String respuestaWebService, LiquidacionTasas lt)",
						com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		HashMap<String, String> resultado = new HashMap<String, String>();
		String[] columnasARecuperar = new String[] { "STRING1_CANU",
				"STRING2_CANU", "STRING3_CANU", "STRING4_CANU" };
		String[] estructurasARecuperar = new String[] { "CANU_CADENAS_NUMEROS",
				"CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS",
				"CANU_CADENAS_NUMEROS" };
		log.debug(
						"wsResponseAltaLiquidacionTasa(String respuestaWebService, LiquidacionTasas lt) Paso 1",
						com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		Map<String, Object> respuestaAsMap = null;
		try {
			respuestaAsMap = XMLUtils.compilaXMLDoc(respuestaWebService,
					estructurasARecuperar, columnasARecuperar, false);
		} catch (RemoteException re) {
			log.error(
							"Error en wsResponseAltaLiquidacionTasa(String respuestaWebService, LiquidacionTasas lt): "
									+ re.getMessage(), re,
							com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(
					"Error en XML de respuesta del alta de liquidaciï¿½n.", re);
		}

		Object[] objcol2 = null;
		objcol2 = (Object[]) respuestaAsMap.get("CANU_CADENAS_NUMEROS");
		log.debug(
						"wsResponseAltaLiquidacionTasa(String respuestaWebService, LiquidacionTasas lt) objcol2:"
								+ objcol2,
						com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		if (objcol2 != null) {
			String[] objStr2 = (String[]) objcol2[0];
			resultado.put("STRING1_CANU", objStr2[0]);
			resultado.put("STRING2_CANU", objStr2[1]);
			resultado.put("STRING3_CANU", objStr2[2]);
			resultado.put("STRING4_CANU", objStr2[3]);
		}
		log.debug(
						"Fin wsResponseAltaLiquidacionTasa(String respuestaWebService, LiquidacionTasas lt)",
						com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return resultado;
	}

	/**
	 * @param respuestaWebService
	 * @param lt
	 * @return
	 * @throws StpawsException
	 */
	public static HashMap<String, String> wsResponseConsultaLiquidacionTasa(
			String respuestaWebService, LiquidacionTasas lt)
			throws StpawsException {
		HashMap<String, String> resultado = new HashMap<String, String>();
		String[] columnasARecuperar = new String[] { "STRING1_CANU",
				"STRING2_CANU", "STRING3_CANU" };
		String[] estructurasARecuperar = new String[] { "CANU_CADENAS_NUMEROS",
				"CANU_CADENAS_NUMEROS", "CANU_CADENAS_NUMEROS" };

		Map<String, Object> respuestaAsMap = null;
		try {
			respuestaAsMap = XMLUtils.compilaXMLDoc(respuestaWebService,
					estructurasARecuperar, columnasARecuperar, false);
		} catch (RemoteException re) {
			Logger log= new Logger(GestorIdLlamada.getIdLlamada());
			log.error(re.getMessage(), re,
					com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(
					"Error en la respuesta del web service consulta liquidacion tasa: "
							+ re.getMessage(), re);
		}

		Object[] objcol2 = null;
		objcol2 = (Object[]) respuestaAsMap.get("CANU_CADENAS_NUMEROS");
		if (objcol2 != null) {
			String[] objStr2 = (String[]) objcol2[0];
			resultado.put("STRING1_CANU", objStr2[0]);
			resultado.put("STRING2_CANU", objStr2[1]);
			resultado.put("STRING3_CANU", objStr2[2]);
		}

		return resultado;
	}

	/**
	 * @param respuestaWebService
	 * @param lt
	 * @return
	 * @throws StpawsException
	 */
	public static HashMap<String, String> wsResponseAnularLiquidacionTasa(
			String respuestaWebService, LiquidacionTasas lt)
			throws StpawsException {
		HashMap<String, String> resultado = new HashMap<String, String>();
		String[] columnasARecuperar = new String[] { "STRING1_CANU",
				"STRING2_CANU" };
		String[] estructurasARecuperar = new String[] { "CANU_CADENAS_NUMEROS",
				"CANU_CADENAS_NUMEROS" };

		Map<String, Object> respuestaAsMap = null;
		try {
			respuestaAsMap = XMLUtils.compilaXMLDoc(respuestaWebService,
					estructurasARecuperar, columnasARecuperar, false);
		} catch (RemoteException re) {
			Logger log= new Logger(GestorIdLlamada.getIdLlamada());
			log.error(re.getMessage(), re,
					com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(
					"Error en la respuesta del web service anular liquidacion tasa: "
							+ re.getMessage(), re);
		}

		Object[] objcol2 = null;
		objcol2 = (Object[]) respuestaAsMap.get("CANU_CADENAS_NUMEROS");
		if (objcol2 != null) {
			String[] objStr2 = (String[]) objcol2[0];
			resultado.put("STRING1_CANU", objStr2[0]);
			resultado.put("STRING2_CANU", objStr2[1]);
		}

		return resultado;
	}

	public static String generarRecibosOnline(String ideper)
			throws StpawsException {
		XMLUtils xmlutils = new XMLUtils();
		xmlutils = iniGenerarPeticion(xmlutils, "access_informes.recibosOnLine");

		fillPeti(xmlutils, ideper, LiquidacionTasasConstantes.WS_NUMERO, null,
				"5");
		fillPeti(xmlutils, "0", LiquidacionTasasConstantes.WS_NUMERO, null, "6");
		fillPeti(xmlutils, "A", LiquidacionTasasConstantes.WS_CADENA, null, "7");
		fillPeti(xmlutils, "-1", LiquidacionTasasConstantes.WS_CADENA, null,
				"8");
		fillPeti(xmlutils, "-1", LiquidacionTasasConstantes.WS_NUMERO, null,
				"9");
		fillPeti(xmlutils, "N", LiquidacionTasasConstantes.WS_CADENA, null,
				"10");
		fillPeti(xmlutils, "CA", LiquidacionTasasConstantes.WS_CADENA, null,
				"11");
		fillPeti(xmlutils, "N", LiquidacionTasasConstantes.WS_CADENA, null,
				"12");
		fillPeti(xmlutils, "S", LiquidacionTasasConstantes.WS_CADENA, null,
				"13");

		return finGenerarPeticion(xmlutils, 14);
	}
	
	//CRUBENCVS
	/**
	 * Notifica el valor que se pasa como parámetro.
	 * @param ideper Eper del valor que se ha de notificar.
	 * @return Peticion generada.
	 * @throws StpawsException
	 */
	public static String notificacionValor(String ideper)
	throws StpawsException {
	XMLUtils xmlutils = new XMLUtils();
	xmlutils = iniGenerarPeticion3(xmlutils, "INTERNET_TASAS.NotiReposicionVoluIntfz");
	
	fillPeti(xmlutils, ideper, LiquidacionTasasConstantes.WS_NUMERO, null,
			"1");
	return finGenerarPeticion(xmlutils, 2);
	}
	/**
	 * Devuelve un mapa con la respuesta del servicio de notificación de valor.
	 * @param respuestaWebService
	 * @return
	 * @throws StpawsException
	 */
	public static HashMap<String, String> wsResponseNotificacionValor(
			String respuestaWebService)
			throws StpawsException {
		Logger log= new Logger(GestorIdLlamada.getIdLlamada());
		log.debug(
						"Ini wsResponseNotificacionValor(String respuestaWebService)",
						com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		HashMap<String, String> resultado = new HashMap<String, String>();
		String[] columnasARecuperar = new String[] { "STRING1_CANU",
				"STRING2_CANU"};
		String[] estructurasARecuperar = new String[] { "CANU_CADENAS_NUMEROS",
				"CANU_CADENAS_NUMEROS"};
		log.debug(
						"wsResponseNotificacionValor(String respuestaWebService) Paso 1",
						com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		Map<String, Object> respuestaAsMap = null;
		try {
			respuestaAsMap = XMLUtils.compilaXMLDoc(respuestaWebService,
					estructurasARecuperar, columnasARecuperar, false);
		} catch (RemoteException re) {
			log.error(
							"Error en wsResponseNotificacionValor(String respuestaWebService): "
									+ re.getMessage(), re,
							com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(
					"Error en XML de respuesta de notificación de valor.", re);
		}

		Object[] objcol2 = null;
		objcol2 = (Object[]) respuestaAsMap.get("CANU_CADENAS_NUMEROS");
		log.debug(
						"wsResponseAltaLiquidacionTasa(String respuestaWebService) objcol2:"
								+ objcol2,
						com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		if (objcol2 != null) {
			String[] objStr2 = (String[]) objcol2[0];
			resultado.put("STRING1_CANU", objStr2[0]);
			resultado.put("STRING2_CANU", objStr2[1]);
		}
		log.debug(
						"Fin wsResponseAltaLiquidacionTasa(String respuestaWebService)",
						com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return resultado;
	}
	public static String altaDocumento(String p_tipo, String p_nombre,
			String p_codVerif, String p_nifSp, String p_nifPr, String doc,
			String p_tipoDoc) throws StpawsException {

		XMLUtils xmlutils = new XMLUtils();
		xmlutils = iniGenerarPeticion(xmlutils,
				"INTERNET_DOCUMENTOSV2.AltaDocumento");
		fillPeti(xmlutils, p_tipo, LiquidacionTasasConstantes.WS_CADENA, null,
				"1");
		fillPeti(xmlutils, p_nombre, LiquidacionTasasConstantes.WS_CADENA,
				null, "2");
		fillPeti(xmlutils, p_codVerif, LiquidacionTasasConstantes.WS_CADENA,
				null, "3");
		fillPeti(xmlutils, p_nifSp, LiquidacionTasasConstantes.WS_CADENA, null,
				"4");
		fillPeti(xmlutils, p_nifPr, LiquidacionTasasConstantes.WS_CADENA, null,
				"5");
		fillPeti(xmlutils, "", LiquidacionTasasConstantes.WS_CADENA, null, "6");
		fillPeti(xmlutils, "P", LiquidacionTasasConstantes.WS_CADENA, null, "7");
		fillPeti(xmlutils, "S", LiquidacionTasasConstantes.WS_CADENA, null, "8");
		fillPeti(xmlutils, "S", LiquidacionTasasConstantes.WS_CADENA, null, "9");

		// Se comprime el documento
		byte[] docDecodificado = Base64.decode(doc.toCharArray());
		String documentzippeado = new String();
		ByteArrayOutputStream resulByteArray = new ByteArrayOutputStream();

		try {
			resulByteArray.write(docDecodificado);
			documentzippeado = PdfComprimidoUtils.comprimirPDF(resulByteArray);
			fillPeti(xmlutils, documentzippeado,
					LiquidacionTasasConstantes.WS_CLOB, null, "10");
		} catch (Exception e) {
			Logger log= new Logger(GestorIdLlamada.getIdLlamada());
			log.error("Error al comprimir PDF:"
					+ e.getMessage(),
					com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}

		fillPeti(xmlutils, p_tipoDoc, LiquidacionTasasConstantes.WS_CADENA,
				null, "11");

		return finGenerarPeticion(xmlutils, 12);
	}
}
