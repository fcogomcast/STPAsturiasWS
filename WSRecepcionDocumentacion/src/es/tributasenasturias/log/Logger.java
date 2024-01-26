package es.tributasenasturias.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import es.tributasenasturias.servicios.RecepcionDocumentos.preferencias.Preferencias;
/**
 * Peque�a clase de log que se utilizar� para mantener el log de aplicaci�n, no el de mensajes SOAP. 
 * 
 *
 */
public class Logger 
{
	private final static String LOG_FILE = "Application.log";
	private final static String LOG_DIR = "proyectos/WSRecepcionDocumentacion";
	private  Preferencias pr=null;
	private  String idLlamada = null;
	
	public Logger()
	{
		
	}
	public Logger(String idLlamada)
	{
		this.idLlamada=idLlamada;
	}
	public enum LEVEL {TRACE, DEBUG, INFO, WARNING, ERROR,ALL}
	//M�todo privado que comprueba el nivel de detalle de log definido en el fichero de preferencias
	// y devuelve si en base a un nivel de detalle de log que se indica por par�metros se han de 
	// imprimir los mensajes o no.
	private boolean esImprimible(LEVEL nivel)
	{
		boolean res=false;
		try
		{
			pr= Preferencias.getPreferencias();
			String modo = pr.getModoLog();
			if (modo.equalsIgnoreCase(LEVEL.ALL.toString())) //Imprimir todo. Siempre verdadero.
			{res=true;}
			else if (modo.equalsIgnoreCase(LEVEL.DEBUG.toString()))
			{
				if (nivel.equals(LEVEL.DEBUG)|| nivel.equals(LEVEL.WARNING)|| nivel.equals(LEVEL.ERROR))
				{res=true;}
				else {res=false;}
			}
			else if (modo.equalsIgnoreCase(LEVEL.WARNING.toString()))
			{
				if (nivel.equals(LEVEL.WARNING)|| nivel.equals(LEVEL.ERROR))
				{res=true;}
				else {res=false;}
			}
			else if (modo.equalsIgnoreCase(LEVEL.ERROR.toString()))
			{
				if (nivel.equals(LEVEL.ERROR))
				{res=true;}
				else {res=false;}
			}
			else if (modo.equalsIgnoreCase(LEVEL.INFO.toString()))
			{
				if (nivel.equals(LEVEL.INFO) || nivel.equals(LEVEL.DEBUG)|| nivel.equals(LEVEL.WARNING)|| nivel.equals(LEVEL.ERROR))
				{res=true;}
				else {res=false;}
			}
			else if (modo.equalsIgnoreCase(LEVEL.TRACE.toString()))
			{
				if (nivel.equals(LEVEL.TRACE))
				{res=true;}
				else {res=false;}
			}
		}
		catch (Exception ex) // En principio s�lo deber�a ocurrir porque hay un error al abrir el fichero
							// de preferencias. En ese caso, devolvemos true para que imprima todo se pase el 
							// par�metro que se pase, inclu�do error.
		{
			res=true;
		}
		return res;
	}
	// M�todo sincronizado. 
	/**
	 * M�todo que realiza log.
	 * 
	 */
	public final synchronized void doLog(String message, LEVEL level)
	{
		File file;
        FileWriter fichero = null;
        PrintWriter pw=null;
        String cPrefijo="";
        try
        {
        	if (idLlamada!=null)
        	{
        		cPrefijo = "Id:"+idLlamada+"::";
        	}
        	else
        	{
        		cPrefijo="";
        	}
            Date today = new Date();
            String completeMsg = "WSRecepcionDocumentacion :: "+cPrefijo + today + " :: " + level + " :: " + message;
            
            file = new File(LOG_DIR);
            if(file.exists() == false)
                if (file.mkdirs()==false)
                	{
                		throw new IOException("No se puede crear el directorio de log."); 
                	}
            
            fichero = new FileWriter(LOG_DIR + "/" + LOG_FILE, true);//true para que agregemos al final del fichero
            if (fichero!=null)
            {
            	pw = new PrintWriter(fichero);
            
            	pw.println(completeMsg);
            }
            
        }
        catch (IOException e)
        {
            System.out.println("WSRecepcionDocumentacion::Error escribiendo log '"+message+"' -> "+e.getMessage());
            e.printStackTrace();
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
            	if(fichero != null)
                {
            		fichero.close();
                }
            }
            catch(Exception e)
            {
                System.out.println("WSRecepcionDocumentacion::Error cerrando fichero de log -> "+e.getMessage());
                e.printStackTrace();
            }
        }
	}
	
	public final void trace(String message)
	{	
		if (esImprimible(LEVEL.TRACE))
		{doLog(message,LEVEL.TRACE);}
	}
	
	public final void trace(StackTraceElement[] stackTraceElements)
	{
		 if (stackTraceElements == null)
	            return;
		 if (esImprimible(LEVEL.TRACE))
		 {
	        for (int i = 0; i < stackTraceElements.length; i++)
	        {
	            doLog("StackTrace -> " + stackTraceElements[i].getFileName() + " :: " + stackTraceElements[i].getClassName() + " :: " + stackTraceElements[i].getFileName() + " :: " + stackTraceElements[i].getMethodName() + " :: " + stackTraceElements[i].getLineNumber(),LEVEL.TRACE);
	        }
		 }
	}
	
	public final void debug(String message)
	{
		if (esImprimible(LEVEL.DEBUG))
		{doLog(message,LEVEL.DEBUG);}
	}
	
	public final void info(String message)
	{ // Info siempre se muestra.
		doLog(message,LEVEL.INFO); 
	}
	
	public final  void warning(String message)
	{
		if (esImprimible(LEVEL.WARNING))
		{doLog(message,LEVEL.WARNING);}
	}
	
	public final void error(String message)
	{
		if (esImprimible(LEVEL.ERROR))
		{doLog(message,LEVEL.ERROR);}
	}
	/**
	 * @return the idLlamada
	 */
	public synchronized final String getIdLlamada() {
		return idLlamada;
	}
	/**
	 * @param idLlamada the idLlamada to set
	 */
	public synchronized final void setPrefijo(String idLlamada) {
		this.idLlamada = idLlamada;
	}
}