/**
 * 
 */
package es.tributasenasturias.webservice.pagopresentacion.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import es.tributasenasturias.pagopresentacionmodelo600utils.Preferencias;


/** Implementa un logger genérico.
 * @author crubencvs
 *
 */
public class GenericAppLogger implements ILoggerAplicacion {

	private String _LOG_FILE = "";
	private String _LOG_DIR = "";
	private Preferencias _pr=null;	
	private String _nombre="";
	//Método privado que comprueba el nivel de detalle de log definido en el fichero de preferencias
	// y devuelve si en base a un nivel de detalle de log que se indica por parámetros se han de 
	// imprimir los mensajes o no.
	private boolean esImprimible(LEVEL nivel)
	{
		boolean res=false;
		try
		{
			_pr= new Preferencias();
			_pr.CargarPreferencias();
			String modo = _pr.getModoLog();
			if (LEVEL.ALL.toString().equalsIgnoreCase(modo)) //Imprimir todo. Siempre verdadero.
			{res=true;}
			else if (LEVEL.DEBUG.toString().equalsIgnoreCase(modo))
			{
				if (LEVEL.DEBUG.equals(nivel)||LEVEL.INFO.equals(nivel)|| LEVEL.TRACE.equals(nivel ) || LEVEL.WARNING.equals(nivel)|| LEVEL.ERROR.equals(nivel))
				{res=true;}
				else {res=false;}
			}
			else if (LEVEL.INFO.toString().equalsIgnoreCase(modo))
			{
				if (LEVEL.INFO.equals(nivel)||  LEVEL.TRACE.equals(nivel ) ||LEVEL.WARNING.equals(nivel)|| LEVEL.ERROR.equals(nivel))
				{res=true;}
				else {res=false;}
			}
			else if (LEVEL.WARNING.toString().equalsIgnoreCase(modo))
			{
				if (LEVEL.WARNING.equals(nivel)|| LEVEL.TRACE.equals(nivel ) ||LEVEL.ERROR.equals(nivel))
				{res=true;}
				else {res=false;}
			}
			else if (modo.equalsIgnoreCase(LEVEL.TRACE.toString()))
			{
				if (LEVEL.TRACE.equals(nivel)||  LEVEL.ERROR.equals(nivel))
				{res=true;}
				else {res=false;}
			}
			else if (LEVEL.ERROR.toString().equalsIgnoreCase(modo))
			{
				if (LEVEL.ERROR.equals(nivel))
				{res=true;}
				else {res=false;}
			}
			else if (LEVEL.NONE.toString().equalsIgnoreCase(modo))
			{
				res=false;
			}
		}
		catch (Exception ex) // En principio sólo debería ocurrir porque hay un error al abrir el fichero
							// de preferencias. En ese caso, devolvemos true para que imprima todo se pase el 
							// parámetro que se pase, incluído error.
		{
			res=true;
		}
		return res;
	}
	/**
	 * Método que realiza log.
	 * 
	 */
	@Override
	public void doLog(String message, LEVEL level)
	{
		File file;
        FileWriter fichero = null;
        PrintWriter pw=null;

        try
        {
            Date today = new Date();
            String completeMsg = _nombre+" :: " + today + " :: " + level + " :: " + message;
            
            file = new File(_LOG_DIR);
            if(file.exists() == false)
                if (file.mkdirs()==false)
                	{
                		throw new IOException("No se puede crear el directorio de log para el log " + _nombre +"."); 
                	}
            
            fichero = new FileWriter(_LOG_DIR + "/" + _LOG_FILE, true);//true para que agregemos al final del fichero
            if (fichero!=null)
            {
            	pw = new PrintWriter(fichero);
            
            	pw.println(completeMsg);
            }
            
        }
        catch (IOException e)
        {
            System.out.println("Error escribiendo log " + _nombre + " :'"+message+"' -> "+e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            if(fichero != null)
            {
            	try
            	{
            		pw.close();
            	}
            	catch (Exception e) // En principio no debería devolver, nunca, una excepción. Se controla 
            						// por si hay cambios en la implementación.
            	{
            		System.out.println ("Error cerrando flujo de impresión para el log "+ _nombre + ": " + e.getMessage());
            		e.printStackTrace();
            	}
                try
                {
                    fichero.close();
                }
                catch(Exception e)
                {
                    System.out.println("Error cerrando fichero de log " + _nombre + "-> "+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
	}
	
	/* (non-Javadoc)
	 * @see es.tributasenasturias.webservices.Certificados.utils.Log.ILoggerAplicacion#error(java.lang.String)
	 */
	@Override
	public void error(String msg) {
		if (esImprimible(LEVEL.ERROR))
		{doLog(msg,LEVEL.ERROR);}
	}

	/* (non-Javadoc)
	 * @see es.tributasenasturias.webservices.Certificados.utils.Log.ILoggerAplicacion#debug(java.lang.String)
	 */
	@Override
	public void debug(String msg) {
		if (esImprimible(LEVEL.DEBUG))
		{doLog(msg,LEVEL.DEBUG);}
	}

	/* (non-Javadoc)
	 * @see es.tributasenasturias.webservices.Certificados.utils.Log.ILoggerAplicacion#info(java.lang.String)
	 */
	@Override
	public void info(String msg) {
		if (esImprimible(LEVEL.INFO))
		{doLog(msg,LEVEL.INFO);}

	}

	/* (non-Javadoc)
	 * @see es.tributasenasturias.webservices.Certificados.utils.Log.ILoggerAplicacion#warning(java.lang.String)
	 */
	@Override
	public void warning(String msg) {
		if (esImprimible(LEVEL.WARNING))
		{doLog(msg,LEVEL.WARNING);}
	}
	@Override
	public final void trace(String message)
	{	
		if (esImprimible(LEVEL.TRACE))
		{doLog(message,LEVEL.TRACE);}
	}
	@Override
	public final void trace(StackTraceElement[] stackTraceElements)
	{
		 if (stackTraceElements == null)
	            return;
		 if (esImprimible(LEVEL.TRACE))
		 {
	        for (int i = 0; i < stackTraceElements.length; i++)
	        {
	            doLog("StackTrace -> " + stackTraceElements[i].getFileName() + " :: " + stackTraceElements[i].getClassName() + " :: " + stackTraceElements[i].getFileName() + " :: " + stackTraceElements[i].getMethodName() + " :: " + stackTraceElements[i].getLineNumber(),LEVEL.INFO);
	        }
		 }
	}
	protected String getLogFile() {
		return _LOG_FILE;
	}
	protected void setLogFile(String logFile) {
		_LOG_FILE = logFile;
	}
	protected String getLogDir() {
		return _LOG_DIR;
	}
	protected void setLogDir(String logDir) {
		_LOG_DIR = logDir;
	}
	protected String getNombre() {
		return _nombre;
	}
	protected void setNombre(String nombre) {
		_nombre = nombre;
	}
}
