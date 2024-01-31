package es.tributasenasturias.pasarelas.comunicacion;

import es.tributasenasturias.exceptions.PasarelaPagoException;

public interface ISelectorPasarela {

	/**
	 * Recupera el código de pasarela a utilizar..
	 * Cada implementación seleccionará la pasarela en función de determinados criterios
	 * que no se especifican aquí.
	 * @return Código de pasarela a utilizar.
	 * @throws PasarelaPagoException
	 */
	String getPasarela() throws PasarelaPagoException;
}