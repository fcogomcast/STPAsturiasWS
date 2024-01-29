/**
 * 
 */
package es.tributasenasturias.servicios.asturcon.consultaDeuda.context;

/**
 * @author crubencvs
 *
 */
public class ContextException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6764586401974018393L;
	public ContextException(String error, Throwable original){
		super(ContextException.class.getName()+"::"+error,original);
	}
	public ContextException (String error)
	{
		super(ContextException.class.getName()+"::"+error);
	}
	

}
