package es.tributasenasturias.webservice;

import es.tributasenasturias.business.Cifrar;
import es.tributasenasturias.business.Descifrar;
import es.tributasenasturias.utilsProgramaAyuda.Logger;

public class Test {

	/**
	 * @param args
	 */
	public static void main (String []args  ) {
 	   
    	try
        {	
    		String datos = "ddddddiiiiiipppp";
    		Logger.info("entra en el servicio");
		
            if(datos == null || datos.length() == 0)
            {
                throw new Exception("El campo xmlData es obligatorio.");
            }
            // Se descifra la entrada
            
            
            // Se cifra la salida
            Cifrar cifrar = new Cifrar("hoooola");
            
            
            Logger.info("cifrando la respuesta");
            String xmlDataCifrada = cifrar.getResultado();
            
            if (cifrar.getCodResultado() == 1) {    
            	System.out.println ("descifrado:");
            	//	return ComponerRespuesta.getRespuestaCifrada(this.v_numAutoliquidacion, "Error al cifrar la salida. "+xmlDataCifrada);
            }
            
            Descifrar descifrar2 = new Descifrar(xmlDataCifrada);                                  
            if (descifrar2.getCodResultado() == 1) {
            	System.out.println ("descifrado:");            	          
            	
            }
                                   
            System.out.println ("datos descifrados:" + descifrar2.getResultado());	
                       

        }
        catch (Exception e)
        {        
        	System.out.println (e.getMessage());
        
        }   		
            		
            		
    }


}
