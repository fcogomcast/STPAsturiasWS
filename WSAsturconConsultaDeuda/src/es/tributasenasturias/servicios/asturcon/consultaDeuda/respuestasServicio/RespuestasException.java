/**
 * 
 */
package es.tributasenasturias.servicios.asturcon.consultaDeuda.respuestasServicio;

/**
 * @author crubencvs
 *
 */
public class RespuestasException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8805772630157637595L;
	public RespuestasException(String error, Throwable original){
		super(RespuestasException.class.getName()+"::"+error,original);
	}
	public RespuestasException (String error)
	{
		super(RespuestasException.class.getName()+"::"+error);
	}
	

}
