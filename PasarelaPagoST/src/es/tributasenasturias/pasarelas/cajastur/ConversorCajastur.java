package es.tributasenasturias.pasarelas.cajastur;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.tibco.schemas.infocajacommon.sharedresources.schemas.service.schema.Header;

import es.infocaja.schemas.consulta_cobros.CONSULTACOBROSIN;
import es.infocaja.schemas.pag_c3po.PAGC3POIN;
import es.tributasenasturias.conversor.ConversorRespuestaCaja;
import es.tributasenasturias.conversor.MensajesCaja;
import es.tributasenasturias.dao.DatosProceso;
import es.tributasenasturias.dao.DatosEntradaServicio;
import es.tributasenasturias.exceptions.ConversionExcepcion;
import es.tributasenasturias.pasarelas.comunicacion.DatosComunicacion;
import es.tributasenasturias.utils.Constantes;
import es.tributasenasturias.utils.Mensajes;
import es.tributasenasturias.utils.Varios;

/**
 * Convierte la entrada de servicio a una entrada de Liberbank, y la salida de
 * Liberbank a una salida de servicio.
 * 
 * @author crubencvs
 * 
 */
public final class ConversorCajastur {
	
	private static final String MASCARA_ANIO_MES_DIA = "yyyy-MM-dd";
	private static final String MASCARA_ANIO_MES = "yyyy-MM";
	private ConversorCajastur(){}
	
