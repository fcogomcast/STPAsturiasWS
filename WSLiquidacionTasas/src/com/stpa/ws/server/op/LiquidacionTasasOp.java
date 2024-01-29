package com.stpa.ws.server.op;

import java.util.HashMap;


import com.stpa.ws.server.base.IStpawsBase;
import com.stpa.ws.server.bean.LiquidacionTasas;
import com.stpa.ws.server.bean.LiquidacionTasasRespuesta;
import com.stpa.ws.server.bean.ValidacionPersonas;
import com.stpa.ws.server.constantes.LiquidacionTasasConstantes;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.util.Logger;
import com.stpa.ws.server.util.PropertiesUtils;
import com.stpa.ws.server.util.StpawsUtil;
import com.stpa.ws.server.util.WebServicesUtils;
import com.stpa.ws.server.validation.LiquidacionTasasValidation;


//import com.stpa.ws.cliente.DocumentoPago.*;

public class LiquidacionTasasOp implements IStpawsBase {
	String idLlamada;
	Logger log=null;
	public LiquidacionTasasOp(String idLlamada) {
		this.idLlamada = idLlamada;
		log = new Logger(idLlamada);
	}
	/* M�todo principal de la operaci�n
	 * @see com.stpa.ws.server.base.IStpawsBase#doOwnAction(java.lang.Object)
	 */
	public Object doOwnAction(Object objIn) {
		LiquidacionTasas lt = null;
		ValidacionPersonas validacionPersonas = new ValidacionPersonas();
		LiquidacionTasasValidation ltv = new LiquidacionTasasValidation(idLlamada);

		log.debug("doOwnAction(Object objIn)",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		if(objIn instanceof LiquidacionTasas){
			lt = (LiquidacionTasas)objIn;

			try{
				Object[] obj=new Object[2];
				obj[0]=lt;
				obj[1]=validacionPersonas;
				
				
				if(ltv.isValid(obj)){					
					if(LiquidacionTasasConstantes.GENERACION_LIQUIDACION_TASA.equals(lt.getPeticion().getOperacion())){						 
						validacionPersonas  = ltv.validarPersona(lt);																		
						lt=altaLiquidacionTasa(lt,validacionPersonas);
					}else if(LiquidacionTasasConstantes.CONSULTA_LIQUIDACION_TASA.equals(lt.getPeticion().getOperacion())){
						lt=consultaLiquidacionTasa(lt);
					}else if(LiquidacionTasasConstantes.RECHAZAR_NOTIFICACION.equals(lt.getPeticion().getOperacion())){
						lt=anularNotificacion(lt);
					}
				}
			}catch(StpawsException e1){
				lt = tratarError(e1.getError(),lt);
			}catch(Throwable e2){
				try {
					log.error(e2.getMessage(),e2,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
					lt = tratarError(PropertiesUtils.getValorConfiguracion(LiquidacionTasasConstantes.MSJ_PROP,"msg.err.gen") + e2.getMessage(),lt);
				} catch(Throwable t){
					log.error(t.getMessage(),t,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				}
			}
			log.debug("Generamos la mac para la respuesta...",Logger.LOGTYPE.APPLOG);
			try{
				lt.getMac().setMac(StpawsUtil.genMacLiquidacionTasasRespuesta(lt, idLlamada));
			}catch(Throwable t){
				log.error(t.getMessage(),t,Logger.LOGTYPE.APPLOG);
			}
			log.debug("Mac generada.",Logger.LOGTYPE.APPLOG);
		}else{
			try {
				lt = tratarError(PropertiesUtils.getValorConfiguracion(LiquidacionTasasConstantes.MSJ_PROP,"msg.obj.no.valid"),null);
			} catch (Throwable t) {
				log.error(t.getMessage(),t,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			} 
		}
		return lt;
	}
	
	/**
	 *  Llamada al WS anular notificaci�n
	 *  
	 * @param lt
	 * @return
	 * @throws StpawsException
	 */
	private LiquidacionTasas anularNotificacion(LiquidacionTasas lt) throws StpawsException{
		log.debug("Ini anularNotificacion(LiquidacionTasas lt)",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		String peticion = WebServicesUtils.generarAnularNotificacion(lt);
		String xmlOut = WebServicesUtils.wsCall(peticion);
		HashMap<String, String> resultado = WebServicesUtils.wsResponseConsultaLiquidacionTasa(xmlOut,lt);
		String codError=resultado.get("STRING2_CANU");
		String descError=null;
		String estadoValor=null;
		if(codError==null || "".equals(codError) ){
			log.error("Error en el retorno del WS Anular Liquidaci�n de Tasas",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(LiquidacionTasasConstantes.MSJ_PROP,"msg.err.anular.notificacion"), null);
		}else if("0000".equals(codError)){
			lt.getRespuesta().setResultado(PropertiesUtils.getValorConfiguracion(LiquidacionTasasConstantes.MSJ_PROP,"msg.err.ok"));
		}else if("0001".equals(codError)){
			log.error("Error 0001 en el retorno del WS Anular Liquidaci�n de Tasas",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			descError=resultado.get("STRING1_CANU");
			estadoValor=resultado.get("STRING4_CANU");
			codError=codError+": "+descError+"; Estado:"+estadoValor;
		}else{
			log.error("Error en el retorno del WS Anular Liquidaci�n de Tasas",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			descError=resultado.get("STRING1_CANU");
			throw new StpawsException(codError+": "+descError, null);
		}
		log.debug("Fin anularNotificacion(LiquidacionTasas lt)",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return lt;
	}
	
	/**
	 * 
	 * Llamada al WS generar liquidaci�n tasa
	 * 
	 * @param lt
	 * @return
	 * @throws StpawsException
	 */
	private LiquidacionTasas altaLiquidacionTasa(LiquidacionTasas lt,ValidacionPersonas validacionPersonas) throws StpawsException{
		
		log.debug("Ini altaLiquidacionTasa(LiquidacionTasas lt,ValidacionPersonas validacionPersonas)",com.stpa.ws.server.util.Logger.LOGTYPE.CLIENTLOG);
		String peticion = WebServicesUtils.generarAltaLiquidacionTasa(lt,validacionPersonas);
		String xmlOut = WebServicesUtils.wsCall(peticion);
		HashMap<String, String> resultado = WebServicesUtils.wsResponseAltaLiquidacionTasa(xmlOut,lt);
		String codError=resultado.get("STRING2_CANU");
		String numeroLiquidacion=null;
		String ideper=null;
		String estadoValor=null;
		String descError=null;
		log.debug("codError"+codError,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		if(codError==null || "".equals(codError)){
			log.error("Error en el retorno del WS Alta Liquidaci�n de Tasas",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(LiquidacionTasasConstantes.MSJ_PROP,"msg.err.alta.liquidacion.tasa"), null);
		}else if("0000".equals(codError)){
			numeroLiquidacion=resultado.get("STRING1_CANU");
			ideper=resultado.get("STRING3_CANU");
			codError=PropertiesUtils.getValorConfiguracion(LiquidacionTasasConstantes.MSJ_PROP,"msg.err.ok");
		}else if("0001".equals(codError) ) {
			log.debug("Alta Liq Error 0001",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			ideper=resultado.get("STRING3_CANU");
			estadoValor=resultado.get("STRING4_CANU");
			codError=codError+"; Estado:"+estadoValor;
			log.debug("Alta Liq Fin Error 0001",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}else{
			descError=resultado.get("STRING1_CANU");
			throw new StpawsException(codError+": "+descError, null);
		}
		
		try{			
			String pdf = new String ();
			if("S".equals(lt.getPeticion().getLiquidacion().getNotificacion())){																											
				log.debug("Id. Eper:" + ideper,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				pdf = WebServicesUtils.obtenerDocumento(ideper,"S");				
				log.debug("Pintamos PDF resultante:" + pdf,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);

				if(pdf==null || "".equals(pdf) || pdf.equalsIgnoreCase("KO")){
					codError=PropertiesUtils.getValorConfiguracion(LiquidacionTasasConstantes.MSJ_PROP,"err.gen.pdf");
				}	
				else
				{
					//Notificamos el valor.
					peticion = WebServicesUtils.notificacionValor(ideper);
					xmlOut = WebServicesUtils.wsCall(peticion);
					resultado = WebServicesUtils.wsResponseNotificacionValor(xmlOut);
					if (!resultado.get("STRING1_CANU").equals("00")) //Hubo algún fallo.
					{
						pdf="";
						log.debug("Notificación valor, error:"+resultado.get("STRING2_CANU"),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
						codError = PropertiesUtils.getValorConfiguracion(LiquidacionTasasConstantes.MSJ_PROP,"err.noti.valor");
					}
				}
																
				lt.getRespuesta().setModeloPdf(pdf);
				lt.getRespuesta().setResultado(codError);
			}	
			/* David Incidencia */
			else {									
				lt.getRespuesta().setResultado(codError);																	
			}								
			
		}catch(Exception e){
			log.error(e.getMessage(),e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(LiquidacionTasasConstantes.MSJ_PROP,"msg.err.alta.liquidacion.tasa"), e);
		}
		log.debug("Fin altaLiquidacionTasa(LiquidacionTasas lt,ValidacionPersonas validacionPersonas)",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return lt;
	}
	

	/**
	 * Llamada al WS recuperar liquidaci�n tasas
	 * @param lt
	 * @return
	 * @throws StpawsException
	 */
	private LiquidacionTasas consultaLiquidacionTasa(LiquidacionTasas lt) throws StpawsException{
		log.debug("Ini consultaLiquidacionTasa(LiquidacionTasas lt)",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		String peticion = WebServicesUtils.generarConsultaLiquidacionTasa(lt);					
		
		String xmlOut = WebServicesUtils.wsCall(peticion);
		HashMap<String, String> resultado = WebServicesUtils.wsResponseConsultaLiquidacionTasa(xmlOut,lt);
		String codError=resultado.get("STRING2_CANU");
		String ideper=null;
		String estadoValor=null;
		String descError=null;
		if(codError==null || "".equals(codError)){
			log.error("Error en el retorno del WS Consulta Liquidaci�n de Tasas",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(LiquidacionTasasConstantes.MSJ_PROP,"msg.err.consulta.liq.tasa"), null);
		}else if("0000".equals(codError)){
			ideper=resultado.get("STRING3_CANU");
			codError=PropertiesUtils.getValorConfiguracion(LiquidacionTasasConstantes.MSJ_PROP,"msg.err.ok");
		}else if("0001".equals(codError) ) {
			ideper=resultado.get("STRING3_CANU");
			estadoValor=resultado.get("STRING4_CANU");
			codError=codError+"; Estado:"+estadoValor;
		}else{
			descError=resultado.get("STRING1_CANU");
			throw new StpawsException(codError+": "+descError, null);
		}
				
		try{
				String pdf = new String ();			
				log.debug("Id. Eper:" + ideper,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
				pdf = WebServicesUtils.obtenerDocumento(ideper,"N");							
				log.debug("Pintamos PDF resultante:" + pdf,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);				
				
				if(pdf==null || "".equals(pdf) || pdf.equalsIgnoreCase("KO")){
					codError=PropertiesUtils.getValorConfiguracion(LiquidacionTasasConstantes.MSJ_PROP,"err.gen.pdf");
				}			
							
				lt.getRespuesta().setModeloPdf(pdf);
				lt.getRespuesta().setResultado(codError);			
											
		}catch(Exception e){
			log.error(e.getMessage(),e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(LiquidacionTasasConstantes.MSJ_PROP,"msg.err.consulta.liq.tasa"), e);
		}
		log.debug("Fin consultaLiquidacionTasa(LiquidacionTasas lt)",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return lt;
	}
		
	private String recibosOnline(String ideper)  throws StpawsException{
		log.debug("Ini recibosOnline(String ideper)",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		String peticion = WebServicesUtils.generarRecibosOnline(ideper);
		String xmlOut = WebServicesUtils.wsCall(peticion);
		log.debug("Fin recibosOnline(String ideper)",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return xmlOut;
	}
	
	/**
	 * Tratamiento de los errores en la operaci�n
	 * @param error
	 * @param liquidacionTasas
	 * @return
	 */
	private LiquidacionTasas tratarError(String error, LiquidacionTasas liquidacionTasas){
		LiquidacionTasasRespuesta ltr = new LiquidacionTasasRespuesta();
		if(liquidacionTasas==null) liquidacionTasas = new LiquidacionTasas();
		ltr.setResultado(error);
		liquidacionTasas.setRespuesta(ltr);
		return liquidacionTasas;
	}
	
	/**
	 * Este fichero divide el XML resultante en dos (en los dos XMLs reales)
	 * @param xmlEntradaPdf
	 * @return
	 */
	private String[] dividirXML(String xmlEntradaPdf){
		String[] res = new String[2];
		res[0]=xmlEntradaPdf;
		res[1]=xmlEntradaPdf;
		return res;
	}
	
	private static String getXmlPrueba(){
		String s="<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"+
				"<estructuras>"+
				"  <estruc nombre=\"CANU_CADENAS_NUMEROS\">"+
				  "  <fila>"+
				"      <STRING1_CANU>Consejería de Medio Ambiente y Desarrollo Rural</STRING1_CANU>"+
				"      <STRING2_CANU>Servicio de Produccion y Sanidad Animal</STRING2_CANU>"+
				"      <STRING3_CANU>Tasa por servicios administrativos veterinarios y servicios </STRING3_CANU>"+
				"      <STRING4_CANU>TP</STRING4_CANU>"+
				"      <NUME1_CANU>7</NUME1_CANU>"+
				"      <NUME2_CANU>13</NUME2_CANU>"+
				"      <NUME3_CANU/>"+
				"      <NUME4_CANU/>"+
				"      <FECHA1_CANU/>"+
				"      <FECHA2_CANU/>"+
				"      <FECHA3_CANU/>"+
				"      <FECHA4_CANU/>"+
				"      <STRING5_CANU/>"+
				"    </fila>"+
				"    <fila>"+
				"      <STRING1_CANU>1804-09-113-0163556</STRING1_CANU>"+
				"      <STRING2_CANU>0</STRING2_CANU>"+
				"      <STRING3_CANU>0</STRING3_CANU>"+
				"      <STRING4_CANU>Tasa por servicios administrativos veterinarios y servicios </STRING4_CANU>"+
				"      <NUME1_CANU>0</NUME1_CANU>"+
				"      <NUME2_CANU>0</NUME2_CANU>"+
				"      <NUME3_CANU>0</NUME3_CANU>"+
				"      <NUME4_CANU/>"+
				"      <FECHA1_CANU/>"+
				"      <FECHA2_CANU/>"+
				"      <FECHA3_CANU/>"+
				"      <FECHA4_CANU/>"+
				"      <STRING5_CANU/>"+
				"    </fila>"+
				"  </estruc>"+
				"  <estruc nombre=\"CAOR_CADENA_ORDEN\">"+
"				    <fila>"+
"				      <STRING_CAOR>1804113-11100346600000061663</STRING_CAOR>"+
"				      <ORDEN_CAOR/>"+
"				    </fila>"+
"				  </estruc>"+
"				  <estruc nombre=\"RICA_RECIBO_INTER_CABECERA\">"+
"				    <fila>"+
"				      <NOMBRE_CONTRIB_RICA>SUAREZ NAREDO CELSA NERI</NOMBRE_CONTRIB_RICA>"+
"				      <NIF_CONTRIB_RICA>  9356465L</NIF_CONTRIB_RICA>"+
"				      <CALLE_DIRE>CL DA NORIA 66</CALLE_DIRE>"+
"				      <CALLE_AMPLIACION_DIRE/>"+
"				      <DISTRITO_DIRE>32600</DISTRITO_DIRE>"+
"				      <MUNICIPIO_RICA>VERIN</MUNICIPIO_RICA>"+
"				      <PROVINCIA_RICA>OURENSE</PROVINCIA_RICA>"+
"				      <NOMBRE_ORGA>PRINCIPADO DE ASTURIAS</NOMBRE_ORGA>"+
"				      <DESCR_CONC>TASAS DEL PRINCIPADO</DESCR_CONC>"+
"				      <PERIODO_VALO>No aplicable</PERIODO_VALO>"+
"				      <ANYO_CARGO_CARG>2009</ANYO_CARGO_CARG>"+
"				      <OBJ_TRIB_VALO/>"+
"				      <NUM_FIJO_VALO>1025413195</NUM_FIJO_VALO>"+
"				      <FECHA_INICIO_VOLU>30/11/09</FECHA_INICIO_VOLU>"+
"				      <FECHA_FIN_VOLU>20/01/10</FECHA_FIN_VOLU>"+
"				      <NUMERO_VALO>20093333090TP01L0000092</NUMERO_VALO>"+
"				      <NOMBRE_SP_VALO>SUAREZ NAREDO CELSA NERI</NOMBRE_SP_VALO>"+
"				      <NIF_SP_VALO>  9356465L</NIF_SP_VALO>"+
"				      <REFERENCIA_RICA>871121064</REFERENCIA_RICA>"+
"				      <IDENTIFICACION_RICA>1073090020</IDENTIFICACION_RICA>"+
"				      <IMPORTE_VALO>550</IMPORTE_VALO>"+
"				      <ENTIDAD_VOLU>331004</ENTIDAD_VOLU>"+
"				      <PLAZ_ACTUAL_RICA>0</PLAZ_ACTUAL_RICA>"+
"				      <PLAZOS_PTES_RICA/>"+
"				      <IMPORTE_PTE_RICA/>"+
"				      <FECHA_ALTA_DOMV/>"+
"				      <CCC_DOMV/>"+
"				      <COD_TFVA>TP</COD_TFVA>"+
"				      <PRINCIPAL_RV>550</PRINCIPAL_RV>"+
"				      <PORC_RECARGO_RV>0</PORC_RECARGO_RV>"+
"				      <RECARGO_RV>0</RECARGO_RV>"+
"				      <COSTAS_RV>0</COSTAS_RV>"+
"				      <INTERESES_RV>0</INTERESES_RV>"+
"				      <INGRESOS_RV>0</INGRESOS_RV>"+
"				      <TIPOINFORMEACCESS>LI</TIPOINFORMEACCESS>"+
"				      <DETALLE_1_RICA>NºREF.EXT.:1804113-11100346600000061663             NºLIQ.:1804-09-113-0163556  </DETALLE_1_RICA>"+
"				      <DETALLE_2_RICA>Consejería de Medio Ambiente y Desarroll-Servicio de Produccion y Sanidad Animal</DETALLE_2_RICA>"+
"				      <DETALLE_3_RICA>              Concepto                               NºUnid./Base  Cuota    %IVA</DETALLE_3_RICA>"+
"				      <DETALLE_4_RICA>Tarifa Guia Equinos, bovinos adultos y similares         3,00     3,52         0</DETALLE_4_RICA>"+
"				      <DETALLE_5_RICA>Tarifa Guia ovinos, caprino, porcino, terneros y         3,00     1,98 Min     0</DETALLE_5_RICA>"+
"				      <DETALLE_6_RICA/>"+
"				      <DETALLE_7_RICA/>"+
"				      <DETALLE_8_RICA/>"+
"				      <DETALLE_9_RICA/>"+
"				      <DETALLE_10_RICA>Porcentaje de Bonificación 0       Porcentaje de Recargo 0                      </DETALLE_10_RICA>"+
"				      <DETALLE_11_RICA>Impte.Bonificado Cuota 5,50        Impte.Recargo 0,00        Importe a Ingresar </DETALLE_11_RICA>"+
"				      <DETALLE_12_RICA>Impte.Bonificado IVA 0,00          Impte.Intereses 0,00           5,50          </DETALLE_12_RICA>"+
"				      <DETALLE_13_RICA>OBJ.LIQUIDACIÓN: Fecha 02-02-2009 Guia - Movimiento 32009000008489797           </DETALLE_13_RICA>"+
"				      <DETALLE_14_RICA/>"+
"				      <DETALLE_15_RICA/>"+
"				      <COD_TESV_RICA>V</COD_TESV_RICA>"+
"				      <DESC_FOPA_RICA>Cobros por carga masiva</DESC_FOPA_RICA>"+
"				      <FECHA_COBRO_RICA/>"+
"				      <USCO_RICA>905213310040008711210641073090020000005500</USCO_RICA>"+
"				      <DESCRL_CONC>TASAS DEL PRINCIPADO</DESCRL_CONC>"+
"				      <ACTIV_CONC/>"+
"				      <MUNICIPIO_VALO_RICA/>"+
"				      <TISU_RICA/>"+
"				      <N_FRACCION_VALO/>"+
"				      <TOTALN_FRACCIONES/>"+
"				      <ID_PADRE_VALO/>"+
"				      <MODO_EMIS_RICA>2</MODO_EMIS_RICA>"+
"				      <ID_EPER_RICA/>"+
"				      <DESCUENTO_RV/>"+
"				      <REF_DOMI_RICA>87112104</REF_DOMI_RICA>"+
"				      <NOMBRE_REPR_RICA>SUAREZ NAREDO CELSA NERI</NOMBRE_REPR_RICA>"+
"				      <NIF_REPR_RICA>  9356465L</NIF_REPR_RICA>"+
"				    </fila>"+
"				  </estruc>"+
"				  <estruc nombre=\"CADE_CADENA\">"+
"				    <fila>"+
"				      <STRING_CADE>S</STRING_CADE>"+
"				    </fila>"+
"				    <fila>"+
"				      <STRING_CADE/>"+
"				    </fila>"+
"				  </estruc>"+
"				  <estruc nombre=\"TDCO_TEX_DEPEN_CONCEPTO\">"+
"				    <fila>"+
"				      <POSICION>L1</POSICION>"+
"				      <COD_TEXT_TEXTOS>94</COD_TEXT_TEXTOS>"+
"				    </fila>"+
"				    <fila>"+
"				      <POSICION>L2</POSICION>"+
"				      <COD_TEXT_TEXTOS>95</COD_TEXT_TEXTOS>"+
"				    </fila>"+
"				    <fila>"+
"				      <POSICION>L3</POSICION>"+
"				      <COD_TEXT_TEXTOS>18</COD_TEXT_TEXTOS>"+
"				    </fila>"+
"				    <fila>"+
"				      <POSICION>L8</POSICION>"+
"				      <COD_TEXT_TEXTOS>19</COD_TEXT_TEXTOS>"+
"				    </fila>"+
"				    <fila>"+
"				      <POSICION>L9</POSICION>"+
"				      <COD_TEXT_TEXTOS>20</COD_TEXT_TEXTOS>"+
"				    </fila>"+
"				  </estruc>"+
"				</estructuras>";
		return s;
	}
	

/* 
 * LLama al Web Service que devuelve la carta de pago.
 */
/*
private String obtenerDocumentoPago (String ideper,String notificacion) {		
	
	String documento = new String(); 	
	DocumentoPagoService documentoPagoService = new DocumentoPagoService(); 	
	DocumentoPago documentoPagoPort = documentoPagoService.getDocumentoPagoPort();		
	
	Preferencias pref =  new Preferencias ();
	pref.CargarPreferencias();
	
	com.stpa.ws.server.util.Logger.debug("PREFERENCIAS ENDPOINTDOCUMENTOPAGO ::" + pref.getM_wsdocumentopagoendpoint() ,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
	com.stpa.ws.server.util.Logger.debug("IDEPER::" + ideper ,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
	
	//Se modifica el endpoint
	String endpointDocumentoPago = pref.getM_wsdocumentopagoendpoint();	
	if (!endpointDocumentoPago.equals("")) {		
		BindingProvider bpr = (BindingProvider) documentoPagoPort;	
		bpr.getRequestContext().put(
				BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				endpointDocumentoPago);
	}
	
	try {	
		XMLUtils xmlutils = new XMLUtils();
		if (notificacion=="S") {
			documento = documentoPagoPort.obtenerDocumentoPago(ideper, "NT");			
		}
		else if (notificacion=="N") {
			documento = documentoPagoPort.obtenerDocumentoPago(ideper, null);
		}	

		//Parseamos el XML de la respuesta.		
		String[] columnasARecuperar = new String[] {"resultado"};
		String[] estructurasARecuperar = new String[] {"modelopdf"};
		com.stpa.ws.server.util.Logger.debug("wsResponseAltaLiquidacionTasa(String respuestaWebService, LiquidacionTasas lt) Paso 1",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);		
		Map<String, Object> respuestaAsMap = null;
		
		try {				
			
			//documento = documento.replace(<![CDATA, newChar)
			respuestaAsMap = XMLUtils.compilaXMLDoc(documento, estructurasARecuperar, columnasARecuperar, false);
			
			
	
			documento  = respuestaAsMap.get("modelopdf").toString();																				
		} catch (RemoteException re) {
			com.stpa.ws.server.util.Logger
					.error("Error en wsResponseAltaLiquidacionTasa(String respuestaWebService, LiquidacionTasas lt): "+ re.getMessage(), re,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(
					"Error en XML de respuesta del alta de liquidaci�n.", re);
		}
								
	}catch (Exception e) {
		com.stpa.ws.server.util.Logger.error("No se ha podido realizar la llamada al Web Service - ObtenerDocumentoPago :"+e.getMessage()+ " :Id. Eper" + ideper,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return "";
	}
			
	return documento;
}
*/	
	
}
