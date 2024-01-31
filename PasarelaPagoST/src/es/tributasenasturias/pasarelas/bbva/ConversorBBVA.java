package es.tributasenasturias.pasarelas.bbva;

import java.text.ParseException;


import es.tributasenasturias.client.pasarela.bbva.ANULACIONIN;
import es.tributasenasturias.client.pasarela.bbva.CONSULTACOBROSIN;
import es.tributasenasturias.client.pasarela.bbva.PAGOIN;
import es.tributasenasturias.conversor.ConversorRespuestaBBVA;
import es.tributasenasturias.conversor.MensajesBBVA;
import es.tributasenasturias.dao.DatosProceso;
import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.exceptions.ConversionExcepcion;
import es.tributasenasturias.pasarelas.comunicacion.DatosComunicacion;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.utils.Varios;

/**
 * Convierte la entrada de servicio a una entrada de BBVA, y la salida de
 * BBVA a una salida de servicio.
 * 
 * @author crubencvs
 * 
 */
public final class ConversorBBVA {
	
	private static final String MASCARA_ANIO_MES_DIA = "yyyy-MM-dd";
	private static final String MASCARA_ANIO_MES = "yyyy-MM";
	private ConversorBBVA(){}
	/**
	 * Crea un mensaje de pago a BBVA en funci�n de los datos de entrada de
	 * 
	 * la petici�n.
	 * 
	 * @param datosProceso
	 * @return
	 * @throws ConversionException
	 */
	public static PAGOIN peticionABBVA(DatosProceso datosProceso, PreferenciasBBVA pref)
			throws ConversionExcepcion {
		DatosEntradaServicio peticionServicio= datosProceso.getPeticionServicio();
		PAGOIN pago = new PAGOIN();
		String nifFinal = "";
		String nifOperanteFinal = "";
		Varios util = new Varios();
		nifFinal = util.formateaNIF(peticionServicio.getNifContribuyente());
		nifOperanteFinal = util.formateaNIF(peticionServicio.getNifOperante());
		try {
			if ("".equals(nifFinal)) {
				nifFinal = nifOperanteFinal;
			}
			pago.setENTIDAD(pref.getEntidad());
			pago.setIP(pref.getIpOrigen());
			pago.setMODALIDAD(peticionServicio.getModalidad());
			pago.setIDENSP(nifFinal);
			pago.setEMISORA(peticionServicio.getEmisora());
			pago.setIMPORTERECIBO(peticionServicio.getImporte());
			pago.setNUMEXPEDIENTE(peticionServicio.getExpediente());
			pago.setCVVTARJETA(""); // Por el momento no se usa.
			if (!"".equals(peticionServicio.getFechaDevengo())) {
				pago.setFECHADEVENGO(Varios.getDateFormatted(peticionServicio
						.getFechaDevengo(), "ddMMyyyy", MASCARA_ANIO_MES_DIA));
			} else {
				pago.setFECHADEVENGO("");
			}
			
			if (!"".equals(peticionServicio.getTarjeta())) {
				pago.setTARJETA(peticionServicio.getTarjeta());
				pago.setCUENTA("");
			} else {
				pago.setTARJETA("");
				pago.setCUENTA(peticionServicio.getCcc());
			}
			if (!"".equals(peticionServicio.getFechaCaducidadTarjeta())) {
				pago.setFECCADTARJETA(Varios.getDateFormatted(peticionServicio
						.getFechaCaducidadTarjeta(), "MMyy", MASCARA_ANIO_MES));
			} else {
				pago.setFECCADTARJETA("");
			}
			switch (datosProceso.getTipoLlamada())
			{
			case SERVICIO_WEB_AUTOLIQUIDACION:
			case PORTAL_AUTOLIQUIDACION:
			case S1_AUTOLIQUIDACION:
				pago.setIDENTIFICACION("");
				pago.setREFERENCIA(datosProceso.getJustificante());
				break;
			case PORTAL_LIQUIDACION:
			case S1_LIQUIDACION:
				pago.setIDENTIFICACION(peticionServicio.getIdentificacion());
				pago.setREFERENCIA(peticionServicio.getReferencia());
				break;
			}
			pago.setDATOESPECIFICO(Varios.extraeDatoEspecifico(peticionServicio.getDatoEspecifico(),pago.getREFERENCIA()));
			pago.setIDENOPE(nifOperanteFinal);
		} catch (ParseException e) {
			throw new ConversionExcepcion(
					"Error de conversi�n de fecha en mensaje de BBVA:"
							+ e.getMessage(), e);
		} catch (Exception e) {
			throw new ConversionExcepcion(
					"Error de conversi�n a mensaje de BBVA:"
							+ e.getMessage(), e);
		}
		return pago;
	}
	/**
	 * Crea un mensaje de consulta de cobro a BBVA. 
	 * @param datosProceso Datos del proceso, incluir� la petici�n al servicio web.
	 * @return
	 * @throws ConversionExcepcion
	 */
	public static CONSULTACOBROSIN consultaABBVA(DatosProceso datosProceso, PreferenciasBBVA pref)
			throws ConversionExcepcion {
		CONSULTACOBROSIN consulta = new CONSULTACOBROSIN();
		DatosEntradaServicio peticionServicio= datosProceso.getPeticionServicio();
		
		String nifFinal = "";
		String nifOperanteFinal = "";
		Varios util = new Varios();
		nifFinal = util.formateaNIF(datosProceso.getNifContribuyente());
		nifOperanteFinal = util.formateaNIF(datosProceso.getNifOperante());
		try {
			if ("".equals(nifFinal)) {
				nifFinal = nifOperanteFinal;
			}
			consulta.setENTIDAD(pref.getEntidad());
			consulta.setIP(pref.getIpOrigen());
			consulta.setMODALIDAD(peticionServicio.getModalidad());
			consulta.setIDENSP(nifFinal);
			consulta.setEMISORA(peticionServicio.getEmisora());
			consulta.setIMPORTERECIBO(datosProceso.getImporte());
			consulta.setNUMEXPEDIENTE(datosProceso.getExpediente());
			consulta.setCVVTARJETA(""); // Por el momento no se usa.
			consulta.setCUENTA("");
			consulta.setTARJETA("");
			consulta.setFECCADTARJETA("");
			String fechaDevengo = datosProceso.getFechaDevengo();
			if (fechaDevengo!=null && !"".equals(fechaDevengo)) {
				consulta.setFECHADEVENGO(Varios.getDateFormatted(fechaDevengo, "dd/MM/yyyy", MASCARA_ANIO_MES_DIA));
			} else {
				consulta.setFECHADEVENGO("");
			}
			switch (datosProceso.getTipoLlamada())
			{
			case SERVICIO_WEB_AUTOLIQUIDACION:
			case PORTAL_AUTOLIQUIDACION:
			case S1_AUTOLIQUIDACION:
				consulta.setIDENTIFICACION("");
				//Dependiendo de si el justificante se ha generado o no, estar� en los datos de entrada.
				if (!"".equals(peticionServicio.getJustificante()))
				{
					consulta.setREFERENCIA(peticionServicio.getJustificante());
				}
				else
				{
					consulta.setREFERENCIA(datosProceso.getJustificante());
				}
				break;
			case PORTAL_LIQUIDACION:
			case S1_LIQUIDACION:
				consulta.setIDENTIFICACION(peticionServicio.getIdentificacion());
				consulta.setREFERENCIA(peticionServicio.getReferencia());
				break;
			}
			consulta.setDATOESPECIFICO(Varios.extraeDatoEspecifico(datosProceso.getDatoEspecifico(),consulta.getREFERENCIA()));
			consulta.setIDENOPE(nifOperanteFinal);
		} catch (ParseException e) {
			throw new ConversionExcepcion(
					"Error de conversi�n de fecha en mensaje de BBVA:"
							+ e.getMessage(), e);
		} catch (Exception e) {
			throw new ConversionExcepcion(
					"Error de conversi�n a mensaje de BBVA:"
							+ e.getMessage(), e);
		}
		return consulta;
	}

