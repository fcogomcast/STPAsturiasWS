package com.stpa.ws.server.validation;

import java.util.Calendar;
import java.util.HashMap;

import com.stpa.ws.pref.lt.Preferencias;
import com.stpa.ws.server.base.IStpawsValidation;
import com.stpa.ws.server.bean.LiquidacionTasas;
import com.stpa.ws.server.bean.LiquidacionTasasDetalleLiquidacion;
import com.stpa.ws.server.bean.LiquidacionTasasPeticion;
import com.stpa.ws.server.bean.Mac;
import com.stpa.ws.server.bean.ValidacionPersonas;
import com.stpa.ws.server.constantes.LiquidacionTasasConstantes;
import com.stpa.ws.server.exception.StpawsException;
import com.stpa.ws.server.util.Logger;
import com.stpa.ws.server.util.PropertiesUtils;
import com.stpa.ws.server.util.StpawsUtil;
import com.stpa.ws.server.util.WebServicesUtils;


public class LiquidacionTasasValidation implements IStpawsValidation {

	Logger log=null;
	String idLlamada;
	public LiquidacionTasasValidation(String idLlamada)
	{
		log = new Logger(idLlamada);
		this.idLlamada=idLlamada;
	}
	@Override
	public boolean isValid(Object param) throws StpawsException {
		log.debug("Ini isValid(Object param)",
				com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		Object[] obj = (Object[]) param;
		boolean isValid = true;
		LiquidacionTasasPeticion ltp = ((LiquidacionTasas) obj[0])
				.getPeticion();
		Mac mac = ((LiquidacionTasas) obj[0]).getMac();
		Preferencias  pref = new Preferencias ();		
		try {
			pref.CargarPreferencias();								
		} catch (Exception e) {
			log.debug("Error al cargar preferencias al validar la liquidación. "+e.getMessage(),com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		}
		if (pref.getM_validarMAC().equals("S"))
		{
			// Validacion MAC
			if (mac != null) {
				if (mac.getMac() == null || "".equals(mac.getMac())) {
					throw new StpawsException(PropertiesUtils
							.getValorConfiguracion(
									LiquidacionTasasConstantes.MSJ_PROP,
									"msg.peti.mac.vacio"), null);
				} else {
					boolean isMacValid = StpawsUtil.isMacLiquidacionTasasValid(ltp,
							mac.getMac(),idLlamada);
					if (!isMacValid) {
						throw new StpawsException(PropertiesUtils
								.getValorConfiguracion(
										LiquidacionTasasConstantes.MSJ_PROP,
										"msg.peti.mac.no.valid"), null);
					}
				}
			} else {
				throw new StpawsException(PropertiesUtils.getValorConfiguracion(
						LiquidacionTasasConstantes.MSJ_PROP, "msg.peti.mac.vacio"),
						null);
			}
		}
		if (ltp != null) {
			// Validacion Cliente
			if (!isValidString(ltp.getCliente(), 0)) {
				throw new StpawsException(PropertiesUtils
						.getValorConfiguracion(
								LiquidacionTasasConstantes.MSJ_PROP,
								"msg.peti.client.vacio"), null);
			}

			// Validacion Operacion
			if (!isValidString(ltp.getOperacion(), 1)) {
				throw new StpawsException(PropertiesUtils
						.getValorConfiguracion(
								LiquidacionTasasConstantes.MSJ_PROP,
								"msg.operacion.no.valid"), null);
			}
			if (!LiquidacionTasasConstantes.GENERACION_LIQUIDACION_TASA
					.equals(ltp.getOperacion())
					&& !LiquidacionTasasConstantes.CONSULTA_LIQUIDACION_TASA
							.equals(ltp.getOperacion())
					&& !LiquidacionTasasConstantes.RECHAZAR_NOTIFICACION
							.equals(ltp.getOperacion())) {
				throw new StpawsException(PropertiesUtils
						.getValorConfiguracion(
								LiquidacionTasasConstantes.MSJ_PROP,
								"msg.operacion.no.valid"), null);
			}

			// Validaciones dependiendo de la operacion
			if (LiquidacionTasasConstantes.GENERACION_LIQUIDACION_TASA
					.equals(ltp.getOperacion())) {
				validacionAlta(ltp);
			} else {
				validacionConsultaYRechazar(ltp);
			}

		} else {
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(
					LiquidacionTasasConstantes.MSJ_PROP, "msg.peti.vacia"),
					null);
		}
		log.debug("Fin isValid(Object param)",
				com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return isValid;
	}

	/**
	 * Validacion de campos requeridos para Alta
	 * 
	 * @param ltp
	 * @throws StpawsException
	 */
	private void validacionAlta(LiquidacionTasasPeticion ltp)
			throws StpawsException {
		log.debug(
				"Ini validacionAlta(LiquidacionTasasPeticion ltp)",
				com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);

		// Validacion Numero unico
		if (!isValidString(ltp.getNumeroUnico(), 40)) {// 20
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(
					LiquidacionTasasConstantes.MSJ_PROP,
					"msg.num.unico.no.valid"), null);
		}

		// Validacion Liquidacion
		if (ltp.getLiquidacion() != null) {

			// Validacion Notificacion
			if (!isValidString(ltp.getLiquidacion().getNotificacion(), 1)) {
				throw new StpawsException(PropertiesUtils
						.getValorConfiguracion(
								LiquidacionTasasConstantes.MSJ_PROP,
								"msg.notificacion.no.valid"), null);
			}
			if (!"S".equals(ltp.getLiquidacion().getNotificacion())
					&& !"N".equals(ltp.getLiquidacion().getNotificacion())) {
				throw new StpawsException(PropertiesUtils
						.getValorConfiguracion(
								LiquidacionTasasConstantes.MSJ_PROP,
								"msg.notificacion.no.valid"), null);
			}

			// Validacion Centro Gestor
			if (!isValidString(ltp.getLiquidacion().getCentroGestor(), 4)) {
				throw new StpawsException(PropertiesUtils
						.getValorConfiguracion(
								LiquidacionTasasConstantes.MSJ_PROP,
								"msg.centro.gestor.no.valid"), null);
			}

			// Validacion Codigo Tasa
			if (!isValidString(ltp.getLiquidacion().getCodTasa(), 5)) {
				throw new StpawsException(PropertiesUtils
						.getValorConfiguracion(
								LiquidacionTasasConstantes.MSJ_PROP,
								"msg.cod.tasa.no.valid"), null);
			}

			// Validacion Fecha Devengo
			if (!isValidString(ltp.getLiquidacion().getFechaDevengo(), 0)
					|| ltp.getLiquidacion().getFechaDevengo().length() != 8) {
				throw new StpawsException(PropertiesUtils
						.getValorConfiguracion(
								LiquidacionTasasConstantes.MSJ_PROP,
								"msg.fecha.devengo.no.valid"), null);
			}
			if (!isFecha(ltp.getLiquidacion().getFechaDevengo())) {
				throw new StpawsException(PropertiesUtils
						.getValorConfiguracion(
								LiquidacionTasasConstantes.MSJ_PROP,
								"msg.fecha.devengo.no.valid"), null);
			}

			int fechaDevengo = Integer.parseInt(ltp.getLiquidacion()
					.getFechaDevengo());
			int fechaProceso = getFechaActual();
			if (fechaDevengo > fechaProceso) {
				throw new StpawsException(PropertiesUtils
						.getValorConfiguracion(
								LiquidacionTasasConstantes.MSJ_PROP,
								"msg.fecha.devengo.mayor.fecha.actual"), null);
			}

			// Detalle Liquidacion
			if (ltp.getLiquidacion().getDetalleLiquidacion() != null) {

				LiquidacionTasasDetalleLiquidacion[] detallesLiquidacion = ltp
						.getLiquidacion().getDetalleLiquidacion();

				for (int i = 0; i < detallesLiquidacion.length; i++) {
					if (detallesLiquidacion[i] != null) {
						// id
						if (!isValidString(detallesLiquidacion[i].getId(), 1)) {

							throw new StpawsException(
									PropertiesUtils
											.getValorConfiguracion(
													LiquidacionTasasConstantes.MSJ_PROP,
													"msg.id.no.valid"), null);
						}

						if (!"1".equals(detallesLiquidacion[i].getId())
								&& !"2".equals(detallesLiquidacion[i].getId())
								&& !"3".equals(detallesLiquidacion[i].getId())
								&& !"4".equals(detallesLiquidacion[i].getId())
								&& !"5".equals(detallesLiquidacion[i].getId())) {

							throw new StpawsException(
									PropertiesUtils
											.getValorConfiguracion(
													LiquidacionTasasConstantes.MSJ_PROP,
													"msg.id.no.valid"), null);
						}

						log.debug(
										"VALIDA::  Detalle Liquidacion(Codigo Tarifa):",
										com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
						// Codigo Tarifa
						if (!isValidString(detallesLiquidacion[i]
								.getCodTarifa(), 10)) {
							throw new StpawsException(
									PropertiesUtils
											.getValorConfiguracion(
													LiquidacionTasasConstantes.MSJ_PROP,
													"msg.cod.tarifa.no.valid"),
									null);
						}

						// Nomero Unidades
						if (!isValidString(detallesLiquidacion[i]
								.getNumUnidades(), 10)) {
							throw new StpawsException(
									PropertiesUtils
											.getValorConfiguracion(
													LiquidacionTasasConstantes.MSJ_PROP,
													"msg.num.unidades.no.valid"),
									null);
						}
						if (!isNumerico(detallesLiquidacion[i].getNumUnidades())) {
							throw new StpawsException(
									PropertiesUtils
											.getValorConfiguracion(
													LiquidacionTasasConstantes.MSJ_PROP,
													"msg.num.unidades.no.valid"),
									null);
						}
					} else {

						throw new StpawsException(PropertiesUtils
								.getValorConfiguracion(
										LiquidacionTasasConstantes.MSJ_PROP,
										"msg.detalle.liquidacion.no.valid"),
								null);
					}
				}
			} else {

				throw new StpawsException(PropertiesUtils
						.getValorConfiguracion(
								LiquidacionTasasConstantes.MSJ_PROP,
								"msg.detalle.liquidacion.no.valid"), null);
			}

			// Porcentaje Bonificacion
			if (isValidString(ltp.getLiquidacion().getPorcBonificacion(), 0)) {
				if (!isNumerico(ltp.getLiquidacion().getPorcBonificacion())) {
					throw new StpawsException(PropertiesUtils
							.getValorConfiguracion(
									LiquidacionTasasConstantes.MSJ_PROP,
									"msg.porc.bonificacion.no.valid"), null);
				}
			}

			// Porcentaje Recargo
			if (isValidString(ltp.getLiquidacion().getPorcRecargo(), 0)) {
				if (!isNumerico(ltp.getLiquidacion().getPorcRecargo())) {
					throw new StpawsException(PropertiesUtils
							.getValorConfiguracion(
									LiquidacionTasasConstantes.MSJ_PROP,
									"msg.porc.recargo.no.valid"), null);
				}
			}

			// Importe Recargo
			if (isValidString(ltp.getLiquidacion().getImpRecargo(), 0)) {
				if (!isNumerico(ltp.getLiquidacion().getImpRecargo())) {
					throw new StpawsException(PropertiesUtils
							.getValorConfiguracion(
									LiquidacionTasasConstantes.MSJ_PROP,
									"msg.imp.recargo.no.valid"), null);
				}
			}

			// Importe Intereses
			if (isValidString(ltp.getLiquidacion().getImpIntereses(), 0)) {
				if (!isNumerico(ltp.getLiquidacion().getImpIntereses())) {
					throw new StpawsException(PropertiesUtils
							.getValorConfiguracion(
									LiquidacionTasasConstantes.MSJ_PROP,
									"msg.imp.intereses.no.valid"), null);
				}
			}

			// Datos SP
			if (ltp.getLiquidacion().getDatosSP() != null) {
				// Id Fiscal SP
				if (!isValidString(ltp.getLiquidacion().getDatosSP()
						.getIdFiscal(), 9)) {
					throw new StpawsException(PropertiesUtils
							.getValorConfiguracion(
									LiquidacionTasasConstantes.MSJ_PROP,
									"msg.id.fiscal.no.valid"), null);
				}

				// Nombre y Apellidos SP
				if (!isValidString(ltp.getLiquidacion().getDatosSP()
						.getNombreApellidos(), 60)) {
					throw new StpawsException(PropertiesUtils
							.getValorConfiguracion(
									LiquidacionTasasConstantes.MSJ_PROP,
									"msg.nombre.apellidos.no.valid"), null);
				}
			} else {
				throw new StpawsException(PropertiesUtils
						.getValorConfiguracion(
								LiquidacionTasasConstantes.MSJ_PROP,
								"msg.datos.sp.no.valid"), null);
			}

			if (isValidString(ltp.getLiquidacion().getDatosPresentador()
					.getIdFiscal(), 0)) {
				if (!isValidString(ltp.getLiquidacion().getDatosPresentador()
						.getNombreApellidos(), 0)) {
					throw new StpawsException(PropertiesUtils
							.getValorConfiguracion(
									LiquidacionTasasConstantes.MSJ_PROP,
									"msg.nombre.apellidos.pr.no.valid"), null);
				}
			}

			// Exp Externo
			// if(!isValidString(ltp.getLiquidacion().getExpExterno(),40)){
			if (ltp.getLiquidacion().getExpExterno() != null) {
				if (ltp.getLiquidacion().getExpExterno().length() > 40) {
					throw new StpawsException(PropertiesUtils
							.getValorConfiguracion(
									LiquidacionTasasConstantes.MSJ_PROP,
									"msg.exp.externo.no.valid"), null);
				}
			}

			// Exp Gestion
			if (ltp.getLiquidacion().getExpGestion() != null) {
				if (ltp.getLiquidacion().getExpGestion().length() > 14) {
					throw new StpawsException(PropertiesUtils
							.getValorConfiguracion(
									LiquidacionTasasConstantes.MSJ_PROP,
									"msg.exp.gestion.no.valid"), null);
				}
			}

			// Periodo
			if (!"00".equals(ltp.getLiquidacion().getPeriodo())
					&& !"01".equals(ltp.getLiquidacion().getPeriodo())
					&& !"02".equals(ltp.getLiquidacion().getPeriodo())
					&& !"03".equals(ltp.getLiquidacion().getPeriodo())
					&& !"04".equals(ltp.getLiquidacion().getPeriodo())
					&& !"05".equals(ltp.getLiquidacion().getPeriodo())
					&& !"06".equals(ltp.getLiquidacion().getPeriodo())
					&& !"07".equals(ltp.getLiquidacion().getPeriodo())
					&& !"08".equals(ltp.getLiquidacion().getPeriodo())
					&& !"09".equals(ltp.getLiquidacion().getPeriodo())
					&& !"10".equals(ltp.getLiquidacion().getPeriodo())
					&& !"11".equals(ltp.getLiquidacion().getPeriodo())
					&& !"12".equals(ltp.getLiquidacion().getPeriodo())
					&& !"1T".equals(ltp.getLiquidacion().getPeriodo())
					&& !"2T".equals(ltp.getLiquidacion().getPeriodo())
					&& !"3T".equals(ltp.getLiquidacion().getPeriodo())
					&& !"4T".equals(ltp.getLiquidacion().getPeriodo())
					&& !"1S".equals(ltp.getLiquidacion().getPeriodo())
					&& !"2S".equals(ltp.getLiquidacion().getPeriodo())
					&& !"1A".equals(ltp.getLiquidacion().getPeriodo())) {

				throw new StpawsException(PropertiesUtils
						.getValorConfiguracion(
								LiquidacionTasasConstantes.MSJ_PROP,
								"msg.liquidacion.periodo.no.valid"), null);
			}

			// Fecha acuerdo
			if (isValidString(ltp.getLiquidacion().getFechaAcuerdo(), 0)) {
				if (!isFecha(ltp.getLiquidacion().getFechaAcuerdo())) {
					throw new StpawsException(PropertiesUtils
							.getValorConfiguracion(
									LiquidacionTasasConstantes.MSJ_PROP,
									"msg.fecha.acuerdo.no.valid"), null);
				}
			}

			// Centro Gestor
			if (!isValidString(ltp.getLiquidacion().getCentroGestor(), 4)) {
				throw new StpawsException(PropertiesUtils
						.getValorConfiguracion(
								LiquidacionTasasConstantes.MSJ_PROP,
								"msg.centro.gestor.no.valid"), null);
			}

			// cod tasa
			if (!isValidString(ltp.getLiquidacion().getCodTasa(), 5)) {
				throw new StpawsException(PropertiesUtils
						.getValorConfiguracion(
								LiquidacionTasasConstantes.MSJ_PROP,
								"msg.cod.tasa.no.valid"), null);
			}

			// Periodo
			if (!isValidString(ltp.getLiquidacion().getPeriodo(), 2)) {
				throw new StpawsException(PropertiesUtils
						.getValorConfiguracion(
								LiquidacionTasasConstantes.MSJ_PROP,
								"msg.periodo.no.valid"), null);
			}
		} else {
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(
					LiquidacionTasasConstantes.MSJ_PROP,
					"msg.liquidacion.no.valid"), null);
		}
		log.debug(
				"Fin validacionAlta(LiquidacionTasasPeticion ltp)",
				com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
	}

	/**
	 * Se comprueba que el String que llega por parometro no es null ni "" y si
	 * el tam pasado es >0, se comprueba que el tamaoo del String no sea
	 * superior a este
	 * 
	 * @param s
	 * @param tam
	 * @return
	 */
	private boolean isValidString(String s, int maxTam) {
		boolean ok = true;
		if (s == null) {
			ok = false;
		} else if ("".equals(s)) {
			ok = false;
		} else if (maxTam > 0 && s.length() > maxTam) {
			ok = false;
		}
		return ok;
	}

	/**
	 * Devuelve la fecha en formato AAAAMMDD
	 * 
	 * @return
	 */
	private int getFechaActual() {
		Calendar cal = Calendar.getInstance();
		String ano = "" + cal.get(Calendar.YEAR);
		String mes = "" + (cal.get(Calendar.MONTH) + 1);
		if (mes.length() == 1) {
			mes = "0" + mes;
		}
		String dia = "" + cal.get(Calendar.DAY_OF_MONTH);
		if (dia.length() == 1) {
			dia = "0" + dia;
		}
		return Integer.parseInt(ano + mes + dia);
	}

	/**
	 * Validacion campos requeridos para consulta y rechazar
	 * 
	 * @param ltp
	 * @throws StpawsException
	 */
	private void validacionConsultaYRechazar(LiquidacionTasasPeticion ltp)
			throws StpawsException {
		log.debug(
						"Ini validacionConsultaYRechazar(LiquidacionTasasPeticion ltp)",
						com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		// Validacion Nomero onico
		if (!isValidString(ltp.getNumeroUnico(), 40)) {// 20
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(
					LiquidacionTasasConstantes.MSJ_PROP,
					"msg.num.unico.no.valid"), null);
		}

		// Validacion Liquidacion
		if (ltp.getLiquidacion() != null) {

			// Validacion Centro Gestor
			if (!isValidString(ltp.getLiquidacion().getCentroGestor(), 4)) {
				throw new StpawsException(PropertiesUtils
						.getValorConfiguracion(
								LiquidacionTasasConstantes.MSJ_PROP,
								"msg.centro.gestor.no.valid"), null);
			}

			// Validacion Codigo Tasa
			if (!isValidString(ltp.getLiquidacion().getCodTasa(), 5)) {
				throw new StpawsException(PropertiesUtils
						.getValorConfiguracion(
								LiquidacionTasasConstantes.MSJ_PROP,
								"msg.cod.tasa.no.valid"), null);
			}
		}
		log.debug(
						"Fin validacionConsultaYRechazar(LiquidacionTasasPeticion ltp)",
						com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
	}

	/**
	 * Retorna si un string llegado por parometro es numorico o no
	 * 
	 * @param numero
	 * @return
	 */
	private boolean isNumerico(String numero) {
		try {
			Long.parseLong(numero);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Validacion de fecha con formato AAAAMMDD
	 * 
	 * @param fechaAcuerdo
	 * @return
	 */
	private boolean isFecha(String fecha) {
		int ano = 0;
		int mes = 0;
		int dia = 0;
		try {
			ano = Integer.parseInt(fecha.substring(0, 4));
			mes = Integer.parseInt(fecha.substring(4, 6));
			dia = Integer.parseInt(fecha.substring(6, 8));
			return validarFecha(dia, mes, ano);
		} catch (Exception e) {
			// Si la fecha es null, no contiene 8 caracteres numoricos...
			return false;
		}
	}

	/**
	 * calcula si es una fecha volida
	 * 
	 * @param dia
	 * @param mes
	 * @param ano
	 * @return
	 */
	private boolean validarFecha(int dia, int mes, int ano) {
		boolean valida = false;
		if (dia < 0 || dia > 31 || mes < 0 || mes > 12) {
			valida = false;
		} else {
			if ((mes == 4 || mes == 6 || mes == 9 || mes == 11) && dia > 30) {
				valida = false;
			} else {
				if (mes == 2 && dia > ultDiaFeb(ano)) {
					valida = false;
				} else {
					valida = true;
				}
			}
		}
		return valida;
	}

	/**
	 * Obtiene el ultimo doa de febrero segun el aoo introducido por parometro
	 * 
	 * @param ano
	 * @return
	 */
	private int ultDiaFeb(int ano) {
		if (isBisiesto(ano)) {
			return 29;
		} else {
			return 28;
		}
	}

	/**
	 * Colcula si el aoo es bisiesto
	 * 
	 * @param ano
	 * @return
	 */
	private boolean isBisiesto(int ano) {
		boolean bisiesto = false;
		if (ano % 4 != 0) {
			bisiesto = false;
		} else if (ano % 400 == 0) {
			bisiesto = true;
		} else if (ano % 100 == 0) {
			bisiesto = false;
		} else {
			bisiesto = true;
		}
		return bisiesto;
	}

	public String consultaPersona(LiquidacionTasas lt, String tipo) throws StpawsException {
		 
		String peticion = WebServicesUtils.generarValidacionPersonas(lt);
		String xmlOut = WebServicesUtils.wsCall(peticion);
		HashMap<String, String> resultado = WebServicesUtils.wsResponseValidarPersona(xmlOut, lt);
		String res = resultado.get("STRING1_CANU");
		String idPers="";
		
		try {
			if (res == null || "".equals(res) || "0000".equals(res)) {
				if (tipo == "SP") {
					idPers = resultado.get("NUME1_CANU"); // Id Pers Sujeto Pasivo
				} else if (tipo == "PR") {
					idPers = resultado.get("NUME2_CANU"); // Id. Pers Presentador
				}
			} else {
				idPers = null;
			}
		}
		catch(Exception e){
			  log.error(e.getMessage(),e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG); 
			  throw new StpawsException(PropertiesUtils.getValorConfiguracion(LiquidacionTasasConstantes.MSJ_PROP,"msg.err.validar.persona"),e);
		}
		
		return idPers;
	}

	public ValidacionPersonas validarPersona(LiquidacionTasas lt)
			throws StpawsException {

		ValidacionPersonas validacionPersonas = new ValidacionPersonas();

		//===========================
		// Consultamos SUJETO PASIVO
		//===========================		
		String persSP = consultaPersona(lt, "SP");

		// Variables SP
		String nifsp = "";
		String nombresp = "";	

		// Variables PR
		String nifpr = "";
		String nombrepr = "";

		//C.R.V. 26/09/2011. La validación en Tributas es la única que se hace.
		//Ya incluye la validación contra la AEAT.
		//Ahora sólo comprobamos que lo que devuelve de TRIBUTAS es un id pers válido.
		if (persSP== null || "".equals(persSP))
		{
			nifsp = lt.getPeticion().getLiquidacion().getDatosSP().getIdFiscal();
			nombresp = lt.getPeticion().getLiquidacion().getDatosSP().getNombreApellidos();
			log.error("0012: El Sujeto pasivo no está dado de alta",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(LiquidacionTasasConstantes.MSJ_PROP,"msg.err.validar.person.no.valid")+ ";" + nifsp + " " + nombresp, null);
		}
		//===========================
		// Consultamos PRESENTADOR
		//===========================		
		String persPR = consultaPersona(lt, "PR");
		//C.R.V. 26/09/2011. La validación en Tributas es la única que se hace.
		//Ya incluye la validación contra la AEAT.
		//Ahora sólo comprobamos que lo que devuelve de TRIBUTAS es un id pers válido.
		if (persPR== null || "".equals(persPR))
		{
			nifpr = lt.getPeticion().getLiquidacion().getDatosPresentador().getIdFiscal();
			nombrepr = lt.getPeticion().getLiquidacion().getDatosPresentador().getNombreApellidos();
			log.error("0012: El Presentador no está dado de alta ",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
			throw new StpawsException(PropertiesUtils.getValorConfiguracion(LiquidacionTasasConstantes.MSJ_PROP,"msg.err.validar.person.no.valid")+ ";" + nifpr + " " + nombrepr, null);
		}
		//Volcamos el valor del ID Pers recuperado.
		validacionPersonas.setPersSP(persSP);
		validacionPersonas.setPersPR(persPR);

		log.debug("ID.PERS SUJETO PASIVO:"+persSP ,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		log.debug("ID.PERS PRESENTADOR:"+persPR , com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		
		
		log.debug("Fin validarPersona(LiquidacionTasas lt)",com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return validacionPersonas;
	}

}
