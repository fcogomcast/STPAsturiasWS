package es.tributasenasturias.GenerarDocs600ANCERTutils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
/**
 * Logger realiza las tareas de abrir, escribir y cerrar el fichero de log del web service del cálculo del modelo 600
 * 
 * Se utilizará el log, siempre que la preferencia Debug tenga el valor 1.
 * 
 * El fichero de log se llama Application.log y se encuentra en la carpeta CalculoModelo600. 
 *
 */
public final class Logger 
{
	private final static String LOG_FILE = "Application.log";
	private final static String LOG_DIR = "proyectos//WSGenerarDocs600ANCERT";
	public enum LEVEL {TRACE, DEBUG, INFO, WARNING, ERROR}
	// Método sincronizado. 
	/**
	 * Método que realiza log.
	 * 
	 */
	public final synchronized static void doLog(String message, LEVEL level)
	{
		File file;
        FileWriter fichero = null;
        PrintWriter pw;

        try
        {
            Date today = new Date();
            String completeMsg = "GenerarDocs600ANCERT :: " + today + " :: " + level + " :: " + message;
            
            file = new File(LOG_DIR);
            if(file.exists() == false)
                file.mkdirs();
            
            fichero = new FileWriter(LOG_DIR + "//" + LOG_FILE, true);//true para que agregemos al final del fichero
            pw = new PrintWriter(fichero);
            
            pw.println(completeMsg);
            
        }
        catch (IOException e)
        {
            System.out.println("Error escribiendo log '"+message+"' -> "+e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            if(fichero != null)
            {
                try
                {
                    fichero.close();
                }
                catch(Exception e)
                {
                    System.out.println("Error cerrando fichero de log -> "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
	}
	
	public final static void trace(String message)
	{
		doLog(message,LEVEL.TRACE);
	}
	
	public final static void trace(StackTraceElement[] stackTraceElements)
	{
		 if (stackTraceElements == null)
	            return;

	        for (int i = 0; i < stackTraceElements.length; i++)
	        {
	            doLog("StackTrace -> " + stackTraceElements[i].getFileName() + " :: " + stackTraceElements[i].getClassName() + " :: " + stackTraceElements[i].getFileName() + " :: " + stackTraceElements[i].getMethodName() + " :: " + stackTraceElements[i].getLineNumber(),LEVEL.TRACE);
	        }
	}
	
	public final static void debug(String message)
	{
		doLog(message,LEVEL.DEBUG);
	}
	
	public final static void info(String message)
	{
		doLog(message,LEVEL.INFO);
	}
	
	public final static void warning(String message)
	{
		doLog(message,LEVEL.WARNING);
	}
	
	public final static void error(String message)
	{
		doLog(message,LEVEL.ERROR);
	}
}