	/**
	 * Crea un mensaje de anulaci�n a BBVA en funci�n de los datos de entrada de
	 * la petici�n.
	 * 
	 * @param datosProceso  Datos del proceso. Incluir� los datos de la operaci�n.
	 * @return ANULACION 
	 * @throws ConversionException
	 */
	public static ANULACIONIN anulacionABBVA(DatosProceso datosProceso, PreferenciasBBVA pref)
			throws ConversionExcepcion {
		DatosEntradaServicio peticionServicio= datosProceso.getPeticionServicio();
		ANULACIONIN anulacion = new ANULACIONIN();
		String nifFinal = "";
		String nifOperanteFinal = "";
		Varios util = new Varios();
		nifFinal = util.formateaNIF(peticionServicio.getNifContribuyente());
		nifOperanteFinal = util.formateaNIF(peticionServicio.getNifOperante());
		try {
			if ("".equals(nifFinal)) {
				nifFinal = nifOperanteFinal;
			}
			anulacion.setENTIDAD(pref.getEntidad());
			anulacion.setIP(pref.getIpOrigen());
			anulacion.setMODALIDAD(peticionServicio.getModalidad());
			anulacion.setIDENSP(nifFinal);
			anulacion.setEMISORA(peticionServicio.getEmisora());
			anulacion.setIMPORTERECIBO(peticionServicio.getImporte());
			anulacion.setNUMOPER(datosProceso.getNumeroOperacion());
			if (!"".equals(peticionServicio.getFechaDevengo())) {
				anulacion.setFECHADEVENGO(Varios.getDateFormatted(peticionServicio
						.getFechaDevengo(), "ddMMyyyy", MASCARA_ANIO_MES_DIA));
			} else {
				anulacion.setFECHADEVENGO("");
			}
			switch (datosProceso.getTipoLlamada())
			{
			case SERVICIO_WEB_AUTOLIQUIDACION:
			case PORTAL_AUTOLIQUIDACION:
			case S1_AUTOLIQUIDACION:
				anulacion.setIDENTIFICACION("");
				anulacion.setREFERENCIA(peticionServicio.getJustificante());
				break;
			case PORTAL_LIQUIDACION:
			case S1_LIQUIDACION:
				anulacion.setIDENTIFICACION(peticionServicio.getIdentificacion());
				anulacion.setREFERENCIA(peticionServicio.getReferencia());
				break;
			}
			anulacion.setDATOESPECIFICO(Varios.extraeDatoEspecifico(peticionServicio.getDatoEspecifico(), anulacion.getREFERENCIA()));
		} catch (ParseException e) {
			throw new ConversionExcepcion(
					"Error de conversi�n de fecha en mensaje de BBVA:"
							+ e.getMessage(), e);
		} catch (Exception e) {
			throw new ConversionExcepcion(
					"Error de conversi�n a mensaje de BBVA:"
							+ e.getMessage(), e);
		}
		return anulacion;
	}
	/**
	 * Convierte la respuesta de BBVA a c�digo de respuesta del servicio, utilizando el fichero de mapeo de errores.
	 * @param datosComunicacion Datos de la comunicaci�n con BBVA
	 * @param resultadoPorDefecto C�digo de resultado si no se encuentra en el mapeo de mensajes.
	 */
	public static void  resultadoBBVAAServicio(DatosComunicacion datosComunicacion, String resultadoPorDefecto)
	{
		String resBBVA = datosComunicacion.getResultadoRemoto();
		java.util.List<MensajesBBVA> mapa= new ConversorRespuestaBBVA().getErrorBBVA(resBBVA);
		if (mapa!=null)
		{
			MensajesBBVA men = mapa.get(0);
			datosComunicacion.setCodigoError(men.getCodigoEpst());
			datosComunicacion.setTextoError(men.getTextoEpst()); 
		}
		else
		{
			datosComunicacion.setCodigoError(resultadoPorDefecto);
			datosComunicacion.setTextoError(Mensajes.getExternalText(resultadoPorDefecto));
		}
	}
}