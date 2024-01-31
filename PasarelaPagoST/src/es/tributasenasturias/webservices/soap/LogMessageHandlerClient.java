package es.tributasenasturias.webservices.soap;

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
public class LogMessageHandlerClient implements SOAPHandler<SOAPMessageContext> 
{
	private final static String LOG_FILE = "SOAP_CLIENT.log";
	private final static String LOG_DIR = "proyectos/PasarelaPagoST";
	private String sesion;
	public LogMessageHandlerClient (String idSesion)
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
    	//obtenemos la ip origen
    	//HttpServletRequest request = (HttpServletRequest)messageContext.get(MessageContext.SERVLET_REQUEST);        
        //String ip = request.getRemoteAddr();
    	
        SOAPMessage msg = messageContext.getMessage();
        FileWriter out = null;
        PrintWriter pw=null;
	     try 
	     {
	    	 Boolean salida = (Boolean) messageContext.get(javax.xml.ws.handler.MessageContext.MESSAGE_OUTBOUND_PROPERTY);
	    	 String direccion=(salida)?"Envío":"Recepción"; // La propiedad nos dice si el mensaje es de salida o no.
	    	 //obtenemos el array de bytes que se corresponde con el mensaje soap
	    	 ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
	         msg.writeTo(byteArray);
	         
	         //componemos la linea del log
	         String soapMessage = new String(byteArray.toByteArray());
	         Date today = new Date();
	         String log;
	         if (sesion!=null)
	         {
	        	 log = "Pasarela Pago::SOAP_CLIENT ::" +"::"+direccion+" :: " + today + " :: " + soapMessage+" :: "+ this.sesion;
	         }
	         else
	         {
	        	 log = "Pasarela Pago::SOAP_CLIENT ::" +"::"+direccion+" :: " + today + " :: " + soapMessage;
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
	         System.out.println("SOAP Exception escribiendo mensaje a fichero: "+ex.getMessage());
	         ex.printStackTrace();
	         //System.out.println(ex.getStackTrace());
	     } 
	     catch (IOException ex) 
	     {
	    	 System.out.println("IO Exception escribiendo mensaje a fichero: "+ex.getMessage());
	    	 ex.printStackTrace();
	    	 //System.out.println(ex.getStackTrace());
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
         	catch (Exception e) // En principio no debería devolver, nunca, una excepción. Se controla 
         						// por si hay cambios en la implementación.
         	{
         		System.out.println ("Error cerrando flujo de impresión: " + e.getMessage());
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
                System.out.println("Error cerrando fichero de log -> "+e.getMessage());
                e.printStackTrace();
            }
	     }
    }
}
