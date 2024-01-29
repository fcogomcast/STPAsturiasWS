package com.stpa.ws.server.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.stpa.ws.server.bean.Modelo046;
import com.stpa.ws.server.bean.Modelo046Detalle;
import com.stpa.ws.server.bean.Modelo046Peticion;
import com.stpa.ws.server.exception.StpawsException;


public class StpawsUtil {

	static int hexcase = 1;
	static int chrsz = 8;
	
	/**
	 * Genera la mac con los datos de la peticion y 
	 * comprueba que sea igual a la pasada por parametro
	 * @param p_cliente Parametro de la peticion
	 * @param p_timestamp Parametro de la peticion
	 * @param p_modelo_autoliquidacion Parametro de la peticion
	 * @param p_libre Parametro de la peticion
	 * @param mac Parametro a validar
	 * @return true si es correcta y false si no coincide
	 */
	public static boolean isMacValid(String param, String mac){
		boolean result = false;
		String s = SHAUtils.hex_sha1(param);
		if(mac!=null && !"".equals(mac) && s!=null && !"".equals(s)){
			if(s.equals(mac)) result = true;
		}
		return result;
	}
	
	/**
	 * Genera la mac como resultado de la respuesta del ws.
	 * @return La nueva mac generada para esta peticion y respuesta
	 */
	public static String genMac(String param){
		return SHAUtils.hex_sha1(param);
	}
	
	/**
	 * Genera la mac sin codificacion base64.
	 * @param Modelo046Peticion
	 * @return
	 */
	public static String mountMac(Modelo046Peticion mp) throws StpawsException{
		String result = mp.getCliente() + mp.getAutoliquidacion().getModelo() + 
			mp.getAutoliquidacion().getFechaDevengo().getDia() + 
			mp.getAutoliquidacion().getFechaDevengo().getMes() +
			mp.getAutoliquidacion().getFechaDevengo().getAno() +
			mp.getAutoliquidacion().getDatoEspecifico().getCentroGestor() +
			mp.getAutoliquidacion().getDatoEspecifico().getAplicacion() +
			mp.getAutoliquidacion().getDatoEspecifico().getEjercicio() +
			mp.getAutoliquidacion().getDatoEspecifico().getPeriodo() +
			mp.getAutoliquidacion().getSujetoPasivo().getNif() +
			mp.getAutoliquidacion().getSujetoPasivo().getApellidosNombre() + 
			mp.getAutoliquidacion().getSujetoPasivo().getSiglasVia() +
			mp.getAutoliquidacion().getSujetoPasivo().getNombreVia() +
			mp.getAutoliquidacion().getSujetoPasivo().getNumero() +
			mp.getAutoliquidacion().getSujetoPasivo().getEscalera() +
			mp.getAutoliquidacion().getSujetoPasivo().getPiso() +
			mp.getAutoliquidacion().getSujetoPasivo().getPuerta() +
			mp.getAutoliquidacion().getSujetoPasivo().getLocalidad() +
			mp.getAutoliquidacion().getSujetoPasivo().getProvincia() +
			mp.getAutoliquidacion().getSujetoPasivo().getCodPostal() +
			mp.getAutoliquidacion().getSujetoPasivo().getTelefono() +
			mp.getAutoliquidacion().getDetalleLiquidacion().getDescripcion();
		
		Modelo046Detalle[] detalle = mp.getAutoliquidacion().getDetalleLiquidacion().getDetalle();
		if(detalle!=null){
			String tarifa = "";
			String descripcion = "";
			String valor = "";
			String importe = "";
			for(int i=0;i<detalle.length;i++){
				String tarifaAux = detalle[i].getTarifa();
				String descripcionAux = detalle[i].getDescripcionConcepto();
				String valorAux = detalle[i].getValor();
				String importeAux = detalle[i].getImporte();
				if(tarifaAux!=null) result += tarifaAux;
				if(descripcionAux!=null) result += descripcionAux;
				if(valorAux!=null) result += valorAux;
				if(importeAux!=null) result += importeAux;
			}
//			result += tarifa;
//			result += descripcion;
//			result += valor;
//			result += importe;
		}
		String clave = WebServicesUtil.obtenerClave();
		
		result += mp.getAutoliquidacion().getTotalValor() + mp.getAutoliquidacion().getTotalImporte() +
		mp.getLibre() + clave;
		
		return result;
	}
	
