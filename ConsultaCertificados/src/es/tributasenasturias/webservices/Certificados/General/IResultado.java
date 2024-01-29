package es.tributasenasturias.webservices.Certificados.General;
/**
 * @author CarlosRuben
 * Interfaz que implementarán las clases que se puedan utilizar como resultado. 
 */
public interface IResultado {
	/**Deben implementar este método para poder devolver una cadena identificando el resultado.
	 * 
	 * @return - cadena que identifica el resultado de la validación
	 */
	public String toString();
}
