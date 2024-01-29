/**
 * 
 */
package es.tributasenasturias.webservices.Certificados.soap.handler;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.webservices.Certificados.sesion.GestorIdLlamada;
import es.tributasenasturias.webservices.Certificados.utils.Log.GenericAppLogger;
import es.tributasenasturias.webservices.Certificados.utils.Log.TributasLogger;

/**
 * @author crubencvs
 * Clase que hará log de los mensajes entrantes.
 */
public class ClientLogHandler implements SOAPHandler<SOAPMessageContext>{

	private static final String LOG_FILE ="SOAP_CLIENT.log";
	private static final String LOG_DIR="proyectos/ConsultaCertificados";
	private void log(SOAPMessageContext context)
	{
        SOAPMessage msg = context.getMessage();
        synchronized (this)
        {
	     try 
	     {
	    	 Boolean salida = (Boolean) context.get(javax.xml.ws.handler.MessageContext.MESSAGE_OUTBOUND_PROPERTY);
	    	 String direccion=(salida)?"Envío":"Recepción"; // La propiedad nos dice si el mensaje es de salida o no.
	    	 //obtenemos el array de bytes que se corresponde con el mensaje soap
	    	 ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
	         msg.writeTo(byteArray);
	         
	         doLog(new String(byteArray.toByteArray()), direccion);
	     } 
	     catch (SOAPException ex) 
	     {
	    	 GenericAppLogger log = new TributasLogger();
     		 log.error("SOAP Exception escribiendo mensaje a fichero: "+ex.getMessage());
	         System.out.println("SOAP Exception escribiendo mensaje a fichero: "+ex.getMessage());
	         ex.printStackTrace();
	     } 
	     catch (IOException ex) 
	     {
	    	 GenericAppLogger log = new TributasLogger();
     		 log.error("IO Exception escribiendo mensaje a fichero: "+ex.getMessage());
	    	 System.out.println("IO Exception escribiendo mensaje a fichero: "+ex.getMessage());
	    	 ex.printStackTrace();
	     }
        }
	}
	
	public void doLog(String data,  String direccion)
	{
        FileWriter out = null;
        PrintWriter pw=null;
        String endl=System.getProperty("line.separator");
        synchronized (this)
        {
	     try 
	     {
	         //componemos la linea del log
	         Date today = new Date();
	         String log = "=========================>ConsultaCertificado::SOAP_CLIENT::Inicio ::+"+direccion+"::"+today+"::"+ GestorIdLlamada.getIdLlamada()+endl;
	         log += data + endl;
	         log +="=========================>ConsultaCertificado::SOAP_CLIENT::Fin ::"+direccion +"::" + today + "::"+ GestorIdLlamada.getIdLlamada()+endl;
	         
	         out = new FileWriter(LOG_DIR+"/"+LOG_FILE,true);
	         if (out!=null)
	         {
	        	 pw = new PrintWriter(out);
	        	 pw.println(log);
	         }
	     } 
	     catch (IOException ex) 
	     {
	    	 GenericAppLogger log = new TributasLogger();
     		 log.error("IO Exception escribiendo mensaje a fichero: "+ex.getMessage());
	    	 System.out.println("IO Exception escribiendo mensaje a fichero: "+ex.getMessage());
	    	 ex.printStackTrace();
	     }
	     finally
	     {
	    	if (pw!=null)
	    	{
	    		try {pw.close();}
	    		catch (Exception e){
	    			GenericAppLogger log = new TributasLogger();
            		log.error("Error cerrando flujo de impresión: " + e.getMessage());
            		System.out.println ("Error cerrando flujo de impresión: " + e.getMessage());
            		e.printStackTrace();
	    		}
	    	}
            if(out != null)
            {
                try
                {
                    out.close();
                }
                catch(Exception e)
                {
                	GenericAppLogger log = new TributasLogger();
                	log.error("Error cerrando fichero de log -> "+e.getMessage());
                    System.out.println("Error cerrando fichero de log -> "+e.getMessage());
                    e.printStackTrace();
                }
            }
	     }
        }
	}
	@Override
	public void close(MessageContext context) {
	}

	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		//Hemos de escribir igualmente.
		log (context);
		return true;
	}

	@Override
	public synchronized boolean handleMessage(SOAPMessageContext context) {
		 	
		log (context);	
		return true;
	}
	

}
