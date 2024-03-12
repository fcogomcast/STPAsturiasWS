package es.tributasenasturias.servicios.accesocertificado.exceptions;

public class STPAException extends Exception {

	//Información para el cliente del servicio. 
	private String mensajeCliente;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8648978212710133132L;

	public STPAException() {
		super();
	}

	public STPAException(String message, Throwable cause) {
		this(message, cause, null);
	}

	public STPAException(String message) {
		this(message, null, null);
	}
	
	public STPAException (String message, Throwable cause, String mensajeCliente)
	{
		super(message,cause);
		this.mensajeCliente= mensajeCliente;
	}
	
	public STPAException (String message, String mensajeCliente)
	{
		super(message);
		this.mensajeCliente= mensajeCliente;
	}
	
	public String getMensajeCliente()
	{
		return mensajeCliente;
	}

}
