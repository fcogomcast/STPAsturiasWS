package es.stpa.servicios.clave.log;

public class LogException extends Exception{



	/**
	 * 
	 */
	private static final long serialVersionUID = -7159541913919592049L;
	public LogException(String error, Throwable original){
		super(LogException.class.getName()+"::"+error,original);
	}
	public LogException (String error)
	{
		super(LogException.class.getName()+"::"+error);
	}
}
