package com.stpa.ws.server.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;


public class ValidatorUtils {
	public static int MAX_DECIMALES = 2;

	public static boolean isEmpty(String text) {
		return StringUtils.isBlank(text);
	}

	public static boolean validarNIFNIE(String nifUsuario) {
		if (isEmpty(nifUsuario))
			return true;
		nifUsuario = nifUsuario.toUpperCase();
		try {
			String primerCaracter = StringUtils.mid(nifUsuario, 0, 1);
			if (determinarNifCif(nifUsuario)) {
				// DNI
				String dni;
				if (!primerCaracter.equals("X"))
					dni = StringUtils.mid(nifUsuario, 0, 8);
				else
					dni = StringUtils.mid(nifUsuario, 1, 7);
				int resto = Integer.valueOf(dni) % 23;

				String[] lstLetras = { "T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B", //
						"N", "J", "Z", "S", "Q", "V", "H", "L", "C", "K", "E", "U" };
				// informa del DNI
				if (!lstLetras[resto].equals(StringUtils.mid(nifUsuario, 8, 1))) {
					return false;
				}
			} else {
				// CIF
				String codigoCIF = StringUtils.mid(nifUsuario, 1, 7);
				String[] uletra = new String[] { "J", "A", "B", "C", "D", "E", "F", "G", "H", "I" };
				int pares = 0;
				int impares = 0;
				String xxx;
				String ultimoDigito = StringUtils.mid(nifUsuario, 8, 1);

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

	public static boolean determinarNifCif(String p_nif) {
		return Pattern.matches("[X0-9]", StringUtils.mid(p_nif, 0, 1));
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
	
}
