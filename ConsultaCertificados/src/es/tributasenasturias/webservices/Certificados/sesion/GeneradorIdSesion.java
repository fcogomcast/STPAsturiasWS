package es.tributasenasturias.webservices.Certificados.sesion;



/**
 * Genera id de sesi�n �nico.
 * 
 * @author crubencvs
 *
 */
public class GeneradorIdSesion {
	private GeneradorIdSesion(){};
	/**
	 * Genera un id de sesi�n �nico.
	 * @return
	 */
	public static String generaIdSesion()
	{
		return new UUIDGenerator().generateHexUID(); // UID para esta petici�n. Se a�adir� al log.
	}
}