	/**
	 * Devuelve la fecha actual en formato dd/mm/yyyy hh:mm:ss
	 * @return
	 */
	public static String getTimeStamp(){
		Calendar ca = new GregorianCalendar();
		return ca.get(Calendar.DAY_OF_MONTH) + "/" + ca.get(Calendar.MONTH) + "/" + ca.get(Calendar.YEAR) + " " + ca.get(Calendar.HOUR) + ":" + ca.get(Calendar.MINUTE) + ":" + ca.get(Calendar.SECOND);
	}
	
	/**
	 * Comprueba si un String es correcto. Es decir, si no esta vacio ni es null.
	 * Si el parametro length es mayor de 0 comprueba que dicho String mida exactamente
	 * este valor.
	 * Si el array de valores contiene elementos, se comprueba que alguno coincida.
	 * @param s
	 * @param length
	 * @param valores
	 * @return
	 */
	public static boolean isStrValid(String s, int length, String[] valores){
		boolean result = false;
		if(s!=null && !"".equals(s)) result = true;
		if(length>0){
			if(s.length()!=length) result = false;   
		}
		if(valores!=null && valores.length>0){
			if(result) result = constains(s,valores);
		}
		return result;
	}
	
	public static boolean constains(String s, String[] valores){
		boolean result = false;
		if(valores!=null && valores.length>0){
			boolean baux = false;
			for(int i=0;i<valores.length;i++){
				String val = valores[i];
				if(val!=null && s.equals(val)) baux = true;
			}
			result = baux;
		}
		return result;
	}
	
