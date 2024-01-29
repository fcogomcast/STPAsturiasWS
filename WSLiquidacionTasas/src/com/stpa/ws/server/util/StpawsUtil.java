package com.stpa.ws.server.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.stpa.ws.server.bean.LiquidacionTasas;
import com.stpa.ws.server.bean.LiquidacionTasasDetalleLiquidacion;
import com.stpa.ws.server.bean.LiquidacionTasasPeticion;
import com.stpa.ws.server.exception.StpawsException;


public class StpawsUtil {

	static int hexcase = 1;
	static int chrsz = 8;
	
	
	public static String getTimeStamp(){
		Calendar ca = new GregorianCalendar();
		return ca.get(Calendar.DAY_OF_MONTH) + "/" + ca.get(Calendar.MONTH) + "/" + ca.get(Calendar.YEAR) + " " + ca.get(Calendar.HOUR) + ":" + ca.get(Calendar.MINUTE) + ":" + ca.get(Calendar.SECOND);
	}
	
	/**
	 * 	Generar MAC para ws Liquidacion Tasas:
	 *  P_CLIENTE+P_OPERACION+P_NUMERO_UNICO+P_NOTIFICACION+P_CENTRO_GESTOR+P_COD_TASA+P_FECHA_DEVENGO
	 *  +[P_COD_TARIFA]+[P_NUM_UNIDADES]+ P_PORC_BONIFICACION+P_PORC_RECARGO+P_IMP_RECARGO+P_IMP_INTERESES
	 *  +P_ID_FISCAL_SP+P_NOMBRE_APELLIDOS_SP+P_TELEFONO_SP+P_DIRECCION_SP+P_CP_SP+P_MUNICIPIO_SP
	 *  +P_PROVINCIA_SP+P_ID_FISCAL_PR+P_NOMBRE_APELLIDOS_PR+P_TELEFONO_PR+P_DIRECCION_PR+P_CP_PR
	 *  +P_MUNICIPIO_PR+P_PROVINCIA_PR+P_OBJ_LIQUIDACION+P_EXP_EXTERNO+P_EXP_GESTION+P_PERIODO+P_FECHA_ACUERDO
	 *  +P_LIBRE+<clave>
	 * @param peticion
	 * @return
	 */
	public static String genMacLiquidacionTasas(LiquidacionTasasPeticion peticion) throws StpawsException{
		
		//codTarifa y numUnidades
		LiquidacionTasasDetalleLiquidacion[] detLiq=peticion.getLiquidacion().getDetalleLiquidacion();
		String result="";
		for(int i=0;i<detLiq.length;i++){
			result+=detLiq[i].getCodTarifa();
			result+=detLiq[i].getNumUnidades();
		}
		
		//Recuperamos la clave
		String clave = WebServicesUtils.obtenerClave();

		return SHAUtils.hex_sha1(peticion.getCliente() + 
				peticion.getOperacion() + 
				peticion.getNumeroUnico() + 
				peticion.getLiquidacion().getNotificacion() + 
				peticion.getLiquidacion().getCentroGestor() + 
				peticion.getLiquidacion().getCodTasa() + 
				peticion.getLiquidacion().getFechaDevengo() + 
				result + 
				peticion.getLiquidacion().getPorcBonificacion() + 
				peticion.getLiquidacion().getPorcRecargo() + 
				peticion.getLiquidacion().getImpRecargo() + 
				peticion.getLiquidacion().getImpIntereses() + 
				peticion.getLiquidacion().getDatosSP().getIdFiscal() + 
				peticion.getLiquidacion().getDatosSP().getNombreApellidos() + 
				peticion.getLiquidacion().getDatosSP().getTelefono() + 
				peticion.getLiquidacion().getDatosSP().getDireccion() + 
				peticion.getLiquidacion().getDatosSP().getCP() + 
				peticion.getLiquidacion().getDatosSP().getMunicipio() + 
				peticion.getLiquidacion().getDatosSP().getProvincia() + 
				peticion.getLiquidacion().getDatosPresentador().getIdFiscal() + 
				peticion.getLiquidacion().getDatosPresentador().getNombreApellidos() + 
				peticion.getLiquidacion().getDatosPresentador().getTelefono() + 
				peticion.getLiquidacion().getDatosPresentador().getDireccion() + 
				peticion.getLiquidacion().getDatosPresentador().getCP() + 
				peticion.getLiquidacion().getDatosPresentador().getMunicipio() + 
				peticion.getLiquidacion().getDatosPresentador().getProvincia() + 
				peticion.getLiquidacion().getObjLiquidacion() + 
				peticion.getLiquidacion().getExpExterno() + 
				peticion.getLiquidacion().getExpGestion() + 
				peticion.getLiquidacion().getPeriodo() + 
				peticion.getLiquidacion().getFechaAcuerdo() + 
				peticion.getLibre() + 
				clave);
	
	}
	
	
	/**
	 * Genera la mac con los datos de la peticion y comprueba que sea igual a la pasada por parametro
	 * @param peticion
	 * @param mac
	 * @return true si es correcta y false si no coincide
	 */
	public static boolean isMacLiquidacionTasasValid(LiquidacionTasasPeticion peticion, String mac, String idLlamada) throws StpawsException{
		boolean result = false;

		String nuevaMac=genMacLiquidacionTasas(peticion);
		
		if(mac!=null && !"".equals(mac) && nuevaMac!=null && !"".equals(nuevaMac)){
			if(nuevaMac.equals(mac)) result = true;
		}
		return result;
	}
	
