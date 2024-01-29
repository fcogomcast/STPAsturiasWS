
package es.tributasenasturias.services.ws.archivodigital.utils.log.LogHandler;


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

import es.tributasenasturias.services.ws.archivodigital.Constantes;
import es.tributasenasturias.services.ws.archivodigital.utils.log.GenericAppLogger;
import es.tributasenasturias.services.ws.archivodigital.utils.log.Logger;
import es.tributasenasturias.services.ws.archivodigital.utils.Preferencias.Preferencias;




/**
 * @author noelianbb
 * Clase que hará log de los mensajes entrantes.
 */
public class LogHandler implements SOAPHandler<SOAPMessageContext>{

	private String LOG_FILE ="";
	private String LOG_DIR="";
	private void log(SOAPMessageContext context)
	{
		//obtenemos la ip origen
		String ip = "";
		try {
			
			HttpServletRequest request = (HttpServletRequest)context.get(MessageContext.SERVLET_REQUEST);        
			ip = request.getRemoteAddr();
		} catch (Exception e) {
			ip = "";
			//Excepción cuando es ejecutado por ClientLogHandler
		}

    	
        FileWriter out = null;
        PrintWriter pw=null;
        String endl=System.getProperty("line.separator");
        String idLlamada = (String) context.get(Constantes.ID_LLAMADA);
   	 	if (idLlamada==null) {
   		 System.out.println ("ArchivoDigital:: Error en manejador SOAP, no se encuentra identificador de llamada");
   		 return;
   	 	}
	   	Preferencias pref= (Preferencias) context.get(Constantes.PREFERENCIAS);
		if (pref==null) 
		{
		 System.out.println("ArchivoDigital::Error en manejador de SOAP_SERVER, no se encuentran preferencias de la llamada");
		 return;
		}
	    try 
	     {
	    	 
	    	 if ((!"DEBUG".equalsIgnoreCase(pref.getDebugSoap())) && (!"ALL".equalsIgnoreCase(pref.getDebugSoap()))) {
	    		 return;
	    	 }
	    	 SOAPMessage msg = context.getMessage();
	    	 Boolean salida = (Boolean) context.get(javax.xml.ws.handler.MessageContext.MESSAGE_OUTBOUND_PROPERTY);
	    	 String direccion=(salida)?"Envío":"Recepción"; // La propiedad nos dice si el mensaje es de salida o no.
	    	 //obtenemos el array de bytes que se corresponde con el mensaje soap
	    	 ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
	         msg.writeTo(byteArray);
	         
	         //componemos la linea del log
	         String soapMessage = new String(byteArray.toByteArray());
	         Date today = new Date();
	         String tipoLog = "";
	         
	         if (this.getClass().getName().equals(ServerLogHandler.class.getName()))
	         {
	        	 tipoLog = "SOAP_SERVER";
	        	 this.LOG_DIR= pref.getServerLogHandlerDir();
	        	 this.LOG_FILE=pref.getServerLogHandlerFile();
	         }
	         else 
	         {
	        	 tipoLog = "SOAP_CLIENT";
	        	 this.LOG_DIR= pref.getClientLogHandlerDir();
	        	 this.LOG_FILE=pref.getClientLogHandlerFile();
	         }
	         String log = "=========================>ArchivoDigital:: "+ tipoLog + "::" + idLlamada +"::Inicio ::+"+direccion+"::"+ ip + " :: "+today+endl;
	         log += soapMessage + endl;
	         log +="=========================>ArchivoDigital::" + tipoLog + "::" + idLlamada + "::Fin ::"+direccion +"::" + today + endl;
	         
	         out = new FileWriter(LOG_DIR+"/"+LOG_FILE,true);
	         if (out!=null)
	         {
	        	 pw = new PrintWriter(out);
	        	 pw.println(log);
	         }
	     } 
	     catch (SOAPException ex) 
	     {
	    	 GenericAppLogger log = new Logger(idLlamada, pref.getAppLogDir(), pref.getAppLogFile());
     		 log.error("SOAP Exception escribiendo mensaje a fichero: "+ex.getMessage());
	         System.out.println("SOAP Exception escribiendo mensaje a fichero: "+ex.getMessage());
	         ex.printStackTrace();
	     } 
	     catch (IOException ex) 
	     {
	    	 GenericAppLogger log = new Logger(idLlamada, pref.getAppLogDir(), pref.getAppLogFile());
     		 log.error("IO Exception escribiendo mensaje a fichero: "+ex.getMessage());
	    	 System.out.println("IO Exception escribiendo mensaje a fichero: "+ex.getMessage());
	    	 ex.printStackTrace();
	     }
	     finally
	     {
            if(out != null)
            {
            	try
            	{
            		pw.close();
            	}
            	catch (Exception e) // En principio no debería devolver, nunca, una excepción. Se controla 
            						// por si hay cambios en la implementación.
            	{
            		GenericAppLogger log = new Logger(idLlamada, pref.getAppLogDir(), pref.getAppLogFile());
            		log.error("Error cerrando flujo de impresión: " + e.getMessage());
            		System.out.println ("Error cerrando flujo de impresión: " + e.getMessage());
            		e.printStackTrace();
            	}
                try
                {
                    out.close();
                }
                catch(Exception e)
                {
                	GenericAppLogger log = new Logger(idLlamada, pref.getAppLogDir(), pref.getAppLogFile());
                	log.error("Error cerrando fichero de log -> "+e.getMessage());
                    System.out.println("Error cerrando fichero de log -> "+e.getMessage());
                    e.printStackTrace();
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
	/**
	 * @return the lOG_FILE
	 */
	public String getLOG_FILE() {
		return LOG_FILE;
	}
	/**
	 * @param log_file the lOG_FILE to set
	 */
	public void setLOG_FILE(String log_file) {
		LOG_FILE = log_file;
	}
	/**
	 * @return the lOG_DIR
	 */
	public String getLOG_DIR() {
		return LOG_DIR;
	}
	/**
	 * @param log_dir the lOG_DIR to set
	 */
	public void setLOG_DIR(String log_dir) {
		LOG_DIR = log_dir;
	}


}
