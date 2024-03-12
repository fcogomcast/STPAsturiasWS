/**
 * 
 */
package es.tributasenasturias.utils.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


/** Implementa el escritor de log por defecto para nuestras aplicaciones. 
 * @author crubencvs
 *
 */
public class TributasLogWriter implements ILogWriter {

		
		private NIVEL_LOG nivelObj;
		private File ficheroLog;
		private IFormateadorMensaje formateador;
		private String charsetName;
		//Crear directorios del archivo, si no existen.
		private void createDirectories(File fichero) throws LogException
		{
			//Sincronizado. Si m�s de un hilo llama a esto, podr�a haber problemas al crear los directorios.
			String directorio = fichero.getParent();
			File dir = new File(directorio);
			if (!dir.exists() ){
				if (!dir.mkdirs())
				{
					throw new LogException("Log:No se puede crear el directorio de log:" + dir.getAbsolutePath());
				}
			}
		}
		
		/**
		 * Constructor por defecto.
		 */
		private TributasLogWriter() throws LogException
		{
		}
		/**
		 * Constructor al que se le pasa el manejador de archivo de log.
		 * La codificaci�n por defecto ser� la de la plataforma en caso de escoger este 
		 * constructor.
		 * @param fichero java.io.File al que se enviar�n las l�neas de logs.
		 */
		public TributasLogWriter( File fichero) throws LogException
		{
			this();
			createDirectories(fichero);
			this.ficheroLog=fichero;
		}
		/**
		 * Constructor al que se le pasa el manejador de archivo de log y la codificaci�n
		 * en que se guardar�n los mensajes de los. La codificaci�n ser� una de las reconocidas por 
		 * el sistema, como ISO-8859-1, UTF-8, etc.
		 * @param fichero java.io.File al que se enviar�n las l�neas de logs.
		 * @param charsetName String en el que se indica el juego de caracteres en que se incluir�n las entradas al log.
		 */
		public TributasLogWriter( File fichero,String charsetName) throws LogException
		{
			this();
			createDirectories(fichero);
			this.ficheroLog=fichero;
			this.charsetName=charsetName;
			
		}
		/**
		 * Constructor al que se le pasa el manejador de archivo y el nivel de log que utilizar� para 
		 * comprobar si se escribe un mensaje o no (ver {@link NIVEL_LOG}).
		 * @param fichero Nombre de fichero de log
		 * @param nivel Nivel de log objetivo a imprimir.
		 */
		public TributasLogWriter( File fichero, NIVEL_LOG nivel) throws LogException
		{
			this(fichero);
			this.nivelObj = nivel;
		}
		/**
		 * Constructor al que se le pasa el manejador de archivo, el nivel de log que deseamos
		 * que utilizar� para comprobar si se escribe un mensaje o no (ver {@link NIVEL_LOG} ) as� como la codificaci�n
		 * que se utilizar� para escribir los mensajes en el log. 
		 * @param fichero Nombre de fichero de log
		 * @param nivel Nivel de log objetivo a imprimir.
		 * @param charsetName String que contiene el juego de caracteres en que se guardar�n las entradas al log.
		 */
		public TributasLogWriter( File fichero, NIVEL_LOG nivel, String charsetName) throws LogException
		{
			this(fichero,charsetName);
			this.nivelObj = nivel;
		}
		//M�todo privado que comprueba el nivel de detalle de log definido en el fichero de preferencias
		// y devuelve si en base a un nivel de detalle de log que se indica por par�metros se han de 
		// imprimir los mensajes o no.
		private boolean esImprimible(NIVEL_LOG nivel)
		{
			boolean res=false;
			try
			{	
				if (nivel==null)
				{
					nivel=NIVEL_LOG.INFO;//Si no se especifica nivel, se usa INFO.
				}
				if (NIVEL_LOG.ALL.equals(nivelObj)) //Imprimir todo. Siempre verdadero.
				{res=true;}
				else if (NIVEL_LOG.DEBUG.equals(nivelObj))
				{
					if (NIVEL_LOG.DEBUG.equals(nivel)||NIVEL_LOG.INFO.equals(nivel)|| NIVEL_LOG.WARNING.equals(nivel)||NIVEL_LOG.TRACE.equals(nivel)|| NIVEL_LOG.ERROR.equals(nivel))
					{res=true;}
					else {res=false;}
				}
				else if (NIVEL_LOG.INFO.equals(nivelObj))
				{
					if (NIVEL_LOG.INFO.equals(nivel)|| NIVEL_LOG.WARNING.equals(nivel)|| NIVEL_LOG.TRACE.equals(nivel)|| NIVEL_LOG.ERROR.equals(nivel))
					{res=true;}
					else {res=false;}
				}
				else if (NIVEL_LOG.WARNING.equals(nivelObj))
				{
					if (NIVEL_LOG.WARNING.equals(nivel)|| NIVEL_LOG.TRACE.equals(nivel) || NIVEL_LOG.ERROR.equals(nivel))
					{res=true;}
					else {res=false;}
				}
				else if (NIVEL_LOG.TRACE.equals(nivelObj))
				{
					if (NIVEL_LOG.TRACE.equals(nivel)|| NIVEL_LOG.ERROR.equals(nivel))
					{res=true; } 
					else { res=false;}
				}
				else if (NIVEL_LOG.ERROR.equals(nivelObj))
				{
					if (NIVEL_LOG.ERROR.equals(nivel))
					{res=true;}
					else {res=false;}
				}
				else if (NIVEL_LOG.NONE.equals(nivelObj))
				{
					res=false;
				}
			}
			catch (Exception ex) // En principio s�lo deber�a ocurrir porque hay un error de tiempo de ejecuci�n. En ese caso, devolvemos true para que imprima todo se pase el 
								// par�metro que se pase, inclu�do error.
			{
				res=true;
			}
			return res;
		}
		/* (non-Javadoc)
		 * @see es.tributasenasturias.services.ancert.enviodiligencias.log.ILogWriter#doLog(java.lang.String, es.tributasenasturias.services.ancert.enviodiligencias.log.NIVEL_LOG)
		 */
		public synchronized void doLog(String message, NIVEL_LOG level)
		{
			OutputStreamWriter fichero=null;
	        PrintWriter pw=null;

	        try
	        {
	        	String completeMsg="";
	        	if (formateador!=null)
	        	{
	        		completeMsg = formateador.formatea(message, level);
	        	}
	        	//Esto creo que es innecesario, podemos suponer que nadie borra el archivo en mitad de una ejecuci�n.
	        	createDirectories(this.ficheroLog);
	        	
	            if (charsetName !=null)
	            {
	                fichero = new OutputStreamWriter(new FileOutputStream(this.ficheroLog,true),charsetName);
	            }
	            else
	            {
	            	fichero = new OutputStreamWriter(new FileOutputStream(this.ficheroLog,true));
	            }
	            if (fichero!=null)
	            {
	            	pw = new PrintWriter(fichero);
	            
	            	pw.println(completeMsg);
	            }
	            
	        }
	        catch (IOException e)
	        {
	            System.out.println("Log:Error escribiendo log :'"+message+"' -> "+e.getMessage());
	            e.printStackTrace();
	        }
	        catch (LogException e)
	        {
	        	System.out.println("Log:Error escribiendo log :'"+message+"' -> "+e.getMessage());
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
            		System.out.println ("Log:Error cerrando flujo de impresi�n para el log : " + e.getMessage());
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
                    System.out.println("Log:Error cerrando fichero de log-> "+e.getMessage());
                    e.printStackTrace();
                }
	        }
		}
		
		/* (non-Javadoc)
		 * @see es.tributasenasturias.services.ancert.enviodiligencias.log.ILogWriter#error(java.lang.String)
		 */
		public void error(String msg) {
			if (esImprimible(NIVEL_LOG.ERROR))
			{doLog(msg,NIVEL_LOG.ERROR);}
		}
		/* (non-Javadoc)
		 * @see es.tributasenasturias.services.ancert.enviodiligencias.log.ILogWriter#debug(java.lang.String)
		 */
		public void debug(String msg) {
			if (esImprimible(NIVEL_LOG.DEBUG))
			{doLog(msg,NIVEL_LOG.DEBUG);}
		}
		/* (non-Javadoc)
		 * @see es.tributasenasturias.services.ancert.enviodiligencias.log.ILogWriter#info(java.lang.String)
		 */
		public void info(String msg) {
			if (esImprimible(NIVEL_LOG.INFO))
			{doLog(msg,NIVEL_LOG.INFO);}

		}
		/* (non-Javadoc)
		 * @see es.tributasenasturias.services.ancert.enviodiligencias.log.ILogWriter#warning(java.lang.String)
		 */
		public void warning(String msg) {
			if (esImprimible(NIVEL_LOG.WARNING))
			{doLog(msg,NIVEL_LOG.WARNING);}
		}
		/* (non-Javadoc)
		 * @see es.tributasenasturias.services.ancert.enviodiligencias.log.ILogWriter#trace(java.lang.String)
		 */
		public final void trace(String message)
		{	
			if (esImprimible(NIVEL_LOG.INFO))
			{
				doLog(message,NIVEL_LOG.TRACE);
			
			}
		}
		/* (non-Javadoc)
		 * @see es.tributasenasturias.services.ancert.enviodiligencias.log.ILogWriter#trace(java.lang.StackTraceElement[])
		 */
		public final void trace(StackTraceElement[] stackTraceElements)
		{
			 if (stackTraceElements == null)
			 {
		            return;
			 }
			 if (esImprimible(NIVEL_LOG.INFO))
			 {
		        for (int i = 0; i < stackTraceElements.length; i++)
		        {
		            doLog("StackTrace -> " + stackTraceElements[i].getFileName() + " :: " + stackTraceElements[i].getClassName() + " :: " + stackTraceElements[i].getFileName() + " :: " + stackTraceElements[i].getMethodName() + " :: " + stackTraceElements[i].getLineNumber(),NIVEL_LOG.TRACE);
		        }
			 }
		}
		/**
		 * Establece nivel de log
		 */
		@Override
		public void setNivelLog(NIVEL_LOG nivel) {
			this.nivelObj=nivel;
		}
		/**
		 * Recupera un manejador de archivo de log.
		 * @return
		 */
		protected synchronized File getFicheroLog() {
			return ficheroLog;
		}
		/**
		 * Establece el fichero de log
		 * @param ficheroLog Manejador de fichero.
		 */
		protected synchronized void setFicheroLog(File ficheroLog) {
			this.ficheroLog = ficheroLog;
		}
		/**
		 * Establece el formateador que se usa para construir el mensaje de salida.
		 */
		public synchronized void setFormateador(IFormateadorMensaje formateador) {
				this.formateador = formateador;
		}
		/**
		 * Recupera el formateador que se usa para establecer formato de mensajes.
		 * @return IFormateadorMensaje
		 */
		@Override
		public synchronized IFormateadorMensaje getFormateador() {
			return formateador;
		}

		/**
		 * Recupera el nombre de juego de caracteres utilizado en este objeto, o null si se usa el juego por defecto.
		 * @return String que contiene el nombre del juego de caracteres.
		 */
		public synchronized String getCharsetName() {
			return charsetName;
		}
		/**
		 * Establece el juego de caracteres que se utilizar� para escribir las l�neas de log.
		 * @param charsetName String con el nombre del juego de caracteres.
		 */
		public synchronized void setCharsetName(String charsetName) {
			this.charsetName = charsetName;
		}
}
