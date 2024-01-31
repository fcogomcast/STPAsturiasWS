package es.tributasenasturias.validacion;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona los validadores. Los ejecuta y recupera el resultado y el error en su caso.
 * @author crubencvs
 *
 */
public class GestorValidadores <T>{

	private boolean valido;
	private String codigoMensaje;
	private List<String> mensajes= new ArrayList<String>();
	private List<IValidador<T>> validadores = new ArrayList<IValidador<T>>();
	/**
	 * @return the valido
	 */
	public final boolean isValido() {
		return valido;
	}
	/**
	 * @param valido the valido to set
	 */
	public final void setValido(boolean valido) {
		this.valido = valido;
	}
	/**
	 * @return the codigoMensaje
	 */
	public final String getCodigoMensaje() {
		return codigoMensaje;
	}
	/**
	 * @param codigoMensaje the codigoMensaje to set
	 */
	public final void setCodigoMensaje(String codigoMensaje) {
		this.codigoMensaje = codigoMensaje;
	}
	
	/**
	 * Añade un nuevo validador a la lista de validadores.
	 * @param validador Validador a añadir.
	 */
	public void addValidador (IValidador<T> validador)
	{
		if (!this.validadores.contains (validador))
		{
			validadores.add (validador);
		}
	}
	/**
	 * Borra un validador de la lista de validadores
	 * @param validador Validador a borrar
	 */
	public void borraValidador (IValidador<T> validador)
	{
		if (this.validadores.contains(validador))
		{
			validadores.remove(validador);
		}
	}
	/**
	 * Borra todos los validadores de la lista.
	 */
	public void borrarValidadores ()
	{
		for (IValidador<T> validador:validadores)
		{
			validadores.remove(validador);
		}
	}
	/**
	 * Ejecuta todos los validadores, hasta terminar con ellos o bien hasta que el primero falla.
	 * En ese caso, en este objeto se podrá recuperar el código de error con el método
	 * getCodigoMensaje
	 * @param datos Objeto que se validará
	 * @throws ValidacionException Si se produce una excepción.
	 */
	public void valida(T datos) throws ValidacionException
	{
		ResultadoIValidador res;
		valido=true;
		for (IValidador<T> validador: validadores)
		{
			res=validador.validar(datos);
			//Recogemos los mensajes generados en el validador.
			mensajes.addAll(validador.getDatosValidacion());
			if (!res.isValido())
			{
				this.valido=false;
				this.codigoMensaje=res.getCodigoResultado();
				break;
			}
		}
	}
	/**
	 * @return the mensajes
	 */
	public final List<String> getMensajes() {
		return mensajes;
	}
}
