package es.tributasenasturias.tarjetas.core.db;

public class PLPasarelaRepositoryException extends Exception{

	private String codigo;
	private String mensaje;

	/**
	 * 
	 */
	private static final long serialVersionUID = -9197298084362279984L;

	public PLPasarelaRepositoryException (String codigo, String mensaje){
		this.codigo= codigo;
		this.mensaje= mensaje;
	}
	/**
	 * @param message
	 * @param cause
	 */
	public PLPasarelaRepositoryException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public PLPasarelaRepositoryException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public final String getCodigo() {
		return codigo;
	}

	public final void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public final String getMensaje() {
		return mensaje;
	}

	public final void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	
}
