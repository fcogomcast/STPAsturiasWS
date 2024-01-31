package es.tributasenasturias.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Varios 
{
	public enum TIPO {NIF,CIF,NIE,ERROR};
	
	public static TIPO esNifNieCif(String data)
	{
		TIPO toReturn = TIPO.ERROR;
		if (esNie(data))
		{
			toReturn = TIPO.NIE;
		}
		else
		{
			if(esNif(data))
			{
				toReturn = TIPO.NIF;
			}
			else
			{
				if(esCif(data))
				{
					toReturn = TIPO.CIF;
				}
			}
		}
		   
		   return toReturn;
		}
	
	public static boolean esNie(String data)
	{
		boolean toReturn = false;
		boolean valido=true;
		String primeraLetra=null;
		Integer prefijo=null;
		// Se sustituye:
		// X por 0
		// Y por 1
		// Z por 2
		// Para despu�s ir a la validaci�n del n�mero resultante como un NIF.
		data = data.toUpperCase();
		//if (data.startsWith("X") || data.startsWith("Y") || data.startsWith("Z"))
		//{
		//	toReturn = esNif(data.substring(1));
		//}
		primeraLetra=data.substring(0,1);
		if (primeraLetra.startsWith("X"))
		{
			prefijo=0;
		}
		else if (primeraLetra.startsWith("Y"))
		{
			prefijo=1;
		}
		else if (primeraLetra.startsWith("Z"))
		{
			prefijo=2;
		}
		else 
		{
			toReturn=false;
			valido=toReturn;
		}
		if (valido)
		{
			toReturn=esNif(prefijo.toString()+ data.substring(1));
		}
		return toReturn;
	}
	
	public static boolean esNif(String data)
	{
		boolean toReturn = false;
		data = data.toUpperCase();
		
		final String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
		//CRUBENCVS 11/01/2023. Sin n�mero. Error por procesar un NIF de seis d�gitos,
		//no lo valida correctamente.
		Pattern nifPattern = Pattern.compile("([LKM]{0,1})([0-9]{6,8})([" + letras + "])");
		Matcher m = nifPattern.matcher(data);
		if(m.matches())
		{
			//String letra = m.group(2);
			String letra = m.group(3); //Ahora hay letras por delante, L, K o M 
			//Extraer letra del NIF
			//int dni = Integer.parseInt(m.group(1));
			int dni = Integer.parseInt(m.group(2)); //Ahora hay letras por delante
	      	dni = dni % 23;
	      	String reference = letras.substring(dni,dni+1);
	 
	      	toReturn = reference.equalsIgnoreCase(letra);
	   }
		
	   return toReturn;
	}
	
	public static boolean esCif(String data)
	{
		boolean toReturn = false;
		data = data.toUpperCase();
		 
		Pattern cifPattern =
			Pattern.compile("([ABCDEFGHJKLMNPQRSUVWabcdefghjklmnpqrsuvw])(\\d)(\\d)(\\d)(\\d)(\\d)(\\d)(\\d)([abcdefghijABCDEFGHIJ0123456789])");
	 
		Matcher m = cifPattern.matcher(data);
		if(m.matches())
		{
			//Sumamos las posiciones pares de los n�meros centrales (en realidad posiciones 3,5,7 generales)
			int sumaPar = Integer.parseInt(m.group(3))+Integer.parseInt(m.group(5))+Integer.parseInt(m.group(7));
	 
			//Multiplicamos por 2 las posiciones impares de los n�meros centrales (en realidad posiciones 2,4,6,8 generales)
			//Y sumamos ambos digitos: el primer digito sale al dividir por 10 (es un entero y quedar� 0 o 1)
			//El segundo d�gito sale de modulo 10
			int sumaDigito2 = ((Integer.parseInt(m.group(2))*2)% 10)+((Integer.parseInt(m.group(2))*2)/ 10);
			int sumaDigito4 = ((Integer.parseInt(m.group(4))*2)% 10)+((Integer.parseInt(m.group(4))*2)/ 10);
			int sumaDigito6 = ((Integer.parseInt(m.group(6))*2)% 10)+((Integer.parseInt(m.group(6))*2)/ 10);
			int sumaDigito8 = ((Integer.parseInt(m.group(8))*2)% 10)+((Integer.parseInt(m.group(8))*2)/ 10);
	 
			int sumaImpar = sumaDigito2 +sumaDigito4 +sumaDigito6 +sumaDigito8 ;

			int suma = sumaPar +sumaImpar;
			int control = 10 - (suma%10);
			//La cadena comienza en el caracter 0, J es 0, no 10
			if (control==10)
			{
				control=0;
			}
			String letras = "JABCDEFGHI";
	 
			//El d�gito de control es una letra
			if (m.group(1).equalsIgnoreCase("K")  ||
				m.group(1).equalsIgnoreCase("Q") || m.group(1).equalsIgnoreCase("S"))
			{
				toReturn = m.group(9).equalsIgnoreCase(letras.substring(control,control+1));
			}
			//El d�gito de control es un n�mero
			else if (m.group(1).equalsIgnoreCase("B") || m.group(1).equalsIgnoreCase("H"))
			{
				toReturn = m.group(9).equalsIgnoreCase(""+control);
			}
			//El d�gito de control puede ser un n�mero o una letra
			else
			{
				toReturn = m.group(9).equalsIgnoreCase(letras.substring(control,control+1))||
					m.group(9).equalsIgnoreCase(""+control);
			}
		}

		
		return toReturn;
	}
	
	
	/* Funci�n para recuperar la diferencia entre dos fechas, en d�as, horas o minutos.
	 * 
	 */
	public static long diffDate(java.util.Date date1, java.util.Date date2,String diffUnit)
	{
		long factor=0; 
		if (diffUnit.equals("D"))
		{
			factor=(60*60*24*1000);//D�as
		}
		else if (diffUnit.equals("H"))
		{
			factor=(60*60 *1000);  //Horas
		}
		else // Segundos, sea lo que sea lo que hayan pasado.
		{
			factor=(60*1000); //Segundos
		}
		java.util.Calendar cal1= java.util.Calendar.getInstance();
		cal1.setTime(date1);
		java.util.Calendar cal2 = java.util.Calendar.getInstance();
		cal2.setTime(date2);
		
		return (cal2.getTimeInMillis() - cal1.getTimeInMillis())/factor;//En minutos
	}
	
	public static String getDateFormatted(String date, String formatIn, String formatOut) throws ParseException
	{
		//obtenemos la fecha en objeto Date
		SimpleDateFormat sdf = new SimpleDateFormat(formatIn);
		Date fechaEntrada = sdf.parse(date);
		//formateamos el date
		sdf = new SimpleDateFormat(formatOut);
		return sdf.format(fechaEntrada);
	}
	/**
	 * Metodo que crea un objeto cabecera para pasarselo al ws de infocaja
	 * 
	 * Requiere especificar por parametro la aplicacion de destino ya que
	 * este valor es que el que definira el metodo que se quiere ejecutar
	 * en el ws de infocaja
	 */
//	public static Header getMessageHeader(String aplicacionDestino) throws Exception
//	{
//		//creamos el mensaje cabecera
//		Header header = new Header();
//		Preferencias preferencias = Preferencias.getPreferencias();
//		header.setAplicacionDestino(aplicacionDestino);
//		header.setAplicacionOrigen(preferencias.getAplicacionOrigen());
//		header.setCaja(preferencias.getEntidad());
//		header.setCanal(preferencias.getCanal());
//		header.setEntorno(preferencias.getEntorno());
//		header.setIdioma(preferencias.getIdioma());
//		header.setIP(preferencias.getIpOrigen());
//		header.setUsuario(preferencias.getUsuarioAplicacion());
//		
//		return header;
//	}
	/*******************************************************************
	 * Metodo que comprueba si su par�metro de tipo String es nulo, y devuelve 
	 * el mismo par�metro con el valor de cadena vac�a en ese caso, o el valor original
	 * 
	 * @param valor El valor a verificar contra nulo. 
	 * @return La cadena vac�a si el par�metro es nulo o el valor original del par�metro
	 * @throws 
	 */
	public static String normalizeNull
			(
					String valor
			)
	{
		return (valor==null)? "":valor.trim();
	}

	/**
	 * M�todo para formatear/validar un nif
	 * @param nif
	 * @return nif formateado
	 */
	public String formateaNIF(String nif)
	{
		if (nif==null)
		{
			return "";
		}
		String nifFinal=null;
		TIPO tipo = esNifNieCif(nif);
		if (tipo == TIPO.NIF) {nifFinal = Constantes.getPrefijoNIF() + lpad(nif,9,'0');}
		else if (tipo == TIPO.NIE) {nifFinal = Constantes.getPrefijoNIE() + nif;}
		else if (tipo == TIPO.CIF){ nifFinal = Constantes.getPrefijoCIF() + nif;}
		else {nifFinal="";}
		return nifFinal;
	}
	/**
	 * Formatea una cadena rellen�ndola por la izquierda con una car�cter determinado, hasta el tama�o que se indica
	 * @param str Cadena a rellenar
	 * @param size Tama�o al que se ha de llegar.
	 * @param padChar Car�cter de relleno.
	 * @return
	 */
	private String lpad(String str, int size, char padChar)
	{
		 if (str.length() <= size) {
		        char[] temp = new char[size];
		        //Se llena el array con los caracteres de relleno
		        for(int i =0;i<size;i++){
		            temp[i]= padChar;
		        }
		        //Se respetan las posiciones por la izquierda que no vaya a ocupar la cadena original.
		        int posIniTemp = size-str.length();
		        for(int i=0;i<str.length();i++){
		            temp[posIniTemp]=str.charAt(i);
		            posIniTemp++;
		        }            
		        return new String(temp);
		    }
		    return str;
		    
		  //Implementaci�n alternativa, por si la anterior diera problemas de velocidad,
		  //aunque deber�a ser las m�s r�pida.
		    /*
		    StringBuilder sb = new StringBuilder();
		    //Creamos "size-longitud(cadena)" posiciones del car�cter de relleno.
		    for (int toprepend=size-str.length(); toprepend>0; toprepend--) {
		        sb.append(padChar);
		    }
		    sb.append(str);
		    return sb.toString();
		    */
	}
	
	public static String extraeDatoEspecifico(String datoEspecificoCompleto, String numAutoliquidacion)
	{
		if ("046".equals(numAutoliquidacion.substring(0,3)))
		{
					
			if (datoEspecificoCompleto==null)
			{
				return datoEspecificoCompleto;
			}
			if (datoEspecificoCompleto.length()<15)
			{
				return datoEspecificoCompleto;
			}
			return datoEspecificoCompleto.substring(8, 14);
		}else
		{
			return datoEspecificoCompleto;
		}
	}
	/**
	 * Elimina un caracter determinado por la izquierda de una cadena.
	 * Recorre la cadena hasta encontrar un car�cter diferente al buscado. En ese momento, devuelve el resto
	 * de la cadena de entrada.
	 * @param str Cadena de la que eliminar el car�cter por la izquierda.
	 * @param tChar Car�cter a eliminar.
	 * @return
	 */
	public static String ltrim(String str, char tChar)
	{
		String st1=null;
		int pos=-1;
		for (int i=0;i<str.length();i++)
		{
			if (str.charAt(i)!=tChar)
			{
				pos=i;
				break;
			}
		}
		if (pos!=-1)
		{
			for (int i=pos;i<str.length();i++)
			{
				st1=str.substring(pos);
			}
		}
		return st1;
	}
	
	public static boolean esNumero(String n)
	{
		for (char c: n.toCharArray())
		{
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}
	/**
	 * Comprobaci�n de formato de tarjeta, s�lo comprueba que no est� vac�a, que tiene al menos 13 caracteres
	 * y que son d�gitos
	 * @param tarjeta N�mero de la tarjeta
	 * @return
	 */
	public static boolean formatoTarjetaValido (String tarjeta)
	{
		if (tarjeta==null || "".equals(tarjeta) || tarjeta.length()<13){
			return false;
		}
		
		if (!esNumero(tarjeta.substring(0, 13)) ) {
			return false;
		}
		return true;
	}
	
	/**
	 * Devuelve el valor de la cadena pasada por par�metro, o cadena vac�a si su valor era nulo.
	 * @param valor Valor de la cadena a convertir.
	 * @return Valor del par�metro de entrada o cadena vac�a si era nulo.
	 */
	public static String null2empty(String valor) {
		return (valor == null) ? "" : valor.trim();
	}
}