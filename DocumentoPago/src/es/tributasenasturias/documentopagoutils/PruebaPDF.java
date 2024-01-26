package es.tributasenasturias.documentopagoutils;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import java.io.*;
import java.io.File;


import es.tributasenasturias.documentos.util.Base64;
import es.tributasenasturias.documentos.util.XMLUtils;

public class PruebaPDF {

	public static void main(String[] args) {

        try {                   	         	       
        	String doc = new String ();        	
	        
        	
	        byte [] docDecodificado = Base64.decode(doc.toCharArray());	        	        	        	      	       	          	        	   	         	        	   
	        ByteArrayOutputStream resulByteArray= new ByteArrayOutputStream ();
	        ByteArrayOutputStream resulByteArray2= new ByteArrayOutputStream ();
	        resulByteArray.write(docDecodificado);
	        	        	       
	        String resultado = "";	        	       
	        
	        resultado = PdfComprimidoUtils.comprimirPDF(resulByteArray);	      	        	      	       	      	        		       
	        char[] decodifico = doc.toCharArray();
	        byte [] resulDecodifico= Base64.decode(decodifico);
	        
	        resulByteArray2 = PdfComprimidoUtils.decompress(resulDecodifico);
	        
	        
	        //resulByteArray2 = PdfComprimidoUtils.descomprimirPDF(resultado);
	        
	        FileOutputStream fichero = new FileOutputStream("f:\\externos\\daviddsa\\zipenbase64_v17.pdf");
	        fichero.write (resulByteArray2.toByteArray(),0,resulByteArray2.toByteArray().length);
	
	        //docDecodificado = resulByteArray.write(fichero.);
	        
	        //System.out.println (resultado);
	      
	        /*	        
	        byte [] docDecodificado2 = Base64.decode(resultado.toCharArray());
	        
	        
	        
	        FileOutputStream fichero = new FileOutputStream("f:\\externos\\daviddsa\\zipenbase64_v8.zip");	        	        
	    	    
	        //ByteArrayOutputStream resulByteArray2= new ByteArrayOutputStream ();
	        //resulByteArray2.write(docDecodificado2);	        	      
	        
	        //System.out.println (resultado);
	        
	        fichero.write (docDecodificado2,0,docDecodificado2.length);
	        
	        
	        
	        //fichero.write (docZip,0,docZip.length);
	        
	        
	    */
	        
	        //ByteArrayOutputStream resulByteArray= new ByteArrayOutputStream ();	    
	        //resulByteArray.write(docZip);
	        	        
	        
	        
	        //String documentzippeado = new String ();
	        
	        
	        
	        //documentzippeado = PdfComprimidoUtils.comprimirPDF(resulByteArray);
	        
	        
		    //System.out.println (documentzippeado);
	        
	        

//	       // fichero.write
	        
	        
	    //    BufferedOutputStream bufferedOutput = new BufferedOutputStream(new FileOutputStream("f:\\externos\\daviddsa\\prueba5.txt"));            
	        	       
	        //Start writing to the output stream           
	      //  bufferedOutput.write(docDecodificado);
	    	
	        
	        
	        /*
	        FileWriter fichero = null;
	        PrintWriter pw = null;
	        try
	        {
	        	
	        	
	            fichero = new FileWriter("f:\\externos\\daviddsa\\fichero.pdf");
	            fichero.write(doc.toCharArray());
	            fichero.w
	            
	            pw = new PrintWriter(fichero);

	            for (int i = 0; i < 10; i++)
	                pw.println("Linea " + i);
	          

	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	           try {
	           // Nuevamente aprovechamos el finally para 
	           // asegurarnos que se cierra el fichero.
	           if (null != fichero)
	              fichero.close();
	           } catch (Exception e2) {
	              e2.printStackTrace();
	           }
	        }

	        
	        */
	         	   	             	          
	        /*
	            ByteArrayOutputStream resulByteArray= new ByteArrayOutputStream ();	    
		         resulByteArray.write(docDecodificado);
	   		       
		         File fichero;
		         fichero.
		         
	             fichero = new File("c:\\externos\\daviddsa\\miprueba2.pdf");
	             fichero.createNewFile();
	             fichero.
	             
	             
	             if(fichero.exists() == false)
	                 fichero.mkdirs();
		         
	             
	      //       FileOutputStream  outputStream = new FileOutputStream(DIRECTORIO_PREFERENCIAS + "//" + FICHERO_PREFERENCIAS);
	             
	             

		         
	         }
	         */
	         
	         //ByteArrayOutputStream resulByteArray= new ByteArrayOutputStream ("f:\\externos\\davidsa\\miprueba2.pdf");	    
	         //resulByteArray.write(docDecodificado);
	         
	         /*
	         resulByteArray.write(doc.getBytes());
	         
	         
	         String documentzippeado = new String();
	         documentzippeado = PdfComprimidoUtils.comprimirPDF(resulByteArray);
	         
	        */
	        
	        //String docDecodificado = new String (Base64.decode(doc.toCharArray()));	       
	     /*
	          byte[] docZip = XMLUtils.compressGZIP(XMLUtils.borraEspacios(docDecodificado));	       
	        String documentoZip = new String(Base64.encode(docZip));
	       */
//	        System.out.println (docDecodificado);
                       	
        } catch (Exception e) {
        	System.out.println ("Error" + e.getMessage());
		}        
               
	}
	
	
	

public static byte[] fileToByteArray(String fileName) {
    try {
      File f = new File(fileName);
      FileInputStream in = new FileInputStream(f);
      byte[] bytes = new byte[(int)f.length()];
      int c = -1;
      int ix = 0;
      while ((c = in.read()) > -1) {
        System.out.println(c);
        bytes[ix] = (byte)c;
        ix++;
      }
      in.close();
      return bytes;
    }
    catch(IOException e){
      e.printStackTrace();
      return null;
    }
  }

	
}
