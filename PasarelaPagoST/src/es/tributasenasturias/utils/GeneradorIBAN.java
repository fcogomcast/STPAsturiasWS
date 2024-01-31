package es.tributasenasturias.utils;

import java.math.BigInteger;

import es.tributasenasturias.exceptions.PasarelaPagoException;


/**
 * Permite generar un c�digo IBAN para una cuenta
 * @author crubencvs
 *
 */
public class GeneradorIBAN {
	/**
	 * Funci�n de utilidad, para devolver los d�gitos que se usar�n
	 * en el algoritmo de c�lculo de d�gito de control de IBAN
	 * @param value Valor sobre el que se calcular�
	 * @return
	 */
	private static String getDigits(String value)
	{
		int n;
		StringBuffer sb = new StringBuffer(30);
		int len = value.length();
		for (int i=0;i<len;i++)
		{
			n = (int)value.charAt(i);
			if (n>47 && n<58)
			{
				sb.append(n-48);
			}
			else 
			{
				sb.append(n-55);
			}
		}
		return sb.toString();
	}
	/**
	 * Genera el IBAN a partir de una cuenta, en formato COD PAIS + DIGITOS CONTROL IBAN + CCC
	 * @param ccc
	 * @return C�digo IBAN completo de la cuenta
	 * @throws PasarelaPagoException
	 */
	public static String generarIBAN(String ccc) throws PasarelaPagoException
	{
		String iban="";
		String aux;
		BigInteger bi;
		int resto;
		int dcIban;
		if (ccc.length()<20)
		{
			throw new PasarelaPagoException ("El CCC no tiene el formato adecuado para c�lculo de IBAN");
		}
		aux=getDigits(ccc)+ getDigits("ES")+"00";
		//Tenemos que usar BigIntegers, porque los n�meros son demasiado grandes
		bi = new BigInteger(aux);
		resto = bi.mod(new BigInteger("97")).intValue();
		dcIban = 98 - resto;
		if (dcIban<10)
		{
			iban = "ES0" + dcIban+ccc;
		}
		else 
		{
			iban="ES"+dcIban+ccc;
		}
		return iban;
	}
	
}
