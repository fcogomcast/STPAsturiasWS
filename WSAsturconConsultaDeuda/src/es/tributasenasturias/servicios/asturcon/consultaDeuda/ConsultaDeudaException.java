/**
 * 
 */
package es.tributasenasturias.servicios.asturcon.consultaDeuda;

/**
 * @author crubencvs
 *
 */
public class ConsultaDeudaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8805772630157637595L;
	public ConsultaDeudaException(String error, Throwable original){
		super(ConsultaDeudaException.class.getName()+"::"+error,original);
	}
	public ConsultaDeudaException (String error)
	{
		super(ConsultaDeudaException.class.getName()+"::"+error);
	}
	

}
