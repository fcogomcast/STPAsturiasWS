package es.tributasenasturias.validacion;

import java.util.List;

/**
 * Interfaz que implementar�n los validadores de pasarela de pago
 * @author crubencvs
 *
 * @param <ANULACION_COMENZADA> Tipo de dato que se validar�
 */
public interface IValidador <T> {

	/**
	 * Realiza la  operaci�n de validaci�n.
	 * @param datos Datos a validar
	 * @return Resultado de validaci�n, en un objeto {@link ResultadoIValidador}
	 * @throws ValidacionException Si se producen fallos no controlados
	 */
	ResultadoIValidador validar (T datos) throws ValidacionException;
	/**
	 * Devuelve opcionalmente datos de la validaci�n, como mensajes de operaci�n, etc.
	 * @return
	 */
	List<String> getDatosValidacion(); 
}
