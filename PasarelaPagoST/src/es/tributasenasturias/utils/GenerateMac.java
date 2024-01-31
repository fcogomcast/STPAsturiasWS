package es.tributasenasturias.utils;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import es.tributasenasturias.bd.Datos;

public class GenerateMac 
{
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	private GenerateMac(){}
	@SuppressWarnings("unused")
	@Deprecated
	private static String calculate(final String data, final String key) throws NoSuchAlgorithmException, InvalidKeyException
	{
		String result="";
		
		byte[] keyBytes = key.getBytes();			
		SecretKeySpec signingKey = new SecretKeySpec(keyBytes, HMAC_SHA1_ALGORITHM);
		
		
		Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);

		byte[] rawHmac = mac.doFinal(data.getBytes());
		
		result = Base64.encode(rawHmac);
	
		return result;
	}
	public static String calculateHex(String data) throws NoSuchAlgorithmException, InvalidKeyException,Exception
	{
		Datos datos;
		String errorLlamada;
		//datos=Datos.getDatos();
		datos = new Datos(GestorIdLlamada.getIdLlamada());
		String key = datos.obtenerClaveConsejerias();
		

		errorLlamada = datos.getErrorLlamada();
		if (errorLlamada!=null && !"".equals(errorLlamada))
		{
			throw new Exception ("Error al recuperar la clave de consejer�as para la verificaci�n de MAC:" + errorLlamada);
		}
		byte[] buffer, digest;
		MessageDigest md;
		StringBuffer hash=new StringBuffer();
		String hexChar="";
		//ANULADO los datos que se pasan para codificar, se les a�ade la clave de consejer�as.
		data += key;
		buffer = data.getBytes();
        md = MessageDigest.getInstance("SHA1");
        md.update(buffer);
        digest = md.digest();
        for(byte aux : digest) {
            int b = aux & 0xff; // Convierte de unsigned a integer, respetando signo (integer tiene signo, y podr�a considerar que un byte es negativo)
            hexChar=Integer.toHexString(b);
            hash.append(hexChar.length()==1?("0"+hexChar):hexChar); //Left Padding, as� se rellena correctamente.
        }
        
        return hash.toString().toUpperCase();
	}
	public static String calculateHex(String data, String cliente) throws NoSuchAlgorithmException, InvalidKeyException,Exception
	{
		Datos datos;
		String errorLlamada;
		datos = new Datos(GestorIdLlamada.getIdLlamada());
		Map<String,String>resultado = datos.obtenerClaveCliente(cliente);
		String key="";
		errorLlamada = datos.getErrorLlamada();
		if (errorLlamada!=null && !errorLlamada.equals(""))
		{
			throw new Exception ("Error al recuperar la clave de cliente para la verificaci�n de MAC para cliente:" + cliente + "-->" + errorLlamada);
		}
		key = resultado.get("clave");
		String estado=resultado.get("estado");
		if (estado.equals("01"))
		{
			throw new Exception ("Error al recuperar la clave de cliente para la verificaci�n de MAC para cliente: "+cliente+".");
		}
		byte[] buffer, digest;
		MessageDigest md;
		StringBuffer hash=new StringBuffer();
		String hexChar="";
		//ANULADO los datos que se pasan para codificar, se les a�ade la clave de consejer�as.
		data += key;
		buffer = data.getBytes();
        md = MessageDigest.getInstance("SHA1");
        md.update(buffer);
        digest = md.digest();
        for(byte aux : digest) {
            int b = aux & 0xff; // Convierte de unsigned a integer, respetando signo (integer tiene signo, y podr�a considerar que un byte es negativo)
            hexChar=Integer.toHexString(b);
            hash.append(hexChar.length()==1?("0"+hexChar):hexChar); //Left Padding, as� se rellena correctamente.
        }
        
        return hash.toString().toUpperCase();
	}
}
