/**
 * 
 */
package es.tributasenasturias.validacion;

/**
 * @author CarlosRuben
 * Interfaz que implementarán los objetos de validación.
 * @param <T>
 */
public interface IValidator<T> {
	
	/**Método que devuelve un objeto indicando si el objeto a validar es correcto o no.
	 * 
	 * @param objeto - Objeto a validar
	 * @return - true si el objeto es válido, false si no.
	 */

	public  boolean isValid(T objeto);
	/**Devuelve un objeto que representa la información sobre el resultado o resultados de la validación.
	 * 
	 * @return - Resultados de validación
	 */
	public IResultadoValidacion getResultado(); 
}