	//Convierte el importe al formato que requieren, numérico con punto decimal y dos posiciones
	//Utilizo el Locale de estados Unidos, porque utilizan el punto.
	private static String formateaImporte(String importe)
	{
		double imp = Double.valueOf(importe)/100.0;
		return String.format(java.util.Locale.US, "%.2f", imp);
	}
	/**
	 * Crea un mensaje de pago a Liberbank en función de los datos de entrada de
	 * la petición.
	 * 
	 * @param datosProceso
	 * @return
	 * @throws ConversionException
	 */
	public static PAGC3POIN peticionACajastur(DatosProceso datosProceso)
			throws ConversionExcepcion {
		DatosEntradaServicio peticionServicio= datosProceso.getPeticionServicio();
		PAGC3POIN pago = new PAGC3POIN();
		String nifFinal = "";
		String nifOperanteFinal = "";
		Varios util = new Varios();
		nifFinal = util.formateaNIF(peticionServicio.getNifContribuyente());
		nifOperanteFinal = util.formateaNIF(peticionServicio.getNifOperante());
		try {
			if ("".equals(nifFinal)) {
				nifFinal = nifOperanteFinal;
			}
			pago.setCODIDENTIF(nifFinal);
			pago.setEMISORA(peticionServicio.getEmisora());
			pago.setIMPORTERECIBO(formateaImporte(peticionServicio.getImporte()));
			pago.setNUMEXPEDIENTE(peticionServicio.getExpediente());
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
			if (peticionServicio.getModalidad().equals(
					Constantes.getModalidadAutoliquidacion())) {
				pago.setIDENTIFICATIVO("");
				pago.setREFERENCIA(datosProceso.getJustificante());

			} else if ((peticionServicio.getOrigen().equals(
					Constantes.getOrigenPortal()) || peticionServicio.getOrigen()
					.equals(Constantes.getOrigenS1()))
					&& peticionServicio.getModalidad().equals(
							Constantes.getModalidadLiquidacion())) {
				pago.setIDENTIFICATIVO(peticionServicio.getIdentificacion());
				pago.setREFERENCIA(peticionServicio.getReferencia());
			}
			if (!"".equals(peticionServicio.getFechaDevengo())) {
				pago.setFECDEVENGO(Varios.getDateFormatted(peticionServicio
						.getFechaDevengo(), "ddMMyyyy", MASCARA_ANIO_MES_DIA));
			} else {
				pago.setFECDEVENGO("");
			}
			pago.setDATOESPECIFICO(Varios.extraeDatoEspecifico(peticionServicio.getDatoEspecifico(),pago.getREFERENCIA()));
			pago.setNIFOPECONS(nifOperanteFinal);
			pago.setINDCAJA("N");
			pago.setINDDOMICILIADO("N");
			pago.setOPCION("1");// Se permite el "1". No permite expediente
			// al mismo tiempo.

			// el sistema no permite que sean null
			pago.setCODVALTARJETA("");
			pago.setCANAL("");
			pago.setCTADOMICILIA("");
			pago.setFECCOMISION("");
			pago.setFECLIMITECOBRO("");
			pago.setFECLIQUIDACION("");
			pago.setFECSOPORTE("");
			pago.setFECTRAMITA("");
			pago.setIMPORTERECARGO("0");
			pago.setINDESTADO("");
			pago.setNOMCONTRIBUYE("");
			pago.setNUMIDENTOPERA("");
			pago.setRUTNUM("1");
			pago.setSUFIJO("");
		} catch (ParseException e) {
			throw new ConversionExcepcion(
					"Error de conversión de fecha en mensaje de Liberbank:"
							+ e.getMessage(), e);
		} catch (Exception e) {
			throw new ConversionExcepcion(
					"Error de conversión a mensaje de liberbank:"
							+ e.getMessage(), e);
		}
		return pago;
	}
	/**
	 * Crea un mensaje de consulta de cobro a Cajastur. 
	 * @param datosProceso Datos del proceso, incluirá la petición al servicio web.
	 * @return
	 * @throws ConversionExcepcion
	 */
	public static CONSULTACOBROSIN consultaACajastur(DatosProceso datosProceso)
			throws ConversionExcepcion {
		String TIPO_FECHA_OPERACION="1";
		CONSULTACOBROSIN consulta = new CONSULTACOBROSIN();
		DatosEntradaServicio peticionServicio= datosProceso.getPeticionServicio();
		consulta.setEMISOR(peticionServicio.getEmisora());
		String origen = peticionServicio.getOrigen();
		if ((origen.equals(Constantes.getOrigenServicioWeb()) || origen .equals(Constantes.getOrigenPortal()) ||
			origen.equals(Constantes.getOrigenS1()))&& peticionServicio.getModalidad().equals(Constantes.getModalidadAutoliquidacion()))
		{
			//Dependiendo de si el justificante se ha generado o no, estará en los datos de entrada.
			if (!"".equals(peticionServicio.getJustificante()))
			{
				consulta.setREFEREN(peticionServicio.getJustificante());
			}
			else
			{
				consulta.setREFEREN(datosProceso.getJustificante());
			}
		}
		else if ((origen .equals(Constantes.getOrigenPortal()) ||
			origen.equals(Constantes.getOrigenS1())) && peticionServicio.getModalidad().equals(Constantes.getModalidadLiquidacion()))
		{
			consulta.setIDENTIF(peticionServicio.getIdentificacion());
			consulta.setREFEREN(peticionServicio.getReferencia());
		}
		String fecha = datosProceso.getFechaOperacion();
		Date fechaOperacion=null;
		if (fecha!=null && !"".equals(fecha))
		{
			try
			{
				SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
				if (fecha.trim().length()== fmt.toPattern().length())
				{
					fechaOperacion = new SimpleDateFormat("yyyyMMdd").parse(fecha);
				}
			}
			catch (Exception e)
			{
				throw new ConversionExcepcion ("Error al convertir la fecha de operación:"+ e.getMessage(),e);
			}
			
		}
		
		String fecha_dsd=null;
		String fecha_hst=null;
		try
		{
			if (fechaOperacion!=null)
			{
				java.util.Calendar cal = java.util.Calendar.getInstance();
				cal.setTime(fechaOperacion);
				cal.add(java.util.Calendar.DAY_OF_MONTH, -1);
				fecha_dsd = new SimpleDateFormat(MASCARA_ANIO_MES_DIA).format(cal.getTime()); // Fecha desde
				//Utilizo como fecha hasta la de sistema +1. 
				//Si se utilizase sólo la fecha de operación, podría no encontrar datos nunca.
				//Esto es así porque la fecha de operación se da de alta sólo al crear el registro.
				//Si el registro se crea, pero no se puede realizar el pago hasta varios días después,
				//tendremos que la consulta desde fecha de operación-1 a fecha de operación+1 no nos devolvería datos nunca.
				//Esto es así por haber eliminado el que recoja la fecha de pago o en su defecto la de operación.
				cal.setTime(new Date());
				cal.add(java.util.Calendar.DAY_OF_MONTH,1); 
				fecha_hst = new SimpleDateFormat (MASCARA_ANIO_MES_DIA).format(cal.getTime()); // Fecha hasta
			}
			else
			{
				fecha_dsd="";
				fecha_hst="";
			}
		}
		catch (Exception e)
		{
			throw new ConversionExcepcion ("Error al recuperar las fechas desde y hasta de la consulta:"+ e.getMessage(),e);
		}
		consulta.setFECDSD(fecha_dsd);
		consulta.setFECHST(fecha_hst);
		//infoCaja.setESTADO(estadoCobrado);
		consulta.setESTADO("");
		//Objetos que hay que poner a "Cadena Vacía" para que pueda ejecutar la consulta.
		consulta.setCANAL("");
		consulta.setOPCION("C");
		consulta.setTIPEMI("B60");
		consulta.setTIPFEC(TIPO_FECHA_OPERACION);
		return consulta;
	}

