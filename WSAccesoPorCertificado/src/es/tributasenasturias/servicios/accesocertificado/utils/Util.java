package es.tributasenasturias.servicios.accesocertificado.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

/**
 * Clase que proporciona servicios de utilidad gen�ricos.
 * @author crubencvs
 *
 */
public class Util {
	
	private static String hexEncode( byte[] aInput){
		int MASK_240 = 0xf0;
		int SHIFT_DIVIDIR_16 = 4;
		int MASK_15 = 0x0f;
	    StringBuilder result = new StringBuilder();
	    char[] digits = {'0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f'};
	    for ( int idx = 0; idx < aInput.length; ++idx) {
	      byte b = aInput[idx];
	      result.append( digits[ (b&MASK_240) >> SHIFT_DIVIDIR_16 ] );
	      result.append( digits[ b&MASK_15] );
	    }
	    return result.toString();
	  }
	/**
	 * Recupera un checkcum basado en un identificador �nico. Se asegura que el identificador es �nico.
	 */
	public static String getIdLlamada()
	{
		UUID id=UUID.randomUUID();
		String identificador=null;
		byte[] mensaje = id.toString().getBytes();
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(mensaje);
			identificador=hexEncode(md.digest());

		}catch(NoSuchAlgorithmException nsae){
			identificador="";
		}
		return identificador;
	}
	/**
	 * Genera un SOAP Fault
	 * @param msg Mensaje SOAP sobre el que se generar� el error. Se utilizar� para a�adirle la secci�n SOAP Fault 
	 * @param pFaultString Texto de raz�n de error (aparecer� en el faultString  )
	 */
	public static final SOAPFaultException createSOAPFaultException(String pFaultString) throws SOAPException
	 {
		 try
		 {
		 SOAPFaultException sex=null;
		 SOAPFault fault = SOAPFactory.newInstance().createFault();
		 fault.setFaultString(pFaultString);
		 fault.setFaultCode(new QName(SOAPConstants.URI_NS_SOAP_ENVELOPE, "WSAccesoPorCertificado"));
		 sex= new SOAPFaultException(fault);
		 return sex;
		 }
		 catch (Exception ex)
		 {
			 throw new SOAPException ("WSAccesoPorCertificado:==> Error grave al construir la excepci�n SOAP." + ex.getMessage());
		 }
	} 
}