	public static boolean isNumber(String s){
		try{
			Pattern p= Pattern.compile("[^0-9]");
			Matcher m=p.matcher(s);
			if(!m.find()){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}


	/**
	 * Validación de fecha con formato AAAAMMDD
	 * @param fechaAcuerdo
	 * @return
	 */
	public static boolean isDate(String fecha) {
		int ano=0;
		int mes=0;
		int dia=0;
		try {
			ano=Integer.parseInt(fecha.substring(0,4));
			mes=Integer.parseInt(fecha.substring(4,6));
			dia=Integer.parseInt(fecha.substring(6,8));
			return validarFecha(dia,mes,ano);
		} catch (Exception e) {
			//Si la fecha es null, no contiene 8 caracteres numéricos... 
			return false;
		}
	}

	/**
	 * calcula si es una fecha válida 
	 * @param dia
	 * @param mes
	 * @param ano
	 * @return
	 */
	private static boolean validarFecha(int dia, int mes, int ano){ 
		boolean valida=false;  
		if(dia < 0 || dia > 31 || mes < 0 || mes > 12){ 
			valida=false; 
		}else{
			if ((mes==4 || mes==6 || mes==9 || mes==11) && dia > 30){ 
				valida=false; 
			}else{
				if (mes==2 && dia > ultDiaFeb(ano)){ 
					valida=false; 
				}else{
					valida=true;
				}
			} 
		}
		return valida; 
	}	
	/**
	 * Obtiene el ultimo día de febrero segun el año introducido por parámetro
	 * @param ano
	 * @return
	 */
	private static int ultDiaFeb(int ano){
		if(isBisiesto(ano)){
			return 29;
		}else{
			return 28;
		}
	}
	/**
	 * Cálcula si el año es bisiesto
	 * @param ano
	 * @return
	 */
	private static boolean isBisiesto(int ano){
		boolean bisiesto=false;
		if(ano % 4 != 0){ 
			bisiesto=false; 
		}else if(ano % 400 == 0){ 
			bisiesto=true; 
		}else if(ano % 100==0) {
			bisiesto=false; 
		}else {
			bisiesto=true;
		}
		return bisiesto; 
	}

	/**
	 * P_CLIENTE+P_MODELO+P_DIA+P_MES+P_ANO+P_CENTRO_GESTOR+P_APLICACION+P_EJERCICIO+P_PERIODO+P_NIF+
	 * P_APELLIDOS_NOMBRE+P_SIGLAS_VIA+P_NOMBRE_VIA+P_NUMERO+P_ESCALERA+P_PISO+P_PUERTA+
	 * P_LOCALIDAD+P_PROVINCIA+P_COD_POSTAL+P_TELEFONO+P_DESCRIPCION+
	 * P_TARIFA[i]+P_DESCRIPCION_CONCEPTO[i]+P_VALOR[i]+P_IMPORTE[i]+ 
	 * P_TOTAL_VALOR+P_IMPORTE+P_LIBRE+R_RESULTADO+<clave>.
	 * @param m046
	 * @return
	 */
	public static String mountMacResp(Modelo046 m046) throws StpawsException {
		Modelo046Peticion mp=m046.getPeticion();
		String result = mp.getCliente() + 
		mp.getAutoliquidacion().getModelo() + 
		mp.getAutoliquidacion().getFechaDevengo().getDia() + 
		mp.getAutoliquidacion().getFechaDevengo().getMes() +
		mp.getAutoliquidacion().getFechaDevengo().getAno() +
		mp.getAutoliquidacion().getDatoEspecifico().getCentroGestor() +
		mp.getAutoliquidacion().getDatoEspecifico().getAplicacion() +
		mp.getAutoliquidacion().getDatoEspecifico().getEjercicio() +
		mp.getAutoliquidacion().getDatoEspecifico().getPeriodo() +
		mp.getAutoliquidacion().getSujetoPasivo().getNif() +
		mp.getAutoliquidacion().getSujetoPasivo().getApellidosNombre() + 
		mp.getAutoliquidacion().getSujetoPasivo().getSiglasVia() +
		mp.getAutoliquidacion().getSujetoPasivo().getNombreVia() +
		mp.getAutoliquidacion().getSujetoPasivo().getNumero() +
		mp.getAutoliquidacion().getSujetoPasivo().getEscalera() +
		mp.getAutoliquidacion().getSujetoPasivo().getPiso() +
		mp.getAutoliquidacion().getSujetoPasivo().getPuerta() +
		mp.getAutoliquidacion().getSujetoPasivo().getLocalidad() +
		mp.getAutoliquidacion().getSujetoPasivo().getProvincia() +
		mp.getAutoliquidacion().getSujetoPasivo().getCodPostal() +
		mp.getAutoliquidacion().getSujetoPasivo().getTelefono() +
		mp.getAutoliquidacion().getDetalleLiquidacion().getDescripcion();

		Modelo046Detalle[] detalle = mp.getAutoliquidacion().getDetalleLiquidacion().getDetalle();
		if(detalle!=null){
			for(int i=0;i<detalle.length;i++){
				String tarifaAux = detalle[i].getTarifa();
				String descripcionAux = detalle[i].getDescripcionConcepto();
				String valorAux = detalle[i].getValor();
				String importeAux = detalle[i].getImporte();
				if(tarifaAux!=null) result += tarifaAux;
				if(descripcionAux!=null) result += descripcionAux;
				if(valorAux!=null) result += valorAux;
				if(importeAux!=null) result += importeAux;
			}
//			result += tarifa;
//			result += descripcion;
//			result += valor;
//			result += importe;
		}

		String clave = WebServicesUtil.obtenerClave();
		
		result += mp.getAutoliquidacion().getTotalValor() + 
		mp.getAutoliquidacion().getTotalImporte() +
		mp.getLibre() + 
		m046.getRespuesta().getResultado() + clave;

		return result;
	}
}
