package com.stpa.ws.server.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;



public class ValidatorUtils {
	public static int MAX_DECIMALES = 2;

	public static boolean isEmpty(String text) {
		return StringUtils.isBlank(text);
	}

	public static boolean validarNIFNIE(String nifUsuario) {
		String nifTmp = fillZeroDni(nifUsuario);
		if (isEmpty(nifTmp))
			return true;
		nifTmp = nifTmp.toUpperCase();
		try {
			String primerCaracter = StringUtils.mid(nifTmp, 0, 1);
			if (determinarNifCif(nifTmp)) {
				// DNI
				String dni;
				int prev = 0;
				if (!primerCaracter.equals("X") && !primerCaracter.equals("Y") && !primerCaracter.equals("Z"))
					dni = StringUtils.mid(nifTmp, 0, 8);
				else{ //MAC: Incidencia 34 A�adir nie con letras X,Y,Z
					dni = StringUtils.mid(nifTmp, 1, 7);
					if(primerCaracter.equals("X")){
						prev = 0;
					}else if(primerCaracter.equals("Y")){
						prev = 10000000;
					}else if(primerCaracter.equals("Z")){
						prev = 20000000;
					}
				}
				int resto = (prev + Integer.valueOf(dni)) % 23;

				//TRWAGMYFPDXBNJZSQVHLCKEU
				String[] lstLetras = { "T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B", //
						"N", "J", "Z", "S", "Q", "V", "H", "L", "C", "K", "E", "U" };
				// informa del DNI
				if (!lstLetras[resto].equals(StringUtils.mid(nifTmp, 8, 1))) {
					return false;
				}
				if (nifTmp.length() > 9) {
					return false;
				}
			} else {
				// CIF
				String codigoCIF = StringUtils.mid(nifTmp, 1, 7);
				String[] uletra = new String[] { "J", "A", "B", "C", "D", "E", "F", "G", "H", "I" };
				int pares = 0;
				int impares = 0;
				String xxx;
				String ultimoDigito = StringUtils.mid(nifTmp, 8, 1);

				if (!Pattern.matches("[A-H,J-N,P-S,U-W]", primerCaracter) || !Pattern.matches("[0-9]+", codigoCIF) || !Pattern.matches("[A-J,0-9]", ultimoDigito))
					return false;
				
				for (int cont = 0; cont < 6; cont++) {
					xxx = (Integer.valueOf(StringUtils.mid(codigoCIF, cont++, 1)) * 2) + "0";
					impares += Integer.valueOf(StringUtils.mid(xxx, 0, 1)) + Integer.valueOf(StringUtils.mid(xxx, 1, 1));
					pares += Integer.valueOf(StringUtils.mid(codigoCIF, cont, 1));
				}

				xxx = (2 * Integer.valueOf(StringUtils.mid(codigoCIF, 6, 1))) + "0";
				impares += Integer.valueOf(StringUtils.mid(xxx, 0, 1)) + Integer.valueOf(StringUtils.mid(xxx, 1, 1));

				String suma = String.valueOf(pares + impares);
				int unumero = 10 - Integer.valueOf((StringUtils.mid(suma, suma.length() - 1, 1)));
				if (unumero == 10)
					unumero = 0;

				if (!(ultimoDigito.equals(String.valueOf(unumero))) && !(ultimoDigito.equals(uletra[unumero])))
					return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	private static String fillZeroDni(String dni){
		String result = dni;
		if(result.length()<9){
			while(result.length()<9){ 
				result = "0" + result; 
	        } 
		}
		return result;
	}

	public static boolean determinarNifCif(String p_nif) {
		return Pattern.matches("[X-Z0-9]", StringUtils.mid(p_nif, 0, 1));
	}

	public static boolean validarEmail(String p_email) {
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher m = p.matcher(p_email);
		return m.matches();
	}

	public static boolean validarCuentaBancaria(String banco, String oficina, String digitoControl, String cuenta) {
		boolean cuentaValida = true;

		if (banco != null) {
			cuentaValida = (banco.trim().length() == 4) && isNumeric(banco) && !"0000".equals(banco);

		}
		if (cuentaValida && oficina != null) {
			cuentaValida = (oficina.trim().length() == 4) && isNumeric(oficina) && !"0000".equals(oficina);
		}
		if (cuentaValida && cuenta != null) {
			cuentaValida = (cuenta.trim().length() == 10) && isNumeric(cuenta) && !"0000000000".equals(cuenta);
		}
		if (cuentaValida && digitoControl != null) {
			cuentaValida = (digitoControl.trim().length() == 2) && isNumeric(digitoControl);

			if (cuentaValida) {
				int digito1 = calcularDigitoControl("00" + banco + oficina);
				int digito2 = calcularDigitoControl(cuenta);

				String dcValido = "" + digito1 + digito2;

				cuentaValida = dcValido.equals(digitoControl.trim());
			}
		}
		return cuentaValida;
	}

	private static int calcularDigitoControl(String numero) {

		int[] multiplicador = { 1, 2, 4, 8, 5, 10, 9, 7, 3, 6 };
		int total = 0;

		for (int i = 0; i < numero.length(); i++) {
			int num = Character.getNumericValue(numero.charAt(i));
			total += num * multiplicador[i];
		}
		int digit = 11 - (total % 11);
		if (digit == 11) {
			digit = 0;
		}
		if (digit == 10) {
			digit = 1;
		}

		return digit;
	}

	public static boolean isNumeric(String cadena) {
		//Retornara true si TODOS los caracteres de la cadena son digitos 0-9
		return StringUtils.isNumeric(cadena);
	}

	public static boolean isValidDecimal(String text, int precision) {
		boolean valido = false;
		text = text.replaceFirst("-", "");
		text = text.replace(".", "");
		int idxComa = text.indexOf(",") + 1;
		String[] split = text.split(",");
		if (split[0]!=null){
			if (!isNumeric(split[0]))return false;
		}if (split.length>1 && split[1]!=null){
			if (!isNumeric(split[1]))return false;
		}
		if (idxComa <= 0) {
			valido = Pattern.matches("[0-9]{1," + text.length() + "}", text.trim());
		} else {
			int totalDecimales = text.length() - idxComa;
			if (totalDecimales > precision) {
				valido = false;
			} else {
				valido = Pattern.matches("[0-9]{1," + (idxComa - 1) + "},[0-9]{1," + totalDecimales + "}", text.trim());
			}
		}
		return valido;
	}

	

	public static boolean isCharsPermitied(String text) {
		return Pattern.matches("[0-9,]+", text.trim());
	}
	
	public static boolean isNumericDecimal(String text) {
		return (Pattern.matches("[0-9]+", text.trim()) || Pattern.matches("[0-9]+,[0-9]+", text.trim())
				|| Pattern.matches("[0-9]+.[0-9]+", text.trim()));
	}
	
	
	

	/**
	 * Valida un importe por ser numérico, su longitud (max 10) y la precision:
	 * 2
	 * 
	 * @param importe
	 * @return
	 */
	public static boolean validaImporte(String importe) {
		com.stpa.ws.server.util.Logger.debug("validaImporte - importe: " + importe,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return validaImporte(importe, 12, MAX_DECIMALES);
	}

	/**
	 * Valida un importe por ser numérico, su longitud y la precision
	 * 
	 * @param importe
	 * @param maxLenght
	 * @param precision
	 * @return
	 */
	public static boolean validaImporte(String importe, int maxLenght, int precision) {
		boolean valido = (importe != null) && (importe.trim().length() <= maxLenght);

		if (valido) {
			valido = isValidDecimal(importe, precision);
		}

		return valido;
	}

	public static boolean validaLongitudCampo(String valor, int longitudPermitida) {
		boolean longitudValida = true;
		if (valor != null) {
			longitudValida = (valor.length() <= longitudPermitida);
		}
		return longitudValida;
	}

	public static boolean validaCodigoPostal(String codPostal) {

		boolean valido = (codPostal != null) && isNumeric(codPostal) && (codPostal.length() == 5);

		if (valido) {
			int provincia = Integer.parseInt(codPostal.substring(0, 2));
			if (provincia < 0 || provincia > 52) {
				valido = false;
			}

			if (valido) {
				String codigo = codPostal.substring(2);
				valido = !"000".equals(codigo);
			}

		}
		return valido;
	}

	public static boolean validarTarjeta (String numTarjeta, String mes, String any){
		com.stpa.ws.server.util.Logger.debug("validarTarjeta - " + numTarjeta + " - " + mes + " - " + any,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		boolean result = true;
		try{
			String numTmp = numTarjeta + mes + any;
			int mesTmp = Integer.parseInt(mes);
			int anyTmp = Integer.parseInt(any,10) + 2000;
			Calendar c = new GregorianCalendar();
			int anyActual = c.get(Calendar.YEAR);
			
			if(numTmp.length()<20) result = false;
			if(mesTmp<1 || mesTmp>12) result = false;
			if(anyTmp<anyActual) result = false;
		}catch(Exception e){
			result = false;
		}
		com.stpa.ws.server.util.Logger.debug("validarTarjeta - result: " + result,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
		return result;
	}
	
	public static boolean validaFormatoFecha(String fecha) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            formatoFecha.parse(fecha);
        } catch (Exception e) {
        	com.stpa.ws.server.util.Logger.error("validaFormatoFecha - " + e.getMessage(),e,com.stpa.ws.server.util.Logger.LOGTYPE.APPLOG);
        	return false;
        }
        return true;
	}
	
	/**
	 * Valida que los datos informados permiten construir una facha valida.
	 * @param dia
	 * @param mes
	 * @param anyo
	 * @return
	 */
	public static boolean validaComponentesFecha(String dia, String mes, String anyo) {
		if (!isEmpty(dia) && !isEmpty(mes) && !isEmpty(anyo) && isNumeric(dia) && isNumeric(mes) && isNumeric(anyo)
				&& dia.length() <= 2 && mes.length() <= 2) {
			String fecha = dia + "/" + mes + "/" + anyo;
			return validaFormatoFecha(fecha);
		}
		return false;
	}
}
