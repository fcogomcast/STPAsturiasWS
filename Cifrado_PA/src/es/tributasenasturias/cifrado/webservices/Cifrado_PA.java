package es.tributasenasturias.cifrado.webservices;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@BindingType("http://www.w3.org/2003/05/soap/bindings/HTTP/")
@WebService(serviceName="Cifrado_PA", portName="Cifrado_PA")
public class Cifrado_PA
{
  public String Descifrar(String clave, String texto)
  {
    String str2;
    try
    {
      String str1;
      byte[] digestOfPassword = clave.getBytes();

      byte[] keyBytes = new byte[24];

      for (int i = 0; i < digestOfPassword.length; ++i)
        keyBytes[i] = digestOfPassword[i];

      int j = 0; for (int k = 16; j < 8; ) {
        keyBytes[(k++)] = keyBytes[(j++)];
      }

      SecretKey key = new SecretKeySpec(keyBytes, "TripleDES");
      IvParameterSpec iv = new IvParameterSpec(new byte[8]);
      Cipher cipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");

      cipher.init(2, key, iv);

      byte[] plainTextBytes = new BASE64Decoder().decodeBuffer(texto);

      byte[] cipherText = cipher.doFinal(plainTextBytes);

      return new String(cipherText, "iso-8859-1");
    } catch (Exception e) {
      e.printStackTrace();
      str2 = e.getMessage(); } return str2;
  }

  public String Cifrar(String clave, String texto)
  {
    String str2;
    try
    {
      String str1;
      byte[] digestOfPassword = clave.getBytes();

      byte[] keyBytes = new byte[24];
      for (int i = 0; i < digestOfPassword.length; ++i)
        keyBytes[i] = digestOfPassword[i];

      int j = 0; for (int k = 16; j < 8; ) {
        keyBytes[(k++)] = keyBytes[(j++)];
      }

      SecretKey key = new SecretKeySpec(keyBytes, "TripleDES");
      IvParameterSpec iv = new IvParameterSpec(new byte[8]);
      Cipher cipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");

      cipher.init(1, key, iv);

      byte[] plainTextBytes = texto.getBytes("iso-8859-1");
      byte[] cipherText = cipher.doFinal(plainTextBytes);
      String encodedCipherText = new BASE64Encoder().encode(cipherText);

      return encodedCipherText;
    } catch (Exception e) {
      e.printStackTrace();
      str2 = e.getMessage(); } return str2;
  }
}