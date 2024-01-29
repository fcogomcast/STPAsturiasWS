package es.tributasenasturias.webservices.Certificados.General;
/**
 * @author CarlosRuben
 * Interfaz que implementar�n las clases que se puedan utilizar como resultado. 
 */
public interface IResultado {
	/**Deben implementar este m�todo para poder devolver una cadena identificando el resultado.
	 * 
	 * @return - cadena que identifica el resultado de la validaci�n
	 */
	public String toString();
}
