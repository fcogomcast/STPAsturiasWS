package es.tributasenasturias.pasarelas.comunicacion;

import es.tributasenasturias.exceptions.PasarelaPagoException;

public interface ISelectorPasarela {

	/**
	 * Recupera el c�digo de pasarela a utilizar..
	 * Cada implementaci�n seleccionar� la pasarela en funci�n de determinados criterios
	 * que no se especifican aqu�.
	 * @return C�digo de pasarela a utilizar.
	 * @throws PasarelaPagoException
	 */
	String getPasarela() throws PasarelaPagoException;
}