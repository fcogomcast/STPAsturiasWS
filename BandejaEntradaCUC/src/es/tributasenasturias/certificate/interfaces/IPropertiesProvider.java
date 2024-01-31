/**
 * 
 */
package es.tributasenasturias.certificate.interfaces;

/**
 * @author CarlosRuben
 * Todos los proveedores de propiedades para este servicio habr�n de implementar esta interfaz.
 */
public interface IPropertiesProvider {
	/** Carga o recarga las preferencias que se utilizar�n en el objeto que implemente este interfaz
	 * 
	 */
	void refreshPreferences() throws Exception;
	/** Recupera el valor de una preferencia.
	 * 
	 * @param prefName
	 * @return
	 */
}