	/**
	 * Generaci�n C�digo MAC de respuesta
	 * @param lt
	 * @return
	 */
	public static String genMacLiquidacionTasasRespuesta(LiquidacionTasas lt, String idLlamada) throws StpawsException{
		
		LiquidacionTasasPeticion peticion=lt.getPeticion();
		//codTarifa y numUnidades
		LiquidacionTasasDetalleLiquidacion[] detLiq=peticion.getLiquidacion().getDetalleLiquidacion();
		
		String detalleLiq = new String();		
		for(int i=0;i<detLiq.length;i++){
			detalleLiq += detLiq[i].getCodTarifa()+ detLiq[i].getNumUnidades();
		}				
		
		String clave = WebServicesUtils.obtenerClave();		
		
		return SHAUtils.hex_sha1(peticion.getCliente() + 
				peticion.getOperacion() + 
				peticion.getNumeroUnico() + 
				peticion.getLiquidacion().getNotificacion() + 
				peticion.getLiquidacion().getCentroGestor() + 
				peticion.getLiquidacion().getCodTasa() + 
				peticion.getLiquidacion().getFechaDevengo() + 
				detalleLiq +
				peticion.getLiquidacion().getPorcBonificacion() + 
				peticion.getLiquidacion().getPorcRecargo() + 
				peticion.getLiquidacion().getImpRecargo() + 
				peticion.getLiquidacion().getImpIntereses() + 
				peticion.getLiquidacion().getDatosSP().getIdFiscal() + 
				peticion.getLiquidacion().getDatosSP().getNombreApellidos() + 
				peticion.getLiquidacion().getDatosSP().getTelefono() + 
				peticion.getLiquidacion().getDatosSP().getDireccion() + 
				peticion.getLiquidacion().getDatosSP().getCP() + 
				peticion.getLiquidacion().getDatosSP().getMunicipio() + 
				peticion.getLiquidacion().getDatosSP().getProvincia() + 
				peticion.getLiquidacion().getDatosPresentador().getIdFiscal() + 
				peticion.getLiquidacion().getDatosPresentador().getNombreApellidos() + 
				peticion.getLiquidacion().getDatosPresentador().getTelefono() + 
				peticion.getLiquidacion().getDatosPresentador().getDireccion() + 
				peticion.getLiquidacion().getDatosPresentador().getCP() + 
				peticion.getLiquidacion().getDatosPresentador().getMunicipio() + 
				peticion.getLiquidacion().getDatosPresentador().getProvincia() + 
				peticion.getLiquidacion().getObjLiquidacion() + 
				peticion.getLiquidacion().getExpExterno() + 
				peticion.getLiquidacion().getExpGestion() + 
				peticion.getLiquidacion().getPeriodo() + 
				peticion.getLiquidacion().getFechaAcuerdo() + 
				peticion.getLibre() + 				
				lt.getRespuesta().getResultado() +
				lt.getRespuesta().getModeloPdf() +
				clave);
	
	}
}
