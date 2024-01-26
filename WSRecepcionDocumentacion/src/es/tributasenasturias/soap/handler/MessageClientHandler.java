package es.tributasenasturias.soap.handler;

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

 
/**
 * Clase manejadora de mensajes soap
 * 
 * @author Andres
 *
 */
public class MessageClientHandler implements SOAPHandler<SOAPMessageContext> 
{
	private final static String LOG_FILE = "SOAP_CLIENT.log";
	private final static String LOG_DIR = "proyectos/WSRecepcionDocumentacion";
	private String sesion;
	public MessageClientHandler (String idSesion)
	{
		this.sesion = idSesion;
	}
	
    public boolean handleMessage(SOAPMessageContext messageContext) 
    {
    	log(messageContext);
    	return true;
    }
 
    public Set<QName> getHeaders() 
    {
        return Collections.emptySet();
    }
 
    public boolean handleFault(SOAPMessageContext messageContext) 
    {	log(messageContext);
        return true;
    }
 
    public void close(MessageContext context) 
    {
    }
    
    private void log(SOAPMessageContext messageContext) 
    { 	
        SOAPMessage msg = messageContext.getMessage();
        FileWriter out = null;
        PrintWriter pw=null;
        synchronized (this)
        {
	     try 
	     {
	    	 Boolean salida = (Boolean) messageContext.get(javax.xml.ws.handler.MessageContext.MESSAGE_OUTBOUND_PROPERTY);
	    	 String direccion=(salida)?"Env�o":"Recepci�n"; // La propiedad nos dice si el mensaje es de salida o no.
	    	 //obtenemos el array de bytes que se corresponde con el mensaje soap
	    	 ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
	         msg.writeTo(byteArray);
	         
	         //componemos la linea del log
	         String soapMessage = new String(byteArray.toByteArray());
	         Date today = new Date();
	         String log;
	         if (sesion!=null)
	         {
	        	 log = "WSRecepcionDocumentacion::SOAP_CLIENT ::" +"::"+direccion+" :: " + today + " :: " + soapMessage+" :: "+ this.sesion;
	         }
	         else
	         {
	        	 log = "WSRecepcionDocumentacion::SOAP_CLIENT ::" +"::"+direccion+" :: " + today + " :: " + soapMessage;
	         }
	         
	         out = new FileWriter(LOG_DIR+"/"+LOG_FILE,true);
	         if (out!=null)
	         {
	        	 pw = new PrintWriter(out);
	        	 pw.println(log);
	         }
	     } 
	     catch (SOAPException ex) 
	     {
	         System.out.println("WSRecepcionDocumentacion::SOAP Exception escribiendo mensaje a fichero: "+ex.getMessage());
	         ex.printStackTrace();
	     } 
	     catch (IOException ex) 
	     {
	    	 System.out.println("WSRecepcionDocumentacion::IO Exception escribiendo mensaje a fichero: "+ex.getMessage());
	    	 ex.printStackTrace();
	     }
	     finally
	     {
	    	try
         	{
	    		if (pw!=null)
	    		{
	    			pw.close();
	    		}
         	}
         	catch (Exception e) // En principio no deber�a devolver, nunca, una excepci�n. Se controla 
         						// por si hay cambios en la implementaci�n.
         	{
         		System.out.println ("WSRecepcionDocumentacion::Error cerrando flujo de impresi�n: " + e.getMessage());
         		e.printStackTrace();
         	}
            
            try
            {
            	if(out != null)
                {
            		out.close();
                }
            }
            catch(Exception e)
            {
                System.out.println("WSRecepcionDocumentacion::Error cerrando fichero de log -> "+e.getMessage());
                e.printStackTrace();
            }
	     }
        }
    }
}