	/**
	 * Crea un mensaje de anulación a Liberbank en función de los datos de entrada de
	 * la petición.
	 * 
	 * @param datosProceso  Datos del proceso. Incluirá los datos de la operación.
	 * @return {@link PAGC3POIN} 
	 * @throws ConversionException
	 */
	public static PAGC3POIN anulacionACajastur(DatosProceso datosProceso)
			throws ConversionExcepcion {
		DatosEntradaServicio peticionServicio= datosProceso.getPeticionServicio();
		PAGC3POIN anulacion = new PAGC3POIN();
		String nifFinal = "";
		String nifOperanteFinal = "";
		Varios util = new Varios();
		nifFinal = util.formateaNIF(peticionServicio.getNifContribuyente());
		nifOperanteFinal = util.formateaNIF(peticionServicio.getNifOperante());
		try {
			if ("".equals(nifFinal)) {
				nifFinal = nifOperanteFinal;
			}
			anulacion.setCODIDENTIF(nifFinal);
			anulacion.setEMISORA(peticionServicio.getEmisora());
			anulacion.setIMPORTERECIBO(formateaImporte(peticionServicio.getImporte()));
			anulacion.setNUMEXPEDIENTE(peticionServicio.getExpediente());
			anulacion.setNUMIDENTOPERA(datosProceso.getNumeroOperacion());
			if (!"".equals(peticionServicio.getTarjeta())) {
				anulacion.setTARJETA(peticionServicio.getTarjeta());
				anulacion.setCUENTA("");
			} else {
				anulacion.setTARJETA("");
				anulacion.setCUENTA(peticionServicio.getCcc());
			}
			if (!"".equals(peticionServicio.getFechaCaducidadTarjeta())) {
				anulacion.setFECCADTARJETA(Varios.getDateFormatted(peticionServicio
						.getFechaCaducidadTarjeta(), "MMyy", MASCARA_ANIO_MES));
			} else {
				anulacion.setFECCADTARJETA("");
			}
			if (peticionServicio.getModalidad().equals(
					Constantes.getModalidadAutoliquidacion())) {
				anulacion.setIDENTIFICATIVO("");
				anulacion.setREFERENCIA(peticionServicio.getJustificante());

			} else if ((peticionServicio.getOrigen().equals(
					Constantes.getOrigenPortal()) || peticionServicio.getOrigen()
					.equals(Constantes.getOrigenS1()))
					&& peticionServicio.getModalidad().equals(
							Constantes.getModalidadLiquidacion())) {
				anulacion.setIDENTIFICATIVO(peticionServicio.getIdentificacion());
				anulacion.setREFERENCIA(peticionServicio.getReferencia());
			}
			if (!"".equals(peticionServicio.getFechaDevengo())) {
				anulacion.setFECDEVENGO(Varios.getDateFormatted(peticionServicio
						.getFechaDevengo(), "ddMMyyyy", MASCARA_ANIO_MES_DIA));
			} else {
				anulacion.setFECDEVENGO("");
			}
			anulacion.setDATOESPECIFICO(Varios.extraeDatoEspecifico(peticionServicio.getDatoEspecifico(),anulacion.getREFERENCIA()));
			anulacion.setNIFOPECONS(nifOperanteFinal);
			anulacion.setINDCAJA("N");
			anulacion.setINDDOMICILIADO("N");
			anulacion.setNUMIDENTOPERA(datosProceso.getNumeroOperacion());
			anulacion.setRUTNUM("3");
			anulacion.setOPCION("2");
			// al mismo tiempo.

			// el sistema no permite que sean null
			anulacion.setCODVALTARJETA("");
			anulacion.setCANAL("");
			anulacion.setCTADOMICILIA("");
			anulacion.setFECCOMISION("");
			anulacion.setFECLIMITECOBRO("");
			anulacion.setFECLIQUIDACION("");
			anulacion.setFECSOPORTE("");
			anulacion.setFECTRAMITA("");
			anulacion.setIMPORTERECARGO("0"); //Parece ser necesario enviarlo a 0
			anulacion.setINDESTADO("");
			anulacion.setNOMCONTRIBUYE("");
			anulacion.setSUFIJO("");
		} catch (ParseException e) {
			throw new ConversionExcepcion(
					"Error de conversión de fecha en mensaje hacia Liberbank:"
							+ e.getMessage(), e);
		} catch (Exception e) {
			throw new ConversionExcepcion(
					"Error de conversión a mensaje de Liberbank:"
							+ e.getMessage(), e);
		}
		return anulacion;
	}
	/**
	 * Metodo que crea un objeto cabecera para pasarselo al ws de Liberbank
	 * 
	 * Requiere especificar por parametro la aplicacion de destino ya que este
	 * valor es que el que definira el metodo que se quiere ejecutar en el ws de
	 * liberbank
	 */
	public static Header getMessageHeader(String aplicacionDestino,
			PreferenciasCajastur pref) {
		// creamos el mensaje cabecera
		Header header = new Header();
		header.setAplicacionDestino(aplicacionDestino);
		header.setAplicacionOrigen(pref.getAplicacionOrigen());
		header.setCaja(pref.getEntidad());
		header.setCanal(pref.getCanal());
		header.setEntorno(pref.getEntorno());
		header.setIdioma(pref.getIdioma());
		header.setIP(pref.getIpOrigen());
		header.setUsuario(pref.getUsuarioAplicacion());
		

		return header;
	}
	/**
	 * Convierte la respuesta de Cajastur a código de respuesta del servicio, utilizando el fichero de mapeo de errores.
	 * @param datosComunicacion Datos de la comunicación con Cajastur
	 * @param resultadoPorDefecto Código de resultado si no se encuentra en el mapeo de mensajes.
	 */
	public static void  resultadoCajasturAServicio(DatosComunicacion datosComunicacion, String resultadoPorDefecto)
	{
		String resCajastur = datosComunicacion.getResultadoComunicacion();
		java.util.List<MensajesCaja> mapa= new ConversorRespuestaCaja().getErrorCaja(resCajastur);
		if (mapa!=null)
		{
			MensajesCaja men = mapa.get(0);
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
