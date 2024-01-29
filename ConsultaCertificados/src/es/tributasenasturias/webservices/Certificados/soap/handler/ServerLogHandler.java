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

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import es.tributasenasturias.webservices.Certificados.sesion.GeneradorIdSesion;
import es.tributasenasturias.webservices.Certificados.sesion.GestorIdLlamada;
import es.tributasenasturias.webservices.Certificados.utils.Log.GenericAppLogger;
import es.tributasenasturias.webservices.Certificados.utils.Log.TributasLogger;

/**
 * @author crubencvs
 * Clase que hará log de los mensajes entrantes.
 */
public class ServerLogHandler implements SOAPHandler<SOAPMessageContext>{

	private static final String LOG_FILE ="SOAP_SERVER.log";
	private static final String LOG_DIR="proyectos/ConsultaCertificados";
	private void log(SOAPMessageContext context)
	{
		//obtenemos la ip origen
    	HttpServletRequest request = (HttpServletRequest)context.get(MessageContext.SERVLET_REQUEST);        
        String ip = request.getRemoteAddr();
    	
        SOAPMessage msg = context.getMessage();
        FileWriter out = null;
        PrintWriter pw=null;
        String endl=System.getProperty("line.separator");
        synchronized (this)
        {
	     try 
	     {
	    	 Boolean salida = (Boolean) context.get(javax.xml.ws.handler.MessageContext.MESSAGE_OUTBOUND_PROPERTY);
	    	 String direccion=(salida)?"Envío":"Recepción"; // La propiedad nos dice si el mensaje es de salida o no.
	    	 //obtenemos el array de bytes que se corresponde con el mensaje soap
	    	 ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
	         msg.writeTo(byteArray);
	         
	         //componemos la linea del log
	         String soapMessage = new String(byteArray.toByteArray());
	         Date today = new Date();
	         String log = "=========================>ConsultaCertificado::SOAP_SERVER::Inicio ::+"+direccion+"::"+ ip + " :: "+today+ "::"+ GestorIdLlamada.getIdLlamada()+endl;
	         log += soapMessage + endl;
	         log +="=========================>ConsultaCertificado::SOAP_SERVER::Fin ::"+direccion +"::" + today + "::"+ GestorIdLlamada.getIdLlamada()+ endl;
	         
	         out = new FileWriter(LOG_DIR+"/"+LOG_FILE,true);
	         if (out!=null)
	         {
	        	 pw = new PrintWriter(out);
	        	 pw.println(log);
	         }
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
	     finally
	     {
	    	if (pw!=null)
	    	 {
	    		try
            	{
            		pw.close();
            	}
            	catch (Exception e) // En principio no debería devolver, nunca, una excepción. Se controla 
            						// por si hay cambios en la implementación.
            	{
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
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (!salida) //No tiene mucho sentido, ya que se trata de un SOAP Fault de entrada
		{
			asociarIdLlamada();
		}
		//Hemos de escribir igualmente.
		log (context);
		if (salida) 
		{
			desasociarIdLlamada();
		}
		return true;
	}

	@Override
	public synchronized boolean handleMessage(SOAPMessageContext context) {
		Boolean salida = (Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (!salida)
		{
			asociarIdLlamada();
		}
		log (context);
		if (salida)
		{
			desasociarIdLlamada();
		}
		return true;
	}
	
	
	
	
	/**
	 * Asocia un Id único con la llamada actual, de tal forma que los log posteriormente lo podrán utilizar
	 */
	private void asociarIdLlamada()
	{
		GestorIdLlamada.asociarIdLlamada(GeneradorIdSesion.generaIdSesion());
	}
	/**
	 * Desasocia un Id único con la llamada actual, para liberar espacio en el hilo.
	 */
	private void desasociarIdLlamada()
	{
		GestorIdLlamada.desasociarIdLlamada();
	}

}
