/**
 * 
 */
package es.tributasenasturias.exceptions;

/**
 * @author crubencvs
 *
 */
public class PasarelaPagoException extends Exception {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3195100100841534432L;
	private String error;
	private Throwable original;
	
	public PasarelaPagoException(String error, Throwable throwable){
		super(error, throwable);
		this.error = error;
		original = throwable;
	}
	public PasarelaPagoException (String error)
	{
		super(error);
		this.error = error;
		original=null;
	}
	
	public Throwable getCause(){
		return original;
	}
	
	public String getMessage(){
		StringBuffer strBuffer = new StringBuffer();
		if(super.getMessage()!=null){
			strBuffer.append(super.getMessage());
		}
		if(original !=null){
			strBuffer.append("[");
			strBuffer.append(original.getMessage());
			strBuffer.append("]");
		}
		return strBuffer.toString();
	}
	
	public String getError(){
		return error;
	}
}
