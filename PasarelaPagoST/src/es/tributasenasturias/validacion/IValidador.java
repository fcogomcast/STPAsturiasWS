package es.tributasenasturias.validacion;

import java.util.List;

/**
 * Interfaz que implementarán los validadores de pasarela de pago
 * @author crubencvs
 *
 * @param <ANULACION_COMENZADA> Tipo de dato que se validará
 */
public interface IValidador <T> {

	/**
	 * Realiza la  operación de validación.
	 * @param datos Datos a validar
	 * @return Resultado de validación, en un objeto {@link ResultadoIValidador}
	 * @throws ValidacionException Si se producen fallos no controlados
	 */
	ResultadoIValidador validar (T datos) throws ValidacionException;
	/**
	 * Devuelve opcionalmente datos de la validación, como mensajes de operación, etc.
	 * @return
	 */
	List<String> getDatosValidacion(); 
}
